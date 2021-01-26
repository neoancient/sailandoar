package ui

import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainUI: View() {
    val chatWindow: ChatWindow by inject()

    override val root: BorderPane by fxml()
    private val panCenter: AnchorPane by fxid()
    private val panChat: AnchorPane by fxid()

    init {
        panChat.children.setAll(chatWindow.root)
        with(chatWindow.root) {
            AnchorPane.setTopAnchor(this, 0.0)
            AnchorPane.setBottomAnchor(this, 0.0)
            AnchorPane.setLeftAnchor(this, 0.0)
            AnchorPane.setRightAnchor(this, 0.0)
        }
    }

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