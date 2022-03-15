/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Feb 20, 2022
 * @version 1.0
 */
package edu;

import java.util.Calendar;

import edu.mngt.EmployeeManager;
import edu.mngt.Management;
import edu.mngt.PersonManager;
import edu.model.Adult;
import edu.model.Child;
import edu.model.Colleague;
import edu.model.Employee;
import edu.model.Manager;
import edu.model.Member;
import edu.model.Person;
import edu.model.Singer;
import edu.model.Student;

public class Fresher {
	static int m(byte a, int b) { return a+b; }
    static int m(short a, short b) { return a-b; } 
    
	public static void main(String[] args) {
		Manager manager = new Manager("Minh");
		
		Employee employee =  new Employee("Thu");
		manager.report(employee);
		
		employee.showMessage();
		employee.study();
		employee.showName();
		employee.work();
		System.out.println(Employee.NAME);
		System.out.println(Employee.firstName);
		Employee.firstName = "Hi";
		System.out.println(Employee.firstName + " - 1");
		
		Student student = new Student("Hoàng");
		student.showName();
		student.study();
		student.showMessage();
		student.run();
		System.out.println(Student.NAME);
		System.out.println(Student.firstName + " - 2");
		
		Member member = new Student("Anh");
		member.showName();
		System.out.println(Member.NAME);

		Person person = new Student("Mai");
		person.showMessage();
		person.study();
		
		int[] arrInt = { 1, 2 };
		float[] arrFloat = { 1f };
//		arrFloat = arrInt;
		
		Person[] arrPerson = { new Person(), new Student("Hà") };
		Student[] arrStudent = { new Student() };
		arrPerson = arrStudent;
		
		Object[] arrObj = { new Person(), "Hello" };
		arrObj = arrPerson;
//		arrObj = arrInt;
		
		Object obj = null;
		obj = 1;
		obj = new Person();
		obj = arrInt;
		obj = arrPerson;
		
		int a = 2;
		long b = a;
		a = (int) b;
		char c = '0';
		a = (int) c;
		int d = (int) a;
		System.out.println(d);
		
		int big = 1234567890;
        float approx = big;
        System.out.println(big);
        System.out.println(approx);
		
		EmployeeManager empManager = new Management();
		PersonManager perManager = empManager;
		perManager.report();
		empManager = (EmployeeManager) perManager;
		empManager.paymentOfSalaries();
        
		Member m = new Employee("Hà");
		m.showName();
		Colleague col = new Employee("Hùng");
		col.work();
		col = (Colleague) m;
		col.work();
		
		Person per = new Employee();
		Employee stu = (Employee) per;
		
		int[] z1 = { 1, 2 };
		int[] z2 = z1;  // OK
//		long[] z3 = z1; // COMPILE-TIME ERROR
		
		Member[] x = {new Employee()};
		Employee[] y = {new Employee()};
//		y = (Employee[]) x; // RUNTIME ERROR
		
		m((byte) 12, 2);
		byte theAnswer = 42;
		
		String a1 = "hello\054\tMai";
		String a2 = "hello,\tMai";
		System.out.println(a2);
		
		Singer singer = new Singer("Huy", Calendar.getInstance().getTime());
		System.out.println(singer.birdthday);
		singer.run();
		singer.work();
		singer.talk();
		
		Singer.footCount = 4;
		System.out.println(Child.footCount);
	}
}
