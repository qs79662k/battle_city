package com.wsp.tank;

/**
 * �Լ��ʱ�侫׼����
 * @author wsp
 */
public class Escapement {
	
	private double spacing;	//���ʱ��(MS)
	private double lastTime;	//��¼��һ��ִ��ʱ��
	
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
	
	//��һ��ִ�в����
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
