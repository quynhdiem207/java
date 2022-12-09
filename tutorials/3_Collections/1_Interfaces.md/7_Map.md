# Lession 1. Interfaces

## 6. The Map Interface

Một *Map* là một object ánh xạ các keys đến các values. Một Map không thể chứa các keys trùng lặp: Mỗi key có thể ánh xạ đến nhiều nhất một value. Nó mô hình hóa sự trừu tượng của các "hàm" toán học. 

```java
public interface Map<K,V> {}
```

Java platform chứa 3 general-purpose Map implementations: 

- **HashMap**, lưu trữ các elements của nó trong một hash table, là cách triển khai hiệu quả nhất; tuy nhiên nó không đảm bảo về thứ tự lặp (iteration).    
- **TreeMap**, lưu trữ các elements của nó trong một red-black tree, sắp xếp thứ tự các elements của nó dựa trên giá trị các keys của chúng; nó chậm hơn đáng kể so với HashMap.  
- **LinkedHashMap**, được triển khai dưới dạng một hash table với một linked list chạy qua nó, sắp xếp thứ tự các elements của nó dựa trên thứ tự mà chúng được chèn vào map (insertion-order). LinkedHashMap giúp users thoát khỏi việc thứ tự hỗn loạn không xác định.    

Theo quy ước, tất cả các general-purpose Map implementations đều cung cấp các constructor nhận một Map object và khởi tạo một Map mới để chứa tất cả các key/value mappings trong Map được chỉ định. Map conversion constructor tiêu chuẩn này hoàn toàn tương tự với Collection conversion constructor tiêu chuẩn: Nó cho phép người gọi tạo Map của một implementation type mong muốn mà ban đầu chứa tất cả các mappings trong Map khác, bất kể implementation type của Map khác là gì. 

*Ví dụ, giả sử bạn có một Map, tên là m. Câu lệnh sau tạo một HashMap mới ban đầu chứa tất cả các key/value mappings giống như m:*

```java
Map<K, V> copy = new HashMap<K, V>(m);
```

Map interface bao gồm các methods cho các operations cơ bản (chẳng hạn như put, get, remove, containsKey, containsValue, size, và empty), các bulk operations (chẳng hạn như putAll và clear) và các collection views (chẳng hạn như keySet, entrySet và values).

**Note**: Map thay đổi hành vi của các *equals()* và *hashCode()* operations, cho phép các instances của Map được so sánh một cách có ý nghĩa ngay cả khi các implementation types của chúng khác nhau, 2 instances của Map là bằng nhau nếu chúng biểu diễn các ánh xạ (mappings) giống nhau. 

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | equals(Object o)
                  | - So sánh bằng giá trị của object được chỉ định với map này. 
                  | - Trả về true nếu object được chỉ định cũng là một map, 2 map biểu diễn các 
                  |   mappings giống nhau,
                  |   Hay, 2 map m1 và m2 bằng nhau nếu: m1.entrySet().equals(m2.entrySet()).
------------------|--------------------------------------------------------------------------------
int	              | hashCode()
                  | - Trả về giá trị hash code của map này. 
                  | - Hashcode của một map được định nghĩa là tổng các hashcode của mỗi entry trong
                  |   entrySet() view của map.
                  | - Cho 2 map m1 và m2, nếu m1.equals(m2) == true thì m1.hashCode() == m2.hashCode() 
```

**Note**: Từ JDK 8 trở lên, bạn có thể dễ dàng tạo một Map từ một nguồn bằng các aggregate operations:

*Ví dụ 1: Phân nhóm nhân viên theo bộ phận:*

```java
// Group employees by department
Map<Department, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));
```

*Ví dụ 2: Tính tổng lương theo bộ phận:*

```java
// Compute sum of salaries by department
Map<Department, Integer> totalByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.summingInt(Employee::getSalary)
    ));
```

*Ví dụ 3: Phân nhóm học sinh theo điểm đạt hoặc không đạt:*

```java
// Partition students into passing and failing
Map<Boolean, List<Student>> passingFailing = students.stream()
    .collect(Collectors.partitioningBy(s -> s.getGrade() >= PASS_THRESHOLD)); 
