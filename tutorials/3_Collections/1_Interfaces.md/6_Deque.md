# Lession 1. Interfaces

## 5. The Deque Interface

Một *Deque* là một double-ended-queue. Một double-ended-queue là một linear collection (tập hợp tuyến tính) của các elements hỗ trợ việc chèn và xóa bỏ các elements ở cả 2 điểm cuối. 

```java
public interface Deque<E> extends Queue<E> {}
```

**Note**: Deque interface có thể được sử dụng làm cả LIFO stacks và FIFO queues. 

Hầu hết các Deque implementations không đặt giới hạn cố định về số lượng elements mà chúng có thể chứa, nhưng interface này hỗ trợ cả các deques bị giới hạn dung lượng cũng như các deques không giới hạn kích thước cố định.

Các Deque implementations được định nghĩa trước như **ArrayDeque** và **LinkedList**.

Mặc dù các Deque implementation không yêu cầu nghiêm ngặt rằng cấm chèn các *null* elements, nhưng bạn không nên lợi dụng điều này, vì null được sử dụng như một giá trị trả về đặc biệt bởi các methods để chỉ ra rằng deque trống.

Deque interface, định nghĩa các methods để truy cập các elements ở cả 2 đầu của Deque instances. Các methods được cung cấp để chèn, loại bỏ và kiểm tra các elements. Mỗi method này tồn tại ở 2 dạng:

- Ném ra một ngoại lệ nếu operation không thành công,   
- Trả về giá trị đặc biệt (null hoặc false, tùy thuộc vào operation).  

Dạng sau của thao tác chèn được thiết kế đặc biệt để sử dụng với các Deque implementations bị giới hạn dung lượng.

```
        |       First Element (Head)	   |        Last Element (Tail)
--------|------------------|---------------|------------------|---------------
        | Throws exception | Special value | Throws exception | Special value
========|==================|===============|==================|===============
Insert  |	addFirst(e)	   | offerFirst(e) |	addLast(e)	  | offerLast(e)
--------|------------------|---------------|------------------|---------------
Remove  |	removeFirst()  | pollFirst()   |	removeLast()  |	pollLast()
--------|------------------|---------------|------------------|---------------
Examine |	getFirst()	   | peekFirst()   |    getLast()	  | peekLast()
```

Deque extends Queue interface. Khi một deque được sử dụng làm queue, hành vi FIFO sẽ dẫn đến kết quả: Các elements được thêm vào cuối deque và xóa bỏ từ đầu deque. Các methods được kế thừa từ Queue interface chính xác tương đương với các Deque methods như được chỉ ra trong bảng sau:


```
    Queue Method    |	Equivalent Deque Method
--------------------|-----------------------------
    add(e)          |       addLast(e)
    offer(e)        |       offerLast(e)
    remove()        |       removeFirst()
    poll()          |       pollFirst()
    element()       |       getFirst()   
    peek()          |       peekFirst()
```

Deques cũng có thể được sử dụng làm LIFO stacks. Khi một deque được sử dụng như một stack, các elements được push và pop từ đầu deque. Các stack methods chính xác tương đương với các Deque methods như được chỉ ra trong bảng dưới đây:

```
    Stack Method    |	Equivalent Deque Method
--------------------|-----------------------------
    push(e)         |       addFirst(e)
    pop()           |       removeFirst()
    peek()          |       peekFirst()
```

Deque implementation thường không định nghĩa các element-based version của các *equals()* và *hashCode()* methods, mà thay vào đó nó kế thừa các identity-based version từ *Object*.

Deque interface cung cấp 2 methods để xóa các elements bên trong deque: *removeFirstOccurrence()* và *removeLastOccurrence()*.

Chi tiết các methods được bổ sung và ghi đè như sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int	              | size()	        
                  | - Trả về số lượng elements trong deque này.
------------------|--------------------------------------------------------------------------------
boolean	          | contains(Object o)
                  | - Trả về true nếu deque này chứa element được chỉ định, hay thỏa mãn:
                  |       o==null ? e==null : o.equals(e)
