# Lession 3. Concurrency

## 2. Thread Objects

Mỗi thread được liên kết với một instance của class *Thread*. Có 2 chiến lược cơ bản cho việc sử dụng các Thread objects để tạo một ứng dụng đồng thời (concurrent application):

- Để trực tiếp kiểm soát việc tạo và quản lý thread, chỉ cần khởi tạo Thread mỗi khi ứng dụng cần bắt đầu các tác vụ bất đồng bộ.  
- Để quản lý abstract thread từ phần còn lại của ứng dụng của bạn, truyền các tác vụ của ứng dụng cho một executor (trình thực thi).  


### 2.1, Defining and Starting a Thread

Một ứng dụng tạo một instance của Thread phải cung cấp mã sẽ chạy trong thread đó. Có 2 cách để làm điều này:

- Cung cấp một *Runnable* object cho Thread constructor.  

    *Runnable* interface định nghĩa một method duy nhất, run, chứa mã được thực thi trong thread.  

    ```java
    public class HelloRunnable implements Runnable {
        public void run() {
            System.out.println("Hello from a thread!");
        }

        public static void main(String args[]) {
            (new Thread(new HelloRunnable())).start();
        }
    }
    ```

- Sử dụng một subclass của Thread, bản thân Thread class đã triển khai *Runnable* interface, mặc dù run() method của nó không thực hiện bất cứ điều gì, bạn có thể cung cấp triển khai cho run() method của subclass:  
    
    ```java
    public class HelloThread extends Thread {
        public void run() {
            System.out.println("Hello from a thread!");
        }

        public static void main(String args[]) {
            (new HelloThread()).start();
        }
    }
    ```

**Note**: gọi **start()** method trên một thread object để bắt đầu khởi chạy một thread mới.

Nên sử dụng cách nào trong 2 cách trên? Cách đầu tiên, sử dụng một Runnable object, tổng quát hơn, vì Runnable object có thể tách thành một class khác với Thread. Cách thứ hai dễ sử dụng hơn trong các ứng dụng đơn giản, nhưng bị hạn chế bởi thực tế là class tác vụ của bạn phải là một subclass của Thread. 

Bài học này tập trung vào cách đầu tiên, cách này tách tác vụ Runnable khỏi Thread object thực thi tác vụ. Cách tiếp cận này không chỉ linh hoạt hơn mà còn có thể áp dụng cho các API quản lý high-level thread.

Thread class định nghĩa một số methods hữu ích cho việc quản lý thread. Chúng bao gồm các static methods, cung cấp thông tin về, hoặc ảnh hưởng đến trạng thái của thread đang gọi method. Các methods khác được gọi từ các threads khác liên quan đến việc quản lý thread và Thread object.


### 2.2, Pausing Execution with Sleep

*Thread.sleep()* khiến thread hiện tại tạm ngừng thực thi trong một khoảng thời gian được chỉ định. Đây là một phương tiện hiệu quả để cung cấp thời gian của bộ xử lý (processor time) cho các threads khác của một ứng dụng hoặc các ứng dụng khác có thể đang chạy trên hệ thống máy tính. *sleep()* method cũng có thể được sử dụng để tạo nhịp độ, và chờ một thread khác với các nhiệm vụ được hiểu là có yêu cầu về thời gian.

Có 2 phiên bản overload của *sleep()* được cung cấp: một phiên bản chỉ định sleep time tính bằng miliseconds và một phiên bản chỉ định sleep time tính bằng nanoseconds. Tuy nhiên, những sleep time này không được đảm bảo chính xác, vì chúng bị giới hạn bởi các tiện ích được cung cấp bởi hệ điều hành. Ngoài ra, sleep time có thể bị chấm dứt bởi các interrupts (ngắt / sự gián đoạn). Trong mọi trường hợp, bạn không thể giả định rằng việc gọi sleep sẽ tạm dừng thread trong một khoảng thời gian chính xác được chỉ định.

*Ví dụ: SleepMessages sử dụng sleep() để in một tin nhắn sau mỗi khoảng thời gian 4 giây:*

