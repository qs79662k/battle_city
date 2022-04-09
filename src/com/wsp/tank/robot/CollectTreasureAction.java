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
	 * ʰȡ����
	 * ʰȡ������
	 * 1�������뵱ǰ̹����ͬһ������(�뱦��֮���ֱ��)
	 * 2������(�뱦��֮��)�в������ϰ���
	 * 3�������в���������̹��
	 * @return ��������ǰ������true������false
	 */
	private boolean collectTreasure() {
		if(battleScene.treasure != null) {
			for(int dir = 0 ; dir < 4 ; dir++) {
				Rectangle tank = playerTank.getRectangle(dir);
				Trajectory t = Trajectory.createTrajectory(battleScene.map, dir, tank, battleScene.treasure);  //������Ϊnullֵ˵�������뵱ǰ̹�˲���ͬһֱ����
				if(t != null) {		
					//�����̹���赲����false
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
					
					//�뱦��֮���Ƿ�����ϰ���
					if(t.trajectoryDecorateCount[Trajectory.BRICK_DECORATE] == 0 && t.trajectoryDecorateCount[Trajectory.STEEL_DECORATE] == 0 && t.trajectoryDecorateCount[Trajectory.BASE_DECORATE] == 0) {
						//���û�д������ҵ����д��ں�������ǰ��ʰȡ����
						if(!playerTank.ship && t.trajectoryDecorateCount[Trajectory.RIVERS_DECORATE] != 0) {
							return false;
						}
						//��ǰ�н�����ȫ����ǰ��ʰȡ����
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