```

*Ví dụ 4: Phân nhóm mọi người theo thành phố:*

```java
// Classify Person objects by city
Map<String, List<Person>> peopleByCity = person.stream()
    .collect(Collectors.groupingBy(Person::getCity));
```

*Ví dụ 5: Xếp 2 collections lồng nhau để phân loại mọi người theo tiểu bang và thành phố:*

```java
// Cascade Collectors 
Map<String, Map<String, List<Person>>> peopleByStateAndCity = person.stream()
    .collect(Collectors.groupingBy(
        Person::getState,
        Collectors.groupingBy(Person::getCity)
    ));
```


### 6.1, Basic Operations

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int	              | size()
                  | - Trả về số cặp key/value trong map này.
------------------|--------------------------------------------------------------------------------
boolean	          | isEmpty()
                  | - Trả về true nếu map này không chứa bất kỳ cặp key/value nào.
------------------|--------------------------------------------------------------------------------
boolean           | containsKey(Object key)
                  | - Trả về true nếu map này chứa một mapping với key được chỉ định. 
                  |   Hay, kiểm tra map chứa mapping với key k sao cho:
                  |         key==null ? k==null : key.equals(k)
------------------|--------------------------------------------------------------------------------
boolean           | containsValue(Object value)
                  | - Trả về true nếu map này ánh xạ một hoặc nhiều key đến giá trị được chỉ định. 
                  |   Hay, kiểm tra map chứa ít nhất một mapping đến giá trị v sao cho: 
                  |         value==null ? v==null : value.equals(v)
------------------|--------------------------------------------------------------------------------
V                 | get(Object key)
                  | - Trả về giá trị mà được ánh xạ bởi key đã chỉ định, nếu key thỏa mãn:
                  |       key==null ? k==null : key.equals(k)
                  |   hoặc trả về null nếu map này không chứa mapping với key đã chỉ định.
                  | - Nếu map này cho phép các giá trị null, thì giá trị trả về là null không nhất 
                  |   thiết chỉ ra rằng map không chứa mapping với key; cũng có thể là map ánh xạ key
                  |   tới null. Có thể sử dụng containsKey() để phân biệt 2 trường hợp này.
------------------|--------------------------------------------------------------------------------
default V         | getOrDefault(Object key, V defaultValue)
                  | - Trả về giá trị mà được ánh xạ bởi key đã chỉ định,
                  |   hoặc trả về defaultValue nếu map này không chứa mapping với key đã chỉ định.
------------------|--------------------------------------------------------------------------------
V                 | remove(Object key)
                  | (optional operation)
                  | - Xóa mapping với key được chỉ định khỏi map này nếu nó có mặt.
                  |   Hay, xóa bỏ mapping có key thỏa mãn: key==null ? k==null : key.equals(k)
                  | - Trả về giá trị mà map này đã liên kết với key trước đó,
                  |   hoặc trả về null nếu map không chứa mapping với key.
                  | - Nếu map này cho phép các giá trị null, thì giá trị trả về là null không nhất 
                  |   thiết chỉ ra rằng map không chứa mapping với key; cũng có thể là map ánh xạ key
                  |   tới null.
------------------|--------------------------------------------------------------------------------
default boolean   | remove(Object key, Object value)
                  | - Xóa entry với key được chỉ định nếu nó hiện ánh xạ tới giá trị được chỉ định.
                  | - Trả về true nếu entry bị xóa, trả về false nếu entry không bị xóa.
------------------|--------------------------------------------------------------------------------
default V         | replace(K key, V value)
                  | - Nếu trong map tồn tại mapping với key được chỉ định, thì thay thế bằng giá trị
                  |   được chỉ định, và trả về giá trị cũ.
                  | - Nếu không, trả về null.
------------------|--------------------------------------------------------------------------------
default boolean   | replace(K key, V oldValue, V newValue)
                  | - Nếu trong map tồn tại mapping với key được chỉ định và ánh xạ tới giá trị oldValue,
                  |   thì thay thế bằng giá trị newValue và trả về true.
                  | - Nếu không, trả về false.
------------------|--------------------------------------------------------------------------------
V                 | put(K key, V value)
                  | (optional operation)
                  | - Nếu trước đó map đã chứa một mapping với key, giá trị cũ sẽ được thay thế bằng
                  |   giá trị được chỉ định, và trả về giá trị trước đó đã liên kết với key.
                  | - Nếu không, một entry mới sẽ được thêm vào map, và trả về null.
------------------|--------------------------------------------------------------------------------
default V         | putIfAbsent(K key, V value)
                  | - Nếu map không chứa mapping với key được chỉ định, thì thêm vào map cặp key/value.
                  |   Nếu map chứa mapping với key ánh xạ tới null, thì thay bằng giá trị chỉ định.
                  |   Trả về giá trị null. 
                  | - Nếu không, trả về giá trị hiện tại được ánh xạ bởi key.
------------------|--------------------------------------------------------------------------------
default V         | merge(
                  |     K key, 
                  |     V value, 
                  |     BiFunction<? super V, ? super V, ? extends V> remappingFunction
                  | )
                  | - Nếu map không chứa mapping với key được chỉ định, thì thêm vào map cặp key/value.
                  |   Nếu map chứa mapping với key ánh xạ tới null, thì thay bằng non-null value đã cho. 
                  |   Trả về non-null value. 
                  | - Nếu không, nếu kết quả của remapping function là null thì xóa bỏ mapping, ngược
                  |   lại sẽ thay thế giá trị được liên kết bằng kết quả của remapping function. 
                  |   Trả về kết quả của remapping function. 
                  | - BiFunction<T,U,R> là một functional interface có "R apply(T, U)" method.
                  |   với first parameter là giá trị cũ được ánh xạ bởi key, 
                  |   second parameter là non-null value.
------------------|--------------------------------------------------------------------------------
default V         | compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
                  | - Nếu map không chứa mapping với key được chỉ định và nếu kết quả của remapping
                  |   function khác null, thì thêm vào map một mapping với key ánh xạ đến kết quả đó.
                  | - Nếu map chứa mapping với key được chỉ định: 
                  |   (1) nếu kết quả của remapping function là null, thì xóa bỏ mapping. 
                  |   (2) ngược lại, thay thế giá trị được ánh xạ bằng kết quả của remapping function. 
                  | - Trả về giá trị kết quả của remapping function.
                  | - BiFunction<T,U,R> là một functional interface có "R apply(T, U)" method.
                  |   với first parameter là key, second parameter là value.
------------------|--------------------------------------------------------------------------------
default V	      | computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)
                  | - Nếu map không chứa mapping với key được chỉ định, và nếu kết quả của mapping
                  |   function khác null, thì thêm vào map một mapping với key ánh xạ đến kết quả đó.
                  | - Nếu map có chứa mapping với key ánh xạ đến null, và nếu kết quả của mapping
                  |   function khác null, thì thay thế giá trị được ánh xạ bởi key bằng kết quả đó.
                  | - Trả về giá trị được ánh xạ bởi key sau operation.
                  | - Function<T,R> là một functional interface có "R apply(T)" method.
                  |   với parameter là value.
------------------|--------------------------------------------------------------------------------
default V	      | computeIfPresent(
                  |     K key, 
                  |     BiFunction<? super K, ? super V, ? extends V> remappingFunction
                  | )
                  | - Nếu map tồn tại mapping với key được chỉ định, và không ánh xạ tới null, thì: 
                  |   (1) nếu kết quả của remapping function là null, thì xóa mapping khỏi map.
                  |   (2) ngược lại, thay thế giá trị được ánh xạ bởi key bằng kết quả đó.
                  | - Trả về kết quả của remapping function.
                  | - BiFunction<T,U,R> là một functional interface có "R apply(T, U)" method.
                  |   với first parameter là key, second parameter là value.
------------------|--------------------------------------------------------------------------------
default void	  | forEach(BiConsumer<? super K, ? super V> action)
                  | - Thực hiện hành động đã cho với từng entry trong map này cho đến khi tất cả các
                  |   entry đã được xử lý hoặc hành động ném ra một ngoại lệ.
                  | - BiConsumer<T,U> là một functional interface có "void accept(T, U)" method.
                  |   với first parameter là key, second parameter là value.
```

