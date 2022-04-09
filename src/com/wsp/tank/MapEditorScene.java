package com.wsp.tank;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

/**
 * 地图编缉器
 * @author wsp
 * 1，当存在自定义地图文件（与jar同目录的map文件）时将使用此文件中的地图做为编缉地图，如不存在则将创建第一关初始地图做为编缉地图
 * 2，相关操作按钮功能介绍
 * 	 F3：清除当前关卡地图中所有可编缉装饰物
 *   F4：创建一张新地图，做为新增关卡。例如：当前最大关卡为35，则创建的地图为36关
 *   F5：删除当前地图，当删除成功后，当前关之后的关卡将后移。例如：当前被删除的关卡为24，则24关卡之后的关卡则-1
 *   F8：保存当前所有可编缉地图。保存路径为jar所在目录
 */
public class MapEditorScene extends Scene {
	
	private final static String SAVE_OK = "MAPS SAVED SUCCESSFULLY!";
	private final static int EXIST_TIME = 5; //S
	private final static int[] DECORATE_POINT = {219550,236550,219584,236584,219618,236618,219652,236652,219686,236686,219720,236720,219754,236754};
	private byte[][][][] decorates;
	private int stage = 1;
	private byte[][][] maps;
	private int decoratePointIndex;
	private int frame;
	private int saveOkExistTime;
	private TankComponent component;
	private Escapement frameEscapement = new Escapement(500);
	private Escapement stageEscapement = new Escapement(200);
	private Escapement saveEscapement = new Escapement(1000);
	
	public MapEditorScene(TankComponent component) {	
		super(component);
		this.component = component;
		decorates = Map.getDecorates();
		if(!Map.isOldMap) {
			maps = Map.getMaps();
		} else {
			maps = new byte[1][][];
			maps[0] = Map.getTemplateMap();
		}	
	}
	
