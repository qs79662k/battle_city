package com.wsp.tank;

public class Rectangle {

	public int x;
	public int y;
	public final int width;
	public final int height;
	
//	public Rectangle() {}
	
	public Rectangle(int x , int y , int width , int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean intersects(Rectangle r) {
		if(x <= r.x && x + width - 1 >= r.x && ((y <= r.y && y + height - 1 >= r.y) || (y <= r.y + r.height - 1 && y + height - 1 >= r.y + r.height - 1) || (y >= r.y && y + height - 1 <= r.y + r.height - 1))) {
			return true;
		}
		
		if(x <= r.x + r.width - 1 && x + width - 1 >= r.x + r.width - 1 && ((y <= r.y && y + height - 1 >= r.y) || (y <= r.y + r.height - 1 && y + height - 1 >= r.y + r.height - 1) || (y >= r.y && y + height - 1 <= r.y + r.height - 1))) {
			return true;
		}
		
		if(x >= r.x && x + width - 1 <= r.x + r.width - 1 && ((y <= r.y && y + height - 1 >= r.y) || (y <= r.y + r.height - 1 && y + height - 1 >= r.y + r.height - 1) || (y >= r.y && y + height - 1 <= r.y + r.height - 1))) {
			return true;
		}
		
		if(y <= r.y && y + height - 1 >= r.y && ((x <= r.x && x + width - 1 >= r.x) || (x <= r.x + r.width - 1 && x + width - 1 >= r.x + r.width - 1) || (x >= r.x && x + width - 1 <= r.x + r.width - 1))) {
			return true;
		}
		
		if(y <= r.y + r.height - 1 && y + height - 1 >= r.y + r.height - 1 && ((x <= r.x && x + width - 1 >= r.x) || (x <= r.x + r.width - 1 && x + width - 1 >= r.x + r.width - 1) || (x >= r.x && x + width - 1 <= r.x + r.width - 1))) {
			return true;
		}
		
		if(y >= r.y && y + height - 1 <= r.y + r.height - 1 && ((x <= r.x && x + width - 1 >= r.x) || (x <= r.x + r.width - 1 && x + width - 1 >= r.x + r.width - 1) || (x >= r.x && x + width - 1 <= r.x + r.width - 1))) {
			return true;
		}
		
		return false;
	}
	
	public boolean isUpIntersects(Rectangle up) {
		if(((x <= up.x && x + width - 1 >= up.x) || (x <= up.x + up.width - 1 && x + width - 1 >= up.x + up.width - 1)|| (x >= up.x && x + width - 1 <= up.x + up.width - 1)) && (y >= up.y && y <= up.y + up.height - 1)) {
			return true;
		}
		return false;
	}
	
	public boolean isDownIntersects(Rectangle down) {
		if(((x <= down.x && x + width - 1 >= down.x) || (x <= down.x + down.width - 1 && x + width - 1 >= down.x + down.width - 1) || (x >= down.x && x + width - 1 <= down.x + down.width - 1)) && (y + height - 1 >= down.y && y + height - 1 <= down.y + down.height - 1)) {
			return true;
		}
		return false;
	}
	
	public boolean isLeftIntersects(Rectangle left) {
		if(((y <= left.y && y + height - 1 >= left.y) || (y <= left.y + left.height - 1 && y + height - 1 >= left.y + left.height - 1) || (y >= left.y && y + height - 1 <= left.y + left.height - 1)) && (x >= left.x && x <= left.x + left.width - 1)) {
			return true;
		}
		return false;
	}
	
	public boolean isRightIntersects(Rectangle right) {
		if(((y <= right.y && y + height - 1 >= right.y) || (y <= right.y + right.height - 1 && y + height - 1 >= right.y + right.height - 1) || (y >= right.y && y + height - 1 <= right.y + right.height - 1)) && (x + width - 1 >= right.x && x + width - 1 <= right.x + right.width - 1)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 相对相交
	 * @param up
	 * @return
	 */
	public boolean isUpRelativeIntersects(Rectangle up) {
		if(y < up.y + up.height - 1) {
			return false;
		}
		
		if((x <= up.x && x + width - 1 >= up.x) || (x <= up.x + up.width - 1 && x + width - 1 >= up.x + up.width - 1) || (x >= up.x && x + width - 1 <= up.x + up.width - 1)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isDownRelativeIntersects(Rectangle down) {
		if(y > down.y) {
			return false;
		}
		
		if((x <= down.x && x + width - 1 >= down.x) || (x <= down.x + down.width - 1 && x + width - 1 >= down.x + down.width - 1) || (x >= down.x && x + width - 1 <= down.x + down.width - 1)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isLeftRelativeIntersects(Rectangle left) {
		if(x < left.x + left.width - 1) {
			return false;
		}
		
		if((y <= left.y && y + height - 1 >= left.y) || (y <= left.y + left.height - 1 && y + height - 1 >= left.y + left.height - 1) || (y >= left.y && y + height - 1 <= left.y + left.height - 1)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isRightRelativeIntersects(Rectangle right) {
		if(x > right.x) {
			return false;
		}
		
		if((y <= right.y && y + height - 1 >= right.y) || (y <= right.y + right.height - 1 && y + height - 1 >= right.y + right.height - 1) || (y >= right.y && y + height - 1 <= right.y + right.height - 1)) {
			return true;
		}
		
		return false;
	}
	
}
