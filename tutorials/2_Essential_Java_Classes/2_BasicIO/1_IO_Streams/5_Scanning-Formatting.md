# Lession 2.1, I/O Streams

## 2.1.4, Scanning and Formatting

Lập trình I/O thường liên quan đến việc dịch sang và từ dữ liệu được định dạng gọn gàng mà con người có thể đọc được. Để hỗ trợ bạn những công việc này, Java platform cung cấp 2 API: 

- Scanner API: chia nhỏ input thành các tokens riêng biệt liên quan với các bit dữ liệu.  
- Formatting API: kết hợp và định dạng dữ liệu thành dạng con người có thể đọc được.  


### 2.1.4.1, Scanning

Các objects thuộc type *Scanner* hữu ích cho việc chia nhỏ input được định dạng thành các tokens và dịch các tokens riêng biệt thành data type tương ứng của chúng.


#### *Breaking Input into Tokens*

Theo mặc định, một scanner sử dụng khoảng trắng để phân tách các tokens. (Các white space characters bao gồm blanks, tabs và line terminators).

*Ví dụ: Chương trình ScanXan đọc các từ riêng biệt trong xanadu.txt và in chúng ra, mỗi từ một dòng.*

```java
import java.io.*;
import java.util.Scanner;

public class ScanXan {
    public static void main(String[] args) throws IOException {

        Scanner s = null;

        try {
            s = new Scanner(new BufferedReader(new FileReader("xanadu.txt")));

            while (s.hasNext()) {
                System.out.println(s.next());
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }
}
```

**Note**: Gọi close method của Scanner khi nó được hoàn thành với Scanner object. Mặc dù một scanner không phải là một stream, nhưng bạn cần đóng nó để cho biết rằng bạn đã hoàn tất với stream của nó.

Để sử dụng một dấu phân tách token khác, cần gọi *useDelimiter()*, chỉ định một regular expression. 

*Ví dụ: giả sử bạn muốn dấu phân tách token là dấu phẩy, tùy chọn theo sau là khoảng trắng. Bạn sẽ gọi useDelimiter() như sau:*

```java
s.useDelimiter(",\\s*");
```


#### *Translating Individual Tokens*

Ví dụ ScanXan coi tất cả các input tokens là các String values đơn giản. *Scanner* cũng hỗ trợ các tokens cho tất cả các primitive types của Java (ngoại trừ char), cũng như BigInteger và BigDecimal. Ngoài ra, các numeric values cũng có thể sử dụng dấu phân tách hàng nghìn. Do đó, trong US local, scanner đọc chính xác string "32,767" dưới dạng đại diện cho một integer value.

Chúng ta phải đề cập đến local, bởi vì dấu phân cách hàng nghìn và ký hiệu thập phân là local-specific. Nhưng, đó không phải là điều bạn thường phải lo lắng, vì input data của bạn thường đến từ các nguồn mà sử dụng cùng local với bạn.

*Ví dụ: Chương trình đọc danh sách Double values và tính tổng sau sẽ không hoạt động chính xác ở tất cả các local nếu chúng ta không chỉ định rằng scanner nên sử dụng US local.*

```java
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;

public class ScanSum {
    public static void main(String[] args) throws IOException {

        Scanner s = null;
        double sum = 0;

        try {
            s = new Scanner(new BufferedReader(new FileReader("usnumbers.txt")));
            s.useLocale(Locale.US);

            while (s.hasNext()) {
                if (s.hasNextDouble()) {
                    sum += s.nextDouble();
                } else {
                    s.next();
                }   
            }
        } finally {
            s.close();
        }

        System.out.println(sum);
    }
}

// Input file usnumbers.txt:
//      8.5
//      32,767
//      3.14159
//      1,000,000.1

// Output string: "1032778.74159".
```

*Trong ví dụ trên, Dấu chấm sẽ là một ký tự khác trong một số local, bởi vì System.out là một PrintStream object và class đó không cung cấp cách ghi đè default local. Chúng ta có thể ghi đè local cho toàn bộ chương trình - hoặc có thể sử dụng định dạng (format).*


### 2.1.4.2, Formatting

Các Stream objects mà thực hiện định dạng là các instances của *PrintWriter* - một *character stream class*, hoặc *PrintStream* - một *byte stream class*.

**Note**: Các *PrintStream* objects duy nhất mà bạn có thể cần là *System.out* và *System.err*. Khi bạn cần tạo một formatted output stream, hãy khởi tạo *PrintWriter* (một output character stream), KHÔNG phải PrintStream (một output byte stream).

