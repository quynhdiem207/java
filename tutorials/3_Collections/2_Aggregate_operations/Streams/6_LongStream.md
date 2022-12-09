# The LongStream Interface 

Một *LongStream* là một sequence (chuỗi) các primitive long-valued elements hỗ trợ các sequential và parallel aggregate operations.

```java
public interface LongStream extends BaseStream<Long, LongStream> {}
```

Xem thêm thông tin chung về các [streams](./2_Stream.md).


## 1, static nested interface LongStream.Builder

*LongStream.Builder* là một *mutable builder* cho một LongStream. Nó cho phép tạo một *LongStream* bằng cách tạo các elements riêng lẻ và thêm chúng vào *Builder* (mà không cần sử dụng một ArrayList làm buffer tạm thời).

```java
public static interface LongStream.Builder extends LongConsumer {}
```

Một stream builder có một life cycle:  

- Bắt đầu trong một building phase (pha xây dựng), trong đó các elements có thể được thêm vào builder,  
- Sau đó chuyển sang một built phase (pha đã xây dựng), lúc này không thể thêm các elements vào builder nếu không một IllegalStateException sẽ được ném ra, bắt đầu khi build() method được gọi, method này tạo một ordered LongStream có các elements theo thứ tự đã được thêm vào LongStream Builder.  

Interface này bao gồm các methods sau:

```
Modifier and Type          |                        Method and Description
---------------------------|-----------------------------------------------------------------------
void	                   | accept(long t)
                           | - Thêm một element vào stream đang được tạo.
---------------------------|-----------------------------------------------------------------------
default LongStream.Builder | add(long t)
                           | - Thêm một element vào stream đang được tạo.
---------------------------|-----------------------------------------------------------------------
LongStream	               | build()
                           | - Tạo stream, chuyển builder này sang built state. 
                           | - Một IllegalStateException sẽ được ném ra nếu cố thêm elements vào
                           |   builder sau khi nó đã chuyển sang built state.
```


## 2, LongStream Operations

### 2.1, Static methods

```
Modifier and Type        |                        Method and Description
-------------------------|-------------------------------------------------------------------------
static LongStream.Builder| builder()
                         | - Trả về một LongStream builder.
-------------------------|-------------------------------------------------------------------------
static LongStream	     | empty()
                         | - Trả về một empty sequential LongStream.
-------------------------|-------------------------------------------------------------------------
static LongStream	     | concat(LongStream a, LongStream b)
                         | - Tạo một LongStream được nối từ 2 arguments là các LongStream, có các 
                         |   elements là tất cả các elements của 1st stream, theo sau là tất cả
                         |   các elements của 2nd stream.
                         | - Returned LongStream được sắp xếp theo thứ tự nếu cả 2 input stream được
                         |   sắp xếp theo thứ tự, song song nếu 1 trong 2 input stream song song.
                         | - Khi returned stream bị đóng, các close handler cho cả 2 input stream
                         |   sẽ được gọi.
-------------------------|-------------------------------------------------------------------------
static LongStream	     | generate(LongSupplier s)
                         | - Trả về một "infinite sequential unordered LongStream", trong đó mỗi
                         |   element được tạo bởi LongSupplier đã cung cấp.
                         | - Phù hợp để tạo các constant streams, stream của các random elements,
                         |   ...
                         | - LongSupplier là một functional interface có method là "long getAsLong()".
-------------------------|-------------------------------------------------------------------------
static LongStream	     | iterate(long seed, LongUnaryOperator f)
                         | - Trả về một "infinite sequential ordered LongStream", được tạo ra bằng
                         |   cách áp dụng lặp đi lặp lại function f cho element seed ban đầu.
                         | - Stream được tạo ra bao gồm các elements: seed, f(seed), f(f(seed)), ...
                         | - LongUnaryOperator là một functional interface với functional method
                         |   "long applyAsLong(long)".
-------------------------|-------------------------------------------------------------------------
static LongStream	     | of(long... values)
                         | - Trả về một "sequential ordered LongStream" có các elements là các values
                         |   được chỉ định.
-------------------------|-------------------------------------------------------------------------
static LongStream	     | of(long t)
                         | - Trả về một sequential LongStream có chứa một element duy nhất được chỉ
                         |   định.
-------------------------|-------------------------------------------------------------------------
static LongStream	     | range(long startInclusive, long endExclusive)
                         | - Trả về một sequential ordered LongStream từ startInclusive (bao gồm) đến 
                         |   endExclusive (không gồm) theo bước tăng dần là 1.
-------------------------|-------------------------------------------------------------------------
static LongStream	     | rangeClosed(long startInclusive, long endInclusive)
                         | - Trả về một sequential ordered LongStream từ startInclusive (bao gồm) đến 
                         |   endExclusive (bao gồm) theo bước tăng dần là 1.
```


