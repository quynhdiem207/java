/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Mar 15, 2022
 * @version 1.0
 */
package edu.model;

import java.util.Date;

public abstract class Adult extends Child {
	public Adult() {}
	public Adult(String name, Date birthday) {
		super(name, birthday);
	}
	
	public void work() {
		System.out.println(getName() + " is working...");
	}
}