------------------|--------------------------------------------------------------------------------
E                 | element()
                  | - Trả về element đầu tiên của queue được đại diện bởi deque này, 
                  |   hoặc ném một ngoại lệ nếu deque này trống.
                  | - Tương đương với getFirst().
------------------|--------------------------------------------------------------------------------
E                 | peek()
                  | - Trả về element đầu tiên của queue được đại diện bởi deque này, 
                  |   hoặc trả về null nếu deque này trống.
                  | - Tương đương với peekFirst().
------------------|--------------------------------------------------------------------------------
boolean	          | add(E e)
                  | - Thêm element được chỉ định vào queue được đại diện bởi deque này
                  |   (thêm vào cuối deque này) nếu không vi phạm giới hạn dung lượng. 
                  | - Trả về true khi thành công, 
                  |   và ném IllegalStateException nếu hiện không có dung lượng. 
                  | - Khi sử dụng deque bị giới hạn dung lượng, nên sử dụng offer().
                  | - Tương đương với addLast(E).
------------------|--------------------------------------------------------------------------------
boolean	          | offer(E e)
                  | - Thêm element được chỉ định vào queue được đại diện bởi deque này
                  |   (thêm vào cuối deque này) nếu không vi phạm giới hạn dung lượng.
                  | - Trả về true khi thành công, trả về false khi không còn dung lượng.
                  | - Khi sử dụng deque bị giới hạn dung lượng, method này thích hợp hơn add(E).
                  | - Tương đương với offerLast(E). 
------------------|--------------------------------------------------------------------------------
E                 | remove()
                  | - Truy xuất và xóa element đầu tiên của queue được đại diện bởi deque này.
                  | - Ném ra một ngoại lệ nếu deque này trống.
                  | - Tương đương với removeFirst().
------------------|--------------------------------------------------------------------------------
E                 | poll()
                  | - Truy xuất và xóa element đầu tiên của queue được đại diện bởi deque này.
                  | - Trả về null nếu deque này trống.
                  | - Tương đương với pollFirst().
------------------|--------------------------------------------------------------------------------
void              | push(E e)
                  | - Đẩy một element lên stack được đại diện bởi deque này (thêm vào đầu deque này)
                  |   nếu không vi phạm các giới hạn dung lượng. 
                  | - Ném IllegalStateException nếu hiện không có dung lượng.
                  | - Tương đương với addFirst(E).
------------------|--------------------------------------------------------------------------------
E                 | pop()
                  | - Lấy một element từ stack được đại diện bởi deque này (xóa element đầu deque này)
                  |   nếu không vi phạm các giới hạn dung lượng. 
                  | - Ném IllegalStateException nếu hiện không có dung lượng.
                  | - Tương đương với removeFirst().
------------------|--------------------------------------------------------------------------------
E                 | getFirst()
                  | - Trả về element đầu tiên của deque này, hoặc ném một ngoại lệ nếu deque này trống.
------------------|--------------------------------------------------------------------------------
E                 | getLast()
                  | - Trả về element cuối cùng của deque này, hoặc ném một ngoại lệ nếu deque này trống.
------------------|--------------------------------------------------------------------------------
E                 | peekFirst()
                  | - Trả về element đầu tiên của deque này, hoặc trả về null nếu deque này trống.
------------------|--------------------------------------------------------------------------------
E                 | peekLast()
                  | - Trả về element cuối cùng của deque này, hoặc trả về null nếu deque này trống.
------------------|--------------------------------------------------------------------------------
void	          | addFirst(E e)
                  | - Thêm element được chỉ định vào đầu của deque này nếu không vi phạm giới hạn 
                  |   dung lượng. 
                  | - Ném IllegalStateException nếu hiện không có dung lượng. 
                  | - Khi sử dụng deque bị giới hạn dung lượng, nên sử dụng offerFirst(E) method.
------------------|--------------------------------------------------------------------------------
void	          | addLast(E e)
                  | - Thêm element được chỉ định vào cuối của deque này nếu không vi phạm giới hạn 
                  |   dung lượng. 
                  | - Ném IllegalStateException nếu hiện không có dung lượng. 
                  | - Khi sử dụng deque bị giới hạn dung lượng, nên sử dụng offerLast(E) method.
