# Lession 3. Implementations

## 1. Set Implementations

Các Set implementations bao gồm: general-purpose và special-purpose implementations. Tất cả chúng đều là các subclass của *AbstractSet* class.

```java
public abstract class AbstractSet<E> extends AbstractCollection<E>
    implements Set<E> {}
```

AbstractSet class cung cấp một skeletal implementation (triển khai cốt lõi) của Set interface.

*AbstractSet* class override các `equals` và `hashCode` methods kế thừa từ *AbstractCollection* class.

Các methods mà AbstractSet hỗ trợ bao gồm: `(abstract) size, isEmpty, add, addAll, constains, constainsAll, remove, removeAll, retainAll, clear, (abstract) iterator, toArray, toArray, toString, equals, hashCode, (default) removeIf, (default) spliterator, (default) stream, (default) parallelStream, (default) forEach`.

Ngoài ra AbstractSet còn thừa kế các methods từ *Object* class: `clone, finalize, getClass, notify, notifyAll, wait, wait, wait`.


### 1.1, General-Purpose Set Implementations

Có 3 general-purpose Set implementations, bao gồm: **HashSet**, **TreeSet**, **LinkedHashSet**.

*HashSet* nhanh hơn nhiều so với *TreeSet*, nhưng không cung cấp đảm bảo về thứ tự. Nếu bạn cần sử dụng các *SortedSet* operations, hoặc nếu yêu cầu lặp theo thứ tự giá trị, hãy sử dụng *TreeSet*; nếu không, hãy sử dụng *HashSet*.

*LinkedHashSet* theo một nghĩa nào đó là trung gian giữa *HashSet* và *TreeSet*. Được triển khai dưới dạng một hash table với một linked list chạy qua nó, nó cung cấp *insertion-ordered iteration* (phép lặp theo thứ tự chèn) và nhanh gần như *HashSet*. *LinkedHashSet* implementation giúp thoát khỏi thứ tự hỗn loạn, không xác định được cung cấp bởi *HashSet* mà không làm tăng chi phí liên quan như *TreeSet*.

Tất cả các general-purpose Set implementations đều cho phép *null* element, và đều không được hỗ trợ đồng bộ hóa.

Một điều đáng lưu ý về *HashSet* là việc chọn dung lượng ban đầu quá cao có thể lãng phí cả không gian và thời gian. Mặt khác, việc chọn dung lượng ban đầu quá thấp sẽ lãng phí thời gian bằng cách sao chép data structure mỗi khi buộc phải tăng dung lượng. Nếu bạn không chỉ định dung lượng ban đầu, giá trị mặc định là 16. Dung lượng ban đầu được chỉ định bằng cách sử dụng int constructor. Dòng mã sau đây phân bổ một HashSet có dung lượng ban đầu là 64:

```java
Set<String> s = new HashSet<String>(64);
```

*HashSet* có một tham số điều chỉnh khác được gọi là *load factor*.

*LinkedHashSet* có các thông số điều chỉnh giống như *HashSet*, nhưng thời gian lặp lại không bị ảnh hưởng bởi dung lượng. *TreeSet* không có thông số điều chỉnh.

Tất cả các general-purpose Set implementations đều triển khai *Serializable* và *Clonable* interface, nên có hỗ trợ `clone()` methods như sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
Object            | clone()
                  | - Trả về một bản sao của HashSet / TreeSet instance này, bản thân các elements
                  |   không được sao chép. 
```


#### *1.1.1, HashSet*

*HashSet* là một Set implementation, nó không đảm bảo thứ tự của các elements.

```java
public class HashSet<E> extends AbstractSet<E>
    implements Set<E>, Cloneable, Serializable {}
```

Các constructors của *HashSet* bao gồm:

```
        Constructors               |                    Description
===================================|===============================================================
HashSet()                          | Tạo một empty set mới có dung lượng ban đầu mặc định (16) và 
                                   | hệ số tải - load factor mặc định (0,75). 
