# Lession 6. Numbers and Strings

## 3. Strings

String là một chuỗi các ký tự.

Trong Java, các strings là các objects.  

Java cung cấp **String** class để tạo và thao tác với các strings.  


### Creating Strings

Cách trực tiếp nhất để tạo một string là sử dụng một *string literal*. Bất cứ khi nào gặp một string literal, compiler sẽ tạo một String object với string literal đó.

```java
String greeting = "Hello world!";
```

Tương tự với các object khác, có thể tạo một String object bằng cách sử dụng new operator và một constructor. String class có 13 constructors cho phép bạn cung cấp giá trị khởi tạo của string sử dụng các sources khác nhau, chẳng hạn như một mảng các ký tự:

```java
char[] helloArray = { 'h', 'e', 'l', 'l', 'o', '.' };
String helloString = new String(helloArray);
System.out.println(helloString); // hello.
```

**Note**: String class là *immutable*, vì vậy khi một String object được tạo ra, nó không thể bị thay đổi. String class có một số methods được cung cấp để thao tác với các String objects, điều chúng thực sự làm là tạo và trả về các String objects mới chứa kết quả của thao tác trên String object ban đầu.


### String Length

Các methods được sử dụng để lấy thông tin về một object được gọi là các *accessor methods*. **length()** là một accessor method được sử dụng để lấy số ký tự có trong một string.

```java
// int length()
String palindrome = "Dot saw I was Tod";
int len = palindrome.length(); // 17
```


### Copying characters from string into a char array.

String class có **getChars()** method giúp sao chép các ký tự từ một string vào một mảng ký tự.

```java
// void	getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
palindrome.getChars(0, len, tempCharArray, 0);
```

Ngoài ra, **toCharArray()** method giúp chuyển đổi string hiện tại thành mảng các ký tự:

```java
// char[] toCharArray()
palindrome.toCharArray();
```


### Concatenating Strings

String class có **concat()** method giúp nối 2 string.

```java
// String concat(String str)
string1.concat(string2); 
"My name is ".concat("Rumplestiltskin");
```

Có thể sử dụng **+ operator** để thực hiện nối chuỗi từ hỗn hợp của bất kỳ objects nào. Đối với các objects không phải String, toString() method của nó sẽ được gọi để lấy string biểu diễn của object đó.

```java
String string1 = "saw I was ";
System.out.println("Dot " + string1 + "Tod");
```

Ngoài ra, String class còn cung cấp **join()** methods để kết hợp các chuỗi ký tự (CharSequence) với dấu phân tách được chỉ định:  

```java
static String join(CharSequence delimiter, CharSequence... elements)
static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements)
```


### Creating Format Strings

Tương tự với các methods *printf()* và *format()* của *PrintStream* giúp in ra output với các numbers được định dạng, String class cũng có **format()** method trả về một String object được định dạng và có thể tái sử dụng, chứ không phải một PrintSream object mà chỉ có thể in một lần.

```java
/**
 * In ra string được định dạng, sử dụng một format string xác định và danh sách các arguments.
 *
 * @param format format string xác định định dạng được sử dụng.
 * @param args   list các arguments sẽ được in ra với định dạng format.
 * @return String object.
 */
public static String format(String format, Object... args)
public static String format(Locale l, String format, Object... args)
```

*Ví dụ:*

```java
// Thay vì:
System.out.printf(
    "The value of the float variable is %f, while " +
    "the value of the integer variable is %d, " +
    "and the string is %s", 
    floatVar, intVar, stringVar
); 

// Có thể viết như sau:
String fs;
fs = String.format(
    "The value of the float variable is %f, " +
    "while the value of the integer variable is %d, " +
    "and the string is %s",
    floatVar, intVar, stringVar
);
System.out.println(fs);
```


### 3.1, Converting Between Numbers and Strings

#### *3.1.1, Converting Strings to Numbers*

Các Number subclasses (Byte, Integer, Double, Float, Long, và Short) bao bọc các primitive numeric types, mỗi subclass đều cung cấp một class method có tên **valueOf** để chuyển đổi một *string* thành một *object* của subclass type đó.

