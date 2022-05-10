# Chapter 9: Interfaces

## 6. Annotation Types

Một *annotation type declaration* xác định một *annotation type* mới, một loại *interface type* đặc biệt. Để phân biệt một *annotation type declaration* với một *normal interface declaration*, một ký tự at-sign (@) được đặt trước keyword interface.

```
- AnnotationTypeDeclaration:
    {InterfaceModifier} @ interface Identifier AnnotationTypeBody
```

Nếu một *annotation type* có cùng *simple name* với enclosing classes or interfaces, sẽ gây ra compile-time error.

*Direct superinterface* của mọi *annotation type* ngầm định là *java.lang.annotation.Annotation*, bởi vậy nó KHÔNG thể extends bất cứ class nào khác.

**Note**: Một *annotation type* KHÔNG thể là *generic*, nó không thể khai báo các *type variables*.

Một *annotation type* kế thừa một số *members* từ *java.lang.annotation.Annotation*, bao gồm các methods được khai báo ngầm định tương ứng với các *instance methods* của *Object*, tuy nhiên những methods này không định nghĩa các *elements* của *annotation type*.

**Note**: Để các thông tin chú thích xuất hiện trong documentation được tạo bởi Javadoc, thì annotation type phải được chú thích bởi annotation **java.lang.annotation.Documented**.  

```java
@Documented
@interface A {...}
```


### 6.1, Annotation Type Elements

Body của một *annotation type* có thể chứa các *method declarations*, mỗi *declaration* định nghĩa một *element* của *annotation type* đó. Một annotation type không có elements nào khác ngoài những elements đã được định nghĩa bởi các methods mà nó khai báo tường minh.  

```
- AnnotationTypeBody: { {AnnotationTypeMemberDeclaration} }
- AnnotationTypeMemberDeclaration:
    + AnnotationTypeElementDeclaration
    + ConstantDeclaration
    + ClassDeclaration
    + InterfaceDeclaration
    + ;
- AnnotationTypeElementDeclaration:
    {AnnotationTypeElementModifier} UnannType Identifier ( ) [Dims] [DefaultValue] ;
- AnnotationTypeElementModifier:
    (one of)
    Annotation public
    abstract
- Dims: {Annotation} [] {{Annotation} []}
```

**Note**: 

- Một *method declaration* trong một annotation type declaration KHÔNG thể có các *formal parameters, type parameters, or a throws clause*.  
- Một *method declaration* trong một annotation type declaration KHÔNG thể là *default* hoặc *static*.  

*Return type* của một method được khai báo trong một annotation type phải là một trong những type sau, nếu không sẽ gây ra compile-time error:  

- Một primitive type,  
- String,  
- Class hoặc một invocation của Class,  
- Một enum type,  
- Một annotation type,  
- Một array type có component type là một trong những types đã liệt kê ở trên.  

**Note**: *Return type của một method trong một annotation type không thể là một nested array type (mảng đa chiều).*

```java
@interface Verboten {
    String[][] value(); // compile-time error
}
```

Sẽ gây ra compile-time error nếu:

- Bất kỳ method nào được khai báo trong một annotation type có một signature là override-equivalent với signature của bất kỳ public hoặc protected method được khai báo trong class Object, hoặc trong interface java.lang.annotation.Annotation.  
- Một annotation type declaration T chứa một element có type T, trực tiếp hoặc gián tiếp.  

```java
@interface SelfRef { SelfRef value(); } // compile-time error

@interface Ping { Pong value(); } // compile-time error
@interface Pong { Ping value(); } // compile-time error
```

Các dạng annotation types:  

- *Marker annotation type*: là annotation type không có elements nào,  
- *Single-element annotation type*: là annotation type có một element,  
- *Normal annotation type*.  

**Quy ước**: Tên của element duy nhất trong một *single-element annotation type* là *value*.

*Ví dụ 1. Annotation Type Declaration*

