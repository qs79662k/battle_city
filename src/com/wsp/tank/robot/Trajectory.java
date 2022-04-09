package com.wsp.tank.robot;

import com.wsp.tank.Map;
import com.wsp.tank.Rectangle;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

public class Trajectory {

	public final static int NOT_DECORATE = 0;  //无
	public final static int TREE_DECORATE = 1; //树
	public final static int RIVERS_DECORATE = 2; //河流
	public final static int BRICK_DECORATE = 3;	//砖
	public final static int STEEL_DECORATE = 4;	//钢
	public final static int BASE_DECORATE = 5;	//基地
	protected int dir;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int length;
	protected int[] trajectoryDecorateCount = new int[6];
	
	public static Trajectory createTrajectory(byte[][] map , Rectangle bullet , Rectangle body) {	
		if(bullet.isUpRelativeIntersects(body)) {
			return createTrajectory(map , Spirit.DIR_UP , bullet , body);
		}
		
		if(bullet.isDownRelativeIntersects(body)) {
			return createTrajectory(map , Spirit.DIR_DOWN , bullet , body);
		}
		
		if(bullet.isLeftRelativeIntersects(body)) {
			return createTrajectory(map , Spirit.DIR_LEFT , bullet , body);
		}
		
		if(bullet.isRightRelativeIntersects(body)) {
			return createTrajectory(map , Spirit.DIR_RIGHT , bullet , body);
		}
		
		return null;
	}
	
	public static Trajectory createTrajectory(byte[][] map , int dir , Rectangle bullet , Rectangle body) {
		Trajectory t = new Trajectory();
		t.dir = dir;
		
		switch(dir) {
			case Spirit.DIR_UP :
				if(!bullet.isUpRelativeIntersects(body)) {
					return null;
				}
				break;
			case Spirit.DIR_DOWN :
				if(!bullet.isDownRelativeIntersects(body)) {
					return null;
				}
				break;
			case Spirit.DIR_LEFT :	
				if(!bullet.isLeftRelativeIntersects(body)) {
					return null;
				}
				break;
			case Spirit.DIR_RIGHT :		
				if(!bullet.isRightRelativeIntersects(body)) {
					return null;
				}
				break;	
		}
		
		int start = 0 , end = 0 , offset = 0 , width = bullet.width / 8;
		switch(dir) {
			case Spirit.DIR_UP :
				start = (body.y + body.height - 16) / 8;  
				start += start > 0 ? -1 : 0;
				end = (bullet.y - 16) / 8;
				offset = (bullet.x - 32) / 8;
				width += (bullet.x - 32) % 8 == 0 ? 0 : 1;
				t.x = bullet.x;
				t.y = body.y + body.height;
				t.width = bullet.width;
				t.height = bullet.y - (body.y + body.height);
				t.length = t.height;
				break;
			case Spirit.DIR_DOWN :
				start = (bullet.y + bullet.height - 16) / 8;
				end = (body.y - 16) / 8;
				end += end < map.length - 1 ? 1 : 0;
				offset = (bullet.x - 32) / 8;
				width += (bullet.x - 32) % 8 == 0 ? 0 : 1;
				t.x = bullet.x;
				t.y = bullet.y + bullet.height;
				t.width = bullet.width;
				t.height = body.y - (bullet.y + bullet.height);		
				t.length = t.height;
				break;
			case Spirit.DIR_LEFT :
				start = (body.x + body.width - 32) / 8;
				start += start > 0 ? -1 : 0;
				end = (bullet.x - 32) / 8;
				offset = (bullet.y - 16) / 8;
				width = bullet.height / 8;
				width += (bullet.y - 16) % 8 == 0 ? 0 : 1;
				t.x = body.x + body.width;
				t.y = bullet.y;
				t.width = bullet.x - (body.x + body.width);
				t.height = bullet.height;
				t.length = t.width;
				break;
			case Spirit.DIR_RIGHT :
				start = (bullet.x + bullet.width - 32) / 8;
				end = (body.x - 32) / 8;
				end += end < map[0].length - 1 ? 1 : 0;
				offset = (bullet.y - 16) / 8;
				width = bullet.height / 8;
				width += (bullet.y - 16) % 8 == 0 ? 0 : 1;
				t.x = bullet.x + bullet.width;
				t.y = bullet.y;
				t.width = body.x - (bullet.x + bullet.width);
				t.height = bullet.height;
				t.length = t.width;
				break;	
		}
	
		for(int i = start ; i < end ; i++) {
			int trajectoryDecorateType = NOT_DECORATE;
			for(int j = 0 ; j < width ; j++) {
				int x = 0 , y = 0;
				if(Spirit.DIR_UP == dir || Spirit.DIR_DOWN == dir) {
					x = j + offset;
					y = i;
				} else {
					x = i;
					y = j + offset;
				}
							
				if(isTree(map , x , y)) {
					if(trajectoryDecorateType == NOT_DECORATE) {
						trajectoryDecorateType = TREE_DECORATE;
					}	
				} else if(isRivers(map, x, y)) {
					if(trajectoryDecorateType == NOT_DECORATE || trajectoryDecorateType == TREE_DECORATE) {
						trajectoryDecorateType = RIVERS_DECORATE;
					}
				} else if(isBrick(map , x , y)) {
					if(trajectoryDecorateType == NOT_DECORATE || trajectoryDecorateType == TREE_DECORATE || trajectoryDecorateType == RIVERS_DECORATE) {
						trajectoryDecorateType = BRICK_DECORATE;
					}
				} else if(isSteel(map , x , y)) {
					if(trajectoryDecorateType == NOT_DECORATE || trajectoryDecorateType == TREE_DECORATE || trajectoryDecorateType == BRICK_DECORATE|| trajectoryDecorateType == RIVERS_DECORATE) {
						trajectoryDecorateType = STEEL_DECORATE;
					}
				} else if(isBase(map , x , y)) {
					if(trajectoryDecorateType == NOT_DECORATE || trajectoryDecorateType == TREE_DECORATE || trajectoryDecorateType == BRICK_DECORATE || trajectoryDecorateType == STEEL_DECORATE|| trajectoryDecorateType == RIVERS_DECORATE) {
						trajectoryDecorateType = BASE_DECORATE;
					}
				} 
			}			
			t.trajectoryDecorateCount[trajectoryDecorateType]++;
		}
	
		return t;
	}
	
