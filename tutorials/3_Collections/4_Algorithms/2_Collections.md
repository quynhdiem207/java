# Lesson 4: Algorithms

## The Collections Class

*Collections* class chỉ bao gồm các static methods hoạt động trên hoặc trả về các collections. Nó chứa các polymorphic algorithms hoạt động trên các collections, "wrappers" trả về một collection mới được back up bởi một collection cụ thể, và một số methods khác.

Tất cả các methods của class này đều ném ra một NullPointerException nếu các collections hoặc các Class object được cung cấp cho chúng là null.

Các "destructive" (hủy diệt) algorithms có trong class này, nghĩa là, các thuật toán sửa đổi các collections mà chúng hoạt động, được chỉ định để ném UnsupportedOperationException nếu collection không hỗ trợ mutation primitive(s) thích hợp, chẳng hạn như set method. Các thuật toán này có thể, nhưng không bắt buộc, phải đưa ra ngoại lệ này nếu một lời gọi không có tác dụng đối với collections. Ví dụ: việc gọi sort method trên một unmodifiable list mà đã được sắp xếp có thể có hoặc không ném ra UnsupportedOperationException.

```
Modifier and Type       |                        Method and Description
------------------------|--------------------------------------------------------------------------
static <T> boolean	    | addAll(Collection<? super T> c, T... elements)
                        | - Thêm tất cả các elements được chỉ định vào collection được chỉ định. Các
                        |   elements được thêm vào có thể được chỉ định riêng lẻ hoặc dưới dạng một
                        |   mảng.
                        | - Tương tự với c.addAll(Arrays.asList(elements)), nhưng methods này có khả
                        |   năng chạy nhanh hơn đáng kể trong hầu hết các implementations.
                        | - Khi các elements được chỉ định riêng lẻ, methods này cung cấp một cách
                        |   thuận tiện để thêm một vài elements vào collection hiện có:
                        |   Collections.addAll(flavors, "Peaches 'n Plutonium", "Rocky Racoon");
-------------------------|-------------------------------------------------------------------------
static <                 |
    T extends Object &   |
    Comparable<? super T>|
    > T	                 | max(Collection<? extends T> coll)
static <T> T	         | max(Collection<? extends T> coll, Comparator<? super T> comp)
static <                 |
    T extends Object &   |
    Comparable<? super T>|
    > T             	 | min(Collection<? extends T> coll)
static <T> T	         | min(Collection<? extends T> coll, Comparator<? super T> comp)
                         | 
                         | - Trả về element lớn nhất / nhỏ nhất của collection đã cho, theo thứ tự
                         |   tự nhiên của các element của nó, hoặc theo comparayor đã chỉ định.
-------------------------|-------------------------------------------------------------------------
static <T> int	        | binarySearch(List<? extends Comparable<? super T>> list, T key)
static <T> int	        | binarySearch(List<? extends T> list, T key, Comparator<? super T> c)
                        | 
                        | - Tìm kiếm trong List được chỉ định cho object được chỉ định bằng cách sử
                        |   dụng thuật toán tìm kiếm nhị phân. List phải được sắp xếp theo thứ tự tăng
                        |   dần theo thứ tự tự nhiên của các elements của nó (theo sort(List) method),
                        |   hoặc theo comparator được cung cấp (theo sort(List, Comparator) method)
                        |   trước khi thực hiện lệnh gọi này. Nếu nó không được sắp xếp, kết quả là 
                        |   xác định. 
                        | - Nếu List chứa nhiều elements bằng object được chỉ định, không có gì đảm
                        |   bảo rằng cái nào sẽ được tìm thấy.
------------------------|--------------------------------------------------------------------------
static <T> Collection<T>| synchronizedCollection(Collection<T> c)
static <T> List<T>	    | synchronizedList(List<T> list)
static <K,V> Map<K,V>	| synchronizedMap(Map<K,V> m)
static <K,V>            |
    NavigableMap<K,V>	| synchronizedNavigableMap(NavigableMap<K,V> m)
static <T> Set<T>	    | synchronizedSet(Set<T> s)
static <T>              |
    NavigableSet<T>	    | synchronizedNavigableSet(NavigableSet<T> s)
static <K,V>            |
    SortedMap<K,V>	    | synchronizedSortedMap(SortedMap<K,V> m)
static <T> SortedSet<T>	| synchronizedSortedSet(SortedSet<T> s)
                        |
                        | - Trả về synchronized collection/list/map/navigable map/set/navigable set/
                        |   sorted set/sorted map (thread-safe) được back up bởi collection đã chỉ
                        |   định. 
                        | - Để đảm bảo truy cập tuần tự, điều quan trọng là tất cả quyền truy cập vào
                        |   backing collection phải được thực hiện thông qua returned collection (hoặc
                        |   các views của nó).
                        | - User bắt buộc phải đồng bộ hóa theo cách thủ công trên returned collection
                        |   khi lặp qua nó hoặc bất kỳ views nào của nó.
                        |
                        |   SortedSet s = Collections.synchronizedSortedSet(new TreeSet());
                        |   SortedSet s2 = s.headSet(foo);
                        |       ...
                        |   synchronized (s) {  // Note: s, not s2!!!
                        |       Iterator i = s2.iterator(); // Must be in the synchronized block
                        |       while (i.hasNext())
                        |           foo(i.next());
                        |   }
------------------------|--------------------------------------------------------------------------
static <T> Collection<T>| unmodifiableCollection(Collection<? extends T> c)
static <T> List<T>	    | unmodifiableList(List<? extends T> list)
static <K,V> Map<K,V>	| unmodifiableMap(Map<? extends K,? extends V> m)
static <K,V>            |
    NavigableMap<K,V>	| unmodifiableNavigableMap(NavigableMap<K,? extends V> m)
static <T> Set<T>	    | unmodifiableSet(Set <? extends T> s)
static <T>              |
    NavigableSet<T>	    | unmodifiableNavigableSet(NavigableSet<T> s)
static <K,V>            |
    SortedMap<K,V>	    | unmodifiableSortedMap(SortedMap<K,? extends V> m)
static <T> SortedSet<T>	| unmodifiableSortedSet(SortedSet<T> s)
                        |
                        | - Trả về một unmodifiable view của collection/list/map/navigable map/set/ 
                        |   navigable set/sorted set/ sorted map được chỉ định.
                        | - Các methods này cho phép các modules cung cấp cho user quyền truy cập
                        |   "read-only" vào internal collection. Các Query operations trên Returned
                        |   collection "đọc qua" collection được chỉ định và cố gắng sửa đổi returned
                        |   collection, cho dù trực tiếp hay thông qua iterator của nó, dẫn đến
                        |   UnsupportedOperationException.
                        | - Returned collection sẽ là serializable nếu collection được chỉ định là
                        |   serializable.
------------------------|--------------------------------------------------------------------------
static <E> Collection<E>| checkedCollection(Collection<E> c, Class<E> type)
static <E> List<E>	    | checkedList(List<E> list, Class<E> type)
static <K,V> Map<K,V>	| checkedMap(Map<K,V> m, Class<K> keyType, Class<V> valueType)
static <K,V>            |
    NavigableMap<K,V>	| checkedNavigableMap(NavigableMap<K,V> m, Class<K> keyType, Class<V> valueType)
static <E> Set<E>	    | checkedSet(Set<E> s, Class<E> type)
static <E>              |
    NavigableSet<E>	    | checkedNavigableSet(NavigableSet<E> s, Class<E> type)
static <K,V>            |
    SortedMap<K,V>	    | checkedSortedMap(SortedMap<K,V> m, Class<K> keyType, Class<V> valueType)
static <E> SortedSet<E>	| checkedSortedSet(SortedSet<E> s, Class<E> type)
static <E> Queue<E>	    | checkedQueue(Queue<E> queue, Class<E> type)
                        | 
                        | - Trả về dynamically typesafe view của collection/list/map/navigable map/
                        |   set/navigable set/sorted set/sorted map/queue được chỉ định.
                        | - Bất kỳ nỗ lực nào để chèn một element không đúng type sẽ dẫn đến một
                        |   ClassCastException ngay lập tức. Giả sử một collection không chứa các
                        |   elements có type không chính xác trước thời điểm một dynamically 
                        |   view được tạo ra và tất cả các truy cập tiếp theo vào collection đều diễn
                        |   ra thông qua view, nó được đảm bảo rằng collection không thể chứa một 
                        |   element có type không chính xác.
                        | - Returned collection sẽ là serializable nếu collection được chỉ định là
                        |   serialiable.
                        | - Vì null được coi là một giá trị của bất kỳ reference type nào, returned
                        |   collection cho phép chèn các phần tử null bất cứ khi nào backing collection
                        |   cho phép.
```