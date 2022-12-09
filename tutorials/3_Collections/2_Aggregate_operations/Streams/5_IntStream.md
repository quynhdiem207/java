# The IntStream Interface 

Một *IntStream* là một sequence (chuỗi) các primitive int-valued elements hỗ trợ các sequential và parallel aggregate operations.

```java
public interface IntStream extends BaseStream<Integer, IntStream> {}
```

Xem thêm thông tin chung về các [streams](./2_Stream.md).


## 1, static nested interface IntStream.Builder

*IntStream.Builder* là một *mutable builder* cho một IntStream. Nó cho phép tạo một *IntStream* bằng cách tạo các elements riêng lẻ và thêm chúng vào *Builder* (mà không cần sử dụng một ArrayList làm buffer tạm thời).

```java
public static interface IntStream.Builder extends IntConsumer {}
```

Một stream builder có một life cycle:  

- Bắt đầu trong một building phase (pha xây dựng), trong đó các elements có thể được thêm vào builder,  
- Sau đó chuyển sang một built phase (pha đã xây dựng), lúc này không thể thêm các elements vào builder nếu không một IllegalStateException sẽ được ném ra, bắt đầu khi build() method được gọi, method này tạo một ordered IntStream có các elements theo thứ tự đã được thêm vào IntStream Builder.  

Interface này bao gồm các methods sau:

```
Modifier and Type         |                        Method and Description
--------------------------|------------------------------------------------------------------------
void	                  | accept(int t)
                          | - Thêm một element vào stream đang được tạo.
--------------------------|------------------------------------------------------------------------
default IntStream.Builder |	add(int t)
                          | - Thêm một element vào stream đang được tạo.
--------------------------|------------------------------------------------------------------------
IntStream	              | build()
                          | - Tạo stream, chuyển builder này sang built state. 
                          | - Một IllegalStateException sẽ được ném ra nếu cố thêm elements vào
                          |   builder sau khi nó đã chuyển sang built state.
```


## 2, IntStream Operations

### 2.1, Static methods

```
Modifier and Type        |                        Method and Description
-------------------------|-------------------------------------------------------------------------
static IntStream.Builder | builder()
                         | - Trả về một IntStream builder.
-------------------------|-------------------------------------------------------------------------
static IntStream	     | empty()
                         | - Trả về một empty sequential IntStream.
-------------------------|-------------------------------------------------------------------------
static IntStream	     | concat(IntStream a, IntStream b)
                         | - Tạo một IntStream được nối từ 2 arguments là các IntStream, có các 
                         |   elements là tất cả các elements của 1st stream, theo sau là tất cả
                         |   các elements của 2nd stream.
                         | - Returned IntStream được sắp xếp theo thứ tự nếu cả 2 input stream được
                         |   sắp xếp theo thứ tự, song song nếu 1 trong 2 input stream song song.
                         | - Khi returned stream bị đóng, các close handler cho cả 2 input stream
                         |   sẽ được gọi.
-------------------------|-------------------------------------------------------------------------
static IntStream	     | generate(IntSupplier s)
                         | - Trả về một "infinite sequential unordered IntStream", trong đó mỗi
                         |   element được tạo bởi IntSupplier đã cung cấp.
                         | - Phù hợp để tạo các constant streams, stream của các random elements,
                         |   ...
                         | - IntSupplier là một functional interface có method là "int getAsInt()".
-------------------------|-------------------------------------------------------------------------
static IntStream	     | iterate(int seed, IntUnaryOperator f)
                         | - Trả về một "infinite sequential ordered IntStream", được tạo ra bằng
                         |   cách áp dụng lặp đi lặp lại function f cho element seed ban đầu.
                         | - Stream được tạo ra bao gồm các elements: seed, f(seed), f(f(seed)), ...
                         | - IntUnaryOperator là một functional interface với functional method
                         |   "int applyAsInt(int)".
-------------------------|-------------------------------------------------------------------------
static IntStream	     | of(int... values)
                         | - Trả về một "sequential ordered IntStream" có các elements là các values
                         |   được chỉ định.
-------------------------|-------------------------------------------------------------------------
static IntStream	     | of(int t)
                         | - Trả về một sequential IntStream có chứa một element duy nhất được chỉ
                         |   định.
-------------------------|-------------------------------------------------------------------------
static IntStream	     | range(int startInclusive, int endExclusive)
                         | - Trả về một sequential ordered IntStream từ startInclusive (bao gồm) đến 
                         |   endExclusive (không gồm) theo bước tăng dần là 1.
-------------------------|-------------------------------------------------------------------------
static IntStream	     | rangeClosed(int startInclusive, int endInclusive)
                         | - Trả về một sequential ordered IntStream từ startInclusive (bao gồm) đến 
                         |   endExclusive (bao gồm) theo bước tăng dần là 1.
```


