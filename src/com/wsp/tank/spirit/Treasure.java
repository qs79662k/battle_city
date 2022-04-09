package com.wsp.tank.spirit;

import java.awt.Graphics;
import java.util.Random;

import com.wsp.tank.Art;
import com.wsp.tank.BattleScene;
import com.wsp.tank.Escapement;
import com.wsp.tank.Map;
import com.wsp.tank.Rectangle;
import com.wsp.tank.sound.PlayerSound;

public class Treasure extends Rectangle implements Spirit {
	
	public final static int TREASURE_FIVE_POINTED_STAR_TYPE = 0;
	public final static int TREASURE_STEEL_HELMET_TYPE = 1;
	public final static int TREASURE_BOMB_TYPE = 2;
	public final static int TREASURE_TANK_TYPE = 3;
	public final static int TREASURE_SHOVEL_TYPE = 4;
	public final static int TREASURE_TIMER_TYPE = 5;
	public final static int TREASURE_SHIP_TYPE = 6;
	public final static int TREASURE_PISTOL_TYPE = 7;
	public int type;
	private boolean show;
	private Escapement e = new Escapement(300); 
	private BattleScene battleScene;
	private Random random = new Random();
	
	public Treasure(BattleScene battleScene) {
		super((new Random()).nextInt(384) + 32 , (new Random()).nextInt(352) + 16 , 32 , 32);
		this.battleScene = battleScene;
		type = random.nextInt(Art.bonusb[0].length);
		if(!isRivers(battleScene.map)) {
			while(type == TREASURE_SHIP_TYPE) {
				type = random.nextInt(Art.bonusb[0].length);
			}
		} 
	}

	@Override
	public void update() {
		if(e.isPass()) {
			show = !show;
		}
	}

	@Override
	public void render(Graphics g) {
		if(show) {
			g.drawImage(Art.bonusb[0][type], x, y, null);
		}
	}
	
