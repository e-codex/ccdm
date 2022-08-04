package eu.ecodex.ccdm.service.deploymentorderexecutor

import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "ccdm.deploy")
class DOExecutorServiceConfigurationProperties {

    var projects: MutableList<Project> = ArrayList()


    class Project {
        var cmtName: String = ""
        var nationalName: String = ""

        var gwComponent: GatewayComponent = GatewayComponent()

    }

    open class Component {
        var driverName: String = "default"
    }

    class GatewayComponent : Component() {
        @NotBlank
        var username: String = ""
        @NotBlank
        var password: String = ""
        @NotBlank
        var url: String = ""
    }


}