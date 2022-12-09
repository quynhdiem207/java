# Lession 1. Interfaces

## 1. The Collection Interface

Một *Collection* đại diện cho một nhóm các object được gọi là các elements của nó. Sử dụng Collection interface khi mong muốn tính tổng quát tối đa. 

```java
public interface Collection<E> extends Iterable<E> {}
```

Theo quy ước, tất cả các general-purpose collection implementations đều có một constructor nhận Collection argument. Constructor này, được gọi là một *conversion constructor*, khởi tạo một collection mới để chứa tất cả các elements trong collection được chỉ định, bất kể subinterface hoặc implementation types của collection đã cho. Nói cách khác, nó cho phép bạn chuyển đổi type của collection.

*Ví dụ:* 

```java
// giả sử rằng bạn có một Collection<String> c, 
// có thể là một List, một Set hoặc một loại Collection. 

// Câu lệnh sau tạo ra một ArrayList mới (một implementation của List interface),
// ban đầu chứa tất cả các elements trong c.

List<String> list = new ArrayList<String>(c);
// Hoặc - nếu sử dụng JDK 7 trở lên - có thể sử dụng diamond operator
List<String> list = new ArrayList<>(c);
```

Collection interface không hề bổ sung các ràng buộc cho *Object.equals()* và *Object.hashCode()*:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | equals(Object o)
                  | - So sánh bằng reference của object được chỉ định với collection này.
                  | - Trả về true nếu cùng reference, ngược lại trả về false.
------------------|--------------------------------------------------------------------------------
int               | hashCode()
                  | - Trả về giá trị hash code của collection này.
                  | - Cho 2 collection c1 và c2, 
                  |   nếu c1.equals(c2) == true thì c1.hashCode() == c2.hashCode()
```


### 2.1, Basic Operations

Collection interface chứa các methods thực hiện các operations cơ bản:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int	              | size()
                  | - Trả về số lượng elements trong collection này.
------------------|--------------------------------------------------------------------------------
boolean	          | isEmpty()
                  | - Trả về true nếu collection này không chứa elements nào.
------------------|--------------------------------------------------------------------------------
boolean	          | contains(Object o)
                  | - Trả về true nếu collection này chứa element được chỉ định.
                  |   Hay, trả về true khi collection này chứa ít nhất một element e sao cho:
                  |       o==null ? e==null : o.equals(e)
------------------|--------------------------------------------------------------------------------
boolean	          | add(E e)
                  | (optional operation)
                  | - Đảm bảo element được chỉ định có trong collection này.
                  | - Trả về true nếu element được thêm vào collection này.
                  | - Trả về false nếu collection này không cho phép trùng lặp, 
                  |   và đã chứa element được chỉ định.
                  | - Các collections hỗ trợ operation này có thể đặt ra các giới hạn về các elements
                  |   có thể được thêm vào collection này.
                  | - Collection có thể từ chối thêm bất kỳ element cụ thể nào, 
                  |   vì bất kỳ lý do nào khác ngoài lý do nó đã chứa element đó, 
                  |   thì nó phải ném ra một ngoại lệ (thay vì trả về false).
------------------|--------------------------------------------------------------------------------
boolean	          | remove(Object o)
                  | (optional operation)
                  | - Xóa bỏ một element được chỉ định khỏi collection này nếu nó có mặt.
                  |   Hay, xóa bỏ element thỏa mãn: o==null ? e==null : o.equals(e)
                  | - Trả về true nếu set này chứa element. 
                  | - Trả về false nếu set này sẽ không chứa element.
------------------|--------------------------------------------------------------------------------
Iterator<E>	      | iterator()
                  | - Trả về một iterator trên các elements trong set này. 
                  | - Các elements được trả về không theo thứ tự cụ thể nào, 
                  |   trừ khi collection này là một instance của một số class cung cấp bảo đảm.
```


### 2.2, Bulk Operations

Nó cũng chứa các methods hoạt động trên toàn bộ collection, còn gọi là các hay *bulk operations*, hoạt động của chúng làm thay đổi collection, chẳng hạn như:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | containsAll(Collection<?> c)
                  | - Trả về true nếu collection này chứa tất cả các elements của collection được 
                  |   chỉ định.
