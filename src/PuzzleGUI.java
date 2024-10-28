import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PuzzleGUI extends JFrame{
    BufferedImage image;    //full image
    //TODO: recheck Array in Java: is that length = 16 or highest index = 16?
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
        this.setLayout(new GridLayout(4,4));

        //generate tile icons
        int boardWidth = puzzleState.boardSize.width;
        int boardHeight = puzzleState.boardSize.height;
        int tileWidth = imageSize.width/boardWidth;
        int tileHeight = imageSize.height/boardHeight;
        for (int i = 0; i <= 15; i++){
            int x = (i%puzzleState.boardSize.width)*(tileWidth);
            int y = (i/puzzleState.boardSize.height)*(tileHeight);
            tileIcons[i] = new ImageIcon(image.getSubimage(x,y,tileWidth,tileHeight));
        }

    }

    public void renderGUI(){
        //draw tiles
        for (ImageIcon icon: tileIcons){
            System.out.println("adding icon");
            this.add(new GameTile(icon));
        }

        //set window to visible
        this.setVisible(true);
    }

    /**
    * Reads and returns the image located at imagePath.
    */
    private BufferedImage loadImage(Path imagePath){
        BufferedImage loadedImage = null;
        System.out.printf("Attempting to load image at path: %s\n", imagePath);
        File file = imagePath.toFile();
        System.out.printf("File exists: %s\n",file.exists());
        try{
            loadedImage = ImageIO.read(file);
            //TODO: why does ImageIO.read need the getResource function?
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
        return loadedImage;

    }

    /**
     * A JPanel which draws the part of the full image corresponding to its tile number.
     * */
    private class GameTile extends JPanel {
        //TODO: finish moving image cropping outside of this class
        //Point imageTileCorner; //the x,y coordinates of the tiles' top left corner on the original image
        ImageIcon icon;
        JLabel label;

        public GameTile(ImageIcon icon) {
            this.icon = icon;
            label = new JLabel(icon);

            this.add(label);
        }
    }
}
