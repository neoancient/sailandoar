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
import board.Board
import game.Game
import game.Player
import game.PlayerColor
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import serialization.polymorphismModule
import unit.ShipStats
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList

interface ClientListener {
    fun receiveGame()
    fun receiveAvailableShips()
}

@Suppress("EXPERIMENTAL_API_USAGE")
class Client(name: String) {
    private val logger = LoggerFactory.getLogger(javaClass)
    var id = -1
    var player = Player(id, name, id, PlayerColor.BLUE)
    var game: Game? = null
    private val availableShips = ArrayList<ShipStats>()
    private val clientListeners: MutableList<ClientListener> = CopyOnWriteArrayList()
    private val connectionListeners: MutableList<ConnectionListener> = CopyOnWriteArrayList()

    private val connector = object : ClientConnector {
        override suspend fun handle(data: String) {
            handlePacket(Json {
                serializersModule = polymorphismModule
            }.decodeFromString(GamePacket.serializer(), data))
        }

        override suspend fun nameConflict(suggestion: String, taken: Set<String>) {
            connectionListeners.forEach {
                it.nameTaken(this@Client, suggestion, taken)
            }
        }

        override suspend fun connectionEstablished(clientId: Int) {
            id = clientId
            connectionListeners.forEach {
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
        client.send(Json {
            serializersModule = polymorphismModule

        }.encodeToString(GamePacket.serializer(), packet))
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
            is SendAvailableShipsPacket -> {
                availableShips.clear()
                availableShips.addAll(packet.ships)
            }
            is AddUnitPacket -> game?.replaceUnit(packet.unit.unitId, packet.unit)
            is RemoveUnitPacket -> game?.removeUnit(packet.unitId)
            is SetBoardPacket -> game?.board = packet.board
            else -> logger.error("Handler not found for packet ${packet.debugString()}")
        }
    }

    fun getAvailableShips(): List<ShipStats> = availableShips

    fun addUnit(shipId: UUID) {
        send(AddShipToForcePacket(id, shipId))
    }

    fun removeUnit(unitId: Int) {
        send(RemoveUnitPacket(id, unitId))
    }

    fun sendBoard(board: Board) {
        send(SetBoardPacket(id, board))
    }

    fun addClientListener(l: ClientListener) {
        clientListeners += l
    }

    fun removeClientListener(l: ClientListener) {
        clientListeners -= l
    }

    fun addConnectionListener(l: ConnectionListener) {
        connectionListeners += l
    }

    fun removeConnectionListener(l: ConnectionListener) {
        connectionListeners -= l
    }
}