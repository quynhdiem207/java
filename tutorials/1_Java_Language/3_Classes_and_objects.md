# Lession 3. Classes and Objects

## 1. Classes

- [Declaring Classes](../../specifications/8_classes-1.md),  
- [Classe members](../../specifications/8_classes-2.md),  
- [Declaring fields](../../specifications/8_classes-3.md),  
- [Defining Methods](../../specifications/8_classes-4.md),  
- [Defining Constructors](../../specifications/8_classes-5.md#8-constructor-declarations),  
- [Initializing Fields](../../specifications/8_classes-5.md#6-instance-initializers).  


## 2. Objects

### 2.1, Creating Objects

Một class cung cấp bản thiết kế cho các object; một object được tạo từ một class, sử dụng new operator gọi một constructor của class đó.


### 2.2, Using Objects

Có thể sử dụng object để:  

- Tham chiếu đến các fields của một object,  
- Gọi các methods của một object.  


## 2.3, Garbage Collector

JVM có garbage collector định kỳ giải phóng bộ nhớ được sử dụng bởi các object không còn được tham chiếu.


## 3. More on Classes

### 3.1, Using the this Keyword

*this* là một reference tới current object, là object có field đang được truy cập hoặc có method hay constructor đang được gọi.  

Các trường hợp phổ biến sử dụng this là:  

- Truy cập các fields bị che bóng (shadow) bởi các parameters hoặc local variables.  
- Từ trong một constructor gọi một constructor khác trong cùng class.  

Chi tiết xem tại [Java specifications about this keyword](../../specifications/15_expressions-1.md#43-this).


### 3.2, Controlling Access to Members of a Class

Các access modifiers xác định liệu các class khác có thể sử dụng một fields cụ thể hoặc gọi một method cụ thể hay không. Có hai cấp độ kiểm soát truy cập:

- Ở top level: public hoặc package-private (không có access modifier tường minh).  
- Ở member level: public, protected, private hoặc package-private (không có access modifier tường minh).  

Một class có thể được khai báo với public modifier, trong trường hợp đó, class đó được hiển thị cho tất cả các class ở mọi nơi. Nếu một class không có access modifier (mặc định, còn được gọi là package-private), thì nó chỉ hiển thị trong package của chính nó (các package được đặt tên là nhóm của các class liên quan).

Ở member level, cũng có thể sử dụng public modifier hoặc không sử dụng access modifier (package-private) giống như với các top-level class và có cùng ý nghĩa. Đối với các member, có hai access modifier bổ sung: private và protected. private modifier chỉ định rằng member chỉ có thể được truy cập trong class riêng của nó. protected modifier chỉ định rằng member chỉ có thể được truy cập trong package của chính nó (như với package-private) và ngoài ra, bởi một subclass của class của nó trong một package khác.

Bảng sau đây cho thấy quyền truy cập vào các member được cho phép bởi mỗi access modifier:

```
Modifier	Class	Package	Subclass	World
public	      Y	      Y	      Y	          Y
protected	  Y	      Y	      Y	          N
no modifier	  Y	      Y	      N	          N
private	      Y	      N	      N	          N
```

Để tránh xảy ra sai sót khi class được sử dụng bởi programmer khác cần:

- Sử dụng private modifier với một member cụ thể, trừ khi có lý do nào khác.  
- Tránh sử dụng các public fields ngoại trừ các constants.  


### 3.3, Understanding Class Members

#### *3.3.1, Class variables*

Khi một số lượng các object được tạo từ cùng một bản thiết kế class, mỗi object đều có các bản sao riêng biệt của các *instance variables*.

Đôi khi, mong muốn có các biến chung cho tất cả các object. Điều này được thực hiện với *static* modifier. Các fields được khai báo với static modifier được gọi là *static fields* hoặc *class variables*. Chúng được liên kết với class chứ không phải với bất kỳ object nào. Mọi instance của class đều chia sẻ một class variables, biến này ở một vị trí cố định trong bộ nhớ. Bất kỳ object nào cũng có thể thay đổi giá trị của một class variable, nhưng các class variable cũng có thể được thao tác mà không cần tạo một instance của lớp.

Class variables được tham chiếu bởi class name, điều này nói rõ chúng là các class variables. Tuy nhiên cũng có thể tham chiếu đến các static fields với một object reference, nhưng điều này không được khuyến khích vì nó không làm rõ ràng rằng chúng là các class variables.


#### *3.3.2, Class methods*

Các class methods được khai báo với static modifier, chúng nên được gọi với class name, mà không cần tạo một instance của class. Tuy nhiên, cũng có thể tham chiếu đến các static methods với một object reference, nhưng điều này không được khuyến khích vì nó không làm rõ ràng rằng chúng là các class methods.

Các static methods được sử dụng phổ biến để truy cập các static fields.

Các lưu ý khi truy cập các fields:  

- Các instance methods có thể truy cập trực tiếp các instance variables và các instance methods.  
- Các instance methods có thể truy cập trực tiếp các class variables và class methods.  
- Các class methods có thể truy cập trực tiếp các class variables và class methods.  
- Các class methods không thể truy cập trực tiếp các instance variables hoặc instance methods, chúng phải sử dụng object reference. Ngoài ra, các class methods không thể sử dụng this và super keyword vì không có instance nào để tham chiếu đến.  


#### *3.3.3, Constants*

Sử dụng kết hợp *static* modifier với *final* modifier để định nghĩa các *constants*, final modifier chỉ ra rằng giá trị của fieldnày không thể thay đổi.

Theo quy ước, tên của các constant values được viết bằng chữ in hoa. Nếu tên gồm nhiều từ, các từ được phân tách bằng dấu gạch dưới (_).

**Note**: Nếu một primitive type hoặc một String được định nghĩa là một constant và giá trị được biết tại compile-time, thì compiler sẽ thay thế tên constant ở mọi nơi trong code bằng giá trị của nó. Đây được gọi là compile-time constant. Nếu giá trị của constant ở thế giới bên ngoài thay đổi (ví dụ: nếu theo luật định rằng pi thực sự phải là 3,975), sẽ cần phải compile lại bất kỳ class nào sử dụng constant này để có được giá trị hiện tại.


### 3.4, Initializing Fields

Có thể cung cấp một giá trị khởi tạo cho một field khi nó được khai báo. Tuy nhiên, hình thức khởi tạo này có những hạn chế vì tính đơn giản của nó. Nếu quá trình khởi tạo yêu cầu một số logic (ví dụ: xử lý lỗi hoặc vòng lặp for để lấp đầy một mảng phức tạp), thì phép gán đơn giản là không đủ. Các instance variable có thể được khởi tạo trong các constructors, nơi xử lý lỗi hoặc logic khác có thể được sử dụng. Để cung cấp khả năng tương tự cho các class variables, Java bao gồm các *static initialization blocks*.


#### *3.4.1, Static Initialization Blocks*

Một static initialization block là một block thông thường, được đặt trước bởi static keyword:

```java
static {
    // whatever code is needed for initialization goes here
}
```

Một class có thể có bất kỳ số lượng *static initialization blocks* nào và chúng có thể xuất hiện ở bất kỳ đâu trong class body. JVM đảm bảo rằng các static initialization blocks được gọi theo thứ tự xuất hiện trong source code.

Có một giải pháp thay thế cho các static initialization blocks - có thể viết một *private static method*:

```java
class Whatever {
    public static varType myVar = initializeClassVariable();
        
    private static varType initializeClassVariable() {
        // initialization code goes here
    }
}
```

Ưu điểm của các private static method là chúng có thể được sử dụng lại sau này nếu cần khởi tạo lại class variable.

Chi tiết xem tại [Java specification about static initializer](../../specifications/8_classes-5.md#7-static-initializers).


#### *3.4.2, Initializing Instance Members*

Thông thường, mã khởi tạo một instance variable sẽ được đặt trong một constructor. Có 2 lựa chọn thay thế cho việc sử dụng một constructor để khởi tạo các instance variables: initializer blocks và *final methods*.

Các initializer blocks cho các instance variables trông giống như các static initializer blocks, nhưng không có từ khóa static:

```java
{
    // whatever code is needed for initialization goes here
}
```

Java compiler sẽ sao chép các initializer blocks vào mọi constructor. Do đó, cách tiếp cận này có thể được sử dụng để chia sẻ một block giữa nhiều constructors.

Một final method không thể được override trong một subclass. 

```java
class Whatever {
    private varType myVar = initializeInstanceVariable();
        
    protected final varType initializeInstanceVariable() {
        // initialization code goes here
    }
}
```

Điều này đặc biệt hữu ích nếu các subclass có thể muốn sử dụng lại method này. method là final vì việc gọi các method không phải là final trong quá trình khởi tạo instance có thể gây ra sự cố.

Chi tiết xem tại [Java specification about instance initializer](../../specifications/8_classes-5.md#6-instance-initializers).


## 4. Nested Classes

Java cho phép định nghĩa các class bên trong một class khác, được gọi là *nested class*. 

Nested classes được chia làm 2 loại: non-static và static. Non-static nested classes được gọi là các *inner classes*. Nested classes được khai báo static được gọi là các *static nested classes*.

*Non-static nested classes* (inner classes) có quyền truy cập đến các static và non-static members của enclosing class, ngay cả khi chúng được khai báo private. *Static nested classes* chỉ có quyền truy cập đến các static members của enclosing class. 

Khi là một member of của OuterClass, một nested class có thể được khai báo là private, public, protected, hay package private để hạn chế quyền truy cập. (Các top-level class chỉ có thể được khai báo là public hoặc package-private.)

Chi tiết xem tại [Java specification about nested classes](../../specifications/8_classes-1.md#13-inner-classes-and-enclosing-instances) và [Javaspecification about member types](../../specifications/8_classes-5.md#5-member-type-declarations).

Lý do sử dụng các nested class:

- Nhóm các class chỉ được sử dụng ở một nơi, để package được sắp xếp một cách hợp lý hơn,  
- Giúp tăng tính đóng gói,  
- Giúp code dễ đọc và dễ bảo trì hơn.  


### 4.1, Inner Class

Giống với các instance variables và methods, một inner class được liên kết với một instance của enclosing class của nó, và có quyền truy cập trực tiếp bằng simple name vào các fields và methods của object đó. Ngoài ra, bởi vì một inner class được liên kết với một instance, nên bản thân nó không thể định nghĩa bất kỳ static members nào, trừ khi member là một *constant variable* (một constant variable là một biến kiểu nguyên thủy hoặc kiểu String được khai báo final và được khởi tạo bằng compile-time constant expression. Một compile-time constant expression thường là một String hoặc một biểu thức số học có thể được đánh giá tại compile-time).  

Các inner class có thể kế thừa các *static member* mà không phải *constant variable* mặc dù nó không thể khai báo chúng.  

```java
class OuterClass {
    ...
    class InnerClass {
        ...
    }
}
```

Các object là các instance của một inner class tồn tại bên trong một instance của outer class.

Để khởi tạo một inner class, trước tiên phải khởi tạo outer class. Sau đó, tạo inner object bên trong outer object:

```java
OuterClass outerObject = new OuterClass();
OuterClass.InnerClass innerObject = outerObject.new InnerClass();
```

Có 3 loại inner classes gồm: member classes, local classes và anonymous classes.


### 4.3, Static Nested Classes

Giống với các class variables và methods, một static nested class được liên kết với outer class của nó, và không thể tham chiếu trực tiếp bằng simple name đến các instance variables hoặc methods được định nghĩa trong enclosing class của nó: nó chỉ có thể sử dụng chúng thông qua một object reference. 

Các static nested class có thể tùy ý khai báo các static members.  

```java
class OuterClass {
    ...
    static class StaticNestedClass {
        ...
    }
}
```

**Note**: Một static nested class tương tác với các instance members của outer class của nó (và các class khác) giống như bất kỳ top-level class nào khác. Trên thực tế, một static nested class về mặt hành vi là một top-level class đã được lồng trong một top-level class khác để thuận tiện cho việc đóng gói.

Khởi tạo một static nested class theo cách giống như một top-level class:

```java
StaticNestedClass staticNestedObject = new StaticNestedClass();
```


### 4.3, Shadowing

Nếu một declaration (vd: field, parameter name) trong một scope cụ thể (vd: một inner class hoặc method definition) có cùng tên với một declaration khác trong enclosing scope, thì declaration này sẽ che khuất (shadow) declaration của enclosing scope. Không thể tham chiếu đến một declaration bị che khuất (shadow) chỉ bằng simple name của nó.


### 4.4, Local Classes

Các local classes là các class được định nghĩa trong một block.

```java
class Outer {
    void instanceMethod() {
        class Inner() { ... }
    }
    static void staticMethod() {
        class Inner() { ... }
    }
}
```

Một local class có quyền truy cập vào các members của enclosing class của nó. Ngoài ra, một local class có quyền truy cập vào các local variables của enclosing block. Tuy nhiên, một local class chỉ có thể truy cập các local variable được khai báo final. Nhưng, bắt đầu từ Java SE 8, một local class có thể truy cập các local variables và các parameters của enclosing block là final hoặc effectively final. Một local variable hoặc parameter có giá trị không bao giờ thay đổi sau khi nó được khởi tạo, là effectively final.


### 4.5, Anonymous Classes

Các anonymous class cho phép khai báo và khởi tạo một class cùng một lúc. Chúng giống như các local class ngoại trừ việc chúng không có tên. Sử dụng chúng nếu chỉ cần sử dụng một local class một lần.

Trong khi các local class là các class declarations, thì các anonymous class là các expressions, nghĩa là định nghĩa class trong một expression khác.

Cú pháp của một anonymous class expression giống như một class instance creation expression, ngoại trừ việc có một class body theo phía sau, và không thể khai báo constructor.

Một anonymous class expression bao gồm:

- new operator,  
- Tên của một interface để implements hoặc một class để extends.  
- Cặp ngoặc () chứa các arguments cho một constructor, giống như một normal class instance creation expression, Khi implements một interface, không có constructor, vì vậy sẽ sử dụng một cặp ngoặc trống.  
- Một class body, nhưng không thể khai báo constructor, nó sẽ được compiler khai báo ngầm định một default constructor.


### 4.6, Lambda Expressions

Một vấn đề với các anonymous class là nếu việc triển khai anonymous class của bạn rất đơn giản, chẳng hạn như một interface chỉ chứa một method, thì cú pháp của các anonymous class có vẻ khó sử dụng và không rõ ràng. Trong những trường hợp này, bạn thường cố gắng chuyển chức năng làm argument cho một method khác. Lambda expression cho phép bạn làm điều này, coi chức năng như argument method hoặc code dưới dạng data.

Anonymous class cho phép triển khai một class cơ sở hoặc interface mà không cần đặt tên cho nó. Mặc dù điều này thường ngắn gọn hơn so với một class được đặt tên, nhưng đối với các functional interface chỉ có một abstract method, ngay cả một anonymous class cũng có vẻ hơi thừa và rườm rà. Lambda expression cho phép tạo các instance của các functional interface một cách gọn gàng hơn.

Một lambda expression bao gồm:

- Một list các formal parameter ngăn cách bởi dấu phẩy được đặt trong cặp ngoặc ().    
- Ký hiệu mũi tên ->,  
- Một body, có thể là một statement hoặc một block.  

Một lambda expression phải xảy ra trong một assignment context, một invocation context, hoặc một casting context.  

Chi tiết xem tại [Java specifications about lambda expression](../../specifications/15_expressions-3.md#9-lambda-expressions).

#### *4.6.1, Accessing Local Variables of the Enclosing Scope*

Giống với local và anonymous class, các lambda expression có quyền truy cập vào các local variables là final hoặc effectively final của enclosing scope. Tuy nhiên, không giống như các local và anonymous class, lambda expression không tạo scope của riêng nó, vì vậy không tồn tại vấn đề shadowing.

#### *4.6.2, Target Typing*

Để xác định type của một lambda expression, Java compiler sử dụng target type của context hoặc tình huống mà lambda expression được tìm thấy.


#### *4.6.3, Target Types and Method Arguments*

Đối với các method arguments, Java compiler xác định target type với 2 tính năng: overload resolution và type argument inference (suy luận type arguments).

```java
// java.lang.Runnable
public interface Runnable {
    void run();
}

// java.util.concurrent.Callable<V>
public interface Callable<V> {
    V call();
}

public class Test {
    void invoke(Runnable r) {
        r.run();
    }

    <T> T invoke(Callable<T> c) {
        return c.call();
    }

    public static void main() {
        Test test = new Test();
        String s = test.invoke(() -> "done");
            // method được gọi sẽ là invoke(Callable<T>) vì method đó returns một value;
            // không phải method invoke(Runnable).
            // trong trường hợp này, type của lambda expression () -> "done" là Callable<T>.
    }
}
```


### 4.7, Method References

Lambda expression được sử dụng để tạo các anonymous methods. Tuy nhiên, đôi khi, một lambda expression không làm gì khác ngoài việc gọi một method hiện có. Trong những trường hợp đó, việc tham chiếu đến method hiện có theo tên thường rõ ràng hơn. *Method reference expression* cho phép bạn làm điều này.

Method reference expression cho phép tạo các instance của các functional interface một cách gọn gàng hơn.

Có 4 loại method reference expression:  

- Tham chiếu tới một static method.  
- Tham chiếu tới một instance method của một object cụ thể.  
- Tham chiếu tới một instance method của một object tùy ý của một type cụ thể.  
- Tham chiếu tới một constructor.  

Cấu trúc của một method reference expression có dạng: 

```
ExpressionName :: [TypeArguments] Identifier
ReferenceType :: [TypeArguments] Identifier
Primary :: [TypeArguments] Identifier
super :: [TypeArguments] Identifier
TypeName . super :: [TypeArguments] Identifier
ClassType :: [TypeArguments] new
ArrayType :: new
```

*Ví dụ: Sử dụng method reference expression tham chiếu tới ArrayList constructor:*

```java
interface ListFactory {
    <T> List<T> make();
}

ListFactory lf  = ArrayList::new;
List<String> ls = lf.make();
```

Chi tiết xem tại [Java specifications about method reference expression](../../specifications/15_expressions-3.md#10-method-reference-expressions).


### 4.8, When to Use Nested Classes, Local Classes, Anonymous Classes, and Lambda Expressions

Các **nested class** cho phép nhóm một cách hợp lý các class chỉ được sử dụng ở một nơi, tăng việc sử dụng đóng gói và tạo mã dễ đọc và dễ bảo trì hơn. Các local class, anonymos class và lambda expression cũng truyền đạt những ưu điểm này; tuy nhiên, chúng được sử dụng cho các tình huống cụ thể hơn:

- **Local class**: Sử dụng nếu cần tạo nhiều instance của một class, truy cập constructor của nó hoặc giới thiệu một type mới được đặt tên (vd: vì cần gọi các method bổ sung sau này).  
- **Anonymos class**: Sử dụng nếu cần khai báo các fields hoặc methods bổ sung.  
- **Lambda expression**:  
    + Sử dụng nếu đang đóng gói một đơn vị hành vi mà muốn truyền sang mã khác. VD: sử dụng lambda expression nếu muốn một hành động nhất định được thực hiện trên mỗi phần tử của collection.  
    + Sử dụng nếu cần một instance đơn giản của một functional interface. VD: không cần constructor, type được đặt tên, các field hoặc method bổ sung.  
- **Nested class**: Sử dụng nếu yêu cầu tương tự như yêu cầu của local class, muốn cung cấp type khả dụng rộng rãi hơn và không yêu cầu quyền truy cập vào các biến cục bộ hoặc tham số phương thức.  
    + Sử dụng một non-static nested class (hoặc inner class) nếu yêu cầu quyền truy cập vào các non-public fields và method của enclosing instance.  
    + Sử dụng một static nested class nếu không yêu cầu quyền truy cập này.  


## 5. Enum Types

Một enum type là một class type đặc biệt, có một tập các instance đã được định nghĩa trước là các enum constants, không thể tạo thêm instance của nó bằng các constructor tường minh.

Nên sử dụng các enum type bất cứ lúc nào cần biểu diễn một tập hợp hằng số cố định, bao gồm các enum type tự nhiên như các hành tinh trong hệ mặt trời và các tập dữ liệu đã biết tất cả các giá trị có thể có tại compile-time, vd: các lựa chọn trên menu, cờ dòng lệnh, ...  

Enum body cũng có thể chứa các member declarations, constructor, static và instance initializers. Nhưng KHÔNG thể tham chiếu đến các static fields từ constructor, instance initializers, instance variable initializer expressions. Constructor của nó không thể là public, protected, mặc định là private. Nếu không khai báo constructor thì enum sẽ có một default constructor.

Ngoài ra enum type còn có các methods được khai báo ngầm định: public static E[] values(), public static E valueOf(String name), public int ordinal().  

Tất cả các enums ngầm định đều extends java.lang.Enum, nên nó không thể extends bất cứ class nào khác.

```java
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY;
}
```

Tất cả các enums đều extends `Enum<E>` nên chúng đều implements *Comparable* và *Serializable*:

```java
public abstract class Enum<E extends Enum<E>> extends Object
    implements Comparable<E>, Serializable {}
```

Chi tiết xem tại [Java specifications about enum](../../specifications/8_classes-5.md#9-enum-types).