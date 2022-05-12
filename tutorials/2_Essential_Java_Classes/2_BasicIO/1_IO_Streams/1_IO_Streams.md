# Lession 2.1, I/O Streams

Một *stream* là một chuỗi dữ liệu. 

Stream hỗ trợ nhiều loại data khác nhau, bao gồm: simple bytes, primitive types, localized characters và objects. Một số streams chỉ đơn giản là truyền dữ liệu; những streams khác thao tác và chuyển đổi dữ liệu theo những cách hữu ích.

Bất kể chúng hoạt động nội bộ như thế nào, tất cả các stream đều hiển thị cùng một mô hình đơn giản cho các chương trình sử dụng chúng: 

- Một chương trình sử dụng một *input stream* để đọc dữ liệu từ một nguồn, một item tại một thời điểm:

    ```
    - Reading infomation into a program:

                Stream
                ------->  ------->
    Data source  01000110  10100110  Program
                ------->  ------->
    ```

- Một chương trình sử dụng một *output stream* để ghi dữ liệu tới một đích, một item tại một thời điểm:

    ```
    - Writing infomation from a program:

                Stream
            ------->  ------->
    Program  01000110  10100110  Data destination
            ------->  ------->
    ```

Các streams có thể xử lý tất cả các loại dữ liệu, từ các primitive values đến các objects.

Data source - nguồn dữ liệu (hay input source - nguồn đầu vào) và data destination - đích dữ liệu (hay output destination - đích đầu ra) có thể là bất kỳ thứ gì mà chứa, tạo hoặc sử dụng dữ liệu. Chúng có thể là các disk file (tệp đĩa), cũng có thể là một chương trình khác, một thiết bị ngoại vi, một ổ cắm mạng hoặc một mảng bộ nhớ.