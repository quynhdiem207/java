# Lession 3. Concurrency

## 7.2, Executors

Trong tất cả các ví dụ trước, có một mối liên hệ chặt chẽ giữa tác vụ đang được thực hiện bởi một thread mới, được định nghĩa bởi *Runnable* object của nó, và chính thread đó, được định nghĩa bởi một *Thread* object. Điều này hoạt động tốt cho các ứng dụng nhỏ, nhưng trong các ứng dụng quy mô lớn, cần tách biệt quản lý và tạo thread khỏi phần còn lại của ứng dụng. Các objects đóng gói các chức năng này được gọi là *executors* (trình thực thi). 

Những mô tả chi tiết cho các executors:

- **Executor Interfaces** định nghĩa 3 executor object types.  
- **Thread Pools** là loại triển khai executor phổ biến nhất.  
- **Fork/Join** là một framework (mới trong JDK 7) để tận dụng multiple processors (nhiều bộ vi xử lý).  


### 7.2.1, Executor Interfaces

*java.util.concurrent* package định nghĩa 3 executor interfaces:

- *Executor*: một interface đơn giản hỗ trợ khởi chạy các tác vụ mới.  
- *ExecutorService*: một subinterface của *Executor*, bổ sung các tính năng giúp quản lý life cycle, gồm cả các tác vụ riêng và của chính executor.  
- *ScheduledExecutorService*: một subinterface của *ExecutorService*, hỗ trợ thực thi các tác vụ trong tương lai và/hoặc định kỳ.  


#### *The Executor Interface*

[Executor](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executor.html) interface cung cấp một method duy nhất, *execute*, được thiết kế để thay thế cho việc tạo và khởi chạy thread phổ biến. Nếu r là một Runnable object và e là một Executor object, bạn có thể thay thế:

```java
(new Thread(r)).start();
```

bằng:  

```java
e.execute(r);
```

Tuy nhiên, định nghĩa của *execute* ít cụ thể hơn. Cách thức low-level tạo một thread mới và khởi chạy nó ngay lập tức. Tùy thuộc vào việc triển khai *Executor*, *execute* có thể làm điều tương tự, nhưng có nhiều khả năng sử dụng một worker thread hiện có để chạy r hoặc đặt r vào queue (hàng đợi) để chờ một worker thread khả dụng (Xem mô tả về các worker threads trong phần Thread Pools). 

Các triển khai executor trong *java.util.concurrent* được thiết kế để sử dụng đầy đủ các interface nâng cao hơn *ExecutorService* và *SchedisedExecutorService*, mặc dù chúng cũng hoạt động với interface cơ sở *Executor*.


#### *The ExecutorService Interface*

*ExecutorService* interface bổ sung *submit* method tương tự *execute* method nhưng linh hoạt hơn. Giống như *execute*, *submit* chấp nhận các *Runnable* objects, nhưng cũng chấp nhận các *Callable* objects, cho phép tác vụ trả về một giá trị. *submit* method trả về một *Future* object, được sử dụng để truy xuất giá trị trả về Callable và để quản lý các states của cả tác vụ Callable và Runnable.

*ExecutorService* cũng cung cấp các methods để gửi các collections lớn của các Callable objects. Cuối cùng, *ExecutorService* cung cấp một số methods để quản lý việc shutdown (tắt) của executor. Để hỗ trợ shutdown ngay lập tức, các tác vụ phải xử lý các interrupts một cách chính xác.


#### *The ScheduledExecutorService Interface*

*ScheduledExecutorService* interface bổ sung các methods của superinterface *ExecutorService* của nó với *schedule*, thực thi một tác vụ *Runnable* hoặc *Callable* sau một khoảng thời gian trì hoãn được chỉ định. 

Ngoài ra, interface này còn định nghĩa *scheduleAtFixedRate* và *scheduleWithFixedDelay* thực thi các tác vụ được chỉ định lặp đi lặp lại, trong các khoảng thời gian xác định.


### 7.2.2, Thread Pools

Hầu hết các triển khai executor trong *java.util.concurrent* đều sử dụng các *thread pools* mà bao gồm các *worker threads*. Loại thread này tồn tại tách biệt với các tác vụ Runnable và Callable mà nó thực thi và thường được sử dụng để thực thi nhiều tác vụ.

Sử dụng các woker threads sẽ giảm thiểu chi phí do tạo thread. Các Thread objects sử dụng một lượng bộ nhớ đáng kể và trong một ứng dụng quy mô lớn, việc cấp phát và phân bổ nhiều Thread objects sẽ tạo ra một chi phí quản lý bộ nhớ đáng kể.

