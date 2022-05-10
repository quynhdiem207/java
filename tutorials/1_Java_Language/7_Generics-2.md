# Lession 7. Generics

## 6. Type Inference

*Type inference* là khả năng của Java compiler, nó xem xét từng method invocation và declaration tương ứng để xác định các type arguments mà invocation có thể áp dụng. Thuật toán suy luận xác định type của các arguments và type của expression đang được gán hoặc được trả về (nếu có). Cuối cùng, cố gắng tìm ra type cụ thể nhất hoạt động với tất cả các arguments.

*Ví dụ: Type inference xác định argument thứ 2 được truyền cho pick() method có type Serializable:*

```java
static <T> T pick(T a1, T a2) { return a2; }
Serializable s = pick("d", new ArrayList<String>());
```


#### *Type Inference and Generic Methods*

Java compiler có thể suy ra các type argument của một generic method invocation. Do đó, trong hầu hết các trường hợp, bạn không cần phải chỉ định chúng. 

*Ví dụ: Để gọi addBox() method, bạn có thể chỉ định type argument là Integer, nếu không compiler sẽ tự động suy ra (từ các argument của method) rằng type argument là Integer:*

```java
import java.util.ArrayList;
import java.util.List;

public class BoxDemo {
    public static <U> void addBox(U u, List<Box<U>> boxes) {
        Box<U> box = new Box<>();
        box.set(u);
        boxes.add(box);
    }

    public static void main(String[] args) {
        ArrayList<Box<Integer>> listOfIntegerBoxes = new ArrayList<>();

        BoxDemo.<Integer>addBox(Integer.valueOf(10), listOfIntegerBoxes);
        BoxDemo.addBox(Integer.valueOf(20), listOfIntegerBoxes); // OK
    }
}
```


#### *Type Inference and Instantiation of Generic Classes*

Từ Java SE 7 trở đi, khi gọi constructor của một generic class, có thể sử dụng một "empty set of type arguments" <> (được gọi là *diamond*) thay cho "list of type arguments" cần thiết, miễn là compiler có thể xác định hoặc suy ra các type arguments từ context.

```java
Map<String, List<String>> myMap = new HashMap<String, List<String>>();
Map<String, List<String>> myMap = new HashMap<>(); // OK
```

**Note**: Sử dụng diamond (<>) để tận dụng lợi ích của type inference trong khi khởi tạo generic class, không nên sử dụng raw type. 

*Ví dụ: Compiler tạo một unchecked conversion warning vì HashMap() constructor tham chiếu đến HashMap raw type, không phải Map<String, List<String>> type:*

```java
Map<String, List<String>> myMap = new HashMap(); // unchecked conversion warning
```


#### *Type Inference and Generic Constructors of Generic and Non-Generic Classes*

Các constructors có thể là generic, nói cách khác, chúng có thể khai báo các type parameters riêng, trong cả generic và non-generic classes.  

*Ví dụ: Xem xét class declaration:*

```java
class MyClass<X> {
    <T> MyClass(T t) { ... }
}
```

*Class instance creation expression dưới đây sẽ tạo ra một instance của parameterized type MyClass<Integer>, nó chỉ định tường minh type Integer cho formal type parameter X của generic class MyClass<X>. Lưu ý rằng constructor của generic class này chứa một formal type parameter T. Compiler suy ra type String cho formal type parameter T của constructor của generic class này (vì argument thực của constructor này là một String object).*

```java
new MyClass<Integer>("");
```

**Note**: Các compiler từ trước Java SE 7 có thể suy ra các type argument thực tế của các generic constructor invocations tương tự như các generic method invocations. Tuy nhiên, các compiler từ Java SE 7 trở đi có thể suy ra các type argument thực của generic class đang được khởi tạo nếu bạn sử dụng diamond (<>). 

*Ví dụ: Compiler suy ra type Integer cho formal type parameter X của generic class MyClass<X>. Nó suy ra type String cho formal type parameter T của constructor của generic class này:*

```java
MyClass<Integer> myObject = new MyClass<>("");
```

