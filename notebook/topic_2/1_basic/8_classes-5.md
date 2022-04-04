# Chapter 8: Classes

## 5. Member Type Declarations

*Member class* là class có declaration được đặt trực tiếp trong body của class hoặc interface declaration khác.

*Member interface* là interface có declaration được đặt trực tiếp trong body của class or interface declaration khác.

Nếu một class khai báo một *member type* với một tên nhất định, thì declaration của type đó được gọi là che giấu (hide) bất kỳ và tất cả các declarations của member types với cùng tên trong superclasses và superinterfaces của class đó.

Class kế thừa từ direct superclass và các direct superinterfaces của nó tất cả các *non-private* member types của superclass và các superinterfaces đều có thể truy cập trong class và không bị ẩn (hidden) bởi một declaration trong class đó.

Một class có thể kế thừa 2 hoặc nhiều type declarations với cùng tên, hoặc từ hai interfaces, hoặc từ superclass của nó và một interface. Nếu cố gắng tham chiếu đến class or interface được kế thừa không rõ ràng bằng *simple name* của nó, sẽ gây ra compile-time error, cần sử dụng *qualified name* hoặc *field access expression* có chứa super keyword.

Nếu cùng một *type declaration* được kế thừa từ một interface bằng nhiều paths, thì class or interface đó chỉ được coi là kế thừa một lần. Nó có thể được tham chiếu bằng simple name.


### 5.1, Static Member Type Declarations

*static* keyword có thể thay đổi declaration của một member type C bên trong body của một *non-inner* class or interface T. Tác dụng của nó là khai báo rằng C không phải một *inner class*. Và, C không có *current instance* của T, cũng không có *enclosing instances*.

Nếu một *static class* có chứa một sử dụng của *non-static member* của *enclosing class*, sẽ gây ra compile-time error.

Một *member interface* ngầm định là *static*. Nhưng, declaration của một member interface vẫn được phép chỉ định tường minh *static* modifier một cách dư thừa.

*Note: Không thể khai báo một member interface trong body của một inner class, vì một inner class không thể có các static members ngoại trừ các constant variables.*


## 6. Instance Initializers

Các *instance initializers* được khai báo trong một class, có các quy tắc sau:

- Các instance initializers được thực thi khi một instance của class đó được tạo,  
- Các instance initializers được thực thi trước khi constructor của class đó được thực thi,  
- Nếu class đó có superclass, thì các instance initializers được thực thi sau khi superclass constructor được thực thi,  
- Các instance initializers được thực thi theo thứ tự mà chúng được khai báo.  

**Note**: sẽ gây ra compile-time error nếu:  

- *Return* statement xuất hiện trong một *instance initializer*.  
- *Instance initializer* không thể hoàn thành bình thường.  

*Instance initializers* được phép: tham chiếu đến *current object* qua keyword *this*, sử dụng keyword *super*, và sử dụng bất kỳ *type variables* trong scope.

```java
class Super {
    Super() {
        System.out.println("Super Constructor");
    }
}

class Sub extends Super {
    String name;

    Sub() {
        System.out.println("Sub Constructor");
    }

    // A instance initializer
    {
        name = "Diêm";
        System.out.println("Instance initializer");
    }

    public static void main(String[] args) {
        Sub sub = new Sub();
    }
}

// Output is: 
//    Super Constructor
//    Instance initializer
//    Sub Constructor
```


## 7. Static Initializers

Một *static initializer* được khai báo trong một class, sẽ được thực thi khi class đó được khởi tạo. Cùng với bất kỳ *field initializers* nào cho các *class variables*, các *static initializers* có thể được sử dụng để khởi tạo các *class variables* của class đó.

Các *static initializers* được thực thi theo thứ tự mà chúng được khai báo.

**Note**: sẽ gây ra compile-time error nếu:  

- *Return* statement xuất hiện trong một *static initializer*.  
- *Static initializer* không thể hoàn thành bình thường.  
- *this* hoặc *super* keyword, hoặc bất kỳ *type variables* được khai báo bên ngoài một *static initializer*, được sử dụng bên trong *static initializer* đó.  

```java
class Test {
    static int x;
    
    // A static initializer
    static {
        x = 100;
    }
}
```


