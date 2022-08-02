package eu.ecodex.ccdm

import eu.ecodex.ccdm.service.CMTConfigSyncServiceConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(CMTConfigSyncServiceConfigurationProperties::class)
class CcdmApplication

fun main(args: Array<String>) {
	runApplication<CcdmApplication>(*args)
}
