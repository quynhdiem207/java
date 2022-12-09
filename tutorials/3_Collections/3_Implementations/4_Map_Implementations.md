# Lession 3. Implementations

## 3. Map Implementations

Các Map implementations bao gồm: general-purpose, special-purpose, và concurrent implementations. Tất cả chúng đều là các subclass của *AbstractMap* class.

```java
public abstract class AbstractMap<K,V> extends Object
    implements Map<K,V> {}
```

*AbstractMap* class cung cấp một skeletal implementation (triển khai cốt lõi) của Map interface.

Để triển khai một *unmodifiable map*, programmer chỉ cần extends AbstractMap và cung cấp một implementation cho `entrySet` method mà trả về một set-view của các mappings của map, Returned set KHÔNG được hỗ trợ các add hoặc remove methods và iterator của nó không được hỗ trợ remove method.

Để triển khai một *modifiable map*, ngoài cung cấp một implementation cho `entrySet` method, programmer phải ghi đè thêm `put` method của AbstractMap (nếu không sẽ ném ra một UnsupportedOperationException) và Iterator được trả về bởi `entrySet().iterator()` phải triển khai thêm `remove` method của nó.

Programmer nên cung cấp một no-argument và map constructor cho Map implementation.

AbstractMap cung cấp các implementations cho các Map operations sau: `size, isEmpty, constainsKey, constainsValue, get, put, putAll, remove(Object), clear, keySet, values, (abstract) entrySet`.

AbstractMap kế thừa các Map operations sau: `(default) getOrDefault, (default) putIfAbsent, (default) remove(Object,Object), (default) replace, (default) replace, (default) replaceAll, (default) merge, (default) compute, (default) computeIfAbsent, (default) computeIfPresent, (default) forEach`.

Ngoài ra *AbstractMap* class override các `equals, hashCode, clone, toString` methods của Object class; và thừa kế các methods sau từ *Object* class: `finalize, getClass, notify, notifyAll, wait, wait, wait`.


### 3.1, General-Purpose Map Implementations

Có 3 general-purpose Map implementations, bao gồm: **HashMap**, **TreeMap**, **LinkedHashMap**.

Nếu bạn cần các SortedMap operations hoặc key-ordered Collection-view iteration, hãy sử dụng *TreeMap*; nếu bạn muốn tốc độ tối đa và không quan tâm đến thứ tự lặp, hãy sử dụng *HashMap*; nếu bạn muốn hiệu suất gần bằng HashMap và lặp theo thứ tự chèn, hãy sử dụng *LinkedHashMap*.

Tất cả các general-purpose Map implementations đều cho phép *null* key và *null* value, đều không được hỗ trợ đồng bộ hóa.

Tất cả các general-purpose Map implementations đều triển khai *Serializable* và *Clonable* interface, nên có hỗ trợ `clone()` method, nhưng các keys và values không được cloned.


#### *3.1.1, HashMap*

*HashMap* là một Map implementation, nó không đảm bảo thứ tự của các entries trong map.

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {}
```

HashMap gần tương tự với HashTable, ngoại trừ việc không được đồng bộ hóa và cho phép null.

Một HashMap instance có 2 tham số ảnh hưởng đến hiệu suất của nó: dung lượng ban đầu (initial capacity) và hệ số tải (load faxtor). Dung lượng ban đầu là dung lượng tại thời điểm hash table được tạo. Hệ số tải là thước đo mức độ đầy của hash table trước khi dung lượng của nó được tự động tăng lên. Khi số lượng entries trong hash table vượt quá tích của hệ số tải và dung lượng hiện tại, hash table sẽ được băm lại (nghĩa là internal data structures sẽ được rebuilt).

Các constructors của *HashMap* bao gồm:

```
        Constructors               |                    Description
===================================|===============================================================
HashMap()                          | Tạo một empty Map mới có dung lượng ban đầu mặc định (16) và 
                                   | hệ số tải - load factor mặc định (0,75). 
