package eu.ecodex.ccdm.ui

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import eu.ecodex.ccdm.dao.DeploymentOrderDao
import eu.ecodex.ccdm.entity.DeploymentOrder

@Route("/deploymentOrderList", layout = MainUI::class)
class DeploymentOrderListView (
    deploymentDao: DeploymentOrderDao
        ): VerticalLayout() {

    init {
        val grid = Grid(DeploymentOrder::class.java)
        grid.addClassName("deploymentOrder-grid")
        grid.setSizeFull()
        grid.isAllRowsVisible = true
        grid.setColumns("config", "deploymentDate")
        grid.setItems(deploymentDao.findAll())

        add(grid)
    }
}

// Qs:
// how to fix: .LazyInitializationException: failed to lazily initialize
// a collection of role: eu.ecodex.ccdm.entity.CMTConfiguration.configDeployments, could not initialize proxy - no Session
// what kinds of issues should be shown to user? -> try catch? Notification?