# Chapter 8: Classes

*Class declaration* định nghĩa reference type mới, và mô tả chúng được triển khai như thế nào.  

*Top level class* là class mà không phải *nested class* (class lồng nhau).  

*Nested class* là bất kỳ class nào mà decralation nằm trong body của class or interface khác, bao gồm:  

- Member class,  
- Local class,  
- Anonymous class.  

Class được đặt tên có thể được khai báo là *abstract*, và phải được khai báo là abstract nếu nó được triển khai không đầy đủ; một lớp như vậy không thể được khởi tạo, nhưng có thể được extend bởi các subclass.  

Class có thể được khai báo là *final*, trong trường hợp đó, nó không thể có subclass. 

Class được khai báo là *public*, thì có thể được tham chiếu từ mã trong bất kỳ package nào của module của nó và có thể từ mã trong các module khác.  

Mọi Class ngoại trừ Object chỉ có thể extend một class duy nhất và có thể triển khai các interfaces.  

Class có thể là *generic*, nghĩa là, chúng có thể khai báo các type variables mà các ràng buộc (binding) của chúng có thể khác nhau giữa các instance khác nhau của class.

Classes có thể được decorated với annotations giống như bất kỳ loại declaration nào khác.  

Body của Class khai báo các:  

- Members:  
    + Fields  
    + Methods  
    + Nested classes  
    + Nested interfaces  
- static initializers  
- instance initializers  
- constructors  

Scope của member là toàn bộ phần nội dung của khai báo của class mà member đó thuộc về.  

Field, method, member class, member interface, và constructor declarations có thể bao gồm các access modifiers: public, protected, private.

Các members của class bao gồm cả các member được khai báo và member được thừa kế:  

- Các fields mới được khai báo của class có thể che dấu (hide) các fields được khai báo trong superclass or superinterface,  
- Các class members và interface members mới được khai báo của class có thể che dấu (hide) các class và interface members được khai báo trong superclass or superinterface,  
- Các methods mới được khai báo của class có thể che dấu (hide), triển khai (implement), ghi đè (override) các methods được khai báo trong superclass or superinterface.  

*Field declaration* mô tả class variables và instance variables. Field có thể được khai báo là *final*, trong trường hợp đó, không thể thay đổi giá trị cho nó nếu đã được gán hoặc được khởi tạo với một giá trị. Bất kỳ Field declaration nào cũng có thể bao gồm một initializer.  

*Member class declaration* mô tả *nested class* là member của class bao quanh. Các member class có thể là *static*, trong trường hợp đó chúng không có quyền truy cập các instance variables của class bao quanh; hoặc member class cũng có thể là *inner class*.  

*Member interface declaration* mô tả *nested interface* là member của class bao quanh.  

*Method declaration* mô tả code mà có thể được gọi bởi *method invocation expression*. Một *Class method* được gọi thông qua class type; Một *Instance method* được gọi thông qua instance của class type. Method mà declaration không chỉ ra nó được triển khai thế nào phải được khai báo là *abstract*. Method có thể được khai báo là *final*, trong trường hợp đó nó không thể bị ẩn (hidden) hay ghi đè (overriden). Method có thể được triển khai bằng platform-dependent native code. *Synchronized method* tự động lock một object trước khi thực thi phần body của nó, và tự động unlock object đó khi trở lại, như thể sử dụng một synchronized statement (câu lệnh đồng bộ hóa), do đó cho phép các activities của nó được đồng bộ với các activities của các threads khác.  

Method names có thể được overloaded.

*Instance initializers* là blocks of executable code mà có thể được sử dụng để giúp khởi tạo một instance khi nó được tạo ra.

*Static initializers* là blocks of executable code mà có thể được sử dụng để giúp khởi tạo một class.  

*Constructors* tương tự với methods, nhưng không thể được gọi trực tiếp bởi một lời gọi method (method call); chúng được sử dụng để khởi tạo class instance mới. Giống với methods, chúng có thể được overloaded.  


## 1. Class Declarations

Class declaration xác định một *reference type* được đặt tên mới.

