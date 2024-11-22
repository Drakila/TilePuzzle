import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleTest {
    //TODO: run the tests on Puzzles of different sizes. Minimum size: 2x2.

    Puzzle puzzle;

    @BeforeEach
    void setUp(){
        puzzle = new Puzzle(new Dimension(4,4));
    }


    @Test
    @DisplayName("Empty tile location after sort")
    void sortBoard() {
        //test that emptyTile points to correct location
        puzzle.sortBoard();
        // offset of -1 because the coordinates are 0-indexed and boardSize is not.
        Point emptyLocation = new Point(puzzle.boardSize.width -1, puzzle.boardSize.height -1);
        assertEquals(puzzle.emptyTile, emptyLocation);
    }

    @Test
    @DisplayName("Board sorted after sort")
    void sortBoardSorted(){
        puzzle.sortBoard();
        for (int i = 0; i < puzzle.tileAmount; i++) {
            assertEquals(i, puzzle.tileLocations[i / puzzle.boardSize.width][i % puzzle.boardSize.height]);
        }
    }

    /*
    moveTile
    check:
    - empty tile and moved tile in correct locations after the move
    - non-legal move is not executed
     */
    @Test
    @DisplayName("Empty Tile moves")
    void moveTileOne() {
        //Assumption: empty tile is in bottom-right corner after initialization

        puzzle.moveTile(new Point(2,3));
        assertEquals(puzzle.tileAmount-1, puzzle.tileLocations[2][3]);
        assertEquals(new Point(2,3), puzzle.emptyTile);

    }

    @Test
    @DisplayName("Moved Tile moves")
    void moveTileTwo(){
        int movedTile = puzzle.tileLocations[2][3];
        puzzle.moveTile(new Point(2,3));
        assertEquals(movedTile, puzzle.tileLocations[3][3]);

    }

    @Test
    @DisplayName("Invalid move (too far from empty Tile)")
    void moveTileThree(){
        //make deep copy of the tile Locations
        int[][] stateCopy = Arrays.stream(puzzle.tileLocations).map(int[]::clone).toArray(int[][]::new);

        //attempt invalid move
        puzzle.moveTile(new Point(1,1));

        //verify board state has not changed
        for (int i = 0; i < puzzle.boardSize.width; i++) {
            assertArrayEquals(stateCopy[i], puzzle.tileLocations[i]);
        }
    }

    @Test
    @DisplayName("Invalid move (outside of board)")
    void moveTileFour(){
        //make deep copy of the tile Locations
        int[][] stateCopy = Arrays.stream(puzzle.tileLocations).map(int[]::clone).toArray(int[][]::new);

        //attempt invalid move
        puzzle.moveTile(new Point(puzzle.boardSize.width,3));

        //verify board state has not changed
        for (int i = 0; i < puzzle.boardSize.width; i++) {
            assertArrayEquals(stateCopy[i], puzzle.tileLocations[i]);
        }
    }

    @Test
    void getTileDrawOrder() {
    }

    @Test
    void getTileByNumber() {
    }
}