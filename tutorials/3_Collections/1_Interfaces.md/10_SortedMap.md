# Lession 1. Interfaces

## 8. The SortedMap Interface

*SortedMap* là một *Map* lưu trữ các entry của nó theo thứ tự tăng dần của các keys, được sắp xếp theo thứ tự tự nhiên của các keys hoặc theo *Comparator* được cung cấp tại thời điểm tạo SortedMap. Thứ tự này được phản ánh khi lặp qua các collection view của sorted map.  

Tất cả các keys được chèn vào một sorted map phải triển khai Comparable interface (hoặc được chấp nhận bởi comparator được chỉ định). Hơn nữa, tất cả các keys như vậy phải là *mutually comparable*: k1.compareTo(k2) hoặc comparator.compare(k1, k2) không được ném ClassCastException cho bất kỳ keys k1 và k2 nào trong sorted map.

**Note**: Thứ tự được duy trì bởi một sorted map (cho dù có cung cấp comparator tường minh hay không) phải nhất quán với *equals* nếu sorted map là để triển khai chính xác Map interface. Bởi vì Map interface được định nghĩa dựa trên thuật ngữ của equals operation, nhưng một sorted map thực hiện tất cả các so sánh key bằng cách sử dụng *compareTo* (hoặc *compare*) method của nó , do đó, 2 keys được coi là bằng nhau theo method này, theo quan điểm của sorted map, là bằng nhau. Hành vi của một tree map là well-defined ngay cả khi thứ tự của nó không nhất quán với equals; nó chỉ không tuân theo hợp đồng (contract) chung của Map interface.

Ngoài các Map operations thông thường, SortedMap interface cung cấp các operations cho:

- *Range view* - cho phép các range operations tùy ý trên sorted map.  
- *Endpoints* - trả về key đầu tiên hoặc cuối cùng trong sorted map.  
- *Comparator access* - trả về Comparator (nếu có), được sử dụng để sắp xếp map.  


### 8.1, Standard Constructors

Tất cả các general-purpose SortedMap implementation được khuyến khích cung cấp 4 "standard" constructor: 

- 1) Một no-arguments constructor, tạo ra một empty sorted map được sắp xếp theo thứ tự tự nhiên của các keys của nó.  
- 2) Một constructor với một argument duy nhất của type *Comparator*, nó tạo ra một empty sorted map được sắp xếp theo comparator được chỉ định.  
- 3) Một constructor với một argument duy nhất của type *Map*, tạo một sorted map mới với các key-value mappings giống như argument của nó, được sắp xếp theo thứ tự tự nhiên của các keys.  
- 4) Một constructor với một argument duy nhất kiểu SortedSet, tạo một sorted set mới với các key-value mappings giống nhau và cùng thứ tự với input sorted set.  


### 8.2, Map Operations

Các operations mà SortedMap kế thừa từ Map, hoạt động trên các sorted map tương tự như trên các map bình thường, ngoại trừ 2 điểm khác biệt:

- *Iterator* được trả về bởi iterator() operation trên bất kỳ Collection views của sorted map duyệt qua các collection theo thứ tự.  
- Mảng được trả về bởi *toArray* operation của các Collection views chứa các keys, values hay entries của sorted map theo thứ tự.  

Các methods được thừa kế từ interface *java.util.Map* bao gồm: size, isEmpty, equals, hashCode, forEach, containsKey, containsValue, get, getOrDefault, put, putAll, putIfAbsent, merge, compute, computeIfAbsent, computeIfPresent, remove, remove, clear, replace, replace, replaceAll.


### 8.3, Range-view Operations

Ngoài 3 collection view operations: keySet, values, và entrySet; SortedMap interface còn cung cấp 3 range-view operations khác, bao gồm:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
SortedMap<K,V>    | headMap(K toKey)
                  | - Trả về một view của một bộ phận của map này có các keys hoàn toàn nhỏ hơn toKey.
                  | - Returned map là back up của map này, vì vậy những thay đổi trong returned map
                  |   được phản ánh trong map này và ngược lại.
                  | - Returned map hỗ trợ tất cả các optional map operations mà map này hỗ trợ.
                  | - Returned map sẽ ném một IllegalArgumentException khi cố gắng chèn một key
                  |   bên ngoài phạm vi (range) của nó.