------------------|--------------------------------------------------------------------------------
boolean	          | offerFirst(E e)
                  | - Thêm element được chỉ định vào đầu của deque này nếu không vi phạm giới hạn 
                  |   dung lượng. 
                  | - Trả về true khi thành công, trả về false khi không còn dung lượng.
                  | - Khi sử dụng deque bị giới hạn dung lượng, method này thích hợp hơn addFirst(E).
------------------|--------------------------------------------------------------------------------
boolean	          | offerLast(E e)
                  | - Thêm element được chỉ định vào cuối của deque này nếu không vi phạm giới hạn 
                  |   dung lượng. 
                  | - Trả về true khi thành công, trả về false khi không còn dung lượng.
                  | - Khi sử dụng deque bị giới hạn dung lượng, method này thích hợp hơn addLast(E).
------------------|--------------------------------------------------------------------------------
E	              | removeFirst() 
                  | - Truy xuất và xóa element đầu tiên của deque này. 
                  | - Ném ra một ngoại lệ nếu deque này trống.  
------------------|--------------------------------------------------------------------------------
E	              | removeLast()
                  | - Truy xuất và xóa element cuối cùng của deque này. 
                  | - Ném ra một ngoại lệ nếu deque này trống.  
------------------|--------------------------------------------------------------------------------
E	              | pollFirst()
                  | - Truy xuất và xóa element đầu tiên của deque này. 
                  | - Trả về null nếu deque này trống.  
------------------|--------------------------------------------------------------------------------
E	              | pollLast()
                  | - Truy xuất và xóa element cuối cùng của deque này. 
                  | - Trả về null nếu deque này trống.  
------------------|--------------------------------------------------------------------------------
boolean	          | removeFirstOccurrence(Object o)
                  | - Xóa bỏ lần xuất hiện đầu tiên của element được chỉ định khỏi deque này sao cho:
                  |       o==null ? e==null : o.equals(e) 
                  |   Nếu deque không chứa element, nó không thay đổi.
                  | - Trả về true nếu deque bị thay đổi do kết quả của lệnh gọi, 
                  |   trả về false nếu deque không bị thay đổi.
------------------|--------------------------------------------------------------------------------
boolean	          | removeLastOccurrence(Object o)
                  | - Xóa bỏ lần xuất hiện cuối cùng của element được chỉ định khỏi deque này sao cho:
                  |       o==null ? e==null : o.equals(e) 
                  |   Nếu deque không chứa element, nó không thay đổi.
                  | - Trả về true nếu deque bị thay đổi do kết quả của lệnh gọi, 
                  |   trả về false nếu deque không bị thay đổi.
------------------|--------------------------------------------------------------------------------
boolean           | remove(Object o)
                  | - Xóa bỏ lần xuất hiện đầu tiên của element được chỉ định khỏi deque này sao cho:
                  |       o==null ? e==null : o.equals(e) 
                  |   Nếu deque không chứa element, nó không thay đổi.
                  | - Trả về true nếu deque bị thay đổi do kết quả của lệnh gọi, 
                  |   trả về false nếu deque không bị thay đổi.
------------------|--------------------------------------------------------------------------------
Iterator<E>	      | iterator()
                  | - Trả về một iterator trên các elements trong deque này theo thứ tự thích hợp,
                  |   từ element đầu tiên đến element cuối cùng.
------------------|--------------------------------------------------------------------------------
Iterator<E>	      | descendingIterator()
                  | - Trả về một iterator trên các elements trong deque này theo thứ tự thích hợp,
                  |   từ element cuối cùng đến element đầu tiên.
```

Các methods được kế thừa bao gồm:

- Các methods được kế thừa từ java.util.Collection interface:  

    + isEmpty,  
    + containsAll,  
    + addAll,  
    + removeAll,  
    + removeIf,  
    + retainAll,  
    + clear,  
    + equals,  
    + hashCode,  
    + spliterator,  
    + stream,  
    + parallelStream,  
    + toArray,  
    + toArray.  

- Các methods được kế thừa từ java.lang.Iterable interface: forEach
