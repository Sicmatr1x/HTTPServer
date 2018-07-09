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
import com.joe.model.Library;

public class HandlerService implements Runnable {

	private Socket socket;
	private BufferedInputStream bis;
	private Properties fileList;
	private BookInterceptorService bookInterceptorService;
	private Library library;

	public HandlerService(Socket socket) {

		this.socket = socket;

	}

	public HandlerService(Socket socket, Properties fileList, Library library) {
		super();
		this.socket = socket;
		this.fileList = fileList;
		this.library = library;
		try {
			this.bookInterceptorService = new BookInterceptorService(socket, library);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String uri = null;
		try {
			System.out.println("====================================");
			Request request = new Request(socket.getInputStream());
			request.parse();
			uri = request.getUri();
			System.out.println("====================================");

			if ("GET".equals(request.getType())) {
//				OutputStream os = socket.getOutputStream();
//				os.write("HTTP/1.1 404 Not Found\n".getBytes());
//				os.write("Content-Type: text/html;charset=utf-8\n".getBytes());
//				os.write("\n".getBytes());
//				os.close();
//				return;
				
				// inceptor GET request
				if(this.bookInterceptorService.intecept(request)) {
					try {
						this.socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				
				System.out.println("server handle " + "GET" + " request");
				FileReader fileReader = new FileReader(uri, fileList);
				bis = fileReader.getFileBufferedInputStream();
				System.out.println("prepare to send response:" + socket + "," + fileReader.getFileType() + "," + bis);
				Response response = new Response(socket, fileReader.getFileType(), bis);
				response.sendResponse();
				bis.close();
			}else if("POST".equals(request.getType().toUpperCase())) {
				System.out.println("server handle " + "POST" + " request");
				BookService bookService = new BookService(this.socket, this.library, request);
				bookService.addBook();
			}else if("DELETE".equals(request.getType().toUpperCase())) {
				System.out.println("server handle " + "DELETE" + " request");
				BookService bookService = new BookService(this.socket, this.library, request);
				bookService.delBook();
			}else if("PUT".equals(request.getType().toUpperCase())) {
				System.out.println("server handle " + "PUT" + " request");
				BookService bookService = new BookService(this.socket, this.library, request);
				bookService.editBook();
			}else {
				System.out.println("Server not supported request type");
				// TODO
			}

		} catch (FileNotFoundException ex) {
			if (uri == null || "".equals(uri)) {
				try {
					throw new IOException("HandlerService:run():FileNotFoundException:uri=" + uri);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FileReader fileReader = new FileReader(uri);
			// æŸ¥æ‰¾æœ¬åœ°fileList.propertiesæ–‡ä»¶
			String hostIP = null;
			hostIP = fileList.getProperty(fileReader.getFileName());
			if (hostIP != null) {
				// å�‘é€�è·³è½¬æŠ¥æ–‡
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
			// è‹¥æ‰¾ä¸�åˆ°
			System.out.println("HandlerService:run():FileNotFoundException:" + "could not find "
					+ fileReader.getFileName() + " in fileListt.properties");
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

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

}
