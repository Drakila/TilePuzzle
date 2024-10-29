import java.awt.*;
import java.nio.file.Path;

public class Main {
    //manage a Puzzle instance
    public static void main(String[] args){
        Puzzle puzzleState = new Puzzle(new Dimension(4,4));
        PuzzleGUI puzzleGUI = new PuzzleGUI(Path.of(args[0]), puzzleState);

    }
}