# Chap 1: Lexical Structure

Chương này đề cập đến:  

- Unicode  
- Unicode Escapes
- Comments  
- Identifiers  
- Keywords  
- Literals  
- Separators  
- Operators  


## 1. Unicode

Unicode là một bộ ký tự chuẩn quốc tế, nó bao gồm tất cả các ký tự từ tất cả các ngôn ngữ trên thế giới, mỗi ký tự có một giá trị số duy nhất.  

Java sử dụng bộ ký tự Unicode với kiểu mã hóa UTF-16 để biểu diễn text theo chuỗi các 16-bit code units (còn được gọi với thuật ngữ UTF-16 code unit).  

Ngoại trừ comments, identifiers, & string literals, thì tất cả các input elements trong một chương trình đều được tạo thành từ các ký tự ASCII (hoặc Unicode escapes có kết quả là các ký tự ASCII).  

**Note**:  

- Unicode với kiểu mã hóa UTF-8 mỗi ký tự là 1 byte, UTF-16 mỗi ký tự là 2 byte, UTF-32 mỗi ký tự là 4 byte,  
- Unicode với các kiểu mã hóa trong MySQL: UTF8-MB4 mỗi ký tự sử dụng 4 byte.  
- Unicode với kiểu mã hóa UTF-16 trong Java mỗi ký tự là 2 byte.  


## 2. Unicode Escapes

Các *Unicode Escape* có dạng: **\uxxxx** đại diện cho UTF-16 code unit có mã hóa là xxxx, trong đó:  

- u là *unicode marker*,  
- xxxx là giá trị thập lục phân,  
    Các ký tự thập lục phân gồm: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, a, b, c, d, e, f, A, B, C, D, E, F  

Ví dụ: \u0567  

Java copiler nhận dạng các Unicode Escapes trong inputs của nó, dịch chúng sang các unicode character (UTF-16 code unit với hexadecimal value xác định) tương ứng.  

*Ví dụ*:  

```java
public class Test {
    public static void main(String[] args) {
        String a1 = "hello\u0567";
        System.out.println(a1);     // hello?
    }
}
```  


## 3. Comments

Trong Java có 2 dạng comments:  

- End-of-line comment: // text  
- Multi-line comment (Block comment):  /* text */  


## 4. Identifiers

Một *identifier* là một chuỗi các ký tự không có độ dài giới hạn, bao gồm các chữ cái, chữ số, kí tự _ và $; ký tự đầu của nó không thể là một chữ số.  

Identifier Rules (Quy tắc định danh):  

- Chỉ có thể chứa các ký tự: chữ cái, chữ số, _, $  
- Không thể bắt đầu bằng chữ số  
- Phân biệt chữ hoa & chữ thường  
- Không thể trùng với các keyword, boolean literals, null literal trong java  

Naming Conventions (Quy ước đặt tên):  

- Package:  
    + Sử dụng các danh từ,  
    + Dạng chữ thường,  
    + Ngăn cách bởi ký tự '.' theo chiều hướng hẹp dần (narrow down)  
    + vd: com.mq.nursing  

- Class or Interface:  
    + Sử dụng các danh từ or cụm danh từ,  
    + Dạng in hoa các ký tự đầu của mỗi từ,  
    + vd: StudentManager  

- Method:  
    + Sử dụng động từ or cụm động từ,  
    + Camel case: Từ đầu tiên dạng chữ thường, các từ tiếp theo dạng in hoa các ký tự đầu,  
    + vd: studyAtHome  

- Variable:  
    + Sử dụng các danh từ or cụm danh từ,  
    + Camel case: Từ đầu tiên dạng chữ thường, các từ tiếp theo dạng in hoa các ký tự đầu,  
    + vd: eyeColor  

- Constant:  
    + Dạng chữ in hoa,  
    + Các từ ngăn cách nhau bởi ký tự '_'  
    + vd: SCORE_AVERAGE  


## 5. Keywords  

Các keywords trong Java không thể sử dụng làm identifier bao gồm:  

```text
abstract   continue   for          new         switch  
assert     default    if           package     synchronized  
boolean    do         goto         private     this  
break      double     implements   protected   throw  
byte       else       import       public      throws  
case       enum       instanceof   return      transient  
catch      extends    int          short       try  
char       final      interface    static      void  
class      finally    long         strictfp    volatile  
const      float      native       super       while  
```


## 6. Literals  

Một *literal* là source code biểu diễn của một value của *primitive type*, *String type*, hoặc *null type*.  

Bao gồm:  

- Integer literals  
- Floating-Point literals  
- Boolean literals  
- Character literals  
- String literals  
- null literal  


#### *6.1, Integer literals*  

Một *integer literal* có thể được biểu diễn dưới dạng decimal (thập phân - base 10), hexadecimal (thập lục phân - base 16), octal (bát phân - base 8), binary (nhị phân - base 2).  

