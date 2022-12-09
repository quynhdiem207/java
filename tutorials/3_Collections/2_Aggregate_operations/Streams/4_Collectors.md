# The Collectors Class 

Các implementations của Collector interface triển khai nhiều reduction operations hữu ích, chẳng hạn như gom các elements vào các collections, phân nhóm các elements theo các tiêu chí khác nhau, ...

```java
public final class Collectors extends Object {}
```

Collectors class hỗ trợ các predefined collectors - là các implementations của Collector interface:

```
Modifier and Type                             |              Method and Description
----------------------------------------------|----------------------------------------------------
static <T> Collector<T,?,Long>	              | counting()
                                              | - Trả về một Collector đếm số input elements. 
----------------------------------------------|----------------------------------------------------
static <T> Collector<T,?,Double>	          | averagingInt(ToIntFunction<? super T> mapper)
static <T> Collector<T,?,Double>	          | averagingLong(ToLongFunction<? super T> mapper)
static <T> Collector<T,?,Double>	          | averagingDouble(ToDoubleFunction<? super T> mapper)
                                              |
                                              | - Trả về một Collector tạo ra giá trị trung bình của
                                              |   các int/long/double values là kết quả áp dụng 
                                              |   mapper function cho các input elements. 
                                              | - Nếu không có elements nào thì kết quả là 0. 
                                              | - Với averagingDouble, giá trị trung bình trả về có
                                              |   thể thay đổi tùy theo thứ tự các giá trị, do lỗi
                                              |   làm tròn tích lũy cùng với các giá trị có độ lớn
                                              |   khác nhau. Nếu bất kỳ giá trị nào là NaN hoặc tổng
                                              |   tại bất kỳ điểm nào là NaN thì giá trị trung bình
                                              |   sẽ là NaN.
                                              | - ToIntFunction<T> là một functional interface có 
                                              |   method "int applyAsInt(T)".
                                              | - ToLongFunction<T> là một functional interface có 
                                              |   method "long applyAsLong(T)".
                                              | - ToDoubleFunction<T> là một functional interface
                                              |   có method "double applyAsDouble(T)".
----------------------------------------------|----------------------------------------------------
static <T> Collector<T,?,Integer>	          | summingInt(ToIntFunction<? super T> mapper)
static <T> Collector<T,?,Long>	              | summingLong(ToLongFunction<? super T> mapper)
static <T> Collector<T,?,Double>	          | summingDouble(ToDoubleFunction<? super T> mapper)
                                              |
                                              | - Trả về một Collector tạo ra tổng của các 
                                              |   int/long/double values là kết quả áp dụng mapper
                                              |   function cho các input elements. 
                                              | - Nếu không có elements nào thì kết quả là 0. 
                                              | - Với summingDouble, tổng được trả về có thể thay đổi
                                              |   tùy theo thứ tự các giá trị, do lỗi làm tròn tích
                                              |   lũy cùng với các giá trị có độ lớn khác nhau. Nếu
                                              |   bất kỳ giá trị nào là NaN hoặc tổng tại bất kỳ điểm
                                              |   nào là NaN thì tổng sẽ là NaN.
                                              | - ToIntFunction<T> là một functional interface có 
                                              |   method "int applyAsInt(T)".
                                              | - ToLongFunction<T> là một functional interface có 
                                              |   method "long applyAsLong(T)".
                                              | - ToDoubleFunction<T> là một functional interface
                                              |   có method "double applyAsDouble(T)".
----------------------------------------------|----------------------------------------------------
static <T> Collector<T,?,IntSummaryStatistics>|	summarizingInt(ToIntFunction<? super T> mapper)
static <T>                                    |
    Collector<T,?,LongSummaryStatistics>	  | summarizingLong(ToLongFunction<? super T> mapper)
static <T>                                    |
    Collector<T,?,DoubleSummaryStatistics>	  | summarizingDouble(ToDoubleFunction<? super T> mapper)
                                              |
                                              | - Trả về một Collector áp dụng mapper function cho
                                              |   từng input element và trả về thống kê tóm tắt 
                                              |   (IntSummaryStatistics / LongSummaryStatistics / 
                                              |   DoubleSummaryStatistics) cho các kết quả.
                                              | - ToIntFunction<T> là một functional interface có 
                                              |   method "int applyAsInt(T)".
                                              | - ToLongFunction<T> là một functional interface có 
                                              |   method "long applyAsLong(T)".
                                              | - ToDoubleFunction<T> là một functional interface
                                              |   có method "double applyAsDouble(T)".
----------------------------------------------|----------------------------------------------------
static Collector<CharSequence,?,String>	      | joining()
static Collector<CharSequence,?,String>	      | joining(CharSequence delimiter)
static Collector<CharSequence,?,String>	      | joining(
                                              |     CharSequence delimiter, 
                                              |     CharSequence prefix, 
                                              |     CharSequence suffix
                                              | )
                                              |
                                              | - Trả về một Collector nối các input elements, được
                                              |   phân tách bởi delimiter được chỉ định (nếu có),
                                              |   với prefix và suffix được chỉ định (nếu có).
----------------------------------------------|----------------------------------------------------
static <T> Collector<T,?,Optional<T>>	      | maxBy(Comparator<? super T> comparator)
static <T> Collector<T,?,Optional<T>>	      | minBy(Comparator<? super T> comparator)
                                              |
                                              | - Trả về một Collector tạo ra element lớn nhất / nhỏ
                                              |   nhất theo một comparator được chỉ định.
                                              | - Comparator<T> là một functional interface với
                                              |   method "int compare(T, T)".
==============================================|====================================================
static <T,C extends Collection<T>>            |
    Collector<T,?,C>	                      | toCollection(Supplier<C> collectionFactory)
                                              |
                                              | - Trả về một Collector gom các input elements vào một
                                              |   Collection mới được tạo ra bởi collectionFactory
                                              |   được cung cấp.
                                              | - Supplier<T> là một functional interface với method
                                              |   "T get()".
                                              |
                                              | Collection<String> people = perList.stream()
                                              |   .map(x -> x.name)
                                              |   .collect(Collectors.toCollection(HashSet::new));
----------------------------------------------|----------------------------------------------------
static <T> Collector<T,?,Set<T>>	          | toSet()
static <T> Collector<T,?,List<T>>             | toList()
                                              |
                                              | - Trả về một Collector gom các input elements vào một
                                              |   Set / List mới. 
                                              | - Không có đảm bảo về type, mutability, serializability,
                                              |   hay thread-safety của Set / List được trả về
----------------------------------------------|----------------------------------------------------
static <T,K,U> Collector<T,?,Map<K,U>>	      | toMap(
                                              |     Function<? super T,? extends K> keyMapper, 
                                              |     Function<? super T,? extends U> valueMapper
                                              | )
                                              |
static <T,K,U> Collector<T,?,Map<K,U>>	      | toMap(
                                              |     Function<? super T,? extends K> keyMapper, 
                                              |     Function<? super T,? extends U> valueMapper,
                                              |     BinaryOperator<U> mergeFunction
                                              | )
static <T,K,U,M extends Map<K,U>>             |
    Collector<T,?,M>	                      | toMap(
                                              |     Function<? super T,? extends K> keyMapper, 
                                              |     Function<? super T,? extends U> valueMapper,
                                              |     BinaryOperator<U> mergeFunction,
                                              |     Supplier<M> mapSupplier
                                              | )
                                              |
                                              | - Trả về một Collector gom các input elements vào một
                                              |   Map có các keys và values là kết quả của việc áp
                                              |   dụng các mapping functions được cung cấp cho các
                                              |   input elements.
                                              | - Nếu các keys bị duplicate, thì áp dụng valueMapper
                                              |   cho từng element và kết quả được kết hợp lại bằng
                                              |   cách sử dụng mergeFunction nếu được cung cấp, nếu
                                              |   không, IllegalStateException sẽ được ném ra.
                                              | - Map được tạo bởi mapSupplier được cung cấp (nếu có).
                                              | - Function<T,R> là một functional interface có method
                                              |   "R apply(T)".
                                              | - BinaryOperator<T> là một functional interface mở
                                              |   rộng BiFunction<T,T,T> có method "T apply(T,T)".
                                              | - Supplier<T> là một functional interface với method
                                              |   "T get()".
----------------------------------------------|----------------------------------------------------
static <T,K,U>                                |
    Collector<T,?,ConcurrentMap<K,U>>	      | toConcurrentMap(
                                              |     Function<? super T,? extends K> keyMapper, 
                                              |     Function<? super T,? extends U> valueMapper
                                              | )
static <T,K,U>                                |
    Collector<T,?,ConcurrentMap<K,U>>	      | toConcurrentMap(
                                              |     Function<? super T,? extends K> keyMapper, 
                                              |     Function<? super T,? extends U> valueMapper,
                                              |     BinaryOperator<U> mergeFunction
                                              | )
static <T,K,U, M extends ConcurrentMap<K,U>>  |
    Collector<T,?,M>	                      | toConcurrentMap(
                                              |     Function<? super T,? extends K> keyMapper, 
                                              |     Function<? super T,? extends U> valueMapper,
                                              |     BinaryOperator<U> mergeFunction,
                                              |     Supplier<M> mapSupplier
                                              | )
                                              |
                                              | - Trả về một Collector gom các input elements vào một
                                              |   ConcurrentMap có các keys và values là kết quả của
                                              |   việc áp dụng các mapping functions được cung cấp cho
                                              |   các input elements.
                                              | - Đây là một concurrent và unordered Collector.
                                              | - Nếu các keys bị duplicate, thì áp dụng valueMapper
                                              |   cho từng element và kết quả được kết hợp lại bằng
                                              |   cách sử dụng mergeFunction nếu được cung cấp, nếu
                                              |   không, IllegalStateException sẽ được ném ra.
                                              | - ConcurrentMap được tạo bởi mapSupplier được cung cấp
                                              |   (nếu có).
                                              | - Function<T,R> là một functional interface có method
                                              |   "R apply(T)".
                                              | - BinaryOperator<T> là một functional interface mở
                                              |   rộng BiFunction<T,T,T> có method "T apply(T,T)".
                                              | - Supplier<T> là một functional interface với method
                                              |   "T get()".
==============================================|====================================================
static <T,U,A,R> Collector<T,?,R>	          | mapping(
                                              |     Function<? super T,? extends U> mapper, 
                                              |     Collector<? super U,A,R> downstream
                                              | )
                                              |
                                              | - Điều chỉnh một Collector chấp nhận các elements của
                                              |   type U thành một Collector chấp nhận elements của
                                              |   type T bằng cách áp dụng mapper function cho từng
                                              |   input element trước khi thực hiện reduction.
                                              | - Hữu ích khi được sử dụng trong multi-level reduction.
                                              | - Function<T,R> là một functional interface có method
                                              |   "R apply(T)".
                                              |
                                              | Set<String> result = personList.stream().collect(
                                              |   Collectors.mapping(x -> x.name, Collectors.toSet())
                                              | );
                                              |
                                              | Map<Integer, Set<String>> result = 
                                              |   intList.stream().collect(Collectors.groupingBy(
                                              |     x -> x.age,
                                              |     Collectors.mapping(x -> x.name, Collectors.toSet())
                                              |   ));
----------------------------------------------|----------------------------------------------------
public static <T,A,R,RR> Collector<T,A,RR>    | collectingAndThen(
                                              |     Collector<T,A,R> downstream,
                                              |     Function<R,RR> finisher
                                              | )
                                              |
                                              | - Điều chỉnh một Collector để thực hiện finisher đối
                                              |   với result cuối cùng của downstream.
                                              | - Function<T,R> là một functional interface có method
                                              |   "R apply(T)".
                                              |
                                              | long count = intList.stream().collect(
                                              |   Collectors.collectingAndThen(
                                              |     Collectors.counting(),
                                              |     x -> x + 2
                                              |   ));
----------------------------------------------|----------------------------------------------------
static <T> Collector<T,?,Optional<T>>	      | reducing(BinaryOperator<T> op)
static <T> Collector<T,?,T>	                  | reducing(T identity, BinaryOperator<T> op)
static <T,U> Collector<T,?,U>	              | reducing(
                                              |     U identity, 
                                              |     Function<? super T,? extends U> mapper, 
                                              |     BinaryOperator<U> op
                                              | )
                                              |
                                              | - Trả về một Collector để thực hiện reduction với các
                                              |   input elements và trả về một result, bằng cách lặp
                                              |   qua các elements và thực thi op.
                                              | - identity (nếu có) được gán cho result trước khi lặp.
                                              | - mapper (nếu có) cho phép chuyển đổi các elements
                                              |   trước khi thực hiện reduction.
                                              | - Hữu ích khi được sử dụng trong multi-level reduction.
                                              | - Function<T,R> là một functional interface có method
                                              |   "R apply(T)".
                                              | - BinaryOperator<T> là một functional interface mở
                                              |   rộng BiFunction<T,T,T> có method "T apply(T,T)".
                                              | 
                                              | Map<String, Optional<Person>> result = people.stream()
                                              |   .collect(Collectors.groupingBy(
                                              |      x -> x.name,
                                              |      Collectors.reducing((a,b) -> a.age > b.age ? a: b)
                                              |    ));
                                              |
                                              | Map<String, Integer> result = people.stream()
                                              |   .collect(Collectors.groupingBy(
                                              |      x -> x.name,
                                              |      Collectors.reducing(2, x -> x.age, (a,b) -> a+b)
                                              |   ));
==============================================|====================================================
static <T> Collector<T,?,Map<Boolean,List<T>>>|	partitioningBy(Predicate<? super T> predicate)
static <T,D,A> Collector<T,?,Map<Boolean,D>>  |	partitioningBy(
                                              |     Predicate<? super T> predicate, 
                                              |     Collector<? super T,A,D> downstream
                                              | )
                                              | 
                                              | - Trả về một Collector phân chia các input elements
                                              |   theo predicate được cung cấp. 
                                              | - Nếu downstream KHÔNG được cung cấp, thì tổ chức các
                                              |   elements thành một Map<Boolean, List<T>>; Nếu không,
                                              |   thực hiện reduction với các values trong mỗi phân
                                              |   vùng theo downstream và tổ chức các elements thành
                                              |   một Map<Boolean, D> có các values là kết quả của
                                              |   reduction.  
                                              | - Không có đảm bảo nào về type, mutability,
                                              |   serializability, hay thread-safety của returned Map.
                                              | - Predicate<T> là một functional interface có method
                                              |   "boolean test(T)".
                                              |
                                              | Map<Boolean, List<Person>> result = people.stream()
                                              |   .collect(Collectors.partitioningBy(
                                              |      x -> x.age > 20 ? true : false
                                              |   ));
                                              |
                                              | Map<Boolean, Long> result = people.stream()
                                              |   .collect(Collectors.partitioningBy(
                                              |      x -> x.age > 20 ? true : false,
                                              |      Collectors.counting()
                                              |   ));
----------------------------------------------|----------------------------------------------------
static <T,K> Collector<T,?,Map<K,List<T>>>    | groupingBy(Function<? super T,? extends K> classifier)
static <T,K,A,D> Collector<T,?,Map<K,D>>	  | groupingBy(
                                              |     Function<? super T,? extends K> classifier,
                                              |     Collector<? super T,A,D> downstream
                                              | )
static <T,K,D,A,M extends Map<K,D>>           |
    Collector<T,?,M>	                      | groupingBy(
                                              |     Function<? super T,? extends K> classifier,
                                              |     Supplier<M> mapFactory, 
                                              |     Collector<? super T,A,D> downstream
                                              | )
                                              | 
                                              | - Trả về Collector thực hiện thao tác "group by" trên
                                              |   các input elements thuộc type T theo classifier và
                                              |   trả về kết quả trong Map.
                                              | - classifier ánh xạ các elements với một số key có
                                              |   type K. Nếu downstream KHÔNG được cung cấp, thì
                                              |   Collector tạo ra một Map<K, List<T>> có các keys là
                                              |   result từ việc áp dụng classifier cho các input
                                              |   elements và có các values tương ứng là Lists chứa các
                                              |   input elements ánh xạ đến key liên quan. Nếu không,
                                              |   thực hiện reduction trên các values được liên kết với
                                              |   một key nhất định bằng cách sử dụng downstream được
                                              |   chỉ định và tạo ra result của type D, Collector sẽ
                                              |   tạo ra một Map<K, D>.
                                              | - Map do Collector tạo ra được tạo bởi mapFactory được
                                              |   cung cấp (nếu có).
                                              | - Không có đảm bảo nào về type, mutability,
                                              |   serializability, hay thread-safety của Map hay List
                                              |   được trả về.
                                              | - Function<T,R> là một functional interface có method
                                              |   "R apply(T)".
                                              | - Supplier<T> là một functional interface với method
                                              |   "T get()".
                                              |
                                              | Map<String, List<Person>> result = intList.stream()
                                              |   .collect(Collectors.groupingBy(x -> x.name));
                                              |
                                              | Map<String, Long> result = intList.stream()
                                              |   .collect(Collectors.groupingBy(
                                              |      x -> x.name, 
                                              |      Collectors.counting()
                                              |   ));
----------------------------------------------|----------------------------------------------------
static <T,K>                                  |
    Collector<T,?,ConcurrentMap<K,List<T>>>	  | groupingByConcurrent(
                                              |     Function<? super T,? extends K> classifier
                                              | )
static <T,K,A,D>                              |
    Collector<T,?,ConcurrentMap<K,D>>	      | groupingByConcurrent(
                                              |     Function<? super T,? extends K> classifier,
                                              |     Collector<? super T,A,D> downstream
                                              | )
static <T,K,A,D,M extends ConcurrentMap<K,D>> |
    Collector<T,?,M>	                      | groupingByConcurrent(
                                              |     Function<? super T,? extends K> classifier,
                                              |     Supplier<M> mapFactory,
                                              |     Collector<? super T,A,D> downstream
                                              | )
                                              |
                                              | - Trả về Collector thực hiện thao tác "group by" trên
                                              |   các input elements thuộc type T theo classifier và
                                              |   trả về kết quả trong ConcurrentMap.
                                              | - Đây là một concurrent và unordered Collector.
                                              | - classifier ánh xạ các elements với một số key có
                                              |   type K. Nếu downstream KHÔNG được cung cấp, thì
                                              |   Collector tạo ra một ConcurrentMap<K, List<T>> có các
                                              |   keys là result từ việc áp dụng classifier cho các input
                                              |   elements và có các values tương ứng là Lists chứa các
                                              |   input elements ánh xạ đến key liên quan. Nếu không,
                                              |   thực hiện reduction trên các values được liên kết với
                                              |   một key nhất định bằng cách sử dụng downstream được
                                              |   chỉ định và tạo ra result của type D, Collector sẽ
                                              |   tạo ra một Map<K, D>.
                                              | - ConcurrentMap do Collector tạo ra được tạo bởi 
                                              |   mapFactory được cung cấp (nếu có).
                                              | - Không có đảm bảo nào về type, mutability,
                                              |   serializabilitycủa Map hay List được trả về, hay về
                                              |   thread-safety của List được trả về.
                                              | - Function<T,R> là một functional interface có method
                                              |   "R apply(T)".
                                              | - Supplier<T> là một functional interface với method
                                              |   "T get()".
```

