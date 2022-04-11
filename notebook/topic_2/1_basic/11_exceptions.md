# Chapter 11: Exceptions

Khi program vi phạm các ràng buộc về ngữ nghĩa của Jvava, thì JVM sẽ báo hiệu error này cho chương trình như một exception.

Java xác định rằng một exception sẽ được ném ra khi các các ràng buộc ngữ nghĩa bị vị phạm, và sẽ gây ra sự chuyển giao quyền kiểm soát non-local từ điểm xảy ra ngoại lệ đến điểm được xác định bởi programmer.

Một exception được cho là được ném từ điểm mà nó xảy ra, và được cho là bị bắt tại điểm mà quyền kiểm soát được chuyển giao.

Programs cũng có thể ném các exceptions tường minh, sử dụng *throw* statements.

Sử dụng tường minh các *throw* statements cung cấp một giải pháp thay thế cho việc xử lý các điều kiện lỗi kiểu cũ bằng cách trả về các giá trị 'hài hước', như giá trị nguyên -1 trong khi đó giá trị âm thường không được mong đợi.

Mỗi exception được đại diện bởi một instance của class *Throwable* hoặc một trong các subclasses của nó. Một object như vậy có thể được sử dụng để mang thông tin từ điểm mà exception xảy ra đến cho trình xử lý (handler) bắt nó. Các handlers được thiết lập bởi *catch* clauses của *try* statements.

Trong quá trình ném một exception, JVM đột ngột hoàn thành từng expressions, statements, method và constructor invocations, initializers, và field initialization expressions đã bắt đầu nhưng chưa hoàn thành việc thực thi trong thread hiện tại. Quá trình này tiếp tục cho đến khi tìm thấy một handler chỉ ra nó xử lý exception cụ thể đó bằng tên của class của exception đó hoặc một superclass của class của exception đó. Nếu không tìm thấy một handler như vậy, thì exception đó có thể được xử lý bởi một trong các phân cấp của hệ thống *uncaught exception handlers* - do đó cần tránh các exception không được xử lý.

Cơ chế exception của Java SE platform được tích hợp với mô hình đồng bộ hóa (synchronization model) của nó, để các monitors được unlock khi các synchronized statements và các invocations của các synchronized methods hoàn thành đột ngột.


## 1. The Kinds and Causes of Exceptions

### 1.1, The Kinds of Exceptions

Một exception được đại diện bởi một instance của class *Throwable* (một direct subclass của *Object*) hoặc một trong các subclasses của nó.

Throwable và tất cả các subclasses của nó gọi chung là các exception classes.

Các classes *Exception* và *Error* là các direct subclasses của *Throwable*:

- *Exception* là superclass của tất cả các exceptions mà các programs có thể muốn khôi phục.

    Class *RuntimeException* là một direct subclass của *Exception*. *RuntimeException* là superclass của tất cả các exceptions có thể được ném vì nhiều lý do trong quá trình đánh giá expression, nhưng vẫn có thể khôi phục.

    *RuntimeException* và tất cả các subclasses của nó được gọi chung là các run-time exception classes.

- *Error* là superclass của tất cả các exceptions mà các programs thường không được mong đợi khôi phục.

    *Error* và tất cả các subclasses của nó được gọi chung là các error classes.

Các *unchecked exception classes* là các *run-time exception classes* và các *error classes*.

Các *checked exception classes* là tất cả các *exception classes* khác với các *unchecked exception classes*. Nghĩa là, các *checked exception classes* là *Throwable* và tất cả các subclasses ngoài *RuntimeException* và các subclasses của nó và *Error* và các subclasses của nó.


### 1.2, The Causes of Exceptions

Một exception được ném vì 1 trong 3 lý do:

- Một *throw* statement được thực thi.  
- Một điều kiện thực thi bất thường được JVM phát hiện đồng bộ, cụ thể là:
    + Đánh giá một expression vi phạm ngữ nghĩa của Java, như chia một số nguyên cho 0.  
    + Xảy ra lỗi trong phần loading, linking, hoặc initializing của program; trong trường hợp này, một instance của một subclass của *LinkageError* được ném ra.  
    + Một lỗi nội bộ hoặc giới hạn tài nguyên ngăn JVM triển khai ngữ nghĩa của Java; trong trường hợp này, một instance của một subclass của *VirtualMachineError* được ném ra.  

    Những exceptions này không được ném ra tại một điểm tùy ý trong program, mà là tại một điểm mà chúng được xác định như là kết quả của việc đánh giá một expression hoặc thực thi một statement.  
- Một exception bất đồng bộ xảy ra.  


