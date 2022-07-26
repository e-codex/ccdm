package eu.ecodex.ccdm.ui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route(value = "", layout = MainUI::class)
class DashboardView: VerticalLayout() {

    init {
        val title = H1("Dashboard")
        title.style.set("font-size", "var(--lumo-font-size-l)")
        title.style.set("margin", "0")

        add(title)
    }
}