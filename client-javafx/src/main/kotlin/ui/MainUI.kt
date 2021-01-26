package ui

import game.GameListener
import javafx.application.Platform
import javafx.concurrent.Worker
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

class MainUI: View() {
    val chatWindow: ChatWindow by inject()

    override val root: BorderPane by fxml()
    private val panCenter: StackPane by fxid()

    init {
        root.bottom = chatWindow.root
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