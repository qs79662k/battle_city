package com.wsp.tank.robot;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Scene;
import com.wsp.tank.spirit.PlayerTank;
import com.wsp.tank.spirit.Spirit;

public abstract class Action {
	
	protected BattleScene battleScene;
	
	protected PlayerTank playerTank;
	
	
	public Action(BattleScene battleScene , PlayerTank playerTank) {
		this.battleScene = battleScene;
		this.playerTank = playerTank;
	}
	
	public abstract boolean action();
	
	protected void move(int dir) { 
		switch(dir) {
			case Spirit.DIR_UP :
				battleScene.toggleKey(Scene.I_KEY_UP + playerTank.on * 5, true);
				break;
			case Spirit.DIR_DOWN :
				battleScene.toggleKey(Scene.I_KEY_DOWN + playerTank.on * 5, true);
				break;
			case Spirit.DIR_LEFT :
				battleScene.toggleKey(Scene.I_KEY_LEFT + playerTank.on * 5, true);
				break;
			case Spirit.DIR_RIGHT :
				battleScene.toggleKey(Scene.I_KEY_RIGHT + playerTank.on * 5, true);
				break;
		}	 
	}
	
}
