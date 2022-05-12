# Lession 2.1, I/O Streams

## 2.1.5, I/O from the Command Line

Một chương trình thường được chạy từ command line và tương tác với người dùng trong môi trường command line. Java platform hỗ trợ loại tương tác này theo 2 cách: thông qua *Standard Stream* và thông qua *Console*.


### *Standard Streams*

*Standard Streams* là một tính năng của nhiều hệ điều hành. Theo mặc định, chúng đọc input từ bàn phím và ghi output vào màn hình. Chúng cũng hỗ trợ I/O trên các file và giữa các chương trình, nhưng tính năng đó được kiểm soát bởi trình thông dịch command line, không phải chương trình.

Java platform hỗ trợ 3 Standard Streams: *Standard Input*, được truy cập thông qua *System.in*; *Standard Output*, được truy cập thông qua *System.out*; và *Standard Error*, được truy cập thông qua *System.err*. Các object này được định nghĩa tự động và không cần phải được mở. Cả Standard Output và Standard Error đều dành cho output; error ouput riêng biệt cho phép người dùng chuyển hướng output thông thường sang file và vẫn có thể đọc error message. 

Bạn có thể mong đợi các Standard Streams là các character streams, nhưng chúng là các byte streams. 

*System.out* và *System.err* được định nghĩa là các *PrintStream* object. Mặc dù về mặt kỹ thuật nó là một byte stream, nhưng PrintStream sử dụng một character stream object nội bộ để mô phỏng nhiều tính năng của các character streams.

Ngược lại, *System.in* được định nghĩa là một *InputStream* byte stream object, và không có các tính năng của character streams. Để sử dụng Standard Input như là một character stream, cần wrap (bao bọc) *System.in* trong *InputStreamReader*.

```java
InputStreamReader cin = new InputStreamReader(System.in);
```


### *The Console*

Một giải pháp thay thế nâng cao hơn cho Standard Stream là Console. Đây là một object duy nhất, được định nghĩa trước của type *Console*, nó có hầu hết các tính năng được cung cấp bởi Standard Stream bên cạnh những tính năng khác. Console đặc biệt hữu ích để nhập mật khẩu an toàn. Console object cũng cung cấp các input và output streams là các character streams, thông qua các *reader* và *writer* method của nó.

Trước khi một chương trình có thể sử dụng Console, nó phải cố gắng truy xuất Console object bằng cách gọi *System.console()*. Nếu Console object có sẵn, thì method này sẽ trả về nó. Nếu *System.console* trả về *NULL*, thì các hoạt động của Console không được phép, vì Hệ điều hành không hỗ trợ chúng hoặc vì chương trình được khởi chạy trong môi trường không tương tác.

Console object hỗ trợ nhập mật khẩu an toàn thông qua *readPassword* method của nó. Method này giúp nhập mật khẩu an toàn theo 2 cách: Đầu tiên, nó ngăn chặn echo, vì vậy mật khẩu không hiển thị trên màn hình của người dùng. Thứ hai, readPassword trả về một character array, không phải một String, vì vậy mật khẩu có thể được ghi đè, xóa nó khỏi bộ nhớ ngay khi không còn cần thiết.

*Ví dụ: Thay đổi password của nười dùng, sử dụng các Console methods:*

```java
import java.io.Console;
import java.util.Arrays;
import java.io.IOException;

public class Password {
    
    public static void main (String args[]) throws IOException {

        Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }

        String login = c.readLine("Enter your login: ");
        char [] oldPassword = c.readPassword("Enter your old password: ");

        if (verify(login, oldPassword)) {
            boolean noMatch;
            do {
                char [] newPassword1 = c.readPassword("Enter your new password: ");
                char [] newPassword2 = c.readPassword("Enter new password again: ");
                noMatch = ! Arrays.equals(newPassword1, newPassword2);
                if (noMatch) {
                    c.format("Passwords don't match. Try again.%n");
                } else {
                    change(login, newPassword1);
                    c.format("Password for %s changed.%n", login);
                }
                Arrays.fill(newPassword1, ' ');
                Arrays.fill(newPassword2, ' ');
            } while (noMatch);
        }

        Arrays.fill(oldPassword, ' ');
    }
    
    static boolean verify(String login, char[] password) {
        return true;
    }

    static void change(String login, char[] password) {
        ...
    }
}
```

*Trong ví dụ trên, Password class làm theo các bước sau:*

- Cố gắng truy xuất Console object. Nếu object không có sẵn, hãy hủy bỏ.  
- Gọi Console.readLine để nhắc và đọc tên đăng nhập của người dùng.  
- Gọi Console.readPassword để nhắc và đọc mật khẩu hiện có của người dùng.  
- Gọi verify để xác nhận rằng người dùng được phép thay đổi mật khẩu.  
- Lặp lại các bước sau cho đến khi người dùng nhập cùng một mật khẩu 2 lần:  
    + Gọi Console.readPassword 2 lần để nhắc và đọc mật khẩu mới.  
    + Nếu người dùng đã nhập cùng một mật khẩu cả 2 lần, hãy gọi change để thay đổi mật khẩu đó.  
    + Ghi đè cả 2 mật khẩu bằng khoảng trống.  
- Ghi đè mật khẩu cũ bằng các khoảng trống.  