package eu.ecodex.ccdm.service

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "ccdm.pmode-sync")
class CMTConfigSyncServiceConfigurationProperties(
        //val cmtUrl: String, var username : String
) {
    var keycloakUrl: String = ""

    var cmtUrl: String = ""

    var username: String = ""

    var pw: String = ""

    var clientId: String = ""

    /*
    Seems like Properties cannot be found? Don't really know whazzup..

    Changes:
    - Created necessary variables in this class
    - Exported important info into properties file
    - Added @EnableConfigurationProperties to CcdmApplikation.kt to specify where props come from

    On Test run:
***************************
APPLICATION FAILED TO START
***************************
Description:

Parameter 0 of constructor in eu.ecodex.ccdm.service.CMTConfigSyncService required a single bean, but 2 were found:
	- CMTConfigSyncServiceConfigurationProperties: defined in file [C:\Entwicklung\repos\ccdm\build\classes\kotlin\main\eu\ecodex\ccdm\service\CMTConfigSyncServiceConfigurationProperties.class]
	- ccdm.pmode-sync-eu.ecodex.ccdm.service.CMTConfigSyncServiceConfigurationProperties: defined in null

Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed


     */
}