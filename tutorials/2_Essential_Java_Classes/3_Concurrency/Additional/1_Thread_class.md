
# Lession 3. Concurrency

## 1, Thread class

Một *thread* là một luồng thực thi trong một chương trình. JVM cho phép một ứng dụng có nhiều luồng thực thi chạy đồng thời.

```java
public class Thread extends Object implements Runnable {}
```


### 1.1, Các static nested types của Thread class

#### *static enum Thread.State*

Static enum *Thread.State* định nghĩa các state của một thread:

- **NEW**: Một thread đã được tạo nhưng chưa được start.  
- **RUNNABLE**: Một thread đang thực thi trong JVM.  
- **BLOCKED**: Một thread bị chặn, đang chờ một monitor lock.  
- **WAITING**: Một thread đang chờ vô thời hạn một thread thực hiện một hành động cụ thể.  
- **TIMED_WAITING**: Một thread đang chờ một thread khác thực hiện một hành động trong thời gian được chỉ định.  
- **TERMINATED**: Một thread đã kết thúc.  

Một thread chỉ có thể ở một state tại một thời điểm nhất định. Các state này là JVM state, không phản ánh bất kỳ state thread nào của hệ điều hành.

![Sơ đồ thread state](./Life_cycle_of_a_Thread_in_Java.jpg).


1. NEW  

    Một NEW thread (hoặc một Born Thread) là một thread đã được tạo nhưng chưa khởi chạy. Nó vẫn ở trạng thái này cho đến khi chúng ta khởi chạy nó bằng *start()* method.

    ```java
    Runnable runnable = new NewState();
    Thread t = new Thread(runnable);
    Log.info(t.getState()); // NEW
    ```  

2. RUNNABLE  

    Khi chúng ta đã tạo một thread mới và gọi *start()* method trên nó, nó sẽ chuyển từ trạng thái NEW sang trạng thái RUNNABLE. Các thread ở trạng thái này đang chạy hoặc sẵn sàng chạy, nhưng chúng đang chờ cấp phát tài nguyên từ hệ thống.  

    Trong môi trường đa luồng, Thread-Scheduler (là một phần của JVM) phân bổ một lượng thời gian cố định cho mỗi thread. Vì vậy, nó chạy trong một khoảng thời gian cụ thể, sau đó chuyển quyền điều khiển cho các RUNNABLE thread khác.  

    ```java
    Runnable runnable = new NewState();
    Thread t = new Thread(runnable);
    t.start();
    Log.info(t.getState()); // RUNNABLE
    ```  

    *Lưu ý rằng trong ví dụ này, không phải lúc nào cũng đảm bảo rằng vào thời điểm điều khiển của chúng ta đạt đến t.getState(), nó vẫn ở trạng thái RUNNABLE. Nó có thể đã được lập lịch ngay lập tức bởi Thread-Scheduler và có thể kết thúc quá trình thực thi. Trong những trường hợp như vậy, chúng ta có thể nhận được một output khác.*  

3. BLOCKED  

    Một thread ở trạng thái BLOCKED khi nó hiện không đủ điều kiện để chạy. Nó đi vào trạng thái này khi nó đang chờ monitor lock và đang cố gắng truy cập vào một phần mã bị khóa bởi một số thread khác.  

    ```java
    public class BlockedState {
        public static void main(String[] args) throws InterruptedException {
            Thread t1 = new Thread(new DemoThreadB());
            Thread t2 = new Thread(new DemoThreadB());
            
            t1.start();
            t2.start();
            
            Thread.sleep(1000);
            
            Log.info(t2.getState()); // BLOCKED
            System.exit(0);
        }
    }

    class DemoThreadB implements Runnable {
        @Override
        public void run() {
            commonResource();
        }
        
        public static synchronized void commonResource() {
            while(true) {
                // Infinite loop để bắt trước xử lý nặng
                // 't1' won't leave this method when 't2' try to enter this
            }
        }
    }
    ```  

    *Trong ví dụ trên:*

    - *Chúng tôi đã tạo 2 thread khác nhau - t1 và t2*,  
    - *t1 khởi chạy và nhập synchronized method commonResource(); điều này có nghĩa là chỉ một thread có thể truy cập nó; tất cả các thread tiếp theo khác cố gắng truy cập vào method này sẽ bị chặn khỏi quá trình thực thi tiếp theo cho đến khi thread hiện tại kết thúc quá trình xử lý,*  
    - *Khi t1 nhập vào method này, nó được giữ trong một vòng lặp while vô hạn; đây chỉ là để bắt chước xử lý nặng để tất cả các thread khác không thể nhập method này,*  
    - *Bây giờ khi chúng ta bắt đầu t2, nó sẽ cố gắng nhập method commonResource (), method này đã được truy cập bởi t1, do đó, t2 sẽ được giữ ở trạng thái BLOCKED.*  

