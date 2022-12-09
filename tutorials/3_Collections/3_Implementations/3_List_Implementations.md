# Lession 3. Implementations

## 2. List Implementations

Các List implementations bao gồm: general-purpose và special-purpose implementations. 

Một số List implementations là các subclass của *AbstractList* class.

```java
public abstract class AbstractList<E> extends AbstractCollection<E>
    implements List<E> {}
```

*AbstractList* class cung cấp một skeletal implementation (triển khai cốt lõi) của List interface, được sử dụng cho các "random access data" (như ArrayList).

Để triển khai một *unmodifiable list* (danh sách không thể thay đổi), programmer chỉ cần mở rộng class này và cung cấp các implementations cho các `get(int)` và `size()` methods.

Để triển khai một *modifiable list* (danh sách có thể sửa đổi), ngoài cung cấp các implementations cho các `get(int)` và `size()` methods, programmer phải ghi đè bổ sung `set(int, E)` method (nếu không sẽ ném ra một UnsupportedOperationException). Nếu list là variable-size (kích thước có thể thay đổi), programmer phải ghi đè bổ sung các `add(int, E)` và `remove(int)` methods.

Programmer nên cung cấp một một no-argument, và Collection constructor cho các List implementations.

*AbstractList* cung cấp implementation cho *iterator* method, ghi đè các equals, hashCode methods, và kế thừa các methods còn lại của AbstractCollection;

Nó còn cung cấp các implementation cho các List operations sau: `add(int, E), addAll(int, Collection), (abstract) get(int), remove(int), set(int, E), subList(int, int), indexOf(Object), lastIndexOf(Object), listIterator(), listIterator(int)`; và kế thừa `(default) spliterator, (default) sort và (default) replaceAll` methods từ List interface.

**Note**:  

+ `add` và `addAll` ném một UnsupportedOperationException nếu `add(int,E)` không được ghi đè.  
+ Iterator được trả về bởi `iterator` method sẽ ném một UnsupportedOperationException khi gọi `remove` method nếu `remove(int)` không bị ghi đè.  
+ clear ném một UnsupportedOperationException nếu `remove(int)` hoặc `removeRange(int, int)` không bị ghi đè.  

*AbstractList* hỗ trợ bổ sung một `removeRange(int, int)` method giúp xóa bỏ các elements có index nằm trong một phạm vi (range):

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
protected void	  | removeRange(int fromIndex, int toIndex)
                  | - Xóa bỏ khỏi list này tất cả các elements có index nằm giữa fromIndex (bao gồm)
                  |   và toIndex (không gồm).
                  | - Dịch chuyển bất kỳ element kế tiếp nào sang trái (giảm index của chúng). 
                  | - Nếu toIndex == fromIndex, operation này không có hiệu lực.
```

Ngoài ra AbstractList còn thừa kế các methods từ *Object* class: `clone, finalize, getClass, notify, notifyAll, wait, wait, wait`.


### 2.1, General-Purpose List Implementations

Có 2 general-purpose List implementations - **ArrayList** và **LinkedList**. 

*ArrayList* cung cấp khả năng truy cập vị trí. Nó không phải cấp phát một node object cho mỗi element trong List, và nó có thể tận dụng lợi thế của *System.arraycopy()* khi nó phải move nhiều elements cùng một lúc.

Nếu bạn thường xuyên thêm các elements vào đầu List hoặc lặp qua List để xóa các elements bên trong của nó, bạn nên cân nhắc sử dụng *LinkedList*.

*ArrayList* có một tham số điều chỉnh - initial capacity (dung lượng ban đầu), đề cập đến số lượng elements mà ArrayList có thể giữ. *LinkedList* không có tham số điều chỉnh.

Tất cả các general-purpose List implementations đều cho phép các *null* element, đều không hỗ trợ đồng bộ hóa, và đều triển khai *Serializable* và *Cloneable* interface nên chúng hỗ trợ `clone()` method:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
Object            | clone()
                  | - Trả về một bản sao của list này, bản thân các elements không được sao chép.
```


#### *2.1.1, ArrayList*

*ArrayList* là một Resizable-array implementation của List interface. 

```java
public class ArrayList<E> extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, Serializable {}
```

Mỗi ArrayList instance đều có một dung lượng. Dung lượng là kích thước của mảng được sử dụng để lưu trữ các elements trong list. Nó luôn lớn ít nhất bằng kích thước list. Khi các elements được thêm vào ArrayList, dung lượng của nó sẽ tự động tăng lên. 

ArrayList bao gồm các constructors sau:

```
            Constructors             |                       Description
=====================================|=============================================================
ArrayList()                          | Tạo một empty list với dung lượng ban đầu là 10.
-------------------------------------|-------------------------------------------------------------
ArrayList(Collection<? extends E> c) | Tạo một list chứa các elements của collection đã chỉ định,
                                     | theo thứ tự chúng được trả về bởi iterator của collection.
-------------------------------------|-------------------------------------------------------------
ArrayList(int initialCapacity)       | Tạo một empty list với dung lượng ban đầu được chỉ định.
```

*ArrayList* là một *modifiable list* mà extends AbstractList, nó cung cấp implementations cho `size` và `get` methods; ghi đè `set(int, E), add(int, E), remove(int)`; và kế thừa tất cả các `AbstractList methods`. 

Có thể tăng dung lượng của một ArrayList instance trước khi thêm một số lượng lớn các elements bằng cách sử dụng `ensureCapacity()` operation. Điều này có thể làm giảm sự gia tăng số lần phân bổ lại.

ArrayList cung cấp các methods để thao tác kích thước của mảng mà được sử dụng nội bộ để lưu trữ list: `ensureCapacity` và `trimToSize`.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
void	          | ensureCapacity(int minCapacity)
                  | - Tăng dung lượng của ArrayList instance này, nếu cần, để đảm bảo rằng nó có thể
                  |   chứa ít nhất số elements được chỉ định bởi minCapacity argument.
------------------|--------------------------------------------------------------------------------
void              | trimToSize()
                  | - Cắt giảm dung lượng của ArrayList instance này thành size hiện tại của list.
```


#### *2.1.2, LinkedList*

*LinkedList* là một doubly-linked list implementation của List và Deque interfaces:

```java
public class LinkedList<E> extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, Serializable {}
```

*LinkedList* bao gồm các constructors sau:

```
            Constructors             |                       Description
=====================================|=============================================================
LinkedList()                         | Tạo một empty list.
-------------------------------------|-------------------------------------------------------------
LinkedList(Collection<? extends E> c)| Tạo một list chứa các elements của collection đã chỉ định,
                                     | theo thứ tự chúng được trả về bởi iterator của collection.
```

*LinkedList* extends **AbstractSequentialList** - một subclass của *AbstractList*:

```java
public abstract class AbstractSequentialList<E> extends AbstractList<E> {}
```

*AbstractSequentialList* cung cấp một skeletal implementation (triển khai cốt lõi) của List interface, được sử dụng cho các "sequential access data" (như LinkedList).

AbstractSequentialList ghi đè `listIterator(int)` method kế thừa từ AbstractList thành **abstract**, và cung cấp implementation cho `get(int)` method:

```
Modifier and Type        |                        Method and Description
-------------------------|-------------------------------------------------------------------------
abstract ListIterator<E> | listIterator(int index)
                         | - Trả về một ListIterator trên các elements trong List này (theo thứ tự
                         |   thích hợp).
```

Để triển khai một *list* với sequential access, cần cung cấp các implementations cho các `listIterator` và `size` methods. 

Đối với một *unmodifiable list* với sequential access, programmer chỉ cần triển khai các `hasNext, next, hasPrevious, previous, nextIndex, và previousIndex` methods của ListIterator.

Đối với *modifiable list* với sequential access, programmer nên triển khai bổ sung `set` method của ListIterator. Đối với variable-size list, programmer nên triển khai bổ sung các `remove` và `add` methods của ListIterator.

*LinkedList* là một *modifiable list* extends AbstractSequentialList. Nó cung cấp implementations cho `size` và `listIterator(int)` methods; ghi đè `set(int, E), add(int, E), remove(int)`; và kế thừa tất cả các `AbstractSequentialList methods`. 

Ngoài ra, LinkedList còn triển khai *Deque* interface, nó cung cấp các implementations cho các Deque operations sau: `addFirst, addLast, offer, offerFirst, offerLast, element, getFirst, getLast, peek, peekFirst, peekLast, remove(), removeFirst, removeLast, poll, pollFirst, pollLast, pop, push, removeFirstOccurrence, removeLastOccurrence`.  


### 2.2, Special-Purpose List Implementations

Có 2 special-purpose List implementations - **CopyOnWriteArrayList** và **Vector**.


#### *2.2.1, CopyOnWriteArrayList*

*CopyOnWriteArrayList* là một List implementation được back up bởi một copy-on-write array, nó có bản chất tương tự như CopyOnWriteArraySet. Không cần đồng bộ hóa, ngay cả trong quá trình lặp và các iterators được đảm bảo không bao giờ ném ConcurrentModificationException.

*CopyOnWriteArrayList* nằm trong *java.util.concurrent* package:

```java
package java.util.concurrent;
public class CopyOnWriteArrayList<E> extends Object
    implements List<E>, RandomAccess, Cloneable, Serializable {}
