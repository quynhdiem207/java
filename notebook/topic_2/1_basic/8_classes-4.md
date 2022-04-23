# Chapter 8: Classes

## 4. Method Declarations

Method khai báo mã thực thi có thể được gọi, truyền một số lượng cố định các giá trị làm đối số.

```
- MethodDeclaration: {MethodModifier} MethodHeader MethodBody
- MethodHeader:
    + Result MethodDeclarator [Throws]
    + TypeParameters {Annotation} Result MethodDeclarator [Throws]
- MethodDeclarator:  Identifier ( [FormalParameterList] ) [Dims]
- Dims: {Annotation} [] {{Annotation} []}
```


### 4.1, Formal Parameters

*Formal parameters* của một method or constructor, nếu có, được xác định bởi một danh sách các *parameter specifiers* được ngăn cách bởi dấu phẩy. Mỗi *parameter specifier* bao gồm một type (tùy chọn được đứng trước bởi *final* modifier và/hoặc một hoặc nhiều annotations) và một identifier (tùy chọn theo sau bởi brackets []) xác định tên của parameter.

Nếu method or constructor không có formal parameters, thì chỉ một cặp ngoặc đơn xuất hiện trong declaration của method or constructor.

```
- FormalParameterList:
    + ReceiverParameter
    + FormalParameters , LastFormalParameter
    + LastFormalParameter
- FormalParameters:
    + FormalParameter {, FormalParameter}
    + ReceiverParameter {, FormalParameter}
- FormalParameter:   {VariableModifier} Type VariableDeclaratorId
- VariableModifier:  Annotation final
- ReceiverParameter: {Annotation} Type [Identifier.] this
- LastFormalParameter:
    + {VariableModifier} Type {Annotation} ... VariableDeclaratorId
    + FormalParameter
- VariableDeclaratorId: Identifier [Dims]
- Dims: {Annotation} [] {{Annotation} []}
```

*Last formal parameter* của method or constructor là đặc biệt: nó có thể là một variable arity parameter (tham số hiếm bất định), được biểu thị bằng dấu chấm lửng (...) theo sau type, nó chỉ có thể đặt ở cuối cùng trong parameter list.

*Receiver parameter* là một vùng nhớ cú pháp tùy chọn cho một *instance method* hoặc *constructor* của một inner class, nó chỉ có thể đặt ở đầu tiên trong parameter list:

- Đối với *instance method*, receiver parameter đại diện cho *object* đã gọi method.  
- Đối với *constructor* của inner class, receiver parameter đại diện cho *immediately enclosing instance* của object mới được tạo.  

*Receiver parameter* tồn tại để cho phép *type* của object được đại diện được biểu thị trong mã nguồn, để có thể chú thích cho *type*, nó không phải declaration của bất kỳ loại variable nào, không bị ràng buộc với bất kỳ giá trị nào được truyền dưới dạng argument trong một method invocation expression hoặc class instance creation expression, nó không có ảnh hưởng gì tại runtime.

Các quy tắc với type và name của *receiver parameter*:

- Đối với *instance method*, type phải là class or interface mà trong đó method được khai báo, name phải là *this*.  
- Đối với *constructor* của inner class, type phải là class or interface bao bọc trực tiếp type declaration của inner class, name phải là *Identifier.this* trong đó Identifier là simple name của class or interface bao bọc trực tiếp type declaration của inner class.  

Type được khai báo của *formal parameter* phụ thuộc vào nó có phải variable arity parameter (tham số hiếm bất định) hay không:  

- Nếu formal parameter không phải variable arity parameter, thì type được khai báo được chỉ ra bởi *Type* nếu không có cặp ngặc ([]) nào xuất hiện trong *Type* và *VariableDeclaratorId*.  
- Nếu formal parameter là variable arity parameter, thì type được khai báo được xác định là *Array of Type*.  

Khi method or constructor được gọi, các values của *actual argument expressions* khởi tạo các *parameter variables* vừa mới được tạo, trước khi thực thi body của method or constructor được gọi. Identifier xuất hiện trong DeclaratorId có thể sử dụng như simple name trong body của method or constructor để tham chiếu đến formal parameter.

