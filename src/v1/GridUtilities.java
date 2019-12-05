package v1;

public class GridUtilities {

	static final int EMPTY = 0;
	static final int OBSTACLE = 1;
	static final int START = 2;
	static final int END = 3;
	static final int PATH = 4;
	static final int CLOSED = 5;
	static final int OPEN = 6;

	public static int[][] convertAGridToWGrid(char[][] charGrid) {
		// TODO Auto-generated method stub

		int[][] grid = new int[charGrid.length][charGrid[0].length];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (charGrid[j][i] == '#') {
					grid[i][j] = OBSTACLE;
				} else if (charGrid[j][i] == '.') {
					grid[i][j] = EMPTY;
				} else if (charGrid[j][i] == 'F') {
					grid[i][j] = END;
				} else if (charGrid[j][i] == 'S') {
					grid[i][j] = START;
				} else if (charGrid[j][i] == 'P') {
					grid[i][j] = PATH;
				} else if (charGrid[j][i] == 'C') {
					grid[i][j] = CLOSED;
				} else if (charGrid[j][i] == 'O') {
					grid[i][j] = OPEN;
				}
			}
		}
		return grid;
	}

	public static char[][] convertWGridToAGrid(int[][] grid) {
		// TODO Auto-generated method stub
		char[][] charGrid = new char[grid.length][grid[0].length];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (grid[i][j] == OBSTACLE) {
					charGrid[j][i] = '#';
				} else if (grid[i][j] == START) {
					charGrid[j][i] = 'S';
				} else if (grid[i][j] == END) {
					charGrid[j][i] = 'F';
				} else if (grid[i][j] == EMPTY) {
					charGrid[j][i] = '.';
				}
			}
		}
		return charGrid;
	}

	public static void clearGrid(int[][] grid) {
		// TODO Auto-generated method stub
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = EMPTY;
			}
		}
	}

	public static String gridSequence(char[][] grid) {
		StringBuilder builder = new StringBuilder();
		for (int rows = 0; rows < grid.length; rows++) {
			for (int cols = 0; cols < grid[rows].length; cols++) {
				builder.append(grid[rows][cols] + " ");
			}
			builder.append("\n");

		}
		return builder.toString();
	}

	public static String intGridSequence(int[][] grid) {
		StringBuilder builder = new StringBuilder();
		for (int rows = 0; rows < grid.length; rows++) {
			for (int cols = 0; cols < grid[rows].length; cols++) {
				builder.append(grid[cols][rows] + " ");
			}
			builder.append("\n");

		}
		return builder.toString();
	}

	public static void resetGrid(int[][] grid) {
		// TODO Auto-generated method stub
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == PATH) {
					grid[i][j] = EMPTY;
				} else if (grid[i][j] == CLOSED) {
					grid[i][j] = EMPTY;
				} else if (grid[i][j] == OPEN) {
					grid[i][j] = EMPTY;
				}
			}
		}
	}
}
