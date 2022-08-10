package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.NavigationEvent
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteConfiguration
import com.vaadin.flow.router.RouteParam
import com.vaadin.flow.router.RouteParameters
import eu.ecodex.ccdm.dao.CMTConfigurationDao
import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.service.CMTConfigSyncService
import org.springframework.data.domain.PageRequest
import javax.swing.SingleSelectionModel

@Route(value = "/upload", layout = MainUI::class)
class UploadView(
    configDao: CMTConfigurationDao,
    cmtSyncService: CMTConfigSyncService
                 ): VerticalLayout() {

    init {
        val syncButton = Button(VaadinIcon.REFRESH.create())
        val syncButtonLayout = VerticalLayout(syncButton)

        // in thread ausladen
        // asynchron/non-blocking ausführen -> Kotlin Coroutine: awaitExchange() ?
        // https://www.baeldung.com/kotlin/spring-boot-kotlin-coroutines
        // wenn fertig geladen, Tabelle neu laden - Check.
        // für grid data provider schreiben! -> vor Vaadin V23: https://vaadin.com/docs/latest/binding-data/data-provider#lazy-data-binding-using-callbacks
        // https://vaadin.com/docs/v14/flow/binding-data/tutorial-flow-data-provider/#lazy-loading-data-from-a-backend-service
        syncButtonLayout.alignItems = Alignment.END

        val project = ComboBox<CMTConfiguration>()
        val environment = ComboBox<String>()

        val formLayout = FormLayout()
        formLayout.responsiveSteps = mutableListOf(FormLayout.ResponsiveStep("0", 1))
        formLayout.addFormItem(project, "Project")
        formLayout.addFormItem(environment, "Environment")

        val grid = Grid(CMTConfiguration::class.java)
        grid.addClassName("cmtConfig-grid")
        grid.setSizeFull()
        grid.setColumns("version", "goLiveDate","downloadDate", "publishDate")

        // https://vaadin.com/docs/latest/components/grid
        grid.isAllRowsVisible = true
        grid.columns.forEach() { col -> col.setAutoWidth(true) }

        grid.addComponentColumn { cmtConfig ->

            Button(Icon("lumo", "chevron-right")) {

                UI.getCurrent().navigate(DeploymentOrderView::class.java, cmtConfig.configId)
            }
        }

        // grid.setItems(configDao.findAll())
        grid.setItems { query ->
            configDao.findAll(
                    PageRequest.of(query.page, query.pageSize))
                    .stream()
        }

        syncButton.onLeftClick {
            cmtSyncService.synchronise()
            grid.dataProvider.refreshAll()
        }

        add(syncButtonLayout, formLayout, grid)
    }

    // https://vaadin.com/docs/v14/flow/routing/tutorial-router-url-parameters
    // https://stackoverflow.com/questions/51533838/vaadin-pass-parameter-to-view
    // when done: pull request -> bitbucket

}