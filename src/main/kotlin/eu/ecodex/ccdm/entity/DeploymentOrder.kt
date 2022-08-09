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


        /*
        DB Design abbilden
        https://vladmihalcea.com/manytoone-jpa-hibernate/
        https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
        https://www.baeldung.com/hibernate-one-to-many
        https://www.javatpoint.com/jpa-one-to-many-mapping
        https://www.javatpoint.com/jpa-many-to-one-mapping

        General explanation:
        https://vladmihalcea.com/a-beginners-guide-to-jpa-hibernate-entity-state-transitions/
        https://vladmihalcea.com/14-high-performance-java-persistence-tips/

        @ManyToOne
        allows you to map the Foreign Key column in the child entity mapping so that the child has an entity object reference
        to its parent entity. This is the most natural way of mapping a database one-to-many database association
         */
) {
}