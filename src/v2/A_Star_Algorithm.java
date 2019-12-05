package v2;

import java.util.ArrayList;

public class A_Star_Algorithm {
	Node start, finish, parent, openNode;
	private char[][] grid;
	private Node[][] nodeGrid;
	ArrayList<Node> obstacleNodes, openNodes, closedNodes, pathNodes, waypointNodes;
	private static int choice = 1;
	private boolean finished;
	private boolean noPathFound;
	private boolean invalidNodes;

	public void setChoice(int choice) {
		A_Star_Algorithm.choice = choice;
	}

	public char[][] getGrid() {
		return grid;
	}

	public void setGrid(char[][] grid) {
		this.grid = grid;
	}

	public void startAlgorithmWthTimer(char[][] grid) {
		System.out.println(gridSequence(grid));
		setup(grid);
		bubbleSort(waypointNodes);
		parent = start;
		openNodes.add(parent);
	}

	public void startAlgorithm(char[][] grid) {
		System.out.println(gridSequence(grid));
		setup(grid);
		Node startTemp = start;
		Node finishTemp = null;
		bubbleSort(waypointNodes);
		if (!invalidNodes) {
			for (int i = 0; i < getWaypointNodes().size(); i++) {
				finished = false;
				finishTemp = getWaypointNodes().get(i);
				findPath(startTemp, finishTemp);
				Node tempNode = finishTemp;
				while (tempNode.getParent() != null) {
					tempNode = tempNode.getParent();
					pathNodes.add(tempNode);
				}
				printPathNodes();
				startTemp = finishTemp;
				openNodes = new ArrayList<Node>();
				closedNodes = new ArrayList<Node>();
			}
			finished = false;
			finishTemp = finish;
			findPath(startTemp, finishTemp);
			Node tempNode = finishTemp;
			while (tempNode.getParent() != null) {
				tempNode = tempNode.getParent();
				pathNodes.add(tempNode);

			}
			printPathNodes();
		}
		System.out.println(finish.getParent().getG());
	}

	private void setup(char[][] grid) {
		openNodes = new ArrayList<Node>();
		closedNodes = new ArrayList<Node>();
		pathNodes = new ArrayList<Node>();
		obstacleNodes = new ArrayList<Node>();
		waypointNodes = new ArrayList<Node>();
		start = null;
		finish = null;
		finished = false;
		invalidNodes = false;
		noPathFound = false;
		this.grid = grid;
		for (int x = 0; x < grid[0].length; x++) {
			for (int y = 0; y < grid.length; y++) {
				if (grid[y][x] == '#') {
					obstacleNodes.add(new Node(x, y));
				} else if (grid[y][x] == 'S') {
					start = new Node(x, y);
				} else if (grid[y][x] == 'F') {
					finish = new Node(x, y);
				} else if (Character.isDigit(grid[y][x])) {
					for (int i = 1; i < 10; i++) {
						if (Character.getNumericValue(grid[y][x]) == i) {
							System.out.println(Character.getNumericValue(grid[y][x]));
							if (!searchNumberSameWaypoint(Character.getNumericValue(grid[y][x]))) {
								getWaypointNodes().add(new Node(x, y, Character.getNumericValue(grid[y][x])));
							} else {
								System.out.println("Waypoint already in waypoint Node List!");
							}
						}
					}
				}
			}
		}
		if (start != null && finish != null) {
			start.setG(0);
			finish.setH(0);
		} else {
			invalidNodes = true;
			finished = true;
		}
	}

	public void finish(Node tempNode) {
		tempNode.getParent().printNode();
		while (tempNode.getParent() != null) {
			tempNode = tempNode.getParent();
			pathNodes.add(tempNode);
		}
		printPathNodes();
	}

