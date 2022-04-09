package com.wsp.tank.sound;

public class PlayerSound implements Runnable {

	private Audio audio;
	private boolean loop;
	
	public PlayerSound(String fileName) {
		this(fileName , false);
	}
	
	public PlayerSound(String fileName , boolean loop) {
		this.loop = loop;
		audio = new Audio(fileName);
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		if(!loop) {
			audio.play();
		} else {
			while(loop) {
				audio.play();		
			}
		}
	}
	
	public void stop() {
		if(audio != null) {
			loop = false;
			audio.stop();		
		}
	}
	
	public void player(boolean loop) {
		this.loop = loop;
		new Thread(this).start();
	}
	
}
