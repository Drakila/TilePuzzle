This is a practice project for Java and Java Swing.

The program offers a GUI to play a simple 4x4 sliding tile puzzle, including shuffling and sorting the puzzle.
By only executing valid moves, the shuffle always results in a solvable puzzle state.
The puzzle image is loaded from the path passed as the first argument.
Moves are executed by clicking on a tile adjacent to the empty space.

Planned features:
  - support for custom puzzle sizes
  - menu to choose image, puzzle size and shuffling strength
  - resizing of large images to fit the window

Further feature ideas:
  - keyboard control (wasd and arrow keys to move the empty spot)
  - highlight of tiles on mousover, perhaps showing whether a move is valid?
  - animation for the moving tile (probably requires a rewrite of the tile rendering classes... but would be good to learn!)
