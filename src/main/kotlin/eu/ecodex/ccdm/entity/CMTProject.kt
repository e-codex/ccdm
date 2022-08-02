package eu.ecodex.ccdm.entity

import javax.persistence.*

@Entity
@Table(name = "cmt_project")
data class CMTProject(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Int? = null,

        @Column(name = "name")
        val name: String,

        @Column(name = "description")
        val description: String,

        @OneToMany(cascade = arrayOf(CascadeType.ALL),
                orphanRemoval = true)
        @JoinColumn(name = "fk_project_id", referencedColumnName = "id", nullable = false)
        val useCase: MutableList<CMTUseCase> = ArrayList()

) {

}