-----------------------------------|---------------------------------------------------------------
HashMap(                           | Tạo một map mới có các mappings giống với map được chỉ định,
    Map<? extends K,? extends V> m | có hệ số tải mặc định (0,75) và dung lượng ban đầu đủ để chứa
)                                  | các mappings trong map được chỉ định.
-----------------------------------|---------------------------------------------------------------
HashMap(int initialCapacity)       | Tạo một empty Map mới có dung lượng ban đầu được chỉ định và
                                   | hệ số tải - load factor mặc định (0,75).
-----------------------------------|---------------------------------------------------------------
HashMap(                           | Tạo một empty Map mới có dung lượng ban đầu và hệ số tải được 
    int initialCapacity,           | chỉ định.
    float loadFactor               | 
)                                  | 
```

*HashMap* class là một modifiable map, nó ghi đè `put` method của *AbstractMap*, cung cấp implementation cho `entrySet` method, và thừa kế tất cả các `AbstractMap methods` còn lại.


#### *3.1.2, LinkedHashMap*

*LinkedHashMap* là một Hash table và linked list implementation của Map interface, với thứ tự lặp có thể dự đoán được, là thứ tự các keys được chèn vào map.

```java
public class LinkedHashMap<K,V> extends HashMap<K,V>
    implements Map<K,V> {}
```

Các constructors của *LinkedHashMap* bao gồm:

```
            Constructors                 |                       Description
=========================================|=========================================================
LinkedHashMap()                          | Tạo một empty insertion-ordered LinkedHashMap mới với 
                                         | dung lượng ban đầu mặc định (16) và load factor mặc định
                                         | (0.75).
-----------------------------------------|---------------------------------------------------------
LinkedHashMap(                           | Tạo một insertion-ordered LinkedHashMap mới chứa các 
    Map<? extends K,? extends V> m       | mappings giống với map được chỉ định.
)                                        | 
-----------------------------------------|---------------------------------------------------------
LinkedHashMap(int initialCapacity)       | Tạo một empty insertion-ordered LinkedHashMap mới với
                                         | dung lượng ban đầu được chỉ định và load factor mặc định
                                         | (0.75).
-----------------------------------------|---------------------------------------------------------
LinkedHashMap(                           | Tạo một empty insertion-ordered LinkedHashMap mới với
    int initialCapacity,                 | dung lượng ban đầu và load factor được chỉ định.
    float loadFactor                     |
)                                        |
-----------------------------------------|---------------------------------------------------------
LinkedHashMap(                           | Tạo một empty insertion-ordered LinkedHashMap mới với
    int initialCapacity,                 | dung lượng ban đầu, load factor, và ordering mode (chế độ
    float loadFactor,                    | sắp xếp) được chỉ định.
    boolean accessOrder                  |
)                                        |

```

LinkedHashMap cung cấp một constructor đặc biệt để tạo một linked hash map có thứ tự lặp là thứ tự mà các entries của nó được truy cập lần cuối, từ truy cập ít nhất đến gần đây nhất (access-order). Loại map này rất phù hợp để xây dựng LRU caches. 

Việc gọi các put, putIfAbsent, get, getOrDefault, compute, computeIfAbsent, computeIfPresent hoặc merge methods dẫn đến access đến entry tương ứng (giả sử nó tồn tại sau khi lệnh gọi hoàn tất). Các replace methods chỉ dẫn đến access của entry nếu value được thay thế. putAll method tạo ra một entry access cho mỗi mapping trong map được chỉ định, theo thứ tự các key-value mappings được cung cấp bởi entry set iterator của map đã chỉ định. Không có methods nào khác tạo ra entry access. Đặc biệt, các operations trên các collection-views không ảnh hưởng đến thứ tự lặp của backing map.

*LinkedHashMap* là một subclass của *HashMap*, nó thừa kế tất cả các `HashMap methods`.

Ngoài ra, LinkedHashMap còn cung cấp `removeEldestEntry` method mà có thể bị ghi đè để áp đặt chính sách tự động xóa các mappings cũ khi các mappings mới được thêm vào map. Điều này giúp dễ dàng để triển khai một custom cache.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
protected boolean | removeEldestEntry(Map.Entry<K,V> eldest)
                  | - Trả về true nếu map này xóa entry cũ nhất của nó. 
                  | - Method này được gọi bởi put và putAll sau khi chèn một entry mới vào map. 
                  |   Nó cung cấp cho người triển khai cơ hội để xóa bỏ entry cũ nhất mỗi khi entry
                  |   mới được thêm vào. Điều này rất hữu ích nếu map đại diện cho một cache: nó cho
                  |   phép map giảm mức tiêu thụ bộ nhớ bằng cách xóa các entries cũ.
```

