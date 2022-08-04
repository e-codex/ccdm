package eu.ecodex.ccdm.service.deploymentorderexecutor

import eu.ecodex.ccdm.entity.ComponentDeploymentOrder
import org.springframework.stereotype.Service

/**
 * Takes all not finished deployment orders and executes them
 */
@Service
class DeploymentOrderExecutorService {


    fun runDeployments() {
        //TODO: find pending not fullfilled deployment orders

        //run deployment order for each component
    }

    fun runDeploymentOrderForComponent(componentDeploymentOrder: ComponentDeploymentOrder) {
        //TODO: lookup driver for this deployment order

        //run deployment order

        //save result
    }

}