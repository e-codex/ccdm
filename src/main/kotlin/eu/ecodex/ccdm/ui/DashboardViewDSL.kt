package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.h1
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.router.Route

@Route(value = "/dsl", layout = MainUIDSL::class)
class DashboardViewDSL: KComposite() {

    private val root = ui {
        verticalLayout {
            h1("Dashboard") {
                style.set("font-size", "var(--lumo-font-size-l)")
                style.set("margin", "0")
            }
        }
    }

}