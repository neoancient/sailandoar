package ui

import client.Client
import client.ConnectionListener
import dialog.SelectNameDialog
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import server.Server
import tornadofx.*
import java.io.IOException
import java.net.SocketException
import java.nio.channels.UnresolvedAddressException

class SailAndOarApp: App(SplashView::class) {
    internal val serverProperty: ObjectProperty<Server> = SimpleObjectProperty()
    internal val clientProperty: ObjectProperty<Client> = SimpleObjectProperty()

    /**
     * Performs all necessary shutdown.
     */
    fun close() {
        clientProperty.value?.stop()
        serverProperty.value?.shutdown()
        Platform.exit()
        System.exit(0)
    }
}

fun Alert.wrapContent(width: Double = 400.0) {
    dialogPane.content = text(contentText) {
        wrappingWidth = width
    }
    dialogPane.style = "-fx-padding:10px"
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
        val dialog = find<StartGameDialog>(mapOf("host" to "localhost"))
        dialog.openModal(stageStyle = StageStyle.UTILITY, block = true)
        if (!dialog.canceled) {
            client = Client(dialog.name)
            client.addConnectionListener(this)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    client.start(dialog.host, dialog.port)
                } catch (ex: Exception) {
                    Platform.runLater {
                        Alert(
                            AlertType.ERROR,
                            when (ex) {
                                is UnresolvedAddressException -> "Cannot resolve host"
                                else -> "While trying to connect to server: ${ex.localizedMessage}"
                            }
                        ).apply {
                            wrapContent()
                        }.showAndWait()
                    }
                }
            }
        }
    }

    fun startGame(name: String, port: Int) {
        server = Server("localhost", port)
        try {
            server.start()
        } catch (ex: Exception) {
            Alert(
                AlertType.ERROR,
                "Could not create server on port $port: ${ex.localizedMessage}"
            ).apply {
                wrapContent()
            }.showAndWait()
            return
        }
        client = Client(name)
        client.addConnectionListener(this)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                client.start("localhost", port)
            } catch (ex: Exception) {
                Platform.runLater {
                    Alert(
                        AlertType.ERROR,
                        when (ex) {
                            is UnresolvedAddressException -> "Cannot resolve host"
                            else -> "While trying to connect to server: ${ex.localizedMessage}"
                        }
                    ).apply {
                        wrapContent()
                    }.showAndWait()
                }
            }
        }
    }

    override fun clientConnected(client: Client) {
        Platform.runLater {
            replaceWith<MainUI>()
        }
        client.removeConnectionListener(this)
    }

    override fun clientDisconnected(client: Client) {
        // Do nothing
    }

    override fun nameTaken(client: Client, suggestion: String, taken: Set<String>) {
        Platform.runLater {
            val dialog = find<SelectNameDialog>(
                params = mapOf(
                    SelectNameDialog::suggested to suggestion,
                    SelectNameDialog::taken to taken
                )
            )
            dialog.title = messages["nameTaken"]
            dialog.openModal(stageStyle = StageStyle.UTILITY, block = true)
            GlobalScope.launch(Dispatchers.IO) {
                client.sendName(dialog.name)
            }
        }
    }
}