### 2.2, Non-static Methods

#### *2.2.1, The Intermediate Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
DoubleStream	  | asDoubleStream()
Stream<Long>	  | boxed()
                  |
                  | - Trả về một DoubleStream / Stream bao gồm các elements của stream này mà được
                  |   converted thành double, hay được boxing thành Long.
------------------|--------------------------------------------------------------------------------
LongStream	      | distinct()
                  | - Trả về một LongStream bao gồm các elements khác biệt của stream này.
------------------|--------------------------------------------------------------------------------
LongStream	      | filter(LongPredicate predicate)
                  | - Trả về một LongStream bao gồm các elements của stream này khớp với predicate
                  |   được chỉ định.
                  | - LongPredicate là một functional interface có một "boolean test(long)" method.
------------------|--------------------------------------------------------------------------------
LongStream	      | limit(long maxSize)
                  | - Trả về một LongStream bao gồm các elements của stream này, được cắt bớt để có
                  |   độ dài không quá maxSize.
------------------|--------------------------------------------------------------------------------
LongStream	      | skip(long n)
                  | - Trả về một LongStream bao gồm các elements còn lại của stream này sau khi loại
                  |   bỏ n elements đầu tiên của stream này.
                  | - Nếu stream này chứa ít hơn n elements thì một LongStream trống sẽ được trả về.
------------------|--------------------------------------------------------------------------------
LongStream	      | flatMap(LongFunction<? extends LongStream> mapper)
                  | - Trả về một LongStream bao gồm các kết quả của việc thay thế từng element của
                  |   stream này bằng contents của mapped stream được tạo ra bằng cách áp dụng mapper
                  |   đã cung cấp cho từng element.
                  | - Mapped stream sẽ bị đóng sau khi contents của nó đã được thay thế vào stream. 
                  | - Nếu mapped stream là null, thì một empty stream sẽ được sử dụng.
                  | - LongFunction<R> là một functional interface có "R apply(long)" method.
------------------|--------------------------------------------------------------------------------
LongStream	      | map(LongUnaryOperator mapper)
IntStream	      | mapToInt(LongToIntFunction mapper)
DoubleStream	  | mapToDouble(LongToDoubleFunction mapper)
<U> Stream<U>	  | mapToObj(LongFunction<? extends U> mapper)
                  |
                  | - Trả về một LongStream / IntStream / DoubleStream / Stream bao gồm các kết quả
                  |   của việc áp dụng mapper được chỉ định cho các elements của stream này.
                  | - LongUnaryOperator là một functional interface có method "long applyAsLong(long)".
                  | - LongToIntFunction là một functional interface có method "int applyAsInt(long)".
                  | - LongToDoubleFunction là functional interface có method "double applyAsDouble(long)".
                  | - LongFunction<R> là một functional interface có "R apply(long)" method.
------------------|--------------------------------------------------------------------------------
LongStream	      | peek(LongConsumer action)
                  | - Trả về một LongStream bao gồm các elements của stream này, thực hiện thêm action
                  |   được cung cấp trên mỗi element khi chúng được sử dụng từ returned stream.
                  | - LongConsumer là một functional interface có một "void accept(long)" method.
------------------|--------------------------------------------------------------------------------
LongStream	      | sorted()
                  | - Trả về một LongStream bao gồm các elements của stream này, được sắp xếp theo
                  |   thứ tự tự nhiên.
------------------|--------------------------------------------------------------------------------
LongStream	      | parallel()
                  | - Trả về một parallel LongStream tương đương, có thể là chính nó, vì stream đã
                  |   song song, hoặc vì trạng thái stream đã được sửa đổi thành song song.
------------------|--------------------------------------------------------------------------------
LongStream	      | sequential()
                  | - Trả về một sequential LongStream tương đương, có thể là chính nó, vì stream đã
                  |   tuần tự, hoặc vì trạng thái stream đã được sửa đổi thành tuần tự.
