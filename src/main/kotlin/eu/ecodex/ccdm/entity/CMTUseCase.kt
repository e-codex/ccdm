package eu.ecodex.ccdm.entity

import javax.persistence.*

@Entity
@Table(name = "cmt_use_case")
data class CMTUseCase(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Int? = null,

        @Column(name = "name")
        val name: String,

        @Column(name = "type")
        val type: String
) {

}
