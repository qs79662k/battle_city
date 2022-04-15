package com.wsp.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wsp.tank.robot.Robot;
import com.wsp.tank.sound.PlayerSound;
import com.wsp.tank.spirit.Bullet;
import com.wsp.tank.spirit.EnemyTank;
import com.wsp.tank.spirit.PlayerTank;
import com.wsp.tank.spirit.Spirit;
import com.wsp.tank.spirit.Tank;
import com.wsp.tank.spirit.Treasure;

public class BattleScene extends Scene {
	
	private final static int[] ENEMY_BORN_POINTS = {224, 32 , 416};  //敌人出生点
	public final static int[] PLAYER_BORN_POINTS = {80400, 144400};  //500，我方出生点
	private int enemyBornPointIndex;
	private boolean isPlayers;  //是否为双人模式
	public int stage;	//关卡
	private boolean isCurtain = true;  //为true显示幕布界面
	private int curtainHeight;	//上下幕布的高
	public byte[][] map;
	private int decorateFrame;  //可动装饰物帧
	public int existEnemyCount = 20;		//未刷新出的敌人坦克数量
	public int activeEnemyCount;	//在场景中活动的敌人坦克数量
	public int maxActiveEnemyCount; //在场景中最大活动的敌人坦克数量，每10关增加一辆
	private int flickerEnemyCount;		//闪烁敌人坦克剩余刷新数量，每个关卡刷新1-2辆闪烁敌人坦克
	public boolean isBaseDestroy;		//为true时基地被摧毁
	public List<Tank> tanks = new ArrayList<Tank>(); 
	public List<Bullet> bullets = new ArrayList<Bullet>();
	public List<Spirit> spirits = new ArrayList<Spirit>();		//爆炸、得分
	public Treasure treasure;	//宝物
	private Random random = new Random();
	private PlayerSound beginSound;	//开始音乐
	private PlayerSound playerMoveSound; //玩家移动声效
	private boolean notPlayerMoveSound = true;  //当为false未开始玩家移动声效
	private PlayerSound enemyMoveSound;	//敌人移动声效
	public PlayerTank[] playerTanks = new PlayerTank[2];
	public int steelWallTimer;  //基地钢墙定时器，当为0钢墙将会变成砖墙
	public int wallType = Map.BRICK_WALL_TYPE; //基地墙类型
	private boolean wallChange;
	private int overCountdown = 10;		//当前关卡结束倒计时，为0时进入杀敌统计场景
	private int startCountdown = 5;	//开始游戏倒计时
	private int demoCountdown = 999;  //最大演示时间
	private Escapement selectEscapement = new Escapement(300);
	private Escapement frameEscapement = new Escapement(500);
	private Escapement enemyEscapement = new Escapement(4000);
	private Escapement timerEscapement = new Escapement(1000);
	private Escapement wallEscapement = new Escapement(1000);
	private Escapement wallChangeEscapement = new Escapement(500);
	private Escapement countdwonEscapement = new Escapement(1000);
	
	private boolean isGameOver;
	private boolean isPlayer1Over;
	private boolean isPlayer2Over;
	private int gameOverY = 400;
	private int player1OverX = 32;  //x坐标32  144
	private int player2OverX = 384; //x坐标384  272
	
	public boolean isDemo;
	private Robot[] robots = new Robot[2];
	
	public BattleScene(TankComponent component) {
		this(component , 20 , true , null);
		isDemo = true;
	}
	
	public BattleScene(TankComponent component , int stage , boolean isPlayers) {
		this(component , stage , isPlayers , null);
	}
	
	public BattleScene(TankComponent component , int stage , boolean isPlayers , PlayerTank[] playerTanks) {
		super(component);
		this.stage = stage;
		this.isPlayers = isPlayers;
		flickerEnemyCount = TankComponent.isTank90 ? random.nextInt(4) + 1 : random.nextInt(2) + 1;  
		if(playerTanks != null) {
			this.playerTanks = playerTanks;
		}
		enemyEscapement.execute();	
	}
	
