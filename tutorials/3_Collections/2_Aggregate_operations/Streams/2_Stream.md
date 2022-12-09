# The Stream Interface 

Một *stream* là một sequence (chuỗi) các elements hỗ trợ các sequential và parallel aggregate operations.

```java

public interface Stream<T> extends BaseStream<T, Stream<T>> {}
```

*Ví dụ: Tạo một stream của các Widget objects thông qua Collection.stream() operation từ một collection widgets. Thực hiện filter() để tạo ra một stream chỉ chứa các red widgets, sau đó mapToInt() chuyển nó thành một stream của các int values đại diện cho trọng lượng của mỗi red widgets. Tiếp đó, sum() tính tổng các elements của stream này để tạo ra tổng trọng lượng:*

```java
  int sum = widgets.stream()
    .filter(w -> w.getColor() == RED)
    .mapToInt(w -> w.getWeight())
    .sum();
```

Ngoài **Stream**, là một stream của các object references, còn có các stream interface chuyên biệt khác cho các primitive types: **IntStream**, **LongStream** và **DoubleStream**. Tất cả đều được gọi chung là các "streams".

Để thực hiện tính toán, các stream operators được tạo thành một *stream pipeleine*. Một stream pipeline bao gồm:

- Một source (có thể là một array, một collection, một generator function, một I/O channel, ...),  
- Không hoặc nhiều intermediate operations (tạo một stream khác),  
- Một terminal operator (tạo ra một kết quả non-stream hoặc side-effect).  

Các collections và streams, mặc dù bề ngoài mang một số điểm tương đồng, nhưng có các mục tiêu khác nhau:

- Các collections chủ yếu quan tâm đến việc quản lý hiệu quả và truy cập vào các elements của chúng.  
- Ngược lại, các stream không cung cấp phương tiện để truy cập hoặc thao tác trực tiếp các elements của chúng, thay vào đó nó quan tâm đến việc mô tả source của chúng và các hoạt động tính toán sẽ được thực hiện tổng hợp trên source đó. Tuy nhiên, nếu các stream operators được cung cấp không cung cấp chức năng mong muốn, thì các operations *BaseStream.iterator()* và *BaseStream.spliterator()* có thể được sử dụng để thực hiện duyệt qua các elements.  

Một stream pipeline, có thể được xem như một query (truy vấn) trên stream source. Trừ khi source được thiết kế tường minh cho việc sửa đổi đồng thời (chẳng hạn như ConcurrentHashMap), các hành vi không thể đoán trước hoặc sai sót có thể dẫn đến việc sửa đổi stream source trong khi nó đang được truy vấn.

Hầu hết các stream operations chấp nhận các parameters mô tả các user-specified behavior, các parameters như vậy là các instances của các functional interfaces (chẳng hạn như Function), và thường là các lambda expressions hay method references.

Stream có một *BaseStream.close()* và triển khai *AutoCloseable*, nhưng gần như tất cả các stream instances thực sự không cần phải đóng sau khi sử dụng. Nói chung, chỉ các streams có source là một *I/O channel* (chẳng hạn như các streams được trả về bởi *Files.lines(Path, Charset)*) mới yêu cầu đóng. (Nếu một stream yêu cầu đóng, nó có thể được khai báo như một resource trong try-with-resources statement.) 
Hầu hết các streams được sao lưu bởi các collections, arrays hoặc các generating functions, không yêu cầu việc special resource management. 

Các stream pipelines có thể thực thi tuần tự hoặc song song. Execution mode này là một thuộc tính của stream. Các streams được tạo với sự lựa chọn ban đầu là thực thi tuần tự hoặc song song. Ví dụ:

- *Collection.stream()* tạo một sequential stream,  
- *Collection.parallelStream()* tạo một parallel stream.  

Việc lựa chọn execution mode có thể được sửa đổi bởi các methods *BaseStream.sequential()* hoặc *BaseStream.parallel()* và có thể được truy vấn bằng method *BaseStream.isParallel()*.


## 1, static nested interface [Stream.Builder<T>]

[Stream.Builder<T>] là một *mutable builder* cho một Stream. Nó cho phép tạo một *Stream* bằng cách tạo các elements riêng lẻ và thêm chúng vào *Builder* (mà không cần sử dụng một ArrayList làm buffer tạm thời).

