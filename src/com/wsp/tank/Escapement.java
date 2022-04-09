package com.wsp.tank;

/**
 * 对间隔时间精准控制
 * @author wsp
 */
public class Escapement {
	
	private double spacing;	//间隔时间(MS)
	private double lastTime;	//记录上一次执行时间
	
	public Escapement(double spacing) {
		this.spacing = spacing;
		lastTime = System.nanoTime() / 1000000;
	}
	
	public double getLastTime() {
		return lastTime;
	}
	
	public double getSpacing() {
		return spacing;
	}
	
	//第一次执行不间隔
	public void execute() {
		lastTime = 0;
	}
	
	public void setSpacing(double spacing) {
		this.spacing = spacing;
	}
	
	public boolean isPass() {
		double currentTime = System.nanoTime() / 1000000;
		if(currentTime - lastTime > spacing) {
			lastTime = currentTime;
			return true;
		}
		return false;
	}

}
