/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu;

import edu.model.Employee;
import edu.model.Manager;
import edu.model.Student;

public class Fresher {
	public static void main(String[] args) {
		Manager manager = new Manager("Minh");
		
		Employee employee =  new Employee("Thu");
		manager.report(employee);
		
		employee.showMessage();
		employee.study();
		employee.showName();
		employee.work();
		
		System.out.println(Employee.NAME);
		
		Student student = new Student("Hoàng");
		student.showName();
		student.study();
		student.showMessage();
		
		System.out.println(Student.NAME);
	}
}