*Ví dụ: Chương trình sau tạo một bảng tần xuất của các từ được tìm thấy trong argument list của nó. Bảng tần suất ánh xạ mỗi từ với số lần nó xuất hiện trong argument list:*

```java
import java.util.*;

public class Freq {
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<String, Integer>();

        // Initialize frequency table from command line
        for (String a : args) {
            Integer freq = m.get(a);
            m.put(a, (freq == null) ? 1 : freq + 1);
        }

        System.out.println(m.size() + " distinct words:");
        System.out.println(m);
    }
}
```

*Chạy chương trình trên:*

```
Command:    java Freq if it is to be it is up to me to delegate
Output:     8 distinct words:
            {to=3, delegate=1, be=1, it=2, up=1, if=1, me=1, is=2}
```

Nếu bạn muốn xem bảng tần suất theo thứ tự bảng chữ cái, cần thay đổi implementation type của Map từ HashMap sang TreeMap. Tương tự, nếu bạn muốn xem bảng tần suất theo thứ tự các từ xuất hiện đầu tiên trên command line, cần thay đổi implementation type của Map thành LinkedHashMap. Tính linh hoạt này do sức mạnh của interface-based framework.  


### 6.2, Bulk Operations

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
void	          | clear()
                  | (optional operation) 
                  | - Xóa bỏ tất cả các mappings khỏi map này.
