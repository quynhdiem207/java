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
	private String name;
	private Date birdthday;
	public static int footCount = 2;
	
	public Child() {}
	public Child(String name, Date birthday) {
		this.name = name;
		this.birdthday = birthday;
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
	/**
	 * @return the birdthday
	 */
	protected Date getBirdthday() {
		return birdthday;
	}
	/**
	 * @param birdthday the birdthday to set
	 */
	protected void setBirdthday(Date birdthday) {
		this.birdthday = birdthday;
	}
	
	public abstract void talk();
	
	public void run() {
		System.out.println(name + " is running...");
	}
}
