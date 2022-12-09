# Lesson 2: Aggregate Operations

## Parallelism

Parallel computing (Tính toán song song) bao gồm việc chia một problem thành các subproblems, giải quyết các problems đó đồng thời (song song, với mỗi subproblems chạy trong một thread riêng biệt), sau đó kết hợp (combine) kết quả của các solutions cho các subproblems đó. Java SE cung cấp *fork/join framework*, cho phép bạn dễ dàng triển khai tính toán song song trong các ứng dụng. Tuy nhiên, với framework này, bạn phải chỉ rõ cách thức các problems được chia nhỏ (partition - phân vùng). Với các aggregate operations, Java runtime thực hiện việc phân vùng và kết hợp các kết quả của các solutions cho bạn.

Một khó khăn trong việc triển khai song song trong các ứng dụng sử dụng các collections là các collections không phải thread-safe, có nghĩa là multiple threads không thể thao tác một collection mà không gây ra *thread interference* (can thiệp luồng) hoặc *memory consistency errors* (lỗi nhất quán bộ nhớ). Collections Framework cung cấp các *synchronization wrappers*, thêm đồng bộ hóa tự động vào một collection tùy ý, làm cho nó trở nên thread-safe. Tuy nhiên, đồng bộ hóa có thể gây nên *thread contention* (sự tranh chấp luồng). Bạn cần phải tránh tranh chấp luồng vì nó ngăn các thread chạy song song. Các aggregate operations và các parallel streams cho phép bạn triển khai song song với các non-thread-safe collections, miễn là bạn không sửa đổi collection trong khi bạn đang hoạt động trên đó.

**Note**: tính năng song song không tự động nhanh hơn so với việc thực hiện các operations tuần tự, mặc dù nó có thể xảy ra nếu bạn có đủ data và processor cores. Mặc dù các aggregate operations cho phép bạn triển khai song song dễ dàng hơn, nhưng bạn vẫn có trách nhiệm xác định xem ứng dụng của mình có phù hợp với song song hay không.

Section này bao gồm các topics sau:

- Executing Streams in Parallel,  
- Concurrent Reduction,  
- Ordering,  
- Side Effects:  
    + Laziness,  
    + Interference,  
    + Stateful Lambda Expressions.  


### 1. Executing Streams in Parallel

Bạn có thể thực thi các streams tuần tự hoặc song song. Khi một stream thực thi song song, Java runtime sẽ phân vùng (partition) stream đó thành nhiều substreams. Các aggregate operations lặp qua và xử lý các substreams này song song và sau đó kết hợp (combine) các kết quả.

Khi bạn tạo một stream, nó luôn là một serial stream, trừ khi được chỉ định là song song. Để tạo một parallel stream, có thể gọi **Collection.parallelStream()** operation, hoặc **BaseStream.parallel()**. 

*Ví dụ: Tính tuổi trung bình của tất cả các thành viên nam song song:*

```java
double average = roster.parallelStream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .mapToInt(Person::getAge)
    .average()
    .getAsDouble();
```


### 2. Concurrent Reduction

*Xem xét ví dụ sau: nhóm các thành viên theo giới tính, sử dụng collect() operation để tạo một Map:*

```java
Map<Person.Sex, List<Person>> byGender = roster.stream()
    .collect(Collectors.groupingBy(Person::getGender));
```

*Sau đây là thực hiện song song với kết quả tương tự:*

```java
ConcurrentMap<Person.Sex, List<Person>> byGender = roster.parallelStream()
    .collect(Collectors.groupingByConcurrent(Person::getGender));
```

Đây được gọi là *concurrent reduction*. Java runtime thực hiện concurrent reduction nếu một pipeline cụ thể có chứa *collect* operation đáp ứng tất cả những điều sau đây:

+ Stream là **parallel**.  
+ Parameter của collect operation, collector, có đặc tính **Collector.Characteristics.CONCURRENT**. Để xác định các characteristics của một collector, cần gọi *Collector.characteristics()* method.  
+ Stream là unordered, hoặc collector có đặc tính **Collector.Characteristics.UNORDERED**. Để đảm bảo stream là unordered, cần gọi *BaseStream.unordered()* operation.  

**Note**: Ví dụ trên trả về một instance của *ConcurrentMap* thay vì *Map*, và gọi *groupingByConcurrent* operation thay vì *groupingBy*. Không giống groupingByConcurrent operation, groupingBy operation hoạt động kém hơn với các parallel streams (Do nó hoạt động bằng cách merge 2 maps theo từng key, tốn kém về mặt tính toán). Tương tự, *Collectors.toConcurrentMap* operation hoạt động tốt hơn với các parallel streams so với *Collectors.toMap* operation.


### 3. Ordering

Thứ tự trong đó một pipeline xử lý các elements của một stream phụ thuộc vào việc stream được thực thi tuần tự hay song song, nguồn của stream và các intermediate operations. 

