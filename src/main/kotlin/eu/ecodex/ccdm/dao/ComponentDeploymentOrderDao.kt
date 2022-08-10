package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.ComponentDeploymentOrder
import org.springframework.data.jpa.repository.JpaRepository

interface ComponentDeploymentOrderDao: JpaRepository<ComponentDeploymentOrder, Int?> {

}