Một *integer literal* nếu có hậu tố là ký tự *L* or *l* sẽ có type là *long*, nếu không sẽ có type là *int*.  

Ký tự underscore (_) được cho phép làm dấu ngăn cách giữa các chữ số biểu thị số nguyên.  

- decimal: Có thể là ký tự 0 duy nhất đại diện số nguyên 0, hoặc bắt đầu bằng chữ số từ 1-9 & theo sau bởi các chữ số từ 0-9 đại diện cho số nguyên:  
    ```text
    int:   0    2     -30     2_147_483_648  
    long:  0l   2L    -30l    2_147_483_648L  
    ```  

- hexadecimal: Bắt đầu bằng các ký tự 0X hoặc 0x, theo sau bởi các ký tự thập lục phân đại diện cho số nguyên:  
    ```text
    int:   0x0     0x1_0000     -0xC0B0  
    long:  0x0l    0x1_0000L    -0xC0B0L  
    ```  

- octal: Bắt đầu bằng ký tự 0 theo, sau bởi các ký tự bát phân đại diện cho số nguyên:  
    ```text  
    int:   0     -0777     01_2321_2342  
    long:  0l    -0777L    01_2321_2342L  
    ```  

- binary: Bắt đầu bằng các ký tự 0B hoặc 0b, theo sau bởi các ký tự nhị phân đại diện cho số nguyên:  
    ```text
    int:   0B0     0b1001_1001  
    long:  0B0l    0b1001_1001L  
    ```  


#### *6.2, Floating-Point literals*  

Một *floating-point literal* gồm các phần sau: phần nguyên, dấu thập phân or thập lục phân (được biểu diễn bởi ký tự dấu .), phần phân số, số mũ, và hậu tố type.  

Một *floating-point literal* có thể được biểu diễn dưới dạng decimal (base 10) or hexadecimal (base 16).  

Đối với các *decimal floating-point literals*, cần có ít nhất một chữ số (ở phần nguyên hoặc phần phân số) và dấu thập phân, hoặc số mũ, hoặc suffix. Tất cả các phần khác là tùy chọn. Nếu có số mũ được biểu diễn bằng ký tự e hoặc E, theo sau là một số nguyên có dấu tùy chọn.  

Đối với các *hexadecimal floating-point literals*, cần có ít nhất một chữ số (ở phần nguyên hoặc phần phân số), số mũ nhị phân là bắt buộc, suffix là tùy chọn. Số mũ được biểu thị bằng ký tự p hoặc P theo sau là một số nguyên có dấu tùy chọn.  

Ký tự underscore (_) được phép làm dấu ngăn cách giữa các chữ số biểu diễn phần nguyên, phần phân số, & phần số mũ.  

*Ví dụ*:  
- decimal floating-point literals:  
    > 0x32.a2P1 = [(3*16^1 + 2*16^0).(10*16^-1 + 2*16^-2)] * 2^1 = 101.265625  

- hexadecimal floating-point literals:  
    > 12.5e2 = 12.5 * 10^2 = 1250.0  

Một *floating-point literal* có type float nếu nó có hậu tố là F hoặc f; nếu không thì type của nó là double và nó có thể có hậu tố tùy chọn D hoặc d.  

Literal dương hữu hạn lớn nhất của type float là 3,4028235e38f.

Literal non-zero dương hữu hạn nhỏ nhất của type float là 1,40e-45f.  

Literal dương hữu hạn lớn nhất của type double là 1,7976931348623157e308.  

Literal none-zero dương hữu hạn nhỏ nhất của type double là 4,9e-324.  

Xảy ra compile time error nếu *non-zero floating-point literal* quá lớn, do đó khi làm tròn, nó sẽ trở thành infinity.

Một chương trình có thể biểu diễn các số infinity mà không tạo ra compile time error bằng cách sử dụng các constant expressions như 1f / 0f hoặc -1d / 0d hoặc bằng cách sử dụng các constants đã được định nghĩa POSITIVE_INFINITY và NEGATIVE_INFINITY của các class Float và Double.

Xảy ra compile time error nếu *non-zero floating-point literal* quá nhỏ, do đó khi làm tròn, nó sẽ trở thành zero.

Sẽ không xảy ra compile time error nếu một *non-zero floating-point literal* có giá trị nhỏ mà khi làm tròn, sẽ trở thành một số không chuẩn hóa khác 0.

Các constant đã định nghĩa đại diện cho các giá trị Not-a-Number được định nghĩa trong các class Float và Double là Float.NaN và Double.NaN.  

*Ví dụ*:  

- float literals:  
    ```text
    1e1f    2.f    .3f    0f    3.14f    6.022137e+23f    0x1a2.c5P2f  
    ```

