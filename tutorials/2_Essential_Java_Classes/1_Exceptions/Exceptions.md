# Lession 1. Exceptions

- What Is an Exception?  
- The Catch or Specify Requirement  
- Catching and Handling Exceptions  
- Specifying the Exceptions Thrown by a Method  
- How to Throw Exceptions  
- Unchecked Exceptions  
- Advantages of Exceptions


## 1. What Is an Exception?

Một *exception* là một sự kiện, xảy ra trong quá trình thực thi chương trình, làm gián đoạn luồng thông thường của các lệnh trong chương trình.

Khi có lỗi xảy ra trong một method, method đó sẽ tạo một *exception object* chứa thông tin về lỗi, bao gồm type của nó và state của chương trình khi lỗi xảy ra, và chuyển nó cho runtime system, điều này được gọi là *throwing an exception* (ném một ngoại lệ).

Sau khi một method ném một ngoại lệ, runtime system sẽ cố gắng xử lý nó.
Runtime system tìm kiếm trong *call stack* (chứa danh sách theo thứ tự các method đã được gọi để truy cập method xảy ra lỗi) một method chứa một *exception handler* có thể xử lý ngoại lệ. Việc tìm kiếm bắt đầu từ method mà lỗi đã xảy ra và tiến hành thông qua call stack theo thứ tự ngược lại mà các method được gọi. Khi tìm thấy một handler thích hợp, runtime system sẽ chuyển ngoại lệ cho handler. Một exception handler được coi là thích hợp nếu type của exception object được ném ra khớp với type mà handler có thể xử lý.

Exception handler được chọn được gọi là *catch the exception* (bắt ngoại lệ). Nếu runtime system tìm kiếm tất cả các method trong call stack mà không tìm thấy exception handler thích hợp, thì chương trình sẽ kết thúc.

```
          (Throws exception)  Method nơi xảy ra lỗi -----
                                          ^             | Looking for
                                          | method      | appropriate
                                          | call        | handler
                                          |             v
        (Forwards exception)  Method không có một exception handler ---
                                          ^                           | Looking for
                                          | method                    | appropriate
                                          | call                      | handler
                                          |                           |
(Catch some other exception)  Method với một exception handler <-------
                                          ^
                                          | method
                                          | call
                                          |
                                         main
```


## 2. The Catch or Specify Requirement

Java code có thể ném một số ngoại lệ cụ thể (checked exceptions) mà sẽ phải được bao bọc bởi một trong hai điều sau, nếu không sẽ gây ra compile-time error:  

- Một *try* statement mà bắt ngoại lệ đó. *try* phải cung cấp một handler cho exception đó.  
- Một method xác định rằng nó có thể ném ngoại lệ đó. Method phải cung cấp một *throws* clause liệt kê ngoại lệ đó.  


#### *The Three Kinds of Exceptions*

Loại ngoại lệ đầu tiên là *checked exception*. Đây là những điều kiện ngoại lệ mà một ứng dụng được viết tốt phải lường trước và phục hồi. 

*Ví dụ: giả sử một ứng dụng nhắc người dùng nhập input file name, sau đó mở file bằng cách truyền tên cho constructor của java.io.FileReader. Thông thường, người dùng cung cấp tên của một file hiện có, có thể đọc được, do đó, việc tạo FileReader object thành công và việc thực thi ứng dụng diễn ra bình thường. Nhưng đôi khi người dùng cung cấp tên của file không tồn tại và constructor ném java.io.FileNotFoundException. Một chương trình được viết tốt sẽ bắt được ngoại lệ này và thông báo cho người dùng về lỗi, có thể nhắc nhở nhập file name chính xác.*

Ngoại trừ những ngoại lệ được chỉ ra bởi Error, RuntimeException và các subclass của chúng, thì tất cả các ngoại lệ đều là checked exceptions. Code ném ra các checked exception phải được bao bọc trong một try statement hoặc một method xác định nó sẽ ném ngoại lệ này.

