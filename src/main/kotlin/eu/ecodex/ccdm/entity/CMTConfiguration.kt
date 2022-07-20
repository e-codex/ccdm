package eu.ecodex.ccdm.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "cmt_config")
/*
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"environment", "project", "party", "version"})
})

 */
data class CMTConfiguration (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Int?=null,

        @Column(name = "environment")
        val environment: String,

        @Column(name = "project")
        val project: String,

        @Column(name = "party")
        val party: String,

        @Column(name = "version")
        val version: String,

        @Column(name = "download_date",  )
        val downloadDate: LocalDateTime,

        @Column(name = "publish_date")
        val publishDate: String,

        @Column(name = "zip")

        val zip: String
        ) {

}
