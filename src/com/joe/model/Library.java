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
import java.util.Iterator;
import java.util.Map;

public class Library implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Hashtable<Integer, Book> bookrack;

	public Library() {
		super();
	}
	
	public void createEmptyBookMap() {
		this.bookrack = new Hashtable<>();
	}
	
	public int addBook(Book book) {
		int max = 0;
		if(!this.bookrack.isEmpty()) {
			Iterator<Map.Entry<Integer, Book>> it = this.bookrack.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Book> entry = it.next();
				if(max < entry.getKey())
					max = entry.getKey();
			}
		}
		book.setId(max + 1);
		this.bookrack.put(max + 1, book);
		System.out.println("Library:addBook():add:" + book + " successful.");
		return 0;
	}
	
	public int delBook(Integer bookId) {
		if(this.bookrack == null || bookId == null || !this.bookrack.containsKey(bookId)) {
			return -1;
		}
		this.bookrack.remove(bookId);
		System.out.println("Library:delBook():remove:" + bookId + " successful.");
		return 0;
	}
	
	public int editBook(Book book) {
		if(this.bookrack == null || book.getId() == null || !this.bookrack.containsKey(book.getId())) {
			return -1;
		}
		this.bookrack.replace(book.getId(), book);
		System.out.println("Library:editBook():edit:" + book + " successful.");
		return 0;
	}
	
	public Hashtable<Integer, Book> getBookrack() {
		return bookrack;
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
	
	
	@Override
	public String toString() {
		return "Library [bookrack=" + bookrack + "]";
	}

	public static void main(String[] args) {
		Library library = new Library();
		library.createEmptyBookMap();
		library.addBook(new Book("Three Body", "LCX", 90.0));
		library.addBook(new Book("Matrix", "WZSJ", 100.5));
		library.addBook(new Book("Think in Java", "aaa", 18.5));
		library.editBook(new Book("Think in Java", "bbb", 78.5));
		library.delBook(2);
		Library.writeLibraryToFile(new File("library.obj"), library);
		Library lib1 = Library.initLibraryFromFile(new File("library.obj"));
		Map<Integer, Book> br = lib1.getBookrack();
		Iterator<Map.Entry<Integer, Book>> it = br.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Book> entry = it.next();
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
	}
}
