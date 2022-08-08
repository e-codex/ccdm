package eu.ecodex.ccdm.ui

import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.refresh
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.listbox.MultiSelectListBox
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.router.Route
import eu.ecodex.ccdm.dao.CMTConfigurationDao
import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.service.CMTConfigSyncService
import org.springframework.data.domain.PageRequest

@Route(value = "/upload", layout = MainUI::class)
class UploadView(configDao: CMTConfigurationDao,
                 cmtSyncService: CMTConfigSyncService
                 ): VerticalLayout()  {

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

        // data provider tryout
        // data provider tryout end

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

}