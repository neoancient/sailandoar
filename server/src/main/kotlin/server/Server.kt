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

import game.Game
import net.*
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


/**
 * Manages client connections and delegates client requests to appropriate handlers
 */

class Server: PacketReceiver, Runnable {
    internal val game = Game()
    private val connections = HashMap<Int, Connection>()
    private val queue  = LinkedBlockingQueue<Packet>()
    // All pending connections have id < 0 to distinguish them from active
    private val pendingConnectionId = AtomicInteger(-1)
    private val pendingConnections = HashMap<Int, ConnectionNegotiator>()
    internal val threadPool = Executors.newCachedThreadPool()
    private val logger = LoggerFactory.getLogger(javaClass)

    private var shutdown = false
    private var socketListener: ServerSocketListener? = null

    fun listen(port: Int) {
        val socketListener = ServerSocketListener(this, port)
        threadPool.execute(socketListener)
    }

    internal fun requestConnection(connection: Connection) {
        val negotiator = ConnectionNegotiator(this, connection)
        connection.id = pendingConnectionId.getAndDecrement()
        pendingConnections[connection.id] = negotiator
        threadPool.execute(negotiator)
    }

    internal fun addConnection(connection: Connection) {
        val player = requireNotNull(game.getPlayer(connection.id))
        connections.forEach {
            if (it.key >= 0) {
                it.value.send(AddPlayerPacket(it.key, player))
            }
        }
        connections[connection.id] = connection
        logger.info("Player ${player.name} joined with id ${player.id}")
    }

    override fun receivePacket(packet: Packet) {
        queue.add(packet)
    }

    fun send(packet: Packet) {
        connections[packet.clientId]?.send(packet)
    }

    internal fun handle(packet: Packet) {
        val handler = when (packet) {
            is RequestAvailableShipsPacket -> RequestAvailableShipsHandler(packet)
            else -> null
        }
        if (handler != null) {
            handler.process()
            handler.packetsToSend().forEach(this::send)
        } else {
            logger.debug(""""Could not find correct handler for ${packet.debugString()} from player
                ${game.getPlayer(packet.clientId)}""".trimMargin())
        }
    }

    fun start() {
        threadPool.execute(this)
    }

    fun shutdown() {
        socketListener?.close()
        shutdown = true
        threadPool.shutdown()
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow()
            }
        } catch (e: InterruptedException) {
            //Re-cancel and preserve interrupt status
            threadPool.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }

    override fun run() {
        logger.info("Starting server")
        while (!shutdown) {
            try {
                val packet = queue.take()
                if (packet.clientId >= 0) {
                    handle(packet)
                } else {
                    pendingConnections[packet.clientId]?.receivePacket(packet)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }
}