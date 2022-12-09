# Lession 1. Interfaces

## 3. The List Interface

Một *List* là một Collection có thứ tự, các elements của nó được lập index. List có thể chứa các elements trùng lặp. 

```java
public interface List<E> extends Collection<E> {}
```

Java platform chứa 2 general-purpose List implementations: **ArrayList** và **LinkedList**. 

Ngoài các operations được kế thừa từ Collection, List interface cung cấp các operations cho: 

- *Positional access* — thao tác các elements dựa trên index của chúng trong list. Bao gồm các methods: get, set, add, addAll, và remove.  
- *Search* — tìm kiếm một object cụ thể trong list và trả về index của nó. Bao gồm các methods: indexOf và lastIndexOf.  
- *Sort* — sắp xếp thứ tự của các elements trong list. Bao gồm sort method.  
- *Iteration* — Mở rộng ngữ nghĩa của Iterator để tận dụng tính tuần tự của list. Bao gồm các listIterator methods.  
- *Range-view* — sublist method trả về view của một phần của list.  

Ngoài ra, List cũng thay đổi hành vi của các *equals()* và *hashCode()* operations, cho phép các instances của List được so sánh một cách có ý nghĩa ngay cả khi các implementation types của chúng khác nhau, 2 instances của List là bằng nhau nếu chúng chứa các elements giống nhau theo cùng một thứ tự.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean	          | equals(Object o)
                  | - So sánh bằng giá trị của object được chỉ định với list này. 
                  | - Trả về true nếu object được chỉ định cũng là một list, 2 list có cùng kích thước,
                  |   và mọi cặp elements tương ứng trong 2 list đều bằng nhau,
                  |   Note: 2 elements e1 và e2 bằng nhau nếu e1==null ? e2==null : e1.equals(e2).
------------------|--------------------------------------------------------------------------------
int	              | hashCode()
                  | - Trả về giá trị hash code của list này. 
                  | - Hash code của một list là kết quả của phép tính sau: 
                  |      int hashCode = 1;
                  |      for (E e : list)
                  |         hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
                  | - Cho 2 list l1 và l2, nếu l1.equals(l2) == true thì l1.hashCode() == l2.hashCode() 