*Ví dụ: ghi đè này sẽ cho phép map tăng lên đến 100 entries và sau đó nó sẽ xóa entries cũ nhất mỗi khi entries mới được thêm vào, duy trì trạng thái ổn định của 100 entries.*

```java
private static final int MAX_ENTRIES = 100;

protected boolean removeEldestEntry(Map.Entry eldest) {
    return size() > MAX_ENTRIES;
}
```


#### *3.1.3, TreeMap*

*TreeMap* là một NavigableMap implementation. Các phần tử được sắp xếp theo thứ tự tự nhiên của chúng hoặc bởi một Comparator được cung cấp tại thời điểm tạo Map, tùy thuộc vào constructor nào được sử dụng.

```java
public class TreeMap<K,V> extends AbstractMap<K,V>
    implements NavigableMap<K,V>, Cloneable, Serializable {}
```

Các constructors của *TreeMap* bao gồm:

```
            Constructors                 |                       Description
=========================================|=========================================================
TreeMap()                                | Tạo một empty tree Map mới, được sắp xếp theo thứ tự tự
                                         | nhiên của các elements của nó.
                                         | Tất cả các keys được chèn vào map phải implement
                                         | Comparable interface.
-----------------------------------------|---------------------------------------------------------
TreeMap(Comparator<? super K> comparator)| Tạo một empty tree Map mới, được sắp xếp theo comparator
                                         | được chỉ định.
-----------------------------------------|---------------------------------------------------------
TreeMap(Map<? extends K,? extends V> m)  | Tạo một tree Map mới chứa các mappings trong map được chỉ
                                         | định, sắp xếp theo thứ tự tự nhiên của các keys của nó.
                                         | Tất cả các keys được chèn vào map phải implement
                                         | Comparable interface.
-----------------------------------------|---------------------------------------------------------
TreeMap(SortedMap<E> s)                  | Tạo một empty tree Map mới chứa các elements giống nhau,
                                         | và sử dụng thứ tự giống như sorted Map đã chỉ định.
```

*TreeMap* implements **NavigableMap** - một interface extends *SortedMap* interface:

```java
public interface NavigableMap<K,V> extends SortedMap<K,V> {}
```

Ngoài các methods của *SortedMap* bao gồm: tất cả các `Map operations, comparator, firstKey, lastKey, headMap, tailMap, subMap`.

*NavigableMap* interface còn hỗ trợ các operations sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
K	              | ceilingKey(K key)
                  | - Trả về key nhỏ nhất trong map này mà "lớn hơn hoặc bằng" key đã cho, 
                  |   hoặc null nếu không có key đó.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | ceilingEntry(K key)
                  | - Trả về key-value mapping được liên kết với key nhỏ nhất trong map này mà 
                  |   "lớn hơn hoặc bằng" key đã cho, hoặc null nếu không có key đó.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
