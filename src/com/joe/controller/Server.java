package com.joe.controller;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import com.joe.file.PropertiesLoader;
import com.joe.model.Library;
import com.joe.service.HandlerService;


public class Server {
	
	private ServerSocket server;
	
	private Properties fileList;
	
	private int port = 5555;
	
	public Server() {
		
	}

	public Server(int port) {
		super();
		this.port = port;
	}

	public void runServer() {
		System.out.println("loading fileList.properties...");
		fileList = PropertiesLoader.getFileListProperties("fileList.properties");
		System.out.println("fileList.properties:" + fileList);
		
		System.out.println("loading library.obj...");
		Library library = Library.initLibraryFromFile(new File("library.obj"));
		System.out.println("library.obj:" + library);
		
		try {
			server = new ServerSocket(port);
			System.out.println("Http Server has started...");
			while (true) {
				System.out.println("Http Server linstening in " + port + "...");
				Socket socket = server.accept();
				
				new Thread(new HandlerService(socket, fileList, library)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		Server server = new Server(5555);
		server.runServer();
	}
}
