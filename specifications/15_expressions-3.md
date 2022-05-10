# Chapter 15. Expressions


## 9. Lambda Expressions

một *lambda expression* giống với một method: nó cung cấp một list các formal parameters và một body (là một expression hoặc một block).

```
- LambdaExpression:
    LambdaParameters -> LambdaBody
```

Một lambda expression phải xảy ra trong một assignment context, một invocation context, hoặc một casting context.  

Việc đánh giá một *lambda expression* sẽ tạo ra một *instance* của một *functional interface type*. Đánh giá một method reference expression không gây ra việc thực thi body của expression; thay vào đó, việc thực thi sẽ xảy ra khi method của functional interface được gọi sau đó.

Types của các formal parameters của một lambda expression có thể được khai báo tường minh hoặc suy luận. Tất cả các formal parameters đều phải được khai báo theo một loại style thống nhất. Và các formal parameters với type được suy luận không thể có modifiers.

```java
() -> {}                // No parameters; result is void
() -> 42                // No parameters, expression body
() -> null              // No parameters, expression body
() -> { return 42; }    // No parameters, block body with return
() -> { System.gc(); }  // No parameters, void block body

() -> {                 // Complex block body with returns
    if (true) return 12;
    else {
        int result = 15;
        for (int i = 1; i < 10; i++)
        result *= i;
        return result;
    }
}                          

(int x) -> x + 1           // Single declared-type parameter
(int x) -> { return x+1; } // Single declared-type parameter
(x) -> x + 1               // Single inferred-type parameter
x -> x + 1                 // Parentheses optional for single inferred-type parameter

(String s) -> s.length()      // Single declared-type parameter
(Thread t) -> { t.start(); }  // Single declared-type parameter
s -> s.length()               // Single inferred-type parameter
t -> { t.start(); }           // Single inferred-type parameter

(int x, int y) -> x + y  // Multiple declared-type parameters
(x, y) -> x + y          // Multiple inferred-type parameters
(x, int y) -> x + y      // Illegal: can't mix inferred and declared types
(x, final y) -> x + y    // Illegal: no modifiers with inferred types
```

*Ví dụ: Sử dụng lambda expression:*

```java
interface Predicate<T> {
    boolean test(T t);
}

public class Person {
    public enum Sex { MALE, FEMALE; }
    String name;
    LocalDate birthday;
    Sex gender;
    public int getAge() {}
    public void printPerson() {}
    public static void printPersons(List<Person> list, Predicate<Person> tester) {
        for (Person p: list) {
            if (tester.test(p)) 
                p.printPerson();
        }
    }
    public static void main(String[] args) {
        List<Person> list = new ArrayList<Person>();
        printPersons(
            list, 
            p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25
        );
    }
}
```

### 9.1, Accessing Local Variables of the Enclosing Scope

Giống như local và anonymous class, bất kỳ local variable, formal parameter, hoặc exception parameter được sử dụng nhưng không được khai báo trong một lambda expression phải được khai báo là final hoặc là effectively final, nếu không sẽ gây ra compile-time error khi cố sử dụng.

Bất kỳ local variable được sử dụng nhưng không được khai báo trong một lambda body chắc chắn phải được gán trước lambda body, nếu không sẽ gây ra compile-time error.

Một lambda expression không tạo ra scope của riêng nó, vì vậy không tồn tại vấn đề shadowing.

```java
void m1(int x) {
    int y = 1;
    foo(() -> x + y); // Legal: x and y are both effectively final.
}

void m2(int x) {
    int y;
    y = 1;
    foo(() -> x + y); // Legal: x and y are both effectively final.
}

void m3(int x) {
    int y;
    if (...) y = 1;
    foo(() -> x + y); // Illegal: y is effectively final, but not definitely assigned.
}

void m4(int x) {
    int y;
    if (...) y = 1; else y = 2;
    foo(() -> x + y); // Legal: x and y are both effectively final.
}


void m5(int x) {
    int y;
    if (...) y = 1;
    y = 2;
    foo(() -> x + y); // Illegal: y is not effectively final.
}

void m6(int x) {
    foo(() -> x + 1);
    x++; // Illegal: x is not effectively final.
}

void m7(int x) {
    foo(() -> x = 1); // Illegal: x is not effectively final.
}

void m8() {
    int y;
    foo(() -> y = 1); // Illegal: y is not definitely assigned before the lambda.
}

void m9(String[] arr) {
    for (String s : arr) {
        foo(() -> s);
        // Legal: s is effectively final (it is a new variable on each iteration)
    }
}

void m10(String[] arr) {
    for (int i = 0; i < arr.length; i++) {
        foo(() -> arr[i]);
        // Illegal: i is not effectively final (it is not final, and is incremented)
    }
}
```

### 9.2, Target Typing

