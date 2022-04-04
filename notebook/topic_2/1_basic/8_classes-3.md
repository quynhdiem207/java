# Chapter 8: Classes

## 3. Field Declarations

Variables của class type được giới thiệu bởi *field declarations*.

```
- FieldDeclaration:       {FieldModifier} Type VariableDeclaratorList ;
- VariableDeclaratorList: VariableDeclarator {, VariableDeclarator}
- VariableDeclarator:     VariableDeclaratorId [= VariableInitializer]
- VariableDeclaratorId:   Identifier [Dims]
- VariableInitializer:
    + Expression
    + ArrayInitializer
- Dims: {Annotation} [ ] {{Annotation} [ ]}
```

Nếu class khai báo một field với tên xác định, thì declaration của field đó được cho là che dấu (hide) bất kỳ và tất cả các declarations có thể truy cập của các fields có cùng tên trong superclasses, và superinterfaces của class đó.

Một field bị che dấu (hidden) có thể được truy cập bằng cách sử dụng *qualified name* nếu nó là *static*, hoặc sử dụng *field access expression* có chứa *super* keyword, hoặc ép kiểu sang *supertype*.  

Nếu một *field declaration* che dấu (hide) declaration của field khác, thì hai fields đó không cần có cùng type.

Class kế thừa từ direct superclass và direct superinterfaces tất cả các *non-private* fields của superclass và superinterfaces đều có thể truy cập trong class đó và không bị che dấu (hidden) bởi declaration trong class đó.

Private field của một superclass có thể được truy cập đối với subclass - vd: nếu cả hai classes là member của cùng một class.

Một class có thể kế thừa nhiều hơn một field với cùng tên, sẽ không gây ra compile-time error. Tuy nhiên, sẽ gây ra compile-time error nếu tham chiếu đến field được kế thừa không rõ ràng bằng *simple name* của nó, cần sử dụng *qualified name* hoặc *field access expression* có chứa super keyword.

Nếu cùng một field declaration được kế thừa từ một interface bằng nhiều paths, thì field đó chỉ được coi là kế thừa một lần. Nó có thể được tham chiếu bằng simple name.

*Ví dụ: Multiply Inherited Fields*

```java
interface Frob  { float v = 2.0f; }
class SuperTest { int   v = 3; }

class Test extends SuperTest implements Frob {
    public static void main(String[] args) {
        new Test().printV();
    }

    // uses the field access expression super.v to refer to the field named v declared in class SuperTest,
    // uses the qualified name Frob.v to refer to the field named v declared in interface Frob.
    void printV() {
        System.out.println((super.v + Frob.v)/2); // 2.5
    }
}
```

*Ví dụ: Multiply Inherited Fields*

```java
interface Color        { int RED=0, GREEN=1,  BLUE=2;  }
interface TrafficLight { int RED=0, YELLOW=1, GREEN=2; }

class Test implements Color, TrafficLight {
    public static void main(String[] args) {
        System.out.println(GREEN);  // compile-time error
        System.out.println(RED);    // compile-time error
    }
}
```

*Ví dụ: Re-inheritance of Fields*

```java
interface Colorable {
    int RED = 0xff0000, GREEN = 0x00ff00, BLUE = 0x0000ff;
}

interface Paintable extends Colorable {
    int MATTE = 0, GLOSSY = 1;
}

class Point { int x, y; }
class ColoredPoint extends Point implements Colorable {}

// fields RED, GREEN, and BLUE được kế thừa bởi class PaintedPoint thông qua cả direct superclass ColoredPoint của nó, và direct superinterface Paintable của nó.
class PaintedPoint extends ColoredPoint implements Paintable {
    int p = RED;
}
```


### 3.1, Field Modifiers

```
FieldModifier gồm:
    + Annotation public protected private
    + static final transient volatile
```


#### *3.1.1, static Fields*

