package board

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

/**
 *
 */
internal class AxialCoordsTest {

    @Test
    fun sameColumnForSameXVertical() {
        val coords1 = AxialCoords.createFromOffset(3, 3, vertical = true, offsetOdd = true)
        val coords2 = AxialCoords.createFromOffset(3, 5, vertical = true, offsetOdd = true)
        assertEquals(coords1.col, coords2.col)
    }

    @Test
    fun sameRowForSameYHorizontal() {
        val coords1 = AxialCoords.createFromOffset(3, 3, vertical = false, offsetOdd = true)
        val coords2 = AxialCoords.createFromOffset(5, 3, vertical = false, offsetOdd = true)
        assertEquals(coords1.row, coords2.row)
    }

    @Test
    fun testOddColumnOffset() {
        val evenColumn = AxialCoords.createFromOffset(0, 0, vertical = true, offsetOdd = true)
        val oddColumn = AxialCoords.createFromOffset(1, 0, vertical = true, offsetOdd = true)
        assertTrue(evenColumn.cartesianY() < oddColumn.cartesianY())
    }

    @Test
    fun testEvenColumnOffset() {
        val evenColumn = AxialCoords.createFromOffset(0, 0, vertical = true, offsetOdd = false)
        val oddColumn = AxialCoords.createFromOffset(1, 0, vertical = true, offsetOdd = false)
        assertTrue(evenColumn.cartesianY() > oddColumn.cartesianY())
    }

    @Test
    fun testOddRowOffset() {
        val evenRow = AxialCoords.createFromOffset(0, 0, vertical = false, offsetOdd = true)
        val oddRow = AxialCoords.createFromOffset(0, 1, vertical = false, offsetOdd = true)
        assertTrue(evenRow.cartesianX() < oddRow.cartesianX())
    }

    @Test
    fun testEvenRowOffset() {
        val evenRow = AxialCoords.createFromOffset(0, 0, vertical = false, offsetOdd = false)
        val oddRow = AxialCoords.createFromOffset(0, 1, vertical = false, offsetOdd = false)
        assertTrue(evenRow.cartesianX() > oddRow.cartesianX())
    }


    @Test
    fun distanceToSameHexIsZero() {
        val coords = AxialCoords(8, 8, true)
        assertEquals(coords.distance(coords), 0)
    }

    @Test
    fun distanceInSameColumnVerticalIsDifferenceInY() {
        val coords1 = AxialCoords.createFromOffset(8, 8, vertical = true, offsetOdd = true)
        val coords2 = AxialCoords.createFromOffset(8, 6, vertical = true, offsetOdd = true)
        assertAll(
                Executable { assertEquals(coords1.distance(coords2), 2) },
                Executable { assertEquals(coords2.distance(coords1), 2) }
        )
    }

    @Test
    fun distanceInSameRowHorizontalIsDifferenceInX() {
        val coords1 = AxialCoords.createFromOffset(8, 8, vertical = false, offsetOdd = true)
        val coords2 = AxialCoords.createFromOffset(6, 8, vertical = false, offsetOdd = true)
        assertAll(
                Executable { assertEquals(coords1.distance(coords2), 2) },
                Executable { assertEquals(coords2.distance(coords1), 2) }
        )
    }

