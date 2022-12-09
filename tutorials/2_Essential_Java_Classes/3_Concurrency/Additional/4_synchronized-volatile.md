# Lession 3. Concurrency

## 4. Synchronized Keyword

Trong môi trường đa luồng, sự tranh chấp xảy ra khi 2 hoặc nhiều thread cố gắng cập nhật mutable shared data cùng một lúc. Java cung cấp một cơ chế để tránh các sự tranh chấp bằng cách đồng bộ hóa thread truy cập vào dữ liệu được chia sẻ.

Phần logic được đánh dấu bằng synchronized sẽ trở thành một synchronized block, chỉ cho phép một thread thực thi tại bất kỳ thời điểm nào.


### 4.1, Why Synchronization?

*Xét ví dụ xảy ra sự tranh chấp khi tính tổng, và nhiều threads thực thi calculate() method:*

```java
public class BaeldungSynchronizedMethods {

    private int sum = 0;

    public void calculate() {
        setSum(getSum() + 1);
    }

    // standard setters and getters
}
```

*Kiểm tra đơn giản như sau:*

```java
@Test
public void givenMultiThread_whenNonSyncMethod() {
    ExecutorService service = Executors.newFixedThreadPool(3);
    BaeldungSynchronizedMethods summation = new BaeldungSynchronizedMethods();

    IntStream.range(0, 1000)
      .forEach(count -> service.submit(summation::calculate));
    service.awaitTermination(1000, TimeUnit.MILLISECONDS);

    assertEquals(1000, summation.getSum());
}
```

Chúng ta đang sử dụng ExecutorService với một 3-threads pool để thực hiện calculate() 1000 lần.

Nếu chúng ta thực thi điều này tuần tự, output dự kiến ​​sẽ là 1000, nhưng thực thi đa luồng của chúng ta hầu như không thành công với output thực tế không nhất quán:

```
java.lang.AssertionError: expected:<1000> but was:<965>
```

Một cách đơn giản để tránh sự xung đột là làm cho hoạt động thread-safe bằng cách sử dụng synchronized keyword.


### 4.2, The Synchronized Keyword

Có thể sử dụng từ synchronized keyword ở các cấp độ khác nhau:  

- Instance methods,  
- Static methods,  
- Code blocks,  

Khi sử dụng một synchronized block, Java sử dụng một monitor nội tại, còn được gọi là monitor lock hoặc intrinsic lock, để cung cấp đồng bộ hóa. Các monitor này được liên kết với một object; do đó, tất cả các synchronized blocks của cùng một object chỉ có thể có một thread thực thi chúng tại cùng một thời điểm.


#### *Synchronized Instance Methods*

Có thể thêm synchronized keyword vào method declaration để làm cho method được đồng bộ hóa:

```java
public synchronized void synchronisedCalculate() {
    setSum(getSum() + 1);
}
```

Các instance methods được đồng bộ hóa trên instance của class sở hữu method, có nghĩa là tại một thời điểm chỉ một thread cho mỗi instance của class có thể thực thi method này.


#### *Synchronized Static Methods*

Các static methods được đồng bộ hóa giống như các instance methods:

```java
public static synchronized void syncStaticCalculate() {
    staticSum = staticSum + 1;
}
```

Các static methods được đồng bộ hóa trên Class object được liên kết với class. Vì chỉ có một Class object tồn tại trong JVM cho mỗi class, nên tại một thời điểm chỉ một thread có thể thực thi một static synchronized method cho mỗi class, bất kể số lượng instance mà nó có.


#### *Synchronized Blocks Within Methods*

Đôi khi chúng ta không muốn đồng bộ hóa toàn bộ method, mà chỉ đồng bộ hóa một số instructions bên trong nó. Chúng ta có thể đạt được điều này bằng cách áp dụng đồng bộ hóa cho một block:

```java
public void performSynchronisedTask() {
    synchronized (this) {
        setCount(getCount()+1);
    }
}
```

Lưu ý rằng chúng ta đã truyền một argument là this cho synchronized block, đây là monitor object. Mã bên trong block được đồng bộ hóa trên monitor object. Nói một cách đơn giản, tại một thời điểm chỉ một thread cho mỗi monitor object có thể thực thi code block đó.

Nếu method là static, chúng ta sẽ truyền Class object thay cho object reference, và class sẽ là một monitor cho sự đồng bộ hóa của block:

```java
public static void performStaticSyncTask(){
    synchronized (SynchronisedBlocks.class) {
        setStaticCount(getStaticCount() + 1);
    }
}
```


#### *Reentrancy*

Khóa phía sau các synchronized methods và blocks sẽ được sử dụng lại. Điều này có nghĩa là thread hiện tại có thể có cùng một synchronized lock lặp đi lặp lại trong khi giữ nó:

```java
Object lock = new Object();
synchronized (lock) {
    System.out.println("First time acquiring it");

    synchronized (lock) {
        System.out.println("Entering again");

        synchronized (lock) {
            System.out.println("And again");
        }
    }
}
```

Như trong ví dụ trên, trong khi chúng ta đang ở trong một synchronized block, chúng ta có thể có được cùng một monitor lock nhiều lần.


## 5. Volatile Keyword

Để hiểu biết về volatile keyword, trước hết cần tìm hiểu một chút về cách thức kiens trúc máy tính hoạt động và memory order (thứ tự bộ nhớ) trong Java.


### 5.1, Shared Multiprocessor Architecture

Bộ xử lý có trách nhiệm thực hiện các lệnh của chương trình. Do đó, chúng cần lấy cả program instructions (tập lệnh chương trình) và required data (dữ liệu cần thiết) từ RAM.

Vì các CPU có khả năng thực hiện một số lượng đáng kể các lệnh mỗi giây, việc tìm nạp từ RAM không phải là điều lý tưởng cho chúng. Để cải thiện tình trạng này, các bộ xử lý đang sử dụng các thủ thuật như Out of order execution (Thực thi không theo thứ tự), Dự đoán nhánh, Thực thi đầu cơ, và tất nhiên Caching (lưu vào bộ nhớ đệm).

Đây là lúc hệ thống phân cấp bộ nhớ sau hoạt động: ![memory hierarchy](./memory_hierarchy.jpg)

Khi các core khác nhau thực thi nhiều lệnh hơn và thao tác nhiều dữ liệu hơn, chúng sẽ lấp đầy bộ nhớ đệm của mình bằng các dữ liệu và hướng dẫn có liên quan hơn. Điều này sẽ cải thiện hiệu suất tổng thể với chi phí đưa ra các thách thức về tính liên kết của bộ nhớ cache.

Nói một cách đơn giản, chúng ta nên suy nghĩ kỹ về điều gì sẽ xảy ra khi một thread cập nhật một giá trị được lưu trong bộ nhớ cache.


### 5.2, When to Use volatile

*Xét ví dụ sau để tìm hiểu về tính nhất quán của bộ đệm:*

```java
public class TaskRunner {

    private static int number;
    private static boolean ready;

    private static class Reader extends Thread {

        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }

            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new Reader().start();
        number = 42;
        ready = true;
    }
}
```

TaskRunner class có 2 biến đơn giản. Trong main method của nó, nó tạo một thread khác thực hiện lặp trên biến ready miễn là biến đó false, khi biến trở thành true, thread sẽ in biến number.

Bạn có thể mong đợi chương trình này chsẽỉ in 42 sau một thời gian ngắn. Tuy nhiên, trên thực tế, thời gian trì hoãn có thể kéo dài hơn nhiều. Nó thậm chí có thể bị treo vĩnh viễn, hoặc thậm chí in số 0!

Nguyên nhân của những dị thường này là do thiếu khả năng hiển thị bộ nhớ và sắp xếp lại thích hợp (memory visibility and reordering).


#### *Memory Visibility*

Trong ví dụ đơn giản này, chúng ta có 2 thread: Main thread và Reader thread. Hãy tưởng tượng một kịch bản trong đó HĐH lập lịch các thread đó trên 2 CPU cores khác nhau, trong đó:

- Main thread có bản copy của các biến number và ready trong bộ nhớ core cache của nó,  
- Reader thread cũng có các bản copy của nó,  
- Main thread cập nhật các giá trị được lưu trong bộ nhớ cache.  

Trên hầu hết các bộ xử lý hiện đại, yêu cầu ghi sẽ không được áp dụng ngay sau khi chúng được đưa ra. Trên thực tế, các bộ xử lý có xu hướng xếp những lần ghi trong một bộ đệm ghi (write buffer) đặc biệt. Sau một thời gian, chúng sẽ áp dụng tất cả các lần ghi đó vào main memory cùng một lúc.

Khi Main thread cập nhật các biến number và ready, không có gì đảm bảo về những gì mà Reader thread có thể thấy. Nói cách khác, Reader thread có thể thấy giá trị được cập nhật ngay lập tức, hoặc với một số độ trễ, hoặc không bao giờ!

Memory visibility (khả năng hiển thị của bộ nhớ) có thể gây ra các vấn đề về liveness trong các chương trình dựa vào khả năng hiển thị.


#### *Reordering*

Vấn đề tồi tệ hơn là, Reader thread có thể thấy những thứ được ghi theo bất kỳ thứ tự nào khác với thứ tự chương trình thực tế. 

*Ví dụ: Khi lần đầu tiên chúng ta cập nhật biến number:*