*Ví dụ: In các elements của một ArrayList instance với forEach operation nhiều lần:*

```java
Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8 };
List<Integer> listOfIntegers = new ArrayList<>(Arrays.asList(intArray));

System.out.println("listOfIntegers:");
listOfIntegers.stream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");

System.out.println("listOfIntegers sorted in reverse order:");
Comparator<Integer> normal = Integer::compare;
Comparator<Integer> reversed = normal.reversed(); 
Collections.sort(listOfIntegers, reversed);  
listOfIntegers.stream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");
     
System.out.println("Parallel stream");
listOfIntegers.parallelStream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");
    
System.out.println("Another parallel stream:");
listOfIntegers.parallelStream()
    .forEach(e -> System.out.print(e + " "));
System.out.println("");
     
System.out.println("With forEachOrdered:");
listOfIntegers.parallelStream()
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
```

*Output của ví dụ trên như sau:*

```
listOfIntegers:
1 2 3 4 5 6 7 8
listOfIntegers sorted in reverse order:
8 7 6 5 4 3 2 1
Parallel stream:
3 4 1 6 2 5 7 8
Another parallel stream:
6 3 1 5 7 8 4 2
With forEachOrdered:
8 7 6 5 4 3 2 1
```

*Ví dụ trên thực hiện như sau:*

+ Pipeline đầu tiên in các elements của listOfIntegers theo thứ tự chúng đã được thêm vào list.  
+ Pipeline thứ 2 in các elements của listOfIntegers sau khi nó được sắp xếp bởi Collections.sort() method.  
+ Pipeline thứ 3 và thứ 4 in các elements của listOfIntegers theo một thứ tự ngẫu nhiên. Các stream operations sử dụng iteration nội bộ khi xử lý các elements của một stream, do đó, khi bạn thực thi một stream song song, Java compiler và runtime sẽ xác định thứ tự xử lý các elements của stream để tối đa hóa lợi ích của tính toán song song, trừ khi stream operations có chỉ định khác.  
+ Pipeline thứ 5 sử dụng forEachOrdered() method xử lý các elements của stream theo thứ tự được chỉ định bởi nguồn của nó, bất kể bạn thực thi stream tuần tự hay song song. Lưu ý rằng bạn có thể mất các lợi ích của tính năng song song nếu bạn sử dụng các operations như forEachOrdered() với các parallel streams.  


### 4. Side Effects

Một method hoặc một expression có side-effect nếu ngoài việc trả về hoặc tạo ra một giá trị, nó còn sửa đổi state của computer. 

Các ví dụ bao gồm các mutable reductions (các operations sử dụng *collect()* operation), cũng như gọi System.out.println() method để debug. 

JDK xử lý tốt các side-effect nhất định trong pipeline. Đặc biệt, collect() operation được thiết kế để thực hiện các stream operations phổ biến nhất có các side-effect một cách parallel-safe. Các operations như forEach() và peek() được thiết kế cho các side-effects; một lambda expression mà trả về void, chẳng hạn như lambda expression gọi System.out.println, có thể không làm gì, nhưng có side-effect. Mặc dù vậy, bạn nên sử dụng các forEach và peek operations một cách cẩn thận; nếu bạn sử dụng một trong các operations này với một parallel stream, thì Java runtime có thể gọi lambda expression mà bạn đã chỉ định làm argument của nó đồng thời từ nhiều thread. Ngoài ra, không bao giờ truyền các lambda expression có side-effect dưới dạng argument trong các operations như filter và map.

Các section sau đây thảo luận về *interference* (sự can thiệp) và *stateful lambda expression*, cả 2 đều có thể là nguồn gây ra các side-effect và có thể trả về kết quả không nhất quán hoặc không thể đoán trước, đặc biệt là trong các parallel streams. Tuy nhiên, khái niệm về *laziness* được thảo luận đầu tiên, vì nó có ảnh hưởng trực tiếp đến interference.


#### *4.1, Laziness*

Tất cả các intermediate operations đều là *lazy*. Một expression, method hoặc algorithm là lazy nếu giá trị của nó chỉ được đánh giá khi nó được yêu cầu. (Một algorithm là *eager* nếu nó được đánh giá hoặc xử lý ngay lập tức.) Các intermediate operations là lazy vì chúng không bắt đầu xử lý content của stream cho đến khi terminal operation bắt đầu. Xử lý stream một cách lazy cho phép Java compiler và runtime tối ưu hóa cách chúng xử lý stream. 

*Ví dụ: Trong một pipeline filter-mapToInt-average, average operation có thể lấy một vài số nguyên đầu tiên từ stream được tạo bởi mapToInt operation mà lấy các elements từ filter operation. average operation sẽ lặp lại quá trình này cho đến khi nó thu được tất cả các elements cần thiết từ stream, và sau đó nó sẽ tính giá trị trung bình.*


