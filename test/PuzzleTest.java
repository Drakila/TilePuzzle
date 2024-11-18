import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

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
    void moveTile() {
    }

    @Test
    void getTileDrawOrder() {
    }

    @Test
    void getTileByNumber() {
    }
}