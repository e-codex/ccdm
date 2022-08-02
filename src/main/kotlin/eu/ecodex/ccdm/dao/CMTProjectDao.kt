package eu.ecodex.ccdm.dao

import eu.ecodex.ccdm.entity.CMTProject
import org.springframework.data.jpa.repository.JpaRepository

interface CMTProjectDao : JpaRepository<CMTProject, Int?> {


}