```

**Note**: Từ JDK 8 trở lên, bạn có thể dễ dàng tạo một List từ một nguồn bằng các aggregate operations:

*Ví dụ: Sử dụng các aggregate operations từ JDK 8 trở lên để tạo một List chứa các tên:*

```java
List<String> list = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());
```


### 4.1, Collection Operations

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int	              | size()
                  | - Trả về số lượng elements trong list này.
------------------|--------------------------------------------------------------------------------
boolean	          | isEmpty()
                  | - Trả về true nếu list này không chứa elements nào.
------------------|--------------------------------------------------------------------------------
boolean	          | contains(Object o)
                  | - Trả về true nếu list này chứa element được chỉ định.
                  |   Hay, chứa element thỏa mãn: o==null ? e==null : o.equals(e)
------------------|--------------------------------------------------------------------------------
boolean	          | containsAll(Collection<?> c)
                  | - Trả về true nếu list này chứa tất cả các elements của collection được chỉ định.
------------------|--------------------------------------------------------------------------------
boolean	          | add(E e)
                  | (optional operation)
                  | - Thêm element được chỉ định vào cuối list này.
                  | - List có thể đặt ra các giới hạn về các elements có thể thêm vào list, 
                  |   và từ chối thêm bất kỳ element cụ thể nào bằng cách ném một ngoại lệ.
------------------|--------------------------------------------------------------------------------
boolean	          | addAll(Collection<? extends E> c)
                  | (optional operation)
                  | - Nối tất cả các elements trong collection được chỉ định vào cuối list này, 
                  |   theo thứ tự được trả về bởi iterator của collection được chỉ định.
                  | - Trả về true nếu list này bị thay đổi sau hành động.
------------------|--------------------------------------------------------------------------------
boolean	          | remove(Object o)
                  | (optional operation)
                  | - Loại bỏ lần xuất hiện đầu của element được chỉ định khỏi list này nếu nó có mặt.
                  |   Hay loại bỏ element có index nhỏ nhất: o==null ? get(i)==null : o.equals(get(i))
                  | - Trả về true nếu list này chứa element. 
                  | - Trả về false nếu list này sẽ không chứa element.
------------------|--------------------------------------------------------------------------------
boolean           | removeAll(Collection<?> c)
                  | (optional operation)
                  | - Xóa tất cả các elements của list này có trong collection được chỉ định.
                  | - Trả về true nếu list này bị thay đổi sau hành động.
------------------|--------------------------------------------------------------------------------
boolean           | retainAll(Collection<?> c)
                  | (optional operation)
                  | - Chỉ giữ lại trong list này các elements có trong collection được chỉ định.
                  | - Trả về true nếu list này bị thay đổi sau hành động.
------------------|--------------------------------------------------------------------------------
void	          | clear()
                  | (optional operation)
                  | - Xóa tất cả các elements khỏi list này.
------------------|--------------------------------------------------------------------------------
Iterator<E>	      | iterator()
                  | - Trả về một iterator trên các elements trong list này theo thứ tự thích hợp,
                  |   từ element đầu tiên đến element cuối cùng. 
------------------|--------------------------------------------------------------------------------
Object[]	      | toArray()
                  | - Trả về một Object array mới chứa tất cả các element trong list này theo thứ tự.
------------------|--------------------------------------------------------------------------------
<T> T[]	          | toArray(T[] a)
                  | - Trả về một array chứa tất cả các element trong list này theo thứ tự thích hợp, 
                  |   Runtime type của array được trả về là type của array được chỉ định.
                  | - Nếu list này phù hợp với array được chỉ định, nó sẽ được trả về trong đó.
                  | - Nếu không, một array mới được cấp phát với runtime type của mảng được chỉ định, 
                  |   và kích thước của list này.
                  | - Nếu list này phù hợp với array được chỉ định và còn chỗ trống 
                  |   (tức là array có nhiều element hơn list này), 
                  |   thì các elements trong array phía sau các elements của list được đặt thành null.
                  | - Note: toArray(new Object[0]) tương tự với toArray().
-----------------------|---------------------------------------------------------------------------
default Spliterator<E> | spliterator()
                       | - Tạo một Spliterator trên các elements trong list này.
```

#### *The inherited methods*

Các operations được thừa kế từ *Collection* interface bao gồm:  

- stream()  
- parallelStream()  
- removeIf()  

Các methods được thừa kế từ *Iterable* interface bao gồm: forEach().

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default Stream<E> | stream()
                  | - Trả về một sequential Stream với collection này là nguồn.
                  | - Default implementation tạo một sequential Stream từ Spliterator của collection này.
------------------|--------------------------------------------------------------------------------
default Stream<E> | parallelStream()
                  | - Trả về một parallel Stream với collection này là nguồn.
                  | - Default implementation tạo một parallel Stream từ Spliterator của collection này.
------------------|--------------------------------------------------------------------------------
default boolean   | removeIf(Predicate<? super E> filter)
                  | - Xóa tất cả các elements của collection này thỏa mãn predicate đã cho.
                  | - Trả về true nếu collection này bị thay đổi sau hành động.
                  | - Predicate<T> là một functional interface có một "boolean test(T)" method.
------------------|--------------------------------------------------------------------------------
default void      | forEach(Consumer<? super T> action)
                  | - Thực hiện hành động đã cho với từng element của Iterable cho đến khi 
                  |   tất cả các elements đã được xử lý hoặc hành động ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface có một method "void accept(T)".
```

*Ví dụ: Nối 2 list:*

```java
list1.addAll(list2);

// or để không sửa đổi list ban đầu:
List<Type> list3 = new ArrayList<Type>(list1);
list3.addAll(list2);
```


### 4.2, Positional Access, Search and Sort Operations

Các positional access và search operations cơ bản bao gồm:

```
Modifier and Type |                        Method and Description
------------------|----------------------------------------------------------------------
void              | add(int index, E element)
                  | (optional operation)
                  | - Chèn element đã chỉ định vào vị trí đã chỉ định trong list này. 
                  | - Dịch chuyển element hiện đang ở vị trí đó (nếu có) và bất kỳ element 
                  |   nào tiếp theo sang bên phải (index của chúng tăng 1).
