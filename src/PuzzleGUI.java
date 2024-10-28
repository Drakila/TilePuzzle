import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PuzzleGUI extends JFrame{
    BufferedImage image;    //full image
    ImageIcon[] tileIcons = new ImageIcon[16]; //parts of the full image, index corresponds to the tile number
    Dimension imageSize;
    Dimension windowSize;
    // reference to assigned Puzzle instance which manages the board state
    Puzzle puzzleState;

    public PuzzleGUI(Path imagePath, Puzzle puzzleState){
        //NOTE: this is one beefy constructor

        this.puzzleState = puzzleState;

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
        int boardWidth = puzzleState.boardSize.width;
        int boardHeight = puzzleState.boardSize.height;
        int tileWidth = imageSize.width/boardWidth;
        int tileHeight = imageSize.height/boardHeight;
        for (int i = 0; i < 16; i++){
            int x = (i%puzzleState.boardSize.width)*(tileWidth);
            int y = (i/puzzleState.boardSize.height)*(tileHeight);
            tileIcons[i] = new ImageIcon(image.getSubimage(x,y,tileWidth,tileHeight));
        }

    }

    public void renderGUI(){

        //for testing purposes
        //puzzleState.moveTile(new Point(3,2));

        //draw tiles according to the board state in puzzleState
        int[] drawOrder = puzzleState.getTileDrawOrder();
        for (int i = 0; i < drawOrder.length; i++) {
            this.add(new GameTile(tileIcons[drawOrder[i]], drawOrder[i]));
        }

        //set window to visible
        this.setVisible(true);
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
    private class GameTile extends JPanel {
        ImageIcon icon;
        JLabel picture;
        int tileNumber;

        public GameTile(ImageIcon icon, int tileNumber) {
            this.icon = icon;
            picture = new JLabel(icon);
            this.tileNumber = tileNumber;

            this.add(picture);

            //for debug purposes to track which tile is which
            this.add(new JLabel(String.valueOf(this.tileNumber)));
        }
    }
}
