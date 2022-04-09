package com.wsp.tank;

import java.awt.Graphics;

import com.wsp.tank.sound.PlayerSound;
import com.wsp.tank.spirit.PlayerTank;

/**
 * 战果统计场景
 * @author wsp
 */
public class ResultsBattleScene extends Scene {
	
	private int hiScore = 20000;
	private int stage;
	private boolean isOver;  //游戏是否已结束
	private boolean isPlayers;
	private PlayerTank[] playerTanks;
	private int countdown = 10;	//场景倒计时，当为0时跳转到其它场景
	private int[] iKillEnemys = new int[4];
	private int[] iiKillEnemys = new int[4];
	private int index;
	private boolean showTotal;
	private Escapement killEscapement = new Escapement(200);
	private Escapement countdwonEscapement = new Escapement(1000);
	
	public ResultsBattleScene(TankComponent component , int stage , boolean isPlayers , boolean isOver , PlayerTank[] playerTanks) {
		super(component);
		this.stage = stage;
		this.isOver = isOver;
		this.isPlayers = isPlayers;
		this.playerTanks = playerTanks;
		killEscapement.execute();
	}
	
	public void update() {
		super.update();
		
		//统计击杀的敌人坦克
		if(killEscapement.isPass()) {
			int iEnemys = playerTanks[0] == null ? 0 : playerTanks[0].killEnemys[index];
			int iiEnemys = playerTanks[1] == null ? 0 : playerTanks[1].killEnemys[index];
			
			if(iEnemys > 0 || iiEnemys > 0) {
				new PlayerSound(Art.countscore);
			}
			
			if(iEnemys > 0 && playerTanks[0] != null) {
				iEnemys--;
				iKillEnemys[index]++;
				playerTanks[0].killEnemys[index]--;
			}
			
			if(iiEnemys > 0 && playerTanks[1] != null) {
				iiEnemys--;
				iiKillEnemys[index]++;
				playerTanks[1].killEnemys[index]--;
			}
			
			if(iEnemys < 1 && iiEnemys < 1) {
				if(!showTotal && index > iiKillEnemys.length - 2) {
					showTotal = true;
					new PlayerSound(Art.bonus1000);
				}
				
				if(index < iKillEnemys.length - 1) {
					index++;
				}
			}
		}
		
		//倒计时场景切换
		if(index > iiKillEnemys.length - 2) {
			if(countdwonEscapement.isPass()) {
				if(countdown > 1) {
					countdown--;
				} else {
					if(isOver) {
						component.toOver(playerTanks);	
					} else {
						for(int i = 0 ; i < playerTanks.length ; i++) {
							PlayerTank playerTank = playerTanks[i];
							if(playerTank != null) {
//								playerTank.score = 0;
								if(!playerTank.die) {
									playerTank.refresh = true;
								}
							}
						}
						component.toBattle(++stage, isPlayers, playerTanks);
					}
				}
			}
		}
	}

	int bx , px;
	boolean isBonus = true;
	