```java
/**
 * Describes the "request-for-enhancement" (RFE)
 * that led to the presence of the annotated API element.
 */
@interface RequestForEnhancement {
    int    id();        // Unique ID number associated with RFE
    String synopsis();  // Synopsis of RFE
    String engineer();  // Name of engineer who implemented RFE
    String date();      // Date RFE was implemented
}
```

*Ví dụ 2. Marker Annotation Type Declaration*

```java
/**
 * An annotation with this type indicates that the specification of 
 * the annotated API element is preliminary and subject to change.
 */
@interface Preliminary {}
```

*Ví dụ 3. Single-Element Annotation Type Declarations*

```java
/**
 * Associates a copyright notice with the annotated API element.
 */
@interface Copyright {
    String value();
}

/**
 * Associates a list of endorsers with the annotated class.
 */
@interface Endorsers {
    String[] value();
}

interface Formatter {}

/**
 * Designates a formatter to pretty-print the annotated class
 */
@interface PrettyPrinter {
    Class<? extends Formatter> value();
}

/**
 * Indicates the author of the annotated program element.
 */
@interface Author {
    Name value();
}

/**
 * A person's name.  This annotation type is not designed to be used directly 
 * to annotate program elements, but to define elements of other annotation types.
 */
@interface Name {
    String first();
    String last();
}
```

**Note**: *Trong một annotation type declaration có thể chứa các element declarations khác ngoài các method declarations:*

```java
@interface Quality {
    enum Level { BAD, INDIFFERENT, GOOD }
    Level value();
}
```


### 6.2, Defaults for Annotation Type Elements

Một annotation type element có thể có một *default value*, được xác định với keyword *default* and một ElementValue đặt sau empty parameter list của element.

```
- DefaultValue:
    default ElementValue
```

Type của element phải tương ứng với *default value* được xác định.

*Default values* không được compile thành các *annotations*, nhưng nó được áp dụng động tại thời điểm annotations được đọc.

*Ví dụ 1. Annotation Type Declaration With Default Values*

```java
@interface RequestForEnhancementDefault {
    int    id();       // No default - must be specified in each annotation
    String synopsis(); // No default - must be specified in each annotation
    String engineer()  default "[unassigned]";
    String date()      default "[unimplemented]";
}
```


### 6.3, Repeatable Annotation Types

Kể từ Java SE 8, repeatable annotation type cho phép áp dụng nhiều annotations của một annotation type cho một declaration hay một sử dụng type.  

Một *annotation type* T là *repeatable* nếu declaration của nó được chú thích với một *@Repeatable* meta-annotation có value element xác định một *containing annotation type* của T.

**Note**: *Một containing annotation type TC của repeatable annotation type T phải khai báo value() method có return type là T[], và bất kỳ methods nào khác value() được khai báo bởi TC phải có một default value.*

Các *repeating annotations* được lưu trữ trong một *container annotation* được tạo tự động bởi Java compiler.  

*Ví dụ 1: Đặt một bộ hẹn giờ để chạy một method vào 11:00 p.m thứ 6 hàng tuần và ngày cuối cùng của tháng:*

```java
import java.lang.annotation.Repeatable;

public @interface Schedules {
    Schedule[] value();
}

// repeating annotations @Schedule is stored in an container annotation @Schedules
@Repeatable(Schedules.class)
public @interface Schedule {
  String dayOfMonth() default "first";
  String dayOfWeek() default "Mon";
  int hour() default 12;
}

@Schedule(dayOfMonth="last")
@Schedule(dayOfWeek="Fri", hour="23")
public void doPeriodicCleanup() { ... }
```


### 6.4, Predefined Annotation Types

Một tập các annotation types được định nghĩa trước trong Java SE API. Một số annotation types được sử dụng bởi Java compiler, và một số được áp dụng cho các annotation types khác.

Các predefined annotation types được sử dụng bởi Java compiler, được định nghĩa trong package java.lang bao gồm: @Deprecated, @Override, @SuppressWarnings, @SafeVarargs và @FunctionalInterface.

Các annotations mà áp dụng cho các annotation types khác được gọi là các *meta-annotations*. Có một số meta-annotation types được định nghĩa trong package java.lang.annotation.