	@Override
	public void update() {
		if(curtainHeight == TankComponent.HEIGHT / 2) {
			/**
			 * 选择关卡
			 * down:下翻
			 * up:上翻
			 */
			if(!isDemo) {
				if(selectEscapement.isPass()) {
					if(keys[I_KEY_DOWN]) {
						if(Map.getSize() > stage) {
							stage++;
						} else {
							stage = 1;
						}
						startCountdown = 5;
					} else if(keys[I_KEY_UP]) {
						if(stage < 2) {
							stage = Map.getSize();
						} else {
							stage--;
						}
						startCountdown = 5;
					}
				}
				
				if(keys[I_KEY_SHOOT]) {
					start();
				}
			}
			
			if(countdwonEscapement.isPass()) {
				if(startCountdown > 1) {
					startCountdown--;
				} else {
					start();
				}
			}
		}
		
		//打开幕布
		if(isCurtain && curtainHeight < TankComponent.HEIGHT / 2) {
			curtainHeight += 8;
		}
		
		if(!isCurtain) {
			//关闭幕布
			if(curtainHeight > 0) {
				curtainHeight -= 8;
			} else {
				//战斗场景装饰物动画帧
				if(frameEscapement.isPass()) {
					decorateFrame = decorateFrame > 0 ? 0 : 1;		
				}
				
				for(int i = 0 ; i < tanks.size(); i++) {
					Tank tank = tanks.get(i);
					tank.update();
				}
				
				for(int i = 0 ; i < bullets.size(); i++) { //子弹
					Bullet bullet = bullets.get(i);
					bullet.update();
				}
				
				for(int i = 0 ; i < spirits.size() ; i++) { //爆炸、击杀坦克得分
					Spirit spirit = spirits.get(i);
					spirit.update();
				}
				
				if(treasure != null) { //宝物
					treasure.update();
				}
				
				createTanks();
				
				//游戏结束(基地被摧毁或玩家生命为0)
				int iLife = playerTanks[0] == null ? 0 : playerTanks[0].life;
				int iiLife = playerTanks[1] == null ? 0 : playerTanks[1].life;
				boolean iDie = playerTanks[0] == null ? true : playerTanks[0].die;
				boolean iiDie = playerTanks[1] == null ? true : playerTanks[1].die;
				if(isBaseDestroy || (iLife == 0 && iiLife == 0 && iDie && iiDie)) {
					if(!isGameOver) {
						isGameOver = true;
					}
					
					for(int i = 0 ; i < 2 ; i++) {
						if(gameOverY > ((TankComponent.GAME_BOX_SIZE - 32) / 2 + 16)) {
							gameOverY--;
						}
					}
					
					//倒计时进入战果统计场景
					if(countdwonEscapement.isPass()) {
						if(overCountdown > 1) {
							overCountdown--;
						} else {
							stop(); //关闭所有声效
							if(isDemo) {
								component.toHeader();
							} else {
								component.toResultsBattle(stage , isPlayers , true, playerTanks);  //进入战果统计场景
							}
						}
					}
				}
				
				//消灭所有敌人后倒计时进入战果统计场景
				if(activeEnemyCount < 1 && existEnemyCount < 1) {
					//倒计时进入战果统计场景
					if(countdwonEscapement.isPass()) {
						if(overCountdown > 1) {
							overCountdown--;
						} else {
							stop(); //关闭所有声效
							if(isDemo) {
								component.toHeader();
							} else {
								component.toResultsBattle(stage , isPlayers , false, playerTanks);  //进入战果统计场景
							}
						}
					}
				}
				
				//玩家结束游戏
				if(!isGameOver) {
					if(playerTanks[0] != null) {
						if(!isPlayer1Over && iLife == 0 && iDie) {
							isPlayer1Over = true;
						}
					}
					
					if(playerTanks[1] != null) {
						if(!isPlayer2Over && iiLife == 0 && iiDie) {
							isPlayer2Over = true;
						}
					}
				}
				
				if(isPlayer1Over) {
					for(int i = 0 ; i < 2 ; i++) {
						if(player1OverX < 144) {
							player1OverX++;
						}
					}
				}
				
				if(isPlayer2Over) {
					for(int i = 0 ; i < 2 ; i++) {
						if(player2OverX > 272) {
							player2OverX--;
						}
					}
				}
			
				//玩家移动声效
				boolean iMove = playerTanks[0] == null ? false : playerTanks[0].isMove;
				boolean iiMove = playerTanks[1] == null ? false : playerTanks[1].isMove;
				if((iMove || iiMove) && (!iDie || !iiDie) && !isBaseDestroy) {			
					if(notPlayerMoveSound && playerMoveSound == null) {
						notPlayerMoveSound = false;
						playerMoveSound = new PlayerSound(Art.tankmove , true);				
					} 
				} else {
					if(playerMoveSound != null) {
						playerMoveSound.stop();
						playerMoveSound = null;
						notPlayerMoveSound = true;
					}		
				}
				
				//钢墙
				if(steelWallTimer > 0) {
					if(steelWallTimer < 9) {
						if(wallChangeEscapement.isPass()) {
							if(wallChange) {							
								brickWall();
								wallChange = false;
							} else {		
								steelWall();
								wallChange = true;
							}
						}
					}
					
					if(wallEscapement.isPass()) {
						steelWallTimer--;
						if(steelWallTimer < 1) {
							brickWall();
						}
					}
				}
				
				if(timerEscapement.isPass()) {
					if(PlayerTank.timer > 0) {
						PlayerTank.timer--;
					}
					
					if(EnemyTank.timer > 0) {
						EnemyTank.timer--;
					}
				}	
				
				if(isDemo) {
					if(robots[0] == null) {
						robots[0] = new Robot(this, playerTanks[0]);
						robots[1] =new Robot(this, playerTanks[1]); 
					}
					
					for(int i = 0 ; i < robots.length ; i++) {
						Robot robot = robots[i];
						robot.action();
					}
					
					if(countdwonEscapement.isPass()) {
						if(demoCountdown > 1) {
							demoCountdown--;
						} else {
							stop(); //关闭所有声效
							component.toHeader();
						}
					}
				}
			}			
		}
		
		//停止敌方坦克移动声效
		if(existEnemyCount < 1 && activeEnemyCount < 1) {
			if(enemyMoveSound != null) {
				enemyMoveSound.stop();
			}
		}
		
		//停止所有声效
		if(keys[KEY_ESC] || keys[KEY_F1]) {
			stop();
		}
		
		super.update();
	}
	
