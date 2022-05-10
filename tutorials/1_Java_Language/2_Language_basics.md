# Lession 2. Language Basics

- **Variables**: variables, array, data types, default value, literals  
- **Operators**  
- **Expressions, Statements, Blocks**: 
    + Các operators có thể được sử dụng trong việc xây dựng các expressions tính toán giá trị,  
    + Các expressions là thành phần cốt lõi của các statements,  
    + Các statements có thể được gom nhóm thành các blocks.  
- **Control Flow Statements**: Bao gồm các statements cho phép thực thi có điều kiện các blocks cụ thể.  


## 1. Variables

Java định nghĩa các loại variables sau:  

- **Instance Variables (Non-Static Fields)**: object lưu trữ các state riêng của chúng trong các *non-static fields*, còn được gọi là *instance variable*, vì giá trị của chúng là riêng biệt đối với mỗi instance của một class.  
- **Class Variables (Static Fields)**: chỉ tồn tại duy nhất một bản sao của các *class variables*, hay còn gọi là *static fields*, dù cho có bao nhiêu instance của class được tạo.  
- **Local Variables**: một method thường sẽ lưu trữ các state tạm thời của nó trong các *local variables*, chúng chỉ hiển thị (visible) với method mà chúng được khai báo.  
- **Parameters**: là các varables cung cấp thêm thông tin cho một method; bao gồm method parameter, constructor parameter, lambda parameter, exception parameter.  


## 2. Primitive Data Types

Data type của một variable giới hạn phạm vi của variable, xác định các giá trị mà variable có thể chứa, cùng với các thao tác có thể được thực hiện trên đó.

Primitive values không chia sẻ state với các primitive values khác.

Java hỗ trợ 8 primitive data type được định nghĩa trước, chúng được đặt tên bởi các keyword dành riêng, gồm:

- **byte**: là một kiểu số nguyên có dấu 8-bit, có khoảng giá trị là [-128, 127].  
- **short**: là một kiểu số nguyên có dấu 16-bit, có khoảng giá trị là [-32_768, 32_767] = [-2^15, 2^15-1].  
- **int**: Theo mặc định, int là kiểu số nguyên có dấu 32-bit, có khoảng giá trị là [-2^31, 2^31-1].  

    *Trong Java SE 8 trở lên, có thể sử dụng int để đại diện cho một số nguyên 32-bit không dấu, có khoảng giá trị là [0, 2^32-1]. Sử dụng class Integer để sử dụng int như một số nguyên không dấu. Các static methods như CompareUnsigned, splitUnsigned, ... đã được thêm vào class Integer để hỗ trợ các phép toán số học cho các số nguyên không dấu.*  

- **long**: Theo mặc định, long là kiểu số nguyên có dấu 64-bit, khoảng giá trị là [-2^63, 2^63-1].  
    
    *Trong Java SE 8 trở lên, có thể sử dụng long để đại diện cho một số nguyên 64-bit không dấu, có khoảng giá trị là [0, 2^64-1]. Sử dụng class Long để sử dụng long như một số nguyên không dấu. Các static methods như CompareUnsigned, splitUnsigned, ... đã được thêm vào class Long để hỗ trợ các phép toán số học cho các số nguyên không dấu.*  

- **float**: là một kiểu floating-point 32-bit.  

    *Không sử dụng float cho các giá trị chính xác như tiền tệ, thay vào đó cần sử dụng java.math.BigDecimal thay thế.*  

- **double**: là một kiểu floating-point 64-bit.  
    
    *Không sử dụng float và double cho các giá trị chính xác như tiền tệ, thay vào đó cần sử dụng *java.math.BigDecimal thay thế.*  

- **boolean**: đại diện cho 1-bit thông tin, chỉ có thể có 2 giá trị: true và false.  
- **char**: là một ký tự Unicode 16-bit, mỗi ký tự tương ứng với một số nguyên không dấu, có giá trị tối thiểu là '\u0000' (hoặc 0) và giá trị lớn nhất là '\uffff' (hoặc 65,535).  