4. WAITING  

    Một thread đang ở trạng thái WAITING khi nó đang đợi một số thread khác thực hiện một hành động cụ thể. Theo JavaDocs, bất kỳ thread nào cũng có thể ở vào trạng thái này bằng cách gọi bất kỳ 1 trong 3 methods sau:  

    - object.wait()  
    - thread.join()  
    - LockSupport.park()  

    Lưu ý rằng khi gọi wait() và join() không chỉ định bất kỳ khoảng thời gian chờ nào.  

    ```java
    public class WaitingState implements Runnable {
        public static Thread t1;

        public static void main(String[] args) {
            t1 = new Thread(new WaitingState());
            t1.start();
        }

        public void run() {
            Thread t2 = new Thread(new DemoThreadWS());
            t2.start();

            try {
                t2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.error("Thread interrupted", e);
            }
        }
    }

    class DemoThreadWS implements Runnable {
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.error("Thread interrupted", e);
            }
            
            Log.info(WaitingState.t1.getState()); // WAITING
        }
    }
    ```  

    *Trong ví dụ trên:*

    - *Chúng tôi đã tạo và khởi chạy t1,*  
    - *t1 tạo ra một t2 và khởi chạy nó,*  
    - *Trong khi quá trình xử lý của t2 tiếp tục, chúng tôi gọi t2.join(), điều này đặt t1 ở trạng thái WAITING cho đến khi t2 kết thúc quá trình thực thi,*  
    - *Vì t1 đang đợi t2 hoàn thành, chúng tôi đang gọi t1.getState () từ t2.*  

5. TIMED_WAITING  

    Một thread ở trạng thái TIMED_WAITING khi nó đang đợi một thread khác thực hiện một hành động cụ thể trong một khoảng thời gian quy định.  

    Theo JavaDocs, có 5 cách để đặt một thread ở trạng thái TIMED_WAITING:  

    - Thread.sleep(long millis),  
    - wait(int timeout) hoặc wait(int timeout, int nanos),  
    - thread.join(long millis),  
    - LockSupport.parkNanos,  
    - LockSupport.parkUntil,  

    ```java
    public class TimedWaitingState {
        public static void main(String[] args) throws InterruptedException {
            DemoThread obj1 = new DemoThread();
            Thread t1 = new Thread(obj1);
            t1.start();
            
            // sleep sẽ cung cấp đủ thời gian cho ThreadScheduler
            // để bắt đầu xử lý của thread t1
            Thread.sleep(1000);
            Log.info(t1.getState()); // TIMED_WAITING
        }
    }

    class DemoThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.error("Thread interrupted", e);
            }
        }
    }
    ```  

    *Trong ví dụ trên, chúng tôi đã tạo và khởi chạy một thread t1 được đưa vào trạng thái ngủ với khoảng thời gian chờ là 5 giây;*

6. TERMINATED  

    Đây là trạng thái của một dead thread. Nó ở trạng thái TERMINATED khi nó đã kết thúc quá trình thực thi hoặc đã bị chấm dứt bất thường.  

    ```java
    public class TerminatedState implements Runnable {
        public static void main(String[] args) throws InterruptedException {
            Thread t1 = new Thread(new TerminatedState());
            t1.start();
            // sleep method sẽ cung cấp đủ thời gian cho thread t1 để hoàn thành
            Thread.sleep(1000);
            Log.info(t1.getState()); // TERMINATED
        }
        
        @Override
        public void run() {
            // No processing in this block
        }
    }
    ```  

    Ngoài thread state, chúng ta có thể sử dụng *isAlive()* method để xác định xem thread còn sống hay không:  

    ```java
    Assert.assertFalse(t1.isAlive()); // false
    ```  

    Tóm lại, một thread vẫn sống khi và chỉ khi nó đã được khởi chạy và chưa chết.  


