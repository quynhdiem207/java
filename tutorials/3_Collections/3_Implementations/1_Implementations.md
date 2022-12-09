# Lession 3. Implementations

Các *implementations* là các data objects được sử dụng để lưu trữ các collections, triển khai các interfaces: Set, List, Queue, Deque, Map, SortedSet, SortedMap. Bài học này mô tả các loại implementations sau:

+ **General-purpose implementations**: là các implementations được sử dụng phổ biến nhất.  
+ **Special-purpose implementations**: được thiết kế để sử dụng trong các tình huống đặc biệt, và cho thấy các đặc điểm hiệu suất, hạn chế sử dụng, hoặc hành vi không tiêu chuẩn.  
+ **Concurrent implementations**: được thiết kế để hỗ trợ high concurrency, thường là với chi phí của single-threaded performance. Các implementations này là một bộ phận của *java.util.concurrent* package.  
+ **Wrapper implementations**: được sử dụng kết hợp với các implementation types khác, thường là những general-purpose implementation types, để cung cấp chức năng được bổ sung hoặc hạn chế.  
+ **Convenience implementations**: là các mini-implementations, thường được tạo sẵn thông qua các static factory methods, cung cấp các lựa chọn thay thế thuận tiện, hiệu quả cho các general-purpose implementations cho các collections đặc biệt (ví dụ: singleton sets).  
+ **Abstract implementations**: là các skeletal implementations (triển khai khung xương) tạo điều kiện thuận lợi cho việc xây dựng các custom implementations.  

Các *general-purpose implementations* được tóm tắt như sau:

```
Interfaces | Hash table      | Resizable array | Tree            | Linked list     | Hash table
           | Implementations | Implementations | Implementations | Implementations | + Linked list
           |                 |                 |                 |                 | Implementations
-----------|-----------------|-----------------|-----------------|-----------------|---------------
Set	       | HashSet	 	 |                 | TreeSet	 	 |                 | LinkedHashSet
-----------|-----------------|-----------------|-----------------|-----------------|---------------
List	   |                 | ArrayList	   |                 | LinkedList	   |
-----------|-----------------|-----------------|-----------------|-----------------|---------------
Queue	   | 	 	 	     |                 |                 |                 | 
-----------|-----------------|-----------------|-----------------|-----------------|---------------
Deque	   |	             | ArrayDeque	   |	             | LinkedList	   |
-----------|-----------------|-----------------|-----------------|-----------------|---------------
Map	       | HashMap	 	 |                 | TreeMap         |          	   | LinkedHashMap
```

Như bạn có thể thấy trong bảng trên, Java Collections Framework cung cấp một số general-purpose implementations của các *Set*, *List*, và *Map* interfaces. Trong mỗi trường hợp, một implementation - **HashSet**, **ArrayList** và **HashMap** - rõ ràng là các implementations được sử dụng cho hầu hết các ứng dụng. Các *SortedSet* và *SortedMap* interfaces không được liệt kê trong bảng, mỗi interface này có một implementation (**TreeSet** và **TreeMap**), và được liệt kê trong các hàng Set và Map. Có 2 general-purpose *Queue* implementations - **LinkedList**, cũng là một List implementation và **PriorityQueue**, không được liệt kê trong bảng, 2 implementations này cung cấp ngữ nghĩa rất khác nhau: LinkedList cung cấp ngữ nghĩa FIFO, trong khi PriorityQueue sắp xếp thứ tự các elements của nó theo giá trị của chúng.

Mỗi general-purpose implementations cung cấp tất cả các optional operations có trong interface của nó. Tất cả đều cho phép các *null* elements, keys và values. Chúng không được đồng bộ hóa (*thread-safe*). Tất cả đều có *fail-fast iterators* giúp phát hiện sửa đổi đồng thời bất hợp pháp trong quá trình lặp, và thất bại nhanh chóng và sạch sẽ, thay vì mạo hiểm với hành vi tùy ý, không xác định tại một thời điểm không xác định trong tương lai. Tất cả đều là *Serializable* và đều hỗ trợ public Object *clone()* method.

Thực tế là các implementations này không được đồng bộ hóa thể hiện sự đứt đoạn với quá khứ: Các legacy collections (collection kế thừa) **Vector** và **Hashtable** được đồng bộ hóa. Cách tiếp cận hiện tại được thực hiện bởi vì các collections thường được sử dụng khi việc đồng bộ hóa không có lợi. Việc sử dụng như vậy bao gồm sử dụng single-threaded, sử dụng read-only và sử dụng như một phần của data object lớn hơn thực hiện đồng bộ hóa riêng của nó. Nói chung, phương pháp thiết kế API tốt là không bắt người dùng trả phí cho một tính năng mà họ không sử dụng. Hơn nữa, đồng bộ hóa không cần thiết có thể dẫn đến bế tắc trong một số trường hợp nhất định.