Ngoài ra, Java cung cấp class **java.lang.String** đại diện cho các chuỗi ký tự.


## 3. Default Values

Không phải lúc nào cũng cần gán một giá trị khi một trường được khai báo. Các fields được khai báo nhưng không được khởi tạo sẽ được compiler đặt thành mặc định hợp lý, tùy thuộc vào data type. Tuy nhiên, không nên dựa vào các default value như vậy.  

```
Data Type	    Default Value (for fields)
byte	                    0
short	                    0
int	                        0
long	                    0L
float	                    0.0f
double	                    0.0d
char	                    '\u0000'
boolean	                    false
String (or any object)  	null
```

Compiler không bao giờ gán default value cho một local variable, nó cần được gán giá trị trước khi sử dụng.


## 4. Literals

Primitive data types là các types đặc biệt được tích hợp sẵn trong Java, chúng không phải là các object được tạo ra từ một class, từ khóa new không được sử dụng khi khởi tạo một variable của primitive types.

Một literal là biểu diễn mã nguồn của một giá trị cố định; các literals được biểu diễn trực tiếp trong source code mà không cần tính toán.

```java
boolean result = true;
char capitalC = 'C';
byte b = 100;
short s = 10000;
int i = 100000;
```


### 4.1, Integer Literals

Một integer literal có kiểu long nếu nó kết thúc bằng chữ cái L hoặc l; nếu không thì nó thuộc kiểu int.

Giá trị của các integral types: byte, short, int và long có thể được tạo từ các ký tự int. Giá trị của kiểu long vượt quá phạm vi int có thể được tạo từ các long literals. 

Các integer literals có thể được biểu thị bằng các number system sau:

- Decimal: Base 10, có các chữ số bao gồm các số từ 0-9;  
- Hexadecimal: Base 16, có các chữ số bao gồm các số từ 0-9 và các chữ cái từ A-F;  
- Octal: Base 8, có các chữ số bao gồm các số từ 0-7;  
- Binary: Base 2, có các chữ số bao gồm các số từ 0 và 1 (có thể tạo các binary literals từ Java SE 7 trở lên).  

Tiền tố 0x biểu thị hệ thập lục phân (hexadecimal), 0 biểu thị hệ bát phân (octal), và 0b biểu thị hệ nhị phân (Binary).

```java

int decVal = 26;      // The number 26, in decimal
int hexVal = 0x1a;    // The number 26, in hexadecimal
int octVal = 032;     // The number 26, in octal
int binVal = 0b11010; // The number 26, in binary
```


### 4.2, Floating-Point Literals

Một floating-point literal thuộc type *float* nếu nó kết thúc bằng chữ cái F hoặc f; nếu không thì type của nó là *double* và nó có thể tùy chọn kết thúc bằng chữ cái D hoặc d.

Các floating-point literal (float và double) cũng có thể được thể hiện bằng cách sử dụng E hoặc e, F hoặc f (32-bit float literal) và D hoặc d (64-bit double literal; đây là mặc định và theo quy ước bị bỏ qua).

```java
double d1 = 123.4;
double d2 = 1.234e2;
float f1  = 123.4f;
```


### 4.3, Character and String Literals

Các char literals và String literals có thể chứa bất kỳ Unicode (UTF-16) characters nào, cũng có thể sử dụng "Unicode escape" như '\u0108', hoặc "S\u00ED Se\u00F1or" (Sí Señor in Spanish). Unicode escape sequences có thể sử dụng ở bất cứ đâu trong chương trình (như trong field names), không chỉ trong char or String literals.  

Luôn sử dụng 'single quotes' cho char literals và "double quotes" cho String literals.  

