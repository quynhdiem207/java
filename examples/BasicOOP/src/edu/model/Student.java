/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu.model;

public class Student extends Person implements Member {
	public Student() {
		super();
	}

	/**
	 * @param name
	 */
	public Student(String name) {
		super(name);
	}

	/**
	 * @return the name
	 */
	protected String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	@Override
	public void study() {
		System.out.println(name + " is studying now...");
	}

	@Override
	public void showName() {
		System.out.println("My name is " + name);
	}
	
	public void run() {
		System.out.println(name + " is running...");
	}
}
