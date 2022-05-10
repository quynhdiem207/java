# Chapter 14: Blocks and Statements

## 12. The break Statement

Một *break* statement chuyển điều khiển ra khỏi một statement bao quanh.

```
- BreakStatement:
    break [Identifier] ;
```

Một break statement không có label sẽ cố gắng chuyển điều khiển cho switch, while, do, hoặc for statement bao quanh trong cùng của method hoặc initializer bao quanh tức thời; statement này được gọi là break target (đích ngắt), sau đó lập tức hoàn thành bình thường.

*Phải có một switch, while, do, hoặc for statement trong method, constructor, hoặc initializer bao bọc tức thời chứa break statement, nếu không sẽ gây ra compile-time error.*

Một break statement với label Identifier cố gắng chuyển điều khiển cho labeled statement bao quanh có cùng Identifier với label của nó; statement này được gọi là break target (đích ngắt), sau đó lập tức hoàn thành bình thường. *Trong trường hợp này, break target không cần phải là một switch, while, do, hay for statement.*

*Một break statement phải tham chiếu đến một label bên trong method, constructor, initializer, or lambda body bao quanh tức thời. Không có bước nhảy non-local. Nếu không có labeled statement với Identifier là label của nó trong method, constructor, initializer, or lambda body bao quanh tức thời chứa break statement, sẽ gây ra compile-time error.*

Một break statement dù có hay không có label luôn hoàn thành đột ngột.  

```java
// 1. Một đồ thị toán học được đại diện bởi một mảng chứa các mảng.
//    Một đồ thị bao gồm 1 tập các nút và 1 tập các cạnh;
// 2. Các nút được đại diện bởi các số nguyên, và có một cạnh từ nút i đến các cạnh nút [i][j].
// 3. Nhiệm vụ của method loseEdges, với số nguyên i và j đã cho, 
//    xây dựng một đồ thị mới bằng việc sao chép một đồ thị đã cho,
//    nhưng loại bỏ cạnh từ nút i đến nút j (nếu có), và cạnh từ nút j đến nút i (nếu có):
class Graph {
    int edges[][];
    public Graph(int[][] edges) { this.edges = edges; }

    public Graph loseEdges(int i, int j) {
        int n = edges.length;
        int[][] newedges = new int[n][];
        for (int k = 0; k < n; ++k) {
            edgelist:
            {
                int z;
                search:
                {
                    if (k == i) {
                        for (z = 0; z < edges[k].length; ++z) {
                            if (edges[k][z] == j) break search;
                        }
                    } else if (k == j) {
                        for (z = 0; z < edges[k].length; ++z) {
                            if (edges[k][z] == i) break search;
                        }
                    }

                    // No edge to be deleted; share this list.
                    newedges[k] = edges[k];
                    break edgelist;
                } //search

                // Copy the list, omitting the edge at position z.
                int m = edges[k].length - 1;
                int ne[] = new int[m];
                System.arraycopy(edges[k], 0, ne, 0, z);
                System.arraycopy(edges[k], z+1, ne, z, m-z);
                newedges[k] = ne;
            } //edgelist
        }
        return new Graph(newedges);
    }
}
```


## 13. The continue Statement

Một *continue* statement có thể xuất hiện trong một while, do, hay for statement; các statements của 3 loại này được gọi là các iteration statements (câu lệnh lặp). Điều khiển được chuyển đến điểm tiếp tục lặp lại của một iteration statement.

```
- ContinueStatement:
    continue [Identifier] ;
```

Một continue statement không có label cố gắng chuyển điều khiển tới một while, do, hay for statement bao quanh trong cùng của method, constructor, hay initializer bao quanh tức thời; statement này được gọi là continue target (đích tiếp tục), sau đó ngay lập tức kết thúc iteration (bước lặp) hiện tại và bắt đầu một iteration mới.

*Phải có một while, do, hay for statement của method, constructor, hay initializer bao quanh tức thời chứa continue statement, nếu không sẽ gây ra compile-time error.*

Một continue statement với label Identifier cố gắng chuyển điều khiển tới labeled statement bao quanh có cùng Identifier với label của nó; statement đó được gọi là continue target (đích tiếp tục), sau đó lập tức kết thúc iteration hiện tại và bắt đầu một iteration mới.