------------------|--------------------------------------------------------------------------------
void	          | putAll(Map<? extends K,? extends V> m)
                  | (optional operation)
                  | - Sao chép tất cả các mappings từ map được chỉ định sang map này.
                  | - Thêm vào map này các mappings của map được chỉ định nếu key chưa có trong map.
                  |   Nếu không, thay thế giá trị được ánh xạ bởi key chung của map này bằng các giá
                  |   trị được ánh xạ của map được chỉ định.
------------------|--------------------------------------------------------------------------------
default void      | replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
                  | - Thay thế giá trị của mỗi entry bằng kết quả của việc gọi function đã cho trên
                  |   entry đó cho đến khi tất cả các entry đã được xử lý hoặc hàm ném ra một ngoại lệ.
                  | - BiFunction<T,U,R> là một functional interface có "R apply(T, U)" method.
                  |   với first parameter là key, second parameter là value.

```


### 6.3, Collection Views

Map interface cung cấp 3 collection-view operations, bao gồm:

```
  Modifier and Type |                        Method and Description
--------------------|------------------------------------------------------------------------------
Set<K>		        | keySet()
                    | - Trả về Set view của các key có trong map. 
                    | - Returned Set là back up của map,
                    |   vì vậy các thay đổi đối với map được phản ánh trong set và ngược lại. 
                    | - Set hỗ trợ xóa bỏ element, xóa bỏ mapping tương ứng khỏi map, thông qua các 
                    |   operations: Iterator.remove, Set.remove, removeAll, retainAll và clear. Nó
                    |   không hỗ trợ add hoặc addAll operations.
--------------------|------------------------------------------------------------------------------
Collection<V>       | values()
                    | - Trả về Collection view của các giá trị có trong map này. 
                    | - Returned Collection là back up của map, 
                    |   vì vậy các thay đổi đối với map được phản ánh trong Collection và ngược lại. 
                    | - Collection hỗ trợ xóa bỏ element, xóa bỏ mapping tương ứng khỏi map, thông
                    |   qua các operations: Iterator.remove, Collection.remove, removeAll, retainAll
                    |   và clear. Nó không hỗ trợ add hoặc addAll operations.
