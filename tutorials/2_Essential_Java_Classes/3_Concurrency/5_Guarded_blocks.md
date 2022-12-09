# Lession 3. Concurrency

## 5. Guarded Blocks

Các threads thường phải phối hợp các hành động của chúng với nhau. Cách thức phối hợp phổ biến nhất là *guarded block*. Một block như vậy bắt đầu bằng cách kiểm tra một điều kiện phải đúng trước khi khối có thể tiến hành. Có một số bước cần làm theo để thực hiện việc này một cách chính xác.

*Ví dụ: Giả sử GuardedJoy là một method không được tiến hành cho đến khi một biến joy được chia sẻ đã được thiết lập bởi một thread khác. Về lý thuyết, một method như vậy có thể lặp lại đơn giản cho đến khi điều kiện được thỏa mãn, nhưng vòng lặp đó rất lãng phí, vì nó thực thi liên tục trong khi chờ đợi.*

```java
public void guardedJoy() {
    // Simple loop guard. Wastes processor time. Don't do this!
    while(!joy) {}
    System.out.println("Joy has been achieved!");
}
```

Một guard hiệu quả hơn bằng cách gọi *Object.wait* để tạm ngưng thread hiện tại. Lời gọi *wait()* sẽ không trả về cho đến khi một thread khác đưa ra thông báo rằng một số sự kiện đặc biệt có thể đã xảy ra - mặc dù không nhất thiết là sự kiện mà thread này đang đợi:

```java
public synchronized void guardedJoy() {
    // This guard only loops once for each special event,
    // which may not be the event we're waiting for.
    while(!joy) {
        try {
            wait();
        } catch (InterruptedException e) {}
    }
    System.out.println("Joy and efficiency have been achieved!");
}
```

**Note**: Luôn gọi *wait()* bên trong một vòng lặp kiểm tra điều kiện đang được chờ đợi. Đừng cho rằng interrupt (ngắt) là vì điều kiện cụ thể mà bạn đang chờ đợi hoặc điều kiện đó vẫn đúng.

Giống như nhiều method tạm ngừng thực thi, *wait* có thể ném *InterruptedException*. 

*Trong ví dụ này, chúng ta có thể bỏ qua ngoại lệ đó - chỉ quan tâm đến giá trị của joy.*

Tại sao GuardedJoy được đồng bộ hóa? Giả sử d là object mà chúng ta đang sử dụng để gọi wait(). Khi một thread gọi d.wait(), nó phải sở hữu khóa nội tại cho d - nếu không sẽ xảy ra lỗi. Gọi wait bên trong một synchronized method là một cách đơn giản để có được khóa nội tại.

Khi wait được gọi, thread sẽ giải phóng khóa và tạm ngừng thực thi. Vào một thời điểm nào đó trong tương lai, một thread khác sẽ có được cùng một khóa và gọi *Object.notifyAll*, thông báo cho tất cả các thread đang chờ trên khóa đó rằng điều gì đó quan trọng đã xảy ra:

```java
public synchronized notifyJoy() {
    joy = true;
    notifyAll();
}
```

Một thời gian sau, khi thread thứ hai đã giải phóng khóa, thread đầu tiên yêu cầu lại khóa và tiếp tục bằng cách trở lại từ lời gọi của *wait*.

**Note**: Có một method thông báo thứ hai, *notify*, đánh thức một thread duy nhất. Bởi vì notify không cho phép bạn chỉ định thread được đánh thức, nó chỉ hữu ích trong các ứng dụng song song lớn - tức là các chương trình có số lượng threads lớn, tất cả đều thực hiện các công việc tương tự. Trong một ứng dụng như vậy, bạn không cần quan tâm thread nào được đánh thức.

*Ví dụ: Sử dụng các guarded block để tạo một ứng dụng Producer-Consumer. Loại ứng dụng này chia sẻ dữ liệu giữa 2 threads: producer, tạo ra dữ liệu và consumer, thực hiện điều gì đó với nó. 2 threads giao tiếp bằng cách sử dụng một object được chia sẻ. Sự phối hợp là điều cần thiết: consumer thread không được cố gắng truy xuất dữ liệu trước khi producer thread đã cung cấp nó và producer thread không được cố gắng cung cấp dữ liệu mới nếu consumer chưa truy xuất dữ liệu cũ.*

*Trong ví dụ này, dữ liệu là một chuỗi các tin nhắn văn bản, được chia sẻ thông qua một object của type Drop:*

```java
public class Drop {
    // Message sent from producer to consumer.
    private String message;

    // True if consumer should wait for producer to send message,
    // false if producer should wait for consumer to retrieve message.
    private boolean empty = true;

    public synchronized String take() {
        // Wait until message is available.
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        empty = true; // Toggle status.
        notifyAll(); // Notify producer that status has changed.
        return message;
    }

    public synchronized void put(String message) {
        // Wait until message has been retrieved.
        while (!empty) {
            try { 
                wait();
            } catch (InterruptedException e) {}
        }
        empty = false; // Toggle status.
        this.message = message; // Store message.
        notifyAll(); // Notify consumer that status has changed.
    }
}
```

*Producer thread, được định nghĩa trong Producer, sẽ gửi một loạt các messages. String "DONE" cho biết rằng tất cả các tin nhắn đã được gửi đi. Để mô phỏng tính chất không thể đoán trước của các ứng dụng trong thế giới thực, producer thread tạm dừng trong khoảng thời gian ngẫu nhiên giữa các messages:*

```java
import java.util.Random;

public class Producer implements Runnable {
    private Drop drop;

    public Producer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        String importantInfo[] = {
            "Mares eat oats",
            "Does eat oats",
            "Little lambs eat ivy",
            "A kid will eat ivy too"
        };
        Random random = new Random();

        for (int i = 0; i < importantInfo.length; i++) {
            drop.put(importantInfo[i]);
            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {}
        }
        drop.put("DONE");
    }
}
```

*Consumer thread, được định nghĩa trong Consumer, chỉ cần truy xuất các tin nhắn và in chúng ra, cho đến khi nó truy xuất string "DONE". Thread này cũng tạm dừng trong khoảng thời gian ngẫu nhiên:*

```java
import java.util.Random;

public class Consumer implements Runnable {
    private Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();
        for (
            String message = drop.take();
            !message.equals("DONE");
            message = drop.take()
        ) {
            System.out.format("MESSAGE RECEIVED: %s%n", message);
            try {
                Thread.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {}
        }
    }
}
```

*Cuối cùng, đây là main thread, được định nghĩa trong ProducerConsumerExample, khởi chạy producer và consumer thread:*

```java
public class ProducerConsumerExample {
    public static void main(String[] args) {
        Drop drop = new Drop();
        (new Thread(new Producer(drop))).start();
        (new Thread(new Consumer(drop))).start();
    }
}
```

**Note**: Drop class được viết để minh họa các guarded blocks. Để tránh tái phát sinh wheel (vòng xoay), hãy xem xét các cấu trúc dữ liệu hiện có trong Java Collections Framework trước khi cố gắng viết mã các object chia sẻ dữ liệu của riêng bạn.