```java
public static void main(String[] args) { 
    new Reader().start();
    number = 42; 
    ready = true; 
}
```

Chúng ta có thể mong đợi Reader thread in ra 42. Tuy nhiên, thực tế có thể thấy số 0 là giá trị được in!

Reordering (sắp xếp lại thứ tự) là một kỹ thuật tối ưu hóa để cải thiện hiệu suất. Điều thú vị là các components khác nhau có thể áp dụng sự tối ưu hóa này:

- Bộ xử lý có thể xả (flush) bộ đệm ghi (write buffer) của nó theo bất kỳ thứ tự nào khác với thứ tự chương trình,  
- Bộ xử lý có thể áp dụng kỹ thuật thực thi không theo thứ tự (out-of-order execution),  
- Trình biên dịch JIT có thể tối ưu hóa thông qua sắp xếp lại (reordering).  


#### *volatile Memory Order*

Để đảm bảo rằng cập nhật cho các biến sẽ được nhìn thấy bởi các thread khác theo một cách có thể dự đoán được, chúng ta nên áp dụng volatile modifier cho các biến đó:

```java
public class TaskRunner {

    private volatile static int number;
    private volatile static boolean ready;

    // same as before
}
```

Bằng cách này, chúng ta giao tiếp với runtime và bộ xử lý để không sắp xếp lại bất kỳ lệnh nào liên quan đến volatile variable. Ngoài ra, bộ xử lý hiểu rằng chúng nên flush (xả) bất kỳ yêu cầu cập nhật nào cho các biến này ngay lập tức.


### 5.3, volatile and Thread Synchronization

Đối với các ứng dụng đa luồng, chúng ta cần đảm bảo một số quy tắc để có hành vi nhất quán:

- Loại trừ lẫn nhau (Mutual Exclusion) - chỉ một thread thực thi một phần quan trọng tại một thời điểm.  
- Khả năng hiển thị (Visibility) - các thay đổi được thực hiện bởi một thread đối với dữ liệu được chia sẻ sẽ hiển thị với các thread khác để duy trì tính nhất quán của dữ liệu.  

các synchronized methods và blocks cung cấp cả 2 thuộc tính trên, với chi phí là hiệu suất của ứng dụng.

volatile là một từ khóa khá hữu ích, vì nó có thể giúp đảm bảo khía cạnh hiển thị của dữ liệu bị thay đổi mà không cung cấp sự loại trừ lẫn nhau. Do đó, nó hữu ích ở những nơi mà ổn khi nhiều thread thực thi song song một code block, nhưng chúng ta cần đảm bảo thuộc tính khả năng hiển thị.


### 5.4, Happens-Before Ordering

Các ảnh hưởng memory visibility của các volatile variables mở rộng ra ngoài bản thân các volatile variables.

Để làm cho vấn đề cụ thể hơn, hãy giả sử thread A ghi vào một volatile variable, và sau đó thread B đọc cùng một volatile variable. Trong những trường hợp như vậy, các giá trị hiển thị cho A trước khi ghi volatile variable sẽ hiển thị cho B sau khi đọc volatile variable:

```
        Main thread                                            Reader thread
    -----------------                                       --------------------
    | // omitted    | Everything before                     | // omitted       |
    | number = 42;  | writing to ready...                   | while(!ready);   |
    | ready = true; | ------------------------------------->| println(number); |
    -----------------                  ... is visible after |                  |
                                       reading the ready    --------------------
```

Về mặt kỹ thuật, bất kỳ lần ghi nào vào một volatile field đều xảy ra trước mỗi lần đọc kế tiếp của cùng một field. Đây là quy tắc volatile variable của Java Memory Model (JMM).


#### *Piggybacking*

Do sức mạnh của happens-before memory ordering, đôi khi chúng ta có thể dựa vào các thuộc tính visibility của một volatile variable khác. 

*Ví dụ, chúng ta chỉ cần đánh dấu biến ready là volatile:*

```java
public class TaskRunner {

    private static int number; // not volatile
    private volatile static boolean ready;

    // same as before
}
```

Biến number sẽ dựa trên memory visibility được thực thi bởi biến ready. Nói một cách đơn giản, mặc dù nó không phải là một volatile variable, nhưng nó đang thể hiện một hành vi volatile.

Bằng cách sử dụng những ngữ nghĩa này, có thể định nghĩa một số biến trong class là biến volatile và tối ưu hóa đảm bảo khả năng hiển thị.

**Note**: Để đảm bảo thứ tự phù hợp của các hoạt động được cho là nhằm ngăn ngừa các vấn đề về sự không nhất quán của bộ nhớ, các non-volatile variables của class định nghĩa volatile variablle cũng được ghi vào main memory.