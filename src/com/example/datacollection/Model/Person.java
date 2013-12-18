package com.example.datacollection.Model;

public class Person {

	
	private String name;
	private String password;
	private boolean admin;
	public Person(String name, String password, boolean admin) {
		this.admin = admin;
		this.password = password;
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
