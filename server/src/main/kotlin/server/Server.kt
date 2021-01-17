package server

import game.Game
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.InitClientPacket
import net.Packet
import net.RequestAvailableShipsPacket
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class Server(address: String, serverPort: Int) {
    val game = Game()
    private val environment = applicationEngineEnvironment {
        module {
            module(this@Server)
        }
        connector {
            host = address
            port = serverPort
        }
    }
    private val server = embeddedServer(Netty, environment)

    fun start() {
        server.start(false)
    }

    fun shutdown() {

    }
}

@Suppress("unused")
fun Application.module(server: Server) {
    install(WebSockets)

    routing {
        val connections = ConcurrentHashMap<Int, ClientConnection?>()
        webSocket("/") {
            println("Adding user")
            val thisConnection = ClientConnection(this)
            connections[thisConnection.id] = thisConnection
            try {
                val packet = InitClientPacket(thisConnection.id, server.game)
                send(Json.encodeToString(packet))
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    handlePacket(Json.decodeFromString(frame.readText()), server.game)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection")
                connections.remove(thisConnection.id)
            }
        }
    }
}

suspend fun DefaultWebSocketSession.handlePacket(packet: Packet, game: Game) {
    val handler = when (packet) {
        is RequestAvailableShipsPacket -> RequestAvailableShipsHandler(packet)
        else -> null
    }
    if (handler != null) {
        handler.process()
        handler.packetsToSend().forEach {
            send(Json.encodeToString(it))
        }
    } else {
        LoggerFactory.getLogger("server").debug(""""Could not find correct handler for ${packet.debugString()} from player
                ${game.getPlayer(packet.clientId)}""".trimMargin())
    }
}


