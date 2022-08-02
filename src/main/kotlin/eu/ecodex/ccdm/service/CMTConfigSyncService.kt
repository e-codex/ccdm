package eu.ecodex.ccdm.service

import elemental.json.Json
import eu.ecodex.ccdm.dao.CMTConfigurationDao
import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.entity.CMTParty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import java.util.*

@Service
class CMTConfigSyncService (private val config: CMTConfigSyncServiceConfigurationProperties, private val cmtConfigDao: CMTConfigurationDao) {

    val logger: Logger = LoggerFactory.getLogger(CMTConfigSyncService::class.java)

    private val keycloakClient = WebClient.builder()
            .baseUrl(config.keycloakUrl)
            .build()

    private val cmtClient = WebClient.builder()
            .baseUrl(config.cmtUrl)
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

        logger.trace("Retrieved authentication token: $parsedToken")

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

        logger.info("Retrieved parties: $temp ; Number of parties: ${temp.count()}")

        return temp
    }

    fun getParties(parties: List<CMTParty>): CMTParty {

        var singleParty = CMTParty()
        parties.forEach  { party ->
            singleParty = party
            return singleParty
        }
        return singleParty
    }

    fun downloadParticipantListForParty(party: CMTParty) {

        // : MutableList<ParticipationParams>
        val downloadedParticipants = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/participation/list/${party.partyId}/${party.partyIdTypeValue}")
                    //.queryParam("partyId", "AT")
                    //.queryParam("partyIdType", "urn:oasis:names:tc:ebcore:partyid-type:ecodex")
                    .build()
        }
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .bodyToFlux(String::class.java)
                .collectList()
                .block()

        /*val participationParams = ParticipationParams (
                partyId = party.partyId,
                partyIdType = party.partyIdTypeValue,
                listOf(environment = downloadedParticipants.get().environment,
                project = downloadedParticipants.get().project)
                )
        */

        /*val temp = mutableListOf<ParticipationParams>()
        if (downloadedParticipants != null) {
            temp.addAll(downloadedParticipants)
        }*/

        logger.info("Retrieved participants: $downloadedParticipants ; Number of participants: ${downloadedParticipants?.count()}}")

        //return temp
    }

    private fun downloadPModeList(params: ParticipationParams): MutableList<PMode> {

        val downloadedPModeList = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/pmode/list").queryParam("partyId", params.partyId)
                    .queryParam("partyIdType", params.partyIdType)
                    .queryParam("environment", params.environment)
                    .queryParam("project", params.project)
                    .build() }
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .bodyToFlux(PMode::class.java)
                .collectList()
                .block()

        val temp = mutableListOf<PMode>()
        if (downloadedPModeList != null) {
            temp.addAll(downloadedPModeList)
        }

        return temp

        // Date Parsing anschauen
        //logger.info(stringToReturn.block().toString())
    }

    private fun PMode.toCMTConfiguration(zip: ByteArray) = CMTConfiguration(
            cmtName = name,
            version = version,
            environment = "",
            project = "",
            party = "",
            goLiveDate = LocalDateTime.now(),
            downloadDate = LocalDateTime.now(),
            publishDate = LocalDateTime.now(),
            zip = zip
            )

    private fun downloadZip(pMode: PMode, params: ParticipationParams): ByteArray {
        val downloadedZip = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/pmode/download").queryParam("partyId", params.partyId)
                    .queryParam("partyIdType", params.partyIdType)
                    .queryParam("environment", params.environment)
                    .queryParam("project", params.project)
                    .queryParam("version", pMode.version)
                    .build() }
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .bodyToMono(ZipData::class.java)
                .block()

        var temp = byteArrayOf()
        if (downloadedZip != null) {
            temp = Base64.getDecoder().decode(downloadedZip.pModeZipBase64)
        }

        return temp
    }

    @Transactional
    fun synchroniseCMTConfig(params: ParticipationParams) {
        downloadPModeList(params).forEach { pMode ->
            if (cmtConfigDao.findByCmtName(pMode.name).isEmpty()) {
                val pModeToSave = pMode.toCMTConfiguration(downloadZip(pMode, params))
                cmtConfigDao.save(pModeToSave)
            }
        }
    }

    // Button mit Funktion hinterlegen

    // https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-kotlin.html

    // https://www.predic8.de/bearer-token-autorisierung-api-security.htm#:~:text=Der%20Begriff%20Bearer%20bedeutet%20auf,eine%20bestimmte%20Identit%C3%A4t%20gebunden%20ist.

    /*private fun filterFunction(): ExchangeFilterFunction { (request: ClientRequest, next: ExchangeFunction) ->
        if(parsedToken.isEmpty()) {
            webClientTryout()
        }
        next.exchange(request)
    }*/

    /*var filterFunction = ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
        if(parsedToken.isEmpty()) {
            webClientTryout()
        }
        next.exchange(request)
    }*/

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