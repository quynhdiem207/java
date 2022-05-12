# Lession 2.1, I/O Streams

## 2.1.6, Data Streams

Các Data streams hỗ trợ binary I/O của các primitive data type values (boolean, char, byte, short, int, long, float và double) cũng như các String values. 

Tất cả các data streams đều triển khai *DataInput* interface hoặc *DataOutput* interface, điển hình là  *DataInputStream* và *DataOutputStream*.

*Ví dụ: DataStreams minh họa các data streams bằng cách ghi ra một tập hợp các data records, sau đó đọc lại chúng. Mỗi bản ghi bao gồm 3 giá trị liên quan đến một mặt hàng trên hóa đơn, như được hiển thị trong bảng sau:*

```
Order |	Data type |	Description |	    Output Method          |	    Input Method	    | Sample Value
------------------------------------------------------------------------------------------------------------
1	  | double	  | Item price	| DataOutputStream.writeDouble | DataInputStream.readDouble | 19.99
2	  | int	      | Unit count	| DataOutputStream.writeInt	   | DataInputStream.readInt	| 12
3	  | String	  | Item desc   | DataOutputStream.writeUTF	   | DataInputStream.readUTF	| "Java T-Shirt"
```

*Trong ví dụ trên, đầu tiên, chương trình xác định một số hằng số chứa tên của tệp dữ liệu và dữ liệu sẽ được ghi vào đó:*

```java
static final String dataFile = "invoicedata";

static final double[] prices = { 19.99, 9.99, 15.99, 3.99, 4.99 };
static final int[] units = { 12, 8, 13, 29, 50 };
static final String[] descs = {
    "Java T-shirt",
    "Java Mug",
    "Duke Juggling Dolls",
    "Java Pin",
    "Java Key Chain"
};
```

*Sau đó, DataStreams open một output stream. Vì DataOutputStream chỉ có thể được tạo dưới dạng một wrapper cho một byte stream object hiện có, DataStreams cung cấp một buffered file output byte stream:*

```java
out = new DataOutputStream(
    new BufferedOutputStream(new FileOutputStream(dataFile))
);
```
*DataStreams viết ra các records and đóng output stream:*

```java
for (int i = 0; i < prices.length; i ++) {
    out.writeDouble(prices[i]);
    out.writeInt(units[i]);
    out.writeUTF(descs[i]);
}
```

*writeUTF* ghi ra các String values ở dạng UTF-8 đã được sửa đổi. Đây là kiểu mã hóa ký tự có độ rộng thay đổi chỉ cần một byte duy nhất cho các ký tự phương Tây thông thường.

*Bây giờ, chương trình DataStreams đọc lại dữ liệu một lần nữa. Đầu tiên nó phải cung cấp một input stream và các biến để chứa input data. Giống như DataOutputStream, DataInputStream phải được tạo dưới dạng một wrapper cho một byte stream:*

```java
in = new DataInputStream(
    new BufferedInputStream(new FileInputStream(dataFile))
);

double price;
int unit;
String desc;
double total = 0.0;
```

*Giờ đây, DataStreams có thể đọc từng record trong stream, báo cáo về dữ liệu mà nó gặp phải:*

```java
try {
    while (true) {
        price = in.readDouble();
        unit = in.readInt();
        desc = in.readUTF();
        System.out.format(
            "You ordered %d units of %s at $%.2f%n", unit, desc, price
        );
        total += unit * price;
    }
} catch (EOFException e) { ... }
```

Lưu ý rằng DataStreams phát hiện điều kiện cuối tệp bằng cách bắt EOFException, thay vì kiểm tra giá trị trả về không hợp lệ. Tất cả các triển khai của các DataInput methods đều sử dụng EOFException thay vì trả về các giá trị.

Cũng lưu ý rằng mỗi *write* method chuyên biệt trong DataStreams sẽ khớp chính xác với một *read* method chuyên biệt tương ứng. Programmer phải đảm bảo rằng các các output types và các input types phải khớp với nhau theo cách sau: input stream bao gồm binary data đơn giản, không có điều gì để chỉ ra type của các giá trị riêng biệt hoặc vị trí bắt đầu của chúng trong stream.

DataStreams sử dụng một kỹ thuật lập trình rất tệ: nó sử dụng các floating point numbers để biểu thị các giá trị tiền tệ. Nói chung, floating point không tốt cho các giá trị chính xác. Nó đặc biệt không tốt cho các phân số thập phân, vì các giá trị phổ biến (chẳng hạn như 0.1) không có biểu diễn nhị phân.  

Type chính xác để sử dụng cho các giá trị tiền tệ là *java.math.BigDecimal*. Thật không may, BigDecimal là một object type, vì vậy nó sẽ không hoạt động với các data streams. Tuy nhiên, BigDecimal sẽ hoạt động với các object streams.

*Ví dụ: Chương trình DataStreams đầy đủ thực hiện ghi ra một tập hợp các data records, sau đó đọc lại chúng như sau:*

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.EOFException;
 
public class DataStreams {
    static final String dataFile = "invoicedata";
 
    static final double[] prices = { 19.99, 9.99, 15.99, 3.99, 4.99 };
    static final int[] units = { 12, 8, 13, 29, 50 };
    static final String[] descs = { 
        "Java T-shirt",
        "Java Mug",
        "Duke Juggling Dolls",
        "Java Pin",
        "Java Key Chain" 
    };
 
    public static void main(String[] args) throws IOException {
 
  
        DataOutputStream out = null;
         
        try {
            out = new DataOutputStream(new
                BufferedOutputStream(new FileOutputStream(dataFile))
            );
 
            for (int i = 0; i < prices.length; i ++) {
                out.writeDouble(prices[i]);
                out.writeInt(units[i]);
                out.writeUTF(descs[i]);
            }
        } finally {
            out.close();
        }
 
        DataInputStream in = null;
        double total = 0.0;
        try {
            in = new DataInputStream(new
                BufferedInputStream(new FileInputStream(dataFile))
            );
 
            double price;
            int unit;
            String desc;
 
            try {
                while (true) {
                    price = in.readDouble();
                    unit = in.readInt();
                    desc = in.readUTF();
                    System.out.format(
                        "You ordered %d units of %s at $%.2f%n", unit, desc, price
                    );
                    total += unit * price;
                }
            } catch (EOFException e) { }
            System.out.format("For a TOTAL of: $%.2f%n", total);
        }
        finally {
            in.close();
        }
    }
}
```