### 2.2, Non-static Methods

#### *2.2.1, The Intermediate Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
LongStream	      | asLongStream()
DoubleStream	  | asDoubleStream()
Stream<Integer>	  | boxed()
                  |
                  | - Trả về một LongStream / DoubleStream / Stream bao gồm các elements của stream
                  |   này mà được converted thành long / double, hay được boxing thành Integer.
------------------|--------------------------------------------------------------------------------
IntStream	      | distinct()
                  | - Trả về một IntStream bao gồm các elements khác biệt của stream này.
------------------|--------------------------------------------------------------------------------
IntStream	      | filter(IntPredicate predicate)
                  | - Trả về một IntStream bao gồm các elements của stream này khớp với predicate
                  |   được chỉ định.
                  | - IntPredicate là một functional interface có một "boolean test(int)" method.
------------------|--------------------------------------------------------------------------------
IntStream	      | limit(long maxSize)
                  | - Trả về một IntStream bao gồm các elements của stream này, được cắt bớt để có
                  |   độ dài không quá maxSize.
------------------|--------------------------------------------------------------------------------
IntStream	      | skip(long n)
                  | - Trả về một IntStream bao gồm các elements còn lại của stream này sau khi loại
                  |   bỏ n elements đầu tiên của stream này.
                  | - Nếu stream này chứa ít hơn n elements thì một IntStream trống sẽ được trả về.
------------------|--------------------------------------------------------------------------------
IntStream	      | flatMap(IntFunction<? extends IntStream> mapper)
                  | - Trả về một IntStream bao gồm các kết quả của việc thay thế từng element của
                  |   stream này bằng contents của mapped stream được tạo ra bằng cách áp dụng mapper
                  |   đã cung cấp cho từng element.
                  | - Mapped stream sẽ bị đóng sau khi contents của nó đã được thay thế vào stream. 
                  | - Nếu mapped stream là null, thì một empty stream sẽ được sử dụng.
                  | - IntFunction<R> là một functional interface có "R apply(int)" method.
------------------|--------------------------------------------------------------------------------
IntStream	      | map(IntUnaryOperator mapper)
LongStream	      | mapToLong(IntToLongFunction mapper)
DoubleStream	  | mapToDouble(IntToDoubleFunction mapper)
<U> Stream<U>	  | mapToObj(IntFunction<? extends U> mapper)
                  |
                  | - Trả về một IntStream / LongStream / DoubleStream / Stream bao gồm các kết quả
                  |   của việc áp dụng mapper được chỉ định cho các elements của stream này.
                  | - IntUnaryOperator là một functional interface với method "int applyAsInt(int)".
                  | - IntToLongFunction là một functional interface có method "long applyAsLong(int)".
                  | - IntToDoubleFunction là functional interface có method "double applyAsDouble(int)".
                  | - IntFunction<R> là một functional interface có "R apply(int)" method.
------------------|--------------------------------------------------------------------------------
IntStream	      | peek(IntConsumer action)
                  | - Trả về một IntStream bao gồm các elements của stream này, thực hiện thêm action
                  |   được cung cấp trên mỗi element khi chúng được sử dụng từ returned stream.
                  | - IntConsumer là một functional interface có một "void accept(int)" method.
------------------|--------------------------------------------------------------------------------
IntStream	      | sorted()
                  | - Trả về một IntStream bao gồm các elements của stream này, được sắp xếp theo
                  |   thứ tự tự nhiên.
------------------|--------------------------------------------------------------------------------
IntStream	      | parallel()
                  | - Trả về một parallel IntStream tương đương, có thể là chính nó, vì stream đã
                  |   song song, hoặc vì trạng thái stream đã được sửa đổi thành song song.
------------------|--------------------------------------------------------------------------------
IntStream	      | sequential()
                  | - Trả về một sequential IntStream tương đương, có thể là chính nó, vì stream đã
                  |   tuần tự, hoặc vì trạng thái stream đã được sửa đổi thành tuần tự.
