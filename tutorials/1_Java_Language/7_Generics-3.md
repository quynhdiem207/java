# Lession 7. Generics

## 9. Restrictions on Generics

Các hạn chế đối với generics bao gồm:

- Không thể khởi tạo các generic types với các primitive types,  
- Không thể tạo instance của các type parameters,  
- Không thể khai báo các static fields có type là các type parameters,  
- Không thể sử dụng các type parameters của generic class trong các static method declaration,  
- Không thể sử dụng Cast hoặc instanceof với các parameterized types,  
- Không thể tạo array của parameterized type,  
- Không thể create, catch, hoặc throw các object của parameterized type,  
- Không thể overload một method trong đó các formal parameter types của mỗi overload erasure là cùng một raw type.  


#### *1, Cannot Instantiate Generic Types with Primitive Types*

Bạn không thể tạo một parameterized type của một generic type với các type arguments là các primitive types.

```java
class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
```

*Khi tạo một Pair object, bạn không thể thay thế một primitive type cho type parameter K hoặc V:*

```java
Pair<int, char> p = new Pair<>(8, 'a');          // compile-time error
Pair<Integer, Character> p = new Pair<>(8, 'a'); // OK
```

*Note: Java compiler tự động thực hiện autoboxing 8 thành Integer.valueOf(8) và 'a' thành new Character('a'):*

```java
Pair<Integer, Character> p = new Pair<>(Integer.valueOf(8), new Character('a'));
```


#### *2, Cannot Create Instances of Type Parameters*

Bạn không thể tạo một instance của một type parameter.

```java
public static <E> void append(List<E> list) {
    E elem = new E();  // compile-time error
    list.add(elem);
}
```

*Để giải quyết vấn đề, bạn có thể tạo một object của một type parameter thông qua reflection:*

```java
public static <E> void append(List<E> list, Class<E> cls) throws Exception {
    E elem = cls.newInstance();   // OK
    list.add(elem);
}
```

*Bạn có thể gọi append method như sau:*

```java
List<String> ls = new ArrayList<>();
append(ls, String.class);
```


#### *3, Cannot Declare Static Fields Whose Types are Type Parameters*

Các static fields của một class là một class-level variable được chia sẻ bởi tất cả các objects của class. Do đó, các static fields của type parameter là không được phép.

```java
public class MobileDevice<T> {
    private static T os; // compile-time error
}
```

*Nếu static field của các type parameter được cho phép, thì đoạn mã sau sẽ bị nhầm lẫn:*

```java
MobileDevice<Smartphone> phone = new MobileDevice<>();
MobileDevice<Pager> pager = new MobileDevice<>();
MobileDevice<TabletPC> pc = new MobileDevice<>();
```

*Bởi vì static field os được chia sẻ bởi phone, pager, và pc, nên type thực tế của os là gì? Nó không thể đồng thời là Smartphone, Pager, và TabletPC. Do đó, bạn không thể tạo các static fields của type parameters.*


#### *4, Cannot Use Casts or instanceof with Parameterized Types*

Bởi vì Java compiler xóa tất cả các type parameters trong generic code, bạn không thể xác minh parameterized type nào của một generic type được sử dụng tại runtime:

```java
public static <E> void rtti(List<E> list) {
    if (list instanceof ArrayList<Integer>) {  // compile-time error
        ...
    }
}
```

Tập các parameterized types được truyền cho rtti method là:

```java
S = { ArrayList<Integer>, ArrayList<String> LinkedList<Character>, ... }
```

*Runtime không theo dõi các type parameter, vì vậy nó không thể phân biệt được sự khác biệt giữa `ArrayList<Integer>` và `ArrayList<String>`. Bạn có thể làm nhiều nhất là sử dụng unbounded wildcard để xác minh rằng danh sách là một ArrayList:*

```java
public static void rtti(List<?> list) {
    if (list instanceof ArrayList<?>) {  // OK; instanceof requires a reifiable type
        ...
    }
}
```

Thông thường, bạn không thể cast (ép kiểu) sang một parameterized type trừ khi nó được tham số hóa bởi các unbounded wildcards.

```java
List<Integer> li = new ArrayList<>();
List<Number>  ln = (List<Number>) li;  // compile-time error
```

Tuy nhiên, trong một số trường hợp, compiler biết rằng một type parameter luôn hợp lệ và cho phép ép kiểu.

```java
List<String> l1 = ...;
ArrayList<String> l2 = (ArrayList<String>) l1;  // OK
```


#### *5, Cannot Create Arrays of Parameterized Types*

Bạn không thể tạo các arrays của các parameterized types.

```java
List<Integer>[] arrayOfLists = new List<Integer>[2];  // compile-time error
```

*Ví dụ sau minh họa điều gì sẽ xảy ra khi các types khác nhau được chèn vào cùng một array:*

```java
Object[] strings = new String[2];
strings[0] = "hi";   // OK
strings[1] = 100;    // An ArrayStoreException is thrown.

Object[] stringLists = new List<String>[2]; // compiler error, nhưng giả sử nó được phép
stringLists[0] = new ArrayList<String>();   // OK
stringLists[1] = new ArrayList<Integer>();  // Một ArrayStoreException được ném,
                                            // nhưng runtime không thể phát hiện nó.
```

*Nếu các arrays của parameterized lists được phép, thì code trên sẽ fail khi throw ArrayStoreException mong muốn.*


#### *6, Cannot Create, Catch, or Throw Objects of Parameterized Types*

Một generic class không thể extends Throwable class trực tiếp hoặc gián tiếp. 

```java
// Extends Throwable indirectly
class MathException<T> extends Exception { ... }    // compile-time error

// Extends Throwable directly
class QueueFullException<T> extends Throwable { ... } // compile-time error
```

Một method không thể catch một instance của một type parameter:

```java
public static <T extends Exception, J> void execute(List<J> jobs) {
    try {
        for (J job : jobs)
            ...
    } catch (T e) {   // compile-time error
        ...
    }
}
```

Tuy nhiên, bạn có thể sử dụng một type parameter trong một throws clause:

```java
class Parser<T extends Exception> {
    public void parse(File file) throws T {     // OK
        ...
    }
}
```


#### *7, Cannot Overload a Method Where the Formal Parameter Types of Each Overload Erase to the Same Raw Type*

Một class không thể có 2 overloaded method mà sẽ có cùng một signature sau type erasure.

```java
public class Example {
    public void print(Set<String> strSet) { }  // compile-time error
    public void print(Set<Integer> intSet) { }
}
```

Tất cả các overloads sẽ chia sẻ cùng một classfile đại diện và sẽ tạo ra compile-time error.