Có 2 loại class declarations:  

- normal class declarations,  
- enum declarations.  

```
- Normal class declaration:  
    {ClassModifier} class TypeIdentifier [TypeParameters] [Superclass] [Superinterfaces] ClassBody
```

Nếu class có cùng tên với class or interface bao quanh nó, sẽ xảy ra compile-time error.  


### 1.1, Class Modifiers

Class declaration có thể bao gồm các *class modifiers*.  

Các Class Modifiers bao gồm:  

- Annotation,  
- public,  
- protected,  
- private,  
- abstract,  
- static,  
- final,  
- strictfp.  

*public* chỉ sử dụng trong *top level class declaration* và *member class declaration*, không sử dụng trong *local class declaration* và *annonymous class declaration*.  

*protected* và *private* chỉ sử dụng trong *member class declaration* bên trong một class declaration trực tiếp bao quanh.  

*static* chỉ sử dụng trong *member class declaration*, không sử dụng trong *top level, or local, or anonymous class declaration*.  

Sẽ xảy ra compile-time error nếu trong class declaration có một modifier keyword xuất hiện nhiều hơn một lần, or có nhiều hơn một trong các modifiers: public, protected, private.  


#### *1.1.1, abstract Classes*

*Abstract Class* là một class không hoàn chỉnh, hoặc được coi là không hoàn chỉnh.  

Nếu cố gắng tạo instance của abstract class bằng cách sử dụng *class instance creation expression*, thì sẽ xảy ra compile-time error.  

Subclass của abstract class mà bản thân nó không phải abstract thì có thể được khởi tạo, dẫn đến việc thực thi một constructor cho abstract class, và do đó sẽ thực thi field initializers cho instance variables của class đó.  

Một *normal class* có thể có các *abstract methods* là các methods được khai báo nhưng chưa được triển khai, chỉ khi nó là *abstract class*. Sẽ xảy ra compile time error nếu một normal class không phải abstract mà có abstract method.  

Một class C có *abstract method* nếu thỏa mãn ít nhất một trong 2 điều kiện sau:  

- Bất cứ member method nào của C dù được khai báo hay được kế thừa là abstract,  
- Bất cứ superclass nào của C có abstract method được khai báo với package access, và không tồn tại method nào ghi đè abstract method từ C or từ superclass của C.  

Một class không phải *abstract* khi extends một *abstract class*, thì phải implement tất cả các *abstract member methods* của abstract class đó.  

```java
// Child.java
package edu.model;
import java.util.Date;

public abstract class Child {
	private String name;
	private Date birdthday;
	public static int footCount = 2;
	
	public Child() {}
	public Child(String name, Date birthday) {
		this.name = name;
		this.birdthday = birthday;
	}

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
	}

    protected Date getBirdthday() {
        return birdthday;
    }

    protected void setBirdthday(Date birdthday) {
        this.birdthday = birdthday;
    }

    public abstract void talk();

    public void run() {
        System.out.println(name + " is running...");
    }
}

// Adult.java
package edu.model;
import java.util.Date;

public abstract class Adult extends Child {
    public Adult() {}
    public Adult(String name, Date birthday) {
        super(name, birthday);
    }
    
    public void work() {
        System.out.println(getName() + " is working...");
    }
}

// Singer.java
package edu.model;
import java.util.Date;

public class Singer extends Adult {
    public Singer() {}
    public Singer(String name, Date birthday) {
        super(name, birthday);
    }

    @Override
    public void talk() {
        System.out.println(getName() + " is talking...");
    }
}

// Test.java
package edu.model;
import java.util.Calendar;

public class Test {
	public static void main(String[] args) {
        Singer singer = new Singer("Huy", Calendar.getInstance().getTime());
        singer.run();  // Huy is running...
        singer.work(); // Huy is working...
        singer.talk(); // Huy is talking...

        Singer.footCount = 4;
        System.out.println(Child.footCount);  // 4
    }
}
```


#### *1.1.2, final Classes*

Một class có thể được khai báo là *final* nếu nó hoàn chỉnh, và không mong muốn nó có subclass.  