Giống như tất cả các byte và character stream objects, các instances của *PrintStream* và *PrintWriter* triển khai một bộ các *write* methods tiêu chuẩn cho byte và character output đơn giản. Ngoài ra, cả PrintStream và PrintWriter đều triển khai cùng một bộ các methods cho việc chuyển đổi dữ liệu nội bộ thành output được định dạng. Có 2 cấp độ định dạng được cung cấp:

- *print* và *println* định dạng các giá trị riêng biệt theo một cách tiêu chuẩn.  
- *format* định dạng hầu hết các giá trị dựa trên một format string, với nhiều tùy chọn để định dạng chính xác.  


#### *The print and println Methods* 

Gọi *print* hoặc *println* xuất ra một giá trị duy nhất sau khi chuyển đổi giá trị bằng cách sử dụng toString method thích hợp.

```java
public class Root {
    public static void main(String[] args) {
        int i = 2;
        double r = Math.sqrt(i);
        
        System.out.print("The square root of ");
        System.out.print(i);
        System.out.print(" is ");
        System.out.print(r);
        System.out.println(".");

        i = 5;
        r = Math.sqrt(i);
        System.out.println("The square root of " + i + " is " + r + ".");
    }
}

// Output: The square root of 2 is 1.4142135623730951.
//         The square root of 5 is 2.23606797749979.
```

*Trong ví dụ trên, các biến i và r được định dạng 2 lần: lần đầu tiên sử dụng một overload của print, lần thứ hai bởi mã chuyển đổi được tạo tự động bởi Java compiler, sử dụng toString.*

Bạn có thể định dạng bất kỳ giá trị nào theo cách này, nhưng bạn không có nhiều quyền kiểm soát kết quả.


#### *The format Method* 

*format* method định dạng nhiều arguments dựa trên một *format string*. format string bao gồm static text được nhúng với các *format specifiers* (bộ đặc tả định dạng); ngoại trừ các format specifiers, format string được xuất ra không thay đổi.

```java
public class Root2 {
    public static void main(String[] args) {
        int i = 2;
        double r = Math.sqrt(i);
        
        System.out.format("The square root of %d is %f.%n", i, r);
    }
}
// Output: The square root of 2 is 1.414214.
```

Tất cả các format specifiers đều bắt đầu bởi ký tự % và kết thúc với một conversion mà chỉ định loại formatted output được tạo ra.

Một số conversions:  

- d định dạng một integer value dưới dạng một decimal value.  
- f định dạng một floating-point value dưới dạng một decimal value.  
- x định dạng một integer value dưới dạng một hexadecimal value.  
- s định dạng bất kỳ value nào dưới dạng một string.  
- n xuất ra một platform-specific line terminator.  

Có nhiều conversions khác, xem chi tiết tại [format string syntax](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#syntax).

**Note**:

- Ngoại trừ %% và %n, tất cả các format specifiers phải khớp với một argument. Nếu không, một ngoại lệ sẽ được ném ra.  
- Trong Java, \n escape luôn tạo một linefeed character (\u000A). Không sử dụng \n trừ khi bạn xác định muốn linefeed character. Để nhận được line separator chính xác cho local plaform, hãy sử dụng %n.  

Ngoại trừ các conversions, một format specifiers có thể chứa một số phần tử bổ sung (tùy chọn) để tùy chỉnh formatted output. 

```java
public class Format {
    public static void main(String[] args) {
        System.out.format("%f, %1$+020.10f %n", Math.PI);
    }
}
// Output: 3.141593, +00000003.1415926536
```

Cấu trúc của một format specifier bao gồm các elements phải theo thứ tự như sau, *vd: %1$+020.10f*:

- %  Begin format specifier  
- 1$ Argument index  
- +0 Flags  
- 20 Width  
- .10 Precision  
- f Conversion  

Các elements tùy chọn là:  

- **Precision**: Đối với floating point values, đây là độ chính xác toán học của giá trị được định dạng. Đối với s và các conversions chung khác, đây là chiều rộng tối đa của giá trị được định dạng; giá trị được cắt ngắn bên phải nếu cần thiết.  
- **Width**: Chiều rộng tối thiểu của giá trị được định dạng; giá trị được đệm nếu cần thiết. Theo mặc định, giá trị được đệm bên trái bằng các khoảng trống.  
- **Flags**: chỉ định các tùy chọn định dạng bổ sung, như: + flag chỉ định rằng số phải luôn được định dạng với dấu và 0 flag chỉ định rằng 0 là ký tự đệm. Các flag khác bao gồm - (đệm ở bên phải) và , (format number với dấu phân cách hàng nghìn theo local cụ thể). Lưu ý rằng một số flag không thể được sử dụng với một số flag khác hoặc với một số conversions nhất định.  
- **Argument Index**: chỉ định vị trí của argument cho format specifier (vd: 1$), hoặc có thể sử dụng ký tự < để sử dụng lại argument cho format specifier trước đó.  