Java cũng hỗ trợ một số escape sequences đặc biệt cho char và String literals: \b (backspace), \t (tab), \n (line feed), \f (form feed), \r (carriage return), \" (double quote), \' (single quote), and \\ (backslash).

Ngoài ra còn có một *null* literal đặc biệt có thể được sử dụng như một giá trị cho bất kỳ reference type nào.  null thường được sử dụng trong chương trình như là một đánh dấu để chỉ ra object không khả dụng (unavailable).

Cuối cùng, còn có một loại literal đặc biệt gọi là class literal, ví dụ: String.class, chúng tham chiếu đến object (của type Class) đại diện cho chính bản thân type đó.  


### 4.4, Using Underscore Characters in Numeric Literals

Từ Java SE 7 trở lên, có thể sử dụng ký tự gạch dưới (_) để phân tách các nhóm chữ số trong các numerical literals, điều này giúp cải thiện khả năng đọc mã.  

```java
long creditCardNumber = 1234_5678_9012_3456L;
long socialSecurityNumber = 999_99_9999L;
float pi =  3.14_15F;
long hexBytes = 0xFF_EC_DE_5E;
long hexWords = 0xCAFE_BABE;
long maxLong = 0x7fff_ffff_ffff_ffffL;
byte nybbles = 0b0010_0101;
long bytes = 0b11010010_01101001_10010100_10010010;
```


## 5. Arrays

Array là một container object (đối tượng vùng chứa) chứa một số lượng cố định các giá trị của một type duy nhất. Độ dài của mảng được thiết lập khi mảng được tạo. Sau khi tạo, chiều dài của nó là cố định.

Mỗi item trong một array được gọi là một *element* (phần tử) và mỗi element được truy cập bằng numerical index của nó. Index bắt đầu từ 0 tương ứng với element đầu tiên của mảng, element cuối cùng sẽ là length-1.  

```java
class ArrayDemo {
    public static void main(String[] args) {
        int[] anArray;         // declares an array of integers
        anArray = new int[10]; // allocates memory for 10 integers
        
        // initialize the elements
        for(int i = 0; i <= 9; i++) {
            anArray[i] = i * 100;
        }

        for(int i: anArray) {
            System.out.println(i);
        }
    }
} 
```

Xem chi tiết về [arrays](../../specifications/10_arrays.md).


### 5.1, Declaring a Variable to Refer to an Array

Một array declaration có 2 thành phần: type của array và tên của array:

```
type[] identifier;
```

trong đó type là type của các element được chứa trong array.

Khai báo không thực sự tạo ra một mảng; nó chỉ đơn giản cho compiler biết rằng biến này sẽ chứa một mảng có type được chỉ định.

```java
byte[] anArrayOfBytes;
short[] anArrayOfShorts;
int[] anArrayofInts;
long[] anArrayOfLongs;
float[] anArrayOfFloats;
double[] anArrayOfDoubles;
boolean[] anArrayOfBooleans;
char[] anArrayOfChars;
String[] anArrayOfStrings;
```

Xem chi tiết về [arrays](../../specifications/10_arrays.md).


### 5.2, Creating, Initializing, and Accessing an Array

Một cách để tạo một array là sử dụng new operator:  

- Nếu không xác định độ dài của array, thì phải có một array initializer để xác định số phần tử của mảng đồng thời khởi tạo giá trị tương ứng cho các phần tử của mảng.  
- Nếu xác định độ dài của mảng, thì các phần tử của mảng sẽ được khởi tạo thành default value tùy thuộc vào type.  

```java
byte[] byteArr = new byte[10];
int[] intArr = new int[] { 2, 5, 4, 8 };
```

Một cách khác để tạo một array là sử dụng array initializer, mảng sẽ chứa các phần tử được khởi tạo thành các giá trị được liệt kê tương ứng:  

```java
int[] intArr = { 5, 6, 8 };
```

Có thể khai báo mảng đa chiều, số chiều của mảng là số cặp ngoặc [] được khai báo:  