K	              | floorKey(K key)
                  | - Trả về key lớn nhất trong map này mà "nhỏ hơn hoặc bằng" key đã cho,
                  |   hoặc null nếu không có key đó.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | floorEntry(K key)
                  | - Trả về key-value mapping được liên kết với key lớn nhất trong map này mà 
                  |   "nhỏ hơn hoặc bằng" key đã cho, hoặc null nếu không có key đó.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
K	              | higherKey(K key)
                  |  - Trả về key nhỏ nhất trong map này mà "lớn hơn" key đã cho,
                  |    hoặc giá trị null nếu không có key như vậy.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | higherEntry(K key)
                  | - Trả về key-value mapping được liên kết với key nhỏ nhất trong map này mà 
                  |   "lớn hơn" key đã cho, hoặc null nếu không có key đó.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
K	              | lowerKey(E e)
                  |  - Trả về key lớn nhất trong map này mà "nhỏ hơn" key đã cho,
                  |    hoặc giá trị null nếu không có key như vậy.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | lowerEntry(K key)
                  | - Trả về key-value mapping được liên kết với key lớn nhất trong map này mà 
                  |   "nhỏ hơn" key đã cho, hoặc null nếu không có key đó.
                  | - Ném NullPointerException nếu key được chỉ định là null, và map này không cho
                  |   phép các null keys.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | firstEntry()
                  | - Trả về key-value mapping được liên kết với key nhỏ nhất trong map này, 
                  |   hoặc null nếu map là empty.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | lastEntry()
                  | - Trả về key-value mapping được liên kết với key lớn nhất trong map này, 
                  |   hoặc null nếu map là empty.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | pollFirstEntry()
                  | - Xóa bỏ và trả về key-value mapping được liên kết với key nhỏ nhất trong map này,
                  |   hoặc trả về null nếu map này trống.
------------------|--------------------------------------------------------------------------------
Map.Entry<K,V>	  | pollLastEntry()
                  | - Xóa bỏ và trả về key-value mapping được liên kết với key lớn nhất trong map này,
                  |   hoặc trả về null nếu map này trống.
------------------|--------------------------------------------------------------------------------
NavigableMap<K,V> |	headMap(K toKey, boolean inclusive)
                  | - Trả về view một phần của map này có các keys nhỏ hơn (hoặc bằng, nếu inclusive
                  |   là true) toKey.
------------------|--------------------------------------------------------------------------------
NavigableMap<K,V> |	tailMap(K fromKey, boolean inclusive)
                  | - Trả về view một phần của map này có các keys lớn hơn (hoặc bằng, nếu inclusive
                  |   là true) toKey. 
------------------|--------------------------------------------------------------------------------
NavigableMap<K,V> |	subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
                  | - Trả về view một phần của map này có các keys trong khoảng từ fromKey đến toKey.
                  |   Nếu fromKey và toKey bằng nhau, returned map là empty trừ khi fromInclusive và
                  |   toInclusive đều là true.
------------------|--------------------------------------------------------------------------------
NavigableMap<K,V> |	descendingMap()
                  | - Trả về reverse order view của các mappings có trong map này.
------------------|--------------------------------------------------------------------------------
NavigableSet<K>	  | navigableKeySet()
                  | - Trả về một NavigableSet view của các keys trong map này.
                  | - Returned Set hỗ trợ xóa bỏ element, xóa bỏ mapping tương ứng khỏi map, thông
                  |   qua các Iterator.remove, Set.remove, removeAll, retainAll và clear operations.
                  |   Nó không hỗ trợ các add hoặc addAll operations.
------------------|--------------------------------------------------------------------------------
NavigableSet<K>	  | descendingKeySet()
                  | - Trả về một reverse order NavigableSet view của các keys trong map này.
                  | - Returned Set hỗ trợ xóa bỏ element, xóa bỏ mapping tương ứng khỏi map, thông
                  |   qua các Iterator.remove, Set.remove, removeAll, retainAll và clear operations.
                  |   Nó không hỗ trợ các add hoặc addAll operations.
