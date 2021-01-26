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

package net

import ClientConnector
import NetworkClient
import game.Game
import game.Player
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.util.concurrent.CopyOnWriteArrayList

class Client(name: String) {
    private val logger = LoggerFactory.getLogger(javaClass)
    var id = -1
    var player = Player(id, name)
    var game: Game? = null
    private val listeners: MutableList<ConnectionListener> = CopyOnWriteArrayList()
    private val connector = object : ClientConnector {
        override suspend fun handle(data: String) {
            handlePacket(Json.decodeFromString(GamePacket.serializer(), data))
        }

        override suspend fun nameConflict(suggestion: String, taken: Set<String>) {
            listeners.forEach {
                it.nameTaken(this@Client, suggestion, taken)
            }
        }

        override suspend fun connectionEstablished(clientId: Int) {
            id = clientId
            listeners.forEach {
                it.clientConnected(this@Client)
            }
            send(RequestAvailableShipsPacket(id))
        }

        override suspend fun receiveChatMessage(html: String) {
            game?.appendChat(html)
        }
    }

    private val client = NetworkClient(name, connector)

    suspend fun start(host: String, port: Int) {
        client.start(host, port)
    }

    fun stop() {
        client.stop()
    }

    fun send(packet: GamePacket) {
        client.send(Json.encodeToString(GamePacket.serializer(), packet))
    }

    suspend fun sendName(name: String) {
        client.changeName(name)
    }

    suspend fun sendChatMessage(text: String) {
        client.sendChatMessage(id, text)
    }

    private fun handlePacket(packet: GamePacket) {
        when (packet) {
            is SendGamePacket -> {
                id = packet.clientId
                game = requireNotNull(packet.game)
                player = requireNotNull(game?.getPlayer(id))
            }
            is AddPlayerPacket -> if (packet.player.id != id) game?.addPlayer(packet.player)
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