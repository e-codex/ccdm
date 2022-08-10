package eu.ecodex.ccdm.ui

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.listbox.MultiSelectListBox
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import eu.ecodex.ccdm.dao.CMTConfigurationDao
import eu.ecodex.ccdm.entity.CMTConfiguration
import eu.ecodex.ccdm.entity.ComponentDeploymentOrder
import eu.ecodex.ccdm.entity.DeploymentOrder
import eu.ecodex.ccdm.service.DeploymentOrderService
import org.slf4j.LoggerFactory

@Route(value = "deployment", layout = MainUI::class)
class DeploymentOrderView(
    private val configDao: CMTConfigurationDao,
    private val deployService: DeploymentOrderService
): VerticalLayout(), HasUrlParameter<Int> {

    private var selectedConfig: CMTConfiguration? = null

    private val pMode = TextField()
    private val environment = TextField()
    private val project = TextField()
    private val goLiveDate = DateTimePicker()
    private val components = MultiSelectListBox<Components>()
    private val saveButton = Button("Save", Icon("lumo", "download")) { event ->
        this.handleSaveButtonClick(event)}
    private val logger = LoggerFactory.getLogger(DeploymentOrderView::class.java)

    init {

        val deploymentOrderLabel = Label ("Create Deployment Order")
        deploymentOrderLabel.style.set("fontWeight","bold")

        components.setItems(Components.values().toMutableList())

        pMode.isReadOnly = true
        environment.isReadOnly = true
        project.isReadOnly = true

        val formLayout = FormLayout()
        formLayout.responsiveSteps = mutableListOf(FormLayout.ResponsiveStep("0", 1))
        formLayout.addFormItem(pMode, "PMode")
        formLayout.addFormItem(environment, "Environment")
        formLayout.addFormItem(project, "Project")
        formLayout.addFormItem(goLiveDate, "Go Live Date")
        formLayout.addFormItem(components, "Components")

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

        val cancelButton = Button("Cancel", Icon(VaadinIcon.EXIT)) {
            UI.getCurrent().navigate(UploadView::class.java)
        }
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR)

        val buttonLayout = HorizontalLayout()
        buttonLayout.add(saveButton, cancelButton)

        add(deploymentOrderLabel, formLayout, buttonLayout)
    }

    private fun handleSaveButtonClick(event: ClickEvent<Button>?) {
        val componentDeploymentOrder = ArrayList<ComponentDeploymentOrder> ()

        components.value.forEach { v ->
            val d = ComponentDeploymentOrder(component = v.nameOfComponent)
            componentDeploymentOrder.add(d)
        }

        val deploymentOrder = DeploymentOrder(config = selectedConfig!!,
            deploymentDate = goLiveDate.value,
            principal = "", //TODO: use security context to retrieve principal
            componentDeploymentOrder = componentDeploymentOrder
        )

        try {
            deployService.createNewDeploymentOrder(deploymentOrder)
            saveButton.isEnabled = false
            Notification.show("Successfully saved")

        } catch (e: Exception) {
            Notification.show("An error occurred")
            logger.warn("Was not able to save Deployment Order because of ", e)
        }
    }

    override fun setParameter(event: BeforeEvent?, parameter: Int?) {
        if (parameter == null) {
            throw IllegalArgumentException("Route param must never be null")
        }
        selectedConfig = configDao.findById(parameter).get()
        pMode.value = selectedConfig!!.cmtName
        project.value = selectedConfig!!.project
        environment.value = selectedConfig!!.environment
        goLiveDate.value = selectedConfig!!.publishDate.plusDays(7)
    }
}

// New feature coming soon: https://vaadin.com/docs/latest/components/multi-select-combo-box
// using parameters with views: https://vaadin.com/docs/v8/framework/articles/UsingParametersWithViews
// pass parameters via URL: https://vaadin.com/docs/v14/flow/routing/tutorial-router-url-parameters
// https://www.baeldung.com/kotlin/spring-boot-kotlin-coroutines
// read up on coroutines: https://kotlinlang.org/docs/coroutines-basics.html