--------------------|------------------------------------------------------------------------------
Set<Map.Entry<K,V>> | entrySet()
                    | - Trả về Set view của các mappings có trong map này. 
                    | - Returned Set là back up của map, 
                    |   vì vậy các thay đổi đối với map được phản ánh trong Set và ngược lại.
                    | - Set hỗ trợ xóa bỏ element, loại bỏ mapping tương ứng khỏi map, thông qua các
                    |   operations: Iterator.remove, Set.remove, removeAll, retainAll và clear. Nó
                    |   không hỗ trợ các add hoặc addAll operations.
```

Nếu map được sửa đổi trong khi một iteration trên collection đang diễn ra (ngoại trừ thông qua remove operation của chính iterator), thì kết quả của iteration là không xác định.


#### *The Map.Entry interface*

Map interface cung cấp một static nested interface [Map.Entry<K,V>] có các methods sau:

```
            Modifier and Type               |               Method and Description
--------------------------------------------|------------------------------------------------------
static <K extends Comparable<? super K>, V> |
    Comparator<Map.Entry<K,V>>	            | comparingByKey()
                                            | - Trả về một comparator so sánh Map.Entry theo thứ tự
                                            |   tự nhiên trên key.
                                            | - Ném NullPointerException khi so sánh entry với key
                                            |   là null.
                                            | - Comparator<T> là một functional interface với
                                            |   "int compare(T, T)" method.
--------------------------------------------|------------------------------------------------------
static <K,V> Comparator<Map.Entry<K,V>>	    | comparingByKey(Comparator<? super K> cmp)
                                            | - Trả về một comparator so sánh Map.Entry bằng key, sử
                                            |   dụng Comparator đã cho.
--------------------------------------------|------------------------------------------------------
static <K, V extends Comparable<? super V>> |
    Comparator<Map.Entry<K,V>>	            | comparingByValue()
                                            | - Trả về một comparator so sánh Map.Entry theo thứ tự
                                            |   tự nhiên trên value.
                                            | - Ném NullPointerException khi so sánh entry với value
                                            |   là null.
--------------------------------------------|------------------------------------------------------
static <K,V> Comparator<Map.Entry<K,V>>     | comparingByValue(Comparator<? super V> cmp)
                                            | - Trả về một comparator so sánh Map.Entry bằng value,
                                            |   sử dụng Comparator đã cho.
--------------------------------------------|------------------------------------------------------
boolean	                                    | equals(Object o)
                                            | - So sánh bằng object được chỉ định với entry này.
                                            | - Trả về true nếu object đã cho cũng là một map entry
                                            |   và 2 entry biểu thị cùng một mapping.
                                            |
                                            |   (
                                            |       e1.getKey()==null ? e2.getKey()==null : 
                                            |       e1.getKey().equals(e2.getKey())
                                            |   )  && (
                                            |       e1.getValue()==null ? e2.getValue()==null : 
                                            |       e1.getValue().equals(e2.getValue())
                                            |   )
