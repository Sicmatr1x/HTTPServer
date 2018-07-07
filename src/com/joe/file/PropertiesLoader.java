package com.joe.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.FileReader;

public class PropertiesLoader {
	public static Properties getFileListProperties(String path) {
		Properties fileList = new Properties();//获取配置文件的对象
		FileReader in;
		try {
			in = new FileReader("fileList.properties");//获取输入流
			fileList.load(in);//将流加载到配置文件对象中
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileList;
	}
}
