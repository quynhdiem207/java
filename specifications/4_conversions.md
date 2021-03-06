## 1. Conversions (chuyển đổi)  

Mọi expression trong Java hoặc không có kết quả hoặc có một type được suy ra (*deduced type*) tại compile time.

Khi một expression xuất hiện trong hầu hết context, nó phải *compatible* (tương thích) với type được mong đợi (*target type*) trong context đó.

Khả năng tương thích của một expression với context xung quanh được tạo điều kiện theo 2 cách:  

- Đối với một số expression được gọi là *poly expression*, *deduced type* (type suy luận) có thể bị ảnh hưởng bởi *target type*. Vì vậy, cùng một expression có thể có type khác nhau trong các context khác nhau.  
- Sau khi type được suy luận, đôi khi có thể thực hiện *implicit conversion* (chuyển đổi ngầm) từ type của expression sang target type.  

Nếu cả 2 cách đều không thể tạo ra type thích hợp, sẽ xảy ra *compile-time error*.  

Các loại *conversions* trong Java:  

- Identity conversions  
- Widening primitive conversions  
- Narrowing primitive conversions  
- Widening reference conversions  
- Narrowing reference conversions  
- Boxing conversions  
- Unboxing conversions  
- Unchecked conversions  
- Capture conversions  
- String conversions  
- Value set conversions  

```java
// Example: Conversions In Various Contexts
class Test {			
    public static void main(String[] args) {

        // if without the cast operator, this would be a compile-time error,
        int i = (int)12.5f; // Casting conversion + narrowing conversion
        
        System.out.println("(int)12.5f==" + i); // String conversion

        float f = i; // Assignment conversion + widening conversion

        // Numeric promotion of i's value to type float. 
        // After promotion, the operation is float*float:
        f = f * i;

        // Numeric promotion of f's value to type double, 
        // needed because the method Math.sin accepts only a double argument:
        double d = Math.sin(f); // Invocation conversion
    }
}
```


## 1.1, Identity Conversion  

Cho phép chuyển đổi từ một type sang cùng type đó đối với bất kỳ type nào.  

```java
class Test {			
    public static void main(String[] args) {
        int a = 2;
        int b = (int) a;
    }
}
```


## 1.2, Widening Primitive Conversion

*Widening Primitive Conversion* là các chuyển đổi trên *primitive type* sau:  

- byte  -> short, int, long, float, double  
- short -> int, long, float, double  
- char  -> int, long, float, double  
- int   -> long, float, double  
- long  -> float, double  
- float -> double  

*Widening Primitive Conversion* có thể mất thông tin độ lớn tổng thể (overall magnitude) của value được chuyển đổi do mất một số bit.  

Một số trường hợp không làm mất thông tin, bảo toàn độ chính xác:  

- integral type     -> integral type khác  
- byte, short, char -> floating-point type  
- int               -> double  
- float             -> double trong một strictfp expression  

Các chuyển đổi có thể làm mất thông tin:  

- int -> float,  
- long -> float,  
- long -> double,  
- float -> double không phải stricfp  

Mặc dù có thể xảy ra mất thông tin, nhưng *Widening Primitive Conversion* không bao giờ dẫn đến run-time exception.  

```java
class Test {
    public static void main(String[] args) {
        int big = 1234567890; // 1234567890
        float approx = big;   // 1.23456794E9 (float value không chính xác đến 9 chữ số có nghĩa)
        System.out.println(big - (int)approx); // -46
    }
}
```


## 1.3, Narrowing Primitive Conversion

*Narrowing Primitive Conversion* là các chuyển đổi trên *primitive type* sau:  

- short     -> byte, char  
- char      -> byte, short  
- int       -> byte, short, char  
- long      -> byte, short, char, int  
- float     -> byte, short, char, int, long  
- double    -> byte, short, char, int, long, float  

*Narrowing Primitive Conversion* có thể mất thông tin do mất một số bit.  

*Narrowing Primitive Conversion* của *floating-point value* -> *integral type* T diễn ra theo 2 bước:  