```java
public class SleepMessages {
    public static void main(String args[]) throws InterruptedException {
        String importantInfo[] = {
            "Mares eat oats",
            "Does eat oats",
            "Little lambs eat ivy",
            "A kid will eat ivy too"
        };

        for (int i = 0; i < importantInfo.length; i++) {
            Thread.sleep(4000); // Pause for 4 seconds
            System.out.println(importantInfo[i]); // Print a message
        }
    }
}
```

**Note**: main khai báo rằng nó ném *InterruptedException*. Đây là một ngoại lệ mà *sleep* ném khi một thread khác làm gián đoạn thread hiện tại trong khi sleep đang hoạt động. 

*Vì ứng dụng trên chưa định nghĩa một thread khác để gây ra ngắt nên nó không bận tâm đến việc bắt InterruptedException.*


### 2.3, Interrupts

Một *interrupt* (ngắt) là một dấu hiệu cho một thread rằng nó nên dừng những gì nó đang làm để làm một việc khác. Programmer quyết định chính xác cách một thread phản hồi với một interrupt như thế nào, nhưng việc kết thúc thread là rất phổ biến.

Một thread gửi một interrupt bằng cách gọi *interrupt()* trên Thread object bị ngắt. 

```java
t.interrupt(); // ngắt t thread
```

Để cơ chế ngắt hoạt động chính xác, thread bị ngắt phải hỗ trợ ngắt của chính nó.


#### *Supporting Interruption*

Làm thế nào để một thread hỗ trợ sự gián đoạn của chính nó? Điều này phụ thuộc vào những gì nó hiện đang làm. Nếu thread thường xuyên gọi các methods ném *InterruptedException*, nó chỉ đơn giản trả về từ *run()* method sau khi bắt ngoại lệ đó. 

*Ví dụ: giả sử vòng lặp thông báo trong ví dụ SleepMessages nằm trong run method của Runnable object của một thread. Để hỗ trợ ngắt, nó có thể được sửa đổi như sau:*

```java
for (int i = 0; i < importantInfo.length; i++) {
    try {
        Thread.sleep(4000); // Pause for 4 seconds
    } catch (InterruptedException e) {
        // We've been interrupted: no more messages.
        return;
    }
    System.out.println(importantInfo[i]); // Print a message
}
```

Nhiều method ném *InterruptedException*, chẳng hạn như sleep, được thiết kế để hủy hoạt động hiện tại của chúng và trả về ngay lập tức khi nhận được một interrupt.

Điều gì sẽ xảy ra nếu một thread trong một thời gian dài mà không gọi một method ném InterruptedException? Lúc này, nó phải định kỳ gọi *Thread.interrupted* method mà trả về true nếu nhận được một interrupt.

*Ví dụ:*

```java
for (int i = 0; i < inputs.length; i++) {
    heavyCrunch(inputs[i]);
    if (Thread.interrupted()) {
        // We've been interrupted: no more crunching.
        return;
    }
}
```

Trong ví dụ đơn giản trên, mã chỉ đơn giản là kiểm tra ngắt và thoát thread nếu nó đã được nhận. Trong các ứng dụng phức tạp hơn, sẽ hợp lý hơn nếu ném một InterruptedException, điều này cho phép mã xử lý ngắt được đặt riêng trong một *catch* clause:

```java
if (Thread.interrupted()) {
    throw new InterruptedException();
}
```


#### *The Interrupt Status Flag*

Cơ chế ngắt được triển khai bằng cách sử dụng flag nội bộ được gọi là *interrupt status* (trạng thái ngắt). Gọi *Thread.interrupt()* sẽ thiết lập flag này. Khi một thread kiểm tra ngắt bằng cách gọi static method *Thread.interrupted()*, interrupt status sẽ bị xóa. Non-static method *isInterrupt()* được sử dụng bởi một thread để truy vấn interrupt status của một thread khác, không thay đổi interrupt status flag.

