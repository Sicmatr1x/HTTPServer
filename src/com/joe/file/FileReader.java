package com.joe.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileReader {

	private BufferedInputStream fileBis;

	private String uri;

	public FileReader() {
		super();
	}

	public FileReader(String uri) {
		super();
		this.uri = uri;
	}

	public String getFileName() {
		if("/".equals(uri)) {
			return "index.html";
		}
		
		String[] work = uri.split("/");

		if (work == null && work.length < 0) {
			// 500
		}
		System.out.println("FileReader:getFileName()" + work[1]);
		return work[1];
	}

	public String getFileType() {
//		System.out.println("FileReader:getFileType()" + this.getFileName());

		String[] work = this.getFileName().split("[.]");
		switch (work[1]) {
		case "":
			return "text";
		case "jpg":
			return "image";
		case "jpeg":
			return "image";
		}

		return "text";
	}

	public File getFile() {
		File root = new File(System.getProperty("user.dir"));
		File file = new File(root.getAbsoluteFile() + "//webpage//" + this.getFileName());
		System.out.println("FileReader:getFile():" + file.getAbsolutePath());
		return file;
	}
	
	public File getNotFoundPageFile() {
		File root = new File(System.getProperty("user.dir"));
		File file = new File(root.getAbsoluteFile() + "//webpage//404page.html");
		System.out.println("FileReader:getFile():" + file.getAbsolutePath());
		return file;
	}

	public BufferedInputStream getFileBufferedInputStream() throws FileNotFoundException {
		File file = this.getFile();
		System.out.println("FileReader:getFileBufferedInputStream():" + file.getAbsolutePath());
		fileBis = new BufferedInputStream(new FileInputStream(file));
		return fileBis;
	}
}
