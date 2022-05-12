# Lession 6. Numbers and Strings

## 1. Numbers

### 1.1, The Numbers Classes

Khi làm việc với numbers, đa phần chúng ta sẽ thường sử dụng primitive types. Tuy nhiên, đôi khi có những lý do để sử dụng các objects thay cho các primitive values. Java cung cấp các wrapper classes cho mỗi primitive data types, các class này "wrap" (bọc) các primitive value trong một object. Thông thường, việc bao bọc được thực hiện bởi compiler — nếu bạn sử dụng một primitive value ở nơi mong đợi một object, thì compiler sẽ *box* (đóng hộp) primitive value trong một object của wrapper class tương ứng của nó cho bạn. Tương tự, nếu bạn sử dụng một number object khi mong đợi một primitive value, thì compiler sẽ unbox (mở hộp) object đó cho bạn.

Tất cả các numeric wrapper classes đều là các subclasses của abstract class *Number*: Byte, Short, Integer, Long, Float, Double.  

**Note**: Ngoài ra còn có 4 subclass khác của Number: BigDecimal và BigInteger được sử dụng cho phép tính có độ chính xác cao, AtomicInteger và AtomicLong được sử dụng cho các ứng dụng đa luồng.  

Có 3 lý do mà có thể sử dụng *Number object* thay vì một primitive value:

- Dưới dạng một argument của một method mong đợi một object (thường được sử dụng khi thao tác với các collections của numbers).  
- Để sử dụng các constants được định nghĩa bởi class, chẳng hạn như MIN_VALUE và MAX_VALUE, cung cấp giới hạn trên và giới hạn dưới của data type.  
- Để sử dụng các class methods cho việc chuyển đổi giá trị sang và từ các primitive types khác, chuyển đổi sang và từ các string, và chuyển đổi giữa các number systems (decimal, octal, hexadecimal, binary).  

Có thể gọi constructor của các Number subclass để tạo object:

```java
Integer n = new Integer(2);
Integer n = new Integer('a');
Integer n = new Integer("2");
```

Các instance methods mà tất cả các subclass của Number triển khai:  

```
        Method                        |                  Description
--------------------------------------|------------------------------------------------------------
byte byteValue()                      | Trả về của primitive value tương ứng của Number object này.
short shortValue()                    | 
int intValue()                        |
long longValue()                      |
float floatValue()                    |
double doubleValue()                  |
--------------------------------------|------------------------------------------------------------
int compareTo(Byte anotherByte)       | So sánh Number object với argument được truyền vào.
int compareTo(Double anotherDouble)   |
int compareTo(Float anotherFloat)     |
int compareTo(Integer anotherInteger) |
int compareTo(Long anotherLong)       |
int compareTo(Short anotherShort)     |
--------------------------------------|------------------------------------------------------------
boolean equals(Object obj)            | - Xác định Number object này có bằng argument hay không.
                                      | - Return true nếu argument không phải null và là object của 
                                      |   cùng type và với cùng numeric value.
                                      | - Có một vài yều cầu bổ sung cho Double và Float objects
                                      |   được mô tả trong Java API documentation.
```

Mỗi Number subclass chứa các methods khác hữu ích cho việc chuyển đổi các numbers sang hay từ các strings và chuyển đổi giữa các number systems. Các methods trong *Integer class* (Các Number subclass khác tương tự) bao gồm:

```
        Method                              |                  Description
--------------------------------------------|------------------------------------------------------
static Integer decode(String s)             | - Decodes một string thành một integer.
                                            | - Có thể chấp nhận một string biểu diễn của decimal,
                                            |   octal, hay hexadecimal numbers làm input.
static int parseInt(String s)               | - Return một integer (chỉ decimal).
static int parseInt(String s, int radix)    | - Return một integer, cho trước một string biểu diễn
                                            |   của decimal, octal, binary hay hexadecimal numbers
                                            |   làm input với cơ số tương ứng 10, 8, 2, 16.
String toString()                           | - Return String object đại diện cho value của Integer này.
static String toString(int i)               | - Return String object đại diện cho integer xác định.
static Integer valueOf(int i)               | - Return Integer object giữ primitive value xác định.
static Integer valueOf(String s)            | - Return Integer object giữ value của string biểu diễn xác định.
static Integer valueOf(String s, int radix) | - Return Integer object giữ value của string biểu diễn xác định,
                                            |   với cơ số xác định.
```


