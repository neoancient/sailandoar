package ui

import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import kotlinx.serialization.ExperimentalSerializationApi
import tornadofx.*

@ExperimentalSerializationApi
class MainUI: View() {
    override val root: BorderPane by fxml()
    private val panCenter: StackPane by fxid()

    @FXML
    private fun menuFileExit() {
        (app as SailAndOarApp).close()
    }

    @FXML
    private fun menuHelpAbout() {}
}