------------------|----------------------------------------------------------------------
boolean           | addAll(int index, Collection<? extends E> c)
                  | (optional operation)
                  | - Chèn tất cả các elements trong collection đã chỉ định vào list này,
                  |   ở vị trí đã chỉ định. 
                  | - Dịch chuyển elements hiện đang ở vị trí đó (nếu có) và bất kỳ elements
                  |   nào tiếp theo sang bên phải (tăng index của chúng). 
                  | - Các elements mới sẽ xuất hiện trong list này theo thứ tự mà chúng 
                  |   được trả về bởi iterator của collection được chỉ định.
------------------|----------------------------------------------------------------------
int               | indexOf(Object o)
                  | - Trả về index của lần xuất hiện đầu tiên của element được chỉ định 
                  |   trong list này, hay trả về index nhỏ nhất thỏa mãn: 
                  |       (o==null ? get(i)==null: o.equals (get(i)))
                  | - Hoặc trả về -1 nếu list này không chứa element được chỉ định. 
------------------|----------------------------------------------------------------------
int               | lastIndexOf(Object o)
                  | - Trả về index của lần xuất hiện cuối cùng của element được chỉ định 
                  |   trong list này, hay trả về index lớn nhất thỏa mãn: 
                  |       (o==null ? get(i)==null: o.equals (get(i)))
                  | - Hoặc trả về -1 nếu list này không chứa element được chỉ định. 
------------------|----------------------------------------------------------------------
E                 | get(int index)
                  | - Trả về element ở vị trí đã chỉ định trong list này.
------------------|----------------------------------------------------------------------
E                 | set(int index, E element)
                  | (optional operation)
                  | - Thay thế element ở vị trí được chỉ định trong list này bằng element
                  |   được chỉ định.
                  | - Trả về element cũ trước đó ở vị trí được chỉ định trong list.
------------------|----------------------------------------------------------------------
E                 | remove(int index)
                  | (optional operation)
                  | - Xóa bỏ element ở vị trí được chỉ định trong list này. 
                  |   Di chuyển bất kỳ element tiếp theo nào sang trái (index của chúng giảm 1).
                  | - Trả về element đã bị xóa khỏi list.
```

*Ví dụ 1: polymorphic algorithm swap trong Collections class, giúp hoán đổi 2 giá trị trong một list:*

```java
public static <E> void swap(List<E> a, int i, int j) {
    E tmp = a.get(i);
    a.set(i, a.get(j));
    a.set(j, tmp);
}
```

*Ví dụ 2: polymorphic algorithm shuffle trong Collections class, giúp hoán đổi ngẫu nhiên các giá trị trong một list:*

```java
public static void shuffle(List<?> list, Random rnd) {
    for (int i = list.size(); i > 1; i--)
        swap(list, i - 1, rnd.nextInt(i));
}
```

*Ví dụ 3: In các giá trị trong argument list của một chương trình theo thứ tự ngẫu nhiên:*

```java
import java.util.*;

public class Shuffle {
    public static void main(String[] args) {
        List<String> list = Arrays.asList(args);
        Collections.shuffle(list);
        System.out.println(list);
    }
}
```

*Arrays class có một static factory method được gọi là asList, cho phép một mảng được xem như một List, method này không sao chép mảng. Các thay đổi trong List ghi vào mảng và ngược lại. List kết quả không phải là một general-purpose List implementation, vì nó không triển khai các optional add và remove operations: Mảng không thể thay đổi kích thước.*

Ngoài các positional access và search operations cơ bản, List interface còn hỗ trợ các methods giúp sắp xếp và thay thế các elements theo xử lý được cung cấp dưới dạng argument như sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default void      | replaceAll(UnaryOperator<E> operator)
                  | - Thay thế từng element của list này bằng kết quả của việc áp dụng operator cho
                  |   element đó.
                  | - UnaryOperator<T> là một functional interface được mở rộng từ Function<T, T> 
                  |   với functional method "T apply(T)".
                  | - Default implementation tương đương với:  
                  |       final ListIterator<E> li = list.listIterator();
                  |       while (li.hasNext()) {
                  |           li.set(operator.apply(li.next()));
                  |       }
                  | - Nếu list-iterator của list không hỗ trợ set operation thì một
                  |   UnsupportedOperationException sẽ được ném ra thay thế element đầu tiên.
------------------|--------------------------------------------------------------------------------
default void      | sort(Comparator<? super E> c)
                  | - Sắp xếp list này theo thứ tự được tạo ra bởi Comparator được chỉ định.
                  | - Comparator<T> là một functional interface với "int compare(T, T)" method.
                  | - list này phải là modifiable, nhưng không cần phải là resizable.
```