-----------------------------------|---------------------------------------------------------------
HashSet(Collection<? extends E> c) | Tạo một set mới chứa các elements trong collection được chỉ
                                   | định, hệ số tải mặc định (0,75) và dung lượng ban đầu đủ để
                                   | chứa các elements trong collection được chỉ định.
-----------------------------------|---------------------------------------------------------------
HashSet(int initialCapacity)       | Tạo một empty set mới có dung lượng ban đầu được chỉ định và
                                   | hệ số tải - load factor mặc định (0,75).
-----------------------------------|---------------------------------------------------------------
HashSet(                           | Tạo một empty set mới có dung lượng ban đầu và hệ số tải được 
    int initialCapacity,           | chỉ định.
    float loadFactor               | 
)                                  | 
```

*HashSet* class là một modifiable set, nó ghi đè `add` method của *AbstractSet*, cung cấp các implementations cho `size` và `iterator` methods, và thừa kế các `AbstractSet methods` còn lại.


#### *1.1.2, LinkedHashSet*

*LinkedHashSet* là một Hash table và linked list implementation của Set interface, với thứ tự lặp có thể dự đoán được, là thứ tự các elements được chèn vào set.

```java
public class LinkedHashSet<E> extends HashSet<E>
    implements Set<E>, Cloneable, Serializable {}
```

Các constructors của *LinkedHashSet* bao gồm:

```
            Constructors                 |                       Description
=========================================|=========================================================
LinkedHashSet()                          | Tạo một empty linked hash set mới với dung lượng ban đầu
                                         | mặc định (16) và load factor mặc định (0.75).
-----------------------------------------|---------------------------------------------------------
LinkedHashSet(Collection<? extends E> c) | Tạo một linked hash set mới chứa các elements giống với
                                         | collection được chỉ định.
-----------------------------------------|---------------------------------------------------------
LinkedHashSet(int initialCapacity)       | Tạo một empty linked hash set mới với dung lượng ban đầu
                                         | được chỉ định và load factor mặc định (0.75).
-----------------------------------------|---------------------------------------------------------
LinkedHashSet(                           | Tạo một empty linked hash set mới với dung lượng ban đầu
    int initialCapacity,                 | và load factor được chỉ định.
    float loadFactor                     |
)                                        |
```

*LinkedHashSet* là một subclass của *HashSet*, nó thừa kế tất cả các `HashSet methods`.


#### *1.1.3, TreeSet*

*TreeSet* là một NavigableSet implementation. Các elements được sắp xếp theo thứ tự tự nhiên của chúng hoặc bởi một Comparator được cung cấp tại thời điểm tạo set, tùy thuộc vào constructor nào được sử dụng.

```java
public class TreeSet<E> extends AbstractSet<E>
    implements NavigableSet<E>, Cloneable, Serializable {}
```

Các constructors của *TreeSet* bao gồm:

```
            Constructors                 |                       Description
=========================================|=========================================================
TreeSet()                                | Tạo một empty tree set mới, được sắp xếp theo thứ tự tự
                                         | nhiên của các elements của nó.
                                         | Tất cả các elements được chèn vào set phải implement
                                         | Comparable interface.
-----------------------------------------|---------------------------------------------------------
TreeSet(Comparator<? super E> comparator)| Tạo một empty tree set mới, được sắp xếp theo comparator
                                         | được chỉ định.
-----------------------------------------|---------------------------------------------------------
TreeSet(Collection<? extends E> c)       | Tạo một tree set mới chứa các elements trong collection 
                                         | được chỉ định, sắp xếp theo thứ tự tự nhiên của các
                                         | elements của nó.
                                         | Tất cả các elements được chèn vào set phải implement
                                         | Comparable interface.
-----------------------------------------|---------------------------------------------------------
TreeSet(SortedSet<E> s)                  | Tạo một empty tree set mới chứa các elements giống nhau,
                                         | và sử dụng thứ tự giống như sorted set đã chỉ định.
