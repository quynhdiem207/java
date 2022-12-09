# Lession 3. Implementations

## 5. Deque Implementations

Các Deque implementations bao gồm: general-purpose và concurrent implementations. 


### 5.1, General-Purpose Deque Implementations

Các general-purpose implementations bao gồm: **LinkedList** và **ArrayDeque**. 

Deque interface hỗ trợ chèn, xóa bỏ và truy xuất các elements ở cả 2 đầu. *ArrayDeque* là resizeable array implementation của Deque interface, trong khi *LinkedList* là list implementation.

LinkedList implementation linh hoạt hơn ArrayDeque implementation. Các *null* elements được phép trong *LinkedList* implementation, nhưng KHÔNG được phép trong *ArrayDeque* implementation.

Về mặt hiệu quả, ArrayDeque hiệu quả hơn LinkedList đối với add và remove operations ở cả 2 đầu. Hoạt động tốt nhất trong LinkedList là xóa element hiện tại trong quá trình lặp. *LinkedList* không phải là cấu trúc lý tưởng để lặp.

*LinkedList* tiêu tốn nhiều bộ nhớ hơn so với *ArrayDeque*. 


#### *5.1.1, ArrayDeque*

*ArrayDeque* là một Resizable-array implementation của Deque interface. 

Các Array deques không có giới hạn về dung lượng; chúng được tự động tăng khi cần thiết để hỗ trợ việc sử dụng. 

ArrayDeque KHÔNG hỗ trợ đồng bộ hóa, KHÔNG cho phép các *null* elements. 

ArrayDeque có thể nhanh hơn Stack khi được sử dụng như một stack, và nhanh hơn LinkedList khi được sử dụng như một queue.

```java
public class ArrayDeque<E> extends AbstractCollection<E>
    implements Deque<E>, Cloneable, Serializable {}
```

Các constructors của *ArrayDeque* bao gồm:

```
            Constructors             |                       Description
=====================================|=============================================================
ArrayDeque()                         | Tạo một empty array deque với dung lượng ban đầu đủ để chứa
                                     | 16 elements.
-------------------------------------|-------------------------------------------------------------
ArrayDeque(int numElements)          | Tạo một empty array deque với dung lượng ban đầu đủ để chứa
                                     | số lượng elements được chỉ định.
-------------------------------------|-------------------------------------------------------------
ArrayDeque(Collection<? extends E> c)| Tạo một array deque chứa các elements của collection đã chỉ
                                     | định, theo thứ tự chúng được trả về bởi iterator của collection.
```

ArrayDeque hỗ trợ tất cả các `Deque operations`.


### 5.2, Concurrent Deque Implementations

**LinkedBlockingDeque** là một concurrent implementation của Deque interface. Nếu deque trống thì các methods như takeFirst và takeLast sẽ đợi cho đến khi element trở nên khả dụng, sau đó truy xuất và xóa bỏ element đó.