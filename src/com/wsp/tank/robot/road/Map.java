package com.wsp.tank.robot.road;

import java.util.List;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Rectangle;
import com.wsp.tank.robot.Trajectory;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

public class Map {

	/**
	 * 获取寻路地图
	 * 1，可通过路格为0，非可通过路格为-1
	 * 2，当路格中有坦克时路格为-1
	 * @param map
	 * @param tank
	 * @param tanks
	 * @return
	 */
	public static int[][] getMap(BattleScene battleScene , Tank tank) {
		int[][] map = new int[13][13];
		for(int y = 0 ; y < map.length ; y++) {
			for(int x = 0 ; x < map[y].length ; x++) {
				Rectangle r = new Rectangle(x * 32 + 32, y * 32 + 16, 32, 32);
				if(!isCoordinateRoad(battleScene.map , tank , x , y) || isCollisionTank(battleScene.tanks , tank , r)) {
					map[y][x] = -1;
				}
			}
		}
		return map;
	}
	
	public static int getStarted(Tank tank) {
		int offsetX = 0;
		int offsetY = 0;
		switch(tank.dir) {
			case Spirit.DIR_UP :
				offsetY = 1;
				break;
			case Spirit.DIR_DOWN :
				offsetY = 31;
				break;
			case Spirit.DIR_LEFT :
				offsetX = 31;
				break;
			case Spirit.DIR_RIGHT :
				offsetX = 1;
				break;
		}
		return ((tank.x - 32 + offsetX) / 32) * AStar.DIVISOR + ((tank.y - 16 + offsetY) / 32);
	}
	
	public static int getEnded(Rectangle r) {
		return ((r.x - 32) / 32) * AStar.DIVISOR + ((r.y - 16) / 32);
	}
	
	public static boolean isStartAvailable(Tank tank) {
		return true;
	}
	
	private static boolean isCollisionTank(List<Tank> tanks , Tank tank , Rectangle r) {
		for(Tank _tank : tanks) {
			if(tank != _tank) {
				if(r.intersects(new Rectangle(_tank.x , _tank.y , 32 ,32))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isCoordinateRoad(byte[][] map , Tank tank , int x , int y) {
		for(int _y = y * 4 ; _y < y * 4 + 4 ; _y++) {
			for(int _x = x * 4 ; _x < x * 4 + 4 ; _x++) {
				if(Trajectory.isBrick(map, _x, _y)) {
					return false;
				}
				
				if(Trajectory.isBase(map, _x, _y)) {
					return false;
				}
				
				if(Trajectory.isSteel(map, _x, _y)) {
					return false;
				}
				
				if(!tank.ship) {  //当没有船时
					if(Trajectory.isRivers(map, _x, _y)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static int getDirection(List<Integer> path , Tank tank) {
		int d = 0;
		if(path.size() > 1) {
			d = path.get(1);
		} else {
			d = path.get(0);
		}
		int x = d / AStar.DIVISOR * 32 + 32;
		int y = d % AStar.DIVISOR * 32 + 16;
		int offsetX = 0;
		int offsetY = 0;
		switch(tank.dir) {
			case Spirit.DIR_UP :
				offsetY = 0;
				break;
			case Spirit.DIR_DOWN :
				offsetY = 1;
				break;
			case Spirit.DIR_LEFT :
				offsetX = 0;
				break;
			case Spirit.DIR_RIGHT :
				offsetX = 1;
				break;
		}
		
		if(y < tank.y + offsetY) {
			return Spirit.DIR_UP;
		}
		
		if(y > tank.y + offsetY) {
			return Spirit.DIR_DOWN;
		}
		
		if(x < tank.x + offsetX) {
			return Spirit.DIR_LEFT;
		}
		
		if(x > tank.x + offsetX) {
			return Spirit.DIR_RIGHT;
		}
		
		return tank.dir;
	}
	
}