Loại ngoại lệ thứ hai là *error*. Đây là những điều kiện ngoại lệ bên ngoài ứng dụng và ứng dụng thường không thể lường trước hoặc khôi phục được.

*Ví dụ: giả sử một ứng dụng mở tệp để nhập thành công nhưng không thể đọc tệp do trục trặc phần cứng hoặc hệ thống. Việc đọc không thành công sẽ xuất hiện java.io.IOError.*

Một ứng dụng có thể lựa chọn bắt ngoại lệ này, để thông báo cho người dùng về sự cố - nhưng nó cũng có thể in ra stack tree và thoát chương trình.

Các errors là những ngoại lệ được chỉ ra bởi Errors và các subclass của nó. Code ném ra các errors KHÔNG cần phải được bao bọc trong một try statement hoặc một method xác định nó sẽ ném ngoại lệ này.

Loại ngoại lệ thứ ba là *runtime exception*. Đây là những điều kiện ngoại lệ bên trong ứng dụng và ứng dụng thường không thể lường trước hoặc khôi phục được. Chúng thường chỉ ra lỗi lập trình, chẳng hạn như lỗi logic hoặc sử dụng API không đúng cách. 

*Ví dụ: hãy xem xét ứng dụng được mô tả trước đó truyền file name đến constructor cho FileReader. Nếu một lỗi logic gây ra giá trị null được truyền cho constructor, thì constructor sẽ ném NullPointerException. Ứng dụng có thể bắt được ngoại lệ này, nhưng có lẽ sẽ hợp lý hơn khi loại bỏ lỗi khiến ngoại lệ xảy ra.*

Các runtime exception là những ngoại lệ được chỉ ra bởi RuntimeException và các subclass của nó. Code ném ra các runtime exceptions KHÔNG cần phải được bao bọc trong một try statement hoặc một method xác định nó sẽ ném ngoại lệ này.

Các errors và runtime exceptions được gọi chung là các *unchecked exceptions*.


## 3. Catching and Handling Exceptions

*Ví dụ: Khi khởi tạo một ListOfNumbers object, sẽ tạo ra một ArrayList chứa 10 Integer elements với các giá trị tuần tự từ 0 - 9. ListOfNumbers class có writeList method thực hiện ghi danh sách các numbers vào một text file có tên OutFile.txt:*

```java
// Note: This class will not compile yet.
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ListOfNumbers {

    private List<Integer> list;
    private static final int SIZE = 10;

    public ListOfNumbers () {
        list = new ArrayList<Integer>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            list.add(new Integer(i));
        }
    }

    public void writeList() {
        // The FileWriter constructor throws IOException, which must be caught.
        PrintWriter out = new PrintWriter(new FileWriter("OutFile.txt"));

        for (int i = 0; i < SIZE; i++) {
            // The get(int) method throws IndexOutOfBoundsException, which must be caught.
            out.println("Value at: " + i + " = " + list.get(i));
        }
        out.close();
    }
}
```

*Constructor invocation 'new FileWriter("OutFile.txt")' khởi tạo một output stream trên file. Nếu không thể mở file, constructor sẽ ném một IOException. Method invocation của get method của ArrayList class, method này ném ra một IndexOutOfBoundsException nếu giá trị của đối số của nó nhỏ hơn 0 hoặc lớn hơn số phần tử hiện có trong ArrayList.*

*Nếu bạn cố gắng biên dịch ListOfNumbers class, compiler sẽ in ra một error message về ngoại lệ do FileWriter constructor ném ra. Tuy nhiên, nó không hiển thị error message về ngoại lệ được ném bởi get. Lý do là ngoại lệ được ném bởi constructor, IOException, là một checked exception và ngoại lệ được ném bởi method get, IndexOutOfBoundsException, là một unchecked exception.*


### 3.1, The try Block

Để xây dựng một exception handler, trước tiên cần phải bao bọc code có thể tạo ra một ngoại lệ trong một *try* block.

```
try {
    // code
}
catch and finally blocks ...
```

