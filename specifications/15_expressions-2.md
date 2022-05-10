# Chapter 15: Expressions

## 7. Field Access Expressions

Một *field access expression* có thể truy cập một field của một object hoặc array, một tham chiếu đến là giá trị của một expression hoặc keyword super đặc biệt.

```
- FieldAccess:
    Primary.Identifier
    super.Identifier
    TypeName.super.Identifier
```

### 7.1, Field Access Using a Primary

- Field access expression có dạng *Primary.Identifier*, tại runtime kết quả được tính như sau:  
    - Nếu field là static:  
        + Trước tiên Primary expression sẽ được đánh giá. Nếu nó hoàn thành đột ngột thì field access expression hoàn thành đột ngột vì cùng lý do.  
        + Nếu không, kết quả đánh giá của Primary expression bị loại bỏ, chỉ sử dụng type của nó. Kết quả đánh giá field access expression sẽ là giá trị của class variable có tên Identifier trong class hoặc interface mà là type của Primary expression.  

    - Nếu field là non-static:  
        + Trước tiên Primary expression sẽ được đánh giá. Nếu nó hoàn thành đột ngột thì field access expression hoàn thành đột ngột vì cùng lý do.  
        + Nếu không, nếu kết quả đánh giá của Primary expression là null, thì một *NullPointerException* được ném, nếu không kết quả đánh giá field access expression sẽ là giá trị của instance variable có tên Identifier được tìm thấy trong object được tham chiếu bởi Primary expression.  

    Type của Primary expression, không phải class của object thực được tham chiếu đến tại run time, chỉ được sử dụng để xác định field nào sẽ sử dụng.  

*Ví dụ 1. Static Binding for Field Access*

```java
class S           { int x = 0; }
class T extends S { int x = 1; }
class Test1 {
    public static void main(String[] args) {
        T t = new T();
        System.out.println("t.x=" + t.x + when("t", t));
        S s = new S();
        System.out.println("s.x=" + s.x + when("s", s));
        s = t;
        System.out.println("s.x=" + s.x + when("s", s));
    }
    static String when(String name, Object t) {
        return " when " + name + " holds a " + t.getClass() + " at run time.";
    }
}
// Output: t.x=1 when t holds a class T at run time.
//         s.x=0 when s holds a class S at run time.
//         s.x=0 when s holds a class T at run time.

class S           { int x = 0; int z() { return x; } }
class T extends S { int x = 1; int z() { return x; } }
class Test2 {
    public static void main(String[] args) {
        T t = new T();
        System.out.println("t.z()=" + t.z() + when("t", t));
        S s = new S();
        System.out.println("s.z()=" + s.z() + when("s", s));
        s = t;
        System.out.println("s.z()=" + s.z() + when("s", s));
    }
    static String when(String name, Object t) {
        return " when " + name + " holds a " + t.getClass() + " at run time.";
    }
}
// Output: t.z=1 when t holds a class T at run time.
//         s.z=0 when s holds a class S at run time.
//         s.z=1 when s holds a class T at run time.
```

*Ví dụ 2. Receiver Variable Is Irrelevant For static Field Access*

```java
// a null reference may be used to access a class (static) variable without causing an exception
class Test3 {
    static String mountain = "Chocorua";
    static Test3 favorite(){
        System.out.print("Mount ");
        return null;
    }
    public static void main(String[] args) {
        System.out.println(favorite().mountain); // Mount Chocorua
    } 
}
// Mặc dù kết quả của favorite() là null, không hề ném một NullPointerException.
// "Mount " được in ra chứng minh rằng Primary expression thực sự được đánh giá tại runtime,
// Mặc dù chỉ type của nó, chứ không phải value của nó, được sử dụng để xác định field cần truy cập,
// (vì field mountain là static).
```

### 7.2, Accessing Superclass Members using super

- Field access expression có dạng *super.Identifier* tham chiếu đến field có tên Identifier của current object, nhưng lúc này current object được coi như một instance của superclass của current class.  

- Field access expression có dạng *T.super.Identifier* tham chiếu đến field có tên Identifier của enclosing instance tương ứng với T, nhưng lúc này instance đó được coi như một instance của superclass của T.  

    Nếu current class không phải một inner class của class T hoặc chính bản thân T, thì sẽ gây ra compile-time error.  

Các trường hợp phổ biến sử dụng super là:

- Truy cập các fields của superclass bị ẩn (hide) bởi các fields của subclass.  
- Gọi các methods của superclass bị override bởi các methods của subclass.  
- Gọi các default methods của interface mà class triển khai.  
- Từ trong một constructor gọi superclass constructor.  

