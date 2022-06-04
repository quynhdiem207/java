# Chapter 15: Expressions

## 1. Expressions and Run-Time Checks

Nếu type của một expression là một primitive type, thì value của expression đó cũng là của primitive type đó.

Nếu type của một expression là một reference type, thì class của object được tham chiếu, hoặc thậm chí liệu value có phải là một reference tới một object thay vì null hay không, không được kiểm tra tại compile time. Điều này có thể gây ra các exception tại runtime.


## 2. Normal and Abrupt Completion of Evaluation

Mọi expression đều có một chế độ bình thường của việc đánh giá, trong đó các bước tính toán nhất định được thực hiện.

Nếu tất cả các bước được thực hiện mà không có exception nào được ném ra, thì expression đó được gọi là hoàn thành bình thường. Tuy nhiên, nếu việc đánh giá một expression ném một exception, thì expression đó được gọi là hoàn thành đột ngột.

Các exceptions có thể được ném bởi các operators như sau:

- Một class instance creation expression, array creation expression, method reference expression, array initializer expression, string concatenation operator expression, hoặc lambda expression sẽ ném một *OutOfMemoryError* nếu không đủ bộ nhớ.  
- Một array creation expression sẽ ném một *NegativeArraySizeException* nếu value của dimension expression < 0.  
- Một array access expression sẽ ném một *NullPointerException* nếu value của array reference expression là null.  
- Một array access expression sẽ ném một *ArrayIndexOutOfBoundsException* nếu value của array index expression là < 0 hoặc >= length của array đó.  
- Một field access expression sẽ ném một *NullPointerException* nếu value của object reference expression là null.  
- Một method invocation expression gọi một instance method sẽ ném một *NullPointerException* nếu target reference là null.  
- Một cast expression sẽ ném một *ClassCastException* nếu type thực tế của object được tham chiếu bởi value của operand expression không tương thích với target type được xác định bởi cast operator.  
- Một integer division hoặc integer remainder operator sẽ ném một *ArithmeticException* nếu value của right-hand operand expression là 0.  
- Một phép gán cho một array component của reference type, một method invocation expression, hoặc một prefix hay postfix increment hoặc decrement operator đều có thể ném một *OutOfMemoryError* do boxing conversion.  
- Một phép gán cho một array component của reference type sẽ ném một *ArrayStoreException* nếu value được gán không tương thích với component type thực tế của array đó.  

Một method invocation expression cũng có thể dẫn đến một exception được ném nếu exception xảy ra khiến việc thực thi của method body hoàn thành đột ngột.

Một class instance creation expression cũng có thể dẫn đến một exception được ném nếu exception xảy ra khiến việc thực thi constructor hoàn thành đột ngột.

Các linkage và virtual machine errors cũng có thể xảy ra trong quá trình đánh giá một expression, những lỗi như vậy rất khó dự đoán và xử lý.

Nếu một exception xảy ra, thì việc đánh giá của một hoặc nhiều expressions có thể kết thúc trước khi tất cả các bước trong chế độ bình thường của việc đánh giá hoàn thành; các expressions như vậy được gọi là hoàn thành đột ngột.

Nếu việc đánh giá của một expression yêu cầu việc đánh giá của một subexpression, thì việc hoàn thành đột ngột của subexpression luôn gây ra việc hoàn thành đột ngột ngay lập tức của expression đó, với cùng lý do, và tất cả các bước tiếp theo trong chế độ đánh giá thông thường đều không được thực hiện.


## 3. Evaluation Order

Java đảm bảo các operands của các operators được đánh giá theo một thứ tự cụ thể như sau:

- Toán hạng phía trái của binary operator luôn được đánh giá trước toán hạng phía phải,  
- Các toán hạng của một operator (ngoại từ &&, ||, ? :) luôn được đánh giá trước khi thực hiện hoạt động,  
- Đánh giá tuân thủ thứ tự ưu tiên của các operators và dấu ngoặc đơn,  
- Các expression trong Argument Lists luôn được đánh giá theo thứ tự từ trái sang phải.  