*Ví dụ: Để xây dựng một exception handler cho method writeList của ListOfNumbers class, hãy bao bọc các statements ném ngoại lệ của nó trong một try block. Có nhiều cách để làm điều này. Bạn có thể đặt mỗi dòng code có thể tạo ra một ngoại lệ trong try block của riêng nó và cung cấp các exception handler riêng cho từng dòng. Hoặc, bạn có thể đặt tất cả writeList code trong một try block duy nhất và liên kết nhiều handlers với nó.*

```java
private List<Integer> list;
private static final int SIZE = 10;

public void writeList() {
    PrintWriter out = null;
    try {
        FileWriter f = new FileWriter("OutFile.txt");
        out = new PrintWriter(f);
        for (int i = 0; i < SIZE; i++) {
            out.println("Value at: " + i + " = " + list.get(i));
        }
    } catch and finally blocks  ...
}
```

Nếu một ngoại lệ xảy ra trong try block, thì ngoại lệ đó sẽ được xử lý bởi một exception handler được liên kết với nó. Để liên kết một exception handler với một try block, bạn phải đặt một catch block sau nó.


### 3.2, The catch Block

Bạn liên kết các exception handlers với try block bằng cách cung cấp một hoặc nhiều *catch* block ngay sau *try* block. 

```java
try {
    ...
} catch (ExceptionType name) {
    ...
} catch (ExceptionType name) {
    ...
}
```

Mỗi catch block là một exception handler xử lý loại ngoại lệ được chỉ ra bởi parameter type của nó. Parameter type, ExceptionType, khai báo kiểu ngoại lệ mà handler có thể xử lý và phải là một subclass của Throwable class.

catch block chứa code mà được thực thi nếu exception handler được gọi. Runtime system gọi một exception handler khi handler này là handler đầu tiên trong call stack có ExceptionType khớp với loại ngoại lệ được ném ra. System coi là khớp nếu object được ném có thể được gán cho parameter của exception handler.

*Ví dụ: 2 exception handlers cho writeList method:*

```java
try {

} catch (IndexOutOfBoundsException e) {
    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
} catch (IOException e) {
    System.err.println("Caught IOException: " + e.getMessage());
}
```

Các exception handlers có thể làm được nhiều việc hơn là chỉ in ra error message hoặc tạm dừng chương trình. Chúng có thể thực hiện khôi phục lỗi, nhắc người dùng đưa ra quyết định hoặc truyền lỗi đến higher-level handler bằng cách sử dụng các chained exceptions.

#### *Catching More Than One Type of Exception with One Exception Handler*

Từ Java SE 7 trở lên, một catch block duy nhất có thể xử lý nhiều hơn một loại ngoại lệ.

Trong catch clause, xác định các loại ngoại lệ mà block có thể xử lý và phân tách các exception types bằng một ký tự vertical bar (|):

```java
catch (IOException | SQLException ex) {
    logger.log(ex);
    throw ex;
}
```

**Note**: Nếu một catch block xử lý nhiều hơn một loại ngoại lệ, thì catch parameter được ngầm định là *final*. 


### 3.3, The finally Block

*finally* block luôn thực thi khi *try* block thoát. Điều này đảm bảo rằng finally block được thực thi ngay cả khi một ngoại lệ không mong muốn xảy ra. Nhưng finally hữu ích cho nhiều việc không chỉ là xử lý ngoại lệ - nó cho phép programmer tránh cleanup code vô tình bị bỏ qua bởi một *return*, *continue*, hay *break*. Đặt cleanup code trong một finally block luôn là một phương pháp tốt, ngay cả khi không có ngoại lệ nào được dự đoán trước.

**Note**: finally block có thể không thực thi nếu JVM thoát ra trong khi try hoặc catch code đang được thực thi.

*Ví dụ: try block của method writeList mà bạn đang làm việc ở đây sẽ open một PrintWriter. Chương trình nên đóng stream đó trước khi thoát khỏi method writeList. Điều này đặt ra một vấn đề hơi phức tạp vì try block của writeList có thể thoát ra theo một trong 3 cách:*

