package eu.ecodex.ccdm.deploymentorder.service

import eu.ecodex.ccdm.entity.CMTConfiguration

interface ComponentDriver {

    /**
     * deploy a configuration on the given component
     */
    fun runDeployment(config: CMTConfiguration, component: DOExecutorServiceConfigurationProperties.Component)

    /**
     * @return: can this driver deploy a configuration on the given component
     */
    fun canDeployComponent(component: DOExecutorServiceConfigurationProperties.Component) : Boolean
}