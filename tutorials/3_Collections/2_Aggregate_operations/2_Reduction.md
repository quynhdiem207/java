# Lesson 2: Aggregate Operations

## Reduction

JDK chứa nhiều terminal operations (chẳng hạn như average, sum, min, max và count) trả về một giá trị bằng cách kết hợp nội dung của một stream. Các operations này được gọi là các *reduction operations*. JDK cũng chứa các *reduction operations* trả về một collection thay vì một value duy nhất. Nhiều reduction operations thực hiện một task cụ thể, chẳng hạn như tìm giá trị trung bình của các giá trị, hoặc nhóm các elements thành các categories. Tuy nhiên, JDK cung cấp cho bạn các general-purpose reduction operations là **collect()** và **reduce()**.


### 1. The Stream.reduce Method

*Stream.reduce* method là một general-purpose reduction operation.

*Ví dụ 1: Xem xét pipeline sau, nó tính tổng số tuổi của các thành viên nam trong roster collection bằng cách sử dụng reduction operation Stream.sum:*

```java
Integer totalAge = roster.stream()
    .mapToInt(Person::getAge)
    .sum();
```

*So sánh ví dụ trên với pipeline sau, sử dụng Stream.reduce để tính toán cùng một giá trị:*

```java
Integer totalAgeReduce = roster.stream()
   .map(Person::getAge)
   .reduce(0, (a, b) -> a + b);
```

*reduce* operation trong ví dụ trên có 2 argument:

- *identity*: identity element vừa là giá trị ban đầu của reduction, vừa là kết quả mặc định nếu không có elements nào trong stream.  

    *Trong ví dụ trên, identity element là 0; đây là giá trị ban đầu của tổng độ tuổi, và là giá trị mặc định nếu không có thành viên nào tồn tại trong roster collection.*  

- *accumulator*: accumulator function có 2 parameter: kết quả một phần của reduction, và element tiếp theo của stream. Nó trả về một kết quả từng phần mới.  

    *Trong ví dụ trên, accumulator function là một lambda expression thực hiện cộng 2 Integer value và trả về một Integer value, kết quả một phần của reduction là tổng của tất cả các số nguyên đã xử lý cho đến nay, và element tiếp theo của stream là một integer: (a, b) -> a + b*

*reduce* operation luôn trả về một giá trị mới. Tuy nhiên, accumulator function cũng trả về một giá trị mới mỗi khi nó xử lý một element của stream. Giả sử rằng bạn muốn thực hiện reduction với các elements của một stream thành một object phức tạp hơn, chẳng hạn như một collection. Điều này có thể cản trở hiệu suất của ứng dụng. Nếu reduce operation liên quan đến việc thêm các element vào một collection, thì mỗi khi accumulator function xử lý một element, nó sẽ tạo ra một collection mới chứa element đó, điều này không hiệu quả. Thay vào đó, sẽ hiệu quả hơn nếu bạn cập nhật một collection hiện có. Bạn có thể thực hiện việc này bằng *Stream.collect* method.


### 2. The Stream.collect Method

Không giống như *reduce* method, luôn tạo ra một giá trị mới khi nó xử lý một element, *collect* method sẽ sửa đổi hoặc thay đổi một giá trị hiện có.

*Ví dụ 2: Xem xét cách tìm giá trị trung bình trong một stream. Bạn yêu cầu 2 phần dữ liệu: số giá trị và tổng các giá trị đó. Tuy nhiên, giống như reduce method và tất cả các reduction methods khác, collect method chỉ trả về một giá trị. Bạn có thể tạo một data type mới chứa các member variables theo dõi số giá trị và tổng các giá trị đó, chẳng hạn như Averager class sau:*

```java
class Averager implements IntConsumer
{
    private int total = 0;
    private int count = 0;
        
    public double average() {
        return count > 0 ? ((double) total)/count : 0;
    }
        
    public void accept(int i) { total += i; count++; }
    public void combine(Averager other) {
        total += other.total;
        count += other.count;
    }
}
```

*Pipeline sau sử dụng Averager class và collect method để tính tuổi trung bình của tất cả các thành viên nam:*

```java
Averager averageCollect = roster.stream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .map(Person::getAge)
    .collect(Averager::new, Averager::accept, Averager::combine);
                   
System.out.println("Average age of male members: " + averageCollect.average());
```

*collect* operation trong ví dụ trên có 3 arguments:

- *supplier*: supplier là một factory method; nó tạo nên các instances mới. Đối với collect operation, nó tạo ra các instance của result container.  

    *Trong ví dụ trên, nó tạo ra một instance mới của Averager class.*  

- *accumulator*: accumulator function kết hợp một stream element vào một result container.  
    
    *Trong ví dụ trên, nó sửa đổi Averager result container bằng cách tăng count variable lên một và cộng giá trị của stream element - một số nguyên đại diện cho tuổi của một thành viên nam vào total variable.*  

- *combiner*: combiner function nhận 2 result container và hợp nhất nội dung của chúng.  

    *Trong ví dụ trên, nó sửa đổi Averager result container bằng cách tăng count variable lên với count variable của Averager instance khác, và cộng vào total variable giá trị của total variable của Averager instance khác.* 