	@Override
	public void render(Graphics g) {
		if(isCurtain) {
			g.setColor(new Color(132 , 132 , 132));
			g.fillRect(0, 0, 512, curtainHeight);
			g.fillRect(0, 448 - curtainHeight, 512, curtainHeight);
			if(curtainHeight == TankComponent.HEIGHT / 2) {
				char[] chars1 = "STAGE".toCharArray();
				char[] chars2 = Integer.toString(stage).toCharArray();
				int x = (TankComponent.WIDTH - (chars1.length + chars2.length + 1) * 16) / 2;
				int y = (TankComponent.HEIGHT - 16) / 2;
				for(int i = 0 ; i < chars1.length ; i++) {
					g.drawImage(Art.numletterB[0][chars1[i] - 55], x + i * 16 , y , 16 , 16 , null);
				}
				x = x + (chars1.length + 1) * 16;
				for(int i = 0 ; i < chars2.length ; i++) {
					g.drawImage(Art.numletterB[0][chars2[i] - 48], x + i * 16 , y, 16, 16, null);
				}
			}
		} else {
			g.setColor(new Color(132 , 132 , 132));
			g.fillRect(0 , 0, TankComponent.WIDTH, TankComponent.HEIGHT);
			g.setColor(Color.BLACK);
			g.fillRect(TankComponent.GAME_BOX_MARGIN_2 , TankComponent.GAME_BOX_MARGIN_1 , TankComponent.GAME_BOX_SIZE , TankComponent.GAME_BOX_SIZE);
			
			int count = 0;
			int h = existEnemyCount / 2 + existEnemyCount % 2;
			for(int y = 0 ; y < h ; y++) {
				for(int x = 0 ; x < 2 ; x++) {
					if(count < existEnemyCount) {
						g.drawImage(Art.iconB[0][0], x * 16 + 464, y * 16 + 48, 16 , 16 , null);
					}
					count++;
				}
			}
			
			g.drawImage(Art.iconB[0][2], 464, 256, 16, 16, null);
			g.drawImage(Art.iconB[0][3], 464 + 16, 256, 16, 16, null);
			g.drawImage(Art.iconB[0][1], 464, 272 , 16, 16, null);
			char[] chars = Integer.toString(playerTanks[0] == null ? 0 : playerTanks[0].life).toCharArray();
			for(int i = 0 ; i < chars.length ; i++) {
				g.drawImage(Art.numletterB[0][chars[i] - 48], 480 + i * 16 , 272 , 16 , 16 , null);
			}
			
			if(isPlayers) {
				g.drawImage(Art.iconB[0][4], 464 , 304 , 16, 16, null);
				g.drawImage(Art.iconB[0][5], 480 , 304 , 16, 16, null);
				g.drawImage(Art.iconB[0][1], 464 , 320 , 16, 16, null);
				chars = Integer.toString(playerTanks[1] == null ? 0 : playerTanks[1].life).toCharArray();
				for(int i = 0 ; i < chars.length ; i++) {
					g.drawImage(Art.numletterB[0][chars[i] - 48], 480 + i * 16 , 320 , 16 , 16 , null);
				}
			}
			
			g.drawImage(Art.flag, 464, 352 , 32, 32, null);
			chars = Integer.toString(stage).toCharArray();
			for(int i = 0 ; i < chars.length ; i++) {
//				g.drawImage(Art.numletterB[0][chars[i] - 48], 464 + i * 16, 389 , 16, 16, null);
				g.drawImage(Art.numletterB[0][chars[i] - 48], 448 + ((64 - chars.length * 16) / 2) + i * 16, 389 , 16, 16, null);
			}  
			
			if(!isCurtain && curtainHeight > 0) {
				g.setColor(new Color(132 , 132 , 132));
				g.fillRect(0, 0, 512, curtainHeight);
				g.fillRect(0, 448 - curtainHeight, 512, curtainHeight);
			} else {
				//画除树之外的装饰物
				for(int y = 0 ; y < map.length ; y++) {
					for(int x = 0 ; x < map.length ; x++) {
						int decorate = 0xff & map[y][x];	//& 0xff是为了消除byte转为int时的负数
						if(decorateFrame > 0) {
							decorate = Map.getCartoonDecorate(decorate);
						}
						//如果基地被摧毁，画被摧毁的基地
						if(isBaseDestroy) {
							decorate = Map.getBaseDestroy(decorate);
						}
						//非道路，非树
						if(decorate != 0 && !isTree(decorate)) {					
							g.drawImage(Art.tileb[decorate % 4][decorate / 4], x * 8 + 32, y * 8 + 16 , null);
						}
					}	
				}   
				
				for(Tank tank : tanks) {
					tank.render(g);
				}
				
				for(Bullet bullet : bullets) {
					bullet.render(g);
				}
				
				//画树，因为树会遮挡住坦克，而如果有船坦克又是在河流之上，所以树需单独画，并且在坦克之后画
				for(int y = 0 ; y < map.length ; y++) {
					for(int x = 0 ; x < map[y].length ; x++) {
						int decorate = 0xff & map[y][x];	//& 0xff是为了消除byte转为int时出现的负数
						if(decorateFrame > 0) {
							decorate = Map.getCartoonDecorate(decorate);
						}
						//非道路，是树
						if(decorate != 0 && isTree(decorate)) {					
							g.drawImage(Art.tileb[decorate % 4][decorate / 4], x * 8 + 32, y * 8 + 16 , null);
						}
					}
				}
				
				//画爆炸与得分
				for(Spirit spirit : spirits) {
					spirit.render(g);
				}
				
				//画宝物
				if(treasure != null) {
					treasure.render(g);;
				}
				
				//画玩家结束游戏
				if(isPlayer1Over) {
					g.drawImage(Art.gameOver , player1OverX , 400 , null);
				}
				
				if(isPlayer2Over) {
					g.drawImage(Art.gameOver , player2OverX , 400 , null);
				}
				
				//画游戏结束帖图
				if(isGameOver) {
					g.drawImage(Art.gameOver , (TankComponent.GAME_BOX_SIZE - 64) / 2 + 32, gameOverY, null);
				}
			}
		}
	}
	