- *'new FileWriter()' statement không thành công và ném ra một IOException.*  
- *'list.get(i)' statement không thành công và ném một IndexOutOfBoundsException.*  
- *Mọi thứ đều thành công và try block thoát bình thường.*  

Runtime system luôn thực hiện các statements trong finally block bất kể điều gì xảy ra trong try block. Vì vậy, nó là nơi hoàn hảo để thực hiện dọn dẹp.  

*Ví dụ: finally block cho method writeList sau đây sẽ dọn dẹp và sau đó đóng PrintWriter và FileWriter.*

```java
finally {
    if (out != null) { 
        System.out.println("Closing PrintWriter");
        out.close(); 
    } else { 
        System.out.println("PrintWriter not open");
    } 
    if (f != null) {
	    System.out.println("Closing FileWriter");
	    f.close();
	}	
}
```

**Important**: Sử dụng *try-with-resources statement* thay vì một *finally* block khi đóng file hoặc khôi phục tài nguyên. 

*Ví dụ: Sử dụng câu lệnh try-with-resources để dọn dẹp và đóng PrintWriter và FileWriter cho method writeList:*

```java
public void writeList() throws IOException {
    try (
        FileWriter f = new FileWriter("OutFile.txt");
        PrintWriter out = new PrintWriter(f)
    ) {
        for (int i = 0; i < SIZE; i++) {
            out.println("Value at: " + i + " = " + list.get(i));
        }
    }
}
```

*try-with-resources* statement sẽ tự động giải phóng tài nguyên hệ thống khi không còn cần thiết.


### 3.4, The try-with-resources Statement

*try-with-resources* statement là try statemnt khai báo một hoặc nhiều tài nguyên. 

Một *resource* là một object phải được đóng lại sau khi chương trình kết thúc thực thi với nó. Bất kỳ object nào triển khai *java.lang.AutoClosable*, bao gồm tất cả các object triển khai *java.io.Closable*, đều có thể được sử dụng làm tài nguyên.  

try-with-resources statement đảm bảo rằng mỗi tài nguyên đều sẽ được đóng ở cuối statement (Note: các close method của tài nguyên được gọi theo thứ tự ngược lại với quá trình tạo của chúng). 

*Ví dụ 1: đọc dòng đầu tiên từ một file, sử dụng một instance của FileReader và BufferedReader để đọc dữ liệu từ file. FileReader và BufferedReader là các resource phải được đóng sau khi chương trình kết thúc với nó:*

```java
static String readFirstLineFromFile(String path) throws IOException {
    try (
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr)
    ) {
        return br.readLine();
    }
}
```

Trước Java SE 7, bạn có thể sử dụng finally block để đảm bảo rằng tài nguyên được đóng bất kể câu lệnh try hoàn thành bình thường hay đột ngột. 

```java
static String readFirstLineFromFileWithFinallyBlock(String path) throws IOException {
   
    FileReader fr = new FileReader(path);
    BufferedReader br = new BufferedReader(fr);
    try {
        return br.readLine();
    } finally {
        br.close();
        fr.close();
    }
}
```

*Ví dụ trên có thể bị rỏ rỉ tài nguyên. Một chương trình phải làm nhiều việc hơn là dựa vào GC để lấy lại bộ nhớ của tài nguyên khi nó hoàn thành. Chương trình cũng phải giải phóng tài nguyên trở lại hệ điều hành, thường bằng cách gọi close method của tài nguyên. Tuy nhiên, nếu một chương trình không thực hiện được điều này trước khi GC lấy lại tài nguyên, thì thông tin cần thiết để giải phóng tài nguyên đó sẽ bị mất. Tài nguyên vẫn được hệ điều hành coi là đang được sử dụng đã bị rò rỉ.*

*Trong ví dụ này, nếu method readLine ném một ngoại lệ và câu lệnh 'br.close()' trong finally block ném một ngoại lệ, thì FileReader đã bị rò rỉ. Do đó, hãy sử dụng try-with-resources statement thay vì finally block để đóng tài nguyên của chương trình của bạn.*