*Ví dụ: Sử dụng các predefined collectors để thực hiện các mutable reduction tasks phổ biến:*

```java
// Accumulate names into a List
List<String> list = people.stream()
    .map(Person::getName).collect(Collectors.toList());

// Accumulate names into a TreeSet
Set<String> set = people.stream()
    .map(Person::getName).collect(Collectors.toCollection(TreeSet::new));

// Convert elements to strings and concatenate them, separated by commas
String joined = things.stream()
    .map(Object::toString).collect(Collectors.joining(", "));

// Compute sum of salaries of employee
int total = employees.stream()
    .collect(Collectors.summingInt(Employee::getSalary));

// Group employees by department
Map<Department, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

// Compute sum of salaries by department
Map<Department, Integer> totalByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.summingInt(Employee::getSalary)
    ));

// Partition students into passing and failing
Map<Boolean, List<Student>> passingFailing = students.stream()
    .collect(Collectors.partitioningBy(s -> s.getGrade() >= PASS_THRESHOLD));
```


## The IntSummaryStatistics Class

Một state object để thu thập số liệu thống kê như count, min, max, sum và average.

Class này được thiết kế để làm việc với các streams:

```java
IntSummaryStatistics stats = intStream.collect(
    IntSummaryStatistics::new,
    IntSummaryStatistics::accept,
    IntSummaryStatistics::combine
);
```