```

*CopyOnWriteArrayList* bao gồm các constructors sau:

```
            Constructors             |                       Description
=====================================|=============================================================
CopyOnWriteArrayList()               | Tạo một empty list.
-------------------------------------|-------------------------------------------------------------
CopyOnWriteArrayList(                | Tạo một list chứa các elements của collection được chỉ định,
    Collection<? extends E> c        | theo thứ tự chúng được trả về bởi iterator của collection.
)                                    | 
-------------------------------------|-------------------------------------------------------------
CopyOnWriteArrayList(E[] toCopyIn)   | Tạo một list chứa một bản copy của array đã chỉ định.
```

CopyOnWriteArrayList implementation này rất phù hợp để lưu trữ các event-handler lists, trong đó sự thay đổi là không thường xuyên, và việc duyệt diễn ra thường xuyên và có khả năng tốn thời gian.

CopyOnWriteArrayList cho phép các *null* element, triển khai *Serializable* và *Cloneable* interface nên nó hỗ trợ `clone()` method.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
Object            | clone()
                  | - Trả về một bản sao của list này, bản thân các elements không được sao chép.
```

Ngoài các List operations, CopyOnWriteArrayList còn hỗ trợ các methods sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | addIfAbsent(E e)
                  | - Thêm element được chỉ định vào list này, nếu list chưa có element này.
                  |   Trả về true nếu element được thêm vào list.
------------------|--------------------------------------------------------------------------------
int	              | addAllAbsent(Collection<? extends E> c)
                  | - Thêm tất cả các elements trong collection được chỉ định mà chưa có trong list
                  |   này vào cuối list này, theo thứ tự mà chúng được trả về bởi iterator của
                  |   collection đã chỉ định.
                  | - Trả về số lượng elements được thêm vào list.
```


#### *2.2.2, Vector*

*Vector* là một Resizable-array implementation của List interface. 

```java
public class Vector<E> extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, Serializable {}
```

*LinkedList* bao gồm các constructors sau:

```
            Constructors          |                       Description
==================================|================================================================
Vector()                          | Tạo một empty vector với internal data array (capacity) là 10,
                                  | và capacity increment tiêu chuẩn là 0.
----------------------------------|----------------------------------------------------------------
Vector(Collection<? extends E> c) | Tạo một vector chứa các elements của collection đã chỉ định,
                                  | theo thứ tự chúng được trả về bởi iterator của collection.
----------------------------------|----------------------------------------------------------------
Vector(int initialCapacity)       | Tạo một empty vector với capacity được chỉ định, và capacity 
                                  | increment là 0.
----------------------------------|----------------------------------------------------------------
Vector(                           | Tạo một empty vector với capacity và capacity increment được chỉ
    int initialCapacity,          | định.
    int capacityIncrement         |
)                                 |
```

Nếu bạn cần đồng bộ hóa, *Vector* sẽ nhanh hơn một chút so với *ArrayList* được đồng bộ hóa với `Collections.synchronizedList`.

Mỗi Vector instance quản lý việc lưu trữ thông qua một capacity và một capacityIncrement. Dung lượng luôn lớn ít nhất bằng vector size. Khi các elements được thêm vào Vector, dung lượng của nó sẽ tự động tăng lên theo từng capacityIncrement. 

Có thể tăng dung lượng của một Vector trước khi thêm một số lượng lớn các elements bằng cách sử dụng `ensureCapacity()` operation. Điều này có thể làm giảm sự gia tăng số lần phân bổ lại.

Vector còn cung cấp các methods để thao tác kích thước của mảng mà được sử dụng nội bộ để lưu trữ data: `capacity, setSize, ensureCapacity, và trimToSize`.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int 	          | capacity
                  | - Trả về dung lượng hiện tại của vector này.
------------------|--------------------------------------------------------------------------------
void	          | setSize(int newSize)
                  | - Set kích thước của vector này. Nếu kích thước mới lớn hơn kích thước hiện tại,
                  |   các null items mới sẽ được thêm vào cuối vector. Nếu kích thước mới nhỏ hơn
                  |   kích thước hiện tại, tất cả các elements tại index newSize trở đi sẽ bị xóa bỏ.
------------------|--------------------------------------------------------------------------------
void	          | ensureCapacity(int minCapacity)
                  | - Tăng dung lượng của Vector này, nếu cần, để đảm bảo rằng nó có thể chứa ít nhất
                  |   số elements được chỉ định bởi minCapacity argument.
------------------|--------------------------------------------------------------------------------
void              | trimToSize()
                  | - Cắt giảm dung lượng của Vector này thành size hiện tại của vector.
```

