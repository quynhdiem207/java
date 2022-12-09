# Lession 3. Implementations

## 4. Queue Implementations

Các Queue implementations bao gồm: general-purpose và concurrent implementations. 

Một số chúng là các subclass của *AbstractQueue* class:

```java
public abstract class AbstractQueue<E> extends AbstractCollection<E>
    implements Queue<E> {}
```

*AbstractQueue* class cung cấp một skeletal implementation (triển khai cốt lõi) của Queue interface. Các implementations trong class này thích hợp khi các implementations cơ sở không cho phép các null elements.

Một Queue implementation extends AbstractQueue phải định nghĩa tối thiểu một `Queue.offer(E)` method KHÔNG cho phép chèn các null elements, cùng với các `Queue.peek(), Queue.poll(), Collection.size(), và Collection.iterator()` methods.

AbstractQueue cung cấp các implementations cho các Queue operations sau: `element, add, remove`; và kế thừa các Queue operations sau: `(abstract) peek, (abstract) offer, (abstract) poll`. Nó cũng kế thừa tất cả các `AbstractCollection methods`.

Ngoài ra AbstractQueue cũng thừa kế các methods sau từ *Object* class: `clone, equals, finalize, getClass, hashCode, notify, notifyAll, wait, wait, wait`.


### 4.1, General-Purpose Queue Implementations

Có 2 general-purpose Queue implementations, bao gồm: **LinkedList** và **PriorityQueue**.


#### *4.1.1, LinkedList*

LinkedList triển khai Queue interface, cung cấp các FIFO queue operations.


#### *4.1.2, PriorityQueue*

*PriorityQueue* class là một priority queue dựa trên *heap* data structure. Các elements của PriorityQueue được sắp xếp theo thứ tự tự nhiên của chúng, hoặc bởi một Comparator được cung cấp tại thời điểm tạo queue, tùy thuộc vào constructor được sử dụng.

```java
public class PriorityQueue<E> extends AbstractQueue<E>
    implements Serializable {}
```

Các constructors của *PriorityQueue* bao gồm:

```
            Constructors                 |                       Description
=========================================|=========================================================
PriorityQueue()                          | Tạo một PriorityQueue với dung lượng ban đầu mặc định (11),
                                         | sắp xếp các elements theo thứ tự tự nhiên của chúng.
-----------------------------------------|---------------------------------------------------------
PriorityQueue(int initialCapacity)       | Tạo một PriorityQueue với dung lượng ban đầu được chỉ định,
                                         | sắp xếp các elements theo thứ tự tự nhiên của chúng.
-----------------------------------------|---------------------------------------------------------
PriorityQueue(                           | Tạo một PriorityQueue với dung lượng ban đầu mặc định (11),
    Comparator<? super E> comparator     | sắp xếp các elements theo Comparator được chỉ định.
)                                        |
-----------------------------------------|---------------------------------------------------------
PriorityQueue(                           | Tạo một PriorityQueue với dung lượng ban đầu được chỉ định,
    int initialCapacity,                 | sắp xếp các elements theo Comparator được chỉ định.
    Comparator<? super E> comparator     |
)                                        |
-----------------------------------------|---------------------------------------------------------
PriorityQueue(Collection<? extends E> c) | Tạo một PriorityQueue chứa các elements trong collection
                                         | được chỉ định.
                                         | Nếu collection đó là một SortedSet instance, hoặc một 
                                         | PriorityQueue khác, thì sắp xếp theo cùng một thứ tự với
                                         | collection. Nếu không, sẽ được sắp xếp theo thứ tự tự
                                         | nhiên của các elements của nó.
-----------------------------------------|---------------------------------------------------------
PriorityQueue(SortedSet<? extends E> c)  | Tạo một PriorityQueue chứa các elements trong SortedSet
                                         | đã chỉ định, được sắp xếp theo thứ tự giống như SortedSet
                                         | đã cho.
-----------------------------------------|---------------------------------------------------------
PriorityQueue(                           | Tạo một PriorityQueue chứa các elements trong PriorityQueue
    PriorityQueue<? extends E> c         | đã chỉ định, được sắp xếp theo cùng thứ tự với 
)                                        | PriorityQueue đã cho.
```

*PriorityQueue* không bị ràng buộc, nhưng một internal capacity điều chỉnh kích thước của một mảng được sử dụng để lưu trữ các elements của queue. Nó luôn lớn ít nhất bằng queue size. Khi các elements được thêm vào PriorityQueue, dung lượng của nó sẽ tự động tăng lên.

*PriorityQueue* KHÔNG cho phép các *null* elements. PriorityQueue dựa trên thứ tự tự nhiên cũng không cho phép chèn các non-comparable objects (làm như vậy có thể dẫn đến ClassCastException).

Các queue operations - poll, remove, peek và element - truy cập element ở đầu hàng đợi. Head của queue là element nhỏ nhất đối với thứ tự được chỉ định. Nếu nhiều elements được gắn với giá trị nhỏ nhất, thì head là một trong những element đó.

PriorityQueue và Iterator của nó triển khai tất cả các optional `Collection operations` và Iterator operations. Iterator được cung cấp bởi `iterator()` method KHÔNG được đảm bảo để duyệt qua các elements của PriorityQueue theo bất kỳ thứ tự cụ thể nào. Nếu muốn duyệt có thứ tự, hãy cân nhắc sử dụng `Arrays.sort(pq.toArray())`.

PriorityQueue không được đồng bộ hóa. Nếu cần hãy sử dụng *PriorityBlockingQueue*.

PriorityQueue không hỗ trợ `clone()` method, nhưng có hỗ trợ Serialization.


### 4.2, Concurrent Queue Implementations

*java.util.concurrent* package chứa một set các synchronized Queue interfaces and classes. 

**BlockingQueue** là một interface mà extends Queue interface với các operations chờ đợi queue trở thành non-empty khi truy xuất một element, và chờ đợi để có space trong queue khi lưu trữ một elements. 

BlockingQueue implementations bao gồm:

+ **LinkedBlockingQueue** — Một optionally bounded FIFO blocking queue backed bởi các linked nodes, Head của queue là element đã ở trong queue lâu nhất.  
+ **ArrayBlockingQueue** — Một bounded FIFO blocking queue được back up bởi một array. Head của queue là element đã ở trong queue lâu nhất.  
+ **PriorityBlockingQueue** — Một unbounded blocking priority queue được back up bởi một heap.  
+ **DelayQueue** — Một time-based scheduling queue được back up bởi một heap, một element chỉ có thể được thực hiện khi delay (độ trễ) của nó đã hết.  
+ **SynchronousQueue** — Một cơ chế hẹn đơn giản sử dụng BlockingQueue interface, mỗi insert operation phải chờ một remove operation tương ứng bởi một thread khác.  

Trong JDK 7, **TransferQueue** là một *BlockingQueue* chuyên biệt, trong đó mã thêm một element vào queue có tùy chọn chờ (block) mã trong một thread khác để truy xuất element. *TransferQueue* có một triển khai implementation duy nhất:  

+ **LinkedTransferQueue** — Một unbounded TransferQueue dựa trên các linked nodes.  