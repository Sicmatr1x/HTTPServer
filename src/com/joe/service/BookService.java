package com.joe.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.joe.controller.Request;
import com.joe.model.Book;
import com.joe.model.Library;

public class BookService {
	private Socket socket;
	private OutputStream os;
	private Library library;
	private Request request;
	public static final String HEAD = "HTTP/1.1 200 OK\n" + "Content-Type: text/html;charset=utf-8\n" + "\n";
	public static final String HTML_HEAD = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<meta charset=\"utf-8\">\n" + "<title>Success</title></head>";
	
	public BookService(Socket socket, Library library, Request request) throws IOException {
		super();
		this.socket = socket;
		this.library = library;
		this.request = request;
		this.os = socket.getOutputStream();
	}
	
	/**
	 * get args from request body
	 * @return
	 */
	private Map<String, String> getArgsMap(){
		if(request.getBody() == null || "".equals(request.getBody())) {
			return null;
		}
		Map<String, String> argsMap = new HashMap<>();
		String[] work = request.getBody().split("&");
		for(int i = 0; i < work.length; i++) {
			String[] args = work[i].split("=");
			argsMap.put(args[0], args[1]);
			System.out.println("BookService:getArgsMap():" + args[0] + "=" + args[1]);
		}
		return argsMap;
	}
	
	public void addBook() throws IOException {
		Map<String, String> argsMap = this.getArgsMap();
		Book book = new Book();
		book.setName(argsMap.get("name").replaceAll("%20", " "));
		book.setAuther(argsMap.get("auther").replaceAll("%20", " "));
		if(argsMap.get("prise") != null) {
			book.setPrise(Double.valueOf(argsMap.get("prise")));
		}
		
		this.library.addBook(book);
		// head
		this.os.write(BookService.HEAD.getBytes());
		// body
		this.os.write(BookService.HTML_HEAD.getBytes());
		this.os.write(("<body>\n"+"<p>"+ "add:" + book + " successful" +"</p>"+"</body>").getBytes());
		this.os.write(("</html>\n").getBytes());
		this.os.close();
	}
	
	public void editBook() throws IOException {
		Map<String, String> argsMap = this.getArgsMap();
		Book book = new Book();
		if(argsMap.get("id") != null) {
			book.setId(Integer.valueOf(argsMap.get("id")));
		}
		book.setName(argsMap.get("name").replaceAll("%20", " "));
		book.setAuther(argsMap.get("auther").replaceAll("%20", " "));
		if(argsMap.get("prise") != null) {
			book.setPrise(Double.valueOf(argsMap.get("prise")));
		}
		
		if(this.library.editBook(book) == -1) {
			this.os.write(("<body>\n"+"<p>"+ "edit:" + book + " fail" +"</p>"+"</body>").getBytes());
			this.os.write(("</html>\n").getBytes());
			this.os.close();
			return;
		}
		// head
		this.os.write(BookService.HEAD.getBytes());
		// body
		this.os.write(BookService.HTML_HEAD.getBytes());
		this.os.write(("<body>\n"+"<p>"+ "edit:" + book + " successful" +"</p>"+"</body>").getBytes());
		this.os.write(("</html>\n").getBytes());
		this.os.close();
	}
	
	public void delBook() throws IOException {
		Map<String, String> argsMap = this.getArgsMap();
		Integer bookId = null;
		if(argsMap.get("id") != null) {
			bookId = Integer.valueOf(argsMap.get("id"));
		}
		
		if(this.library.delBook(bookId) == -1) {
			this.os.write(("<body>\n"+"<p>"+ "del:" + bookId + " fail" +"</p>"+"</body>").getBytes());
			this.os.write(("</html>\n").getBytes());
			this.os.close();
			return;
		}
		// head
		this.os.write(BookService.HEAD.getBytes());
		// body
		this.os.write(BookService.HTML_HEAD.getBytes());
		this.os.write(("<body>\n"+"<p>"+ "del:" + bookId + " successful" +"</p>"+"</body>").getBytes());
		this.os.write(("</html>\n").getBytes());
		this.os.close();
	}
}
