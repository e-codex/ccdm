package eu.ecodex.ccdm.deploymentorder.service

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(DOExecutorServiceConfigurationProperties::class)
class DOEConfiguration {

}