*Ví dụ 1-1. Left-Hand Operand Is Evaluated First*

```java
class Test1 {
    public static void main(String[] args) {
        int i = 2;
        int j = (i=3) * i;
        System.out.println(j); // 9
    }
}
```

*Ví dụ 1-2. Implicit Left-Hand Operand In Operator Of Compound Assigment*

```java
class Test2 {
    public static void main(String[] args) {
        int a = 9;
        a += (a = 3);  // 12
        System.out.println(a);
        int b = 9;
        b = b + (b = 3);  // 12
        System.out.println(b);
    }
}
```

*Ví dụ 1-3. Abrupt Completion of Evaluation of the Left-Hand Operand*

```java
class Test3 {
    public static void main(String[] args) {
        int j = 1;
        try {
            int i = forgetIt() / (j = 2);
        } catch (Exception e) {
            System.out.println(e); // java.lang.Exception: I'm outta here!
            System.out.println("Now j = " + j); // Now j = 1
        }
    }
    static int forgetIt() throws Exception {
        throw new Exception("I'm outta here!");
    }
}
```

*Ví dụ 2-1. Evaluation of Operands Before Operation*

```java
class Test {
    public static void main(String[] args) {
        int divisor = 0;
        try {
            int i = 1 / (divisor * loseBig());
        } catch (Exception e) {
            System.out.println(e); // java.lang.Exception: Shuffle off to Buffalo!
                                   // NOT java.lang.ArithmeticException: / by zero
        }
    }
    static int loseBig() throws Exception {
        throw new Exception("Shuffle off to Buffalo!");
    }
}
```

*Ví dụ 3-1. Evaluation Respects Parentheses and Precedence*

```java
strictfp class Test {
    public static void main(String[] args) {
        double d = 8e+307;
        System.out.println(4.0 * d * 0.5); // Infinity
        System.out.println(2.0 * d);       // 1.6e+308
    }
}
```

*Ví dụ 4-1. Evaluation Order At Method Invocation*

```java
class Test1 {
    public static void main(String[] args) {
        String s = "going, ";
        print3(s, s, s = "gone");
    }
    static void print3(String a, String b, String c) {
        System.out.println(a + b + c); // going, going, gone
    }
}
```

*Ví dụ 4-2. Abrupt Completion of Argument Expression*

```java
class Test2 {
    static int id;
    public static void main(String[] args) {
        try {
            test(id = 1, oops(), id = 3);
        } catch (Exception e) {
            System.out.println(e + ", id=" + id); // java.lang.Exception: oops, id=1
        }
    }
    static int test(int a, int b, int c) {
        return a + b + c;
    }
    static int oops() throws Exception {
        throw new Exception("oops");
    }
}
```


## 4. Primary Expressions

*Primary expressions* bao gồm các loại expressions đơn giản nhất, để xây dựng các expressions khác: 

- Primary no new array:  
    + Literal  
    + Class Literal  
    + this  
    + TypeName.this  
    + Class instance creation expression  
    + Field access  
    + Array access  
    + Method invocation  
    + Method reference  
    + ( Expression )  
- Array creation expression  


### 4.1, Lexical Literals

Một literal biểu thi một giá trị cố định, không thay đổi.

Chúng được phân loại như sau:

- Integer literals  
    vd: 2 (type int), 4L (type long),  
- Floating-Point literals  
    vd: 3.0f (type float), 5.2 (type double),  
- Boolean literals  
    vd: true, false (type boolean),  
- Character literals,  
    vd: 'A' (type char),  
- String literals  
    vd: "Hello" (type String)  
- null literal  
    vd: null (null reference)  


### 4.2, Class Literals

Một class literal là một tham chiếu tới Class object đại diện cho một type được đặt tên (hoặc type void), như thể được định nghĩa bởi class loader xác định của class của instance hiện tại.

*Ví dụ: int[].class có giá trị là một tham chiếu tới một instance của java.lang.Class đại diện cho class int[]. Hay type của int[].class là `Class<int[]>`.*  

