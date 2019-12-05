package v0;

import java.util.ArrayList;
import java.util.Scanner;

public class A_Star_Algorithm {
	private Node start, finish;
	private char[][] grid;
	private ArrayList<Node> obstacles, closedNodes, pathNodes;
	private Scanner scan;
	private static int choice = 1;

	public static void setChoice(int choice) {
		A_Star_Algorithm.choice = choice;
	}

	public char[][] getGrid() {
		return grid;
	}

	public void setGrid(char[][] grid) {
		this.grid = grid;
	}

	public void startAlgorithm() {
		scan = new Scanner(System.in);
		grid = new char[][] {
				{ 'S', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', 'F' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
				{ '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },};

		setup(grid);
		findPath(start,finish);
		Node tempNode = finish;
		while (tempNode.parent != null) {
			tempNode = tempNode.parent;
			pathNodes.add(tempNode);
		}
		printPathNodes();
		scan.close();
	}

	public void startAlgorithm(char[][] grid) {
		scan = new Scanner(System.in);
		setup(grid);
		findPath(start,finish);
		Node tempNode = finish;
		while (tempNode.parent != null) {
			tempNode = tempNode.parent;
			pathNodes.add(tempNode);
		}
		printPathNodes();
		scan.close();
	}

	private void printOpenNodes(ArrayList<Node> openNodes) {
		for (int i = 0; i < openNodes.size(); i++) {
			if (!((openNodes.get(i).x == start.x && openNodes.get(i).y == start.y))
					|| (openNodes.get(i).x == finish.x && openNodes.get(i).y == finish.y))
				grid[openNodes.get(i).y][openNodes.get(i).x] = 'O';
		}
		System.out.println(gridSequence(grid));
	}

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
		closedNodes = new ArrayList<Node>();
		pathNodes = new ArrayList<Node>();
		obstacles = new ArrayList<Node>();
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

	private void findPath(Node start, Node end) {
		// TODO Auto-generated method stub
		Node openNode = null;
		Node parent = start;
		ArrayList<Node> openNodes = new ArrayList<Node>();
		openNodes.add(parent);
		while (openNodes.size() != 0) {
			if (choice == 1) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (j == 0 && i == 0)
							continue;
						int possibleX = parent.x + i;
						int possibleY = parent.y + j;

						calculations(openNodes,openNode, parent, possibleX, possibleY);
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

						calculations(openNodes,openNode, parent, possibleX, possibleY);
					}
				}
			}
			parent = lowestFCost(openNodes);
//			printClosedAndOpenNodes();
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			if (isFinish(parent)) {
				finish.parent = parent.parent;
				return;
			}
			openNodes.remove(parent);
			closedNodes.add(parent);
		}

		System.out.println("No path found!\nSize = " + openNodes.size());
	}

	private void printClosedAndOpenNodes(ArrayList<Node> openNodes) {
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

	private void calculations(ArrayList<Node> openNodes,Node openNode, Node parent, int possibleX, int possibleY) {
		// TODO Auto-generated method stub
		if (!isValid(new Node(possibleX, possibleY)))
			return;
		if (searchObstacles(new Node(possibleX, possibleY)) || searchOpen(openNodes,new Node(possibleX, possibleY))
				|| searchClosed(new Node(possibleX, possibleY)))
			return;

		openNode = new Node(possibleX, possibleY);
		openNode.parent = parent;
		float gCost = 0;
		if(choice == 1)
		gCost = parent.g + 1.416f;
		else if(choice == 2)
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

	private boolean searchOpen(ArrayList<Node> openNodes,Node node) {
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

	public Node lowestFCost(ArrayList<Node> openNodes) {
		bubbleSort(openNodes);
		return (openNodes.get(0) != null) ? openNodes.get(0) : null;
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
		float h = (float) Math.sqrt((node.x - finish.x)*(node.x - finish.x)+(node.y - finish.y)*(node.y - finish.y));
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
}