- Bước 1: floating-point value được convert thành long nếu T là long; hoặc được convert thành int nếu T là byte, short, char, int.  
    + Nếu floating-point value là NaN, kết quả là int or long 0,  
    + Nếu floating-point value không phải một Infinity, sẽ được làm tròn thành integer value:  
        + Nếu T là long, và integer value có thể biểu diễn dưới dạng long, thì kết quả là long value,  
        + Ngược lại, nếu integer value có thể biểu diễn dưới dạng int, thì kết quả là int value,  
    + Nếu không sẽ xảy ra 2 trường hợp:  
        + Nếu value quá nhỏ (số âm lớn or -Infinity), kết quả sẽ là giá trị nhỏ nhất có thể biểu diễn của int or long,  
        + Nếu value quá lớn (số dương lớn or +Infinity), kết quả sẽ là giá trị lớn nhất có thể biểu diễn của int or long,  

- Bước 2:  
    + Nếu T là int or long, thì kết quả là kết quả của bước 1,  
    + Nếu T là byte, short, char, thì kết quả là kết quả của narrowing conversion thành type T của kết quả của bước 1,  

Mặc dù có thể xảy ra overflow, underflow, or mất độ chính xác, nhưng *Narrowing Primitive Conversion* không bao giờ dẫn đến run-time exception.  

```java
class Test {
    public static void main(String[] args) {
        float fmin = Float.NEGATIVE_INFINITY;
        float fmax = Float.POSITIVE_INFINITY;

        // long: -9223372036854775808 .. 9223372036854775807
        System.out.println("long: " + (long)fmin + " .. " + (long)fmax);

        // int: -2147483648 .. 2147483647
        System.out.println("int: " + (int)fmin + " .. " + (int)fmax);

        // short: 0 .. -1
        System.out.println("short: " + (short)fmin + " .. " + (short)fmax);

        // char: 0 .. 65535
        System.out.println("char: " + (int)(char)fmin + " .. " + (int)(char)fmax);

        // byte: 0 .. -1
        System.out.println("byte: " + (byte)fmin + " .. " + (byte)fmax);

        // Kết quả cho byte và short làm mất thông tin về dấu và độ lớn của các giá trị số và cũng mất độ chính xác
    }
}
```


## 1.4, Widening and Narrowing Primitive Conversion

Chuyển đổi byte -> char kết hợp cả *Widening and Narrowing Primitive Conversion*. Đầu tiên, byte được convert thành int thông qua *Widening Primitive Conversion*, sau đó kết quả int được convert thành char thông qua *Narrowing Primitive Conversion*.  


## 1.5, Widening Reference Conversion

*Widening Reference Conversion* là chuyển đổi từ bất cứ reference type S nào thành bất cứ reference type T nào, với S là subtype của T.  

*Widening Reference Conversion* không gây ra run-time exception.  

**Note**: *null* không phải một reference type, vì vậy Widening Reference Conversion không tồn tại chuyển đổi từ null thành reference type. Tuy nhiên, nhiều conversion cho phép null được phép convert tường minh thành reference type.  

```java
public interface PersonManager {
    public void report();
}

public interface StudentManager extends PersonManager {
    public void fee();
}

public class Person {
    public void run() {
        System.out.println("running...");
    }
}

public class Student extends Person implements StudentManager {
    private String name;

    @Override
    public void report() {
        System.out.println("report...");
    }
	
	@Override
    public void fee() {
        System.out.println("fee...");
    }
	
	public void study() {
        System.out.println("studying...");
    }
}

public class Example {
    public static void main(String[] args) {
        Student student = new Student();
        Person person = student; // OK

        StudentManager stuManager = new Student();
        PersonManager perManager = stuManager; // OK
        
        Integer x = 3;
        Long y = (Long) x; // COMPILE-TIME ERROR

		Student[] y = {new Student()};
        Person[] x = y; // OK

        int[] a = {1, 2};
        long[] b = x;   // COMPILE-TIME ERROR
    }
}
```


## 1.6, Narrowing Reference Conversion

Một *Narrowing Reference Conversion* coi các expressions của reference type S như là các expressions của reference type T khác, trong đó S không phải là subtype của T.  

Các cặp types được hỗ trợ đã được định nghĩa trong mục *Allowed Narrowing Reference Conversion*.  

Không giống như *Widening Reference Conversion*, các types không cần phải liên quan trực tiếp với nhau. Tuy nhiên, có những hạn chế nhất định cấm chuyển đổi giữa các cặp types khi nó có thể được chứng minh tĩnh rằng không có value nào có thể thuộc cả hai types.  

