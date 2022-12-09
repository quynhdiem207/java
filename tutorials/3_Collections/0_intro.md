# Introduction to Collections

Một *collection* - đôi khi được gọi là một *container* - chỉ đơn giản là một object nhóm nhiều phần tử thành một đơn vị duy nhất. Các collections được sử dụng để lưu trữ, truy xuất, thao tác và giao tiếp dữ liệu tập hợp (aggregate data). Thông thường, chúng đại diện cho các mục dữ liệu (data items) tạo thành một nhóm tự nhiên (natural group), chẳng hạn như bộ bài poker (bộ sưu tập các lá bài), mail folder (bộ sưu tập thư) hoặc danh bạ điện thoại (ánh xạ tên với số điện thoại). 


## What Is a Collections Framework?

Một *collections framework* là một kiến ​​trúc thống nhất để đại diện và thao tác các collections. Tất cả các collections frameworks đều chứa những thứ sau:

- **Interfaces**: Đây là các abstract data types đại diện cho các collections. Các interfaces cho phép các collections được thao tác độc lập với các chi tiết biểu diễn của chúng.  
- **Implementations**: Đây là những triển khai cụ thể của các collection interfaces. Về bản chất, chúng là cấu trúc dữ liệu (data structures) có thể tái sử dụng.  
- **Algorithms**: Đây là các methods thực hiện các tính toán hữu ích, chẳng hạn như tìm kiếm và sắp xếp trên các object triển khai các collection interfaces. Các thuật toán (algorithms) được cho là đa hình: có nghĩa là, cùng một method có thể được sử dụng trên nhiều cách triển khai khác nhau của collection interface thích hợp. Về bản chất, các thuật toán là chức năng có thể tái sử dụng.  

Hệ thống phân cấp Java Collections Framework như sau:

```
                                Collection                                
                                    |                                     
             ------------------------------------------------             
             |                      |                       |         
            Set                    List                   Queue                           
             |                      |                       |                           
    -------------------             |              --------------------             
    |                 |             |              |                  |               
    |             SortedSet         |              |                Deque           
    |                 |             |              |                  |       
    |- EnumSet        |- TreeSet    |- LinkedList  |- PriorityQueue   |- LinkedList
    |- HashSet                      |- ArrayList                      |- ArrayDeque
          |                         |- Vector
    LinkedHashSet


                Map
                 |
        ------------------
        |                |
    SortedMap            |
        |                |
        |- TreeMap       |- HashMap
                               |
                         LinkedHashMap
```


## Benefits of the Java Collections Framework

Java Collections Framework cung cấp các lợi ích sau:

- Giảm công sức lập trình  
- Tăng tốc độ và chất lượng chương trình  
- Cho phép khả năng tương tác giữa các API không liên quan  
- Giảm công sức tìm hiểu và sử dụng các API mới  
- Giảm công sức thiết kế các API mới  
- Thúc đẩy tái sử dụng phần mềm  