## 8. Constructor Declarations

Một *constructor* được sử dụng để tạo một object là một instance của một class.

```
- ConstructorDeclaration: {ConstructorModifier} ConstructorDeclarator [Throws] ConstructorBody
- ConstructorDeclarator:  [TypeParameters] SimpleTypeName ( [FormalParameterList] )
- SimpleTypeName:         Identifier
```

SimpleTypeName trong ConstructorDeclarator phải là simple name của class chứa constructor declaration, nếu không sẽ gây ra compile-time error.

Các *constructor* declarations không phải các members, chúng không bao giờ được thừa kế, do đó không bị ẩn (*hiding*) hay ghi đè (*overriding*).

Các *constructors* được gọi bởi các class instance creation expressions, bởi các conversions và nối chuỗi gây ra bởi toán tử nối chuỗi +, và bởi lời gọi constructor tường minh từ các constructors khác. Quyền truy cập các constructors được quy định bởi các *access modifiers*, vì vậy có thể ngăn việc khởi tạo bằng cách khai báo một constructor không thể truy cập.

Các constructors không bao giờ được gọi bởi các method invocation expressions.

*Ví dụ: Constructor Declarations*

```java
class Point {
    int x, y;
    Point(int x, int y) { 
        this.x = x; 
        this.y = y; 
    }
}
```


### 8.1, Formal Parameters

Các *formal parameters* của một constructor tương tự về syntax và semantics với các formal parameters của một method.

Constructor của một *non-private inner member class* khai báo ngầm định formal parameter đầu tiên là một variable đại diện *immediately enclosing instance* của class đó.  


### 8.2, Constructor Signature

Sẽ gây ra compile-time error nếu:  

- Khai báo 2 constructors với override-equivalent signatures trong một class. 
- Hoặc khai báo 2 constructors có signatures có cùng erasure trong một class.  


### 8.3, Constructor Modifiers

Constructor modifier có thể là một trong các:  

- Annotation,  
- public,  
- protected,  
- private.  

Trong một normal class declaration, một constructor declaration không có access modifiers sẽ có package access.

Không giống với các methods, một constructor không thể là *abstract, static, final, native, strictfp, or synchronized*.  


### 8.4, Generic Constructors

Một constructor là *generic* nếu nó khai báo một hoặc nhiều *type variables*, những type variables này được gọi là type parameters của constructor đó.

Một constructor có thể là generic không phụ thuộc vào việc liệu class mà constructor được khai báo bên trong có phải là generic hay không.

Một generic constructor declaration định nghĩa một tập các constructors. Các type arguments có thể không cần cung cấp tường minh khi một generic constructor được gọi, vì chúng thường có thể được suy luận.


### 8.5, Constructor Throws

*throws* clause cho một constructor tương tự về cấu trúc và hành vi với throws clause cho một method.


### 8.6, The Type of a Constructor

Type của một constructor bao gồm signature của nó và các exception types được đưa ra bởi throws clause của nó.


### 8.7, Constructor Body

Câu lệnh đầu tiên của một constructor body có thể là một lời gọi tường minh của một constructor khác của cùng class hoặc của direct superclass.

Nếu một constructor gọi chính nó trực tiếp hoặc gián tiếp thông qua lời gọi constructor tường minh liên quan đến *this*, sẽ gây ra compile-time error.

Nếu một constructor body không bắt đầu với một lời gọi constructor tường minh và constructor đang được khai báo không phải một bộ phận của class Object nguyên thủy, thì constructor body ngầm định sẽ bắt đầu với lời gọi superclass constructor **"super();"**, một lời gọi constructor không có arguments của direct superclass của nó.

Ngoại trừ các lời gọi constructor tường minh, và việc trả về một giá trị tường minh, thì body của một constructor giống với body của một method.

Một *return* statement có thể được sử dụng trong body của một constructor nếu nó không bao gồm một expression.

*Ví dụ: Constructor Bodies*

