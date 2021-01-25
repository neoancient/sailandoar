import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.*
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/*
 * Sail and Oar
 * Copyright (c) 2021 Carl W Spain
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

public interface ServerConnector {
    public suspend fun handle(data: String)
}

public class NetworkServer(
    address: String, serverPort: Int,
    private val connector: ServerConnector
) {
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
    private val connections = ConcurrentHashMap<Int, ClientConnection>()
    public val users: Users = Users()

    public fun start(wait: Boolean = false) {
        server.start(wait)
    }

    public fun shutdown(gracePeriodMillis: Long, timeoutMillis: Long) {
        server.stop(gracePeriodMillis, timeoutMillis)
    }

    @Suppress("unused")
    private fun Application.module() {
        install(WebSockets)

        routing {
            webSocket("/") {
                val thisConnection = ClientConnection(this)
                try {
                    val packet = RequestNamePacket()
                    send(Json.encodeToString(Packet.serializer(), packet))
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        handlePacket(Json.decodeFromString(Packet.serializer(), frame.readText()), thisConnection)
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

    private suspend fun handlePacket(packet: Packet, connection: ClientConnection) {
        when (packet) {
            is SendNamePacket ->
                if (packet.name in users) {
                    users.suggestAlternateName(packet.name).let {
                        connection.send(SuggestNamePacket(it.first, it.second))
                    }
                } else {
                    users += User(packet.name)
                    connection.send(InitClientPacket(connection.id))
                }
            is TextPacket -> connector.handle(packet.text)
        }
    }

    public suspend fun send(id: Int, data: String) {
        val packet = TextPacket(data)
        if (id < 0) {
            connections.values.forEach {
                it.send(packet)
            }
        } else {
            connections[id]?.send(packet)
        }
    }
}

private class ClientConnection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val id = lastId.getAndIncrement()
    var name: String = "New Player"

    suspend fun send(packet: Packet) {
        session.send(Frame.Text(Json.encodeToString(Packet.serializer(), packet)))
    }
}