#### *static interface Thread.UncaughtExceptionHandler*

*Thread.UncaughtExceptionHandler* là một functional interface cho các handlers được gọi khi một thread đột ngột kết thúc do một uncaught exception.

Khi một thread sắp kết thúc do một uncaught exception, JVM sẽ truy vấn thread cho *UncaughtExceptionHandler* của nó bằng cách sử dụng method *Thread.getUncaughtExceptionHandler()* của Thread class, và sẽ gọi *uncaughtException* method của handler, truyền thread và exception làm đối số. Nếu một thread chưa được thiết lập rõ ràng UncaughtExceptionHandler, thì ThreadGroup object của nó sẽ hoạt động như UncaughtExceptionHandler của nó. Nếu ThreadGroup object không có yêu cầu đặc biệt nào để xử lý ngoại lệ, thì nó có thể chuyển tiếp lời gọi đến default uncaught exception handler mà được thiết lập bởi static method *setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler eh)* của Thread class.

*Thread.UncaughtExceptionHandler* interface chứa một method là: 

```java
/**
 * Method được gọi khi thread t đã cho kết thúc vì một uncaught exception e đã cho.
 * Bất kỳ ngoại lệ nào được ném ra bởi method này sẽ bị JVM bỏ qua.
 */
void uncaughtException(Thread t, Throwable e); 
``` 


### 1.2, Các constants của Thread class

```
            Fields                    |               Description
--------------------------------------|---------------------------------------------
public static final int MAX_PRIORITY  | Độ ưu tiên tối đa của thread.
public static final int MIN_PRIORITY  | Độ ưu tiên tối thiểu của một thread.
public static final int NORM_PRIORITY | Độ ưu tiên mặc định được gán cho một thread.
```


### 1.3, Các static methods của Thread class

```java
Thread()
Thread(Runnable target)
Thread(Runnable target, String name)
Thread(String name)
Thread(ThreadGroup group, Runnable target)
Thread(ThreadGroup group, Runnable target, String name)
Thread(ThreadGroup group, Runnable target, String name, long stackSize)
Thread(ThreadGroup group, String name)
```


### 1.4, Các static methods của Thread class

```
            Methods                 |               Description
------------------------------------|--------------------------------------------------------------
int	    activeCount()               | Trả về ước tính số thread đang hoạt động trong thread group
                                    | của thread hiện tại và các subgroups của nó.
------------------------------------|--------------------------------------------------------------
Thread	currentThread()             | Trả về tham chiếu tới thread object đang thực thi.
------------------------------------|--------------------------------------------------------------
int	    enumerate(Thread[] tarray)  | Sao chép vào mảng được chỉ định mọi thread đang hoạt động trong
                                    | thread group của thread hiện tại và các subgroups của nó.
------------------------------------|--------------------------------------------------------------
Map<Thread,StackTraceElement[]>	    | Trả về một map của các stack traces cho tất cả các live threads.
        getAllStackTraces()         |
------------------------------------|--------------------------------------------------------------
boolean	holdsLock(Object obj)       | Trả về true nếu và chỉ khi thread hiện tại giữ monitor lock 
                                    | trên object được chỉ định.
------------------------------------|--------------------------------------------------------------
boolean	interrupted()               | Kiểm tra xem thread hiện tại có bị ngắt hay không.
------------------------------------|--------------------------------------------------------------
void	sleep(long millis)          | Làm cho thread hiện đang thực thi ở trạng thái ngủ (tạm ngừng
void	sleep(long millis,int nanos)| thực thi) trong số miliseconds được chỉ định (cộng với số 
                                    | nanoseconds được chỉ định), tùy thuộc vào độ chính xác của 
                                    | timers và schedulers của system.
------------------------------------|--------------------------------------------------------------
void	yield()                     | Một gợi ý cho schedulers rằng thread hiện tại sẵn sàng nhường 
                                    | quyền sử dụng processor hiện tại của nó.
------------------------------------|--------------------------------------------------------------
void	dumpStack()                 | In một stack trace của thread hiện tại vào standard error stream.
------------------------------------|----------|---------------------------------------------------
void	setDefaultUncaughtExceptionHandler(    | Set default handler được gọi khi một thread đột 
            Thread.UncaughtExceptionHandler eh | ngột kết thúc do một uncaught exception và chưa có 
        )                                      | handler nào khác được định nghĩa cho thread đó.
-----------------------------------------------|---------------------------------------------------
Thread.UncaughtExceptionHandler                | Trả về default handler được gọi khi một thread đột
    getDefaultUncaughtExceptionHandler()       | ngột kết thúc do một uncaught exception.
                                     
```