```java
class Point {
    int x, y;
    Point(int x, int y) { this.x = x; this.y = y; }
}

class ColoredPoint extends Point {
    static final int WHITE = 0, BLACK = 1;
    int color;

    ColoredPoint(int x, int y) {
        // the constructor ColoredPoint(int, int) invokes
        // the constructor ColoredPoint(int, int, int) of the same class
        this(x, y, WHITE);
    }

    ColoredPoint(int x, int y, int color) {
        // the constructor ColoredPoint(int, int, int) invokes
        // the constructor Point(int, int) of the direct superclass
        super(x, y);
        this.color = color;
    }
}
```


#### 8.7.1, Explicit Constructor Invocations

```
- ExplicitConstructorInvocation:
    + [TypeArguments] this ( [ArgumentList] );
    + [TypeArguments] super ( [ArgumentList] );
    + ExpressionName.[TypeArguments] super ( [ArgumentList] );
    + Primary.[TypeArguments] super ( [ArgumentList] );

- TypeArguments: < TypeArgumentList >
- ArgumentList:  Expression {, Expression}
```

*Explicit constructor invocation statements* được chia làm 2 loại: 

- *Alternate constructor invocations* bắt đầu với keyword *this* (có thể bắt đầu với các type arguments tường minh). Chúng được sử dụng để gọi một constructor thay thế của cùng một class.  
- *Superclass constructor invocations* bắt đầu với keyword *super* (có thể bắt đầu với các type arguments tường minh), hoặc một *Primary expression*, hoặc một *ExpressionName*. Chúng được sử dụng để gọi một constructor của  direct superclass. Chúng được chia nhỏ hơn như sau:  
    + *Unqualified superclass constructor invocations* bắt đầu với keyword super (có thể bắt đầu với các type arguments tường minh).  
    + *Qualified superclass constructor invocations* bắt đầu với một *Primary* expression hoặc một *ExpressionName*. Chúng cho phép một subclass constructor xác định tường minh immediately enclosing instance của object mới được tạo với quan hệ qua direct superclass. Điều này có thể cần thiết khi superclass là một inner class.  

Sẽ gây ra compile-time error nếu:  

- Một explicit constructor invocation statement trong một constructor body tham chiếu đến bất kỳ instance variables, hoặc instance methods, hoặc inner classes được khai báo bên trong class này hoặc bất kỳ superclass nào, hoặc sử dụng this hoặc super trong bất kỳ expression nào.  
- Nếu TypeArguments hiện diện ở bên trái của this or super, và bất kỳ type arguments nào là wildcards.  

*Ví dụ 1: Restrictions on Explicit Constructor Invocation Statements (hạn chế)*

```java
class Point {
    int x, y;
    Point(int x, int y) { this.x = x; this.y = y; }
}

class ColoredPoint extends Point {
    static final int WHITE = 0, BLACK = 1;
    int color;
    ColoredPoint(int x, int y) {
        this(x, y, color);  // compile-time error 
        // because the instance variable color cannot be used by a explicit constructor invocation statement
    }
    ColoredPoint(int x, int y, int color) {
        super(x, y);
        this.color = color;
    }
}
```

*Ví dụ 2: Qualified Superclass Constructor Invocation:*

*ChildOfInner không có enclosing type declaration, bởi vậy một instance của ChildOfInner không có enclosing instance. Tuy nhiên, superclass của ChildOfInner (Inner) có một enclosing type declaration (Outer), và một instance của Inner phải có một enclosing instance của Outer. Enclosing instance của Outer được set khi một instance của Inner được tạo. Do đó, khi tạo một instance của ChildOfInner, nó ngầm định là một instance của Inner, phải cung cấp enclosing instance của Outer thông qua một qualified superclass invocation statement trong constructor của ChildOfInner. Instance của Outer được gọi là immediately enclosing instance của ChildOfInner với quan hệ qua Inner.*

```java
class Outer {
    class Inner {}
}
class ChildOfInner extends Outer.Inner {
    ChildOfInner() { (new Outer()).super(); }
}
```

*Cùng một instance của Outer có thể đóng vai trò là immediately enclosing instance của ChildOfInner với quan hệ qua Inner cho nhiều instances của ChildOfInner. Những instances của ChildOfInner ngầm định được liên kết với cùng một instance của Outer.*

