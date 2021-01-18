package ui

import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import tornadofx.*

class MainUI: View() {
    override val root: BorderPane by fxml()
    private val panCenter: StackPane by fxid()

    @FXML
    private fun menuFileExit() {
        (app as SailAndOarApp).close()
        close()
    }

    @FXML
    private fun menuHelpAbout() {}

    override fun onDock() {
        currentWindow?.setOnCloseRequest {
            (app as SailAndOarApp).close()
        }
    }
}