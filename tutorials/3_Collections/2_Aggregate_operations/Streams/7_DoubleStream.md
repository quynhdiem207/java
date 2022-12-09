# The DoubleStream Interface 

Một *DoubleStream* là một sequence (chuỗi) các primitive double-valued elements hỗ trợ các sequential và parallel aggregate operations.

```java
public interface DoubleStream extends BaseStream<Double, DoubleStream> {}
```

Xem thêm thông tin chung về các [streams](./2_Stream.md).


## 1, static nested interface DoubleStream.Builder

*DoubleStream.Builder* là một *mutable builder* cho một DoubleStream. Nó cho phép tạo một *DoubleStream* bằng cách tạo các elements riêng lẻ và thêm chúng vào *Builder* (mà không cần sử dụng một ArrayList làm buffer tạm thời).

```java
public static interface DoubleStream.Builder extends DoubleConsumer {}
```

Một stream builder có một life cycle:  

- Bắt đầu trong một building phase (pha xây dựng), trong đó các elements có thể được thêm vào builder,  
- Sau đó chuyển sang một built phase (pha đã xây dựng), lúc này không thể thêm các elements vào builder nếu không một IllegalStateException sẽ được ném ra, bắt đầu khi build() method được gọi, method này tạo một ordered DoubleStream có các elements theo thứ tự đã được thêm vào DoubleStream Builder.  

Interface này bao gồm các methods sau:

```
Modifier and Type            |                        Method and Description
-----------------------------|---------------------------------------------------------------------
void	                     | accept(double t)
                             | - Thêm một element vào stream đang được tạo.
-----------------------------|---------------------------------------------------------------------
default DoubleStream.Builder | add(double t)
                             | - Thêm một element vào stream đang được tạo.
-----------------------------|---------------------------------------------------------------------
DoubleStream	             | build()
                             | - Tạo stream, chuyển builder này sang built state. 
                             | - Một IllegalStateException sẽ được ném ra nếu cố thêm elements vào
                             |   builder sau khi nó đã chuyển sang built state.
```


## 2, DoubleStream Operations

### 2.1, Static methods

```
Modifier and Type        |                        Method and Description
-------------------------|-------------------------------------------------------------------------
static                   |
    DoubleStream.Builder | builder()
                         | - Trả về một DoubleStream builder.
-------------------------|-------------------------------------------------------------------------
static DoubleStream	     | empty()
                         | - Trả về một empty sequential DoubleStream.
-------------------------|-------------------------------------------------------------------------
static DoubleStream	     | concat(DoubleStream a, DoubleStream b)
                         | - Tạo một DoubleStream được nối từ 2 arguments là các DoubleStream, có các 
                         |   elements là tất cả các elements của 1st stream, theo sau là tất cả
                         |   các elements của 2nd stream.
                         | - Returned DoubleStream được sắp xếp theo thứ tự nếu cả 2 input stream được
                         |   sắp xếp theo thứ tự, song song nếu 1 trong 2 input stream song song.
                         | - Khi returned stream bị đóng, các close handler cho cả 2 input stream
                         |   sẽ được gọi.
-------------------------|-------------------------------------------------------------------------
static DoubleStream	     | generate(DoubleSupplier s)
                         | - Trả về một "infinite sequential unordered DoubleStream", trong đó mỗi
                         |   element được tạo bởi DoubleSupplier đã cung cấp.
                         | - Phù hợp để tạo các constant streams, stream của các random elements,
                         |   ...
                         | - DoubleSupplier là một functional interface có method
                         |   "double getAsDouble()".
-------------------------|-------------------------------------------------------------------------
static DoubleStream	     | iterate(double seed, DoubleUnaryOperator f)
                         | - Trả về một "infinite sequential ordered DoubleStream", được tạo ra bằng
                         |   cách áp dụng lặp đi lặp lại function f cho element seed ban đầu.
                         | - Stream được tạo ra bao gồm các elements: seed, f(seed), f(f(seed)), ...
                         | - DoubleUnaryOperator là một functional interface với functional method
                         |   "double applyAsDouble(double)".
-------------------------|-------------------------------------------------------------------------
static DoubleStream	     | of(double... values)
                         | - Trả về một "sequential ordered DoubleStream" có các elements là các values
                         |   được chỉ định.
-------------------------|-------------------------------------------------------------------------
static DoubleStream	     | of(double t)
                         | - Trả về một sequential DoubleStream có chứa một element duy nhất được chỉ
                         |   định.
```


### 2.2, Non-static Methods

