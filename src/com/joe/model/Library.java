package com.joe.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class Library implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<String, Book> bookrack;

	public Library() {
		super();
	}
	
	public Map<String, Book> createEmptyBookMap() {
		this.bookrack = new Hashtable<>();
		return this.bookrack;
	}
	
	public int addBook(Book book) {
		if(this.bookrack == null || book == null || book.getName() == null || this.bookrack.containsKey(book.getName())) {
			return -1;
		}
		this.bookrack.put(book.getName(), book);
		System.out.println("Library:addBook():add:" + book + " successful.");
		return 0;
	}
	
	public int delBook(String bookName) {
		if(this.bookrack == null || bookName == null || "".equals(bookName) || !this.bookrack.containsKey(bookName)) {
			return -1;
		}
		this.bookrack.remove(bookName);
		System.out.println("Library:delBook():remove:" + bookName + " successful.");
		return 0;
	}
	
	public int editBook(Book book) {
		System.out.println("Library:editBook():start edit...");
		if(this.delBook(book.getName()) == -1) {
			return -1;
		}
		if(this.addBook(book) == -1) {
			return -1;
		}
		System.out.println("Library:editBook():end edit:" + book + " successful.");
		return 0;
	}
	
	public static Library initLibraryFromFile(File libraryFile) {
		Library library = null;
		try {
			FileInputStream in = new FileInputStream(libraryFile);
			ObjectInputStream objinput = new ObjectInputStream(in);
			library = (Library)objinput.readObject();
			return library;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeLibraryToFile(File libraryFile, Library library) {
		
		try {
			FileOutputStream in = new FileOutputStream(libraryFile);
			ObjectOutputStream objinput = new ObjectOutputStream(in);
			objinput.writeObject(library);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
