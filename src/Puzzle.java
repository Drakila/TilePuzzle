import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * Manages the game state of a sliding puzzle
 * The sliding puzzle consists of a 4 x 4 playing board.
 * Pieces can be moved into an empty space adjacent to them.
 * The puzzle can be sorted or randomized.
 */
public class Puzzle {
    Dimension boardSize;    //playing field size measured in tiles
    int[][] tileLocations;  //represents which tile is located in which position (indices == x,y position, value == tile number, highest tile nr == empty spot)
    Point emptyTile; //keeps a record of where the empty tile is, so we don't have to search through tileLocations every turn

    public Puzzle(Dimension boardSize) {
        //TODO: fix non-4x4 square puzzles not working (render and turns)
        //TODO: fix non-square puzzles not working (render and turns)
        this.boardSize = boardSize;
        initialize();
        /* Puzzle puzzle = new Puzzle();
        puzzle.loadImage();
        puzzle.displayGUI(new Dimension(500,500));*/
    }

    private void initialize(){
        //initialize tile positions, sorted
        //TODO: initialize with a random order. How to generate integers 0-x without repeats?
        tileLocations = new int[boardSize.width][boardSize.height];
        for (int i = 0; i < (boardSize.width * boardSize.height); i++) {
            tileLocations[i/ boardSize.width][i% boardSize.height] = i;
        }
        emptyTile = new Point(boardSize.width-1, boardSize.height-1);


    }

    protected void sortBoard(){
        //TODO: implement sorting

    }

    protected void randomizeBoard(){
        ArrayList<Integer> numberRange = new ArrayList<Integer>(boardSize.width* boardSize.height);
        for (int i = 0; i < boardSize.width* boardSize.height; i++) {
            numberRange.add(i);
        }
        Collections.shuffle(numberRange);

        for (int i = 0; i < (boardSize.width * boardSize.height); i++) {
            tileLocations[i/ boardSize.width][i% boardSize.height] = numberRange.get(i);
        }

        //update empty tile position
        int index = numberRange.indexOf( (boardSize.width* boardSize.height)-1);
        emptyTile.setLocation(index/boardSize.width, index%boardSize.width);


    }

    protected void moveTile(Point tileLocation){
        int distance = Math.abs(tileLocation.x - emptyTile.x) + (Math.abs(tileLocation.y - emptyTile.y));
        if (1 == distance){
            // tile is adjacent to the empty tile
            //swap with empty tile
            tileLocations[emptyTile.x][emptyTile.y] = tileLocations[tileLocation.x][tileLocation.y];
            emptyTile.setLocation(tileLocation.x, tileLocation.y);
            tileLocations[tileLocation.x][tileLocation.y] = (boardSize.height * boardSize.width) -1;
        }

    }

    /**
     * Returns the order in which to draw the tiles assuming a wrapping grid with the same dimensions as boardSize
     * */
    protected int[] getTileDrawOrder(){
        int[] result = new int[boardSize.width * boardSize.height];
        for (int i = 0; i < boardSize.width; i++) {
            for (int j = 0; j < boardSize.height; j++) {
                result[(i * boardSize.width) + j] = tileLocations[i][j] ;
            }

        }
        return result;
    }

    /**
     * Returns the x,y indices (=coordinates) of the tile.
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