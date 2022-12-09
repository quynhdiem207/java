# Lession 3. Concurrency

## 2. The Thread.join() Method

Ngoài *wait()* và *notify()* methods, *join()* là một cơ chế khác của đồng bộ hóa liên luồng (inter-thread synchronization).

Có 3 signature của join() method được định nghĩa trong Thread class:  

```java
// Chờ đến khi thread này chết.  
public final void join() throws InterruptedException  

// Chờ tối đa mili miliseconds để thread này chết.
// Thời gian chờ bằng 0 có nghĩa là phải chờ đợi mãi mãi.  
public final void join(long millis) throws InterruptedException  

// Chờ tối đa mili miliseconds cộng với nano nanoseconds để thread này chết.  
public final void join(long millis,int nanos) throws InterruptedException  
```


### 2.1, The Thread.join() Method

Khi *join()* method trên một thread, thread đang gọi sẽ chuyển sang trạng thái chờ. Nó vẫn ở trạng thái chờ đợi cho đến khi thread được tham chiếu kết thúc.

```java
class SampleThread extends Thread {
    public int processingCount = 0;

    SampleThread(int processingCount) {
        this.processingCount = processingCount;
        LOGGER.info("Thread Created");
    }

    @Override
    public void run() {
        LOGGER.info("Thread " + this.getName() + " started");
        while (processingCount > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.info("Thread " + this.getName() + " interrupted");
            }
            processingCount--;
        }
        LOGGER.info("Thread " + this.getName() + " exiting");
    }
}

@Test
public void givenStartedThread_whenJoinCalled_waitsTillCompletion() 
    throws InterruptedException 
{
    Thread t2 = new SampleThread(1);
    t2.start();
    LOGGER.info("Invoking join");
    t2.join();
    LOGGER.info("Returned from join");
    assertFalse(t2.isAlive());
}
```

*Sau khi thực thi code, output như sau:*

```
INFO: Thread Created
INFO: Invoking join
INFO: Thread Thread-1 started
INFO: Thread Thread-1 exiting
INFO: Returned from join
```

*join()* method cũng có thể trả về nếu thread được tham chiếu bị ngắt (interrupt). Trong trường hợp này, method ném ra một InterruptedException.

Nếu thread được tham chiếu đã bị kết thúc hoặc chưa được bắt đầu, thì lời gọi tới *join()* sẽ trả về ngay lập tức.

```java
Thread t1 = new SampleThread(0);
t1.join();  //returns immediately
```


### 2.2, Thread.join() Methods with Timeout

Thread gọi *join()* sẽ tiếp tục chờ nếu thread được tham chiếu bị chặn (blocked) hoặc mất quá nhiều thời gian để xử lý. Điều này có thể trở thành một vấn đề vì thread gọi join() sẽ trở nên không phản hồi. Để xử lý những tình huống này, chúng ta sử dụng các overloaded versions của join() cho phép chỉ định khoảng thời gian chờ.

```java
@Test
public void givenStartedThread_whenTimedJoinCalled_waitsUntilTimedout()
    throws InterruptedException 
{
    Thread t3 = new SampleThread(10);
    t3.start();
    t3.join(1000);
    assertTrue(t3.isAlive());
}
```

*Trong ví dụ trên, thread gọi join() đợi khoảng 1 giây để thread t3 kết thúc. Nếu thread t3 không kết thúc trong khoảng thời gian này, join() method trả về quyền điều khiển cho method gọi nó.*

Timed join() phụ thuộc vào hệ điều hành để định thời gian. Vì vậy, chúng ta không thể giả định rằng join() sẽ đợi khoảng thời gian chính xác như đã chỉ định.


### 2.3, Thread.join() Methods and Synchronization

Ngoài việc chờ đợi, việc gọi *join()* có tác dụng đồng bộ hóa. join() tạo ra một mối quan hệ happens-before: “Tất cả các hành động trong một thread xảy ra trước (happen-before) bất kỳ thread nào khác trả về thành công từ một join() method trên thread đó.”

Điều này có nghĩa là khi một thread t1 gọi t2.join(), thì tất cả các thay đổi được thực hiện bởi t2 sẽ hiển thị trong t1 khi trả về. Tuy nhiên, nếu chúng tôi không gọi join() hoặc sử dụng các cơ chế đồng bộ hóa khác, chúng tôi không có bất kỳ đảm bảo nào rằng các thay đổi trong thread khác sẽ hiển thị với thread hiện tại ngay cả khi thread khác đã hoàn thành.

Do đó, mặc dù lời gọi join() method tới một thread ở terminated state trả về ngay lập tức, chúng ta vẫn cần gọi nó trong một số trường hợp.

*Ví dụ: synchronized code không đúng cách:*

```java
SampleThread t4 = new SampleThread(10);
t4.start();

// không được đảm bảo dừng ngay cả khi t4 kết thúc.
do {
       
} while (t4.processingCount > 0);
```

Để đồng bộ hóa đúng đoạn mã trên, chúng ta có thể thêm timed t4.join() vào bên trong vòng lặp hoặc sử dụng một số cơ chế đồng bộ hóa khác.
