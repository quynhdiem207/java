# Lession 1. Interfaces

## 2. The Set Interface

Một *Set* là Collection không thể chứa các elements trùng lặp. Nó mô hình hóa sự trừu tượng của các "tập hợp" toán học. 

```java
public interface Set<E> extends Collection<E> {}
```

Java platform chứa 3 general-purpose Set implementations: 

- **HashSet**, lưu trữ các elements của nó trong một hash table, là cách triển khai hiệu quả nhất; tuy nhiên nó không đảm bảo về thứ tự lặp (iteration).  
- **TreeSet**, lưu trữ các elements của nó trong một red-black tree, sắp xếp thứ tự các elements của nó dựa trên giá trị của chúng; nó chậm hơn đáng kể so với HashSet.  
- **LinkedHashSet**, được triển khai dưới dạng một hash table với một linked list chạy qua nó, sắp xếp thứ tự các elements của nó dựa trên thứ tự mà chúng được chèn vào set (insertion-order). LinkedHashSet giúp users thoát khỏi việc thứ tự hỗn loạn không xác định.  

Có thể sử dụng conversion constructor để tạo ra một Set.

*Ví dụ: Giả sử bạn có một Collection c và bạn muốn tạo một Collection khác chứa các elements giống nhau nhưng đã loại bỏ tất cả các elements trùng lặp:*

```java
Collection<Type> noDups = new HashSet<Type>(c);
```

Set interface chỉ chứa các methods được kế thừa từ Collection và thêm hạn chế là các elements trùng lặp bị cấm.

Set cũng thay đổi hành vi của các *equals()* và *hashCode()* operations, cho phép các instances của Set được so sánh một cách có ý nghĩa ngay cả khi các implementation types của chúng khác nhau, 2 instances của Set là bằng nhau nếu chúng chứa các elements giống nhau.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | equals(Object o)
                  | - So sánh bằng giá trị của object được chỉ định với set này. 
                  | - Trả về true nếu object được chỉ định cũng là một set, 2 set có cùng kích thước,
                  |   và mọi elements của set được chỉ định đều được chứa trong set này.
------------------|--------------------------------------------------------------------------------
int	              | hashCode()
                  | - Trả về giá trị hash code của set này. 
                  | - Hash code của một set là tổng các hash code của các element trong set,
                  |   trong đó hash code của một null element bằng 0. 
                  | - Cho 2 set s1 và s2, nếu s1.equals(s2) == true thì s1.hashCode() == s2.hashCode() 
```

**Note**: Từ JDK 8 trở lên, bạn có thể dễ dàng tạo một Set từ một nguồn bằng các aggregate operations:

```java
c.stream().collect(Collectors.toSet()); // no duplicates
```

*Ví dụ 2: Tích lũy một collection chứa các tên vào một TreeSet:*

```java
Set<String> set = people.stream()
    .map(Person::getName)
    .collect(Collectors.toCollection(TreeSet::new));
```

Giữ nguyên thứ tự của collection ban đầu trong khi loại bỏ các element trùng lặp:

```java
Collection<Type> noDups = new LinkedHashSet<Type>(c);
```

*Ví dụ 3: Một generic method trả về một Set của cùng generic type với collection được truyền cho nó:*

```java
public static <E> Set<E> removeDups(Collection<E> c) {
    return new LinkedHashSet<E>(c);
}
```


### 3.1, Basic Operations

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int	              | size()
                  | - Trả về số lượng elements trong set này.
------------------|--------------------------------------------------------------------------------
boolean	          | isEmpty()
                  | - Trả về true nếu set này không chứa elements nào.
------------------|--------------------------------------------------------------------------------
boolean	          | contains(Object o)
                  | - Trả về true nếu set này chứa element được chỉ định.
                  |   Hay, kiểm tra set này có chứa element thỏa mãn: o==null ? e==null : o.equals(e)
------------------|--------------------------------------------------------------------------------
boolean	          | add(E e)
                  | (optional operation)
                  | - Thêm element được chỉ định vào set này nếu nó chưa có element e2 sao cho:
                  |       e==null ? e2==null : e.equals(e2)
                  | - Trả về true nếu element được thêm vào set này.
                  | - Nếu set này đã chứa element, thì lệnh gọi sẽ giữ nguyên set đó và trả về false.
                  | - Set có thể đặt ra các giới hạn về các elements có thể thêm vào set, 
                  |   và từ chối thêm bất kỳ element cụ thể nào bằng cách ném một ngoại lệ.
------------------|--------------------------------------------------------------------------------
boolean	          | remove(Object o)
                  | (optional operation)
                  | - Xóa bỏ element được chỉ định khỏi set này nếu nó có mặt.
                  |   Hay, xóa bỏ element thỏa mãn: o==null ? e==null : o.equals(e)
                  | - Trả về true nếu set này chứa element. 
                  | - Trả về false nếu set này sẽ không chứa element.
------------------|--------------------------------------------------------------------------------
Iterator<E>	      | iterator()
                  | - Trả về một iterator trên các elements trong set này. 
                  | - Các elements được trả về không theo thứ tự cụ thể nào, 
                  |   trừ khi set này là một instance của một số class cung cấp bảo đảm.
```

