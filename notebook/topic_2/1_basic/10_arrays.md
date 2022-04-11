# Chapter 10: Arrays

Trong Java, *arrays* là các *objects*, được tạo động, và có thể được gán cho các variables của type *Object*. Tất cả các methods của class Object đều có thể được gọi trên một array.

Một array object chứa một số *variables*. Số lượng variables có thể là 0, trong trường hợp này array được gọi là *empty*. Các variables được chứa trong một array không có tên, thay vào đó chúng được tham chiếu bởi các *array access expressions* sử dụng các giá trị *index* (non-negative integer values). Những variables này được gọi là các *components* của array. Nếu một array có n components, thì n là *length* của array; Các components của array được tham chiếu sử dụng các chỉ số nguyên từ 0 đến n-1.

Tất cả các components của một array đều có cùng type, được gọi là *component type* của array. Nếu component type của một array là T, thì type của array đó được viết là T[].

Component type của một array có thể là một array type. Các components của một array như vậy có thể có thể chứa các references tới các subarrays.

Có một số trường hợp trong đó một element của một array có thể là một array: Nếu element type là *Object* hoặc *Cloneable* hoặc *java.io.Serializable*, thì một số hoặc tất cả các elements có thể là các arrays, vì bất kỳ array object nào đều có thể được gán cho bất kỳ variable nào của những types này.


## 1. Array Types

Các *array types* được sử dụng trong các *declarations* và trong các *cast expressions*.

Một array type được viết dưới dạng tên của element type, theo sau là một số cặp ngặc vuông trống []. Số lượng các cặp ngoặc [] chỉ ra độ sâu của lồng ghép mảng.

Mỗi cặp ngặc [] của array type có thể được chú thích bởi các type annotations. Một annotation áp dụng cho cặp ngoặc [] (hoặc dấu chấm lửng ..., trong một variable arity parameter declaration) phía sau nó.

Element type của một array có thể là bất kỳ type nào, *primitive* hoặc *reference*. Đặc biệt:

- Element type của arrays có thể là một *interface type*.  

    Một element của một array như vậy có thể có giá trị là một null reference hoặc một reference tới instance của bất kỳ type nào implements interface đó.  

- Element type của arrays có thể là *abstract class type*.  

    Một element của một array như vậy có thể có giá trị là một null reference hoặc một reference tới instance của bất kỳ subclass nào của abstract class đó mà bản thân nó không phải abstract.  


Các quy tắc sau định nghĩa quan hệ *direct supertype* giữa các *array types*:  

- Nếu S và T đều là reference types, thì S[] >1 T[] chỉ khi S >1 T.  
- Object >1 Object[]  
- Cloneable >1 Object[]  
- java.io.Serializable >1 Object[]  
- Nếu P là primitive type, thì:  
    + Object >1 P[]  
    + Cloneable >1 P[]  
    + java.io.Serializable >1 P[]  

**Note**: Quan hệ *supertype* cho các *array types* không giống với quan hệ *superclass*. VD: Direct supertype của Integer[] là Number[], nhưng direct superclass của Integer[] là Object. Trong thực tế điều này không quan trọng, vì Object cũng là một supertype của tất cả các array types.

```java
public class Test {
    public static void main(String[] args) {
        Integer[] x = {1, 2};
        Number[] y = x;              // OK

        Number[] a = {1, 2};
        Integer[] b = (Integer[]) a; // RUNTIME ERROR

        int[] c = {1, 2};
        long[] d = (long[]) c;       // COMPILE-TIME ERROR

        Long[] z = x;    // COMPILE-TIME ERROR
        int[] w = x;     // COMPILE-TIME ERROR
        Integer[] t = c; // COMPILE-TIME ERROR
    }
}
```


## 2. Array Variables

Một *variable* của *array type* giữ một reference tới một object. Khai báo một variable của array type không tạo ra một array object hay cấp phát bất kỳ không gian nào cho các array components. Nó chỉ tạo ra bản thân variable đó, có thể chứa một reference tới một array. Tuy nhiên, phần *initializer* của một declarator có thể tạo ra một array, một reference tới array đó sẽ trở thành initial value của variable đó.

*Ví dụ: 1. Declarations of Array Variables*

