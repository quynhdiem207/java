/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Mar 15, 2022
 * @version 1.0
 */
package edu.model;

import java.util.Date;

public abstract class Child {
	public String name;
	public Date birdthday;
	public static int footCount = 2;
	
	public Child() {}
	public Child(String name, Date birthday) {
		this.name = name;
		this.birdthday = birthday;
	}
	
	public abstract void talk();
	
	public void run() {
		System.out.println(name + " is running...");
	}
}