	private void createTanks() {
		//生成敌方坦克
		if(existEnemyCount > 0 && activeEnemyCount < maxActiveEnemyCount) {
			if(enemyEscapement.isPass()) {
				int count = stage / Map.getSize() + 1;
				count = count > 3 ? 3 : count;  //最多三个出生点同时生成敌方坦克
				for(int i = 0 ; i < count ; i++) {
					if(existEnemyCount > 0 && activeEnemyCount < maxActiveEnemyCount) {
						int type = random.nextInt(4);
						boolean isFlickerTank = false;
						if(existEnemyCount <= flickerEnemyCount) {
							isFlickerTank = true;
						} else if(flickerEnemyCount > 0) {
							isFlickerTank = random.nextInt(4) == 0 ? true : false;
						}
						activeEnemyCount++;
						existEnemyCount--;
						if(isFlickerTank) {
							flickerEnemyCount--;
						}
						tanks.add(new EnemyTank(this, type, ENEMY_BORN_POINTS[enemyBornPointIndex], 16, Spirit.DIR_DOWN, isFlickerTank));
						if(enemyBornPointIndex > 1) {
							enemyBornPointIndex = 0;
						} else {
							enemyBornPointIndex++;
						}
					}
				}
			}
		}
		
		//生成已方坦克
		for(int i = 0 ; i < playerTanks.length ; i++) {
			if(i > 0 && !isPlayers) {
				break;
			}
			//生成已方坦克
			if(playerTanks[i] == null) {
				playerTanks[i] = new PlayerTank(this, PlayerTank.PLAYER_TANK_TYPE_1, PLAYER_BORN_POINTS[i] / 500 , PLAYER_BORN_POINTS[i] % 500 , Spirit.DIR_UP , 0, i);
				tanks.add(playerTanks[i]);		
			} else {	
				if(playerTanks[i].refresh) {
					playerTanks[i].init(this);
					tanks.add(playerTanks[i]);
				} else {
					if(playerTanks[i].die && playerTanks[i].life > 0) {
						playerTanks[i].life--;
						playerTanks[i].init();
						tanks.add(playerTanks[i]);
					}	
				}
			}	
		}
	}
	
