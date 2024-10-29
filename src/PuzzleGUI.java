import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class PuzzleGUI extends JFrame{
    //TODO: instead of calling updateGUI() manually, is it possible to watch puzzleState.tileLocations and trigger a redraw whenever that changes?
    BufferedImage image;    //full image
    int tileAmount;
    ImageIcon[] tileIcons; //parts of the full image, index corresponds to the tile number
    Dimension imageSize;
    Dimension windowSize;
    // reference to assigned Puzzle instance which manages the board state
    Puzzle puzzleState;

    public PuzzleGUI(Path imagePath, Puzzle puzzleState){
        //NOTE: this is one beefy constructor

        this.puzzleState = puzzleState;

        this.tileAmount = puzzleState.boardSize.height * puzzleState.boardSize.width;

        //load image
        image = loadImage(imagePath);

        //determine image size
        imageSize = new Dimension(image.getWidth(this), image.getHeight(this));

        //determine window size
        //TODO: set a maximum window size and scale image down if needed
        //TODO: add the extra space needed for margins between the tiles
        windowSize = imageSize;

        //window settings
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.darkGray);
        this.setSize(windowSize);

        this.setLayout(new GridLayout(4,4,0,0));


        //generate tile icons
        this.tileIcons = new ImageIcon[tileAmount];

        int boardWidth = puzzleState.boardSize.width;
        int boardHeight = puzzleState.boardSize.height;
        int tileWidth = imageSize.width/boardWidth;
        int tileHeight = imageSize.height/boardHeight;
        for (int i = 0; i < (tileIcons.length -1); i++){
            int x = (i%puzzleState.boardSize.width)*(tileWidth);
            int y = (i/puzzleState.boardSize.height)*(tileHeight);
            tileIcons[i] = new ImageIcon(image.getSubimage(x,y,tileWidth,tileHeight));
        }
        tileIcons[tileIcons.length-1] = new ImageIcon(new BufferedImage(tileWidth, tileHeight, 5));

    }

    public void renderGUI(){

        //for testing purposes
        //puzzleState.moveTile(new Point(3,2));

        //draw tiles according to the board state in puzzleState
        int[] drawOrder = puzzleState.getTileDrawOrder();
        for (int j : drawOrder) {
            this.add(new GameTile(tileIcons[j], j));
        }

        //set window to visible
        this.setVisible(true);
    }

    private void updateGUI(){
        //TODO: figure out a more efficient way than remaking and adding ALL tiles on EVERY turn. Maybe use GridBagLayout?
        this.getContentPane().removeAll();
        renderGUI();
    }

    /**
    * Reads and returns the image located at imagePath.
    * imagePath: absolute path to the image which will be used as the puzzle.
    */
    private BufferedImage loadImage(Path imagePath){
        BufferedImage loadedImage = null;
        System.out.printf("Attempting to load image at path: %s\n", imagePath);
        File file = imagePath.toFile();
        System.out.printf("File exists: %s\n",file.exists());
        try{
            loadedImage = ImageIO.read(file);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
        return loadedImage;

    }

    /**
     * A JPanel which draws the part of the full image corresponding to its tile number.
     * */
    private class GameTile extends JPanel implements MouseListener {
        ImageIcon icon;
        JLabel picture;
        int tileNumber;

        public GameTile(ImageIcon icon, int tileNumber) {
            this.icon = icon;
            picture = new JLabel(icon);
            this.tileNumber = tileNumber;

            picture.addMouseListener(this);
            this.add(picture);



            //for debug purposes to track which tile is which
            this.add(new JLabel(String.valueOf(this.tileNumber)));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //find tile coordinates by tile number

            System.out.printf("Tile nr. %s was clicked\n", tileNumber);
            Point tileLocation = puzzleState.getTileByNumber(tileNumber);

            puzzleState.moveTile(tileLocation);

            updateGUI();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //do nothing
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //do nothing
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //TODO: highlight tile in some fashion?
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //TODO: un-highlight tile
        }
    }
}