```java
// 1. The declarations do not create array objects:
int[]     ai;        // array of int
short[][] as;        // array of array of short
short     s,         // scalar short
          aas[][];   // array of array of short
Object[]  ao,        // array of Object
          otherAo;   // array of Object
Collection<?>[] ca;  // array of Collection of unknown type

// 2. The declarations of array variables that do create array objects:
Exception ae[]  = new Exception[3];
Object aao[][]  = new Exception[2][3];
float af[]      = new float[] { 2f, 3f };
int[] factorial = { 1, 1, 2, 6, 24, 120, 720, 5040 };
char ac[]       = { 'n', 'o', 't', ' ', 'a', ' ', 'S', 't', 'r', 'i', 'n', 'g' };
String[] aas    = { "array", "of", "String", };
```

*Ví dụ 2. Array Variables and Array Types*

```java
byte[] rowvector, colvector, matrix[];
// tương đương với:
byte rowvector[], colvector[], matrix[][];

int a, b[], c[][];
// tương đương với:
int a;
int[] b;
int[][] c;

float[][] f[][], g[][][], h[];  
// tương đương với:
float[][][][] f;
float[][][][][] g;
float[][][] h;

// Các parameter declarations có cùng array type:
void m(int @A [] @B []  x) {}
void n(int @A [] @B ... y) {}

// Các field declarations có cùng array type:
int @A [] f @B [];
int @B [] @A [] g;
```

Khi một array object được tạo, độ dài (length) của nó không bao giờ thay đổi.

Nếu một array variable v có type A[], trong đó A là một *reference type*, thì v có thể giữ một reference tới một instance của bất kỳ array type B[] nào, nếu B có thể được gán cho A. Nếu type của giá trị được gán không tương thích với component type, một *ArrayStoreException* sẽ được ném ra.

```java
Number[] nums = new Integer[] {2, 3};
```


## 3. Array Creation

Một array được tạo bởi một *array creation expression* hoặc một *array initializer*.

Một *array creation expression* xác định *element type*, số cấp của mảng lồng nhau (dimensions), và độ dài (length) của array cho ít nhất một trong các cấp lồng nhau. Độ dài của array có sẵn dưới dạng một *final instance variable*: **length**.  

Một *array initializer* tạo ra một array và cung cấp các initial values cho tất cả các components của nó.

```java
// array creation expression
int[][] x = new int[2][];
x[0] = new int[1];
x[1] = new int[] { 5, 2 };

// array initializer
int[] y = { 3, 5 }; 
int[][] z = { y };
int[][] w = { {1, 2}, null };
```


## 4. Array Access

Một *component* của một array được truy cập bởi một *array access expression* bao gồm một expression có value là một array reference theo sau bởi một *indexing expression* được bao bởi cặp ngoặc [], như A[i].  

Tất cả các arrays đều có index gốc là 0. Một array với độ dài (length) n có thể được lập index bởi các số nguyên từ 0 tới n-1.

*Ví dụ 1. Array Access*

```java
class Gauss {
    public static void main(String[] args) {
        int[] x = new int[101];
        for (int i = 0; i < x.length; i++) x[i] = i;
        int sum = 0;
        for (int e : x) sum += e;
        System.out.println(sum); // 5050

        int y = new int[1];
        System.out.println(y[0]); // null

    }
}
```

Các arrays phải được lập index bởi các *int values*; các short, byte, hoặc char values cũng có thể được sử dụng như các *index values*, vì chúng sẽ được mở rộng thành các int values.

Nếu cố gắng truy cập một array component với một *long index value* sẽ gây ra compile-time error.

Tất cả các truy cập array đều được kiểm tra tại runtime:  

- Nếu sử dụng 'index < 0' hoặc 'index >= length' của array thì *ArrayIndexOutOfBoundsException* sẽ được ném ra.  
- Nếu sử dụng 'null reference' thì *NullPointerException* sẽ được ném ra.  

```java
class Test {
    public static void main(String[] args) {
        int x = new int[1];
        System.out.println(x[0]);  // OK
        System.out.println(x[0L]); // Compile-time error
        System.out.println(x[-1]); // Run-time error (ArrayIndexOutOfBoundsException)

        long i = 1;
        System.out.println(x[(int) i]); // OK

        int[] y = null;
        y[0] = 1;       // Run-time error (NullPointerException)
    }
}
```


