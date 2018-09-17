import java.util.Random;

// Helper class that stores coordinates, works like a tuple
class Point implements Comparable<Point>{
	public final int x, y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Point p) {
		if (x == p.x && y == p.y) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
}

public class Board {
	private Cell grid[][];
	private Cell mines[];
	private int flaggedMines = 0;
	private int totalFlags = 0;
	private boolean gameFinished = false;
	
	public Board (int width, int height, int numMines) {
		super();	
		
		// initializes the game grid and mines array
		grid = new Cell[height][width];
		Point[] minePoints = getMinePositions(width, height, numMines);
		mines = new Cell[numMines];
		
		int i = 0;
		for (Point point : minePoints) {
			// add a mine to the grid
			grid[point.y][point.x] = new Cell(true);
			mines[i++] = grid[point.y][point.x];
			
			// calculate the mines possible neighbors 			
			boolean possibilities[] = movementPossibilities(point.x, point.y);
			
			// add the indication cells
			if (possibilities[0])
				updateNeighbor(point.x-1, point.y);
			if (possibilities[1])
				updateNeighbor(point.x+1, point.y);
			if (possibilities[2])
				updateNeighbor(point.x, point.y-1);
			if (possibilities[3])
				updateNeighbor(point.x, point.y+1);
			if (possibilities[0] && possibilities[2])
				updateNeighbor(point.x-1, point.y-1);
			if (possibilities[0] && possibilities[3])
				updateNeighbor(point.x-1, point.y+1);
			if (possibilities[1] && possibilities[2])
				updateNeighbor(point.x+1, point.y-1);
			if (possibilities[1] && possibilities[3])
				updateNeighbor(point.x+1, point.y+1);
		}
		
		// fill out the remaining form the grid with empty cells
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				if (grid[y][x] == null) {
					grid[y][x] = new Cell(false);
				}
			}
		}
	}
	
	// returns a list of random points where the mines will be placed
	
	private Point[] getMinePositions (int width, int height, int numMines) {
		Random r = new Random();
		Point p = null;
		int minesCreated = 0;
		Point mines[] = new Point[numMines];
		
		boolean isReapeat = false;
		
		// creates random positions without repeating
		while (minesCreated < numMines) {
			p = new Point(r.nextInt(width), r.nextInt(height));
			
			for (int i=0; i<minesCreated && !isReapeat; i++) {
				if (mines[i].compareTo(p) == 0) {
					isReapeat = true;
				}
			}
			
			if (!isReapeat) {
				mines[minesCreated++] = p;
			}
			
			isReapeat = false;
		}
		
		return mines;
	}
	
	// this function is called when creating the cells adjacent to a mine,
	// it updates the cell value or creates a new cell with value 1
	
	private void updateNeighbor (int x, int y) {
		if (grid[y][x] != null) {
			Cell v = grid[y][x];
			v.setValue((v.getValue()+1));
		} else {
			grid[y][x] = new Cell(false, 1);
		}
	}
	
	// helper function that returns whether or not a move on the x or y axis is possible
	// the array order is [Left, Right, Up, Down]
	
	private boolean[] movementPossibilities(int x, int y) {
		boolean[] posibilities = {
				x-1 >= 0,
				x+1 < grid[y].length,
				y-1 >= 0,
				y+1 < grid.length
		};
		
		return posibilities;
	}
	
	// Uncovers a cell and checks if the player lost
	public void reveal (int x, int y) {
		if (grid[y][x].isMine()) {
			endGame(false);
			return;
		}
		
		// check if cell is blank
		if (grid[y][x].getValue() == 0)
			// handle blank chunk
			revealBlanks(x, y);
		else {
			unflag(x, y);
			grid[y][x].setVisible(true);
		}

	}
	
	// recursive method for uncovering a chunk of blank cells
	private void revealBlanks (int x, int y) {
		if (!grid[y][x].isVisible()) {
			unflag(x, y);
			grid[y][x].setVisible(true);
			if (grid[y][x].getValue() == 0) {
				boolean possibilities[] = movementPossibilities(x, y);
				if (possibilities[0])
					revealBlanks(x-1, y);
				if (possibilities[1])
					revealBlanks(x+1, y);
				if (possibilities[2])
					revealBlanks(x, y-1);
				if (possibilities[3])
					revealBlanks(x, y+1);
				if (possibilities[0] && possibilities[2])
					revealBlanks(x-1, y-1);
				if (possibilities[0] && possibilities[3])
					revealBlanks(x-1, y+1);
				if (possibilities[1] && possibilities[2])
					revealBlanks(x+1, y-1);
				if (possibilities[1] && possibilities[3])
					revealBlanks(x+1, y+1);
			}
		}
	}
	
	// logic function call for flagging or unflagging a given cell
	public void markFlag (int x, int y) {
		if (grid[y][x].isFlagged()) {
			unflag(x, y);
		} else {
			flag(x, y);
		}
	}
	
	// adds a flag to a cell and checks if the player won
	private void flag (int x, int y) {
		if (!grid[y][x].isVisible() && !grid[y][x].isFlagged()) {
			grid[y][x].setFlagged(true);
			totalFlags++;
			if (grid[y][x].isMine()) {
				flaggedMines++;
			}
			
			if (flaggedMines == totalFlags && flaggedMines == mines.length) {
				endGame(true);
			}
		}
	}
	
	// removes a flag from a flagged cell and checks if the player won
	private void unflag (int x, int y) {
		if (!grid[y][x].isVisible() && grid[y][x].isFlagged()) {
			grid[y][x].setFlagged(false);
			totalFlags--;
			if (grid[y][x].isMine()) {
				flaggedMines--;
			}
			
			if (flaggedMines == totalFlags && flaggedMines == mines.length) {
				endGame(true);
			}
		}
	}
	
	// ends the game in either victory or loss
	private void endGame(boolean won) {
		gameFinished = true;
		if (won) {
			System.out.println("Congratulations, you win!");
		} else {
			for (Cell c: mines) {
				c.setVisible(true);
			}
			System.out.println("Mission failed, we'll get em next time :(");
		}
	}
	
	// getters
	public int getTotalFlags() {
		return totalFlags;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int y=0; y<grid.length; y++) {
			for (int x=0; x<grid[y].length-1; x++) {
				sb.append(grid[y][x].toString() + " ");
			}
			sb.append(grid[y][grid[y].length-1].toString() + "\n");
		}
		return sb.toString();
	}
}
