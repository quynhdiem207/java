# Lession 3. Concurrency

## 3. Synchronization

Các threads chủ yếu giao tiếp bằng cách chia sẻ quyền truy cập vào các fields và các objects mà các reference fields tham chiếu đến. Hình thức giao tiếp này cực kỳ hiệu quả, nhưng có thể gây ra 2 loại lỗi: *thread interference* (can thiệp luồng) và *memory consistency errors* (lỗi nhất quán bộ nhớ). Công cụ cần thiết để ngăn chặn những lỗi này là *synchronization* (đồng bộ hóa).

Tuy nhiên, synchronization (đồng bộ hóa) có thể gây ra *thread contention* (tranh chấp luồng), xảy ra khi 2 hoặc nhiều threads cố gắng truy cập đồng thời vào cùng một tài nguyên và khiến Java runtime thực thi một hoặc nhiều threads chậm hơn, hoặc thậm chí tạm dừng việc thực thi của chúng. *Starvation* và *livelock* là các hình thức tranh chấp threads.

Các chủ đề sẽ tìm hiểu gồm:  

- **Thread Interference**: mô tả cách lỗi được đưa ra khi nhiều threads truy cập vào data được chia sẻ.  
- **Memory Consistency Errors**: mô tả các lỗi do các views (lượt quan sát) không nhất quán của bộ nhớ dùng chung.  
- **Synchronized Methods**: mô tả việc ngăn chặn hiệu quả sự thread interference (can thiệp luồng) và các memory consistency errors (lỗi nhất quán bộ nhớ).  
- **Intrinsic Locks and Synchronization**: mô tả đồng bộ hóa tổng quát hơn và mô tả cách đồng bộ hóa dựa trên các khóa ngầm định.  
- **Atomic Access**: nói về ý tưởng chung của các hoạt động mà các threads khác không thể can thiệp vào.  


### 3.1, Thread Interference

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

*Counter được thiết kế để mỗi lệnh gọi increment sẽ cộng 1 vào c và mỗi lệnh gọi decrement sẽ trừ đi 1 từ c. Tuy nhiên, nếu một Counter object được tham chiếu từ nhiều threads, sự can thiệp giữa các threads có thể ngăn điều này xảy ra như mong đợi.*

Interference (can thiệp) xảy ra khi 2 operations, chạy trong các threads khác nhau, nhưng hoạt động trên cùng một dữ liệu, xen vào nhau. Điều này có nghĩa là 2 operations bao gồm nhiều bước và trình tự của các bước chồng lên nhau.  

*Có vẻ như không thể thực hiện xen kẽ các operations trên các instances của Counter, vì cả 2 operations trên c đều là các câu lệnh đơn. Tuy nhiên, ngay cả những câu lệnh đơn giản cũng có thể được JVM dịch sang nhiều bước. Expression đơn c++ có thể được phân tách thành ba bước:*

- 1, Truy xuất giá trị hiện tại của c.  
- 2, Tăng giá trị đã truy xuất lên 1.  
- 3, Lưu trữ giá trị đã tăng trở lại trong c.  

*Giả sử thread A gọi increment cùng lúc với thread B gọi decrement. Nếu giá trị ban đầu của c là 0, các hành động xen kẽ của chúng có thể tuân theo trình tự sau:*  

- 1, Thread A: Truy xuất c.  
- 2, Thread B: Truy xuất c.  
- 3, Thread A: Tăng giá trị đã truy xuất; kết quả là 1.  
- 4, Thread B: Giảm giá trị đã truy xuất; kết quả là -1.  
- 5, Thread A: Lưu trữ kết quả trong c; c bây giờ là 1.  
- 6, Thread B: Lưu trữ kết quả trong c; c bây giờ là -1.  

*Kết quả của thread A bị mất, bị ghi đè bởi thread B. Sự đan xen cụ thể này chỉ là một khả năng. Trong các trường hợp khác nhau, kết quả của thread B có thể bị mất hoặc không có lỗi nào cả. Bởi vì chúng không thể đoán trước.* 

Các lỗi thread interference (can thiệp luồng) có thể khó phát hiện và sửa chữa.


