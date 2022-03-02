## 1. Abstract Class

Các đặc điểm của abstract classes:  

- Abstract class có thể hoặc không chứa các abstract methods, cũng có thể chứa các non-abstract methods.  
- Nếu class chứa ít nhất 1 abstract method, nó phải được khai báo là abstract class.  
- Nếu class được khai báo là abstract, không thể sử dụng nó để khởi tạo object với *new* keyword một cách bình thường.  

*Ví dụ*:  

```java
package edu;

public abstract class Person {
    String message;

    public abstract void study();
    
    public void showMessage() {
        System.out.println(message);
    }
}
```