Lời gọi của một *variable arity method* có thể chứa nhiều *actual argument expressions* hơn *formal parameter*. Tất cả các actual argument expressions không tương ứng với các formal parameters đứng trước variable arity parameter, sẽ được đánh giá và kết quả được lưu trữ vào một array mà sẽ được truyền cho lệnh gọi method.

*Ví dụ 1: Receiver parameter*

```java
class Test {
    Test() {}
      // No receiver parameter is permitted in the constructor of a top level class.

    void m(Test this) {}
      // OK: receiver parameter in an instance method

    static void n(Test this) {}
      // Illegal: receiver parameter in a static method                         

    class A {
        A(Test Test.this) {}
          // OK: receiver parameter represents the instance of Test which immediately encloses the instance
          // of A being constructed.

        void m(A this) {}
          // OK: receiver parameter represents the instance of A for which A.m() is invoked.

        class B {
            B(Test.A A.this) {}
              // OK: receiver parameter represents the instance of A which immediately encloses the instance
              // of B being constructed.

            void m(Test.A.B this) {}
              // OK: receiver parameter represents the instance of B for which B.m() is invoked.
        }
    }
}
```

*Note: constructor và instance method của B cho thấy type của receiver parameter có thể được chỉ ra với qualified TypeName; nhưng name của receiver parameter trong một constructor của inner class phải sử dụng simple name của enclosing class.*

*Ví dụ 2: Last formal parameter*

```java
class Test {
    void test(int count, String... languages) {
        System.out.println(count);
        for(String i : languages) {
            System.out.print(i + ", ");
        }
    }

    public static void main(String[] args) {
        new Test().test(3, "English", "French");
    }
}

// Output:
//    3
//    English, French
```


### 4.2, Method Signature

Hai methods or constructors là M và N có cùng signature nếu chúng có cùng *name*, cùng *type parameters* (nếu có), và cùng số lượng, types và thứ tự các *formal parameter*.

Signature của method m1 là một subsignature của signature của method m2 nếu:  

- m2 có cùng signature với m1, or  
- Signature của m1 giống với erasure của signature của m2.  

Hai method signatures m1 và m2 là override-equivalent (tương đương) nếu m1 là subsignature của m2, hoặc m2 là subsignature của m1.  

Sẽ xảy ra compile-time error nếu khai báo 2 methods với override-equivalent signature trong cùng một class, hay chúng có cùng erasure của method signature.  

*Ví dụ: Override-Equivalent Signatures*

```java
abstract class Point {
    int x, y;

    abstract void move(int dx, int dy);                      
    void move(int dx, int dy) { x += dx; y += dy; }          // compile-time error
    int move(int dx, int dy) { x += dx; y += dy; return 1; } // compile-time error

    void list(Collection lst) {}         // compile-time error
    void list(Collection<String> lst) {}
        // both methods 'list(Collection)' and 'list(Collection<String>)' have same erasure

    <T>   T execute(Collection<T> a); // compile-time error
    <S,T> S execute(Collection<S> a);
        // because different signatures, same erasure)
}

class Point2D { int x, y; }
class ColoredPoint extends Point2D { int color; }
class Test {
    static int test(ColoredPoint p) { return p.color; } // OK
    static String test(Point2D p) { return "Point"; }
}
```


### 4.3, Method Modifiers

```
- MethodModifier:
    Annotation public protected private
    abstract static final synchronized native strictfp
```

Sẽ gây ra compile-time error nếu:  

- Method declaration chứa *abstract* keyword và một trong các keywords sau: *private, static, final, native, strictfp, or synchronized*.  
- Method declaration chứa cả *native* và *strictfp* keyword.  


#### *4.3.1, abstract Methods*

*Abstract method declaration* giới thiệu method là một member, cung cấp signature, result, và throws clause (nếu có), nhưng không cung cấp một implementation.  

Một method không phải abstract được gọi là một *concrete method*.

Declaration của một abstract method m phải xuất hiện trực tiếp bên trong abstract class A, trừ khi nó xuất hiện trong enum declaration, nếu không sẽ gây ra compile-time error.

Mọi subclass của A mà không phải abstract phải cung cấp một implementation cho m, nếu không sẽ gây ra compile-time error.

Abstract class có thể override một abstract method bằng cách cung cấp một abstract method declaration khác.

