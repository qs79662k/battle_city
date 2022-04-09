package com.wsp.tank;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Art {
	
	//声效
	public final static String up = "/resource/1UP.wav";
	public final static String bang2 = "/resource/Bang2.wav";
	public final static String bang = "/resource/Bang.wav";
	public final static String bonus1000 = "/resource/bonus1000.wav";
	public final static String countscore = "/resource/countscore.wav";
	public final static String eatbonus = "/resource/eatbonus.wav";
	public final static String enemymove = "/resource/enemymove.wav";
	public final static String gameover = "/resource/gameover.wav";
	public final static String gunfire = "/resource/gunfire.wav";
	public final static String hiscore = "/resource/hiscore.wav";
	public final static String hitiron = "/resource/hitiron.wav";
	public final static String hitbullet = "/resource/hitbullet.wav";
	public final static String hitirontank = "/resource/hitirontank.wav";
	public final static String levelbegin = "/resource/levelbegin.wav";
	public final static String pause = "/resource/pause.wav";
	public final static String powerbullet = "/resource/powerbullet.wav";
	public final static String redbang = "/resource/redbang.wav";
	public final static String tankmove = "/resource/tankmove.wav";
	public final static String bomb = "/resource/bomb.wav";
	//帖图
	public static Image[][] bomb1;
	public static Image[][] bomb2;
	public static Image[][] bonusa;
	public static Image[][] bonusb;
	public static Image[][] bullet;
	public static Image[][] enemya;
	public static Image[][] iconB;
	public static Image[][] iconH;
	public static Image[][] iconW;
	public static Image[][] font;
	public static Image[][] numletterB;
	public static Image[][] numletterC;
	public static Image[][] numletterH;
	public static Image[][] numletterW;
	public static Image[][] numletterJ;
	public static Image[][] numletterL;
	public static Image[][] numletterN;
	public static Image[][] player1a;
	public static Image[][] player2a;
	public static Image[][] score;
	public static Image[][] shell;
	public static Image[][] shellb;
	public static Image[][] star;
	public static Image[][] tile;
	public static Image[][] tileb;
	public static Image title;
	public static Image flag;
	public static Image gameOver;
	public static Image scoreboard;
	
	/**
	 * 初始化帖图数据
	 */
	public static void init(GraphicsConfiguration gc) {
		bomb1 = cutImage(gc , getColorReplaceImage("/resource/bomb1.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		bomb2 = cutImage(gc , getColorReplaceImage("/resource/bomb2.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 64, 64);
		bonusa = cutImage(gc , getColorReplaceImage("/resource/bonusa.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		bonusb = cutImage(gc , getColorReplaceImage("/resource/bonusb.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		bullet = cutImage(gc , getColorReplaceImage("/resource/bullet.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 8, 8);
		enemya = cutImage(gc , getColorReplaceImage("/resource/enemya.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		player1a = cutImage(gc , getColorReplaceImage("/resource/player1a.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		player2a = cutImage(gc , getColorReplaceImage("/resource/player2a.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		score = cutImage(gc , getColorReplaceImage("/resource/score.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		shell = cutImage(gc , getColorReplaceImage("/resource/shell.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		shellb = cutImage(gc , getColorReplaceImage("/resource/shellb.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		star = cutImage(gc , getColorReplaceImage("/resource/star.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 32, 32);
		tile = cutImage(gc , getColorReplaceImage("/resource/tile.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 8, 8);
		tileb = cutImage(gc , getColorReplaceImage("/resource/tileb.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 8, 8);
		flag = getImage(gc , getColorReplaceImage("/resource/flag.png", Color.WHITE, new Color(0 , 0 , 0 , 0)));
		iconB = cutImage(gc , getColorReplaceImage("/resource/iconB.png" , Color.WHITE, new Color(0 , 0, 0 , 0)), 8, 8);
		iconH = cutImage(gc , getColorReplaceImage(getColorReplaceImage("/resource/iconB.png" , Color.WHITE, new Color(0 , 0, 0 , 0)), Color.BLACK , new Color(219 , 43 , 0)), 8, 8);
		iconW = cutImage(gc , getColorReplaceImage("/resource/iconW.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 8, 8);
		font = cutImage(gc, "/resource/font.gif", 8, 8);
		numletterB = cutImage(gc , getColorReplaceImage("/resource/numletterB.png" , Color.WHITE, new Color(0 , 0, 0 , 0)), 8, 8);
		numletterC = cutImage(gc , getColorReplaceImage(getColorReplaceImage("/resource/numletterB.png" , Color.WHITE, new Color(0 , 0, 0 , 0)), Color.BLACK , new Color(255 , 155 , 59)), 8, 8);
		numletterH = cutImage(gc , getColorReplaceImage(getColorReplaceImage("/resource/numletterW.png" , Color.WHITE, new Color(219 , 43 , 0)), Color.BLACK , new Color(0 , 0 , 0 , 0)), 8, 8);
		numletterW = cutImage(gc , getColorReplaceImage("/resource/numletterW.png" , Color.BLACK, new Color(0 , 0, 0 , 0)), 8, 8);
		numletterJ = cutImage(gc , getColorReplaceImage(getColorReplaceImage("/resource/numletterB.png" , Color.WHITE, new Color(0 , 0, 0 , 0)), Color.BLACK , new Color(255 , 255 , 0)), 8, 8);
		numletterL = cutImage(gc , getColorReplaceImage(getColorReplaceImage("/resource/numletterB.png" , Color.WHITE, new Color(0 , 0, 0 , 0)), Color.BLACK , new Color(0 , 0 , 255)), 8, 8);
		numletterN = cutImage(gc , getColorReplaceImage(getColorReplaceImage("/resource/numletterB.png" , Color.WHITE, new Color(0 , 0, 0 , 0)), Color.BLACK , new Color(0 , 255 , 0)), 8, 8);
		title = getImage(gc , getImage("/resource/title.png"));
		gameOver = getImage(gc , getColorReplaceImage("/resource/gameover.png", Color.BLACK, new Color(0, 0, 0, 0)));
		scoreboard = getImage(gc , getImage("/resource/scoreboard.png"));		
	}
	
	public static BufferedImage getImage(String file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Art.class.getResourceAsStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return image;
	}
	
	public static Image getImage(GraphicsConfiguration gc , BufferedImage source) {
		Image image = gc.createCompatibleImage(source.getWidth(), source.getHeight(), Transparency.BITMASK);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return image;
	}
	
	/**
	 * 切割图片为指定大小的N个图块
	 * @param file
	 * @param width 图块宽
	 * @param height 图块高
	 * @return
	 */
	public static Image[][] cutImage(GraphicsConfiguration gc , String file , int width , int height) {
		BufferedImage source = getImage(file);
		return cutImage(gc , source , width , height);
	}
	
	public static Image[][] cutImage(GraphicsConfiguration gc , BufferedImage source , int width , int height) {
		Image[][] images = new Image[source.getHeight() / height][source.getWidth() / width];
		for(int y = 0 ; y < images.length ; y++) {
			for(int x = 0 ; x < images[y].length ; x++) {
				Image image = gc.createCompatibleImage(width , height, Transparency.BITMASK);
				Graphics2D g = (Graphics2D)image.getGraphics();
				g.drawImage(source, -x * width, -y * height, null);
				images[y][x] = image;
				g.dispose();
			}
		}
		return images;
	}
	
	/**
	 * 替换图片中指定的颜色
	 * @param file
	 * @param oldColo 原始颜色
	 * @param newColor 新颜色	
	 * @return
	 */
	public static BufferedImage getColorReplaceImage(String file , Color oldColor , Color newColor) {
		BufferedImage image = getImage(file);
		return getColorReplaceImage(image , oldColor , newColor);
	}
	
	public static BufferedImage getColorReplaceImage(BufferedImage source , Color oldColor , Color newColor) {
		BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)image.getGraphics();
		for(int y = 0 ; y < source.getHeight() ; y++) {
			for(int x = 0 ; x < source.getWidth() ; x++) {
				int rgb = source.getRGB(x, y);
				if(rgb == oldColor.getRGB()) {	
					g.setColor(newColor);
					g.drawLine(x, y, x, y);
				} else {
					image.setRGB(x, y, rgb);
				}
			}
		}
		g.dispose();
		return image;
	}
	
	public static int getR(int rgb) {
		return rgb & 0xff0000 >> 16;
	}
	
	public static int getG(int rgb) {
		return rgb & 0xff00 >> 8;
	}
	
	public static int getB(int rgb) {
		return rgb & 0xff;
	}
	
}