```

Đối với các range-view operations có các lưu ý sau:

+ Returned Map là back up của map này, vì vậy những thay đổi trong Returned Map được phản ánh trong map này và ngược lại.  
+ Returned Map hỗ trợ tất cả các map operations tùy chọn mà map này hỗ trợ.  
+ Returned Map sẽ ném một IllegalArgumentException khi cố gắng chèn một key bên ngoài phạm vi của nó.  

*TreeMap* class là một modifiable map, nó ghi đè `put` method của *AbstractMap*, cung cấp implementation cho `entrySet` method, và thừa kế tất cả các `AbstractMap methods` còn lại. Ngoài ra, nó còn cung cấp các implementations cho toàn bộ các của `NavigableMap methods`.


### 3.2, Special-Purpose Map Implementations

Có 3 special-purpose Map implementations, bao gồm: **EnumMap** và **WeakHashMap** và **IdentityHashMap**.


#### *3.2.1, EnumMap*

*EnumMap* là một high-performance Map implementation enum type keys. Tất cả các keys của một enum map phải có cùng enum type. 

```java
public class EnumMap<K extends Enum<K>,V> extends AbstractMap<K,V>
    implements Serializable, Cloneable {}
```

Các constructors của *EnumMap* bao gồm:

```
            Constructors          |                       Description
==================================|================================================================
EnumMap(Class<K> keyType)         | Tạo một empty enum map với key type được chỉ định.
----------------------------------|----------------------------------------------------------------
EnumMap(Map<K,? extends V> m)     | Tạo một enum map được khởi tạo từ map được chỉ định.
                                  | Nếu map được chỉ định là một EnumMap, thì constructor này hoạt
                                  | động giống hệt với EnumMap(EnumMap). Nếu không, map được chỉ định
                                  | phải chứa ít nhất một mapping (để xác định key type của enum map
                                  | mới).
----------------------------------|----------------------------------------------------------------
EnumMap(EnumMap<K,? extends V> m) | Tạo một enum map có cùng key type với enum map được chỉ định,
                                  | ban đầu chứa các mappings giống nhau (nếu có).
```

Các enum maps lưu trữ theo thứ tự tự nhiên của các keys (thứ tự mà các enum constants được khai báo).

Các *null* keys là KHÔNG được phép. Cố chèn một null key sẽ ném NullPointerException. Tuy nhiên, cố gắng kiểm tra sự hiện diện của null key, hoặc xóa bỏ một null key sẽ hoạt động bình thường.

EnumMap không được hỗ trợ đồng bộ hóa, EnumMap có triển khai *Serializable* và *Clonable* interface, nên có hỗ trợ `clone()` methods.

```
Modifier and Type                     |                 Method and Description
--------------------------------------|------------------------------------------------------------
EnumMap<K,V>	                      | clone()
                                      | - Trả về một bản sao của EnumMap instance này, bản thân các
                                      |   keys và values không được clone.
```

*EnumMap* là một *modifiable Map*, nó ghi đè `put` method của *AbstractMap*, cung cấp implementation cho `entrySet` method, và thừa kế tất cả các `AbstractMap methods` còn lại.


#### *3.2.2, WeakHashMap*

*WeakHashMap* là một Map implementation với các "weak keys". Một entry trong WeakHashMap sẽ tự động bị xóa khi key của nó không còn được sử dụng bình thường nữa. Chính xác hơn, sự hiện diện của một mapping cho một key nhất định sẽ không ngăn key đó bị garbage collector loại bỏ, nghĩa là, được thực thi finalizable, hoàn thiện và sau đó thu hồi. Khi một key đã bị loại bỏ, entry của nó sẽ bị xóa khỏi map.

```java
public class WeakHashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V> {}
```

Các constructor của *WeakHashMap* bao gồm:

```
            Constructors           |                    Description