*Ví dụ 1. The super Expression*

```java
interface I           { int x = 0; }
class T1 implements I { int x = 1; }
class T2 extends T1   { int x = 2; }
class T3 extends T2 {
    int x = 3;
    void test() {
        System.out.println("x = " + x);                       // x = 3
        System.out.println("super.x = " + super.x);           // super.x = 2
        System.out.println("((T2)this).x = " + ((T2)this).x); // ((T2)this).x = 2
        System.out.println("((T1)this).x = " + ((T1)this).x); // ((T1)this).x = 1
        System.out.println("((I)this).x = "  + ((I)this).x);  // ((I)this).x = 0
    }
}

class Super { int x = 2; }
class Outer extends Super {
    int x = 6;

    class Inner {
        Inner(Outer Outer.this) {
            System.out.println("Outer.this.x" + Outer.this.x);      // Outer.this.x = 6
            System.out.println("Outer.super.x = " + Outer.super.x); // Outer.super.x = 2
        }
    }
}

class Test {
    public static void main(String[] args) {
        new T3().test();
        new Outer().new Inner();
    }
}
```


## 8. Method Invocation Expressions

Một *method invocation expression* được sử dụng để gọi một class hay instance method.

```
- MethodInvocation:
    MethodName ( [ArgumentList] )
    TypeName . [TypeArguments] Identifier ( [ArgumentList] )
    ExpressionName . [TypeArguments] Identifier ( [ArgumentList] )
    Primary . [TypeArguments] Identifier ( [ArgumentList] )
    super . [TypeArguments] Identifier ( [ArgumentList] )
    TypeName . super . [TypeArguments] Identifier ( [ArgumentList] )

- ArgumentList: Expression {, Expression}
```

Giải quyết một method name tại compile time phức tạp hơn việc giải quyết một field name bởi vì method overloading. Gọi một method tại run time cũng phức tạp hơn truy cập một field bởi vì instance method overriding.

Việc xác định method sẽ được gọi bởi một method invocation expression bao gồm một số bước:  

- Compile-time 1: Xác định method name được gọi và class hoặc interface để tìm kiếm các definitions của các methods với tên đó,  
- Compile-time 2: Xác định Method Signature (có thể có nhiều signature phù hợp, signature cụ thể nhất sẽ được chọn),  
- Compile-time 3: Xác định method được chọn có phù hợp hay không (các modifiers),  
- Runtime: Đánh giá method invocation expression bao gồm 5 bước:  
    + Tính toán target reference,  
    + Đánh giá các argument expressions,  
    + Kiểm tra khả năng truy cập của method được gọi,  
    + Định vị method code được thực thi (overriding),  
    + Tạo một frame mới, thực hiện đồng bộ hóa (nếu cần), và chuyển điều khiển cho method code.  

Một method m trong class S nào đó được xác định là một method được gọi.

Bây giờ một *activation frame* mới được tạo, nó chứa *target reference* (nếu có) và các *argument values* (nếu có), cũng như đủ không gian cho các *local variables* và stack cho method được gọi và bất kỳ thông tin nào khác được yêu cầu bởi implementation (stack pointer, program counter, reference tới previous activation frame, và những thứ tương tự). Nếu không có đủ bộ nhớ để tạo một activation frame như vậy, một *StackOverflowError* sẽ được ném.

Activation frame mới được tạo sẽ trở thành current activation frame. Các argument values sẽ được gán cho các parameter variables mới được tạo tương ứng của method, và target reference có sẵn dưới dạng this, nếu có một target reference. Trước khi mỗi argument value được gán cho parameter variable tương ứng, nó phải được thực hiện *invocation conversion*, bao gồm bất kỳ value set conversion bắt buộc nào.

Nếu method m là một native method nhưng native code của implementation cần thiết, chưa được loaded hoặc không thể được liên kết động, thì một UnsatisfiedLinkError được ném.

Nếu method m không phải synchronized, điều khiển được chuyển đến body của method m được gọi.

Nếu method m là synchronized, thì một object phải bị khóa (lock) trước khi chuyển điều khiển. Không thể tạo thêm progress (tiến trình) nào cho đến khi current thread có thể lấy được khóa (lock). Nếu có một target reference, thì target object phải bị khóa; nếu không Class object đại diện cho class S, class của method m, phải bị khóa. Sau đó điều khiển sẽ được chuyển đến body của method m được gọi. object sẽ tự động được mở khóa (unlock) khi việc thực thi body của method hoàn thành, bất kể bình thường hay đột ngột. Hành vi lock và unlock chính xác như body của method được nhúng trong một synchronized statement.