*IntSummaryStatistics* có thể được sử dụng như một reduction target cho một stream:

```java
IntSummaryStatistics stats = people.stream()
    .collect(Collectors.summarizingInt(Person::getDependents));
```


Các methods được hỗ trợ bao gồm:

```
Modifier and Type |              Method and Description
------------------|--------------------------------------------------------------------------------
void	          | accept(int value)
                  | - Ghi lại một giá trị mới vào summary information.
------------------|--------------------------------------------------------------------------------
void	          | combine(IntSummaryStatistics other)
                  | - Kết hợp trạng thái của một IntSummaryStatistics khác vào object này.
                  | - Ném NullPointerException nếu other là null.
------------------|--------------------------------------------------------------------------------
double	          | getAverage()
                  | - Trả về giá trị trung bình của các giá trị được ghi lại, hoặc bằng 0 nếu không
                  |   có giá trị nào được ghi lại.
------------------|--------------------------------------------------------------------------------
long	          | getSum()
                  | - Trả về tổng các giá trị được ghi lại, hoặc bằng 0 nếu không có giá trị nào
                  |   được ghi lại.
------------------|--------------------------------------------------------------------------------
long	          | getCount()
                  | - Trả về số lượng các giá trị đã ghi.
------------------|--------------------------------------------------------------------------------
int	              | getMax()
                  | - Trả về giá trị lớn nhất đã ghi, hoặc Integer.MIN_VALUE nếu không có giá trị
                  |   nào được ghi.
------------------|--------------------------------------------------------------------------------
int	              | getMin()
                  | - Trả về giá trị nhỏ nhất được ghi lại, hoặc Integer.MAX_VALUE nếu không có giá
                  |   trị nào được ghi lại.
------------------|--------------------------------------------------------------------------------
String	          | toString()
                  | - Trả về một string đại diện của object.
```


