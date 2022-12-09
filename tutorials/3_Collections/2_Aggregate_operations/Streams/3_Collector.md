# The Collector Interface

```java
public interface Collector<T,A,R> {}
```

Một *mutable reduction operation* tích lũy các input elements vào một mutable result container, tùy chọn chuyển đổi accumulated result (kết quả tích lũy) thành một biểu diễn cuối cùng sau khi tất cả các input elements đã được xử lý. Các reduction operations có thể được thực hiện tuần tự hoặc song song.

Ví dụ về các mutable reduction operations bao gồm: tích lũy các elements vào một Collection; nối các strings bằng cách sử dụng StringBuilder; thông tin tóm tắt tính toán về các elements như sum, min, max hoặc average; tính toán tóm tắt "pivot table" (bảng tổng hợp) chẳng hạn như "giao dịch có giá trị tối đa của người bán", ... *Collectors* class cung cấp các implementations của nhiều mutable reductions phổ biến.

Một *Collector* được chỉ định bởi 4 functions làm việc với nhau để tích lũy các entries vào một mutable result container và tùy chọn thực hiện một biến đổi cuối cùng trên kết quả. Chúng thực hiện:

+ Tạo một result container mới (supplier()).  
+ Kết hợp một data element mới vào một result container (accumulator()).  
+ Kết hợp 2 result container thành một (combiner()).  
+ Thực hiện một biến đổi cuối cùng tùy chọn trên result container (finisher()).  

Các Collector cũng có một set các đặc điểm (characteristics), chẳng hạn như *Collector.Characteristics.CONCURRENT*, nó cung cấp các gợi ý có thể được sử dụng bởi một reduction implementation để mang lại hiệu suất tốt hơn.

Một sequential implementation của một reduction sử dụng một Collector, sẽ tạo ra một result container duy nhất bằng cách sử dụng supplier() function, và gọi accumulator() function một lần cho mỗi input element. Một parallel implementation sẽ phân vùng (partition) input, tạo một result container cho mỗi phân vùng, tích lũy content của mỗi phân vùng thành một subresult cho phân vùng đó, sau đó sử dụng combiner() function để hợp nhất các subresults thành một combined result.

Để đảm bảo rằng các thực thi tuần tự và song song tạo ra các kết quả tương đương, các collector phải thỏa mãn các *identity* và *associativity* constraints:

+ *identity* constraint nói rằng đối với bất kỳ partially accumulated result nào, việc kết hợp (combine) nó với một empty result container phải tạo ra một result tương đương.  

    Nghĩa là, đối với một partially accumulated result, a, là kết quả của bất kỳ chuỗi accumulator() và combiner() invocations nào, a phải tương đương với combiner.apply(a, supplier.get()).  

+ *Associativity* constraint nói rằng việc chia nhỏ phép tính phải tạo ra result tương đương.  
    
    ```
    associativity:
        (a op b) op c      ==    a op (b op c)
        a op b op c op d   ==    (a op b) op (c op d)
    ```  

    Nghĩa là, đối với bất kỳ input elements t1 và t2 nào, kết quả r1 và r2 trong phép tính dưới đây phải tương đương:  

    ```java
    A a1 = supplier.get();
    accumulator.accept(a1, t1);
    accumulator.accept(a1, t2);
    R r1 = finisher.apply(a1);  // result without splitting

    A a2 = supplier.get();
    accumulator.accept(a2, t1);
    A a3 = supplier.get();
    accumulator.accept(a3, t2);
    R r2 = finisher.apply(combiner.apply(a2, a3));  // result with splitting
    ```  

Đối với các *Collector* không có đặc tính *UNORDERED*, 2 accumulated results a1 và a2 là tương đương nếu finisher.apply(a1).equals(finisher.apply(a2)). Đối với các unordered collectors, sự tương đương được nới lỏng để cho phép non-equality liên quan đến sự khác biệt trong thứ tự. 

*Ví dụ: một unordered collector tích lũy các elements vào một List, sẽ coi 2 list là tương đương nếu chúng chứa các elements giống nhau, bỏ qua thứ tự.*

Các libraries triển khai reduction dựa trên Collector, chẳng hạn như **Stream.collect(Collector)**, phải tuân thủ các constraints sau:  

+ First argument được truyền cho accumulator() function, cả 2 arguments được truyền cho combiner() function, và argument được truyền cho finisher() function phải là result của invocation trước đó của result supplier, accumulator, hoặc combiner functions.  

+ Implementation không được thực hiện bất kỳ điều gì với result của bất kỳ result supplier, accumulator, hay combiner functions nào, ngoài việc truyền chúng lại cho các accumulator, combiner, hoặc finisher functions, hoặc trả về cho caller của reduction operation.  

+ Nếu một result được truyền đến combiner hoặc finisher function, và cùng object đó không được trả về từ function đó, thì nó sẽ không bao giờ được sử dụng lại.  

+ Khi một result được truyền đến combiner hoặc finisher function, nó sẽ không bao giờ được truyền đến accumulator function nữa.  

+ Đối với các non-concurrent collectors, bất kỳ result nào được trả về từ các result supplier, accumulator, hay combiner functions phải là thread-confined (hạn chế luồng) tuần tự. Điều này cho phép việc thu thập diễn ra song song mà Collector không cần triển khai bất kỳ đồng bộ hóa bổ sung nào. Reduction implementation phải quản lý rằng input được phân vùng đúng cách, các phân vùng được xử lý riêng biệt, và việc kết hợp chỉ xảy ra sau khi quá trình tích lũy hoàn tất.  