```java
public class ValueOfDemo {
    public static void main(String[] args) {

        // this program requires two arguments on the command line 
        if (args.length == 2) {
            // convert strings to numbers
            float a = (Float.valueOf(args[0])).floatValue(); 
            float b = (Float.valueOf(args[1])).floatValue();

            // do some arithmetic
            System.out.println("a + b = " + (a + b));
            System.out.println("a - b = " + (a - b));
            System.out.println("a * b = " + (a * b));
            System.out.println("a / b = " + (a / b));
            System.out.println("a % b = " + (a % b));
        } else {
            System.out.println("This program requires two command-line arguments.");
        }
    }
}
// java ValueOfDemo 4.5 87.2
// Output: a + b = 91.7
//         a - b = -82.7
//         a * b = 392.4
//         a / b = 0.0516055
//         a % b = 4.5 
```

**Note**: Mỗi Number subclasses bao bọc primitive numeric types cũng cung cấp một class method có tên **parseXXXX()** (vd: parseFloat()) có thể được sử dụng để chuyển đổi *strings* thành *primitive numbers*:

```java
float a = Float.parseFloat(args[0]);
float b = Float.parseFloat(args[1]);
```


#### *3.1.2, Converting Numbers to Strings*

Đôi khi cần chuyển đổi một number thành string vì cần thao tác trên giá trị ở dạng chuỗi của nó. Có một số cách dễ dàng để chuyển đổi một number thành một string:

```java
int i;

// Concatenate "i" with an empty string; conversion is handled for you.
String s1 = "" + i;

// or

// The valueOf class method.
String s2 = String.valueOf(i);
```

String class cung cấp static method **valueOf()** giúp chuyển đổi các primitive value hoặc object thành một String object:

```java
static String	valueOf(boolean b)
static String	valueOf(char c)
static String	valueOf(char[] data)
static String	valueOf(char[] data, int offset, int count)
static String	valueOf(double d)
static String	valueOf(float f)
static String	valueOf(int i)
static String	valueOf(long l)
static String	valueOf(Object obj)
```

Mỗi *Number subclass* ngoài một *instance method* - **toString()** giúp chuyển đổi Subclass object thành string, thì đều bao gồm một *class method* - **toString()** giúp chuyển đổi một primitive number thành một string:

```java
int i;
double d;
String s3 = Integer.toString(i); 
String s4 = Double.toString(d); 
```

*Ví dụ: Tính số lượng chữ số trước và sau dấu thập phân của một floating-point number:*

```java
public class ToStringDemo {
    public static void main(String[] args) {
        double d = 858.48;
        String s = Double.toString(d);
        
        int dot = s.indexOf('.');
        
        System.out.println(dot + " digits before decimal point.");
        System.out.println((s.length() - dot - 1) + " digits after decimal point.");
    }
}
// Output: 3 digits before decimal point.
//         2 digits after decimal point.
```


### 3.2, Manipulating Characters in a String

String class cung cấp một số methods để kiểm tra content của strings, tìm kiếm các characters hay substrings bên trong một string, thay đổi chữ in hoa hay chữ thường, và các tác vụ khác.


#### *3.2.1, Getting Characters and Substrings by Index*

Có thể lấy ký tự tại một index cụ thể bên trong một string bằng cách gọi **charAt()** accessor method. Trong một string, index của ký tự đầu tiên là 0, trong khi index của ký tự cuối cùng là length()-1.

```java
String anotherPalindrome = "Niagara. O roar again!"; 
char aChar = anotherPalindrome.charAt(9); // O
```

Nếu muốn lấy nhiều hơn một ký tự liên tiếp từ một string, có thể sử dụng **substring()** method với 2 signature được cung cấp như sau:

```
                Method                        |         Description
----------------------------------------------|------------------------------------------------------
char charAt(int index)                        | Trả về ký tự tại vị trí index của string này.
----------------------------------------------|------------------------------------------------------
String substring(int beginIndex, int endIndex)| Trả về một string mới là substring của string này,
                                              | từ beginIndex tới endIndex, nhưng không gồm endIndex.
----------------------------------------------|------------------------------------------------------
String substring(int beginIndex)              | Trả về một string mới là substring của string này,
                                              | từ beginIndex tới cuối string ban đầu.
```

*Ví dụ: Lấy substring từ một string:*

