package com.wsp.tank.robot;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Rectangle;
import com.wsp.tank.Scene;
import com.wsp.tank.spirit.Bullet;
import com.wsp.tank.spirit.PlayerTank;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

/**
 * ����
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
	 * ��ǰ�����Ƿ���Է��𹥻�
	 * 1������������ڻ��أ�����false
	 * 2������������ڻ��ػ��壬����false
	 * 3������������true
	 * @return
	 */
	private boolean isAttack() {
		int x = playerTank.x + (32 - 8) / 2;
		int y = playerTank.y + (32 - 8) / 2;
		if(Trajectory.isBaseInTrajectory(playerTank)) {			
			//����������ез�̹�ˣ����ҵ�����û�л��ط���true�����Թ�����
			for(Tank tank : battleScene.tanks) {
				if(playerTank.isEnemy != tank.isEnemy && tank.starCount < 1) {
					Trajectory trajectory = Trajectory.createTrajectory(battleScene.map, playerTank.dir , new Rectangle(x, y, 8, 8) , tank);
					if(trajectory != null && trajectory.trajectoryDecorateCount[Trajectory.BASE_DECORATE] == 0) {	
						return true;
					}
				}
			}
			//����������ез��ӵ������ҵ�����û�л��ط���true�����Թ�����
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
		
		//ǰ���Ƿ�Ϊ���ػ��壬�Ƿ���false���񷵻�true
		if(isBackplate(playerTank.dir, new Rectangle(x , y , 8 , 8))) {
			//����������ез�̹�ˣ����ҵ�����û�л��ط���true�����Թ�����
			for(Tank tank : battleScene.tanks) {
				if(playerTank.isEnemy != tank.isEnemy && tank.starCount < 1) {
					Trajectory trajectory = Trajectory.createTrajectory(battleScene.map, playerTank.dir , new Rectangle(x, y, 8, 8) , tank);
					if(trajectory != null && trajectory.trajectoryDecorateCount[Trajectory.STEEL_DECORATE] == 0) {	
						return true;
					}
				}
			}
			//����������ез��ӵ������ҵ�����û�л��ط���true�����Թ�����
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
	 * ��ǰ���������Ƿ�Ϊ���ػ���(����ָ�������Ϸ��ĸֿ�װ����)
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