Khi field được khai báo là *static*, tồn tại duy nhất một hiện thân của field được tạo, bất kể có bao nhiêu instance của class được tạo. Do đó, *static field* còn được gọi là *class variable*.

Field không được khai báo là static (hay còn gọi là *non-static field*) được gọi là *instance variable*. Bất cứ khi nào instance mới của class được tạo, thì một variable mới được liên kết với instance đó được tạo cho mỗi instance variable được khai báo trong class đó hoặc superclass của nó.

*Ví dụ 1: static Fields*

```java
class Point {
    int x, y, useCount;
    Point(int x, int y) { this.x = x; this.y = y; }
    static final Point origin = new Point(0, 0);
}

class Test {
    public static void main(String[] args) {
        Point p = new Point(1,1);
        Point q = new Point(2,2);

        p.x = 3;
        p.y = 3;
        p.useCount++;
        p.origin.useCount++;

        // Thay đổi các fields x, y, and useCount của p không ảnh hưởng các fields của q,
        // vì các fields này là instance variables trong các objects khác biệt.
        System.out.println("(" + q.x + "," + q.y + ")"); // (2,2)
        System.out.println(q.useCount);                  // 0

        // Class variable origin của class Point được tham chiếu bằng:
        // - Sử dụng qualifier name Point.origin
        // - Sử dụng variables của class type trong field access expressions p.origin and q.origin
        // Chúng tham chiếu đến cùng variable
        System.out.println(q.origin == Point.origin);    // true
        System.out.println(q.origin.useCount);           // 1
    }
}
```

*Ví dụ 2: Hiding of Class Variables*

```java
class Point {
    static int x = 2;
}

// vì declaration của x trong class Test che dấu definition của x trong class Point,
// nên class Test không kế thừa field x từ superclass Point của nó.
// Trong declaration của class Test, simple name x tham chiếu đến field được khai báo trong class Test.
// Code trong class Test có thể tham chiếu đến field x của class Point qua:
// super.x (or Point.x, vì x là static).
class Test extends Point {
    static double x = 4.7;

    public static void main(String[] args) {
        new Test().printX();
    }

    void printX() {
        System.out.println(x + " - " + super.x); // 4.7 - 2
    }
}
```

*Trong vd2, nếu Test.x bị xóa:*

```java
class Point {
    static int x = 2;
}


// field x của class Point không bị che dấu bên trong class Test; 
// Do đó, simple name x tham chiếu đến field Point.x.
// Code trong class Test vẫn có thể tham chiếu đến field đó dưới dạng super.x.
class Test extends Point {
    public static void main(String[] args) {
        new Test().printX();
    }

    void printX() {
        System.out.println(x + " - " + super.x); // 2 - 2
    }
}
```

*Ví dụ 3: Hiding of Instance Variables*

```java
class Point {
    int x = 2;
}

// Vì declaration của x trong class Test che dấu definition của x trong class Point,
// nên class Test không kế thừa field x từ superclass Point của nó.
// tuy nhiên, field x vẫn được implemented bởi instances của class Test.
// Nói cách khác, mọi instance của class Test đều chứa 2 fields có tên x: có type int và type double.
class Test extends Point {
    double x = 4.7;
    
	public Test() {}
	public Test(double x) {
		this.x = x;
	}

    // Trong declaration của class Test, simple name x luôn tham chiếu đên field được khai báo trong class Test.
    // Code trong instance methods của class Test có thể tham chiếu đến instance variable x của class Point dưới dạng super.x.
    void printBoth() {
        System.out.println(x + " - " + super.x);                  // 5.0 - 2
    }

    // Khi sử dụng field access expression để truy cập field x, 
    // thì sẽ truy cập field x trong class được xác định bởi type của reference expression.
    // Do đó, expression sample.x truy cập double value, instance variable được khai báo trong class Test,
    // vì type của variable sample là Test,
    // nhưng expression ((Point)sample).x truy cập int value, instance variable được khai báo trong class Point,
    // vì ép kiểu sang type Point.
    public static void main(String[] args) {
        Test sample = new Test(5.0);
        sample.printBoth();
        System.out.println(sample.x + " - " + ((Point)sample).x); // 5.0 - 2
    }
}
```

