## 1. Java

Các đặc điểm của Java:  

- Ngôn ngữ đa luồng  
- Platform independent: Không phụ thuộc nền tảng linux, window, ...  
- Hướng đối tượng OOP: trừu tượng, kế thừa, đa hình, đóng gói.  

Để thực thi chương trình Java, cần có *javac* là trình biên dịch giúp compile java file có chứa Java code thành class file chứa Java bytecode để có thể chạy trên JVM (Java Virtual Machine).  

JRE (Java Runtime Environment) là trình thông dịch được cài đặt trên các HĐH.  


## 2. Hello world

- Viết code: HelloWorld.java  

    ```java
    // examples/HelloWorld.java
    package examples;

    public class HelloWorld {
        public static void main(String[] args) {
            System.out.println("Hello world!");
        }
    }
    ```

- Compile code:   
    > javac HelloWorld.java  

- Run code:   
    > java HelloWorld  


## 3. IDEs  

Một số IDEs phổ biến:  

- Eclipse  
- Netbean  
- IntelliJ  


## 4. Eclipse  

- Working Set: cho phép lựa chọn các projects hiện đang làm việc tại 1 thời điểm, tránh việc hiển thị quá nhiều projects trong package explorer gây nhầm lẫn.  
- Code formatter: Giúp format code, có thể edit tại Window/Preferences/  
- Code template: Giúp thêm các headers (eg: comments) khi thực hiện hành động nào đó (eg: Create a class), có thể edit tại Window/Preferences/  

    ```java
    /*
    * (C) Copyright 2022
    * 
    * @author diem_quynh
    * @date Feb 19, 2022
    * @version 1.0
    */

    public class Customer {}
    ```


## 5. main() method

main() là method đặc biệt trong Java.  

```java
public static void main(String[] args) {}
```

- Là starting point & bắt buộc của các Java programs.  
- Phải là public & static.  
- Không có return type (void).  
- Arguments của program là String[].  


## 6. Comments

Trong Java có 2 dạng comments:  

- Single-line comment: //  
- Multi-line comment (Block comment):  /* */


## 7. jar file (java archive)  

- *jar* file là các file ở định dạng zip (zip format) bao gồm các class files đã được compiled.  
- Trong quá trình chạy ứng dụng, tại thời điểm runtime không cần compile nữa.  
- jar file có thể sử dụng theo 2 hình thức:  
    - Sử dụng như 1 file ứng dụng độc lập, có tính executable & runable.  
    (Note: trường hợp này cần có 1 class chứa main() method để có thể biết chạy từ đâu)  
    - Sử dụng như các libraries cho project.  

#### 7.1, jar command  

Trong JDK có jar command giúp create & run các file jar.

- Trước khi sử dụng *jar* command cần compile các file *java* tạo thành các file *class*.  
- Tạo file manifest:  

    ```txt
    Main-Class: HelloWorld
    ```

- Chạy command tạo jar file:  
    > jar -cvfm HelloWorld.jar manifest.txt *.class

- Run jar file:  
    > java -jar HelloWorld.jar  

#### 7.2, jar file với eclipse  

- Để tạo file *jar* với Eclipse, trên Toolbar chọn menu File/Export/Java/JAR file | Runable JAR file.  
- Chọn class chứa main() method tại mục Launch configuration, & điền Export destination.  
- Run Jar file:  
    > java -jar HelloWorld.jar  


## 8. Java build tools

Build tools là các công cụ tạo các jar file từ project một cách tự động bằng cách xây dựng các script, thay vì sử dụng các command. Nó cho phép quản lý các dependencies của project, ngoài ra có thể tích hợp tự động build, test, deploy với script.  

Java có các build tools phổ biến sau:  

- Maven  
- Gradle  
- Ant  

