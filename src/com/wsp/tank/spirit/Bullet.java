package com.wsp.tank.spirit;

import java.awt.Graphics;

import com.wsp.tank.Art;
import com.wsp.tank.BattleScene;
import com.wsp.tank.Map;
import com.wsp.tank.Rectangle;
import com.wsp.tank.sound.PlayerSound;

public class Bullet extends Rectangle implements Spirit {

	public int dir;
	public final static int speed = 10;  //子弹移动速度
	private int energy;  //子弹威力
	public boolean isEnemy;	
	private int on;	//是几号玩家的子弹，如为敌人坦克则此值无效
	private BattleScene battleScene;
	
	public Bullet(BattleScene battleScene , int x , int y , int dir , int energy) {
		this(battleScene , x , y , dir , energy , 0);
		isEnemy = true;
	}
	
	public Bullet(BattleScene battleScene , int x , int y , int dir , int energy , int on) {
		super(x , y , 8 , 8);
		this.battleScene = battleScene;
		this.dir = dir;
		this.energy = energy;
		this.on = on;
	}

	@Override
	public void update() {
		move();
		hitTank();
		hitBullet();
	}
	
	public void move() {
		switch(dir) {
			case DIR_UP :
				for(int i = 0 ; i < speed ; i++) {
					if(isUp(x , y - 1)) {
						y--;
					}
				}
				break;
			case DIR_DOWN :
				for(int i = 0 ; i < speed ; i++) {
					if(isDown(x , y + 1)) {
						y++;
					}
				}
				break;
			case DIR_LEFT :
				for(int i = 0 ; i < speed ; i++) {
					if(isLeft(x - 1 , y)) {
						x--;
					}
				}
				break;
			case DIR_RIGHT :
				for(int i = 0 ; i < speed ; i++) {
					if(isRight(x + 1 , y)) {
						x++;
					}
				}
				break;
		}
	}

	@Override
	public void render(Graphics g) {
		switch(dir) {
			case DIR_UP:
				g.drawImage(Art.bullet[0][0], x, y, null);
				break;
			case DIR_DOWN:
				g.drawImage(Art.bullet[0][2], x, y, null);
				break;
			case DIR_LEFT:
				g.drawImage(Art.bullet[0][3], x, y, null);
				break;
			case DIR_RIGHT:
				g.drawImage(Art.bullet[0][1], x, y, null);
				break;
		}
	}
	