*Nếu các method readLine và close đều ném ra ngoại lệ, thì method readFirstLineFromFileWithFinallyBlock ném ngoại lệ được ném ra từ finally block; ngoại lệ được ném ra từ try block bị chặn. Ngược lại, trong ví dụ readFirstLineFromFile, nếu các ngoại lệ được ném ra từ cả try block và try-with-resources statement, thì phương thức readFirstLineFromFile sẽ ném ngoại lệ từ try block; ngoại lệ được đưa ra từ khối try-with-resources bị loại bỏ. Trong Java SE 7 trở lên, bạn có thể truy xuất các ngoại lệ bị chặn.*

**Note**: Một *try-with-resources* statement có thể có các *catch* và *finally* blocks giống như một *try* statement thông thường. Trong một try-with-resources statement, bất kỳ catch hay finally block nào cũng sẽ được thực thi sau khi các resources được khai báo đã được đóng.


#### *Suppressed Exceptions*

Một exception có thể được ném ra từ block được liên kết với  try-with-resources statement. 

Nếu một exception được ném ra từ try block và một hoặc nhiều exception được ném ra từ try-with-resources statement, thì những exception được ném ra từ try-with-resources statement sẽ bị loại bỏ, và exception do try block ném ra sẽ được ném bởi method chứa statement này. Bạn có thể truy xuất các exception bị loại bỏ này bằng cách gọi "Throwable.getSuppressed" method từ exception được ném bởi try block.


#### *Classes That Implement the AutoCloseable or Closeable Interface*

*Closeable* interface extends *AutoCloseable* interface. *close* method của *Closeable* interface ném các exceptions của type *IOException*, trong khi đó *close* method của *AutoCloseable* interface ném các exceptions của type *Exception*. Do đó, các subclasses của AutoCloseable interface có thể override hành vi này của close method để ném các exceptions chuyên biệt, chẳng hạn như IOException, hoặc hoàn toàn không ném bất cứ exception nào.


## 4. Specifying the Exceptions Thrown by a Method

Đôi khi, có những tình huống sẽ tốt hơn nếu để một higher-level method trong call stack xử lý ngoại lệ được ném ra. 

*Ví dụ: nếu bạn đang cung cấp ListOfNumbers class như một phần của một package, bạn có thể không dự đoán trước được nhu cầu của tất cả người dùng package của mình. Trong trường hợp này, tốt hơn là bạn không nên bắt ngoại lệ và cho phép một higher-level method trong call stack xử lý nó.*  

Nếu một method không bắt được các checked exception có thể xảy ra bên trong nó, thì nó phải chỉ định rằng nó có thể ném các ngoại lệ này bằng cách sử dụng *throws* clause. 

```java
public void writeList() throws IOException, IndexOutOfBoundsException {
    PrintWriter out = new PrintWriter(new FileWriter("OutFile.txt"));
    for (int i = 0; i < SIZE; i++) {
        out.println("Value at: " + i + " = " + list.get(i));
    }
    out.close();
}
```

*Bởi vì IndexOutOfBoundsException là một unchecked exception, không bắt buộc phải liệt kê nó trong throws clause:*

```java
public void writeList() throws IOException { ... }
```


## 5. How to Throw Exceptions

Trước khi bạn có thể bắt một ngoại lệ, một số mã ở đâu đó phải ném một ngoại lệ. Bất kỳ mã nào cũng có thể tạo ra một ngoại lệ: mã của bạn, mã từ một package được viết bởi người khác, chẳng hạn như các package đi kèm với Java platform hoặc JRE. Bất kể thứ gì ném ngoại lệ, nó luôn được ném với *throw* statement.

Java platform cung cấp nhiều exception classes. Tất cả các class này đều là hậu duệ của *Throwable* class và tất cả đều cho phép các programs phân biệt giữa các loại ngoại lệ khác nhau có thể xảy ra trong quá trình thực thi một program.