Một final class không có bất cứ subclass nào, nên sẽ xảy ra compile time error nếu tên của final class xuất hiện trong mệnh đề *extends* của class declaration khác.  

Sẽ xảy ra compile time error nếu một class được khai báo cả *final* và *abstract*, bởi vì việc triển khai của class như vậy có thể không bao giờ được hoàn thành.  

Các methods của *final class* không bao giờ bị override, vì nó không có bất kỳ subclass nào.  

```java
final class Example { ... }
```


#### *1.1.3, strictfp Classes*

Tác dụng của *strictfp* modifier là làm cho tất cả các *float or double expression* trong class declaration (bao gồm trong variable initializers, instance initializers, static initializers, và constructors) phải là FP-strict một cách tường minh.

Điều này ngụ ý rằng tất cả các methods được khai báo trong class và tất cả các nested type được khai báo trong class, ngầm định đều là strictfp.


### 1.2, Generic Classes and Type Parameters

Một class là *generic* nếu nó khai báo một hoặc nhiều *type variables*.  

Những *type variables* này được gọi là *type parameters* của class. Type parameter section theo sau class name và được phân tách bằng cặp ngoặc nhọn (<>).

```
- TypeParameters:        < TypeParameterList >
- TypeParameterList:     TypeParameter {, TypeParameter}
- TypeParameter:         {TypeParameterModifier} Identifier [TypeBound]
- TypeParameterModifier: Annotation  
- TypeBound: 
    + extends TypeVariable
    + extends ClassOrInterfaceType {AdditionalBound}  
- AdditionalBound: & InterfaceType
```

```java
class Box<T extends Person & Member> { ... }
```

Trong type parameter section của một class: Type variable T trực tiếp phụ thuộc vào type variable S nếu S là ràng buộc của T. Trong khi đó, T phụ thuộc vào S nếu T trực tiếp phụ thuộc vào S, hoặc T trực tiếp phụ thuộc vào type variable U mà phụ thuộc vào S.  

```java
class Box<S, T extends S> { ... }
```

Một generic class declaration định nghĩa một tập các parameterized type. Tất cả các parameterized type này chia sẻ cùng một class tại runtime.  

```java
Vector<String>  x = new Vector<String>();
Vector<Integer> y = new Vector<Integer>();
boolean b = x.getClass() == y.getClass();  // true
```

*Note*:

- Sẽ xảy ra compile time error, nếu generic class là subclass trực tiếp hoặc gián tiếp của *Throwable*.  
- Sẽ xảy ra compile time error, Khi tham chiếu đến type parameter của generic class C trong những trường hợp sau:  
    + Khai báo của *static member* của C,  
    + Khai báo của *static member* của bất kỳ *nested type declaration* bên trong C,  
    + Một *static initializer* của C,  
    + Một *static initializer* của bất kỳ *nested type declaration* bên trong C.  

*Ví dụ: Nested Generic Classes*  

```java
class Seq<T> { 
    T      head;
    Seq<T> tail;

    Seq() { 
        this(null, null); 
    } 

    Seq(T head, Seq<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    boolean isEmpty() { 
        return tail == null; 
    }
    
    public String toString() {
        return "(" + head + ", " + tail + ")";
    }

    class Zipper<S> { 
        Seq<Pair<T,S>> zip(Seq<S> that) { 
            if (isEmpty() || that.isEmpty()) {
                return new Seq<Pair<T,S>>(); 
            } else {
                Seq<T>.Zipper<S> tailZipper = tail.new Zipper<S>();
                return new Seq<Pair<T,S>>(
                    new Pair<T,S>(head, that.head),
                    tailZipper.zip(that.tail)
                );
            }
        }
    }
}

class Pair<T, S> {
    T fst; 
    S snd;

    Pair(T f, S s) { 
        fst = f; 
        snd = s; 
    }

    public String toString() {
        return fst + " - " + snd;
    }
}

class Test {
    public static void main(String[] args) {
        Seq<String> strs = new Seq<String>(
            "a",
            new Seq<String>("b", new Seq<String>())
        );

        Seq<Number> nums = new Seq<Number>(
            new Integer(1),
            new Seq<Number>(new Double(1.5), new Seq<Number>())
        );

        Seq<String>.Zipper<Number> zipper = strs.new Zipper<Number>();
        Seq<Pair<String,Number>> combined = zipper.zip(nums);

        System.out.println(combined); // (a - 1, (b - 1.5, (null, null)))
    }
}
```