## 5. Array Store Exception

Đối với một array có type là A[], trong đó A là một *reference type*, một phép gán cho một component của array sẽ được kiểm tra tại runtime để đảm bảo rằng value được gán có thể gán cho component đó.

Nếu type của value được gán không tương thích với component type, thì một *ArrayStoreException* sẽ được ném ra.

*Ví dụ 1. ArrayStoreException*

```java
class Point { int x, y; }
class ColoredPoint extends Point { int color; }
class Test {
    public static void main(String[] args) {
        ColoredPoint[] cpa = new ColoredPoint[10];
        Point[] pa = cpa;
        System.out.println(pa[1] == null); // true
        try {
            pa[0] = new Point();
        } catch (ArrayStoreException e) {
            System.out.println(e);
        }
    }
}

// Output: true
//         java.lang.ArrayStoreException: Point

// Variable pa có type Point[],
// và variable cpa có giá trị là một reference đến một object của type ColoredPoint[].
// Một ColoredPoint có thể được gán cho một Point; do đó, value của cpa có thể được gán cho pa.

// Một reference tới array pa, vd: pa[1], sẽ không gây ra run-time type error.
// vì element của array của type ColoredPoint[] là một ColoredPoint,
// và mọi ColoredPoint có thể thay thế cho một Point, vì Point là superclass của ColoredPoint.

// Mặt khác, việc gán cho array pa có thể dẫn đến run-time error.
// Tại compile-time, phép gán cho element của pa được kiểm tra để đảm bảo value được gán là một Point.
// Nhưng vì pa giữ một reference tới một array của ColoredPoint,
// nên phép gán chỉ hợp lệ nếu type của value được gán tại run-time là ColoredPoint.

// JVM sẽ kiểm tra tình huống như vậy tại run-time để đảm bảo phép gán là hợp lệ;
// nếu không một ArrayStoreException sẽ được ném ra.
```


## 6. Array Initializers

Một *array initializer* có thể được xác định trong một *field declaration* hoặc *local variable declaration*, hoặc như một phần của một *array creation expression*, để tạo ra một array và cung cấp một số initial values.

```java
int[][] a = { {1, 2}, null };
int[] b = new int[] { 2, 5 };
```

Một *array initializer* được viết dưới dạng một list các expressions được phân tách bởi dấu phẩy (,), và được bao bọc bởi cặp ngoặc {}.

Sẽ gây ra compile-time error nếu:  

- Bất kỳ *variable initializer* nào không tương thích với component type của array.  
- Component type của array đang được khởi tạo là non-reifiable.  

Độ dài (length) của array được tạo sẽ bằng với số lượng *variable initializers* được bao trực tiếp bởi cặp ngoặc {} của *array initializer*. Không gian được phân bổ cho một array mới với độ dài đó. Nếu không đủ không gian để phân bổ array, việc đánh giá của array initializer sẽ hoàn thành đột ngột bởi ném ra một *OutOfMemoryError*. Nếu không, một *one-dimensional array* sẽ được tạo với độ dài length đã xác định, và mỗi component của array được khởi tạo thành default value của nó.

Sau đó, các *variable initializers* được bao bọc trực tiếp bởi cặp ngoặc {} của array initializer sẽ được thực thi từ trái sang phải theo thứ tự mà chúng xuất hiện trong source code. Variable initializer thứ n xác định value của array component thứ n-1. Nếu việc thực thi của một variable initializer hoàn thành đột ngột, thì việc thực thi của array initializer cũng hoàn thành đột ngột vì lý do tương tự. Nếu tất cả các variable initializer expressions đều hoàn thành bình thường, thì array initializer hoàn thành bình thường, với value của array mới được khởi tạo.

Nếu component type là một *array type*, thì *variable initializer* chỉ định một component có thể là một *array initializer*; nghĩa là, các array initializers có thể là nested. Trong trường hợp này, việc thực thi của *nested array initializer* sẽ tạo và khởi tạo một array object bằng cách áp dụng đệ quy thuật toán trên, và gán nó cho component.