Một *Narrowing Reference Conversion* có thể yêu cầu kiểm tra tại runtime để xác thực value của type S có phải là một value hợp lệ của type T, một *ClassCastException* sẽ được ném ra nếu kiểm tra fail.  


### *1.6.1, Allowed Narrowing Reference Conversion*  

Một *Narrowing Reference Conversion* tồn tại từ reference type S sang reference type T, nếu thỏa mãn tất cả các điều kiện sau:  

- S không phải subtype của T,  
- Nếu tồn tại một parameterized type X là supertype của T, và một parameterized type Y là supertype của S, sao cho the erasures của X và Y là giống nhau, thì X và Y không khác biệt.  
    
    *ví dụ: Không tồn tại narrowing reference conversion từ: `ArrayList<String>` -> `ArrayList<Object>` or ngược lại, `ArrayList<String>` -> `List<Object>` or ngược lại. (vì các type parameters String và Object khác biệt)*  
    
- Đáp ứng một trong các trường hợp sau:  
    + S và T là class types, và thỏa mãn |S| <: |T| or |T| <: |S|,  
    + S và T là interface types,  
    + S là class type, T là interface type, và S không phải final class,  
    + S là class type, T là interface type, và S là final class mà implement interface T,  
    + S là interface type, T là class type, và T không phải final class,  
    + S là interface type, T là class type, và T là final class mà implement interface S,  
    + S là class type Object or interface type java.io.Serializable or Cloneable (interfaces duy nhất được implemented bởi arrays), and T is an array type.  
    + S là array type SC[], tức là một array of components của type SC; T là array type TC[], tức là một array of components của type TC; và một narrowing reference conversion tồn tại từ SC sang TC.  
    + S là type variable, và một narrowing reference conversion tồn tại từ upper bound của S sang T.  
    + T là type variable, và có một widening reference conversion or một narrowing reference conversion tồn tại từ S sang upper bound của T.  
    + S là một intersection type S1 &...& Sn, và với mọi i (1 ≤ i ≤ n), có một widening reference conversion or một narrowing reference conversion tồn tại từ Si sang T.  
    + T là một intersection type T1 &...& Tn, và với mọi i (1 ≤ i ≤ n), có một widening reference conversion or một narrowing reference conversion tồn tại từ S sang Ti.  


### *1.6.2, Checked and Unchecked Narrowing Reference Conversions*  

Một *Narrowing Reference Conversion* có thể là *Checked* or *Unchecked*. Các thuật ngữ này đề cập đến JVM có thể validate type của conversion hay không.  

Nếu một *Narrowing Reference Conversion* là *unchecked*, thì JVM sẽ không thể hoàn toàn xác thực tính đúng đắn của type, có thể dẫn đến ô nhiễm heap (heap pollution). Để gắn cờ điều này cho lập trình viên, một *Unchecked Narrowing Reference Conversion* sẽ gây ra compile-time *unchecked warning*, trừ khi bị chặn bởi @SuppressWarnings. Ngược lại, nếu một *Narrowing Reference Conversion* là *Checked*, JVM có thể xác thực đầy đủ tính đúng đắn của type, vì vậy không có cảnh báo nào được đưa ra tại compile-time.

Các *Unchecked Narrowing Reference Conversion* là:  

- *Narrowing reference conversion* từ type S sang một *parameterized class or interface type* T là *unchecked*, trừ khi một trong các điều kiện sau là true:  
    + Tất cả các *type arguments* của T là *unbounded wildcards*.  
    + T <: S, and S không có subtype X nào khác T sao cho *type arguments* của X không nằm trong *type arguments* của T.  
- *Narrowing reference conversion* từ type S sang một *type variable* T là unchecked.  
- *Narrowing reference conversion* từ type S sang một *intersection type* T1 &...& Tn là unchecked nếu tồn tại Ti (1 ≤ i ≤ n) sao cho S không phải subtype của Ti và một *narrowing reference conversion* từ S sang Ti là unchecked.  


### *1.6.3, Narrowing Reference Conversions at Run Time*  

Tất cả các *checked narrowing reference conversions* đều yêu cầu kiểm tra xác thực tại runtime. 