```java
public static interface Stream.Builder<T> extends Consumer<T>
```

Một stream builder có một life cycle:  

- Bắt đầu trong một building phase (pha xây dựng), trong đó các elements có thể được thêm vào builder,  
- Sau đó chuyển sang một built phase (pha đã xây dựng), lúc này không thể thêm các elements vào builder nếu không một IllegalStateException sẽ được ném ra, bắt đầu khi build() method được gọi, method này tạo một ordered Stream có các elements theo thứ tự đã được thêm vào stream builder.  

Interface này bao gồm các methods sau:

```
Modifier and Type         |                        Method and Description
--------------------------|------------------------------------------------------------------------
void	                  | accept(T t)
                          | - Thêm một element vào stream đang được tạo.
--------------------------|------------------------------------------------------------------------
default Stream.Builder<T> |	add(T t)
                          | - Thêm một element vào stream đang được tạo.
--------------------------|------------------------------------------------------------------------
Stream<T>	              | build()
                          | - Tạo stream, chuyển builder này sang built state. 
                          | - Một IllegalStateException sẽ được ném ra nếu cố thêm elements vào
                          |   builder sau khi nó đã chuyển sang built state.
```


## 2, Stream Operations

### 2.1, Static methods

```
Modifier and Type            |                        Method and Description
-----------------------------|---------------------------------------------------------------------
static <T> Stream.Builder<T> | builder()
                             | - Trả về một Stream builder.
-----------------------------|---------------------------------------------------------------------
static <T> Stream<T>	     | empty()
                             | - Trả về một empty sequential stream.
-----------------------------|---------------------------------------------------------------------
static <T> Stream<T>	     | concat(Stream<? extends T> a, Stream<? extends T> b)
                             | - Tạo một stream được nối từ 2 arguments là các streams, có các 
                             |   elements là tất cả các elements của 1st stream, theo sau là tất cả
                             |   các elements của 2nd stream.
                             | - Returned stream được sắp xếp theo thứ tự nếu cả 2 input stream được
                             |   sắp xếp theo thứ tự, song song nếu 1 trong 2 input stream song song.
                             | - Khi returned stream bị đóng, các close handler cho cả 2 input stream
                             |   sẽ được gọi.
-----------------------------|---------------------------------------------------------------------
static <T> Stream<T>	     | generate(Supplier<T> s)
                             | - Trả về một "infinite sequential unordered stream", trong đó mỗi
                             |   element được tạo bởi Supplier đã cung cấp.
                             | - Phù hợp để tạo các constant streams, stream của các random elements,
                             |   ...
                             | - Supplier<T> là một functional interface có method là "T get()".
-----------------------------|---------------------------------------------------------------------
static <T> Stream<T>	     | iterate(T seed, UnaryOperator<T> f)
                             | - Trả về một "infinite sequential ordered stream", được tạo ra bằng
                             |   cách áp dụng lặp đi lặp lại function f cho element seed ban đầu.
                             | - Stream được tạo ra bao gồm các elements: seed, f(seed), f(f(seed)), ...
                             | - UnaryOperator<T> là một functional interface được mở rộng từ
                             |   Function<T, T> với functional method "T apply(T)".
-----------------------------|---------------------------------------------------------------------
static <T> Stream<T>	     | of(T... values)
                             | - Trả về một "sequential ordered stream" có các elements là các values
                             |   được chỉ định.
-----------------------------|---------------------------------------------------------------------
static <T> Stream<T>	     | of(T t)
                             | - Trả về một sequential stream có chứa một element duy nhất được chỉ
                             |   định.

```


### 2.2, Non-static Methods

#### *2.2.1, The Intermediate Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
Stream<T>	      | distinct()
                  | - Trả về một Stream bao gồm các elements khác biệt (theo Object.equals(Obkect))
                  |   của stream này.
------------------|--------------------------------------------------------------------------------
Stream<T>	      | filter(Predicate<? super T> predicate)
                  | - Trả về một Stream bao gồm các elements của stream này khớp với predicate đã cho.
                  | - Predicate<T> là một functional interface có một "boolean test(T)" method.
