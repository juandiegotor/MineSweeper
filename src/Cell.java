public class Cell {
	private boolean isVisible = false;
	private boolean isFlagged = false;
	private boolean isMine;
	private int value = 0;
	
	// initialize a cell as mine (isMine = true)
	// as a blank cell (value 0)
	// or as a cell near a mine (non zero value)
	public Cell(boolean isMine) {
		super();
		this.isMine = isMine;
	}
	
	public Cell(boolean isMine, int value) {
		super();
		this.isMine = isMine;
		this.value = value;
	}

	// getters and setters
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}
	
	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		// handles problem specific cell displaying 
		if (isFlagged) {
			return "P";
		} else if (isVisible) {
			if (isMine) {
				return "*";
			} else if (value != 0) {
				return Integer.toString(value);
			} else {
				return "-";
			}
		} else {
			return ".";
		}
	}
}
