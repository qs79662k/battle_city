package com.wsp.tank;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Á÷¶¯µÄÐéÏß
 * @author wsp
 *
 */
public class FlowDottedLine {
	
	private int width;
	private int height;
	private int lineWidth;
	private int lineHeight;
	private int forward = 0;
	private Escapement e = new Escapement(80);
	
	public FlowDottedLine(int width , int height , int lineWidth , int lineHeight) {
		this.width = width;
		this.height = height;
		this.lineWidth = lineWidth;
		this.lineHeight = lineHeight;
	}
	
	public void drawFlowDottedLine(Graphics g , int x , int y) {
		int offsetX = 0;
		int offsetY = 0;
		int pixels = width * 2 + height * 2;
		if(forward < width) {
			offsetX = forward;
		} else if(forward >= width && forward < width + height)
		{
			offsetX = width;
			offsetY = forward - width;
		} else if(forward >= width + height && forward < width * 2 + height) {
			offsetY = height;
			offsetX = width - (forward - width - height);			
		} else if(forward >= width * 2 + height && forward < width * 2 + height * 2) {
			offsetX = 0;
			offsetY = height - (forward - width * 2 - height);
		}
		
		int count = 0;
		boolean change = false;
		boolean isUp = false;
		boolean isDown = false;
		boolean isLeft = false;
		@SuppressWarnings("unused")
		boolean isRight = false;
		for(int i = 0 ; i < pixels ; i++) {	
			if(i + forward < width) {
				offsetX++;
				isUp = true;
				isDown = false;
				isLeft = false;
				isRight = false;
			} else if(i + forward >= width && i + forward < width + height) {
				offsetY++;				
				isUp = false;
				isDown = false;
				isLeft = false;
				isRight = true;
			} else if(i + forward >= width + height && i + forward < width * 2 + height) {
				offsetX--;			
				isUp = false;
				isDown = true;
				isLeft = false;
				isRight = false;
			} else if(i + forward >= width * 2 + height && i + forward < width * 2 + height * 2) {
				offsetY--;				
				isUp = false;
				isDown = false;
				isLeft = true;
				isRight = false;
			} else if(i + forward >= width * 2 + height * 2 && i + forward < width * 2 + height * 2 + width) {
				offsetX++;				
				isUp = true;
				isDown = false;
				isLeft = false;
				isRight = false;
			} else if(i + forward >= width * 2 + height * 2 + width && i + forward < width * 2 + height * 2 + width + height) {
				offsetY++;			
				isUp = false;
				isDown = false;
				isLeft = false;
				isRight = true;
			} else if(i + forward >= width * 2 + height * 2 + width + height &&  i + forward < width * 2 + height * 2 + width * 2 + height) {
				offsetX--;
				isUp = false;
				isDown = true;
				isLeft = false;
				isRight = false;
			} else if(i + forward >= width * 2 + height * 2 + width * 2 + height && i + forward < width * 2 + height * 2 + width * 2 + height * 2) {
				offsetY--;				
				isUp = false;
				isDown = false;
				isLeft = true;
				isRight = false;
			}  
			
			if(!change) {
				count++;
				g.setColor(Color.WHITE);
				if(count == lineWidth) {
					change = true;
				}
			}
			
			if(change) {
				count--;
				g.setColor(Color.BLACK);			
				if(count == 0) {
					change = false;
				}
			}
			
			g.drawLine(x + offsetX , y + offsetY , x + offsetX, y + offsetY);
			
			if(isUp) {
				for(int j = 0 ; j < lineHeight ; j++) {
					g.drawLine(x + offsetX , y + offsetY + j, x + offsetX, y + offsetY + j);
				}
			} else if(isDown) {
				for(int j = 0 ; j < lineHeight ; j++) {
					g.drawLine(x + offsetX, y + offsetY - j, x + offsetX, y + offsetY - j);
				}
			} else if(isLeft) {
				for(int j = 0 ; j < lineHeight ; j++) {
					g.drawLine(x + offsetX + j, y + offsetY, x + offsetX + j, y + offsetY);
				}
			} else {
				for(int j = 0 ; j < lineHeight ; j++) {
					g.drawLine(x + offsetX - j, y + offsetY, x + offsetX - j, y + offsetY);
				}
			}			
		}           
		
		if(e.isPass()) {
			if(forward < pixels - 1) {
				forward++;
			} else {
				forward = 0;
			}
		}	
	}

}
