/**
 * 
 */
package com.lzf.serverDemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库工具类
 */
public class DBUtil {
	// MySQL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; // JDBC 驱动名
	// 数据库 URL
	private static final String DB_URL = "jdbc:mysql://localhost:3306/sql_inject"; // 数据库 URL
	static final String USER = "root"; // 数据库的用户名
	static final String PASS = "MJCoder"; // 数据库的密码

	/**
	 * 打开数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("连接数据库");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void closeDB(Connection conn, Statement ps, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("关闭数据库连接");
	}
}