### 1.3, Asynchronous Exceptions

Hầu hết các exceptions xảy ra *đồng bộ* là kết quả của một action bởi một thread, và tại một điểm trong chương trình được xác định có thể dẫn đến một exception như vậy. Ngược lại, một exception *bất đồng bộ* là một exception mà có thể xảy ra tại bất kỳ điểm nào trong quá trình thực thi chương trình.

Các *asynchronous exceptions* chỉ xảy ra khi là kết quả của:

- Một invocation của (deprecated) *stop method* của class *Thread* hoặc *ThreadGroup*.  

    Các (deprecated) *stop methods* có thể được gọi bởi một thread ảnh hưởng đến một thread khác, hoặc tất cả các threads trong một thread group xác định. Chúng là bất đồng bộ vì chúng có thể xảy ra tại bất kỳ điểm nào trong quá trình thực thi của một thread hoặc các threads khác.

- Một lỗi nội bộ hoặc giới hạn tài nguyên trong JVM ngăn nó triển khai ngữ nghĩa của Java. Trong trường hợp này, asynchronous exception được ném ra là một instance của một subclass của *VirtualMachineError*.

*Ví dụ: StackOverflowError, một subclass của VirtualMachineError, có thể được ném đồng bộ bởi method invocation, cũng như bất đồng bộ vì native method execution hoặc JVM resource limitations.*


## 2. Compile-Time Checking of Exceptions

Java yêu cầu chương trình phải chứa các *handlers* cho các *checked exceptions* mà có thể là kết quả của việc thực thi một method hoặc constructor. *Compile-time checking* tìm các *exception handlers* được thiết kế để giảm số lượng các exceptions không được xử lý đúng cách. Đối với mỗi *checked exception* là một kết quả có khả năng xảy ra, throws clause cho method hoặc constructor phải đề cập đến class của exception đó hoặc một trong các superclasses của class của exception đó.

Các *checked exception classes* trong throws clause là một phần của hợp đồng giữa implementor (người triển khai) và user (người sử dụng) của method hoặc constructor. throws clause của một overriding method có thể không xác định rằng method này sẽ dẫn đến ném bất kỳ checked exception nào mà overridden method không được phép. Khi có sự tha gia của các interfaces, nhiều hơn một method declaration có thể bị overridden bởi một overriding declaration duy nhất. Trong trường hợp này, overriding declaration phải có một throws clause tương thích với tất cả các overridden declarations.

Các *unchecked exception classes* được miễn *compile-time checking*.

Một statement hoặc expression có thể ném một exception class E nếu, việc thực thi statement hoặc expression có thể dẫn đến một exception của class E được ném ra.

Một *catch clause* có thể bắt các exception class(es) có thể bắt được của nó:

- Exception class có thể bắt được của một *uni-catch clause* là type được khai báo của exception parameter của nó.  
- Các exception classes của một *multi-catch clause* là các alterntives (lựa chọn thay thế) trong union biểu thị type của exception parameter của nó.


### 2.1, Exception Analysis of Expressions

Một *class instance creation expression* có thể ném một exception class E nếu:

- Expression là một qualified class instance creation expression và qualifying expression có thể ném E; hoặc,  
- Một số expression của argument list có thể ném E; hoặc,  
- E là một trong các exception types của invocation type của constructor được chọn; hoặc,  
- Class instance creation expression bao gồm một ClassBody, và một số instance initializer hoặc instance variable initializer trong ClassBody có thể ném E.  

Một *method invocation expression* có thể ném một exception class E nếu một trong các điều sau là đúng:

- Method invocation expression có dạng 'Primary.[TypeArguments] Identifier' và Primary expression có thể ném E; hoặc,  
- Một số expression của argument list có thể ném E; hoặc,  
- E là một trong các exception types của invocation type của method được chọn.  

Một *lambda expression* không thể ném các exception classes.

Đối với các loại expression khác, expression có thể ném exception class E nếu một trong các các subexpressions tức thời của nó có thể ném E.


### 2.2, Exception Analysis of Statements

Một *throw* statement mà có thrown expression có static type E và không phải một final hoặc effectively final exception parameter, thì có thể ném E hoặc bất kỳ exception class nào mà thrown expression có thể ném.

*Ví dụ: Câu lệnh "throw new java.io.FileNotFoundException();" chỉ có thể ném java.io.FileNotFoundException. Về mặt hình thức, không phải trường hợp mà nó "có thể ném" một subclass hoặc superclass của java.io.FileNotFoundException.*

Một *throw* statement mà có thrown expression là một final hoặc effectively final exception parameter của một catch clause C, thì có thể ném một exception class E nếu tất cả các điều sau là đúng:

- E là một exception class mà khối lệnh try của try statement khai báo C có thể ném; và  
- E tương thích với bất kỳ exception classes nào có thể bắt được của C; và  
- E không tương thích với bất kỳ exception classes nào có thể bắt được của các catch clauses được khai báo ở bên trái của C trong cùng try statement.  

Một *try* statement có thể ném một exception class E nếu một trong các điều sau là đúng:

- Khối lệnh try có thể ném E, hoặc một expression được sử dụng để khởi tạo một resource (trong một try-with-resources statement) có thể ném E, hoặc lời gọi tự động của close() method của một resource (trong một try-with-resources statement) có thể ném E, và E không tương thích với bất kỳ exception class có thể bắt được của bất kỳ catch clause nào của statement, và không có khối finally nào hoặc khối finally có thể hoàn thành bình thường; hoặc  
- Một số khối catch của try statement có thể ném E và không có khối finally nào hoặc khối finally có thể hoàn thành bình thường; hoặc  
- Có một khối finally có thể ném E.  

Một *explicit constructor invocation statement* có thể ném một exception class E nếu một trong các điều sau là đúng:

- Một số expression của parameter list của constructor invocation có thể ném E; hoặc  
- E được xác định là một exception class của throws clause của constructor được gọi.  

Bất kỳ statement S nào khác có thể ném một exception class E nếu một expression hoặc statement được chứa tức thời trong S có thể ném E.


### 2.3, Exception Checking

Sẽ gây ra compile-time error nếu:

- Một method hoặc constructor body có thể ném một số exception class E khi E là một checked exception class và E không phải là một subclass của một số class được khai báo trong throws clause của method hoặc constructor.  
- Một lambda body có thể ném một số exception class E khi E là một checked exception class và E không phải một subclass của một số class được khai báo trong throws clause của function type được nhắm tới bởi lambda expression.  
- Một class variable initializer hoặc static initializer của một class hoặc interface được đặt tên có thể ném một checked exception class.  
- Một instance variable initializer hoặc instance initializer của một class được đặt tên có thể ném một checked exception class, trừ khi class được đặt tên có ít nhất một constructor được khai báo tường minh và exception class hoặc một trong các superclasses của nó được khai báo tường minh trong throws clause của mỗi constructor.  
- Một catch clause có thể bắt một checked exception class E1 và không phải trường hợp mà khối try tương ứng với catch clause đó có thể ném một checked exception class là một subclass hoặc superclass của E1, trừ khi E1 là Exception hoặc một superclass của Exception.  
- Một catch clause có thể bắt một exception class E1 và một catch clause phía trước của immediately enclosing try statement có thể bắt E1 hoặc một superclass của E1.  

*Ví dụ 1. Catching Checked Exceptions*

```java
import java.io.*;

class StaticallyThrownExceptionsIncludeSubtypes {
    public static void main(String[] args) {
        try {
            throw new FileNotFoundException();
        } catch (IOException ioe) {
            // "catch IOException" catches IOException and any subtype.
        }

        try {
            throw new FileNotFoundException();
              // Statement "can throw" FileNotFoundException.
              // It is not the case that statement "can throw" a subtype or supertype of FileNotFoundException.
        } catch (FileNotFoundException fnfe) {
            // ... Handle exception ...
        } catch (IOException ioe) {
            // Legal, but compilers are encouraged to give warnings as of Java SE 7,
            // because all subtypes of IOException that the try block "can throw"
            // have already been caught by the prior catch clause.
        }

        try {
            m();
              // m's declaration says "throws IOException", so m "can throw" IOException.
              // It is not the case that m "can throw" a subtype or supertype of IOException (e.g. Exception).
        } catch (FileNotFoundException fnfe) {
            // Legal, because the dynamic type of the exception might be FileNotFoundException.
        } catch (IOException ioe) {
            // Legal, because the dynamic type of the exception might be a different subtype of IOException.
        } catch (Throwable t) {
            // Can always catch Throwable.
        }
    }

    static void m() throws IOException {
        throw new FileNotFoundException();
    }
}
```

*Ví dụ 2: Theo các quy tắc trên, mỗi alternative trong một multi-catch clause phải có khả năng bắt một số exception class được ném bởi try block và không bị bắt bởi các catch clauses đứng trước.*

```java
// the second catch clause below would cause a compile-time error,
// because exception analysis determines that SubclassOfFoo is already caught by the first catch clause:
try { ... }
catch (Foo f) { ... }
catch (Bar | SubclassOfFoo e) { ... } // compile-time error
```


## 3. Run-Time Handling of an Exception