*Ví dụ: Chương trình sau sẽ in ra tất cả các từ riêng biệt trong argument list của nó. 2 phiên bản của chương trình này được cung cấp:*

- Sử dụng các JDK 8 aggregate operations:  

    ```java
    import java.util.*;
    import java.util.stream.*;

    public class FindDups {
        public static void main(String[] args) {
            Set<String> distinctWords = Arrays.asList(args).stream()
                .collect(Collectors.toSet()); 
            System.out.println(
                distinctWords.size() + " distinct words: " + distinctWords
            );
        }
    }
    ```  

- Sử dụng cấu trúc for-each:  

    ```java
    import java.util.*;

    public class FindDups {
        public static void main(String[] args) {
            Set<String> s = new HashSet<String>();
            for (String a : args)
                s.add(a);
            System.out.println(s.size() + " distinct words: " + s);
        }
    }
    ```  

*Chạy chương trình:*

```
Command:    java FindDups i came i saw i left
Output:     4 distinct words: [left, came, saw, i]
```

**Note**: Luôn khai báo các variables được sử dụng để lưu trữ một collection hoặc các parameters được sử dụng để truyền một collection với *interface type* của nó, chứ không phải *implementation type* của nó. Đây là một phương pháp lập trình được khuyến khích mạnh mẽ vì nó mang lại cho bạn sự linh hoạt để thay đổi các implementations chỉ bằng cách thay đổi constructor.

*Trong ví dụ trước, implementation type của Set là HashSet, không đảm bảo thứ tự của các elements trong Set. Nếu bạn muốn chương trình in danh sách các từ theo thứ tự bảng chữ cái, chỉ cần thay đổi implementation type của Set từ HashSet thành TreeSet. Việc thực hiện thay đổi này tạo ra kết quả sau:*

```
Command:    java FindDups i came i saw i left
Output:     4 distinct words: [came, i, left, saw]
```


### 3.2, Bulk Operations

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | containsAll(Collection<?> c)
                  | - Trả về true nếu set này chứa tất cả các elements của collection được chỉ định.
                  | - Quan hệ subset ("tập hợp con") trong toán học.
------------------|--------------------------------------------------------------------------------
boolean	          | addAll(Collection<? extends E> c)
                  | (optional operation)
                  | - Thêm tất cả các elements trong collection được chỉ định vào set này 
                  |   nếu chúng chưa có mặt.
                  | - Nếu collection được chỉ định cũng là một set, thì addAll sẽ sửa đổi set này
                  |   thành union của 2 set.
                  | - Trả về true nếu set này bị thay đổi sau hành động.
                  | - Quan hệ union ("hợp") trong toán học.
------------------|--------------------------------------------------------------------------------
boolean           | removeAll(Collection<?> c)
                  | (optional operation)
                  | - Xóa tất cả các elements của set này có trong collection được chỉ định.
                  | - Trả về true nếu set này bị thay đổi sau hành động.
------------------|--------------------------------------------------------------------------------
boolean           | retainAll(Collection<?> c)
                  | (optional operation)
                  | - Chỉ giữ lại trong set này các elements có trong collection được chỉ định.
                  | - Nếu collection được chỉ định cũng là một set, thì retainAll sẽ sửa đổi set này
                  |   thành intersection của 2 set.
                  | - Trả về true nếu set này bị thay đổi sau hành động.
                  | - Quan hệ intersection ("giao") trong toán học.
