# Lession 7. Generics

## 1. Why Use Generics?

Generic cho phép các types là các parameters khi định nghĩa các class, interface, constructor, method và field. Các type parameters cung cấp một cách để sử dụng lại code với input là các type arguments khác nhau.

So với non-generic code thì generic code có các lợi ích sau:

- Type checks mạnh hơn tại compile time.  
    Java compiler áp dụng type checking mạnh mẽ cho generic code và đưa ra các errors nếu code vi phạm các type safety. Sửa các compile-time errors dễ dàng hơn sửa các runtime errors khó tìm.  

- Loại bỏ casts (ép kiểu).  
    *Ví dụ: Đoạn code sau không có generics yêu cầu casting:*  

    ```java
    List list = new ArrayList();
    list.add("hello");
    String s = (String) list.get(0);
    ```  

    *Khi viết lại sử dụng generics, code không cần casting:*  

    ```java
    List<String> list = new ArrayList<String>();
    list.add("hello");
    String s = list.get(0);   // no cast
    ```  

- Cho phép programmers triển khai các thuật toán generic.  
    Bằng cách sử dụng generics, programmers có thể triển khai các thuật toán generic hoạt động trên các collections của các types khác nhau, có thể được tùy chỉnh, là type safe và dễ đọc hơn.  


## 2. Generic Types

Một *generic type* là một generic class hoặc interface mà được tham số hóa trên các types. 

Note: Trong cú pháp khai báo generic type, danh sách các type parameters phải đặt sau type name.