#### *6.4.1, @Override*

@Override annotation thông báo cho compiler rằng element được dùng để override một element được khai báo trong một superclass.

Annotation type *java.lang.Override* hỗ trợ phát hiện các vấn đề phát sinh khi override một *method declaration*.  

Mặc dù không bắt buộc phải sử dụng annotation này khi overriding một method, nhưng nó giúp ngăn ngừa các errors. Nếu một method được đánh dấu bởi @Override không override chính xác một method trong một trong các superclasses của nó, thì compiler sẽ tạo ra một error.

```java
class Foo     { @Override public int hashCode() {..} } // OK
interface Bar { @Override int hashCode(); }            // OK
interface Quux { @Override Object clone(); }           // Error 
    // Vì Object.clone là protected, 
    // không có member nào được gọi là clone được khai báo ngầm trong Quux.
    // Do đó, declaration tường minh của clone trong Quux,
    // không được coi là "implement" bất kỳ method nào khác.
class Beep { @Override protected Object clone() {..} } // OK
```


#### *6.4.2, @SuppressWarnings*

Các Java compilers ngày càng có khả năng đưa ra các warnings hữu ích "lint-like".

Annotation type *java.lang.SuppressWarnings* hỗ trợ chặn các warnings đưa ra bởi Java compiler. Nó chứa một single element là một array of String.  

Nếu một declaration được chú thích với annotation @SuppressWarnings(value = {S1, ..., Sk}), thì Java compiler không được phép đưa ra bất kỳ warning nào được xác định bởi một trong S1 ... Sk. Nếu không thì các warning đó sẽ được tạo ra là kết quả của declaration đó hoặc bất kỳ phần nào của nó.

*Ví dụ: Một deprecated method được sử dụng, và compiler thường tạo ra một warning. Tuy nhiên, annotation khiến warning đó bị chặn.*

```java
 // use a deprecated method and tell compiler not to generate a warning
@SuppressWarnings("deprecation")
void useDeprecatedMethod() {
    // deprecation warning - suppressed
    objectOne.deprecatedMethod();
}
```

Mỗi compiler warning đều thuộc về một category (thể loại). Java liệt kê 2 categories bao gồm: *deprecation* và *unchecked*. Để chặn nhiều danh mục warning, hãy truyền một mảng làm giá trị cho annotation:

```java
@SuppressWarnings({"unchecked", "deprecation"})
```


#### *6.4.3, @Deprecated*

Annotation *@Deprecated* được sử dụng để chú thích cho một *program element* không được khuyến khích sử dụng, thường bởi vì nó nguy hiểm, hoặc tồn tại một element thay thế tốt hơn.

Java compiler phải đưa ra một warning phản đối khi một *field, method, constructor, or type* có declaration được chú thích với @Deprecated được sử dụng (overridden, invoked, or referenced by name) trong một cấu trúc được khai báo tường minh hay ngầm định, trừ khi:

- Sử dụng trong một entity mà bản thân nó được chú thích với annotation @Deprecated; hoặc  
- Sử dụng trong một entity mà đực chú thích để loại bỏ warning với annotation @SuppressWarnings("deprecation"); or  
- Cả sử dụng và declaration đều bên trong outermost class.  

Sử dụng @Deprecated annotation trên một *local variable declaration* hoặc trên một *parameter declaration* không có tác dụng.

Khi một element không còn được dùng nữa, nó cũng phải được ghi lại bằng cách sử dụng Javadoc @deprecated tag:

```java
// Javadoc comment follows
/**
 * @deprecated
 * explanation of why it was deprecated
 */
@Deprecated
static void deprecatedMethod() { }
```


#### *6.4.4, @SafeVarargs*

Một *variable arity parameter* (tham số hiếm bất định) với một *non-reifiable element type* có thể gây *heap pollution* và gây ra các compile-time unchecked warnings. Nếu body của variable arity method là well-behaved (an toàn) đối với variable arity parameter, thì các warnings như vậy là uninformative (không có thông tin cần thiết). 