*Trong vd3, Nếu declaration của x bị xóa khỏi class Test:*

```java
class Point {
    int x = 2;
}

// Field x của class Point không còn bị che dấu trong class Test.
// Trong instance methods trong declaration của class Test, 
// simple name x tham chiếu đến field được khai báo trong class Point.
// Code trong class Test vẫn có thể tham chiếu đến field x qua super.x.
// Expression sample.x vẫn tham chiếu đến field x bên trong type Test,
// nhưng field x giờ là field được kế thừa, do đó tham chiếu đến field x được khai báo trong class Point.
class Test extends Point {
    void printBoth() {
        System.out.println(x + " - " + super.x);
    }

    public static void main(String[] args) {
        Test sample = new Test();
        sample.printBoth();                                         // 2 - 2
        System.out.println(sample.x + " - " +	((Point)sample).x); // 2 - 2
    }
}
```


#### *3.1.2, final Fields*

Field có thể được khai báo *final*. Cả class và instance variables (static và non-static fields) đều có thể được khai báo *final*.

*Blank final class variable* phải được gán rõ ràng bởi một *static initializer* của class mà nó được khai báo, nếu không sẽ gây ra compile-time error.

*Blank final instance variable* phải được gán rõ ràng ở cuối mỗi constructor của class mà nó được khai báo,nếu không sẽ gây ra compile-time error.


#### *3.1.3, transient Fields*

Variables có thể được đánh dấu là *transient* (tạm thời), để chỉ ra rằng chúng không phải một phần của *persistent state* (trạng thái ổn định, bền vững) của một object.

*Serialization* là quá trình chuyển đổi state của một *object* thành một *byte stream*, và *deserialization* là ngược lại với nó.