Cơ bản, các conversions này đa phần là chuyển sang các class and interface types không tham số hóa.  

Một vài *unchecked narrowing reference conversions* yêu cầu kiểm tra xác thực tại runtime. Điều này phụ thuộc vào conversion là *completely unchecked* hay *partially unchecked*.  

Các thuật ngữ *completely unchecked* và *partially unchecked* đề cập đến khả năng tương thích của type trong conversion khi được xem như các *raw types*. Nếu conversion là "upcast" về mặt khái niệm thì nó là *completely unchecked*, vì conversion là hợp lệ trong non-generic system của JVM. Ngược lại, nếu conversion là "downcast" về mặt khái niệm thì nó là *partially unchecked*.  

*Ví dụ: Conversion từ `ArrayList<String>` sang `Collection<T>` là completely unchecked, bởi vì (raw) type ArrayList là một subtype của (raw) type Collection trong JVM. Ngược lại, conversion từ `Collection<T>` sang `ArrayList<String>` là partially unchecked, bởi vì (raw) type Collection không phải subtype của (raw) type ArrayList trong JVM.*  

Sự phân loại của *unchecked narrowing reference conversions* như sau:  

- Unchecked narrowing reference conversion từ S sang *non-intersection type* T là *completely unchecked* nếu |S| <: |T|. Nếu không thì nó là *partially unchecked*.  
- Unchecked narrowing reference conversion từ S sang *intersection type* T1 &...& Tn là *completely unchecked* nếu với mọi i (1 ≤ i ≤ n), hoặc S <: Ti, hoặc narrowing reference conversion từ S sang Ti là *completely unchecked*. Nếu không thì nó là *partially unchecked*.  

Kiểm tra tính hợp lệ tại runtime cho *checked or partially unchecked narrowing reference conversion* như sau:  

- Nếu value tại runtime là null, conversion được cho phép,  
- Nếu không thì, gọi R là class của object được tham chiếu bởi value, và T là erasure của type được chuyển đổi thành. Sau đó:  
    + Nếu R là class thông thường (không phải array class):  
        + Nếu T là class type, thì R phải là cùng class với T, hoặc subclass của T, nếu không ClassCastException sẽ được ném ra.  
        + Nếu T là interface type, thì R phải implement T, nếu không ClassCastException sẽ được ném ra.  
        + Nếu T là array type, thì ClassCastException sẽ được ném ra.  
    
    + Nếu R là interface:  
        + Nếu T là class type, thì T phải là Object, nếu không ClassCastException sẽ được ném ra.  
        + Nếu T là interface type, thì R phải cùng interface với T, hoặc subinterface của T, nếu không ClassCastException sẽ được ném ra.  
        + Nếu T là array type, thì ClassCastException sẽ được ném ra.  

        *Note*: R không thể là interface khi lần đều áp dụng các rules này cho conversion, nhưng nếu các rules này được áp dụng đệ quy thì R có thể là interface, vì reference value có thể tham chiếu đến array mà element type là interface type.  
    
    + Nếu R là class đại diện cho array type RC[], tức là array of components của type RC:  
        + Nếu T là class type, thì T phải là Object, nếu không ClassCastException sẽ được ném ra.  
        + Nếu T là interface type, thì T phải là java.io.Serializable or Cloneable type (các interfaces duy nhất được implemented bởi arrays), nếu không ClassCastException sẽ được ném ra.  
        + Nếu T là array type TC[], thì RC và TC phải là cùng một primitive type, hoặc RC và TC là các reference types đồng thời được phép áp dụng đệ quy các rules này, nếu không ClassCastException sẽ được ném ra.  

Nếu conversion là chuyển sang intersection type T1 &...& Tn, thì đối với mọi i (1 ≤ i ≤ n), bất cứ run-time check cần thiết cho conversion từ S sang Ti cũng cần thiết cho conversion sang intersection type.  

