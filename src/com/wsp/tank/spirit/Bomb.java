package com.wsp.tank.spirit;

import java.awt.Graphics;
import java.util.Random;

import com.wsp.tank.Art;
import com.wsp.tank.BattleScene;
import com.wsp.tank.Escapement;
import com.wsp.tank.Rectangle;
import com.wsp.tank.sound.PlayerSound;

public class Bomb implements Spirit {
	
	public final static int MIN_BOMB_TYPE = 0;
	public final static int MAX_BOMB_TYPE = 1;
	private int x;
	private int y;
	private int type;
	private int enemyType;
	private boolean isEnemyBomb;
	private int bombFrame;
	private int minBombCount;
	private BattleScene battleScene;
	private Escapement e = new Escapement(80);
	
	public Bomb(BattleScene battleScene , int type , int x , int y) {
		this(battleScene , type , x , y , 0);
		isEnemyBomb = false;
	}
	
	public Bomb(BattleScene battleScene , int type , int x , int y , int enemyType) {
		this.battleScene = battleScene;
		this.type = type;
		this.x = x;
		this.y = y;
		this.enemyType = enemyType;
		isEnemyBomb = true;
		minBombCount = Art.bomb1[0].length;
	}

	@Override
	public void update() {
		if(type == MIN_BOMB_TYPE) {
			if(e.isPass()) {
				bombFrame++;
				if(bombFrame > Art.bomb1[0].length - 1) {
					//爆炸动画播放完时删除自己
					battleScene.spirits.remove(this);
				}
			}
		} else {
			if(minBombCount > -1) {
				if(e.isPass()) {
					minBombCount--;
				}
			} else {
				if(e.isPass()) {
					bombFrame++;
					if(bombFrame > Art.bomb2[0].length - 1) {
						if(isEnemyBomb) {
							battleScene.spirits.add(new Score(battleScene, enemyType , x + 16, y + 16));
						}
						battleScene.spirits.remove(this);
					}
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if(type == MIN_BOMB_TYPE) {
			g.drawImage(Art.bomb1[0][bombFrame],x , y , null);
		} else {
			if(minBombCount < 0) {
				g.drawImage(Art.bomb2[0][bombFrame],x , y , null);
			}
		} 
	}
	
	/**
	 * @param battleScene
	 * @param dir
	 * @param x
	 * @param y
	 */
	public static void minBomb(BattleScene battleScene , int dir , int x , int y) {
		int _x = x;
		int _y = y;
		switch(dir) {
			case Spirit.DIR_UP :
				_x -= 12;
				_y -= 16;
				break;
			case Spirit.DIR_DOWN :
				_x -= 12;
				_y -= 8;
				break;
			case Spirit.DIR_LEFT :
				_x -= 16;
				_y -= 12; 
				break;
			case Spirit.DIR_RIGHT :
				_x -= 8;
				_y -= 12;
				break;
		}
		battleScene.spirits.add(new Bomb(battleScene, Bomb.MIN_BOMB_TYPE, _x, _y));	
	}
	
	/**
	 * @param tank 被击毁的坦克
	 */
	public static void tankBomb(BattleScene battleScene , Tank tank) {
		new PlayerSound(Art.bang);
		Random random = new Random();
		int minBombCount = random.nextInt(3) + 1;
		for(int i = 0 ; i < minBombCount ; i++) {
			int bx = random.nextInt(32);
			int by = random.nextInt(32);
			battleScene.spirits.add(new Bomb(battleScene, Bomb.MIN_BOMB_TYPE , bx + tank.x - 16 , by + tank.y - 16));
		}
		
		if(!tank.isEnemy) {
			battleScene.spirits.add(new Bomb(battleScene, Bomb.MAX_BOMB_TYPE , tank.x - 16, tank.y - 16));
		} else {
			battleScene.spirits.add(new Bomb(battleScene, Bomb.MAX_BOMB_TYPE , tank.x - 16, tank.y - 16, tank.type));
		}
	}
	
	/**
	 * 基地被击毁
	 */
	public static void baseBomb(BattleScene battleScene) {
		new PlayerSound(Art.bang2);
		Random random = new Random();
		int minBombCount = random.nextInt(4) + 2; //随机小爆炸个数
		for(int i = 0 ; i < minBombCount ; i++) { //生成随机的小爆炸坐标
			int bx = random.nextInt(32);
			int by = random.nextInt(32);
			battleScene.spirits.add(new Bomb(battleScene, Bomb.MIN_BOMB_TYPE , bx + 208 , by + 384));
		}
		battleScene.spirits.add(new Bomb(battleScene, Bomb.MAX_BOMB_TYPE , 208, 384));
	}

}
