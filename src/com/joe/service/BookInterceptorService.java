package com.joe.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.joe.controller.Request;
import com.joe.model.Book;
import com.joe.model.Library;

/**
 * 拦截GET请求 /book/getBookById?id= /book/getBookByName?name= /book
 * 
 * @author Joe
 *
 */
public class BookInterceptorService {

	private Socket socket;
	private OutputStream os;
	private Library library;

	public BookInterceptorService(Socket socket, Library library) throws IOException {
		super();
		this.socket = socket;
		this.os = socket.getOutputStream();
		this.library = library;

	}

	public boolean intecept(Request request) throws IOException {
		if (request.getUri() == null) {

		}

		String uri = request.getUri();
		String[] work = uri.split("/");
		if (work.length < 1) {
			System.out.println("BookInterceptorService:intecept():work.length=" + work.length);
		} else if (work.length >= 1) {
			System.out.println("BookInterceptorService:intecept():work[1]=" + work[1]);

			// head
			os.write("HTTP/1.1 200 OK\n".getBytes());
			this.os.write("Content-Type: text/html;charset=utf-8\n".getBytes());
			os.write("\n".getBytes());
			// body

			String html = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<meta charset=\"utf-8\">\n";

			if ("book".equals(work[1])) {
				if (work.length == 2) {
					Map<Integer, Book> br = this.library.getBookrack();
					Iterator<Map.Entry<Integer, Book>> it = br.entrySet().iterator();
					this.os.write(html.getBytes());
					this.os.write("<title>Book List</title></head>\\n".getBytes());
					this.os.write("<body>".getBytes());
					this.os.write("<table border=\"1\">\n".getBytes());
					while (it.hasNext()) {
						Map.Entry<Integer, Book> entry = it.next();
						Book book = entry.getValue();
						this.os.write(("<tr>\n<td>\n" + book.getId() + "</td>\n<td>" + book.getName() + "</td>\n<td>"
								+ book.getAuther() + "</td>\n<td>" + book.getPrise() + "</td>\n</tr>\n").getBytes());
						System.out.println("BookInterceptorService:intecept():"+entry.getKey() + "," + entry.getValue());
					}
					this.os.write("</table>\n".getBytes());
					this.os.write("</body>\n</html>".getBytes());
					
					this.os.close();
					return true;
				}
				String[] getArg = work[2].split("[?]");
//				System.out.println("BookInterceptorService:intecept():getArg[0]=" + getArg[0]);
//				System.out.println("BookInterceptorService:intecept():getArg[1]=" + getArg[1]);
				switch (getArg[0]) {
				case "getBookById":
					String[] args = getArg[1].split("=");
					Book book = this.library.getBookrack().get(Integer.valueOf(args[1]));
					this.os.write(html.getBytes());
					this.os.write("<title>Book List</title></head>\n".getBytes());
					this.os.write("<body>".getBytes());
					this.os.write("<table border=\"1\">\n".getBytes());
					this.os.write(("<tr>\n<td>\n" + book.getId() + "</td>\n<td>" + book.getName() + "</td>\n<td>"
							+ book.getAuther() + "</td>\n<td>" + book.getPrise() + "</td>\n</tr>\n").getBytes());
					this.os.write("</table>\n".getBytes());
					this.os.write("</body>\n</html>".getBytes());
					System.out.println("BookInterceptorService:intecept():getBookById"+book);
					
					this.os.close();
					return true;
					
				case "getBookByName":
					// TODO
					break;
				default:
					break;
				}
			}
		}
		return false;
	}

}
