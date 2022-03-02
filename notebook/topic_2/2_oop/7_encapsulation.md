## 3. Encapsulation

Encapsulation (tính bao đóng) giúp ẩn giấu thông tin, dữ liệu bên trong, tăng tính bảo mật.

Để làm được điều này, cta cần:  

- Khai báo các attributes là private.  
- Khi get & set value của các attributes này cần thông qua getters & setters, các class khác không thể truy xuất trực tiếp đến các attributes này, nhờ đó ta có thể xử lý các logic khác như validate.  
- Nhóm các classes & interfaces vào các packages khác nhau.  