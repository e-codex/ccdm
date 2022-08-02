package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTProject
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class CMTProjectDaoTests {

    @Autowired
    lateinit var projectDao: CMTProjectDao

    @Test
    @Sql("/deleteDB.sql")
    fun emptyTest() {
        assertThat(projectDao.count()).isEqualTo(0)
    }

    @Test
    @Sql("/deleteDB.sql")
    fun testSave() {
        val newEntry = CMTProject (
                id = 6,
                name = "Test Project",
                description = "Something to Test"
                )

        projectDao.save(newEntry)
        assertThat(newEntry.id).isNotNull()
    }

    @Test
    @Sql("/testdata.sql")
    fun testFind() {
        assertEquals(3, projectDao.findAll().size)
    }

    @Test
    @Sql("/deleteDB.sql")
    @Sql("/testdata.sql")
    fun testDeleteById() {
        projectDao.deleteById(3)
        assertEquals(2, projectDao.findAll().size)
    }

}