package com.joe.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Response {

	private Socket socket;
	private BufferedInputStream bis;
	private String type;

	public Response(Socket socket) {
		this.socket = socket;
	}
	
	public Response(Socket socket, String type, BufferedInputStream bis) {
		this.type = type;
		this.socket = socket;
		this.bis = bis;
	}

	public void sendResponse() throws IOException {
		if (bis == null)
			System.out.println("Response:BufferedInputStream bis=" + bis);

		System.out.println("sendResponse");

		OutputStream os = socket.getOutputStream();
		os.write("HTTP/1.1 200 OK\n".getBytes());
		if ("text".equals(type)) {
			os.write("Content-Type: text/html;charset=utf-8\n".getBytes());
		} else if ("image".equals(type)) {
			os.write("Content-Type: image/jpeg;charset=utf-8\n".getBytes());
		} else {
			// 500
			os.write("Content-Type: text/html;charset=utf-8\n".getBytes());
		}

		os.write("\n".getBytes());
		byte[] b = new byte[1024];
		int length = -1;
		while ((length = bis.read(b)) != -1) {
			os.write(b, 0, length);

		}
		bis.close();
		os.close();

	}
	
	public void sendNotFoundResponse() throws IOException {
		System.out.println("sendNotFoundResponse");

		OutputStream os = socket.getOutputStream();
		os.write("HTTP/1.1 404 Not Found\n".getBytes());
		if ("text".equals(type)) {
			os.write("Content-Type: text/html;charset=utf-8\n".getBytes());
		} 
		os.close();

	}
	
	public void sendServerErrorResponse() throws IOException {
		System.out.println("sendNotFoundResponse");

		OutputStream os = socket.getOutputStream();
		os.write("HTTP/1.1 500 Internal Server Error\n".getBytes());
		if ("text".equals(type)) {
			os.write("Content-Type: text/html;charset=utf-8\n".getBytes());
		} 
		os.close();

	}
	
	public void sendMoveTemporarilyResponse(String hostIP) throws IOException {
		System.out.println("sendMoveTemporarilyResponse");

		OutputStream os = socket.getOutputStream();
		os.write("HTTP/1.1 302 Move Temporarily\n".getBytes());
		if ("text".equals(type)) {
			os.write("Content-Type: text/html;charset=utf-8\n".getBytes());
			os.write(("Location:" + hostIP + "\n").getBytes());
		} 
		os.close();

	}

}