Nếu bạn cần các thread-safe collections, thì các synchronization wrappers, được mô tả trong phần *Wrapper Implementations*, cho phép bất kỳ collections nào được chuyển đổi thành một synchronized collection. Do đó, đồng bộ hóa là tùy chọn đối với các general-purpose implementations, trong khi đó là bắt buộc đối với các legacy implementations (triển khai kế thừa). Hơn nữa, *java.util.concurrent* package cung cấp các concurrent implementations như *BlockingQueue* interface mà extends Queue, và của *ConcurrentMap* interface mà extends Map. Các implementations này cung cấp tính đồng thời cao hơn nhiều so với các synchronized implementations đơn thuần.

Như một quy luật, bạn nên nghĩ về các interfaces, không phải các implementations. Việc lựa chọn implementation chỉ ảnh hưởng đến hiệu suất. Phong cách ưa thích là chọn implementation khi Collection được tạo và gán ngay collection mới cho một biến của interface type tương ứng (hoặc truyền collection đến một method mong đợi một argument của interface type). Bằng cách này, chương trình không trở nên phụ thuộc vào bất kỳ methods bổ sung nào trong một implementation nhất định, cho phép programmer tự do thay đổi implementation bất cứ lúc nào mà nó được đảm bảo bởi các mối quan tâm về hiệu suất hoặc chi tiết hành vi.

Sơ đồ phân cấp của các *Abstract implementations* như sau:

```
                                AbstractCollection
                                        |
    --------------------------------------------------------------------------
    |                       |                         |                      |  
AbstractSet             AbstractList             AbstractQueue               |
    |                       |                         |                      | 
    |- EnumSet              |- Vector                 |- PriorityQueue       |
    |- CopyOnWriteArraySet  |- CopyOnWriteArrayList                      ArrayDeque
    |- TreeSet              |- ArrayList
    |- HashSet              |           
          |- LinkedHashSet  |
                            |      
                  AbstractSequentialList
                            |- LinkedList

            AbstractMap
                |
                |- EnumMap
                |- WeakHashMap
                |- IdentityHashMap
                |- TreeMap
                |- HashMap
                      |
                LinkedHashMap
```


### The AbstractCollection Class

*AbstractCollection* class cung cấp một skeletal implementation (triển khai khung xương) của Collection interface.

```java
public abstract class AbstractCollection<E> extends Object
    implements Collection<E> {}
```

Để triển khai một *unmodifiable collection* (tập hợp không thể thay đổi), programmer chỉ cần mở rộng class này và cung cấp các implementations cho các `iterator` và `size` methods. (*Iterator* được trả về bởi `iterator` method phải triển khai `hasNext` và `next`.)

Để triển khai một *modifiable collection* (tập hợp có thể sửa đổi), ngoài cung cấp các implementations cho các `iterator` và `size` methods, programmer phải override thêm `add` method của class này (nếu không sẽ ném ra một UnsupportedOperationException), và *Iterator* được trả về bởi `iterator` method phải triển khai thêm `remove` method ngoài `hasNext` và `next` của nó.

Programmer nên cung cấp một no-argument, và một Collection constructor cho các Collection implementations.

*AbstractCollection* class cung cấp các implementations cho các methods sau: (abstract) `size`, `isEmpty`, `add`, `addAll`, `constains`, `constainsAll`, `remove`, `removeAll`, `retainAll`, `clear`, (abstract) `iterator`, `toArray`, `toArray`, `toString`.

Các methods khác được thừa kế từ *Iterable* và *Collection* interface bao gồm: (default) `removeIf`, (default) `spliterator`, (default) `stream`, (default) `parallelStream`, (default) `forEach`.

Ngoài ra AbstractCollection còn thừa kế các methods từ *Object* class: `equals, hashCode, clone, equals, hashCode, finalize, getClass, notify, notifyAll, wait, wait, wait`.

**Note**: 

+ `addAll` ném ra một UnsupportedOperationException nếu add không bị override.  
+ Các `remove, removeAll, retainAll, clear, removeIf` method sẽ ném một UnsupportedOperationException nếu Iterator được trả về bởi `iterator` method KHÔNG triển khai `remove` method, và collection này chứa Object được chỉ định.  