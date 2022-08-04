package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.DeploymentOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DeploymentOrderDao: JpaRepository<DeploymentOrder, Int?> {

    fun findByDeploymentDateAfter(day: LocalDateTime): List <DeploymentOrder>
}