*Ví dụ: Thay thế các phần tử trong list và sắp xếp theo thứ tự tăng dần:*

```java
public class Test {
    public static void main(String[] args) {
        Integer[] arr = {2, 1, 7, 5};
        List<Integer> lst = Arrays.asList(arr);
        lst.replaceAll(x -> (x + 1));
        lst.forEach(System.out::print);

        lst.sort((x, y) -> (x>y ? 1: x==y ? 0 : -1));
        lst.forEach(System.out::print);
    }
}
// Output:  3286
//          2368
```


### 4.3, Iterators

*Iterator* được trả về bởi *iterator* operation của List trả về các elements của list theo trình tự thích hợp. List cũng cung cấp một iterator khác phong phú hơn, được gọi là *ListIterator*, cho phép bạn duyệt qua list theo 1 trong 2 hướng, sửa đổi list trong quá trình lặp và lấy vị trí hiện tại của iterator trong list.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
ListIterator<E>   | listIterator()
                  | - Trả về một list iterator trên các elements trong list này (theo thứ tự thích hợp),
                  |   được đặt tại vị trí bắt đầu của list. 
------------------|--------------------------------------------------------------------------------
ListIterator<E>   | listIterator(int index)
                  | - Trả về một list iterator trên các elements trong list này (theo thứ tự thích hợp),
                  |   được đặt tại vị trí đã chỉ định trong list.
                  | - Index được chỉ định cho biết element đầu tiên sẽ được trả về bởi một lời gọi 
                  |   next() đầu tiên. Một lời gọi previous() đầu tiên sẽ trả về element có index-1.
```

*ListIterator* interface mở rộng từ *Iterator* interface:

```java
public interface ListIterator<E> extends Iterator<E>
```

*ListIterator* không có element hiện tại; vị trí con trỏ của nó luôn nằm giữa element sẽ được trả về bởi một lệnh gọi tới previous() và element sẽ được trả về bởi một lệnh gọi tới next(). Một iterator cho list có độ dài n có n+1 vị trí con trỏ hợp lệ, từ 0 đến n:

```
                    Element(0)   Element(1)   Element(2)   ... Element(n-1)
cursor positions: ^            ^            ^            ^                  ^
           index: 0            1            2            3                  n