```


#### *2.2.2, The Terminal Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
void	          | forEach(IntConsumer action)
void	          | forEachOrdered(IntConsumer action)
                  |
                  | - Thực hiện một hành động cho từng element của stream này.
                  | - forEachOrdered thực hiện action theo thứ tự nếu stream có một thứ tự xác định.
                  | - IntConsumer là một functional interface có một "void accept(int)" method.
------------------|--------------------------------------------------------------------------------
int[]	          | toArray()
                  | - Trả về một mảng chứa các elements của stream này.
------------------|--------------------------------------------------------------------------------
long	          | count()
                  | - Trả về số lượng elements trong stream này.
------------------|--------------------------------------------------------------------------------
int	              | sum()
                  | - Trả về tổng của các elements trong stream này.
------------------|--------------------------------------------------------------------------------
OptionalDouble	  | average()
                  | - Trả về một OptionalDouble mô tả giá trị trung bình của các elements của stream
                  |   này, hoặc một empty OptionalDouble nếu stream là empty.
------------------|--------------------------------------------------------------------------------
OptionalInt	      | max()
OptionalInt	      | min()
                  |
                  | - Trả về một OptionalInt mô tả element lớn nhất / nhỏ nhất của stream này, hoặc
                  |   một empty OptionalInt nếu stream này là empty.
------------------|--------------------------------------------------------------------------------
IntSummaryStatistics |	summaryStatistics()
                     | - Trả về một IntSummaryStatistics mô tả summary data về các elements của
                     |   stream này, hỗ trợ thực hiện thống kê như đếm, tính tổng, tính trung bình,
                     |   tìm max, tìm min.
------------------|--------------------------------------------------------------------------------
boolean	          | allMatch(IntPredicate predicate)
                  | - Trả về true nếu tất cả các elements của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - IntPredicate là một functional interface có method "boolean test(int)".
------------------|--------------------------------------------------------------------------------
boolean	          | anyMatch(IntPredicate predicate)
                  | - Trả về true nếu bất kỳ elements nào của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "false" và predicate không được đánh giá.
                  | - IntPredicate là một functional interface có method "boolean test(int)".
------------------|--------------------------------------------------------------------------------
boolean	          | noneMatch(IntPredicate predicate)
                  | - Trả về true nếu KHÔNG có bất kỳ elements nào của stream này có khớp với
                  |   predicate được cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - IntPredicate là một functional interface có method "boolean test(int)".
------------------|--------------------------------------------------------------------------------
OptionalInt	      | findFirst()
                  | - Trả về một OptionalInt mô tả element đầu tiên của stream này, hoặc một empty
                  |   OptionalInt nếu stream là empty.
                  | - Nếu stream không có thứ tự, thì bất kỳ element nào cũng có thể được trả về.
------------------|--------------------------------------------------------------------------------
OptionalInt	      | findAny()
                  | - Trả về một OptionalInt mô tả một số elements của stream này, hoặc một empty
                  |   OptionalInt nếu stream là empty.
------------------|--------------------------------------------------------------------------------
OptionalInt       | reduce(IntBinaryOperator op)
                  | - Trả về một OptionalInt mô tả kết quả của việc thực thi reduction trên các
                  |   elements của stream này.
                  | - IntBinaryOperator là một functional interface có method
                  |   "int applyAsInt(int, int)".
                  |   1st parameter là kết quả thực thi op.applyAsInt(int,int) của lần lặp trước,
                  |   2nd parameter là element của lần lặp hiện tại.
                  |
                  |     boolean foundAny = false;
                  |     int result;
                  |     for (int element : this_stream) {
                  |         if (!foundAny) {
                  |             foundAny = true;
                  |             result = element;
                  |         }
                  |         else
                  |             result = op.applyAsInt(result, element);
                  |     }
                  |     return foundAny ? OptionalInt.of(result) : OptionalInt.empty();
------------------|--------------------------------------------------------------------------------
int	              | reduce(int identity, IntBinaryOperator op)
                  | - Trả về một int value là kết quả của việc thực thi reduction trên các elements
                  |   của stream này.
                  | - IntBinaryOperator là một functional interface có method
                  |   "int applyAsInt(int,int)".
                  |   1st parameter là kết quả thực thi op.applyAsInt(int,int) của lần lặp trước,
                  |   2nd parameter là element của lần lặp hiện tại.
                  |
                  |      int result = identity;
                  |      for (int element : this_stream)
                  |          result = op.applyAsInt(result, element)
                  |      return result;
------------------|--------------------------------------------------------------------------------
<R> R	          | collect(
                  |     Supplier<R> supplier, 
                  |     ObjIntConsumer<R> accumulator, 
                  |     BiConsumer<R,R> combiner
                  | )
                  | - Thực hiện một mutable reduction operation trên các elements của stream này.
                  | - Kết quả được tạo ra là một mutable result container, như ArrayList, và các
                  |   elements được hợp lại bằng cách cập nhật state của kết quả chứ không phải bằng
                  |   cách thay thế kết quả.
                  | - Supplier<T> là một functional interface với functional method "T get()",
                  |   + supplier là một function tạo ra một result container object.
                  | - ObjIntConsumer<T> là một functional interface với method "void accept(T, int)".
                  |   + accumulator là function giúp cập nhật result: 
                  |     1st param là container, 2nd param là element cần thêm vào container.
                  | - BiConsumer<T,U> là một functional interface với method "void accept(T, U)",
                  |   + combiner là function cho việc kết hợp (combine) 2 giá trị, sử dụng cho các 
                  |     parallel streams: 2 param đều là các containers.
                  |
                  |   R result = supplier.get();
                  |   for (int element : this stream)
                  |       accumulator.accept(result, element);
                  |   return result;
------------------|--------------------------------------------------------------------------------
PrimitiveIterator.OfInt | iterator()
                        | - Trả về một iterator cho các elements của stream này.
------------------------|--------------------------------------------------------------------------
Spliterator.OfInt	    | spliterator()
                        | - Trả về một spliterator cho các elements của stream này.
```