------------------|--------------------------------------------------------------------------------
void	          | clear()
                  | (optional operation)
                  | - Xóa tất cả các elements khỏi set này.
```

Để không sửa các set, cần phải sao chép trước khi thực hiện các bulk operations.

```java
Set<Type> union = new HashSet<Type>(s1);
union.addAll(s2);

Set<Type> intersection = new HashSet<Type>(s1);
intersection.retainAll(s2);

Set<Type> difference = new HashSet<Type>(s1);
difference.removeAll(s2);
```

*Ví dụ 1: In riêng biệt các từ trùng lặp và không trùng lặp trong argument list. Điều này có thể đạt được bằng cách tạo ra 2 set - một set chứa mọi từ trong argument list và set kia chỉ chứa các từ trùng lặp:*

```java
import java.util.*;

public class FindDups2 {
    public static void main(String[] args) {
        Set<String> uniques = new HashSet<String>();
        Set<String> dups    = new HashSet<String>();

        for (String a : args)
            if (!uniques.add(a))
                dups.add(a);

        // Destructive set-difference
        uniques.removeAll(dups);

        System.out.println("Unique words:    " + uniques);
        System.out.println("Duplicate words: " + dups);
    }
}
```

*Chạy chương trình:*

```
Command:    java FindDups2 i came i saw i left
Output:     Unique words:    [left, saw, came]
            Duplicate words: [i]
```

*Ví dụ 2: Tìm các element xuất hiện ở một trong 2 set, nhưng không được phép xuất hiện trong cả 2 set:*

```java
Set<Type> symmetricDiff = new HashSet<Type>(s1);
symmetricDiff.addAll(s2);
Set<Type> tmp = new HashSet<Type>(s1);
tmp.retainAll(s2);
symmetricDiff.removeAll(tmp);
```


### 3.3, Array Operations

Tương tự với Collection interface, Set interface cũng chứa các array operations:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
Object[]	      | toArray()
                  | - Trả về một Object array mới chứa tất cả các element trong set này.
------------------|--------------------------------------------------------------------------------
<T> T[]	          | toArray(T[] a)
                  | - Trả về một array chứa tất cả các element trong set này, 
                  |   Runtime type của array được trả về là type của array được chỉ định.
                  | - Nếu set này phù hợp với array được chỉ định, nó sẽ được trả về trong đó.
                  | - Nếu không, một array mới được cấp phát với runtime type của mảng được chỉ định, 
                  |   và kích thước của set này.
                  | - Nếu set này phù hợp với array được chỉ định và còn chỗ trống 
                  |   (tức là array có nhiều element hơn set này), 
                  |   thì các elements trong array phía sau các elements của set được đặt thành null.
                  | - Note: toArray(new Object[0]) tương tự với toArray().
```


### 3.4, Other Operations

```
Modifier and Type      |                Method and Description
-----------------------|-------------------------------------------------------
default Spliterator<E> | spliterator()
                       | - Tạo một Spliterator trên các elements trong set này.
```


### 3.5, The inherited method

Các operations được thừa kế từ *Collection* interface bao gồm:  

- stream()  
- parallelStream()  
- removeIf()  

Các methods được thừa kế từ *Iterable* interface bao gồm: forEach().

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default Stream<E> | stream()
                  | - Trả về một sequential Stream với collection này là nguồn.
                  | - Default implementation tạo một sequential Stream từ Spliterator của collection này.
------------------|--------------------------------------------------------------------------------
default Stream<E> | parallelStream()
                  | - Trả về một parallel Stream với collection này là nguồn.
                  | - Default implementation tạo một parallel Stream từ Spliterator của collection này.
------------------|--------------------------------------------------------------------------------
default boolean   | removeIf(Predicate<? super E> filter)
                  | - Xóa tất cả các elements của collection này thỏa mãn predicate đã cho.
                  | - Trả về true nếu collection này bị thay đổi sau hành động.
                  | - Predicate<T> là một functional interface có một "boolean test(T)" method.
------------------|--------------------------------------------------------------------------------
default void      | forEach(Consumer<? super T> action)
                  | - Thực hiện hành động đã cho với từng element của Iterable cho đến khi 
                  |   tất cả các elements đã được xử lý hoặc hành động ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface có một "void accept(T)" method.
```