------------------|--------------------------------------------------------------------------------
Stream<T>	      | limit(long maxSize)
                  | - Trả về một Stream bao gồm các elements của stream này, được cắt bớt để có
                  |   độ dài không quá maxSize.
------------------|--------------------------------------------------------------------------------
Stream<T>	      | skip(long n)
                  | - Trả về một Stream bao gồm các elements còn lại của stream này sau khi loại bỏ
                  |   n elements đầu tiên của stream.
                  | - Nếu stream này chứa ít hơn n elements thì một stream trống sẽ được trả về.
------------------|--------------------------------------------------------------------------------
<R> Stream<R>	  | flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)
IntStream	      | flatMapToInt(Function<? super T, ? extends IntStream> mapper)
LongStream	      | flatMapToLong(Function<? super T, ? extends LongStream> mapper)
DoubleStream	  | flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper)
                  |
                  | - Trả về một Stream / IntStream / LongStream / DoubleStream bao gồm các kết quả
                  |   của việc thay thế từng element của stream này bằng contents của mapped stream
                  |   được tạo ra bằng cách áp dụng mapper đã cung cấp cho từng element.
                  | - Mapped stream sẽ bị đóng sau khi contents của nó đã được thay thế vào stream. 
                  | - Nếu mapped stream là null, thì một empty stream sẽ được sử dụng.
                  | - Function<T,R> là một functional interface có "R apply(T)" method.
                  | 
                  | IntStream ageStream = people.stream().flatMapToInt(x -> IntStream.of(x.age));
------------------|--------------------------------------------------------------------------------
<R> Stream<R>	  | map(Function<? super T, ? extends R> mapper)
IntStream	      | mapToInt(ToIntFunction<? super T> mapper)
LongStream	      | mapToLong(ToLongFunction<? super T> mapper)
DoubleStream	  | mapToDouble(ToDoubleFunction<? super T> mapper)
                  |
                  | - Trả về một Stream / IntStream / LongStream / DoubleStream bao gồm các kết quả
                  |   của việc áp dụng mapper được chỉ định cho các elements của stream này.
                  | - Function<T,R> là một functional interface có "R apply(T)" method.
                  | - ToIntFunction<T> là một functional interface có method "int applyAsInt(T)".
                  | - ToLongFunction<T> là một functional interface có method "long applyAsLong(T)".
                  | - ToDoubleFunction<T> là functional interface có method "double applyAsDouble(T)".
------------------|--------------------------------------------------------------------------------
Stream<T>	      | peek(Consumer<? super T> action)
                  | - Trả về một Stream bao gồm các elements của stream này, thực hiện thêm action
                  |   được cung cấp trên mỗi element khi chúng được sử dụng từ returned stream.
                  | - Consumer<T> là một functional interface có một "void accept(T)" method.
------------------|--------------------------------------------------------------------------------
Stream<T>	      | sorted()
                  | - Trả về một Stream bao gồm các elements của stream này, được sắp xếp theo thứ tự
                  |   tự nhiên.
                  | - Nếu các elements của stream này không phải là Comparable, thì một 
                  |   java.lang.ClassCastException có thể được ném ra khi terminal operation được
                  |   thực thi.
                  | - Đối với các ordered streams, việc sắp xếp ổn định. Đối với các unordered streams,
                  |   không có đảm bảo ổn định nào được thực hiện.
------------------|--------------------------------------------------------------------------------
Stream<T>	      | sorted(Comparator<? super T> comparator)
                  | - Trả về một Stream bao gồm các elements của stream này, được sắp xếp theo
                  |   Comparator được cung cấp.
                  | - Đối với các ordered streams, việc sắp xếp ổn định. Đối với các unordered streams,
                  |   không có đảm bảo ổn định nào được thực hiện.
                  | - Comparator<T> là một functional interface với method "int compare(T, T)".
