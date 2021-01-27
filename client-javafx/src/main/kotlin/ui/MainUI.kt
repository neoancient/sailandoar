package ui

import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainUI: View() {
    internal val lobby: LobbyView by inject()
    internal val chatWindow: ChatWindow by inject()

    override val root: BorderPane by fxml()
    private val panMain: AnchorPane by fxid()
    private val panChat: AnchorPane by fxid()

    init {
        fun anchor(child: Node) {
            AnchorPane.setTopAnchor(child, 0.0)
            AnchorPane.setBottomAnchor(child, 0.0)
            AnchorPane.setLeftAnchor(child, 0.0)
            AnchorPane.setRightAnchor(child, 0.0)
        }
        panMain.children.setAll(lobby.root)
        panChat.children.setAll(chatWindow.root)
        anchor(lobby.root)
        anchor(chatWindow.root)
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