```java
String anotherPalindrome = "Niagara. O roar again!"; 
String roar = anotherPalindrome.substring(11, 15);  // roar
```


#### *3.2.2, Other Methods for Manipulating Strings*

Một số methods khác được cung cấp để thao tác với string:

```
                        Method                        |         Description
------------------------------------------------------|------------------------------------------------
String[] split(String regex)                          | Chia string này thành mảng các string tương ứng,
String[] split(String regex, int limit)               | phân tách bởi một regular expression xác định.
                                    	              | Đối số tùy chọn limit chỉ định max size của mảng.
------------------------------------------------------|------------------------------------------------
CharSequence subSequence(int beginIndex, int endIndex)|	Trả về một chuỗi ký tự (CharSequence type) mới,
                                                      | được tạo từ beginIndex đến endIndex-1.
------------------------------------------------------|------------------------------------------------
String trim()	                                      | Trả về bản sao của string này với:
            	                                      | khoảng trắng ở đầu và cuối của string bị xóa.
------------------------------------------------------|------------------------------------------------
String toLowerCase()                                  | Trả về bản sao của string này,
String toUpperCase()	                              | được chuyển đổi thành lowercase hoặc uppercase.
                    	                              | Nếu không cần chuyển đổi, sẽ trả về string ban đầu.
```

**Note**: *CharSequence* là một interface được triển khai bởi String class.


#### *3.2.3, Searching for Characters and Substrings in a String*

Ngoài ra, String class còn cung cấp một số methods khác để tìm kiếm một ký tự hoặc substring trong một string:  

- **indexOf()** và **lastIndexOf()**: Trả về một số nguyên là vị trí trong string hiện tại của một ký tự hoặc substring cụ thể, nếu không tìm thấy sẽ return -1. Trong đó, indexOf() tìm kiếm bắt đầu từ đầu string, và lastIndexOf() tìm kiếm ngược từ cuối string.  
- **contains()**: Trả về một giá trị boolean true nếu string hiện tại chứa chuỗi ký tự (CharSequence type) cụ thể.  

```
                Method                    |         Description
------------------------------------------|----------------------------------------------
int indexOf(int ch)                       | Trả về index trong string này ở lần xuất hiện  
int lastIndexOf(int ch)	                  | đầu tiên / cuối cùng của một ký tự xác định.
------------------------------------------|----------------------------------------------
int indexOf(int ch, int fromIndex)        | Trả về index trong string này ở lần xuất hiện
int lastIndexOf(int ch, int fromIndex)	  | đầu tiên / cuối cùng của một ký tự xác định,
                                          | tìm kiếm tiến / lùi từ index chỉ định.
------------------------------------------|----------------------------------------------
int indexOf(String str)                   | Trả về index trong string này ở lần xuất hiện
int lastIndexOf(String str)	              | đầu tiên / cuối cùng của một substring xác định.
------------------------------------------|----------------------------------------------
int indexOf(String str, int fromIndex)    | Trả về index trong string này ở lần xuất hiện
int lastIndexOf(String str, int fromIndex)|	đầu tiên / cuối cùng của một substring xác định,
                                          | tìm kiếm tiến / lùi từ index chỉ định.
------------------------------------------|----------------------------------------------
boolean contains(CharSequence s)	      | Trả về true,
                                          | nếu string này chứa một chuỗi ký tự xác định.
```

**Note**: *CharSequence* là một interface được triển khai bởi String class. Do đó, có thể sử dụng một string làm argument cho *contains()* method.


#### *3.2.4, Replacing Characters and Substrings into a String*

String class cung cấp rất ít các methods để chèn một ký tự hoặc substring vào một string, điều này không cần thiết, có thể tạo string mới bằng cách tách string ban đầu thành các substring tại vị trí muốn chèn và nối với substring muốn chèn.

String class cung cấp 4 methods để thay thế các ký tự và substring được tìm thấy trong một string:

```
                            Method                           |         Description
-------------------------------------------------------------|-------------------------------------------
String replace(char oldChar, char newChar)                   | Trả về một string mới,
                                                             | thay thế tất cả oldChar bằng newchar.
-------------------------------------------------------------|-------------------------------------------
String replace(CharSequence target, CharSequence replacement)| Trả về một string mới,
                                                        	 | thay thế tất cả substring khớp với target,
                                                             | bằng replacement xác định.
-------------------------------------------------------------|-------------------------------------------
String replaceAll(String regex, String replacement)          | Trả về một string mới,
                                                        	 | thay thế tất cả substring khớp với regex,
                                                             | bằng replacement xác định.
-------------------------------------------------------------|-------------------------------------------
String replaceFirst(String regex, String replacement)        | Trả về một string mới,
                                                        	 | thay thế substring đầu tiên khớp với regex,
                                                             | bằng replacement xác định.
```

*Ví dụ: Tách biệt các phần trong một filename, giả sử argument là một full directory path và filename có phần extension:*

```java
public class Filename {
    private String fullPath;
    private char pathSeparator, extensionSeparator;

    public Filename(String str, char sep, char ext) {
        fullPath = str;
        pathSeparator = sep;
        extensionSeparator = ext;
    }

    public String extension() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        return fullPath.substring(dot + 1);
    }

    // gets filename without extension
    public String filename() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }

    public String path() {
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(0, sep);
    }
}

public class FilenameDemo {
    public static void main(String[] args) {
        final String FPATH = "/home/user/index.html";
        Filename myHomePage = new Filename(FPATH, '/', '.');
        System.out.println("Extension = " + myHomePage.extension()); // Extension = html
        System.out.println("Filename = " + myHomePage.filename());   // Filename = index
        System.out.println("Path = " + myHomePage.path()); // Path = /home/user
    }
}
```


### 3.3, Comparing Strings and Portions of Strings

String class cung cấp một số methods để so sánh các strings và các phần của strings:

```
                Method                        |                  Description
----------------------------------------------|-----------------------------------------------------
boolean	isEmpty()                             | Trả về true khi và chỉ khi length() = 0.
----------------------------------------------|-----------------------------------------------------
boolean endsWith(String suffix)               | Trả về true nếu string bắt đầu / kết thúc 
boolean startsWith(String prefix)	          | với substring xác định.
----------------------------------------------|-----------------------------------------------------
boolean startsWith(String prefix, int offset) |	Trả về true nếu string bắt đầu tại index offset
                                              |	với substring xác định.
----------------------------------------------|-----------------------------------------------------
int compareTo(String anotherString)	          | So sánh 2 strings về mặt content literal.
                                              |	Trả về một số nguyên chỉ ra string này
                                              |	lớn hơn (result > 0), bằng (result = 0),
                                              |	hay nhỏ hơn (result < 0) một string khác.
----------------------------------------------|-----------------------------------------------------
int compareToIgnoreCase(String str)	          | So sánh 2 strings về mặt content literal,
                                              |	bỏ qua sự phân biệt chữ hoa và chữ thường.
                                              |	Trả về một số nguyên chỉ ra string này
                                              |	lớn hơn (result > 0), bằng (result = 0),
                                              |	hay nhỏ hơn (result < 0) một string khác.
----------------------------------------------|-----------------------------------------------------
boolean equals(Object anObject)	              | Trả về true nếu và chỉ nếu một object xác định
                                              |	đại diện cho cùng một chuỗi ký tự với string này.
----------------------------------------------|-----------------------------------------------------
boolean equalsIgnoreCase(String anotherString)|	Trả về true nếu và chỉ nếu một object xác định
                                              |	đại diện cho cùng một chuỗi ký tự với string này,
                                              | không phân biệt chữ hoa và chữ thường.
----------------------------------------------|-----------------------------------------------------
boolean regionMatches(                        | Kiểm tra xem một vùng xác định từ index toffset với
    int toffset,                              | độ dài len của string này, có khớp với một vùng xác
    String other, int ooffset, int len        | định từ index ooffset với độ dài len của một string
)                                             | khác hay không.
----------------------------------------------|-----------------------------------------------------
boolean regionMatches(                        | Kiểm tra xem một vùng xác định từ index toffset với
    boolean ignoreCase,                       | độ dài len của string này, có khớp với một vùng xác
    int toffset,                              | định từ index ooffset với độ dài len của một string
    String other, int ooffset, int len        | khác hay không.
)	                                          | ignoreCase quyết định liệu có phân biệt chữ hoa thường.
----------------------------------------------|-----------------------------------------------------
boolean matches(String regex)	              | Kiểm tra xem string này có khớp với một 
                                              | regular expression xác định hay không.
```