Các class literal bao gồm:

- TypeName {[]}.class  
- numericType {[]}.class  
- boolean {[]}.class  
- void.class  

Type của C.class, trong đó C là tên của một class, interface, hoặc array type, là `Class<C>`.

Type của p.class, trong đó p là tên của một primitive type, là `Class<B>`, trong đó B là type của một expression của type p sau boxing conversion.

Type của void.class là `Class<Void>`.

*Note: private constructor `Class<T>`() nhận một argument là một class loader.*


### 4.3, this

Keyword *this* chỉ có thể được sử dụng trong các contexts sau:

- Trong body của một instance method hoặc default method,  
- Trong body của một constructor của một class,  
- Trong một instance initializer của một class, 
- Trong initializer của một instance variable của một class,  
- Để biểu thị một receiver parameter.  

Khi được sử dụng như một primary expression, keyword this biểu thị một tham chiếu tới curent object mà có instance method hoặc default method đang được gọi, hoặc có field đang được truy cập, hoặc tham chiếu đến object đang được tạo.

Các trường hợp phổ biến sử dụng this là:  

- Truy cập các fields bị che bóng (shadow) bởi các parameters hoặc local variables.  
- Từ trong một constructor gọi một constructor khác trong cùng class.  

Type của this là class hoặc interface type T mà keyword this xuất hiện bên trong.

Tại runtime, class của object thực được tham chiếu có thể là T, nếu T là một class type, hoặc một class mà là một subtype của T.

*Ví dụ 1: The this Expression*

```java
class IntVector {
    int[] v;
    boolean equals(IntVector other) {
        if (this == other)
            return true;
        if (v.length != other.v.length)
            return false;
        for (int i = 0; i < v.length; i++) {
            if (v[i] != other.v[i]) return false;
        }
        return true;
    }
}
```


### 4.4, Qualified this

Bất kỳ *enclosing instance* nào đều có thể được tham chiếu bằng cách xác định tường minh keyword this.

Gọi T là type được biểu thị bởi TypeName. Gọi n là một số nguyên sao cho T là enclosing type declaration cấp thứ n của class hoặc interface mà trong đó qualified this expression xuất hiện.

Giá trị của một expression có dạng 'TypeName.this' là *enclosing instance* cấp thứ n của this. Và, type của 'TypeName.this' là TypeName.

Nếu 'TypeName.this' xảy ra trong một class hoặc interface KHÔNG phải một *inner class* của class T hoặc *bản thân* T, sẽ gây ra compile-time error.

```java
class Outer {
    class Inner {
        // T.class occurs within a inner class of T
        Inner(Outer Outer.this) {
            System.out.println(Outer.this.getClass()); // class Outer
        }

        // T.class occurs within a inner class of T
        class InnerMember {
            void test(InnerMember this) {
                System.out.println(Outer.Inner.this.getClass()); // class Outer$Inner
            }
        }
    }

    static void classMethod() {
        // The class Local occurs in a static context, it have no enclosing instance
        class Local {
            Local(Outer Outer.this) {} // Compile-time error
        }
    }

    void instanceMethod() {
        // T.class occurs within T itself
        System.out.println(Outer.this.getClass() + " - " + this.getClass()); // class Outer - class Outer
        
        // T.class occurs within a inner class of T
        class Local {
            Local(Outer Outer.this) {
                System.out.println(Outer.this.getClass()); // class Outer
            }
        }
        new Local();
    }

    public static void main(String[] args) {
        Outer outer = new Outer();
        outer.new Inner(). new InnerMember().test();
        outer.instanceMethod();
    }
}
```


## 5. Class Instance Creation Expressions

Một *class instance creation expression* được sử dụng để tạo các objects mới là các instances của các classes.

