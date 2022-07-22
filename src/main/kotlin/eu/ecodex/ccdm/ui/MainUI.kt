package eu.ecodex.ccdm.ui

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(value = "")
@PageTitle(value = "MainUI")
class MainUI: AppLayout() {

    //private lateinit var tabs: Tabs

        init {
            val toggle = DrawerToggle()
            val dashboardTab = Tab("Dashboard")
            //dashboardTab.add(VaadinIcon.DASHBOARD)
            val updateTab = Tab("Update")

            val tabs = Tabs(dashboardTab, updateTab)
            tabs.orientation = Tabs.Orientation.VERTICAL

            addToDrawer(tabs)
            addToNavbar(toggle)
        }

    //(VaadinIcon.DASHBOARD, "Dashboard")
    // (VaadinIcon.DASHBOARD, "Dashboard")

        /*val button = Button("Hello")
        this.addToDrawer(button)*/
    }


/*
Useful Links:
https://vaadin.com/docs/latest/tutorial/overview
https://vaadin.com/docs/latest/routing/layout
https://vaadin.com/docs/latest/configuration/live-reload

Kotlin vs. Java:
https://www.kotlinvsjava.com/

To check:
localhost:8080
in browser
 */