```java
class Outer {
    int secret = 5;
    class Inner {
        int  getSecret()      { return secret; }
        void setSecret(int s) { secret = s; }
    }
}
class ChildOfInner extends Outer.Inner {
    ChildOfInner(Outer x) { x.super(); }
}

public class Test {
    public static void main(String[] args) {
        Outer x = new Outer();
        ChildOfInner a = new ChildOfInner(x);
        ChildOfInner b = new ChildOfInner(x);
        System.out.println(b.getSecret()); // 5
        a.setSecret(6);
        System.out.println(b.getSecret()); // 6
    }
}
```


### 8.8, Constructor Overloading

Overloading của các constructors tương tự như overloading của các methods. Overloading được giải quyết tại compile time bởi mỗi class instance creation expression. 


### 8.9, Default Constructor

Nếu một class không chứa các constructor declarations, thì một *default constructor* sẽ được khai báo ngầm định. Form của default constructor cho một *top level class*, *member class*, hoặc *local class* như sau:  

- Default constructor có khả năng truy cập giống với class.  
- Default constructor không có formal parameters, ngoại trừ trong một *non-private inner member class*, trong đó default constructor ngầm định khai báo một formal parameter đại diện cho immediately enclosing instance của class.  
- Default constructor không có throws clauses.  
- Nếu class đang được khai báo là class Object nguyên thủy, thì default constructor có một body rỗng. Nếu không, default constructor chỉ đơn giản gọi superclass constructor không có arguments.  

Một *anonymous class* không thể có một constructor được khai báo tường minh. Thay vào đó, một anonymous constructor được khai báo ngầm định cho một anonymous class.

Nếu một default constructor được khai báo ngầm định, nhưng superclass không có một constructor không có arguments và không có throws clause mà có thể truy cập được, thì sẽ gây ra compile-time error.

*Ví dụ 1. Default Constructors*

```java
public class Point {
    int x, y;
}

// tương đương với:
public class Point {
    int x, y;

    // default constructor is public because the class Point is public
    public Point() { 
        super(); 
    }
}
```

*Ví dụ 2. Accessibility of Constructors v.Classes*

**Note**: *Default constructor của một class có cùng khả năng truy cập với class của nó. Tuy nhiên, điều này không có nghĩa là constructor có thể truy cập được bất cứ khi nào class có thể truy cập được*:

```java
package p1;
public class Outer {
    protected class Inner {}
}

// Inner có thể truy cập trong SonOfOuter, vì nó là một protected member class của Outer.
// Constructor của Inner không thể truy cập được trong SonOfOuter,
// vì class SonOfOuter không phải là subclass của Inner!
// Do đó, trong SonOfOutermặc dù Inner có thể truy cập được, nhưng default constructor của nó thì không.
package p2;
class SonOfOuter extends p1.Outer {
    void foo() {
        new Inner();  // compile-time access error
    }
}
```


### 8.10, Preventing Instantiation of a Class

Một class có thể được thiết kế để ngăn code bên ngoài class declaration tạo các instances của class đó, bằng cách khai báo ít nhất một constructor, để ngăn việc tạo một default constructor, và khai báo tất cả các constructors là *private*.

Tương tự như vậy, một *public class* có thể ngăn chặn việc tạo các instances bên ngoài package của nó, bằng cách khai báo ít nhất một constructor, để ngăn việc tạo một default constructor có quyền truy cập public và không khai báo constructor nào là public.

*Ví dụ 1. Preventing Instantiation via Constructor Accessibility*

```java
// chỉ có thể khởi tạo instance của class ClassOnly bên trong chính class đó
class ClassOnly {
    private ClassOnly() { }
    static String just = "only the lonely";
}

// chỉ có thể khởi tạo instance của class PackageOnly bên trong package just mà nó được khai báo.
package just;
public class PackageOnly {
    PackageOnly() { }
    String[] justDesserts = { "cheesecake", "ice cream" };
}
```


## 9. Enum Types

Một *enum declaration* xác định một *enum type* mới, là một loại *class type* đặc biệt.

```
- EnumDeclaration:
    {ClassModifier} enum Identifier [Superinterfaces] EnumBody
```

Nếu một *enum declaration* có modifier *abstract* hoặc *final*, sẽ gây ra compile-time error.

Một *enum declaration* được ngầm định là *final*, trừ khi nó chứa ít nhất một *enum constant* mà có một *class body*.

