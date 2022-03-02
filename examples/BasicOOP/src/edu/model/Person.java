/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu.model;

public abstract class Person {
	String message;

    public abstract void study();
    
    public void showMessage() {
        System.out.println(message);
    }
}
