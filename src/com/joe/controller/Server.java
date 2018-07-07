package com.joe.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.joe.service.HandlerService;


public class Server {
	
	private ServerSocket server;
	
	private int port = 5555;
	
	public Server() {
		
	}

	public Server(int port) {
		super();
		this.port = port;
	}

	public void runServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("Http Server has started...");
			while (true) {
				System.out.println("Http Server linstening in " + port + "...");
				Socket socket = server.accept();
				
				new Thread(new HandlerService(socket)).start();
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