Instance method không phải abstract có thể bị override bởi một abstract method.

*Ví dụ 1: Abstract/Abstract Method Overriding*

```java
class BufferEmpty extends Exception {
    BufferEmpty() { super(); }
    BufferEmpty(String s) { super(s); }
}

class BufferError extends Exception {
    BufferError() { super(); }
    BufferError(String s) { super(s); }
}

interface Buffer {
    char get() throws BufferEmpty, BufferError;
}

abstract class InfiniteBuffer implements Buffer {
    // InfiniteBuffer.get overrides Buffer.get
    public abstract char get() throws BufferError;
}
```

*Ví dụ 2: Abstract/Non-Abstract Method Overriding*

```java
abstract class Point {
    int x, y;

    // Point.toString overrides Object.toString
    public abstract String toString();
}

class ColoredPoint extends Point {
    int color;
    public String toString() {
        // because super.toString() refers to Point.toString, which is abstract and therefore cannot be invoked
        return super.toString() + ": color " + color;  // error
    }
}
```


#### *4.3.2, static Methods*

Method được khai báo *static* được gọi là *class method*.

Sẽ gây ra compile-time error nếu:  

- Sử dụng name của một *type parameter* của bất kỳ declaration bao quanh nào trong header hoặc body của một *class method*.  
- Cố sử dụng keyword *this* or *super* để tham chiếu tới current object trong method body, vì class method luôn được gọi mà không cần tham chiếu tới một object cụ thể.  
- Cố sử dụng tham chiếu tới các *instance variables* và *instance methods* trong method body, vì chúng cần tham chiếu thông qua current object. Tuy nhiên, vẫn có thể tham chiếu tới *member classes* và *member interfaces* trong method body.  


Method không được khai báo static được gọi là *instance method*, hay *non-static method*.

Instance method luôn được gọi với quan hệ thông qua một object, chính là current object mà keywords *this* và *super* tham chiếu đến trong method body.

```java
class Super {
    static String greeting() { return "Goodnight"; }
    String name() { return "Richard"; }
}

class Sub extends Super {
    int age;
    class A {}

    static String greeting() { 
        A a = null;                         // OK
        System.out.println(age);            // compile-time error
        return "Hello, " +  super.name();   // compile-time error
    }
    String name() { 
        return super.name() + "Dick";            // OK 
    }
    String sayGoodnight() {
        return super.greeting() + ", " + name(); // OK
    }
}
```


#### *4.3.3, final Methods*

Method có thể được khai báo *final* để ngăn subclasses override or hide nó.

Sẽ gây ra compile-time error nếu cố override or hide một *final method*.

*Private method* và tất cả các methods được khai báo trực tiếp bên trong một *final class* hoạt động như thể chúng là *final*, vì vậy không thể override chúng.

*Ví dụ:*

```java
final class Point {
    int x, y;
    void move(int dx, int dy) { x += dx; y += dy; }
}

class Test {
    public static void main(String[] args) {
        Point[] p = new Point[100];
        for (int i = 0; i < p.length; i++) {
            p[i] = new Point();
            p[i].move(i, p.length-1-i);
        }
    }
}
```

*Trong vd trên, move method của class Point sẽ chuyển đổi vòng lặp for thành:*

```java
for (int i = 0; i < p.length; i++) {
    p[i] = new Point();
    Point pi = p[i];
    int j = p.length-1-i;
    pi.x += i;
    pi.y += j;
}
```


#### *4.3.4, native Methods*

*Native method* được triển khai bằng platform-dependent code, thường được viết bằng ngôn ngữ lập trình khác chẳng hạn như C. Native method không có phần implementation.

```java
package java.io;
public class RandomAccessFile implements DataOutput, DataInput {
    public native void open(String name, boolean writeable) throws IOException;
    public native int readBytes(byte[] b, int off, int len) throws IOException;
    public native void writeBytes(byte[] b, int off, int len) throws IOException;
    public native long getFilePointer() throws IOException;
    public native void seek(long pos) throws IOException;
    public native long length() throws IOException;
    public native void close() throws IOException;
}
```


#### *4.3.5, strictfp Methods*