### 1.2, Formatting Numeric Print Output

Có thể sử dụng **print** và **println** để in ra các string cho standard output (System.out). Vì tất cả các numbers đều có thể được chuyển đổi thành string, nên có thể sử dụng các methods này để in ra hỗn hợp cả strings và numbers. Tuy nhiên Java còn có các methods khác cho phép kiểm soát việc in ra output bao gồm các numbers.


#### *1.2.1, The printf and format Methods*

*java.io* package bao gồm một **PrintStream** class có 2 formatting methods có thể sử dụng để thay thế *print* và *println*. Các methods này là **format** và **printf**, chúng tương đương với nhau. *System.out* quen thuộc mà bạn đang sử dụng là một *PrintStream object*, bởi vậy có thể gọi các PrintStream methods trên System.out.

```java
System.out.printf();
System.out.format();
```

Cú pháp của 2 method này giống nhau:

```java
/**
 * In ra string được định dạng, sử dụng một format string xác định và danh sách các arguments.
 *
 * @param format format string xác định định dạng được sử dụng.
 * @param args   list các arguments sẽ được in ra với định dạng format.
 * @return PrintStream object.
 */
public PrintStream format(String format, Object... args)
public PrintStream printf(String format, Object... args)
```

First parameter, format, là một format string xác định cách các objects trong second parameter, args, được định dạng. Format string chứa văn bản thuần túy cũng như các *format specifiers* (mã định dạng), là các ký tự đặc biệt định dạng các arguments của Object... args. 

Các methods này được overload, ví dụ:  

```java
public PrintStream format(Locale l, String format, Object... args)
```

*Format specifiers* bắt đầu bằng ký tự % và kết thúc bằng một *converter*. Converter là một ký tự chỉ ra type của argument sẽ được định dạng. Ở giữa ký tự % và converter có thể có các optional flags và specifiers. 

Trong *java.util.Formatter* có nhiều converters, flags, và specifiers:

```
Converter |             Explanation
----------|--------------------------------------------------------------------------
d         | Một decimal integer.
f         | Một float.
n         | Một ký tự new line phù hợp với platform chạy app (sử dụng %n thay vì \n).
tB        | Một chuyển đổi date & time - locale-specific full name of month.
tm        | Một chuyển đổi date & time - months in 2 digits, với 0 đứng đầu khi cần.
td, te    | Một chuyển đổi date & time - 2-digit day of month.
          | -> td có chữ số 0 đứng đầu khi cần thiết, còn te thì không.
ty, tY    | Một chuyển đổi date & time - ty = 2-digit year, tY = 4-digit year.
tD        | Một chuyển đổi date & time - date dưới dạng %tm%td%ty
tl        | Một chuyển đổi date & time - hour in 12-hour clock.
tM        | Một chuyển đổi date & time - minutes in 2 digits, với 0 đứng đầu khi cần.
tp        | Một chuyển đổi date & time - locale-specific am/pm (lower case).


Flag |             Explanation
-----|-------------------------------------------------------------------------------
08   | Chiều rộng tám ký tự, với các chữ số 0 ở đầu nếu cần.
+    | Bao gồm dấu, dù là positive hay negative.
,    | Bao gồm các ký tự gom nhóm theo locale-specific.
-    | Căn chỉnh trái.
.3   | 3 ký tự sau dấu thập phân.
10.3 | Chiều rộng 10 ký tự, căn phải, với 3 ký tự sau dấu thập phân.
```

Ngoài ra, một format specifier có thể chứa một argument index chỉ định vị trí của argument cho nó, một argument có thể được tham chiếu nhiều hơn 1 lần, argument đầu tiên là 1$. Hoặc có thể sử dụng ký tự < để sử dụng lại argument cho format specifier trước đó.  

