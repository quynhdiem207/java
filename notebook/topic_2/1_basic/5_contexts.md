## 1. Context (ngữ cảnh)

Mọi expression trong Java hoặc không có kết quả hoặc có một type được suy ra (*deduced type*) tại compile time.

Khi một expression xuất hiện trong hầu hết context, nó phải *compatible* (tương thích) với type được mong đợi (*target type*) trong context đó.

Khả năng tương thích của một expression với context xung quanh được tạo điều kiện theo 2 cách:  

- Đối với một số expression được gọi là *poly expression* (expression chứa expression khác), *deduced type* (type suy luận) có thể bị ảnh hưởng bởi *target type*. Vì vậy, cùng một expression có thể có type khác nhau trong các context khác nhau.  
- Sau khi type được suy luận, đôi khi có thể thực hiện *implicit conversion* (chuyển đổi ngầm) từ type của expression sang target type.  

Nếu cả 2 cách đều không thể tạo ra type thích hợp, sẽ xảy ra *compile-time error*.  

Các loại *conversion context* mà trong đó *poly expression* có thể bị ảnh hưởng bởi context or *implicit conversion* có thể xảy ra, mỗi loại sẽ có *rules* khác nhau:  

- **Assignment contexts**: cho phép gán value của expression cho một variable, type của expression phải được convert thành type của variable.  
    + Nếu expression là một *constant expression* của type byte, short, char, or int:  
        + *narrowing primitive conversion* có thể được sử dụng nếu type của variable là type byte, short, or char, và value của constant expression có thể được biểu diễn ở type của variable.  
        + *narrowing primitive conversion* sau đó *boxing conversion* có thể được sử dụng nếu variable có type Byte, Short or Character, và value của constant expression có thể biểu diễn ở type byte, short hoặc char tương ứng.  

        ```java
        class Test {
            public static void main(String[] args) {
                byte b = (byte)42;  // cast is permitted but not required
                short s = 123;
                char c = s;    // error: would require cast
                s = c;         // error: would require cast
            }
        }
        ```  
    + null reference có thể được gán cho bất cứ reference type nào.  
    + Các exception có thể xảy ra:  
        + ClassCastException: là kết quả của reference conversion khi class thực tế của object được tham chiếu bởi giá trị của biểu thức toán hạng không tương thích với target type được xác định bởi cast operator,  
        + OutOfMemoryError: kết quả của boxing conversion khi không đủ bộ nhớ cấp phát cho instance mới của wrapper class,  
        + NullPointerException: kết quả của unboxing conversion với null reference,  
        + ArrayStoreException: xảy ra trong các trường hợp gán cho array elements của reference type một reference value của một object có type thực tế không tương thích với component type thực tế của mảng.  

- **Invocation contexts**: cho phép một argument value trong lệnh gọi method or constructor được gán cho formal parameter (tham số hình thức) tương ứng.  

    Strict invocation contexts cho phép:  

    + identity conversion,  
    + widening primitive conversion,  
    + widening reference conversion.  

    Loose invocation contexts cho phép:  

    + identity conversion,  
    + widening primitive conversion,  
    + widening reference conversion,  
    + boxing conversion có thể theo sau bởi widening reference conversion,  
    + unboxing conversion có thể theo sau bởi widening primitive conversion.  

    ```java
    class Test {
        static int m(byte a, int b) { return a+b; }
        static int m(short a, short b) { return a-b; }
        public static void main(String[] args) {
            System.out.println(m(12, 2));       // compile-time error
            System.out.println(m((byte)12, 2)); // OK
        }
    }
    ```

- **Casting contexts**: cho phép operand (toán hạng) của một cast expression được convert tường minh sang một type xác định bởi cast operator.  
- **String contexts**: chỉ áp dụng cho toán hạng không phải String của toán tử + khi toán hạng khác là String, value của bất cứ type nào được convert thành một object của String type.  
- **Numeric contexts**: áp dụng cho các toán hạng của một arithmetic operator (toán tử số học) có thể được mở rộng (widened) thành một type để thực hiện tính toán.  