*Vector* là một *modifiable list* mà extends AbstractList, nó cung cấp implementations cho `size` và `get` methods; ghi đè `set(int, E), add(int, E), remove(int)`; và kế thừa tất cả các `AbstractList methods`. 

Ngoài các *AbstractList* operations, Vector class còn hỗ trợ các methods sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
E	              | elementAt(int index)
                  | - Trả về element tại index được chỉ định của vector này. 
                  | - Tương tự với get(int).
------------------|--------------------------------------------------------------------------------
E	              | firstElement()
                  | - Trả về element đầu tiên (tại index 0) của vector này.
------------------|--------------------------------------------------------------------------------
E	              | lastElement()
                  | - Trả về element cuối cùng của vector này.
------------------|--------------------------------------------------------------------------------
void	          | addElement(E obj)
                  | - Thêm element được chỉ định vào cuối vector này, tăng kích thước của nó lên 1.
                  |   Dung lượng của vector sẽ tăng lên nếu size của nó lớn hơn capacity của nó.
                  | - Tương tự với add(E).
------------------|--------------------------------------------------------------------------------
void	          | insertElementAt(E obj, int index)
                  | - Chèn element được chỉ định vào vector này tại index được chỉ định. Index của
                  |   mỗi element trong vector này có index >= index được chỉ định sẽ được tăng lên 1.
                  | - index được chỉ định phải >= 0 và <= size hiện tại của vector. (Nếu index bằng
                  |   kích thước hiện tại của vectơ, element mới sẽ được nối vào Vector.)
                  | - Tương tự với add(int, E).
------------------|--------------------------------------------------------------------------------
boolean	          | removeElement(Object obj)
                  | - Xóa bỏ lần xuất hiện đầu tiên (index nhỏ nhất) của object được chỉ định khỏi
                  |   vector này.
                  | - Nếu object được tìm thấy trong vector này, index của mỗi element trong vector
                  |   có index >= index của object sẽ được giảm đi 1.
                  | - Tương tự với remove(Object).
------------------|--------------------------------------------------------------------------------
void	          | removeElementAt(int index)
                  | - Xóa element tại index được chỉ định. Index của mỗi element trong vector có 
                  |   index >= index được chỉ định sẽ được giảm đi 1. Kích thước của vector này giảm
                  |   đi 1.
                  | - Index phải >= 0 và <= size hiện tại của vector.
                  | - Tương tự với remove(int).
------------------|--------------------------------------------------------------------------------
void	          | removeAllElements()
                  | - Xóa bỏ tất cả các elements khỏi vector này, và set kích thước của nó thành 0.
                  | - Tương đương với clear() method.
------------------|--------------------------------------------------------------------------------
void	          | setElementAt(E obj, int index)
                  | - Set element tại index được chỉ định của vector này thành element được chỉ định.
                  |   Element trước đó tại vị trí đó bị xóa bỏ.
                  | - Index phải >= 0 và <= size hiện tại của vector.
                  | - Tương đương với set(int, E) method.
------------------|--------------------------------------------------------------------------------
void	          | copyInto(Object[] anArray)
                  | - Sao chép các elements của vector này vào anArray được chỉ định. Element ở
                  |   index k trong vector này được sao chép vào component k của anArray.
------------------|--------------------------------------------------------------------------------
int	              | indexOf(Object o, int index)
int	              | lastIndexOf(Object o, int index)
                  |
                  | - Trả về index của lần xuất hiện đầu tiên / cuối cùng của element được chỉ định
                  |   trong vector này, tìm kiếm ngược từ index được chỉ định,
                  |   hoặc trả về -1 nếu element không được tìm thấy.
------------------|--------------------------------------------------------------------------------
Enumeration<E>	  | elements()
                  | - Trả về một Enumeration của tất cả các elements của vector này.
                  |   Enumeration có 2 methods: boolean	hasMoreElements(), và E	nextElement().
```

Nếu List của bạn có kích thước cố định - nghĩa là bạn sẽ không bao giờ sử dụng remove, add, hoặc bất kỳ bulk operations nào khác ngoài containsAll - bạn nên xem xét sử dụng **Arrays.asList** operation.