===================================|===============================================================
WeakHashMap()                      | Tạo một empty WeakHashMap mới có dung lượng ban đầu mặc định (16) 
                                   | và hệ số tải - load factor mặc định (0,75). 
-----------------------------------|---------------------------------------------------------------
WeakHashMap(                       | Tạo một WeakHashMap mới có các mappings giống với map được chỉ 
    Map<? extends K,? extends V> m | định, có hệ số tải mặc định (0,75) và dung lượng ban đầu đủ để
)                                  | chứa các mappings trong map được chỉ định.
-----------------------------------|---------------------------------------------------------------
WeakHashMap(int initialCapacity)   | Tạo một empty WeakHashMap mới có dung lượng ban đầu được chỉ 
                                   | định và hệ số tải - load factor mặc định (0,75).
-----------------------------------|---------------------------------------------------------------
WeakHashMap(                       | Tạo một empty WeakHashMap mới có dung lượng ban đầu và hệ số
    int initialCapacity,           | tải được chỉ định.
    float loadFactor               | 
)                                  | 
```

*WeakHashMap* KHÔNG implements *Serializable* và Cloneable interface, nên nó KHÔNG hỗ trợ *clone()* method và Serialization.

WeakHashMap không được hỗ trợ đồng bộ hóa. Nó cho phép cả các *null* keys và *null* values.

*WeakHashMap* là một *modifiable map*, nó ghi đè `put` method của *AbstractMap*, cung cấp implementation cho `entrySet` method, và thừa kế tất cả các `AbstractMap methods` còn lại.


#### *3.2.3, IdentityHashMap*

*IdentityHashMap* là một Map implementation sử dụng "reference-equality" thay cho "object-equality" khi so sánh các keys và values. Nói cách khác, trong IdentityHashMap, 2 key k1 và k2 được coi là bằng nhau chỉ khi k1 == k2. (Trong các Map implementations bình thường như HashMap, 2 key k1 và k2 được coi là bằng nhau chỉ khi k1 == null ? k2 == null : k1.equals(k2).)

```java
public class IdentityHashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Serializable, Cloneable {}
```

Các constructor của *IdentityHashMap* bao gồm:

```
            Constructors             |                   Description
=====================================|=============================================================
IdentityHashMap()                    | Tạo một empty IdentityHashMap mới có kích thước tối đa dự kiến
                                     | mặc định (21).
-------------------------------------|-------------------------------------------------------------
IdentityHashMap(int expectedMaxSize) | Tạo một empty IdentityHashMap mới có kích thước tối đa dự kiến  
                                     | được chỉ định.
-------------------------------------|-------------------------------------------------------------
IdentityHashMap(                     | Tạo một IdentityHashMap mới chứa các keys-value mappings trong
    Map<? extends K,? extends V> m   | map đực chỉ định
)                                    | 
```

*IdentityHashMap* implements *Serializable* và Cloneable interface, nên nó hỗ trợ `clone()` method và Serialization.

IdentityHashMap không được hỗ trợ đồng bộ hóa. Nó cho phép cả các *null* keys và *null* values.

*IdentityHashMap* là một *modifiable map*, nó ghi đè `put` method của *AbstractMap*, cung cấp implementation cho `entrySet` method, và thừa kế tất cả các `AbstractMap methods` còn lại.


### 3.3, Concurrent Map Implementations

*java.util.concurrent* package chứa **ConcurrentMap** interface, extends *Map* với các atomic putIfAbsent, remove, và replace methods; và **ConcurrentHashMap** implementation của ConcurrentMap interface.

*ConcurrentHashMap* là một concurrent, high-performance implementation được back up bởi một hash table. Nó không bao giờ chặn khi thực hiện truy xuất và cho phép người sử dụng chọn concurrency level cho các bản updates. Nó được thiết kế để thay thế cho Hashtable: ngoài việc triển khai ConcurrentMap, nó hỗ trợ tất cả các legacy methods đặc biệt của Hashtable.