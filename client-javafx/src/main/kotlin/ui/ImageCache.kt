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

package ui

import board.TerrainType
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import unit.ShipStats
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

const val SPRITE_WIDTH = 320
const val SPRITE_HEIGHT = 180

class ImageCache {
    companion object {
        private val imageMap = HashMap<UUID, Image>()
        private val spriteMap = HashMap<SpriteKey, Image>()
        private val terrainMap = HashMap<TerrainType, Image>()

        operator fun get(ship: ShipStats): Image? =
            imageMap[ship.id] ?:
            ShipStats::class.java.getResourceAsStream(ship.image)?.let {
                Image(it).apply {
                    imageMap[ship.id] = this
                }
            }

        fun get(ship: ShipStats, windDirection: Int,
                         facing: Int, isometric: Boolean): Image? =
            get(ship, SpriteKey(ship.id, windDirection, facing, isometric))

        fun get(ship: ShipStats, key: SpriteKey): Image? =
            spriteMap[key] ?:
            ShipStats::class.java.getResourceAsStream(ship.spriteImage)?.let {
                fillSpriteRow(Image(it), key)
            }

        private fun fillSpriteRow(atlas: Image, key: SpriteKey): Image? {
            val y = SPRITE_HEIGHT * if (key.isometric) 6 else key.windDirection
            for (facing in 0..5) {
                val x = SPRITE_WIDTH * if (key.isometric) {
                    (6 - key.windDirection + facing) % 6
                } else {
                    facing
                }
                spriteMap[SpriteKey(key.shipId, key.windDirection, facing, key.isometric)] =
                    WritableImage(atlas.pixelReader, x, y, SPRITE_WIDTH, SPRITE_HEIGHT)
            }
            return spriteMap[key]
        }

        fun get(terrain: TerrainType): Image? =
            terrainMap[terrain] ?:
            TerrainType::class.java.getResourceAsStream(terrain.name.toLowerCase() + ".png")?.let {
                Image(it).apply {
                    terrainMap[terrain] = this
                }
            }
    }
}

data class SpriteKey(
    val shipId: UUID,
    val windDirection: Int,
    val facing: Int,
    val isometric: Boolean
)