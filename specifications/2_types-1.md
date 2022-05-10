## 1. Types

Java là ngôn ngữ định kiểu tĩnh, tức là mọi variable và expression đều có một type được biết đến tại thời điểm compile.  

Type của variable luôn được khai báo, còn type của expression có thể được suy ra tại compile-time.   

*Types* giới hạn value mà variable có thể giữ or expression có thể tạo ra, đồng thời giới hạn các operation (phép toán) hỗ trợ cho các values này.  

Trong Java *types* được chia làm 2 loại:  

- **Primitive Types** (Kiểu dữ liệu nguyên thủy): Là các types đã được định nghĩa của Java, bao gồm:  
    + Numeric Types:  
        + Integral types (Số nguyên):  
            >+ byte    1 byte      0  
            >+ short   2 bytes     0  
            >+ int     4 bytes     0  
            >+ long    8 bytes     0L || 0  
            >+ char    2 bytes     '\u0000' (ký tự NULL)  
        + Floating-point types (Số thực):  
            >+ float   4 bytes     0.0f  
            >+ double  8 bytes     0.0d || 0.0  
    + Boolean types:  
        >+ boolean     1 bit       false  

- **Reference Types** (Kiểu dữ liệu tham chiếu):  
    + Array types  
    + Class types  
    + Interface types  

**Note**: Không sử dụng float và double cho các giá trị chính xác như tiền tệ, thay vào đó cần sử dụng *java.math.BigDecimal* thay thế.  


## 2. Values

*Values* có thể được lưu trữ trong *variables*, truyền dưới dạng *arguments*, trả về bởi methods, và tính toán trên chúng.  

Tương ứng với 2 loại *types*, trong Java có 2 loại *values*:  

- *Primitive Values*  
- *Reference Values*  


### 2.1, Primitive Values

- Integral Values:  
    + byte, short, int, long là các số nguyên có dấu.  
    + char là số nguyên không dấu (0 -> 2^16 - 1).  
- Floating-point Values: float, double là các số thực  
- boolean values: true và false  


#### *2.2.1, Integral Values*

- Hỗ trợ các operators (toán tử):  
    + Toán tử so sánh: <, <=, >, >=, ==, !=  
    + Toán tử số học:  
        + Một ngôi: +, -  
        + Tính toán: +, -, *, /, %  
        + Tăng giảm: ++, --  
        + Bit: ~, &, |, ^, >>, <<, >>>  
    + Toán tử điều kiện: ? :  
    + Toán tử ép kiểu: Chuyển đối từ integral value sang bất cứ numeric type nào  
    + Toán tử nối chuỗi: +  

- Các constants, constructors, methods khác hỗ trợ cho các values này được định nghĩa trong các Wrapper Class: Byte, Short, Integer, Long, Character, và class Math.  
- Nếu trong phép toán có ít nhất 1 toán hạng kiểu *long*, thì sẽ sử dụng độ chính xác 8 bytes, nếu toán hạng còn lại không phải kiểu long sẽ thực hiện mở rộng thành kiểu long; Nếu không có toán hạng kiểu *long* sẽ sử dụng độ chính xác 4 bytes, các toán hạng sẽ được mở rộng thành kiểu *int* nếu không phải kiểu *int*.  
- Có thể ép kiểu bất cứ value nào của *Integral types* từ or sang bất cứ *numeric types* nào, nhưng không thể ép kiểu giữa *integral type* và *boolean type*.  
    ```java
    public static void main(String[] args) {
        int a = 2;
        long b = a;  // OK -> implicit
        a = b;       // ERROR -> implicit
        a = (int) b; // OK -> explicit

        char d = 'a';
        a = (int) d; // OK

        boolean c = (boolean) a; // ERROR

    }
    ```
- Các integer operator không biểu thị *overflow* hay *underflow* theo bất cứ cách nào.  
    ```java
    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE); // 2147483647
        System.out.println(Integer.MIN_VALUE); // -2147483648

        long a = Integer.MAX_VALUE * 2; // -2 -> Tràn bộ nhớ
        long b = Integer.MAX_VALUE + 2; // -2147483647 -> Tràn bộ nhớ

        long c = Integer.MIN_VALUE * 2; // 0 -> Tràn bộ nhớ
        long d = Integer.MIN_VALUE - 2; // 2147483646 -> Tràn bộ nhớ
    }
    ```
- Các exceptions:  
    + Integer operator ném *NullPointerException* nếu yêu cầu chuyển đổi *unboxing* của *null reference*,  
    + Các operator /, % ném *ArithmeticException* nếu toán hạng phải là 0,  
    + Các operator ++, -- ném *OutOfMemoryError* nếu yêu cầu chuyển đổi *boxing* mà không có đủ bộ nhớ.  

    ```java
    public static void main(String[] args) {
        int z = 2 / 0;      // Error (ArithmeticException)

        Integer y = null;
        int x = y;          // Error (NullPointerException)
    }
    ```