	public static boolean isBaseInTrajectory(Tank tank) {		
		switch(tank.dir) {
			case Spirit.DIR_UP :
				return false;
			case Spirit.DIR_DOWN :
				if(tank.x >= 192 && tank.x <= 256) {
					return true;
				}
				return false;
			case Spirit.DIR_LEFT :
				if(tank.y >= 368 && tank.x >= 208) {
					return true;
				}
				return false;
			case Spirit.DIR_RIGHT :
				if(tank.y >= 368 && tank.x <= 240) {
					return true;
				}
				return false;
		}
		
		return false;
	}
	
	public static boolean isBrick(byte[][] map , int x , int y) {
		for(int i = 0 ; i < Map.BRICK.length ; i++) {
			if(map[y][x] == Map.BRICK[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isSteel(byte[][] map , int x , int y) {
		for(int i = 0 ; i < Map.STEEL.length ; i++) {
			if(map[y][x] == Map.STEEL[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isRivers(byte[][] map , int x , int y) {
		for(int i = 0 ; i < Map.RIVERS.length ; i++) {
			if(map[y][x] == Map.RIVERS[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isTree(byte[][] map , int x , int y) {
		for(int i = 0 ; i < Map.TREE.length ; i++) {
			if(map[y][x] == Map.TREE[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBase(byte[][] map , int x , int y) {
		for(int i = 0 ; i < Map.BASE.length ; i++) {
			if(map[y][x] == Map.BASE[i]) {
				return true;
			}
		}
		return false;
	}
	
}
