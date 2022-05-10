# Lession 1. Object-Oriented Programming Concepts

- **Object**: là một gói phần mềm bao gồm các trạng thái (state) và hành vi (behavior) liên quan. Các đối tượng phần mềm (software object) thường được sử dụng để mô hình hóa các đối tượng trong thế giới thực (real-world object).  
- **Class**: Là một bản thiết kế hoặc nguyên mẫu mà từ đó objects được tạo ra, mô hình hóa trạng thái (state) và hành vi (behavior) của đối tượng trong thế giới thực (real-world object).  
- **Inheritance**: là sự kế thừa trạng thái (state) và hành vi (behavior) từ superclass của một subclass.    
- **Interface**: là một hợp đồng giữa một class và thế giới bên ngoài. Khi một class triển khai một interface, nó hứa hẹn cung cấp hành vi (behavior) được phát hành bởi interface đó.  
- **Package**: là một namespace để tổ chức các class và interface một cách hợp lý, giúp quản lý các dự án phần mềm lớn dễ dàng hơn.  


## 1. Object

Các real-world objects đều có trạng thái (state) và hành vi (behavior).

Các software objects tương tự với các real-world objects: chúng cũng bao gồm state và behavior liên quan. Một object chứa state của nó trong các *fields* (hay còn gọi là các variables) và thể hiện behavior của nó thông qua các *methods* (hay còn gọi là các functions). Các methods hoạt động trên state bên trong của một object và đóng vai trò là cơ chế chính trong việc giao tiếp object-to-object. Việc ẩn state bên trong và yêu cầu tất cả các tương tác phải được thực hiện thông qua các methods của một object được gọi là *data encapsulation* (đóng gói dữ liệu) — một nguyên tắc cơ bản của OOP.

Việc gói code vào các software object riêng lẻ mang lại một số lợi ích, bao gồm:

- Tính mô-đun: Mã nguồn cho một object có thể được viết và duy trì độc lập với mã nguồn cho các object khác.  
- Che giấu thông tin: Bằng cách chỉ tương tác với các methods của một object, các chi tiết về việc triển khai bên trong của nó vẫn bị che giấu khỏi thế giới bên ngoài.  
- Tái sử dụng mã: Nếu một object đã tồn tại (có thể được viết bởi một developer khác), bạn có thể sử dụng object đó trong chương trình của mình.  
- Tính dễ hiểu và dễ gỡ lỗi: Nếu một object cụ thể có vấn đề, bạn có thể chỉ cần xóa nó khỏi ứng dụng của mình và cắm một object khác làm object thay thế.  


## 2. Class

Một class là một bản thiết kế mà từ đó các objects riêng lẻ được tạo ra.


## 3. Inheritance

Các loại objects thường có một số điểm chung nhất định với nhau. 

OOP cho phép các class kế thừa state và behavior chung từ các class khác. Do đó, cho phép tập trung vào các đặc điểm làm nó trở nên khác biệt.  

Trong Java, mỗi class được phép có một superclass trực tiếp và mỗi superclass có thể có số lượng subclass không giới hạn.  


## 4. Interface

Các objects định nghĩa sự tương tác của chúng với thế giới bên ngoài thông qua các methods mà chúng đưa ra. Các methods hình thành interface của object với thế giới bên ngoài.  

*Ví dụ: Các nút của TV là interface giữa bạn và hệ thống dây điện ở phía bên kia của vỏ nhựa. Bạn ấn nút "nguồn" để mở và tắt TV.*

Triển khai một interface làm cho class trở nên hình thức hơn về các behavior mà nó hứa sẽ cung cấp. Interfaces tạo thành một hợp đồng giữa class và thế giới bên ngoài, và hợp đồng này được thực hiện tại build time bởi compiler. Nếu class tuyên bố triển khai một interface, tất cả các methods được định nghĩa bởi interface đó phải xuất hiện trong source code của nó trước khi class đó compile thành công.


## 5. Package

Một package là một namespace tổ chức một tập các class và interface có liên quan.  


## Questions

- Real-world objects chứa *state* và *behavior*.    
- Một state của software object được chứ trong các *fields*.  
- Một behavior của software object được thể hiện thông qua các *methods*.  
- Ẩn dữ liệu nộ bộ khỏi thế giới bên ngoài, và chỉ truy cập nó thông qua các methods đưa ra công khai được gọi là *data encapsulation*.  
- Một bản thiết kế cho một software object được gọi là một *class*.  
- Behavior chung có thể được định nghĩa trong một *superclass* và được kế thừa vào trong một *subclass* sử dụng *extends* keyword.  
- Một tập hợp các methods không có triển khai được gọi là một *interface*.  
- Một namespace tổ chức các class và interface theo chức năng được gọi là một *package*.  
- Thuật ngữ API là viết tắt của *Application Programming Interface*.  