package com.wsp.tank.robot;

import com.wsp.tank.BattleScene;
import com.wsp.tank.Rectangle;
import com.wsp.tank.spirit.PlayerTank;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;

public class CollectTreasureAction extends Action {
	
	public CollectTreasureAction(BattleScene battleScene , PlayerTank playerTank) {
		super(battleScene, playerTank);
	}

	@Override
	public boolean action() {
		if(battleScene.treasure != null) {
			return collectTreasure();
		}
		return false;
	}
	
	/**
	 * 拾取宝物
	 * 拾取条件：
	 * 1，宝物与当前坦克在同一弹道中(与宝物之间的直线)
	 * 2，弹道(与宝物之间)中不存在障碍物
	 * 3，弹道中不存在其它坦克
	 * @return 正在向宝物前进返回true，否则false
	 */
	private boolean collectTreasure() {
		if(battleScene.treasure != null) {
			for(int dir = 0 ; dir < 4 ; dir++) {
				Rectangle tank = playerTank.getRectangle(dir);
				Trajectory t = Trajectory.createTrajectory(battleScene.map, dir, tank, battleScene.treasure);  //弹道如为null值说明宝物与当前坦克不在同一直线中
				if(t != null) {		
					//如果有坦克阻挡返回false
					for(Tank _tank : battleScene.tanks) {
						if(_tank != playerTank) {
							switch(dir) {
								case Spirit.DIR_UP :
									if(playerTank.isUpRelativeIntersects(_tank) && _tank.y + _tank.height >= battleScene.treasure.y + battleScene.treasure.height) {
										return false;
									}
									break;
								case Spirit.DIR_DOWN :
									if(playerTank.isDownRelativeIntersects(_tank) && _tank.y <= battleScene.treasure.y) {
										return false;
									}
									break;
								case Spirit.DIR_LEFT :
									if(playerTank.isLeftRelativeIntersects(_tank) && _tank.x + _tank.width >= battleScene.treasure.x + battleScene.treasure.width) {
										return false;
									}
									break;
								case Spirit.DIR_RIGHT :
									if(playerTank.isRightRelativeIntersects(_tank) && _tank.x <= battleScene.treasure.x) {
										return false;
									}
									break;
							}
						}
					}
					
					//与宝物之间是否存在障碍物
					if(t.trajectoryDecorateCount[Trajectory.BRICK_DECORATE] == 0 && t.trajectoryDecorateCount[Trajectory.STEEL_DECORATE] == 0 && t.trajectoryDecorateCount[Trajectory.BASE_DECORATE] == 0) {
						//如果没有船，并且弹道中存在河流不可前进拾取宝物
						if(!playerTank.ship && t.trajectoryDecorateCount[Trajectory.RIVERS_DECORATE] != 0) {
							return false;
						}
						//当前行进方向安全方可前进拾取宝物
						if(!Danger.isDirectionDanger(battleScene , dir , playerTank.isEnemy , tank)) {
							move(dir);
							return true;
						}
					}
					return false;
				}
			}
		}
		return false;
	}
	
}