**PrimitiveIterator.OfInt** interface hỗ trợ các methods sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default void	  | forEachRemaining(Consumer<? super Integer> action)
default void	  | forEachRemaining(IntConsumer action)
                  |
                  | - Thực hiện action được chỉ định cho từng element còn lại cho đến khi tất cả các
                  |   elements đã được xử lý hoặc action ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface có một "void accept(T)" method.
                  | - IntConsumer là một functional interface có một "void accept(int)" method.
------------------|--------------------------------------------------------------------------------
default Integer	  | next()
                  | - Trả về element tiếp theo trong vòng lặp, result được thực hiện boxing.
------------------|--------------------------------------------------------------------------------
int	              | nextInt()
                  | - Trả về int element tiếp theo trong vòng lặp.
                  | - Ném NoSuchElementException nếu iteration không có element tiếp theo.
```


### 2.3, The Inherited Methods

Các methods được thừa kế từ *java.util.stream.BaseStream* interface bao gồm:  

- close(), 
- isParallel(),  
- onClose(),    
- unordered()  


## 3. The OptionalInt Class

Một OptionalInt instance là một container object có thể chứa hoặc không thể chứa một int value.

```java
public final class OptionalInt extends Object {}
```

Các methods mà OptionalInt class hỗ trợ bao gồm:

```
Modifier and Type       |                        Method and Description
------------------------|---------------------------------------------------------------------------
static OptionalInt      | empty() 
                        | - Trả về một empty OptionalInt instance. Không có value nào cho OptionalInt
                        |   này.
------------------------|---------------------------------------------------------------------------
static OptionalInt      | of(int value)
                        | - Trả về một OptionalInt instance với value đã chỉ định.
------------------------|---------------------------------------------------------------------------
boolean	                | isPresent()
                        | - Trả về true nếu có value, ngược lại trả về false.
------------------------|---------------------------------------------------------------------------
void	                | ifPresent(IntConsumer consumer)
                        | - Nếu có value, thì gọi consumer được chỉ định với value đó, 
                        | - Nếu không thì không làm gì cả.
                        | - Ném NullPointerException nếu không có value và consumer là null.
                        | - IntConsumer là một functional interface có một "void accept(int)" method.
------------------------|---------------------------------------------------------------------------
int	                    | getAsInt()
                        | - Nếu có một value trong OptionalInt này, thì trả về value, 
                        | - Nếu không thì ném NoSuchElementException.
------------------------|---------------------------------------------------------------------------
int	                    | orElse(int other)
                        | - Nếu có một value trong OptionalInt này, thì trả về value, 
                        | - Nếu không thì trả về other được chỉ định.
------------------------|---------------------------------------------------------------------------
int	                    | orElseGet(IntSupplier other)
                        | - Nếu có một value trong OptionalInt này, thì trả về value, 
                        | - Nếu không thì gọi other được chỉ định và trả về kết quả.
                        | - Ném NullPointerException nếu không có value và other là null.
                        | - IntSupplier là một functional interface với method "int getAsInt()".
------------------------|---------------------------------------------------------------------------
<X extends Throwable>   |
    int                 | orElseThrow(Supplier<X> exceptionSupplier) 
                        |    throws X extends Throwable
                        | - Nếu có một value trong OptionalInt này, thì trả về value, 
                        | - Nếu không thì ném một ngoại lệ do exceptionSupplier được chỉ định tạo ra.
                        | - Ném NullPointerException nếu không có value và exceptionSupplier là null.
                        | - Supplier<T> là một functional interface với functional method "T get()".
------------------------|---------------------------------------------------------------------------
boolean	                | equals(Object obj)
                        | - So sánh bằng object được chỉ định với OptionalInt này.
                        | - Trả về true nếu object được chỉ định cũng là một OptionalInt instance, và:
                        |   2 instances đều không có value, hoặc 2 value bằng nhau qua ==.
------------------------|---------------------------------------------------------------------------
int	                    | hashCode()
                        | - Trả về hash code của value hiện tại (nếu có), hoặc 0 nếu không có value.
------------------------|---------------------------------------------------------------------------
String	                | toString()
                        | - Trả về non-empty string biểu diễn của Optional này phù hợp cho debug.
```