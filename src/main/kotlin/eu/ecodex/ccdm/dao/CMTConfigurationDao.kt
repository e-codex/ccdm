package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CMTConfigurationDao : JpaRepository<CMTConfiguration, Int?> {

}