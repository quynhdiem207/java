# Lession 3. Concurrency

## 7. High Level Concurrency Objects

Trong phần này, chúng ta sẽ xem xét một số tính năng đồng thời cấp cao được giới thiệu với version 5.0 của Java platform. Hầu hết các tính năng này được triển khai trong các *java.util.concurrent* package mới. Ngoài ra còn có các concurrent data structures mới trong Java Collections Framework.

- **Lock objects** hỗ trợ locking đơn giản hóa nhiều concurrent applications.  
- **Executors** định nghĩa một high-level API cho việc khởi chạy và quản lý các threads. Các executor implementations được cung cấp bởi java.util.concurrent cung cấp khả năng quản lý thread pool cho các applications quy mô lớn.  
- **Concurrent collections** giúp quản lý các data collections lớn dễ dàng hơn và có thể giảm đáng kể nhu cầu đồng bộ hóa.  
- **Atomic variables**: có các tính năng giảm thiểu đồng bộ hóa và giúp tránh các memory consistency errors (lỗi nhất quán bộ nhớ).  
- **ThreadLocalRandom** (trong JDK 7) cung cấp một cách hiệu quả tạo các numbers giả ngẫu nhiên từ nhiều threads.  