    @Test
    fun testDistanceNonStraightLine() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(coords1)
                .translate(1)
                .translate(1)
                .translate(0)
        assertAll(
                Executable { assertEquals(coords1.distance(coords2), 3) },
                Executable { assertEquals(coords2.distance(coords1), 3) }
        )
    }

    @Test
    fun directionFromSameHexIsZeroDegreesWithVerticalOrientation() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(8, 8, true)
        assertEquals(coords1.degreesTo(coords2), 0)
    }

    @Test
    fun directionFromSameHexIsZeroDegreesWithHorizontalOrientation() {
        val coords1 = AxialCoords(8, 8, false)
        val coords2 = AxialCoords(8, 8, false)
        assertEquals(coords1.degreesTo(coords2), 0)
    }

    @Test
    fun directionToHexAboveIsZeroDegreesWithVerticalOrientation() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(coords1)
        coords2.translate(V_DIR_N)
        coords2.translate(V_DIR_N)
        assertEquals(coords1.degreesTo(coords2), 0)
    }

    @Test
    fun directionToHexAboveIsZeroDegreesWithHorizontalOrientation() {
        val coords1 = AxialCoords(8, 8, false)
        val coords2 = AxialCoords(coords1)
        coords2.translate(H_DIR_NE)
        coords2.translate(H_DIR_NW)
        assertEquals(coords1.degreesTo(coords2), 0)
    }

    @Test
    fun degreesToHexBelowIs180DegreesWithVerticalOrientation() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(coords1)
                .translate(V_DIR_S)
                .translate(V_DIR_S)
        assertEquals(coords1.degreesTo(coords2), 180)
    }

    @Test
    fun degreesToHexBelowIs180DegreesWithHorizontalOrientation() {
        val coords1 = AxialCoords(8, 8, false)
        val coords2 = AxialCoords(coords1)
                .translate(H_DIR_SE)
                .translate(H_DIR_SW)
        assertEquals(coords1.degreesTo(coords2), 180)
    }

    @Test
    fun degreesToHexToRightIs90DegreesWithVerticalOrientation() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(coords1)
                .translate(V_DIR_NE)
                .translate(V_DIR_SE)
        assertEquals(coords1.degreesTo(coords2), 90)
    }

    @Test
    fun degreesToHexToRightIs90DegreesWithHorizontalOrientation() {
        val coords1 = AxialCoords(8, 8, false)
        val coords2 = AxialCoords(coords1)
                .translate(H_DIR_E)
                .translate(H_DIR_E)
        assertEquals(coords1.degreesTo(coords2), 90)
    }

    @Test
    fun degreesToHexToLeftIs270DegreesWithVerticalOrientation() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(coords1)
                .translate(V_DIR_NW)
                .translate(V_DIR_SW)
        assertEquals(coords1.degreesTo(coords2), 270)
    }

    @Test
    fun degreesToHexToLeftIs270DegreesWithHorizontalOrientation() {
        val coords1 = AxialCoords(8, 8, false)
        val coords2 = AxialCoords(coords1)
                .translate(H_DIR_W)
                .translate(H_DIR_W)
        assertEquals(coords1.degreesTo(coords2), 270)
    }

    @Test
    fun relativeBearingFromNEToNWIs240() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(coords1).translate(V_DIR_NW)
        assertEquals(coords1.relativeBearingDegrees(V_DIR_NE, coords2), 240)
    }

    @Test
    fun relativeBearingFromNWToNEIs120() {
        val coords1 = AxialCoords(8, 8, true)
        val coords2 = AxialCoords(coords1).translate(V_DIR_NE)
        assertEquals(coords1.relativeBearingDegrees(V_DIR_NW, coords2), 120)
    }

    @Test
    fun rotatePositiveSteps() {
        val center = AxialCoords(8, 8, true)
        val testHex = AxialCoords(center)
                .translate(1)
                .translate(1)
                .rotate(2, center)
        assertEquals(center.degreesTo(testHex), 180)
    }

    @Test
    fun rotatePositiveStepsPast180() {
        val center = AxialCoords(8, 8, true)
        val testHex = AxialCoords(center)
                .translate(1)
                .translate(1)
                .rotate(4, center)
        assertEquals(300, center.degreesTo(testHex))
    }

    @Test
    fun rotateZero() {
        val center = AxialCoords(8, 8, true)
        val testHex = AxialCoords(center)
                .translate(1)
                .translate(1)
                .rotate(0, center)
        assertEquals(center.degreesTo(testHex), 60)
    }

    @Test
    fun rotateMoreThanSixSteps() {
        val center = AxialCoords(8, 8, true)
        val testHex = AxialCoords(center)
                .translate(1)
                .translate(1)
                .rotate(7, center)
        assertEquals(center.degreesTo(testHex), 120)
    }

    @Test
    fun rotateNegativeSteps() {
        val center = AxialCoords(8, 8, true)
        val testHex = AxialCoords(center)
                .translate(1)
                .translate(1)
                .rotate(-2, center)
        assertEquals(center.degreesTo(testHex), 300)
    }

    @Test
    fun rotateMoreThanSixNegativeSteps() {
        val center = AxialCoords(8, 8, true)
        val testHex = AxialCoords(center)
                .translate(1)
                .translate(1)
                .rotate(-9, center)
        assertEquals(center.degreesTo(testHex), 240)
    }

}