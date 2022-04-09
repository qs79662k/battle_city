package com.wsp.tank.robot;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Rectangle;
import com.wsp.tank.spirit.Bullet;
import com.wsp.tank.spirit.PlayerTank;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

public class DodgeAction extends Action {
	
	public final static int SAFE_SCORE = 35;
	private int reticent;  //当发现危险时，此值将会被设置成20。此值只要大于0，action方法将会一直返回true
	
	public DodgeAction(BattleScene battleScene , PlayerTank playerTank) {
		super(battleScene, playerTank);
	}
	
	@Override
	public boolean action() {
		if(reticent > 0) {
			reticent--;
		}
		
		boolean isDanger = Danger.isDanger(battleScene , playerTank.isEnemy , playerTank);
		if(isDanger) { 
			reticent = 20;
			findSafeDirection();
			return true;
		} 
		
		if(reticent > 0) {
			move(playerTank.dir);
			return true;
		}
		
		return false;
	}

	/**
	 * 找最安全的方向
	 * @return
	 */
	private boolean findSafeDirection() {
		//得分越高的方向越安全
		Rectangle r = null;
		int[] scores = {999 , 999 , 999 , 999};
		//被子弹攻击
		for(Bullet bullet : battleScene.bullets) {
			if(playerTank.isEnemy != bullet.isEnemy) {
				Trajectory t = Trajectory.createTrajectory(battleScene.map , bullet.dir , bullet , playerTank);
				if(t != null) {
					int score = Danger.getTrajectorySafeScore(t);
					if(scores[t.dir] > score) {
						r = bullet;
						scores[t.dir] = score;
					}
				}
			}
		}
		
		//敌人朝向自己
		for(Tank tank : battleScene.tanks) {
			if(playerTank.isEnemy != tank.isEnemy && tank.starCount < 1) {
				int x = tank.x + (32 - 8) / 2;
				int y = tank.y + (32 - 8) / 2;
				Trajectory t = Trajectory.createTrajectory(battleScene.map , tank.dir , new Rectangle(x , y , 8 , 8) , playerTank);
				if(t != null) {
					int score = Danger.getTrajectorySafeScore(t);
					if(scores[t.dir] > score) {
						r = new Rectangle(x , y , 8 , 8);
						scores[t.dir] = score;
					}
				}
			}
		}
		
		int dir = getMostDangerDirection(scores);		//最危险的方向
		if(dir == Spirit.DIR_UP || dir == Spirit.DIR_DOWN) {
			int y = playerTank.y;
			if(playerTank.dir == Spirit.DIR_UP || playerTank.dir == Spirit.DIR_DOWN) {
				y = playerTank.y / 16 * 16;
				if(playerTank.y - y > 8) {
					y = (playerTank.y / 16 + 1) * 16;
				}
			}
			int lGap = Math.min(tankGap(Spirit.DIR_LEFT , playerTank.x , y), decorateGap(Spirit.DIR_LEFT , playerTank.x , y));
			int rGap = Math.min(tankGap(Spirit.DIR_RIGHT , playerTank.x , y), decorateGap(Spirit.DIR_RIGHT , playerTank.x , y));
			boolean isRight = (playerTank.x + playerTank.width / 2 > r.x + r.width / 2);
			int lScore = Danger.getDirectionScore(battleScene, Spirit.DIR_LEFT , playerTank.isEnemy , new Rectangle(playerTank.x , y , playerTank.width , playerTank.height));
			int rScore = Danger.getDirectionScore(battleScene, Spirit.DIR_RIGHT , playerTank.isEnemy , new Rectangle(playerTank.x , y , playerTank.width , playerTank.height));											
			if(playerTank.x + playerTank.width - lGap + 1 < r.x && lScore > SAFE_SCORE) {
				if(!isRight || (isRight && rScore < SAFE_SCORE)) {				
					move(Spirit.DIR_LEFT);
					return true;
				} 
			}
			
			if(playerTank.x + rGap - 1 > r.x + r.width && rScore > SAFE_SCORE) {			
				move(Spirit.DIR_RIGHT);
				return true;
			}			
		} else {
			int x = playerTank.x;
			if(playerTank.dir == Spirit.DIR_LEFT || playerTank.dir == Spirit.DIR_RIGHT) {
				x = playerTank.x / 16 * 16;
				if(playerTank.x - x > 8) {
					x = (playerTank.x / 16 + 1) * 16;
				}
			}
			int uGap = Math.min(tankGap(Spirit.DIR_UP , x , playerTank.y), decorateGap(Spirit.DIR_UP , x , playerTank.y));
			int dGap = Math.min(tankGap(Spirit.DIR_DOWN , x , playerTank.y), decorateGap(Spirit.DIR_DOWN , x , playerTank.y));
			boolean isDown = (playerTank.y + playerTank.height / 2 > r.y + r.height / 2);
			int uScore = Danger.getDirectionScore(battleScene, Spirit.DIR_UP , playerTank.isEnemy , new Rectangle(x , playerTank.y , playerTank.width , playerTank.height));
			int dScore = Danger.getDirectionScore(battleScene, Spirit.DIR_DOWN , playerTank.isEnemy , new Rectangle(x , playerTank.y , playerTank.width , playerTank.height));									
			if(playerTank.y + playerTank.height - uGap + 1 < r.y && uScore > SAFE_SCORE) {
				if(!isDown || (isDown && dScore < SAFE_SCORE)) {
					move(Spirit.DIR_UP);
					return true;
				} 
			}
			
			if(playerTank.y + dGap - 1 > r.y + r.height && dScore > SAFE_SCORE) {
				move(Spirit.DIR_DOWN);
				return true;
			}
		}
		
		move(dir);
		
		return true;
	}
	