```

#### *2.2.2, The Terminal Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
void	          | forEach(Consumer<? super T> action)
void	          | forEachOrdered(Consumer<? super T> action)
                  |
                  | - Thực hiện một hành động cho từng element của stream này.
                  | - forEachOrdered thực hiện action theo thứ tự nếu stream có một thứ tự xác định.
                  | - Consumer<T> là một functional interface có một "void accept(T)" method.
------------------|--------------------------------------------------------------------------------
Object[]	      | toArray()
                  | - Trả về một mảng chứa các elements của stream này.
------------------|--------------------------------------------------------------------------------
<A> A[]           | toArray(IntFunction<A[]> generator)
                  | - Trả về một mảng chứa các elements của stream này, sử dụng generator function
                  |   được cung cấp để phân bổ returned array.
                  | - IntFunction<R> là một functional interface có functional method "R apply(int)".
------------------|--------------------------------------------------------------------------------
long	          | count()
                  | - Trả về số lượng elements trong stream này.
------------------|--------------------------------------------------------------------------------
boolean	          | allMatch(Predicate<? super T> predicate)
                  | - Trả về true nếu tất cả các elements của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - Predicate<T> là một functional interface có functional method "boolean test(T)".
------------------|--------------------------------------------------------------------------------
boolean	          | anyMatch(Predicate<? super T> predicate)
                  | - Trả về true nếu bất kỳ elements nào của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "false" và predicate không được đánh giá.
                  | - Predicate<T> là một functional interface có functional method "boolean test(T)".
------------------|--------------------------------------------------------------------------------
boolean	          | noneMatch(Predicate<? super T> predicate)
                  | - Trả về true nếu KHÔNG có bất kỳ elements nào của stream này có khớp với
                  |   predicate được cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - Predicate<T> là một functional interface có functional method "boolean test(T)".
------------------|--------------------------------------------------------------------------------
Optional<T>	      | findFirst()
                  | - Trả về một Optional mô tả element đầu tiên của stream này, hoặc empty Optional
                  |   nếu stream là empty.
                  | - Nếu stream không có thứ tự, thì bất kỳ element nào cũng có thể được trả về.
                  | - Ném NullPointerException nếu element được chọn là null.
------------------|--------------------------------------------------------------------------------
Optional<T>	      | findAny()
                  | - Trả về một Optional mô tả một số elements của stream này, hoặc empty Optional
                  |   nếu stream là empty.
                  | - Ném NullPointerException nếu element được chọn là null.
------------------|--------------------------------------------------------------------------------
Optional<T>	      | max(Comparator<? super T> comparator)
Optional<T>	      | min(Comparator<? super T> comparator)
                  |
                  | - Trả về một Optional mô tả element lớn nhất / nhỏ nhất của stream này theo
                  |   comparator được cung cấp.
                  | - Ném NullPointerException nếu element lớn nhất là null.
                  | - Comparator<T> là một functional interface với method "int compare(T, T)".
------------------|--------------------------------------------------------------------------------
Optional<T>	      | reduce(BinaryOperator<T> accumulator)
                  | - Trả về một Optional mô tả kết quả của việc thực thi reduction trên các elements
                  |   của stream này, nếu có.
                  | - Ném NullPointerException nếu result là null.
                  | - BinaryOperator<T> là một functional interface mở rộng BiFunction<T,T,T> có
                  |   functional method "T apply(T,T)".
                  |   1st parameter là kết quả thực thi accumulator.apply(T,T) của lần lặp trước,
                  |   2nd parameter là element của lần lặp hiện tại.
                  |
                  |     boolean foundAny = false;
                  |     T result = null;
                  |     for (T element : this_stream) {
                  |         if (!foundAny) {
                  |             foundAny = true;
                  |             result = element;
                  |         }
                  |         else
                  |             result = accumulator.apply(result, element);
                  |     }
                  |     return foundAny ? Optional.of(result) : Optional.empty();
------------------|--------------------------------------------------------------------------------
T	              | reduce(T identity, BinaryOperator<T> accumulator)
                  | - Trả về kết quả của việc thực thi reduction trên các elements của stream này.
                  | - BinaryOperator<T> là một functional interface mở rộng BiFunction<T,T,T> có
                  |   functional method "T apply(T,T)".
                  |   1st parameter là kết quả thực thi accumulator.apply(T,T) của lần lặp trước,
                  |   2nd parameter là element của lần lặp hiện tại.
                  |
                  |      T result = identity;
                  |      for (T element : this_stream)
                  |          result = accumulator.apply(result, element)
                  |      return result;
------------------|--------------------------------------------------------------------------------
<U> U	          | reduce(
                  |     U identity,
                  |     BiFunction<U,? super T,U> accumulator, 
                  |     BinaryOperator<U> combiner
                  | )
                  | - Trả về kết quả của việc thực thi reduction trên các elements của stream này.
                  |   Cho phép chuyển đổi các elements trước khi thực hiện reduction.
                  | - BiFunction<T,U,R> là một functional interface có method "R apply(T,U)".
                  |   1st parameter là kết quả thực thi accumulator.apply(T,U) của lần lặp trước,
                  |   2nd parameter là element của lần lặp hiện tại.
                  | - BinaryOperator<T> là một functional interface mở rộng BiFunction<T,T,T> có
                  |   functional method "T apply(T,T)".
                  |
                  |      U result = identity;
                  |      for (T element : this_stream)
                  |          result = accumulator.apply(result, element)
                  |      return result;
------------------|--------------------------------------------------------------------------------
<R> R	          | collect(
                  |     Supplier<R> supplier, 
                  |     BiConsumer<R, ? super T> accumulator, 
                  |     BiConsumer<R,R> combiner
                  | )
                  | - Thực hiện một mutable reduction operation trên các elements của stream này.
                  | - Kết quả được tạo ra là một mutable result container, như ArrayList, và các
                  |   elements được hợp lại bằng cách cập nhật state của kết quả chứ không phải bằng
                  |   cách thay thế kết quả.
                  | - Supplier<T> là một functional interface với functional method "T get()",
                  |   + supplier là một function tạo ra một result container object.
                  | - BiConsumer<T,U> là một functional interface với method "void accept(T, U)",
                  |   + accumulator là function giúp cập nhật result: 
                  |     1st param là container, 2nd param là element cần thêm vào container.
                  |   + combiner là function cho việc kết hợp (combine) 2 giá trị, sử dụng cho các 
                  |     parallel streams: 2 param đều là các containers.
                  |
                  |   R result = supplier.get();
                  |   for (T element : this stream)
                  |       accumulator.accept(result, element);
                  |   return result;
                  |   
                  |  VD: List<String> asList = 
                  |         stringStream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                  |
                  |      intList.stream().collect(
                  |         ArrayList::new,
                  |         (x, y) -> { if(y.age > 12) x.add(y); },
                  |         (x, y) -> x.addAll(y)
                  |      );
------------------|--------------------------------------------------------------------------------
<R,A> R	          | collect(Collector<? super T,A,R> collector)
                  | - Thực hiện một mutable reduction operation trên các elements của stream này
                  |   bằng cách sử dụng một Collector.
                  | - Một Collector đóng gói các functions được sử dụng làm arguments cho 
                  |   collect(Supplier, BiConsumer, BiConsumer), cho phép sử dụng lại các collection
                  |   strategies và thành phần của các collect operations, chẳng hạn như multi-level
                  |   grouping hoặc partitioning (phân vùng).
                  | - Xem Collectors class hỗ trợ các predefined collectors - là các implementation
                  |   của Collector interface, triển khai nhiều reduction operations hữu ích.
```


