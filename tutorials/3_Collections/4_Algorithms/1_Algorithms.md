# Lesson 4: Algorithms

Các *polymorphic algorithms* (thuật toán đa hình) được mô tả ở đây là các phần của chức năng có thể tái sử dụng được cung cấp bởi Java platform. Tất cả chúng đều đến từ **Collections** class, và tất cả đều ở dạng *static methods* có first argument là Collection mà thao tác sẽ được thực hiện. Phần lớn các thuật toán được cung cấp bởi Java platform hoạt động trên các List instances, nhưng một số ít trong số chúng hoạt động trên các Collection instances tùy ý. Phần này mô tả ngắn gọn các thuật toán sau:

+ Sorting (sắp xếp)  
+ Shuffling (xáo trộn)  
+ Routine Data Manipulation (Thao tác dữ liệu thông thường)  
+ Searching (Tìm kiếm)  
+ Composition (Thành phần)  
+ Finding Extreme Values (Tìm giá trị lớn/nhỏ nhất)  


## 1. Sorting

`sort` algorithm sắp xếp lại một *List* để các elements của nó có thứ tự tăng dần theo một mối quan hệ. Có 2 hình thức của operation này được cung cấp:

+ Dạng đơn giản nhận một List và sắp xếp nó theo thứ tự tự nhiên của các elements của nó.  
+ Dạng thứ 2 sử dụng một Comparator được cung cấp ngoài List, và sắp xếp các elements bằng Comparator.  

*Ví dụ: in ra các arguments của chương trình theo thứ tự từ vựng (theo bảng chữ cái):*

```java
import java.util.*;

public class Sort {
    public static void main(String[] args) {
        List<String> list = Arrays.asList(args);
        Collections.sort(list);
        System.out.println(list);
    }
}

// Run:     java Sort i walk the line
// Output:  [i, line, the, walk]
```


## 2. Shuffling

`shuffle` (xáo trộn) algorithm thực hiện ngược lại với `sort`, phá hủy bất kỳ dấu vết thứ tự nào có thể đã có trong *List*. Nghĩa là, thuật toán này sắp xếp lại List dựa trên input từ một source ngẫu nhiên sao cho tất cả các hoán vị có thể xảy ra với khả năng như nhau, giả sử là một source ngẫu nhiên công bằng. Thuật toán này rất hữu ích trong việc triển khai các trò chơi may rủi. Ví dụ, nó có thể được sử dụng để xáo trộn List các đối tượng Thẻ đại diện cho một bộ bài. Ngoài ra, nó hữu ích để tạo các trường hợp thử nghiệm.

Operation này có 2 dạng: 

+ một dạng nhận một List và sử dụng một source ngẫu nhiên mặc định, và  
+ một dạng khác yêu cầu người gọi cung cấp một *Random* object để sử dụng như một source ngẫu nhiên.  


## 3. Routine Data Manipulation

Collections class cung cấp 5 thuật toán để thực hiện thao tác dữ liệu thông thường trên các *List* object:

+ `reverse` - đảo ngược thứ tự của các elements trong một List.  
+ `fill` - ghi đè mọi elements trong List với giá trị được chỉ định.  
+ `copy` - nhận 2 arguments, một destination List và một source List, và sao chép các elements của nguồn vào đích, ghi đè lên nội dung của nó. List đích ít nhất phải dài bằng nguồn. Nếu nó dài hơn, các elements còn lại trong List đích không bị ảnh hưởng.  
+ `swap` - hoán đổi các elements tại các vị trí được chỉ định trong một List.  
+ `addAll` - thêm tất cả các elements được chỉ định vào một Collection. Các elements được thêm vào có thể được chỉ định riêng lẻ hoặc dưới dạng một mảng.  


## 4. Searching

`binarySearch` algorithm tìm kiếm một element được chỉ định trong *sorted List*. Thuật toán này có 2 dạng: 

+ Đầu tiên nhận một List và một element để tìm kiếm ("search key"). Dạng này giả định rằng List được sắp xếp theo thứ tự tăng dần theo thứ tự tự nhiên của các element của nó.  
+ Dạng thứ 2 nhận một Comparator ngoài List và search key, và giả định rằng List được sắp xếp theo thứ tự tăng dần theo Comparator đã chỉ định.  

`sort` algorithm có thể được sử dụng để sắp xếp List trước khi gọi `binarySearch`.

Giá trị trả về là như nhau cho cả 2 hình thức. Nếu List chứa search key, index của nó sẽ được trả về. Nếu không, giá trị trả về là `-(insertion point) - 1`, trong đó insertion point là điểm tại đó giá trị sẽ được chèn vào List hoặc index của element đầu tiên lớn hơn giá trị hoặc `list.size()` nếu tất cả các element trong List nhỏ hơn giá trị được chỉ định. Công thức xấu xí được thừa nhận này đảm bảo rằng giá trị trả về sẽ là >= 0 nếu và chỉ khi search key được tìm thấy.

Cách thức sau có thể sử dụng với cả 2 dạng của `binarySearch` operation, tìm kiếm search key được chỉ định và chèn nó vào vị trí thích hợp nếu nó chưa có:

```java
int pos = Collections.binarySearch(list, key);
if (pos < 0)
   l.add(-pos-1, key);
```


## 5. Composition

Các `frequency` (tần suất) và `disjoint` (rời rạc) algorithms kiểm tra một số khía cạnh của thành phần của một hoặc nhiều *Collection*:

+ `frequency` - đếm số lần elements được chỉ định xuất hiện trong Collection được chỉ định.  
+ `disjoint` - xác định xem 2 Collections có rời rạc hay không; nghĩa là, xác nhận chúng không có elements nào chung.  


## 6. Finding Extreme Values

Các `min` và `max` algorithms lần lượt trả về element tối thiểu và tối đa có trong một *Collection* được chỉ định. Cả 2 operations này đều có 2 dạng. Dạng đơn giản chỉ nhận một Collection và trả về element tối thiểu (hoặc tối đa) theo thứ tự tự nhiên của các elements. Dạng thứ 2 nhận một Comparator ngoài Collection và trả về element tối thiểu (hoặc tối đa) theo Comparator được chỉ định.