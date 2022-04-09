package com.wsp.tank.robot.road;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class AStar {

	public final static int ROAD = 0;	
	public final static int BAR = 1;	
	public final static int STEP = 10;	
	public final static int DIVISOR = 14;
	private int[][] map;  //13 * 13
	private int start;  //  x/14£«y%14
	private int end;  //  x/14£«y%14
	private List<Node> openList = new ArrayList<Node>();  
	private List<Node> closeList = new ArrayList<Node>();
	
	public AStar() {}
	
	public AStar(int[][] map , int start , int end) {
		this.map = map;
		this.start = start;
		this.end = end;
	}
	
	public List<Integer> startFindPath(int[][] map , int start , int end) {
		this.map = map;
		this.start = start;
		this.end = end;
		return startFindPath();
	}
	
	public List<Integer> startFindPath() {
		openList.add(new Node(start / DIVISOR, start % DIVISOR));
		while(openList.size() > 0) {
			Node parentNode = findParentNode();
			if(parentNode.x == end / DIVISOR && parentNode.y == end % DIVISOR) {
				return findPath();
			}
			addChildNode(parentNode);
		}
		openList.clear();
		closeList.clear();
		return null;
	}
	
	private Node findParentNode() {
		Node parentNode = openList.get(0);
		for(Node node : openList) {
			if(node.f <= parentNode.f) {
				parentNode = node;
			}
		}
		closeList.add(parentNode);
		openList.remove(parentNode);
		return parentNode;
	}
	
	private void addChildNode(Node parentNode) {
		//ÉÏ
		int x = parentNode.x;
		int y = parentNode.y - 1;
		if(!exist(x, y) && !exist(parentNode, x, y)) {
			Node node = new Node(x, y , parentNode);
			node.g = parentNode.g + STEP;
			node.h = getH(x , y , end);
			node.f = node.g + node.h;
			openList.add(node);
		}
		
		//ÏÂ
		x = parentNode.x;
		y = parentNode.y + 1;
		if(!exist(x, y) && !exist(parentNode, x, y)) {
			Node node = new Node(x, y , parentNode);
			node.g = parentNode.g + STEP;
			node.h = getH(x , y , end);
			node.f = node.g + node.h;
			openList.add(node);
		}
		
		//×ó
		x = parentNode.x - 1;
		y = parentNode.y;
		if(!exist(x, y) && !exist(parentNode, x, y)) {
			Node node = new Node(x, y , parentNode);
			node.g = parentNode.g + STEP;
			node.h = getH(x , y , end);
			node.f = node.g + node.h;
			openList.add(node);
		}
		
		//ÓÒ
		x = parentNode.x + 1;
		y = parentNode.y;
		if(!exist(x, y) && !exist(parentNode, x, y)) {
			Node node = new Node(x, y , parentNode);
			node.g = parentNode.g + STEP;
			node.h = getH(x , y , end);
			node.f = node.g + node.h;
			openList.add(node);
		}
	}
	
	private boolean exist(int x , int y) {
		if(x > -1 && x < map[0].length && y > -1 && y < map.length) {
			return map[y][x] != 0;
		}
		return true;
	}
	
	private boolean exist(Node parentNode , int x , int y) {
		for(Node node : closeList) {
			if(node.x == x && node.y == y) {
				return true;
			}
		}
		
		for(Node node : openList) {
			if(node.x == x && node.y == y) {
				if(node.g > parentNode.g + STEP) {
					node.parent = parentNode;
					node.g = parentNode.g + STEP;
					node.f = node.g + node.h;
				}
				return true;
			}
		}
		
		return false;
	}
	
	private int getH(int x , int y , int end) {
		return (Math.abs(x - end / DIVISOR) + Math.abs(y - end % DIVISOR)) * STEP;
	}
	
	private List<Integer> findPath() {
		List<Integer> path = new ArrayList<Integer>();
		Node parentNode = closeList.get(closeList.size() - 1);
		path.add(parentNode.x * DIVISOR + parentNode.y);
		while(closeList.size() > 0) {
			for(int i = 0 ; i < closeList.size() ; i++) {
				Node node = closeList.get(i);
				if(node.equals(parentNode)) {
					parentNode = node.parent;
					if(parentNode != null) {
						path.add(parentNode.x * DIVISOR + parentNode.y);
					} else {
						openList.clear();
						closeList.clear();
					}
				}
			}
		}
		Collections.reverse(path);
		return path;
	}
	
}
