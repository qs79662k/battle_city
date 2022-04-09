package com.wsp.tank.robot;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Rectangle;
import com.wsp.tank.spirit.Bullet;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

public class Danger {
	
	/**
	 * 获取当前方向坦克安全得分(得分越高越安全 )
	 * @param battleScene
	 * @param dir 当前坦克行进方向
	 * @param isEnemy 
	 * @param tank 当前坦克
	 * @return
	 */
	public static int getDirectionScore(BattleScene battleScene , int dir , boolean isEnemy , Rectangle tank) {
		int score = 999;
		for(Bullet bullet : battleScene.bullets) {
			if(isEnemy != bullet.isEnemy) {
				Trajectory t = Trajectory.createTrajectory(battleScene.map , bullet.dir , bullet , offsetDirection(dir , tank));
				if(t != null) {
					int tempScore = getTrajectorySafeScore(t);
					if(tempScore < score) {
						score = tempScore;
					}
				}
			}
		}
		
		for(Tank _tank : battleScene.tanks) {
			if(isEnemy != _tank.isEnemy && _tank.starCount < 1) {
				int _x = _tank.x + (32 - 8) / 2;
				int _y = _tank.y + (32 - 8) / 2;
				Trajectory t = Trajectory.createTrajectory(battleScene.map , _tank.dir , new Rectangle(_x , _y , 8 , 8) , offsetDirection(dir , tank));
				if(t != null) {
					int tempScore = getTrajectorySafeScore(t);
					if(tempScore < score) {
						score = tempScore;
					}
				}
			}
		}
		
		return score;
	}
	
	/**
	 * 当前坦克的进行方向是否存在危险
	 * @param battleScene
	 * @param dir
	 * @param isEnemy
	 * @param tank
	 * @return 存在危险返回true，否则返回false
	 */
	public static boolean isDirectionDanger(BattleScene battleScene , int dir , boolean isEnemy , Rectangle tank) {
		return isDanger(battleScene , isEnemy , offsetDirection(dir , tank));
	}

	/**
	 * 当前坦克所在位置是否存在危险
	 * @param battleScene
	 * @param isEnemy
	 * @param tank
	 * @return 存在危险返回true，否则返回false
	 */
	public static boolean isDanger(BattleScene battleScene , boolean isEnemy , Rectangle tank) {
		for(Bullet bullet : battleScene.bullets) {
			if(isEnemy != bullet.isEnemy) {
				Trajectory t = Trajectory.createTrajectory(battleScene.map , bullet.dir , bullet , tank);
				if(t != null) {
					int score = getTrajectorySafeScore(t);
					if(isDanger(score)) {
						return true;
					}
				}
			}
		}
		
		for(Tank _tank : battleScene.tanks) {
			if(isEnemy != _tank.isEnemy && _tank.starCount < 1) {
				int _x = _tank.x + (32 - 8) / 2;
				int _y = _tank.y + (32 - 8) / 2;
				Trajectory t = Trajectory.createTrajectory(battleScene.map , _tank.dir , new Rectangle(_x , _y , 8 , 8) , tank);
				if(t != null) {
					int score = getTrajectorySafeScore(t) + 10;
					if(isDanger(score)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 获取当前坦克行进方向的偏移数据
	 * @param dir
	 * @param tank
	 * @return
	 */
	public static Rectangle offsetDirection(int dir , Rectangle tank) {
		switch(dir) {
			case Spirit.DIR_UP :
				return new Rectangle(tank.x , tank.y - tank.height , tank.width , tank.height);
			case Spirit.DIR_DOWN :
				return new Rectangle(tank.x , tank.y + tank.height , tank.width , tank.height);
			case Spirit.DIR_LEFT :
				return new Rectangle(tank.x - tank.width , tank.y , tank.width , tank.height);
			case Spirit.DIR_RIGHT :
				return new Rectangle(tank.x + tank.width , tank.y , tank.width , tank.height);
		}
		return tank;
	}
	
	public static boolean isDanger(int score) {
		if(score < 50) {
			return true;
		}
		return false;
	}
	
	public static int getTrajectorySafeScore(Trajectory t) {
		int score = t.trajectoryDecorateCount[Trajectory.NOT_DECORATE] * 1;
		score += t.trajectoryDecorateCount[Trajectory.BRICK_DECORATE] * 15;
		score += t.trajectoryDecorateCount[Trajectory.STEEL_DECORATE] * 50;
		score += t.trajectoryDecorateCount[Trajectory.TREE_DECORATE] * 1;
		return score;
	}
	
}
