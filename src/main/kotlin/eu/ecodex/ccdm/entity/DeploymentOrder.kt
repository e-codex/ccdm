package eu.ecodex.ccdm.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "deployment_order")
data class DeploymentOrder(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Int?=null,

        @ManyToOne
        @JoinColumn(name = "config_id")
        val config: CMTConfiguration,

        @Column(name = "deployment_date")
        val deploymentDate: LocalDateTime,

        @Column(name = "principal")
        val principal: String,

        @OneToMany(cascade = arrayOf(CascadeType.ALL),
                orphanRemoval = true)
        @JoinColumn(name = "fk_deployment_order", referencedColumnName = "id", nullable = false)
        val componentDeploymentOrder: MutableList<ComponentDeploymentOrder> = ArrayList()

) {
}