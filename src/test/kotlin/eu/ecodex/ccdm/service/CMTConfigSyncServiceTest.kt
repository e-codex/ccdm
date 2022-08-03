package eu.ecodex.ccdm.service

import eu.ecodex.ccdm.dao.CMTConfigurationDao
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@ActiveProfiles("dev")
 class CMTConfigSyncServiceTest {

    @Autowired
    lateinit var service : CMTConfigSyncService

    @Autowired
    lateinit var cmtConfigDao: CMTConfigurationDao

    @Test
    fun webClientTest() {
        service.getToken()
    }

    @Test
    fun filterTest() {
    }

    /*@Test
    fun downloadPModeListTest() {
        service.downloadPModeList()
    }*/

    @Test
    @Sql("classpath:deleteDB.sql")
    fun synchronisePModeListTest() {
        val params = ParticipationParams(
                partyId = "AT",
                partyIdType = "asdf",
                environment = "home",
                project = "test project"
        )
        service.synchroniseCMTConfig(params)
        println("Configs counted: ${cmtConfigDao.count()}")
    }

    @Test
    fun downloadPartyListTest () {
        service.downloadPartyList()
    }

    @Test
    fun synchronisePartyWithDb () {
        service.syncPartiesToDB();
    }

}