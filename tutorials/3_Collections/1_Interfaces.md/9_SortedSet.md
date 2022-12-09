# Lession 1. Interfaces

## 7. The SortedSet Interface

*SortedSet* là một *Set* lưu trữ các elements của nó theo thứ tự tăng dần, được sắp xếp theo thứ tự tự nhiên của các elements hoặc theo một *Comparator* được cung cấp tại thời điểm tạo SortedSet. 

```java
public interface SortedSet<E> extends Set<E> {}
```

Tất cả các elements được chèn vào một sorted set phải triển khai Comparable interface (hoặc được chấp nhận bởi comparator được chỉ định). Hơn nữa, tất cả các elements như vậy phải là *mutually comparable*: e1.compareTo(e2) hoặc comparator.compare(e1, e2) không được ném ClassCastException cho bất kỳ elements e1 và e2 nào trong sorted set.

**Note**: Thứ tự được duy trì bởi một sorted set (cho dù có cung cấp comparator tường minh hay không) phải nhất quán với *equals* nếu sorted set là để triển khai chính xác Set interface. Bởi vì Set interface được định nghĩa dựa trên thuật ngữ của equals operation, nhưng một sorted set thực hiện tất cả các so sánh element bằng cách sử dụng *compareTo* (hoặc *compare*) method của nó , do đó, 2 elements được coi là bằng nhau theo method này, theo quan điểm của sorted set, là bằng nhau. Hành vi của một sorted set là well-defined ngay cả khi thứ tự của nó không nhất quán với equals; nó chỉ không tuân theo hợp đồng (contract) chung của Set interface.

Ngoài các Set operations thông thường, SortedSet interface cung cấp các operations cho:

- *Range view* - cho phép các range operations tùy ý trên sorted set.  
- *Endpoints* - trả về element đầu tiên hoặc cuối cùng trong sorted set.  
- *Comparator access* - trả về Comparator (nếu có), được sử dụng để sắp xếp set.  


### 7.1, Standard Constructors

Tất cả các general-purpose SortedSet implementation được khuyến khích cung cấp 4 "standard" constructor: 

- 1) Một no-arguments constructor, tạo ra một empty sorted set được sắp xếp theo thứ tự tự nhiên của các elements của nó.  
- 2) Một constructor với một argument duy nhất của type *Comparator*, nó tạo ra một empty sorted set được sắp xếp theo comparator được chỉ định.  
- 3) Một constructor với một argument duy nhất của type *Collection*, tạo một sorted set mới với các elements giống như argument của nó, được sắp xếp theo thứ tự tự nhiên của các elements.  
- 4) Một constructor với một argument duy nhất kiểu SortedSet, tạo một sorted set mới với các elements giống nhau và cùng thứ tự với input sorted set.  


### 7.2, Set Operations

Các operations mà SortedSet kế thừa từ Set, hoạt động trên các sorted set tương tự như trên các set bình thường, ngoại trừ 2 điểm khác biệt:

- *Iterator* được trả về bởi *iterator()* operation duyệt qua sorted set theo thứ tự.  
- Mảng được trả về bởi *toArray* operation chứa các element của sorted set theo thứ tự.  

Các methods được thừa kế từ interface *java.util.Set* bao gồm: size, isEmpty, add, addAll, contains, containsAll, equals, hashCode, iterator, remove, removeAll, retainAll, clear, toArray, toArray.

Các methods được thừa kế từ interface *java.util.Collection* bao gồm: removeIf, parallelStream, stream.

Các methods được thừa kế từ interface *java.lang.Iterable* bao gồm: forEach.

Ngoài ra, SortedSet interface còn định nghĩa lại spliterator() method, tạo một Spliterator trên các elements của sorted set này.


### 7.3, Range-view Operations

Các range-view operations hơi tương tự với subList operation cung cấp bởi List interface, nhưng có một điểm khác biệt lớn: Các range-views của một sorted set vẫn hợp lệ ngay cả khi backing sorted set được sửa đổi trực tiếp. Điều này là khả thi vì các endpoints của một range-view của một sorted set là các điểm tuyệt đối trong element space chứ không phải là các element cụ thể trong backing collection như List. Các thay đổi đối với range-views sẽ ghi lại vào backing sorted set và ngược lại. Do đó, bạn có thể sử dụng các range-views trên các sorted set trong một khoảng thời gian dài, không giống với các range-views trên list.

SortedSet interface cung cấp 3 range-view operations, bao gồm:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
SortedSet<E>      | headSet(E toElement)
                  | - Trả về một view của một bộ phận của set này có các elements hoàn toàn nhỏ hơn
                  |   so với toElement.
                  | - Returned set là back up của set này, vì vậy những thay đổi trong returned set
                  |   được phản ánh trong set này và ngược lại.
                  | - Returned set hỗ trợ tất cả các optional set operations mà set này hỗ trợ.
                  | - Returned set sẽ ném một IllegalArgumentException khi cố gắng chèn một element
                  |   bên ngoài phạm vi (range) của nó.