	private int tankGap(int dir , int x , int y) {
		int gap = 999;
		for(Tank tank : battleScene.tanks) {
			if(playerTank != tank) {
				switch(dir) {
					case Spirit.DIR_UP :
						if(new Rectangle(x , y , playerTank.width , playerTank.height).isUpRelativeIntersects(tank)) {
							int tempGap = y - (tank.y + tank.height);					
							if(tempGap < gap) {
								gap = tempGap;
							}
						}
						break;
					case Spirit.DIR_DOWN :
						if(new Rectangle(x , y , playerTank.width , playerTank.height).isDownRelativeIntersects(tank)) {
							int tempGap = tank.y - (y + playerTank.height);
							if(tempGap < gap) {
								gap = tempGap;
							}
						}
						break;
					case Spirit.DIR_LEFT :	
						if(new Rectangle(x , y , playerTank.width , playerTank.height).isLeftRelativeIntersects(tank)) {
							int tempGap = x - (tank.x + tank.width);
							if(tempGap < gap) {
								gap = tempGap;
							}
						}
						break;
					case Spirit.DIR_RIGHT :		
						if(new Rectangle(x , y , playerTank.width , playerTank.height).isRightRelativeIntersects(tank)) {
							int tempGap = tank.x - (x + playerTank.width);
							if(tempGap < gap) {
								gap = tempGap;
							}
						}
						break;	
				}
			}
		}
		
		return gap;
	}
	
	private int decorateGap(int dir , int x , int y) {
		int gap = 0;
		int sx = (x - 32) / 8;
		int sy = (y - 16) / 8;
		int rx = x - 32 - sx * 8;
		int ry = y - 16 - sy * 8;
		int width = playerTank.width / 8 + ((x + playerTank.width - 32) % 8 == 0 ? 0 : 1);
		switch(dir) {
			case Spirit.DIR_UP :	
				if((sy -= 1) < 0) {
					return 0;
				}
		
				for(int _y = sy ; _y > -1 ; _y--) {
					for(int _x = sx ; _x < sx + width ; _x++) {
						if(playerTank.isDecorateCollide(_x, _y)) {
							gap += ry;
							return gap;
						}
					}
					gap += 8;
				}
				break;
			case Spirit.DIR_DOWN :
				sy += 4;
				if(sy > battleScene.map.length - 1) {
					return 0;
				}
					
				for(int _y = sy ; _y < battleScene.map.length ; _y++) {
					for(int _x = sx ; _x < sx + width ; _x++) {
						if(playerTank.isDecorateCollide(_x, _y)) {
							gap += ry;
							return gap;
						}
					}
					gap += 8;
				}
				break;
			case Spirit.DIR_LEFT :
				if((sx -= 1) < 0) {
					return 0;
				}
	
				width = 4 + ((y - 16) % 8 == 0 ? 0 : 1);
				
				for(int _x = sx ; _x > -1 ; _x--) {
					for(int _y = sy ; _y < sy + width ; _y++) {
						if(playerTank.isDecorateCollide(_x, _y)) {
							gap += rx;
							return gap;
						}
					}
					gap += 8;
				}
				break;
			case Spirit.DIR_RIGHT :
				sx += 4;
				if(sx > battleScene.map[0].length - 1) {
					return 0;
				}
	
				width = 4 + ((y - 16) % 8 == 0 ? 0 : 1);
			
				for(int _x = sx ; _x < battleScene.map[0].length ; _x++) {
					for(int _y = sy ; _y < sy + width ; _y++) {
						if(playerTank.isDecorateCollide(_x, _y)) {
							gap += rx;
							return gap;
						}
					}
					gap += 8;
				}
				break;
		}
		return gap;
	}
	
	private int getMostDangerDirection(int[] scores) {
		int dir = 0;
		int score = scores[0];
		for(int i = 1 ; i < scores.length ; i++) {
			int tempScore = scores[i];
			if(tempScore < score) {
				dir = i;
				score = tempScore;
			}
		}
		
		switch(dir) {
			case Spirit.DIR_UP :
				return Spirit.DIR_DOWN;
			case Spirit.DIR_DOWN :
				return Spirit.DIR_UP;
			case Spirit.DIR_LEFT :
				return Spirit.DIR_RIGHT;
			case Spirit.DIR_RIGHT :
				return Spirit.DIR_LEFT;
		}
		
		return dir;
	}
	
}
