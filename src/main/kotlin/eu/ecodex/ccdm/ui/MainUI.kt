package eu.ecodex.ccdm.ui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLayout

@PageTitle("CMT Configuration Download Manager")
class MainUI: AppLayout() {

    private val dashboardTab = Tab(VaadinIcon.DASHBOARD.create(), Span("Dashboard"))
    private val updateTab = Tab(VaadinIcon.REFRESH.create(), Span("Update"))
    private val deploymentOrderTab = Tab(VaadinIcon.LIST_OL.create(), Span ("Deployment Orders"))

        init {
            val toggle = DrawerToggle()
            val title = H1("CMT Configuration Download Manager")
            title.style.set("font-size", "var(--lumo-font-size-l)")
            title.style.set("margin", "0")

            val tabs = Tabs(dashboardTab, updateTab, deploymentOrderTab)
            tabs.orientation = Tabs.Orientation.VERTICAL

            tabs.addSelectedChangeListener(this::selectedTabChanged)

            this.addToDrawer(tabs)
            this.addToNavbar(toggle, title)
        }

    private fun selectedTabChanged(event: Tabs.SelectedChangeEvent) {

        when (event.selectedTab) {
            dashboardTab -> UI.getCurrent().navigate(DashboardView::class.java)
            updateTab -> UI.getCurrent().navigate(UploadView::class.java)
            deploymentOrderTab -> UI.getCurrent().navigate(DeploymentOrderListView::class.java)
        }
    }
}