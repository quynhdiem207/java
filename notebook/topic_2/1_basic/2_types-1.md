## 1. Types

Java là ngôn ngữ định kiểu tĩnh, tức là mọi variable & expression đều có một type được biết đến tại thời điểm compile.  

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


## 2. Values

*Values* có thể được lưu trữ trong *variables*, truyền dưới dạng *arguments*, trả về bởi methods, & tính toán trên chúng.  

Tương ứng với 2 loại *types*, trong Java có 2 loại *values*:  

- *Primitive Values*  
- *Reference Values*  


### 2.1, Primitive Values

- Integral Values:  
    + byte, short, int, long là các số nguyên có dấu.  
    + char là số nguyên không dấu (0 -> 2^16 - 1).  
- Floating-point Values: float, double là các số thực  
- boolean values: true & false  


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
- Có thể ép kiểu bất cứ value nào của *Integral types* từ or sang bất cứ *numeric types* nào, nhưng không thể ép kiểu giữa *integral type* & *boolean type*.  
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

- Floating-Point Values bao gồm: số dương, số âm, 0, -0, Infinity, -Infinity, & NaN.  
- Có thể sử dụng *Extended-Exponent Values* (giá trị lũy thừa mở rộng, vd: 1e5) để biểu diễn Floating-Point Values.  
    ```java
    public static void main(String[] args) {
        float a = 1e2f;  // 100
		double b = 1e-3; // 0.001

        System.out.format("%f %f", a, b); // 100.000000 0.001000
    }
    ```
- Hằng số NaN của Float & Double đã được định nghĩa trước: *Float.NaN*, *Double.NaN*.  
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
- Có thể ép kiểu bất cứ value nào của *Floating-Point types* từ or sang bất cứ *numeric types* nào, nhưng không thể ép kiểu giữa *Floating-Point type* & *boolean type*.  
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

- boolean values gồm 2 giá trị: true & false.  
- boolean operators gồm:  
    + Toán tử quan hệ: ==, !=  
    + Toán tử phủ định: !  
    + Toán tử logic: &, |, ^  
    + Toán tử điều kiện: &&, ||, ? :  
    + Toán tử nối chuỗi: +  
- Chỉ boolean & Boolean expression có thể được sử dụng trong câu lệnh control flow (if, while, do while, for) & là toán hạng đầu của toán tử điều kiện ? :  
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


#### *2.2.2, The class Object*

The class *Object* là superclass của tất cả các classes.  

Tất cả các class & array types đều thừa kế các methods của class *Object*, nói cách khác tất cả các *objects* đều hỗ trợ các methods của class *Object*. Các methods này bao gồm:  

- *clone* method: Tạo bản sao của object,  
- *equals* method: So sánh 2 object dựa vào value mà không dựa vào reference,  
- *finalize* method: Được chạy trước khi object bị phá hủy,  
- *getClass* method: Trả về Class object đại diện cho class của object,  
- *hashCode* method: Hữu ích trong hashtables như java.util.HashMap,  
- *toString* method: Trả về string đại diện cho object,  
- *wait, notify, notifyAll* methods: Sử dụng trong concurrent programming (lập trình đồng thời) sử dụng các threads.  


#### *2.2.3, The class String*

Các instance của class *String* biểu diễn chuỗi các Unicode code point.  

Một String object có một giá trị không đổi (a constant value).  

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