Khi đánh dấu bất kỳ variables nào là *transient*, thì variable đó không được serialized. Vì các *transient field* không xuất hiện ở dạng serialized của một object, nên quá trình deserialization sẽ sử dụng các giá trị mặc định cho các field đó khi tạo một object từ dạng serialized.

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = -2936687026040726549L;
    private String bookName;
    private transient String description;
    private transient int copies;
    
    // getters and setters
    ...

    public static void serialize(Book book) throws Exception {
        FileOutputStream file = new FileOutputStream("D:\\Transient");
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(book);
        out.close();
        file.close();
    }

    public static Book deserialize() throws Exception {
        FileInputStream file = new FileInputStream("D:\\Transient");
        ObjectInputStream in = new ObjectInputStream(file);
        Book book = (Book) in.readObject();
        in.close();
        file.close();
        return book;
    }

    public static void main(String[] args) {
        Book book = new Book();
        book.setBookName("Java Reference");
        book.setDescription("will not be saved");
        book.setCopies(25);

        try {
            Book.serialize(book);
            Book book2 = Book.deserialize();
            
            System.out.println(book2.getBookName());    // Java Reference
            System.out.println(book2.getDescription()); // null
            System.out.println(book2.getCopies());      // 0
        } catch(Exception e) {
            System.out.println("Error");
        }
    }
}
```


#### *3.1.4, volatile Fields*

Java cho phép các threads truy cập các variables được chia sẻ. Theo quy tắc, để đảm bảo rằng các biến được chia sẻ được cập nhật một cách nhất quán và đáng tin cậy, một thread phải đảm bảo rằng nó được sử dụng độc quyền các biến đó bằng cách lấy một *lock*, mà theo quy ước thực thi loại trừ lẫn nhau đối với các biến được chia sẻ đó.

Để thuận tiện hơn, Java cung cấp cơ chế *volatile field*, Java Memory Model đảm bảo rằng tất cả các threads đều thấy một giá trị nhất quán cho variables.

Nếu *final variable* được khai báo là *volatile* sẽ gây ra compile-time error.

*Ví dụ: Một thread liên tục gọi method one (không quá Integer.MAX_VALUE lần), một thread khác liên tục gọi method two:*

```java
class Test {
    static int i = 0, j = 0;
    static void one() { i++; j++; }
    static void two() {
        System.out.println("i=" + i + " j=" + j);
    }
}
```

*Trong vd trên, method two có thể in ra giá trị j lớn hơn i, vì không đồng bộ hóa, nên các giá trị được chia sẻ là i và j có thể cập nhật không theo thứ tự. Để ngăn chặn điều này cần khai báo method one và two là "synchronized ", ngăn method one và two thực thi đồng thời, hơn nữa đảm bảo các giá trị được chia sẻ i và j được cập nhật trước khi method one return, do vậy method two luôn in ra i và j có giá trị bằng nhau :*

```java
class Test {
    static int i = 0, j = 0;
    static synchronized void one() { i++; j++; }
    static synchronized void two() {
        System.out.println("i=" + i + " j=" + j);
    }
}
```

*Một phương pháp khác là khai báo i và j là "volatile", điều này cho phép method one và two được thực thi đồng thời, nhưng đảm bảo rằng việc truy cập vào các giá trị được chia sẻ i và j xảy ra chính xác các lần và theo đúng thứ tự như chúng xuất hiện trong program text của mỗi thread. Do đó giá trị được chia sẻ j không bao giờ lớn hơn i, do mỗi lần cập nhật i phải được ánh xạ trong giá trị được chia sẻ i trước khi cập nhật j xảy ra, tuy nhiên lời gọi method two có thể quan sát giá trị của j lớn hơn i, vì method one có thể được thực thi nhiều lần trong khi method two tìm nạp (fetch) giá trị của i và fetch giá trị của j:*

```java
class Test {
    static volatile int i = 0, j = 0;
    static void one() { i++; j++; }
    static void two() {
        System.out.println("i=" + i + " j=" + j);
    }
}
```


### 3.2, Field Initialization

Nếu một declarator trong một field declaration có một *variable initializer*, thì declarator đó có ngữ nghĩa của phép gán cho variable được khai báo.

Nếu declarator dành cho một *class variable* (hay *static field*), thì các quy tắc sau được áp dụng cho initializer của nó:  

- Nếu một tham chiếu bởi một simple name đến bất kỳ *instance variable* nào xảy ra trong initializer, thì sẽ xảy ra compile-time error.  
- Nếu *this* hoặc *super* keyword xảy ra trong initializer, thì sẽ xảy ra compile-time error.  
- Tại runtime, initializer được đánh giá và thực hiện gán chính xác một lần, khi class được khởi tạo.  

Nếu declarator dành cho một *instance variable* (hay *non-static field*), thì các quy tắc sau được áp dụng cho initializer của nó:  

- Initializer có thể sử dụng simple name của bất kỳ class variable nào được khai báo hoặc kế thừa bởi class, ngay cả variable mà có declaration về mặt text xảy ra sau initializer.  
- Initializer có thể tham chiếu đến object hiện tại qua *this* keyword, và có thể sử dụng *super* keyword.  
- Tại runtime, initializer được đánh giá và thực hiện gán mỗi khi một instance của class được tạo.  

*Note: Variable initializers cũng được sử dụng trong local variable declaration statements, trong đó initializer được đánh giá và thực hiện gán mỗi lần local variable declaration statement được thực thi.*

*Ví dụ 1: Field Initialization*

```java
class Point {
    int x = 1, y = 5;
}

