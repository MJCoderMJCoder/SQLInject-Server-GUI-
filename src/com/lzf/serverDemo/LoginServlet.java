package com.lzf.serverDemo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet 实现类 LoginServlet
 */
@WebServlet(description = "登录", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection conn = null;
	private static Statement ps = null;
	private static ResultSet rs = null;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 *      处理GET请求
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			conn = DBUtil.getConnection();
			String phone = request.getParameter("phone");
			String password = request.getParameter("password");
			// 易受SQL注入攻击代码(以下四行)
			ps = conn.createStatement();
			String sql = "select * from user where phone = '" + phone + "' and password = '" + password + "'";
			System.out.println(sql);
			rs = ps.executeQuery(sql);

			// 防止SQL注入攻击代码(以下四行)
			// ps = conn.prepareStatement("select * from user where phone = ? and password =
			// ?");
			// ((PreparedStatement) ps).setString(1, phone);
			// ((PreparedStatement) ps).setString(2, password);
			// rs = ((PreparedStatement) ps).executeQuery();
			// System.out.println(ps);

			String respStr = "手机号或密码不正确";
			while (rs.next()) {
				respStr = "id：" + rs.getInt(1) + "；username：" + rs.getString(2) + "；phone：" + rs.getString(3)
						+ "；password：" + rs.getString(4);
			}
			response.getWriter().println(respStr);
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().println(e.getMessage());
		} finally {
			DBUtil.closeDB(conn, ps, rs);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 *      处理POST请求
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
