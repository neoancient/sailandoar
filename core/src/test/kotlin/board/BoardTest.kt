package board

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*


/**
 *
 */
internal class BoardTest {

    @Test
    fun defaultHexFactoryGeneratesSeaHex() {
        val board = Board(10, 10)
        val coords = board.createCoords(5, 5)
        val (terrain, depth, elevation) = board.getHex(coords)
        assertAll(
                { assertEquals(terrain, TerrainType.SEA) },
                { assertTrue(depth > 0) },
                { assertEquals(elevation, 0) })
    }

    @Test
    fun testHexFactory() {
        val testBoard = Board(10, 10,
                defaultHex = HexData(TerrainType.ROCKS, 0, 2))
        val (terrain, depth, elevation) = testBoard.getHex(4, 4)
        assertAll(
                { assertEquals(terrain, TerrainType.ROCKS) },
                { assertEquals(depth, 0) },
                { assertEquals(elevation, 2) })
    }

    @Test
    fun testTerrainInitialization() {
        val initMap: MutableMap<HexCoords, HexData> = HashMap()
        initMap[AxialCoords(1, 1, true)] = HexData(TerrainType.ROCKS, 0, 4)
        initMap[AxialCoords(1, 2, true)] = HexData(TerrainType.REEF, 2, 0)
        val testBoard = Board(10, 10, initHexes = initMap,
                defaultHex = HexData(TerrainType.SEA, DEPTH_DEEP_SEA, 0))
        assertAll(
                { assertEquals(testBoard.getHex(1, 1).terrain, TerrainType.ROCKS) },
                { assertEquals(testBoard.getHex(1, 2).terrain, TerrainType.REEF) },
                { assertEquals(testBoard.getHex(4, 4).terrain, TerrainType.SEA) })
    }

    @Test
    fun verticalGridHasSameOffsetXForSameColumn() {
        val board = Board(10, 10)
        val coords1 = board.createCoords(4, 4)
        val coords2 = board.createCoords(4, 6)
        assertEquals(board.getOffsetCoordX(coords1), board.getOffsetCoordX(coords2))
    }

    @Test
    fun testVerticalGridOffsetYOddOffset() {
        val board = Board(10, 10)
        val coords1 = board.createCoords(4, 4)
        val coords2 = coords1.adjacent(V_DIR_SE)
        assertEquals(board.getOffsetCoordY(coords1), board.getOffsetCoordY(coords2))
    }

    @Test
    fun testVerticalGridOffsetYEvenOffset() {
        val board = Board(10, 10, oddOffset = false)
        val coords1 = board.createCoords(4, 4)
        val coords2 = coords1.adjacent(V_DIR_NE)
        assertEquals(board.getOffsetCoordY(coords1), board.getOffsetCoordY(coords2))
    }

    @Test
    fun horizontalGridHasSameOffsetYForSameRow() {
        val board = Board(10, 10, verticalGrid = false)
        val coords1 = board.createCoords(4, 4)
        val coords2 = board.createCoords(6, 4)
        assertEquals(board.getOffsetCoordY(coords1), board.getOffsetCoordY(coords2))
    }

    @Test
    fun testHorizontalGridOffsetXOddOffset() {
        val board = Board(10, 10, verticalGrid = false)
        val coords1 = board.createCoords(4, 4)
        val coords2 = coords1.adjacent(H_DIR_NE)
        assertEquals(board.getOffsetCoordX(coords1), board.getOffsetCoordX(coords2))
    }

    @Test
    fun testHorizontalGridOffsetXEvenOffset() {
        val board = Board(10, 10, false, false)
        val coords1 = board.createCoords(4, 4)
        val coords2 = coords1.adjacent(H_DIR_NW)
        assertEquals(board.getOffsetCoordX(coords1), board.getOffsetCoordX(coords2))
    }

    @Test
    fun verticalGridCartesianColumnWidth() {
        val board = Board(10, 10)
        val coords1 = board.createCoords(2, 2)
        val coords2 = coords1.adjacent(V_DIR_SE)
        assertEquals(board.getCartesianX(coords1) + Math.sqrt(3.0) / 2.0,
                board.getCartesianX(coords2), 0.001)
    }

    @Test
    fun verticalGridCartesianRowHeight() {
        val board = Board(10, 10)
        val coords1 = board.createCoords(2, 2)
        val coords2 = coords1.adjacent(V_DIR_SE)
        assertEquals(board.getCartesianY(coords1) + 0.5,
                board.getCartesianY(coords2), 0.001)
    }

    @Test
    fun horizontalGridCartesianColumnWidth() {
        val board = Board(10, 10, verticalGrid =false)
        val coords1 = board.createCoords(2, 2)
        val coords2 = coords1.adjacent(H_DIR_SE)
        assertEquals(board.getCartesianX(coords1) + 0.5,
                board.getCartesianX(coords2), 0.001)
    }

    @Test
    fun horizonalGridCartesianRowHeight() {
        val board = Board(10, 10, verticalGrid = false)
        val coords1 = board.createCoords(2, 2)
        val coords2 = coords1.adjacent(H_DIR_SE)
        assertEquals(board.getCartesianY(coords1) + Math.sqrt(3.0) / 2.0,
                board.getCartesianY(coords2), 0.001)
    }
}