package com.wsp.tank.robot.road;

public class Node {

	protected int x;
	protected int y;
	protected int f;
	protected int g;
	protected int h;
	protected Node parent;
	
	public Node(int x , int y) {
		this(x, y, null);
	}
	
	public Node(int x , int y , Node parent) {
		this.x  = x;
		this.y = y;
		this.parent = parent;
	}
	
}