**Note**: Thuật toán suy luận chỉ sử dụng các invocation arguments, target types, và có thể là return type được mong đợi rõ ràng để suy ra type; nó không sử dụng kết quả từ phần sau của chương trình.


#### *Target Types*

Java compiler tận dụng target type để suy ra các type argument của generic method invocation. *Target type* của một expression là data type mà Java compiler mong đợi tùy thuộc vào vị trí expression xuất hiện. 

*Xét ví dụ sau:*

```java
static <T> List<T> emptyList();
```

*Câu lệnh gán sau mong đợi một instance của List<String>, đây là target type. Bởi vì emptyList() method trả về một giá trị của List<T>, compiler suy ra type argument T phải là String. Điều này hoạt động trong cả Java SE 7 và 8.*

```java
List<String> listOne = Collections.emptyList();
```

*Ngoài ra, có thể chỉ định tường minh type argument cho T, tuy nhiên điều này là không cần thiết trong context này:*

```java
List<String> listOne = Collections.<String>emptyList();
```

**Note**: Kể từ Java SE 8 trở đi, khái niệm *target type* được mở rộng để bao gồm cả các *method argument types* được yêu cầu.

*Xét ví dụ sau:*

```java
void processStringList(List<String> stringList) { ... }
```

*Xét method invocation sau, Java compiler SE 7 sẽ tạo ra một error: "List<Object> cannot be converted to List<String>", Java compiler SE 7 yêu cầu một giá trị cho type argument của method invocation emptyList(), do không có thông tin về target type trong context này, nên nó suy ra type argument là Object, emptyList() sẽ trả về một giá trị của List<Object> không tương thích với processStringList() mong đợi, do đó trong Java SE 7 phải chỉ định tường minh type argument là String. Trong khi đó điều này không cần thiết trong Java SE 8, processStringList() yêu cầu một argument của List<String>, đây là target type, mà emptyList() trả về một giá trị của List<T>, nên compiler suy ra type argument là String.*

```java
processStringList(Collections.emptyList());
```


## 7. Wildcards

Trong generic code, ký tự ?, được gọi là *wildcard*, đại diện cho một loại *unknown type*. Wildcard có thể được sử dụng trong nhiều trường hợp khác nhau: như type của một field, parameter, hoặc local variable; đôi khi là một return type. Wildcard KHÔNG bao giờ được sử dụng làm type argument cho một *generic method invocation*, *generic class instance creation*, hoặc *supertype*.


### 7.1, Upper Bounded Wildcards

Upper bounded wildcard hạn chế unknown type thành một type cụ thể hoặc một subtype của type đó.

Có thể sử dụng upper bounded wildcard để nới lỏng các hạn chế đối với một variable. 

*Ví dụ: giả sử bạn muốn viết một method hoạt động trên List<Integer>, List<Double> và List<Number>; bạn có thể đạt được điều này bằng cách sử dụng upper bounded wildcard.*

Để khai báo một upper bounded wildcard, sử dụng ký tự wildcard ('?') theo sau là từ khóa **extends**, tiếp theo là *upper bound* của nó, VD: <? extends A>. (*Note: trong ngữ cảnh này, extends có ý nghĩa tương tự với "extends" một class và "implements" các interface.*)

*Ví dụ: Để viết một method hoạt động trên danh sách của Number và các subtypes của Number, chẳng hạn như Integer, Double và Float, bạn cần chỉ định List<? extends Number>. Type List<Number> hạn chế hơn List<? extends Number> vì cái trước chỉ khớp với danh sách kiểu Number, trong khi cái sau khớp với danh sách kiểu Number hoặc bất kỳ subtypes nào của nó.*

Ngoài ra, upper bounded wildcard cho truy cập các members được định nghĩa trong upper bound.

*Ví dụ: sumOfList() trả về tổng các số trong danh sách:*

```java
class Test {
    public static double sumOfList(List<? extends Number> list) {
        double s = 0.0;
        for (Number n : list)
            // gọi doubleValue method được định nghĩa trong các Number subclass thông qua n.
            s += n.doubleValue();
        return s;
    }

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1, 2, 3);
        System.out.println("sum = " + sumOfList(li)); // sum = 6.0

        List<Double> ld = Arrays.asList(1.2, 2.3, 3.5);
        System.out.println("sum = " + sumOfList(ld)); // sum = 7.0
    }
}
```