Một loại thread pool phổ biến là *fixed thread pool*. Loại pool này luôn có một số lượng xác định các threads đang chạy; nếu một thread nào đó bị kết thúc bằng cách nào đó trong khi nó vẫn đang được sử dụng, nó sẽ tự động được thay thế bằng một thread mới. Các tác vụ được gửi đến pool thông qua một queue (hàng đợi) nội bộ, queue này chứa các tác vụ bổ sung bất cứ khi nào có nhiều tác vụ hoạt động hơn threads.

Một lợi thế quan trọng của *fixed thread pool* là "Graceful Degradation" (suy giảm mềm). *Ví dụ: Một web server application trong đó mỗi HTTP requesst được xử lý bởi một thread riêng biệt. Nếu ứng dụng chỉ đơn giản tạo một thread mới cho mỗi HTTP requesst mới và hệ thống nhận được nhiều request hơn mức nó có thể xử lý ngay lập tức, ứng dụng sẽ đột ngột ngừng phản hồi tất cả các request khi tổng chi phí của tất cả các thread đó vượt quá khả năng của hệ thống. Với giới hạn về số lượng các thread có thể được tạo, ứng dụng sẽ không phục vụ các HTTP request nhanh nhất khi chúng đến, nhưng nó sẽ phục vụ chúng nhanh nhất khi hệ thống có thể duy trì.*

Một cách đơn giản để tạo một executor sử dụng một fixed thread pool là gọi *newFixedThreadPool* factory method trong *java.util.concurrent.Executors*. Class này cũng cung cấp các factory methods sau:

- *newCachedThreadPool* method tạo một executor với một thread pool có thể mở rộng. executor này thích hợp cho các ứng dụng khởi chạy nhiều tác vụ trong thời gian ngắn.  
- *newSingleThreadExecutor* method tạo một executor thực thi một tác vụ duy nhất tại một thời điểm.  
- Một số factory methods là các *ScheduledExecutorService* versions của các executor ở trên.  

Nếu không có executor nào được cung cấp bởi các factory methods ở trên đáp ứng nhu cầu của bạn, việc tạo các instances của *java.util.concurrent.ThreadPoolExecutor* hoặc *java.util.concurrent.ScheduledThreadPoolExecutor* sẽ cung cấp cho bạn các tùy chọn bổ sung.


### 7.2.3, Fork/Join

Fork/Join framework là một triển khai của *ExecutorService* interface giúp bạn tận dụng lợi thế của multiple processors. Nó được thiết kế cho công việc có thể được chia thành các phần nhỏ hơn một cách đệ quy. Mục đích là sử dụng tất cả sức mạnh xử lý hiện có để nâng cao hiệu suất cho ứng dụng của bạn.

Như với bất kỳ triển khai ExecutorService nào, fork/join framework phân phối các tác vụ cho các worker threads trong một thread pool. Fork/Join framework khác biệt vì nó sử dụng thuật toán *work-stealing* (đánh cắp công việc). Các worker threads không còn việc để làm có thể lấy cắp nhiệm vụ từ các threads khác vẫn đang bận.

Trung tâm của fork/join framework là *ForkJoinPool* class, một extension của *AbstractExecutorService* class. *ForkJoinPool* triển khai thuật toán work-stealing cốt lõi và có thể thực thi các *ForkJoinTask* process.


#### *Basic Use*

Bước đầu tiên để sử dụng fork/join là viết mã thực hiện một phân đoạn của công việc. Mã của bạn sẽ trông giống với mã giả sau:

```java
if (phần công việc của tôi đủ nhỏ)
    làm công việc trực tiếp
else
    chia công việc của tôi thành 2 phần
    gọi 2 phần và chờ kết quả
```

Wrap mã này trong một *ForkJoinTask* subclass, thường sử dụng một trong những kiểu chuyên biệt hơn của nó, *RecursiveTask* (cái mà có thể trả về một kết quả) hoặc *RecursiveAction*.

Sau khi *ForkJoinTask* subclass của bạn đã sẵn sàng, hãy tạo object đại diện cho tất cả công việc cần thực hiện và truyền nó cho *invoke()* method của một *ForkJoinPool* instance.


#### *Blurring for Clarity*

*Để hiểu cách thức hoạt động của fork/join framwork, hãy xem xét ví dụ sau: Giả sử rằng bạn muốn làm mờ một hình ảnh. Hình ảnh nguồn ban đầu được đại diện bởi một mảng các số nguyên, trong đó mỗi số nguyên chứa các giá trị màu cho một pixel. Hình ảnh đích bị mờ cũng được biểu diễn bằng một mảng số nguyên có cùng kích thước với nguồn.*