### 3.2, Memory Consistency Errors

Các Memory consistency errors (lỗi nhất quán bộ nhớ) xảy ra khi các threads khác nhau có các views (lượt quan sát) không nhất quán về cùng một dữ liệu. Nguyên nhân của lỗi nhất quán bộ nhớ rất phức tạp và nằm ngoài phạm vi của hướng dẫn này. May mắn thay, programmer không cần hiểu biết chi tiết về những nguyên nhân này. Tất cả những gì cần thiết là một chiến lược để tránh chúng.

Chìa khóa để tránh các lỗi về tính nhất quán của bộ nhớ là hiểu mối quan hệ *happens-before*. Mối quan hệ này chỉ đơn giản là một đảm bảo rằng bộ nhớ được ghi bởi một câu lệnh cụ thể sẽ hiển thị với một câu lệnh cụ thể khác.

*Ví dụ: Giả sử một int field đơn giản được định nghĩa và khởi tạo:*

```java
int counter = 0;
```

*counter field được chia sẻ giữa hai thread A và B. Giả sử thread A tăng counter:*

```java
counter++;
```

*Ngay sau đó, thread B in ra counter:*

```java
System.out.println(counter);
```

*Nếu 2 statement đã được thực thi trong cùng một thread, sẽ an toàn để giả sử rằng giá trị được in ra sẽ là "1". Nhưng nếu 2 statement được thực thi trong các thread riêng biệt, giá trị được in ra cũng có thể là "0", bởi vì không có gì đảm bảo rằng sự thay đổi counter của thread A sẽ hiển thị với thread B - trừ khi programmer đã thiết lập mối quan hệ happens-before giữa 2 statement này.*

Có một số hành động tạo ra các mối quan hệ happens-before. Một trong số đó là synchronization (đồng bộ hóa).

Chúng ta đã thấy 2 hành động tạo ra các mối quan hệ happens-before.

- Khi một statement gọi Thread.start, mọi statement có mối quan hệ happens-before với statement đó cũng có mối quan hệ happens-before với mọi statement được thực thi bởi thread mới. Các tác động của mã dẫn đến việc tạo thread mới sẽ hiển thị đối với thread mới.  

- Khi một thread kết thúc và khiến một Thread.join trong một thread khác quay trở lại, thì tất cả các statement được thực thi bởi thread bị kết thúc có mối quan hệ happens-before với tất cả các statement theo sau join thành công. Các hiệu ứng của mã trong thread bây giờ được hiển thị cho thread đã thực hiện join.  

