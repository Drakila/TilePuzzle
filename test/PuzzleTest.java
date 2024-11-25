import org.junit.jupiter.api.*;

import java.awt.*;
import java.sql.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class PuzzleTest {
    //TODO: run the tests on Puzzles of different sizes. Minimum size: 3x3.

    Puzzle puzzle;

    @Nested
    class FourByFourTests{
        int[] sortedOrder = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

        @BeforeEach
        void setUp(){
            puzzle = new Puzzle(new Dimension(4,4));
        }

        @Test
        @DisplayName("Empty tile location after sort")
        void sortBoard() {
            //test that emptyTile points to correct location
            puzzle.sortBoard();
            Point emptyLocation = new Point(3, 3);
            assertEquals(puzzle.emptyTile, emptyLocation);
        }

        @Test
        @DisplayName("Board sorted after sort")
        void sortBoardSorted(){
            puzzle.sortBoard();

            assertArrayEquals(new int[]{0, 4, 8, 12}, puzzle.tileLocations[0]);
            assertArrayEquals(new int[]{1, 5, 9, 13}, puzzle.tileLocations[1]);
            assertArrayEquals(new int[]{2, 6, 10, 14}, puzzle.tileLocations[2]);
            assertArrayEquals(new int[]{3, 7, 11, 15}, puzzle.tileLocations[3]);
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
        @DisplayName("Correct Tile moves")
        void moveTileFive(){

        }

        /*
        getTileDrawOrder Tests
        check:
        - correct order after initialization
        - correct length
        - randomize, then check order?
         */
        @Test
        @DisplayName("Draw Order length")
        void drawOrderOne() {
            int[] order = puzzle.getTileDrawOrder();
            assertEquals( puzzle.tileAmount, order.length);
        }

        @Test
        @DisplayName("Draw Order initial")
        void drawOrderTwo() {
            int[] order = puzzle.getTileDrawOrder();
            int[] expectedOrder = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
            assertArrayEquals(expectedOrder, order);
        }


        @Test
        @Disabled
        @DisplayName("Draw Order not inital")
            //to catch a potential case where getTileDrawOrder always returns the initial, sorted array
        void drawOrderThree() {
            //don't use randomness, since that would necessitate logic akin to the production logic to compute the expected order
            assumeTrue( puzzle.moveTile(new Point(3,2)));    //switches tiles 15 and 11
            int[] order = puzzle.getTileDrawOrder();
            int[] expectedOrder = {0,1,2,3,4,5,6,7,8,9,10,14,12,13,11,15};
            assertArrayEquals(expectedOrder, order);
        }

        /*
        getTileNyNumber tests
        check:
        -
         */
        @Test
        void getTileByNumber() {
        }
    }


}