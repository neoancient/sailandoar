package ui

import client.Client
import client.ConnectionListener
import dialog.StartGameDialog
import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.StageStyle
import org.slf4j.LoggerFactory
import server.Server
import tornadofx.*
import java.io.IOException

class SailAndOarApp: App(SplashView::class) {
    internal val serverProperty: ObjectProperty<Server> = SimpleObjectProperty()
    internal val clientProperty: ObjectProperty<Client> = SimpleObjectProperty()

    /**
     * Performs all necessary shutdown.
     */
    fun close() {
        serverProperty.value?.shutdown()
    }
}

class SplashView: View(), ConnectionListener {
    private var server: Server by (app as SailAndOarApp).serverProperty
    private var client: Client by (app as SailAndOarApp).clientProperty

    private val logger = LoggerFactory.getLogger(javaClass)

    override val root = borderpane {
        center = imageview {
            image = Image(SplashView::class.java.getResourceAsStream("800px-Trafalgar-Auguste_Mayer.jpg"))
            fitHeight = image.height
            fitWidth = image.width
        }
        bottom = hbox {
            alignment = Pos.CENTER
            spacing = 5.0
            BorderPane.setAlignment(this, Pos.CENTER)
            BorderPane.setMargin(this, Insets(5.0, 5.0, 5.0, 5.0))
            button(messages["newGame"]) {
                action {handleNewGame()}
            }
            button(messages["connectToGame"]) {
                action {handleJoinGame()}
            }
            button(messages["startServer"]) {
                action {handleStartServer()}
            }
        }
    }

    private fun handleNewGame() {
        val dialog = find<StartGameDialog>()
        dialog.openModal(stageStyle = StageStyle.UTILITY, block = true)
        if (!dialog.canceled) {
            startGame(dialog.name, dialog.port)
        }
    }

    private fun handleJoinGame() {
        val dialog = find<StartGameDialog>()
        dialog.openModal(stageStyle = StageStyle.UTILITY, block = true)
        if (!dialog.canceled) {
            try {
                client = Client(dialog.name)
                client.addConnectionListener(this)
                client.start("localhost", dialog.port)
            } catch (ex: IOException) {
                logger.error("While trying to connect to server:", ex)
                val alert = Alert(AlertType.ERROR,
                        "Could not connect to server. See log for details.",
                        ButtonType.OK)
                alert.showAndWait()
            }
        }
    }

    private fun handleStartServer() {
        server = Server("127.0.0.1", 1805)
        try {
            server.start()
        } catch (ex: IOException) {
            logger.error("While trying to create server on port 1805", ex)
            val alert = Alert(AlertType.ERROR,
                    "Could not create server on port 1805. See log for details.")
            alert.showAndWait()
            return
        }
    }

    fun startGame(name: String, port: Int) {
        server = Server("127.0.0.1", port)
        try {
            server.start()
        } catch (ex: IOException) {
            logger.error("While trying to create server on port $port", ex)
            val alert = Alert(AlertType.ERROR,
                    "Could not create server on port $port. See log for details.")
            alert.showAndWait()
            return
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
        }
        try {
            client = Client(name)
            client.addConnectionListener(this)
            client.start("localhost", port)
        } catch (ex: IOException) {
            logger.error("While trying to connect to server:", ex)
            val alert = Alert(AlertType.ERROR,
                    "Could not connect to server. See log for details.",
                    ButtonType.OK)
            alert.showAndWait()
        }
    }

    override fun clientConnected(client: Client) {
        Platform.runLater {
            replaceWith<MainUI>()
        }
    }

    override fun clientDisconnected(client: Client) {
        // Do nothing
    }
}