### 1.3, Inner Classes and Enclosing Instances

*Inner class* là một *nested class* mà không được khai báo ngầm định hay tường minh là *static*.  

*Inner class* có thể là *non-static member class*, *local class*, hoặc *anonymous class*. 

**Note**: Trong khi member và local class đều là một class declaration, thì anonymous class là một expression. Một anonymous class được ngầm định là *final*, nó không bao giờ là *abstract*, cũng không bao giờ là *static*. Cấu trúc của anonymous class expression bao gồm:

- new operator,  
- Name của một class để extend, hoặc một interface để implement,  
- Cặp dấu ngoặc đơn () chứa danh sách các arguments cho một constructor khi extend một class, hoặc cặp ngoặc đơn trống khi implement một interface,  
- Body, chính là một class declaration body, có thể khai báo các fields, methods, local classes, instance initializers, nhưng không thể khai báo constructors.  

*Member class* của một interface được ngầm định là *static*, vì vậy nó không phải inner class. *Member interface* được ngầm định là *static*, vì vậy nó không phải inner. 

*Anonymous class* luôn là một inner class, nó không bao giờ là **static.  

**Note**: Sẽ xảy ra compile time error nếu *inner class* khai báo một *static initializer*, hoặc khai báo ngầm định hoặc tường minh một *static member* trừ khi member là một *constant variable*.  

*Inner class* có thể kế thừa các *static member* mà không phải *constant variable* mặc dù nó không thể khai báo chúng.  

*Nested class* mà không phải *inner class* có thể tự do khai báo *static member*.  

*Ví dụ 1: Member inner class*

```java
class HasStatic {
    static int j = 100; // OK: Inner class can inherit static members
}

class Outer {
    // class Inner has an enclosing instance of class Outer
    class Inner extends HasStatic {
        static final int x = 3;  // OK: constant variable
        static int y = 4;        // Compile-time error: an inner class
    }

    static class NestedButNotInner{
        static int z = 5;    // OK: not an inner class
    }

    interface NeverInner {}  // Interfaces are never inner because it is implicitly static
}
```

Một statement or expression chỉ xảy ra trong *static context* nếu innermost method, constructor, instance initializer, static initializer, field initializer, hoặc explicit constructor invocation statement bao quanh nó là một static method, static initializer, variable initializer của static variable, hoặc explicit constructor invocation statement.  

Inner class C là *direct inner class* của class or interface O nếu O là *immediately enclosing type declaration* của C và declaration của C không xảy ra trong *static context*.  

Class C là inner class của class or interface O nếu nó là direct inner class của O, hoặc là inner class của inner class của O.

Instance i của direct inner class C của một class or interface O được liên kết với một instance của O, được gọi là **immediately enclosing instance** của i. *Immediately enclosing instance* của một object (nếu có) được xác định ngay khi object được tạo.  

Instance của inner class I có declaration xảy ra trong *static context* không có *enclosing instances*. Tuy nhiên, nếu I được khai báo ngay lập tức bên trong một *static method* or *static initializer* thì I có một *enclosing block*.

Đối với mọi superclass S của class C mà bản thân S là direct inner class của class or interface SO, có một instance của SO được liên kết với instance i của C, mà được gọi là *immediately enclosing instance* của i với quan hệ S. *Immediately enclosing instance* của một object với quan hệ direct superclass của class của nó (nếu có), được xác định khi superclass constructor được gọi thông qua explicit constructor invocation statement.  

Khi một *inner class* (có declaration không xảy ra trong static context) tham chiếu đến một *instance variable* là member của *enclosing type declaration*, thì variable của *enclosing instance* tương ứng được sử dụng.

