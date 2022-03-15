/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu.model;

public class Person {
	protected String name;
	public static String firstName;
	
	public Person() {}
	public Person(String name) {
		this.name = name;
	}

    public void study() {};
    
    public void showMessage() {
        System.out.println("message...");
    }
}
