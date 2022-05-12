# Lession 2.1, I/O Streams

## 2.1.7, Object Streams

Cũng giống như các data streams hỗ trợ I/O của các primitive data types, các object streams hỗ trợ I/O của các objects. Hầu hết, nhưng không phải tất cả, các class tiêu chuẩn hỗ trợ serialization (tuần tự hóa) các objects của chúng bằng cách triển khai *Serializable* interface.

Các object stream class là *ObjectInputStream* và *ObjectOutputStream*. Các class này triển khai *ObjectInput* và *ObjectOutput*, là các subinterfaces của *DataInput* và *DataOutput*. Điều đó có nghĩa là tất cả các primitive data I/O methods cũng được triển khai trong các object streams. Vì vậy, một object stream có thể chứa hỗn hợp các primitive và object values. 

*Ví dụ: Chương trình ObjectStreams tương tự như DataStreams, thực hiện ghi ra một tập hợp các data records, sau đó đọc lại chúng, với một vài thay đổi: Đầu tiên, giá là BigDecimal objects, để thể hiện tốt hơn các giá trị phân số. Thứ hai, một Calender object được ghi vào tệp dữ liệu, cho biết ngày của hóa đơn:*

```java 
import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
 
public class ObjectStreams {
    static final String dataFile = "invoicedata";
 
    static final BigDecimal[] prices = { 
        new BigDecimal("19.99"), 
        new BigDecimal("9.99"),
        new BigDecimal("15.99"),
        new BigDecimal("3.99"),
        new BigDecimal("4.99") 
    };
    static final int[] units = { 12, 8, 13, 29, 50 };
    static final String[] descs = { 
        "Java T-shirt",
        "Java Mug",
        "Duke Juggling Dolls",
        "Java Pin",
        "Java Key Chain" 
    };
 
    public static void main(String[] args) 
        throws IOException, ClassNotFoundException {
 
  
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(dataFile))
            );
 
            out.writeObject(Calendar.getInstance());
            for (int i = 0; i < prices.length; i ++) {
                out.writeObject(prices[i]);
                out.writeInt(units[i]);
                out.writeUTF(descs[i]);
            }
        } finally {
            out.close();
        }
 
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(dataFile))
            );
 
            Calendar date = null;
            BigDecimal price;
            int unit;
            String desc;
            BigDecimal total = new BigDecimal(0);
 
            date = (Calendar) in.readObject();
 
            System.out.format ("On %tA, %<tB %<te, %<tY:%n", date);
 
            try {
                while (true) {
                    price = (BigDecimal) in.readObject();
                    unit = in.readInt();
                    desc = in.readUTF();
                    System.out.format(
                        "You ordered %d units of %s at $%.2f%n", unit, desc, price
                    );
                    total = total.add(price.multiply(new BigDecimal(unit)));
                }
            } catch (EOFException e) {}
            System.out.format("For a TOTAL of: $%.2f%n", total);
        } finally {
            in.close();
        }
    }
}
```

**Note**: Nếu *readObject()* không trả về object type mong đợi, việc cố gắng cast (ép kiểu) nó sang đúng type có thể ném ra một *ClassNotFoundException*.

*Trong ví dụ đơn giản trên, điều đó không thể xảy ra, vì vậy chúng tôi không cố gắng bắt ngoại lệ. Thay vào đó, chúng tôi thông báo cho compiler rằng chúng tôi đã biết về vấn đề này bằng cách thêm ClassNotFoundException vào throws clause của main method.*


### *Output and Input of Complex Objects*

Các *writeObject* và *readObject* methods rất dễ sử dụng, nhưng chúng chứa một số logic quản lý đối tượng rất phức tạp. Điều này không quan trọng đối với một lớp như Calendar chỉ đóng gói các primitive values. Nhưng nhiều objects chứa tham chiếu đến các objects khác. Nếu *readObject* là để tạo lại một object từ một stream, nó phải có khả năng phục hồi tất cả các objects được tham chiếu bởi object ban đầu. Các objects bổ sung này có thể có các tham chiếu riêng của chúng, ... Trong tình huống này, *writeObject* sẽ duyệt qua toàn bộ các object references và ghi tất cả các objects đó vào stream. Do đó, một lệnh gọi writeObject duy nhất có thể gây ra một số lượng lớn các objects được ghi vào stream.

*Ví dụ: writeObject được gọi để ghi một object duy nhất có tên là a. Object này chứa các tham chiếu đến các object b và c, trong khi b chứa các tham chiếu đến d và e. Gọi writeobject(a) không chỉ ghi a, mà tất cả các object cần thiết để tạo lại a, vì vậy 4 object khác cũng được ghi. Khi a được readObject đọc lại, 4 object khác cũng được đọc lại và tất cả các object references ban đầu được giữ nguyên:*

```
                        Stream
writeObject(a) --->   c  e  d  b  a  ---> readObject()
        
        --- a ---                       --- a ---
        |       |                       |       |
        v       v                       v       v
    --- b ---   c                   --- b ---   c
    |       |                       |       |
    v       v                       v       v
    d       e                       d       e
```

Điều gì sẽ xảy ra nếu 2 objects trên cùng một stream đều chứa các tham chiếu đến cùng một object. Liệu cả 2 đều tham chiếu đến một object duy nhất khi chúng được đọc lại? Câu trả lời là "có." Một stream chỉ có thể chứa một bản sao của một object, bất kể số lượng tham chiếu đến nó. Vì vậy, nếu bạn ghi tường minh một object vào một stream 2 lần, bạn thực sự chỉ ghi tham chiếu 2 lần. 

*Ví dụ: nếu đoạn mã sau ghi một object ob 2 lần vào một stream:*

```java
Object ob = new Object();
out.writeObject(ob);
out.writeObject(ob);
```

*thì mỗi writeObject phải khớp với một readObject, vì vậy code đọc lại stream sẽ trông giống như sau:*

```java
Object ob1 = in.readObject();
Object ob2 = in.readObject();
```

*Điều này dẫn đến 2 biến, ob1 và ob2, là các tham chiếu đến một object duy nhất.*

Tuy nhiên, nếu một object duy nhất được ghi vào 2 stream khác nhau, thì nó sẽ được duplicate một cách hiệu quả - một chương trình duy nhất đọc cả 2 luồng trở lại sẽ thấy 2 objects riêng biệt.