```

*ListIterator* interface bao gồm các methods:  

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
boolean           | hasNext()
                  | - Trả về true nếu list iterator này có nhiều element hơn khi duyệt qua list theo 
                  |   forward direction. Hay, trả về true nếu next() sẽ trả về một element thay vì 
                  |   ném một ngoại lệ.
------------------|--------------------------------------------------------------------------------
boolean           | hasPrevious()
                  | - Trả về true nếu list iterator này có nhiều element hơn khi duyệt qua list theo 
                  |   reverse direction. Hay, trả về true nếu previous() sẽ trả về một element thay 
                  |   vì ném một ngoại lệ.
------------------|--------------------------------------------------------------------------------
E	              | next()
                  | - Trả về element tiếp theo trong list và tăng vị trí con trỏ. 
                  | - Có thể gọi method này lặp đi lặp lại để lặp qua list.
------------------|--------------------------------------------------------------------------------
int	              | nextIndex()
                  | - Trả về index của element sẽ được trả về bởi một lệnh gọi tiếp theo tới next(). 
                  | - Trả về kích thước của list nếu list iterator ở cuối list.
------------------|--------------------------------------------------------------------------------
E	              | previous()
                  | - Trả về element trước đó trong list và giảm vị trí con trỏ. 
                  | - Có thể gọi method này lặp đi lặp lại để lặp qua list.
------------------|--------------------------------------------------------------------------------
int	              | previousIndex()
                  | - Trả về index của element sẽ được trả về bởi một lệnh gọi tiếp theo tới previous(). 
                  | - Trả về -1 nếu list iterator ở đầu list.
------------------|--------------------------------------------------------------------------------
void              | add(E e)
                  | (optional operation)
                  | - Chèn element đã chỉ định vào list.
                  | - Element được chèn ngay trước element sẽ được trả về bởi next(), nếu có,
                  |   và sau element sẽ được trả về bởi previous(), nếu có.
                  | - Nếu list không chứa element nào, element mới sẽ trở thành element duy nhất.
                  | - Lệnh gọi tiếp theo tới next() sẽ không bị ảnh hưởng và một lệnh gọi tiếp theo
                  |   đến previous() sẽ trả về element mới.
------------------|--------------------------------------------------------------------------------
void	          | remove()
                  | (optional operation)
                  | - Xóa khỏi list này element cuối cùng được trả về bởi next() hoặc previous().
------------------|--------------------------------------------------------------------------------
void	          | set(E e)
                  | (optional operation)
                  | - Thay thế element cuối cùng được trả về bởi next() hoặc privious() bằng element
                  |   được chỉ định.
```

Ngoài ra, *ListIterator* interface thừa kế *forEachRemaining* method từ *Iterator* interface:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
default void	  | forEachRemaining(Consumer<? super E> action)
                  | - Thực hiện hành động đã cho với từng element còn lại cho đến khi tất cả các 
                  |   elements đã được xử lý hoặc hành động ném ra một ngoại lệ.
                  | - Consumer<T> là một functional interface với "void accept(T)" method.
```

*Ví dụ 1: Cách triển khai của List.indexOf:*

```java
public int indexOf(E e) {
    for (ListIterator<E> it = listIterator(); it.hasNext(); )
        if (e == null ? it.next() == null : e.equals(it.next()))
            return it.previousIndex();
    return -1; // Element not found
}
```

*Ví dụ 2: Polymorphic algorithm sau sử dụng set() để thay thế tất cả các lần xuất hiện của một giá trị được chỉ định bằng một giá trị khác:*

```java
public static <E> void replace(List<E> list, E val, E newVal) {
    for (ListIterator<E> it = list.listIterator(); it.hasNext(); )
        if (val == null ? it.next() == null : val.equals(it.next()))
            it.set(newVal);
}
```

*Ví dụ 3: Polymorphic algorithm sau sử dụng add() để thay thế tất cả các lần xuất hiện của một giá trị được chỉ định bằng các giá trị có trong list được chỉ định:*

```java
public static <E> void replace(List<E> list, E val, List<? extends E> newVals) {
    for (ListIterator<E> it = list.listIterator(); it.hasNext(); ){
        if (val == null ? it.next() == null : val.equals(it.next())) {
            it.remove();
            for (E e : newVals)
                it.add(e);
        }
    }
}
```


### 4.4, Range-View Operation

List interface cung cấp range-view operation: subList(int fromIndex, int toIndex), trả về một List view của một phần list này có các index nằm trong khoảng fromIndex đến toIndex.

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
List<E>           | subList(int fromIndex, int toIndex)
                  | - Trả về view của một bộ phận của list này từ fromIndex đến toIndex, 
                  |   Nếu fromIndex và toIndex bằng nhau, returned sublist trống. 
                  | - Returned sublist là back up của list này, vì vậy những thay đổi non-structural 
                  |   trong sublist được phản ánh trong list này và ngược lại. 
                  | - Returned sublist hỗ trợ tất cả các optional operations được hỗ trợ bởi list này.
                  | - Ngữ nghĩa của returned sublist bởi method này trở nên không xác định nếu list 
                  |   này được sửa đổi cấu trúc theo bất kỳ cách nào khác ngoài thông qua sublist. 
                  | - Các sửa đổi về cấu trúc là những sửa đổi làm thay đổi kích thước của list này.
```

