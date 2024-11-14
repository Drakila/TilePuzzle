import java.awt.*;
import java.nio.file.Path;

public class Main {
    //manage a Puzzle instance
    //TODO: When implementing n*n puzzle support, display a window first to choose dimension and image.
    public static void main(String[] args){
        Puzzle puzzleState = new Puzzle(new Dimension(4,4));
        PuzzleGUI puzzleGUI = new PuzzleGUI(Path.of(args[0]), puzzleState);

    }
}