**Note**: In các numbers trong French system, trong đó dấu phẩy được sử dụng thay cho vị trí thập phân trong cách biểu diễn các floating point numbers của English system.

*Ví dụ*:

```java
import java.util.Calendar;
import java.util.Locale;

public class TestFormat {
    
    public static void main(String[] args) {
        long n = 461012;
        System.out.format("%d%n", n);      //  "461012"
        System.out.format("%08d%n", n);    //  "00461012"
        System.out.format("%+8d%n", n);    //  " +461012"
        System.out.format("%,8d%n", n);    //  " 461,012"
        System.out.format("%+,8d%n%n", n); //  "+461,012"
        
        double pi = Math.PI;
        System.out.format("%f%n", pi);                       //  "3.141593"
        System.out.format("%.3f%n", pi);                     //  "3.142"
        System.out.format("%10.3f%n", pi);                   //  "     3.142"
        System.out.format("%-10.3f%n", pi);                  //  "3.142"
        System.out.format(Locale.FRANCE, "%-10.4f%n%n", pi); //  "3,1416"

        Calendar c = Calendar.getInstance();
        System.out.format("%tB %te, %tY%n", c, c, c); //  "May 29, 2006"
        System.out.format("%tl:%tM %tp%n", c, c, c);  //  "2:34 am"
        System.out.format("%tD%n", c);                //  "05/29/06"

        // "3.141593, +00000003.1415926536"
        System.out.format("%f, %1$+020.10f %n", Math.PI);
        System.out.format("%f, %<+020.10f %n", Math.PI);
    }
}
```


#### *1.2.2, The DecimalFormat Class*

Có thể sử dụng **java.text.DecimalFormat** class để kiểm soát việc hiển thị số 0 ở đầu và cuối, tiền tố và hậu tố, dấu phân tách nhóm (hàng nghìn), dấu phân tách thập phân. DecimalFormat cung cấp tính linh hoạt rất lớn trong việc định dạng numbers, nhưng nó có thể làm cho code phức tạp hơn.

```java
import java.text.*;

public class DecimalFormatDemo {
    static public void customFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        System.out.println(output);
    }

    static public void main(String[] args) {
        customFormat("###,###.###", 123456.789); // 123,456.789
        customFormat("###.##", 123456.789);      // 123456.79
        customFormat("000000.000", 123.78);      // 000123.780
        customFormat("$###,###.###", 12345.67);  // $12,345.67
    }
}
```


### 1.3, Beyond Basic Arithmetic

Java hỗ trợ số học cơ bản với các toán tử số học: +, -, *, /, %. **java.lang.Math** class cung cấp các methods và constants cho việc thực hiện tính toán toán học nâng cao hơn.

Các methods trong Math class đều là *static*, vì vậy có thể gọi trực tiếp từ class như:

```java
import java.lang.Math;
Math.cos(angle);
```

**Note**: Sử dụng *static import feature*, để không phải viết Math trước mỗi math function, điều này cho phép gọi các Math class methods bằng simple name của chúng:

```java
import static java.lang.Math.*;
cos(angle);
```


#### *1.3.1, Constants and Basic Methods*

Math class gồm 2 constants:

- **Math.E**, là cơ số của logarit tự nhiên,  
- **Math.PI**, là số pi.  

Math class gồm hơn 40 static methods. Các methods cơ bản gồm:

```
    Method	                        |               Description
------------------------------------|-----------------------------------------------------------------
double abs(double d)                | Trả về giá trị tuyệt đối của arguments.
float abs(float f)                  |
int abs(int i)                      |
long abs(long lng)	                |
------------------------------------|-----------------------------------------------------------------
double ceil(double d)	            | Trả về một số nguyên, là giá trị được làm tròn lên của argument.
double floor(double d)	            | Trả về một số nguyên, là giá trị được làm tròn xuống của argument.
double rint(double d)	            | Trả về một số nguyên, là giá trị gần nhất của argument.
long round(double d)                | Trả về một số nguyên, là giá trị gần nhất của argument.
int round(float f)	                |
------------------------------------|-----------------------------------------------------------------
double min(double arg1, double arg2)| Trả về giá trị nhỏ hơn trong 2 arguments.
float min(float arg1, float arg2)   | 
int min(int arg1, int arg2)         |
long min(long arg1, long arg2)	    | 
------------------------------------|-----------------------------------------------------------------
double max(double arg1, double arg2)| Trả về giá trị lớn hơn trong 2 arguments.
float max(float arg1, float arg2)   |
int max(int arg1, int arg2)         |
long max(long arg1, long arg2)	    | 
```

*Ví dụ:*

```java
public class BasicMathDemo {
    public static void main(String[] args) {
        double a = -191.635;
        double b = 43.74;
        int c = 16, d = 45;

        System.out.printf("The absolute value of %.3f is %.3f%n", a, Math.abs(a));  // 191.635
        System.out.printf("The ceiling of %.2f is %.0f%n", b, Math.ceil(b));        // 44
        System.out.printf("The floor of %.2f is %.0f%n", b, Math.floor(b));         // 43
        System.out.printf("The rint of %.2f is %.0f%n", b, Math.rint(b));           // 44
        System.out.printf("The max of %d and %d is %d%n", c, d, Math.max(c, d));    // 45
        System.out.printf("The min of of %d and %d is %d%n", c, d, Math.min(c, d)); // 16
    }
}
```


#### *1.3.2, Exponential and Logarithmic Methods*

Các methods lũy thừa và logarit của Math class gồm:

```
    Method	                            |               Description
----------------------------------------|-----------------------------------------
double exp(double d)	                | Trả về giá trị e^d.
double log(double d)	                | Trả về giá trị ln(d).
double pow(double base, double exponent)| Trả về giá trị base^(exponent).
double sqrt(double d)	                | Trả về giá trị căn bậc 2 của argument d.
```

*Ví dụ:*

```java
public class ExponentialDemo {
    public static void main(String[] args) {
        double x = 11.635;
        double y = 2.76;

        System.out.printf("The value of " + "e is %.4f%n", Math.E);                // 2.7183
        System.out.printf("exp(%.3f) " + "is %.3f%n", x, Math.exp(x));             // 112983.831
        System.out.printf("log(%.3f) is " + "%.3f%n", x, Math.log(x));             // 2.454
        System.out.printf("pow(%.3f, %.3f) " + "is %.3f%n", x, y, Math.pow(x, y)); // 874.008
        System.out.printf("sqrt(%.3f) is " + "%.3f%n", x, Math.sqrt(x));           // 3.411
    }
}
```


#### *1.3.3, Trigonometric Methods*

Math class cung cấp một tập các hàm lượng giác, argument được truyền cho các methods này là một góc được biểu thị bằng đơn vị radians, có thể sử dụng *toRadians* method để convert từ degrees sang radians.

```
    Method	                    |               Description
--------------------------------|--------------------------------------------------------------------
double sin(double d)	        | Returns sin(d).
double cos(double d)	        | Returns cos(d).
double tan(double d)	        | Returns tan(d).
double asin(double d)	        | Returns arcsin(d).
double acos(double d)	        | Returns arccos(d).
double atan(double d)	        | Returns arctan(d).
double atan2(double y, double x)| Converts từ tọa độ hình chữ nhật (x, y) thành tọa độ cực (r, theta).
                                | Returns theta.
double toDegrees(double d)      | Converts argument từ radians sang degrees.
double toRadians(double d)	    | Converts argument từ degrees sang radians.
```

*Ví dụ:*