Để biết danh sách các hành động tạo mối quan hệ happens-before, hãy tham khảo trang [java.util.concurrent](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html#MemoryVisibility).


### 3.3, Synchronized Methods

Java cung cấp 2 cách thức đồng bộ hóa cơ bản: *synchronized methods* và *synchronized statements*.

Để tạo một phương thức được đồng bộ hóa, chỉ cần thêm *synchronized* keyword vào declaration của nó:

```java
public class SynchronizedCounter {
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

Nếu count là một instance của SynchronizedCounter, thì việc làm cho các methods này được đồng bộ hóa có 2 tác dụng:  

- Đầu tiên, không thể có 2 lệnh gọi của các synchronized methods trên cùng một object xen kẽ nhau. Khi một thread đang thực thi một synchronized method cho một object, tất cả các threads khác gọi các synchronized methods cho cùng một object (tạm ngừng thực thi) cho đến khi thread đầu tiên hoàn thành với object.  

- Thứ hai, khi một synchronized method thoát ra, nó sẽ tự động thiết lập một mối quan hệ happens-before với bất kỳ lệnh gọi nào sau đó của một synchronized method cho cùng một object. Điều này đảm bảo rằng các thay đổi đối với các state của object được hiển thị cho tất cả các threads.  

**Note**: các constructor không thể được đồng bộ hóa - việc sử dụng synchronized keyword với một constructor là một lỗi cú pháp. Đồng bộ hóa các constructor không có ý nghĩa, bởi vì chỉ thread tạo object mới có quyền truy cập vào nó trong khi nó đang được tạo.

**Warning**: Khi tạo một object sẽ được chia sẻ giữa các thread, hãy hết sức cẩn thận rằng một tham chiếu đến object không bị "rò rỉ" sớm. 

*Ví dụ, giả sử bạn muốn bảo tồn một List được gọi là instances chứa mọi instance của class. Bạn có thể bị cám dỗ để thêm dòng sau vào hàm tạo của mình:*

```java
instances.add(this);
```

*Nhưng sau đó các thread khác có thể sử dụng instances để truy cập các objects trước khi quá trình tạo object hoàn tất.*

Các synchronized methods cho phép một chiến lược đơn giản để ngăn chặn thread interference (sự can thiệp luồng) và memory consistency errors (lỗi nhất quán bộ nhớ): nếu một object hiển thị cho nhiều hơn một thread, thì tất cả việc đọc hoặc ghi vào các biến của object đó đều được thực hiện thông qua các synchronized methods. (Một exception quan trọng: các final fields, không thể sửa đổi sau khi object được tạo, có thể được đọc một cách an toàn thông qua các non-synchronized methods, sau khi object được tạo) Chiến lược này hiệu quả, nhưng có thể gây ra các vấn đề với *liveness*.


### 3.4, Intrinsic Locks and Synchronization

Synchronization (đồng bộ hóa) được xây dựng xung quanh một thực thể nội bộ được gọi là *intrinsic lock* (khóa nội tại) hoặc *monitor lock* (khóa màn hình). Các khóa nội tại đóng một vai trò trong cả 2 khía cạnh của đồng bộ hóa: thực thi truy cập độc quyền vào state của object và thiết lập các mối quan hệ happens-before cần thiết cho khả năng hiển thị.

Mọi object đều có một khóa nội tại liên kết với nó. Theo quy ước, một thread cần truy cập độc quyền và nhất quán vào các fields của object phải có được khóa nội tại của object trước khi truy cập chúng, sau đó giải phóng khóa nội tại khi hoàn thành xong việc đó. Một thread được cho là sở hữu khóa nội tại trong khoảng thời gian từ khi nó có được khóa đến khi giải phóng khóa. Miễn là một thread sở hữu một khóa nội tại, không một thread nào khác có thể có được cùng một khóa. Thread khác sẽ bị chặn khi nó cố gắng lấy khóa.

Khi một thread giải phóng một khóa nội tại, một mối quan hệ happens-before được thiết lập giữa hành động đó và bất kỳ lần sở hữu lại nào sau đó của cùng một khóa.


#### *Locks In Synchronized Methods*

Khi một thread gọi một synchronized method, nó sẽ tự động nhận được khóa nội tại cho object của method đó và giải phóng nó khi method trả về. Việc giải phóng khóa xảy ra ngay cả khi việc trả lại được gây ra bởi một uncaught exception.

Điều gì sẽ xảy ra khi một static synchronized method được gọi, vì một static method được liên kết với một class chứ không phải một object. Trong trường hợp này, thread có được khóa nội tại cho Class object được liên kết với class đó. Do đó, quyền truy cập vào các static fields của class được kiểm soát bởi một khóa khác với khóa cho bất kỳ instances nào của class.


#### *Synchronized Statements*

Một cách khác để tạo mã được đồng bộ hóa là sử dụng các *synchronized statements*. Không giống như các synchronized methods, các synchronized statements phải chỉ định object cung cấp khóa nội tại:

```java
public void addName(String name) {
    synchronized(this) {
        lastName = name;
        nameCount++;
    }
    nameList.add(name);
}
```

*Trong ví dụ này, addName method cần đồng bộ hóa các thay đổi đối với lastName và nameCount, nhưng cũng cần tránh đồng bộ hóa các lệnh gọi method của các objects khác (việc gọi các method của object khác từ mã được đồng bộ hóa có thể tạo ra các vấn đề về Liveness). Nếu không có các synchronized statements, sẽ phải có một method riêng biệt, không đồng bộ hóa cho mục đích duy nhất là gọi nameList.add.*

Các synchronized statements cũng hữu ích để cải thiện tính đồng thời (concurrency) với tính năng đồng bộ hóa chi tiết. 

*Ví dụ, giả sử class MsLunch có 2 instance fields, c1 và c2, không bao giờ được sử dụng cùng nhau. Tất cả các cập nhật của các fields này phải được đồng bộ hóa, nhưng không có lý do gì để ngăn cập nhật c1 xen kẽ với cập nhật c2 - và làm như vậy sẽ giảm sự đồng thời bằng cách tạo ra các chặn không cần thiết. Thay vì sử dụng các synchronized methods hoặc sử dụng khóa được liên kết với this, chúng tôi tạo 2 object chỉ để cung cấp khóa:*

```java
public class MsLunch {
    private long c1 = 0;
    private long c2 = 0;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void inc1() {
        synchronized(lock1) {
            c1++;
        }
    }

    public void inc2() {
        synchronized(lock2) {
            c2++;
        }
    }
}
```

Cần sử dụng các synchronized statements một cách cẩn thận. Bạn phải hoàn toàn chắc chắn rằng việc xen kẽ quyền truy cập của các fields bị ảnh hưởng là thực sự an toàn.


#### *Reentrant Synchronization*

Nhớ lại rằng một thread không thể có được một khóa thuộc sở hữu của một thread khác. Nhưng một thread có thể có được một khóa mà nó đã sở hữu. Việc cho phép một thread có được cùng một khóa nhiều lần sẽ cho phép *reentrant synchronization* (đồng bộ hóa lập lại). Điều này mô tả một tình huống trong đó mã được đồng bộ hóa, trực tiếp hoặc gián tiếp, gọi một method cũng chứa mã được đồng bộ hóa và cả 2 bộ mã đều sử dụng cùng một khóa. Nếu không có đồng bộ hóa lập lại, mã được đồng bộ hóa sẽ phải thực hiện nhiều biện pháp phòng ngừa bổ sung để tránh một thread chặn chính nó.


### 3.5, Atomic Access

Trong lập trình, một *atomic* action là một hành động có hiệu quả xảy ra cùng một lúc. Một atomic action không thể dừng lại ở giữa chừng: nó xảy ra hoàn toàn, hoặc hoàn toàn không xảy ra. Không có hiệu ứng phụ của một atomic action có thể nhìn thấy cho đến khi hành động hoàn tất.

Chúng ta đã thấy rằng một biểu thức tăng dần, chẳng hạn như c++, không mô tả một atomic action. Ngay cả những biểu thức rất đơn giản cũng có thể định nghĩa các hành động phức tạp mà có thể phân tách thành các hành động khác. Tuy nhiên, có những hành động bạn có thể chỉ định là atomic:

- Đọc và ghi là atomic đối với các reference variables và đối với hầu hết các primitive variables (tất cả các types ngoại trừ long và double).  
- Đọc và ghi là atomic cho tất cả các variables được khai báo là volatile (bao gồm cả các long và double variables).  

Các atomic actions không thể được xen kẽ, vì vậy chúng có thể được sử dụng mà không sợ thread interference (can thiệp luồng). Tuy nhiên, điều này không loại bỏ tất cả các nhu cầu đồng bộ hóa các atomic actions, bởi vì memory consistency errors (lỗi nhất quán bộ nhớ) vẫn có thể xảy ra. Việc sử dụng các volatile variable làm giảm nguy cơ mắc lỗi nhất quán bộ nhớ, bởi vì bất kỳ lần ghi nào vào một volatile variable đều thiết lập mối quan hệ happens-before với các lần đọc tiếp theo của cùng một biến đó. Điều này có nghĩa là các thay đổi đối với một volatile variable luôn hiển thị với các thread khác. Hơn nữa, điều đó cũng có nghĩa là khi một thread đọc một volatile variable, nó không chỉ thấy thay đổi mới nhất đối với volatile variable mà còn cả các hiệu ứng phụ của mã dẫn đến thay đổi đó.

Sử dụng truy cập atomic variable đơn giản hiệu quả hơn truy cập các biến này thông qua mã được đồng bộ hóa, nhưng yêu cầu programmer cẩn thận hơn để tránh lỗi nhất quán bộ nhớ.

Một số class trong *java.util.concurrent* package cung cấp các atomic methods không dựa vào đồng bộ hóa.