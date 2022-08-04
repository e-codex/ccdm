package eu.ecodex.ccdm.service.deploymentorderexecutor

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(DOExecutorServiceConfigurationProperties::class)
class DOEConfiguration {

}