```java
String[][] strArr = new String[3][]; // 2 dimention array
```

Các phần tử của mảng được truy xuất bằng cách sử dụng index expression:  

```java
for(int i = 0;  i < 9; i++) {
    System.out.println(intArr[i]);
}
```

Có thể sử dụng final field **length** để lấy độ dài của một mảng:

```java
System.out.println(intArr.length);
```

Xem chi tiết về [arrays](../../specifications/10_arrays.md).


### 5.3, Copying Arrays

Có thể sử dụng static method **arraycopy** của **System** class để copy data từ một array sang một array khác:

```java
public static void arraycopy(
    Object src,     // mảng nguồn
    int srcPos,     // vị trí bắt đầu trong mảng nguồn
    Object dest,    // mảng đích
    int destPos,    // vị trí bắt đầu trong mảng đích
    int length      // số phần tử cần copy
)
```

*Ví dụ*:

```java
class ArrayCopyDemo {
    public static void main(String[] args) {
        String[] copyFrom = {
            "Affogato", "Americano", "Cappuccino", "Corretto", "Cortado",   
            "Doppio", "Espresso", "Frappucino", "Freddo", "Lungo", 
            "Macchiato", "Marocchino", "Ristretto" 
        };
        
        String[] copyTo = new String[7];
        System.arraycopy(copyFrom, 2, copyTo, 0, 7);
        for (String coffee : copyTo) {
            System.out.print(coffee + " ");           
        }
    }
}
// Output: Cappuccino Corretto Cortado Doppio Espresso Frappucino Freddo
```

Ngoài ra, array object có thể gọi **clone** method để nhân bản tạo ra một array mới:

```java
class Test1 {
    public static void main(String[] args) {
        int[] ia1 = { 1, 2 };
        int[] ia2 = ia1.clone();
        System.out.print((ia1 == ia2) + " "); // false
        ia1[1]++;
        System.out.println(ia2[1]); // 2

        int[][] ia = { {1,2}, null };
        int[][] ja = ia.clone();
        System.out.print((ia == ja) + " "); // false
        System.out.println(ia[0] == ja[0] && ia[1] == ja[1]); // true
    }
}
```


### 5.4, Array Manipulations

Java cung cấp class **java.util.Arrays** chứa một số methods để thực hiện các thao tác với mảng:

- **asList**: Trả về một list có kích thước cố định dựa vào một array xác định.  
- **binarySearch**: Tìm kiếm trong mảng một phần tử có giá trị xác định, trả về index của phần tử.  
- **copyOf**: Copy một mảng xác định, trả về một mảng mới, cắt bớt hoặc đệm vào các phần tử với giá trị mặc định để có độ dài xác định.  
- **copyOfRange**: Copy một phạm vi được chỉ định của một mảng xác định, trả về một mảng mới.  
- **equals**: So sánh giá trị các phần tử trong 2 mảng, trả về giá trị boolean.  
- **fill**: Gán một giá trị xác định cho mỗi phần tử được chỉ định trong mảng.  
- **hashcode**: Trả về một hash code dựa trên nội dung của một mảng xác định.  
- **sort**: Sắp xếp các phần tử trong mảng theo thứ tự tăng dần, thực hiện tuần tự.  
- **setAll**: Set tất cả các phần tử của một mảng xác định, sử dụng generator function được cung cấp để tính toán giá trị (function này nhận 1 arguments là index hiện tại cần set).  
- **spliterator**: Trả về một Spliterator bao gồm các phần tử của một mảng xác định.  
- **stream**: Trả về một stream tuần tự sử dụng một array làm nguồn của nó.  
- **toString**:  Trả về một string biểu diễn nội dung của một mảng xác định.  

