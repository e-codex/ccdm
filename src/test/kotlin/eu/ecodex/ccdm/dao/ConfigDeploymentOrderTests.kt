package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.entity.DeploymentOrder
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@SpringBootTest
class ConfigDeploymentOrderTests {

    @Autowired
    lateinit var deployDao: DeploymentOrderDao

    @Test
    @Sql("/deleteDB.sql")
    fun testForEmpty() {
        println(deployDao.count())
        assertThat(deployDao.count()).isEqualTo(0)
    }

    @Test
    @Sql("/deleteDB.sql")
    fun testCreate() {
        println(deployDao.findAll())
        val newConfig = CMTConfiguration(
                configId = 5,
                cmtName = "oiconimus",
                environment = "Space",
                project = "Xilon",
                partyId = "CMTParty",
                version = "255",
                downloadDate = LocalDateTime.now(),
                publishDate = LocalDateTime.of(1992, 4, 15, 0, 0),
                zip = "asdf".toByteArray(),
                goLiveDate = LocalDateTime.now())
        val newOrder = DeploymentOrder(
                id = 5,
                deploymentDate = LocalDateTime.now(),
                principal = "Neil Armstrong",
                config = newConfig
        )
        newConfig.configDeployments.add(newOrder)
        deployDao.save((newOrder))
        //println(deployDao.findAll())
        assertThat(newOrder.id).isNotNull()
    }

    @Test
    @Sql("/deleteDB.sql")
    @Sql("/testdata.sql")
    fun testRead() {
        println(deployDao.findAll())
        assertThat(deployDao.count()).isGreaterThan(1)
    }

    @Test
    @Sql("/deleteDB.sql")
    @Sql("/testdata.sql")
    fun testDelete() {
        deployDao.deleteById(3)
        println(deployDao.findAll())
        assertEquals(2, deployDao.findAll().size)
    }

    @Test
    @Sql("/testdata.sql")
    fun testFindDeployLaterThan() {
        println(deployDao.findByDeploymentDateAfter(LocalDateTime.of(1990, 1, 1, 0, 0)))
        assertEquals(2, deployDao.findByDeploymentDateAfter(LocalDateTime.of(1990, 1, 1, 0, 0)).size)
    }
}