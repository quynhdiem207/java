# Lession 8. Packages

## 1. Creating and Using Packages

Để dễ tìm và sử dụng các types hơn, tránh xung đột trong việc đặt tên và kiểm soát quyền truy cập, programmer gói các nhóm types có liên quan vào các package.

**Định nghĩa**: Một *package* là một nhóm của các types có liên quan với nhau, cung cấp khả năng bảo vệ truy cập và quản lý namespace. 

Note: các types đề cập đến các class, interface, enumerations và annotation type. Trong đó, enumerations và annotation types tương ứng là các class và interface types đặc biệt.

Các types mà là một phần của Java platform, là các members của các packages khác nhau mà gói các class theo chức năng: các classes cơ bản nằm trong java.lang, các classes cho reading và writing (input và output) nằm trong java.io, ... Bạn cũng có thể đặt các types của mình trong các packages.

Bạn nên gói các class và interface này trong một package vì một số lý do sau:  

- Bạn và các programmer khác có thể dễ dàng xác định rằng các types này có liên quan với nhau.  
- Bạn và các programmer khác biết nơi để tìm các types có thể cung cấp các chức năng liên quan đến đồ họa.  
- Tên các types của bạn sẽ không xung đột với tên các type trong các package khác, bởi vì package tạo ra một namespace mới.  
- Bạn có thể cho phép các types bên trong package có quyền truy cập không hạn chế lẫn nhau, nhưng vẫn hạn chế quyền truy cập đối với các types bên ngoài package.  


### 1.1, Creating a Package

Để tạo một package, bạn cần chọn tên cho package và đặt một *package* statement với tên đó ở đầu *every source file* có chứa các types (class, interface, enum và annotation type) mà bạn muốn bao gồm trong package đó.

Package statement (*VD: package graphics;*) phải là dòng đầu tiên trong source file. Chỉ có thể có một package statement trong mỗi source file và nó áp dụng cho tất cả các types trong tệp.  

**Note**: Nếu bạn đặt nhiều types trong một source file, chỉ một type có thể là *public* và nó phải có cùng tên với source file. 

*Ví dụ: bạn có thể định nghĩa public class Circle trong file Circle.java, định nghĩa public interface Draggable trong file Draggable.java, định nghĩa public enum Day trong file Day.java, ...*

Bạn có thể bao gồm các non-public types trong cùng một file với một public type (không khuyến khích, trừ khi các non-public types nhỏ và có liên quan chặt chẽ với public type đó), nhưng chỉ public type mới có thể truy cập được từ bên ngoài package. Tất cả các top-level, non-public types sẽ là package private.

Nếu bạn không sử dụng package statement, type của bạn sẽ kết thúc bằng một unnamed package. Nói chung, một unnamed package chỉ dành cho các ứng dụng nhỏ hoặc tạm thời hoặc khi bạn mới bắt đầu quá trình phát triển.


*Ví dụ: Giả sử bạn viết một nhóm các class đại diện cho các graphic objects, chẳng hạn như circles, rectangles, lines, và points. Bạn cũng có thể viết một interface, Draggable, mà các class sẽ implements nếu chúng có thể được kéo bằng chuột. Tất cả chúng được đặt trong một package có tên là graphics:*

```java
//in the Draggable.java file
package graphics;
public interface Draggable { ... }

//in the Graphic.java file
package graphics;
public abstract class Graphic { ... }

//in the Circle.java file
package graphics;
public class Circle extends Graphic implements Draggable { ... }

//in the Rectangle.java file
package graphics;
public class Rectangle extends Graphic implements Draggable { ... }

//in the Point.java file
package graphics;
public class Point extends Graphic implements Draggable { ... }

//in the Line.java file
package graphics;
public class Line extends Graphic implements Draggable { ... }
```


### 1.2, Naming a Package

Có khả năng các programmers sẽ sử dụng cùng một tên cho các types khác nhau trong cùng một dự án, điều này được phép nếu chúng nằm trong các packages khác nhau.  

*Ví dụ: Định nghĩa một class Rectangle khi đã có một class Rectangle trong java.awt package. Tuy nhiên, compiler cho phép cả hai class có cùng tên nếu chúng nằm trong các package khác nhau. Fully qualified name của mỗi class Rectangle bao gồm package name. Nghĩa là, fully qualified name của class Rectangle trong package graphics là 'graphics.Rectangle' và fully qualified name của class Rectangle trong package java.awt là 'java.awt.Rectangle.'*

#### *Naming Convensions*

Package name được viết bằng tất cả các chữ thường để tránh xung đột với tên của các class hoặc interface.

