package com.joe.model;

public class Book {
	private int id;
	private String name;
	private double prise;
	private String auther;
	public Book() {
		
	}
	public Book(int id, String name, double prise, String auther) {
		super();
		this.id = id;
		this.name = name;
		this.prise = prise;
		this.auther = auther;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public void setPrise(double prise) {
		this.prise = prise;
	}
	public String getAuther() {
		return auther;
	}
	public void setAuther(String auther) {
		this.auther = auther;
	}
}
