## 1. Basic OOP  

- Class  
- Interface  
- Package  
- Access Modifiers  
- OOP Characteristics  


## 2. Class  

Class là một khai báo để tạo ra các object tương tự nhau.  

Syntax:  

> AccessModifier class ClassName {}

*Ví dụ*:  

```java
package edu.fa;

public class HelloWorld {
    private String message = "Hello World!";
    public void showMessage() {
        System.out.println("Hello world!");
    }
}
```

*Note*:  

- Trong java file có thể khai báo 0, 1 hay nhiều class. Tuy nhiên, chỉ có thể khai báo một hoặc không có *public class* trong một java file.  
- Public class name (nếu có) phải được sử dụng để đặt tên cho java file, nếu java file không có public class thì có thể đặt bất cứ tên gì.  


## 3. Package

Package là một định danh (namespace) cho tập hợp các classes & interfaces.  

*Ví dụ*:  

```java
package edu.fa;

public class HelloWorld {
    private String message = "Hello World!";
    public void showMessage() {
        System.out.println("Hello world!");
    }
}
```


## 4. Access Modifiers

Access Modifiers quy định phạm vi sử dụng của các tài nguyên bao gồm variables, methods, constructors, or classes.  

Trong Java có 4 Access Modifiers:  

- **public**: Cho phép truy xuất từ mọi nơi.  
- **protected**: Cho phép truy xuất từ bất cứ class nào trong cùng package, & chỉ cho phép truy xuất từ subclass ngoài package đó.  
- **default**: Chỉ cho phép truy xuất từ các class trong cùng package.  
- **private**: Chỉ cho phép truy xuất trong chính class đó.  

*Note*: default access modifier không sử dụng *default* keyword, không cần khai báo access modifier, Java sẽ tự động hiểu đây là default access modifier.  


## 5. Attributes (properties)

Syntax:  

> Modifiers Type AttributeName [ = InitValue];

Nếu không gán giá trị cho attributes, Java sẽ gán default value cho attribute đó:  

>- byte                     0  
>- short                    0  
>- int                      0  
>- long                     0  
>- float                    0  
>- double                   0  
>- char                     u0000  
>- boolean                  false  
>- String (or any object)   null  


## 6. Methods

Syntax:  

> Modifiers ReturnType MethodName([Parameters]) [throws ExceptionList] {}

*Ví dụ*:

Cấu trúc thư mục:  

> + src  
>     + edu.model  
>         + Employee.java  
>         + Manager.java  
>     + edu.mngt  
>         + Management.java

```java
// edu.model/Employee.java
package edu.model;

public class Employee {
    private String name;
}

// edu.model/Manager.java
package edu.model;

public class Manager {
	public String name;
	
	public void report(Employee employee) {
		System.out.println(employee.name);
	}
}

// edu.mngt/Management.java
package edu.mngt;

import edu.model.Employee;

public class Management {
	public void work(Employee employee) {
		System.out.println(employee.name);
	}
}
```


## 7. Abstract method

Abstract method là method đặc biệt được khai báo trong *abstract class* & *interface* sử dụng *abstract* keyword (là một *none access modifier*), abstract method không có body.  

Syntax:  

> Modifier abstract ReturnType MethodName([Parameters]);

*Ví dụ*:

```java
public abstract void study();
```


## 8. Constructor

*Constructor* là method đặc biệt được sử dụng để tạo ra các object.  

- Có cùng tên với class.  
- Không có Return type.  
- Mọi class đều có constructor, kể cả abstract class.  
- Có thể có parameters hoặc không.  
- Sử dụng *new* keyword để khởi tạo object, sẽ tự động gọi constructor.  


### 8.1, Default constructor

Mặc định, mọi class đều có 1 *public* constructor không có parameter, kể cả abstract class.  

Nếu cta khai báo 1 constructor, thì Java sẽ xóa bỏ default constructor.  

*Ví dụ*:

Cấu trúc thư mục:  

> + src  
>     + edu  
>         + Fresher.java
>     + edu.model  
>         + Manager.java  

```java
// edu.model/Manager.java
package edu.model;

public class Manager {
	public String name;

	public Manager() {
		System.out.println("a manager object without name is created");
	}

	public Manager(String name) {
		System.out.println("a manager object is created with name: " + name);
	}

	public void report(Employee employee) {
		System.out.println(employee.name);
	}
}

// edu/Fresher.java
package edu;

import edu.model.Manager;

public class Fresher {
	public static void main(String[] args) {
		Manager manager = new Manager();
		Manager manager2 = new Manager("Minh");
	}
}
```


## 9. Getters & Setters

Luôn set các attibute là *private* để bảo vệ data, Lúc này để có thể truy xuất các attributes từ bên ngoài class cần có các getters & setters.  

### 9.1, Setters

- Được sử dụng để set value cho attributes.  
- Return type phải là *void*.  
- Syntax:  
    ```java
    public void setAttributeName(Type newValue) {
        this.attributeName = newValue;
    }
    ```

### 9.2, Getters

- Được sử dụng để get value của attributes.  
- Không có parameter.  
- Syntax:  
    ```java
    public Type getAttributeName() {
        return this.attributeName;
    }
    ```

*Ví dụ*:


Cấu trúc thư mục:  

> + src  
>     + edu  
>         + Fresher.java  
>     + edu.model  
>         + Employee.java  
>         + Manager.java

```java
// edu.model/Employee.java
package edu.model;

public class Employee {
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
}

// edu.model/Manager.java
package edu.model;

public class Manager {
	public String name;

	public void report(Employee employee) {
		System.out.println(employee.getName());
	}
}

// edu/Fresher.java
package edu;

import edu.model.Employee;
import edu.model.Manager;

public class Fresher {
	public static void main(String[] args) {
		Manager manager = new Manager();
		
		Employee employee =  new Employee("Thu");
		manager.report(employee); // Thu
	}
}
```