------------------|--------------------------------------------------------------------------------
boolean	          | addAll(Collection<? extends E> c)
                  | (optional operation)
                  | - Thêm tất cả các elements trong collection được chỉ định vào collection này.
                  | - Trả về true nếu collection này bị thay đổi sau hành động.
------------------|--------------------------------------------------------------------------------
boolean           | removeAll(Collection<?> c)
                  | (optional operation)
                  | - Xóa tất cả các elements của collection này có trong collection được chỉ định.
                  | - Trả về true nếu collection này bị thay đổi sau hành động.
------------------|--------------------------------------------------------------------------------
default boolean   | removeIf(Predicate<? super E> filter)
                  | - Xóa tất cả các elements của collection này thỏa mãn predicate đã cho.
                  | - Trả về true nếu collection này bị thay đổi sau hành động.
                  | - Predicate<T> là một functional interface có một "boolean test(T)" method.
------------------|--------------------------------------------------------------------------------
boolean           | retainAll(Collection<?> c)
                  | (optional operation)
                  | - Chỉ giữ lại các elements của collection này có trong collection được chỉ định.
                  | - Trả về true nếu collection này bị thay đổi sau hành động.
------------------|--------------------------------------------------------------------------------
void	          | clear()
                  | (optional operation)
                  | - Xóa tất cả các elements khỏi collection này.
```

*Ví dụ: Xóa bỏ tất cả các instances của một element được chỉ định, e, khỏi Collection, c.*

```java
c.removeAll(Collections.singleton(e));
```

*Cụ thể hơn, giả sử bạn muốn xóa tất cả các null elements khỏi Collection:*

```java
c.removeAll(Collections.singleton(null));
```

*Collections.singleton* là một static factory method trả về một immutable *Set* chỉ chứa element xác định.


### 2.3, Array Operations

Collection interface cũng chứa các *toArray()* methods cho phép tạo một array mới từ các collections.

Các toArray methods được cung cấp như một cầu nối giữa các collections và các API cũ nhận input là các mảng. Các array operations cho phép nội dung của một Collection được dịch sang một array. 

- *toArray()* không có argument, sẽ tạo ra một Object array mới.  
- *toArray(T[])* nhận argument là một array, cho phép người gọi cung cấp một array hoặc chọn runtime type của output array.  

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
Object[]	      | toArray()
                  | - Trả về một Object array mới chứa tất cả các element trong collection này.
------------------|--------------------------------------------------------------------------------
<T> T[]	          | toArray(T[] a)
                  | - Trả về một array chứa tất cả các element trong collection này, 
                  |   Runtime type của array được trả về là type của array được chỉ định.
                  | - Nếu collection này phù hợp với array được chỉ định, nó sẽ được trả về trong đó.
                  | - Nếu không, một array mới được cấp phát với runtime type của mảng được chỉ định, 
                  |   và kích thước của collection này.
                  | - Nếu collection này phù hợp với array được chỉ định và còn chỗ trống 
                  |   (tức là array có nhiều element hơn collection này), 
                  |   thì các elements trong array phía sau của collection được đặt thành null.
                  | - Note: toArray(new Object[0]) tương tự với toArray().
```

*Ví dụ: Đưa nội dung của Collection c vào một Object array mới được cấp phát có độ dài giống với số elements trong c:*

```java
Object[] a = c.toArray();
```

*Ví dụ: Giả sử rằng c được biết là chỉ chứa các string (có lẽ vì c thuộc type `Collection<String>`). Đoạn mã sau đây đưa nội dung của c vào một String array mới được cấp phát có độ dài giống với số elements trong c:*

```java
String[] a = c.toArray(new String[0]);
```


### 2.4, Stream Operators

Trong JDK 8 trở lên, Collection interface có thêm các methods để tạo các sequential hoặc parallel streams từ các collections:  

