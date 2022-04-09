package com.wsp.tank.robot;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Scene;
import com.wsp.tank.spirit.PlayerTank;

public class Robot {
	
	private Action dodge;
	private Action patrol;
	private Action attack;
	private Action collectTreasure;
	private BattleScene battleScene;
	private PlayerTank playerTank;
	
	public Robot(BattleScene battleScene , PlayerTank playerTank) {
		this.battleScene = battleScene;
		this.playerTank = playerTank;
		patrol = new PatrolAction(battleScene , playerTank);
		attack = new AttackAction(battleScene, playerTank);
		dodge = new DodgeAction(battleScene , playerTank);
		collectTreasure = new CollectTreasureAction(battleScene , playerTank);
	}
	
	public boolean action() {
		//释放所有按键
		battleScene.toggleKey(Scene.I_KEY_UP + playerTank.on * 5, false);
		battleScene.toggleKey(Scene.I_KEY_DOWN + playerTank.on * 5, false);
		battleScene.toggleKey(Scene.I_KEY_LEFT + playerTank.on * 5, false);
		battleScene.toggleKey(Scene.I_KEY_RIGHT + playerTank.on * 5, false);
		battleScene.toggleKey(Scene.I_KEY_SHOOT + playerTank.on * 5, false);   
		attack.action();  
		if(!dodge.action()) {
			if(!collectTreasure.action()) {
				patrol.action();
			}
		}
		return true;
	}
	
}