```


#### *2.2.2, The Terminal Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
void	          | forEach(LongConsumer action)
void	          | forEachOrdered(LongConsumer action)
                  |
                  | - Thực hiện một hành động cho từng element của stream này.
                  | - forEachOrdered thực hiện action theo thứ tự nếu stream có một thứ tự xác định.
                  | - LongConsumer là một functional interface có một "void accept(long)" method.
------------------|--------------------------------------------------------------------------------
long[]	          | toArray()
                  | - Trả về một mảng chứa các elements của stream này.
------------------|--------------------------------------------------------------------------------
long	          | count()
                  | - Trả về số lượng elements trong stream này.
------------------|--------------------------------------------------------------------------------
long	          | sum()
                  | - Trả về tổng của các elements trong stream này.
------------------|--------------------------------------------------------------------------------
OptionalDouble	  | average()
                  | - Trả về một OptionalDouble mô tả giá trị trung bình của các elements của stream
                  |   này, hoặc một empty OptionalDouble nếu stream là empty.
------------------|--------------------------------------------------------------------------------
OptionalLong	  | max()
OptionalLong	  | min()
                  |
                  | - Trả về một OptionalLong mô tả element lớn nhất / nhỏ nhất của stream này, hoặc
                  |   một empty OptionalLong nếu stream này là empty.
------------------|--------------------------------------------------------------------------------
LongSummaryStatistics |	summaryStatistics()
                      | - Trả về một LongSummaryStatistics mô tả summary data về các elements của
                      |   stream này, hỗ trợ thực hiện thống kê như đếm, tính tổng, tính trung bình,
                      |   tìm max, tìm min.
------------------|--------------------------------------------------------------------------------
boolean	          | allMatch(LongPredicate predicate)
                  | - Trả về true nếu tất cả các elements của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - LongPredicate là một functional interface có method "boolean test(long)".
------------------|--------------------------------------------------------------------------------
boolean	          | anyMatch(LongPredicate predicate)
                  | - Trả về true nếu bất kỳ elements nào của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "false" và predicate không được đánh giá.
                  | - LongPredicate là một functional interface có method "boolean test(long)".
------------------|--------------------------------------------------------------------------------
boolean	          | noneMatch(LongPredicate predicate)
                  | - Trả về true nếu KHÔNG có bất kỳ elements nào của stream này có khớp với
                  |   predicate được cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - LongPredicate là một functional interface có method "boolean test(long)".
------------------|--------------------------------------------------------------------------------
OptionalLong	  | findFirst()
                  | - Trả về một OptionalLong mô tả element đầu tiên của stream này, hoặc một empty
                  |   OptionalLong nếu stream là empty.
                  | - Nếu stream không có thứ tự, thì bất kỳ element nào cũng có thể được trả về.
------------------|--------------------------------------------------------------------------------
OptionalLong	  | findAny()
                  | - Trả về một OptionalLong mô tả một số elements của stream này, hoặc một empty
                  |   OptionalLong nếu stream là empty.
------------------|--------------------------------------------------------------------------------
OptionalLong      | reduce(LongBinaryOperator op)
                  | - Trả về một OptionalLong mô tả kết quả của việc thực thi reduction trên các
                  |   elements của stream này.
                  | - LongBinaryOperator là một functional interface có method
                  |   "long applyAsLong(long, long)".
                  |   1st parameter là kết quả thực thi op.applyAsLong(long,long) của lần lặp trước,
                  |   2nd parameter là element của lần lặp hiện tại.
                  |
                  |     boolean foundAny = false;
                  |     long result;
                  |     for (long element : this_stream) {
                  |         if (!foundAny) {
                  |             foundAny = true;
                  |             result = element;
                  |         }
                  |         else
                  |             result = op.applyAsLong(result, element);
                  |     }
                  |     return foundAny ? OptionalLong.of(result) : OptionalLong.empty();
------------------|--------------------------------------------------------------------------------
long	          | reduce(long identity, LongBinaryOperator op)
                  | - Trả về một long value là kết quả của việc thực thi reduction trên các elements
                  |   của stream này.
                  | - LongBinaryOperator là một functional interface có method 
                  |   "long applyAsLong(long,long)".
                  |   1st parameter là kết quả thực thi op.applyAsLong(long,long) của lần lặp trước,
                  |   2nd parameter là element của lần lặp hiện tại.
                  |
                  |      long result = identity;
                  |      for (long element : this_stream)
                  |          result = op.applyAsLong(result, element)
                  |      return result;
------------------|--------------------------------------------------------------------------------
<R> R	          | collect(
                  |     Supplier<R> supplier, 
                  |     ObjLongConsumer<R> accumulator, 
                  |     BiConsumer<R,R> combiner
                  | )
                  | - Thực hiện một mutable reduction operation trên các elements của stream này.
                  | - Kết quả được tạo ra là một mutable result container, như ArrayList, và các
                  |   elements được hợp lại bằng cách cập nhật state của kết quả chứ không phải bằng
                  |   cách thay thế kết quả.
                  | - Supplier<T> là một functional interface với functional method "T get()",
                  |   + supplier là một function tạo ra một result container object.
                  | - ObjLongConsumer<T> là một functional interface có method "void accept(T, long)".
                  |   + accumulator là function giúp cập nhật result: 
                  |     1st param là container, 2nd param là element cần thêm vào container.
                  | - BiConsumer<T,U> là một functional interface với method "void accept(T, U)",
                  |   + combiner là function cho việc kết hợp (combine) 2 giá trị, sử dụng cho các 
                  |     parallel streams: 2 param đều là các containers.
                  |
                  |   R result = supplier.get();
                  |   for (long element : this stream)
                  |       accumulator.accept(result, element);
                  |   return result;
------------------|--------------------------------------------------------------------------------
PrimitiveIterator.OfLong | iterator()
                         | - Trả về một iterator cho các elements của stream này.
-------------------------|-------------------------------------------------------------------------
Spliterator.OfLong	     | spliterator()
                         | - Trả về một spliterator cho các elements của stream này.
```