Tác dụng của *strictfp* modifier là làm cho tất cả các float hoặc double expression bên trong method body phải là FP-strict một cách tường minh.


#### *4.3.6, synchronized Methods*

*Synchronized method* yêu cầu một monitor trước khi nó thực thi:

- Đối với class (static) method, monitor được liên kết với Class object cho class của method được sử dụng.  
- Đối với instance method, monitor được liên kết với this (object gọi method) được sử dụng.

*Ví dụ 1: synchronized Monitors*

```java
class Test {
    int count;
    synchronized void bump() {
        count++;
    }

    static int classCount;
    static synchronized void classBump() {
        classCount++;
    }
}

// Tương tự như code trên
class BumpTest {
    int count;
    void bump() {
        synchronized (this) { count++; }
    }

    static int classCount;
    static void classBump() {
        try {
            synchronized (Class.forName("BumpTest")) {
                classCount++;
            }
        } catch (ClassNotFoundException e) {}
    }
}
```

*Ví dụ 2: synchronized Methods*

```java
public class Box {
    // Mỗi instance của class Box đều có một instance variable boxContents chứa tham chiếu đến bất cứ object nào
    private Object boxContents;

    // Có thể lấy thứ gì đó ra khỏi box bằng cách gọi get, trả về null nếu box rỗng
    public synchronized Object get() {
        Object contents = boxContents;
        boxContents = null;
        return contents;
    }

    // Có thể đặt một object vào một box bằng cách gọi put, trả về false nếu box đầy.
    public synchronized boolean put(Object contents) {
        if (boxContents != null) return false;
        boxContents = contents;
        return true;
    }
}

// Nếu put và get không phải synchronized,
// và 2 threads đang thực thi chúng cho cùng một instance của Box tại cùng một thời điểm,
// thì code có thể hoạt động sai.
```


### 4.4, Generic Methods

Method là *generic* nếu nó có một hoặc nhiều *type variables*. Các type variables này được gọi là *type parameters* của method.

*Generic method* định nghĩa một tập các methods. Khi gọi generic method có thể không cần cung cấp tường minh các type arguments, vì chúng thường được suy luận.

```java
class Test {
    public <T> void testG(T t) {
    	System.out.println(t);
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.<String>testG("Hi"); // OK
        test.testG("Hello");      // OK
    }
}
```


### 4.5, Method Result

Result của một method declaration khai báo type của value mà method trả về (*return type*), hoặc sử dụng *void* keyword để chỉ ra rằng method không trả về giá trị.

*Return types* có thể khác nhau giữa các methods mà override lẫn nhau nếu các return types là *reference types*.

Gọi method declaration d1 có return type R1 là return-type-substitutable (return type có thể thay thế) cho method d2 với return type R2, nếu thỏa mãn một trong các điều sau:

- Nếu R1 là void thì R2 is void.  
- Nếu R1 là một primitive type thì R2 đồng nhất (giống) với R1.  
- Nếu R1 là một reference type thì cần thỏa mãn một trong các điều sau:  
    + R1, được điều chỉnh phù hợp với type parameters của d2, sẽ là subtype của R2.  
    + R1 có thể được convert thành một subtype của R2 bởi unchecked conversion.  
    + d1 không có cùng signature với d2, và R1 = |R2|.  

```java
class Person {
    public Person getPerson() { return new Person(); }
    public int getRandom() { return 2; }
    public double getRandom(double x) { return 2; }
    Iterable<String> m() { return new ArrayList<>(); }
}

class Student extends Person {
    public Student getPerson() { return new Student(); } // OK
    public Object getPerson() { return new Student(); }  // Error

    public int getRandom() { return 4; }  // OK
    public long getRandom() { return 4; } // Error

    Iterable m() {return new ArrayList<>();}          // OK (unchecked coversion)
    Iterable<Integer> m() {return new ArrayList<>();} // Error
}
```


### 4.6, Method Throws

Một *throws* clause được sử dụng để khai báo bất kỳ *exception classes* đã được kiểm tra rằng statements trong một method or constructor body có thể ném ra.

```
- Throws:            throws ExceptionTypeList
- ExceptionTypeList: ExceptionType {, ExceptionType}
- ExceptionType:
    + ClassType
    + TypeVariable
```

