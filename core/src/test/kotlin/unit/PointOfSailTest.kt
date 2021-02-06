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

package unit

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PointOfSailTest {

    @Test
    fun testReachingSpeed() {
        with (PointOfSail.REACHING) {
            assertAll(
                { assertEquals(12, sailSpeed(12, RiggingType.SQUARE)) },
                { assertEquals(12, sailSpeed(12, RiggingType.FORE_AND_AFT)) },
                { assertEquals(12, sailSpeed(12, RiggingType.JUNK)) },
            )
        }
    }

    @Test
    fun testRunningSpeed() {
        with (PointOfSail.RUNNING) {
            assertAll(
                { assertEquals(8, sailSpeed(12, RiggingType.SQUARE)) },
                { assertEquals(8, sailSpeed(12, RiggingType.FORE_AND_AFT)) },
                { assertEquals(9, sailSpeed(12, RiggingType.JUNK)) },
            )
        }
    }

    @Test
    fun testBeatingSpeed() {
        with (PointOfSail.BEATING) {
            assertAll(
                { assertEquals(3, sailSpeed(12, RiggingType.SQUARE)) },
                { assertEquals(4, sailSpeed(12, RiggingType.FORE_AND_AFT)) },
                { assertEquals(3, sailSpeed(12, RiggingType.JUNK)) },
            )
        }
    }

    @Test
    fun testIntoWindSpeed() {
        with (PointOfSail.INTO_WIND) {
            assertAll(
                { assertEquals(0, sailSpeed(12, RiggingType.SQUARE)) },
                { assertEquals(0, sailSpeed(12, RiggingType.FORE_AND_AFT)) },
                { assertEquals(0, sailSpeed(12, RiggingType.JUNK)) },
            )
        }
    }


    @Test
    fun testImmobileReachingSpeed() {
        with (PointOfSail.REACHING) {
            assertAll(
                { assertEquals(0, sailSpeed(0, RiggingType.SQUARE)) },
                { assertEquals(0, sailSpeed(0, RiggingType.FORE_AND_AFT)) },
                { assertEquals(0, sailSpeed(0, RiggingType.JUNK)) },
            )
        }
    }

    @Test
    fun testImmobileRunningSpeed() {
        with (PointOfSail.RUNNING) {
            assertAll(
                { assertEquals(0, sailSpeed(0, RiggingType.SQUARE)) },
                { assertEquals(0, sailSpeed(0, RiggingType.FORE_AND_AFT)) },
                { assertEquals(0, sailSpeed(0, RiggingType.JUNK)) },
            )
        }
    }

    @Test
    fun testImmobileBeatingSpeed() {
        with (PointOfSail.BEATING) {
            assertAll(
                { assertEquals(0, sailSpeed(0, RiggingType.SQUARE)) },
                { assertEquals(0, sailSpeed(0, RiggingType.FORE_AND_AFT)) },
                { assertEquals(0, sailSpeed(0, RiggingType.JUNK)) },
            )
        }
    }

    @Test
    fun testImmobileIntoWindSpeed() {
        with (PointOfSail.INTO_WIND) {
            assertAll(
                { assertEquals(0, sailSpeed(0, RiggingType.SQUARE)) },
                { assertEquals(0, sailSpeed(0, RiggingType.FORE_AND_AFT)) },
                { assertEquals(0, sailSpeed(0, RiggingType.JUNK)) },
            )
        }
    }

    @Test
    fun testRowingSpeed() {
        assertAll(
            { assertEquals(6, PointOfSail.RUNNING.rowingSpeed(5)) },
            { assertEquals(5, PointOfSail.REACHING.rowingSpeed(5)) },
            { assertEquals(5, PointOfSail.BEATING.rowingSpeed(5)) },
            { assertEquals(4, PointOfSail.INTO_WIND.rowingSpeed(5)) },
        )
    }

    @Test
    fun testImmobileRowingSpeed() {
        assertAll(
            { assertEquals(0, PointOfSail.RUNNING.rowingSpeed(0)) },
            { assertEquals(0, PointOfSail.REACHING.rowingSpeed(0)) },
            { assertEquals(0, PointOfSail.BEATING.rowingSpeed(0)) },
            { assertEquals(0, PointOfSail.INTO_WIND.rowingSpeed(0)) },
        )
    }
}