package eu.ecodex.ccdm.service.deploymentorderexecutor

import eu.ecodex.ccdm.entity.CMTConfiguration

interface ComponentDriver {

    /**
     * run deployment for a specific component
     */
    fun runDeployment(config: CMTConfiguration, component: DOExecutorServiceConfigurationProperties.Component)
}