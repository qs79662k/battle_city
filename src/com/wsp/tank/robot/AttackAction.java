package com.wsp.tank.robot;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Rectangle;
import com.wsp.tank.Scene;
import com.wsp.tank.spirit.Bullet;
import com.wsp.tank.spirit.PlayerTank;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

/**
 * 攻击
 * @author wsp
 *
 */
public class AttackAction extends Action {
	
	public AttackAction(BattleScene battleScene , PlayerTank playerTank) {
		super(battleScene, playerTank);
	}

	@Override
	public boolean action() {		
		boolean isAttack = isAttack();
		battleScene.toggleKey(Scene.I_KEY_SHOOT + playerTank.on * 5, isAttack);
		return isAttack;
	}
	
	/**
	 * 当前方向是否可以发起攻击
	 * 1，攻击方向存在基地，返回false
	 * 2，攻击方向存在基地护板，返回false
	 * 3，其它，返回true
	 * @return
	 */
	private boolean isAttack() {
		int x = playerTank.x + (32 - 8) / 2;
		int y = playerTank.y + (32 - 8) / 2;
		if(Trajectory.isBaseInTrajectory(playerTank)) {			
			//如果弹道中有敌方坦克，并且弹道中没有基地返回true（可以攻击）
			for(Tank tank : battleScene.tanks) {
				if(playerTank.isEnemy != tank.isEnemy && tank.starCount < 1) {
					Trajectory trajectory = Trajectory.createTrajectory(battleScene.map, playerTank.dir , new Rectangle(x, y, 8, 8) , tank);
					if(trajectory != null && trajectory.trajectoryDecorateCount[Trajectory.BASE_DECORATE] == 0) {	
						return true;
					}
				}
			}
			//如果弹道中有敌方子弹，并且弹道中没有基地返回true（可以攻击）
			for(Bullet bullet : battleScene.bullets) {
				if(playerTank.isEnemy != bullet.isEnemy) {
					Trajectory trajectory = Trajectory.createTrajectory(battleScene.map, playerTank.dir , new Rectangle(x, y, 8, 8) , bullet);
					if(trajectory != null && trajectory.trajectoryDecorateCount[Trajectory.BASE_DECORATE] == 0) {	
						return true;
					}
				}
			}  
			return false;
		}
		
		//前方是否为基地护板，是返回false，否返回true
		if(isBackplate(playerTank.dir, new Rectangle(x , y , 8 , 8))) {
			//如果弹道中有敌方坦克，并且弹道中没有基地返回true（可以攻击）
			for(Tank tank : battleScene.tanks) {
				if(playerTank.isEnemy != tank.isEnemy && tank.starCount < 1) {
					Trajectory trajectory = Trajectory.createTrajectory(battleScene.map, playerTank.dir , new Rectangle(x, y, 8, 8) , tank);
					if(trajectory != null && trajectory.trajectoryDecorateCount[Trajectory.STEEL_DECORATE] == 0) {	
						return true;
					}
				}
			}
			//如果弹道中有敌方子弹，并且弹道中没有基地返回true（可以攻击）
			for(Bullet bullet : battleScene.bullets) {
				if(playerTank.isEnemy != bullet.isEnemy) {
					Trajectory trajectory = Trajectory.createTrajectory(battleScene.map, playerTank.dir , new Rectangle(x, y, 8, 8) , bullet);
					if(trajectory != null && trajectory.trajectoryDecorateCount[Trajectory.STEEL_DECORATE] == 0) {	
						return true;
					}
				}
			}  
			return false;
		}
		
		return true;
	}
	
	/**
	 * 当前攻击方向是否为基地护板(护板指基地正上方的钢块装饰物)
	 * @param dir
	 * @param bullet
	 * @return
	 */
	private boolean isBackplate(int dir , Rectangle bullet) {
		byte[][] map = battleScene.map;
		int _x = (bullet.x - 32) / 8;
		int _y = (bullet.y - 16) / 8;
		switch(dir) {
			case Spirit.DIR_UP :
				_y += (bullet.y - 16) % 8 == 0 ? 0 : 1;
				if(bullet.x >= 192 && bullet.x <= 256) {
					for(int y = _y ; y > -1 ; y--) {
						if(Trajectory.isSteel(map, _x, y) || Trajectory.isSteel(map, _x + 1, y)) {
							return true;
						}
						
						if(map[y][_x] != 0 || map[y][_x + 1] != 0) {
							return false;
						}
					}
				}
				return false;
			case Spirit.DIR_DOWN :
				if(bullet.x >= 192 && bullet.x <= 256) {
					for(int y = _y ; y < map.length ; y++) {
						if(Trajectory.isSteel(map, _x, y) || Trajectory.isSteel(map, _x + 1, y)) {
							return true;
						}
						
						if(map[y][_x] != 0 || map[y][_x + 1] != 0) {
							return false;
						}
					}
				}
				return false;
			case Spirit.DIR_LEFT :
				_x += (bullet.x - 32) % 8 == 0 ? 0 : 1;
				if(bullet.x >= 192) {
					for(int x = _x ; x > -1 ; x--) {
						if(Trajectory.isSteel(map, x, _y) || Trajectory.isSteel(map, x, _y + 1)) {
							return true;
						}
						
						if(map[_y][x] != 0 || map[_y + 1][x] != 0) {
							return false;
						}
					}
				}
				return false;
			case Spirit.DIR_RIGHT :
				if(bullet.x <= 256) {
					for(int x = _x ; x < map[0].length ; x++) {
						if(Trajectory.isSteel(map, x, _y) || Trajectory.isSteel(map, x, _y + 1)) {
							return true;
						}
						
						if(map[_y][x] != 0 || map[_y + 1][x] != 0) {
							return false;
						}
					}
				}
				return false;
		}
		return false;
	}

}