*Note*: 

- Inner classes có declarations xảy ra trong static context không thể tham chiếu đến instance variables của enclosing type declaration của chúng.  
- Bất kỳ local variable, formal parameter, or exception parameter được sử dụng nhưng không được khai báo trong inner class phải được khai báo *final*, hoặc là *effectively final* (có hiệu lực final, khi đã giữ một giá trị thì không thể thay đổi), nếu không sẽ xảy ra compile-time error.  
- Bất kỳ local variable được sử dụng nhưng không được khai báo trong inner class phải được gán trước body của inner class, nếu không sẽ xảy ra compile-time error.  
- Một *blank final field* của một *enclosing type declaration* phải được gán bên trong một inner class, nếu không sẽ xảy ra compile-time error.  

*Ví dụ 2: Local inner class được khai báo trong method*  

```java
class Outer {
    int i = 100;

    static void classMethod() {
        final int l = 200;

        // A local class
        // Class declaration của LocalInStaticContext xảy ra trong static context vì nằm trong static method classMethod.
        // class LocalInStaticContext không có enclosing instance.
        class LocalInStaticContext {
            // Instance variables của class Outer không available trong body của static method.
            int k = i;  // Compile-time error

            // Từ trong inner class có thể tham chiếu đến Local variables của method bao quanh mà không có lỗi,
            // miễn chúng là final, hoặc có hiệu lực final.
            int m = l;  // OK
        }
    }

    void foo() {
        int l = 200;

        // A local class
        // class declaration của Local không xảy ra trong static context vì nằm trong non-static method foo. 
        // class Local có một enclosing instance của class Outer. 
        class Local {
            // Instance variables của class Outer là available trong body của non-static method.
            int j = i; // OK
            int m = l; // nếu l có hiệu lực final, không được gán lần 2 trong method body, OK,
                       // nếu không sẽ là ERROR.
        }
    }
}
```

*Ví dụ 3: Anonymous inner class*

```java
abstract class Person{  
    abstract void eat();  
}  

interface Eatable{  
    void eat();  
}  

class TestAnonymousInner{  
    public static void main(String args[]){  
        // 1. A class is created, but its name is decided by the compiler,
        //    which extends the Person class and provides the implementation of the eat() method.
        // 2. An object of the Anonymous class is created that is referred to by 'p,'
        //    a reference variable of Person type.
        Person p = new Person(){  
            void eat(){ System.out.println("nice fruits"); }  
        };  
        p.eat();  

        // 1. A class is created, but its name is decided by the compiler,
        //    which implements the Eatable interface and provides the implementation of the eat() method.
        // 2. An object of the Anonymous class is created that is referred to by 'p',
        //    a reference variable of the Eatable type.
        Eatable e = new Eatable(){  
            public void eat(){ System.out.println("nice fruits"); }  
        };  
        e.eat();  
    }  
}  
```

*Ví dụ 4: Direct inner class*  

```java
// Mọi instance của WithDeepNesting.Nested.DeeplyNested đều có: 
// + Một enclosing instance của class WithDeepNesting.Nested (its immediately enclosing instance),
// + Và một enclosing instance của class WithDeepNesting (its 2nd lexically enclosing instance).

class WithDeepNesting {
    boolean toBe;
    WithDeepNesting(boolean b) { toBe = b; }

    class Nested {
        boolean theQuestion;
        class DeeplyNested {
            DeeplyNested(){
                theQuestion = toBe || !toBe;
            }
        }
    }
}
```


### 1.4, Superclasses and Subclasses

Mệnh đề tùy chọn *extends* trong một *normal class declaration* xác định *direct superclass* của class hiện tại.  

```
extends ClassType
```

*Note*:  

- ClassType phải là tên của class type có thể truy cập, nếu không sẽ xảy ra compile time error.  
- ClassType nếu là tên của final class type, sẽ xảy ra compile time error.  
- ClassType nếu là tên của Enum class type, sẽ xảy ra compile time error.  
- ClassType nếu có type arguments, nó phải là một well-formed parameterized type, không có type argument nào là wildcard, nếu không sẽ xảy ra compile time error.  