```
- ClassInstanceCreationExpression:
    UnqualifiedClassInstanceCreationExpression
    ExpressionName . UnqualifiedClassInstanceCreationExpression
    Primary . UnqualifiedClassInstanceCreationExpression
- UnqualifiedClassInstanceCreationExpression:
    new [TypeArguments] ClassOrInterfaceTypeToInstantiate ( [ArgumentList] ) [ClassBody]
- ClassOrInterfaceTypeToInstantiate:
    {Annotation} Identifier {. {Annotation} Identifier} [TypeArgumentsOrDiamond]
- TypeArgumentsOrDiamond:
    TypeArguments
    <>
```

Một class instance creation expression xác định một class sẽ được khởi tạo, có thể theo sau là các type arguments hoặc một diamond (<>) nếu class được khởi tạo là generic, theo sau bởi list các arguments giá trị thực (có thể là empty) cho constructor.

Nếu type argument list của class là empty — dạng diamond <> — thì type arguments của class được suy luận.

Nếu constructor là generic, thì các type arguments của constructor có thể được suy luận hặc được truyền tường minh. Nếu được truyền tường minh thì các type arguments của constructor được đặt ngay keyword new.

Nếu một class instance creation expression cung cấp các type arguments cho một constructor, nhưng lại sử dụng dạng diamond cho các type arguments của class, sẽ gây ra compile-time error.

