package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTConfiguration
import org.springframework.data.domain.Pageable
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

    @Query("select distinct c.environment from CMTConfiguration c")
    fun findAllEnvironments(): List<String>

    @Query("select distinct c.project from CMTConfiguration c")
    fun findAllProjects(): List<String>

    @Query("select c from CMTConfiguration c where (c.environment = :environment or :environment = '*') and (c.project = :project or :project = '*') ")
    fun findAllByEnvironmentAndProject(environment: String, project: String, p: Pageable): List<CMTConfiguration>

}