**Note**: Giá trị kiểu *char* phải nằm trong cặp dấu nháy đơn '', vd: 'A'  


#### *2.2.2, Floating-Point Values*

- Floating-Point Values bao gồm: số dương, số âm, 0, -0, Infinity, -Infinity, và NaN.  
- Có thể sử dụng *Extended-Exponent Values* (giá trị lũy thừa mở rộng, vd: 1e5) để biểu diễn Floating-Point Values.  
    ```java
    public static void main(String[] args) {
        float a = 1e2f;  // 100
        double b = 1e-3; // 0.001

        System.out.format("%f %f", a, b); // 100.000000 0.001000
    }
    ```
- Hằng số NaN của Float và Double đã được định nghĩa trước: *Float.NaN*, *Double.NaN*.  
- Các phép toán không có kết quả xác định về mặt toán học tạo ra *NaN*.  
- Các toán tử số học với *NaN* sẽ tạo ra kết quả *NaN*.  
    ```java
    public static void main(String[] args) {
        float a = Float.NaN; // NaN
        float b = a * 5; 	 // NaN

        System.out.format("%f %f", a, b); // NaN NaN
    }
    ```
- NaN không có thứ tự, vì vậy nếu 1 or cả 2 toán hạng là *NaN*: 
    + Toán tử so sánh ==, >, >=, <, <= đều trả về false,  
    + Toán tử != trả về true.  

    ```java
    public static void main(String[] args) {
        float a = 0.0f / 0.0f;  // NaN
        boolean b = a == a;     // false
        boolean c = a != a;     // true
        boolean d = a >= a;     // false

        System.out.format("%f %b %b %b", a, b, c, d); // NaN false true false 
    }
    ```
- So sánh 2 số +0 và -0 bằng nhau, nhưng một số phép toán sẽ phân biệt +0 và -0, ví dụ:  
    ```java
    public static void main(String[] args) {
        boolean a = 0.0f == -0.0f;  // true
        boolean b = 0.0f > -0.0f;   // false

        float c = 2.0f / 0.0f;  // Infinity
        float d = 2.0f / -0.0f; // -Infinity

        System.out.format("%b %b %f %f", a, b, c, d); // true false Infinity -Infinity
    }
    ```
- Không thể sử dụng toán tử == so sánh bằng giá trị 2 số thực (type float hoặc double), vì cách Java lưu trữ giá trị kiểu số thực, so sánh như vậy kết quả sẽ không chính xác. Để kiểm tra 2 số thực có bằng nhau hay không, cần kiểm tra giá trị của 2 số có hơn kém nhau với khoảng cách quá nhỏ hay không, khoảng cách này là bao nhiêu tùy thuộc vào độ chính xác mong muốn (double <= 1e-9, float <= 1e-6):  
    ```java
    double x = 0.3 * 3 + 0.1;
    double y = 1.0;
    System.out.println(x == y);          // false
    System.out.println(x + " - " + y);   // 0.9999999999999999 - 1
    System.out.println(Math.abs(x - y) <= 1e-9); // true => kết luận x == y

    float a = 10f - 0.1f;
    float b = 10f - 0.1f - 0.1f;
    System.out.println(a == 9.9f);       // true
    System.out.println(b == 9.8f);       // false
    System.out.println(a + " - " + b);   // 9.9 - 9.799999
    System.out.println(Math.abs(9.8f - b) <= 1e-6); // true => kết luận 9.8f == b
    ```

- Hỗ trợ các operators (toán tử):  
    + Toán tử so sánh: <, <=, >, >=, ==, !=  
    + Toán tử số học:  
        + Một ngôi: +, -  
        + Tính toán: +, -, *, /, %  
        + Tăng giảm: ++, --  
    + Toán tử điều kiện: ? :  
    + Toán tử ép kiểu: Chuyển đối từ Floating-Point value sang bất cứ numeric type nào  
    + Toán tử nối chuỗi: +  