#### *2.2.1, The Intermediate Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
Stream<Double>	  | boxed()
                  | - Trả về một Stream bao gồm các elements của stream này mà được boxing thành
                  |   Double.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | distinct()
                  | - Trả về một DoubleStream bao gồm các elements khác biệt của stream này.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | filter(DoublePredicate predicate)
                  | - Trả về một DoubleStream bao gồm các elements của stream này khớp với predicate
                  |   được chỉ định.
                  | - DoublePredicate là một functional interface có một "boolean test(double)" method.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | limit(long maxSize)
                  | - Trả về một DoubleStream bao gồm các elements của stream này, được cắt bớt để có
                  |   độ dài không quá maxSize.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | skip(long n)
                  | - Trả về một DoubleStream bao gồm các elements còn lại của stream này sau khi loại
                  |   bỏ n elements đầu tiên của stream này.
                  | - Nếu stream này chứa ít hơn n elements thì một DoubleStream trống sẽ được trả về.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | flatMap(DoubleFunction<? extends DoubleStream> mapper)
                  | - Trả về một DoubleStream bao gồm các kết quả của việc thay thế từng element của
                  |   stream này bằng contents của mapped stream được tạo ra bằng cách áp dụng mapper
                  |   đã cung cấp cho từng element.
                  | - Mapped stream sẽ bị đóng sau khi contents của nó đã được thay thế vào stream. 
                  | - Nếu mapped stream là null, thì một empty stream sẽ được sử dụng.
                  | - DoubleFunction<R> là một functional interface có "R apply(double)" method.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | map(DoubleUnaryOperator mapper)
IntStream	      | mapToInt(DoubleToIntFunction mapper)
LongStream	      | mapToDouble(DoubleToLongFunction mapper)
<U> Stream<U>	  | mapToObj(DoubleFunction<? extends U> mapper)
                  |
                  | - Trả về một DoubleStream/ IntStream  / LongStream / Stream bao gồm các kết quả
                  |   của việc áp dụng mapper được chỉ định cho các elements của stream này.
                  | - DoubleUnaryOperator là một functional interface có method
                  |   "double applyAsDouble(double)".
                  | - DoubleToIntFunction là một functional interface có method
                  |   "int applyAsInt(double)".
                  | - DoubleToLongFunction là functional interface có method "long applyAsLong(double)".
                  | - DoubleFunction<R> là một functional interface có method "R apply(double)".
------------------|--------------------------------------------------------------------------------
DoubleStream	  | peek(DoubleConsumer action)
                  | - Trả về một DoubleStream bao gồm các elements của stream này, thực hiện thêm action
                  |   được cung cấp trên mỗi element khi chúng được sử dụng từ returned stream.
                  | - DoubleConsumer là một functional interface có một "void accept(double)" method.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | sorted()
                  | - Trả về một DoubleStream bao gồm các elements của stream này, được sắp xếp theo
                  |   thứ tự tự nhiên.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | parallel()
                  | - Trả về một parallel DoubleStream tương đương, có thể là chính nó, vì stream đã
                  |   song song, hoặc vì trạng thái stream đã được sửa đổi thành song song.
------------------|--------------------------------------------------------------------------------
DoubleStream	  | sequential()
                  | - Trả về một sequential DoubleStream tương đương, có thể là chính nó, vì stream
                  |   đã tuần tự, hoặc vì trạng thái stream đã được sửa đổi thành tuần tự.
