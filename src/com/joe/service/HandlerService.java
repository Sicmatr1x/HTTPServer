package com.joe.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

import com.joe.controller.Request;
import com.joe.controller.Response;
import com.joe.file.FileReader;

public class HandlerService implements Runnable {

	private Socket socket;
	private BufferedInputStream bis;
	private Properties fileList;

	public HandlerService(Socket socket) {

		this.socket = socket;

	}

	public HandlerService(Socket socket, Properties fileList) {
		super();
		this.socket = socket;
		this.fileList = fileList;
	}

	@Override
	public void run() {
		String uri = null;
		try {
			// BufferedReader bufferedReader = new BufferedReader(new
			// InputStreamReader(socket.getInputStream(), "utf-8"));
			// String line;
			// String head = "";
			// for(int i = 0; i < 20; i++) {
			// line = bufferedReader.readLine();
			// System.out.println("for:"+line);
			// }
			// while((line = bufferedReader.readLine()) != null) {
			// System.out.println("HandlerService:"+line);
			// head+=line;
			// if("".equals(line)) {
			// break;
			// }
			// }
			// line = null;
			// line = bufferedReader.readLine();
			// if(line == null) {
			//
			// }else {
			// while((line = bufferedReader.readLine()) != null) {
			// System.out.println("HandlerService:"+line);
			// head.append(line);
			// if("".equals(line)) {
			// break;
			// }
			// }
			// }
			System.out.println("====================================");
			Request request = new Request(socket.getInputStream());
			request.parse();
			System.out.println("------------------------------------");
			uri = request.getUri();
			System.out.println("uri:" + uri);
			System.out.println("====================================");

			FileReader fileReader = new FileReader(uri, fileList);
			bis = fileReader.getFileBufferedInputStream();
			System.out.println("prepare to send response:" + socket + "," + fileReader.getFileType() + "," + bis);
			Response response = new Response(socket, fileReader.getFileType(), bis);
			response.sendResponse();
			bis.close();

		} catch (FileNotFoundException ex) {
			if(uri == null || "".equals(uri)) {
				try {
					throw new IOException("HandlerService:run():FileNotFoundException:uri=" + uri);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FileReader fileReader = new FileReader(uri);
			// 查找本地fileList.properties文件
			String hostIP = null;
			hostIP = fileList.getProperty(fileReader.getFileName());
			if (hostIP != null) {
				// 发送跳转报文
				Response response = new Response(socket);
				try {
					response.sendMoveTemporarilyResponse(hostIP);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("HandlerService:run():" + ex.getMessage() + ",find in:" + hostIP);
				return;
			} 
			// 若找不到
			System.out.println("HandlerService:run():FileNotFoundException:" + "could not find " + fileReader.getFileName() + " in fileListt.properties");
			Response response = new Response(socket);
			try {
				response.sendNotFoundResponse();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("HandlerService:run():FileNotFoundException:" + ex.getMessage());

			// e.printStackTrace();
		} catch (IOException e) {
			Response response = new Response(socket);
			try {
				response.sendServerErrorResponse();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("HandlerService:run():IOException:" + e.getMessage());
			// e.printStackTrace();
		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
