package com.joe.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Request {

    private InputStream input;
    private String request;
    private String uri;
    private String type;
    private String body;

    public Request(InputStream input) {
        this.input = input;
    }

    //从InputStream中读取request信息，并从request中获取uri值
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        
        this.request = request.toString();
        System.out.println(this.request);
        System.out.println("----------------------");
        
        this.parseRequestType(this.request);
        System.out.println("type:" + this.type);
        
        this.parseRequestBody(this.request);
        System.out.println("body:" + this.body);
        
        this.uri = parseUri(this.request);
        System.out.println("uri:" + this.uri);
    }
    
    public String parseRequestType(String request) {
    	String[] work = request.split("\n");
//        System.out.println("work=" + work[0]);
        String[] args = work[0].split(" ");
        this.type = args[0];
        return this.type;
    }
    
    public String parseRequestBody(String request) {
    	byte[] b = {13};
    	String[] work = request.split("\n");
    	
    	for(int i = 0; i < work.length; i++) {
//    		System.out.println("work[" + i + "]=" + work[i]);
    		if(work[i].contains("Content-Length")) {
    			int length = 0;
				try {
					String[] temp = work[i].replaceAll(new String(b, "utf-8"), "").split(" ");
					length = Integer.valueOf(temp[1]);
				} catch (NumberFormatException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			this.body = request.substring(request.length() - length, request.length());
    			break;
    		}
    	}
//        if(work.length == 1) {
//        	System.out.println("parseRequestBody0=" + work[0]);
//        }
//        System.out.println("parseRequestBody1=" + work[1]);
//        String[] args = work[1].split("&");
        
        return this.body;
    }
    
//    public void parsePlus() throws IOException {
//    	
//    	byte[] b = new byte[8];
//    	String result = "";
//    	int length = -1;
//    	while(input.available() != 0) {
//    		length = input.read(b);
//    		result += new String(b, 0, length);
//    	}
//    	
//    	uri = parseUri(result.toString());
//    }

    
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

	public String getType() {
		return type;
	}

	public String getBody() {
		return body;
	}

}