	@Override
	public void render(Graphics g) {
		int iTotal = 0;
		
		g.drawImage(Art.scoreboard , 0 , 0 , null);
		char[] chars = Integer.toString(hiScore).toCharArray();
		for(int i = 0 ; i < chars.length ; i++) {
			g.drawImage(Art.numletterC[0][chars[i] - 48] , 304 + i * 16 , 32, 16, 16 , null);
		}
		
		//关卡
		chars = Integer.toString(stage).toCharArray();
		for(int i = 0 ; i < chars.length ; i++) {
			g.drawImage(Art.numletterW[0][chars[i] - 48] , 304 + i * 16 , 64, 16, 16 , null);
		}
		
		if(playerTanks[0] != null) {
			chars = Integer.toString(playerTanks[0].score).toCharArray();
			for(int i = 0 ; i < chars.length ; i++) {
				g.drawImage(Art.numletterC[0][chars[i] - 48], (112 - chars.length * 8) + i * 16 , 132, 16, 16, null);
			}
			//画坦克击杀数量与得分
			for(int i = 0 ; i <= index ; i++) {
				//画得分
				chars = Integer.toString(iKillEnemys[i] * (i * 100 + 100)).toCharArray();
				for(int j = 0 ; j < chars.length ; j++) {
					g.drawImage(Art.numletterW[0][chars[j] - 48], 50 + j * 16 , i * 48 + 176, 16, 16, null);
				}
				//画坦克击杀数量
				chars = Integer.toString(iKillEnemys[i]).toCharArray();
				for(int j = 0 ; j < chars.length ; j++) {
					g.drawImage(Art.numletterW[0][chars[j] - 48], (iKillEnemys[i] < 10 ? 196 : 186) + j * 16 , i * 48 + 176, 16, 16, null);
				}
			}
			
			if(showTotal) {
				for(int i = 0 ; i < iKillEnemys.length ; i++) {
					iTotal += iKillEnemys[i];
				}
				chars = Integer.toString(iTotal).toCharArray();
				for(int i = 0 ; i < chars.length ; i++) {
					g.drawImage(Art.numletterW[0][chars[i] - 48], (iTotal < 10 ? 196 : 186) + i * 16 , 352, 16, 16, null);
				}
			}
		}
		
		if(playerTanks[1] != null) {
			g.drawImage(Art.numletterW[0][38], 273 , 176, 16, 16, null);
			g.drawImage(Art.numletterW[0][38], 273 , 224, 16, 16, null);
			g.drawImage(Art.numletterW[0][38], 273 , 272, 16, 16, null);
			g.drawImage(Art.numletterW[0][38], 273 , 320, 16, 16, null);
			for(int i = 0 ; i < iiKillEnemys.length ; i++) {
				g.drawImage(Art.numletterW[0][38], 273 , i * 48 + 176, 16, 16, null);
			}
			g.drawImage(Art.iconH[0][4], 336 , 96, 16, 16, null);
			g.drawImage(Art.iconH[0][6], 352 , 96, 16, 16, null);
			chars = "PLAYER".toCharArray();
			for(int i = 0 ; i < chars.length ; i++)
			{
				g.drawImage(Art.numletterH[0][chars[i] - 55], 368 + i * 16 , 96, 16, 16, null);
			}
			
			chars = "PTS".toCharArray();
			for(int i = 0 ; i < iiKillEnemys.length ; i++) {
				for(int j = 0 ; j < chars.length ; j++) {
					g.drawImage(Art.numletterW[0][chars[j] - 55], 337 + j * 16 , i * 48 + 176, 16, 16, null);
				}
			}
			
			chars = Integer.toString(playerTanks[1].score).toCharArray();
			for(int i = 0 ; i < chars.length ; i++) {
				g.drawImage(Art.numletterC[0][chars[i] - 48], (400 - chars.length * 8) + i * 16 , 132, 16, 16, null);
			}
			
			//画坦克击杀数量与得分
			for(int i = 0 ; i <= index ; i++) {
				//画得分
				chars = Integer.toString(iiKillEnemys[i] * (i * 100 + 100)).toCharArray();
				for(int j = 0 ; j < chars.length ; j++) {
					g.drawImage(Art.numletterW[0][chars[j] - 48], (465 - chars.length * 16) + j * 16 , i * 48 + 176, 16, 16, null);
				}
				//画坦克击杀数量
				chars = Integer.toString(iiKillEnemys[i]).toCharArray();
				for(int j = 0 ; j < chars.length ; j++) {
					g.drawImage(Art.numletterW[0][chars[j] - 48], (iiKillEnemys[i] < 10 ? 302 : 294) + j * 16 , i * 48 + 176, 16, 16, null);
				}
			}
			
			if(showTotal) {
				int iiTotal = 0;
				for(int i = 0 ; i < iiKillEnemys.length ; i++) {
					iiTotal += iiKillEnemys[i];
				}
				chars = Integer.toString(iiTotal).toCharArray();
				for(int i = 0 ; i < chars.length ; i++) {
					g.drawImage(Art.numletterW[0][chars[i] - 48], (iiTotal < 10 ? 302 : 294) + i * 16 , 352, 16, 16, null);
				}
				//画击杀坦克最多奖励
				if(iTotal != iiTotal) {
					if(isBonus) {
						if(iTotal > iiTotal) {
							playerTanks[0].score += 1000;
						} else {
							bx = 318;
							px = 286;
							playerTanks[1].score += 1000;
						}
						isBonus = false;
					}
					chars = "BONUS".toCharArray();
					for(int i = 0 ; i < chars.length ; i++) {
						g.drawImage(Art.numletterH[0][chars[i] - 55], 50 + i * 16 + bx , 384, 16, 16, null);
					}
					g.drawImage(Art.numletterH[0][36], 130 + bx , 384, 16, 16, null);
					chars = "1000".toCharArray();
					for(int i = 0 ; i < chars.length ; i++) {
						g.drawImage(Art.numletterW[0][chars[i] - 48], 50 + i * 16 + px , 408, 16, 16, null);
					}
					chars = "PTS".toCharArray();
					for(int i = 0 ; i < chars.length ; i++) {
						g.drawImage(Art.numletterW[0][chars[i] - 55], 130 + i * 16 + px , 408, 16, 16, null);
					}
				}
			}
		}
	}

}