------------------|--------------------------------------------------------------------------------
SortedSet<E>	  | tailSet(E fromElement)
                  | - Trả về một view của một bộ phận của set này có các elements lớn hơn hoặc bằng
                  |   với toElement.
                  | - Returned set là back up của set này, vì vậy những thay đổi trong returned set
                  |   được phản ánh trong set này và ngược lại.
                  | - Returned set hỗ trợ tất cả các optional set operations mà set này hỗ trợ.
                  | - Returned set sẽ ném một IllegalArgumentException khi cố gắng chèn một element
                  |   bên ngoài phạm vi (range) của nó.
------------------|--------------------------------------------------------------------------------
SortedSet<E>	  | subSet(E fromElement, E toElement)
                  | - Trả về một view của một bộ phận của set này có phạm vi elements từ fromElement
                  |   tới toElement. (Nếu fromElement và toElement bằng nhau, returned set là empty.)
                  | - Returned set là back up của set này, vì vậy những thay đổi trong returned set
                  |   được phản ánh trong set này và ngược lại.
                  | - Returned set hỗ trợ tất cả các optional set operations mà set này hỗ trợ.
                  | - Returned set sẽ ném một IllegalArgumentException khi cố gắng chèn một element
                  |   bên ngoài phạm vi (range) của nó.
```

*Ví dụ 1: Đếm số từ nằm giữa "doorbell" và "pickle" (bao gồm doorbell nhưng không gồm pickle) được chứa trong một sorted set của các strings được gọi là dictionary:*

```java
int count = dictionary.subSet("doorbell", "pickle").size();
```

*Ví dụ 2: Xóa tất cả các từ bắt đầu bằng ký tự f:*

```java
dictionary.subSet("f", "g").clear();
```

*Ví dụ 3: Đếm số từ bắt đầu bằng mỗi chữ cái:*

```java
for (char ch = 'a'; ch <= 'z'; ) {
    String from = String.valueOf(ch++);
    String to = String.valueOf(ch);
    System.out.println(from + ": " + dictionary.subSet(from, to).size());
}
```

**Note**: Giả sử bạn muốn lấy view của một *closed interval* (khoảng đóng), chứa cả 2 endpoints của nó, thay vì một *open interval* (khoảng mở). Nếu element type cho phép tính toán successor của một giá trị nhất định trong element space, chỉ cần yêu cầu *subSet* từ lowEndpoint đến successor(highEndpoint). Mặc dù điều đó không hoàn toàn rõ ràng, nhưng successor của một string s trong thứ tự tự nhiên của String là s + "\0" - nghĩa là s với một null character được thêm vào.

*Ví dụ 4: Đếm số từ nằm giữa "doorbell" và "pickle" (bao gồm doorbell và pickle) được chứa trong một sorted set của các strings được gọi là dictionary:*

```java
count = dictionary.subSet("doorbell", "pickle\0").size();
```

**Note**: Để lấy view của một *open interval*, không chứa cả 2 endpoints, sử dụng subSet từ successor(lowEndpoint) đến highEndpoint.

*Ví dụ 5: Đếm số từ nằm giữa "doorbell" và "pickle" (không bao gồm doorbell và pickle) được chứa trong một sorted set của các strings được gọi là dictionary:*

```java
count = dictionary.subSet("doorbell\0", "pickle").size();
```

*Ví dụ 6: Lấy view chứa các từ có ký tự bắt đầu đứng trước n của một sorted set chứa các strings được gọi là dictionary:*

```java
SortedSet<String> volume1 = dictionary.headSet("n");
```

*Ví dụ 7: Lấy view chứa các từ có ký tự bắt đầu từ n trở đi của một sorted set chứa các strings được gọi là dictionary:*

```java
SortedSet<String> volume2 = dictionary.tailSet("n");
```


### 7.4, Endpoint Operations

SortedSet interface cung cấp các operations để trả về element đầu tiên và cuối cùng trong sorted set:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
E                 | first()
                  | - Trả về element đầu tiên (thấp nhất) hiện có trong set này.
                  | - Ném một NoSuchElementException nếu set này empty.
------------------|--------------------------------------------------------------------------------
E                 | last()
                  | - Trả về element cuối cùng (cao nhất) hiện có trong set này.
                  | - Ném một NoSuchElementException nếu set này empty.
```

*Ví dụ: lấy element lớn nhất mà nhỏ hơn một object xác định trong element space:*

```java
Object predecessor = sortedset.headSet(o).last();
```


### 7.5, Comparator Accessor

SortedSet interface cung cấp một accessor method được gọi là *comparator* được cung cấp để các sorted set có thể được sao chép thành các sorted set mới với cùng một thứ tự:

```
Modifier and Type     |                        Method and Description
----------------------|----------------------------------------------------------------------------
Comparator<? super E> |	comparator()
                      | - Trả về một *Comparator* được sử dụng để sắp xếp thứ tự các elements trong
                      |   set này, hoặc trả về null nếu set này sử dụng thứ tự tự nhiên của các 
                      |   elements của nó.
```