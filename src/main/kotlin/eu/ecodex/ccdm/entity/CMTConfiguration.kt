package eu.ecodex.ccdm.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table( name = "cmt_config" )
/*
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"environment", "project", "party", "version"})
})

 */
data class CMTConfiguration (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "config_id")
        val configId: Int?=null,

        @OneToMany(cascade = arrayOf(CascadeType.ALL),
                mappedBy = "config",
                orphanRemoval = true)
        val configDeployments: MutableList<ConfigDeploymentOrder> = ArrayList (),
        //https://stackoverflow.com/questions/50815189/how-to-initialize-an-empty-arraylist-in-kotlin
        //Continue with mappedBy error:
        // https://stackoverflow.com/questions/2584521/what-is-the-inverse-side-of-the-association-in-a-bidirectional-jpa-onetomany-m

        @Column(name = "environment")
        val environment: String,

        @Column(name = "project")
        val project: String,

        @Column(name = "party")
        val party: String,

        @Column(name = "version")
        val version: String,

        @Column(name = "download_date")
        val downloadDate: LocalDateTime,

        @Column(name = "publish_date")
        val publishDate: LocalDateTime,

        @Column(name = "zip")

        val zip: String
        ) {

}