	private boolean isUp(int x , int y) {
		if(y < 17) {
			new PlayerSound(Art.hitiron);
			battleScene.bullets.remove(this);
			Bomb.minBomb(battleScene, DIR_UP , x, y);
			return false;
		}
		
		int _x = (x - 32) / 8;
		int _y = (y - 16) / 8;
		boolean isHit = false;
		boolean isHitBase = false;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x - 1, _y)) {
				isHitBase = true;
			}
			hitBrick(_x - 1, _y);
			hitSteel(_x - 1 , _y);
			hitTree(_x - 1, _y);
		}
		
		_x += 1;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x + 1, _y)) {
				isHitBase = true;
			}
			hitBrick(_x + 1, _y);
			hitSteel(_x + 1 , _y);
			hitTree(_x + 1, _y);
		}
		
		if(isHitBase) {
			Bomb.baseBomb(battleScene);
			battleScene.bullets.remove(this);
			return false;
		}
		
		if(isHit) {
			new PlayerSound(Art.powerbullet);
			Bomb.minBomb(battleScene, DIR_UP , x, y);
			battleScene.bullets.remove(this);
			return false;
		}
		
		return true;
	}
	
	private boolean isDown(int x , int y) {
		if(y > 423) {
			new PlayerSound(Art.hitiron);
			battleScene.bullets.remove(this);
			Bomb.minBomb(battleScene, DIR_DOWN , x, y);
			return false;
		}
		
		//左边两块装饰物
		int _x = (x - 32) / 8;
		int _y = (y - 16 + 8) / 8;
		boolean isHit = false;
		boolean isHitBase = false;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x - 1, _y)) {
				isHitBase = true;
			}
			hitBrick(_x - 1 , _y);
			hitSteel(_x - 1 , _y);
			hitTree(_x - 1 , _y);
		}
		
		_x += 1;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x + 1 , _y)) {
				isHitBase = true;
			}
			hitBrick(_x + 1 , _y);
			hitSteel(_x + 1 , _y);
			hitTree(_x + 1 , _y);
		}
		
		if(isHitBase) {
			Bomb.baseBomb(battleScene);
			battleScene.bullets.remove(this);
			return false;
		}
		
		if(isHit) {
			new PlayerSound(Art.powerbullet);
			Bomb.minBomb(battleScene, DIR_DOWN , x, y);
			battleScene.bullets.remove(this);
			return false;
		}
		
		return true;
	}
	
	private boolean isLeft(int x , int y) {
		if(x < 33) {
			new PlayerSound(Art.hitiron);
			battleScene.bullets.remove(this);
			Bomb.minBomb(battleScene, DIR_LEFT , x, y);
			return false;
		}
		
		//上边两块装饰物
		int _x = (x - 32) / 8;
		int _y = (y - 16) / 8;
		boolean isHit = false;
		boolean isHitBase = false;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x, _y - 1)) {
				isHitBase = true;
			}
			hitBrick(_x , _y - 1);
			hitSteel(_x , _y - 1);
			hitTree(_x , _y - 1);
		}
		
		_y += 1;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x , _y + 1)) {
				isHitBase = true;
			}
			hitBrick(_x , _y + 1);
			hitSteel(_x , _y + 1);
			hitTree(_x , _y + 1);
		}

		if(isHitBase) {
			Bomb.baseBomb(battleScene);
			battleScene.bullets.remove(this);
			return false;
		}
		
		if(isHit) {
			new PlayerSound(Art.powerbullet);
			Bomb.minBomb(battleScene, DIR_LEFT , x, y);
			battleScene.bullets.remove(this);
			return false;
		}
		
		return true;
	}
	
	private boolean isRight(int x , int y) {
		if(x > 439) {
			new PlayerSound(Art.hitiron);
			battleScene.bullets.remove(this);
			Bomb.minBomb(battleScene, DIR_RIGHT , x, y);
			return false;
		}
		
		//上边两块装饰物
		int _x = (x - 32 + 8) / 8;
		int _y = (y - 16) / 8;
		boolean isHit = false;
		boolean isHitBase = false;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x, _y - 1)) {
				isHitBase = true;
			}
			hitBrick(_x , _y - 1);
			hitSteel(_x , _y - 1);
			hitTree(_x , _y - 1);
		}
		
		_y += 1;
		
		if(hitBase(_x, _y)) {
			isHitBase = true;
		}
		
		if(hitBrick(_x, _y) || hitSteel(_x , _y) || hitTree(_x, _y)) {
			isHit = true;
			if(hitBase(_x , _y + 1)) {
				isHitBase = true;
			}
			hitBrick(_x , _y + 1);
			hitSteel(_x , _y + 1);
			hitTree(_x , _y + 1);
		}
		
		if(isHitBase) {
			Bomb.baseBomb(battleScene);
			battleScene.bullets.remove(this);
			return false;
		}
		
		if(isHit) {
			new PlayerSound(Art.powerbullet);
			Bomb.minBomb(battleScene, DIR_RIGHT , x, y);
			battleScene.bullets.remove(this);
			return false;
		}
		
		return true;
	}
	
	private boolean hitBrick(int x , int y) {
		int brick = battleScene.map[y][x];
		for(int i = 0 ; i < Map.BRICK.length ; i++) {
			int _brick = Map.BRICK[i];
			if(brick == _brick) {
				battleScene.map[y][x] = 0;
				return true;
			}
		}
		return false;
	}
	
	private boolean hitSteel(int x , int y) {
		int steel = battleScene.map[y][x];
		for(int i = 0 ; i < Map.STEEL.length ; i++) {
			int _steel = Map.STEEL[i];
			if(steel == _steel) {
				if(energy > 0) {
					battleScene.map[y][x] = 0;
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean hitTree(int x , int y) {
		if(energy > 1) {
			int tree = battleScene.map[y][x];
			for(int i = 0 ; i < Map.TREE.length ; i++) {
				int _tree = Map.TREE[i];
				if(tree == _tree) {
					battleScene.map[y][x] = 0;
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hitBase(int x , int y) {
		if(!battleScene.isBaseDestroy) {
			int base = battleScene.map[y][x];
			for(int i = 0 ; i < Map.BASE.length ; i++) {
				int _base = Map.BASE[i];
				if(base == _base) {
					return battleScene.isBaseDestroy = true;
				}
			}
		}
		return false;
	}
	
	private boolean hitTank() {
		for(int i = 0 ; i < battleScene.tanks.size(); i++) {
			Tank tank = battleScene.tanks.get(i);
			if(tank.starCount < 1 && isEnemy != tank.isEnemy) { 
				if(this.intersects(tank)) {
					if(tank.invincibleTime < 1) {
						if(tank.ship) { //船被打掉(有船时相当于多一点护甲)
							new PlayerSound(Art.hitirontank);
							tank.ship = false;
						} else if(tank.shield > 0) {  //当坦克护甲大于0时被击中只会掉1点护甲
							new PlayerSound(Art.hitirontank);
							tank.shield--;
						} else {  //护甲点为0时坦克被击中将会死掉
							Bomb.tankBomb(battleScene , tank);
							battleScene.tanks.remove(tank);
							if(isEnemy) {
								PlayerTank playerTank = (PlayerTank)tank;
								playerTank.die = true;							
							} else {
								battleScene.activeEnemyCount--;
								battleScene.playerTanks[on].killEnemys[tank.type]++;
								battleScene.playerTanks[on].score += tank.type * 100 + 100;
							}
						}
						//掉落宝物
						if(tank.isEnemy) {
							EnemyTank enemyTank = (EnemyTank)tank;
							if(enemyTank.isFlickerTank) { //子弹击中闪烁坦克，刷新宝物
								new PlayerSound(Art.redbang);
								battleScene.treasure = new Treasure(battleScene);
							}
						}
						Bomb.minBomb(battleScene , dir , x , y);
					}
					battleScene.bullets.remove(this);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean hitBullet() {
		for(int i = 0 ; i < battleScene.bullets.size(); i++) {
			Bullet bullet = battleScene.bullets.get(i);
			if(isEnemy != bullet.isEnemy) {
				if(this.intersects(bullet)) {
					new PlayerSound(Art.hitbullet);
					battleScene.bullets.remove(this);
					battleScene.bullets.remove(bullet);
					return true;
				}
			}
		}
		return false;
	}
	
}