```java
public interface Member {
    String NAME = "Diêm";
    void showName();
}

public interface Colleague {
    void study();
}

public class Person {
    public void showMessage() {
        System.out.println("message...");
    }
}

public class Employee extends Person implements Member, Colleague {
    private String name;
    
    public Employee() {}
    public Employee(String name) {
        this.name = name;
    }
    
    public void work() {
        System.out.println(name + " is working...");
    }
    
    @Override
    public void showName() {
        System.out.println("I'm " + name);
    }
    
    @Override
    public void study() {
        System.out.println(name + " is studying...");
    }
}

public class Example {
    public static void main(String[] args) {
        Member member = new Employee("Hà");
        Colleague col = new Employee("Hùng");
        
        col = (Colleague) m; // OK

        Person person1 = new Person();
        Person person2 = new Employee();
        Employee employee = null;

        employee = (Employee) person2; // OK
        employee = (Employee) person1; // RUNTIME ERROR

        Long x = 3l;
        Integer y = (Integer) x; // COMPILE-TIME ERROR

        Person[] x = { new Employee() };
        Member[] y = { new Employee() };
        Employee[] z = null;
        z = (Employee[]) x; // RUNTIME ERROR
        z = (Employee[]) y; // RUNTIME ERROR

        long[] z1 = { 1, 2 };
        long[] z2 = z1;         // OK
        int[] z3 = (int[]) z1;  // COMPILE-TIME ERROR
    }
}
```


## 1.7, Boxing Conversion

Java cung cấp các *Wrapper Class* hỗ trợ:  

- Wrap các *primitive values* bên trong object,  
- Cung cấp các APIs để thao tác với các *primitive values*.  

Java cung cấp các *Wrapper Class* đã được định nghĩa bao gồm: *Boolean, Byte, Character, Short, Integer, Long, Float hoặc Double*. Tất cả các class này đều extends class *Number*.  

*Boxing Conversion* coi các expression của *primitive type* như là các expression của *reference type* tương ứng, bao gồm:  

>- Từ type boolean -> class type Boolean  
>- Từ type byte    -> class type Byte  
>- Từ type short   -> class type Short  
>- Từ type char    -> class type Character  
>- Từ type int     -> class type Integer  
>- Từ type long    -> class type Long  
>- Từ type float   -> class type Float  
>- Từ type double  -> class type Double  

Tại runtime, Boxing Conversion thực hiện:  

- Convert *primitive value* p thành reference r của *Wrapper class type* tương ứng, nếu p là:  
    + boolean value -> reference r của class type Boolean, với r.booleanValue() == p,  
    + byte value -> reference r của class type Byte, với r.byteValue() == p,  
    + char value -> reference r của class type Character, với r.charValue() == p,  
    + short value -> reference r của class type Short, với r.shortValue() == p,  
    + int value -> reference r của class type Integer, với r.intValue() == p,  
    + long value -> reference r của class type Long, với r.longValue() == p,  
    + float value -> reference r của class type Float, sao cho:  
        + Nếu p là NaN, thì r.isNaN() == true,  
        + Nếu p không phải NaN, thì r.floatValue() == p,  
    + double value -> reference r của class type Double, sao cho:  
        + Nếu p là NaN, thì r.isNaN() == true,  
        + Nếu p không phải NaN, thì r.doubleValue() == p.  
        
Boxing conversion có thể dẫn đến *OutOfMemoryError* nếu instance mới của Wrapper Class cần được cấp phát và không có đủ bộ nhớ.  


## 1.8, Unboxing Conversion

*Unboxing Conversion* coi các expression của *reference type* như là các expression của *primitive type* tương ứng, bao gồm:  

>- Từ class type Boolean   -> type boolean  
>- Từ class type Byte      -> type byte     
>- Từ class type Short     -> type short    
>- Từ class type Character -> type char     
>- Từ class type Integer   -> type int      
>- Từ class type Long      -> type long     
>- Từ class type Float     -> type float    
>- Từ class type Double    -> type double   

Tại runtime, Unboxing Conversion thực hiện:  

- Convert reference r của *Wrapper class type* thành *primitive value* tương ứng, nếu r là:  
    + reference của class type Boolean -> r.booleanValue(),  
    + reference của class type Byte -> r.byteValue(),  
    + reference của class type Character -> r.charValue(),  
    + reference của class type Short -> r.shortValue(),  
    + reference của class type Integer -> r.intValue(),  
    + reference của class type Long -> r.longValue(),  
    + reference của class type Float -> r.floatValue(),  
    + reference của class type Double -> r.doubleValue(),  
    + null -> throws *NullPointerException*.  