*Ví dụ: Tìm kiếm một string bên trong một string khác với regionMatches():*

```java
public class RegionMatchesDemo {
    public static void main(String[] args) {
        String searchMe = "Green Eggs and Ham";
        String findMe = "Eggs";
        int searchMeLength = searchMe.length();
        int findMeLength = findMe.length();
        boolean foundIt = false;
        for (int i = 0; i <= (searchMeLength - findMeLength); i++) {
            if (searchMe.regionMatches(i, findMe, 0, findMeLength)) {
                foundIt = true;
                System.out.println(searchMe.substring(i, i + findMeLength));
                break;
            }
        }
        if (!foundIt)
            System.out.println("No match found.");
    }
}
```


### 3.4, The StringBuilder Class

*StringBuilder objects* tương tự như các *String objects*, ngoại trừ việc chúng có thể bị sửa đổi.

Nên sử dụng các String objects, trừ khi các StringBuilder objects làm code đơn giản hơn hay hiệu suất tốt hơn. *Ví dụ, nếu cần nối một số lượng lớn các strings, việc append vào một StringBuilder object sẽ hiệu quả hơn.*

**Note**: Còn có một **StringBuffer** class hoàn toàn giống với *StringBuilder* class, ngoại trừ việc nó an toàn cho luồng (thread-safe) nhờ việc các methods của nó đều được đồng bộ hóa.  


#### *3.4.1, Length and Capacity*

StringBuilder class giống với String class, có **length()** method trả về độ dài của chuỗi ký tự trong builder.

Không giống các strings, mọi string builder đều có một dung lượng (capacity), là số lượng không gian ký tự đã được cấp phát. Dung lượng được trả về bởi **capacity()** method, luôn lớn hơn hoặc bằng độ dài (capacity >= length) và sẽ tự động mở rộng khi cần thiết để cung cấp cho việc bổ sung các ký tự vào string builder.

```
            Constructor         |                  Description
--------------------------------|------------------------------------------------------------------
StringBuilder()	                | Tạo một string builder rỗng có dung lượng 16 (16 empty elements).
--------------------------------|------------------------------------------------------------------
StringBuilder(int initCapacity) | Tạo một string builder rỗng có dung lượng ban đầu xác định.
--------------------------------|------------------------------------------------------------------
StringBuilder(CharSequence cs)  | Tạo một string builder chứa các ký tự giống với CharSequence,
                                | cùng với 16 empty elements theo sau CharSequence.
--------------------------------|------------------------------------------------------------------
StringBuilder(String s)	        | Tạo một string builder có giá trị được khởi tạo bởi một string,
                                | cùng với 16 empty elements theo sau string.
```

*Ví dụ: Tạo một string builder có độ dài 9 và dung lượng 16:*

```java
StringBuilder sb = new StringBuilder(); // creates empty builder, capacity 16
sb.append("Greetings");                 // adds 9 character string at beginning
```

StringBuilder class có một số methods liên quan đến độ dài và dung lượng mà String class không có:

```
            Method                  |                  Description
------------------------------------|--------------------------------------------------------------
void setLength(int newLength)       | Đặt độ dài của chuỗi ký tự. Nếu newLength nhỏ hơn length(),
                                    | các ký tự cuối cùng trong chuỗi ký tự sẽ bị cắt bớt.
                                    | các ký tự cuối cùng trong chuỗi ký tự sẽ bị cắt bớt.
                                    | Nếu newLength lớn hơn length (), các ký tự null sẽ được thêm
                                    | vào cuối chuỗi ký tự.
------------------------------------|--------------------------------------------------------------
void ensureCapacity(int minCapacity)| Đảm bảo rằng dung lượng ít nhất phải bằng mức tối thiểu 
                                    | được chỉ định.
------------------------------------|------------------------------------------------------
void trimToSize()                   | Cố gắng giảm bộ nhớ được sử dụng cho chuỗi ký tự.
```

Một số methods (vd: append(), insert(), hay setLength()) có thể tăng độ dài của chuỗi ký tự trong string builder, bởi vậy sẽ khiến kết quả length()lớn hơn capacity() hiện tại. Khi điều này xảy ra, dung lượng sẽ tự động được tăng lên.


