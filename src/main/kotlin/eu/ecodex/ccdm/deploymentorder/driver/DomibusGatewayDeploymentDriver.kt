package eu.ecodex.ccdm.deploymentorder.driver

import eu.ecodex.ccdm.deploymentorder.service.ComponentDriver
import eu.ecodex.ccdm.deploymentorder.service.DOExecutorServiceConfigurationProperties
import eu.ecodex.ccdm.entity.CMTConfiguration
import org.springframework.stereotype.Component

@Component
class DomibusGatewayDeploymentDriver : ComponentDriver {


    override fun runDeployment(
        config: CMTConfiguration,
        component: DOExecutorServiceConfigurationProperties.Component
    ) {
        TODO("Not yet implemented")
        //TODO: open webclient session and upload p-Modes...


    }

    override fun canDeployComponent(component: DOExecutorServiceConfigurationProperties.Component): Boolean {
        return component.driverName == DomibusGatewayDeploymentDriver::class.qualifiedName
    }
}