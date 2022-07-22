package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.ConfigDeploymentOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ConfigDeploymentOrderDao: JpaRepository<ConfigDeploymentOrder, Int?> {

    fun findByDeploymentDateAfter(day: LocalDateTime): List <ConfigDeploymentOrder>
}