### 7.2, Unbounded Wildcards

Unbounded wildcard được chỉ định bằng cách sử dụng ký tự wildcard (?), *VD: List<?>, đây được gọi là list of unknown type (danh sách kiểu không xác định)*. 

Có 2 trường hợp trong đó ký tự unbounded wildcard rất hữu ích:

- Nếu bạn đang viết một method có thể được triển khai bằng các methods được cung cấp trong Object class.  
- Khi code đang sử dụng các methods trong một generic class mà không phụ thuộc vào type parameter. *VD: List.size hoặc List.clear*. Trên thực tế, Class<?> Thường được sử dụng vì hầu hết các methods trong Class<T> không phụ thuộc vào T.  

*Ví dụ: Mục tiêu của printList() in một danh sách của bất cứ type nào, nhưng nó không đạt được mục tiêu đó - nó chỉ in một danh sách các Object instances; nó không thể in List<Integer>, List<String>, List<Double>, ..., vì chúng không phải là subtypes của List<Object>:*

```java
public static void printList(List<Object> list) {
    for (Object elem : list)
        System.out.println(elem + " ");
    System.out.println();
}
```

*Trong ví dụ trên, để đạt được mục tiêu, cần viết một generic method printList, hãy sử dụng List<?>:*

```java
public static void printList(List<?> list) {
    for (Object elem: list)
        System.out.print(elem + " ");
    System.out.println();
}
```

*Bởi vì đối với bất kỳ reference type A cụ thể nào, List<A> đều là một subtype của List<?>, nên có thể sử dụng printList để in một danh sách của type bất kỳ:*

```java
List<Integer> li = Arrays.asList(1, 2, 3);
List<String>  ls = Arrays.asList("one", "two", "three");
printList(li);
printList(ls);
```

**Note**: List<Object> và List<?> không giống nhau, bạn có thể chèn một Object hoặc bất kỳ subtypes nào của Object vào List<Object>, nhưng bạn chỉ có thể chèn null vào một List<?>.


### 7.3, Lower Bounded Wildcards

Lower bounded wildcard hạn chế unknown type thành một type cụ thể hoặc một supertype của type đó.

Để khai báo một lower bounded wildcard, sử dụng ký tự wildcard ('?') theo sau là từ khóa **super**, tiếp theo là *lower bound* của nó, *VD: <? super A>*.

**Note**: Bạn có thể chỉ định upper bound cho một wildcard, hoặc bạn có thể chỉ định lower bound, nhưng bạn không thể chỉ định cả hai.

*Ví dụ: Giả sử bạn muốn viết một method đặt các Integer objects vào một danh sách. Để tối đa hóa tính linh hoạt, bạn muốn method hoạt động trên List<Integer>, List<Number> và List<Object> - bất kỳ thứ gì có thể chứa giá trị Integer. Để đạt mục đích này, bạn cần chỉ định List<? super Integer>. Type List<Integer> hạn chế hơn List<? super Integer> bởi vì cái trước chỉ khớp với danh sách kiểu Integer, trong khi cái sau khớp với danh sách bất kỳ kiểu nào là supertype của Integer:*

```java
public static void addNumbers(List<? super Integer> list) {
    for (int i = 1; i <= 10; i++) {
        list.add(i);
    }
}
```


### 7.4, Wildcards and Subtyping

Có thể sử dụng wildcard để tạo ra mối quan hệ giữa các parameterized types của một generic type.

*Ví dụ: Mặc dù Integer là một subtype của Number, nhưng trên thực tế List<Integer> KHÔNG phải một subtype của List<Number>, chúng không có quan hệ với nhau. Tuy nhiên, List<?> là supertype của cả List<Integer> và List<Number>:*

