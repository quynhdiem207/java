# Lession 3. Concurrency

## 1. Processes and Threads

Trong lập trình đồng thời (concurrent programming), có 2 đơn vị thực thi cơ bản: tiến trình (process) và luồng (thread). Trong Java, concurrent programming chủ yếu quan tâm đến các threads. Tuy nhiên, các processes cũng rất quan trọng.

Một hệ thống máy tính thường có nhiều process và thread hoạt động. Điều này đúng ngay cả trong các hệ thống chỉ có một lõi thực thi duy nhất và do đó chỉ có một luồng thực sự thực thi tại bất kỳ thời điểm nào. Thời gian xử lý cho một lõi duy nhất được chia sẻ giữa các process và thread thông qua một tính năng hệ điều hành được gọi là cắt thời gian (time slicing).

Hiện nay, các hệ thống máy tính có nhiều bộ xử lý (processors) hoặc bộ xử lý có nhiều lõi thực thi ngày càng trở nên phổ biến hơn. Điều này giúp tăng cường đáng kể khả năng của hệ thống để thực thi đồng thời các process và thread - nhưng concurrency vẫn có thể xảy ra ngay cả trên các hệ thống đơn giản, không có nhiều bộ xử lý hoặc lõi thực thi.

### *Processes*

Một process có một môi trường thực thi độc lập. Một process thường có một tập hợp các runtime resources cơ bản hoàn chỉnh, riêng tư; đặc biệt, mỗi process có không gian bộ nhớ riêng.

Các process thường được coi là đồng nghĩa với các chương trình hoặc ứng dụng. Tuy nhiên, những gì người dùng nhìn thấy như một ứng dụng duy nhất, trên thực tế có thể là một tập hợp các process hợp tác với nhau. Để tạo điều kiện giao tiếp giữa các process, hầu hết các hệ điều hành hỗ trợ các *Inter Process Communication* (IPC) resources - tài nguyên giao tiếp giữa các process, chẳng hạn như pipes và sockets. IPC không chỉ được sử dụng để liên lạc giữa các process trên cùng một hệ thống mà còn sử dụng cho các process trên các hệ thống khác nhau.

Hầu hết các triển khai của JVM đều chạy dưới dạng một process duy nhất. Một ứng dụng Java có thể tạo các process bổ sung bằng cách sử dụng một *ProcessBuilder* object. Các ứng dụng đa tiến trình (multiprocess applications) nằm ngoài phạm vi của bài học này.


### *Threads*

Thread (luồng) về cơ bản là một sub-process. Một đơn vị xử lý nhỏ nhất của máy tính có thể thực hiện một công việc riêng biệt. Trong Java, các thread được quản lý bởi JVM.

Các threads đôi khi được gọi là *lightweight processes*. Cả process và thread đều cung cấp một môi trường thực thi, nhưng việc tạo một thread mới yêu cầu ít tài nguyên hơn so với tạo một process mới.

Các threads tồn tại trong một process - mỗi process đều có ít nhất một thread. Các threads chia sẻ tài nguyên của process, bao gồm cả bộ nhớ và các tệp mở. Điều này tạo nên sự hiệu quả, nhưng có thể dẫn tới vấn đề về giao tiếp.


## 2. Multi-Thread

Multi-thread (đa luồng) là một tiến trình thực hiện nhiều thread đồng thời. Một ứng dụng Java ngoài main thread có thể có các thread khác thực thi đồng thời giúp ứng dụng chạy nhanh và hiệu quả hơn.

*VD: Trình duyệt web hay các chương trình chơi nhạc là 1 ví dụ điển hình về multi-thread.*

+ Khi duyệt 1 trang web, có rất nhiều hình ảnh, CSS, javascript, ... được tải đồng thời bởi các thread khác nhau.  
+ Khi play nhạc, chúng ta vẫn có thể tương tác được với nút điều khiển như: Play, pause, next, back, ... vì thread phát nhạc là thread riêng biệt với thread tiếp nhận tương tác của người dùng.  

Thực thi đa luồng (Multithreaded execution) là một tính năng thiết yếu của Java platform. Mọi ứng dụng đều có ít nhất một thread - hoặc một vài thread, nếu bạn đếm các "system" thread thực hiện những việc như quản lý bộ nhớ và xử lý tín hiệu. Nhưng theo quan điểm của application programmer, bạn bắt đầu chỉ với một thread, được gọi là *main thread*. Thread này có khả năng tạo các threads bổ sung.


## 3. Multi-Tasking (Đa nhiệm)

Multitasking: Là khả năng chạy đồng thời một hoặc nhiều chương trình cùng một lúc trên một hệ điều hành. Hệ điều hành quản lý việc này và sắp xếp lịch phù hợp cho các chương trình đó. 

*Ví dụ: Trên HĐH Windows chúng ta có làm việc đồng thời với các chương trình khác nhau như: Microsoft Word, Excel, Media Player, …*

Chúng ta sử dụng đa nhiệm để tận dụng tính năng của CPU.

Đa nhiệm có thể đạt được bằng 2 cách:

- Đa nhiệm dựa trên đơn tiến trình (Process) – Đa tiến trình (Multi-processing).  
    + Mỗi process có địa chỉ riêng trong bộ nhớ, tức là mỗi process phân bổ vùng nhớ riêng biệt.  
    + Process là nặng.  
    + Sự giao tiếp giữa các process có chi phí cao.  
    + Chuyển đổi từ process này sang process khác đòi hỏi thời gian để đăng ký việc lưu và tải các bản đồ bộ nhớ, các danh sách cập nhật, ...  

- Đa nhiệm dựa trên luồng (Thread) – Đa luồng (MultiThreading).  
    + Các thread chia sẻ không gian vùng nhớ.  
    + Thread là nhẹ.
    + Sự giao tiếp giữa các thread có chi phí thấp.  

Đa tiến trình (multi-processing) và đa luồng (multi-threading) cả 2 được sử dụng để tạo ra hệ thống đa nhiệm (multitasking). Nhưng chúng ta sử dụng multi-thread nhiều hơn multi-process bởi vì các thread chia sẻ một vùng bộ nhớ chung. Chúng không phân bổ vùng bộ nhớ riêng biệt để tiết kiệm bộ nhớ, và chuyển đổi ngữ cảnh giữa các thread mất ít thời gian hơn process.


Ưu điểm của multi-thread:  

- Nó không chặn người sử dụng vì các thread là độc lập, bạn có thể thực hiện nhiều công việc cùng một lúc.  
- Mỗi thread có thể dùng chung và chia sẻ nguồn tài nguyên trong quá trình chạy, nhưng có thể thực hiện một cách độc lập.  
- Thread là độc lập vì vậy nó không ảnh hưởng đến thread khác nếu ngoại lệ xảy ra trong một thread duy nhất.  
- Có thể thực hiện nhiều hoạt động song song để tiết kiệm thời gian. Ví dụ một ứng dụng có thể được tách thành: main thread chạy giao diện người dùng và các thread phụ chịu trách nhiệm xử lý và gửi kết quả đến main thread.  

Nhược điểm của multi-thread:  

- Càng nhiều thread thì xử lý càng phức tạp.  
- Xử lý vấn đề về tranh chấp bộ nhớ, đồng bộ dữ liệu khá phức tạp.  
- Cần phát hiện tránh các vấn đề: Deadlock, Livelock, ...  