## 1. Java

Các đặc điểm của Java:  

- Ngôn ngữ đa luồng  
- Platform independent: Không phụ thuộc nền tảng linux, window, ...  
- Hướng đối tượng OOP: trừu tượng, kế thừa, đa hình, đóng gói.  


## 2. Example

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

- Output: Hello world!


## 3. main() method

*main()* là method đặc biệt trong Java:  

- Là starting point & bắt buộc của các Java programs.  
- Modifiers phải là *public* & *static*.  
- Return type phải là *void*.  
- First parameter phải có type là *String[]* chứa các arguments được truyền vào khi chạy chương trình.  

```java
// HelloWorld.java
public class HelloWorld {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++)
            System.out.print(i == 0 ? args[i] : " " + args[i]);
        System.out.println();
    }
}
```

Compile code:  
> javac HelloWorld.java  

Run code:  
> java HelloWorld Hello, world  

Output:  
> Hello, world  


## 4. IDEs  

Một số IDEs phổ biến hỗ trợ phát triển chương trình Java:  

- Eclipse  
- Netbean  
- IntelliJ  


## 5. Eclipse  

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


## 6. jar file (java archive)  

- *jar* file là các file ở định dạng zip (zip format) bao gồm các class files đã được compiled.  
- Trong quá trình chạy ứng dụng, tại thời điểm runtime không cần compile nữa.  
- jar file có thể sử dụng theo 2 hình thức:  
    - Sử dụng như 1 file ứng dụng độc lập, có tính executable & runable.  
    (Note: trường hợp này cần có 1 class chứa main() method để có thể biết chạy từ đâu)  
    - Sử dụng như các libraries cho project.  

#### *6.1, jar command*  

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

#### *6.2, jar file với eclipse*  

- Để tạo file *jar* với Eclipse, trên Toolbar chọn menu File/Export/Java/JAR file | Runable JAR file.  
- Chọn class chứa main() method tại mục Launch configuration, & điền Export destination.  
- Run Jar file:  
    > java -jar HelloWorld.jar  


## 7. Java build tools

Build tools là các công cụ tạo các jar file từ project một cách tự động bằng cách xây dựng các script, thay vì sử dụng các command. Nó cho phép quản lý các dependencies của project, ngoài ra có thể tích hợp tự động build, test, deploy với script.  

Java có các build tools phổ biến sau:  

- Maven  
- Gradle  
- Ant  
