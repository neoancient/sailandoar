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

package net

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

@ExperimentalSerializationApi
class NetworkConnection(override var id: Int,
                        private val socket: Socket,
                        private val receiver: PacketReceiver): Connection, Runnable {

    private val outputStream = ObjectOutputStream(socket.getOutputStream())
    private val inputStream = ObjectInputStream(socket.getInputStream())
    private val logger = LoggerFactory.getLogger(javaClass)
    private val shutdown = false

    override fun send(packet: Packet) {
        try {
            val bytes = ProtoBuf.encodeToByteArray(packet)
            outputStream.write(bytes)
            outputStream.flush()
            logger.debug("Sending ${packet.debugString()}")
        } catch (ex: IOException) {
            logger.error("While sending ${packet.debugString()}", ex)
        }
    }

    override fun run() {
        while (!shutdown) {
            try {
                val bytes = inputStream.readBytes()
                val packet: Packet = ProtoBuf.decodeFromByteArray(bytes)
                receiver.receivePacket(packet)
                logger.debug("Received ${packet.debugString()}")
            } catch (ex: IOException) {
                logger.error("While reading packet from clientId $id", ex)
            }
        }
        socket.close()
    }
}