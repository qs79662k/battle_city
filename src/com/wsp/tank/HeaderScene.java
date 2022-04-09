package com.wsp.tank;

import java.awt.Color;
import java.awt.Graphics;

public class HeaderScene extends Scene {
	
	public final static int PLAYER_Y = 247;
	public final static int PLAYERS_Y = 278;
	public final static int CONS_TRUCTION_Y = 309;
	private int[][] tank90;
	private int hy = 448;
	private int cursorX = 135;	//选择游标x、y坐标
	private int cursorY = 247;
	private int iScore;		//玩家1、2得分
	private int iiScore;
	private int hiScore = 20000;
	private int cursorFrame;
	private int countdown = 10;
	private TankComponent component;
	private Escapement cursorEscapement = new Escapement(16);
	private Escapement selectEscapement = new Escapement(100);
	private Escapement countdownEscapement = new Escapement(1000);
	
	public HeaderScene(TankComponent component) {
		this(component , 0 , 0);
	}
	
	public HeaderScene(TankComponent component , int iScore , int iiScore) {
		super(component);
		this.component = component;
		this.iScore = iScore;
		this.iiScore = iiScore;
		init();
	}
	
	private void init() {
		if(TankComponent.isTank90) {
			tank90 = new int[17][47];
			char[] chars = "TANKER".toCharArray();
			for(int i = 0 ; i < chars.length ; i++) {
				byte[][] brickChar = BrickChar.getBrickChar(chars[i]);
				for(int y = 0 ; y < brickChar.length ; y++) {
					for(int x = 0 ; x < brickChar[y].length ; x++) {
						tank90[y][x + i * 8] = brickChar[y][x];
					}
				}
			}
			chars = "1990".toCharArray();
			for(int i = 0 ; i < chars.length ; i++) {
				byte[][] brickChar = BrickChar.getBrickChar(chars[i]);
				for(int y = 0 ; y < brickChar.length ; y++) {
					for(int x = 0 ; x < brickChar[y].length ; x++) {
						tank90[y + 10][x + i * 8 + 8] = brickChar[y][x];
					}
				}
			}
		}
	}
	
	@Override
	public void update() {
		if(hy > 0) {
			hy--;
			if(keys[KEY_ENTER]) {
				hy = 0;
			}
		} else {
			if(selectEscapement.isPass()) {
				if(keys[I_KEY_DOWN]) {
					if(cursorY == PLAYER_Y) {
						cursorY = PLAYERS_Y;
					} else if(cursorY == PLAYERS_Y) {
						cursorY = CONS_TRUCTION_Y;
					} else {
						cursorY = PLAYER_Y;
					}
					countdown = 10;
				} else if(keys[I_KEY_UP]) {
					if(cursorY == PLAYER_Y) {
						cursorY = CONS_TRUCTION_Y;
					} else if(cursorY == PLAYERS_Y) {
						cursorY = PLAYER_Y;
					} else {
						cursorY = PLAYERS_Y;
					}
					countdown = 10;
				}
			}
			
			//场景选择
			if(keys[I_KEY_SHOOT]) {
				if(cursorY == PLAYER_Y) {
					component.toBattle(1 , false);
				} else if(cursorY == PLAYERS_Y) {
					component.toBattle(1 , true);
				} else {
					component.toMapEditor();
				} 
			}
			
			if(cursorEscapement.isPass()) {
				cursorFrame = cursorFrame > 0 ? 0 : 1;
			}
			
			if(countdownEscapement.isPass()) {
				if(countdown < 1) {
					component.toBattle();
				} else {
					countdown--;
				}
			}
		}
		
		super.update();
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0 , TankComponent.WIDTH, TankComponent.HEIGHT);
		g.drawImage(Art.title, 0 , hy , null);
		
		g.drawImage(Art.iconW[0][2], 32 , hy + 32 , 16 , 16 , null);
		g.drawImage(Art.iconW[0][6], 48, hy + 32, 16 , 16 , null);
		char[] chars = Integer.toString(iScore).toCharArray();
		for(int i = 0 ; i < chars.length ; i++) {
			g.drawImage(Art.numletterW[0][chars[i] - 48], (144 - chars.length * 16) + i * 16 , hy + 32, 16 , 16 , null);
		}
		
		g.drawImage(Art.numletterW[0][17], 176 , hy + 32, 16 , 16 , null);
		g.drawImage(Art.numletterW[0][18], 192, hy + 32, 16 , 16 , null);
		g.drawImage(Art.numletterW[0][39], 208, hy + 32 , 16, 16, null);
		chars = Integer.toString(hiScore).toCharArray();
		for(int i = 0 ; i < chars.length ; i++) {
			g.drawImage(Art.numletterW[0][chars[i] - 48], 240 + i * 16 , hy + 32, 16 , 16 , null);
		}
		
		g.drawImage(Art.iconW[0][4] , 352 , hy + 32, 16, 16, null);
		g.drawImage(Art.iconW[0][6], 368, hy + 32, 16 , 16 , null);
		chars = Integer.toString(iiScore).toCharArray();
		for(int i = 0 ; i < chars.length ; i++) {
			g.drawImage(Art.numletterW[0][chars[i] - 48], (464 - chars.length * 16) + i * 16 , hy + 32, 16 , 16 , null);
		}
		
		if(TankComponent.isTank90) {
			if(tank90 == null) {
				init();
			}
			g.setColor(Color.BLACK);
			g.fillRect(50 , 80 + hy , 376, 136);
			for(int y = 0 ; y < tank90.length ; y++) {
				for(int x = 0 ; x < tank90[y].length ; x++) {
					int decorate = tank90[y][x];	
					if(decorate != 0) {
						g.drawImage(Art.tileb[decorate % 4][decorate / 4], 64 + x * 8, 80 + y * 8 + hy, null);
					}					
				}
			}
		}
		
		if(hy == 0) {
			g.drawImage(Art.player1a[1][cursorFrame], cursorX, cursorY, null);
		}
	}

}