Một *nested enum type* được ngầm định là *static*, nhưng vẫn cho phép declaration của một *nested enum type* chỉ định *static* modifier một cách dư thừa.

*Note: Không thể khai báo một enum type trong body của một inner class, vì một inner class không thể có các static members ngoại trừ các constant variables.*

*Direct superclass* của một enum type E là *Enum<E>*.

Một *enum type* không có instances nào khác với những instances đã được định nghĩa bởi các *enum constants* của nó. Nếu cố gắng khởi tạo một *enum type* một cách tường minh, sẽ gây ra compile-time error.  


### 9.1, Enum Constants

Body của một enum declaration có thể chứa các *enum constants*. Một *enum constant* định nghĩa một *instance* của *enum type*.

```
- EnumBody:             { [EnumConstantList] [,] [EnumBodyDeclarations] }
- EnumConstantList:     EnumConstant {, EnumConstant}
- EnumConstant:         {EnumConstantModifier} Identifier [( [ArgumentList] )] [ClassBody]
- EnumConstantModifier: Annotation
- ArgumentList:         Expression {, Expression}
```

Identifier trong một EnumConstant có thể được sử dụng trong một name để tham chiếu đến *enum constant*.

Một *enum constant* có thể được theo sau bởi các *arguments* mà được truyền cho constructor của enum khi constant được tạo trong quá trình class initialization. Constructor được gọi được chọn bằng cách sử dụng các quy tắc thông thường của overload resolution. Nếu các arguments bị bỏ qua, một empty argument list sẽ được giả định.

Class body (tùy chọn) của một *enum constant* ngầm định định nghĩa một *anonymous class declaration* mà extends *immediately enclosing enum type*. Class body tuân theo các quy tắc của các anonymous classes, đặc biệt nó không thể chứa các constructors. Các *instance methods* được khai báo trong những class bodies này chỉ có thể được gọi bên ngoài *enclosing enum type* nếu chúng override các methods có thể truy cập được trong *enclosing enum type*.

Nếu class body của một *enum constant* khai báo một *abstract method*, sẽ gây ra compile-time error.

Bởi vì chỉ có một instance của mỗi *enum constant*, nên được phép sử dụng == operator thay cho equals method khi so sánh object references nếu biết rằng ít nhất một trong số chúng tham chiếu đến một enum constant.

*Note: equals method trong Enum là một final method*.


### 9.2, Enum Body Declarations

Ngoài các *enum constants*, body của một enum declaration có thể chứa các *constructor và member declarations*, cũng như các *instance và static initializers*.

Bất kỳ *constructor or member declarations* trong body của một enum declaration đều áp dụng chính xác cho enum type như thể chúng nằm trong body của một normal class declaration, trừ khi quy tắc khác được nêu rõ.

Sẽ gây ra compile-time error nếu:  

- Một *constructor declaration* trong một enum declaration là *public* or *protected*.  
- Hoặc nếu một *constructor declaration* trong một enum declaration chứa một lời gọi *superclass constructor*.  
- Hoặc tham chiếu tới một *static field* của một enum type từ các *constructors, instance initializers, or instance variable initializer expressions* của enum type đó, trừ khi field đó là một *constant variable*.  

Trong một enum declaration, một *constructor declaration* không có access modifiers sẽ là *private*.

Trong một enum declaration không có *constructor declarations*, thì một *default constructor* được tạo ngầm định. Default constructor là *private*, không có các formal parameters, và không có throws clause.

Sẽ gây ra compile-time error nếu:  

- enum declaration E có một *abstract method* m là một member, trừ khi E có ít nhất một *enum constant* và tất cả các *enum constants* của E đều có class bodies mà cung cấp phần triển khai cụ thể của m.  
- Hoặc enum declaration khai báo một *finalizer* (class Object có protected method finalize có thể bị override bởi các class khác, một định nghĩa cụ thể của finalize mà có thể được gọi cho một object, được gọi là finalizer của object đó, finalizer được gọi trước khi garbage collector thu hồi bộ nhớ của object).  

*Ví dụ 1. Enum Body Declarations*

```java
enum Coin {
    PENNY(1), NICKEL(5), DIME(10), QUARTER(25);
    Coin(int value) { this.value = value; }

    private final int value;
    public int value() { return value; }
}
```

