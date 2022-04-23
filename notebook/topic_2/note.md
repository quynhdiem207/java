## L0 - Topic 2

- String buffer là thread-safe (hỗ trợ đa luồng).  
- String builder không thread-safe, tốc độ nhanh hơn String buffer.   

- String pool là một phần của heap.  
- tạo string với new sẽ lưu trong heap, không phải trong string pool.  

- Có 3 cách so sánh string:   
    - ==,  
    - equals (so sánh giá trị chuỗi),  
    - compareTo (so sánh giá trị chuỗi, dựa trên giá trị unicode của mỗi ký tự trong các chuỗi, trả về số nguyên là hiệu giá trị số của 2 ký tự đầu tiên khác nhau của 2 chuỗi).  

- Riêng đối với String có thể khởi tạo một object không cần constructor mà sử dụng string literal, vì các string đó được lưu trong String constant pool.  

- Không thể sử dụng toán tử == so sánh bằng giá trị 2 số thực (type float hoặc double), vì cách Java lưu trữ giá trị kiểu số thực, so sánh như vậy kết quả sẽ không chính xác. Để kiểm tra 2 số thực có bằng nhau hay không, cần kiểm tra giá trị của 2 số có hơn kém nhau với khoảng cách quá nhỏ hay không, khoảng cách này là bao nhiêu tùy thuộc vào độ chính xác mong muốn (double <= 1e-9, float <= 1e-6):  
    ```java
    double x = 0.3 * 3 + 0.1;
    double y = 1.0;
    System.out.println(x == y);          // false
    System.out.println(x + " - " + y);   // 0.9999999999999999 - 1
    System.out.println(Math.abs(x - y) <= 1e-9); // true => kết luận x == y

    float a = 10f - 0.1f;
    float b = 10f - 0.1f - 0.1f;
    System.out.println(a == 9.9f);       // true
    System.out.println(b == 9.8f);       // false
    System.out.println(a + " - " + b);   // 9.9 - 9.799999
    System.out.println(Math.abs(9.8f - b) <= 1e-6); // true => kết luận 9.8f == b
    ```

- ép kiểu nguyên thủy có thể gây nên việc mất một số bit dữ liệu làm mất sự chính xác.  

- static members là đặc trưng của class hoặc interface, nó có thể được truy cập mà không cần tạo object.  

- Thường sử dụng public static final để khai báo hằng số vì:  
    - Có thể sử dụng ở bất kỳ đâu, nhưng không thể thay đổi giá trị của chúng,  
    - Có thể sử dụng mà không cần khởi tạo object.  

- Primitive type và wrapper type:  
    - Mục đích: Đều được sử dụng để lưu trữ data,  
        + Primitive type là các data type được định nghĩa trước bởi Java.  
        + Wrapper type là các class types cung cấp cơ chế chuyển đổi primitive type thành object (boxing) và object thành primitive type (unboxing).  
    
    - Class liên kết:  
        + Một Primitive type không phải một object, bởi vậy nó không thuộc về một class.  
        + Một Wrapper class được sử dụng để tạo một object; do đó, nó có một class tương ứng.  

    - Giá trị null:  
        + Một primitive type không cho phép giá trị null.  
        + Wrapper type object cho phép giá trị null.  

    - Yêu cầu bộ nhớ:  
        + Primitive type yêu cầu bộ nhớ thấp hơn so với các Wrapper types.  
    
    - Collections:  
        + Primitive type không thể được sử dụng với các collections.  
        + Wrapper type có thể được sử dụng với các collections như ArrayList.  