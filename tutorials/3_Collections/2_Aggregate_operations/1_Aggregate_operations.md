# Lesson 2: Aggregate Operations

Bạn sử dụng collection để làm gì? Bạn không chỉ lưu trữ các objects trong một collection và để chúng ở đó. Trong hầu hết các trường hợp, bạn sử dụng collection để truy xuất các items được lưu trữ trong chúng.

*Ví dụ 1: Giả sử rằng bạn đang tạo một ứng dụng mạng xã hội. Bạn muốn tạo một tính năng cho administrator thực hiện bất kỳ loại hành động nào, chẳng hạn như gửi tin nhắn đến các members của ứng dụng mạng xã hội đáp ứng các tiêu chí nhất định.*

*Giả sử rằng các members của ứng dụng mạng xã hội này được đại diện bởi Person class sau:*

```java
public class Person {

    public enum Sex {
        MALE, FEMALE
    }

    String name;
    LocalDate birthday;
    Sex gender;
    String emailAddress;

    public int getAge() { ... }
    public String getName() { ... }
}
```

*In tên của tất cả các members có trong roster collection với một for-each loop:*

```java
for (Person p : roster) {
    System.out.println(p.getName());
}
```

*In tên của tất cả các members có trong roster collection nhưng với aggregate operation forEach:*

```java
roster.stream()
    .forEach(e -> System.out.println(e.getName()));
```


## 1. Pipelines and Streams

Một *pipeline* là một sequence (chuỗi) các *aggregate operations*.

*Ví dụ 2: In các thành viên nam có trong roster collection với một pipeline bao gồm các aggregate operations filter và forEach:*

```java
roster.stream()
    .filter(e -> e.getGender() == Person.Sex.MALE)
    .forEach(e -> System.out.println(e.getName()));
```

*So sánh ví dụ trên với ví dụ sau in các thành viên nam có trong roster collection với một for-each loop:*

```java
for (Person p : roster) {
    if (p.getGender() == Person.Sex.MALE) {
        System.out.println(p.getName());
    }
}
```

Một *pipeline* chứa các components sau:

- Một **source**: Đây có thể là một collection, một array, một generator function, hay một I/O channel.  
- Không hoặc nhiều **intermediate operations** (hoạt động trung gian) mà tạo ra một stream mới.  

    Một *stream* là một sequence (chuỗi) các elements. Không giống như một collection, nó không phải là một cấu trúc dữ liệu lưu trữ các elements. Thay vào đó, một stream mang các giá trị từ một source thông qua một pipeline. 
    
    *Ví dụ trên tạo một stream từ roster collection bằng cách gọi stream() method. filter() operation trả về một stream mới có chứa các elements khớp với predicate của nó (parameter của operation này), ở đây, predicate là lambda expression e -> e.getGender() == Person.Sex.MALE, nó trả về giá trị boolean true nếu gender field của object e có giá trị Person.Sex.MALE. Do đó, filter() trả về một stream chứa tất cả các thành viên nam trong roster collection.*

- Một **terminal operation** (hoạt động đầu cuối), chẳng hạn như forEach, tạo ra một non-stream result, chẳng hạn như primitive value, một collection, hoặc tạo ra một side-effect mà không phải một giá trị. 

    *Trong ví dụ trên, argument của forEach operations là lambda expression e -> System.out.println(e.getName()), nó gọi getName() trên object e. (Runtime và Java compiler suy ra rằng type của object e là Person.)*

*Ví dụ 3: Tính toán độ tuổi trung bình của tất cả các thành viên nam có trong roster collection bằng một pipeline bao gồm các aggregate operations filter, mapToInt và average:*

```java
double average = roster.stream()
    .filter(p -> p.getGender() == Person.Sex.MALE)
    .mapToInt(Person::getAge)
    .average()
    .getAsDouble();
```

**filter()** trả về một stream mới có chứa các elements khớp với predicate parameter của nó.

**mapToInt()** trả về một stream mới của type *IntStream* (là một stream chỉ chứa các integer value). Operation này áp dụng function được chỉ định trong parameter của nó cho từng elements trong một stream cụ thể. 

*Trong ví dụ này, fuction là Person::getAge, là một method reference trả về tuổi của thành viên. (Ngoài ra, bạn có thể sử dụng lambda expression e -> e.getAge().) Do đó, mapToInt operation trong ví dụ này trả về một stream chứa tuổi của tất cả các thành viên nam trong roster collection.*

**average()** operation tính toán giá trị trung bình của các elements có trong một stream của type IntStream. Nó trả về một object của type OptionalDouble. Nếu stream không chứa elements nào, thì average operation trả về một empty instance của OptionalDouble và việc gọi **getAsDouble()** method ném ra một NoSuchElementException. 

JDK chứa nhiều terminal operations như average mà trả về một giá trị bằng cách kết hợp nội dung của một stream. Các operations này được gọi là các *reduction operations*;


## 2. Differences Between Aggregate Operations and Iterators

Các *aggregate operations*, như forEach, có vẻ giống như các *iterators*. Tuy nhiên, chúng có một số điểm khác biệt cơ bản:

- **Chúng sử dụng một internal iteration**: Các aggregate operations không chứa một method như *next* để hướng dẫn chúng xử lý element tiếp theo của collection. Với *internal delegation*, ứng dụng của bạn xác định collection mà nó lặp qua, nhưng JDK xác định cách lặp qua collection. Với *external iteration*, ứng dụng của bạn xác định cả collection mà nó lặp qua và cách nó lặp qua. Tuy nhiên, external iteration chỉ có thể lặp qua tuần tự các elements của collection. Internal delegation không có giới hạn này. Nó có thể dễ dàng tận dụng lợi thế của tính toán song song, bao gồm việc chia một problem thành các subproblems, giải quyết các problems đó đồng thời và sau đó kết hợp các kết quả.

- **Chúng xử lý các elements từ một stream**: Aggregate operation xử lý các elements từ một stream, không phải trực tiếp từ một collection. Do đó, chúng còn được gọi là *stream operations*.

- **Chúng hỗ trợ hành vi dưới dạng các parameters**: Bạn có thể chỉ định lambda expression hay method reference expression làm argument cho hầu hết các aggregate operations. Điều này cho phép bạn tùy chỉnh hành vi của một aggregate operation cụ thể.