# Lession 2.1, I/O Streams

## 2.1.3, Buffered Streams

Hầu hết các ví dụ trong bài học cho đến nay đều sử dụng *unbuffered I/O* (I/O không có bộ đệm). Điều này có nghĩa là mỗi yêu cầu đọc hoặc ghi được xử lý trực tiếp bởi hệ điều hành. Điều này có thể làm cho một chương trình kém hiệu quả hơn nhiều, vì mỗi yêu cầu như vậy thường kích hoạt truy cập đĩa, hoạt động mạng hoặc một số hoạt động khác tương đối tốn kém.

Để giảm bớt loại phí tổn này, Java platform triển khai các *buffered I/O*. Các buffered input streams đọc dữ liệu từ một vùng nhớ được gọi là *buffer* (bộ đệm), native input API chỉ được gọi khi bộ đệm trống. Tương tự, các buffered output streams ghi dữ liệu vào bộ đệm và native output API chỉ được gọi khi bộ đệm đầy.

Một chương trình có thể chuyển đổi một unbuffered stream thành một buffered stream bằng cách gọi constructor của một buffered stream class và truyền một unbuffered stream object cho nó.

*Ví dụ:*

```java
inputStream = new BufferedReader(new FileReader("xanadu.txt"));
outputStream = new BufferedWriter(new FileWriter("characteroutput.txt"));
```

Có 4 buffered stream class được sử dụng để wrap (bao bọc) các unbuffered streams: 

- *BufferedInputStream* và *BufferedOutputStream* tạo các buffered byte streams,   
- *BufferedReader* và *BufferedWriter* tạo các buffered character streams.


### *Flushing Buffered Streams*

Việc ghi ra vùng đệm mà không cần đợi nó đầy được gọi là xả bộ đệm.

Một số buffered output classes hỗ trợ *autoflush*, được chỉ định bởi một constructor argument tùy chọn. Khi kích hoạt autoflush, một số sự kiện quan trọng sẽ được kích hoạt để tiến hành xả bộ đệm. 

*Ví dụ, một autoflush PrintWriter object sẽ xóa bộ đệm trên mọi lệnh gọi println hoặc format.*

Để xả một stream theo cách thủ công, cần gọi *flush* method của nó. flush method hợp lệ trên bất kỳ output stream nào, nhưng chỉ có tác dụng khi stream đó là buffered.