package v0;

public class Node {
	public Node parent;
	public float f,g,h;
	public int x,y;
	
	public Node(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public Node(float f) {
		this.f = f;
	}
}