Bạn cũng có thể tạo các exception class của riêng mình để biểu diễn các vấn đề có thể xảy ra trong các class bạn viết. Trên thực tế, nếu bạn là một package developer, bạn có thể phải tạo tập hợp các exception class của riêng mình để cho phép người dùng phân biệt lỗi có thể xảy ra trong package của bạn với lỗi xảy ra trong Java platform hoặc các package khác.

Bạn cũng có thể tạo các *chained exceptions*.


#### *The throw Statement*

Tất cả các method sử dụng *throw* statement để ném một ngoại lệ. throw statement yêu cầu một argument duy nhất: một throwable object, là một instance của bất kỳ subclass nào của Throwable class. 

```java
throw someThrowableObject;
```

*Ví dụ: pop method được lấy từ một class triển khai một stack object chung. Method này xóa top element từ stack và trả về object đó.*

```java
public Object pop() {
    Object obj;

    if (size == 0) {
        throw new EmptyStackException();
    }

    obj = objectAt(size - 1);
    setObjectAt(size - 1, null);
    size--;
    return obj;
}
```

*pop method sẽ kiểm tra xem liệu có bất kỳ phần tử nào nằm trên stack hay không. Nếu stack trống (size = 0), pop khởi tạo một EmptyStackException object mới (một member của java.util) và ném nó.*

*pop method declaration không chứa throws clause. EmptyStackException không phải là một unchecked exception, vì vậy pop không bắt buộc phải nói rằng nó có thể xảy ra.*


#### *Throwable Class and Its Subclasses*

Sơ đồ phân cấp của *Throwable* class và các subclass của nó:

```java
            Object
               |
            Throwable
               |
    -------------------------
    |                       |
  Error                 Exception
    |                       |
  ----              -----------------
  |  |              |               |
...  ...           ...      RuntimeException
                                    |
                                ---------
                                |       |
                               ...     ...
```


#### *Error Class*

Khi xảy ra lỗi dynamic linking hoặc lỗi cứng khác trong JVM, JVM sẽ ném một Error. Các chương trình đơn giản thường không bắt hoặc ném các Errors.


#### *Exception Class*

Hầu hết các chương trình ném và bắt các object có nguồn gốc từ *Exception* class. Một Exception chỉ ra rằng một vấn đề đã xảy ra, nhưng nó không phải là một vấn đề hệ thống nghiêm trọng. Hầu hết các chương trình bạn viết sẽ ném và bắt các *Exceptions* thay vì *Errors*.

Java platform định nghĩa nhiều subclass của lớp *Exception* class. Những subclass này chỉ ra nhiều loại ngoại lệ khác nhau có thể xảy ra.

*Ví dụ: IllegalAccessException báo hiệu rằng không thể tìm thấy một method cụ thể nào đó và NegativeArraySizeException chỉ ra rằng một chương trình đã cố gắng tạo một mảng có kích thước âm.*

Một Exception subclass, RuntimeException, được dành riêng cho các exceptions chỉ ra việc sử dụng API không chính xác. 

*Ví dụ: NullPointerException là một runtime exception, xảy ra khi một method cố gắng truy cập vào member của một object thông qua null reference.*


### 5.1, Chained Exceptions

Một ứng dụng thường phản hồi một exception bằng cách ném một exception khác. Trên thực tế, exception đầu tiên gây ra exception thứ hai. Có thể rất hữu ích nếu biết khi nào một exception gây ra một exception khác. *Chained Exceptions* giúp programmer thực hiện điều này.

Các method và constructor trong *Throwable* hỗ trợ các *chained exceptions*:

```java
/**
 * @param   Throwable argument của initCause method và các Throwable constructors,
 *          là exception gây ra exception hiện tại.
 *
 * @return  getCause method trả về exception gây ra exception hiện tại.
 * @return  initCause method set exception gây ra exception hiện tại.
 */

Throwable getCause()
Throwable initCause(Throwable)
Throwable(String, Throwable)
Throwable(Throwable)
```

*Ví dụ: Sử dụng chained exceptions: khi một IOException bị bắt, một ngoại lệ SampleException mới được tạo với nguyên nhân ban đầu được đính kèm và chuỗi ngoại lệ được ném lên higher-level exception handler tiếp theo.*

