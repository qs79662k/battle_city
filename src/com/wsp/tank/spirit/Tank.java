package com.wsp.tank.spirit;

import java.awt.Graphics;
import java.util.Random;

import com.wsp.tank.Art;
import com.wsp.tank.BattleScene;
import com.wsp.tank.Escapement;
import com.wsp.tank.Map;
import com.wsp.tank.Rectangle;
import com.wsp.tank.TankComponent;
import com.wsp.tank.sound.PlayerSound;

public abstract class Tank extends Rectangle implements Spirit {
	
	protected int shield;	//护甲，一点护甲将抵抗一次字弹攻击
	public int energy;		//子弹威力
	public int dir;		//方向
	protected int lastDir;
	protected int type;	//坦克类型
	public boolean isEnemy;
	protected int speed; 	//移动速度
	public boolean isMove;  //是否在移动中
	protected int tankFrame;
	public int starCount;	//出生时闪烁的星星，4表示4个周期，一个周期为变到大至变小
	protected int starFrame;
	protected boolean starFlag;
	protected int invincibleTime;	//无敌时间S
	protected int shellFrame;	//无敌装饰
	protected BattleScene battleScene;
	public boolean ship;	//当船为true时可以通过河流并相当于多了一点护甲，被击中则消失
	protected Escapement tankEscapement = new Escapement(18);
	protected Escapement starEscapement = new Escapement(120);
	protected Escapement invincibleEscapement = new Escapement(1000);
	protected Escapement shellEscapement = new Escapement(30);
	protected Random random = new Random();
	
	public Tank(BattleScene battleScene , int x , int y , int width , int height) {
		this(battleScene , false , x , y , width , height);
	}
	
	public Tank(BattleScene battleScene , boolean isEnemy , int x , int y , int width , int height) {
		super(x , y , width , height);
		this.battleScene = battleScene;
		this.isEnemy = isEnemy;
	}
	
	@Override
	public void update() {
		if(starCount > 0) {
			if(starEscapement.isPass()) {		
				if(!starFlag) {
					starFrame++;
					if(starFrame > Art.star[0].length - 2) {
						starCount--;
						starFlag = true;
					} 
				} else {
					starFrame--;
					if(starFrame < 1) {
						starFlag = false;
					}
				}	
			}
		} else {
			//坦克移动动画帧更新
			if(isMove) {
				if(tankEscapement.isPass()) {
					tankFrame = tankFrame > 0 ? 0 : 1;
				}
			}
			//无敌动画帧更新
			if(invincibleTime > 0) {
				if(invincibleEscapement.isPass()) {
					invincibleTime--;
				}
				
				if(shellEscapement.isPass()) {
					shellFrame = shellFrame > 0 ? 0 : 1;
				}
			}
			//拾取宝物
			collectTreasure();
		}
	}
	
