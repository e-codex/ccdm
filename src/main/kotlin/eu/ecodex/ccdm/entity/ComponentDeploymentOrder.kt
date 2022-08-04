package eu.ecodex.ccdm.entity

import javax.persistence.*

@Entity
@Table(name = "component_deployment_order")
data class ComponentDeploymentOrder (


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Int?=null,

    @Column(name = "component")
    val component: String = ""

        )
{



}