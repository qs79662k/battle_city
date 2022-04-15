package com.wsp.tank.spirit;

import java.awt.Graphics;

import com.wsp.tank.Art;
import com.wsp.tank.BattleScene;
import com.wsp.tank.Escapement;

public class Score implements Spirit {
	
	public final static int SCORE_100 = 0;
	public final static int SCORE_200 = 1;
	public final static int SCORE_300 = 2;
	public final static int SCORE_400 = 3;
	public final static int SCORE_500 = 4;
	private int x;
	private int y;
	private int type;
	private BattleScene battleScene;
	private int showCount = 2;	//停留显示2S
	private Escapement e = new Escapement(1000);
	
	public Score(BattleScene battleScene , int type , int x , int y) {
		this.battleScene = battleScene;
		this.type = type;
		this.x = x;
		this.y = y;		
	}

	@Override
	public void update() {
		if(e.isPass()) {
			if(showCount > 1) {
				showCount--;
			} else {
				battleScene.spirits.remove(this);
			}		
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Art.score[0][type], x, y, null);
	}

}
