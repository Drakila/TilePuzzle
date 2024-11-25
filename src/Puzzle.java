import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * Manages the game state of a sliding puzzle
 * The sliding puzzle consists of a 4 x 4 playing board.
 * Pieces can be moved into an empty space adjacent to them.
 * The puzzle can be sorted or randomized.
 */
public class Puzzle {
    Dimension boardSize;    //playing field size measured in tiles
    int tileAmount; //amount of total tiles on the board, including the empty one. boardSize.height * boardSize.width.
    int[][] tileLocations;  //represents which tile is located in which position (indices == x,y position, value == tile number, highest tile nr == empty spot)
    Point emptyTile; //keeps a record of where the empty tile is, so we don't have to search through tileLocations every turn

    /**
     * Constructor for Puzzle.
     * Generates the board in a sorted state.
     * @param boardSize board dimension
     */
    public Puzzle(Dimension boardSize) {
        //TODO: fix non-4x4 square puzzles not working (render and turns)
        //TODO: fix non-square puzzles not working (render and turns)
        this.boardSize = boardSize;
        this.tileAmount = boardSize.height * boardSize.width;

        tileLocations = new int[boardSize.width][boardSize.height];
        emptyTile = new Point(boardSize.width-1, boardSize.height-1);
        sortBoard();
    }

    /**
     * Sorts all tiles on the game board.
     * Places the empty tile in the bottom-right corner.
     */
    protected void sortBoard(){
        for (int i = 0; i < (tileAmount); i++) {
            tileLocations[i% boardSize.width][i/ boardSize.height] = i;
        }
        emptyTile.setLocation(boardSize.width-1, boardSize.height-1);

    }

    /**
     * Shuffle the game tiles so that the resulting state is solvable.
     * Executes tileAmount * shuffleStrength number of random valid moves.
     * @param shuffleStrength Scales the amount of steps taken to shuffle.
     */
    protected void randomizeBoard(int shuffleStrength){
        int shuffleSteps = tileAmount * shuffleStrength;

        //generate a sequence of move directions
        ArrayList<Integer> movement = new ArrayList<>(shuffleSteps); //values 0-3 encode direction to move the empty tile (0:up, 1:right, 2:down, 3:left)
        for (int i = 0; i < shuffleSteps; i++) {
            movement.add(i % 4);
        }
        Collections.shuffle(movement);

        //execute generated movement
        for (int i : movement){
            Point moveTarget = new Point(emptyTile);
            //compute target tile from movement direction
            //there has got to be a more efficient way to do this with some math.
            switch (i){
                case 0:
                    moveTarget.y = moveTarget.y -1;
                    break;
                case 1:
                    moveTarget.x = moveTarget.x +1;
                    break;
                case 2:
                    moveTarget.y = moveTarget.y +1;
                    break;
                case 3:
                    moveTarget.x = moveTarget.x -1;
                    break;

            }
            moveTile(moveTarget);
        }
    }

    /**
     * Move the targeted tile into the empty spot if it is adjacent to it.
     * @param tileLocation Location of the tile attempting a move
     * @return true if move was legal, false if move was illegal
     */
    protected boolean moveTile(Point tileLocation){
        //verify that tile location is a valid coordinate
        if (tileLocation.x <0 || tileLocation.x >= boardSize.width || tileLocation.y < 0 || tileLocation.y >= boardSize.height){
            return false;
        }

        int distance = Math.abs(tileLocation.x - emptyTile.x) + (Math.abs(tileLocation.y - emptyTile.y));

        if (1 == distance){
            // tile is adjacent to the empty tile
            //swap with empty tile
            tileLocations[emptyTile.x][emptyTile.y] = tileLocations[tileLocation.x][tileLocation.y];
            emptyTile.setLocation(tileLocation.x, tileLocation.y);
            tileLocations[tileLocation.x][tileLocation.y] = (tileAmount) -1;

            return true;
        }
        return false;
    }

    /**
     * Returns the order in which to draw the tiles assuming a wrapping grid with the same dimensions as boardSize.
     * Each entry represents the number of the tile to be drawn.
     * */
    protected int[] getTileDrawOrder(){
        int[] result = new int[tileAmount];

        for (int i = 0; i < boardSize.height; i++) {
            for (int j = 0; j < boardSize.width; j++) {
                result[j+(i* boardSize.width)] = tileLocations[j][i];
            }
        }
        return result;
    }

    /**
     * Returns the x,y indices (=coordinates) of the tile.
     * @param tileNumber number of the tile to be found
     * */
    protected Point getTileByNumber(int tileNumber){
        Point result = new Point();

        for (int i = 0; i < boardSize.width; i++) {
            for (int j = 0; j < boardSize.height; j++) {
                if (tileLocations[i][j] == tileNumber){
                    result.setLocation(i,j);
                    break;
                }
            }
        }

        return result;
    }
}