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
import org.slf4j.LoggerFactory

class Server(address: String, serverPort: Int) : ServerConnector {
    private val game = Game()
    private val server = NetworkServer(address, serverPort, this)

    fun start() {
        server.start(false)
    }

    fun shutdown() {
        server.shutdown(1000, 1000)
    }

    override suspend fun handle(data: String) {
        handlePacket(Json.decodeFromString(Packet.serializer(), data))
    }

    private suspend fun handlePacket(packet: Packet) {
        val handler = when (packet) {
            is RequestAvailableShipsPacket -> RequestAvailableShipsHandler(packet)
            else -> null
        }
        if (handler != null) {
            handler.process()
            handler.packetsToSend().forEach { p ->
                server.send(p.clientId, Json.encodeToString(Packet.serializer(), packet))
            }
        } else {
            LoggerFactory.getLogger("server").debug(""""Could not find correct handler for ${packet.debugString()} from player
                ${game.getPlayer(packet.clientId)}""".trimMargin())
        }
    }
}


