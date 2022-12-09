# Lession 1. Interfaces

## 4. The Queue Interface

Một *Queue* là một collection để lưu giữ các elements trước khi xử lý. 

```java
public interface Queue<E> extends Collection<E> {}
```

Bên cạnh các thao tác Collection cơ bản, Queue cung cấp thêm các thao tác chèn, xóa bỏ và kiểm tra. Mỗi method này tồn tại ở 2 dạng: 

- Ném ra một ngoại lệ nếu operation không thành công,   
- Trả về giá trị đặc biệt (null hoặc false, tùy thuộc vào operation).  

Dạng sau của thao tác chèn được thiết kế đặc biệt để sử dụng với các Queue implementations bị giới hạn dung lượng.

```
	    | Throws exception	| Returns special value
--------|-------------------|-------------------------
Insert	|   add(e)	        |   offer(e)
--------|-------------------|-------------------------
Remove	|   remove()	    |   poll()
--------|-------------------|-------------------------
Examine	|   element()	    |   peek()
```

Queue thường, nhưng không nhất thiết, sắp xếp các elements theo cách FIFO. Trong số các trường hợp ngoại lệ có **priority queue** (hàng đợi ưu tiên), sắp xếp thứ tự các elements theo giá trị của chúng. Bất kể thứ tự nào được sử dụng, element nằm ở đầu của queue sẽ bị xóa bỏ bởi lệnh gọi *remove()* hoặc *poll()*. Trong *FIFO queue*, tất cả các elements mới được chèn vào cuối queue. Các loại queues khác có thể sử dụng các quy tắc vị trí khác nhau. Mọi queue implementations phải chỉ định các thuộc tính sắp xếp của nó.

Queue implementation có thể hạn chế số lượng elements mà nó nắm giữ; Các queues như vậy được gọi là *bounded*. Một số Queue implementations trong *java.util.concurrent* bị giới hạn, nhưng các implementations trong *java.util* thì không.

Queue implementation thường không cho phép chèn các *null* elements, **LinkedList** implementation là một ngoại lệ, nó cho phép các null elements, nhưng bạn không nên lợi dụng điều này, vì null được sử dụng như một giá trị trả về đặc biệt bởi các *poll* và *peek* methods để chỉ ra rằng queue trống.

Queue implementation thường không định nghĩa các element-based version của các *equals()* và *hashCode()* methods, mà thay vào đó nó kế thừa các identity-based version từ *Object*.

Queue interface không định nghĩa các blocking queue methods mà thường gặp trong lập trình đồng thời. Các methods này được định nghĩa trong *java.util.concurrent.BlockingQueue* mà extends *Queue*.

Chi tiết các methods được bổ sung và ghi đè như sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | add(E e)
                  | - Chèn element được chỉ định vào queue này nếu không vi phạm giới hạn dung lượng. 
                  | - Trả về true khi thành công, 
                  |   hoặc ném IllegalStateException nếu hiện không còn dung lượng.
------------------|--------------------------------------------------------------------------------
boolean	          | offer(E e)
                  | - Chèn element được chỉ định vào queue này nếu không vi phạm giới hạn dung lượng. 
                  | - Trả về true khi thêm thành công, trả về false khi không còn dung lượng. 
                  | - Khi sử dụng queue giới hạn dung lượng, method này thường được ưu tiên hơn add(E).
------------------|--------------------------------------------------------------------------------
E                 | remove()
                  | - Truy xuất và xóa bỏ element đầu tiên của queue này. 
                  | - Trả về element bị xóa bỏ, hoặc ném ra một ngoại lệ nếu queue này trống.
------------------|--------------------------------------------------------------------------------
E                 | poll()
                  | - Truy xuất và xóa bỏ element đầu tiên của queue này. 
                  | - Trả về element bị xóa bỏ, hoặc trả về null nếu queue này trống.
------------------|--------------------------------------------------------------------------------
E                 | element()
                  | - Trả về element đầu tiên của queue này, 
                  |   hoặc ném ra một ngoại lệ nếu queue này trống.
------------------|--------------------------------------------------------------------------------
E                 | peek()
                  | - Trả về element đầu tiên của queue này, hoặc trả về null nếu queue này trống.
```

Các methods được kế thừa bao gồm:

- Các methods được kế thừa từ *java.util.Collection* interface:  

    + size,  
    + isEmpty,  
    + contains,  
    + remove,  
    + iterator,  
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

- Các methods được kế thừa từ *java.lang.Iterable* interface: forEach

*Ví dụ 1: Sử dụng một queue để triển khai đồng hồ đếm ngược. Queue được tải trước với tất cả các giá trị nguyên từ một số được chỉ định từ command line đến 0, theo thứ tự giảm dần. Sau đó, các giá trị được xóa khỏi hàng đợi và được in trong khoảng thời gian một giây. Ví dụ này chỉ với mục đích minh họa queue:*

```java
import java.util.*;

public class Countdown {
    public static void main(String[] args) throws InterruptedException {
        int time = Integer.parseInt(args[0]);
        Queue<Integer> queue = new LinkedList<Integer>();

        for (int i = time; i >= 0; i--)
            queue.add(i);

        while (!queue.isEmpty()) {
            System.out.println(queue.remove());
            Thread.sleep(1000);
        }
    }
}
```

*Ví dụ 2: một priority queue được sử dụng để sắp xếp một tập hợp các phần tử. Ví dụ này chỉ với mục đích minh họa queue:*

```java
static <E> List<E> heapSort(Collection<E> c) {
    Queue<E> queue = new PriorityQueue<E>(c);
    List<E> result = new ArrayList<E>();

    while (!queue.isEmpty())
        result.add(queue.remove());

    return result;
}
```