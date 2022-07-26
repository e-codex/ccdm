//package eu.ecodex.ccdm.entity
//
//import com.github.vokorm.*
//import com.gitlab.mvysny.jdbiorm.Dao
//import java.time.LocalDateTime
//
//data class CMTConfigurationDSL (
//
//        override var id: Int? = null,
//
//        val configDeployments: MutableList<ConfigDeploymentOrder> = ArrayList (),
//
//        val environment: String? = null,
//
//        val project: String? = null,
//
//        val party: String? = null,
//
//        val version: String? = null,
//
//        val goLiveDate: LocalDateTime? = null,
//
//        val downloadDate: LocalDateTime? = null,
//
//        val publishDate: LocalDateTime? = null,
//
//        val zip: String? = null,
//
//): KEntity<Int> {
//
//    companion object: Dao<CMTConfigurationDSL, Int>(CMTConfigurationDSL::class.java)
//}