*ExceptionType* được đề cập trong *throws* clause phải là một subtype của *Throwable*, nếu không sẽ xảy ra compile-time error.

*Type variables* được cho phép trong *throws* clause mặc dù chúng không được cho phép trong *catch* clause.

*Ví dụ: Type Variables as Thrown Exception Types*

```java
import java.io.FileNotFoundException;
interface PrivilegedExceptionAction<E extends Exception> { 
    void run() throws E;
} 

class AccessController {
    public static <E extends Exception> 
    Object doPrivileged(PrivilegedExceptionAction<E> action) throws E {
        action.run();
        return "success";
    }
}

class Test {
    public static void main(String[] args) {
        try {
            AccessController.doPrivileged(
                new PrivilegedExceptionAction<FileNotFoundException>() {
                    public void run() throws FileNotFoundException {
                        // ... delete a file ...
                    } 
                }
            ); 
        } catch (FileNotFoundException f) { /* Do something */ }
    }
}
```


### 4.7, Method Body

Method body là khối mã triển khai method, hoặc đơn giản là một dấu chấm phẩy (;) để chỉ ra thiếu implementation.

Method body phải là một dấu chấm phẩy (;) nếu method là *abstract* or *native*, nếu không nó phải là một block.

Nếu một method được khai báo có một *return type*, thì nếu body của method có thể hoàn thành bình thường (không được kết thúc thực thi bởi lệnh *return* hoặc *throw* gây ra việc chuyển quyền điều khiển dẫn đến hoàn thành đột ngột), thì sẽ gây ra compile-time error.

*Ví dụ: Method has a return type, but contain no return statement*

```java
class DizzyDean {
    int pitch() {
        throw new RuntimeException("90 mph?!"); 
    }
}
```


### 4.8, Inheritance, Overriding, and Hiding

Class C kế thừa từ direct superclass của nó tất cả các concrete methods m (cả static and instance) của superclass khi tất cả các điều sau là đúng:

- m là một member của direct superclass của C.  
- m là public, protected, hoặc được khai báo với package access trong cùng package với C.  
- Không có method nào được khai báo trong C có signature là một subsignature của signature của m.  

Class C kế thừa từ direct superclass và các direct superinterfaces của nó tất cả các abstract và default methods m khi tất cả các điều sau là đúng:  

- m là một member của direct superclass hoặc một direct superinterface, D, của C.  
- m là public, protected, hoặc được khai báo với package access trong cùng package với C.  
- Không có method được khai báo trong C có signature là một subsignature của signature của m.  
- Không có concrete method được kế thừa bởi C từ direct superclass của nó có một signature là subsignature của signature của m.  
- Không tồn tại method m' là một member của direct superclass hoặc một direct superinterface, D', của C (m khác m', D khác D'), sao cho m' từ D' overrides declaration của method m.  

**Note**: Class không kế thừa các *static methods* từ các *superinterfaces* của nó.  


#### *4.8.1, Overriding (by Instance Methods)*

Instance method mC được khai báo hoặc kế thừa bởi class C, overrides từ C một method mA khác được khai báo trong class A, chỉ khi tất cả các điều sau là đúng:

- A là một superclass của C.  
- C không thừa kế mA.  
- Signature của mC là một subsignature của signature của mA.  
- Một trong các điều sau là đúng:  
    + mA là public.  
    + mA is protected.  
    + mA được khai báo với package access trong cùng package với C, và C khai báo mC hoặc mA is một member của direct superclass của C.  
    + mA được khai báo với package access và mC overrides mA từ một số superclass của C.  
    + mA được khai báo với package access và mC overrides một method m' từ C (m' khác mC và mA), sao cho m' overrides mA từ một số superclass của C.  

Nếu một *non-abstract method* mC overrides một *abstract method* mA từ một class C, thì mC được gọi là implement mA từ C.  

Instance method mC được khai báo hoặc được kế thừa bởi class C, overrides từ C một method mI khác được khai báo trong một interface I, nếu tất cả các điều sau là đúng:  

- I là một superinterface của C.  
- mI là một abstract or default method.  
- Signature của mC là một subsignature của signature của mI.  

Signature của overriding method có thể khác với overridden method nếu một formal parameter trong một trong số chúng có raw type, trong khi parameter tương ứng trong method kia có một parameterized type.

