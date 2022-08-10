package eu.ecodex.ccdm.service

import eu.ecodex.ccdm.dao.DeploymentOrderDao
import eu.ecodex.ccdm.entity.DeploymentOrder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeploymentOrderService(
    private val deploymentDao: DeploymentOrderDao
    ) {
    @Transactional
    fun createNewDeploymentOrder(newDeploymentOrder: DeploymentOrder) {
        deploymentDao.save(newDeploymentOrder)
    }

    //TODO: trigger deployment
}