```

*TreeSet* implements **NavigableSet** - một interface extends *SortedSet* interface:

```java
public interface NavigableSet<E> extends SortedSet<E> {}
```

Ngoài các methods của *SortedSet*: tất cả các `Set operations`, `comparator`, `first`, `last`,
`headSet`, `tailSet`, `subSet`. 

*NavigableSet* interface còn hỗ trợ các operations sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
E	              | ceiling(E e)
                  | - Trả về element nhỏ nhất trong set này mà "lớn hơn hoặc bằng" element đã cho, 
                  |   hoặc null nếu không có element đó.
                  | - Ném NullPointerException nếu element được chỉ định là null, và set này không
                  |   cho phép các null elements.
------------------|--------------------------------------------------------------------------------
E	              | floor(E e)
                  | - Trả về element lớn nhất trong set này mà "nhỏ hơn hoặc bằng" element đã cho,
                  |   hoặc null nếu không có element đó.
                  | - Ném NullPointerException nếu element được chỉ định là null, và set này không
                  |   cho phép các null elements.
------------------|--------------------------------------------------------------------------------
E	              | higher(E e)
                  |  - Trả về element nhỏ nhất trong set này mà "lớn hơn" element đã cho,
                  |    hoặc giá trị null nếu không có element như vậy.
                  | - Ném NullPointerException nếu element được chỉ định là null, và set này không
                  |   cho phép các null elements.
------------------|--------------------------------------------------------------------------------
E	              | lower(E e)
                  |  - Trả về element lớn nhất trong set này mà "nhỏ hơn" element đã cho,
                  |    hoặc giá trị null nếu không có element như vậy.
                  | - Ném NullPointerException nếu element được chỉ định là null, và set này không
                  |   cho phép các null elements.
------------------|--------------------------------------------------------------------------------
E	              | pollFirst()
                  | - Truy xuất và xóa bỏ element đầu tiên (nhỏ nhất) trong set này, trả về element
                  |   bị xóa, hoặc trả về null nếu set này trống.
------------------|--------------------------------------------------------------------------------
E	              | pollLast()
                  | - Truy xuất và xóa bỏ element cuối cùng (lớn nhất) trong set này, trả về element
                  |   bị xóa, hoặc trả về null nếu set này trống.
------------------|--------------------------------------------------------------------------------
NavigableSet<E>	  | headSet(E toElement, boolean inclusive)
                  | - Trả về view một phần của set này có các elements nhỏ hơn (hoặc bằng, nếu
                  |   inclusive là true) toElement. 
------------------|--------------------------------------------------------------------------------
NavigableSet<E>	  | tailSet(E fromElement, boolean inclusive)
                  | - Trả về view một phần của set này có các elements lớn hơn (hoặc bằng, nếu
                  |   inclusive là true) toElement. 
------------------|--------------------------------------------------------------------------------
NavigableSet<E>	  | subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
                  | - Trả về view một phần của set này có các elements trong khoảng từ fromElement
                  |   đến toElement. Nếu fromElement và toElement bằng nhau, returned set là empty
                  |   trừ khi fromInclusive và toInclusive đều là true. 
------------------|--------------------------------------------------------------------------------
NavigableSet<E>	  | descendingSet()
                  | - Trả về reverse order view của các elements có trong set này.
------------------|--------------------------------------------------------------------------------
Iterator<E>	      | descendingIterator()
                  | - Trả về một iterator trên các elements trong set này, theo thứ tự giảm dần.
```

Đối với các range-view operations có các lưu ý sau:

+ Returned Set là back up của set này, vì vậy những thay đổi trong Returned Set được phản ánh trong set này và ngược lại.  
+ Returned Set hỗ trợ tất cả các set operations tùy chọn mà set này hỗ trợ.  
+ Returned Set sẽ ném một IllegalArgumentException khi cố gắng chèn một element bên ngoài phạm vi của nó.  

