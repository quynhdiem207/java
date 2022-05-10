# Lession 5. Interfaces and Inheritances

## 1. Interfaces

Xem chi tiết tại [Java specification about interface](../../specifications/9_interfaces-1.md).

### 1.1, Defining an Interface

Cú pháp một interface declaration bao gồm:  

```
{InterfaceModifier} interface Identifier [TypeParameters] [ExtendsInterfaces] InterfaceBody
```

Một interface có thể extends nhiều interface, Danh sách các interface được extends trong declaration được phân tách bởi dấu phẩy.  

Interface body có thể chứa các abstract methods, default methods và static methods. Các default method được định nghĩa với default modifier, các static methods được định nghĩa với static modifier. Tất cả các methods của interface đều là public.

Ngoài ra, interface body có thể chứa các constant declarations, là các public static final fields ngầm định.  


### 1.2, Implementing an Interface

Để khai báo một class triển khai một interface, sử dụng implements clasuse trong class declaration. Một class có thể triển khai nhiều hơn một interface, do đó, danh sách các interface được triển khai bởi một được phân tách nhau bằng dấu phẩy.


### 1.3, Using an Interface as a Type

Khi định nghĩa một interface mới, một reference data type mới được định nghĩa. Nếu định nghĩa một reference variable có type là một interface, thì bất kỳ object nào được gán cho nó phải là một instance của một class triển khai interface đó.


### 1.4, Evolving Interfaces

Giả sử một interface đã được phát triển như sau:  

```java
public interface DoIt {
   void doSomething(int i, double x);
   int doSomethingElse(String s);
}
```

Một thời gian sau, bạn muốn thêm một method vào interface như sau:  

```java
public interface DoIt {
   void doSomething(int i, double x);
   int doSomethingElse(String s);
   boolean didItWork(int i, double x, String s);
}
```

Thực hiện thay đổi này sẽ dẫn đến tất cả các class triển khai interface này bị hỏng. Vì vậy, hãy cố gắng dự đoán tất cả các mục đích sử dụng cho interface và xác định nó hoàn toàn ngay từ đầu. Nếu muốn thêm các method bổ sung vào interface, có một số tùy chọn:  

- Tạo một interface mới extends interface cũ, người sử dụng code của bạn có thể lựa chọn tiếp tục sử dụng interface cũ hoặc nâng cấp lên interface mới.  
    ```java
    public interface DoItPlus extends DoIt {
        boolean didItWork(int i, double x, String s);
    }
    ```  
- Định nghĩa method muốn bổ sung là một default methods nếu nó là một essential, core methods (phương thức cốt lõi thiết yếu), hoặc static methods nếu nó là một utility methods (phương thức tiện ích). Người sử dụng có các class triển khai interface của bạn đã được bổ sung các methods sẽ không phải sửa đổi hay compile lại chúng để phù hợp với methods bổ dung.  
    ```java
    public interface DoIt {
        void doSomething(int i, double x);
        int doSomethingElse(String s);
        default boolean didItWork(int i, double x, String s) {
            // Method body 
        }
    }
    ```


### 1.5, Default Methods

Khi muốn bổ sung các methods vào một interface, nếu thêm chúng vào interface ban đầu, thì các programmers đã triển khai interface này sẽ phải viết lại các triển khai của họ. Nếu thêm chúng dưới dạng các static methods, thì các programmers sẽ coi chúng là các utility methods (method tiện ích), không phải các essential, core methods (method cốt lõi, thiết yếu).

Các phương pháp mặc định cho phép bạn thêm chức năng mới vào các giao diện của thư viện và đảm bảo tính tương thích nhị phân với mã được viết cho các phiên bản cũ hơn của các giao diện đó.

Khi extend một interface có chứa một default method, có thể:

- Hoàn toàn không cung cấp triển khai cho default method, kế thừa default method với triển khai mặc định được cung cấp.  
- Khai báo lại default method, làm nó trở nên abstract.  
- Cung cấp một triển khai khác cho default method, override triển khai cũ.  


### 1.6, Static Methods