Khái niệm overriding bao gồm methods override method khác từ một số subclass của class khai báo chúng.  

Nếu một *instance method* overrides một *static method*, thì sẽ gây ra compile-time error.

Method bị overridden có thể được truy cập bằng cách sử dụng method invocation expression có chứa keyword *super*. Qualified name hoặc ép kiểu sang superclass type **không** thể truy cập method bị overridden.

```java
class Point {
    int x = 0, y = 0;
    void move(int dx, int dy) { x += dx; y += dy; }
}

class SlowPoint extends Point {
    int xLimit, yLimit;

    // Khi move method được gọi cho một instance của class SlowPoint,
    // thì overriding definition trong class SlowPoint luôn được gọi,
    // dù tham chiếu tới SlowPoint object được lấy từ biến có type Point.
    void move(int dx, int dy) {
        super.move(limit(dx, xLimit), limit(dy, yLimit));
    }

    static int limit(int d, int limit) {
        return d > limit ? limit : d < -limit ? -limit : d;
    }
}
```


#### *4.8.2, Hiding (by Class Methods)*

Nếu class C khai báo hoặc thừa kế một static method m, thì m được gọi là che giấu (hide) bất kỳ method m' nào, trong đó signature của m là một subsignature của signature của m' trong superclasses và superinterfaces của C, mà nếu không có thể truy cập trong C.

Nếu một static method hides một instance method, sẽ gây ra compile-time error.

**Note**: *static variable* có thể che giấu *instance variable*.

Method bị che giấu có thể được truy cập bằng cách sử dụng một *qualified name*, hoặc sử dụng một method invocation expression có chứa keyword *super*, hoặc *ép kiểu* sang superclass type.

```java
class Super {
    static String greeting() { return "Goodnight"; }
    String name() { return "Richard"; }
}

class Sub extends Super {
    static String greeting() { return "Hello"; }
    String name() { return "Dick"; }
}

class Test {
    public static void main(String[] args) {
        Super sub = new Sub();
        System.out.println(sub.greeting() + ", " + sub.name()); // Goodnight, Dick
        // vì static method bị hidden có thể được gọi qua tham chiếu có type chứa declaration của method.

        Super sup = new Super();
        System.out.println(sup.greeting() + ", " + sup.name()); // Goodnight, Richard
    }
}
```


#### *4.8.3, Requirements in Overriding and Hiding*

*Return type*: Nếu method declaration d1 với return type R1 overrides or hides declaration của another method d2 với return type R2, thì d1 phải là *return-type-substitutable* cho d2.

*throws clause*: Giả sử B là một class or interface, và A là một superclass or superinterface của B, và một method declaration m2 trong B overrides or hides một method declaration m1 trong A. Thì:

- Nếu m2 có một throws clause đề cập đến bất kỳ checked exception types nào, thì m1 phải có một throws clause.  
- Đối với mọi checked exception type được liệt kê trong throws clause của m2, thì cùng exception class đó hoặc một trong các supertypes của nó phải được liệt kê trong erasure của throws clause của m1.  
- Nếu unerased throws clause của m1 không chứa một supertype của mỗi exception type trong throws clause của m2 (được chỉnh thành type parameters của m1 nếu cần), thì sẽ gây ra compile-time unchecked warning.  

*Signature*: Nếu một type declaration T có một member method m1 và tồn tại một method m2 được khai báo trong T or một supertype của T, thì sẽ gây ra compile-time error khi tất cả các điều sau là đúng:

- m1 và m2 có cùng name.  
- m2 có thể truy cập từ T.  
- Signature của m1 không phải một subsignature của signature của m2.  
- Signature của m1 hoặc một số method mà m1 overrides (trực tiếp hoặc gián tiếp) có cùng erasure với signature của m2 hoặc một số method mà m2 overrides (trực tiếp hoặc gián tiếp).  

*Access modifier*: Một overriding or hiding method ít nhất phải được cung cấp access như overridden or hidden method, cụ thể:

- Nếu overridden or hidden method là *public*, thì overriding or hiding method phải là *public*.  
- Nếu overridden or hidden method là *protected*, thì overriding or hiding method phải là *protected* or *public*.  
- Nếu overridden or hidden method có package access, thì overriding or hiding method không thể là private.  

