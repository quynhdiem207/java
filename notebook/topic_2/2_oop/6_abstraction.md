## 2. Abstraction 

Abstraction (tính trừu tượng) giúp che giấu chi tiết implementation của methods (chi tiết quy trình hoạt động diễn ra) với user, chỉ hiển thị những tính năng thiết yếu như tên chức năng, input, output, result.  

Để thực hiện được điều này, Java sử dụng abstract class & interface.  

Abstraction làm tăng tính mở rộng của hệ thống, các subclass khác nhau có thể implements các abstract method của super class theo cách của riêng nó.  

*Ví dụ*:  


Cấu trúc thư mục:  

> + src  
>     + edu.model  
>         + Person.java    (abstract class)  
>         + Member.java    (interface)  
>         + Employee.java  (class)  
>         + Student.java   (class)  
>     + edu
>         + Fresher.java    (main class)  

```java
// edu.model/Person.java
package edu.model;

public abstract class Person {
	String message;

    public abstract void study();
    
    public void showMessage() {
        System.out.println(message);
    }
}

// edu.model/Member.java
package edu.model;

public interface Member {
	String NAME = "Diêm";
	
	void showName();
}

// edu.model/Student.java
package edu.model;

public class Student extends Person implements Member {
	private String name;

	public Student() {}

	/**
	 * @param name
	 */
	public Student(String name) {
		this.name = name;
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

	@Override
	public void study() {
		System.out.println(name + " is studying now...");
	}

	@Override
	public void showName() {
		System.out.println("My name is " + name);
	}
}

// edu.model/Employee.java
package edu.model;

public class Employee extends Person implements Member, Colleague {
	private String name;

	public Employee() {}

	/**
	 * @param name
	 */
	public Employee(String name) {
		this.name = name;
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

// edu/Fresher.java
package edu;

import edu.model.Employee;
import edu.model.Student;

public class Fresher {
	public static void main(String[] args) {
		Employee employee =  new Employee("Thu");
		
		employee.showName();    // I'm Thu
		employee.work();        // Thu is working...
		employee.study();       // Thu is studying...
		employee.showMessage(); // null
		
		System.out.println(Employee.NAME); // Diêm

        Student student = new Student("Hoàng");
		student.showName();     // My name is Hoàng
		student.study();        // Hoàng is studying now...
		student.showMessage();  // null
		
		System.out.println(Student.NAME); // Diêm
	}
}
```