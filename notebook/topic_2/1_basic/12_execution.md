# Chap 12: Execution  

Chương này đề cập đến các activities xảy ra trong quá trình thực thi một chương trình, nó được tổ chức xung quanh life cycle (vòng đời) của JVM, & các class, interface, object tạo thành một chương trình.  

JVM khởi động bằng cách load (tải) một class hoặc interface được chỉ định, sau đó gọi method main trong class hoặc interface được chỉ định này. Phần §12.1 phác thảo các bước loading, liking và initialization liên quan đến việc thực thi main. Các phần khác chỉ rõ chi tiết về loading, linking và initialization.

Chương này tiếp tục với đặc tả của các quy trình để tạo các class instance mới (§12.5); và hoàn thiện các class instance (§12.6). Nó kết thúc bằng cách mô tả việc unload (dỡ bỏ) các class (§12.7) và quy trình khi một chương trình thoát (§12.8).  


## 1. JVM Start up

JVM bắt đầu thực thi bằng cách gọi method main của một số class hoặc interface được chỉ định, truyền cho nó một đối số duy nhất là một array các chuỗi.  

Trong ví dụ của chương này, class đầu tiên này là Test. JVM thực hiện các bước để thực thi Test: loading, linking, & initialization.  


### 1.1, Load the Class Test

JVM cố gắng thực thi main method của class Test, & phát hiện class Test chưa được load. Sau đó JVM sử dụng một class loader để tìm kiếm, nếu quá trình này fail thì một lỗi sẽ được ném ra.  


### 1.2, Link Test: Verify, Prepare, (Optionally) Resolve

Sau khi Test được load, nó phải được khởi tạo trước khi main được gọi. Giống như tất cả các class & interface types, Test phải được liên kết trước khi nó được khởi tạo. Linking bao gồm: verification, preparation, & (optionally) resolution.  

Verification kiểm tra xem class Test đã được load có được định dạng tốt, với symbol table thích hợp hay không. Verification cũng kiểm tra xem code triển khai Test có tuân theo các yêu cầu ngữ nghĩa của ngôn ngữ lập trình Java và JVM hay không. Nếu một vấn đề được phát hiện trong quá trình verification, thì một lỗi sẽ được ném ra.  

Preparation liên quan đến việc phân bổ static storage và bất kỳ data structures (cấu trúc dữ liệu) nào được sử dụng nội bộ bởi việc triển khai JVM, chẳng hạn như method tables.  

Resolution là quá trình kiểm tra các symbolic references (tham chiếu biểu tượng) từ Test đến các class và interface khác, bằng cách load các class và interface khác được đề cập và kiểm tra xem các tham chiếu đó là chính xác.  

Bước Resolution là tùy chọn, giải quyết các symbolic references từ class or interface đang được liên kết, thậm chí là các symbolic references từ các class & interface được tham chiếu một cách đệ quy.  


### 1.3, Initialize Test: Execute Initializers  

JVM cố gắng thực thi main method của class Test, điều này chỉ được phép khi class Test đã được khởi tạo.  

Initialization bao gồm việc thực thi bất kỳ class variable initializers & static initializers của class Test. Nhưng trước khi Test được khởi tạo, superclass trực tiếp của nó phải được khởi tạo, cũng như superclass trực tiếp của superclass trực tiếp của nó một cách đệ quy.  

*Ví dụ*: Trong trường hợp đơn giản nhất, Test có Object là superclass trực tiếp ngầm định của nó; nếu class Object chưa được khởi tạo, thì nó phải được khởi tạo trước khi Test được khởi tạo. Class Object không có superclass, vì vậy đệ quy kết thúc ở đây.  

Nếu class Test có một super class khác làm superclass của nó, thì Super phải được khởi tạo trước Test. Điều này yêu cầu loading, verifying, & preparing Super nếu chưa được thực hiện, và tùy thuộc vào việc triển khai cũng có thể liên quan đến việc giải quyết các symbolic references từ Super, v.v., một cách đệ quy.  

Do đó, Initialization có thể gây ra lỗi loading, linking và initialization, bao gồm các lỗi liên quan đến các types khác.


### 1.4, Invoke Test.main

Cuối cùng, sau khi hoàn thành quá trình initialization cho class Test, method main của Test được gọi.  

Method main phải được khai báo là public, static, & void. Nó phải xác định một tham số hình thức (formal parameter) có type được khai báo là array các String. Do đó, các khai báo sau được chấp nhận:  

```java
public static void main(String[] args) {}

public static void main(String... args) {}
```


## 2. Loading of Classes and Interfaces

*Loading* đề cập đến quá trình tìm kiếm dạng nhị phân (binary form) của một class or interface type với một name cụ thể, và từ dạng nhị phân đó, tạo một *Class* object để đại diện cho class or interface.  

Định dạng nhị phân của class or interface thường là định dạng class file.  

Method *defineClass* của ClassLoader có thể được sử dụng để tạo các Class object là instance của *java.lang.Class* từ định dạng class file.  