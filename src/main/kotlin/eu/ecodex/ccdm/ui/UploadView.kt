package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.Route
import eu.ecodex.ccdm.dao.CMTConfigurationDao
import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.service.CMTConfigSyncService
import org.springframework.data.domain.PageRequest
import java.util.concurrent.Executors

@Route(value = "/upload", layout = MainUI::class)
class UploadView(
    private val configDao: CMTConfigurationDao,
    private val cmtSyncService: CMTConfigSyncService
                 ): VerticalLayout(), AfterNavigationObserver {

    private val grid = Grid(CMTConfiguration::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private val projectBox = Select<String>()
    private val environmentBox = Select<String>()

    init {
        val syncButton = Button(VaadinIcon.REFRESH.create())
        val syncButtonLayout = VerticalLayout(syncButton)

        syncButtonLayout.alignItems = Alignment.END

        projectBox.isEmptySelectionAllowed = true
        projectBox.addValueChangeListener { refreshGrid() }

        environmentBox.isEmptySelectionAllowed = true
        environmentBox.addValueChangeListener {refreshGrid()}

        val formLayout = FormLayout()
        formLayout.responsiveSteps = mutableListOf(FormLayout.ResponsiveStep("0", 1))
        formLayout.addFormItem(projectBox, "Project")
        formLayout.addFormItem(environmentBox, "Environment")

        grid.addClassName("cmtConfig-grid")
        grid.setSizeFull()
        grid.isVerticalScrollingEnabled = false
        grid.setColumns("version", "project", "environment", "goLiveDate","downloadDate", "publishDate")

        grid.isAllRowsVisible = true
        grid.columns.forEach { col -> col.setAutoWidth(true) }

        grid.addComponentColumn { cmtConfig ->

            Button(Icon("lumo", "chevron-right")) {

                UI.getCurrent().navigate(DeploymentOrderView::class.java, cmtConfig.configId)
            }
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
                projectBox.setItems(configDao.findAllProjects())
                environmentBox.setItems(configDao.findAllEnvironments())
            }
        }
    }

    private fun refreshGrid() {
        grid.dataProvider.refreshAll()
    }

    override fun afterNavigation(event: AfterNavigationEvent?) {

        environmentBox.setItems (configDao.findAllEnvironments())
        grid.setItems { query ->
            configDao.findAllByEnvironmentAndProject(
                if(environmentBox.value == null) "*" else environmentBox.value,
                if(projectBox.value == null) "*" else projectBox.value,
                PageRequest.of(query.page, query.pageSize))
                .stream()
        }
    }
}
