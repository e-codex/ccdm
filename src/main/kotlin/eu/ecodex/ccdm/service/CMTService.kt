package eu.ecodex.ccdm.service

import eu.ecodex.ccdm.entity.CMTConfiguration

interface CMTService {

    fun fetchConfigs (offset: Int, limit: Int): List<CMTConfiguration>
}