*Ví dụ 1: Covariant Return Types*

```java
class C implements Cloneable { 
    C copy() throws CloneNotSupportedException {
        return (C)clone();
    } 
}

class D extends C implements Cloneable {
    // D.copy() overrides C.copy
    D copy() throws CloneNotSupportedException { // OK
        return (D)clone();
    } 
}
```

*Ví dụ 2: Unchecked Warning from Return Type*

```java
class StringSorter {
    List<String> toList(Collection<String> c) {...}
    Iterable<String> m(Collection x) {...}
}

class Overrider extends StringSorter {
    List toList(Collection c) {...} // unchecked warning
        // because List is not subtype of List<String>

    Iterable m(Collection<String> x) {...} // compile-time error
        // because m(Collection<String>) is not a subsignature of m(Collection)
}
```

*Ví dụ 3: Incorrect Overriding because of throws*

```java
class BadPointException extends Exception {
    BadPointException() { super(); }
    BadPointException(String s) { super(s); }
}

class Point {
    int x, y;
    void move(int dx, int dy) { x += dx; y += dy; }
}

// CASE 1:
class CheckedPoint extends Point {
    // CheckedPoint.move() overrides Point.move()
    void move(int dx, int dy) throws BadPointException { // error 
                // -> Point.move() not declare checked exception
        if ((x + dx) < 0 || (y + dy) < 0)
            throw new BadPointException();
        x += dx; y += dy;
    }
}

// CASE 2:
class CheckedPoint extends Point {
    void move(int dx, int dy) {
        if ((x + dx) < 0 || (y + dy) < 0)
            throw new BadPointException(); // error 
                // -> can't throw a checked exception that not appear in throws clause
        x += dx; y += dy;
    }
}
```

*Ví dụ 4: Erasure Affects Overriding*

```java
// CASE 1: A class cannot have two member methods with the same name and type erasure:
class C<T> {
    T id (T x) {...}
}
class D extends C<String> {
    Object id(Object x) {...} // error
    // because D.id(Object) is a member of D, C<String>.id(String) is declared in a supertype of D, and:
    // - The two methods have the same name, id
    // - C<String>.id(String) is accessible to D
    // - The signature of D.id(Object) is not a subsignature of that of C<String>.id(String)
    // - The two methods have the same erasure
}

// CASE 2: Two different methods of a class may not override methods with the same erasure
class C<T> {
    T id(T x) {...}
}
interface I<T> {
    T id(T x);
}
class D extends C<String> implements I<Integer> {
    public String  id(String x)  {...} // error
    public Integer id(Integer x) {...} // error
    // because D.id(String) is a member of D, D.id(Integer) is declared in D, and:
    // - The two methods have the same name, id
    // - D.id(Integer) is accessible to D
    // - The two methods have different signatures (and neither is a subsignature of the other)
    // - D.id(String) overrides C<String>.id(String) and D.id(Integer) overrides I.id(Integer), 
    //   yet the two overridden methods have the same erasure
}
```


#### *4.8.4, Inheriting Methods with Override-Equivalent Signatures*

Class có thể thừa kế nhiều methods với override-equivalent signatures.

Sẽ xảy ra compile-time error nếu:  

- Class C thừa kế một *concrete method* có signature là override-equivalent với *concrete method* khác được thừa kế bởi C.  
- Class C thừa kế một *default method* có signature là override-equivalent với một *abstract or default method* khác được thừa kế bởi C. Trừ khi tồn tại một non-static method được khai báo hoặc được thừa kế bởi C mà là override-equivalent với hai methods kia.  

Một trong các methods được kế thừa phải là return-type-substitutable (return type có thể thay thế) cho mỗi method được thừa kế khác; nếu không sẽ xảy ra compile-time error.