	@Override
	public void update() {
		//更新场景中可动物体的帧下标
		if(frameEscapement.isPass()) {
			frame = frame > 0 ? 0 : 1;
		}
		//选择编缉地图
		if(stageEscapement.isPass()) {
			//上翻地图
			if(keys[I_KEY_UP]) {
				if(stage < 2) {
					stage = maps.length;
				} else {
					stage--;
				}
			}
			//下翻地图
			if(keys[I_KEY_DOWN]) {
				if(stage > maps.length - 1) {
					stage = 1;
				} else {
					stage++;
				}
			}
			//清空当前地图数据
			if(keys[KEY_F3]) {
				clearMap();
			}
			//创建自定义地图
			if(keys[KEY_F4]) {
				createMap();
			}
			//删除当前地图
			if(keys[KEY_F5]) {
				deleteMap();
			}
			//保存自定义地图
			if(keys[KEY_F8]) {
				saveMaps();
			}
		}
		//地图保存成功提示
		if(saveOkExistTime > 0 && saveEscapement.isPass()) {
			saveOkExistTime--;
		}
		
		if(keys[KEY_F1] || keys[KEY_ESC]) {
			component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		super.update();
	}
	
	@Override
	public void render(Graphics g) {
		//画背景
		g.setColor(new Color(132 , 132 , 132));
		g.fillRect(0, 0, TankComponent.WIDTH, TankComponent.HEIGHT);
		g.setColor(Color.BLACK);
		g.fillRect(TankComponent.GAME_BOX_MARGIN_1, TankComponent.GAME_BOX_MARGIN_1, TankComponent.GAME_BOX_SIZE, TankComponent.GAME_BOX_SIZE);
		//画地图装饰物
		if(maps[stage - 1] != null) {
			for(int y = 0 ; y < maps[stage - 1].length ; y++) {
				for(int x = 0 ; x < maps[stage - 1][y].length ; x++) {
					int decorate = 0xff & maps[stage - 1][y][x];	//& 0xff是为了消除byte转为int时的负数
					if(frame > 0) {
						decorate = Map.getCartoonDecorate(decorate);
					}
					if(decorate != 0) {					
						g.drawImage(Art.tileb[decorate % 4][decorate / 4], x * 8 + 16, y * 8 + 16 , null);
					}
				}	
			}
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(437 , 48 , 70 , 240);
		
		for(int y = 0 ; y < decorates.length ; y++) {
			for(int x = 0 ; x < decorates[y].length ; x++) {
				for(int _y = 0 ; _y < decorates[y][x].length ; _y++) {
					for(int _x = 0 ; _x < decorates[y][x][_y].length ; _x++) {
						int decorate = 0xff & decorates[y][x][_y][_x];	//& 0xff是为了消除byte转为int时的负数
						if(frame > 0) {
							decorate = Map.getCartoonDecorate(decorate);
						}
						if(decorate != 0) {					
							g.drawImage(Art.tileb[decorate % 4][decorate / 4], x * 32 + _x * 8 + x * 2 + 439, y * 32 + _y * 8 + y * 2 + 50 , null);
						} else {
							g.setColor(Color.BLACK);
							g.fillRect(x * 32 + _x * 8 + x * 2 + 439, y * 32 + _y * 8 + y * 2 + 50 , 8 , 8);
						}
					}
				}
			}
		}
		
		//鼠标进入与选择装饰物
		if(pointX > 450 && pointX < 520 && pointY > 80 && pointY < 327) {		
			int x = (pointX - 451) / 34 + ((pointX - 451) / 34 > 0 ? ((pointX - 451) % 34 == 0 ? -1 : 0) : 0);
			int y = (pointY - 81) / 35 + ((pointY - 81) % 35 == 0 ? 0 : 1);		
			int decoratePointIndex = x + (y == 0 ? 0 : y - 1) * 2;			
			g.setColor(new Color(254 , 254 , 65 , 100));
			g.fillRect(DECORATE_POINT[decoratePointIndex] / 500 , DECORATE_POINT[decoratePointIndex] % 500, 32, 32);
			//选择装饰物
			if(isMousePressed) {
				this.decoratePointIndex = decoratePointIndex;
			}
		}
		
		//鼠标进入游戏框与生成装饰物
		if(pointX > 18 && pointX < 447 && pointY > 46 && pointY < 477) {
			int x = (pointX - 19) / 33;
			int y = (pointY - 48) / 33;
			component.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			if(x > 4 && x < 8 && y > 10) {
				g.setColor(new Color(255 , 0 , 0 , 150));
				g.fillRect(x * 32 + 16 , y * 32 + 16 , 32 , 32);
			} else {				
				int dx = ((decoratePointIndex + 1) % 2) == 0 ? 1 : 0;
				int dy = (decoratePointIndex + 1) / 2 + ((decoratePointIndex + 1) % 2 == 0 ? 0 : 1) - 1;
				byte[][] decorate = decorates[dy][dx];
				for(int _y = 0 ; _y < decorate.length ; _y++) {
					for(int _x = 0 ; _x < decorate[_y].length ; _x++) {
						int _decorate = 0xff & decorate[_y][_x];
						if(frame > 0) {
							_decorate = Map.getCartoonDecorate(_decorate);
						}
						if(_decorate != 0) {
							g.drawImage(Art.tileb[_decorate % 4][_decorate / 4] , x * 32 + 16 + _x * 8 , y * 32 + 16 + _y * 8 , null);
						} else {
							g.setColor(Color.BLACK);
							g.fillRect(x * 32 + 16 + _x * 8 , y * 32 + 16 + _y * 8 , 8 , 8);
						}	
					}
				}
				//单击鼠标左键时替换地图装饰物
				if(isMousePressed) {
					for(int _y = 0 ; _y < 4 ; _y++) {
						for(int _x = 0 ; _x < 4 ; _x++) {
							maps[stage - 1][y * 4 + _y][x * 4 + _x] = decorate[_y][_x];						
						}
					}					
				}
				FlowDottedLine.drawFlowDottedLine(g, x * 32 + 17 , y * 32 + 17 , 30 , 30 , 6 , 1);
			}
		} else {
			component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		g.drawImage(Art.flag, 458, 352 , 32, 32, null);
		char[] chars = Integer.toString(stage).toCharArray();
		for(int i = 0 ; i < chars.length ; i++) {
//			g.drawImage(Art.numletterB[0][chars[i] - 48], 458 + i * 16, 389 , 16, 16, null);
			g.drawImage(Art.numletterB[0][chars[i] - 48], 432 + ((80 - chars.length * 16) / 2) + i * 16, 389 , 16, 16, null);
		}
		
		g.setColor(new Color(254 , 254 , 65 , 100));
		g.fillRect(DECORATE_POINT[decoratePointIndex] / 500 , DECORATE_POINT[decoratePointIndex] % 500, 32, 32);
		FlowDottedLine.drawFlowDottedLine(g, DECORATE_POINT[decoratePointIndex] / 500 , DECORATE_POINT[decoratePointIndex] % 500 , 31 , 31 , 6 , 1);
		
		//画经纬线
		g.setColor(Color.WHITE);
		for(int y = 0 ; y < TankComponent.GAME_BOX_SIZE / 32 + 1 ; y++) {
			for(int x = 0 ; x < TankComponent.GAME_BOX_SIZE / 32 ; x++) {
				g.drawLine(TankComponent.GAME_BOX_MARGIN_1 , y * 32 + TankComponent.GAME_BOX_MARGIN_1 , TankComponent.GAME_BOX_SIZE + TankComponent.GAME_BOX_MARGIN_1, y * 32 + TankComponent.GAME_BOX_MARGIN_1);
				g.drawLine(y * 32 + TankComponent.GAME_BOX_MARGIN_1 , TankComponent.GAME_BOX_MARGIN_1 , y * 32 + TankComponent.GAME_BOX_MARGIN_1 , TankComponent.GAME_BOX_SIZE + TankComponent.GAME_BOX_MARGIN_1);
			}
		}
		
		//保存地图成功提示信息
		if(saveOkExistTime > 0) {
			chars = SAVE_OK.toCharArray();
			int x = (TankComponent.GAME_BOX_SIZE - chars.length * 16) / 2 + TankComponent.GAME_BOX_MARGIN_1;
			int y = (TankComponent.GAME_BOX_SIZE - 16) / 2 + TankComponent.GAME_BOX_MARGIN_1;
			for(int i = 0 ; i < chars.length ; i++) {
				g.drawImage(Art.font[0][chars[i] - 32], x + i * 16 + 1, y + 1, 16 , 16 , null);
				g.drawImage(Art.font[7][chars[i] - 32], x + i * 16, y, 16 , 16 , null);				
			}
		}
	}
	
	/**
	 * 清除自定义地图中多余的装饰物(恢复为地图初始装态)
	 */
	private void clearMap() {
		byte[][] templateMap = Map.getTemplateMap();
		for(int y = 0 ; y < maps[stage - 1].length ; y++) {
			for(int x = 0 ; x < maps[stage - 1][y].length ; x++) {
				maps[stage - 1][y][x] = templateMap[y][x];
			}
		}
	}
	
	/**
	 * 删除当前stage自定义地图
	 */
	private void deleteMap() {
		byte[][][] maps = null;
		if(this.maps.length == 1) {
			maps = this.maps;
			maps[stage - 1] = Map.getTemplateMap();
		} else {
			maps = new byte[this.maps.length - 1][][];			
			if(stage == 1) {
				System.arraycopy(this.maps , stage, maps, 0, maps.length);
			} else {
				System.arraycopy(this.maps , 0, maps, 0, stage - 1);
				if(stage < this.maps.length) {
					System.arraycopy(this.maps , stage, maps, stage - 1, this.maps.length - stage);
				} else {
					stage--;
				}					
			}
		}
		this.maps = maps;
	}
	
	/**
	 * 创建自定义地图
	 */
	private void createMap() {		
		byte[][][] maps = new byte[this.maps.length + 1][][];
		System.arraycopy(this.maps, 0, maps, 0, this.maps.length);
		maps[maps.length - 1] = Map.getTemplateMap();
		stage = maps.length;		
		this.maps = maps;
	}
	
	/**
	 * 保存自义定地图
	 */
	private void saveMaps() {
		Map.saveMaps(maps);
		saveOkExistTime = EXIST_TIME;
	}
	
}
