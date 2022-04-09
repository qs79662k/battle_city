package com.wsp.tank;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class FrameLauncher extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public FrameLauncher() {
		Art.init(getGraphicsConfiguration());
		TankComponent component = new TankComponent(this);
		setContentPane(component);
		pack();
		setTitle("Battle City");
//		setIconImage(Art.flag);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(component);
		addMouseListener(component);
		addMouseMotionListener(component);
		addWindowFocusListener(component);
		setVisible(true);
		new Thread(component).start();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		new FrameLauncher();			
	}
	
}