Ngoài các default methods, có thể định nghĩa các static methods trong interface (static method là một method được liên kết với class mà nó được định nghĩa thay vì với bất kỳ object nào. Mỗi instance của class đều chia sẻ các static method của nó). Điều này giúp bạn dễ dàng tổ chức các helper methods trong libraries của mình; bạn có thể giữ các static method cụ thể cho một interface trong interface đó thay vì trong một class riêng biệt.


## 2. Inheritance

Trong Java, các class có thể được dẫn xuất từ các class khác, do đó kế thừa các fields và methods từ các class khác.

- Định nghĩa: Một class có nguồn gốc từ một class khác được gọi là subclass (derived class, extended class hoặc child class). class mà từ đó subclass được dẫn xuất được gọi là superclass (base class hoặc parent class).  
- Ngoại trừ Object, không có superclass, mọi class đều có một và chỉ một direct superclass (kế thừa đơn). Trong trường hợp không có bất kỳ superclass tường minh nào khác, mọi class đều ngầm định là một subclass của Object.  
- Các class có thể được dẫn xuất từ ​​các class được dẫn xuất... và cuối cùng được dẫn xuất từ ​​class trên cùng, Object. Một class như vậy được cho là hậu duệ của tất cả các class trong chuỗi kế thừa kéo dài trở lại Object.  

Ý tưởng về kế thừa rất đơn giản nhưng mạnh mẽ: Khi bạn muốn tạo một class mới và đã có một class bao gồm một số mã mà bạn muốn, bạn có thể dẫn xuất class mới từ class hiện có để có thể sử dụng lại các fields và methods của class hiện có mà không cần phải tự viết (và gỡ lỗi) chúng.

Một subclass kế thừa tất cả các members (fields, methods và các nested class) từ superclass của nó. Các constructor không phải là members, vì vậy chúng không được kế thừa bởi các subclass, nhưng constructor của superclass có thể được gọi từ subclass.


### The Java Platform Class Hierarchy

Class Object, được định nghĩa trong package java.lang, định nghĩa và triển khai hành vi chung cho tất cả các class — kể cả những class mà bạn viết. Trong Java, nhiều class dẫn xuất trực tiếp từ Object, các class khác dẫn xuất từ một số class đó, v.v., tạo thành một hệ thống phân cấp của các class.

Tất cả các class trong Java đều là hậu duệ của Object.

Ở trên cùng của hệ thống phân cấp, Object cung cấp các hành vi chung nhất giữa tất cả các class. Các class ở cuối hệ thống phân cấp cung cấp hành vi chuyên biệt hơn.


### What You Can Do in a Subclass

Một subclass kế thừa tất cả các public và protected members của superclass của nó, bất kể subclass nằm trong package nào. Nếu subclass nằm trong cùng một package với superclass của nó, nó cũng kế thừa các package-private members của superclass. Bạn có thể sử dụng các members được kế thừa nguyên trạng, thay thế chúng, ẩn (hide) chúng hoặc bổ sung chúng bằng các members mới:  

- Các fields được kế thừa có thể được sử dụng trực tiếp, giống như bất kỳ fields nào khác được khai báo trong subclass.  
- Có thể khai báo một field trong subclass cùng tên với một field trong superclass, để ẩn (hide) nó (không khuyến khích).  
- Có thể khai báo các fields mới trong subclass mà không có trong superclass.  
- Các methods được kế thừa có thể được sử dụng trực tiếp như các methods được khai báo trong subclass.  
- Có thể viết một instance method mới trong subclass có cùng signature với một instance method trong superclass, để override nó.  
- Có thể viết một static method mới trong subclass có cùng signature với một static method trong superclass, để ẩn (hide) nó.  
- Có thể khai báo các methods mới trong subclass mà không có trong superclass.  
- Có thể viết một subclass constructor gọi constructor của superclass, hoặc ngầm định hoặc sử dụng keyword super.  


### Private Members in a Superclass

Một subclass không kế thừa các private members của superclass của nó. Tuy nhiên, nếu superclass có các methods để truy cập các private fields của nó, thì những methods này cũng có thể được subclass sử dụng nếu nó kế thừa chúng.  

