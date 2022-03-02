## 1. Interface

*Interface* là một kiểu tham chiếu trong Java, tương tự như class, nhưng có những điểm đặc biệt.  

Có thể coi interface là một tập hợp của các *abstract* methods.  

Syntax:  

> AccessModifier interface InterfaceName {}

*Ví dụ*:

```java
package edu;

public interface Fresher {
    public static final String NAME = "Fresher";
    public abstract void shareContent();
}
```


## 2. Interface Attributes

Các đặc điểm của interface attributes:  

- Dù không khai báo các modifiers *public static final*, nhưng Java vẫn sẽ hiểu các Attributes của interface đều là *public static final*.  
- Bắt buộc phải gán value cho các attributes của interface.  
- Syntax:  
    > public static final ATTR_NAME = value;  


## 3. Interface Methods

Các đặc điểm của interface methods:  

- Dù không khai báo các modifiers *public abstract*, nhưng Java vẫn sẽ hiểu các methods của interface đều là *public abstract*.  
- Syntax:  
    > public abstract ReturnType MethodName([Parameters]);  

*Ví dụ*:

```java
// edu/Member.java
package edu;

public interface Member {
	String NAME = "Diêm";
	
	void showName();
}
```