+ Đối với những concurrent collectors, implementation là tự do (nhưng không bắt buộc) triển khai reduction đồng thời. Một concurrent reduction là một reduction mà trong đó accumulator function được gọi đồng thời từ nhiều threads, sử dụng cùng một concurrently-modifiable result container, thay vì giữ result bị cô lập trong quá trình tích lũy. Một concurrent reduction chỉ nên được áp dụng nếu Collector có đặc tính Collector.Characteristics.UNORDERED, hoặc nếu dữ liệu gốc là unordered.  

Ngoài các predefined implementations trong **Collectors** class, các static factory methods **of(Supplier, BiConsumer, BinaryOperator, Characteristics...)** có thể được sử dụng để tạo các collectors. 

*Ví dụ: bạn có thể tạo một collector tích lũy các widgets vào một TreeSet với:*

```java
Collector<Widget, ?, TreeSet<Widget>> intoSet = Collector.of(
    TreeSet::new, 
    TreeSet::add,
    (left, right) -> { left.addAll(right); return left; }
);
```

*Hành vi trên cũng được triển khai bởi predefined collector Collectors.toCollection(Supplier)).*

**Note**: Thực hiện reduction operation với một *Collector* sẽ tạo ra kết quả tương đương với:

```java
R container = collector.supplier().get();
for (T t : data)
    collector.accumulator().accept(container, t);
return collector.finisher().apply(container);
```

Tuy nhiên, library có thể tự do phân vùng input, thực hiện reduction trên các phân vùng, sau đó sử dụng combiner function để kết hợp các partial results để đạt được parallel reduction. (Tùy thuộc vào reduction operation cụ thể, điều này có thể hoạt động tốt hơn hoặc kém hơn, tùy thuộc vào chi phí tương đối của các accumulator và combiner functions.)


## 1. Nested Static Enum Collector.Characteristics

Các *characteristics* chỉ ra thuộc tính của một *Collector*, có thể được sử dụng để tối ưu hóa các reduction implementations.

```java
public static enum Collector.Characteristics 
    extends Enum<Collector.Characteristics> {}
```

Các Enum Constants của Collector.Characteristics enum bao gồm:

```
Enum Constant   |                        Description
----------------|----------------------------------------------------------------------------------
CONCURRENT      | Chỉ ra rằng collector này là concurrent, có nghĩa là result container có thể hỗ
                | trợ accumulator function được gọi đồng thời với cùng một result container từ nhiều
                | threads.
----------------|----------------------------------------------------------------------------------
UNORDERED       | Chỉ ra rằng collection operation không cam kết bảo toàn thứ tự gặp gỡ của các
                | input elements.
----------------|----------------------------------------------------------------------------------
IDENTITY_FINISH | Chỉ ra rằng finisher function là identity function và có thể bị lược đi.
```


## 2. The Collector Static Methods

```
        Modifier and Type	    |                   Method and Description
--------------------------------|------------------------------------------------------------------
static <T,R> Collector<T,R,R>   | of(
                                |   Supplier<R> supplier, 
                                |   BiConsumer<R,T> accumulator, 
                                |   BinaryOperator<R> combiner, 
                                |   Collector.Characteristics... characteristics
                                | )
                                |
                                | - Trả về một Collector mới được mô tả bởi các supplier, accumulator,
                                |   và combiner functions đã cho. Returned Collector có thuộc tính 
                                |   Collector.Characteristics.IDENTITY_FINISH.
--------------------------------|------------------------------------------------------------------
static <T,A,R> Collector<T,A,R> | of(
                                |   Supplier<A> supplier, 
                                |   BiConsumer<A,T> accumulator, 
                                |   BinaryOperator<A> combiner, 
                                |   Function<A,R> finisher, 
                                |   Collector.Characteristics... characteristics
                                | )
                                |
                                | - Trả về một Collector mới được mô tả bởi các supplier, accumulator,
                                |   combiner, và finisher functions đã cho.
```


## 2. The Collector Non-Static Methods

```
        Modifier and Type	   |                   Method and Description
-------------------------------|-------------------------------------------------------------------
Set<Collector.Characteristics> | characteristics()
                               | - Trả về một Set của các Collector.Characteristics chỉ ra các đặc
                               |   điểm của Collector này.
-------------------------------|-------------------------------------------------------------------
Supplier<A>	                   | supplier()
                               | - Một function tạo và trả về một mutable result container mới.
-------------------------------|-------------------------------------------------------------------
BiConsumer<A,T>	               | accumulator()
                               | - Một function đưa một value vào một mutable result container.
-------------------------------|-------------------------------------------------------------------
BinaryOperator<A>	           | combiner()
                               | - Một function nhận 2 partial results và merge chúng. 
                               | - Combiner function có thể đưa các state từ argument này sang
                               |   argument kia, hoặc trả về một mutable result container mới.
-------------------------------|-------------------------------------------------------------------
Function<A,R>	               | finisher()
                               | - Thực hiện phép biến đổi cuối cùng từ type tích lũy trung gian A
                               |   thành type kết quả cuối cùng R.
                               | - Nếu thuộc tính IDENTITY_TRANSFORM được set, thì function này có
                               |   thể được coi là một phép biến đổi identity với một unchecked cast
                               |   từ A sang R.
```