```java
// List<Integer>   --X-->   List<Number>

List<Integer> intList = new ArrayList<>();
List<Number> numList = lb;   // compile-time error


//             List<?>
//               |
//     ---------------------
//     |                   |
// List<Integer>      List<Number>

List<? extends Integer> intList = new ArrayList<>();
List<? extends Number>  numList = intList;  // OK. 
    // List<? extends Integer> is a subtype of List<? extends Number>
```

*Ví dụ: Sơ đồ phân cấp thể hiện mối quan hệ của các parameterized type của generic type List<E>:*

```java
                        List<?>
                          |
        ------------------------------------
        |                                  |
List<? extends Number> <--   ----> List<? super Integer>
        |                |   |             |
List<? extends Integer>  |   |     List<? super Number>
        |                |   |             |
    List<Integer> -------|----         List<Number>
                         |                 |          
                         -------------------
```


### 7.5, Wildcard Capture and Helper Methods

Trong một số trường hợp, compiler sẽ suy luận type của một wildcard. Điều này được gọi là *wildcard capture*.

*Ví dụ, một danh sách có thể được định nghĩa là List<?>, nhưng khi đánh giá một expression, compiler suy ra một type cụ thể từ mã.*

Phần lớn, bạn không cần phải lo lắng về wildcard capture, ngoại trừ khi bạn thấy thông báo lỗi có chứa cụm từ "capture of".

*Ví dụ: WildcardError tạo ra capture error khi được compile:*

```java
import java.util.List;

public class WildcardError {
    void foo(List<?> i) {
        i.set(0, i.get(0)); // compile-time error
    }
}
```

*Trong ví dụ này, compiler xử lý tham số đầu vào i có kiểu Object. Khi method foo gọi List.set(int, E), compiler không thể xác nhận type của object đang được chèn vào danh sách và lỗi được tạo ra. Khi loại lỗi này xảy ra, nó thường có nghĩa là compiler tin rằng bạn đang gán sai kiểu cho một biến. Generics đã được thêm vào Java vì lý do này - để thực thi type safety tại compile-time.*

*Để có thể khắc phục compile-time-error, có thể sửa nó bằng cách viết một private helper method:*

```java
public class WildcardFixed {

    void foo(List<?> i) {
        fooHelper(i);
    }

    // Helper method được tạo cho việc capture wildcard thông qua type inference.
    private <T> void fooHelper(List<T> l) {
        l.set(0, l.get(0));
    }

}
```


### 7.6, Guidelines for Wildcard Use

Một "in" variable: cung cấp data cho mã. Một "out" variable: giữ data để sử dụng ở nơi khác.

*Ví dụ: copy(src, dest) sẽ có src là "in" parameter vì nó cung cấp data để copy, và dest là "out" parameter vì nó nhận data.*

Có một số variable được sử dụng cho cả mục đích "in" và "out".

Nên sử dụng wildcard khi:  

- Một "in" variable nên được định nghĩa với một upper bounded wildcard.  
- Một "out" variable nên được định nghĩa với một lower bounded wildcard.  
- Trong trường hợp có thể truy cập "in" variable bằng cách sử dụng các methods được định nghĩa trong Object class, nên sử dụng một unbounded wildcard.  
- Trong trường hợp code cần truy cập variable dưới dạng cả "in" và "out" variable, không nên sử dụng wildcard.  

Các nguyên tắc trên KHÔNG áp dụng cho "return type" của một method. Nên tránh sử dụng wildcard làm return type vì nó buộc các programmer mà sử dụng code sẽ phải xử lý wildcard.

*Ví dụ: Vì List<EvenNumber> là một subtype của List<? extends NaturalNumber>, bạn có thể gán le cho ln, nhưng không thể sử dụng ln để add một natural number vào một list của các even numbers.*

```java
class NaturalNumber {
    private int i;
    public NaturalNumber(int i) { this.i = i; }
}

class EvenNumber extends NaturalNumber {
    public EvenNumber(int i) { super(i); }
}

class Test {
    public static void main(String[] args) {
        List<EvenNumber> le = new ArrayList<>();
        List<? extends NaturalNumber> ln = le;
        ln.add(new NaturalNumber(35));  // compile-time error
    }
}
```

