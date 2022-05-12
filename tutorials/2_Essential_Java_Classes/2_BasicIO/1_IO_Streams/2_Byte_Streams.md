# Lession 2.1, I/O Streams

## 2.1.1, Byte Streams

Các chương trình sử dụng các *byte streams* để thực hiện input (nhập) và output (xuất) các 8-bit bytes. 

Tất cả các *byte stream class* đều có nguồn gốc từ *InputStream* và *OutputStream*.


### *Using Byte Streams*

Có rất nhiều byte stream class. Để minh họa cách hoạt động của các byte stream, chúng ta sẽ tập trung vào các file I/O byte streams, *FileInputStream* và *FileOutputStream*. Các loại byte streams khác cũng được sử dụng theo cách tương tự; chúng khác nhau chủ yếu ở cách chúng được xây dựng.

*Ví dụ: Chương trình CopyBytes sử dụng các byte streams để sao chép file:*

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyBytes {
    public static void main(String[] args) throws IOException {

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream("xanadu.txt");
            out = new FileOutputStream("outagain.txt");
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
```

*CopyBytes dành phần lớn thời gian trong một vòng lặp đơn giản để đọc input stream và ghi output stream, mỗi lần một byte, như sau:*

```
            Input Stream
    I n    X a n a d u    d i d 
                          |
                -----------
                |
                v
    -------- read(b) <-------
    |           |           |
    |           v           |
    |   Integer Variable    |
    |       -----------     |
    |       |       d |     |
    |       -----------     |
    |           |           |
    |           v           |
    --------> write(b) ------
                |
                -----------
                          |
                          v
    I n    X a n a d u    d
            Output Stream
```

![Simple byte stream input and output](./img/byteStream.gif).


### *Always Close Streams*

Việc đóng một stream khi nó không còn cần thiết là rất quan trọng, nó giúp tránh rò rỉ tài nguyên nghiêm trọng.

*Trong ví dụ trên, CopyBytes sử dụng một finally block để đảm bảo rằng cả 2 streams sẽ được đóng ngay cả khi xảy ra lỗi.* 

*Một lỗi có thể xảy ra là CopyBytes không thể mở 1 hoặc cả 2 files. Khi điều đó xảy ra, stream variable tương ứng với file không bao giờ thay đổi giá trị null ban đầu của nó. Đó là lý do tại sao CopyBytes đảm bảo rằng mỗi stream variable chứa một object reference trước khi gọi close method.*


### *When Not to Use Byte Streams*

*CopyBytes có vẻ giống như một chương trình bình thường, nhưng nó thực sự đại diện cho một loại low-level I/O mà bạn nên tránh, vì xanadu.txt chứa character data, nên cách tốt nhất là sử dụng các Character Streams.* 

Ngoài ra, còn có các streams khác cho các data types phức tạp hơn. Các byte streams chỉ nên được sử dụng cho I/O nguyên thủy nhất.

Tất cả các stream types khác đều được xây dựng dựa trên byte streams.