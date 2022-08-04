package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.onLeftClick
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

@Route(value = "/upload", layout = MainUI::class)
class UploadView(configDao: CMTConfigurationDao,
                 cmtSyncService: CMTConfigSyncService,
                 ): VerticalLayout()  {

    init {
        val syncButton = Button(VaadinIcon.REFRESH.create())
        val syncButtonLayout = VerticalLayout(syncButton)
        syncButton.onLeftClick { cmtSyncService.synchronise() }
        // in thread ausladen
        // asynchron/non-blocking ausführen
        // wenn fertig geladen, Tabelle neu laden
        // für grid data provider schreiben!
        syncButtonLayout.alignItems = Alignment.END

        val project = ComboBox<String>()
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

        // if label for Deployment column needed: probably need to change to addColumn and add ComponentRenderer instead of addComponentColumn,
        // see code: https://vaadin.com/docs/latest/components/grid/#dynamic-height
        grid.addComponentColumn {
            Button(Icon("lumo", "chevron-right")) {
                println(it)
            }
        }
        //grid.setItems(listOf(CMTConfiguration(configId = 2)))
        grid.setItems(configDao.findAll())

        add(syncButtonLayout, formLayout, grid)
    }
}