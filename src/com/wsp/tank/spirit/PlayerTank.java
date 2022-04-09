package com.wsp.tank.spirit;

import java.awt.Graphics;

import com.wsp.tank.Art;
import com.wsp.tank.BattleScene;
import com.wsp.tank.Escapement;
import com.wsp.tank.Scene;

public class PlayerTank extends Tank {
	
	public final static int PLAYER_TANK_TYPE_1 = 0;
	public final static int PLAYER_TANK_TYPE_2 = 1;
	public final static int PLAYER_TANK_TYPE_3 = 2;
	public final static int PLAYER_TANK_TYPE_4 = 3;
	public final static int PLAYER_TANK_1 = 0; //1号坦克
	public final static int PLAYER_TANK_2 = 1;	//2号坦克
	public final int[] firedTimes = {800 , 600 , 80};
	public int life = 2;
	public boolean die;
	public int score;
	public int[] killEnemys = new int[4];
	public int on;  //0为已方1号坦克，1为已方2号坦克
	public boolean refresh;  
	public static int timer;	//定时器，当为0时坦克方可移动、发射子弹
	public Escapement firedEscapement;
	
	public PlayerTank(BattleScene battleScene , int type , int x , int y , int dir , int energy , int on) {
		super(battleScene , x , y , 32 , 32);
		this.type = type;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.energy = energy;
		this.on = on;
		speed = 1;
		starCount = 4;
		invincibleTime = 5;
		shield = type > PLAYER_TANK_TYPE_2 ? (type > PLAYER_TANK_TYPE_3 ? 2 : 1) : 0;
		firedEscapement = new Escapement(firedTimes[(type == PLAYER_TANK_TYPE_1 ? 0 : 1)]);
	}
	
	public void init() {
		x = BattleScene.PLAYER_BORN_POINTS[on] / 500;
		y = BattleScene.PLAYER_BORN_POINTS[on] % 500;
		dir = Tank.DIR_UP;
		isMove = false;
		starCount = 4;
		invincibleTime = 5;
		refresh = false;
		if(!die) {
			shield = type > PLAYER_TANK_TYPE_2 ? (type > PLAYER_TANK_TYPE_3 ? 2 : 1) : 0;
			firedEscapement = new Escapement(firedTimes[(type == PLAYER_TANK_TYPE_1 ? 0 : 1)]);
		} else {
			die = false;
			type = PLAYER_TANK_TYPE_1;
			energy = 0;
			shield = 0;
			firedEscapement = new Escapement(firedTimes[0]);
		}		
	}
	
	public void init(BattleScene battleScene) {
		this.battleScene = battleScene;
		init();
	}
	
	public void update() {
		super.update();		
		if(starCount < 1 && !die && timer < 1 && !battleScene.isBaseDestroy) {
			lastDir = dir;
			if(battleScene.keys[Scene.I_KEY_UP + on * 5]) {
				dir = DIR_UP;		
				isMove = true;				
			} else if(battleScene.keys[Scene.I_KEY_DOWN + on * 5]) {
				dir = DIR_DOWN;
				isMove = true;
			} else if(battleScene.keys[Scene.I_KEY_LEFT + on * 5]) {
				dir = DIR_LEFT;
				isMove = true;
			} else if(battleScene.keys[Scene.I_KEY_RIGHT + on * 5]) {
				dir = DIR_RIGHT;
				isMove = true;			
			}
			
			if(battleScene.keys[Scene.I_KEY_SHOOT + on * 5] && timer < 1) {
				firedBullet();
			}
			
			super.move();
		}
		
		if(!battleScene.keys[Scene.I_KEY_UP + on * 5] && !battleScene.keys[Scene.I_KEY_DOWN + on * 5] && !battleScene.keys[Scene.I_KEY_LEFT + on * 5] && !battleScene.keys[Scene.I_KEY_RIGHT + on * 5]) {
			isMove = false;
		} 
	}

	@Override
	public void render(Graphics g) {
		if(starCount < 1) {
			if(on == PLAYER_TANK_1) {
				g.drawImage(Art.player1a[dir][type * 2 + tankFrame], x, y, null);
			} else {
				g.drawImage(Art.player2a[dir][type * 2 + tankFrame], x, y, null);
			}
		}
		super.render(g);
	}

	private double lastTime;
	
	@Override
	protected void firedBullet() {
		if(type < PLAYER_TANK_TYPE_3) {
			if(firedEscapement.isPass()) {
				super.firedBullet();
			}
		} else {
			if(firedEscapement.isPass()) {
				double spacing = firedEscapement.getSpacing();
				double currentTime = System.nanoTime() / 1000000;
				if(spacing == firedTimes[1] || currentTime - lastTime > firedTimes[1]) {
					firedEscapement.setSpacing(firedTimes[2]);
				} else {
					firedEscapement.setSpacing(firedTimes[1]);
				}
				lastTime = firedEscapement.getLastTime();
				super.firedBullet();
			}
		}
	}

}