Xem chi tiết tại [Java specification about generic type](../../specifications/2_types-2.md#3-type-variables).

*Ví dụ: non-generic class Box hoạt động trên các objects của bất cứ types nào, cung cấp 2 method: set - thêm một object vào box, và get - lấy object ra khỏi box.*

```java
public class Box {
    private Object object;

    public void set(Object object) { this.object = object; }
    public Object get() { return object; }
}
```

*Bạn có thể chuyển bất cứ thứ gì vào box, miễn là không phải primitive value, do tại compile-time không có cách nào xác minh object được chuyển vào box, nên có thể dẫn đến runtime error khi bạn đặt một Integer object vào box và mong muốn lấy ra khỏi hộp một Integer object, trong khi một phần code khác chuyển nhầm một String. Để giải quyết vấn đề này, có thể sử dụng generic type:*

```java
/**
 * Generic version of the Box class.
 * @param <T> the type of the value being boxed
 */
public class Box<T> {
    private T t; // T stands for "Type"

    public void set(T t) { this.t = t; }
    public T get() { return t; }
}
```


#### *Type Parameter Naming Conventions*

Theo quy ước tên của một type parameter là một chữ cái in hoa duy nhất, nhằm mục đích phân biệt giữa một type variable và tên của một class hay interface.

Các tên thường được sử dụng phổ biến cho type parameter là:

+ E - Element (được sử dụng rộng rãi bởi Java Collections Framework)  
+ K - Key  
+ N - Number  
+ T - Type  
+ V - Value  
+ S,U,V,... - 2nd, 3rd, 4th types  


#### *Invoking and Instantiating a Generic Type*

Một generic type invocation tương tự như một method invocation, nhưng thay vì truyền các arguments cho method, bạn cần truyền các type arguments:  

```java
Box<Integer> integerBox;
```

Một generic type invocation thường được gọi là một *parameterized types*.

Để khởi tạo một object của một generic class, như thường lệ sử dụng new operator với một constructor invocation, đồng thời truyền các type arguments cần thiết:

```java
Box<Integer> integerBox = new Box<Integer>();
```


#### *The Diamond*

Từ Java SE 7 trở đi, khi gọi constructor của một generic class, có thể sử dụng một "empty set of type arguments" <> (được gọi là *diamond*) thay cho "list of type arguments" cần thiết, miễn là compiler có thể xác định hoặc suy ra các type arguments từ context.

*Ví dụ: Compiler có thể suy ra type argument khi gọi Box constructor từ variable declaration:*

```java
Box<Integer> integerBox = new Box<>();
```


#### *Multiple Type Parameters*

Một generic class hay interface có thể có nhiều type parameters:

```java
public interface Pair<K, V> {
    public K getKey();
    public V getValue();
}

public class OrderedPair<K, V> implements Pair<K, V> {

    private K key;
    private V value;

    public OrderedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey()	{ return key; }
    public V getValue() { return value; }

    public static void main(String... args) {
        // invoke the constructor of a generic class to instantiate an object,
        // type argument được cung cấp tường minh
        Pair<String, Integer> p1 = new OrderedPair<String, Integer>("Even", 8);
        Pair<String, String>  p2 = new OrderedPair<String, String>("hello", "world");

        // or using the diamond
        OrderedPair<String, Integer> p1 = new OrderedPair<>("Even", 8);
        OrderedPair<String, String>  p2 = new OrderedPair<>("hello", "world");
    }
}
```


#### *Parameterized Types*

Một *parameterized type* là một generic type invocation được truyền các type arguments:

```java
Box<Integer> integerBox;
```

Các type arguments cũng có thể là các parameterized types, ví dụ:

```java
OrderedPair<String, Box<Integer>> p = new OrderedPair<>("primes", new Box<Integer>(...));
```


### 2.1, Raw Types

Một *raw type* là một generic type không được cung cấp bất kỳ type arguments nào.

*Ví dụ: Với generic class `Box<T>`, có thể tạo một parameterized type `Box<Integer>` của `Box<T>`, nhưng nếu bỏ qua type argument sẽ tạo ra một raw type Box của `Box<T>`, như sau:*

```java
public class Box<T> {              // generic type Box<T>
    public void set(T t) { ... }
}        
Box<Integer> intBox = new Box<>(); // parameterized type Box<Integer> của Box<T>
Box rawBox = new Box();            // raw type Box của Box<T>
```

**Note**: Một non-generic type KHÔNG phải một raw type.

Các raw type hiển thị trong mã kế thừa vì rất nhiều API class (chẳng hạn như các Clollections class) không phải là generic type trước JDK 5.0. Khi sử dụng các raw type, về cơ bản bạn sẽ có được pre-generic behavior (*vd: một Box cung cấp cho bạn các Object*). 

Để tương thích ngược, cho phép gán một parameterized type cho raw type tương ứng của nó:

```java
Box<String> stringBox = new Box<>();
Box rawBox = stringBox;               // OK
```

Nhưng nếu bạn gán một raw type cho một parameterized type, bạn sẽ nhận được một warning:

```java
Box rawBox = new Box();        // rawBox is a raw type of Box<T>
Box<Integer> intBox = rawBox;  // warning: unchecked conversion
```

Bạn cũng nhận được một warning nếu bạn sử dụng một raw type để gọi các methods có phụ thuộc vào type variable của trong generic type tương ứng:

```java
Box<String> stringBox = new Box<>();
Box rawBox = stringBox;
rawBox.set(8);  // warning: unchecked invocation to set(T)
```

Warning cho thấy rằng các raw type bỏ qua kiểm tra generic type, trì hoãn việc bắt unsafe code trong runtime. Vì vậy, bạn nên tránh sử dụng các raw type.


#### *Unchecked Error Messages*

Bạn có thể nhận được các unchecked warning khi sử dụng các API cũ hoạt động trên các raw types:

```java
public class WarningDemo {
    public static void main(String[] args){
        Box<Integer> bi;
        bi = createBox();
    }

    static Box createBox(){
        return new Box();
    }
}
```

Thuật ngữ "unchecked" có nghĩa là compiler không có đủ thông tin về type để thực hiện tất cả các type checks cần thiết để đảm bảo type safety. Theo mặc định, "unchecked" warning bị tắt, mặc dù compiler đưa ra gợi ý. Để xem tất cả các "unchecked" warning, hãy compile lại với *-Xlint:unchecked* flag.

Khi compile lại với *-Xlint:unchecked* sẽ thấy các thông tin bổ sung sau:

```
WarningDemo.java:4: warning: [unchecked] unchecked conversion
found   : Box
required: Box<java.lang.Integer>
        bi = createBox();
                ^
            1 warning
```

Để tắt hoàn toàn các unchecked warnings, sử dụng -Xlint:-unchecked flag. @SuppressWarnings("unchecked") annotation ngăn chặn các unchecked warnings.


## 3. Generic Methods

Các *generic methods* là các methods giới thiệu các type parameters riêng của chúng. Cho phép các static và non-static generic methods, cũng như các generic constructors.

Note: Trong cú pháp khai báo generic method, danh sách các type parameters phải được đặt trước return type của method.

```java
public class Util {
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        return p1.getKey().equals(p2.getKey()) &&
               p1.getValue().equals(p2.getValue());
    }
}

public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) { this.key = key; }
    public void setValue(V value) { this.value = value; }
    public K getKey()   { return key; }
    public V getValue() { return value; }
}

public class Test {
    public static void main(String... args) {
        Pair<Integer, String> p1 = new Pair<>(1, "apple");
        Pair<Integer, String> p2 = new Pair<>(2, "pear");

        // type argument được cung cấp tường minh
        boolean same = Util.<Integer, String>compare(p1, p2); 

        // hoặc nếu type argument bị bỏ qua, compiler sẽ suy ra type cần thiết từ context
        boolean same = Util.compare(p1, p2);
    }
}
```

*Type inference*, cho phép bạn gọi một generic method như một method thông thường, mà không cần chỉ định type.


## 4. Bounded Type Parameters

Có thể đôi khi bạn muốn hạn chế các types có thể được sử dụng làm type argument để tạo nên các paramterized type của một generic type, hoặc cung cấp cho các generic methods hay constructor. Đây là ý nghĩa của các *bounded type parameters*.

*Ví dụ: một method hoạt động trên các number, có thể chỉ muốn chấp nhận các instance của Number hoặc các subclass của nó.*

Để khai báo một bounded type parameter, sử dụng **extends** keyword đặt sau tên của một type parameter, tiếp theo là danh sách *upper bound* của nó. (*Note: trong ngữ cảnh này, extends có ý nghĩa tương tự với "extends" một class và "implements" các interface.*)

*Ví dụ: generic method bao gồm một bounded type parameter, bây giờ việc compile sẽ không thành công, vì lệnh gọi inspect nhận được một String thay vì một Number hay một subtype của nó:*

```java
public class Box<T> {

    private T t;          

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public <U extends Number> void inspect(U u){
        System.out.println("T: " + t.getClass().getName());
        System.out.println("U: " + u.getClass().getName());
    }

    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<Integer>();
        integerBox.set(new Integer(10));
        integerBox.inspect("some text"); // error: this is still String!
    }
}
```

Ngoài việc giới hạn các types, các *bounded type parameters* cho phép bạn truy cập các members được định nghĩa trong các *bounds* của nó:

```java
public class NaturalNumber<T extends Integer> {

    private T n;

    public NaturalNumber(T n)  { this.n = n; }

    public boolean isEven() {
        // gọi intValue method được định nghĩa trong Integer class thông qua n.
        return n.intValue() % 2 == 0;
    }
}
```

#### *Multiple Bounds*

Một type parameter có thể có nhiều *bounds*. Danh sách các bounds của một type parameter, được phân tách bởi ký tự **&**, và tối đa chỉ có thể có 1 class nằm đầu tiên trong danh sách (nếu có), tiếp theo mới là các interfaces.

Một type variable với nhiều bounds sẽ là một subtype của tất cả các types được liệt kê trong danh sách. 

```java
Class A { ... }
interface B { ... }
interface C { ... }

class D <T extends A & B & C> { ... }
class D <T extends B & A & C> { ... } // compile-time error
```


### 4.1, Generic Methods and Bounded Type Parameters

Các *bounded type parameters* là chìa khóa để triển khai các generic algorithms (thuật toán). 

*Ví dụ: Đếm số lượng các elements lớn hơn một giá tri xác định trong một mảng T[]:*

```java
public static <T> int countGreaterThan(T[] anArray, T elem) {
    int count = 0;
    for (T e : anArray)
        if (e > elem)  // compiler error
            ++count;
    return count;
}
```

*Trong ví dụ trên bạn sẽ nhận được một compile-time error vì > operator chỉ áp dụng cho các primitive numbers, không sử dụng để so sánh các object. Để giải quyết vấn đề này, cần giới hạn type parameter T bởi interface `Comparable<T>`:*

```java
public interface Comparable<T> {
    public int compareTo(T o);
}

public static <T extends Comparable<T>> int countGreaterThan(T[] anArray, T elem) {
    int count = 0;
    for (T e : anArray)
        if (e.compareTo(elem) > 0)
            ++count;
    return count;
}
```


## 5. Generics, Inheritance, and Subtypes

Có thể gán một object của một type này cho một variable của type khác khi các types đó tương thích. 

*Ví dụ: bạn có thể gán một Integer cho một Object, hay truyền một Integer cho một method nhận Number object, vì Number là một trong những supertypes của Integer, nên Integer cũng là một loại Number:*

```java
Object someObject = new Object();
Integer someInteger = new Integer(10);
someObject = someInteger;   // OK

public void someMethod(Number n) { /* ... */ }
someMethod(new Integer(10));   // OK
someMethod(new Double(10.1));  // OK
```

*Ví dụ: parameterized type `Box<Number>` của generic type `Box<T>`, một box sẽ chứa một Number, bạn có thể add một Integer vào box, vì Integer cũng là một loại Number:*

```java
Box<Number> box = new Box<Number>();
box.add(new Integer(10));   // OK
box.add(new Double(10.1));  // OK
```

**Note**: Đối với 2 types A và B cụ thể (*vd: Number và Integer*), `MyClass<A>` không có mối quan hệ nào với `MyClass<B>`, bất kể A và B có liên quan với nhau hay không. Superclass chung của `MyClass<A>` và `MyClass<B>` là Object. Hay, `A <: B` không có nghĩa `MyClass<A> <: MyClass<B>`.  

*Ví dụ: Xét method sau, boxTest() chỉ nhận `Box<Number>`, bạn không thể truyền `Box<Integer>` hay `Box<Double>` cho method này, vì `Box<Integer>` và `Box<Double>` không phải các subtypes của `Box<Number>`:*

```java
public void boxTest(Box<Number> n) { ... }
```


#### *Generic Classes and Subtyping*

Các subtypes của một generic type là các type mà extends hay implements nó. Mối quan hệ giữa các parameterized type của các generic types này cũng được xác định thông qua extends và implements clause.

*Ví dụ: `ArrayList<E>` implements `List<E>`, và `List<E>` extends `Collection<E>`. Vì vậy, `ArrayList<String>` là một subtype của `List<String>`, cái mà là một subtype của `Collection<String>`, miễn là type argument của các parameterized type của các generic types này giống nhau, thì mối quan hệ subtype giữa các parameterized types vẫn giữ nguyên như giữa các generic types.*

```java
ArrayList<E>        ------>   List<E>        ---->   Collection<E>
ArrayList<String>   ------>   List<String>   ---->   Collection<String>
ArrayList<Object>   ---X-->   List<String>
```

*Xét ví dụ sau:*

```java
interface PayloadList<E,P> extends List<E> { ... }
```

*Trong ví dụ trên, hệ thống phân cấp theo quan hệ subtype như sau:*

```java
                                Collection<String>
                                        |
                                    List<String>
                                        |
        -----------------------------------------------------------------
        |                               |                               |
PayloadList<String, String>   PayloadList<String, Integer>   PayloadList<String, Exception>
```