Nếu các TypeArguments xuất hiện ngay sau new keyword, hoặc ngay trước (, thì nếu bất kỳ type arguments nào là wildcard sẽ gây ra compile-time error.

Class instance creation expressions có 2 dạng:

- *Unqualified class instance creation expressions* bắt đầu với keyword new.  

    Một unqualified class instance creation expression có thể được sử dụng để tạo một instance của một class, bất kể class đó là một top level, member, local, hay anonymous class.  

- *Qualified class instance creation expressions* bắt đầu với một Primary expression hoặc một ExpressionName.  

    Một qualified class instance creation expression cho phép tạo các instances của các inner member classes và các anonymous subclasses của chúng.  

Các *class instance creation expressions* có thể tùy chọn kết thúc với một class body để khai báo một *anonymous class* và tạo một instance của nó.

Khởi tạo class liên quan đến việc xác định class được khởi tạo, các enclosing instances (nếu có) của instance mới được tạo, và constructor được gọi để tạo instance mới.  


### 5.1, Run-Time Evaluation of Class Instance Creation Expressions

Tại runtime, việc đánh giá của một class instance creation expression như sau:

- Đầu tiên, nếu class instance creation expression là một qualified class instance creation expression, thì primary expression được đánh giá. Nếu kết quả đánh giá là null, thì một *NullPointerException* được ném ra, và class instance creation expression hoàn thành đột ngột. Nếu việc đánh giá primary expression hoàn thành đột ngột, thì class instance creation expression hoàn thành đột ngột vì cùng lý do.  
- Tiếp theo, không gian được cấp cho class instance mới. Nếu không đủ không gian để cấp phát object, việc đánh giá của class instance creation expression hoàn thành đột ngột vì ném một *OutOfMemoryError*.  
- object mới chứa các instances mới của tất cả các fields được khai báo trong class type xác định và tất cả các superclasses của nó. Khi mỗi field instance mới được tạo, nó được khởi tạo thành default value của nó.  
- Tiếp theo, các arguments thực của constructor được đánh giá, từ trái sang phải. Nếu đánh giá bất kỳ argument nào hoàn thành đột ngột, thì class instance creation expression hoàn thành đột ngột ngay lập tức vì cùng lý do.  
- Tiếp theo, constructor được chọn của class type xác định được gọi. Điều này dẫn đến constructor của mỗi superclass của class type đó được gọi.  
- Giá trị của một class instance creation expression là một tham chiếu tới object vừa được tạo của class xác định. Mỗi lần expression được đánh giá, một object mới sẽ được tạo.

*Ví dụ 1. Evaluation Order and Out-Of-Memory Detection*

```java
class List {
    int value;
    List next;
    static List head = new List(0);
    List(int n) { value = n; next = head; head = this; }
}
class Test {
    public static void main(String[] args) {
        int id = 0, oldid = 0;
        try {
            for (;;) {
                ++id;
                new List(oldid = id);
            }
        } catch (Error e) {
            List.head = null;
            System.out.println(e.getClass() + ", " + (oldid == id));
        }
    }
}
// Output: class java.lang.OutOfMemoryError, false
// vì điều kiện out-of-memory được phát hiện trước khi argument expression oldid = id được đánh giá.
```


## 6. Array Creation and Access Expressions

### 6.1, Array Creation Expressions

Một *array creation expression* được sử dụng để tạo các arrays mới có các elements của một type xác định.

```
- ArrayCreationExpression:  
    new PrimitiveType DimExprs [Dims]
    new ClassOrInterfaceType DimExprs [Dims]
    new PrimitiveType Dims ArrayInitializer
    new ClassOrInterfaceType Dims ArrayInitializer
- DimExprs: DimExpr {DimExpr}
- DimExpr:  {Annotation} [ Expression ]
- Dims:     {Annotation} [ ] {{Annotation} [ ]}
```

Element type trong một array creation expression không thể là một parameterized type, trừ khi tất cả các type arguments cho parameterized type đều là unbounded wildcards.

Type của dimension expression phải là int, hoặc cũng có thể là byte, short, char vì chúng sẽ được mở rộng thành int.


### 6.2, Run-Time Evaluation of Array Creation Expressions

Tại runtime, việc đánh giá của array creation expression hoạt động như sau:

- Nếu không có *dimension expressions*, thì phải có một *array initializer*. Một array mới được cấp phát sẽ được khởi tạo với các giá trị được cung cấp bởi array initializer. Giá trị của array initializer trở thành giá trị của array creation expression.  

    Một *OutOfMemoryError* có thể xảy ra khi một object của reference type được cấp phát trong khi đánh giá một variable initializer expression, hoặc khi không gian được cấp phát cho một array để giữ các giá trị của một array initializer (có thể lồng nhau).  

- Nếu không, sẽ không có *array initializer*, và:  
    - Đầu tiên, dimension expressions được đánh giá, từ trái sang phải. Nếu bất kỳ đánh giá expression nào hoàn thành đột ngột, thì các expressions bên phải nó sẽ không được đánh giá.  
    - Tiếp theo, giá trị của các dimension expressions được kiểm tra. Nếu bất kỳ expression nào có value < 0, thì một *NegativeArraySizeException* được ném ra.  
    - Tiếp theo, không gian được phân bổ cho array mới. Nếu không có đủ không gian để phân bổ array, thì việc đánh giá array creation expression hoàn thành đột ngột bằng cách ném một *OutOfMemoryError*.  
    - Sau đó:  
        + Nếu array creation expression chỉ định tạo mảng 1 chiều, thì một mảng 1 chiều sẽ được tạo với length xác định, và mỗi componentcủa array sẽ được khởi tạo thành default value của nó.  
        + Nếu array creation expression chỉ định tạo mảng n chiều, thì việc tạo mảng sẽ thực thi một tập các vòng lặp lồng nhau với độ sâu n-1 để tạo các arrays được chứa bên trong các arrays (Note: Mảng đa chiều không cần chứa các array có cùng length ở mỗi cấp).  

*Ví dụ 1. Array Creation Evaluation*

```java
class Test1 {
    public static void main(String[] args) {
        int i = 4;
        int ia[][] = new int[i][i=3];
        System.out.println("[" + ia.length + "," + ia[0].length + "]"); // [4,3]
            // because the first dimension is calculated as 4,
            // before the second dimension expression sets i to 3.
    }
}

class Test2 {
    public static void main(String[] args) {
        int[][] a = { { 00, 01 }, { 10, 11 } };
        int i = 99;
        try {
            a[val()][i = 1]++;
        } catch (Exception e) {
            System.out.println(e + ", i=" + i);
        }
    }
    static int val() throws Exception {
        throw new Exception("unimplemented");
    }
}

// Output: java.lang.Exception: unimplemented, i=99
// because the embedded assignment that sets i to 1 is never executed.
```

*Ví dụ 2. Multi-Dimensional Array Creation*

```java
float[][] matrix = new float[3][3];

// tương đương:
float[][] matrix = new float[3][];
for (int d = 0; d < matrix.length; d++)
    matrix[d] = new float[3];
```

*Ví dụ 3. OutOfMemoryError and Dimension Expression Evaluation*

```java
class Test3 {
    public static void main(String[] args) {
        int len = 0, oldlen = 0;
        Object[] a = new Object[0];
        try {
            for (;;) {
                ++len;
                Object[] temp = new Object[oldlen = len];
                temp[0] = a;
                a = temp;
            }
        } catch (Error e) {
            System.out.println(e + ", " + (oldlen == len));
        }
    }
}
// Output: java.lang.OutOfMemoryError, true
// because the out-of-memory condition is detected,
// after the dimension expression oldlen = len is evaluated.
```


### 6.3, Array Access Expressions

Một *array access expression* tham chiếu đến một variable là một component của một array.

```
- ArrayAccess:
    ExpressionName [ Expression ]
    PrimaryNoNewArray [ Expression ]
```

Một *array access expression* chứa 2 subexpressions, là *array reference expression* và *index expression*.

Type của array reference expression phải là một array type.

Type của index expression phải là int, hoặc byte, short, char vì chúng sẽ được mở rộng thành int.


### 6.4, Run-Time Evaluation of Array Access Expressions

Tại runtime, việc đánh giá một array access expression hoạt động như sau:

- Đầu tiên, array reference expression được đánh giá. Nếu việc đánh giá hoàn thành đột ngột, thì array access expression hoàn thành đột ngột vì cùng lý do và index expression không được đánh giá.  
- Nếu không, index expression được đánh giá. Nếu việc đánh giá hoàn thành đột ngột, thì array access expression hoàn thành đột ngột vì cùng lý do.  
- Nếu không, nếu giá trị của array reference expression là null, thì một *NullPointerException* được ném.  
- Nếu không, giá trị của array reference expression thực sự tham chiếu đến một array. Nếu giá trị của index expression < 0, hoặc >= length của array, thì một *ArrayIndexOutOfBoundsException* được ném.  
- Nếu không, kết quả của array access expression là variable của type T, bên trong array đó, được chọn bởi giá trị của index expression.  

*Ví dụ 1. Array Reference Is Evaluated First*

```java
class Test1 {
    public static void main(String[] args) {
        int[] a = { 11, 12, 13, 14 };
        int[] b = { 0, 1, 2, 3 };
        System.out.println(a[(a=b)[3]]); // 14
            // because the expression's value is equivalent to a[b[3]] or a[3] or 14.
    }
}
```

*Ví dụ 2. Abrupt Completion of Array Reference Evaluation*

```java
class Test2 {
    public static void main(String[] args) {
        int index = 1;
        try {
            skedaddle()[index=2]++;
        } catch (Exception e) {
            System.out.println(e + ", index=" + index);
        }
    }
    static int[] skedaddle() throws Exception {
        throw new Exception("Ciao");
    }
}
// Output: java.lang.Exception: Ciao, index=1
// because the embedded assignment of 2 to index never occurs.
```

*Ví dụ 3. null Array Reference*

```java
class Test3 {
    public static void main(String[] args) {
        int index = 1;
        try {
            nada()[index=2]++;
        } catch (Exception e) {
            System.out.println(e + ", index=" + index);
        }
    }
    static int[] nada() { return null; }
}
// Output: java.lang.NullPointerException, index=2
// because the embedded assignment of 2 to index,
// occurs before the check for a null array reference expression.

class Test4 {
    public static void main(String[] args) {
        int[] a = null;
        try {
            int i = a[vamoose()];
            System.out.println(i);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    static int vamoose() throws Exception {
        throw new Exception("Twenty-three skidoo!");
    }
}
// Output: java.lang.Exception: Twenty-three skidoo!
// A NullPointerException never occurs,
// because the index expression must be completely evaluated,
// before any further part of the array access occurs,
// and that includes the check as to whether the value of the array reference expression is null.
```