### 2.3, The Inherited Methods

Các methods được thừa kế từ *java.util.stream.BaseStream* interface bao gồm:  

- close(), 
- isParallel(),  
- onClose(),  
- parallel(),  
- sequential(),  
- unordered(),  
- iterator(),  
- spliterator()  


## 3. The Optional Class

Một Optional instance là một container object có thể chứa hoặc không thể chứa một non-null value.

```java
public final class Optional<T> extends Object {}
```

Các methods mà Optional class hỗ trợ bao gồm:

```
Modifier and Type       |                        Method and Description
------------------------|---------------------------------------------------------------------------
static <T> Optional<T>  | empty() 
                        | - Trả về một empty Optional instance. Không có value nào cho Optional này.
------------------------|---------------------------------------------------------------------------
static <T> Optional<T>  | of(T value)
                        | - Trả về một Optional instance với non-null value đã chỉ định.
                        | - Ném NullPointerException nếu value là null.
------------------------|---------------------------------------------------------------------------
static <T> Optional<T>  | ofNullable(T value)
                        | - Trả về một Optional instance mô tả value được chỉ định, nếu là non-null. 
                        | - Nếu không, trả về một empty Optional.
------------------------|---------------------------------------------------------------------------
boolean	                | isPresent()
                        | - Trả về true nếu có value, ngược lại trả về false.
------------------------|---------------------------------------------------------------------------
void	                | ifPresent(Consumer<? super T> consumer)
                        | - Nếu có value, thì gọi consumer được chỉ định với value đó, 
                        | - Nếu không thì không làm gì cả.
                        | - Ném NullPointerException nếu không có value và consumer là null.
                        | - Consumer<T> là một functional interface có một "void accept(T)" method.
------------------------|---------------------------------------------------------------------------
Optional<T>	            | filter(Predicate<? super T> predicate)
                        | - Nếu có một value khớp với predicate đã cho, thì trả về một Optional mô tả
                        |   value.
                        | - Nếu không, trả về một empty Optional.
                        | - Ném NullPointerException nếu predicate là null.
                        | - Predicate<T> là một functional interface có method "boolean test(T)".
------------------------|---------------------------------------------------------------------------
<U> Optional<U>	        | flatMap(Function<? super T, Optional<U>> mapper)
                        | - Nếu có một value, hãy áp dụng mapping function đã cung cấp cho nó, nếu
                        |   result là non-null thì trả về một Optional mô tả result đó, nếu không thì
                        |   trả về một empty Optional.
                        | - Ném NullPointerException nếu mapping function là null hoặc trả về null.
                        | - Function<T,R> là một functional interface có "R apply(T)" method.
------------------------|---------------------------------------------------------------------------
<U> Optional<U>	        | map(Function<? super T, ? extends U> mapper)
                        | - Nếu có một value, hãy áp dụng mapping function đã cung cấp cho nó, nếu
                        |   result là non-null thì trả về một Optional mô tả result đó, nếu không thì
                        |   trả về một empty Optional.
                        | - Ném NullPointerException nếu mapping function là null.
                        | - Function<T,R> là một functional interface có "R apply(T)" method.
------------------------|---------------------------------------------------------------------------
T	                    | get()
                        | - Nếu có một value trong Optional này, thì trả về value, 
                        | - Nếu không thì ném NoSuchElementException.
------------------------|---------------------------------------------------------------------------
T	                    | orElse(T other)
                        | - Nếu có một value trong Optional này, thì trả về value, 
                        | - Nếu không thì trả về other được chỉ định (có thể là null).
------------------------|---------------------------------------------------------------------------
T	                    | orElseGet(Supplier<? extends T> other)
                        | - Nếu có một value trong Optional này, thì trả về value, 
                        | - Nếu không thì gọi other được chỉ định và trả về kết quả.
                        | - Ném NullPointerException nếu không có value và other là null.
                        | - Supplier<T> là một functional interface với functional method "T get()".
------------------------|---------------------------------------------------------------------------
<X extends Throwable> T | orElseThrow(Supplier<? extends X> exceptionSupplier) 
                        |    throws X extends Throwable
                        | - Nếu có một value trong Optional này, thì trả về value, 
                        | - Nếu không thì ném một ngoại lệ do exceptionSupplier được chỉ định tạo ra.
                        | - Ném NullPointerException nếu không có value và exceptionSupplier là null.
                        | - Supplier<T> là một functional interface với functional method "T get()".
------------------------|---------------------------------------------------------------------------
boolean	                | equals(Object obj)
                        | - So sánh bằng object được chỉ định với Optional này.
                        | - Trả về true nếu object được chỉ định cũng là một Optional instance, và:
                        |   2 instances đều không có value, hoặc 2 value bằng nhau qua equals().
------------------------|---------------------------------------------------------------------------
int	                    | hashCode()
                        | - Trả về hash code của value hiện tại (nếu có), hoặc 0 nếu không có value.
------------------------|---------------------------------------------------------------------------
String	                | toString()
                        | - Trả về non-empty string biểu diễn của Optional này phù hợp cho debug.
```