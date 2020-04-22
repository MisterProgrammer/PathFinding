package v3.Pathfinding;

import v3.Frame.GridPanel;

import java.util.*;

public class PathFindingAlgorithm {
	private Grid grid;
	private Node start, finish;
	private List<Node> openNodes, waypointNodes;
	private Set<Node> closedNodes;
	private LinkedList<Node> pathNodes;
	private boolean invalidNodes, noPathFound, isDiagonal, traverseSamePath;

	private Type algorithmType;
	private GridPanel gridP;
	private long timerValue;

	public long getTimerValue() {
		return timerValue;
	}

	public void setTimerValue(int timerValue) {
		this.timerValue = timerValue;
	}

	public enum Type {
		AASTERISK, DJIKSTRA, BREADTH
	}

	public Grid startAlgorithm(char[][] charGrid) {
		setup(charGrid);
		timerValue = 0;
		findPaths(start, finish, waypointNodes);
		return grid;
	}

	public Grid startAlgorithm(char[][] charGrid, GridPanel gridP) {
		// TODO Auto-generated method stub
		this.gridP = gridP;
		timerValue = -1;
		algorithmType = null;
		while (timerValue == -1) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		while (algorithmType == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		setup(charGrid);
		findPaths(start, finish, waypointNodes);

		return grid;
	}

	private void setup(char[][] charGrid) {
		start = null;
		finish = null;
		invalidNodes = false;
		noPathFound = false;
		grid = new Grid(charGrid.length);
		openNodes = new LinkedList<Node>();
		waypointNodes = new LinkedList<Node>();
		closedNodes = new HashSet<Node>();

		for (int y = 0; y < grid.getSize(); y++) {
			for (int x = 0; x < grid.getSize(); x++) {
				if (charGrid[y][x] == '#') {
					grid.get()[y][x].setType(Grid.Type.OBSTACLE);
				} else if (charGrid[y][x] == 'S') {
					start = grid.get()[y][x];
					start.setType(Grid.Type.START);
				} else if (charGrid[y][x] == 'F') {
					finish = grid.get()[y][x];
					finish.setType(Grid.Type.FINISH);
				} else if (charGrid[y][x] == '.')
					grid.get()[y][x].setType(Grid.Type.EMPTY);
				else if (Character.isDigit(charGrid[y][x])) {
					for (int i = 1; i < 10; i++) {
						if (Character.getNumericValue(charGrid[y][x]) == i) {
							if (!searchNumberSameWaypoint(Character.getNumericValue(charGrid[y][x]))) {
								waypointNodes.add(grid.get()[y][x]);
								grid.get()[y][x].setType(Grid.Type.WAYPOINT);
								grid.get()[y][x].setWaypointNumber(Character.getNumericValue(charGrid[y][x]));
							} else {
								System.out.println("Waypoint already in waypoint Node List!");
								invalidNodes = true;
							}
						}
					}
				}
			}
		}

		if (start != null && finish != null)

		{
			start.setG(0);
			finish.setH(0);
		} else {
			invalidNodes = true;
		}
	}

	public void findPaths(Node start, Node finish, List<Node> waypointNodes) {
		// TODO Auto-generated method stub
		pathNodes = new LinkedList<Node>();
		int size = waypointNodes.size();
		bubbleSort(waypointNodes);
		Node startTemp = start;
		Node finishTemp = null;

		for (int i = 0; i <= size; i++) {
			grid.restart();
			openNodes = new LinkedList<Node>();
			closedNodes = new HashSet<Node>();
			if (!(i >= size)) {
				finishTemp = waypointNodes.get(i);
			} else {
				finishTemp = finish;
			}
			if (!invalidNodes) {
				List<Node> pathNodesTemp = null;
				switch (algorithmType) {
				case AASTERISK:
					pathNodesTemp = findPathAAsterisk(startTemp, finishTemp);
					break;
				case BREADTH:
					pathNodesTemp = findPathBreadthFirst(startTemp, finishTemp);
					break;
				case DJIKSTRA:
					pathNodesTemp = findPathDjikstra(startTemp, finishTemp);
					break;
				default:
					break;
				}
				if (pathNodesTemp != null) {
					pathNodes.addAll(pathNodesTemp);
					for (Node node : pathNodes) {
						if (grid.get()[node.getY()][node.getX()].getType() != Grid.Type.START
								&& grid.get()[node.getY()][node.getX()].getType() != Grid.Type.FINISH) {
							if (!traverseSamePath) {
								if (!waypointNodes.contains(node))
									grid.get()[node.getY()][node.getX()].setState(Grid.State.PATH);
							} else {
								grid.get()[node.getY()][node.getX()].setState(Grid.State.PATH);
							}
						}
					}
				} else {
					noPathFound = true;
					System.out.println("Path not found");
					return;
				}

			} else {
				System.out.println(invalidNodes);
				System.out.println("Invalid Nodes");
				return;
			}

			startTemp = finishTemp;
		}

	}

	private boolean searchNumberSameWaypoint(int k) {
		boolean found = false;
		for (int i = 0; i < waypointNodes.size(); i++)
			if (waypointNodes.get(i).getWaypointNumber() == k)
				return true;

		return found;
	}

	private Node getLowestFScore(List<Node> list) {
		Node lowestF = list.get(0);
		for (Node n : list) {
			if (n.getF() < lowestF.getF()) {
				lowestF = n;
			}
		}
		return lowestF;
	}

	private List<Node> findPathDjikstra(Node startTemp, Node finishTemp) {
		// TODO Auto-generated method stub
		for (Node[] nodeAr : grid.get()) {
			for (Node node : nodeAr) {
				node.setG(Float.MAX_VALUE);
				openNodes.add(node);
			}
		}
		startTemp.setG(0);
		openNodes.add(startTemp);
		while (openNodes.size() > 0) {
			Node currentNode = getLowestGScore(openNodes);
			openNodes.remove(currentNode);

			if (currentNode.equals(finishTemp)) {
				finishTemp.setParent(currentNode.getParent());
				return PathNodes(finishTemp);
			}

			for (Node neighbour : grid.getNeighbours(currentNode, isDiagonal)) {
				currentNode.printNode();
				float newGCost = currentNode.getG() + getDistance(currentNode, neighbour);
				if (newGCost < neighbour.getG()) {
					neighbour.setG(newGCost);
					neighbour.setParent(currentNode);
					grid.get()[neighbour.getY()][neighbour.getX()].setState(Grid.State.OPEN);
				}
			}
			try {
				Thread.sleep(timerValue);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gridP.setGrid(grid);
			gridP.repaint();
		}
		return null;
	}

	private Node getLowestGScore(List<Node> list) {
		// TODO Auto-generated method stub
		Node lowestG = list.get(0);
		for (Node n : list) {
			if (n.getG() < lowestG.getG()) {
				lowestG = n;
			}
		}
		return lowestG;
	}

	private List<Node> findPathBreadthFirst(Node startTemp, Node finishTemp) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Queue<Node> q = new LinkedList<>();
		startTemp.setG(1);
		q.add(startTemp);
		while (q.size() > 0) {
			System.out.println("happened");
			Node currentNode = q.poll();
			if (currentNode.equals(finishTemp)) {
				finishTemp.setParent(currentNode.getParent());
				return PathNodes(finishTemp);
			}
			for (Node neighbour : grid.getNeighbours(currentNode, isDiagonal)) {
				if (neighbour.getG() != 1) {
					neighbour.setG(1);
					neighbour.setParent(currentNode);
					q.add(neighbour);
					grid.get()[neighbour.getY()][neighbour.getX()].setState(Grid.State.OPEN);
				}
			}
			try {
				Thread.sleep(timerValue);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gridP.setGrid(grid);
			gridP.repaint();
		}

		return null;

	}

	private LinkedList<Node> findPathAAsterisk(Node start, Node finish) {
		openNodes.add(start);
		while (openNodes.size() > 0) {
			Node currentNode = getLowestFScore(openNodes);
			openNodes.remove(currentNode);
			closedNodes.add(currentNode);
			grid.get()[currentNode.getY()][currentNode.getX()].setState(Grid.State.CLOSED);

			if (currentNode.equals(finish)) {
				finish.setParent(currentNode.getParent());
				return PathNodes(finish);
			}

			for (Node neighbour : grid.getNeighbours(currentNode, isDiagonal)) {

				if (closedNodes.contains(neighbour))
					continue;
				if (!traverseSamePath)
					if (pathNodes != null)
						if (waypointNodes.contains(neighbour))
							continue;

				float newGCost = currentNode.getG() + getDistance(currentNode, neighbour);
				if (newGCost < neighbour.getG() || !openNodes.contains(neighbour)) {
					neighbour.setG(newGCost);
					neighbour.setH(getDistance(neighbour, finish));
					neighbour.setParent(currentNode);
					if (!openNodes.contains(neighbour)) {
						openNodes.add(neighbour);
						grid.get()[neighbour.getY()][neighbour.getX()].setState(Grid.State.OPEN);
					}
				}
			}

			try {
				Thread.sleep(timerValue);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gridP.setGrid(grid);
			gridP.repaint();
		}
		return null;
	}

	private LinkedList<Node> PathNodes(Node finish) {
		LinkedList<Node> pathNodes = new LinkedList<Node>();
		Node tempNode = finish;
		while (tempNode.getParent() != null) {
			tempNode = tempNode.getParent();
			pathNodes.add(tempNode);
		}
		return pathNodes;
	}

	public float getDistance(Node node, Node node2) {
		float h = (float) Math.sqrt((node.getX() - node2.getX()) * (node.getX() - node2.getX())
				+ (node.getY() - node2.getY()) * (node.getY() - node2.getY()));
		return h;
	}

	public void bubbleSort(List<Node> waypointNodes2) {
		for (int i = waypointNodes2.size() - 1; i > 0; i--) {
			for (int j = 0; j < waypointNodes2.size() - 1; j++) {
				if (waypointNodes2.get(j).getWaypointNumber() > waypointNodes2.get(j + 1).getWaypointNumber()) {
					swap(waypointNodes2, j, j + 1);
				}
			}
		}
	}

	void swap(List<Node> nodes, int i, int j) {
		Node temp = nodes.get(j);
		nodes.set(j, nodes.get(i));
		nodes.set(i, temp);
	}

	public void setAlgorithmType(Type algorithmType) {
		this.algorithmType = algorithmType;
	}

	public boolean isDiagonal() {
		return isDiagonal;
	}

	public void setDiagonal(boolean isDiagonal) {
		this.isDiagonal = isDiagonal;
	}

	public boolean isInvalidNodes() {
		return invalidNodes;
	}

	public boolean isNoPathFound() {
		return noPathFound;
	}

	public boolean isTraverseSamePath() {
		return traverseSamePath;
	}

	public void setTraverseSamePath(boolean traverseSamePath) {
		this.traverseSamePath = traverseSamePath;
	}

}
