package com.wsp.tank.robot;

import java.util.Random;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Rectangle;
import com.wsp.tank.spirit.PlayerTank;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

/**
 * 巡逻
 * @author wsp
 *
 */
public class PatrolAction extends Action {

	private int step;
	private Random random = new Random();
	
	public PatrolAction(BattleScene battleScene , PlayerTank playerTank) {
		super(battleScene, playerTank);
	}
	
	@Override
	public boolean action() {
		if(!aim()) {
			patrol();
		}
		return true;
	}
	
	/**
	 * 巡逻
	 */
	private void patrol() {
		int dir = -1;
		//生成坦克的步数与方向
		if(playerTank.starCount < 1) {
			if(step > 0) {
				step--;
			} else {		
				dir = random.nextInt(4);
				step = random.nextInt(60) + 10;				
			}
		}
		
		if(dir == -1) {
			dir = playerTank.dir;
		}
	
		//当方向存在危险时生成随机方向，如果所有方向都存在危险将会停止移动
		if(!Danger.isDirectionDanger(battleScene, dir, playerTank.isEnemy, playerTank.getRectangle(dir))) {
			move(dir);
		} else {
			int[] arr = getRandomDirectionArray(dir);
			for(int i = 0 ; i < arr.length ; i++) {
				int _dir = arr[i];
				if(!Danger.isDirectionDanger(battleScene, _dir, playerTank.isEnemy, playerTank.getRectangle(_dir))) {
					move(_dir);
					return;
				}
			}
		}
	}
	
	/**
	 * 瞄准敌人
	 * @return
	 */
	private boolean aim() {
		int score = 999;
		int dir = playerTank.dir;
		for(Tank tank : battleScene.tanks) {
			if(playerTank.isEnemy != tank.isEnemy && tank.starCount < 1) {
				int x = playerTank.x + (32 - 8) / 2;
				int y = playerTank.y + (32 - 8) / 2;
				if(playerTank.dir == Spirit.DIR_UP || playerTank.dir == Spirit.DIR_DOWN) {
					if(tank.x + tank.width < playerTank.x || tank.x > playerTank.x + playerTank.width) {
						y = playerTank.y / 16 * 16;
						if(playerTank.y - y > 8) {
							y = (playerTank.y / 16 + 1) * 16;
						}
						y += (32 - 8) / 2;
					}
				} else {
					if(tank.y + tank.height < playerTank.y || tank.y > playerTank.x + playerTank.height) {
						x = playerTank.x / 16 * 16;
						if(playerTank.x - x > 8) {
							x = (playerTank.x / 16 + 1) * 16;
						}
						x += (32 - 8) / 2;
					}
				}
				Trajectory t1 = Trajectory.createTrajectory(battleScene.map, new Rectangle(x, y, 8, 8) , tank);
				x -= (32 - 8) * 2;
				y -= (32 - 8) * 2;
				int _x = tank.x + (32 - 8) / 2;
				int _y = tank.y + (32 - 8) / 2;
				Trajectory t2 = Trajectory.createTrajectory(battleScene.map , tank.dir , new Rectangle(_x , _y , 8 , 8) , new Rectangle(x , y, 32 , 32));
				if(t1 != null && t2 == null) {
					int tempScore = Danger.getTrajectorySafeScore(t1);
					if(tempScore < score) {
						score = tempScore;
						dir = t1.dir;
					}
				}
			}
		}

		if(score < 52) {
			//当与坦克方向不一至时调整方向瞄准敌人
			if(playerTank.dir != dir) {
				if(!Danger.isDanger(battleScene, playerTank.isEnemy, playerTank.getRectangle(dir))) {
					move(dir);
				}
			}
			//如果向瞄准方向前进时有危险则停止移动
			if(!Danger.isDirectionDanger(battleScene, dir, playerTank.isEnemy, playerTank.getRectangle(dir))) {
				Trajectory t = getShortTrajectory(dir);
				if(t != null) {
					if(t.length > Bullet.speed * 6) {   //>96为安全攻击距离，大于安全距离才会向瞄准移动
						move(dir);
					}
				}
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取当前瞄准方向上最短的弹道
	 * @param dir
	 * @return
	 */
	private Trajectory getShortTrajectory(int dir) {
		int score = 999;
		Trajectory t = null;
		for(Tank tank : battleScene.tanks) {
			if(playerTank.isEnemy != tank.isEnemy && tank.starCount < 1) {
				int x = playerTank.x + (32 - 8) / 2;
				int y = playerTank.y + (32 - 8) / 2;
				if(playerTank.dir == Spirit.DIR_UP || playerTank.dir == Spirit.DIR_DOWN) {
					y = playerTank.y / 16 * 16;
					if(playerTank.y - y > 8) {
						y = (playerTank.y / 16 + 1) * 16;
					}
					y += (32 - 8) / 2;
				} else {
					x = playerTank.x / 16 * 16;
					if(playerTank.x - x > 8) {
						x = (playerTank.x / 16 + 1) * 16;
					}
					x += (32 - 8) / 2;
				}
				Trajectory tempTrajectory = Trajectory.createTrajectory(battleScene.map, dir , new Rectangle(x, y, 8, 8) , tank);
				if(tempTrajectory != null) {
					int tempScore = Danger.getTrajectorySafeScore(tempTrajectory);
					if(tempScore < score) {
						score = tempScore;
						t = tempTrajectory;
					}
				}
			}
		}
		return t;
	}
	
	/**
	 * 获取随机方向数组
	 * @param dir 被排除的方向
	 * @return
	 */
	private int[] getRandomDirectionArray(int dir) {
		int index = 0;
		int[] arr = new int[3];
		for(int i = 0 ; i < 4 ; i++) {
			if(i != dir) {
				arr[index++] = i;
			}
		}
		//乱序方向
		Random random = new Random();
		for(int i = arr.length - 1 ; i > 0 ; i--) {
			int r = random.nextInt(i + 1);
			int temp = arr[i];
			arr[i] = arr[r];
			arr[r] = temp;
		}
		return arr;
	}
	
}
