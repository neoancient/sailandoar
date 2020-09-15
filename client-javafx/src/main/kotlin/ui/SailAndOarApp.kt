package ui

import client.Client
import client.ConnectionListener
import dialog.StartGameDialog
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
import kotlinx.serialization.ExperimentalSerializationApi
import net.NetworkConnection
import org.slf4j.LoggerFactory
import server.Server
import tornadofx.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
class SailAndOarApp: App(SplashView::class) {
    internal val serverProperty: ObjectProperty<Server> = SimpleObjectProperty()
    internal val clientProperty: ObjectProperty<Client> = SimpleObjectProperty()
    internal val clientThreadPool: ExecutorService = Executors.newCachedThreadPool()

    /**
     * Performs all necessary shutdown.
     */
    fun close() {
        serverProperty.value?.shutdown()
        clientThreadPool.shutdown()
        try {
            if (!clientThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                clientThreadPool.shutdownNow()
            }
        } catch (e: InterruptedException) {
            // Re-cancel and preserve interrupt status
            clientThreadPool.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }
}

@ExperimentalSerializationApi
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

    @ExperimentalSerializationApi
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
                val socket = Socket()
                socket.connect(InetSocketAddress("localhost", dialog.port))
                val connection = NetworkConnection(client.id, socket, client)
                (app as SailAndOarApp).clientThreadPool.execute(connection)
                client.start((app as SailAndOarApp).clientThreadPool)
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
        server = Server()
        try {
            server.listen(1805)
            server.start()
        } catch (ex: IOException) {
            logger.error("While trying to create server on port 1805", ex)
            val alert = Alert(AlertType.ERROR,
                    "Could not create server on port 1805. See log for details.")
            alert.showAndWait()
            return
        }
    }

    @ExperimentalSerializationApi
    fun startGame(name: String, port: Int) {
        server = Server()
        try {
            server.listen(port)
            server.start()
        } catch (ex: IOException) {
            logger.error("While trying to create server on port $port", ex)
            val alert = Alert(AlertType.ERROR,
                    "Could not create server on port $port. See log for details.")
            alert.showAndWait()
            return
        }
        try {
            client = Client(name)
            client.addConnectionListener(this)
            client.start((app as SailAndOarApp).clientThreadPool)
            val socket = Socket()
            socket.connect(InetSocketAddress("localhost", port))
            val connection = NetworkConnection(client.id, socket, client)
            (app as SailAndOarApp).clientThreadPool.execute(connection)
        } catch (ex: IOException) {
            logger.error("While trying to connect to server:", ex)
            val alert = Alert(AlertType.ERROR,
                    "Could not connect to server. See log for details.",
                    ButtonType.OK)
            alert.showAndWait()
        }
    }

    override fun clientConnected(client: Client) {
        replaceWith<MainUI>()
    }

    override fun clientDisconnected(client: Client) {
        // Do nothing
    }
}