- Các constants, constructors, methods khác hỗ trợ cho các values này được định nghĩa trong các Wrapper Class: Float, Double, và class Math.  
- Nếu trong phép toán có ít nhất 1 toán hạng là Floating-Point value, thì phép toán đó là phép toán Floating-Point.  
- Nếu trong phép toán có ít nhất 1 toán hạng kiểu Double, thì sẽ sử dụng độ chính xác 8 bytes, thực hiện mở rộng thành kiểu Double nếu toán hạng còn lại không phải kiểu Double; Nếu không sẽ sử dụng độ chính xác 4 bytes, các toán hạng sẽ được mở rộng thành kiểu Float nếu không phải kiểu Float.  
- Có thể ép kiểu bất cứ value nào của *Floating-Point types* từ or sang bất cứ *numeric types* nào, nhưng không thể ép kiểu giữa *Floating-Point type* và *boolean type*.  
- Khi chuyển đổi Floating-Point Values thành Integer, sẽ sử dụng "làm tròn về 0", tức là loại bỏ phần thập phân:  
    ```java
    public static void main(String[] args) {
        float a = 4.7f;  // 4.7
        int b = (int) a; // 4

        System.out.format("%f %d", a, b); // 4.700000 4
	}
    ```  
- Các phép toán Floating-Point mà *overflow* sẽ tạo ra *Infinity* có dấu,  
- Các phép toán Floating-Point mà *underflow* sẽ tạo ra *giá trị không chuẩn hóa* or *số 0 có dấu*.  
    ```java
    public static void main(String[] args) {
        System.out.println(Float.MAX_VALUE); // 3.4028235E38
        System.out.println(Float.MIN_VALUE); // 1.4E-45
        
        double a = Float.MAX_VALUE * 2; // Infinity
        double b = Float.MIN_VALUE / 2; // 0.0
	}
    ```  
- Các exceptions:  
    + Floating-Point operator ném *NullPointerException* nếu yêu cầu chuyển đổi *unboxing* của *null reference*,  
    + Các operator ++, -- ném *OutOfMemoryError* nếu yêu cầu chuyển đổi *boxing* mà không có đủ bộ nhớ.  


#### *2.2.3, boolean values*

- boolean values gồm 2 giá trị: true và false.  
- boolean operators gồm:  
    + Toán tử quan hệ: ==, !=  
    + Toán tử phủ định: !  
    + Toán tử logic: &, |, ^  
    + Toán tử điều kiện: &&, ||, ? :  
    + Toán tử nối chuỗi: +  
- Chỉ boolean và Boolean expression có thể được sử dụng trong câu lệnh control flow (if, while, do while, for) và là toán hạng đầu của toán tử điều kiện ? :  
- boolean value có thể được convert thành String,     
- boolean value có thể ép kiểu thành type: boolean, Boolean, or Object. Các ép kiểu khác là không được phép.    


### 2.2, Reference Values

#### *2.2.1, objects*

Một *object* là một *class instance* or một *array*.  

*Reference values* là các pointers (con trỏ) đến các *objects*.  

Có một *reference values* đặc biệt là *null* không tham chiếu đến bất cứ *object* nào.  

```java
class Point {
    int x, y;
    Point() { System.out.println("default"); }
    Point(int x, int y) { this.x = x; this.y = y; }

    /* A Point instance is explicitly created at class initialization time: */
    static Point origin = new Point(0,0);

    /* A String can be implicitly created by a + operator: */
    public String toString() { return "(" + x + "," + y + ")"; }
}

class Test {
    public static void main(String[] args) {
        /* A Point is explicitly created using newInstance: */
        Point p1 = null, p2 = null, p3 = null;
        try {
            p1 = (Point) Class.forName("Point").getDeclaredConstructor().newInstance(); // default
            p2 = (Point) Point.class.getDeclaredConstructor(int.class, int.class).newInstance(2, 2);
            p3 = new Point(3, 3);
        } catch (Exception e) {
            System.out.println(e);
        }

        /* An array is implicitly created by an array constructor: */
        Point a[] = { new Point(0,0), new Point(1,1) };

        /* Strings are implicitly created by + operators: */
        System.out.println("p1: " + p1); // p1: (0,0)
        System.out.println("p2: " + p2); // p2: (2,2)
        System.out.println("p3: " + p3); // p3: (3,3)
        System.out.println("a: { " + a[0] + ", " + a[1] + " }"); // a: { (0,0), (1,1) }
    
        /* An array is explicitly created by an array creation expression: */
        String sa[] = new String[2];
        sa[0] = "he"; sa[1] = "llo";
        System.out.println(sa[0] + sa[1]); // hello
    }
}
```

Các operator được hỗ trợ làm việc với các *references*:  

- Truy xuất fields  
- Gọi methods  
- Toán tử ép kiểu (cast operator)  
- Toán tử nối chuỗi +  
- Toán tử *instanceof*  
- Toán tử so sánh cùng tham chiếu ==, !=  
- Toán tử điều kiện ? :  

Có thể có nhiều references tới cùng một object. Hầu hết các object đều có trạng thái (state) được lưu trong các fields của objects. Có thể thay đổi state của object thông qua một reference tới object của một biến, sau đó quan sát thay đổi qua reference trong biến khác:  