*Half-open range* này phản ánh vòng lặp for điển hình:

```java
for (int i = fromIndex; i < toIndex; i++) {
    ...
}
```

Như cách thuật ngữ view ngụ ý, returned List là back up của backing list mà *subList* được gọi, vì vậy những thay đổi trong list này được phản ánh trong list kia và ngược lại.

Method này loại bỏ sự cần thiết của các range operations tường minh (thường tồn tại cho các arrays). Bất kỳ operation nào đều có thể sử dụng một List như một range operation bằng cách truyền một subList view thay vì toàn bộ List.

*Ví dụ 1: Xóa bỏ một loạt các elements trong một phạm vi (range) của một list:*

```java
list.subList(from, to).clear();
```

*Ví dụ 2: Tìm kiếm một element trong một phạm vi (range) của List, tuy nhiên chúng trả về index trong sublist, không phải backing list:*

```java
int i = list.subList(fromIndex, toIndex).indexOf(o);
int j = list.subList(fromIndex, toIndex).lastIndexOf(o);
```

**Note**: Bất kỳ polymorphic algorithm nào hoạt động trên một List, chẳng hạn như replace hay shuffle, đều hoạt động với returned List bởi *subList()*.

*Ví dụ 3: Đây là một polymorphic algorithm có triển khai sử dụng subList() để xử lý một ván bài từ một bộ bài. Nghĩa là, nó trả về một new List("ván bài") chứa số elements được chỉ định lấy từ cuối List("bộ bài") được chỉ định. Các elements trong ván bài sẽ bị loại bỏ khỏi bộ bài:*

```java
public static <E> List<E> dealHand(List<E> deck, int n) {
    int deckSize = deck.size();
    List<E> handView = deck.subList(deckSize - n, deckSize);
    List<E> hand = new ArrayList<E>(handView);
    handView.clear();
    return hand;
}
```

**Note**: Đối với nhiều List implementations phổ biến, chẳng hạn như ArrayList, hiệu suất xóa các elements ở cuối List về cơ bản tốt hơn nhiều so với việc xóa các elements ở đầu List.

**Note**: Ngữ nghĩa của List do *subList()* trả về trở nên không xác định nếu các elements được thêm vào hoặc xóa khỏi backing List theo bất kỳ cách nào khác ngoài thông qua returned List. Do đó, chỉ nên sử dụng returned List bởi *subList()* như một object tạm thời - để thực hiện một hoặc một chuỗi các range operations trên backing List.


### 4.5, List Algorithms

Hầu hết các polymorphic algorithm trong *Collections* class đều áp dụng riêng cho List, chúng giúp bạn thao tác các list rất dễ dàng. Một số thuật toán thườn sử dụng gồm:  

- **sort** — sắp xếp một List bằng cách sử dụng merge sort algorithm, cung cấp một cách sắp xếp ổn định và nhanh chóng (Một sắp xếp ổn định là một sắp xếp không sắp xếp lại các elements bằng nhau).  
- **shuffle** — hoán vị ngẫu nhiên các elements trong một List.  
- **reverse** — đảo ngược thứ tự của các elements trong một List.  
- **rotate** — xoay tất cả các elements trong List theo một khoảng cách cụ thể.  
- **swap** — hoán đổi các elements tại các vị trí được chỉ định trong một List.  
- **replaceAll** — thay thế tất cả các lần xuất hiện của một value được chỉ định bằng một value khác.  
- **fill** — ghi đè mọi elements trong List với giá trị được chỉ định.  
- **copy** — sao chép List nguồn vào List đích.  
- **binarySearch** — tìm kiếm một element trong một ordered List bằng cách sử dụng binary search algorithm.  
- **indexOfSubList** — trả về index của sublist đầu tiên của một List.  
- **lastIndexOfSubList** — trả về index của sublist cuối cùng của một List.  