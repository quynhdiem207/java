# Lession 1. Interfaces

Các *core collection interfaces* đóng gói các types của các collections khác nhau. Các interfaces này cho phép các collections được thao tác độc lập với các chi tiết biểu diễn của chúng. Các core collection interfaces là nền tảng của Java Collections Framework. 

Như bạn có thể thấy trong hình sau, các core collection interfaces tạo thành một hệ thống phân cấp:

```
                Collection                     
                    |                            
    ---------------------------------            
    |               |               |         
   Set            List            Queue          Map
    |                               |             |
 SortedSet                        Deque       SortedMap
```

Một *Set* là một loại *Collection* đặc biệt, một *SortedSet* là một loại *Set* đặc biệt, ... Cũng lưu ý rằng hệ thống phân cấp bao gồm hai trees riêng biệt - *Map* không phải là một *Collection* thực sự.

Lưu ý rằng tất cả các core collection interfaces là generic. 

*Ví dụ, đây là declaration của Collection interface:*

```java
public interface Collection<E> extends Iterable<E> {}
```

Khi khai báo một Collection instance, bạn có thể và nên chỉ định type của object được chứa trong collection. Việc chỉ định type cho phép compiler xác minh (tại compile-time) rằng type của object mà bạn đưa vào collection là chính xác, do đó giảm lỗi tại runtime. 

Để có thể quản lý số lượng các core collection interfaces, Java platform không cung cấp các interfaces riêng biệt cho từng biến thể của mỗi collection type (Các biến thể như vậy có thể bao gồm immutable, fixed-size, và append-only.) Thay vào đó, các hoạt động sửa đổi trong mỗi interfaces được chỉ định là tùy chọn - một implementation nhất định có thể chọn không hỗ trợ tất cả các hoạt động. Nếu một hoạt động không được hỗ trợ được gọi, một collection sẽ ném ra một UnsupportedOperationException. Các implementations có trách nhiệm ghi lại những hoạt động tùy chọn nào mà chúng hỗ trợ. Tất cả các implementations mục đích chung của Java platform hỗ trợ tất cả các hoạt động tùy chọn.

Danh sách các core collection interfaces bao gồm:

- **Collection**: root của hệ thống phân cấp collection. Một collection đại diện cho một nhóm các object được gọi là các elements của nó. Collection interface được triển khai bởi tất cả các collections. Java platform không cung cấp bất kỳ implementations trực tiếp nào của intreface này nhưng cung cấp các implementations của các subinterfaces cụ thể hơn, chẳng hạn như Set và List.  

- **Set**: một collection không được chứa các elements trùng lặp, nó mô hình hóa sự trừu tượng hóa tập hợp toán học.  

- **List**: một collection có thứ tự. List có thể chứa các elements trùng lặp. Người dùng List thường có quyền kiểm soát chính xác vị trí mà mỗi element được chèn vào trong danh sách và có thể truy cập các elements bằng integer index (vị trí) của chúng.  

- **Queue**: một collection được sử dụng để chứa các elements trước khi xử lý. Bên cạnh các Collection operations cơ bản, Queue cung cấp thêm các thao tác chèn (insertion), trích xuất (extraction) và kiểm tra (inspection).  

    Các *Queue* thường, nhưng không nhất thiết phải sắp xếp các elements theo cách FIFO. Một trong số các trường hợp ngoại lệ là hàng đợi ưu tiên (priority queue), xếp thứ tự các elements theo một comparator được cung cấp hoặc thứ tự tự nhiên của các elements, bất kể thứ tự được sử dụng là gì, phần đầu của queue là element sẽ bị xóa bỏ bởi lệnh gọi *remove* hoặc *poll*. Trong FIFO queue, tất cả các elements mới được chèn vào cuối của queue. Các loại queue khác có thể sử dụng các quy tắc vị trí khác nhau. Mọi Queue implementation phải chỉ định các thuộc tính sắp xếp của nó.  

- **Deque**: một collection được sử dụng để chứa các elements trước khi xử lý. Bên cạnh các Collection operations cơ bản, Dueue cung cấp thêm các thao tác chèn (insertion), trích xuất (extraction) và kiểm tra (inspection).  

    Các *Deque* có thể được sử dụng dưới dạng FIFO và LIFO. Trong một deque, tất cả các element mới có thể được chèn, truy xuất và xóa bỏ ở cả 2 đầu.  

- **Map**: một object ánh xạ các keys đến các values. Map không thể chứa các key trùng lặp; mỗi key có thể ánh xạ đến nhiều nhất một value.  

Hai core collection interfaces cuối cùng chỉ là các phiên bản được sắp xếp của *Set* và *Map*:  

- **SortedSet**: một *Set* lưu trữ các elements của nó theo thứ tự tăng dần. Một số operations bổ sung được cung cấp để tận dụng lợi ích của việc sắp xếp theo thứ tự. Các sorted set được sử dụng cho các tập hợp có thứ tự tự nhiên, chẳng hạn như danh sách từ vựng và danh sách thành viên.  

- **SortedMap**: một *Map* lưu trữ các ánh xạ của nó theo thứ tự key tăng dần. Các sorted map được sử dụng cho các collections có thứ tự tự nhiên của các cặp key/value, chẳng hạn như từ điển và danh bạ điện thoại.  