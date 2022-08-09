package eu.ecodex.ccdm.ui

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import eu.ecodex.ccdm.dao.DeploymentOrderDao
import eu.ecodex.ccdm.entity.DeploymentOrder

@Route("/deploymentOrderList", layout = MainUI::class)
class DeploymentOrderListView (
    val deploymentDao: DeploymentOrderDao
        ): VerticalLayout() {

    init {
        val grid = Grid(DeploymentOrder::class.java)
        grid.addClassName("deploymentOrder-grid")
        grid.setSizeFull()
        grid.isAllRowsVisible = true
        //deploymentDao.findAll()

    }

}