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

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Takes the output frames of a rendered animation and joins them into an image atlas
 */
fun main() {
    val dir = File("images")
    dir.listFiles()?.let {
        for (file in it) {
            if (file.isDirectory) {
                val atlas = processDirectory(file)
                ImageIO.write(atlas, "png", File(dir, "${file.name}_atlas.png"))
            }
        }
    }
}

const val WIDTH = 320
const val HEIGHT = 180

/**
 * Frame indices; the ones place is the orientation of the ship and the tens
 * place is the point of sail.
 * Ship orientation is toward the bow on 0, proceeding around in 60 degree increments
 * starting to port for 1-5, and directly above with the ship point to the right for 6.
 * Point of sail is running on 10, with the wind direction moving around anti-clockwise
 * every 20 frames. The extra frames are for the wind animation to fill the sails.
 */
val indices = arrayOf(
    arrayOf("013", "034", "055", "070", "091", "112"),
    arrayOf("113", "014", "035", "050", "071", "092"),
    arrayOf("093", "114", "015", "030", "051", "072"),
    arrayOf("073", "094", "115", "010", "031", "052"),
    arrayOf("053", "074", "095", "110", "011", "032"),
    arrayOf("033", "054", "075", "090", "031", "012"),
    arrayOf("016", "116", "096", "076", "056", "036"),
)

fun processDirectory(dir: File): BufferedImage {
    val atlas = BufferedImage(WIDTH * 6, HEIGHT * 7, BufferedImage.TYPE_INT_ARGB)
    for (row in indices.withIndex()) {
        for (col in row.value.withIndex()) {
            val file = File(dir, "0${col.value}.png")
            if (file.exists()) {
                val image = ImageIO.read(file).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT)
                atlas.graphics.drawImage(image, WIDTH * col.index, HEIGHT * row.index, null)
            } else {
                System.err.println("Could not find file 0${col.value}.png")
            }
        }
    }
    return atlas
}