```
Modifier and Type      |                        Method and Description
-----------------------|----------------------------------------------------------------------
default Spliterator<E> | spliterator()
                       | - Tạo một Spliterator trên các elements trong collection này.
-----------------------|----------------------------------------------------------------------
default Stream<E>      | stream()
                       | - Trả về một sequential Stream với collection này là nguồn.
                       | - Default implementation tạo một sequential Stream từ Spliterator của 
                       |   collection này.
-----------------------|----------------------------------------------------------------------
default Stream<E>      | parallelStream()
                       | - Trả về một parallel Stream với collection này là nguồn.
                       | - Default implementation tạo một parallel Stream từ Spliterator của 
                       |   collection này.
```


### 2.5, The inherited method

Ngoài ra, Collection interface cũng kế thừa default method *forEach()* từ Iterable interface:  

```
Modifier and Type |                        Method and Description
------------------|---------------------------------------------------------------------------
default void      | forEach(Consumer<? super T> action)
                  | - Thực hiện hành động đã cho với từng element của Iterable cho đến khi 
                  |   tất cả các elements đã được xử lý hoặc hành động ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface có một "void accept(T)" method.
```

*Ví dụ: In ra các elements trong một collection:*

```java
void print(Collection<?> c) {
    c.forEach(System.out::println);
}
```


### 2.6, Traversing Collections

Có 3 cách để duyệt qua các collections: 

- (1) Sử dụng các aggregate operations,  
- (2) Sử dụng cấu trúc for-each,  
- (3) Sử dụng các Iterators.  


#### *Aggregate Operations*

Trong JDK 8 trở lên, method được ưa thích để lặp qua một collection là tạo một *stream* và thực hiện các *aggregate operations* trên đó. Các aggregate operations thường được sử dụng kết hợp với các lambda expression để sử dụng ít dòng mã hơn. 

*Ví dụ 1: Đoạn mã sau lặp lại tuần tự qua một collection của các shapes và in ra các red object:*

```java
myShapesCollection.stream()
    .filter(e -> e.getColor() == Color.RED)
    .forEach(e -> System.out.println(e.getName()));
```

*Tương tự, có thể sử dụng một parallel stream nếu collection đủ lớn và máy tính đủ lõi:*

```java
myShapesCollection.parallelStream()
    .filter(e -> e.getColor() == Color.RED)
    .forEach(e -> System.out.println(e.getName()));
```

*Ví dụ 2: chuyển đổi các element của Collection thành các String object, sau đó nối chúng, phân tách bằng dấu phẩy:*

```java
String joined = elements.stream()
    .map(Object::toString)
    .collect(Collectors.joining(", "));
```

*Ví dụ 3: Tính tổng tiền lương của nhân viên:*

```java
int total = employees.stream()
    .collect(Collectors.summingInt(Employee::getSalary));
```

**Note**: Sự khác biệt giữa các *bulk operations* (như addAll, ...) với các *aggregate operations* là: Các bulk operations đều sửa đổi các collections, còn các aggregate operations thì không.  


#### *for-each Construct*

Cấu trúc *for-each* cho phép bạn duyệt một cách ngắn gọn một collection hoặc array bằng cách sử dụng vòng lặp for. 

*Ví dụ: Đoạn mã sau sử dụng cấu trúc for-each để in ra từng element của collection trên một dòng riêng biệt:*

```java
for (Object o : collection)
    System.out.println(o);
```

Hoặc có thể sử dụng *forEach()* method của Iterable interface:

```java
Collection<Integer> lst = new ArrayList<>();
lst.forEach(System.out::println);
```


#### *Iterators*

Một *Iterator* là một object cho phép bạn duyệt qua một collection, nó không đảm bảo thứ tự lặp của các elements, và cho phép xóa các elements khỏi collection trong quá trình lặp nếu muốn. 

Sở dĩ một bạn có thể duyệt qua các elements của một Collection bằng Iterator là vì Collection interface mở rộng từ Iterable interface. Triển khai *Iterable* interface cho phép object trở thành target của "for-each loop" statement. **Iterable** interface có 3 methods sau:

```
Modifier and Type      |                        Method and Description
-----------------------|---------------------------------------------------------------------------
default void           | forEach(Consumer<? super T> action)
                       | - Thực hiện hành động đã cho với từng element của Iterable cho đến khi tất 
                       |   cả các elements đã được xử lý hoặc hành động ném ra một ngoại lệ.
                       | - Consumer<T> là một functional interface có một "void accept(T)" method.
-----------------------|---------------------------------------------------------------------------
Iterator<T>	           | iterator()
                       | - Trả về một iterator trên các elements của type T.
-----------------------|---------------------------------------------------------------------------
default Spliterator<T> | spliterator()
                       | - Tạo một Spliterator trên tất cả các elements được mô tả bởi Iterable này.
```

Bạn nhận được một Iterator cho một collection bằng cách gọi *iterator()* method của nó. Sau đây là *Iterator* interface:

```java
public interface Iterator<E> {
    default void forEachRemaining(Consumer<? super E> action);
    boolean	hasNext();
    E next();
    default void remove(); // optional operation.
}
```

Chi tiết các methods của **Iterator** interface như sau:  

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default void	  | forEachRemaining(Consumer<? super E> action)
                  | - Thực hiện hành động đã cho với từng element còn lại cho đến khi tất cả các 
                  |   elements đã được xử lý hoặc hành động ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface với "void accept(T)" method.
------------------|--------------------------------------------------------------------------------
boolean	          | hasNext()
                  | - Trả về true nếu iteration có nhiều element hơn.
                  |   hay, trả về true nếu next() sẽ trả về một element thay vì ném một ngoại lệ.
------------------|--------------------------------------------------------------------------------
E	              | next()
                  | - Trả về element tiếp theo trong interation.
------------------|--------------------------------------------------------------------------------
default void	  | remove()
                  | (optional operation)
                  | - Xóa bỏ khỏi collection này element cuối cùng được trả về bởi iterator này. 
```

Nếu chỉ đơn giản là chỉ thực hiện một hành động đối với từng element cho đến khi tất cả các elements đều được xử lý hoặc hành động xảy ra ngoại lệ, bạn có thể sử dụng:  

- *forEachRemaining()* method của Iterator interface,   
- hoặc *next* method của Iterator interface để lặp, và sử dụng *hasNext* để kiểm tra điều kiện tồn tại element kế tiếp. 

*Ví dụ 1:*

```java
public static void main(String[] args) {
    Set<String> flowers = new HashSet<String>();

    flowers.add("Tulip");
    flowers.add("Daffodil");
    flowers.add("Poppy");
    flowers.add("Sunflower");
    flowers.add("Bluebell");
    
    // Iterator không đảm bảo thứ tự lặp
    Iterator<String> iterator = flowers.iterator();
    
    String flower1 = iterator.next();
    String flower2 = iterator.next();
    
    System.out.println("Flower 1: " + flower1);
    System.out.println("Flower 2: " + flower2);
    
    iterator.forEachRemaining(System.out::println);
}

// Output:  Flower 1: Poppy
//          Flower 2: Tulip
//          Daffodil
//          Sunflower
//          Bluebell
```

*Ví dụ 2:*

```java
public static void main(String[] args) {
    List<String> flowers = new ArrayList<String>();

    flowers.add("Tulip");
    flowers.add("Daffodil");
    flowers.add("Poppy");
    flowers.add("Sunflower");
    flowers.add("Bluebell");
    
    Iterator<String> iterator = flowers.iterator();
    
    while(iterator.hasNext()) {
        String flower = iterator.next();
        System.out.println(flower);
    }
}

// Output:  Tulip
//          Daffodil
//          Poppy
//          Sunflower
//          Bluebell
```

Nếu muốn xóa element hiện tại ra khỏi collection trong quá trình lặp, bạn không thể sử dụng các cấu trúc for-each, vì nó đã ẩn đi iterator, vì vậy cần sử dụng Iterator object. Tuy nhiên cần lưu ý, nếu type của Collection không cho phép hoạt động này, một UnsupportedOperationException sẽ được ném ra.

```java
static void filter(Collection<Integer> c) {
    for (Iterator<Integer> it = c.iterator(); it.hasNext(); ) {
        Integer current = it.next();
        if(current % 2 == 0) {
            it.remove(); // Remove current element.
        }
    }
}
```