```java
class Value { 
    int val; 
}

class Test {
    public static void main(String[] args) {
        int i1 = 3;
        int i2 = i1;
        i2 = 4;
        System.out.format("i1 = %d, i2 = %d", i1, i2); // i1 = 3, i2 = 4

        Value v1 = new Value();
        v1.val = 5;

        Value v2 = v1;
        v2.val = 6;
        
        System.out.println("v1.val = %d, v2.val = %d", v1.val, v2.val); // v1.val = 6, v2.val = 6
    }
}
```


#### *2.2.2, The Object class*

The class *Object* là superclass của tất cả các classes.  

Class *Object* không có direct superclass.  

Tất cả các class và array types đều thừa kế các methods của class *Object*, nói cách khác tất cả các *objects* đều hỗ trợ các methods của class *Object*. Các methods này bao gồm:  

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

Các *notify*, *notifyAll*, và *wait* methods của Object được sử dụng trong concurrent programming (lập trình đồng thời), đóng một vai trò trong việc đồng bộ hóa các hoạt động của các threads chạy độc lập trong một program, bao gồm:

- public final void notify()  
- public final void notifyAll()  
- public final void wait()  
- public final void wait(long timeout)  
- public final void wait(long timeout, int nanos)  


##### *2.2.2.1, The clone() Method*

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


##### *2.2.2.2, The equals() Method*

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


##### *2.2.2.3, The finalize() Method*

Object class cung cấp một callback method, finalize(), được gọi trên một object khi nó trở thành rác. Sự triển khai finalize() của Object không làm bất cứ điều gì — bạn có thể override finalize() để thực hiện dọn dẹp, chẳng hạn như giải phóng tài nguyên.

finalize() method có thể được gọi tự động bởi system, nhưng khi nào nó được gọi, hoặc thậm chí việc nó được gọi, thì không chắc chắn. Do đó, đừng dựa vào method này để thực hiện việc dọn dẹp. Ví dụ: nếu bạn không đóng file descriptors trong mã của mình sau khi thực hiện I/O và mong đợi finalize() đóng chúng cho bạn, bạn có thể mất file descriptors. Thay vào đó, hãy sử dụng một try-with resources statement để tự động đóng các resources của ứng dụng. 


##### *2.2.2.4, The getClass() Method*

KHÔNG thể override getClass() method của Object.

getClass() method trả về một Class object mà có các methods có thể sử dụng để lấy thông tin về class của một object, chẳng hạn như tên của nó - getSimpleName(), superclass của nó - getSuperclass(), và các interfaces nó  implements - getInterfaces(). Ví dụ:

```java
void printClassName(Object obj) {
    System.out.println("The object's class is " + obj.getClass().getSimpleName());
}
```

Class class, trong java.lang package, có một số lượng lớn các methods (hơn 50). Ví dụ, có thể kiểm tra class có phải một annotation - isAnnotation(), một interface - isInterface(), hay một enumeration - isEnum(); hay cũng có thể lấy thông tin của class như các fields - getFields(), hay các methods - getMethods(), ...


##### *2.2.2.5, The hashCode() Method*

Giá trị được trả về bởi hashCode() method là hash code của object, là một *integer value* được tạo bởi một thuật toán băm.

Theo định nghĩa, nếu 2 objects bằng nhau, thì hash code của chúng cũng bằng nhau. Nếu bạn override equals() method của Object, thay đổi cách 2 objects được cho là bằng nhau và sự triển khai hashCode() của Object sẽ không còn hợp lệ. Do đó, nếu override equals() method, cũng phải override hashCode() method.


##### *2.2.2.6, The toString() Method*

Nên override toString() method trong các class của bạn.

toString() method của Object returns một String biểu diễn của object, điều này hữu ích cho việc debug.


#### *2.2.3, The String class*

Các instance của class *String* biểu diễn chuỗi các Unicode code point.  

String class là *immutable* (không đổi), nên các String object có một giá trị không đổi (constant value), các thao tác làm thay đổi chuỗi đều tạo ra một string khác.  

Các *String literals* (chuỗi ký tự) là các references đến các instances của class *String*.  

*Toán tử nối chuỗi +* ngầm định sẽ tạo một String object mới khi kết quả không phải là một biểu thức không đổi (a constant expression).  

**Note**: Giá trị kiểu *String* phải nằm trong cặp dấu nháy kép "", vd: "Hello"  

```java
public static void main(String[] args) {
    String x = "Hello";
    String y = new String("Hello");
    System.out.println(x.equals(y) + ", " + (x == y)); // true, false
}
```
