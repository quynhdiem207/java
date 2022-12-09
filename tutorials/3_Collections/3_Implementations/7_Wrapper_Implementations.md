# Lession 3. Implementations

## 6. Wrapper Implementations

Các Wrapper implementations ủy quyền tất cả công việc thực sự của chúng cho một collection cụ thể nhưng thêm chức năng bổ sung trên những gì collection này cung cấp.

Các Wrapper implementations là anonymous; thay vì cung cấp một public class, library cung cấp một static factory method. Tất cả các Wrapper implementations đều được tìm thấy trong **Collections** class mà chỉ bao gồm các static methods.


### 6.1, Synchronization Wrappers

Các synchronization wrappers thêm đồng bộ hóa tự động (thread-safe) vào một collection tùy ý. Trong số 6 core interfaces - Collection, Set, List, Map, SortedSet và SortedMap - mỗi interface đều có một static factory method.

```java
public static <T> Collection<T> synchronizedCollection(Collection<T> c);
public static <T> Set<T> synchronizedSet(Set<T> s);
public static <T> List<T> synchronizedList(List<T> list);
public static <K,V> Map<K,V> synchronizedMap(Map<K,V> m);
public static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> s);
public static <K,V> SortedMap<K,V> synchronizedSortedMap(SortedMap<K,V> m);
```

Mỗi method trên trả về một synchronized (thread-safe) Collection được back up bởi collection đã chỉ định. Để đảm bảo truy cập tuần tự, tất cả access vào bộ backing collection phải được thực hiện thông qua returned collection. Cách dễ dàng để đảm bảo điều này là không giữ một reference đến backing collection. Tạo synchronized collection với thủ thuật sau:

```java
List<Type> list = Collections.synchronizedList(new ArrayList<Type>());
```

Một collection được tạo ra theo kiểu này là thread-safe từng bit tương tự như các synchronized collections thông thường, chẳng hạn như Vector.

Khi đối mặt với concurrent access, user bắt buộc phải đồng bộ hóa theo cách thủ công trên returned collection khi lặp qua collection đó. Lý do là quá trình lặp được thực hiện thông qua nhiều lệnh gọi vào collection đó, mà phải được tạo thành một atomic operation duy nhất. Sau đây là cách thức để lặp qua một wrapper-synchronized collection:

```java
Collection<Type> c = Collections.synchronizedCollection(myCollection);
synchronized(c) {
    for (Type e : c)
        foo(e);
}
```

Nếu một iterator tường minh được sử dụng, thì iterator() method phải được gọi từ bên trong synchronized block. Không tuân theo điều này có thể dẫn đến hành vi không xác định. Cách thức để lặp qua Collection view của synchronized Map cũng tương tự. User bắt buộc phải đồng bộ hóa trên synchronized Map khi lặp qua bất kỳ Collection view nào của nó thay vì đồng bộ hóa trên chính Collection view, như được minh họa trong ví dụ sau:

```java
Map<KeyType, ValType> m = Collections.synchronizedMap(new HashMap<KeyType, ValType>());
Set<KeyType> s = m.keySet();

// Synchronizing on m, not s!
synchronized(m) {
    while (KeyType k : s)
        foo(k);
}
```

Một nhược điểm nhỏ của việc sử dụng wrapper implementations là bạn không có khả năng thực thi bất kỳ non-interface operations nào của một wrapper implementations. Vì vậy, ví dụ, trong List example trước đó, bạn không thể gọi ensureCapacity() operation của ArrayList trên wrapped ArrayList.


### 6.2, Unmodifiable Wrappers

Không giống như các synchronization wrappers, bổ sung chức năng cho wrapped collection, các unmodifiable wrappers sẽ lấy đi chức năng. Đặc biệt, chúng lấy đi khả năng sửa đổi collection bằng cách chặn tất cả các operations sẽ sửa đổi collection và ném ra một UnsupportedOperationException. Các unmodifiable wrappers có 2 cách sử dụng chính, như sau:

+ Làm cho một collection trở nên immutable sau khi nó đã được xây dựng. Trong trường hợp này, bạn không nên giữ reference đến backing collection. Điều này hoàn toàn đảm bảo tính immutable.  
+ Cho phép một số clients nhất định truy cập read-only vào các data structures của bạn. Bạn giữ một reference đến backing collection nhưng đưa ra một reference đến trình wrapper. Bằng cách này, clients có thể xem nhưng không thể sửa đổi, trong khi bạn giữ quyền truy cập đầy đủ.  

Giống như các synchronization wrappers, mỗi interface trong số 6 core interfaces có một static factory method:

```java
public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> c);
public static <T> Set<T> unmodifiableSet(Set<? extends T> s);
public static <T> List<T> unmodifiableList(List<? extends T> list);
public static <K,V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m);
public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<? extends T> s);
public static <K,V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> m);
```


### 6.3, Checked Interface Wrappers

Các **Collections.checked** interface wrappers được cung cấp để sử dụng với các generic collections. Các implementations này trả về một *dynamically type-safe view* của collection được chỉ định, mà ném ra một ClassCastException nếu một clients cố gắng thêm một elements không đúng type. Cơ chế generics trong Java cung cấp tính năng compile-time (static) type-checking, nhưng cơ chế này có khả năng bị đánh bại. Dynamically type-safe view loại bỏ hoàn toàn khả năng này.