	private boolean isTree(int decorate) {
		for(int i = 0 ; i < Map.TREE.length ; i++) {
			int tree = 0xff & Map.TREE[i];
			if(tree == decorate) {
				return true;
			}
		}
		return false;
	}
	
	private void start() {		
		Map.init();
		isCurtain = false;
		if(!isDemo) {
			map = Map.getMap((stage - 1) % Map.getSize() + 1);
		} else {
			map = new byte[52][52];
			for(int y = 0 ; y < map.length ; y++) {
				for(int x = 0 ; x < map[y].length ; x++) {
					map[y][x] = Demo.MAP[y][x];
				}
			}
		}  
		maxActiveEnemyCount = isDemo == true ? 10 : stage / 10 + 5;
		beginSound = new PlayerSound(Art.levelbegin);
		if(existEnemyCount > 0 || activeEnemyCount > 0) {
			enemyMoveSound = new PlayerSound(Art.enemymove , true);
		}	
	}
	
	/**
	 * 关闭所有声效播放
	 */
	private void stop() {
		if(beginSound != null) {
			beginSound.stop();
			beginSound = null;
		}
		
		if(playerMoveSound != null) {
			playerMoveSound.stop();
			playerMoveSound = null;
		}
		
		if(enemyMoveSound != null) {
			enemyMoveSound.stop();
			enemyMoveSound = null;
		}
	}
	
	public void brickWall() {
		wall(Map.getBrickWall());;
	}
	
	public void steelWall() {
		wall(Map.getSteelWall());
	}
	
	public void notWall() {
		wall(Map.getNotWall());
	}
	
	public void wall(byte[][] wall) {
		for(int y = 0 ; y < wall.length ; y++) {
			for(int x = 0 ; x < wall[y].length ; x++) {
				int decorate = wall[y][x];
				if(decorate < 80) {
					map[y + 46][x + 22] = wall[y][x];
				}
			}
		} 
	}
	
}
