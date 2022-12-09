# Lession 3. Concurrency

## 6. Immutable Objects

Một object được coi là *immutable* (bất biến) nếu state của nó không thể thay đổi sau khi nó được tạo. Sự phụ thuộc tối đa vào các immutable object được chấp nhận rộng rãi như một chiến lược hợp lý để tạo mã đơn giản, đáng tin cậy.

Các immutable object đặc biệt hữu ích trong các ứng dụng đồng thời. Vì chúng không thể thay đổi trạng thái, chúng không thể bị hỏng do thread interference (can thiệp luồng) hoặc quan sát ở trạng thái không nhất quán.

Các programmers thường không muốn sử dụng các immutable objects, bởi vì họ lo lắng về phí tổn tạo một object mới thay vì cập nhật một object tại chỗ. Tác động của việc tạo object thường được đánh giá quá cao và có thể được bù đắp bằng một số hiệu quả liên quan đến các immutable objects. Chúng bao gồm giảm phí tổn do thu thập rác và loại bỏ mã cần thiết để bảo vệ các mutable object khỏi bị hỏng.


### 6.1, A Synchronized Class Example

*Ví dụ: SynchronizedRGB class, định nghĩa các object đại diện cho màu sắc. Mỗi object đại diện cho màu dưới dạng ba số nguyên đại diện cho các giá trị màu chính và một chuỗi cung cấp tên của màu:*

```java
public class SynchronizedRGB {

    // Values must be between 0 and 255.
    private int red;
    private int green;
    private int blue;
    private String name;

    private void check(int red, int green, int blue) {
        if (
            red < 0 || red > 255
            || green < 0 || green > 255
            || blue < 0 || blue > 255
        ) {
            throw new IllegalArgumentException();
        }
    }

    public SynchronizedRGB(int red, int green, int blue, String name) {
        check(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
    }

    public void set(int red, int green, int blue, String name) {
        check(red, green, blue);
        synchronized (this) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.name = name;
        }
    }

    public synchronized int getRGB() {
        return ((red << 16) | (green << 8) | blue);
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void invert() {
        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;
        name = "Inverse of " + name;
    }
}
```

*SynchronizedRGB phải được sử dụng cẩn thận để tránh bị nhìn thấy ở trạng thái không nhất quán. Ví dụ, giả sử một thread thực thi đoạn mã sau:*

```java
SynchronizedRGB color = new SynchronizedRGB(0, 0, 0, "Pitch Black");
// ...
int myColorInt = color.getRGB();      // Statement 1
String myColorName = color.getName(); // Statement 2
```

*Nếu một thread khác gọi color.set sau Statement 1 nhưng trước Statement 2, giá trị của myColorInt sẽ không khớp với giá trị của myColorName. Để tránh kết quả này, 2 câu lệnh phải được ràng buộc với nhau:*

```java
synchronized (color) {
    int myColorInt = color.getRGB();
    String myColorName = color.getName();
} 
```

*Loại không nhất quán này chỉ có thể xảy ra đối với các mutable objects - nó sẽ không phải là vấn đề đối với immutable version của SynchronizedRGB.*


### 6.2, A Strategy for Defining Immutable Objects

Các quy tắc sau đây xác định một chiến lược đơn giản để tạo các immutable objects. Không phải tất cả các class được ghi nhận là "immutable" đều tuân theo các quy tắc này. Điều này không nhất thiết có nghĩa là những người tạo ra các class này đã cẩu thả - họ có thể có lý do chính đáng để tin rằng các instances của các class của họ không bao giờ thay đổi sau khi được tạo. Tuy nhiên, những chiến lược như vậy đòi hỏi sự phân tích phức tạp và không dành cho người mới bắt đầu:

1. Không cung cấp các "setter" methods - các methods sửa đổi các fields hoặc objects được các fields tham chiếu đến.  
2. Khai báo tất cả các fields đều là *final* và *private*.  
3. Không cho phép các subclass ghi đè các methods. Cách đơn giản nhất để làm điều này là khai báo class là *final*. Một cách phức tạp hơn là khai báo constructor là *private* và tạo các instances trong các factory methods.  
4. Nếu các instance fields bao gồm các tham chiếu đến các mutable objects, không cho phép thay đổi các objects đó:  
    - Không cung cấp các methods sửa đổi các mutable objects đó.  
    - Không chia sẻ tham chiếu đến các mutable objects. Không bao giờ lưu trữ các tham chiếu ra bên ngoài, các mutable objects được truyền cho constructor; nếu cần, hãy tạo các bản copy và lưu trữ các tham chiếu đến các bản copy. Tương tự, hãy tạo bản copy của các mutable objects nội bộ của bạn khi cần thiết để tránh trả lại bản gốc trong các methods của bạn.  

*Ví dụ: Áp dụng chiến lược này cho SynchronizedRGB dẫn đến các bước sau:*

1. *Có 2 setter methods trong class này: set - biến đổi object một cách tùy ý mà điều này không được phép trong immutable version của class, invert, có thể được điều chỉnh bằng cách tạo một object mới thay vì sửa đổi object hiện có.*  
2. *Tất cả các fields đã là private; cần thiết lập thêm cho chúng là final.*  
3. *Khai báo bản thân class là final.*  
4. *Chỉ một field tham chiếu đến một object và bản thân object đó là bất biến. Do đó, không cần các biện pháp bảo vệ để chống lại việc thay đổi trạng thái của các mutable objects.*  

*Sau những thay đổi này, chúng ta có ImmutableRGB:*

```java
final public class ImmutableRGB {

    // Values must be between 0 and 255.
    final private int red;
    final private int green;
    final private int blue;
    final private String name;

    private void check(int red, int green, int blue) {
        if (
            red < 0 || red > 255
            || green < 0 || green > 255
            || blue < 0 || blue > 255
        ) {
            throw new IllegalArgumentException();
        }
    }

    public ImmutableRGB(int red, int green, int blue, String name) {
        check(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
    }


    public int getRGB() {
        return ((red << 16) | (green << 8) | blue);
    }

    public String getName() {
        return name;
    }

    public ImmutableRGB invert() {
        return new ImmutableRGB(
            255 - red, 
            255 - green, 
            255 - blue, 
            "Inverse of " + name
        );
    }
}
```