package v1;

import java.util.ArrayList;
import java.util.Scanner;

public class A_Star_Algorithm {
	private Node start, finish;
	private char[][] grid;
	private ArrayList<Node> obstacles, openNodes, closedNodes, pathNodes;
	private Scanner scan;
	private static int choice = 1;
	private boolean finished;
	private boolean noPathFound;
	Node parent;
	Node openNode;

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
		scan = new Scanner(System.in);

		setup(grid);
		parent = start;
		openNodes.add(parent);
	}

	public void startAlgorithm(char[][] grid) {
		scan = new Scanner(System.in);
		setup(grid);
		noPathFound = false;
		findPath();
		Node tempNode = finish;
		while (tempNode.parent != null) {
			tempNode = tempNode.parent;
			pathNodes.add(tempNode);
		}
		printPathNodes();
		scan.close();
	}

	public void finish() {
		Node tempNode = finish;
		while (tempNode.parent != null) {
			tempNode = tempNode.parent;
			pathNodes.add(tempNode);
		}
		printPathNodes();
		scan.close();
	}

	@SuppressWarnings("unused")
	private void printOpenNodes() {
		for (int i = 0; i < openNodes.size(); i++) {
			if (!((openNodes.get(i).x == start.x && openNodes.get(i).y == start.y))
					|| (openNodes.get(i).x == finish.x && openNodes.get(i).y == finish.y))
				grid[openNodes.get(i).y][openNodes.get(i).x] = 'O';
		}
		System.out.println(gridSequence(grid));
	}

	@SuppressWarnings("unused")
	private void printClosedNodes() {
		for (int i = 0; i < closedNodes.size(); i++) {
			if (!((closedNodes.get(i).x == start.x && closedNodes.get(i).y == start.y))
					|| (closedNodes.get(i).x == finish.x && closedNodes.get(i).y == finish.y))
				grid[closedNodes.get(i).y][closedNodes.get(i).x] = 'C';
		}
		System.out.println(gridSequence(grid));
	}

	private void printPathNodes() {
		for (int i = 0; i < pathNodes.size(); i++) {
			if (!(pathNodes.get(i).x == finish.x && pathNodes.get(i).y == finish.y))
				if (!(pathNodes.get(i).x == start.x && pathNodes.get(i).y == start.y))
					grid[pathNodes.get(i).y][pathNodes.get(i).x] = 'P';
		}
		System.out.println(gridSequence(grid));
	}

	private void setup(char[][] grid) {
		openNodes = new ArrayList<Node>();
		closedNodes = new ArrayList<Node>();
		pathNodes = new ArrayList<Node>();
		obstacles = new ArrayList<Node>();
		noPathFound = false;
		finished = false;
		this.grid = grid;
		for (int x = 0; x < grid[0].length; x++) {
			for (int y = 0; y < grid.length; y++) {
				if (grid[y][x] == '#') {
					obstacles.add(new Node(x, y));
				} else if (grid[y][x] == 'S') {
					start = new Node(x, y);
				} else if (grid[y][x] == 'F') {
					finish = new Node(x, y);
				}
			}
		}
		start.g = 0;
		finish.h = 0;

	}

	public void findPathWthTimer() {
		openNodes.remove(parent);
		// TODO Auto-generated method stub
		if (choice == 1) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (j == 0 && i == 0)
						continue;
					int possibleX = parent.x + i;
					int possibleY = parent.y + j;

					int crossBorderX = parent.x + (possibleX - parent.x);
					int crossBorderY = parent.y + (possibleY - parent.x);

					if (searchObstacles(new Node(crossBorderX, parent.y))
							| searchObstacles(new Node(parent.x, crossBorderY)) && ((j == -1 | j == 1) && i != 0)) {
						continue;
					}
					calculations(openNode, parent, possibleX, possibleY);

				}
			}
		} else if (choice == 2) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if ((j == 0 && i == 0) || (j == -1 && i == -1) || (j == 1 && i == 1) || (j == 1 && i == -1)
							|| (j == -1 && i == 1))
						continue;
					int possibleX = parent.x + i;
					int possibleY = parent.y + j;
					calculations(openNode, parent, possibleX, possibleY);
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
		if (isFinish(parent)) {
			finish.parent = parent.parent;
			finished = true;
			return;
		}

		closedNodes.add(parent);
	}

	private void findPath() {
		// TODO Auto-generated method stub
		Node openNode = null;
		Node parent = start;
		openNodes.add(parent);
		while (openNodes.size() != 0) {
			openNodes.remove(parent);
			if (choice == 1) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (j == 0 && i == 0)
							continue;
						int possibleX = parent.x + i;
						int possibleY = parent.y + j;

						int crossBorderX = parent.x + (possibleX - parent.x);
						int crossBorderY = parent.y + (possibleY - parent.x);

						if (searchObstacles(new Node(crossBorderX, parent.y))
								| searchObstacles(new Node(parent.x, crossBorderY)) && ((j == -1 | j == 1) && i != 0)) {
							continue;
						}
						calculations(openNode, parent, possibleX, possibleY);

					}
				}
			} else if (choice == 2) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if ((j == 0 && i == 0) || (j == -1 && i == -1) || (j == 1 && i == 1) || (j == 1 && i == -1)
								|| (j == -1 && i == 1))
							continue;
						int possibleX = parent.x + i;
						int possibleY = parent.y + j;

						calculations(openNode, parent, possibleX, possibleY);
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
			if (isFinish(parent)) {
				finish.parent = parent.parent;
				finished = true;
				return;
			}
			closedNodes.add(parent);
		}

		System.out.println("No path found!\nSize = " + openNodes.size());
	}

	private void printClosedAndOpenNodes() {
		// TODO Auto-generated method stub
		for (int i = 0; i < openNodes.size(); i++) {
			if (!(openNodes.get(i).x == finish.x && openNodes.get(i).y == finish.y))
				if (!(openNodes.get(i).x == start.x && openNodes.get(i).y == start.y))
					grid[openNodes.get(i).y][openNodes.get(i).x] = 'O';
		}
		for (int i = 0; i < closedNodes.size(); i++) {
			if (!(closedNodes.get(i).x == finish.x && closedNodes.get(i).y == finish.y))
				if (!(closedNodes.get(i).x == start.x && closedNodes.get(i).y == start.y))
					grid[closedNodes.get(i).y][closedNodes.get(i).x] = 'C';
		}

		System.out.println(gridSequence(grid));
	}

	private void calculations(Node openNode, Node parent, int possibleX, int possibleY) {
		// TODO Auto-generated method stub
		if (!isValid(new Node(possibleX, possibleY)))
			return;
		if (searchObstacles(new Node(possibleX, possibleY)) || searchOpen(new Node(possibleX, possibleY))
				|| searchClosed(new Node(possibleX, possibleY)))
			return;

		openNode = new Node(possibleX, possibleY);
		openNode.parent = parent;
		float gCost = 0;
		if (choice == 1)
			gCost = parent.g + 1.416f;
		else if (choice == 2)
			gCost = parent.g + 1;
		float hCost = hDistance(openNode);
		openNode.h = hCost;
		openNode.g = gCost;
		openNode.f = calculateF(openNode.g, openNode.h);
		openNodes.add(openNode);
	}

	private float calculateF(float gCost, float hCost) {
		// TODO Auto-generated method stub
		return gCost + hCost;
	}

	private boolean searchOpen(Node node) {
		boolean found = false;
		for (int i = 0; i < openNodes.size(); i++) {
			if (openNodes.get(i).x == node.x && openNodes.get(i).y == node.y) {
				return true;
			}
		}
		return found;
	}

	private boolean searchClosed(Node node) {
		boolean found = false;
		for (int i = 0; i < closedNodes.size(); i++) {
			if (closedNodes.get(i).x == node.x && closedNodes.get(i).y == node.y) {
				return true;
			}
		}
		return found;
	}

	private boolean searchObstacles(Node node) {
		boolean found = false;
		for (int i = 0; i < obstacles.size(); i++) {
			if (obstacles.get(i).x == node.x && obstacles.get(i).y == node.y) {
				return true;
			}
		}
		return found;
	}

	public void bubbleSort(ArrayList<Node> nodes) {
		for (int i = nodes.size() - 1; i > 0; i--) {
			for (int j = 0; j < nodes.size() - 1; j++) {
				if (nodes.get(j).f > nodes.get(j + 1).f) {
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

		if (start.x == finish.x && start.y == finish.y) {
			System.out.println("You are already at the finish Line");
			return;
		}
	}

	public boolean isFinish(Node node) {
		if (node.x == finish.x && node.y == finish.y)
			return true;
		else
			return false;
	}

	public boolean isValid(Node node) {
		if (node.x >= 0 && node.x < grid.length && node.y >= 0 && node.y < grid[0].length)
			return true;
		else
			return false;
	}

	public float hDistance(Node node) {
		float h = (float) Math
				.sqrt((node.x - finish.x) * (node.x - finish.x) + (node.y - finish.y) * (node.y - finish.y));
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
}