Khi một exception được ném, quyền kiểm soát được chuyển từ code gây ra exception sang catch clause động bao quanh gần nhất (nếu có) của một try statement có thể xử lý exception đó.

Một statement hoặc expression được bao bọc động bởi một catch clause nếu nó xuất hiện bên trong try block của try statement mà catch clause đó là một thành phần, hoặc nếu caller của statement hoặc expression được bao bọc động bởi catch clause đó.

Caller của một statement hoặc expression phụ thuộc vào nơi mà nó xảy ra:

- Nếu bên trong một method, thì caller là *method invocation expression* mà được thực thi để method được gọi.  
- Nếu bên trong một constructor hoặc một instance initializer hoặc một initializer cho một instance variable, thì caller là *class instance creation expression* hoặc *method invocation* của newInstance mà được thực thi để tạo một object.  
- Nếu bên trong một static initializer hoặc một initializer cho một static variable, thì caller là *expression* mà sử dụng class hoặc interface để khởi tạo nó.  

Liệu một catch clause cụ thể có thể xử lý một exception hay không được xác định bằng cách so sánh class của object được ném với các exception classes có thể bắt được của catch clause. catch clause có thể xử lý exception nếu một trong các exception classes có thể bắt được của nó là class của exception đó hoặc một superclass của class của exception đó.

Việc chuyển giao điều khiển xảy ra khi một exception được ném gây ra việc hoàn thành đột ngột của các expressions và statements cho đến khi gặp một catch clause có thể xử lý exception này; việc thực thi sau đó tiếp tục bằng cách thực thi block của catch clause đó. Code gây ra exception sẽ không bao giờ được tiếp tục thực thi.

Nếu không tìm thấy catch clause nào có thể xử lý exception, thì current thread (thread gặp exception) sẽ bị chấm dứt. Trước khi chấm dứt, tất cả các finally clauses đều được thực thi và uncaught exception được xử lý theo các quy tắc sau:

- Nếu current thread có một bộ uncaught exception handler, thì handler đó sẽ được thực thi.  
- Nếu không, method *uncaughtException* được gọi cho ThreadGroup là parent của current thread. Nếu ThreadGroup và parent ThreadGroups của nó không override uncaughtException, thì uncaughtException method của default handler sẽ được gọi.  

*Ví dụ 1. Throwing and Catching Exceptions*

```java
class TestException extends Exception {
    TestException()         { super(); }
    TestException(String s) { super(s); }
}

class Test {
    public static void main(String[] args) {
        for (String arg : args) {
            try {
                thrower(arg);
                System.out.println("Test \"" + arg + "\" didn't throw an exception");
            } catch (Exception e) {
                System.out.println(
                    "Test \"" + arg + "\" threw a " + e.getClass() + " with message: " + e.getMessage()
                );
            }
        }
    }

    // The declaration of the method thrower must have a throws clause,
    // because it can throw instances of TestException, which is a checked exception class.
    // A compile-time error would occur if the throws clause were omitted.
    static int thrower(String s) throws TestException {
        try {
            if (s.equals("divide")) {
                int i = 0;
                return i/i;
            }
            if (s.equals("null")) {
                s = null;
                return s.length();
            }
            if (s.equals("test")) {
                throw new TestException("Test message");
            }
            return 0;
        } finally {
            // The finally clause is executed on every invocation of thrower,
            // whether or not an exception occurs.
            System.out.println("[thrower(\"" + s + "\") done]");
        }
    }
}

// Execute the program: java Test divide null not test

// Output:  [thrower("divide") done]
//          Test "divide" threw a class java.lang.ArithmeticException with message: / by zero
//          [thrower("null") done]
//          Test "null" threw a class java.lang.NullPointerException with message: null
//          [thrower("not") done]
//          Test "not" didn't throw an exception
//          [thrower("test") done]
//          Test "test" threw a class TestException with message: Test message
```

**Note**:  

- Nếu muốn đảm bảo một block luôn được thực thi sau block khác, ngay cả khi block kia hoàn thành đột ngột, có thể sử dụng finally clause của một try statement.  
- Nếu một try hoặc catch block trong một try-finally hoặc try-catch-finally statement hoàn thành đột ngột, thì finally clause được thực thi trong quá trình truyền exception, ngay cả khi không tìm thấy catch clause nào phù hợp.  
- Nếu một finally clause được thực thi do sự hoàn thành đột ngột của try block và bản thân finally clause cũng hoàn thành đột ngột, thì lý do cho sự hoàn thành đột ngột của try block sẽ bị loại bỏ, và lý do mới sẽ được truyền.  