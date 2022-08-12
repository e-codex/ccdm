package eu.ecodex.ccdm.service

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
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import java.util.*

@Service
class CMTConfigSyncService(
    config: CMTConfigSyncServiceConfigurationProperties,
    private val cmtConfigDao: CMTConfigurationDao,
    private val partyDao: CMTPartyDao
) {

    private final val webClientFilter = WebClientFilter(config)
    val logger: Logger = LoggerFactory.getLogger(CMTConfigSyncService::class.java)

    private val cmtClient = WebClient.builder()
            .baseUrl(config.cmtUrl)
            //.filter(webClientFilter.filterFunction)
            .filter(ExchangeFilterFunction.ofRequestProcessor(webClientFilter))
            .codecs { configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 10000) }
            .build()

    fun downloadPartyList(): MutableList<CMTParty> {
        val downloadedPartyList = cmtClient.get().uri { uriBuilder ->
            uriBuilder.path("/party/list").build()
        }
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
                    .build()
        }
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
                .retrieve()
                .bodyToFlux(PModeDTO::class.java)
                .collectList()
                .block()

        val temp = mutableListOf<PModeDTO>()
        if (downloadedPModeDTOList != null) {
            temp.addAll(downloadedPModeDTOList)
        }

        return temp
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

}