*continue target phải là một while, do, hoặc for statement, nếu không sẽ gây ra compile-time error.*

*Một continue statement phải tham chiếu đến một label bên trong method, constructor, initializer, or lambda body bao quanh tức thời. Không có bước nhảy non-local. Nếu không có labeled statement với Identifier là label của nó trong method, constructor, initializer, or lambda body bao quanh tức thời chứa continue statement, sẽ gây ra compile-time error.*

Một continue statement dù có hay không có label luôn hoàn thành đột ngột.  

```java
class Graph {
    int edges[][];
    public Graph(int[][] edges) { this.edges = edges; }

    public Graph loseEdges(int i, int j) {
        int n = edges.length;
        int[][] newedges = new int[n][];
        edgelists:
        for (int k = 0; k < n; ++k) {
            int z;
            search:
            {
                if (k == i) {
                    for (z = 0; z < edges[k].length; ++z) {
                        if (edges[k][z] == j) break search;
                    }
                } else if (k == j) {
                    for (z = 0; z < edges[k].length; ++z) {
                        if (edges[k][z] == i) break search;
                    }
                }

                // No edge to be deleted; share this list.
                newedges[k] = edges[k];
                continue edgelists;
            } //search

            // Copy the list, omitting the edge at position z.
            int m = edges[k].length - 1;
            int ne[] = new int[m];
            System.arraycopy(edges[k], 0, ne, 0, z);
            System.arraycopy(edges[k], z+1, ne, z, m-z);
            newedges[k] = ne;
        } //edgelists
        return new Graph(newedges);
    }
}
```


## 14. The return Statement

Một *return* statement trả điều khiển cho invoker (lời gọi) của một method hay constructor.

```
- ReturnStatement:
    return [Expression] ;
```

Một return statement được chứa bên trong một constructor, method, initializer, hay lambda expression trong cùng có body bao bọc return statement.

*Nếu một return statement được chứa trong một instance initializer hoặc một static initializer, sẽ gây compile-time error.*

Sẽ gây compile-time error nếu return statement không có Expression không được chứa trong một trong các:  

- Một method được khai báo với keyword void, không trả về một giá trị,  
- Một constructor,  
- Một lambda expression.  

Một return statement không có Expression cố gắng chuyển điều khiển tới invoker của method, constructor, hoặc lambda body chứa nó. 

Sẽ gây compile-time error nếu một return statement với một Expression không được chứa trong một trong các:

- Một method được khai báo để trả về một giá trị,  
- Một lambda expression.  

*Expression phải là một variable hoặc một giá trị, nếu không sẽ gây compile-time error.*

*Khi một return statement với một Expression xuất hiện trong một method declaration, thì Expression phải có thể gán cho return type đã khai báo của method, nếu không sẽ gây ra compile-time error.*

Một return statement với một Expression cố gắng chuyển điều khiển cho invoker của method hoặc lambda body chứa nó; giá trị của Expression trở thành giá trị của method invocation. (Khi thực thi một return statement với một Expression trước tiên sẽ đánh giá Expression.)

Một return statement dù có hay không có Expression luôn hoàn thành đột ngột.

```java
class Test {
    void test(int n) {
        if(n < 0) return;
        System.out.println("Test...");
    }
}
```


## 15. The throw Statement

Một throw statement khiến một exception được ném ra. Kết quả là một chuyển giao điều khiển ngay lập tức cho đến khi tìm thấy một try statement bắt giá trị được ném ra. Nếu không tìm thấy một try statement như vậy, thì việc thực thi của thread mà đã thực hiện ném exception sẽ bị chấm dứt sau khi gọi uncaughtException method cho thread group mà thread đó thuộc về.

```
- ThrowStatement:
    throw Expression ;
```

Expression trong một throw statement phải là một variable hoặc giá trị của một reference type có thể gán cho type Throwable, hoặc một null reference, nếu không sẽ gây ra compile-time error.

Ít nhất phải thỏa mãn 1 trong 3 điều kiện sau, nếu không sẽ gây ra compile-time error:

- Type của Expression là một unchecked exception class hoặc null type.  
- throw statement được chứa trong một try block của một try statement và không phải trường hợp mà try statement có thể ném một exception với type của Expression. (Trong trường hợp này giá trị được ném ra bị bắt bởi try statement.)  
- throw statement được chứa trong một method hoặc constructor declaration và type của Expression có thể gán cho ít nhất một type được liệt kê trong throws clause của declaration.  

Một throw statement được thực thi, trước tiên sẽ đánh giá Expression. Sau đó:

- Nếu việc đánh giá của Expression hoàn thành đột ngột vì lý do nào đó, thì throw completes hoàn thành đột ngột vì lý do đó.  
- Nếu việc đánh giá của Expression hoàn thành bình thường, tạo ra một giá trị non-null V, sau đó throw statement hoàn thành đột ngột, với lý do là  một throw với giá trị V.  
- Nếu việc đánh giá của Expression hoàn thành bình thường, tạo ra một giá trị null, sau đó một instance V' của class NullPointerException được tạo và ném thay vì null. Sau đó, throw statement hoàn thành đột ngột, với lý do là  một throw với giá trị V'.  

Nếu có bất kỳ *try statements* bao quanh nào có try blocks chứa throw statement, thì bất kỳ finally clauses nào của những try statements này được thực thi khi điều khiển được chuyển ra ngoài, cho đến khi giá trị được ném ra bị bắt.

Nếu một throw statement được chứa trong một *method declaration* hoặc một *lambda expression*, nhưng giá trị của nó không được bắt bởi các try statement chứa nó, thì invocation của method hoàn thành đột ngột vì throw.

Nếu một throw statement được chứa trong một *constructor declaration*, nhưng giá trị của nó không được bắt bởi các try statement chứa nó, thì class instance creation expression đã gọi constructor sẽ hoàn thành đột ngột vì throw.

Nếu một throw statement được chứa trong một *static initializer*, thì một compile-time check đảm bảo rằng giá trị của nó luôn là một unchecked exception hoặc giá trị của nó luôn bị bắt bởi try statement chứa nó.

Nếu một throw statement được chứa trong một *instance initializer*, thì một compile-time check đảm bảo rằng giá trị của nó luôn là một unchecked exception hoặc giá trị của nó luôn bị bắt bởi try statement chứa nó, hoặc type của exception được ném (hoặc một trong các superclasses của nó) xuất hiện trong throws clause của mọi constructor của class đó.

```java
class Test {
    void eat() {
        throw new IOException();    // Compile-time error
    }

    void run() {
        throw new OutOfMemoryError(); // OK
    }

    void eat() throws IOException {
        throw new IOException();    // OK
    }

    void talk() {
        try {
           throw new IOException(); // OK
       } catch (IOException e) {}
    }
}
```


## 16. The synchronized Statement

Một *synchronized* statement nhận được một mutual-exclusion lock (khóa loại trừ lẫn nhau) thay mặt cho thread thực thi, thực thi một block, sau đó giải phóng khóa. Trong khi thread thực thi sở hữu khóa, không luồng nào khác có thể nhận được khóa.

```
SynchronizedStatement:
    synchronized ( Expression ) Block
```

Type của Expression phải là một reference type, nếu không sẽ gây ra compile-time error.

Một synchronized statement được thực thi, trước tiên sẽ đánh giá Expression. Sau đó:

- Nếu việc đánh giá Expression hoàn thành đột ngột vì lý do nào đó, thì synchronized statement hoàn thành đột ngột vì lý do đó.  
- Ngược lại, nếu giá trị của Expression là null, thì một NullPointerException sẽ được ném ra.  
- Ngược lại, gọi giá trị non-null của Expression là V. Thread thực thi khóa (lock) monitor được liên kết với V. Sau đó Block được thực thi, và:  
    + Nếu việc thực thi Block hoàn thành bình thường, thì monitor được giải khóa (unlocked) và the synchronized statement hoàn thành bình thường.  
    + Nếu việc thực thi Block hoàn thành đột ngột vì lý do nào đó, thì monitor được giải khóa (unlocked) và synchronized statement hoàn thành đột ngột vì cùng lý do.

Các khóa (locks) nhận được được bởi các *synchronized statements* giống với các khóa (locks) nhận được ngầm định bởi *synchronized methods*. Một thread riêng lẻ có thể nhận được một khóa (lock) nhiều hơn một lần.