*Ví dụ 1. Array Initializers*

```java
class Test {
    public static void main(String[] args) {
        int ia[][] = { {1, 2}, null };
        for (int[] ea : ia) {
            for (int e: ea) {
                System.out.println(e);
            }
        }
    }
}

// Output được in ra trước khi gây ra một NullPointerException:
// (vì cố lập index cho component thứ 2 là một null reference)
//    1
//    2
```


## 7. Array Members

Các members của một array type bao gồm:

- *public final field length*, chứa số lượng components của array. length có thể là 0 hoặc một số nguyên dương.  
- *public method clone*, overrides method clone trong class Object và không ném checked exceptions nào. Return type của clone method của một array type T[] là T[].  

    Một clone của một *multidimensional array* là *shallow*, nghĩa là nó chỉ tạo một array mới duy nhất. Các subarrays sẽ được chia sẻ.  

- Tất cả các members được thừa kế từ class Object; method duy nhất của Object không được kế thừa là clone method.  

*Ví dụ 1. Arrays Are Cloneable*

```java
class Test1 {
    public static void main(String[] args) {
        int ia1[] = { 1, 2 };
        int ia2[] = ia1.clone();
        System.out.print((ia1 == ia2) + " "); // false
        ia1[1]++;
        System.out.println(ia2[1]); // 2
    }
}

// Output: false 2
// ia1 và ia2 tham chiếu đến 2 array khác nhau,
// Các components của các arrays được tham chiếu bởi ia1 và ia2 là các variables khác nhau.
```

*Ví dụ 2. Shared Subarrays After A Clone*

```java
// The subarrays are shared when a multidimensional array is cloned.
class Test2 {
    public static void main(String[] args) throws Throwable {
        int ia[][] = { {1,2}, null };
        int ja[][] = ia.clone();
        System.out.print((ia == ja) + " "); // false
        System.out.println(ia[0] == ja[0] && ia[1] == ja[1]); // true
    }
}
// ia và ja tham chiếu đến 2 array khác nhau,
// ia[0] và ja[0] tham chiếu tới cùng một array.
```


## 8. Class Objects for Arrays

Mọi array đều có một *Class* object được liên kết, được chia sẻ với tất cả các arrays khác có cùng *component type*.

Mặc dù một *array type* không phải một *class*, nhưng *Class* object của mọi array hoạt động như thể:  

- Direct superclass của mọi array type là *Object*.  
- Mọi array type đều implements các interfaces *Cloneable* và *java.io.Serializable*.  

*Ví dụ 1. Class Object Of Array*

```java
class Test1 {
    public static void main(String[] args) {
        int[] ia = new int[3];              
        System.out.println(ia.getClass());                 
        System.out.println(ia.getClass().getSuperclass());
        for (Class<?> c : ia.getClass().getInterfaces())
            System.out.println("Superinterface: " + c);
    }
}

// Output: class [I
//         class java.lang.Object
//         Superinterface: interface java.lang.Cloneable
//         Superinterface: interface java.io.Serializable

// string "[I" là run-time type signature cho Class object "array with component type int".
```

*Ví dụ 2. Array Class Objects Are Shared*

```java
class Test2 {
    public static void main(String[] args) {
        int[] ia = new int[3];
        int[] ib = new int[6];
        System.out.println(ia == ib); // false
        System.out.println(ia.getClass() == ib.getClass()); // true
    }
}

// Trong khi ia và ib tham chiếu đến các arrays khác nhau, 
// Tất cả các arrays có component type là int đều là các instances của cùng một array type (int[]).
```


## 9. An Array of Characters Is Not a String

Trong Java, một *array of char* không phải một *String*, cả một String hay một array of char đều không được kết thúc bởi '\u0000' (NUL character).

Một *String object* là *immutable* (bất biến), nghĩa là, nội dung của nó không bao giờ, trong khi một *array of char* có các *mutable elements* (các phần tử có thể thay đổi).

*Note: Method toCharArray trong class String trả về một mảng các ký tự chứa cùng chuỗi ký tự giống như một String. Class StringBuffer triển khai các methods hữu ích trên các mutable arrays of characters.*
