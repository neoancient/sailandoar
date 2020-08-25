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

package unit

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.util.*
import java.util.concurrent.ConcurrentHashMap

const val DEFAULT_LOC = "ships.json"

class ShipLibrary private constructor() {
    private val ships: MutableMap<UUID, ShipStats> = ConcurrentHashMap()
    private val source: InputStream = ShipLibrary::class.java.getResourceAsStream(DEFAULT_LOC)

    init {
        val data = Json.decodeFromString<List<ShipStats>>(readInputStream())
        data.forEach {
            ships[it.id] = it
        }
    }

    fun allShips(): Collection<ShipStats> = ships.values

    fun getShip(id: UUID): ShipStats? = ships[id]

    private fun readInputStream(): String = source.readBytes().toString(Charsets.UTF_8)

    companion object {
        val instance: ShipLibrary by lazy {
            ShipLibrary()
        }
    }
}