Việc nhận được khóa (lock) được liên kết với một object bản thân nó không ngăn các threads khác truy cập các fields của object hay gọi các un-synchronized methods trên object đó. Các threads khác cũng có thể sử dụng synchronized methods hoặc synchronized statement theo một cách thông thường để loại trừ lẫn nhau.

```java
// Note: chương trình sẽ bế tắc nếu một thread riêng lẻ không được phép lock một monitor nhiều hơn 1 lần.
class Test {
    public static void main(String[] args) {
        Test t = new Test();
        synchronized(t) {
            synchronized(t) {
                System.out.println("made it!");
            }
        }
    }
}
// Output: made it!
```


## 17. The try statement

Một *try* statement thực thi một block. Nếu một giá trị được ném ra và try statement có một hoặc nhiều catch clauses có thể bắt nó it, thì điều khiển sẽ được chuyển cho catch clause đầu tiên. Nếu try statement có một finally clause, thì finally block sẽ được thực thi, bất kể try block hoàn thành bình thường hay đột ngột, và bất kể một catch clause có được cấp quyền điều khiển hay không.

```
- TryStatement:
    try Block Catches
    try Block [Catches] Finally
    TryWithResourcesStatement
- Catches:              CatchClause {CatchClause}
- CatchClause:          catch ( CatchFormalParameter ) Block
- CatchFormalParameter: {VariableModifier} CatchType VariableDeclaratorId
- CatchType:            UnannClassType {| ClassType}
- Finally:              finally Block

- VariableModifier:
    (one of)
    Annotation final
- VariableDeclaratorId: Identifier [Dims]
- Dims:                 {Annotation} [ ] {{Annotation} [ ]}
```

Một try statement có thể có các *catch clauses*, cũng được gọi là các *exception handlers*. Một catch clause khai báo chính xác một parameter, được gọi là một *exception parameter*.

Một exception parameter có thể bểu thị type của nó là một class type riêng lẻ hay một union của hai hay nhiều class types (được gọi là các alternatives). Các alternatives của một union về cú pháp được phân tách bởi |.

Một catch clause được gọi là *uni-catch clause* nếu có exception parameter được biểu thị là một class type riêng lẻ, hay *multi-catch clause* nếu có exception parameter được biểu thị là một union của các types.

Sẽ gây ra compile-time error nếu:

- Mỗi class type được sử dụng để biểu thị type của một exception parameter không phải là class *Throwable* hoặc một subclass của Throwable, hoặc  
- Một type variable được sử dụng để biểu thị type của một exception parameter, hoặc  
- Một union của các types chứa 2 alternatives Di và Dj (i ≠ j) trong đó Di là một subtype của Dj.  

Một exception parameter của một *multi-catch clause* được khai báo ngầm định là *final* nếu nó không khai báo tường minh là final.  

Một exception parameter của một *uni-catch clause* KHÔNG được khai báo ngầm định final, nhưng nó có thể được khai báo tường minh final hoặc là effectively final.

```java
// Một multi-catch clause có thể được coi là một chuỗi các uni-catch clauses. 
try {
    // ... throws ReflectiveOperationException ...
}
catch (ClassNotFoundException | IllegalAccessException ex) {
    // ... body ...
}

// code trên tương đương với:
try {
    // ... throws ReflectiveOperationException ...
}
catch (final ClassNotFoundException ex1) {
    final ReflectiveOperationException ex = ex1;
    // ... body ...
}
catch (final IllegalAccessException ex2) {
    final ReflectiveOperationException ex = ex2;
    // ... body ...
}
```


### 17.1. Execution of try-catch

Khi một try statement không có một finally block được thực thi, trước tiên sẽ thực thi try block. Sau đó:

- Nếu việc thực thi try block hoàn thành bình thường, thì try statement hoàn thành bình thường.  
- Nếu việc thực thi try block hoàn thành đột ngột vì một throw của một value V, thì:  
    + Nếu run-time type của V tương thích gán với một exception class có thể bắt được của bất kỳ catch clause nào của try statement, thì catch clause đầu tiên (ngoài cùng bên trái) được chọn. Giá trị V được gán cho parameter của catch clause được chọn, và Block của catch clause đó được thực thi, sau đó:  
        + Nếu block đó hoàn thành bình thường, thì try statement hoàn thành bình thường.  
        + Nếu block đó hoàn thành đột ngột vì lý do nào đó, thì try statement hoàn thành đột ngột vì cùng lý do.  
    + Nếu run-time type của V KHÔNG tương thích gán với exception class có thể bắt được của bất kỳ catch clause nào của try statement, thì try statement hoàn thành đột ngột vì một throw của một value V.  
- Nếu việc thực thi try block hoàn thành đột ngột vì lý do nào khác, thì try statement hoàn thành đột ngột vì cùng lý do.  

*Ví dụ: Catching An Exception*

```java
class BlewIt extends Exception {
    BlewIt() { }
    BlewIt(String s) { super(s); }
}

// the exception BlewIt is thrown by the method blowUp.
// The try-catch statement in the body of main has two catch clauses.
// The run-time type of the exception is BlewIt which is not assignable to a variable of type RuntimeException,
// but is assignable to a variable of type BlewIt.
class Test {
    static void blowUp() throws BlewIt { 
        throw new BlewIt(); 
    }

    public static void main(String[] args) {
        try {
            blowUp();
        } catch (RuntimeException r) {
            System.out.println("Caught RuntimeException");
        } catch (BlewIt b) {
            System.out.println("Caught BlewIt");
        }
    }
}
// Output: Caught BlewIt
```


### 17.2. Execution of try-finally and try-catch-finally

Khi một try statement với một finally block được thực thi, trước tiên sẽ thực thi try block. Sau đó:  

- Nếu việc thực thi try block hoàn thành bình thường, thì finally block được thực thi, sau đó:  
    + Nếu finally block hoàn thành bình thường, thì try statement hoàn thành bình thường.  
    + Nếu finally block hoàn thành đột ngột vì lý do S, thì try statement hoàn thành đột ngột vì lý do S.  

- Nếu việc thực thi try block hoàn thành đột ngột vì một throw của một value V, thì:  
    + Nếu run-time type của V tương thích gán với một exception class có thể bắt được của bất kỳ catch clause nào của try statement, thì catch clause đầu tiên (ngoài cùng bên trái) được chọn. Giá trị V được gán cho parameter của catch clause được chọn, và Block của catch clause đó được thực thi, sau đó:  
        + Nếu catch block hoàn thành bình thường, thì finally block được thực thi. sau đó:  
            + Nếu finally block hoàn thành bình thường, thì try statement hoàn thành bình thường.  
            + Nếu finally block hoàn thành đột ngột vì lý do nào đó, thì try statement hoàn thành đột ngột vì cùng lý do.  
        + Nếu catch block hoàn thành đột ngột vì lý do R, thì finally block được thực thi. sau đó:  
            + Nếu finally block hoàn thành bình thường, thì try statement hoàn thành đột ngột vì lý do R.  
            + Nếu finally block hoàn thành đột ngột vì lý do S, thì try statement hoàn thành đột ngột vì lý do S (và lý do R bị loại bỏ).  

    + Nếu run-time type của V KHÔNG tương thích gán với một exception class có thể bắt được của bất kỳ catch clause nào của try statement, thì finally block được thực thi. sau đó:  
        + Nếu finally block hoàn thành bình thường, thì try statement hoàn thành đột ngột vì một throw của value V.  
        + Nếu finally block hoàn thành đột ngột vì lý do S, thì try statement hoàn thành đột ngột vì lý do S (và throw của value V bị loại bỏ và bị lãng quên).  

- If việc thực thi try block hoàn thành đột ngột vì bất kỳ lý do R nào khác, thì finally block được thực thi, và sau đó:  
    + Nếu finally block hoàn thành bình thường, thì try statement hoàn thành đột ngột vì lý do R.  
    + Nếu finally block hoàn thành đột ngột vì lý do S, thì try statement hoàn thành đột ngột vì lý do S (và lý do R bị loại bỏ).  

*Ví dụ: Handling An Uncaught Exception With finally*

