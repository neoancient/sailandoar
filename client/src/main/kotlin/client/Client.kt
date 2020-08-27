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
package client

import game.Game
import game.Player
import net.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue

class Client: PacketReceiver {
    var id = -1
    var player = Player(id, "New Player")
    var game: Game? = null
    var connection: Connection? = null

    private var shutdown = false
    private val queue: BlockingQueue<Packet> = LinkedBlockingQueue()
    private val listeners: MutableList<ConnectionListener> = CopyOnWriteArrayList()

    override fun receivePacket(packet: Packet) {
        queue.add(packet)
    }

    private fun handle(packet: Packet) {
        when (packet) {
            is RequestNamePacket -> connection?.send(SendNamePacket(id, player.name))
            is InitClientPacket -> {
                id = packet.clientId
                game = requireNotNull(packet.game)
                player = requireNotNull(game?.getPlayer(id))
                listeners.forEach{ it.clientConnected(this) }
            }
            is AddPlayerPacket -> game?.addPlayer(packet.player)
            is RemovePlayerPacket -> game?.removePlayer(packet.player.id)
        }
    }

    fun addConnectionListener(l: ConnectionListener) {
        listeners.add(l)
    }

    fun removeConnectionListener(l: ConnectionListener) {
        listeners.remove(l)
    }
}