```java
try {
    ...
} catch (IOException e) {
    throw new SampleException("Other IOException", e);
}
```


#### *Accessing Stack Trace Information*

Giả sử rằng higher-level exception handler muốn kết xuất stack trace theo format của riêng nó.

**Definition**: Một *stack trace* cung cấp thông tin về lịch sử thực thi của thread hiện tại, liệt kê tên của các class và method đã được gọi tại điểm khi exception xảy ra. Một stack trace là một debug tool hữu ích mà bạn thường sẽ tận dụng khi một exception được ném ra.

Gọi *getStackTrace* method trên exception object để lấy thông tin stack trace:  

```java
catch (Exception cause) {
    StackTraceElement elements[] = cause.getStackTrace();
    for (int i = 0, n = elements.length; i < n; i++) {       
        System.err.println(
            elements[i].getFileName() + ":" 
            + elements[i].getLineNumber() + ">> "
            + elements[i].getMethodName() + "()"
        );
    }
}
```


#### *Logging API*

Thay vì phân tích cú pháp stack trace thủ công và gửi output đến System.err(), có thể sử dụng tiện ích logging được cung cấp trong *java.util.logging* package để ghi các logs nơi một exception xảy ra từ bên trong catch block vào một file.

```java
try {
    Handler handler = new FileHandler("OutFile.log");
    Logger.getLogger("").addHandler(handler);
} catch (IOException e) {
    Logger logger = Logger.getLogger("package.name"); 
    StackTraceElement elements[] = e.getStackTrace();
    for (int i = 0, n = elements.length; i < n; i++) {
        logger.log(Level.WARNING, elements[i].getMethodName());
    }
}
```


### 5.2, Creating Exception Classes

Khi đối mặt với việc chọn exception type để ném, bạn có thể sử dụng exception type do người khác viết - Java platform cung cấp rất nhiều exception class mà bạn có thể sử dụng - hoặc bạn có thể viết một exception class của riêng mình.

Bạn nên viết các exception class của riêng mình nếu là một trong các trường hợp:

- Bạn cần một exception type không được đại diện bởi những exception trong Java platform,  
- Nó giúp ích cho người dùng, nếu họ có thể phân biệt các exception của bạn từ các exceptions được ném bởi các class được viết bởi các nhà cung cấp khác,  
- Mã của bạn có nhiều hơn một exception liên quan,  
- Khi bạn sử dụng các exception của người khác, người dùng sẽ có quyền truy cập vào các exception đó. Và package của bạn độc lập và khép kín.  


#### *Example*

*Ví dụ: Giả sử bạn viết một linked list class hỗ trợ các methods sau:*

+ objectAt(int n) — Trả về object ở vị trí n trong list. Ném một exception nếu argument < 0 hoặc lớn hơn số lượng objects trong list hiện tại.  
+ firstObject() — Trả về object đầu tiên trong list. Ném một exception nếu list không chứa objects nào.  
+ indexOf(Object o) — Tìm kiếm trong list một Object xác định và trả về vị trí của nó trong list. Ném một exception nếu object được truyền vào method không có trong list.  

*Linked list class có thể ném ra nhiều ngoại lệ và sẽ rất tiện lợi nếu có thể bắt tất cả các ngoại lệ do linked list ném ra bằng một exception handler. Ngoài ra, nếu bạn định phân phối linked list của mình trong một package, tất cả mã liên quan nên được đóng gói cùng nhau. Do đó, linked list phải cung cấp tập hợp các exception class của riêng nó.*

*Hệ thống phân cấp các class khả thi cho các exceptions do linked list ném ra:*

```java
            LinkedListException
                    |
        -------------------------
        |                       |
InvalidIndexException   ObjectNotFoundException
                                |
                            EmptyListException
```


#### *Choosing a Superclass*