### 1.5, Các non-static methods của Thread class

```
            Methods                 |               Description
------------------------------------|--------------------------------------------------------------
void	checkAccess()               | Xác định xem thread hiện đang chạy có quyền sửa đổi thread này
                                    | hay không.
------------------------------------|--------------------------------------------------------------
ClassLoader	getContextClassLoader() | Trả về context ClassLoader cho thread này.
------------------------------------|--------------------------------------------------------------
long	getId()                     | Trả về identifier của thread này.
------------------------------------|--------------------------------------------------------------
String	getName()                   | Trả về tên của thread này.
------------------------------------|--------------------------------------------------------------
int	    getPriority()               | Trả về độ ưu tiên của thread này.
------------------------------------|--------------------------------------------------------------
Thread.State	getState()          | Trả về state của thread này.
------------------------------------|--------------------------------------------------------------
ThreadGroup	    getThreadGroup()    | Trả về thread group mà thread này thuộc về.
------------------------------------|--------------------------------------------------------------
void	interrupt()                 | Ngắt thread này.
------------------------------------|--------------------------------------------------------------
boolean	isAlive()                   | Kiểm tra xem thread này còn sống hay không.
------------------------------------|--------------------------------------------------------------
boolean	isDaemon()                  | Kiểm tra xem thread này có phải là một daemon thread hay không.
------------------------------------|--------------------------------------------------------------
boolean	isInterrupted()             | Kiểm tra xem thread này có bị ngắt hay không.
------------------------------------|--------------------------------------------------------------
void	join()                      | Chờ cho thread này chết.
------------------------------------|--------------------------------------------------------------
void	join(long millis)           | Chờ tối đa milis miliseconds để thread này chết.
------------------------------------|--------------------------------------------------------------
void	join(long millis, int nanos)| Chờ tối đa milis miliseconds cộng với nanos nanoseconds để
                                    | thread này chết.
------------------------------------|--------------------------------------------------------------
void	run()                       | Nếu thread này được tạo bằng một Runnable object riêng biệt,
                                    | thì run method của Runnable object đó được gọi; nếu không, 
                                    | method này không làm gì cả và trả về.
------------------------------------|--------------------------------------------------------------
void    setContextClassLoader(      | Set context ClassLoader cho Thread này.
            ClassLoader cl          |
        )                           |
------------------------------------|--------------------------------------------------------------
void	setDaemon(boolean on)       | Đánh dấu thread này là một thread daemon hoặc một user thread.
------------------------------------|--------------------------------------------------------------
void	setName(String name)        | Thay đổi tên của thread này.
------------------------------------|--------------------------------------------------------------
void	setPriority(int newPriority)| Thay đổi độ ưu tiên của thread này.
------------------------------------|--------------------------------------------------------------
void	start()                     | Khiến thread này bắt đầu thực thi; 
                                    | JVM gọi run method của thread này.
------------------------------------|--------------------------------------------------------------
String	toString()                  | Trả về biểu diễn string của thread này, bao gồm tên, mức độ ưu
                                    | tiên và thread group.
------------------------------------|--------------------------------------------------------------
StackTraceElement[]	getStackTrace() | Trả về một mảng các stack trace elements đại diện cho stack dump
                                    | (kết xuất stack) của thread này.
------------------------------------|--------------------------------------------------------------
Thread.UncaughtExceptionHandler	    | Trả về handler được gọi khi thread này đột ngột kết thúc do
    getUncaughtExceptionHandler()   | một uncaught exception.
------------------------------------|----------|---------------------------------------------------
void	setUncaughtExceptionHandler(           | Set handler được gọi khi thread này đột ngột kết thúc
            Thread.UncaughtExceptionHandler eh | do một uncaught exception.
        )                                      |
```
