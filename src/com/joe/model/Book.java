package com.joe.model;

import java.io.Serializable;

public class Book implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private Double prise;
	private String auther;
	public Book() {
		
	}
	public Book(String name, String auther, Double prise) {
		super();
		this.name = name;
		this.prise = prise;
		this.auther = auther;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrise() {
		return prise;
	}
	public void setPrise(Double prise) {
		this.prise = prise;
	}
	public String getAuther() {
		return auther;
	}
	public void setAuther(String auther) {
		this.auther = auther;
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", prise=" + prise + ", auther=" + auther + "]";
	}
	
}
