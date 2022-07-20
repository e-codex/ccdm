package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTConfiguration
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Lob

@SpringBootTest
class CMTConfigurationDaoTests {

    @Autowired
    lateinit var dao: CMTConfigurationDao

    @Test
    fun exampleTest() {
        dao.count()
    }

    @Test
    fun testSave() {
        val newDao = CMTConfiguration(
                environment = "Oberunterdumpfing",
                project = "X Files",
                party = "SS Sinking Party",
                version = "13",
                downloadDate = LocalDateTime.now(),
                publishDate = "15.05.1822",
                zip = "asdf")
        dao.save(newDao)

        assertThat(newDao.id).isNotNull();



    }

    @Test
    @Sql("/testdata.sql")
    fun testFind() {
        assertEquals(1, dao.findAll().size)
    }

    @Test
    @Sql("/testdata.sql")
    fun testDelete() {

        //dao.delete()
        assertEquals(1, dao.findAll().size)

    }

    @Test
    @Sql("/testdata.sql")
    fun testFindPriorDate() {

        assertEquals(1, dao.findAll().size)
        
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