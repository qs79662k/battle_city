package com.wsp.tank;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.VolatileImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.wsp.tank.sound.PlayerSound;
import com.wsp.tank.spirit.PlayerTank;

public class TankComponent extends JComponent implements Runnable , KeyListener , MouseListener , MouseMotionListener , WindowFocusListener {

	private static final long serialVersionUID = 1L;
	public final static int WIDTH = 512;
	public final static int HEIGHT = 448;
	public final static int GAME_BOX_SIZE = 416;
	public final static int GAME_BOX_MARGIN_1 = 16;
	public final static int GAME_BOX_MARGIN_2 = 32;
	public final static String LOSE_FOCUS_INFO = "LOSE FOCUS!";
	public static boolean isTank90;	//当前true时支持敌方捡宝
	private Scene scene;
	private JFrame frame;
	protected int oldWidth;
	protected int oldHeight;
	protected int screenWidth;
	protected int screenHeight;
	private boolean isLoseFocus;
	private boolean showFocus; //控制失去焦点时提示信息闪动
	private Escapement e = new Escapement(300);
	
	public TankComponent(JFrame frame) {
		this.frame = frame;
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH , HEIGHT));
	}

	@Override
	public void run() {
		oldWidth = getWidth();
		oldHeight = getHeight();
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		VolatileImage image = createVolatileImage(WIDTH, HEIGHT);
		Graphics g = image.getGraphics();
		
/*		int
		fps = 0,
		count = 0;	*///帧计数
		
		Escapement globalEscapement = new Escapement(10);
//		Escapement fpsEscapement = new Escapement(1000);
		
		toHeader();	
	
		while(true) {
			//更新、渲染场景
			if(globalEscapement.isPass()) {
				if(scene != null) {
					if(!isLoseFocus) {
						scene.update();
					}					
					scene.render(g);
				}
			}
			//fps
	/*		count++;
			if(fpsEscapement.isPass()) {
				fps = count;
				count = 0;
			} */
			//失去焦点
			if(isLoseFocus) {
				if(e.isPass()) {
					showFocus = !showFocus;
				}
				//画失去焦点提示信息
				if(showFocus) {
					char[] chars = LOSE_FOCUS_INFO.toCharArray();
					int x = (WIDTH - chars.length * 16) / 2;
					int y = (HEIGHT - 16) / 2;
					for(int i = 0 ; i < chars.length ; i++) {
						g.drawImage(Art.font[0][chars[i] - 32], x + i * 16 + 1, y + 1, 16 , 16 , null);
						g.drawImage(Art.font[7][chars[i] - 32], x + i * 16, y, 16 , 16 , null);			
					}
				}
			}
			//渲染
			g.setFont(new Font("黑体" , Font.BOLD , 14));
			g.setColor(Color.YELLOW);
//			g.drawString("FPS:" + Integer.toString(fps) , 2 , 12);
			//
			Graphics _g = getGraphics();		
			if(_g != null) {
				int height = getHeight();
				int width = getWidth();					
				int x = 0;
				if(height == screenHeight) {
					width = height / oldHeight * oldWidth;
					x = (screenWidth - width) / 2;
					//画全屏后左右两边的黑边
					_g.setColor(Color.BLACK);
					_g.fillRect(0, 0, x, height);
					_g.fillRect(x + width, 0, width - x, height);
				}
				_g.drawImage(image , x , 0 , width , height ,  null);
			}
			//循环休眠
			try {
				Thread.sleep(2);	//休眠时间最好是间隔时间/2以上
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void toHeader() {
		toHeader(0 , 0);
	}
	
	protected void toHeader(int iScore , int iiScore) {
		Map.init();
		scene = new HeaderScene(this , iScore , iiScore);
	}
	
	protected void toBattle() {
		scene = new BattleScene(this);
	}
	
	protected void toBattle(int stage , boolean isPlayers) {
		scene = new BattleScene(this, stage, isPlayers);
	}
	
	protected void toBattle(int stage , boolean isPlayers , PlayerTank[] playerTanks) {
		scene = new BattleScene(this, stage, isPlayers , playerTanks);
	}
	
	protected void toResultsBattle(int stage , boolean isPlayers , boolean isOver , PlayerTank[] playerTanks) {
		scene = new ResultsBattleScene(this, stage, isPlayers, isOver, playerTanks);
	}
	
	protected void toOver(PlayerTank[] playerTanks) {
		scene = new OverScene(this, playerTanks);
	}
	
	protected void toMapEditor() {
		scene = new MapEditorScene(this);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(scene != null) {
			scene.mouseMoved(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@SuppressWarnings("static-access")
	@Override
	public void mousePressed(MouseEvent e) {
		if(scene != null && e.getButton() == e.BUTTON1) {
			scene.mousePressed(true);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void mouseReleased(MouseEvent e) {
		if(scene != null && e.getButton() == e.BUTTON1) {
			scene.mousePressed(false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(scene != null) {
			switch(e.getKeyCode()) {
				case 10:
					scene.toggleKey(Scene.KEY_ENTER , true);
					break;
				case 74:
					scene.toggleKey(Scene.I_KEY_SHOOT, true);
					break;
				case 87:
					scene.toggleKey(Scene.I_KEY_UP, true);
					break;
				case 65:
					scene.toggleKey(Scene.I_KEY_LEFT, true);
					break;
				case 68:
					scene.toggleKey(Scene.I_KEY_RIGHT, true);
					break;
				case 83:
					scene.toggleKey(Scene.I_KEY_DOWN, true);
					break;	
				case 110:
					scene.toggleKey(Scene.II_KEY_SHOOT, true);
					break;
				case 38:
					scene.toggleKey(Scene.II_KEY_UP, true);
					break;
				case 37:
					scene.toggleKey(Scene.II_KEY_LEFT, true);
					break;
				case 39:
					scene.toggleKey(Scene.II_KEY_RIGHT, true);
					break;
				case 40:
					scene.toggleKey(Scene.II_KEY_DOWN, true);
					break;	
				case 27:
					scene.toggleKey(Scene.KEY_ESC , true);
					break;
				case 112:
					scene.toggleKey(Scene.KEY_F1 , true);
					break;
				case 113:
					scene.toggleKey(Scene.KEY_F2 , true);
					break;
				case 114:
					scene.toggleKey(Scene.KEY_F3 , true);
					break;
				case 115:
					scene.toggleKey(Scene.KEY_F4 , true);
					break;	
				case 116:
					scene.toggleKey(Scene.KEY_F5 , true);	
					break;	
				case 119:
					scene.toggleKey(Scene.KEY_F8 , true);	
					break;
				case 123:
					windowModel();
					break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(scene != null) {
			switch(e.getKeyCode()) {
			case 10:
				scene.toggleKey(Scene.KEY_ENTER , false);
				break;
			case 74:
				scene.toggleKey(Scene.I_KEY_SHOOT, false);
				break;
			case 87:
				scene.toggleKey(Scene.I_KEY_UP, false);
				break;
			case 65:
				scene.toggleKey(Scene.I_KEY_LEFT, false);
				break;
			case 68:
				scene.toggleKey(Scene.I_KEY_RIGHT, false);
				break;
			case 83:
				scene.toggleKey(Scene.I_KEY_DOWN, false);
				break;	
			case 110:
				scene.toggleKey(Scene.II_KEY_SHOOT, false);
				break;
			case 38:
				scene.toggleKey(Scene.II_KEY_UP, false);
				break;
			case 37:
				scene.toggleKey(Scene.II_KEY_LEFT, false);
				break;
			case 39:
				scene.toggleKey(Scene.II_KEY_RIGHT, false);
				break;
			case 40:
				scene.toggleKey(Scene.II_KEY_DOWN, false);
				break;					
			case 27:
				scene.toggleKey(Scene.KEY_ESC , false);
				break;
			case 112:
				scene.toggleKey(Scene.KEY_F1 , false);
				break;
			case 113:
				scene.toggleKey(Scene.KEY_F2 , false);
				break;
			case 114:
				scene.toggleKey(Scene.KEY_F3 , false);
				break;
			case 115:
				scene.toggleKey(Scene.KEY_F4 , false);
				break;	
			case 116:
				scene.toggleKey(Scene.KEY_F5 , false);	
				break;	
			case 119:
				scene.toggleKey(Scene.KEY_F8 , false);	
				break;		
			}
		}
	}
	
	@Override
	public void windowGainedFocus(WindowEvent e) {
		isLoseFocus = false;
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		isLoseFocus = true;
		new PlayerSound(Art.pause);
	}	
	
	protected boolean isWindowModel = true;
	
	protected void windowModel() {
		if((isWindowModel = !isWindowModel)) {
			//窗口模式
			frame.dispose();
			frame.setUndecorated(false);
			frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
			frame.setVisible(true);
		} else {
			//全屏模式
			frame.dispose();
			frame.setUndecorated(true);
			frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(frame);
			setPreferredSize(new Dimension(screenWidth, screenHeight));
		}
	}

}
