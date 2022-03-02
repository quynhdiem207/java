## 1. Inheritance

Inheritance (tính kế thừa) là cách để tái sử dụng tài nguyên của class.  

- Sử dụng 2 keywords: *extends* & *implements*.  
- Tài nguyên được thừa kế phụ thuộc vào *access modifiers*.  
- Mặc định, mỗi class đều được thừa kế 1 *default constructor*.  
- Tất cả Java classes đều được kế thừa từ *Object* class trong package java.lang.  
- Một class khi *extends* một abstract class hay *implements* một interface, phải override tất cả các abstract method, đồng thời implement phần method body.  
- Trong Java cho phép đơn thừa kế các class với nhau (một class chỉ có thể extends 1 class), nhưng có thể đa thừa kế với các interfaces (một class có thể implements nhiều interfaces).  

> class     --- extends ------> class  
> class     --- implements ---> interface
> interface --- extends ------> interface

*Ví dụ*:  

Cấu trúc thư mục:  

> + src  
>     + edu.model  
>         + Person.java    (abstract class)  
>         + Member.java    (interface)  
>         + Colleague.java (interface)  
>         + Employee.java  (class)  
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

// edu.model/Colleague.java
package edu.model;

public interface Colleague {
	void work();
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
import edu.model.Manager;

public class Fresher {
	public static void main(String[] args) {
		Employee employee =  new Employee("Thu");
		
		employee.showName();    // I'm Thu
		employee.study();       // Thu is studying...
		employee.showMessage(); // null
		employee.work();        // Thu is working...
		
		System.out.println(Employee.NAME); // Diêm
	}
}
```