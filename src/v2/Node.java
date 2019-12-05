package v2;

 class Node {
	private Node parent;
	private float f, g, h;
	private int x, y;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Node(int x, int y, float f) {
		this.x = x;
		this.y = y;
		this.f = f;
	}

	public void printNode() {
		System.out.println("**********");
		System.out.println("*X:" + x + ",Y:" + y + "*");
		System.out.println("*G:" + g + ",H:" + h + "*");
		System.out.println("**********");
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

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void calculateF() {
		// TODO Auto-generated method stub
		this.f = this.g +  this.h;
	}

	public float getF() {
		// TODO Auto-generated method stub
		return f;
	}

}
