package com.wsp.tank.spirit;

import java.awt.Graphics;

public interface Spirit {
	
	public final static int DIR_UP = 0;
	public final static int DIR_DOWN = 2;
	public final static int DIR_LEFT = 3;
	public final static int DIR_RIGHT = 1;  

	public void update();
	
	public void render(Graphics g);
	
}
