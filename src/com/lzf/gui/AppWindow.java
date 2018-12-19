package com.lzf.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Window.Type;
import java.awt.GridLayout;

/**
 * SQL注入攻击的应用主界面入口
 */
public class AppWindow {

	private JFrame frame;

	/**
	 * 启动应用程序。
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					AppWindow window = new AppWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 创建应用程序。
	 */
	public AppWindow() {
		initialize();
	}

	/**
	 * 初始化窗口的内容。
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setType(Type.UTILITY);
		frame.setResizable(false);
		frame.getContentPane().setForeground(Color.BLACK);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setLayout(new GridLayout(1, 1, 10, 10));

		MainPanel topPanel = new MainPanel();
		frame.getContentPane().add(topPanel);
		frame.setTitle("五邑大学");
		frame.setForeground(Color.BLACK);
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 505, 376);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
