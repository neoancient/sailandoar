package server

import game.Game
import io.ktor.application.Application
import io.ktor.application.install
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
import kotlinx.serialization.json.Json
import net.*
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class Server(address: String, serverPort: Int) {
    private val game = Game()
    private val environment = applicationEngineEnvironment {
        module {
            module()
        }
        connector {
            host = address
            port = serverPort
        }
    }
    private val server = embeddedServer(Netty, environment)
    private val connections = ConcurrentHashMap<Int, ClientConnection?>()

    fun start() {
        server.start(false)
    }

    fun shutdown() {
        server.stop(1000, 1000)
    }

    @Suppress("unused")
    fun Application.module() {
        install(WebSockets)

        routing {
            webSocket("/") {
                println("Adding user")
                val thisConnection = ClientConnection(this)
                connections[thisConnection.id] = thisConnection
                try {
                    val packet = RequestNamePacket(thisConnection.id)
                    send(Json.encodeToString(Packet.serializer(), packet))
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        handlePacket(Json.decodeFromString(Packet.serializer(), frame.readText()))
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

    private suspend fun handlePacket(packet: Packet) {
        val handler = when (packet) {
            is SendNamePacket -> ConnectionNegotiator(packet, game)
            is RequestAvailableShipsPacket -> RequestAvailableShipsHandler(packet)
            else -> null
        }
        if (handler != null) {
            handler.process()
            handler.packetsToSend().forEach { p ->
                if (p.clientId < 0) {
                    connections.values.forEach { c ->
                        c?.session?.send(Json.encodeToString(Packet.serializer(), p))
                    }
                } else {
                    connections[p.clientId]?.session?.send(Json.encodeToString(Packet.serializer(), p))
                }
            }
        } else {
            LoggerFactory.getLogger("server").debug(""""Could not find correct handler for ${packet.debugString()} from player
                ${game.getPlayer(packet.clientId)}""".trimMargin())
        }
    }
}


