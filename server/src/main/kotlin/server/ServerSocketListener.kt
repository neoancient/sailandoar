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

import kotlinx.serialization.ExperimentalSerializationApi
import net.NetworkConnection
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket

class ServerSocketListener(private val server: Server): Runnable {
    private val socket = ServerSocket()
    private var shutdown = false

    constructor(server: Server, port: Int): this(server) {
        bind(port)
    }

    fun bind(port: Int) {
        val socketAddress = InetSocketAddress(port)
        socket.bind(socketAddress)
    }

    fun close() {
        shutdown = true
    }

    @ExperimentalSerializationApi
    override fun run() {
        while (!shutdown) {
            try {
                val s = socket.accept()
                val conn = NetworkConnection(-1, s, server)
                server.threadPool.execute(conn)
                server.requestConnection(conn)
            } catch (ex: IOException) {
                LoggerFactory.getLogger(javaClass).error("Error while accepting connection", ex)
            }
        }
        socket.close()
    }
}