Đối với một variable của parameterized type của generic type List với type argument là một wildcard:  

- Có thể add một giá trị null,  
- Có thể goi clear method,  
- Có thể thực hiện lặp và gọi remove method,  
- Có thể capture wildcard và ghi các elements đã đọc từ list này,  
- KHÔNG thể lưu trữ một element mới,  
- KHÔNG thể sửa đổi element tồn tại trong list.  


## 8. Type Erasure

Generics đã được đưa vào Java để cung cấp type checks chặt chẽ hơn tại compile-time và hỗ trợ lập trình generic. Để triển khai generic, Java compiler áp dụng *type erasure* cho:  

- Thay thế tất cả các type parameters trong các generic types bằng các bounds của chúng hoặc Object nếu các type parameters không có bound. Do đó, bytecode được tạo ra chỉ chứa các class, interface và method thông thường.  
- Chèn các type cast nếu cần thiết để giữ type safety.  
- Tạo các bridge methods để bảo toàn tính đa hình trong các generic types được extends.  

Type erasure đảm bảo rằng không có class mới nào được tạo cho các parameterized type.


### 8.1, Erasure of Generic Types

Trong quá trình type erasure, Java compiler sẽ xóa tất cả các type parameters và thay thế từng parameter bằng bound đầu tiên của nó nếu type parameters có các bounds hoặc Object nếu các type parameters không có bound.

*Ví dụ 1: generic class Note<T> sử dụng một unbounded type parameter, đại diện cho một node trong một linked list.*

```java
public class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
}
```

*Trong ví dụ trên, bởi vì type parameter T không có bound, Java compiler thay thế nó bằng Object:*

```java
public class Node {

    private Object data;
    private Node next;

    public Node(Object data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Object getData() { return data; }
}
```

*Ví dụ 2: generic class Node<T extends Comparable<T>> sử dụng một bounded type parameter:*

```java
public class Node<T extends Comparable<T>> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
}
```

*Trong ví dụ trên, Java compiler thay thế bounded type parameter T bằng first bound của nó, Comparable:*

```java
public class Node {

    private Comparable data;
    private Node next;

    public Node(Comparable data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Comparable getData() { return data; }
}
```


### 8.2, Erasure of Generic Methods

Java compiler cũng xóa các type parameters trong generic method.

*Ví dụ 1: Đếm số lần xuất hiện của một giá trị trong một array:*

```java
public static <T> int count(T[] anArray, T elem) {
    int cnt = 0;
    for (T e : anArray)
        if (e.equals(elem))
            ++cnt;
    return cnt;
}
```

*Trong ví dụ trên, bởi vì T không có bound, Java compiler thay thế nó bằng Object:*

```java
public static int count(Object[] anArray, Object elem) {
    int cnt = 0;
    for (Object e : anArray)
        if (e.equals(elem))
            ++cnt;
    return cnt;
}
```

*Ví dụ 2: Vẽ các hình khác nhau:*

```java
class Shape { ... }
class Circle extends Shape { ... }
class Rectangle extends Shape { ... }
```

*Để vẽ các hình khác nhau có thể định nghĩa một generic method:*

```java
public static <T extends Shape> void draw(T shape) { ... }
```

*Java compiler thay thế T bằng Shape:*

```java
public static void draw(Shape shape) { ... }
```


### 8.3, Effects of Type Erasure and Bridge Methods

Đôi khi type erasure gây ra tình huống mà bạn có thể không lường trước được, compiler sẽ tạo ra một *bridge method*, như một phần của quá trình type erasure.

*Ví dụ:*

```java
public class Node<T> {

    public T data;

    public Node(T data) { this.data = data; }

    public void setData(T data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}

public class MyNode extends Node<Integer> {
    public MyNode(Integer data) { super(data); }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}
```

*Xét đoạn code sau:*

```java
MyNode mn = new MyNode(5);
Node n = mn;            // A raw type - compiler throws an unchecked warning
n.setData("Hello");     // Gây nên một ClassCastException được ném ra.
Integer x = mn.data;    
```

*Sau type erasure, đoạn code này trở thành:*