## The LongSummaryStatistics Class

```
Modifier and Type |              Method and Description
------------------|--------------------------------------------------------------------------------
void	          | accept(int value)
void	          | accept(long value)
                  | - Ghi lại một int / long value mới vào summary information.
------------------|--------------------------------------------------------------------------------
void	          | combine(LongSummaryStatistics other)
                  | - Kết hợp trạng thái của một LongSummaryStatistics khác vào object này.
                  | - Ném NullPointerException nếu other là null.
------------------|--------------------------------------------------------------------------------
double	          | getAverage()
                  | - Trả về giá trị trung bình của các giá trị được ghi lại, hoặc bằng 0 nếu không
                  |   có giá trị nào được ghi lại.
------------------|--------------------------------------------------------------------------------
long	          | getSum()
                  | - Trả về tổng các giá trị được ghi lại, hoặc bằng 0 nếu không có giá trị nào
                  |   được ghi lại.
------------------|--------------------------------------------------------------------------------
long	          | getCount()
                  | - Trả về số lượng các giá trị đã ghi.
------------------|--------------------------------------------------------------------------------
long              | getMax()
                  | - Trả về giá trị lớn nhất đã ghi, hoặc Long.MIN_VALUE nếu không có giá trị nào
                  |   được ghi.
------------------|--------------------------------------------------------------------------------
long	          | getMin()
                  | - Trả về giá trị nhỏ nhất được ghi lại, hoặc Long.MAX_VALUE nếu không có giá
                  |   trị nào được ghi lại.
------------------|--------------------------------------------------------------------------------
String	          | toString()
                  | - Trả về một string đại diện của object.
```