*Ví dụ*:  

```java
public class Example {
    public static void main(String[] args) {
        int year = 2020;

        // Integer number = new Integer(2020);
        // Integer number = Integer.valueOf(year);

        Integer number = year; // Autoboxing
        year = number;         // Unboxing

        number = null;
        year = number; // RUNTIME ERROR (NullPointerException)
        
        // API Chuyển đổi các giá trị sang kiểu number cơ số 10:  
        System.out.println(Integer.parseInt("100", 2)); // 4
        System.out.println(Integer.parseInt("101", 8)); // 65
        System.out.println(Integer.parseInt("101"));    // 101
        System.out.println(Double.parseDouble("12"));   // 12.0
    }
}
```


## 1.9. Unchecked Conversion

Gọi G là một *generic type* với n *type parameters*.  

Có một *Unchecked Conversion* từ *raw class or interface type* G sang bất kỳ *parameterized type* nào có dạng `G<T1, ..., Tn>`.  

Có một *Unchecked Conversion* từ *raw array type* G[]k sang bất kỳ *array type* nào có dạng `G<T1, ..., Tn>`[]k. (Kí hiệu []k cho biết array type có k chiều).  

Việc sử dụng một *Unchecked Conversion* gây ra compile-time *unchecked warning*, trừ khi tất cả các *type arguments* Ti (1 ≤ i ≤ n) là *unbounded wildcards* hoặc warning bị chặn bởi @SuppressWarnings.  


## 1.10. Capture Conversion

*Capture conversion* là type conversion của một *parameterized type*.  

Capture conversion chỉ cần thiết cho type checking tại compile time, vì vậy nó không bao giờ ném một runtime exception.

Gọi G là một *generic type declaration* với n type parameters A1,...,An tương ứng với các ràng buộc U1,...,Un.

Tồn tại một *capture conversion* từ parameterized type `G<T1, ..., Tn>` sang parameterized type `G<S1, ..., Sn>`, sao cho, với 1 ≤ i ≤ n:

- Nếu Ti là một *unbound wildcard* type argument có dạng '?', thì Si là type variable mới có upper bound là Ui [A1 := S1, ..., An := Sn] và lower bound là type null.  

    ```java
    public interface List<T> { ... }
    public class Foo<T extends Appendable> { ... }

    // List<?> ==> List<X> where X <: Object  
    // Foo<?>  ==> Foo<X> where X <: Appendable  
    ```  

- Nếu Ti là một *upper bound wildcard* type argument có dạng '? extends Bi', thì Si là một type variable mới có upper bound là glb(Bi, Ui [A1 := S1, ..., An := Sn]) và có lower bound là type null.  
    + **Note**: glb(V1, ..., Vm) được định nghĩa là V1 &...& Vm.  
    + Xảy ra compile time error nếu đối với hai class bất kỳ (không phải interface) Vi và Vj, Vi không phải là subclass của Vj hoặc ngược lại.  
    
    ```java
    public interface List<T> { ... }
    public class Foo<T extends Appendable> { ... }

    // List<? extends Person>       ==> List<X> where X <: Person  
    // Foo<? extends CharSequence>  ==> Foo<X> where X <: Appendable & CharSequence 
    ```  

- Nếu Ti là *lower bound wildcard* type argument có dạng '? super Bi', thì Si là một type variable mới có upper bound là Ui [A1 := S1, ..., An := Sn] và lower bound là Bi.  
    
    ```java
    public interface List<T> { ... }
    public class Foo<T extends Appendable> { ... }

    // List<? super String>     ==> List<X> where String <: X  
    // Foo<? super FileWriter>  ==> Foo<X> where FileWriter<: X <: Appendable  
    ```  

- Ngược lại, Si = Ti.  
    
    ```java
    public interface List<T> { ... }

    // List<T>  ==>  List<S> where S = X  
    ```  

Capture conversion trên bất kỳ type nào khác với parameterized type hoạt động như một identifier conversion.

Capture conversion không được áp dụng đệ quy.

*Ví dụ*:  