- **deepEquals**: So sánh "deeply equal" giá trị các phần tử trong 2 mảng, sử dụng với mảng đa chiều, trả về giá trị boolean.  
- **deepHashCode**: Trả về một hash code dựa trên "deep contents" của một mảng xác định, sử dụng với mảng đa chiều.  
- **deepToString**: Trả về một string biểu diễn "deep contents" của một mảng xác định, sử dụng với mảng đa chiều.  

- **parallelPrefix**: Tính toán dồn lại từng phần tử của một mảng lớn đã cho tại vị trí, thay thế giá trị cũ, sử dụng function được cung cấp (function này nhận 2 arguments là giá trị đã được tính tại vị trí phía trước và giá trị tại vị trí hiện tại cần tính), diễn ra song song trên hệ thống multiprocessor (Từ Java 8 trở đi).  
- **parallelSetAll**: Set các phần tử trong mảng lớn đã cho, sử dụng generator function được cung cấp để tính toán giá trị (function này nhận 1 arguments là index hiện tại cần set), diễn ra song song trên hệ thống multiprocessor, nhanh hơn so với set tuần tự (Từ Java 8 trở đi).  
- **parallelSort**: Sắp xếp các phần tử của một mảng lớn đã cho theo thứ tự tăng dần, diễn ra song song trên hệ thống multiprocessor, nhanh hơn so với sắp xếp tuần tự (Từ Java 8 trở đi).  

Xem chi tiết tại https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html.  

*Ví dụ 1: Sao chép các phàn tử của mảng sử dụng copyOfRange của Arrays:*

```java
class ArrayCopyOfDemo {
    public static void main(String[] args) {
        String[] copyFrom = {
            "Affogato", "Americano", "Cappuccino", "Corretto", "Cortado",   
            "Doppio", "Espresso", "Frappucino", "Freddo", "Lungo", 
            "Macchiato", "Marocchino", "Ristretto" 
        };
        
        String[] copyTo = java.util.Arrays.copyOfRange(copyFrom, 2, 9);        
        for (String coffee : copyTo) {
            System.out.print(coffee + " ");           
        }            
    }
}
// Output: Cappuccino Corretto Cortado Doppio Espresso Frappucino Freddo
```

*Ví dụ 2: In các phần tử trong mảng sử dụng stream của Arrays:*

```java
java.util.Arrays.stream(copyTo).map(coffee -> coffee + " ").forEach(System.out::print); 
```

*Ví dụ 3: Chuyển đổi một mảng thành một chuỗi và in ra:*

```java
System.out.println(java.util.Arrays.toString(copyTo));
// Output: [Cappuccino, Corretto, Cortado, Doppio, Espresso, Frappucino, Freddo]
```

*Ví dụ 4: Sử dụng parallelPrefix cộng gộp các phần tử trong mảng tại vị trí, thay thế giá trị cũ:*

```java
import java.util.Arrays;
import java.util.function.IntBinaryOperator;
class StudyTonight { 
	public static void main(String args[]) 
	{ 
        int arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        IntBinaryOperator op = (x, y) -> x + y;
        Arrays.parallelPrefix(arr, 2, 10, op); 
        for(int num:arr) {
        	System.out.print(num+" ");
        }
	} 
}
// Output: 1 2 3 7 12 18 25 33 42 52 11 12 13 14 15
```

*Ví dụ 5: Set giá trị cho các phần tử của mảng sử dụng setAll của Arrays:*

```java
import java.util.Arrays;
public class SetAllExample {
    public static void main(String... args) {
        int[] arr = new int[10];
        Arrays.setAll(arr, (index) -> 1 + index);
        System.out.println(Arrays.toString(arr));
    }
}
// Output: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
```

*Ví dụ 6: Tạo một Spliterator bao gồm các phần tử của một mảng xác định sử dụng spliterator của Arrays:*

```java
import java.util.Arrays;
import java.util.Spliterator;
public class SpliteratorExample {
    public static void main(String... args) {
        int[] arr = { 1, 2, 3, 4, 5 };
        Spliterator<Integer> s1 = Arrays.spliterator(arr);
        s1.forEachRemaining(System.out::print);
    }
}
// Output: 12345
```


