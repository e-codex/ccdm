package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.RouterLayout

class MainUIDSL: KComposite(), RouterLayout {

    private val root = ui {
        ui {
            appLayout {
                navbar {
                    drawerToggle()
                    h3("CMT Configuration Download Manager")
                }
                drawer {
                    tabs {
                        orientation = Tabs.Orientation.VERTICAL
                        tab {
                            button(" Dashboard", icon = icon(VaadinIcon.DASHBOARD)) {
                                onLeftClick { navigateToView(DashboardViewDSL::class) }
                            }
                        }
                        tab {
                            button(" Upload", icon = icon(VaadinIcon.REFRESH)) {
                                onLeftClick { navigateToView(UploadViewDSL::class) }
                            }
                        }
                    }
                }
            }
        }
    }
}

// https://www.vaadinonkotlin.eu//navigating/