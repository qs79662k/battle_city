package com.wsp.tank;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public abstract class Scene {
	
	public static final int I_KEY_UP = 0;
	public static final int I_KEY_DOWN = 1;
	public static final int I_KEY_LEFT = 2;
	public static final int I_KEY_RIGHT = 3;
	public static final int I_KEY_SHOOT = 4;
	public static final int II_KEY_UP = 5;
	public static final int II_KEY_DOWN = 6;
	public static final int II_KEY_LEFT = 7;
	public static final int II_KEY_RIGHT = 8;
	public static final int II_KEY_SHOOT = 9;
	public static final int KEY_ENTER = 10;
	public static final int KEY_ESC = 11;
	public static final int KEY_F1 = 12;
	public static final int KEY_F2 = 13;
	public static final int KEY_F3 = 14;
	public static final int KEY_F4 = 15;
	public static final int KEY_F5 = 16;
	public static final int KEY_F8 = 17;
	public boolean[] keys = new boolean[18];
	protected int pointX;
	protected int pointY;
	protected boolean isMousePressed;
	protected TankComponent component;
	
	public Scene(TankComponent component) {
		this.component = component;
	}
	
	/**
	 * ��ز�������������(true)���ͷ�(false)	 
	 * @param key kyes�±�
	 * @param isPressed ����(true)���ͷ�(false)
	 */
	public void toggleKey(int key , boolean isPressed) {
		keys[key] = isPressed;
	}
	
	/**
	 * ����ƶ�ʱ������
	 * @param e
	 */
	protected void mouseMoved(MouseEvent e) {
		if(component.isWindowModel) {
			pointX = e.getX();
			pointY = e.getY();			
		} else {
			double m = component.screenHeight / component.oldHeight;
			pointX = (int)((e.getX() - (component.screenWidth - component.oldWidth * m) / 2) / m) + 4;
			pointY = (int)(e.getY() / m) + 32;			
		}
	}
	
	/**
	 * ����������ʱisMousePressedΪtrue���ͷ�Ϊfalse
	 * @param isMousePressed
	 */
	public void mousePressed(boolean isMousePressed) {
		this.isMousePressed = isMousePressed;
	}
	
	/**
	 * ���³����е�����
	 */
	public void update() {
		if(keys[KEY_F1]) {
			TankComponent.isTank90 = !TankComponent.isTank90;
			component.toHeader();
		} else if(keys[KEY_ESC]) {
			component.toHeader();
		}
	}
	
	/**
	 * ��Ⱦ����
	 * @param g
	 */
	public abstract void render(Graphics g);

}
