import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
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
        this.boardSize = boardSize;
        initialize();
        /* Puzzle puzzle = new Puzzle();
        puzzle.loadImage();
        puzzle.displayGUI(new Dimension(500,500));*/
    }

    private void initialize(){
        //initialize tile positions, sorted
        tileLocations = new int[boardSize.width][boardSize.height];
        for (int i = 0; i < 16; i++) {
            tileLocations[i/ boardSize.width][i% boardSize.height] = i;
        }
        emptyTile = new Point(boardSize.width-1, boardSize.height-1);
    }

    private void sortBoard(){
        //TODO: implement sorting

    }

    private void randomizeBoard(){
        //TODO: implement randomization

    }

    protected void moveTile(Point tileLocation){
        int distance = Math.abs(tileLocation.x - emptyTile.x) + (Math.abs(tileLocation.y - emptyTile.y));
        if (1 == distance){
            // tile is adjacent to the empty tile
            //swap with empty tile
            tileLocations[emptyTile.x][emptyTile.y] = tileLocations[tileLocation.x][tileLocation.y];
            emptyTile.setLocation(tileLocation.x, tileLocation.y);
            tileLocations[tileLocation.x][tileLocation.y] = 15;
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
}