```java
import java.util.ArrayList;
import java.util.List;

class Person {
    String name;
    void study() { ... }
}

class Student extends Person { ... }

class class ExampleWildcard <E extends Person> {
    void test(List<E> lst) {
        for(E ls : lst) {
            ls.study();
        }
    }
}

public class Test {
    public static void main(String[] args) {
        Student student = new Student();
        List<Student> lst = new ArrayList<>();
        lst.add(student);
        
        ExampleWildcard<Student> ex1 = new ExampleWildcard<>();
        ex1.test(lst); // OK

        ExampleWildcard<? super Student> ex2 = new ExampleWildcard<Student>();
        ex2.test(lst); // ERROR -> Do compiler suy luận type argument của class là unknown
        // The method test(List<capture#1-of ? super Student>) in the type 
        // ExampleWildcard<capture#1-of ? super Student> is not applicable for the arguments (List<Student>)
    }
}
```


## 1.11. String Conversion

Bất kỳ type nào đều có thể được convert sang type String bởi String conversion.  

*Primitive value* T trước tiên sẽ được convert sang *reference value*, như thể đưa nó làm argument cho expression tạo instance:  

- Nếu T là boolean, thì sử dụng new Boolean(x).  
- Nếu T là char, thì sử dụng new Character(x).  
- Nếu T là byte, short, or int, thì sử dụng new Integer(x).  
- Nếu T là long, thì sử dụng new Long(x).  
- Nếu T là float, thì sử dụng new Float(x).  
- Nếu T là double, thì sử dụng new Double(x).  

Sau đó, reference value sẽ được convert thành String type:  

- Nếu reference là null, nó sẽ sược convert thành string "null",  
- Nếu không, conversion được thực hiện bằng lệnh gọi method *toString* của object được tham chiếu mà không truyền arguments. Nhưng nếu kết quả của lệnh gọi method *toString* là null, thì string "null" được sử dụng thay thế.  

**Note**: *toString* method được định nghĩa bởi class *Object*. Các Wrapper class ghi đè method này.  


## 1.12. Forbidden Conversions

Bất kỳ conversion nào không được cho phép tường minh đều bị cấm.  


## 1.13. Value Set Conversion

*Value Set Conversion* là quá trình ánh xạ một floating-point value từ tập giá trị này sang tập giá trị khác mà không thay đổi type của nó.

Trong một expression không phải FP-strict, value set conversion cung cấp các lựa chọn để triển khai ngôn ngữ lập trình Java:

- Nếu giá trị là một phần tử của tập giá trị float-extension-exponent, thì việc triển khai có thể ánh xạ giá trị tới phần tử gần nhất của tập giá trị float theo tùy chọn của nó. Việc chuyển đổi này có thể dẫn đến hiện tượng overflow (trong trường hợp giá trị được thay thế bằng một infinity cùng dấu) hoặc underflow (trong trường hợp đó, giá trị có thể mất độ chính xác vì nó được thay thế bằng một số không chuẩn hóa hoặc số 0 cùng dấu).  
    ```java
    float f = 5e-2f;
    ```  

- Nếu giá trị là một phần tử của tập giá trị double-extended-exponent, thì việc triển khai có thể ánh xạ giá trị tới phần tử gần nhất của tập giá trị double theo tùy chọn của nó. Việc chuyển đổi này có thể dẫn đến hiện tượng overflow (trong trường hợp giá trị được thay thế bằng một infinity cùng dấu) hoặc underflow (trong trường hợp đó, giá trị có thể mất độ chính xác vì nó được thay thế bằng một số không chuẩn hóa hoặc số 0 cùng dấu).  
    ```java
    double d = 5e-2;
    ```  

Trong một FP-strict expression, chuyển đổi tập giá trị không cung cấp bất kỳ lựa chọn nào; mọi triển khai phải hoạt động theo cùng một cách:  

- Nếu giá trị thuộc type float và không phải là một phần tử của tập giá trị float, thì việc triển khai phải ánh xạ giá trị đến phần tử gần nhất của tập giá trị float. Việc chuyển đổi này có thể dẫn đến overflow hoặc underflow.  
- Nếu giá trị thuộc type double và không phải là một phần tử của tập giá trị double, thì việc triển khai phải ánh xạ giá trị đến phần tử gần nhất của tập giá trị double. Việc chuyển đổi này có thể dẫn đến overflow hoặc underflow.  