Một nested class có quyền truy cập vào tất cả các members của enclosing class của nó, kể cả các private members. Do đó, một nested member class được kế thừa bởi một subclass sẽ có quyền truy cập gián tiếp vào tất cả các private members của superclass.

```java
class Outer {
    private int x;
    class Inner { ... }
}

class Sub extends Outer { ... }
```


### Casting Objects

Casting cho thấy việc sử dụng một object của type này thay cho một type khác, mà giữa các types này có sự kế thừa và triển khai.

```java
Object obj = new MountainBike();
```

Trong ví dụ trên, MoutainBike là subclass của Object, tham chiếu tới một instance của MoutainBike được ép kiểu thành Object, đây là *implicit casting*.  

```java
MountainBike myBike = obj;
```

Trong ví dụ trên, sẽ nhận được một compile-time error, vì obj chứa một tham chiếu của Object, compiler không chắc rằng nó sẽ tham chiếu tới một instance của MountainBike. Tuy nhiên, có thể cho compiler biết obj sẽ tham chiếu đến một MountainBike, đây là *explicit casting*:  

```java
MountainBike myBike = (MountainBike) obj;
```

Explicit casting sẽ chèn một runtime check để kiểm tra object có tham chiếu đến một object của MountainBike hay không. Nếu tại runtime obj không tham chiếu đến một object của MountainBike thì một exception sẽ được ném ra.    

**Note**: Có thể sử dụng *instanceof* operator để xác nhận type của một object cụ thể, nhằm tránh runtime exception gây ra do cast operator khi thực hiện *explicit casting*.

```java
if (obj instanceof MountainBike) {
    MountainBike myBike = (MountainBike)obj;
}
```


### 2.1, Multiple Inheritance of State, Implementation, and Type

Một sự khác biệt đáng kể giữa các class và interface là các class có thể có các instance fields trong khi các interface thì không. Ngoài ra, có thể khởi tạo một class để tạo một object, điều mà bạn không thể làm với các interface. 

Một object lưu trữ state của nó trong các fields, được định nghĩa trong các class. Một lý do tại sao Java không cho phép extends nhiều hơn một class là để tránh các vấn đề về *đa kế thừa của state* (multiple inheritance of state), đó là khả năng kế thừa các fields từ nhiều class. Ví dụ: giả sử rằng có thể định nghĩa một class mới extends nhiều class. Khi tạo một object bằng cách khởi tạo class đó, object đó sẽ kế thừa các fields từ tất cả các superclass của class đó. Điều gì sẽ xảy ra nếu các methods hoặc constructor từ các superclass khác nhau khởi tạo cùng một field? Method hoặc constructor nào sẽ được ưu tiên hơn? Bởi vì các interface không chứa các instance fields, không phải lo lắng về các vấn đề do đa kế thừa state.

*Đa kế thừa của sự triển khai* (Multiple inheritance of implementation) là khả năng kế thừa các method definitions từ nhiều class. Các vấn đề nảy sinh với kiểu đa thừa kế này, chẳng hạn như xung đột tên và sự không rõ ràng. Khi compiler của các ngôn ngữ lập trình hỗ trợ kiểu đa kế thừa này gặp phải các superclass chứa các methods có cùng tên, chúng đôi khi không thể xác định member hoặc method nào để truy cập hoặc gọi.

Java hỗ trợ *đa kế thừa của type* (multiple inheritance of type), đó là khả năng của một class để triển khai nhiều hơn một interface. Một object có thể có nhiều type: type của class riêng của nó và type của tất cả các interface mà class triển khai. Điều này có nghĩa là nếu một biến được khai báo là type của một interface, thì giá trị của nó có thể tham chiếu đến bất kỳ object nào được khởi tạo từ bất kỳ class nào triển khai interface. 

Giống như đa kế thừa của sự triển khai, một class có thể kế thừa các triển khai khác nhau của một method đã được định nghĩa (default method) trong các interface mà nó triển khai. Trong trường hợp này, compiler hoặc người dùng phải quyết định sử dụng cái nào.


### 2.2, Overriding and Hiding Methods

#### *2.2.1, Instance Methods*

Một instance method trong subclass có cùng signature (tên, số lượng và kiểu tham số của nó) và kiểu trả về như một instance method trong superclass sẽ override method của superclass.