## Questions

- Thuật ngữ "instance variable" là một tên khác của "non-static variable".  
- Thuật ngữ "class variable" là một tên khác của "static variable".  
- Một "local variable" chứa các state tạm thời; nó được khai báo trong một method.  
- Một variable được khai báo bên trong cặp ngoặc () của một method signature được gọi là một "parameter".  
- Java hỗ trợ 8 primitive data types là: byte, short, int, long, float, double, char, boolean.  
- Các chuỗi ký tự được đại diện bởi class "java.lang.String".  
- Một "array" là một container object giữ một số lượng cố định các giá trị của một type duy nhất.  


## 6. Operators (toán tử)

*Operators* là các ký hiệu (symbols) đặc biệt thực hiện các phép toán (operation) cụ thể trên một, hai hoặc ba toán hạng, sau đó trả về một kết quả.

Các operator có mức độ ưu tiên cao hơn được đánh giá trước các operator có mức độ ưu tiên tương đối thấp hơn. Khi các operator có mức độ ưu tiên như nhau xuất hiện trong cùng một biểu thức, thì tất cả các operator nhị phân ngoại trừ các operator gán được đánh giá từ trái sang phải; operator gán được đánh giá từ phải sang trái.

Mức độ ưu tiên của các operators giảm dần theo thứ tự sau:  

```
 1. postfix	                expr++ expr--
 2. unary    	            ++expr --expr +expr -expr ~ !
 3. multiplicative	        * / %
 4. additive	            + -
 5. shift	                << >> >>>
 6. relational	            < > <= >= instanceof
 7. equality	            == !=
 8. bitwise AND	            &
 9. bitwise exclusive OR	^
10. bitwise inclusive OR	|
11. logical AND	            &&
12. logical OR	            ||
13. ternary	                ? :
15. assignment	            = += -= *= /= %= &= ^= |= <<= >>= >>>=
```

Sự khác biệt giữa expr++ và ++expr:  

- expr++ thực hiện:  
    + trước tiên, tạo biến tạm thời temp = expr;  
    + tăng giá trị expr lên 1;  
    + trả về giá trị temp.  
- ++expr thực hiện:  
    + trước tiên, tăng giá trị của expr lên 1;  
    + trả về giá trị của expr.  

*Ví dụ:*

```java
class ArithmeticDemo {
    public static void main (String[] args){
        int i = 10;
        int n = i++%5;
        System.out.printf("i = %d, n = %d", i, n); // i = 11, n = 0
    }
}
```


## 7. Expressions, Statements, Blocks

+ Một expression là một cấu trúc được tạo thành từ các variables, operators, hay các subexpessions. Nó có thể trả về một giá trị.  
+ Một statement tạo thành một đơn vị thực thi hoàn chỉnh, các expressions là thành phần cốt lõi của các statements.  
+ Một block là một nhóm gồm 0 hoặc nhiều statements được đặt trong cặp ngoặc {}.  

Xem chi tiết về các [statements 1](../../specifications/14_blocks-statements-1.md) và [statements 2](../../specifications/14_blocks-statements-2.md).

Xem chi tiết về các [expressions 1](../../specifications/15_expressions-1.md) và [expression 2](../../specifications/15_expressions-2.md) và [expression 3](../../specifications/15_expressions-3.md).


## 8. Control flow statements

Các *control flow statements* chia nhỏ luồng thực thi bằng cách sử dụng các câu lệnh ra quyết định (if-else, switch), lặp (for, while, do-while) và phân nhánh (break, continue, return), cho phép chương trình thực thi có điều kiện các blocks cụ thể.  

Chi tiết xem tại [Java specifications about statements](../../specifications/14_blocks-statements-1.md) và [Java specifications about statements](../../specifications/14_blocks-statements-2.md).