Theo quy ước, bất kỳ methods nào thoát ra bằng cách ném một InterruptException sẽ xóa interrupt status khi nó làm như vậy. Tuy nhiên, luôn có khả năng interrupt status sẽ ngay lập tức được thiết lập lại bởi một thread khác gọi *interrupt()*.


### 2.4, Joins

*join()* method cho phép một thread chờ một thread khác hoàn thành thực thi.

Nếu t là một Thread object hiện tại đang thực thi, việc gọi join() trên t sẽ khiến thread hiện tại tạm dừng thực thi cho đến khi thread của t kết thúc.

```java
t.join();
```

Overload của *join* cho phép programmer chỉ định khoảng thời gian chờ. Tuy nhiên, giống như với *sleep*, *join* phụ thuộc vào hệ điều hành để xác định thời gian, vì vậy bạn không nên cho rằng join sẽ đợi chính xác khoảng thời gian mà bạn chỉ định.

Giống như *sleep*, *join* phản hồi với một interrupt bằng cách thoát ra với một *InterruptedException*.


### 2.5, The Simple Threads Example

*Ví dụ: SimpleThreads bao gồm 2 thread. Đầu tiên là main thread mà mọi ứng dụng Java đều có. Main thread tạo một thread mới từ Runnable object, MessageLoop, và đợi nó kết thúc. Nếu MessageLoop thread mất quá nhiều thời gian để kết thúc, main thread sẽ ngắt nó.*

*MessageLoop thread in ra một loạt tin nhắn. Nếu bị gián đoạn trước khi nó in tất cả các tin nhắn, MessageLoop thread sẽ in một tin nhắn và thoát ra.*

```java
public class SimpleThreads {

    // Hiển thị một message, đứng trước bởi tên của thread hiện tại
    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    private static class MessageLoop implements Runnable {
        public void run() {
            String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too"
            };
            try {
                for (int i = 0; i < importantInfo.length; i++) {
                    Thread.sleep(4000); // Pause for 4 seconds
                    threadMessage(importantInfo[i]); // Print a message
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }
    }

    public static void main(String args[]) throws InterruptedException {

        // Delay, tính bằng milliseconds trước khi ngắt MessageLoop thread (default 1h).
        long patience = 1000 * 60 * 60;

        // Nếu có command line argument, tính thời gian delay bằng seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // lặp cho đến khi MessageLoop thread thoát ra.
        while (t.isAlive()) {
            threadMessage("Still waiting...");
            // Chờ tối đa 1 second để MessageLoop thread kết thúc.
            t.join(1000);
            if (
                ((System.currentTimeMillis() - startTime) > patience) 
                && t.isAlive()
            ) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                // Chắc không lâu -- đợi vô thời hạn
                t.join();
                System.out.println("--------------");
            }
        }
        threadMessage("Finally!");
    }
}
```

*Chạy chương trình trên, truyền cho chương trình đối số thời gian delay là 6 seconds, output được in ra sẽ như sau:*

```
main: Starting MessageLoop thread
main: Waiting for MessageLoop thread to finish
main: Still waiting...
main: Still waiting...
main: Still waiting...
main: Still waiting...
Thread-0: Mares eat oats
main: Still waiting...
main: Still waiting...
main: Tired of waiting!
Thread-0: I wasn't done!
--------------
main: Finally!
```

*Chạy chương trình trên, không truyền đối số cho chương trình, output được in ra sẽ như sau:*

```
main: Starting MessageLoop thread
main: Waiting for MessageLoop thread to finish
main: Still waiting...
main: Still waiting...
main: Still waiting...
main: Still waiting...
Thread-0: Mares eat oats
main: Still waiting...
main: Still waiting...
main: Still waiting...
main: Still waiting...
Thread-0: Does eat oats
main: Still waiting...
main: Still waiting...
main: Still waiting...
main: Still waiting...
Thread-0: Little lambs eat ivy
main: Still waiting...
main: Still waiting...
main: Still waiting...
main: Still waiting...
Thread-0: A kid will eat ivy too
main: Finally!
```