```


#### *2.2.2, The Terminal Operations*

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
void	          | forEach(DoubleConsumer action)
void	          | forEachOrdered(DoubleConsumer action)
                  |
                  | - Thực hiện một hành động cho từng element của stream này.
                  | - forEachOrdered thực hiện action theo thứ tự nếu stream có một thứ tự xác định.
                  | - DoubleConsumer là một functional interface có một "void accept(double)" method.
------------------|--------------------------------------------------------------------------------
double[]	      | toArray()
                  | - Trả về một mảng chứa các elements của stream này.
------------------|--------------------------------------------------------------------------------
long	          | count()
                  | - Trả về số lượng elements trong stream này.
------------------|--------------------------------------------------------------------------------
double	          | sum()
                  | - Trả về tổng của các elements trong stream này.
                  | - Nếu bất kỳ elements nào là NaN, hoặc tổng tại bất kỳ điểm nào là NaN, thì tổng
                  |   sẽ là NaN.
------------------|--------------------------------------------------------------------------------
OptionalDouble	  | average()
                  | - Trả về một OptionalDouble mô tả giá trị trung bình của các elements của stream
                  |   này, hoặc một empty OptionalDouble nếu stream là empty.
                  | - Nếu bất kỳ elements nào là NaN, hoặc tổng tại bất kỳ điểm nào là NaN, thì giá
                  |   trị trung bình sẽ là NaN.
                  | - Giá trị trung bình được trả về có thể thay đổi tùy theo thứ tự các giá trị. 
------------------|--------------------------------------------------------------------------------
OptionalDouble	  | max()
OptionalDouble	  | min()
                  |
                  | - Trả về một OptionalDouble mô tả element lớn nhất / nhỏ nhất của stream này,
                  |   hoặc một empty OptionalDouble nếu stream này là empty.
                  | - Element lớn nhất / nhỏ nhất sẽ là Double.NaN nếu bất kỳ elements nào là NaN.
                  | - Các methods này coi -0 < +0
------------------|--------------------------------------------------------------------------------
DoubleSummaryStatistics |	summaryStatistics()
                        | - Trả về một DoubleSummaryStatistics mô tả summary data về các elements của
                        |   stream này, hỗ trợ thực hiện thống kê như đếm, tính tổng, tính trung bình,
                        |   tìm max, tìm min.
------------------|--------------------------------------------------------------------------------
boolean	          | allMatch(DoublePredicate predicate)
                  | - Trả về true nếu tất cả các elements của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - DoublePredicate là một functional interface có method "boolean test(double)".
------------------|--------------------------------------------------------------------------------
boolean	          | anyMatch(DoublePredicate predicate)
                  | - Trả về true nếu bất kỳ elements nào của stream này có khớp với predicate được
                  |   cung cấp.
                  | - Nếu stream là empty thì trả về "false" và predicate không được đánh giá.
                  | - DoublePredicate là một functional interface có method "boolean test(double)".
------------------|--------------------------------------------------------------------------------
boolean	          | noneMatch(DoublePredicate predicate)
                  | - Trả về true nếu KHÔNG có bất kỳ elements nào của stream này có khớp với
                  |   predicate được cung cấp.
                  | - Nếu stream là empty thì trả về "true" và predicate không được đánh giá.
                  | - DoublePredicate là một functional interface có method "boolean test(double)".
------------------|--------------------------------------------------------------------------------
OptionalDouble	  | findFirst()
                  | - Trả về một OptionalDouble mô tả element đầu tiên của stream này, hoặc một empty
                  |   OptionalDouble nếu stream là empty.
                  | - Nếu stream không có thứ tự, thì bất kỳ element nào cũng có thể được trả về.
------------------|--------------------------------------------------------------------------------
OptionalDouble	  | findAny()
                  | - Trả về một OptionalDouble mô tả một số elements của stream này, hoặc một empty
                  |   OptionalDouble nếu stream là empty.
------------------|--------------------------------------------------------------------------------
OptionalDouble    | reduce(DoubleBinaryOperator op)
                  | - Trả về một OptionalDouble mô tả kết quả của việc thực thi reduction trên các
                  |   elements của stream này.
                  | - DoubleBinaryOperator là một functional interface có method
                  |   "double applyAsDouble(double, double)".
                  |   1st param là kết quả thực thi op.applyAsDouble(double,double) của lần lặp trước,
                  |   2nd param là element của lần lặp hiện tại.
                  |
                  |     boolean foundAny = false;
                  |     double result;
                  |     for (double element : this_stream) {
                  |         if (!foundAny) {
                  |             foundAny = true;
                  |             result = element;
                  |         }
                  |         else
                  |             result = op.applyAsDouble(result, element);
                  |     }
                  |     return foundAny ? OptionalDouble.of(result) : OptionalDouble.empty();
------------------|--------------------------------------------------------------------------------
double	          | reduce(double identity, DoubleBinaryOperator op)
                  | - Trả về một double value là kết quả của việc thực thi reduction trên các elements
                  |   của stream này.
                  | - DoubleBinaryOperator là một functional interface có method 
                  |   "double applyAsDouble(double,double)".
                  |   1st param là kết quả thực thi op.applyAsDouble(double,double) của lần lặp trước,
                  |   2nd param là element của lần lặp hiện tại.
                  |
                  |      double result = identity;
                  |      for (double element : this_stream)
                  |          result = op.applyAsDouble(result, element)
                  |      return result;
------------------|--------------------------------------------------------------------------------
<R> R	          | collect(
                  |     Supplier<R> supplier, 
                  |     ObjDoubleConsumer<R> accumulator, 
                  |     BiConsumer<R,R> combiner
                  | )
                  | - Thực hiện một mutable reduction operation trên các elements của stream này.
                  | - Kết quả được tạo ra là một mutable result container, như ArrayList, và các
                  |   elements được hợp lại bằng cách cập nhật state của kết quả chứ không phải bằng
                  |   cách thay thế kết quả.
                  | - Supplier<T> là một functional interface với functional method "T get()",
                  |   + supplier là một function tạo ra một result container object.
                  | - ObjDoubleConsumer<T> là một functional interface có method
                  |   "void accept(T, double)".
                  |   + accumulator là function giúp cập nhật result: 
                  |     1st param là container, 2nd param là element cần thêm vào container.
                  | - BiConsumer<T,U> là một functional interface với method "void accept(T, U)",
                  |   + combiner là function cho việc kết hợp (combine) 2 giá trị, sử dụng cho các 
                  |     parallel streams: 2 param đều là các containers.
                  |
                  |   R result = supplier.get();
                  |   for (double element : this stream)
                  |       accumulator.accept(result, element);
                  |   return result;
------------------|--------------------------------------------------------------------------------
PrimitiveIterator.OfDouble | iterator()
                           | - Trả về một iterator cho các elements của stream này.
---------------------------|-----------------------------------------------------------------------
Spliterator.OfDouble	   | spliterator()
                           | - Trả về một spliterator cho các elements của stream này.
```

