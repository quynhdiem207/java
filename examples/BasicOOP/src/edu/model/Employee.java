/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu.model;

public class Employee extends Person implements Member, Colleague {

	public Employee() {}

	/**
	 * @param name
	 */
	public Employee(String name) {
		super(name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void study() {
		System.out.println(name + " is studying...");
	}

	@Override
	public void showName() {
		System.out.println("I'm " + name);
	}

	@Override
	public void work() {
		System.out.println(name + " is working...");
	}
}