## The DoubleSummaryStatistics Class

```
Modifier and Type |              Method and Description
------------------|--------------------------------------------------------------------------------
void	          | accept(double value)
                  | - Ghi lại một giá trị mới vào summary information.
------------------|--------------------------------------------------------------------------------
void	          | combine(DoubleSummaryStatistics other)
                  | - Kết hợp trạng thái của một DoubleSummaryStatistics khác vào object này.
                  | - Ném NullPointerException nếu other là null.
------------------|--------------------------------------------------------------------------------
double	          | getAverage()
                  | - Trả về giá trị trung bình của các giá trị được ghi lại, hoặc bằng 0 nếu không
                  |   có giá trị nào được ghi lại.
                  | - Nếu bất kỳ giá trị nào được ghi lại là NaN, hoặc tổng tại bất kỳ điểm nào là
                  |   NaN thì giá trị trung bình sẽ là mã NaN.
                  | - Giá trị trung bình được trả lại có thể thay đổi tùy theo thứ tự các giá trị.
------------------|--------------------------------------------------------------------------------
double	          | getSum()
                  | - Trả về tổng các giá trị được ghi lại, hoặc bằng 0 nếu không có giá trị nào
                  |   được ghi lại.
                  | - Nếu bất kỳ giá trị nào được ghi lại là NaN, hoặc tổng tại bất kỳ điểm nào là
                  |   NaN thì tổng sẽ là NaN.
------------------|--------------------------------------------------------------------------------
long	          | getCount()
                  | - Trả về số lượng các giá trị đã ghi.
------------------|--------------------------------------------------------------------------------
double	          | getMax()
                  | - Trả về giá trị lớn nhất được ghi lại, hoặc NaN nếu bất kỳ giá trị nào được ghi
                  |   lại là NaN, hoặc Double.NEGATIVE_INFINITY nếu không có giá trị nào được ghi lại.
                  | - Method này coi -0 < +0.
------------------|--------------------------------------------------------------------------------
double	          | getMin()
                  | - Trả về giá trị nhỏ nhất được ghi lại, hoặc NaN nếu bất kỳ giá trị nào được ghi
                  |   lại là NaN, hoặc Double.POSITIVE_INFINITY nếu không có giá trị nào được ghi lại.
                  | - Method này coi -0 < +0.
------------------|--------------------------------------------------------------------------------
String	          | toString()
                  | - Trả về một string đại diện của object.
```