	private boolean isRivers(byte[][] map) {
		for(int y = 0 ; y < map.length ; y++) {
			for(int x = 0 ; x < map[y].length ; x++) {
				for(int _x = 0 ; _x < Map.RIVERS.length ; _x++) {
					if(map[y][x] == Map.RIVERS[_x]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void fivePointedStar(Tank tank) {
		if(tank.isEnemy) {
			tank.energy++;
		} else {
			PlayerTank playerTank = (PlayerTank)tank;
			playerTank.score += 500;
			playerTank.firedEscapement.setSpacing(playerTank.firedTimes[1]);  //增加子弹发射速度
			//升级坦克
			if(playerTank.type < PlayerTank.PLAYER_TANK_TYPE_4) {
				playerTank.type++;
				if(playerTank.type > PlayerTank.PLAYER_TANK_TYPE_2) {
					playerTank.shield++;
				}
			}
			//增强坦克子弹威力
			if(playerTank.type > PlayerTank.PLAYER_TANK_TYPE_3 && playerTank.energy < 2) {
				tank.energy++;
			}
			battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
		}
		new PlayerSound(Art.eatbonus);
	}
	
	public void helmet(Tank tank) {
		tank.invincibleTime = 6;
		if(!tank.isEnemy) {
			PlayerTank playerTank = (PlayerTank)tank;
			playerTank.score += 500;
			battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
		}
		new PlayerSound(Art.eatbonus);
	}
	
	public void antitankGrenade(Tank tank) {
		for(int i = battleScene.tanks.size() - 1 ; i > -1 ; i--) {
			Tank _tank = battleScene.tanks.get(i);
			if(tank.isEnemy != _tank.isEnemy && _tank.starCount < 1 && _tank.invincibleTime < 1) {
				if(_tank.isEnemy) {
					PlayerTank playerTank = (PlayerTank)tank;
					playerTank.score += 500;
					battleScene.activeEnemyCount--;
					battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
				} else {
					PlayerTank playerTank = (PlayerTank)_tank;
					playerTank.die = true;
				}
				tankBomb(_tank);
				battleScene.tanks.remove(_tank);
			}
		}
		new PlayerSound(Art.eatbonus);
	}
	
	public void tank(Tank tank) {
		if(tank.isEnemy) {
			tank.shield++;
			new PlayerSound(Art.eatbonus);
		} else {
			PlayerTank playerTank = (PlayerTank)tank;
			playerTank.score += 500;
			if(playerTank.life < 9) {
				playerTank.life++;
			}
			battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
			new PlayerSound(Art.up);
		}
	}
	
	public void shovel(Tank tank) {
		if(tank.isEnemy) {
			switch(battleScene.wallType) {
				case Map.BRICK_WALL_TYPE :
					battleScene.notWall();
					battleScene.wallType = Map.NOT_WALL_TYPE;
					break;
				case Map.STEEL_WALL_TYPE :
					battleScene.brickWall();
					battleScene.steelWallTimer = 0;
					battleScene.wallType = Map.BRICK_WALL_TYPE;
					break;
			}
		} else {
			PlayerTank playerTank = (PlayerTank)tank;
			playerTank.score += 500;
			switch(battleScene.wallType) {
				case Map.NOT_WALL_TYPE :
					battleScene.brickWall();
					battleScene.wallType = Map.BRICK_WALL_TYPE;
					break;
				case Map.BRICK_WALL_TYPE :
					battleScene.steelWall();
					battleScene.steelWallTimer = 20;
					battleScene.wallType = Map.STEEL_WALL_TYPE;
					break;
				case Map.STEEL_WALL_TYPE :
					battleScene.steelWall();
					battleScene.steelWallTimer = 20;
					break;
			}
			battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
		}
		new PlayerSound(Art.eatbonus);
	}
	
	public void timer(Tank tank) {
		if(tank.isEnemy) {
			PlayerTank.timer = 8;
		} else {
			EnemyTank.timer = 8;
			PlayerTank playerTank = (PlayerTank)tank;
			playerTank.score += 500;
			battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
		}
		new PlayerSound(Art.eatbonus);
	}
	
	public void ship(Tank tank) {
		tank.ship = true;
		if(!tank.isEnemy) {
			PlayerTank playerTank = (PlayerTank)tank;
			playerTank.score += 500;
			battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
		}
		new PlayerSound(Art.eatbonus);
	}
	
	public void pistol(Tank tank) {
		if(tank.isEnemy) {
			tank.energy++;
		} else {
			PlayerTank playerTank = (PlayerTank)tank;
			playerTank.score += 500;
			playerTank.firedEscapement.setSpacing(playerTank.firedTimes[1]);
			if(playerTank.type < PlayerTank.PLAYER_TANK_TYPE_4) {
				playerTank.shield = 2;
			}
			//子弹威力
			if(playerTank.type > PlayerTank.PLAYER_TANK_TYPE_1) {
				playerTank.energy = 2;
			} else {
				playerTank.energy = 1;
			}
			playerTank.type = PlayerTank.PLAYER_TANK_TYPE_4;
			battleScene.spirits.add(new Score(battleScene , Score.SCORE_500 , x , y));
		}
		new PlayerSound(Art.eatbonus);
	}
	
	private void tankBomb(Tank tank) {
		new PlayerSound(Art.bang);	
		int minBombCount = random.nextInt(2) + 2;
		for(int i = 0 ; i < minBombCount ; i++) {
			int bx = random.nextInt(32);
			int by = random.nextInt(32);
			battleScene.spirits.add(new Bomb(battleScene, Bomb.MIN_BOMB_TYPE , bx + tank.x - 16 , by + tank.y - 16));
		}
		battleScene.spirits.add(new Bomb(battleScene, Bomb.MAX_BOMB_TYPE , tank.x - 16, tank.y - 16));
	}
	
}
