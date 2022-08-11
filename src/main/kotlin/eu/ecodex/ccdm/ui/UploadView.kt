package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import eu.ecodex.ccdm.dao.CMTConfigurationDao
import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.service.CMTConfigSyncService
import org.springframework.data.domain.PageRequest
import java.util.concurrent.Executors

@Route(value = "/upload", layout = MainUI::class)
open class UploadView(
    configDao: CMTConfigurationDao,
    private val cmtSyncService: CMTConfigSyncService
                 ): VerticalLayout() {

    private val grid = Grid(CMTConfiguration::class.java)
    private val executor = Executors.newSingleThreadExecutor()

    init {
        val syncButton = Button(VaadinIcon.REFRESH.create())
        val syncButtonLayout = VerticalLayout(syncButton)

        syncButtonLayout.alignItems = Alignment.END

        val project = ComboBox<CMTConfiguration>()
        val environment = ComboBox<String>()

        val formLayout = FormLayout()
        formLayout.responsiveSteps = mutableListOf(FormLayout.ResponsiveStep("0", 1))
        formLayout.addFormItem(project, "Project")
        formLayout.addFormItem(environment, "Environment")


        grid.addClassName("cmtConfig-grid")
        grid.setSizeFull()
        grid.setColumns("version", "goLiveDate","downloadDate", "publishDate")

        grid.isAllRowsVisible = true
        grid.columns.forEach() { col -> col.setAutoWidth(true) }

        grid.addComponentColumn { cmtConfig ->

            Button(Icon("lumo", "chevron-right")) {

                UI.getCurrent().navigate(DeploymentOrderView::class.java, cmtConfig.configId)
            }
        }

        grid.setItems { query ->
            configDao.findAll(
                    PageRequest.of(query.page, query.pageSize))
                    .stream()
        }

        syncButton.onLeftClick {
           handleSyncButtonClick()
        }

        add(syncButtonLayout, formLayout, grid)
    }

    private fun handleSyncButtonClick() {
        val currentUi = this.ui.get()

        executor.execute {
            cmtSyncService.synchronise()

            currentUi.access {
                grid.dataProvider.refreshAll()
            }
        }
    }
}