#### *3.4.2, StringBuilder Operations*

Một số methods của StringBuilder class:

```
            Method                          |                  Description
--------------------------------------------|------------------------------------------------------
StringBuilder append(boolean b)             | Thêm các ký tự của string được chuyển đổi từ argument
StringBuilder append(char c)                | vào cuối chuỗi ký tự của string builder này.
StringBuilder append(char[] str)            |
StringBuilder append(                       |
    char[] str, int offset, int len         |
)                                           |
StringBuilder append(double d)              |
StringBuilder append(float f)               |
StringBuilder append(int i)                 |
StringBuilder append(long lng)              |
StringBuilder append(Object obj)            |
StringBuilder append(String s)              |
--------------------------------------------|------------------------------------------------------
StringBuilder delete(int start, int end)    | Method thứ 1 xóa subsequence từ start đến end-1 (gồm)
StringBuilder deleteCharAt(int index))      | trong chuỗi ký tự của string builder này.
                                            | Method thứ 2 xóa ký tự tại index.
--------------------------------------------|------------------------------------------------------
StringBuilder insert(int offset, boolean b) | Chèn các ký tự của string được chuyển đổi từ argument
StringBuilder insert(int offset, char c)    | vào string builder này bắt đầu từ vị trí offset.
StringBuilder insert(int offset, char[] str)|
StringBuilder insert(                       |
    int index,                              |
    char[] str, int offset, int len         |
)                                           |
StringBuilder insert(int offset, double d)  |
StringBuilder insert(int offset, float f)   |
StringBuilder insert(int offset, int i)     |
StringBuilder insert(int offset, long lng)  |
StringBuilder insert(int offset, Object obj)|
StringBuilder insert(int offset, String s)  |
--------------------------------------------|------------------------------------------------------
StringBuilder replace(                      | Thay thế các ký tự xác định trong string builder này.
    int start, int end, String s            |
)                                           |
void setCharAt(int index, char c)           | 
--------------------------------------------|------------------------------------------------------
StringBuilder reverse()                     | Đảo ngược chuỗi ký tự trong string builder.
--------------------------------------------|------------------------------------------------------
int indexOf(String str)                     | Trả về index trong chuỗi ký tự này ở lần xuất hiện
int indexOf(String str, int fromIndex)      | đầu tiên / cuối cùng của một substring xác định.
int lastIndexOf(String str)	                | 
int lastIndexOf(String str, int fromIndex)  | 
--------------------------------------------|------------------------------------------------------
char charAt(int index)                      | Trả về ký tự tại vị trí index của string builder này.
--------------------------------------------|------------------------------------------------------
CharSequence subSequence(int start, int end)| Trả về một subsequence của chuỗi ký tự hiện tại.
--------------------------------------------|------------------------------------------------------
String substring(int start)                 | Trả về một string chứa một subsequence của chuỗi ký tự
String substring(int start, int end)        | trong string builder này.
--------------------------------------------|------------------------------------------------------
String toString()                           | Trả về string chứa chuỗi ký tự trong string builder.
--------------------------------------------|------------------------------------------------------
void getChars(                              | Copy các ký tự trong chuỗi ký tự của string builder
    int srcBegin, int srcEnd,               | vào một mảng đích.
    char[] dst, int dstBegin                |
)                                           | 
```

**Note**: có thể sử dụng bất kỳ String method nào trên một StringBuilder object bằng cách chuyển đổi string builder thành một string với *toString()* method của StringBuilder class. Sau đó, chuyển đổi string trở lại thành một string builder bằng cách sử dụng *StringBuilder(String str)* constructor.

*Ví dụ: Đảo ngược chuỗi ký tự:*

```java
public class StringBuilderDemo {
    public static void main(String[] args) {
        String palindrome = "Dot saw I was Tod";
         
        StringBuilder sb = new StringBuilder(palindrome);
        
        sb.reverse();  // reverse it

        System.out.println(sb); // doT saw I was toD
    }
}
```

**Note**: *println()* method in ra một *string builder*, bởi vì *toString()* method của StringBuilder được gọi ngầm định như bất kỳ object nào khác.