Annotation type *java.lang.annotation.SafeVarargs*, được sử dụng để chú thích một *method or constructor declaration*, ngăn Java compiler đưa ra các unchecked warnings cho declaration hoặc invocation của một variable arity method or constructor, nếu không compiler sẽ làm vậy vì variable arity parameter có non-reifiable element type.

*VD: một method chỉ thực hiện in ra các phần tử trong mảng được tham chiếu bởi một variable arity parameter, như vậy các hoạt đông mà method thực hiện là an toàn với variable arity parameter. Lúc này các unchecked warning là không cần thiết, có thể sử dụng @SafeVarargs để ngăn compiler đưa ra warning.*

Sẽ gây ra compile-time error nếu:

- Một fixed arity method or constructor declaration được chú thích với annotation @SafeVarargs.  
- Một variable arity method declaration không phải static hoặc final được chú thích với annotation @SafeVarargs.  

```java
public static <T> boolean addAll(Collection<? super T> c, T... elements)
// The variable arity parameter has declared type T[], which is non-reifiable.
```


#### *6.4.5, @FunctionalInterface*

Annotation type *java.lang.annotation.FunctionalInterface* được sử dụng để chỉ ra rằng một *interface* được đánh dấu sẽ là một *functional interface*. Nó hỗ trợ phát hiện các method declarations không phù hợp xuất hiện trong một interface hoặc được thừa kế bởi một interface mà là *functional*.  

```java
@FunctionalInterface
interface Animal { ... }
```


#### *6.4.6, @Target*

Một annotation của type *java.lang.annotation.Target* được sử dụng trên declaration của một *annotation type* T để xác định các *contexts* trong đó T được áp dụng. 

java.lang.annotation.Target có một single element, *value*, của type *java.lang.annotation.ElementType[]*, để xác định các contexts.

Các *annotation types* có thể được áp dụng trong *declaration contexts*, trong đó các annotations áp dụng cho các *declarations*, hoặc trong *type contexts*, trong đó các annotations áp dụng cho các *types* được sử dụng trong các declarations và expressions.

Có 8 *declaration contexts*, mỗi context tương ứng với một enum constant của *java.lang.annotation.ElementType*:

- Package declarations:  
    java.lang.annotation.ElementType.*PACKAGE*,  

- Type declarations: class, interface, enum, and annotation type declarations:  
    java.lang.annotation.ElementType.*TYPE*,  
    java.lang.annotation.ElementType.*ANNOTATION_TYPE*,  

- Method declarations (bao gồm các elements của annotation types):  
    java.lang.annotation.ElementType.*METHOD*,  

- Constructor declarations:  
    java.lang.annotation.ElementType.*CONSTRUCTOR*,  

- Type parameter declarations của generic classes, interfaces, methods, and constructors:  
    java.lang.annotation.ElementType.*TYPE_PARAMETER*,  

- Field declarations (bao gồm các enum constants):  
    java.lang.annotation.ElementType.*FIELD*,  

- Formal and exception parameter declarations:  
    java.lang.annotation.ElementType.*PARAMETER*,  

- Local variable declarations (bao gồm các loop variables của for statements và các resource variables của try-with-resources statements):  
    java.lang.annotation.ElementType.*LOCAL_VARIABLE*,  

Có 16 *type contexts*, tất cả đều được đại diện bởi enum constant *TYPE_USE* của *java.lang.annotation.ElementType*.  

Nếu annotation của type java.lang.annotation.Target không xuất hiện trên declaration của một *annotation type* T, thì T có thể áp dụng trên tất cả các declaration contexts ngoại trừ các *type parameter declarations*, và không có type contexts.

```java
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@interface annotations { ... }
```


#### *6.4.7, @Retention*

annotation type *java.lang.annotation.Retention* được sử dụng để chỉ ra rằng các *annotations* chỉ hiện diện trong source code, hay chúng có thể hiện diện trong dạng binary của một class hoặc interface. Một annotation hiện diện ở dạng binary có thể có hoặc không available tại runtimethông qua reflection libraries của Java SE platform.

