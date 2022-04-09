package com.wsp.tank.spirit;

import java.awt.Graphics;
import java.util.Random;

import com.wsp.tank.Art;
import com.wsp.tank.BattleScene;
import com.wsp.tank.Escapement;

public class EnemyTank extends Tank {
	
	public final static int ENEMY_TANK_TYPE_1 = 0;
	public final static int ENEMY_TANK_TYPE_2 = 1;
	public final static int ENEMY_TANK_TYPE_3 = 2;
	public final static int ENEMY_TANK_TYPE_4 = 3;
	private int step;
	private int firedBulletChance; //�ӵ��������
	protected boolean isFlickerTank; //��˸̹��
	private int flicker;  //��ֵΪ2ʱΪ��ɫ̹��
	public static int timer;	//��ʱ������Ϊ0ʱ̹�˷����ƶ��������ӵ�
	private Random random = new Random();
	private Escapement flickerEscapement = new Escapement(200);
	private Escapement firedEscapement = new Escapement(200);
	
	public EnemyTank(BattleScene battleScene , int type , int x , int y , int dir , boolean isFlickerTank) {
		super(battleScene , true , x , y , 32 , 32);
		this.type = type;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.isFlickerTank = isFlickerTank;
		shield = type;
		starCount = 4;
		speed = type == 1 ? 2 : 1;
		isMove = true;
//		firedBulletChance = battleScene.stage / 10 * 10 + 10;
//		firedBulletChance = firedBulletChance > 50 ? 50 : firedBulletChance;
		firedBulletChance = battleScene.stage / 10 * 5 + 10;
		firedBulletChance = firedBulletChance > 40 ? 40 : firedBulletChance;
	}
	
	public void update() {
		super.update();
		if(starCount < 1) {
			if(timer < 1) {
				move();
				firedBullet();
			} 
			
			if(isFlickerTank) {
				if(flickerEscapement.isPass()) {
					if(flicker < 1) {
						flicker = 2;
					} else {
						flicker = 0;
					}
				}
			}
		}
	}
	
	@Override
	public void move() {
		//����̹�˵Ĳ����뷽��
		if(step > 0) {
			step--;
		} else {
			lastDir = dir;	//��¼��һ�η����Ա�ȷ��ת��ʱ��̹������
			dir = random.nextInt(4);
			step = random.nextInt(200) + 10;
		}
		super.move();
	}
	
	@Override
	protected void firedBullet() {
		if(firedEscapement.isPass()) {
			if(random.nextInt(100) <= firedBulletChance) {
				super.firedBullet();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if(starCount > 0) {
			g.drawImage(Art.star[0][starFrame], x, y, null);	
		} else {
			int index = type * 4 + tankFrame + flicker;
			switch(dir) {
				case DIR_UP :
					g.drawImage(Art.enemya[0][index], x , y, null);
					break;
				case DIR_DOWN :
					g.drawImage(Art.enemya[2][index], x , y, null);
					break;
				case DIR_LEFT :
					g.drawImage(Art.enemya[3][index], x , y, null);
					break;
				case DIR_RIGHT :
					g.drawImage(Art.enemya[1][index], x , y, null);
					break;
			}
		}
		super.render(g);
	}

}
