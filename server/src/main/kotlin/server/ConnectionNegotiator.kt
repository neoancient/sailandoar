/**
 * Sail and Oar
 * Copyright (c) 2020 Carl W Spain
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
 */
package server

import net.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * Negotiates new connection
 */
class ConnectionNegotiator(val server: Server, val connection: Connection): PacketReceiver, Runnable {
    private var finished = false
    private val incomingQueue by lazy { LinkedBlockingQueue<Packet>() }

    override fun run() {
        connection.send(RequestNamePacket(connection.id))
        while (!finished) {
            try {
                val packet = incomingQueue.take()
                handlePacket(packet)
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    override fun receivePacket(packet: Packet) {
        incomingQueue.add(packet)
    }

    fun handlePacket(packet: Packet) {
        when (packet) {
            is SendNamePacket -> {
                val request = packet.name
                val player = server.game.newPlayer(request)
                if (player == null) {
                    connection.send(SuggestNamePacket(-1, server.game.suggestAlternateName(request)))
                } else {
                    connection.id = player.id
                    server.addConnection(connection)
                    connection.send(InitClientPacket(connection.id, server.game))
                }
            }
        }
    }
}