Nếu một annotation a tương ứng với type T, và T có một (meta-)annotation m tương ứng với *java.lang.annotation.Retention*, thì:  

- Nếu m có một element có value là *java.lang.annotation.RetentionPolicy.SOURCE*, thì Java compiler phải đảm bảo rằng a không hiện diện trong dạng binary của class hoặc interface mà a xuất hiện.  
- Nếu m có một element có value là *java.lang.annotation.RetentionPolicy.CLASS* hoặc *java.lang.annotation.RetentionPolicy.RUNTIME*, thì Java compiler phải đảm bảo rằng a sẽ hiện diện trong dạng binary của class hoặc interface mà a xuất hiện, trừ khi m chú thích một *local variable declaration*.  

    Một annotation trên một local variable declaration không bao giờ được giữ lại trong biểu diễn binary.  

    Nếu m có một element có value là *java.lang.annotation.RetentionPolicy.CLASS*, thì nó sẽ bị bỏ qua bởi JVM.  

    Nếu m có một element có value là *java.lang.annotation.RetentionPolicy.RUNTIME*, thì nó sẽ được giữ lại để sử dụng trong runtime (reflection libraries của Java SE platform phải available tại runtime để có thể truy xuất các annotations như interface java.lang.reflect.AnnotatedElement).  

Nếu T không có một (meta-)annotation m tương ứng với java.lang.annotation.Retention, thì Java compiler phải coi T như thể nó có một meta-annotation m với một element có value là *java.lang.annotation.RetentionPolicy.CLASS*.  

```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@interface annotations { ... }
```


#### *6.4.8, @Inherited*

Annotation type *java.lang.annotation.Inherited* được sử dụng để chỉ ra rằng các *annotations* trên một *class* C tương ứng với một *annotation type* xác định, sẽ được kế thừa bởi các subclasses của C.


#### *6.4.9, @Repeatable*

Annotation type *java.lang.annotation.Repeatable* được sử dụng trên declaration của một *repeatable annotation type* để chỉ ra *containing annotation type* của nó.  


#### *6.4.10, @Documented*

*@Documented* annotation chỉ ra rằng bất cứ khi nào một annotation của một annotation type xác định được sử dụng, các elements này phải được ghi lại bằng Javadoc tool. (Mặc định, các annotations không được bao gồm trong Javadoc.)


## 7. Annotations

Annotations là một dạng metadata (siêu dữ liệu), cung cấp dữ liệu về một chương trình cho compiler mà không phải là một phần của chính chương trình đó. Annotations không có ảnh hưởng trực tiếp đến hoạt động của code mà chúng chú thích.

Ký tự @ cho compiler biết đây là một annotation.  

Annotations có một số cách sử dụng, trong số đó:

- **Thông tin cho compiler** - Compiler có thể sử dụng các annotations để phát hiện lỗi hoặc ngăn chặn các cảnh báo.  
- **Xử lý compile-time và deployment-time** - Các công cụ phần mềm có thể xử lý thông tin chú thích để tạo mã, tệp XML, ...  
- **Xử lý runtime** - Một số annotations có sẵn cho việc kiểm tra trong runtime.  

Một annotation biểu thị một *invocation* cụ thể của một *annotation type* và thường cung cấp các values cho các elements của type đó.


### 7.1, Normal Annotations

```
- NormalAnnotation:     @ TypeName ( [ElementValuePairList] )
- ElementValuePairList: ElementValuePair {, ElementValuePair}
- ElementValuePair:     Identifier = ElementValue
```

Một normal annotation phải chứa một cặp element-value cho mỗi element của annotation type tương ứng, ngoại trừ những elements có default values, nếu không sẽ gây ra compile-time error.

*Ví dụ: Normal Annotations* 