#### *4.2, Interference*

Lambda expression trong các stream operations không nên *interfere* (can thiệp). Interference xảy ra khi nguồn của một stream được sửa đổi trong khi một pipeline xử lý stream. 

*Ví dụ, đoạn mã sau cố gắng nối các strings có trong List listOfStrings. Tuy nhiên, nó ném ra một ConcurrentModificationException:*

```java
try {
    List<String> listOfStrings = new ArrayList<>(Arrays.asList("one", "two"));
         
    // Điều này sẽ fail vì peek operation sẽ cố gắng thêm string "three" vào source
    // sau khi terminal operation bắt đầu. 
    String concatenatedString = listOfStrings.stream()
        .peek(s -> listOfStrings.add("three")) // Đừng làm điều này! Interference xảy ra ở đây.
        .reduce((a, b) -> a + " " + b)
        .get();
                 
    System.out.println("Concatenated string: " + concatenatedString);
         
} catch (Exception e) {
    System.out.println("Exception caught: " + e.toString());
}
```

*Ví dụ trên nối các strings có trong listOfStrings thành một [Optional<String>] với reduce() operation, là một terminal operation. Tuy nhiên, pipeline ở đây gọi intermediate operation peek(), nó cố gắng thêm một element mới vào listOfStrings. Hãy nhớ rằng, tất cả các intermediate operations là lazy. Điều này có nghĩa là pipeline trong ví dụ này bắt đầu thực thi khi get() operation được gọi, và kết thúc thực thi khi get() operation hoàn tất. Argument của peek() operation cố gắng sửa đổi stream source trong quá trình thực thi pipeline, điều này khiến Java runtime ném ra một ConcurrentModificationException.*


#### *4.3, Stateful Lambda Expressions*

Tránh sử dụng các *stateful lambda expression* làm argument trong các stream operations. State lambda expression là biểu thức có kết quả phụ thuộc vào bất kỳ state nào có thể thay đổi trong quá trình thực thi một pipeline. 

*Ví dụ: Thêm các elements từ List listOfIntegers vào một List instance mới bằng map() intermediate operation. Nó thực hiện điều này 2 lần, đầu tiên với một serial stream, và sau đó với một parallel stream:*

```java
List<Integer> serialStorage = new ArrayList<>();
     
System.out.println("Serial stream:");
listOfIntegers.stream()
    .map(e -> { serialStorage.add(e); return e; }) // Don't do this!
                                                   // It uses a stateful lambda expression.
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
     
serialStorage.stream()
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");

System.out.println("Parallel stream:");
List<Integer> parallelStorage = Collections.synchronizedList(new ArrayList<>());
listOfIntegers.parallelStream()
    .map(e -> { parallelStorage.add(e); return e; }) // Don't do this!
                                                     // It uses a stateful lambda expression.
    
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
     
parallelStorage.stream()
    .forEachOrdered(e -> System.out.print(e + " "));
System.out.println("");
```

*Lambda expression "e -> { parallelStorage.add(e); return e; }" là một stateful lambda expression. Kết quả của nó có thể thay đổi mỗi khi mã được chạy. Ví dụ này in ra như sau:*

```
Serial stream:
8 7 6 5 4 3 2 1
8 7 6 5 4 3 2 1
Parallel stream:
8 7 6 5 4 3 2 1
1 3 6 2 4 5 8 7
```

forEachOrdered operation xử lý các elements theo thứ tự được chỉ định bởi stream, bất kể stream được thực thi tuần tự hay song song. Tuy nhiên, khi một stream được thực thi song song, map operation sẽ xử lý các elements của stream được chỉ định bởi Java runtime và compiler. Do đó, thứ tự trong đó lambda expression  "e -> { parallelStorage.add(e); return e; }" thêm các elements vào List parallelStorage có thể thay đổi mỗi khi mã được chạy. Đối với các kết quả xác định và có thể dự đoán được, hãy đảm bảo rằng các lambda expression argument trong các stream operations không phải là stateful.

**Note**: Ví dụ trên gọi *synchronizedList()* method để List parallelStorage là thread-safe. Hãy nhớ rằng các collections KHÔNG phải là thread-safe. Điều này có nghĩa là nhiều threads không nên truy cập vào một collections cụ thể cùng một lúc. Giả sử rằng bạn không gọi synchronizedList() method khi tạo List parallelStorage:

```java
List<Integer> parallelStorage = new ArrayList<>();
```

Ví dụ trên sẽ hoạt động thất thường vì nhiều thread truy cập và sửa đổi List parallelStorage mà không có cơ chế như đồng bộ hóa để lên lịch khi một thread cụ thể có thể truy cập vào List instance. Do đó, ví dụ trên có thể in output tương tự như sau:

```
Parallel stream:
8 7 6 5 4 3 2 1
null 3 5 4 7 8 1 2
```