*Thực hiện làm mờ được thực hiện bằng cách làm việc thông qua mảng nguồn một pixel tại một thời điểm. Mỗi pixel được tính trung bình với các pixel xung quanh của nó (các thành phần màu đỏ, xanh lá cây và xanh lam được tính trung bình) và kết quả được đặt trong mảng đích. Vì hình ảnh là một mảng lớn, quá trình này có thể mất nhiều thời gian. Bạn có thể tận dụng lợi thế của quá trình xử lý đồng thời trên các hệ thống đa xử lý bằng cách triển khai thuật toán sử dụng fork/join framework. Đây là một trong những cách triển khai có thể thực hiện:*

```java
public class ForkBlur extends RecursiveAction {
    private int[] mSource;
    private int mStart;
    private int mLength;
    private int[] mDestination;
  
    // Processing window size; should be odd.
    private int mBlurWidth = 15;
  
    public ForkBlur(int[] src, int start, int length, int[] dst) {
        mSource = src;
        mStart = start;
        mLength = length;
        mDestination = dst;
    }

    protected void computeDirectly() {
        int sidePixels = (mBlurWidth - 1) / 2;
        for (int index = mStart; index < mStart + mLength; index++) {
            // Calculate average.
            float rt = 0, gt = 0, bt = 0;
            for (int mi = -sidePixels; mi <= sidePixels; mi++) {
                int mindex = Math.min(
                    Math.max(mi + index, 0), 
                    mSource.length - 1
                );
                int pixel = mSource[mindex];
                rt += (float)((pixel & 0x00ff0000) >> 16) / mBlurWidth;
                gt += (float)((pixel & 0x0000ff00) >>  8) / mBlurWidth;
                bt += (float)((pixel & 0x000000ff) >>  0) / mBlurWidth;
            }
          
            // Reassemble destination pixel.
            int dpixel = (0xff000000) |
                   (((int)rt) << 16) |
                   (((int)gt) <<  8) |
                   (((int)bt) <<  0);
            mDestination[index] = dpixel;
        }
    }
    ...
}
```

*Bây giờ bạn triển khai abstract **compute()** method, method này thực hiện trực tiếp làm mờ hoặc chia nó thành hai tác vụ nhỏ hơn. Một ngưỡng độ dài mảng đơn giản giúp xác định xem công việc được thực hiện hay được phân chia:*

```java
// Giá trị sThreshold xác định liệu quá trình làm mờ
// sẽ được thực hiện trực tiếp hay chia thành 2 tasks.
protected static int sThreshold = 100000;

protected void compute() {
    if (mLength < sThreshold) {
        computeDirectly();
        return;
    }
    
    int split = mLength / 2;
    
    invokeAll(
        new ForkBlur(mSource, mStart, split, mDestination),
        new ForkBlur(mSource, mStart + split, mLength - split, mDestination)
    );
}
```

Nếu các methods trước đó nằm trong subclass của *RecursiveAction* class, thì việc thiết lập tác vụ để chạy trong *ForkJoinPool* rất đơn giản và bao gồm các bước sau:

1. Tạo một tác vụ đại diện cho tất cả các công việc sẽ được thực hiện.  

    ```java
    // source image pixels are in src
    // destination image pixels are in dst
    ForkBlur fb = new ForkBlur(src, 0, src.length, dst);
    ```  

2. Tạo ForkJoinPool sẽ chạy tác vụ.  

    ```java
    ForkJoinPool pool = new ForkJoinPool();
    ```  

3. Chạy tác vụ.  

    ```java
    pool.invoke(fb);
    ```  


#### *Standard Implementations*

Bên cạnh việc sử dụng fork/join framework để triển khai các thuật toán tùy chỉnh cho các tác vụ được thực hiện đồng thời trên hệ thống đa xử lý (*chẳng hạn như ví dụ ForkBlur.java trong phần trước*), có một số tính năng hữu ích trong Java SE đã được triển khai bằng cách sử dụng fork/join framwork. Một cách triển khai như vậy, được giới thiệu trong Java SE 8, được sử dụng bởi *java.util.Arrays* class cho các *parallelSort()* methods của nó. Các methods này tương tự như *sort()*, nhưng tận dụng sự đồng thời thông qua fork/join framework. Sắp xếp song song các mảng lớn nhanh hơn sắp xếp tuần tự khi chạy trên hệ thống đa xử lý.

Một cách triển khai khác của fork/join framework được sử dụng bởi các methods trong *java.util.streams* package.