```java
@interface RequestForEnhancementDefault {
    int    id();       // No default - must be specified in each annotation
    String synopsis(); // No default - must be specified in each annotation
    String engineer()  default "[unassigned]";
    String date()      default "[unimplemented]";
}

@RequestForEnhancement(
    id       = 2868724,
    synopsis = "Provide time-travel functionality",
    engineer = "Mr. Peabody",
    date     = "4/1/2004"
)
public static void travelThroughTime(Date destination) { ... }

@RequestForEnhancement(
    id       = 4561414,
    synopsis = "Balance the federal budget"
)
public static void balanceFederalBudget() {
    throw new UnsupportedOperationException("Not implemented");
}
```


### 7.2, Marker Annotations

```
- MarkerAnnotation:
    @TypeName

- It is shorthand for the normal annotation:
    @TypeName()
```

*Ví dụ: Marker Annotations*

```java
@interface Preliminary {}

@Preliminary public class TimeTravel { ... }
```


### 7.3, Single-Element Annotations

```
- SingleElementAnnotation: 
    @ TypeName ( ElementValue )

- It is Shorthand for the normal annotation:
    @TypeName(value = ElementValue)
```

*Ví dụ: Single-Element Annotations*

```java
// EXAMPLE 1:
@interface Copyright {
    String value();
}

@Copyright("2002 Yoyodyne Propulsion Systems, Inc.")
public class OscillationOverthruster { ... }

// EXAMPLE 2: Khi sử dụng shorthand, type của element là array, cặp ngoặc {} có thể được lược bỏ
@interface Endorsers {
    String[] value();
}

@Endorsers({ "Children", "Unscrupulous dentists" })
public class Lollipop { ... }

@Endorsers("Epicurus") 
public class Pleasure { ... }

// EXAMPLE 3:
interface Formatter {}

@interface PrettyPrinter {
    Class<? extends Formatter> value();
}

class GorgeousFormatter implements Formatter { ... }

@PrettyPrinter(GorgeousFormatter.class)
public class Petunia { ... }

@PrettyPrinter(String.class) // Illegal; String is not a subtype of Formatter
public class Begonia { ... }

// EXAMPLE 4:
@interface Author {
    Name value();
}

@interface Name {
    String first();
    String last();
}

@Author(@Name(first = "Joe", last = "Hacker"))
public class BitTwiddle { ... }

// EXAMPLE 5:
@interface Quality {
    enum Level { BAD, INDIFFERENT, GOOD }
    Level value();
}

@Quality(Quality.Level.GOOD)
public class Karma { ... }
```


### 7.4, Where Annotations May Appear

Một *declaration annotation* là một annotation áp dụng cho một *declaration*.

Một *type annotation* là một annotation áp dụng cho việc sử dụng một *type* (hoặc bất kỳ phần nào của một type), được hỗ trợ kể từ Java SE 8 nhằm hỗ trợ cải thiện type checking mạnh mẽ hơn, ví dụ:  

- Field declaration:  
    ```java
    @NonNull String str;
    ```  
- Class instance creation expression:  
    ```java
    new @Interned MyObject();
    ```  
- Type cast:  
    ```java
    myString = (@NonNull String) str;
    ```  
- implements clause:  
    ```java
    class UnmodifiableList<T> implements @Readonly List<@Readonly T> { ... }
    ```  
- Thrown exception declaration:  
    ```java
    void monitorTemperature() throws @Critical TemperatureException { ... }
    ```  

Một annotation có thể đồng thời là một declaration annotation và một type annotation.

Ngoài ra, các annotations có thể thay thế các comment chú thích cho các declaration trong code.  

*Ví dụ 1:*

```java
@Foo int f;
    // 1, @Foo là một declaration annotation trên f,
    //    nếu annotation type Foo được chú thích bởi @Target(ElementType.FIELD).
    // 2, @Foo là một type annotation trên int,
    //    nếu annotation type Foo được chú thích bởi @Target(ElementType.TYPE_USE).
    // 3, Có thể cho @Foo đồng thời là một declaration annotation và một type annotation.

// Các type annotations có thể áp dụng cho một array type hoặc bất kỳ component type nào:
@C int @A [] @B [] f;
    // @A áp dụng cho array type int[][],
    // @B áp dụng cho component type int[] của nó,
    // @C áp dụng cho element type int.

// Hay @C áp dụng cho type int trong tất cả các khai báo: 
@C int f;
@C int[] f;
@C int[][] f;
```

