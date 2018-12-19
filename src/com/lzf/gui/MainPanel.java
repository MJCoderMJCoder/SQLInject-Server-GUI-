package com.lzf.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.lzf.inject.GetSQLInject;
import com.lzf.inject.PostSQLInject;

import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 界面详情页：在此处理SQL注入攻击的业务逻辑
 * 
 *
 */
public class MainPanel extends JPanel {
	private static final long serialVersionUID = 7245014852975643388L;
	private JTextField urlText;
	private JTextField paramText;
	private ButtonGroup buttonGroup;
	private final JPanel bottomPanel = new JPanel();
	private JPanel urlPanel;
	private JPanel paramPanel;
	private JLabel paramLabel;
	private static JTextPane textPane;
	private GetSQLInject getSQLInject;
	private PostSQLInject postSQLInject;
	private JButton clear;

	/**
	 * 创建面板。
	 */
	public MainPanel() {
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		urlPanel = new JPanel();
		urlPanel.setForeground(Color.BLACK);
		urlPanel.setBackground(Color.WHITE);
		add(urlPanel);
		urlPanel.setLayout(new BoxLayout(urlPanel, BoxLayout.X_AXIS));

		JLabel urlLabel = new JLabel("请输入要攻击的URL：");
		urlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		urlLabel.setFont(new Font("楷体", Font.PLAIN, 15));
		urlLabel.setBackground(Color.WHITE);
		urlPanel.add(urlLabel);

		urlText = new JTextField();
		urlText.setColumns(23);
		urlText.setFont(new Font("Arial", Font.PLAIN, 15));
		urlText.setText("http://localhost:8080/SQLInject/LoginServlet");
		urlText.setHorizontalAlignment(SwingConstants.LEFT);
		urlText.setBackground(Color.WHITE);
		urlPanel.add(urlText);

		paramPanel = new JPanel();
		paramPanel.setForeground(Color.BLACK);
		paramPanel.setBackground(Color.WHITE);
		add(paramPanel);
		paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.X_AXIS));

		paramLabel = new JLabel("请输入所需的参数：");
		paramLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		paramLabel.setFont(new Font("楷体", Font.PLAIN, 15));
		paramLabel.setBackground(Color.WHITE);
		paramPanel.add(paramLabel);

		paramText = new JTextField();
		paramText.setColumns(24);
		paramText.setFont(new Font("Arial", Font.PLAIN, 15));
		paramText.setText("phone-password");
		paramText.setHorizontalAlignment(SwingConstants.LEFT);
		paramText.setBackground(Color.WHITE);
		paramPanel.add(paramText);

		bottomPanel.setForeground(Color.BLACK);
		bottomPanel.setBackground(Color.WHITE);
		add(bottomPanel);
		bottomPanel.setLayout(new GridLayout(0, 5, 12, 10));

		buttonGroup = new ButtonGroup();
		JRadioButton get = new JRadioButton("GET请求");
		get.setForeground(Color.BLACK);
		get.setSelected(true);
		get.setHorizontalAlignment(SwingConstants.CENTER);
		get.setBackground(Color.WHITE);
		JRadioButton post = new JRadioButton("POST请求");
		post.setHorizontalAlignment(SwingConstants.CENTER);
		post.setForeground(Color.BLACK);
		post.setBackground(Color.WHITE);
		buttonGroup.add(get);
		buttonGroup.add(post);
		bottomPanel.add(get);
		bottomPanel.add(post);

		JButton startAttack = new JButton("开始攻击");
		startAttack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("开始攻击：" + e);
				if (get.isSelected()) { // 攻击GET请求
					if (getSQLInject != null) {
						getSQLInject.setSQLInject(false);
					}
					getSQLInject = new GetSQLInject();
					getSQLInject.setSQLInject(true);
					getSQLInject.setUrl(urlText.getText().trim());
					getSQLInject.setParams(paramText.getText().trim());
					getSQLInject.start();
				} else if (post.isSelected()) { // 攻击POST请求
					if (postSQLInject != null) {
						postSQLInject.setSQLInject(false);
					}
					postSQLInject = new PostSQLInject();
					postSQLInject.setSQLInject(true);
					postSQLInject.setUrl(urlText.getText().trim());
					postSQLInject.setParams(paramText.getText().trim());
					postSQLInject.start();
				}
			}
		});
		startAttack.setForeground(Color.BLACK);
		startAttack.setBackground(Color.WHITE);
		bottomPanel.add(startAttack);

		JButton stopAttack = new JButton("停止攻击");
		stopAttack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getSQLInject != null) {
					getSQLInject.setSQLInject(false);
				}
				if (postSQLInject != null) {
					postSQLInject.setSQLInject(false);
				}
			}
		});
		stopAttack.setForeground(Color.BLACK);
		stopAttack.setBackground(Color.WHITE);
		bottomPanel.add(stopAttack);

		clear = new JButton("清空");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPane.setText("");
			}
		});
		clear.setForeground(Color.BLACK);
		clear.setBackground(Color.WHITE);
		bottomPanel.add(clear);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192), 1, true),
				"\u7ED3\u679C\u5C55\u793A", TitledBorder.LEADING, TitledBorder.TOP, null, Color.LIGHT_GRAY));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setText("...");
		textPane.setForeground(Color.BLACK);
		textPane.setBackground(Color.WHITE);
		Dimension ds = new Dimension(445, 200);
		textPane.setPreferredSize(ds);
		scrollPane.setViewportView(textPane);
		add(scrollPane);
	}

	public static void setText(String str) {
		if (textPane.getText().length() % 40 == 0) {
			textPane.setText(textPane.getText() + "\n" + str);
		} else {
			textPane.setText(textPane.getText() + " " + str);
		}
	}
}