*Ví dụ 2. Restriction On Enum Constant Self-Reference*

```java
import java.util.Map;
import java.util.HashMap;

enum Color {
    RED, GREEN, BLUE;

    static final Map<String,Color> colorMap = new HashMap<String,Color>();
    static {
        for (Color c : Color.values())
            colorMap.put(c.toString(), c);
    }
}
```


### 9.3, Enum Members

Các members của một enum type E gồm:

- Các members được khai báo trong body của declaration của E.  
- Các members được thừa kế từ [Enum<E>].  
- Đối với mỗi enum constant c được khai báo trong body của declaration của E, E sẽ có một *public static final field* của type E được khai báo ngầm định có cùng tên với c.  

    Những fields này được khai báo ngầm định theo thứ tự tương ứng với các enum constants, trước bất kỳ static fields nào được khai báo tường minh trong body của declaration của E.  

    Một enum constant được cho là sẽ được tạo khi field được khai báo ngầm định tương ứng được khởi tạo.  

- Các methods sau được khai báo ngầm định:  
    ```java
    /**
    * Returns an array containing the constants of this enum type, in the order they're declared.
    * This method may be used to iterate over the constants as follows:
    *
    *    for(E c : E.values())
    *        System.out.println(c);
    *
    * @return an array containing the constants of this enum type, in the order they're declared
    */
    public static E[] values();

    /**
    * Returns the enum constant of this type with the specified name.
    * The string must match exactly an identifier used to declare an enum constant in this type.
    * 
    * @return the enum constant with the specified name
    * @throws IllegalArgumentException if this enum type has no constant with the specified name
    */
    public static E valueOf(String name);

    /**
    * Returns the enum constant index can be found, just like an array index.
    * 
    * @return the enum constant index.
    */
    public int ordinal();
    ```

*Ví dụ 1. Iterating Over Enum Constants With An Enhanced for Loop*

```java
public class Test {
    enum Season { WINTER, SPRING, SUMMER, FALL }

    public static void main(String[] args) {
        for (Season s : Season.values())
            s + "\tindex: " + s.ordinal()
    }
}

// Output:
//    WINTER	index: 0
//    SPRING	index: 1
//    SUMMER	index: 2
//    FALL	    index: 3
```

*Ví dụ 2. Switching Over Enum Constants*

```java
class Test {
    enum Coin {
        PENNY(1), NICKEL(5), DIME(10), QUARTER(25);
        Coin(int value) { this.value = value; }

        private final int value;
        public int value() { return value; }
    }

    enum CoinColor { COPPER, NICKEL, SILVER }

    static CoinColor color(Coin c) {
        switch (c) {
            case PENNY:
                return CoinColor.COPPER;
            case NICKEL:
                return CoinColor.NICKEL;
            case DIME: 
            case QUARTER:
                return CoinColor.SILVER;
            default:
                throw new AssertionError("Unknown coin: " + c);
        }
    }

    public static void main(String[] args) {
        for (Coin c : Coin.values())
            System.out.println(c + "\t\t" + c.value() + "\t" + color(c));
    }
}

// Output:
//    PENNY           1       COPPER
//    NICKEL          5       NICKEL
//    DIME            10      SILVER
//    QUARTER         25      SILVER
```

*Ví dụ 3. Enum Constants with Class Bodies*

```java
enum Operation {
    PLUS {
        double eval(double x, double y) { return x + y; }
    },
    MINUS {
        double eval(double x, double y) { return x - y; }
    },
    TIMES {
        double eval(double x, double y) { return x * y; }
    },
    DIVIDED_BY {
        double eval(double x, double y) { return x / y; }
    };

    // Each constant supports an arithmetic operation
    abstract double eval(double x, double y);

    public static void main(String args[]) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        for (Operation op : Operation.values())
            System.out.println(x + " " + op + " " + y + " = " + op.eval(x, y));
    }
}

// java Operation 2.0 4.0
// 2.0 PLUS 4.0 = 6.0
// 2.0 MINUS 4.0 = -2.0
// 2.0 TIMES 4.0 = 8.0
// 2.0 DIVIDED_BY 4.0 = 0.5
```