*Ví dụ 2*:

```java
@Target(ElementType.TYPE_USE)
@interface Foo {}

// CASE 1:
class Test {
    class A {
        static class B {} // compile-time error
            // Không thể khai báo static class trong một inner class
    }

    // Không thể viết A.this trong body của B, vì B không có enclosing instances. 
    // Do đó, không thể áp dụng @Foo cho A trong type A.B,
    // vì A về mặt logic chỉ là một tên, không phải một type.
    @Foo A.B x;  // Illegal 
}

// CASE 2:
class Test {
    static class C {
        class D {}
    }

    // Có thể viết C.this trong body của D.
    // Do đó, có thể áp dụng @Foo cho C trong type C.D,
    // vì C đại diện cho type của một số object tại run time.
    @Foo C.D x;  // Legal 
}
```


### 7.5, Multiple Annotations of the Same Type

Nếu một declaration context hoặc type context có nhiều annotations của một *repeatable annotation type* T, thì như thể context đó không có annotations của type T nào được khai báo tường minh, và có một annotation của *containing annotation type* của T được khai báo ngầm định.

Annotation được khai báo ngầm định được gọi là *container annotation*, và multiple annotations của type T xuất hiện trong context được gọi là các *base annotations*. Các elements của value element (array type) của container annotation là tất cả các base annotations theo thứ tự từ trái sang phải mà chúng xuất hiện trong context.

Sẽ gây compile-time error nếu:  

- Nhiều annotations của cùng type T xuất hiện trong một declaration context hoặc type context, và T không phải repeatable.  
- Trong một declaration context hoặc type context, có nhiều annotations của một repeatable annotation type T và bất kỳ annotations nào của containing annotation type của T.  
- Trong một declaration context hoặc type context, có một annotation của một repeatable annotation type T và nhiều annotations của một containing annotation type của T.  

Nếu chỉ có một annotation của một repeatable annotation type T xuất hiện trong một declaration context hoặc type context, thì sẽ không có annotation nào của containing annotation type của T được khai báo ngầm định. Do vậy trong một declaration context hoặc type context có thể có một annotation của repeatable annotation type T và một annotation của containing annotation type của T.

```java
@Repeatable(FooContainer.class)
@interface FooContainer { Foo[] value(); }

@Foo(0) @Foo(1) // OK
class A {}

@Foo(1) @FooContainer({@Foo(2)}) // OK
class A {}

@Foo(0) @Foo(1) @FooContainer({@Foo(2)}) // compile-time error
class A {}

@Foo(1) @FooContainer({@Foo(2)}) @FooContainer({@Foo(3)}) // compile-time error
class A {}
```


## 8. Retrieving Annotations

Có một số methods có sẵn trong *Reflection API* có thể được sử dụng để truy xuất các annotations, ví dụ như *AnnotatedElement.getAnnotation(Class<T>)* chỉ return một annotation duy nhất nếu có annotation của type được yêu cầu. Nếu có nhiều hơn một annotation của type được yêu cầu, trước tiên có thể lấy container annotation của chúng. Các methods khác được giới thiệu trong Java SE 8 quét qua container annotation để return nhiều annotations cùng một lúc, ví dụ như *AnnotatedElement.getAnnotationsByType(Class<T>)*.

```java
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Arrays;
  
public class GFG {
  
    // initialize field with default value in annotation
    @annotations(32512.21)
    private double realNumbers;
  
    public static void main(String[] args) throws NoSuchFieldException {
        // create Field object
        Field field = GFG.class.getDeclaredField("realNumbers");
  
        // apply getAnnotationsByType()
        annotations[] annotations = field.getAnnotationsByType(annotations.class);
  
        // print results
        System.out.println(Arrays.toString(annotations));
    }
  
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    private @interface annotations {
        double value() default 99.9;
    }
}
// Output: [@GFG$annotations(value=32512.21)]
```