class Test {
    public static void main(String[] args) {
        // vì phép gán cho x và y xảy ra bất cứ khi nào một new Point được tạo.
        Point p = new Point();
        System.out.println(p.x + ", " + p.y); // 1, 5
    }
}
```

*Ví dụ 2: Forward Reference to a Class Variable*

```java
class Test {
    // no error
    // khởi tạo j thành 1 khi class Test được khởi tạo.
    // khởi tạo f thành giá trị hiện tại của j mỗi khi một instance của Class Test được tạo.
    float f = j; 
    static int j = 1; 
}
```


### 3.3, Forward References During Field Initialization

Việc sử dụng *class variable* có declaration xuất hiện trong program text sau việc sử dụng thì đôi khi bị hạn chế, mặc dù trong scope. Cụ thể sẽ gây ra compile-time error nếu tất cả những điều sau là đúng:  

- Declaration của một class variable trong một class hoặc interface C xuất hiện dưới dạng văn bản sau khi sử dụng class variable;  
- Sử dụng một simple name trong class variable initializer của C hoặc static initializer của C;  
- Việc sử dụng không nằm ở phía bên trái của một phép gán;  
- C là innermost class hoặc interface bao quanh việc sử dụng.  

Việc sử dụng *instance variable* có declaration xuất hiện trong program text sau việc sử dụng thì đôi khi bị hạn chế, mặc dù trong scope. Cụ thể sẽ gây ra compile-time error nếu tất cả những điều sau là đúng:  

- Declaration của một instance variable trong một class hoặc interface C xuất hiện dưới dạng văn bản sau khi sử dụng instance variable;  
- Sử dụng một simple name trong instance variable initializer của C hoặc instance initializer của C;  
- Việc sử dụng không nằm ở phía bên trái của một phép gán;  
- C là innermost class hoặc interface bao quanh việc sử dụng.  

*Ví dụ: Restrictions on Field Initialization (hạn chế)*

```java
// CASE 1:
class Test1 {
    int i = j; // compile-time error: incorrect forward reference
    int j = 1;
}

// CASE 2:
class Test2 {
    Test2() { k = 2; } // ok
    int j = 1;
    int i = j;
    int k;
}

// CASE 3:
class Z {
    static int i = j + 2; // compile-time error
    static int j = 4;
}

// CASE 4:
class Z {
    static { i = j + 2; } // compile-time error
    static int i, j;
    static { j = 4; }
}

// CASE 5:
class Z {
    static int peek() { return j; }
    static int i = peek();
    static int j = 1;
}

class Test {
    public static void main(String[] args) {
        // vì variable initializer cho i sử dụng class method peek để truy xuất value của variable j
        // trước khi j được khởi tạo bởi variable initializer của nó,
        // tại thời điểm đó, nó vẫn có giá trị mặc định
        System.out.println(Z.i); // 0
    }
} 
```

*Ví dụ*:

```java
class UseBeforeDeclaration {
    static {
        x = 100;       // ok - assignment
        int y = x + 1; // ERROR - read before declaration
        int v = x = 3; // ok - x at left hand side of assignment
        int z = UseBeforeDeclaration.x * 2; // ok - not accessed via simple name

        Object o = new Object() { 
            void foo() { x++; } // ok - occurs in a different class
            { x++; }            // ok - occurs in a different class
        };
    }

    {
        j = 200;            // ok - assignment
        j = j + 1;          // ERROR - right hand side reads before declaration
        int k = j = j + 1;  // ERROR - illegal forward reference to j
        int n = j = 300;    // ok - j at left hand side of assignment
        int h = j++;        // ERROR - read before declaration
        int l = this.j * 3; // ok - not accessed via simple name

        Object o = new Object() { 
            void foo(){ j++; } // ok - occurs in a different class
            { j = j + 1; }     // ok - occurs in a different class
        };
    }

    int w = x = 3; // ok - x at left hand side of assignment
    int p = x;     // ok - instance initializers may access static fields

    static int u = (new Object() { int bar() { return x; } }).bar();
	    // ok - occurs in a different class

    static int x;

    int m = j = 4; // ok - j at left hand side of assignment
    int o = (new Object() { int bar() { return j; } }).bar(); 
        // ok - occurs in a different class
    int j;
}
```