------------------|--------------------------------------------------------------------------------
SortedMap<K,V>	  | tailMap(K fromKey)
                  | - Trả về một view của một bộ phận của map này có các keys lớn hơn hoặc bằng toKey.
                  | - Returned map là back up của map này, vì vậy những thay đổi trong returned map
                  |   được phản ánh trong map này và ngược lại.
                  | - Returned map hỗ trợ tất cả các optional map operations mà map này hỗ trợ.
                  | - Returned map sẽ ném một IllegalArgumentException khi cố gắng chèn một key
                  |   bên ngoài phạm vi (range) của nó.
------------------|--------------------------------------------------------------------------------
SortedMap<K,V>	  | subMap(K fromKey, K toKey)
                  | - Trả về một view của một bộ phận của map này có phạm vi key từ fromKey tới toKey.
                  |   (Nếu fromKey và toKey bằng nhau, returned map là empty.)
                  | - Returned map là back up của map này, vì vậy những thay đổi trong returned map
                  |   được phản ánh trong map này và ngược lại.
                  | - Returned map hỗ trợ tất cả các optional map operations mà map này hỗ trợ.
                  | - Returned map sẽ ném một IllegalArgumentException khi cố gắng chèn một key
                  |   bên ngoài phạm vi (range) của nó.
--------------------|------------------------------------------------------------------------------
Set<K>		        | keySet()
                    | - Trả về Set view của các key có trong map. 
                    | - Iterator của set trả về các keys theo thứ tự tăng dần.
                    | - Returned Set là back up của map,
                    |   vì vậy các thay đổi đối với map được phản ánh trong set và ngược lại. 
                    | - Set hỗ trợ xóa bỏ element, xóa bỏ mapping tương ứng khỏi map, thông qua các 
                    |   operations: Iterator.remove, Set.remove, removeAll, retainAll và clear. Nó
                    |   không hỗ trợ add hoặc addAll operations.
--------------------|------------------------------------------------------------------------------
Collection<V>       | values()
                    | - Trả về Collection view của các giá trị có trong map này. 
                    | - Iterator của collection trả về các values theo thứ tự tăng dần của các keys
                    |   tương ứng.
                    | - Returned Collection là back up của map, 
                    |   vì vậy các thay đổi đối với map được phản ánh trong Collection và ngược lại. 
                    | - Collection hỗ trợ xóa bỏ element, xóa bỏ mapping tương ứng khỏi map, thông
                    |   qua các operations: Iterator.remove, Collection.remove, removeAll, retainAll
                    |   và clear. Nó không hỗ trợ add hoặc addAll operations.
--------------------|------------------------------------------------------------------------------
Set<Map.Entry<K,V>> | entrySet()
                    | - Trả về Set view của các mappings có trong map này. 
                    | - Iterator của set trả về các entry theo thứ tự key tăng dần.
                    | - Returned Set là back up của map, 
                    |   vì vậy các thay đổi đối với map được phản ánh trong Set và ngược lại.
                    | - Set hỗ trợ xóa bỏ element, loại bỏ mapping tương ứng khỏi map, thông qua các
                    |   operations: Iterator.remove, Set.remove, removeAll, retainAll và clear. Nó
                    |   không hỗ trợ các add hoặc addAll operations.
```

Nếu map được sửa đổi trong khi một iteration trên collection đang diễn ra (ngoại trừ thông qua remove operation của chính iterator), thì kết quả của iteration là không xác định.


### 8.4, Endpoint Operations

SortedMap interface cung cấp các operations để trả về key đầu tiên và cuối cùng trong sorted map:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
K                 | firstKey()
                  | - Trả về key đầu tiên (thấp nhất) hiện có trong map này.
                  | - Ném một NoSuchElementException nếu map này empty.
------------------|--------------------------------------------------------------------------------
K                 | lastKey()
                  | - Trả về key cuối cùng (cao nhất) hiện có trong map này.
                  | - Ném một NoSuchElementException nếu map này empty.
```


### 8.5, Comparator Accessor

SortedMap interface cung cấp một accessor method được gọi là *comparator* được cung cấp để giúp các sorted map có thể được sao chép thành các sorted map mới với cùng một thứ tự:

```
Modifier and Type     |                        Method and Description
----------------------|----------------------------------------------------------------------------
Comparator<? super K> |	comparator()
                      | - Trả về một *Comparator* được sử dụng để sắp xếp các keys trong map này, 
                      |   hoặc trả về null nếu map này sử dụng thứ tự tự nhiên của các keys của nó.
```