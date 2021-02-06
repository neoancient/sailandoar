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

import kotlin.math.roundToInt

/**
 * Represents angle between ship facing and wind direction
 */
enum class PointOfSail {
    RUNNING,
    REACHING,
    BEATING,
    INTO_WIND;

    fun sailSpeed(baseSpeed: Int, rigging: RiggingType) =
        if (baseSpeed == 0) 0
        else when (this) {
            REACHING -> baseSpeed
            RUNNING -> (baseSpeed *
                if (rigging == RiggingType.JUNK) 0.75
                else 0.67).roundToInt().coerceAtLeast(1)
            BEATING -> (baseSpeed *
                    if (rigging == RiggingType.FORE_AND_AFT) 0.33
                    else 0.25).roundToInt().coerceAtLeast(1)
            INTO_WIND -> 0
        }

    fun rowingSpeed(baseSpeed: Int) =
        if (baseSpeed == 0) 0
        else when (this) {
            RUNNING -> baseSpeed + 1
            INTO_WIND -> baseSpeed - 1
            else -> baseSpeed
        }
}