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

import com.joe.controller.Request;
import com.joe.controller.Response;
import com.joe.file.FileReader;

public class HandlerService implements Runnable{

	private Socket socket;
	private BufferedInputStream bis;

	public HandlerService(Socket socket) {

		this.socket = socket;
		
	}

	@Override
	public void run() {
		try {
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
//			String line;
//			String head = "";
//			for(int i = 0; i < 20; i++) {
//				line = bufferedReader.readLine();
//				System.out.println("for:"+line);
//			}
//			while((line = bufferedReader.readLine()) != null) {
//				System.out.println("HandlerService:"+line);
//				head+=line;
//				if("".equals(line)) {
//					break;
//				}
//			}
//			line = null;
//			line = bufferedReader.readLine();
//			if(line == null) {
//				
//			}else {
//				while((line = bufferedReader.readLine()) != null) {
//					System.out.println("HandlerService:"+line);
//					head.append(line);
//					if("".equals(line)) {
//						break;
//					}
//				}
//			}
			System.out.println("====================================");
			Request request = new Request(socket.getInputStream());
			request.parse();
			System.out.println("------------------------------------");
			String uri = request.getUri();
			System.out.println("uri:" + uri);
			System.out.println("====================================");
			
			FileReader fileReader = new FileReader(uri);
			bis = fileReader.getFileBufferedInputStream();
			System.out.println("prepare to send response:" + socket + ","+fileReader.getFileType()+","+bis);
			Response response = new Response(socket, fileReader.getFileType(), bis);
			response.sendResponse();
			bis.close();
			
		} catch(FileNotFoundException ex) {
			Response response = new Response(socket);
			try {
				response.sendNotFoundResponse();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("HandlerService:run():FileNotFoundException:" + ex.getMessage());
			
//			e.printStackTrace();
		} catch (IOException e) {
			Response response = new Response(socket);
			try {
				response.sendServerErrorResponse();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("HandlerService:run():IOException:" + e.getMessage());
//			e.printStackTrace();
		}finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
}
