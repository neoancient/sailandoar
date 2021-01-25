import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.DefaultClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

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

public interface ClientConnector {
    public suspend fun handle(data: String)
    public suspend fun nameConflict(suggestion: String, taken: Set<String>)
    public suspend fun connectionEstablished(clientId: Int)
}

public class NetworkClient(private var name: String, private val handler: ClientConnector) {
    private val queue = Channel<Packet>()
    private val logger = LoggerFactory.getLogger(javaClass)

    @KtorExperimentalAPI
    private val client = HttpClient {
        install(WebSockets)
    }

    @KtorExperimentalAPI
    public suspend fun start(host: String, port: Int) {
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
    public fun stop() {
        client.close()
    }

    private suspend fun DefaultClientWebSocketSession.sendPackets() {
        for (packet in queue) {
            send(Json.encodeToString(Packet.serializer(), packet))
        }
    }

    private suspend fun DefaultClientWebSocketSession.receivePackets() {
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                handlePacket(Json.decodeFromString(Packet.serializer(), frame.readText()))
            }
        } catch (e: Exception) {
            logger.error(e.localizedMessage)
        }
    }

    private suspend fun handlePacket(packet: Packet) {
        when (packet) {
            is RequestNamePacket -> queue.send(SendNamePacket(name))
            is SuggestNamePacket -> handler.nameConflict(packet.name, packet.taken)
            is InitClientPacket -> handler.connectionEstablished(packet.clientId)
        }
    }

    public fun send(data: String) {
        GlobalScope.launch(Dispatchers.IO) {
            queue.send(TextPacket(data))
        }
    }

    public suspend fun changeName(name: String) {
        this.name = name
        queue.send(SendNamePacket(name))
    }
}
