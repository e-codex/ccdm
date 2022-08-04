package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTConfiguration
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
class CMTConfigurationDaoTests {

    @Autowired
    lateinit var dao: CMTConfigurationDao

    @Test
    @Sql("/deleteDB.sql")
    fun exampleTest() {
        //dao.count()
        //println(dao.count())

        assertThat(dao.count()).isEqualTo(0)
    }

    @Test
    @Sql("/deleteDB.sql")
    fun testSave() {
        val newDao = CMTConfiguration(
                configId = 4,
                cmtName = "econimus",
                environment = "Downunder",
                project = "FuzzyLogic",
                partyId = "Confusio",
                version = "99",
                downloadDate = LocalDateTime.now(),
                publishDate = LocalDateTime.of(2020, 8, 3, 0, 0),
                zip = "asdf".toByteArray(),
                goLiveDate = LocalDateTime.now())
        dao.save(newDao)

        assertThat(newDao.configId).isNotNull()
    }

    @Test
    @Sql("/testdata.sql")
    fun testFind() {
        assertEquals(3, dao.findAll().size)
    }

    @Test
    @Sql("/deleteDB.sql")
    @Sql("/testdata.sql")
    fun testDeleteById() {
       /* println( dao.count())
        println( dao.findAll())*/
        dao.deleteById(2)
       /* println( dao.count())
        println( dao.findAll())*/
        assertEquals(2, dao.findAll().size)
    }

    @Test
    @Sql("/deleteDB.sql")
    @Sql("/testdata.sql")
    @Transactional
    fun testFindDatePrior() {

        // Automatic Custom Query
        //println(dao.findByPublishDateBefore(LocalDateTime.of(2020, 1,1, 0,0)))
        //assertEquals(2, dao.findByPublishDateBefore(LocalDateTime.of(2020, 1,1, 0,0)).size)


        // Manual Custom Query
        println(dao.sqlFindByPublishDateBefore(LocalDateTime.of(2020, 1,1, 0,0)))
        assertEquals(2, dao.sqlFindByPublishDateBefore(LocalDateTime.of(2020, 1,1, 0,0)).size)

        //query finde alle einträge vor dem datum X
        // spring-data : https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details
    }

    /*
    Useful Links:
    https://www.baeldung.com/spring-data-jpa-query
    https://www.concretepage.com/spring-5/sql-example-spring-test
    https://www.baeldung.com/spring-boot-data-sql-and-schema-sql

    https://www.baeldung.com/hibernate-lob
    https://medium.com/@wifekraissi/spring-boot-kotlin-data-access-be85d69d6657
    https://spring.io/guides/tutorials/spring-boot-kotlin/
    https://docs.spring.io/spring-framework/docs/4.1.x/spring-framework-reference/html/testing.html#testcontext-executing-sql-declaratively
     */
}