```java
public class TrigonometricDemo {
    public static void main(String[] args) {
        double degrees = 45.0;
        double radians = Math.toRadians(degrees); // π/4
        
        System.out.format("The value of pi is %.4f%n", Math.PI); // 3.1416

        System.out.format(
            "The arcsine of %.4f is %.4f degrees %n", 
            Math.sin(radians), // 0.7071
            Math.toDegrees(Math.asin(Math.sin(radians))) // 45.0000
        );

        System.out.format(
            "The arccosine of %.4f is %.4f degrees %n", 
            Math.cos(radians),  // 0.7071
            Math.toDegrees(Math.acos(Math.cos(radians))) // 45.0000
        );

        System.out.format(
            "The arctangent of %.4f is %.4f degrees %n", 
            Math.tan(radians), // 1.0000
            Math.toDegrees(Math.atan(Math.tan(radians))) // 45.0000
        );
    }
}
```


#### *1.3.4, Random Numbers*

random() method trả về một number được chọn ngẫu nhiên trong khoảng 0.0 đến 1.0, nói cách khác 0.0 <= Math.random() < 1.0. 

Để nhận một số ngẫu nhiên nằm trong một phạm vi khác, cần phải tính toán với giá trị ngẫu nhiên nhận được từ Math.random(). 

*ví dụ: Trả về một số nguyên ngẫu nhiên nằm trong phạm vi 0 - 9:*

```java
int number = (int)(Math.random() * 10); // 0.0 <= number < 10.0.
```

**Note**: Sử dụng *Math.random* khi cần tạo một number ngẫu nhiên. Nếu cần tạo một chuỗi các numbers ngẫu nhiên, nên tạo một instance của **java.util.Random** và gọi các methods của nó để tạo các numbers.


## 2. Characters

Khi sử dụng một character value riêng lẻ, đa phần chúng ta sẽ thường sử dụng primitive char type, ví dụ như:

```java
char ch = 'a'; 
char uniChar = '\u03A9'; // Unicode for uppercase Greek Omega character Ω
char[] charArray = { 'a', 'b', 'c', 'd', 'e' }; // an array of chars
``` 

Tuy nhiên, đôi khi cần sử dụng một char value dưới dạng một object — vd: dưới dạng argument cho method mong đợi một object. Java cung cấp một *wrapper class* để "wrap" (bọc) char value trong một *Character object*. Một object của type Character chứa một field duy nhất có type là char. Character class cung cấp một số class (static) methods hữu ích để thao tác với các ký tự.

Có thể tạo một Character object với Character constructor:

```java
Character ch = new Character('a');
```

Java compiler sẽ tự động tạo một Character object bọc lại char value nhận được trong trường hợp cần thiết. Ví dụ: khi truyền primitive char cho một method muốn nhận một object, compiler sẽ tự động convert primitive value thành một Character object. Chuyển đổi này được gọi là autoboxing, hoặc unboxing, nếu chuyển đổi ngược lại.

**Note**: Character class là *immutable*, vì vậy khi Character object được tạo, nó không thể bị thay đổi.

Một số *static methods* hữu ích của Character class:

```
    Method	                 |               Description
-----------------------------|-------------------------------------------------------------------
boolean isLetter(char ch)    | Xác định một char value là chữ cái hay chữ số.
boolean isDigit(char ch)	 |
-----------------------------|-------------------------------------------------------------------
boolean isWhitespace(char ch)| Xác định một char value có phải khoảng trắng hay không.
-----------------------------|-------------------------------------------------------------------
boolean isUpperCase(char ch) | Xác định một char value là uppercase hay lowercase.
boolean isLowerCase(char ch) |
-----------------------------|-------------------------------------------------------------------
char toUpperCase(char ch)    | Trả về dạng uppercase hay lowercase của một char value xác định.
char toLowerCase(char ch)	 | 
-----------------------------|-------------------------------------------------------------------
toString(char ch)	         | Trả về một String object biểu diễn một char value - chuỗi 1 ký tự.
```


### 2.1, Escape Sequences

Một ký tự được đứng trước bởi một backslash (/) là một *escape sequence*, nó mang một ý nghĩa đặc biệt đối với compiler.

Các Java escape sequences bao gồm:

