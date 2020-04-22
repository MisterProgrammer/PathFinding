package v3.Pathfinding;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Maze {

	private Stack<Node> stack;
	private Random random;
	private int[][] maze;
	int size;

	public Maze(int size) {
		stack = new Stack<>();
		random = new Random();
		maze = new int[size][size];
		this.size = size;
	}

	public void generateMaze() {
		stack.push(new Node(0, 0));
		while (!stack.empty()) {
			Node next = stack.pop();
			if (validNextNode(next)) {
				maze[next.getY()][next.getX()] = 1;
				ArrayList<Node> neighbors = findNeighbors(next);
				addNeighboursToStack(neighbors);
			}
		
		}
	}

	public int[][] getMaze() {
		return maze;
	}

	private boolean validNextNode(Node node) {
		int numNeighboringOnes = 0;
		for (int y = node.getY() - 1; y < node.getY() + 2; y++) {
			for (int x = node.getX() - 1; x < node.getX() + 2; x++) {
				if (insideGrid(x, y) && isNotNode(node, x, y) && maze[y][x] == 1) {
					numNeighboringOnes++;
				}
			}
		}
		return (numNeighboringOnes < 3) && maze[node.getY()][node.getX()] != 1;
	}

	private void addNeighboursToStack(ArrayList<Node> neighbours) {
		int targetIndex;
		while (!neighbours.isEmpty()) {
			targetIndex = random.nextInt(neighbours.size());
			stack.push(neighbours.get(targetIndex));
			neighbours.remove(targetIndex);
		}
	}

	private ArrayList<Node> findNeighbors(Node node) {
		ArrayList<Node> neighbors = new ArrayList<>();
		for (int y = node.getY() - 1; y < node.getY() + 2; y++) {
			for (int x = node.getX() - 1; x < node.getX() + 2; x++) {
				if (insideGrid(x, y) && isNotCorner(node, x, y) && isNotNode(node, x, y)) {
					neighbors.add(new Node(x, y));
				}
			}
		}
		return neighbors;
	}

	private Boolean insideGrid(int x, int y) {
		if (x >= 0 && y >= 0 && x < size && y < size)
			return true;
		else
			return false;
	}

	private Boolean isNotCorner(Node node, int x, int y) {
		if (x == node.getX() || y == node.getY())
			return true;
		else
			return false;
	}

	private Boolean isNotNode(Node node, int x, int y) {
		if (!(x == node.getX() && y == node.getY()))
			return true;
		else
			return false;
	}
}