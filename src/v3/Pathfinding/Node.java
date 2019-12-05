package v3.Pathfinding;

import v3.Pathfinding.Grid.State;
import v3.Pathfinding.Grid.Type;

public class Node {
	private Node parent;
	private float g, h;
	private int x, y;
	private Type type;
	private State state;
	private int waypointNumber;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Node(int x, int y,int wayNumber) {
		this.x = x;
		this.y = y;
		this.setWaypointNumber(wayNumber);
	} 

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}

	public int getX() {
		return x;
	}

	public void printNode() {
		System.out.println("**********");
		System.out.println("*X:" + x + ",Y:" + y + "*");
		System.out.println("*G:" + g + ",H:" + h + "*");
		System.out.println("**********");
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getF() {
		// TODO Auto-generated method stub
		return this.g + this.h;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	public int getWaypointNumber() {
		return waypointNumber;
	}
	public void setWaypointNumber(int waypointNumber) {
		this.waypointNumber = waypointNumber;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}

}
