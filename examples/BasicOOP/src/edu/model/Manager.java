/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu.model;

public class Manager {
	public String name;

	public Manager() {
		System.out.println("a manager object without name is created");
	}

	public Manager(String name) {
		System.out.println("a manager object is created with name: " + name);
	}

	public void report(Employee employee) {
		System.out.println(employee.getName());
	}
}
