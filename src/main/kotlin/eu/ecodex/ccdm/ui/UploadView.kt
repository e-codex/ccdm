package eu.ecodex.ccdm.ui

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

@Route(value = "/upload", layout = MainUI::class)
class UploadView(configDao: CMTConfigurationDao): VerticalLayout()  {

    init {
        val syncButton = Button(VaadinIcon.REFRESH.create())
        val syncButtonLayout = VerticalLayout(syncButton)
        syncButtonLayout.alignItems = Alignment.END

        val useCase = ComboBox<String>()
        val stage = ComboBox<String>()

        val formLayout = FormLayout()
        // to make formLayout only display 1 column, consisting of description and ComboBox:
        formLayout.responsiveSteps = mutableListOf(FormLayout.ResponsiveStep("0", 1))
        formLayout.addFormItem(useCase, "Use Case")
        formLayout.addFormItem(stage, "Stage")

        val grid = Grid(CMTConfiguration::class.java)
        grid.addClassName("cmtConfig-grid")
        grid.setSizeFull()
        grid.setColumns("version", "goLiveDate","downloadDate", "publishDate")

        // https://vaadin.com/docs/latest/components/grid
        grid.isAllRowsVisible = true
        grid.columns.forEach() { col -> col.setAutoWidth(true) }

        // how to assign number for each column?
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