package eu.ecodex.ccdm

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import eu.ecodex.ccdm.service.CMTConfigSyncServiceConfigurationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableConfigurationProperties(CMTConfigSyncServiceConfigurationProperties::class)
@EnableAsync
@Push
class CcdmApplication: AppShellConfigurator

fun main(args: Array<String>) {
	runApplication<CcdmApplication>(*args)
}