*Ví dụ 1:*

```java
// Cú pháp TypeName.super được overload: 
// Theo thông lệ, TypeName tham chiếu tới enclosing type declaration là một class,
// và target là superclass của class này,
// như thể invocation là một unqualified super trong enclosing type declaration.
class Superclass {
    void foo() { System.out.println("Hi"); }
}

class Subclass1 extends Superclass {
    void foo() { throw new UnsupportedOperationException(); }

    Runnable tweak = new Runnable() {
        void run() {
            Subclass1.super.foo();  // Gets the 'println' behavior
        }
    };
}

// Để hỗ trợ gọi default methods trong các superinterfaces,
// TypeName cũng có thể tham chiếu đến một direct superinterface của current class hoặc interface,
// và target là superinterface đó.
interface Superinterface {
    default void foo() { System.out.println("Hi"); }
}

class Subclass2 implements Superinterface {
    void foo() { throw new UnsupportedOperationException(); }

    void tweak() {
        Superinterface.super.foo();  // Gets the 'println' behavior
    }
}
```

*Ví dụ 2-1. Method Applicability*

```java
// CASE 1:
class Doubler {
    static int two() { return two(1); } // OK, Doubler.two(int) được gọi
    private static int two(int i) { return 2*i;    }
}
class Test extends Doubler {	
    static long two(long j) { return j + j; }

    public static void main(String[] args) {
        System.out.println(two(3));         // OK
            // Test.two(long) được gọi, và đối số 3 được chuyển đổi thành type long,
            // chuyển đổi type int thành long được cho phép trong invocation conversion.

        System.out.println(Doubler.two(3)); // compile-time error
            // bởi vì Doubler.two(int) không thể truy cập được
    }
}

// CASE 2:
class ColoredPoint {
    int x, y;
    byte color;
    void setColor(byte color) { this.color = color; }
}
class Test {
    public static void main(String[] args) {
        ColoredPoint cp = new ColoredPoint();
        byte color = 37;
        cp.setColor(color); // OK, ColoredPoint.setColor(byte) được gọi
        cp.setColor(37);    // compile-time error
            // bởi vì không tìm thấy method thích hợp tại compile-time,
            // literal 37 có type int, và int không thể được convert thành byte bởi invocation conversion.
    }
}

// CASE 3:
class Point { int x, y; }
class ColoredPoint extends Point { int color; }
class Test {
    static void test(ColoredPoint p, Point q) {
        System.out.println("(ColoredPoint, Point)");
    }
    static void test(Point p, ColoredPoint q) {
        System.out.println("(Point, ColoredPoint)");
    }
    public static void main(String[] args) {
        ColoredPoint cp = new ColoredPoint();
        test(cp, cp);  // compile-time error
            // vì có 2 declaration của test có thể áp dụng và đều có thể truy cập được,
            // và không có declaration nào cụ thể hơn declaration còn lại.
            // Do đó, việc gọi method là không rõ ràng.
    }

    // Nếu thêm một method cụ thể hơn 2 method trên sẽ không còn gây lỗi:
    /**
     * static void test(ColoredPoint p, ColoredPoint q) {
     *     System.out.println("(ColoredPoint, ColoredPoint)");
     * }
     */
}
```

*Ví dụ 2-2. Return Type Not Considered During Method Selection*

```java
class Point { int x, y; }
class ColoredPoint extends Point { int color; }
class Test {
    static int test(ColoredPoint p) {
        return p.color;
    }
    static String test(Point p) {
        return "Point";
    }
    public static void main(String[] args) {
        ColoredPoint cp = new ColoredPoint();
        String s = test(cp);  // compile-time error
            // Declaration cụ thể nhất của method test là test(ColoredPoint), 
            // và result type của method này là int, 
            // compile-time error xảy ra vì int không thể được convert thành String bởi assignment conversion.
    }
}
```

*Ví dụ 4.1-1. Target References and static Methods*

```java
// Khi target reference được tính toán, và sau đó giá trị bị loại bỏ vì invocation mode là static,
// reference sẽ không được kiểm tra xem có phải null hay không:
class Test1 {
    static void mountain() {
        System.out.println("Monadnock");
    }
    static Test1 favorite(){
        System.out.print("Mount ");
        return null;
    }
    public static void main(String[] args) {
        favorite().mountain(); // Mount Monadnock
            // OK, favorite() returns null, nhưng NullPointerException không được ném.
    }
}
```

