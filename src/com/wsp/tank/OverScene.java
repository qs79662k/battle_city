package com.wsp.tank;

import java.awt.Color;
import java.awt.Graphics;

import com.wsp.tank.sound.PlayerSound;
import com.wsp.tank.spirit.PlayerTank;

public class OverScene extends Scene {
	
	private byte[][] over;
	private PlayerSound overSound;
	private int countdown = 10;
	private Escapement e = new Escapement(1000);
	private PlayerTank[] playerTanks;
	
	public OverScene(TankComponent component , PlayerTank[] playerTanks) {
		super(component);
		this.playerTanks = playerTanks;
		overSound = new PlayerSound(Art.hiscore);
		over = new byte[19][31];
		char[] chars = "GAMEOVER".toCharArray();
		for(int y = 0 ; y < 2 ; y++) {
			for(int x = 0 ; x < chars.length / 2 ; x++) {
				byte[][] brickChar = BrickChar.getBrickChar(chars[y * 4 + x]);
				for(int _y = 0 ; _y < brickChar.length ; _y++) {
					for(int _x = 0 ; _x < brickChar[_y].length ; _x++) {
						over[_y + y * 12][_x + x * 8] = brickChar[_y][_x];
					}
				}
			}
		}
	}
	
	@Override
	public void update() {
		if(e.isPass()) {
			if(countdown > 1) {
				countdown--;
			} else {
				int iScore = playerTanks[0] == null ? 0 : playerTanks[0].score;
				int iiScore = playerTanks[1] == null ? 0 : playerTanks[1].score;
				component.toHeader(iScore , iiScore);
			}
			
			if(keys[KEY_ESC] || keys[KEY_F1]) {
				overSound.stop();
				overSound = null;
			}
		}
		super.update();
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0 , TankComponent.WIDTH, TankComponent.HEIGHT);
		for(int y = 0 ; y < over.length ; y++) {
			for(int x = 0 ; x < over[y].length ; x++) {
				int brick = over[y][x];
				if(brick != 0) {
					g.drawImage(Art.tileb[brick % 4][brick / 4] , x * 8 + 132 , y * 8 + 148 , null);
				}	
			}
		}
	}

}
