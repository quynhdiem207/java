# Lession 3. Concurrency

## 7. High Level Concurrency Objects

### 7.5, Concurrent Random Numbers

Trong JDK 7, *java.util.concurrent* bao gồm một class tiện ích, [ThreadLocalRandom](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadLocalRandom.html), dành cho các ứng dụng mong đợi sử dụng các số ngẫu nhiên từ nhiều threads hoặc *ForkJoinTasks*.

Đối với truy cập đồng thời, sử dụng *ThreadLocalRandom* thay vì *Math.random()* dẫn đến ít tranh chấp hơn và hiệu suất tốt hơn.

Tất cả những gì bạn cần làm là gọi *ThreadLocalRandom.current()*, sau đó gọi một trong các methods của nó để lấy một số ngẫu nhiên. 

*Ví dụ:*

```java
int r = ThreadLocalRandom.current().nextInt(4, 77);
```