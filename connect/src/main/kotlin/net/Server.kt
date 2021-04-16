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

import NetworkServer
import ServerConnector
import game.Game
import kotlinx.serialization.json.Json
import net.handler.*
import net.handler.AddShipHandler
import net.handler.RemoveUnitHandler
import org.slf4j.LoggerFactory
import serialization.polymorphismModule

class Server(address: String, serverPort: Int) {
    private val game = Game()
    private val connector = object : ServerConnector {
        override suspend fun handle(json: String) {
            handlePacket(Json {
                serializersModule = polymorphismModule

            }.decodeFromString(GamePacket.serializer(), json))
        }

        override suspend fun playerConnected(id: Int, name: String) {
            val player = game.newPlayer(id, name)
            send(SendGamePacket(id, game))
            send(AddPlayerPacket(ALL_CLIENTS, player))
        }
    }

    private val server = NetworkServer(address, serverPort, connector)

    fun start() {
        server.start(false)
    }

    fun shutdown() {
        server.shutdown(1000, 1000)
    }

    private suspend fun send(packet: GamePacket) {
        server.send(packet.clientId, Json {
            serializersModule = polymorphismModule

        }.encodeToString(GamePacket.serializer(), packet))
    }

    private suspend fun handlePacket(packet: GamePacket) {
        val handler = when (packet) {
            is UpdatePlayerPacket -> UpdatePlayerHandler(packet)
            is RequestAvailableShipsPacket -> RequestAvailableShipsHandler(packet)
            is AddShipToForcePacket -> AddShipHandler(packet)
            is RemoveUnitPacket -> RemoveUnitHandler(packet)
            is SetBoardPacket -> SetBoardHandler(packet)
            is SetWeatherPacket -> SetWeatherHandler(packet)
            else -> {
                LoggerFactory.getLogger("server").debug(""""Could not find correct handler for ${packet.debugString()} from player
                    ${game.getPlayer(packet.clientId)}""".trimMargin())
                null
            }
        }
        if (handler != null) {
            handler.process(game)
            handler.packetsToSend().forEach { p -> send(p) }
        }
    }
}


