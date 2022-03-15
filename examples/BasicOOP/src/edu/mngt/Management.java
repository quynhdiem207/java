/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu.mngt;

import edu.model.Employee;

public class Management implements EmployeeManager {
	@Override
	public void report() {
		System.out.println("report...");
	}
	
	@Override
	public void paymentOfSalaries() {
		System.out.println("Payment of salaries...");
	}
	
	public void work(Employee employee) {
		System.out.println(employee.getName() + " is working...");
	}
}
