/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Mar 15, 2022
 * @version 1.0
 */
package edu.model;

import java.util.Date;

public class Singer extends Adult {
	public Singer() {}
	public Singer(String name, Date birthday) {
		super(name, birthday);
	}

	@Override
	public void talk() {
		System.out.println(name + " is talking...");
	}

}