*TreeSet* class là một modifiable set, nó ghi đè `add` method của *AbstractSet*, cung cấp các implementations cho `size` và `iterator` methods, và thừa kế các `AbstractSet methods` còn lại. Ngoài ra, nó còn cung cấp các implementations cho toàn bộ các `NavigableSet methods`.


### 1.2, Special-Purpose Set Implementations

Có 2 special-purpose Set implementations, bao gồm: **EnumSet** và **CopyOnWriteArraySet**.


#### *1.2.1, EnumSet*

*EnumSet* là một high-performance Set implementation sử dụng cho các enum types. Tất cả các members của một enum set phải có cùng enum type. 

```java
public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E>
    implements Cloneable, Serializable {}
```

Iterator được trả về bởi iterator() method duyệt qua các elements theo thứ tự tự nhiên của chúng (thứ tự mà các enum constants được khai báo).

Các *null* elements là KHÔNG được phép. Cố chèn một null element sẽ ném NullPointerException. Tuy nhiên, cố gắng kiểm tra sự hiện diện của null element, hoặc xóa bỏ một null element sẽ hoạt động bình thường.

EnumSet không được hỗ trợ đồng bộ hóa, EnumSet có triển khai *Serializable* và *Clonable* interface, nên có hỗ trợ `clone()` methods.

```
Modifier and Type                     |                 Method and Description
--------------------------------------|------------------------------------------------------------
EnumSet<E>	                          | clone()
                                      | - Trả về một bản sao của EnumSet instance này, bản thân các
                                      |   elements không được sao chép.
```



*EnumSet* là một *modifiable Set*, nó ghi đè `add` method của *AbstractSet*, cung cấp các implementations cho `size` và `iterator` methods, và thừa kế các `AbstractSet methods` còn lại. 

Ngoài ra, EnumSet là một **abstract** object, vì vậy không thể khởi tạo instance trực tiếp thông qua constructor. Tuy nhiên EnumSet class cung cấp một số static methods giúp tạo các EnumSet instances sau:

```

Modifier and Type                     |                 Method and Description
--------------------------------------|------------------------------------------------------------
static <E extends Enum<E>> EnumSet<E> | noneOf(Class<E> elementType)
                                      | - Tạo một empty enum set với element type được chỉ định.
--------------------------------------|------------------------------------------------------------
static <E extends Enum<E>> EnumSet<E> | allOf(Class<E> elementType)
                                      | - Tạo một enum set chứa tất cả các enum constants trong enum
                                      |   type được chỉ định. 
--------------------------------------|------------------------------------------------------------
static <E extends Enum<E>> EnumSet<E> | complementOf(EnumSet<E> s)
                                      | - Tạo một enum set có cùng element type với enum set đã chỉ
                                      |   định, ban đầu chứa tất cả các enum constants của enum type
                                      |   này mà không có trong enum set đã chỉ định.
--------------------------------------|------------------------------------------------------------
static <E extends Enum<E>> EnumSet<E> | copyOf(EnumSet<E> s)
                                      | - Tạo một enum set có cùng element type với enum set được chỉ
                                      |   định, ban đầu chứa các elements giống với enum set đã chỉ
                                      |   định.
--------------------------------------|------------------------------------------------------------
static <E extends Enum<E>> EnumSet<E> | copyOf(Collection<E> c)
                                      | - Tạo một enum set từ collection được chỉ định. Nếu collection
                                      |   được chỉ định là một EnumSet instance, thì method này tương
                                      |   tự với copyOf(EnumSet). Nếu không, collection đã chỉ định
                                      |   phải chứa ít nhất 1 element là một enum constant (để xác định
                                      |   element type của enum set mới).
--------------------------------------|------------------------------------------------------------
static <E extends Enum<E>> EnumSet<E> | of(E e)
static <E extends Enum<E>> EnumSet<E> | of(E e1, E e2)
static <E extends Enum<E>> EnumSet<E> | of(E e1, E e2, E e3)
static <E extends Enum<E>> EnumSet<E> | of(E e1, E e2, E e3, E e4)
static <E extends Enum<E>> EnumSet<E> | of(E e1, E e2, E e3, E e4, E e5)
static <E extends Enum<E>> EnumSet<E> | of(E first, E... rest)
                                      | 
                                      | - Tạo một enum set ban đầu chứa các elements được chỉ định.
--------------------------------------|------------------------------------------------------------
static <E extends Enum<E>> EnumSet<E> | range(E from, E to)
                                      | - Tạo một enum set ban đầu chứa tất cả các các elements trong
                                      |   phạm vi (range) được chỉ định bởi 2 endpoints.
                                      | - Returned enum set sẽ chứa cả các endpoints.
```

