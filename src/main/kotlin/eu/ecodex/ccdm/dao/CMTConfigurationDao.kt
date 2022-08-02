package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CMTConfigurationDao : JpaRepository<CMTConfiguration, Int?> {

    /*fun findByPublishDateBefore(day: LocalDateTime): List <CMTConfiguration>
*/
    @Query("select c from CMTConfiguration c where c.publishDate < ?1")
    fun sqlFindByPublishDateBefore(day: LocalDateTime): List <CMTConfiguration>

    fun findByCmtName(cmtName: String): List<CMTConfiguration>

}