```java
MyNode mn = new MyNode(5);
Node n = mn;  // A raw type - compiler throws an unchecked warning
              // Note: thay vào đó, statement này có thể là:
              // Node n = (Node)mn;
              // Tuy nhiên, compiler không tạo một cast vì nó không cần thiết.
n.setData("Hello"); // Gây nên một ClassCastException được ném ra.
Integer x = (Integer)mn.data; 
```

#### *Bridge Methods*

Khi compile một class hoặc interface mà extends một parameterized class hoặc implements một parameterized interface, compiler cần tạo một bridge method, như là một phần của quá trình type erasure. Bạn không cần lo lắng về các bridge methods, nhưng bạn có thể bối rối nếu chúng xuất hiện trong stack trace.

*Sau type erasure, Node và MyNode classes sẽ trở thành:*

```java
public class Node {

    public Object data;

    public Node(Object data) { this.data = data; }

    public void setData(Object data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}

public class MyNode extends Node {

    public MyNode(Integer data) { super(data); }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}
```

*Sau type erasure, các method signatures không khớp; Node.setData(T) method trở thành Node.setData(Object). Do đó, MyNode.setData(Integer) method không override Node.setData(Object) method.* 

Để giải quyết vấn đề này, và đảm bảo tính đa hình cho generic types sau type erasure, Java compiler tạo một bridge method để đảm bảo subtype hoạt động như mong đợi.

*Đối với MyNode class, compiler tạo bridge method sau cho setData:*

```java
class MyNode extends Node {

    // Bridge method generated by the compiler
    public void setData(Object data) {
        setData((Integer) data);
    }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}
```

*Bridge method MyNode.setData(object) ủy quyền cho MyNode.setData(Integer) method ban đầu. Do đó, 'n.setData("Hello");' statement gọi method MyNode.setData(Object), và một ClassCastException được ném bởi vì không thể ép kiểu "Hello" thành Integer.*


### 8.4, Non-Reifiable Types

Một *reifiable type* là một type có đầy đủ type information tại runtime, là các primitive types, non-generic types, raw types, và các parameterized types với tất cả type arguments là unbound wildcards.

Các *non-reifiable types* là các types có information đã bị xóa tại compile-time bởi type erasure — các parameterized types có ít nhất một trong các type arguments là unbound wildcards. Một non-reifiable type không có sẵn information của nó tại runtime.

*Ví dụ: List<String> và List<Number> là các non-reifiable types; JVM không thể phân biệt sự khác biệt giữa các types này tại runtime.*

there are certain situations where  cannot be used: in an , for example, or as an element in an array.

Có một số trường hợp nhất định không thể sử dụng các non-reifiable types, như trong một *instanceof expression*, hoặc *cast*, hoặc dưới dạng một *element type của một array*.

#### *Heap Pollution*

*Heap pollution* xảy ra khi một variable của parameterized type tham chiếu đến một object không phải của parameterized type đó. Tình huống này xảy ra nếu chương trình thực hiện một số thao tác dẫn đến unchecked warning tại compile-time. Một *unchecked warning* được tạo ra nếu, tại compile-time (trong giới hạn của compile-time type checking rules) hoặc trong runtime, tính đúng đắn của một thao tác liên quan đến parameterized type (*ví dụ: ép kiểu hoặc gọi phương thức*) không thể được xác minh. 

*Ví dụ, Heap pollution xảy ra khi pha trộn các raw type và các parameterized type, hoặc khi thực hiện các unchecked casts.*

Trong các tình huống bình thường, khi tất cả code được compile cùng một lúc, compiler sẽ đưa ra một unchecked warning để thu hút sự chú ý của bạn về heap pollution tiềm ẩn. Nếu bạn compile các phần mã của mình một cách riêng biệt, rất khó để phát hiện nguy cơ Heap pollution. Nếu bạn đảm bảo rằng mã của bạn compile mà không có cảnh báo, thì không có Heap pollution có thể xảy ra.


#### *Potential Vulnerabilities of Varargs Methods with Non-Reifiable Formal Parameters*