	public void findPathWthTimer(Node finishTemp) {
		openNodes.remove(parent);
		// TODO Auto-generated method stub
		if (choice == 1) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (j == 0 && i == 0)
						continue;
					int possibleX = parent.getX() + i;
					int possibleY = parent.getY() + j;

					int crossBorderX = parent.getX() + (possibleX - parent.getX());
					int crossBorderY = parent.getY() + (possibleY - parent.getX());

					if (searchObstacles(new Node(crossBorderX, parent.getY()))
							| searchObstacles(new Node(parent.getX(), crossBorderY))
							&& ((j == -1 | j == 1) && i != 0)) {
						continue;
					}

					boolean isDiagonal = false;
					if ((j == -1 && i == -1) || (j == 1 && i == 1) || (j == 1 && i == -1) || (j == -1 && i == 1)) {
						isDiagonal = true;
					}
					calculations(parent, possibleX, possibleY, finishTemp, isDiagonal);

				}
			}
		} else if (choice == 2) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if ((j == 0 && i == 0) || (j == -1 && i == -1) || (j == 1 && i == 1) || (j == 1 && i == -1)
							|| (j == -1 && i == 1))
						continue;
					int possibleX = parent.getX() + i;
					int possibleY = parent.getY() + j;
					calculations(parent, possibleX, possibleY, finishTemp, false);
				}
			}
		}
		parent = lowestFCost();
		printClosedAndOpenNodes();
		if (parent == null) {
			finished = true;
			System.out.println("No path Found");
			noPathFound = true;
			return;
		}
		if (isFinish(parent, finishTemp)) {
			finishTemp.setParent(parent.getParent());
			finished = true;
			return;
		}

		closedNodes.add(parent);
	}

	private void findPath(Node startTemp, Node finishTemp) {
		// TODO Auto-generated method stub
		Node parent = startTemp;
		openNodes.add(parent);

		while (openNodes.size() != 0) {
			parent = lowestFCost();
			printClosedAndOpenNodes();

			openNodes.remove(parent);
			closedNodes.add(parent);
			if (parent == null) {
				finished = true;
				System.out.println("No path Found");
				noPathFound = true;
				return;
			}
			if (isFinish(parent, finishTemp)) {
				finishTemp.setParent(parent.getParent());
				finished = true;
				return;
			}

			if (choice == 1) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (j == 0 && i == 0)
							continue;
						int possibleX = parent.getX() + i;
						int possibleY = parent.getY() + j;

						int crossBorderX = parent.getX() + (possibleX - parent.getX());
						int crossBorderY = parent.getY() + (possibleY - parent.getX());

						boolean isDiagonal = false;
						if ((j == -1 && i == -1) || (j == 1 && i == 1) || (j == 1 && i == -1) || (j == -1 && i == 1)) {
							isDiagonal = true;
						}

						calculations(parent, possibleX, possibleY, finishTemp, isDiagonal);

					}
				}
			} else if (choice == 2) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if ((j == 0 && i == 0) || (j == -1 && i == -1) || (j == 1 && i == 1) || (j == 1 && i == -1)
								|| (j == -1 && i == 1))
							continue;
						int possibleX = parent.getX() + i;
						int possibleY = parent.getY() + j;

						calculations(parent, possibleX, possibleY, finishTemp, false);
					}
				}
			}

		}

		System.out.println("No path found!\nSize = " + openNodes.size());
	}

	private void printClosedAndOpenNodes() {
		// TODO Auto-generated method stub
		for (int i = 0; i < openNodes.size(); i++) {
			if (!(openNodes.get(i).getX() == finish.getX() && openNodes.get(i).getY() == finish.getY()))
				if (!(openNodes.get(i).getX() == start.getX() && openNodes.get(i).getY() == start.getY()))
					if (!searchWaypoint(openNodes.get(i)))
						if (!searchPath(openNodes.get(i)))
							grid[openNodes.get(i).getY()][openNodes.get(i).getX()] = 'O';
		}
		for (int i = 0; i < closedNodes.size(); i++) {
			if (!(closedNodes.get(i).getX() == finish.getX() && closedNodes.get(i).getY() == finish.getY()))
				if (!(closedNodes.get(i).getX() == start.getX() && closedNodes.get(i).getY() == start.getY()))
					if (!searchWaypoint(closedNodes.get(i)))
						if (!searchPath(closedNodes.get(i)))
							
							grid[closedNodes.get(i).getY()][closedNodes.get(i).getX()] = 'C';
		}

		System.out.println(gridSequence(grid));
	}

	private void calculations(Node parent, int possibleX, int possibleY, Node finishTemp, boolean isDiagonal) {
		Node openNode = null;
		// TODO Auto-generated method stub
		if (searchObstacles(new Node(possibleX, possibleY)) || !isValid(new Node(possibleX, possibleY))
				|| searchClosed(new Node(possibleX, possibleY)) != -1
				|| searchOpen(new Node(possibleX, possibleY)) != -1)
			return;
		float gCost = 0;
		if (choice == 1) {
			if (isDiagonal)
				gCost = parent.getG() + 1.416f;
			else
				gCost = parent.getG() + 1;

		} else if (choice == 2)
			gCost = parent.getG() + 1;

		openNode = new Node(possibleX, possibleY);
		openNode.setH(hDistance(openNode, finishTemp));
		openNode.setG(gCost);
		openNode.calculateF();
		openNode.setParent(parent);
		openNodes.add(openNode);
	}

	@SuppressWarnings("unused")
	private void printOpenNodes() {
		for (int i = 0; i < openNodes.size(); i++) {
			if (!((openNodes.get(i).getX() == start.getX() && openNodes.get(i).getY() == start.getY()))
					|| (openNodes.get(i).getX() == finish.getX() && openNodes.get(i).getY() == finish.getY()))
				if (!searchWaypoint(pathNodes.get(i)))
					grid[openNodes.get(i).getY()][openNodes.get(i).getX()] = 'O';
		}
		System.out.println(gridSequence(grid));
	}

	@SuppressWarnings("unused")
	private void printClosedNodes() {
		for (int i = 0; i < closedNodes.size(); i++) {
			if (!((closedNodes.get(i).getX() == start.getX() && closedNodes.get(i).getY() == start.getY()))
					|| (closedNodes.get(i).getX() == finish.getX() && closedNodes.get(i).getY() == finish.getY()))
				if (!searchWaypoint(pathNodes.get(i)))
					grid[closedNodes.get(i).getY()][closedNodes.get(i).getX()] = 'C';
		}
		System.out.println(gridSequence(grid));
	}

	private void printPathNodes() {
		for (int i = 0; i < pathNodes.size(); i++) {
			if (!(pathNodes.get(i).getX() == finish.getX() && pathNodes.get(i).getY() == finish.getY())) {
				if (!(pathNodes.get(i).getX() == start.getX() && pathNodes.get(i).getY() == start.getY())) {
					if (!searchWaypoint(pathNodes.get(i)))
						grid[pathNodes.get(i).getY()][pathNodes.get(i).getX()] = 'P';
				}
			}
		}

		System.out.println(gridSequence(grid));
	}

	private boolean searchPath(Node node) {
		boolean found = false;
		for (int i = 0; i < pathNodes.size(); i++) {
			if (pathNodes.get(i).getX() == node.getX() && pathNodes.get(i).getY() == node.getY()) {
				return true;
			}
		}
		return found;
	}

	private boolean searchWaypoint(Node node) {
		boolean found = false;
		for (int i = 0; i < getWaypointNodes().size(); i++) {
			if (getWaypointNodes().get(i).getX() == node.getX() && getWaypointNodes().get(i).getY() == node.getY()) {
				return true;
			}
		}
		return found;
	}

	private boolean searchNumberSameWaypoint(int k) {
		boolean found = false;
		for (int i = 0; i < getWaypointNodes().size(); i++) {
			if (getWaypointNodes().get(i).getF() == k) {
				return true;
			}
		}
		return found;
	}

	private float calculateF(float gCost, float hCost) {
		// TODO Auto-generated method stub
		return gCost + hCost;
	}

	private int searchOpen(Node node) {
		for (int i = 0; i < openNodes.size(); i++) {
			if (openNodes.get(i).getX() == node.getX() && openNodes.get(i).getY() == node.getY()) {
				return i;
			}
		}
		return -1;
	}

	private int searchClosed(Node node) {
		for (int i = 0; i < closedNodes.size(); i++) {
			if (closedNodes.get(i).getX() == node.getX() && closedNodes.get(i).getY() == node.getY()) {
				return i;
			}
		}
		return -1;
	}

	private boolean searchObstacles(Node node) {
		boolean found = false;
		for (int i = 0; i < obstacleNodes.size(); i++) {
			if (obstacleNodes.get(i).getX() == node.getX() && obstacleNodes.get(i).getY() == node.getY()) {
				return true;
			}
		}
		return found;
	}

	public void bubbleSort(ArrayList<Node> nodes) {
		for (int i = nodes.size() - 1; i > 0; i--) {
			for (int j = 0; j < nodes.size() - 1; j++) {
				if (nodes.get(j).getF() > nodes.get(j + 1).getF()) {
					swap(nodes, j, j + 1);
				}
			}
		}
	}

	void swap(ArrayList<Node> nodes, int i, int j) {
		Node temp = nodes.get(j);
		nodes.set(j, nodes.get(i));
		nodes.set(i, temp);
	}

	public Node lowestFCost() {
		bubbleSort(openNodes);
		Node bestNode = null;
		if (openNodes.size() != 0) {
			bestNode = openNodes.get(0);
		}
		return bestNode;
	}

	public void aStarSearch() {
		if (!(isValid(start) && isValid(finish))) {
			System.out.println("Start or Finish are not valid coordinates");
			return;
		}

		if (start.getX() == finish.getX() && start.getY() == finish.getY()) {
			System.out.println("You are already at the finish Line");
			return;
		}
	}

	public boolean isFinish(Node node, Node finish) {
		if (node.getX() == finish.getX() && node.getY() == finish.getY())
			return true;
		else
			return false;
	}

	public boolean isValid(Node node) {
		if (node.getX() >= 0 && node.getX() < grid.length && node.getY() >= 0 && node.getY() < grid[0].length)
			return true;
		else
			return false;
	}

	public float hDistance(Node node, Node finish) {
		float h = (float) Math.sqrt((node.getX() - finish.getX()) * (node.getX() - finish.getX())
				+ (node.getY() - finish.getY()) * (node.getY() - finish.getY()));
		return h;
	}

	public void fillGrid(char[][] grid) {
		for (int rows = 0; rows < grid.length; rows++) {
			for (int cols = 0; cols < grid[rows].length; cols++) {
				grid[rows][cols] = '.';
			}
		}
	}

	public void fillWithObjects(char[][] grid) {

		for (int i = 2; i < 8; i++) {
			grid[i][5] = '#';
		}

	}

	public void fillWithRandomObstacles(char[][] grid) {
		for (int rows = (int) (Math.random() * grid.length); rows < Math.random() * grid.length; rows++) {
			for (int cols = (int) (Math.random() * grid[rows].length); cols < Math.random()
					* grid[rows].length; cols++) {
				grid[rows][cols] = '1';
			}
		}
	}

	public String gridSequence(char[][] grid) {
		StringBuilder builder = new StringBuilder();
		for (int rows = 0; rows < grid.length; rows++) {
			for (int cols = 0; cols < grid[rows].length; cols++) {
				builder.append(grid[rows][cols] + " ");
			}
			builder.append("\n");

		}
		return builder.toString();
	}

	public boolean getFinished() {
		// TODO Auto-generated method stub
		return finished;
	}

	public boolean getNoPathFound() {
		// TODO Auto-generated method stub
		return noPathFound;
	}

	public ArrayList<Node> getWaypointNodes() {
		return waypointNodes;
	}

	public void setFinished(boolean b) {
		// TODO Auto-generated method stub
		this.finished = b;
	}

	public boolean getInvalidNodes() {
		// TODO Auto-generated method stub
		return invalidNodes;
	}
}