*Ví dụ 4.1-2. Evaluation Order During Method Invocation*

```java
// Khi method invocation expression được dánh giá,
// thì expression biểu thị object đang gọi method,
// được đánh giá trước bất kỳ expression nào trong argument list,
class Test2 {
    public static void main(String[] args) {
        String s = "one";
        if (s.startsWith(s = "two")) // false
            System.out.println("oops");
    }
}
// Khi s.startsWith(s = "two") được đánh giá,
// thì expression s được đánh giá trước argument expression s = "two".
// Do đó, một tham chiếu tới string "one" được ghi nhớ như target reference,
// trước khi local variable s bị thay đổi thànhtham chiếu đến string "two".
// Do đó, startsWith method được gọi bởi target object "one" với argument "two",
// nên kết quả của invocation là false false, vì string "one" không bắt đầu với "two".
```

*Ví dụ 4.4-1. Overriding and Method Invocation*

```java
class Point {
    final int EDGE = 20;
    int x, y;
    void move(int dx, int dy) {
        x += dx; y += dy;
        if (Math.abs(x) >= EDGE || Math.abs(y) >= EDGE)
            clear();
    }
    void clear() {
        System.out.println("\tPoint clear");
        x = 0; y = 0;
    }
}

// 1. subclass ColoredPoint mở rộng method clear() được định nghĩa bởi superclass Point của nó.
//    Nó làm điều này bằng cách override method clear() bởi method của riêng nó,
//    method này gọi clear method của superclass của nó, sử dụng super.clear().
// 2. Sau đó, method clear này của subclass, 
//    sẽ được gọi bất cứ khi nào target object gọi clear method là một ColoredPoint.
//    Ngay cả method move trong Point cũng sẽ gọi clear method của class ColoredPoint,
//    khi class của this là ColoredPoint
class ColoredPoint extends Point {
    int color;
    void clear() {
        System.out.println("\tColoredPoint clear");
        super.clear();
        color = 0;
    }
}

class Test1 {
    public static void main(String[] args) {
        Point p = new Point();
        System.out.println("p.move(20,20):");
        p.move(20, 20);

        ColoredPoint cp = new ColoredPoint();
        System.out.println("cp.move(20,20):");
        cp.move(20, 20);

        p = new ColoredPoint();
        System.out.println("p.move(20,20), p colored:");
        p.move(20, 20);
    }
}

// Output:
//      p.move(20,20):
//              Point clear
//      cp.move(20,20):
//              ColoredPoint clear
//              Point clear
//      p.move(20,20), p colored:
//              ColoredPoint clear
//              Point clear
```

*Ví dụ 4.4-2. Method Invocation Using super*

```java
class T1 {
    String s() { return "1"; }
}
class T2 extends T1 {
    String s() { return "2"; }
}
class T3 extends T2 {
    String s() { return "3"; }
    void test() {
        System.out.println("s()=\t\t"          + s());
        System.out.println("super.s()=\t"      + super.s());
        System.out.println("((T2)this).s()=\t" + ((T2)this).s());
        System.out.println("((T1)this).s()=\t" + ((T1)this).s());
    }
}
// Ép kiểu (casts) sang types T1 và T2 không thay đổi method được gọi,
// vì instance method được gọi là được chọn dựa vào run-time class của object được tham chiếu bởi this.
// Ép kiểu không thay đổi class của một object;
// nó chỉ kiểm tra xem class có tương thích với một type xác định hay không.
class Test2 {
    public static void main(String[] args) {
        T3 t3 = new T3();
        t3.test();
    }
}

// Output:
//      s()=            3
//      super.s()=      2
//      ((T2)this).s()= 3
//      ((T1)this).s()= 3
```

*Ví dụ 4.5-1. Invoked Method Signature Has Different Erasure Than Compile-Time Method Signature*

```java
abstract class C<T> {
    abstract T id(T x);
}
class D extends C<String> {
    String id(String x) { return x; }
}
class Test3 {
    public static void main(String[] args) {
        C c = new D();
        c.id(new Object());  // fails with a ClassCastException
            // erasure của method thực sự được gọi, D.id(),
            // signature của nó khác với compile-time method declaration, C.id().
            // D.id() nhận argument có type String trong khi C.id() nhận argument có type Object.
            // Invocation fails với một ClassCastException trước khi body của method được thực thi.
    }
}
```