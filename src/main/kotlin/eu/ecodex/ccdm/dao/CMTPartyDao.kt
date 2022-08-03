package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTParty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CMTPartyDao : JpaRepository<CMTParty, Int> {

}