```java
class BlewIt extends Exception {
    BlewIt() { }
    BlewIt(String s) { super(s); }
}

// NullPointerException (là một loại RuntimeException),
// được ném bởi method blowUp không được bắt bởi try statement trong main,
// vì một NullPointerException không thể gán cho một variable của type BlewIt.
// finally clause được thực thi, sau đó thread mà thực thi main, là thread duy nhất của test program,
// sẽ kết thúc vì một uncaught exception, thường dẫn đến việc in ra exception name và một simple backtrace.
// Tuy nhiên, không bắt buộc phải có một backtrace.

// Vấn đề với việc bắt buộc một backtrace là một exception có thể được tạo tại một điểm trong program,
// và ném tại một điểm sau đó. Việc lưu trữ một stack trace trong một exception là cực kỳ tốn kém,
// trừ khi nó thực sự được ném ra (trong trường họp này trace có thể được tạo trong khi dỡ khỏi stack).
// Do đó không bắt buộc một back trace trong mọi exception.
class Test {
    static void blowUp() throws BlewIt {
        throw new NullPointerException(); // a RuntimeException
    }
    public static void main(String[] args) {
        try {
            blowUp();
        } catch (BlewIt b) {
            System.out.println("Caught BlewIt");
        } finally {
            System.out.println("Uncaught Exception");
        }
    }
}

// Output: Uncaught Exception
//         Exception in thread "main" java.lang.NullPointerException
//              at Test.blowUp(Test.java:7)
//              at Test.main(Test.java:11)
```


### 17.3. try-with-resources

Một *try-with-resources* statement được tham số hóa với các variables (được gọi là các resources) được khởi tạo trước khi thực thi try block và được đóng tự động, theo thứ tự ngược lại mà chúng được khởi tạo, sau khi thực thi try block. Các catch clauses và một finally clause thường không cần thiết khi các resources được đóng tự động.

```
- TryWithResourcesStatement: try ResourceSpecification Block [Catches] [Finally]
- ResourceSpecification:     ( ResourceList [;] )
- ResourceList:              Resource {; Resource}
- Resource:                  {VariableModifier} UnannType VariableDeclaratorId = Expression

- VariableModifier:
    (one of)
    Annotation final
- VariableDeclaratorId: Identifier [Dims]
- Dims:                 {Annotation} [ ] {{Annotation} [ ]}
```

Một variable được khai báo trong resource specification được ngầm định là *final* nếu nó không được khai báo tường minh là final.

Type của một variable được khai báo trong một resource specification phải là một subtype của *AutoCloseable*, nếu không sẽ gây ra compile-time error.

Các resources được khởi tạo theo thứ tự từ trái sang phải. Nếu một resource thất bại khi khởi tạo (nghĩa là, initializer expression của nó ném một exception), thì tất cả các resources đã được khởi tạo trước đó bởi try-with-resources statement sẽ bị closed. Nếu tất cả các resources khởi tạo thành công, thì try block được thực thi như bình thường, sau đó tất cả các non-null resources của try-with-resources statement bị closed.  

Các resources bị closed theo thứ tự ngược lại với mà chúng được khởi tạo. Một resource chỉ được closed nếu nó được khởi tạo thành một non-null value. Một exception từ việc đóng một resource không ngăn cản việc đóng các resources khác. Một exception như vậy sẽ bị loại bỏ nếu một exception được ném trước đó bởi một initializer, try block, hoặc việc đóng một resource.

Một try-with-resources statement có resource specification khai báo nhiều resources được coi như thể nó là nhiều try-with-resources statements, mỗi statement có một resource specification khai báo một resource. Khi một try-with-resources statement với n resources (n > 1) được dịch, kết quả là một try-with-resources statement với n-1 resources. Sau n lần dịch như vậy, sẽ có n try-catch-finally statements được lồng nhau.

```java
static String readFirstLineFromFile(String path) throws IOException {
    // FileReader and BufferedReader are subtypes of interface type AutoCloseable
    try (
        FileReader fr = new FileReader("path"); 
        BufferedReader br = new BufferedReader(fr)
    ) {
        return br.readLine();
    }
}
```


## 18. Unreachable Statements

Nếu một statement không thể được thực thi vì nó là *unreachable*, sẽ gây ra compile-time error.