**Note**:

- supplier là một lambda expression (hoặc một method reference), trái ngược với một giá trị như identity element trong reduce operation.  
- Các accumulator và combiner functions không trả về giá trị.  
- Bạn có thể sử dụng các collect operator với các parallel streams. Nếu bạn chạy collect method với một parallel streams, thì JDK sẽ tạo một stream mới bất cứ khi nào combiner function tạo một object mới, chẳng hạn như Averager object trong ví dụ trên. Do đó, bạn không phải lo lắng về việc đồng bộ hóa.

Mặc dù JDK cung cấp cho bạn *average* operation để tính giá trị trung bình của các elements trong một stream, nhưng bạn có thể sử dụng *collect* operation và một Custom class nếu bạn cần tính toán một số giá trị từ các elements của một stream.

*collect* operation là vô cùng phù hợp khi sử dụng cho các collections. 

*Ví dụ 3: Đặt tên của các thành viên nam trong một collection với collect operation:*

```java
List<String> namesOfMaleMembersCollect = roster.stream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .map(p -> p.getName())
    .collect(Collectors.toList());
```

Có một version của *collect* operator nhận một parameter của *Collector* interface type, nó đóng gói các hàm được sử dụng làm các arguments trong collect operator mà yêu cầu 3 arguments (supplier, accumulator, và combiner functions).

*Collectors* class chứa nhiều reduction operators hữu ích, chẳng hạn như tích lũy các elements vào các collections và tổng hợp các elements theo nhiều tiêu chí khác nhau. Các reduction operators này trả về các instances của *Collector* interface, vì vậy bạn có thể sử dụng chúng như một argument cho reduction operation.

*Ví dụ trên sử dụng Collectors.toList operation, nó tích lũy các stream elements vào một instance mới của List. Như với hầu hết các operations trong Collectors class, toList operation trả về một instance của Collector, không phải là một collection.*

*Ví dụ 4: Nhóm các thành viên của roster collection theo giới tính:*

```java
Map<Person.Sex, List<Person>> byGender = roster.stream()
    .collect(Collectors.groupingBy(Person::getGender));
```

*groupingBy* operation trả về một map có các keys là giá trị kết quả của việc áp dụng lambda expression được chỉ định làm argument của nó (được gọi là *classification function* - hàm phân loại). 

*Trong ví dụ trên, map được trả về chứa 2 keys, Person.Sex.MALE và Person.Sex.FEMALE. Các values tương ứng của các keys là các instances của List chứa các stream elements mà khi được classification function xử lý, sẽ tương ứng với key. Ví dụ, giá trị tương ứng với key Person.Sex.MALE là một instance của List chứa tất cả các thành viên nam.*

*Ví dụ 5: lấy tên của từng thành viên trong roster collection và nhóm chúng theo giới tính:*

```java
Map<Person.Sex, List<String>> namesByGender = roster.stream()
    .collect(Collectors.groupingBy(
        Person::getGender,                      
        Collectors.mapping(Person::getName, Collectors.toList())
    ));
```

*groupingBy* operation trong ví dụ này nhận 2 parameter: một classification function và một instance của Collector. Collector parameter được gọi là *downstream collector*. Đây là một collector mà Java runtime áp dụng cho các kết quả của một collector khác. Do đó, groupingBy operator này cho phép bạn áp dụng *collect* method cho các giá trị List được tạo bởi groupingBy operator. 

*Ví dụ trên áp dụng **mapping** collector, cái mà áp dụng mapping function Person::getName cho từng element của stream. Do đó, stream kết quả chỉ bao gồm tên của các thành viên.*

Một pipeline chứa một hoặc nhiều downstream collectors, được gọi là *multilevel reduction*.

*Ví dụ 6: Truy xuất tổng số tuổi của các thành viên của mỗi giới tính:*

```java
Map<Person.Sex, Integer> totalAgeByGender = roster.stream()
    .collect(Collectors.groupingBy(
        Person::getGender,                      
        Collectors.reducing(0, Person::getAge, Integer::sum)
    ));
```

**reducing** operation có 3 parameters:

- *identity*: Giống như *Stream.reduce* operation, identity element vừa là giá trị ban đầu của reduction, vừa là kết quả mặc định nếu không có elements nào trong stream.  

    *Trong ví dụ trên, identity element là 0; đây là giá trị ban đầu của tổng độ tuổi và giá trị mặc định nếu không có thành viên nào tồn tại.*  

- *mapper*: reducing operation áp dụng mapper function này cho tất cả các stream elements.  

    *Trong ví dụ trên, mapper truy xuất tuổi của mỗi thành viên.*  

- *operation*: operation function được sử dụng để thực hiện reduction với các mapped values.  
    
    *Trong ví dụ trên, operation function cộng các Integer values.*  

*Ví dụ 7: lấy độ tuổi trung bình của các thành viên thuộc mỗi giới tính:*

```java
Map<Person.Sex, Double> averageAgeByGender = roster.stream()
    .collect(Collectors.groupingBy(
        Person::getGender,                      
        Collectors.averagingInt(Person::getAge)
    ));
```