--------------------------------------------|------------------------------------------------------
int	                                        | hashCode()
                                            | - Trả về giá trị hash code cho map entry này.
                                            |   Hash code của map entry e được định nghĩa là:
                                            |   (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
                                            |   (e.getValue()==null ? 0 : e.getValue().hashCode())
--------------------------------------------|------------------------------------------------------
K	                                        | getKey()
                                            | - Trả về key tương ứng với entry này.
--------------------------------------------|------------------------------------------------------
V	                                        | getValue()
                                            | - Trả về value tương ứng với entry này.
--------------------------------------------|------------------------------------------------------
V	                                        | setValue(V value)
                                            | (optional operation)
                                            | - Thay thế giá trị tương ứng với entry này bằng value
                                            |   được chỉ định.
```

Cần sử dụng iterator để duyệt qua các collection views:

```java
// for-each construct:
for (KeyType key : m.keySet())
    System.out.println(key);

for (Map.Entry<KeyType, ValType> e : m.entrySet())
    System.out.println(e.getKey() + ": " + e.getValue());

// iterator:
for (Iterator<Type> it = m.keySet().iterator(); it.hasNext(); )
    if (it.next().isBogus())
        it.remove();
```


### 6.4, Fancy Uses of Collection Views: Map Algebra

Khi được áp dụng cho các Collection views, các bulk opearations (containsAll, removeAll, và retainAll) là những công cụ mạnh một cách đáng ngạc nhiên.

*Ví dụ 1: Kiểm tra liệu một map có phải là một submap của một map khác hay không:*

```java
if (m1.entrySet().containsAll(m2.entrySet())) {
    ...
}
```

*Ví dụ 2: Kiểm tra liệu 2 Map object có chứa các mappings với tất cả các keys giống nhau hay không:*

```java
if (m1.keySet().equals(m2.keySet())) {
    ...
}
```

*Ví dụ 3: Giả sử bạn có một Map đại diện cho collection của các cặp attribute-value và 2 Set đại diện cho các attribute bắt buộc và attribute được phép. (Các attribute được phép bao gồm các attribute bắt buộc.) Đoạn mã sau xác định liệu map có tuân thủ các ràng buộc này hay không, nếu không sẽ in ra một thông báo lỗi chi tiết.*

```java
static <K, V> boolean validate(Map<K, V> attrMap, Set<K> requiredAttrs, Set<K>permittedAttrs) {
    boolean valid = true;
    Set<K> attrs = attrMap.keySet();

    if (! attrs.containsAll(requiredAttrs)) {
        Set<K> missing = new HashSet<K>(requiredAttrs);
        missing.removeAll(attrs);
        System.out.println("Missing attributes: " + missing);
        valid = false;
    }
    if (! permittedAttrs.containsAll(attrs)) {
        Set<K> illegal = new HashSet<K>(attrs);
        illegal.removeAll(permittedAttrs);
        System.out.println("Illegal attributes: " + illegal);
        valid = false;
    }
    return valid;
}
```

*Ví dụ 4: Lấy tất cả các keys chung của 2 map:*

```java
Set<KeyType> commonKeys = new HashSet<KeyType>(m1.keySet());
commonKeys.retainAll(m2.keySet());
```

Ngoài ra các Collection views còn hỗ trợ xóa bỏ các elements, nhằm xóa các mappings khỏi map.

*Ví dụ 5: Xóa tất cả các cặp key-value của một Map có chung với một Map khác:*

```java
m1.entrySet().removeAll(m2.entrySet());
```

*Ví dụ 6: Xóa khỏi một Map tất cả các mapping với key có mặt trong một Map khác.*

```java
m1.keySet().removeAll(m2.keySet());
```

*Ví dụ 7: Giả sử bạn có một Map managers, ánh xạ từng nhân viên trong một công ty với người quản lý của nhân viên đó. Bây giờ, giả sử bạn muốn biết tất cả những người không phải là người quản lý là ai:*

```java
Set<Employee> individualContributors = new HashSet<Employee>(managers.keySet());
individualContributors.removeAll(managers.values());
```

*Giả sử bạn muốn sa thải tất cả những nhân viên báo cáo trực tiếp với người quản lý nào đó, Simon:*

```java
// Collections.singleton là một static factory method
// trả về một immutable Set với element duy nhất được chỉ định.

Employee simon = ... ;
managers.values().removeAll(Collections.singleton(simon));
```

*Khi bạn đã làm điều này, bạn có thể có một loạt nhân viên mà người quản lý không còn làm việc cho công ty (nếu bất kỳ báo cáo trực tiếp nào của Simon đều là người quản lý). Để kiểm tra những nhân viên nào có người quản lý không còn làm việc cho công ty:*

```java
Map<Employee, Employee> m = new HashMap<Employee, Employee>(managers);
m.values().removeAll(managers.keySet());
Set<Employee> slackers = m.keySet();
```


### 6.5, Multimaps

Một *multimap* giống như một Map nhưng nó có thể ánh xạ mỗi key tới nhiều values. Java Collections Framework không bao gồm interface cho các multimaps vì chúng không được sử dụng phổ biến. Bạn có thể sử dụng một Map có giá trị là các List instances dưới dạng một multimap. 
