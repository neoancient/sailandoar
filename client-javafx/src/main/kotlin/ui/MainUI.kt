package ui

import game.GameListener
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.web.WebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*

class MainUI: GameListener, View() {
    override val root: BorderPane by fxml()
    private val panCenter: StackPane by fxid()
    private val txtChatWindow: WebView by fxid()
    private val txtChatEntry: TextField by fxid()
    private val btnSend: Button by fxid()

    private val chatMessages = StringBuilder()

    init {
        btnSend.enableWhen(txtChatEntry.textProperty().isNotEmpty)
        (app as SailAndOarApp).clientProperty.value?.game?.addListener(this)
    }

    @FXML
    private fun menuFileExit() {
        (app as SailAndOarApp).close()
        close()
    }

    @FXML
    private fun menuHelpAbout() {}

    @FXML
    private fun btnSendAction() {
        GlobalScope.launch(Dispatchers.IO) {
            (app as SailAndOarApp).clientProperty.value?.sendChatMessage(txtChatEntry.text)
            Platform.runLater {
                txtChatEntry.text = ""
            }
        }
    }

    override fun onDock() {
        currentWindow?.setOnCloseRequest {
            (app as SailAndOarApp).close()
        }
    }

    override fun playerAdded(playerId: Int) {
    }

    override fun playerRemoved(playerId: Int) {
    }

    override fun unitAdded(unitId: Int) {
    }

    override fun unitRemoved(unitId: Int) {
    }

    override fun appendChat(text: String) {
        Platform.runLater {
            chatMessages.append(text)
            txtChatWindow.engine.loadContent(chatMessages.toString())
        }
    }
}