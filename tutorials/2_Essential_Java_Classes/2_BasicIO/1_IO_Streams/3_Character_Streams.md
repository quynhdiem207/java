# Lession 2.1, I/O Streams

## 2.1.2, Character Streams

Java platform lưu trữ các character values bằng cách sử dụng định dạng Unicode. Character stream I/O tự động dịch định dạng nội bộ này sang và từ local character set. Trong Western locales (ngôn ngữ phương Tây), local character set thường là 8-bit superset của ASCII.

Đối với hầu hết các ứng dụng, I/O với các character streams không phức tạp hơn so với I/O với các byte streams. Input và output được thực hiện với các stream class tự động dịch sang và từ local character set. Một chương trình sử dụng các character streams thay cho các byte streams sẽ tự động điều chỉnh với local character set và sẵn sàng để quốc tế hóa - tất cả mà programmer không cần nỗ lực thêm.

Nếu quốc tế hóa không phải là ưu tiên, bạn có thể chỉ sử dụng các character stream class mà không cần quan tâm nhiều đến các vấn đề về character set. Sau đó, nếu quốc tế hóa trở thành ưu tiên, chương trình của bạn có thể được điều chỉnh mà không cần phí công viết lại mã.


### *Using Character Streams*

Tất cả các *character stream classes* đều có nguồn gốc từ *Reader* và *Writer*. Giống với byte streams, có các character stream classes riêng dành cho file I/O: *FileReader* và *FileWriter*.

*Ví dụ: Chương trình CopyCharacters sử dụng các character streams để sao chép file:*

```java
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyCharacters {
    public static void main(String[] args) throws IOException {

        FileReader inputStream = null;
        FileWriter outputStream = null;

        try {
            inputStream = new FileReader("xanadu.txt");
            outputStream = new FileWriter("characteroutput.txt");

            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
```

*Chương trình CopyCharacters rất giống với CopyBytes. Sự khác biệt quan trọng nhất là CopyCharacters sử dụng FileReader và FileWriter cho input và output thay cho FileInputStream và FileOutputStream. Lưu ý rằng cả CopyBytes và CopyCharacters đều sử dụng một biến int để đọc và ghi từ nó. Tuy nhiên, trong CopyCharacters, biến int giữ một character value trong 16 bit cuối cùng của nó; còn trong CopyBytes, biến int giữ một byte value trong 8 bit cuối cùng của nó.*


### *Character Streams that Use Byte Streams*

Các character streams thường là "wrappers" (bao ngoài) cho các byte streams. Các character streams sử dụng các byte streams để thực hiện I/O vật lý, trong quá trình xử lý việc dịch giữa các characters và bytes.

*Ví dụ: FileReader sử dụng FileInputStream, trong khi FileWriter sử dụng FileOutputStream.*

Có 2 "bridge" stream với mục đích dịch byte-to-character: *InputStreamReader* và *OutputStreamWriter*. Sử dụng chúng để tạo character streams khi không có character stream class nào đáp ứng nhu cầu của bạn.  


### *Line-Oriented I/O*

Character I/O thường xảy ra trong các đơn vị lớn hơn các ký tự đơn lẻ. Một đơn vị phổ biến là line: một chuỗi ký tự với một line terminator ở cuối. Một line terminator có thể là một carriage-return/line-feed sequence ("\r\n"), một carriage-return ("\r") hoặc một line-feed ("\n"). Hỗ trợ tất cả các line terminator cho phép các chương trình đọc các tệp văn bản được tạo trên bất kỳ hệ điều hành nào.

Để sử dụng Line-Oriented I/O, cần sử dụng 2 class: *BufferedReader* và *PrintWriter*.

*Ví dụ: Chương trình CopyLines gọi BufferedReader.readLine và PrintWriter.println để thực hiện input và output từng dòng một:*

```java
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

public class CopyLines {
    public static void main(String[] args) throws IOException {

        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader("xanadu.txt"));
            outputStream = new PrintWriter(new FileWriter("characteroutput.txt"));

            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
```

*Gọi readLine trả về một dòng văn bản. CopyLines xuất ra từng dòng bằng println, trong khi đó nó sẽ thêm một line terminator phù hợp với hệ điều hành hiện tại, mà có thể không phải là cùng line terminator đã được sử dụng trong input file.*

Có nhiều cách để cấu trúc input và output văn bản ngoài các characters và lines.