package eu.ecodex.ccdm.deploymentorder.driver

import eu.ecodex.ccdm.deploymentorder.service.DOExecutorServiceConfigurationProperties
import eu.ecodex.ccdm.entity.CMTConfiguration
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [DomibusGatewayDeploymentDriver::class])
internal class DomibusGatewayDeploymentDriverTest(private val gwDriver : DomibusGatewayDeploymentDriver) {


    val gwComponent = DOExecutorServiceConfigurationProperties.GatewayComponent()



    init {
        gwComponent.driverName = DomibusGatewayDeploymentDriver::class.qualifiedName!!
        gwComponent.username = "test"
        gwComponent.password = "test"
        gwComponent.url = "http://localhost:8080"
    }

    @Test
    fun runDeployment() {

//        CMTConfiguration cmtConfiguration = CMTConfiguration()
//
//        gwDriver.runDeployment(gwComponent, )
    }

    @Test
    fun canDeployComponent() {



    }


}