Các công ty sử dụng tên miền Internet đã đảo ngược của họ để bắt đầu các package name của họ — *ví dụ: com.example.mypackage cho một package có tên mypackage được tạo bởi một programmer tại example.com.*

Các xung đột về tên xảy ra trong một công ty cần được xử lý theo quy ước trong công ty đó, có thể bằng cách bao gồm khu vực hoặc project name sau tên công ty (*ví dụ: com.example.region.mypackage*).

Các package trong bản thân ngôn ngữ Java bắt đầu bằng *java.* hoặc *javax.*

Trong một số trường hợp, tên miền internet có thể không phải là package name hợp lệ. Điều này có thể xảy ra nếu tên miền chứa dấu gạch ngang hoặc ký tự đặc biệt khác, nếu package name bắt đầu bằng một chữ số hoặc ký tự khác không hợp pháp để sử dụng dưới dạng bắt đầu của Java name hoặc nếu package name chứa keyword dành riêng của Java, chẳng hạn như "int". Trong trường hợp này, quy ước được đề xuất là thêm dấu gạch dưới. Ví dụ:

```
        Domain Name            |         Package Name Prefix
-------------------------------|--------------------------------
hyphenated-name.example.org	   |    org.example.hyphenated_name
example.int	                   |    int_.example
123name.example.com	           |    com.example._123name
```


### 1.3, Using Package Members

Các types nằm trong một package được gọi là các package members.

Để sử dụng một public package member từ bên ngoài package của nó, bạn phải thực hiện một trong những điều sau:  

- Tham chiếu đến member đó bằng fully qualified name của nó.  
- Import package member đó.  
- Import toàn bộ package của member đó.  


#### *Referring to a Package Member by Its Qualified Name*

Bạn có thể sử dụng simple name của một package member nếu mã bạn đang viết nằm trong cùng package với member đó hoặc nếu member đó đã được import.

Tuy nhiên, nếu bạn đang cố gắng sử dụng một member từ một package khác và package đó chưa được import, bạn phải sử dụng fully qualified name của member đó.

```java
graphics.Rectangle myRect = new graphics.Rectangle();
```

**Note**: Cách này không thích hợp khi sử dụng các members của một package nhiều lần.


#### *Importing a Package Member*

Để import một member cụ thể vào file hiện tại, cần đặt *import* statement ở đầu file trước bất kỳ type definitions nào nhưng sau *package* statement (nếu có). 

```java
import graphics.Rectangle;
```

Sau khi import các package members, có thể tham chiếu đến chúng bằng simpile name.  

```java
Rectangle myRectangle = new Rectangle();
```

**Note**: Cách này nên sử dụng nếu bạn chỉ sử dụng một vài members trong cùng một package. Nếu bạn sử dụng nhiều types từ một package, bạn nên import toàn bộ package đó.


#### *Importing an Entire Package*

Để import tất cả các types nằm trong một package cụ thể, sử dụng import statement với ký tự hoa thị (*).

```java
import graphics.*;
```

Sau khi import toàn bộ package, có thể tham chiếu đến tất cả các types trong package đó bằng simpile name của chúng.

```java
Circle myCircle = new Circle();
Rectangle myRectangle = new Rectangle();
```

Dấu * trong import statement chỉ có thể được sử dụng để chỉ định tất cả các types trong một package. Nó không thể được sử dụng để so khớp một subset của các types trong một package. 

```java
import graphics.A*; // compile-time error
```

**Note**: Một hình thức import khác, ít phổ biến hơn cho phép bạn import tất cả các public nested types của một enclosing class. 

```java
import graphics.Rectangle;
import graphics.Rectangle.*; // this statement will not import Rectangle
```

Để thuận tiện, Java compiler tự động import toàn bộ 2 packages: (1) java.lang package, và (2) current package (package of current file).


#### *Apparent Hierarchies of Packages*

Khi import, tiền tố của các packages không thể hiện việc bao gồm import các subpackages có cùng tiền tố, nó chỉ cho thấy giữa các packages này có sự liên quan đến nhau. 

*Ví dụ: Java API bao gồm một java.awt package, một java.awt.color package, một java.awt.font package và nhiều package khác bắt đầu bằng java.awt. Tuy nhiên, java.awt.color package, java.awt.font package và các java.awt.xxxx package khác không được bao gồm trong package java.awt. Tiền tố java.awt được sử dụng cho một số package liên quan để làm cho mối quan hệ trở nên rõ ràng, nhưng không thể hiện sự bao gồm.*

Import java.awt.* sẽ import tất cả các types trong java.awt package, nhưng không import java.awt.color, java.awt.font hoặc bất kỳ java.awt.xxxx package nào khác. Nếu bạn định sử dụng các types trong java.awt.color cũng như các types trong java.awt, bạn phải import cả 2 packages:

```java
import java.awt.*;
import java.awt.color.*;
```


#### *Name Ambiguities*

Nếu một member trong một package chia sẻ tên của nó với một member trong một package khác và cả 2 package đều được import, bạn phải tham chiếu đến từng member bằng qualified name của nó. 

*Ví dụ: graphics package đã định nghĩa một class có tên là Rectangle. java.awt package cũng chứa một class Rectangle. Nếu cả graphics và java.awt đều đã được import, thì statement sau sẽ không rõ ràng, tạo nên sự mơ hồ:*

```java
Rectangle rect; // compile-time error
```

*Trong tình huống như vậy, bạn phải sử dụng qualified name của member để chỉ ra chính xác class Rectangle mà bạn muốn. Ví dụ,*

```java
graphics.Rectangle rect;
```


#### *The Static Import Statement*

*Static import* statement cung cấp cho bạn một cách để import các public static final fields (constants) và public static methods mà bạn muốn sử dụng mà không cần phải thêm tiền tố type name của chúng. Có thể import riêng lẻ từng static member, hoặc import toàn bộ các static members.

*Ví dụ: Thông thường, để sử dụng các constants và static methods này từ một type khác, bạn sử dụng tiền tố type name, như sau:*

```java
double r = Math.cos(Math.PI * theta);
```

*Có thể sử dụng static import statement để import các static members của java.lang.Math để không cần thêm tiền tố class name, Math. Các static members của Math có thể được import riêng lẻ hoặc dưới dạng nhóm tất cả chúng:*

```java
import static java.lang.Math.PI;
// or
import static java.lang.Math.*;
```

Sau khi import, các static members có thể tham chiếu đến tất cả các static members bằng simple name của chúng.

```java
double r = cos(PI * theta);
```

**Note**: Việc lạm dụng static import có thể dẫn đến mã khó đọc và khó bảo trì, bởi vì người đọc mã sẽ không biết type nào định nghĩa một static member cụ thể. 


### 1.4, Managing Source and Class Files

Nhiều triển khai của Java platform dựa vào hệ thống tệp phân cấp để quản lý source và các class files.

Đặt source code của một class, interface, enum, annotation type trong tệp văn bản có tên là simple name của type đó và có extension là '.java'.

```java
//in the Rectangle.java file 
package graphics;
public class Rectangle { ... }
```

Sau đó, đặt source file vào một thư mục có tên ánh xạ tên của package mà type đó thuộc về:

```
....\graphics\Rectangle.java

Qualified name của package member và path name đến file đó như sau:
- class name:       graphics.Rectangle  
- pathname to file: graphics\Rectangle.java  
```

Mỗi thành phần của package name tương ứng với một subdirectory.

*Ví dụ: com.example.graphics package chứa một Rectangle.java source file, nó sẽ được chứa trong một loạt các subdirectories lồng nhau như sau:*

```
....\com\example\graphics\Rectangle.java
```

Khi bạn compile một source file, compiler sẽ tạo ra một output file khác nhau cho mỗi type được định nghĩa trong nó. Base name của output file là name của type, và extension của nó là '.class'. 

*Ví dụ: Cho source file như sau:*

```java
//in the Rectangle.java file
package com.example.graphics;
public class Rectangle { ... }
class Helper{ ... }
```

*thì các output files đã compile sẽ được đặt tại:*

```
....\com\example\graphics\Rectangle.class
....\com\example\graphics\Helper.class
```

Đường dẫn đầy đủ đến *classes directory*, được gọi là *class path* và được set bằng CLASSPATH system variable. Cả compiler và JVM đều xây dựng đường dẫn đến các .class files của bạn bằng cách thêm package name vào class path.

Theo mặc định, compiler và JVM tìm kiếm thư mục hiện tại và tệp JAR chứa các class của nền tảng Java để các thư mục này tự động nằm trong class path của bạn.


#### *Setting the CLASSPATH System Variable*

Để hiển thị CLASSPATH variable hiện tại, sử dụng các commands sau trong Windows và UNIX (Bourne shell):

```
In Windows:   C:\> set CLASSPATH
In UNIX:      % echo $CLASSPATH
```

Để xóa nội dung hiện tại của CLASSPATH variable, sử dụng các commands sau:

```
In Windows:   C:\> set CLASSPATH=
In UNIX:      % unset CLASSPATH; export CLASSPATH
```

Để set CLASSPATH variable, sử dụng các commands sau (ví dụ):

```
In Windows:   C:\> set CLASSPATH=C:\users\george\java\classes
In UNIX:      % CLASSPATH=/home/george/java/classes; export CLASSPATH
```