Các generic methods mà có các vararg input parameters có thể gây ra heap pollution.

*Ví dụ:*

```java
public class ArrayBuilder {

    public static <T> void addToList (List<T> listArg, T... elements) {
        for (T x : elements) {
            listArg.add(x);
        }
    }

    public static void faultyMethod(List<String>... l) {
        Object[] objectArray = l;     // Valid
        objectArray[0] = Arrays.asList(42);
        String s = l[0].get(0);       // ClassCastException thrown here
    }
}

public class HeapPollutionExample {
    public static void main(String[] args) {

        List<String> stringListA = new ArrayList<String>();
        List<String> stringListB = new ArrayList<String>();

        ArrayBuilder.addToList(stringListA, "Seven", "Eight", "Nine");
        ArrayBuilder.addToList(stringListB, "Ten", "Eleven", "Twelve");
        List<List<String>> listOfStringLists = new ArrayList<List<String>>();
        ArrayBuilder.addToList(listOfStringLists, stringListA, stringListB);

        ArrayBuilder.faultyMethod(Arrays.asList("Hello!"), Arrays.asList("World!"));
    }
}
```

*Khi ví dụ trên được compile, cảnh báo sau được tạo ra bởi definition của ArrayBuilder.addToList method:*

```
warning: [varargs] Possible heap pollution from parameterized vararg type T
```

*Khi compiler gặp một varargs method, nó sẽ dịch varargs formal parameter thành một mảng. Tuy nhiên, Java không cho phép tạo array of parameterized type. Trong ArrayBuilder.addToList method, compiler dịch 'T... elements' formal parameter varargs thành formal parameter 'T[] elements', một array. Tuy nhiên, vì type erasure, compiler chuyển đổi formal parameter varargs thành 'Object[] elements'. Do đó, có khả năng xảy ra heap pollution.*

*Trong ví dụ trên, statement sau đây gán varargs formal parameter l cho Object[] ObjectArgs:*

```java
Object[] objectArray = l;
```

*Statement này có thể gây heap pollution. Một giá trị phù hợp với parameterized type của varargs formal parameter l có thể được gán cho biến objectArray, và do đó có thể được gán cho l. Tuy nhiên, compiler không tạo ra unchecked warning tại câu lệnh này. Compiler đã tạo ra một cảnh báo khi nó dịch formal parameter varargs List<String>... l sang formal parameter List[] l. Statement này là hợp lệ; biến l có kiểu List[], là một subtype của Object[].*

*Do đó, compiler không đưa ra cảnh báo hoặc lỗi nếu bạn gán một List object của bất kỳ type nào cho bất kỳ array component nào của mảng objectArray, như được thể hiên qua câu lệnh sau, gán cho array component đầu tiên của mảng objectArray một List object có chứa một Integer object:*

```java
objectArray[0] = Arrays.asList(42);
```

*Giả sử bạn gọi ArrayBuilder.faultyMethod với câu lệnh sau:*

```java
ArrayBuilder.faultyMethod(Arrays.asList("Hello!"), Arrays.asList("World!"));
```

*Tại runtime, JVM ném một ClassCastException tại statement sau, bởi vì object được lưu trữ trong array component đầu tiên của biến l có type List<Integer>, nhưng câu lệnh này đang mong đợi một object của List<String> type:*

```java
String s = l[0].get(0); // ClassCastException thrown here
```


#### *Prevent Warnings from Varargs Methods with Non-Reifiable Formal Parameters*

Nếu bạn khai báo một varargs method có các parameters của parameterized type và bạn đảm bảo rằng phần thân của method không ném ClassCastException hoặc ngoại lệ tương tự khác do xử lý các varargs formal parameters không đúng, bạn có thể ngăn cảnh báo mà compiler tạo cho các varargs method như vậy bằng cách thêm annotation sau vào các static and non-constructor method declarations:

```java
@SafeVarargs
```

Mặc dù không khuyến khích, nhưng bạn cũng có thể loại bỏ các cảnh báo như vậy bằng cách thêm annotation sau vào method declaration:

```java
@SuppressWarnings({"unchecked", "varargs"})
```