```java
// CASE 1:
interface Fish  { int getNumberOfScales(); }
interface Piano { int getNumberOfScales(); }

class Tuna implements Fish, Piano {
    // method getNumberOfScales in class Tuna has a name, signature, and return type that matches:
    // - the method declared in interface Fish 
    // - the method declared in interface Piano;
    public int getNumberOfScales() { return 91; } // OK
}

// CASE 2:
interface Fish       { int    getNumberOfScales(); }
interface StringBass { double getNumberOfScales(); }

class Bass implements Fish, StringBass {
    // Không thể khai báo một method getNumberOfScales có signature và return type mà 
    // tương thích với cả 2 methods được khai báo trong interface Fish and trong interface StringBass,
    // Vì một class không thể có nhiều methods với cùng signature và khác primitive return types. 
    // Vì vậy, Không thể để single class mà implement cả interface Fish and interface StringBass.
    public ?? getNumberOfScales() { return 91; } // Error
}

// CASE 3:
interface Member() { double getRandom(); }
class Person { double getRandom() { return Math.random(); } }

class Student extends Person implements Member {} // OK

// CASE 4:
class AB { public int getAge() { return 33; } }
interface AC { default int getAge() { return 22; } }
interface AD { default int getAge() { return 11; } }

public class Default extends AB implements AC, AD {} // OK
public class Default2 implements AC, AD {}           // Error
```


### 4.9, Overloading

Nếu 2 methods của một class (dù cả hai được khai báo trong cùng class, hay cả hai được thừa kế bởi một class, hay một được khai báo và một được thừa kế) có cùng name, nhưng signatures không phải override-equivalent, thì method name được gọi là *overloaded*.

Không có mối quan hệ bắt buộc nào giữa *modifier*, *return types* hay *throws* clauses của hai methods với cùng name, trừ khi signatures của chúng là override-equivalent.

Tại compile time, căn cứ vào số lượng actual arguments (và bất kỳ type arguments tường minh nào), compile-time types và thứ tự của arguments được truyền cho method invocation để xác định signature của method được gọi.

*Ví dụ 1: Overloading*

```java
class Point {
    float x, y;

    // Overloaded move method
    void move(int dx, int dy) { x += dx; y += dy; }
    void move(float dx, float dy) { x += dx; y += dy; }

    // Point does not inherit the toString method of class Object,
    // because that method is overridden by the declaration of the toString method in class Point
    public String toString() { return "(" + x + "," + y +")"; }
}

class Point2D { int x, y; }
class ColoredPoint extends Point2D { int color; }
class Test {
    static int test(ColoredPoint p) {
        return p.color;
    }
    static String test(Point2D p) {
        return "Point";
    }
    public static void main(String[] args) {
        ColoredPoint cp = new ColoredPoint();
        Point2D p = cp;

        int i1 = test(cp);     // OK
        String s1 = test(cp);  // compile-time error

        int i2 = test(p);      // compile-time error
        String s2 = test(p);   // OK
    }
}
```

*Ví dụ 2:  Overloading, Overriding, and Hiding*

```java
class Point {
    int x = 0, y = 0;
    void move(int dx, int dy) { x += dx; y += dy; }
    int getX() { return x; }
    int getY() { return y; }
    int color;
}

class RealPoint extends Point {
    float x = 0.0f, y = 0.0f; // hides Point.x and Point.y

    // overloading
    void move(int dx, int dy) { move((float)dx, (float)dy); } // overrides Point.move(int, int)
    void move(float dx, float dy) { x += dx; y += dy; }

    // overrides Point.getX() and Point.getY()
    int getX() { return (int)Math.floor(x); }
    int getY() { return (int)Math.floor(y); }
}

class Test {
    public static void main(String[] args) {
        RealPoint rp = new RealPoint();
        Point p = rp;
        rp.move(1.71828f, 4.14159f); // invokes RealPoint.move(float, float)
        p.move(1, -1);               // invokes RealPoint.move(int, int)

        show(p.x, p.y);             // (0,0) -> reference to x and y of Point
        show(rp.x, rp.y);           // (2.7182798, 3.14159) -> reference to x and y of RealPoint
        show(p.getX(), p.getY());   // (2, 3) -> invokes RealPoint.getX() and RealPoint.getX()
        show(rp.getX(), rp.getY()); // (2, 3) -> invokes RealPoint.getX() and RealPoint.getX()
    }

    static void show(int x, int y) {
        System.out.println("(" + x + ", " + y + ")");
    }

    static void show(float x, float y) {
        System.out.println("(" + x + ", " + y + ")");
    }
}
```