*Ví dụ: Tạo một EnumSet chứa một số enum constants đại diện cho các ngày thường trong tuần:*

```java
EnumSet<DayOfWeeks> = EnumSet.range(DayOfWeeks.MONDAY, DayOfWeeks.FRIDAY);
```


#### *1.2.2, CopyOnWriteArraySet*

*CopyOnWriteArraySet* là một Set implementation được back up bởi một copy-on-write array. Tất cả mutative operations, chẳng hạn như add và remove, được triển khai bằng cách tạo một bản copy mới của array; không cần lock. Ngay cả iteration cũng có thể xử lý đồng thời một cách an toàn với việc chèn và xóa element.

*CopyOnWriteArraySet* nằm trong *java.util.concurrent* package:

```java
package java.util.concurrent;
public class CopyOnWriteArraySet<E> extends AbstractSet<E>
    implements Serializable {}
```

Không giống như hầu hết các Set implementations, các add, remove và constains methods yêu cầu thời gian tỷ lệ với kích thước của set. *CopyOnWriteArraySet* implementation này chỉ thích hợp cho các set có kích thước nhỏ, hiếm khi được sửa đổi, nhưng thường xuyên lặp. Nó rất phù hợp để lưu trữ các event-handler lists mà phải ngăn chặn duplicates.

*CopyOnWriteArraySet* là một Set mà sử dụng một *CopyOnWriteArrayList* nội bộ cho tất cả các operations của nó. Do đó, nó có các thuộc tính cơ bản sau:

+ Nó phù hợp nhất cho các ứng dụng trong đó kích thước set thường nhỏ, các read-only operations nhiều hơn rất nhiều so với các mutative operations, và bạn cần ngăn chặn sự can thiệp giữa các thread trong quá trình duyệt.  
+ Nó là thread-safe.  
+ Các mutative operations (add, remove, v.v.) rất tốn kém vì chúng thường đòi hỏi phải sao chép toàn bộ mảng.  
+ Các iterators không hỗ trợ mutative remove operations.  
+ Duyệt thông qua các iterators nhanh chóng và không thể có sự can thiệp từ các thread khác. Các iterators dựa trên các snapshots không thay đổi của mảng tại thời điểm các iterators được tạo.

Các constructor của *CopyOnWriteArraySet* bao gồm:

```
            Constructors                      |                    Description
==============================================|====================================================
CopyOnWriteArraySet()                         | Tạo một empty set. 
----------------------------------------------|----------------------------------------------------
CopyOnWriteArraySet(Collection<? extends E> c)| Tạo một set chứa tất cả các elements của collection
                                              | được chỉ định.
```

*CopyOnWriteArraySet* implements *Serializable* interface, nhưng KHÔNG implements Cloneable interface, nên nó KHÔNG hỗ trợ *clone()* method.

*CopyOnWriteArraySet* là một *modifiable Set*, nó ghi đè `add` method của *AbstractSet*, cung cấp các implementations cho `size` và `iterator` methods, và thừa kế các `AbstractSet methods` còn lại.