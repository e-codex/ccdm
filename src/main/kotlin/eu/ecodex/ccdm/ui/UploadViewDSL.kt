package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.router.Route
import eu.ecodex.ccdm.entity.CMTConfiguration

@Route(value = "/dsl/upload", layout = MainUIDSL::class)
class UploadViewDSL: KComposite() {

    private val root = ui {
        verticalLayout {
            button(icon = icon(VaadinIcon.REFRESH)) {
                horizontalAlignSelf = FlexComponent.Alignment.END
            }
            formLayout {
                responsiveSteps = mutableListOf(FormLayout.ResponsiveStep("0", 1))
                horizontalAlignSelf = FlexComponent.Alignment.START
                formItem {
                    horizontalLayout {
                        comboBox<String> (label = "Use Case") {
                        } }
                }
                formItem {
                    horizontalLayout {
                        comboBox<String>(label = "Stage") {
                        }
                    }
                }
            }
            // for grid: (dataProvider = CMTConfiguration.dataProvider)
            // write new config & dao for dataProvider
           //grid {
           //    setSizeFull()
           //}
        }
    }
}

// https://www.vaadinonkotlin.eu//tutorial/