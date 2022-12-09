# Lession 3. Concurrency

## 7.4, Atomic Variables

*java.util.concurrent.atomic* package định nghĩa các class hỗ trợ các atomic operations trên các biến duy nhất. Tất cả các class đều có các *get* và *set* methods hoạt động như đọc và ghi trên các *volatile* variables. Nghĩa là, một *set* có mối quan hệ happens-before với bất kỳ lần *get* nào tiếp theo trên cùng một biến. Atomic *compareAndSet* method cũng có các đặc điểm nhất quán về bộ nhớ này, cũng như các atomic arithmetic methods đơn giản áp dụng cho các integer atomic variables.

*Ví dụ:*

```java
class Counter {
    private int c = 0;

    public void increment() {
        c++;
    }

    public void decrement() {
        c--;
    }

    public int value() {
        return c;
    }
}
```

*Một cách để làm cho Counter an toàn khỏi thread interference (sự can thiệp luồng) là làm cho các methods của nó được đồng bộ hóa, như trong SynchronizedCounter:*

```java
class SynchronizedCounter {
    private int c = 0;

    public synchronized void increment() {
        c++;
    }

    public synchronized void decrement() {
        c--;
    }

    public synchronized int value() {
        return c;
    }
}
```

*Đối với class đơn giản này, đồng bộ hóa là một giải pháp có thể chấp nhận được. Nhưng đối với một class phức tạp hơn, chúng ta có thể muốn tránh tác động trực tiếp của việc đồng bộ hóa không cần thiết. Thay thế int field bằng AtomicInteger cho phép chúng tôi ngăn chặn thread interference mà không cần sử dụng đến đồng bộ hóa, như trong AtomicCounter:*

```java
import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    private AtomicInteger c = new AtomicInteger(0);

    public void increment() {
        c.incrementAndGet();
    }

    public void decrement() {
        c.decrementAndGet();
    }

    public int value() {
        return c.get();
    }
}
```