package eu.ecodex.ccdm.entity

import javax.persistence.*

@Entity
@Table(name = "cmt_party")
data class CMTParty(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Int? = null,

        @Column(name = "party_id")
        val partyId: String = "",

        @Column(name = "party_id_type_key")
        val partyIdTypeKey: String = "",

        @Column(name = "party_id_type_value")
        val partyIdTypeValue: String = ""
) {

}
