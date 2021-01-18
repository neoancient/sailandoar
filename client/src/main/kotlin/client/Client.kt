/*
 *  Sail and Oar
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

package client

import game.Game
import game.Player
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.DefaultClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.*
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList

class Client(name: String) {
    private val logger = LoggerFactory.getLogger(javaClass)
    var id = -1
    var player = Player(id, name)
    var game: Game? = null
    private val queue = Channel<Packet>()
    private val listeners: MutableList<ConnectionListener> = CopyOnWriteArrayList()

    @KtorExperimentalAPI
    val client = HttpClient {
        install(WebSockets)
    }

    @KtorExperimentalAPI
    suspend fun start(host: String, port: Int) {
        client.webSocket(method = HttpMethod.Get, host = host, port = port, path = "/") {
            val sendRoutine = launch { sendPackets() }
            val receiveRoutine = launch { receivePackets() }

            receiveRoutine.join()
            sendRoutine.cancelAndJoin()
        }
        client.close()
        logger.info("Client closed")
    }

    @KtorExperimentalAPI
    fun stop() {
        client.close()
    }

    fun send(packet: Packet) {
        runBlocking {
            queue.send(packet)
        }
    }

    suspend fun DefaultClientWebSocketSession.sendPackets() {
        for (packet in queue) {
            send(Json.encodeToString(Packet.serializer(), packet))
        }
    }

    suspend fun DefaultClientWebSocketSession.receivePackets() {
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                handlePacket(Json.decodeFromString(Packet.serializer(), frame.readText()))
            }
        } catch (e: Exception) {
            logger.error(e.localizedMessage)
        }
    }

    private fun handlePacket(packet: Packet) {
        when (packet) {
            is RequestNamePacket -> {
                id = packet.clientId
                send(SendNamePacket(id, player.name))
            }
            is InitClientPacket -> {
                game = requireNotNull(packet.game)
                player = requireNotNull(game?.getPlayer(id))
                listeners.forEach { it.clientConnected(this) }
            }
            is AddPlayerPacket -> game?.addPlayer(packet.player)
            is RemovePlayerPacket -> game?.removePlayer(packet.player.id)
        }
    }

    fun addConnectionListener(l: ConnectionListener) {
        listeners += l
    }

    fun removeConnectionListener(l: ConnectionListener) {
        listeners -= l
    }
}