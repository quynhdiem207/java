# Lession 3. Concurrency

## 7.3, Concurrent Collections

*java.util.concurrent* package bao gồm một số bổ sung cho Java Collections Framework. Chúng được phân loại dễ dàng nhất bởi các collection interfaces được cung cấp:

- **BlockingQueue** định nghĩa một cấu trúc dữ liệu FIFO mà chặn hoặc time out (vượt quá hạn) khi bạn cố gắng thêm vào hàng đợi đầy hoặc truy xuất từ ​​hàng đợi trống.  
- **ConcurrentMap** là một subinterface của *java.util.Map* mà định nghĩa các atomic operators hữu ích. Các operators này chỉ xóa hoặc thay thế một cặp key-value nếu key tồn tại hoặc chỉ thêm một cặp key-value nếu key không tồn tại. Làm cho các operations này trở thành atomic giúp tránh đồng bộ hóa. Standard eneral-purpose implementation của *ConcurrentMap* là *ConcurrentHashMap*, là một bản đồng thời của *HashMap*.  
- **ConcurrentNavigableMap** là một subinterface của *ConcurrentMap* hỗ trợ các tương thích gần đúng.  Standard eneral-purpose implementation của *ConcurrentNavigableMap* là *ConcurrentSkipListMap*, là một bản đồng thời của *TreeMap*.  

Tất cả các collections này giúp tránh Memory Consistency Errors (Lỗi nhất quán bộ nhớ) bằng cách xác định mối quan hệ happens-before giữa một thao tác thêm một object vào collection với các thao tác tiếp theo truy cập hoặc xóa object đó.