**PrimitiveIterator.OfLong** interface hỗ trợ các methods sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default void	  | forEachRemaining(Consumer<? super Long> action)
default void	  | forEachRemaining(LongConsumer action)
                  |
                  | - Thực hiện action được chỉ định cho từng element còn lại cho đến khi tất cả các
                  |   elements đã được xử lý hoặc action ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface có một "void accept(T)" method.
                  | - LongConsumer là một functional interface có một "void accept(long)" method.
------------------|--------------------------------------------------------------------------------
default Long	  | next()
                  | - Trả về element tiếp theo trong vòng lặp, result được thực hiện boxing.
------------------|--------------------------------------------------------------------------------
long	          | nextLong()
                  | - Trả về long element tiếp theo trong vòng lặp.
                  | - Ném NoSuchElementException nếu iteration không có element tiếp theo.
```


### 2.3, The Inherited Methods

Các methods được thừa kế từ *java.util.stream.BaseStream* interface bao gồm:  

- close(), 
- isParallel(),  
- onClose(),    
- unordered()  


## 3. The OptionalLong Class

Một OptionalLong instance là một container object có thể chứa hoặc không thể chứa một long value.

```java
public final class OptionalLong extends Object {}
```

Các methods mà OptionalLong class hỗ trợ bao gồm:

```
Modifier and Type       |                        Method and Description
------------------------|---------------------------------------------------------------------------
static OptionalLong     | empty() 
                        | - Trả về một empty OptionalLong instance. Không có value nào cho
                        |   OptionalLong này.
------------------------|---------------------------------------------------------------------------
static OptionalLong     | of(long value)
                        | - Trả về một OptionalLong instance với value đã chỉ định.
------------------------|---------------------------------------------------------------------------
boolean	                | isPresent()
                        | - Trả về true nếu có value, ngược lại trả về false.
------------------------|---------------------------------------------------------------------------
void	                | ifPresent(LongConsumer consumer)
                        | - Nếu có value, thì gọi consumer được chỉ định với value đó, 
                        | - Nếu không thì không làm gì cả.
                        | - Ném NullPointerException nếu không có value và consumer là null.
                        | - LongConsumer là một functional interface có một "void accept(long)" method.
------------------------|---------------------------------------------------------------------------
long	                | getAsLong()
                        | - Nếu có một value trong OptionalLong này, thì trả về value, 
                        | - Nếu không thì ném NoSuchElementException.
------------------------|---------------------------------------------------------------------------
long	                | orElse(long other)
                        | - Nếu có một value trong OptionalLong này, thì trả về value, 
                        | - Nếu không thì trả về other được chỉ định.
------------------------|---------------------------------------------------------------------------
long	                | orElseGet(LongSupplier other)
                        | - Nếu có một value trong OptionalLong này, thì trả về value, 
                        | - Nếu không thì gọi other được chỉ định và trả về kết quả.
                        | - Ném NullPointerException nếu không có value và other là null.
                        | - LongSupplier là một functional interface với method "long getAsLong()".
------------------------|---------------------------------------------------------------------------
<X extends Throwable>   |
    long                | orElseThrow(Supplier<X> exceptionSupplier) 
                        |    throws X extends Throwable
                        | - Nếu có một value trong OptionalLong này, thì trả về value, 
                        | - Nếu không thì ném một ngoại lệ do exceptionSupplier được chỉ định tạo ra.
                        | - Ném NullPointerException nếu không có value và exceptionSupplier là null.
                        | - Supplier<T> là một functional interface với functional method "T get()".
------------------------|---------------------------------------------------------------------------
boolean	                | equals(Object obj)
                        | - So sánh bằng object được chỉ định với OptionalLong này.
                        | - Trả về true nếu object được chỉ định cũng là một OptionalLong instance, và:
                        |   2 instances đều không có value, hoặc 2 value bằng nhau qua ==.
------------------------|---------------------------------------------------------------------------
int	                    | hashCode()
                        | - Trả về hash code của value hiện tại (nếu có), hoặc 0 nếu không có value.
------------------------|---------------------------------------------------------------------------
String	                | toString()
                        | - Trả về non-empty string biểu diễn của Optional này phù hợp cho debug.
```