- double literals:  
    ```text
    1e1    2.    .3    0.0    3.14    1e-9d    1e137    0x1a2.c5P2  
    ```  


#### *6.3, Boolean Literals*

Boolean type có 2 values, được biểu diễn bằng các *boolean literals* true & false.  

Một *boolean literal* luôn có type Boolean.  


#### *6.4, Character Literals*

Một *character literal* được biểu diễn dưới dạng một ký tự hoặc một escape sequence, và được đặt trong cặp dấu nháy đơn ('').

*Character Literal* chỉ có thể biểu diễn UTF-16 code units, tức là chúng bị giới hạn ở các values từ \u0000 đến \uffff (tương ứng 0-65536).  

Một *character literal* luôn có type char.  

Ví dụ:  

```text
'a'   '%'   '\t'   '\\'   '\"'   '\u03a9'   '\177'  
```  


#### *6.5, String Literals*

Một *string literal* bao gồm không hoặc nhiều ký tự được đặt trong cặp dấu nháy kép (""). Các ký tự có thể được biểu thị bằng escape sequence.  

Một *string literal* luôn có type String.  

Ví dụ:  

```java
""                    // the empty string
"\""                  // a string containing " alone
"This is a string"    // a string containing 16 characters
"This is a " + "two-line string" // actually a string-valued constant expression, formed from two string literals
```

Một string literal là một reference tới một instance của class String.  

Hơn nữa, một string literal luôn tham chiếu tới cùng instance của class String.

```java
// package testPackage
package testPackage;
class Test {
    public static void main(String[] args) {
        String hello = "Hello", lo = "lo";

        System.out.print((hello == "Hello") + " ");           // true
        System.out.print((Other.hello == hello) + " ");       // true
        System.out.print((other.Other.hello == hello) + " "); // true
        System.out.print((hello == ("Hel"+"lo")) + " ");      // true
        System.out.print((hello == ("Hel"+lo)) + " ");        // false
        System.out.println(hello == ("Hel"+lo).intern());     // true
    }
}
class Other { static String hello = "Hello"; }

// package other
package other;
public class Other { public static String hello = "Hello"; }
```

Ví dụ trên minh họa 6 điểm:  

- Literal strings trong cùng class trong cùng package đại diện references tới cùng String object.  
- Literal strings trong classes khác nhau trong cùng package đại diện references tới cùng String object.  
- Literal strings trong classes khác nhau trong packages khác nhau cũng đại diện references tới cùng String object.  
- Strings được tính toán bởi constant expressions được tính toán tại compile time sau đó sẽ được xử lý như thể chúng là các literals.  
- Strings được tính toán bởi việc nối tại run time là được tạo mới, do đó sẽ khác biệt nhau.  
- Kết quả của việc can thiệp một cách tường minh vào string được tính toán là cùng string với bất kì string literal nào đã tồn tại trước đó với cùng nội dung.  


#### *6.6, Escape Sequences for Character and String Literals*  

Các *character & string escape sequences* cho phép biểu diễn một số ký tự phi đồ họa mà không sử dụng Unicode escapes, cũng như các ký tự dấu nháy đơn ('), dấu nháy kép (") và dấu gạch chéo ngược (\ backslash), trong các *character literals* và *string literals*.  

- Escape Sequences:  
    + \ b (backspace BS, Unicode \u0008)  
    + \ t (horizontal tab HT, Unicode \u0009)  
    + \ n (linefeed LF, Unicode \u000a)  
    + \ f (form feed FF, Unicode \u000c)  
    + \ r (carriage return CR, Unicode \u000d)  
    + \ " (double quote ", Unicode \u0022)  
    + \ ' (single quote ', Unicode \u0027)  
    + \ \ (backslash \, Unicode \u005c)  
    + Octal Escape (octal value, Unicode \u0000 to \u00ff)  

- Octal Escape:  
    + \ OctalDigit  
    + \ OctalDigit OctalDigit  
    + \ ZeroToThree OctalDigit OctalDigit  

*Ví dụ*:  

```java
public class Test {
    public static void main(String[] args) {
        String a1 = "hello\054\0Mai";
        String a2 = "hello,\tMai";
        System.out.println(a1); // hello, Mai
        System.out.println(a2); // hello,   Mai
    }
}
```  


#### *6.7, The Null Literal*

null type có một value là null reference, được biểu diễn bằng *null literal* là null.  

null literal luôn có type null.  


## 7. Separators

Các *separators* bao gồm: 

```text
(   )   {   }   [   ]   ;   ,   .   ...   @   ::  
```


## 8. Operators  

Các *operators* bao gồm:  

```text
=   >   <   !   ~   ?   :   ->
==  >=  <=  !=  &&  ||  ++  --
+   -   *   /   &   |   ^   %   <<   >>   >>>
+=  -=  *=  /=  &=  |=  ^=  %=  <<=  >>=  >>>=
```
