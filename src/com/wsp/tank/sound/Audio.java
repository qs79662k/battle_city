package com.wsp.tank.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Audio {
	
	private String fileName;
	private AudioInputStream in;
	private AudioFormat format;
	private DataLine.Info info;
	private SourceDataLine line;
	private boolean stop;
	
	public Audio(String fileName) {
		this.fileName = fileName;
	}
	
	public void play() {
		try {
			in = AudioSystem.getAudioInputStream(Audio.class.getResource(fileName));
			format = in.getFormat();
			info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(format);
			line.start();
			int len = 0;
			byte[] buffer = new byte[512];
			while(!stop) {
				if((len = in.read(buffer, 0 , buffer.length)) < 1) {
					break;
				}
				line.write(buffer, 0, len);
			}
			line.drain();
			line.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		if(line != null) {
			stop = true;
			line.stop();
		}
	}

}