	public void move() {		
		if(isMove) {
			//移动坦克
			int speed = this.speed;
			//如坦克在雪地上(包括一部分)将会以2倍的速度前进
			if(isSnowfieldCollide(this.x, y)) {
				speed = speed * 2;
			}
			switch(dir) {
				case DIR_UP :
					//转向
					if(lastDir == DIR_LEFT || lastDir == DIR_RIGHT) {
						int x = this.x / 16 * 16;
						if(this.x - x > 8) {
							if(isCurve((this.x / 16 + 1) * 16, y)) {
								this.x = (this.x / 16 + 1) * 16;
							}
						} else {
							if(isCurve(x , y)) {
								this.x = x;
							}
						}
					} 
					//移动
					for(int i = 0 ; i < speed ; i++) {
						if(isUp(x , y - 1)) {
							y--;
						}
					}
					break;
				case DIR_DOWN :
					//转向
					if(lastDir == DIR_LEFT || lastDir == DIR_RIGHT) {
						int x = this.x / 16 * 16;
						if(this.x - x > 8) {
							if(isCurve((this.x / 16 + 1) * 16 , y)) {
								this.x = (this.x / 16 + 1) * 16;
							}
						} else {
							if(isCurve(x , y)) {
								this.x = x;
							}
						}
					}
					//移动
					for(int i = 0 ; i < speed ; i++) {
						if(isDown(x , y + 1)) {
							y++;
						}
					}
					break;
				case DIR_LEFT :
					//转向
					if(lastDir == DIR_UP || lastDir == DIR_DOWN) {
						int y = this.y / 16 * 16;
						if(this.y - y > 8) {
							if(isCurve(x , (this.y / 16 + 1) * 16)) {
								this.y = (this.y / 16 + 1) * 16;
							}
						} else {
							if(isCurve(x , y)) {
								this.y = y;
							}
						}
					}
					//移动
					for(int i = 0 ; i < speed ; i++) {
						if(isLeft(x - 1, y)) {
							x--;
						}
					}
					break;
				case DIR_RIGHT :
					//转向
					if(lastDir == DIR_UP || lastDir == DIR_DOWN) {
						int y = this.y / 16 * 16;
						if(this.y - y > 8) {
							if(isCurve(x , (this.y / 16 + 1) * 16)) {
								this.y = (this.y / 16 + 1) * 16;
							}
						} else {
							if(isCurve(x , y)) {
								this.y = y;
							}
						}
					}
					//移动
					for(int i = 0 ; i < speed ; i++) {
						if(isRight(x + 1, y)) {
							x++;
						}
					}
					break;
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if(starCount > 0) {
			g.drawImage(Art.star[0][starFrame], x, y, null);
		} else {
			if(ship) {
				g.drawImage(Art.shellb[0][2], x, y, null);
			}
			
			if(invincibleTime > 0) {
				g.drawImage(Art.shellb[0][shellFrame], x, y, null);
			}
		}	
	}
	
	protected boolean isUp(int x , int y) {
		if(y < 16) {
			return false;
		}
		
		//检测与装饰物的碰撞检测
		int _x = (x - 32) / 8;
		int _y = (y - 16) / 8;
		int len = 4 + ((x - 32) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x + i, _y)) {
				return false;
			}
		}
		
		//检测与坦克的碰撞(向上)
		for(Tank tank : battleScene.tanks) {
			if(tank != this && tank.starCount == 0) {
				if(getRectangle(x , y).isUpIntersects(tank)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected boolean isDown(int x , int y) {
		if(y > 399) {
			return false;
		}
		
		//检测与装饰物的碰撞检测
		int _x = (x - 32) / 8;
		int _y = (y - 16 + 32) / 8;
		int len = 4 + ((x - 32) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x + i, _y)) {
				return false;
			}
		}
		
		//检测与坦克的碰撞(向下)
		for(Tank tank : battleScene.tanks) {
			if(tank != this && tank.starCount == 0) {
				if(getRectangle(x , y).isDownIntersects(tank)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected boolean isLeft(int x , int y) {
		if(x < 32) {
			return false;
		}
		
		//检测与装饰物的碰撞检测
		int _x = (x - 32) / 8;
		int _y = (y - 16) / 8;
		int len = 4 + ((y - 16) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x, _y + i)) {
				return false;
			}
		}
		
		//检测与坦克的碰撞(向左)
		for(Tank tank : battleScene.tanks) {
			if(tank != this && tank.starCount == 0) {
				if(getRectangle(x , y).isLeftIntersects(tank)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected boolean isRight(int x , int y) {
		if(x > 415) {
			return false;
		}
		
		//检测与装饰物的碰撞检测
		int _x = (x - 32 + 32) / 8;
		int _y = (y - 16) / 8;
		int len = 4 + ((y - 16) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x, _y + i)) {
				return false;
			}
		}
		
		//检测与坦克的碰撞(向右)
		for(Tank tank : battleScene.tanks) {
			if(tank != this && tank.starCount == 0) {
				if(getRectangle(x , y).isRightIntersects(tank)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	//坦克是否能过弯道
	protected boolean isCurve(int x , int y) {
		int _x = (x - 32) / 8;
		int _y = (y - 16) /8;
		int lenX = 4 + ((x - 32) % 8 == 0 ? 0 : 1);
		int lenY = 4 + ((y - 16) % 8 == 0 ? 0 : 1); 
		for(int i = 0 ; i < lenX ; i++) {
			int cx = _x + i;
			for(int j = 0 ; j < lenY ; j++) {
				int cy = _y + j;
				if(isDecorateCollide(cx, cy)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isDecorateCollide(int x , int y) {
		int decorate = battleScene.map[y][x];
		//检测与砖块的碰撞
		for(int _x = 0 ; _x < Map.BRICK.length ; _x++) {
			if(Map.BRICK[_x] == decorate) {
				return true;
			}
		}
		//检测与钢块的碰撞
		for(int _x = 0 ; _x < Map.STEEL.length ; _x++) {
			if(Map.STEEL[_x] == decorate) {
				return true;
			}
		}
		//检测与河流的碰撞
		if(!ship) {
			for(int _x = 0 ; _x < Map.RIVERS.length ; _x++) {
				if(Map.RIVERS[_x] == decorate) {
					return true;
				}
			}
		}
		//检测与基地的碰撞
		if(!battleScene.isBaseDestroy) {
			for(int _x = 0 ; _x < Map.BASE.length ; _x++) {
				if(Map.BASE[_x] == decorate) {
					return true;
				}
			}
		}
		return false;
	}
	
	//检测与雪地的碰撞
	public boolean isSnowfieldCollide(int x , int y) {
		int _x = (x - 32) / 8;
		int _y = (y - 16) / 8;
		int lenX = 4 + ((x - 32) % 8 == 0 ? 0 : 1);
		int lenY = 4 + ((y - 16) % 8 == 0 ? 0 : 1);
		for(int i = 0 ; i< lenY ; i++) {
			for(int j = 0 ; j < lenX ; j++) {
				for(int k = 0 ; k < Map.SNOWFIELD.length ; k++) {
					if(battleScene.map[_y + i][_x + j] == Map.SNOWFIELD[k]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//拾取宝物
	protected void collectTreasure() {
		Treasure treasure = battleScene.treasure;
		if(treasure == null) {
			return;
		}
		
		if(!isEnemy || TankComponent.isTank90) {
			if(this.intersects(treasure)) {
				switch(treasure.type) {
					case Treasure.TREASURE_FIVE_POINTED_STAR_TYPE :  //五角星
						treasure.fivePointedStar(this);
						break;
					case Treasure.TREASURE_STEEL_HELMET_TYPE :   //无敌
						treasure.helmet(this);
						break;
					case Treasure.TREASURE_BOMB_TYPE :	//炸弹
						treasure.antitankGrenade(this);
						break;
					case Treasure.TREASURE_TANK_TYPE :  //生命
						treasure.tank(this);
						break;
					case Treasure.TREASURE_SHOVEL_TYPE : //铁锹
						treasure.shovel(this);
						break;
					case Treasure.TREASURE_TIMER_TYPE :  //定时器
						treasure.timer(this);
						break;
					case Treasure.TREASURE_SHIP_TYPE :  //船
						treasure.ship(this);
						break;
					case Treasure.TREASURE_PISTOL_TYPE : //手枪
						treasure.pistol(this);
						break;
				}
				//宝物被拾取后设为null
				battleScene.treasure = null;
			}
		}
	}
	
	protected void firedBullet() {
		int bx = x;
		int by = y;
		switch(dir) {
			case DIR_UP:
				bx += 12;
				break;
			case DIR_DOWN:
				bx += 12;
				by += 24;
				break;
			case DIR_LEFT:
				by += 12;
				break;
			case DIR_RIGHT:
				bx += 24;
				by += 12;
				break;
		}	
		if(isEnemy) {
			battleScene.bullets.add(new Bullet(battleScene , bx , by , dir , energy));
		} else {
			PlayerTank playerTank = (PlayerTank)this;
			battleScene.bullets.add(new Bullet(battleScene , bx , by , dir , energy , playerTank.on));
		}
		new PlayerSound(Art.gunfire);
	}
	
	public Rectangle getRectangle(int x , int y) {
		return new Rectangle(x, y, width, height);
	}
	
	public Rectangle getRectangle(int dir) {
		int x = this.x;
		int y = this.y;
		if(this.dir == Spirit.DIR_UP || this.dir == Spirit.DIR_DOWN) {
			if(dir == Spirit.DIR_LEFT || dir == Spirit.DIR_RIGHT) {
				y = this.y / 16 * 16;
				if(this.y - y > 8) {
					y = (this.y / 16 + 1) * 16;
				}
			}
		} else {
			if(dir == Spirit.DIR_UP || dir == Spirit.DIR_DOWN) {
				x = this.x / 16 * 16;
				if(this.x - x > 8) {
					x = (this.x / 16 + 1) * 16;
				}
			}
		}
		return new Rectangle(x , y , width , height);
	}
	
}
