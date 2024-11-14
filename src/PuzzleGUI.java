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

public class PuzzleGUI extends JFrame{
    //TODO: instead of calling updateGUI() manually, is it possible to watch puzzleState.tileLocations and trigger a redraw whenever that changes?
    BufferedImage image;    //full image
    int tileAmount;
    ImageIcon[] tileIcons; //parts of the full image, index corresponds to the tile number
    Dimension imageSize;
    Dimension windowSize;
    // reference to assigned Puzzle instance which manages the board state
    Puzzle puzzleState;

    GameBoard puzzlePanel;
    ControlPanel controlPanel;

    public PuzzleGUI(Path imagePath, Puzzle puzzleState){
        //NOTE: this is one beefy constructor

        this.puzzleState = puzzleState;

        this.tileAmount = puzzleState.boardSize.height * puzzleState.boardSize.width;

        //load image
        image = loadImage(imagePath);

        //determine image size
        imageSize = new Dimension(image.getWidth(this), image.getHeight(this));

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

        //determine window size
        //TODO: set a maximum window size and scale image down if needed
        //TODO: add the extra space needed for margins between the tiles
        windowSize = new Dimension(imageSize.width + 100, imageSize.height + 200);

        //window settings
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.darkGray);
        this.setSize(windowSize);
        //getContentPane is necessary because setLayout automatically sets the Layout for the content pane, not the JFrame
        //and the target needs to match ala "target.setLayout(new BoxLayout(target, axis))"
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        //construct window layout
        puzzlePanel = new GameBoard();
        controlPanel = new ControlPanel(puzzlePanel);

        this.add(puzzlePanel);
        this.add(controlPanel);

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
     * Panel which displays the current state of the puzzle using GameTiles
     * */
    private class GameBoard extends JPanel{
        //list of tiles on the board, in order of tileNumber
        final GameTile[] tileList = new GameTile[16];

        public GameBoard(){
            this.setLayout(new GridLayout(4,4,0,0));

            //generate list of GameTiles
            int tileAmount = puzzleState.boardSize.height * puzzleState.boardSize.width;
            for (int i = 0; i < tileAmount; i++) {
                tileList[i] = new GameTile(tileIcons[i], i, this);
            }

            //add GameTiles to the board in the current order
            int[] drawOrder = puzzleState.getTileDrawOrder();
            for (int j : drawOrder) {
                this.add(tileList[j]);
            }
        }

        /**
         * Trigger a complete redraw of the game board.
         * */
        private void redraw(){
            this.removeAll();

            int[] drawOrder = puzzleState.getTileDrawOrder();
            for (int j : drawOrder) {
                GameTile newTile = new GameTile(tileIcons[j], j, this);
                //update the order of tileList
                tileList[j] = newTile;
                this.add(newTile);
            }

            this.revalidate();
        }

        /**
         * Updates the game tile numbers and pictures to fake the tile moving.
         * @param tileNumber Number of the tile that swapped places with the empty tile.
         */
        private void renderMove(int tileNumber){
            //swap the tile with the empty tile by changing their picture and tileNumber
            GameTile tile = tileList[tileNumber];
            GameTile emptyTile = tileList[15];

            ImageIcon emptyImage = emptyTile.icon;

            emptyTile.update(tile.icon, tile.tileNumber);
            tile.update(emptyImage, 15);

            //maintain order of tileList
            tileList[tileNumber] = emptyTile;
            tileList[15] = tile;

            this.revalidate();
        }
    }

    /**
     * A JPanel which represents one tile in the game. Draws the part of the full image corresponding to its tile number.
     * */
    private class GameTile extends JPanel implements MouseListener {
        ImageIcon icon;
        JLabel picture;
        int tileNumber;
        final GameBoard gameBoard;

        public GameTile(ImageIcon icon, int tileNumber, GameBoard gameBoard) {
            this.icon = icon;
            picture = new JLabel(icon);
            this.tileNumber = tileNumber;
            this.gameBoard = gameBoard;

            picture.addMouseListener(this);
            this.add(picture);

            //for debug purposes to track which tile is which
            //this.add(new JLabel(String.valueOf(this.tileNumber)));
        }

        /**
         * Changes the Icon and Tile number, making this GameTile object represent a different actual tile than before.
         * This allows fake movement of the game tiles.
         * @param icon new image to overwrite the old one with
         * @param tileNumber new tile number
         */
        private void update(ImageIcon icon, int tileNumber){
            this.icon = icon;
            this.tileNumber = tileNumber;

            picture = new JLabel(icon);
            picture.addMouseListener(this);

            this.removeAll();
            this.add(picture);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            //find tile coordinates by tile number
            //System.out.printf("Tile nr. %s was clicked\n", tileNumber);
            Point tileLocation = puzzleState.getTileByNumber(tileNumber);

            if (puzzleState.moveTile(tileLocation)) {
                gameBoard.renderMove(tileNumber);
            }

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

    private class ControlPanel extends JPanel implements ActionListener{
        JButton shuffleButton;
        JButton sortButton;
        GameBoard controlledGame;
        public ControlPanel(GameBoard controlledGame){
            this.controlledGame = controlledGame;

            shuffleButton = new JButton("Shuffle");
            shuffleButton.addActionListener(this);
            sortButton = new JButton("Sort");
            sortButton.addActionListener(this);

            this.add(shuffleButton);
            this.add(sortButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == shuffleButton){
                puzzleState.randomizeBoard(10);
                controlledGame.redraw();
            } else if (source == sortButton) {
                puzzleState.sortBoard();
                controlledGame.redraw();
            }
        }
    }
}