```
\t  | Chèn một tab trong văn bản tại điểm này.
\b	| Chèn một backspace trong văn bản tại điểm này.
\n	| Chèn một newline trong văn bản tại điểm này.
\r	| Chèn một dấu xuống dòng trong văn bản tại điểm này.
\f	| Chèn một form feed (trang kế tiếp) trong văn bản tại điểm này.
\'	| Chèn một dấu nháy đơn trong văn bản tại điểm này.
\"	| Chèn một dấu nháy kép trong văn bản tại điểm này.
\\	| Chèn một ký tự backslash trong văn bản tại điểm này.
```

Khi bắt gặp một escape sequence trong một print statement, compiler sẽ diễn giải nó phù hợp. Ví dụ:

```java
System.out.println("She said \"Hello!\" to me."); // She said "Hello!" to me.
```


## 3. Autoboxing and Unboxing

**Autoboxing** là sự chuyển đổi tự động mà Java compiler thực hiện từ primitive values thành một object của wrapper class tương ứng của chúng, và **unboxing** là sự chuyển đổi ngược lại. Ví dụ:

```java
Character ch = 'a'; // autoboxing
char c = ch;        // boxing
```

Autoboxing và unboxing cho phép developers viết code rõ ràng, dễ đọc hơn. Các primitive types và wrapper classes tương ứng của chúng được Java compiler sử dụng cho autoboxing và unboxing là:

```
Primitive type  |  Wrapper class
----------------|----------------
boolean	        |  Boolean
byte	        |  Byte
char	        |  Character
float	        |  Float
int	            |  Integer
long	        |  Long
short	        |  Short
double	        |  Double
```

*Ví dụ:*

```java
List<Integer> li = new ArrayList<>();
for (int i = 1; i < 50; i += 2)
    li.add(i);
```

*Trong ví dụ trên, mặc dù thêm các int values dưới dạng các primitive types, không phải các Integer objects, vào List<Integer> li, nhưng compiler không đưa ra compile-time error, vì nó tạo một Integer object từ primitive value i và thêm object đó vào li. Do đó, tại runtime, compiler chuyển đổi code trước đó thành như sau:*

```java
List<Integer> li = new ArrayList<>();
for (int i = 1; i < 50; i += 2)
    li.add(Integer.valueOf(i));
```

Chuyển đổi một primitive value (vd: int value) thành một object của wrapper class tương ứng (Integer) được gọi là *autoboxing*. Java compiler áp dụng autoboxing khi một primitive value:

- Được truyền dưới dạng argument cho một method mong đợi một object của wrapper class tương ứng.  
- Được gán cho một variable của wrapper class tương ứng.  

```java
public static int sumEven(List<Integer> li) {
    int sum = 0;
    for (Integer i: li)
        if (i % 2 == 0)
            sum += i;
    return sum;
}
```

*Trong ví dụ trên, các toán tử % và += không áp dụng cho Integer object, nhưng compiler không đưa ra compile-time error, vì nó gọi intValue() method để trả về int value của Integer object tại runtime:*

```java
public static int sumEven(List<Integer> li) {
    int sum = 0;
    for (Integer i : li)
        if (i.intValue() % 2 == 0)
            sum += i.intValue();
    return sum;
}
```

Chuyển đổi một object của một wrapper type (vd: Integer) thành primitive value (int) được gọi là *unboxing*. Java compiler áp dụng unboxing khi một object của wrapper class:

- Được truyền dưới dạng một argument cho một method mong đợi một value của primitive type tương ứng.  
- Được gán cho một variable của primitive type tương ứng.  

```java
public class Unboxing {
    public static void main(String[] args) {
        Integer i = new Integer(-8);

        // 1. Unboxing through method invocation
        int absVal = absoluteValue(i);
        System.out.println("absolute value of " + i + " = " + absVal);

        List<Double> ld = new ArrayList<>();
        ld.add(3.1416);    // π is autoboxed through method invocation.

        // 2. Unboxing through assignment
        double pi = ld.get(0);
        System.out.println("pi = " + pi);
    }

    public static int absoluteValue(int i) {
        return (i < 0) ? -i : i;
    }
}
```