**PrimitiveIterator.OfDouble** interface hỗ trợ các methods sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default void	  | forEachRemaining(Consumer<? super Double> action)
default void	  | forEachRemaining(DoubleConsumer action)
                  |
                  | - Thực hiện action được chỉ định cho từng element còn lại cho đến khi tất cả các
                  |   elements đã được xử lý hoặc action ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface có một "void accept(T)" method.
                  | - DoubleConsumer là một functional interface có một "void accept(double)" method.
------------------|--------------------------------------------------------------------------------
default Double	  | next()
                  | - Trả về element tiếp theo trong vòng lặp, result được thực hiện boxing.
------------------|--------------------------------------------------------------------------------
double	          | nextDouble()
                  | - Trả về double element tiếp theo trong vòng lặp.
                  | - Ném NoSuchElementException nếu iteration không có element tiếp theo.
```


### 2.3, The Inherited Methods

Các methods được thừa kế từ *java.util.stream.BaseStream* interface bao gồm:  

- close(), 
- isParallel(),  
- onClose(),    
- unordered()  


## 3. The OptionalDouble Class

Một OptionalDouble instance là một container object có thể chứa hoặc không thể chứa một double value.

```java
public final class OptionalDouble extends Object {}
```

Các methods mà OptionalDouble class hỗ trợ bao gồm:

```
Modifier and Type       |                        Method and Description
------------------------|---------------------------------------------------------------------------
static OptionalDouble   | empty() 
                        | - Trả về một empty OptionalDouble instance. Không có value nào cho
                        |   OptionalDouble này.
------------------------|---------------------------------------------------------------------------
static OptionalDouble   | of(double value)
                        | - Trả về một OptionalDouble instance với value đã chỉ định.
------------------------|---------------------------------------------------------------------------
boolean	                | isPresent()
                        | - Trả về true nếu có value, ngược lại trả về false.
------------------------|---------------------------------------------------------------------------
void	                | ifPresent(DoubleConsumer consumer)
                        | - Nếu có value, thì gọi consumer được chỉ định với value đó, 
                        | - Nếu không thì không làm gì cả.
                        | - Ném NullPointerException nếu không có value và consumer là null.
                        | - DoubleConsumer là một functional interface có "void accept(double)" method.
------------------------|---------------------------------------------------------------------------
double	                | getAsDouble()
                        | - Nếu có một value trong OptionalDouble này, thì trả về value, 
                        | - Nếu không thì ném NoSuchElementException.
------------------------|---------------------------------------------------------------------------
double	                | orElse(double other)
                        | - Nếu có một value trong OptionalDouble này, thì trả về value, 
                        | - Nếu không thì trả về other được chỉ định.
------------------------|---------------------------------------------------------------------------
double	                | orElseGet(DoubleSupplier other)
                        | - Nếu có một value trong OptionalDouble này, thì trả về value, 
                        | - Nếu không thì gọi other được chỉ định và trả về kết quả.
                        | - Ném NullPointerException nếu không có value và other là null.
                        | - DoubleSupplier là một functional interface có method "double getAsDouble()".
------------------------|---------------------------------------------------------------------------
<X extends Throwable>   |
    double              | orElseThrow(Supplier<X> exceptionSupplier) 
                        |    throws X extends Throwable
                        | - Nếu có một value trong OptionalDouble này, thì trả về value, 
                        | - Nếu không thì ném một ngoại lệ do exceptionSupplier được chỉ định tạo ra.
                        | - Ném NullPointerException nếu không có value và exceptionSupplier là null.
                        | - Supplier<T> là một functional interface với functional method "T get()".
------------------------|---------------------------------------------------------------------------
boolean	                | equals(Object obj)
                        | - So sánh bằng object được chỉ định với OptionalDouble này.
                        | - Trả về true nếu object được chỉ định cũng là một OptionalDouble instance,
                        |   và: 2 instances đều không có value, hoặc 2 value bằng nhau qua ==.
------------------------|---------------------------------------------------------------------------
int	                    | hashCode()
                        | - Trả về hash code của value hiện tại (nếu có), hoặc 0 nếu không có value.
------------------------|---------------------------------------------------------------------------
String	                | toString()
                        | - Trả về non-empty string biểu diễn của Optional này phù hợp cho debug.
```