class *Object* không có direct super class.  

Cho một class declaration (có thể là generic) C<F1,...,Fn> (n ≥ 0, C ≠ Object), thì direct superclass của class type C<F1,...,Fn> là type đã cho trong mệnh đề extends của declaration nếu có mệnh đề extends, nếu không sẽ là Object.  

Cho một generic class declaration C<F1,...,Fn> (n > 0), thì direct superclass của parameterized type C<T1,...,Tn> với Ti (1 ≤ i ≤ n) là D<U1 θ,...,Uk θ>, trong đó D<U1,...,Uk> là direct superclass của C<F1,...,Fn>, và θ là phép thay thế [F1:=T1,...,Fn:=Tn].  

Một class được cho là một *direct subclass* của direct superclass của nó.  

Class A là subclass của class C nếu 1 trong 2 điều sau là đúng:  

- A là direct subclass của C,  
- Tồn tại class B sao cho A là subclass của B, và B là subclass của C, áp dụng đệ quy.  

Class C được nói là superclass của class A bất cứ khi nào A là subclass của C.  

*Ví dụ: Direct Superclasses and Subclasses*  

```java
// class Point is a direct subclass of Object
class Point { int x, y; }

// class ColoredPoint is a direct subclass of class Point.
final class ColoredPoint extends Point { int color; }

class Colored3DPoint extends ColoredPoint { int z; }  // error
```

*Ví dụ: Superclasses and Subclasses*  

```java
class Point { int x, y; }

// class Point is a superclass of class ColoredPoint.
class ColoredPoint extends Point { int color; }

// class Point is a superclass of class Colored3dPoint
final class Colored3dPoint extends ColoredPoint { int z; }
```

*Ví dụ: Generic class*

```java
class ClassA<T> { T a; }

// ClassA<T> is a super class of ClassB<T>
class ClassB<T> extends ClassA<T> {}

public class Test {
	public static void main(String[] args) {
        ClassB<String> strB = new ClassB<String>();

        // ClassA<String> is a super class of ClassB<String>
        ClassA<String> strA = strB; 
		
		System.out.println(strB.a); // null
	}
}
```

Class C phụ thuộc trực tiếp vào type T nếu T được đề cập trong extends or implements clause của C.  

Class C phụ thuộc vào reference type T nếu bất kỳ điều nào sau đây là đúng:  

- C phụ thuộc trực tiếp vào T,  
- C phụ thuộc trực tiếp vào interface I mà phụ thuộc vào T,  
- C phụ thuộc trực tiếp vào class D mà phụ thuộc vào T (áp dụng đệ quy).  

*Note*:  

- Nếu class phụ thuộc vào chính nó, compile-time error sẽ xảy ra.  
- Nếu các class được khai báo vòng tròn được phát hiện tại runtime, khi classes được load, thì ClassCircularityError sẽ được ném ra.  

```java
class Point extends ColoredPoint { int x, y; }
class ColoredPoint extends Point { int color; }
```


### 1.5, Superinterfaces

Mệnh đề tùy chọn *implements* trong một *class declaration* liệt kê tên các interfaces là các *direct superinterface* của class đang được khai báo.  

```
Superinterfaces:    implements InterfaceTypeList
InterfaceTypeList:  InterfaceType {, InterfaceType}
```

*Note*:  

- InterfaceType phải là tên của interface type có thể truy cập, nếu không sẽ xảy ra compile time error.  
- InterfaceType nếu có type arguments, nó phải là một well-formed parameterized type, không có type argument nào là wildcard, nếu không sẽ xảy ra compile time error.  
- Nếu cùng InterfaceType xuất hiện nhiều hơn một lần trong một mệnh đề implements, sẽ xảy ra compile time error.  

Cho một class declaration (có thể là generic) C<F1,...,Fn> (n ≥ 0, C ≠ Object), thì direct superinterfaces của class type C<F1,...,Fn> là các types đã cho trong mệnh đề implements của declaration nếu có mệnh đề implements.  