Để xác định type của một lambda expression, Java compiler sử dụng target type của context hoặc tình huống mà lambda expression được tìm thấy.


### 9.3, Target Types and Method Arguments

Đối với các method arguments, Java compiler xác định target type với 2 tính năng: overload resolution và type argument inference (suy luận type arguments).

```java
// java.lang.Runnable
public interface Runnable {
    void run();
}

// java.util.concurrent.Callable<V>
public interface Callable<V> {
    V call();
}

public class Test {
    void invoke(Runnable r) {
        r.run();
    }

    <T> T invoke(Callable<T> c) {
        return c.call();
    }

    public static void main() {
        Test test = new Test();
        String s = test.invoke(() -> "done");
            // method được gọi sẽ là invoke(Callable<T>) vì method đó returns một value;
            // không phải method invoke(Runnable).
            // trong trường hợp này, type của lambda expression () -> "done" là Callable<T>.
    }
}
```


## 10. Method Reference Expressions

Lambda expression được sử dụng để tạo các anonymous methods. Tuy nhiên, đôi khi, một lambda expression không làm gì khác ngoài việc gọi một method hiện có. Trong những trường hợp đó, việc tham chiếu đến method hiện có theo tên thường rõ ràng hơn. *Method reference expression* cho phép bạn làm điều này, ví dụ:  

```java
Comparator.comparing((card) -> card.getRank());  
Comparator.comparing(Card::getRank);
```

Một *method reference expression* được sử dụng để tham chiếu đến invocation của một method mà không thực sự thực thi invocation. 

Một số hình thức của method reference expression cũng cho phép tạo class instance hoặc tạo array, được coi như thể một method invocation.  

Có 4 loại method reference expression:  

- Tham chiếu tới một static method.  
- Tham chiếu tới một instance method của một object cụ thể.  
- Tham chiếu tới một instance method của một object tùy ý của một type cụ thể.  
- Tham chiếu tới một constructor.  

Cấu trúc của một method reference expression có dạng: 

```
- MethodReference:
    ExpressionName :: [TypeArguments] Identifier
    ReferenceType :: [TypeArguments] Identifier
    Primary :: [TypeArguments] Identifier
    super :: [TypeArguments] Identifier
    TypeName . super :: [TypeArguments] Identifier
    ClassType :: [TypeArguments] new
    ArrayType :: new
```

Target reference của một instance method có thể được cung cấp bởi method reference expression sử dụng một ExpressionName, một Primary, hoặc super, hoặc nó có thể được cung cấp sau khi method được gọi. Immediately enclosing instance của một inner class instance mới được cung cấp bởi một enclosing instance của this.

Khi nhiều hơn một member method của một type có cùng tên, hoặc khi một class có nhiều hơn một constructor, thì method hoặc constructor thích hợp được chọn dựa trên functional interface type được nhắm tới bởi expression.

Một method reference expression phải xảy ra trong một assignment context, một invocation context, hoặc một casting context.  

Việc đánh giá một *method reference expression* sẽ tạo ra một *instance* của một *functional interface type*. Đánh giá một method reference expression không gây ra việc thực thi method tương ứng; thay vào đó, việc thực thi sẽ xảy ra khi method của functional interface được gọi sau đó.

*Ví dụ 1: Some method reference expressions, first with no target reference and then with a target reference:*

```java
// 1. Some method reference expressions with no target reference:
String::length             // instance method
System::currentTimeMillis  // static method
List<String>::size  // explicit type arguments for generic type
List::size          // inferred type arguments for generic type
int[]::clone
T::tvarMember

// 2. Some method reference expressions with a target reference
System.out::println
"abc"::length
foo[x]::bar
(test ? list.replaceAll(String::trim) : list) :: iterator
super::toString
```

*Ví dụ 2:*

```java
String::valueOf       // overload resolution needed     
Arrays::sort          // type arguments inferred from context
Arrays::<String>sort  // explicit type arguments
```

*Ví dụ 3: Some method reference expressions that represent a deferred creation of an object or an array:*

```java
ArrayList<String>::new     // constructor for parameterized type
ArrayList::new             // inferred type arguments for generic class
Foo::<Integer>new          // explicit type arguments for generic constructor
Bar<String>::<Integer>new  // generic class, generic constructor
Outer.Inner::new           // inner class constructor
int[]::new                 // array creation
```

*Note: Không thể xác định một signature cụ thể ăn khớp, ví dụ Arrays::sort(int[]). Thay vào đó, functional interface cung cấp các argument types được sử dụng làm input cho việc giải quyết overload. Điều này đáp ứng phần lớn các trường hợp cần sử dụng, khi muốn điều khiển chính xác hơn có thể sử dụng một lambda expression.*

*Ví dụ: 4: Không thể xác định method sẽ được gọi là instance method hay static method:*

