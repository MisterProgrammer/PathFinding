package v3.Pathfinding;

import java.util.HashSet;
import java.util.Set;

public class Grid {
	private Node[][] Grid;
	private int size;

	public enum State {
		OPEN, CLOSED, PATH
	}

	public enum Type {
		START, FINISH, OBSTACLE, EMPTY, WAYPOINT
	}

	public Grid(int size) {
		this.size = size;
		Grid = new Node[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Grid[i][j] = new Node(j, i);
				Grid[i][j].setG(0);
				Grid[i][j].setH(0);
				Grid[i][j].setType(Type.EMPTY);
			}
		}
	}

	public void restart() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Grid[i][j].setG(0);
				Grid[i][j].setH(0);
				Grid[i][j].setParent(null);
			}
		}
	}

	public Set<Node> getNeighbours(Node node, boolean isDiagonal) {
		Set<Node> neighbours = new HashSet<Node>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {

				if (isDiagonal) {
					if (j == 0 && i == 0)
						continue;
				} else if ((j == 0 && i == 0) || (j == -1 && i == -1) || (j == 1 && i == 1) || (j == 1 && i == -1)
						|| (j == -1 && i == 1))
					continue;

				int neighbourX = node.getX() + i;
				int neighbourY = node.getY() + j;

				if (isValid(new Node(neighbourX, neighbourY)))
					if (Grid[neighbourY][neighbourX].getType() != Type.OBSTACLE) {
						if (isNotJumpingObstacles(node, Grid[neighbourY][neighbourX])) {
							neighbours.add(Grid[neighbourY][neighbourX]);
						}
					}
			}
		}

		return neighbours;
	}

	private boolean isNotJumpingObstacles(Node parent, Node node) {
		// TODO Auto-generated method stub
		String direction = "";
		if (parent.getX() > node.getX() && parent.getY() < node.getY()) {
			direction = "SW";
		} else if (parent.getX() > node.getX() && parent.getY() > node.getY()) {
			direction = "NW";
		} else if (parent.getX() < node.getX() && parent.getY() < node.getY()) {
			direction = "SE";
		} else if (parent.getX() < node.getX() && parent.getY() > node.getY()) {
			direction = "NE";
		}

		switch (direction) {
		case "SW":
			if (Grid[parent.getY()][parent.getX() - 1].getType() == Type.OBSTACLE
					&& Grid[parent.getY() + 1][parent.getX()].getType() == Type.OBSTACLE)
				return false;
			break;
		case "NW":
			if (Grid[parent.getY()][parent.getX() - 1].getType() == Type.OBSTACLE
					&& Grid[parent.getY() - 1][parent.getX()].getType() == Type.OBSTACLE)
				return false;
			break;
		case "SE":
			if (Grid[parent.getY()][parent.getX() + 1].getType() == Type.OBSTACLE
					&& Grid[parent.getY() + 1][parent.getX()].getType() == Type.OBSTACLE)
				return false;
			break;
		case "NE":
			if (Grid[parent.getY() - 1][parent.getX()].getType() == Type.OBSTACLE
					&& Grid[parent.getY()][parent.getX() + 1].getType() == Type.OBSTACLE) {
				return false;
			}
			break;
		default:
			return true;
		}

		return true;
	}

	public boolean isValid(Node node) {
		if ((node.getX() >= 0 && node.getX() < size && node.getY() >= 0 && node.getY() < size))
			return true;
		else
			return false;
	}

	public void printValueGrid() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print("(" + Math.round(Grid[i][j].getG()) + "," + Math.round(Grid[i][j].getH()) + ","
						+ Math.round(Grid[i][j].getF()) + ","
						+ ((Grid[i][j].getState() != null) ? Grid[i][j].getState() : Grid[i][j].getType()) + ") ");
			}
			System.out.println("\n");
		}
	}

	public void printGrid() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				char c = 0;
				State state = Grid[i][j].getState();
				Type type = Grid[i][j].getType();

				if (state != null)
					switch (state) {
					case CLOSED:
						c = '.';
						break;
					case OPEN:
						c = '.';
						break;
					case PATH:
						c = 'P';
						break;
					}

				switch (type) {
				case OBSTACLE:
					c = '#';
					break;
				case WAYPOINT:
					c = (char) (Grid[i][j].getWaypointNumber() + '0');
					break;
				case START:
					c = 'S';
					break;
				case FINISH:
					c = 'F';
					break;
				case EMPTY:
					if (state == null)
						c = '.';
					break;
				}
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}

	public char getDirection(Node node) {
		Node parent = node.getParent();
		if (parent != null) {
			if (parent.getX() == node.getX() && (parent.getY() < node.getY() || parent.getY() > node.getY())) {
				return '|';
			} else if (parent.getY() == node.getY() && (parent.getX() < node.getX() || parent.getX() > node.getX())) {
				return '-';
			} else if ((parent.getX() > node.getX() && parent.getY() < node.getY())
					|| (parent.getX() < node.getX() && parent.getY() > node.getY())) {
				return '/';
			} else if ((parent.getX() < node.getX() && parent.getY() < node.getY())
					|| (parent.getX() > node.getX() && parent.getY() > node.getY())) {
				return 92;
			}
		}

		return 'X';
	}

	public int getSize() {
		return size;
	}

	public Node[][] get() {
		// TODO Auto-generated method stub
		return Grid;
	}

	public char[][] getCharGrid() {
		// TODO Auto-generated method stub
		char[][] grid = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				char c = 0;
				State state = Grid[i][j].getState();
				Type type = Grid[i][j].getType();

				if (state != null)
					switch (state) {
					case CLOSED:
						c = '.';
						break;
					case OPEN:
						c = '.';
						break;
					case PATH:
						c = 'P';
						break;
					}
				if (type != null)
					switch (type) {
					case OBSTACLE:
						c = '#';
						break;
					case WAYPOINT:
						c = (char) (Grid[i][j].getWaypointNumber() + '0');
						break;
					case START:
						c = 'S';
						break;
					case FINISH:
						c = 'F';
						break;
					case EMPTY:
						if (state == null)
							c = '.';
						break;
					}
				grid[i][j] = c;
			}
		}
		return grid;
	}

	public boolean contains(Type type) {
		// TODO Auto-generated method stub
		for (Node[] y : Grid) {
			for (Node x : y) {
				if (x.getType() == type)
					return true;
			}
		}
		return false;
	}

}
