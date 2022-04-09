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
	
	protected int shield;	//���ף�һ�㻤�׽��ֿ�һ���ֵ�����
	public int energy;		//�ӵ�����
	public int dir;		//����
	protected int lastDir;
	protected int type;	//̹������
	public boolean isEnemy;
	protected int speed; 	//�ƶ��ٶ�
	public boolean isMove;  //�Ƿ����ƶ���
	protected int tankFrame;
	public int starCount;	//����ʱ��˸�����ǣ�4��ʾ4�����ڣ�һ������Ϊ�䵽������С
	protected int starFrame;
	protected boolean starFlag;
	protected int invincibleTime;	//�޵�ʱ��S
	protected int shellFrame;	//�޵�װ��
	protected BattleScene battleScene;
	public boolean ship;	//����Ϊtrueʱ����ͨ���������൱�ڶ���һ�㻤�ף�����������ʧ
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
			//̹���ƶ�����֡����
			if(isMove) {
				if(tankEscapement.isPass()) {
					tankFrame = tankFrame > 0 ? 0 : 1;
				}
			}
			//�޵ж���֡����
			if(invincibleTime > 0) {
				if(invincibleEscapement.isPass()) {
					invincibleTime--;
				}
				
				if(shellEscapement.isPass()) {
					shellFrame = shellFrame > 0 ? 0 : 1;
				}
			}
			//ʰȡ����
			collectTreasure();
		}
	}
	
	public void move() {		
		if(isMove) {
			//�ƶ�̹��
			int speed = this.speed;
			//��̹����ѩ����(����һ����)������2�����ٶ�ǰ��
			if(isSnowfieldCollide(this.x, y)) {
				speed = speed * 2;
			}
			switch(dir) {
				case DIR_UP :
					//ת��
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
					//�ƶ�
					for(int i = 0 ; i < speed ; i++) {
						if(isUp(x , y - 1)) {
							y--;
						}
					}
					break;
				case DIR_DOWN :
					//ת��
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
					//�ƶ�
					for(int i = 0 ; i < speed ; i++) {
						if(isDown(x , y + 1)) {
							y++;
						}
					}
					break;
				case DIR_LEFT :
					//ת��
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
					//�ƶ�
					for(int i = 0 ; i < speed ; i++) {
						if(isLeft(x - 1, y)) {
							x--;
						}
					}
					break;
				case DIR_RIGHT :
					//ת��
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
					//�ƶ�
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
		
		//�����װ�������ײ���
		int _x = (x - 32) / 8;
		int _y = (y - 16) / 8;
		int len = 4 + ((x - 32) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x + i, _y)) {
				return false;
			}
		}
		
		//�����̹�˵���ײ(����)
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
		
		//�����װ�������ײ���
		int _x = (x - 32) / 8;
		int _y = (y - 16 + 32) / 8;
		int len = 4 + ((x - 32) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x + i, _y)) {
				return false;
			}
		}
		
		//�����̹�˵���ײ(����)
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
		
		//�����װ�������ײ���
		int _x = (x - 32) / 8;
		int _y = (y - 16) / 8;
		int len = 4 + ((y - 16) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x, _y + i)) {
				return false;
			}
		}
		
		//�����̹�˵���ײ(����)
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
		
		//�����װ�������ײ���
		int _x = (x - 32 + 32) / 8;
		int _y = (y - 16) / 8;
		int len = 4 + ((y - 16) % 8 > 0 ? 1 : 0);
		for(int i = 0 ; i < len ; i++) {
			if(isDecorateCollide(_x, _y + i)) {
				return false;
			}
		}
		
		//�����̹�˵���ײ(����)
		for(Tank tank : battleScene.tanks) {
			if(tank != this && tank.starCount == 0) {
				if(getRectangle(x , y).isRightIntersects(tank)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	//̹���Ƿ��ܹ����
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
		//�����ש�����ײ
		for(int _x = 0 ; _x < Map.BRICK.length ; _x++) {
			if(Map.BRICK[_x] == decorate) {
				return true;
			}
		}
		//�����ֿ����ײ
		for(int _x = 0 ; _x < Map.STEEL.length ; _x++) {
			if(Map.STEEL[_x] == decorate) {
				return true;
			}
		}
		//������������ײ
		if(!ship) {
			for(int _x = 0 ; _x < Map.RIVERS.length ; _x++) {
				if(Map.RIVERS[_x] == decorate) {
					return true;
				}
			}
		}
		//�������ص���ײ
		if(!battleScene.isBaseDestroy) {
			for(int _x = 0 ; _x < Map.BASE.length ; _x++) {
				if(Map.BASE[_x] == decorate) {
					return true;
				}
			}
		}
		return false;
	}
	
	//�����ѩ�ص���ײ
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
	
	//ʰȡ����
	protected void collectTreasure() {
		Treasure treasure = battleScene.treasure;
		if(treasure == null) {
			return;
		}
		
		if(!isEnemy || TankComponent.isTank90) {
			if(this.intersects(treasure)) {
				switch(treasure.type) {
					case Treasure.TREASURE_FIVE_POINTED_STAR_TYPE :  //�����
						treasure.fivePointedStar(this);
						break;
					case Treasure.TREASURE_STEEL_HELMET_TYPE :   //�޵�
						treasure.helmet(this);
						break;
					case Treasure.TREASURE_BOMB_TYPE :	//ը��
						treasure.antitankGrenade(this);
						break;
					case Treasure.TREASURE_TANK_TYPE :  //����
						treasure.tank(this);
						break;
					case Treasure.TREASURE_SHOVEL_TYPE : //����
						treasure.shovel(this);
						break;
					case Treasure.TREASURE_TIMER_TYPE :  //��ʱ��
						treasure.timer(this);
						break;
					case Treasure.TREASURE_SHIP_TYPE :  //��
						treasure.ship(this);
						break;
					case Treasure.TREASURE_PISTOL_TYPE : //��ǹ
						treasure.pistol(this);
						break;
				}
				//���ﱻʰȡ����Ϊnull
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