```java
interface Fun<T,R> { R apply(T arg); }

// CASE 1:
class C {
    int size() { return 0; }
    static int size(Object arg) { return 0; }

    void test() {
        Fun<C, Integer> f1 = C::size;
            // Error: instance method size() or static method size(Object)?
    }
}

// CASE 2:
class C {
    int size() { return 0; }
    int size(Object arg) { return 0; }
    int size(C arg) { return 0; }

    void test() {
        Fun<C, Integer> f1 = C::size;
            // OK: reference is to instance method size()
    }
}
```

*Ví dụ 5: Sử dụng method reference expression để tạo một list:*

```java
interface ListFactory {
    <T> List<T> make();
}

ListFactory lf  = ArrayList::new;
List<String> ls = lf.make();
List<Number> ln = lf.make();
```

*Ví dụ 6: method reference expression cung cấp cơ chế chuyển đổi giữa các functional interface:*

```java
Task t = () -> System.out.println("hi");
Runnable r = t::invoke;
```


## 11. Constant Expressions

Một *constant expression* là expression mà có giá trị có thể được xác định tại thời điểm compile, nó biểu thị một giá trị của *primitive type* hoặc String type.

```java
true
(short)(1*2*3*4*5*6)
Integer.MAX_VALUE / 2
2.0 * Math.PI
"The integer " + Long.MAX_VALUE + " is mighty big."
```


## 12. Postfix Expressions

```
Expression ++
Expression --
```


## 13. Unary Operators

```
 + Expression
 - Expression
 ! Expression
 ~ Expression
++ Expression
-- Expression
(Type) Expression
```


## 14. Cast Expressions

```
- CastExpression:
    ( PrimitiveType ) Expression
    ( ReferenceType {AdditionalBound} ) Expression
    ( ReferenceType {AdditionalBound} ) LambdaExpression

- AdditionalBound:
    & InterfaceType
```

*Note: ép kiểu nguyên thủy có thể gây nên việc mất một số bit dữ liệu làm mất sự chính xác.*


## 15. Multiplicative Operators

```
Expression1 * Expression2
Expression1 / Expression2
Expression1 % Expression2
```


## 16. Additive Operators

```
Expression1 + Expression2
Expression1 - Expression2
```


## 17. Shift Operators

```
Expression1 << Expression2
Expression1 >> Expression2
Expression1 >>> Expression2
```


## 18. Relational Operators

```
Expression1 < Expression2
Expression1 > Expression2
Expression1 <= Expression2
Expression1 >= Expression2
RelationalExpression instanceof ReferenceType
```

Type của RelationalExpression operand của *instanceof* operator phải là một reference type hoặc null type; nếu không sẽ gây ra compile-time error.

Nếu ép kiểu RelationalExpression sang ReferenceType gây ra compile-time error, thì instanceof relational expression cũng sẽ gây ra compile-time error.

Tại runtime, kết quả của instanceof operator là true nếu giá trị của RelationalExpression không phải null và reference có thể được ép kiểu thành ReferenceType mà không ném một ClassCastException. Nếu không, kết quả sẽ là false.

*Ví dụ 1. The instanceof Operator*

```java
class Point   { int x, y; }
class Element { int atomicNumber; }
class Test {
    public static void main(String[] args) {
        Point   p = new Point();
        Element e = new Element();
        if (e instanceof Point) {  // compile-time error
            System.out.println("I get your point!");
            p = (Point)e;  // compile-time error
        }
    }
}
```


## 19. Equality Operators

```
Expression == Expression2
Expression != Expression2
```

**Note**: Không thể sử dụng toán tử == so sánh bằng giá trị 2 số thực có type double, vì cách Java lưu trữ giá trị double. Để kiểm tra 2 giá trị double có bằng nhau hay không, cần kiểm tra giá trị tuyệt đối của hiệu 2 số có quá nhỏ hay không (Math.abs(x - y) <= 1e-9):  

```java
double x = 0.3 * 3 + 0.1;
double y = 1.0;
System.out.println(x == y);          // false
System.out.println(x + " - " + y);   // 0.9999999999999999 - 1

System.out.println(Math.abs(x - y) <= 1e-9); // true (x == y)
```



## 20. Bitwise and Logical Operators

```
Expression1 & Expression2
Expression1 ^ Expression2
Expression1 | Expression2a
```


## 21. Conditional Operator

```
Expression1 && Expression2
Expression1 || Expression2
Expression1 ? Expression2 : Expression3
Expression1 ? Expression2 : LambdaExpression
```


## 22. Assignment Operators

```
- Assignment:
    LeftHandSide AssignmentOperator Expression2

- LeftHandSide:
    ExpressionName
    FieldAccess
    ArrayAccess

- AssignmentOperator:
    =  *=  /=  %=  +=  -=  <<=  >>=  >>>=  &=  ^=  |=
```