Khả năng override một method của một subclass cho phép một class kế thừa từ một superclass có hành vi "đủ gần" và sau đó sửa đổi hành vi khi cần thiết. Overriding method có cùng tên, số lượng và kiểu tham số, cũng như return type giống với method mà nó override. Overriding method cũng có thể trả về một subtype của type được trả về bởi overriden method. Subtype này được gọi là *covariant return type* (kiểu trả về đồng biến).

Khi override một method, có thể sử dụng annotation @Override để cho compiler biết bạn định override một method trong superclass. Nếu compiler phát hiện ra rằng method không tồn tại ở một trong các superclass, thì nó sẽ tạo ra một lỗi.

Xem chi tiết tại [Java specification about instance methods](../../specifications/8_classes-4.md#481-overriding-by-instance-methods).


#### *2.2.2, Static Methods*

Nếu một subclass định nghĩa một static method có cùng signature với một static method trong superclass, thì method trong subclass sẽ ẩn method trong superclass.

Sự khác biệt giữa ẩn một static method và ghi đè một instance method có ý nghĩa quan trọng:

- Phiên bản của overridden instance method được gọi là phiên bản trong subclass.  
- Phiên bản của hidden static method được gọi phụ thuộc vào việc nó được gọi từ superclass hay subclass.  

Xem chi tiết tại [Java specification about static methods](../../specifications/8_classes-4.md#482-hiding-by-class-methods).


#### *2.2.3, Interface Methods*

Các default methods và abstract methods trong các interfaces được kế thừa giống như các instance methods. Tuy nhiên, khi các supertypes của một class hoặc interface cung cấp nhiều default methods với cùng signature, thì Java compiler tuân theo các quy tắc kế thừa để giải quyết xung đột tên. Các quy tắc này được thúc đẩy bởi 2 nguyên tắc sau:

- Các instance methods được ưu tiên hơn các default methods của interface.  
- Các methods đã bị override bởi các ứng cử viên khác sẽ bị bỏ qua. Trường hợp này có thể phát sinh khi các supertypes có chung một tổ tiên.  

Nếu 2 hoặc nhiều default methods được định nghĩa độc lập xung đột hoặc một default method xung đột với một abstract method, thì Java compiler sẽ tạo ra compile-time error. Bạn phải override tường minh các methods của các supertypes.

```java
public interface OperateCar {
    default public int startEngine(EncryptedKey key) {
        // Implementation
    }
}
public interface FlyCar {
    default public int startEngine(EncryptedKey key) {
        // Implementation
    }
}
public class FlyingCar implements OperateCar, FlyCar {
    public int startEngine(EncryptedKey key) {
        FlyCar.super.startEngine(key);
        OperateCar.super.startEngine(key);
    }
}
```

Các instance methods được thừa kế từ các class có thể override các abstract methods của các interfaces.

**Note**: Các static methods trong các interfaces không bao giờ được kế thừa.

Xem chi tiết tại [Java specification about Interface](../../specifications/9_interfaces-1.md).


#### *2.2.3, Modifiers*

Access modifier của một overriding method có thể cao hơn, nhưng không được phép thấp hơn, so với access của overriden method.

Sẽ gặp compile-time error nếu cố gắng thay đổi một instance method trong superclass thành một static method trong subclass và ngược lại.

**Note**: Trong một subclass, có thể overload các methods được kế thừa từ superclass. Các methods được overload như vậy không hide các static methods hay override các instance methods của superclass — chúng là các methods mới, duy nhất cho subclass.


### 2.3, Polymorphism

Định nghĩa đa hình đề cập đến một nguyên tắc trong sinh học, trong đó một sinh vật hoặc loài có thể có nhiều dạng hoặc nhiều giai đoạn khác nhau. Nguyên tắc này cũng có thể được áp dụng cho OOP. Các subclass của một class có thể định nghĩa các hành vi độc đáo của riêng chúng nhưng vẫn chia sẻ một số chức năng giống nhau của superclass.

JVM gọi method thích hợp cho object được tham chiếu trong mỗi biến. Nó không gọi method được định nghĩa bởi type của biến. Hành vi này được gọi là *virtual method invocation* và thể hiện một khía cạnh của tính năng đa hình quan trọng trong Java.

Xem chi tiết tại [Java specification about overriding methods](../../specifications/8_classes-4.md#48-inheritance-overriding-and-hiding).


### 2.4, Hiding Fields

Trong một class, một field trùng tên với một field trong superclass sẽ ẩn field của superclass, ngay cả khi type của chúng khác nhau. Trong subclass, field trong superclass không thể được tham chiếu bằng simple name của nó. Thay vào đó, field phải được truy cập thông qua super. Tuy nhiên, không nên ẩn các field vì nó làm cho mã khó đọc.


### 2.5, Using the Keyword super

Nếu method overrides một trong các method của superclass của nó, thì có thể gọi overridden method (method bị override) thông qua việc sử dụng keyword super. Cũng có thể sử dụng super để tham chiếu đến một hidden field (field bị ẩn). 

Cũng có thể sử dụng super để gọi constructor của superclass, invocation của một superclass constructor phải là dòng đầu tiên trong subclass constructor.

**Note**: Nếu một constructor không gọi tường minh một superclass constructor, thì Java compiler sẽ tự động chèn một lời gọi đến no-argument constructor của superclass. Nếu superclass không có no-argument constructor, sẽ nhận được một compile-time error. *Object* có một constructor như vậy, bởi vậy nếu Object là superclass duy nhất, thì không có vấn đề.

Nếu một subclass constructor gọi một constructor của superclass của nó, dù tường minh hay ngầm định, thực tế, sẽ có một chuỗi các constructors được gọi, trở lại đến constructor của Object. Đây được gọi là *constructor chaining*.


### 2.6, Object as a Superclass

Object class, trong package java.lang, nằm ở trên cùng của biểu đồ cây phân cấp class. Mọi class đều là hậu duệ, trực tiếp hoặc gián tiếp, của Object class. Mọi class đều kế thừa các instance methods của Object, có thể ghi đè một số chúng bằng mã dành riêng cho class của bạn.

Các methods được thừa kế từ Object bao gồm:  

- *protected Object clone() throws CloneNotSupportedException*  
    Creates and returns a copy of this object.  
- *public boolean equals(Object obj)*  
    Indicates whether some other object is "equal to" this one.  
- *protected void finalize() throws Throwable*  
    Called by the garbage collector on an object when garbage collection determines that there are no more references to the object.  
- *public final Class getClass()*  
    Returns the runtime class of an object.  
- *public int hashCode()*  
    Returns a hash code value for the object.  
- *public String toString()*  
    Returns a string representation of the object.  

Các *notify*, *notifyAll*, và *wait* methods của Object đóng một vai trò trong việc đồng bộ hóa các hoạt động của các threads chạy độc lập trong một program, bao gồm:

- public final void notify()  
- public final void notifyAll()  
- public final void wait()  
- public final void wait(long timeout)  
- public final void wait(long timeout, int nanos)  


#### *2.6.1, The clone() Method*

Nếu một class, hoặc một trong các superclass của nó, implements Cloneable interface, thì có thể sử dụng clone() method để tạo một bản sao từ một object hiện có:

```java
aCloneableObject.clone();
```

Sự triển khai method này của Object kiểm tra xem liệu object gọi clone() có implements Cloneable interface hay không, nếu object không triển khai, sẽ ném một *CloneNotSupportedException* exception.

Nếu class của bạn override clone() method của Object, nó phải được khai báo như sau:

```java
protected Object clone() throws CloneNotSupportedException
// or:
public Object clone() throws CloneNotSupportedException
```

Nếu object gọi clone() triển khai Cloneable interface, thì sự triển khai clone() method của Object sẽ tạo một object của cùng class với object ban đầu và khởi tạo các member variables của object mới có cùng values với các member variables tương ứng của object ban đầu.

Đối với một số class, hành vi mặc định của clone() method của Object hoạt động tốt. Tuy nhiên, nếu một object chứa một reference tới một external object, cần override clone() để có hành vi chính xác. Nếu không, một thay đổi trong external object thực hiện bởi object ban đầu cũng sẽ gây ảnh hưởng bản clone của nó. Điều này có nghĩa object ban đầu và bản clone của nó không độc lập — để làm chúng thực sự độc lập, phải override clone() của Object.


#### *2.6.2, The equals() Method*

equals() method so sánh 2 objects có bằng nhau hay không, và returns true nếu chúng bằng nhau. equals() method được cung cấp trong Object class sử dụng == operator để xác định 2 objects có bằng nhau hay không. Đối với *primitive data types* và các *String literals*, điều này cho kết quả chính xác. Tuy nhiên, đối với các objects kết quả sẽ KHÔNG chính xác, nó kiểm tra xem các *object references* có bằng nhau hay không — nghĩa là, kiểm tra các references được so sánh có phải cùng một object.

Để kiểm tra 2 objects có bằng nhau hay không theo nghĩa tương đương (chứa cùng thông tin), phải override equals() method của Object:

```java
public class Book {
    String ISBN;
    
    public String getISBN() { 
        return ISBN;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Book)
            return ISBN.equals((Book)obj.getISBN()); 
        else
            return false;
    }
}
```

**Note**: Nếu bạn override equals(), cũng phải override hashCode().


#### *2.6.3, The finalize() Method*

Object class cung cấp một callback method, finalize(), được gọi trên một object khi nó trở thành rác. Sự triển khai finalize() của Object không làm bất cứ điều gì — bạn có thể override finalize() để thực hiện dọn dẹp, chẳng hạn như giải phóng tài nguyên.

finalize() method có thể được gọi tự động bởi system, nhưng khi nào nó được gọi, hoặc thậm chí việc nó được gọi, thì không chắc chắn. Do đó, đừng dựa vào method này để thực hiện việc dọn dẹp. Ví dụ: nếu bạn không đóng file descriptors trong mã của mình sau khi thực hiện I/O và mong đợi finalize() đóng chúng cho bạn, bạn có thể mất file descriptors. Thay vào đó, hãy sử dụng một try-with resources statement để tự động đóng các resources của ứng dụng. 


#### *2.6.4, The getClass() Method*

KHÔNG thể override getClass() method của Object.

getClass() method trả về một Class object mà có các methods có thể sử dụng để lấy thông tin về class của một object, chẳng hạn như tên của nó - getSimpleName(), superclass của nó - getSuperclass(), và các interfaces nó  implements - getInterfaces(). Ví dụ:

```java
void printClassName(Object obj) {
    System.out.println("The object's class is " + obj.getClass().getSimpleName());
}
```

Class class, trong java.lang package, có một số lượng lớn các methods (hơn 50). Ví dụ, có thể kiểm tra class có phải một annotation - isAnnotation(), một interface - isInterface(), hay một enumeration - isEnum(); hay cũng có thể lấy thông tin của class như các fields - getFields(), hay các methods - getMethods(), ...


#### *2.6.5, The hashCode() Method*

Giá trị được trả về bởi hashCode() method là hash code của object, là một *integer value* được tạo bởi một thuật toán băm.

Theo định nghĩa, nếu 2 objects bằng nhau, thì hash code của chúng cũng bằng nhau. Nếu bạn override equals() method của Object, thay đổi cách 2 objects được cho là bằng nhau và sự triển khai hashCode() của Object sẽ không còn hợp lệ. Do đó, nếu override equals() method, cũng phải override hashCode() method.


#### *2.6.6, The toString() Method*

Nên override toString() method trong các class của bạn.

toString() method của Object returns một String biểu diễn của object, điều này hữu ích cho việc debug.


### 2.7, Writing Final Classes and Methods

Có thể khai báo một số hoặc tất cả các methods củ một class là *final*. Sử dụng final keyword trong một method declaration để chỉ ra rằng method KHÔNG thể bị override bởi các subclass.

Một số methods của Object class là final như getClass, notify, notifyAll, wait.

Có thể khai báo một method là final nếu muốn implementation không thể bị thay đổi và nó rất quan trọng đối với state nhất quán của object.

```java
class ChessAlgorithm {
    enum ChessPlayer { WHITE, BLACK }
    
    final ChessPlayer getFirstPlayer() {
        return ChessPlayer.WHITE;
    }
}
```

Các methods được gọi từ constructors thường được khai báo final. Nếu một constructor gọi một non-final method, một subclass có thể định nghĩa lại method đó với các kết quả không mong muốn.

**Note**: Có thể khai báo toàn bộ class là final. Một class được khai báo final được kế thừa, nên không thể có subclass. Điều này rất hữu ích, như khi tạo một immutable class như String class.


### 2.8, Abstract Methods and Classes

Một abstract class là một class được khai báo abstract — nó có thể hoặc không bao gồm các abstract methods. Các abstract classes không thể được khởi tạo, nhưng chúng có thể có các subclass.

Một abstract method là một method được khai báo mà không có một implementation.

Nếu một class bao gồm các abstract methods, thì bản thân class đó phải được khai báo abstract.

Khi một abstract class được extends, các subclass thường cung cấp các implementations cho tất cả các abstract methods trong superclass. Tuy nhiên, nếu không cung cấp thì subclass cũng phải là abstract.

**Note**: Các methods trong một interface không được khai báo là default hay static sẽ ngầm định là abstract, vì vậy có thể sử dụng abstract modifier với các interface methods, nhưng không cần thiết.


#### Abstract Classes Compared to Interfaces

Các abstract class tương tự như các interface, KHÔNG thể khởi tạo chúng và chúng có thể chứa hỗn hợp các method được khai báo có hoặc không có một triển khai. Tuy nhiên, với các abstract class, có thể khai báo các fields không phải là các static final fields, đồng thời có thể định nghĩa các concrete methods là public, protected hay private. Với interface, tất cả các fields tự động là public static final fields và tất cả các methods đều là public. Ngoài ra, chỉ có thể extends một class, cho dù nó có phải abstract hay không, trong khi có thể implements các interface với số lượng bất kỳ.

Nên sử dụng abstract class hay interface?

- Cân nhắc sử dụng *abstract class* trong các trường hợp sau:  
    + Muốn chia sẻ code giữa một số class liên quan chặt chẽ.  
    + Mong đợi các class extends abstract class có nhiều methods hay fields chung, hay yêu cầu các access modifiers khác public.  
    + Muốn khai báo các non-static hay non-final fields. Điều này cho phép định nghĩa các methods có thể truy cập và sửa đổi các state của object mà chúng thuộc về.  
- Cân nhắc sử dụng *interface* trong các trường hợp sau:  
    + Mong đợi các class không liên quan triển khai interface.  
        *VD: Các interfaces Comparable và Cloneable được triển khai bởi nhiều class không liên quan.*  
    + Muốn chỉ định các hành vi của một data type cụ thể, nhưng không quan tâm ai sẽ triển khai các hành vi của nó.  
    + Muốn tận dụng đa kế thừa của type.  

*Ví dụ: AbstractMap là một abstract class trong JDK, là một bộ phận của Collections Framework. Các subclass của nó (bao gồm HashMap, TreeMap, và ConcurrentHashMap) chia sẻ các methods (bao gồm get, put, isEmpty, containsKey, và containsValue) mà AbstractMap định nghĩa.*

*Ví dụ: HashMap là một class trong JDK, implements một số interfaces Serializable, Cloneable, và Map<K, V>. Một instance của HashMap có thể được clone, serializable (nghĩa là có thể được chuyển đổi thành một byte stream), và có chức năng của một map. Ngoài ra, Map<K, V> interface đã được thêm nhiều default methods như merge và forEach mà các class cũ đã triển khai interface này không phải định nghĩa.*

Note: Nhiều software libraries sử dụng cả các abstract classes và các interfaces, *ví dụ như HashMap class implements một vài interfaces và cũng extends abstract class AbstractMap*.  


#### When an Abstract Class Implements an Interface

Có thể định nghĩa một class không triển khai tất cả các abstract methods của interface, trong trường hợp class này là abstract.

#### Class Members

Một abstract class có thể có các static fields và static methods. Có thể sử dụng những static members này với một class reference (vd, AbstractClass.staticMethod()) như bất kỳ class nào khác.