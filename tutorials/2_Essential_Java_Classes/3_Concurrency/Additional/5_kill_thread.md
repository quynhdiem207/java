# Lession 3. Concurrency

## 6. How to Kill a Java Thread

### 6.1, Using a Flag

Hãy bắt đầu với một class mà tạo và khởi chạy một thread. Tác vụ này sẽ không tự kết thúc, vì vậy chúng tôi cần một số cách để dừng thread đó.

Chúng tôi sẽ sử dụng atomic flag cho điều đó:

```java
public class ControlSubThread implements Runnable {

    private Thread worker;
    private int interval;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ControlSubThread(int sleepInterval) {
        interval = sleepInterval;
    }
 
    public void start() {
        worker = new Thread(this);
        worker.start();
    }
 
    public void stop() {
        running.set(false);
    }

    public void run() { 
        running.set(true);
        while (running.get()) {
            try { 
                Thread.sleep(interval); 
            } catch (InterruptedException e){ 
                Thread.currentThread().interrupt();
                System.out.println(
                    "Thread was interrupted, Failed to complete operation"
                );
            }
            // do something here 
        } 
    } 
}
```

Thay vì có một vòng lặp while đánh giá một constant là true, chúng tôi đang sử dụng *AtomicBoolean* và bây giờ chúng tôi có thể start / stop thực thi bằng cách set nó thành true / false.

Sử dụng *AtomicBoolean* ngăn ngừa xung đột trong việc thiết lập và kiểm tra biến từ các thread khác nhau.


### 6.2, Interrupting a Thread

Điều gì xảy ra khi *sleep()* được set thành một khoảng thời gian dài hoặc nếu chúng ta đang chờ một khóa có thể không bao giờ được mở ra?  

Chúng ta phải đối mặt với nguy cơ bị tắc nghẽn trong một thời gian dài hoặc không bao giờ kết thúc hoàn toàn.

Chúng ta có thể tạo *interrupt()* method cho những trường hợp này:

```java
public class ControlSubThread implements Runnable {

    private Thread worker;
    private int interval;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicBoolean stopped = new AtomicBoolean(true);

    public void interrupt() {
        running.set(false);
        worker.interrupt();
    }

    boolean isRunning() {
        return running.get();
    }

    boolean isStopped() {
        return stopped.get();
    }

    public void run() {
        running.set(true);
        stopped.set(false);
        while (running.get()) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(
                    "Thread was interrupted, Failed to complete operation"
                );
            }
            // do something
        }
        stopped.set(true);
    }
}
```

Chúng ta đã thêm một interrupt() method set running flag của chúng ta thành false và gọi interrupt() method của worker thread.

Nếu thread đang ở chế độ ngủ khi điều này được gọi, sleep() sẽ exit với một InterruptedException, giống như bất kỳ blocking call nào khác.

Điều này trả về thread trở lại vòng lặp và nó sẽ exit vì running là false.