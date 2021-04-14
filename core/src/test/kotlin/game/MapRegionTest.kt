package game

import board.Board
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MapRegionTest {
    @Test
    fun testNorthRegion() {
        val board = Board(12, 10)
        val region = MapRegion.NORTH

        val evenColIn = board.createCoordsFromOffset(3, 2)
        val evenColOut = board.createCoordsFromOffset(3, 3)
        val oddColIn = board.createCoordsFromOffset(6, 2)
        val oddColOut = board.createCoordsFromOffset(6, 3)

        assertAll(
            { assertTrue(region.hexInRegion(evenColIn, board, 3)) },
            { assertTrue(region.hexInRegion(oddColIn, board, 3)) },
            { assertFalse(region.hexInRegion(evenColOut, board, 3)) },
            { assertFalse(region.hexInRegion(oddColOut, board, 3)) },
        )
    }

    @Test
    fun testSouthRegion() {
        val board = Board(12, 10)
        val region = MapRegion.SOUTH

        val evenColIn = board.createCoordsFromOffset(3, 7)
        val evenColOut = board.createCoordsFromOffset(3, 6)
        val oddColIn = board.createCoordsFromOffset(6, 7)
        val oddColOut = board.createCoordsFromOffset(6, 6)

        assertAll(
            { assertTrue(region.hexInRegion(evenColIn, board, 3)) },
            { assertTrue(region.hexInRegion(oddColIn, board, 3)) },
            { assertFalse(region.hexInRegion(evenColOut, board, 3)) },
            { assertFalse(region.hexInRegion(oddColOut, board, 3)) },
        )
    }

    @Test
    fun testWestRegion() {
        val board = Board(12, 10)
        val region = MapRegion.WEST

        val evenRowIn = board.createCoordsFromOffset(2, 3)
        val evenRowOut = board.createCoordsFromOffset(3, 3)
        val oddRowIn = board.createCoordsFromOffset(2, 6)
        val oddRowOut = board.createCoordsFromOffset(3, 6)

        assertAll(
            { assertTrue(region.hexInRegion(evenRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(oddRowIn, board, 3)) },
            { assertFalse(region.hexInRegion(evenRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(oddRowOut, board, 3)) },
        )
    }

    @Test
    fun testEastRegion() {
        val board = Board(12, 10)
        val region = MapRegion.EAST

        val evenRowIn = board.createCoordsFromOffset(9, 3)
        val evenRowOut = board.createCoordsFromOffset(8, 3)
        val oddRowIn = board.createCoordsFromOffset(9, 6)
        val oddRowOut = board.createCoordsFromOffset(8, 6)

        assertAll(
            { assertTrue(region.hexInRegion(evenRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(oddRowIn, board, 3)) },
            { assertFalse(region.hexInRegion(evenRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(oddRowOut, board, 3)) },
        )
    }

    @Test
    fun testNorthEastRegion() {
        val board = Board(12, 10)
        val region = MapRegion.NORTHEAST

        val topRowIn = board.createCoordsFromOffset(6, 2)
        val topRowOut = board.createCoordsFromOffset(5, 2)
        val rightColIn = board.createCoordsFromOffset(9, 4)
        val rightColOut = board.createCoordsFromOffset(9, 5)
        val cornerIn = board.createCoordsFromOffset(9, 2)
        val cornerOut = board.createCoordsFromOffset(8, 3)

        assertAll(
            { assertTrue(region.hexInRegion(topRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(rightColIn, board, 3)) },
            { assertTrue(region.hexInRegion(cornerIn, board, 3)) },
            { assertFalse(region.hexInRegion(topRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(rightColOut, board, 3)) },
            { assertFalse(region.hexInRegion(cornerOut, board, 3)) },
        )
    }

    @Test
    fun testSouthWestRegion() {
        val board = Board(12, 10)
        val region = MapRegion.SOUTHWEST

        val bottomRowIn = board.createCoordsFromOffset(5, 7)
        val bottomRowOut = board.createCoordsFromOffset(6, 7)
        val leftColIn = board.createCoordsFromOffset(2, 5)
        val leftColOut = board.createCoordsFromOffset(2, 4)
        val cornerIn = board.createCoordsFromOffset(2, 7)
        val cornerOut = board.createCoordsFromOffset(3, 6)

        assertAll(
            { assertTrue(region.hexInRegion(bottomRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(leftColIn, board, 3)) },
            { assertTrue(region.hexInRegion(cornerIn, board, 3)) },
            { assertFalse(region.hexInRegion(bottomRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(leftColOut, board, 3)) },
            { assertFalse(region.hexInRegion(cornerOut, board, 3)) },
        )
    }

    @Test
    fun testSouthEastRegion() {
        val board = Board(12, 10)
        val region = MapRegion.SOUTHEAST

        val bottomRowIn = board.createCoordsFromOffset(6, 7)
        val bottomRowOut = board.createCoordsFromOffset(5, 7)
        val rightColIn = board.createCoordsFromOffset(9, 5)
        val rightColOut = board.createCoordsFromOffset(9, 4)
        val cornerIn = board.createCoordsFromOffset(9, 7)
        val cornerOut = board.createCoordsFromOffset(8, 6)

        assertAll(
            { assertTrue(region.hexInRegion(bottomRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(rightColIn, board, 3)) },
            { assertTrue(region.hexInRegion(cornerIn, board, 3)) },
            { assertFalse(region.hexInRegion(bottomRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(rightColOut, board, 3)) },
            { assertFalse(region.hexInRegion(cornerOut, board, 3)) },
        )
    }

    @Test
    fun testNorthWestRegion() {
        val board = Board(12, 10)
        val region = MapRegion.NORTHWEST

        val topRowIn = board.createCoordsFromOffset(5, 2)
        val topRowOut = board.createCoordsFromOffset(6, 2)
        val leftColIn = board.createCoordsFromOffset(2, 4)
        val leftColOut = board.createCoordsFromOffset(2, 5)
        val cornerIn = board.createCoordsFromOffset(2, 2)
        val cornerOut = board.createCoordsFromOffset(3, 3)

        assertAll(
            { assertTrue(region.hexInRegion(topRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(leftColIn, board, 3)) },
            { assertTrue(region.hexInRegion(cornerIn, board, 3)) },
            { assertFalse(region.hexInRegion(topRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(leftColOut, board, 3)) },
            { assertFalse(region.hexInRegion(cornerOut, board, 3)) },
        )
    }

    @Test
    fun testCenterRegion() {
        val board = Board(12, 10)
        val region = MapRegion.CENTER

        val topRowIn = board.createCoordsFromOffset(5, 3)
        val topRowOut = board.createCoordsFromOffset(5, 2)
        val bottomRowIn = board.createCoordsFromOffset(5, 6)
        val bottomRowOut = board.createCoordsFromOffset(5, 7)
        val leftColIn = board.createCoordsFromOffset(3, 5)
        val leftColOut = board.createCoordsFromOffset(2, 5)
        val rightColIn = board.createCoordsFromOffset(8, 5)
        val rightColOut = board.createCoordsFromOffset(9, 5)

        assertAll(
            { assertTrue(region.hexInRegion(topRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(bottomRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(leftColIn, board, 3)) },
            { assertTrue(region.hexInRegion(rightColIn, board, 3)) },
            { assertFalse(region.hexInRegion(topRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(bottomRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(leftColOut, board, 3)) },
            { assertFalse(region.hexInRegion(rightColOut, board, 3)) },
        )
    }

    @Test
    fun testAnyEdgeRegion() {
        val board = Board(12, 10)
        val region = MapRegion.ANY_EDGE

        val topRowIn = board.createCoordsFromOffset(5, 2)
        val topRowOut = board.createCoordsFromOffset(5, 3)
        val bottomRowIn = board.createCoordsFromOffset(5, 7)
        val bottomRowOut = board.createCoordsFromOffset(5, 6)
        val leftColIn = board.createCoordsFromOffset(2, 5)
        val leftColOut = board.createCoordsFromOffset(3, 5)
        val rightColIn = board.createCoordsFromOffset(9, 5)
        val rightColOut = board.createCoordsFromOffset(8, 5)

        assertAll(
            { assertTrue(region.hexInRegion(topRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(bottomRowIn, board, 3)) },
            { assertTrue(region.hexInRegion(leftColIn, board, 3)) },
            { assertTrue(region.hexInRegion(rightColIn, board, 3)) },
            { assertFalse(region.hexInRegion(topRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(bottomRowOut, board, 3)) },
            { assertFalse(region.hexInRegion(leftColOut, board, 3)) },
            { assertFalse(region.hexInRegion(rightColOut, board, 3)) },
        )
    }
}