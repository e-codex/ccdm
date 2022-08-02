package eu.ecodex.ccdm.ui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@PageTitle("CMT Configuration Download Manager")
class MainUI: AppLayout() {

    //private val tabToViewMap = mutableMapOf<Tab, Class<out Component>>()
    private val dashboardTab = Tab(VaadinIcon.DASHBOARD.create(), Span("Dashboard"))
    private val updateTab = Tab(VaadinIcon.REFRESH.create(), Span("Update"))

        init {
            val toggle = DrawerToggle()
            val title = H1("CMT Configuration Download Manager")
            title.style.set("font-size", "var(--lumo-font-size-l)")
            title.style.set("margin", "0")

             /*tabToViewMap[dashboardTab] = DashboardView::class.java
             tabToViewMap[updateTab] = UploadView::class.java
             val selectedTab = mapOf(dashboardTab to DashboardView::class.java, updateTab to UploadView::class.java)
             tabToViewMap.putAll(selectedTab)*/

            val tabs = Tabs(dashboardTab, updateTab)
            tabs.orientation = Tabs.Orientation.VERTICAL

            tabs.addSelectedChangeListener(this::selectedTabChanged)

            this.addToDrawer(tabs)
            this.addToNavbar(toggle, title)
        }

    private fun selectedTabChanged(event: Tabs.SelectedChangeEvent) {

        //UI.getCurrent().navigate(tabToViewMap[event.selectedTab])

        when (event.selectedTab) {
            dashboardTab -> UI.getCurrent().navigate(DashboardView::class.java)
            updateTab -> UI.getCurrent().navigate(UploadView::class.java)
        }
    }
}

/*
Useful Links:
https://www.vaadinonkotlin.eu//tutorial/
https://vaadin.com/docs/latest/tutorial/overview
https://vaadin.com/docs/latest/routing/layout
https://vaadin.com/docs/latest/configuration/live-reload

Helpful YT vid:
https://youtu.be/bxy2JgqqKDU

Kotlin vs. Java:
https://www.kotlinvsjava.com/

Look up data provider:
https://vaadin.com/docs/v14/flow/binding-data/tutorial-flow-data-provider

Spring WebClient:
https://www.baeldung.com/spring-5-webclient

Possibly accessing token:
https://stackoverflow.com/questions/57213636/how-can-i-get-access-token-from-json-data



To check:
localhost:8080
in browser
 */
