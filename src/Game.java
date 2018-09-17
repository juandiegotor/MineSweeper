import java.util.Scanner;

public class Game {
	private Board b;
	private Scanner s;
	private int width, height;
	
	// Helper method that checks if a string only contains numbers
	private boolean isNumeric (String s) {
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	// initializes the board and iterates unitil the game is over
	public Game(Scanner s) {
		this.s = s;
		initializeBoard();
		System.out.println("Board initialized");
		System.out.println(b);
		while (!b.isGameFinished()) {
			applyAction();
			System.out.println(b);
		}
	}

	// validates user input and instantiates the game board
	private void initializeBoard () {
		int height;
		int width;
		int numMines;
		
		String h;
		String w;
		String n;
		
		do {
			System.out.println("Please enter: Height Width NumberOfMines");
			h = s.next();
			w = s.next();
			n = s.next();
		} while (!(isNumeric(h) && isNumeric(w) && isNumeric(n)));
		
		height = Integer.parseInt(h);
		width = Integer.parseInt(w);
		numMines = Integer.parseInt(n);
			
		if (height < 1 || width < 1) {
			System.out.println("The board dimensions cannot be smaller than 1");
		} else if (width * height < numMines) {
			System.out.println("The number of mines cannot surpass the total number of cells");
		} else {
			this.b = new Board(width, height, numMines);
			this.width = width;
			this.height = height;
		}
		
	}
	
	// uncovers or marks cells  
	private void applyAction () {
		
		String ys = s.next();
		String xs = s.next();
		char c = s.next().charAt(0);
		
		while (!(isNumeric(ys) && isNumeric(xs))) {
			System.out.println("Coordinates have to be numeric data");
			ys = s.next();
			xs = s.next();
			c = s.next().charAt(0);
		}
		
		int y = Integer.parseInt(ys) - 1;
		int x = Integer.parseInt(xs) - 1;
		
		if (y >= 0 && x >= 0 && y < height && x < width) {
			if (c == 'U') {
				b.reveal(x, y);
			} else if (c == 'M') {
				b.markFlag(x, y);
			} else {
				System.out.printf("Non Valid Action %c, valid actions are:\n", c);
				System.out.println("row column U: uncover cell (row, column)");
				System.out.println("row column M: mark cell (row, column)");
			}
		} else {
			System.out.println("Coordinates out of bounds");
		}
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Game g = new Game(s);
		
		System.out.println("exit game? [y/n]");
		
		while (s.next().compareTo("n") == 0) {
			g = new Game(s);
			System.out.println("exit game? [y/n]");
		}
		
		s.close();
	}
}
