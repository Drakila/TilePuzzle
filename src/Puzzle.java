import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * Manages the game state of a sliding puzzle
 * The sliding puzzle consists of a 4 x 4 playing board.
 * Pieces can be moved into an empty space adjacent to them.
 * The puzzle can be sorted or randomized by clicking on the frame.
 */
//TODO: get the image size into displayGUI somehow. This might be borked because I'm instantiating Puzzle in itself => make a separate Main class?
public class Puzzle {
    /*
    * New plan:
    * Puzzle constructor gets passed path from caller and loads image, checks size
    * should the constructor display the GUI or should the caller have to call Puzzle.displayGUI() for that?
    * -> should the GUI be separate form the game state management? That seems like a good idea!
    * => make new class that acts as the puzzle "frontend" (making a bridge pattern! :D )
    * */

    Dimension boardSize;    //playing field size measured in tiles
    //board state (location of tiles on the board)
    int[][] tileLocations;  //represents which tile is located in which position (indices == position, value == tile number, tile 15 == empty spot)
    Image image; //TODO: move to PuzzleGUI

    public Puzzle(Dimension boardSize) {
        this.boardSize = boardSize;
        initialize();
        /* Puzzle puzzle = new Puzzle();
        puzzle.loadImage();
        puzzle.displayGUI(new Dimension(500,500));*/
    }

    private void initialize(){
        //initialize tile positions, sorted
        tileLocations = new int[boardSize.height][boardSize.width];
        for (int i = 0; i < 16; i++) {
            tileLocations[i/ boardSize.height][i% boardSize.width] = i;
        }
    }


    private void displayGUI(Dimension windowSize){
        JFrame mainWindow = new JFrame("Sliding Tile Puzzle");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(windowSize.width, windowSize.height);
        mainWindow.getContentPane().setBackground(Color.darkGray);
        mainWindow.setLayout(new GridLayout(4,4));


        //add components to window
        /*GamePanel gamePanel = new GamePanel();
        mainWindow.setContentPane(gamePanel);*/
        GameTile[] gameTiles = new GameTile[16];
        for (int i = 0; i < 16; i++) {
            gameTiles[i] = new GameTile(new Point(i/4, i%4), tileLocations[i/4][i%4], image);
        }

        /*GameTile testTile = new GameTile(new Point(0,1), 4, image);
        mainWindow.add(testTile);*/
        for (GameTile gameTile : gameTiles) {
            mainWindow.add(gameTile);
            //System.out.printf("adding tile number %d, with x %d and y %d \n", gameTile.tileNumber, gameTile.tileLocation.x, gameTile.tileLocation.y);;
        }


        mainWindow.setVisible(true);
    }

    private void loadImage(){
        try{
            image = ImageIO.read(Puzzle.class.getResource("resources/Character_Sunday_Splash_Art.jpg"));
            //TODO: why does ImageIO.read need the getResource function?
        }catch(IOException e){

            System.err.println(e.getMessage());
        }

    }

    private class GamePanel extends JPanel{
        //TODO: let this take a path as argument so it can use different images


        public GamePanel(){

            /*fieldSize = new Dimension(image.getWidth(this),image.getHeight(this));
            tileSize = new Dimension(fieldSize.width / 4, fieldSize.height / 4);*/


        }



        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            //TODO: whats an observer?
            g.drawImage(image, 0, 0, this);
        }

    }

    private class GameTile extends JPanel{
        Point tileLocation; //defines where to draw the tile
        int tileNumber; //defines which part of the image to use
        Point imageTileCorner; //the x,y coordinates of the tiles' top left corner on the original image
        Image image;
        Dimension fieldSize;
        Dimension tileSize;

        public GameTile(Point tileLocation, int tileNumber, Image image){
            this.tileLocation = tileLocation;
            this.tileNumber = tileNumber;
            this.image = image;

            System.out.printf("imagewidth: %d\n", image.getWidth(this));
            this.imageTileCorner = new Point((tileNumber%4) *(image.getWidth(this)/4), (tileNumber/4) *(image.getHeight(this)/4));
            fieldSize = new Dimension(image.getWidth(this),image.getHeight(this));
            tileSize = new Dimension(fieldSize.width / 4, fieldSize.height / 4);
        }

        @Override
        protected void paintComponent(Graphics g) {
            System.out.printf("drawing tile number %d \n", tileNumber);
            System.out.printf("coordinates: Desitnation height: %d, width: %d, src topleft:%d, %d, src bottomright:%d, %d\n",
                    tileSize.height,
                    tileSize.width,
                    imageTileCorner.x,
                    imageTileCorner.y,
                    imageTileCorner.x + tileSize.width,
                    imageTileCorner.y + tileSize.height);
            super.paintComponent(g);
            //TODO: need dimension of playing field
            g.drawImage(
                    image,
                    0,
                    0,
                    tileSize.height,
                    tileSize.width,
                    imageTileCorner.x,
                    imageTileCorner.y,
                    imageTileCorner.x + tileSize.width,
                    imageTileCorner.y + tileSize.height,
                    this);
        }
    }



}