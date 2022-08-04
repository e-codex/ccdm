package eu.ecodex.ccdm.service

import elemental.json.Json
import eu.ecodex.ccdm.dao.CMTConfigurationDao
import eu.ecodex.ccdm.dao.CMTPartyDao
import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.entity.CMTParty
import eu.ecodex.ccdm.service.cmtclient.PModeDTO
import eu.ecodex.ccdm.service.cmtclient.ParticipationParamsDTO
import eu.ecodex.ccdm.service.cmtclient.ZipDataDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import java.util.*

@Service
class CMTConfigSyncService (
        private val config: CMTConfigSyncServiceConfigurationProperties,
        private val cmtConfigDao: CMTConfigurationDao,
        private val partyDao: CMTPartyDao
) {

    val logger: Logger = LoggerFactory.getLogger(CMTConfigSyncService::class.java)

    private val keycloakClient = WebClient.builder()
            .baseUrl(config.keycloakUrl)
            .build()

    private val cmtClient = WebClient.builder()
            .baseUrl(config.cmtUrl)
            .codecs { configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 10000) }
            .build()

    fun getToken(): String {

        val block = keycloakClient.post()
                .uri(config.keycloakUrl)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("username", config.username)
                        .with("password", config.pw)
                        .with("client_id", config.clientId)
                )
                .retrieve()
                .bodyToMono(String::class.java)

        val parsedJson = Json.parse(block.block())
        val parsedToken = parsedJson.getString("access_token")

        logger.trace("Retrieved authentication token: [{}]", parsedToken)

        return parsedToken

        // Json parsing: https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
    }

    fun downloadPartyList(): MutableList<CMTParty> {
        val downloadedPartyList = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/party/list").build()
        }
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .bodyToFlux(CMTParty::class.java)
                .collectList()
                .block()

        val temp = mutableListOf<CMTParty>()
        if (downloadedPartyList != null) {
            temp.addAll(downloadedPartyList)
        }

        logger.trace("Retrieved parties: [{}] ; Number of parties: [{}]", temp, temp.count())

        return temp
    }

    @Transactional
    fun syncPartiesToDB() {
        val parties = downloadPartyList()

        parties.forEach  { party ->

            val p = CMTParty(
                    partyId = party.partyId,
                    partyIdTypeKey = party.partyIdTypeKey,
                    partyIdTypeValue =  party.partyIdTypeValue
            )

            if(!partyDao.exists(Example.of(p))) {
                partyDao.save(p)
            }
        }
    }

    fun synchronise() {
        val partyList = downloadPartyList()
        partyList.forEach { p ->
            logger.debug("Synchronising Party [{}]", p)
            val participationList = downloadParticipantListForParty(p)
            participationList.forEach{ part ->
                logger.debug("Synchronising Participation [{}]", part)
                synchroniseCMTConfig(part)
            }
        }
    }

    fun downloadParticipantListForParty(party: CMTParty): MutableList<ParticipationParamsDTO> {

        val downloadedParticipants = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/participation/list/${party.partyId}/${party.partyIdTypeValue}")
                    //.queryParam("partyId", "AT")
                    //.queryParam("partyIdType", "urn:oasis:names:tc:ebcore:partyid-type:ecodex")
                    .build()
            // pass parameters to build()? See: https://www.baeldung.com/webflux-webclient-parameters
        }
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .bodyToFlux(ParticipationDTO::class.java)
                .map { dtoParams ->
                    val p = ParticipationParamsDTO()
                    p.environment = dtoParams.environment
                    p.project = dtoParams.project
                    p.partyId = party.partyId
                    p.partyIdType = party.partyIdTypeValue
                    p
                }
                .collectList()
                .block()

        val temp = mutableListOf<ParticipationParamsDTO>()
        if (downloadedParticipants != null) {
            temp.addAll(downloadedParticipants)
        }

        logger.trace("Retrieved participants: $downloadedParticipants ; Number of participants: ${downloadedParticipants?.count()}}")

        return temp
    }

    private fun downloadPModeList(params: ParticipationParamsDTO): MutableList<PModeDTO> {

        val downloadedPModeDTOList = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/pmode/list").queryParam("partyId", params.partyId)
                    .queryParam("partyIdType", params.partyIdType)
                    .queryParam("environment", params.environment)
                    .queryParam("project", params.project)
                    .build() }
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .bodyToFlux(PModeDTO::class.java)
                .collectList()
                .block()

        val temp = mutableListOf<PModeDTO>()
        if (downloadedPModeDTOList != null) {
            temp.addAll(downloadedPModeDTOList)
        }

        return temp

        // Date Parsing anschauen
        //logger.info(stringToReturn.block().toString())
    }

    private fun mapPModeToCMTConfiguration(
            name: String,
            version: String,
            zip: ByteArray,
            environment: String,
            project: String,
            partyId: String,
            partyIdType: String,
            pMode: PModeDTO
    ): CMTConfiguration {
        return CMTConfiguration(
                cmtName = name,
                version = version,
                environment = environment,
                project = project,
                partyId = partyId,
                partyIdType = partyIdType,
                goLiveDate = LocalDateTime.now(),
                downloadDate = LocalDateTime.now(),
                publishDate = pMode.creationDate,
                zip = zip
        )
    }

    private fun downloadZip(pMode: PModeDTO, params: ParticipationParamsDTO): ByteArray {
        val downloadedZip = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/pmode/download").queryParam("partyId", params.partyId)
                    .queryParam("partyIdType", params.partyIdType)
                    .queryParam("environment", params.environment)
                    .queryParam("project", params.project)
                    .queryParam("version", pMode.version)
                    .build() }
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .bodyToMono(ZipDataDTO::class.java)
                .block()

        var temp = byteArrayOf()
        if (downloadedZip != null) {
            temp = Base64.getDecoder().decode(downloadedZip.pModeZipBase64)
        }

        return temp
    }

    @Transactional
    fun synchroniseCMTConfig(params: ParticipationParamsDTO) {
        downloadPModeList(params).forEach { pMode ->
            if (cmtConfigDao.findByCmtName(pMode.name).isEmpty()) {
                val pModeToSave = mapPModeToCMTConfiguration(
                        zip = downloadZip(pMode, params),
                        environment = params.environment,
                        project = params.project,
                        partyId = params.partyId,
                        pMode = pMode,
                        name = pMode.name,
                        partyIdType = params.partyIdType,
                        version = pMode.version
                )
                cmtConfigDao.save(pModeToSave)
            }
        }
    }

    class ParticipationDTO {

        var environment: String = ""
        var project: String = ""
        var useCases: Array<String> = arrayOf()

    }

    // Info links:
    // https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-kotlin.html
    // https://www.predic8.de/bearer-token-autorisierung-api-security.htm#:~:text=Der%20Begriff%20Bearer%20bedeutet%20auf,eine%20bestimmte%20Identit%C3%A4t%20gebunden%20ist.

    // Useful links:
    // https://www.baeldung.com/spring-webclient-oauth2
    // https://docs.spring.io/spring-security/reference/5.6.0-RC1/reactive/integrations/webclient.html
    // https://stackoverflow.com/questions/69306969/failing-to-add-client-credentials-clientid-clientsecret-at-spring-webclient-r
    // https://stackoverflow.com/questions/59792224/how-to-post-request-with-spring-boot-web-client-for-form-data-for-content-type-a

    // Spring Boot & Kotlin Coroutines:
    // https://www.baeldung.com/kotlin/spring-boot-kotlin-coroutines

    // https://developer.okta.com/blog/2018/06/29/what-is-the-oauth2-password-grant

    // Curl Command in Git Bash:
    // curl -k --data "grant_type=password&username=user_at&password=test&client_id=cmt" https://keycloak-route-ju-eu-ejustice-eqs.apps.a2.cp.cna.at/realms/cmt-ecodex-test/protocol/openid-connect/token
}