Cho một generic class declaration C<F1,...,Fn> (n > 0), thì direct superinterfaces của parameterized type C<T1,...,Tn> với Ti (1 ≤ i ≤ n) là tất cả các types I<U1 θ,...,Uk θ>, trong đó I<U1,...,Uk> là direct superinterface của C<F1,...,Fn>, và θ là phép thay thế [F1:=T1,...,Fn:=Tn].  

Interface type I là superinterface của class type C nếu một trong các điều sau là đúng:  

- I là direct superinterface của C,  
- I là superinterface của direct superclass của C,  
- C có direct superinterface J mà có superinterface I.  

**Note**: Một class không thể đồng thời là subtype của 2 interface types là các tham số hóa khác nhau của cùng generic interface.

*Ví dụ: Illegal Multiple Inheritance of an Interface*

```java
interface I<T> {}
class B implements I<Integer> {}
class C extends B implements I<String> {} // error
// C can not be subtype of both I<Integer> and I<String>
```

Một class được cho là *implement* tất cả các superinterface của nó.  

Trừ khi class được khai báo là *abstract*, nếu không tất cả các *abstract member methods* của mỗi *direct superinterface* phải được implement bằng một declaration trong class này, hoặc bằng một declaration tồn tại được kế thừa từ direct superclass or direct superinterface.  

Mỗi *default method* (method được khai báo với default keyword) của một superinterface của class có thể được override bởi một method trong class, nếu không default method sẽ được kế thừa và behavior của nó được chỉ định bởi default body của nó.  

Cho phép một method declaration duy nhất trong class triển khai các methods của nhiều hơn một superinterfaces khi chúng có cùng name, signature và return type.

**Note**: Class không kế thừa các *static methods* từ *superinterfaces* của nó. 

*Ví dụ: Superinterfaces and implemment methods*

```java
interface Colorable {
    void setColor(int color);
    int getColor();
}

enum Finish { MATTE, GLOSSY }

interface Paintable extends Colorable {
    void setFinish(Finish finish);
    Finish getFinish();
}

class Point { int x, y; }

class ColoredPoint extends Point implements Colorable {
    int color;
    public void setColor(int color) { this.color = color; }
    public int getColor() { return color; }
}

class PaintedPoint extends ColoredPoint implements Paintable {
    Finish finish;
    public void setFinish(Finish finish) {
        this.finish = finish;
    }
    public Finish getFinish() { return finish; }
}
```

*Ví dụ: a method of class A's superclass implements abstract method of class A's superinterface*

```java
interface Member() {
    public double getRandom();
}

class Person {
    public double getRandom() { return Math.random(); }
}

class Student extends Person implements Member {} // OK
```

*Ví dụ: a single method declaration in class implements both superinterface*  

```java
interface Fish  { int getNumberOfScales(); }
interface Piano { int getNumberOfScales(); }

class Tuna implements Fish, Piano {
    // method getNumberOfScales in class Tuna has a name, signature, and return type that matches:
    // - the method declared in interface Fish 
    // - the method declared in interface Piano;
    public int getNumberOfScales() { return 91; } // OK
}
```

*Ví dụ: a single method declaration in class implements both superinterface*  

```java
interface Fish       { int    getNumberOfScales(); }
interface StringBass { double getNumberOfScales(); }

class Bass implements Fish, StringBass {
    // Không thể khai báo một method getNumberOfScales có signature và return type mà 
    // tương thích với cả 2 methods được khai báo trong interface Fish and trong interface StringBass,
    // Vì một class không thể có nhiều methods với cùng signature và khác primitive return types. 
    // Vì vậy, Không thể để single class mà implement cả interface Fish and interface StringBass.
    public ?? getNumberOfScales() { return 91; } // error
}
```


### 1.6, Class Body and Member Declarations

Class body có thể chứa declaration của các *members* của class: *fields, methods, classes, và interfaces*.  

Class body cũng có thể chứa: *instance initializers, static initializers*, và declarations của *constructors* của class.