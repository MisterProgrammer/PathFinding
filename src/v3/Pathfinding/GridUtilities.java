package v3.Pathfinding;

public class GridUtilities {

	static final char EMPTY = '.';
	static final char OBSTACLE = '#';
	static final char START = 'S';
	static final char END = 'F';
	static final char PATH = 'P';
	static final char CLOSED = 'C';
	static final char OPEN = 'O';
	static final char[] CHARWAYPOINT = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	static final char WAYPOINT = 'W';
	
	public static void clearGrid(char[][] grid) {
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

	public static void resetGrid(char[][] grid) {
		// TODO Auto-generated method stub
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[j][i] == PATH) {
					grid[j][i] = EMPTY;
				} else if (grid[j][i] == CLOSED) {
					grid[j][i] = EMPTY;
				} else if (grid[j][i] == OPEN) {
					grid[j][i] = EMPTY;
				}
			}
		}
	}
}