Bất kỳ Exception subclass nào cũng có thể được sử dụng làm superclass của LinkedListException. Tuy nhiên, việc xem xét nhanh các subclass đó cho thấy rằng chúng không phù hợp vì chúng quá chuyên biệt hoặc hoàn toàn không liên quan đến LinkedListException. Do đó, superclass của LinkedListException phải là Exception.

Hầu hết các applet và ứng dụng bạn viết sẽ ném các object là Exception. Error thường được sử dụng cho các lỗi nghiêm trọng, khó xảy ra trong hệ thống, chẳng hạn như lỗi ngăn JVM chạy.

**Note**: Để code dễ đọc hơn, nên đưa string Exception vào tên của tất cả các subclass của Exception class.


## 6. Unchecked Exceptions

Bởi vì Java không yêu cầu các method để bắt hoặc chỉ định các unchecked exceptions (RuntimeException, Error và các subclass của chúng), các programmer có thể bị cám dỗ để viết code chỉ ném các unchecked exception hoặc để làm cho tất cả các exception subclass của họ kế thừa từ RuntimeException. Cả 2 đường tắt này đều cho phép programmer viết code mà không cần bận tâm đến các compile-time error và không cần phải chỉ định hoặc bắt bất kỳ exceptions nào. Mặc dù điều này có vẻ thuận tiện đối với programmer, nhưng nó có thể gây ra sự cố cho những người khác sử dụng các class của bạn.

Tại sao các designers quyết định bắt buộc một method chỉ định tất cả các uncaught checked exceptions có thể được ném bên trong scope của nó? Bất kỳ Exception nào có thể được ném bởi một method đều là một phần của public programming interface của method. Người gọi một method phải biết về các exceptions mà một method có thể ném ra để họ có thể quyết định phải làm gì với chúng. Các exceptions này cũng là một phần của programming interface đó như các parameters và return value của method đó.

Câu hỏi tiếp theo là: "Nếu có thể sử dụng documentation cho API của một method, bao gồm các exception mà nó có thể ném ra, tại sao lại không chỉ định các runtime exceptions?" Các runtime exception đại diện cho các vấn đề là kết quả của sự cố lập trình, do đó, API client code không thể được mong đợi một cách hợp lý để khôi phục chúng hoặc xử lý chúng theo bất kỳ cách nào. Những vấn đề như vậy bao gồm các arithmetic exceptions, *chẳng hạn như chia cho số không*; pointer exceptions, *chẳng hạn như cố gắng truy cập một đối tượng thông qua tham chiếu null*; và indexing exception, *chẳng hạn như cố gắng truy cập vào một phần tử mảng thông qua một index quá lớn hoặc quá nhỏ*.

Các runtime exception có thể xảy ra ở bất kỳ đâu trong một chương trình và trong một chương trình điển hình, chúng có thể rất nhiều. Việc phải thêm các runtime exception trong mọi method declartion sẽ làm giảm độ rõ ràng của chương trình. Do đó, compiler không yêu cầu bạn bắt hoặc chỉ định các runtime exception (mặc dù bạn có thể).

Một trường hợp phổ biến là ném RuntimeException là khi người dùng gọi một method không chính xác. *Ví dụ, một method có thể kiểm tra xem một trong các argument của nó có giá trị null không. Nếu một argument là null, method có thể ném ra một NullPointerException, là một unchecked exception*.

Nói chung, không ném RuntimeException hoặc tạo một subclass của RuntimeException đơn giản vì bạn không muốn bị làm phiền với việc chỉ định các exception mà method của bạn có thể ném.

Đây là nguyên tắc mấu chốt: Nếu một client có thể được mong đợi một cách hợp lý để phục hồi sau một exception, hãy đặt nó là một checked exception. Nếu client không thể làm bất cứ điều gì để khôi phục exception, hãy đặt nó là một unchecked exception.


## 7. Advantages of Exceptions

Những lợi ích khi sử dụng các exceptions trong chương trình:

- Tách biệt code xử lý lỗi và code thông thường,  
- Chuyển lỗi lên higher-level trong call stack,  
- Gom nhóm và phận biệt các loại lỗi.  