# Lession 4. Annotations

Annotation là một dạng metadata (siêu dữ liệu), cung cấp dữ liệu về một chương trình, nó không phải là một phần của bản thân chương trình đó. Annotations không có ảnh hưởng trực tiếp đến hoạt động của mã mà chúng chú thích.

Annotations có một số cách sử dụng, trong số đó:

- **Thông tin cho compiler** - Compiler có thể sử dụng các annotations để phát hiện lỗi hoặc ngăn chặn các cảnh báo.  
- **Xử lý compile-time và deployment-time** - Các công cụ phần mềm có thể xử lý thông tin chú thích để tạo mã, tệp XML, ...  
- **Xử lý runtime** - Một số annotations có sẵn cho việc kiểm tra trong runtime.  


## 1. Annotations Basics

Ký tự @ cho compiler biết đây là một annotation.

Annotations có thể có các elements.

Từ Java SE 8 hỗ trợ các repeating annotations, là các annotations cùng types áp dụng cho một declaration hay một type.

Java hỗ trợ các predefined annotation types như Override, SuppressWarnings. Hay có thể tự định nghĩa các custom annotation types.  

Chi tiết xem tại [Java specifications about annotation](../../specifications/9_interfaces-2.md).


### 1.1, The Format of an Annotation

Các dạng annotations:  

- *Marker annotation*: là annotation không có elements nào,  
    ``` java
    @Override()
    @Override   // shorthand
    ```  
- *Single-element annotation*: là annotation có một element,  
    **Quy ước**: Tên của element duy nhất trong một *single-element annotation type* là *value*.

    ```java
    @SuppressWarnings(value = "unchecked")
    @SuppressWarnings("unchecked")          // shorthand
    ```  
- *Normal annotation*,  
    ```
    @Author(name = "Benjamin Franklin", date = "3/27/2003")
    ```  


### 1.2, Where Annotations Can Be Used

Các annotations có thể được áp dụng cho các declarations: các declarations của classes, fields, methods, và các elements khác của chương trình. Các annotaions này được gọi là các *declaration annotations*.

Từ Java SE 8 trở đi, các annotations cũng có thể được áp dụng cho việc sử dụng các types, được gọi là các *type annotations*, ví dụ:

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


## 2. Declaring an Annotation Type

Nhiều annotations thay thế các comments trong code.

Giả sử rằng các phần mềm truyền thống chú thích các thông tin quan trọng cho mỗi class như sau:  

```java
public class Generation3List extends Generation2List {
    
    // Author: John Doe
    // Date: 3/17/2002
    // Current revision: 6
    // Last modified: 4/12/2004
    // By: Jane Doe
    // Reviewers: Alice, Bill, Cindy

    // class code goes here
}
```

Để thêm metadata tương tự với một annotation, trước tiên cần phải định nghĩa một annotation type:

```java
@interface ClassPreamble {
   String author();
   String date();
   int currentRevision() default 1;
   String lastModified() default "N/A";
   String lastModifiedBy() default "N/A";
   String[] reviewers();
}
```

Các annotation types là một dạng interface, phần body của annotation definition có thể chứa các element declarations, và có thể định nghĩa các default value cho chúng.

Sau khi định nghĩa các annotation type, có thể sử dụng các annotations của type đó, và truyền các thông tin chú thích như sau:

```java
@ClassPreamble (
   author = "John Doe",
   date = "3/17/2002",
   currentRevision = 6,
   lastModified = "4/12/2004",
   lastModifiedBy = "Jane Doe",
   reviewers = {"Alice", "Bob", "Cindy"}
)
public class Generation3List extends Generation2List {
    // class code goes here
}
```

**Note**: Để các thông tin chú thích xuất hiện trong documentation được tạo bởi Javadoc, thì annotation type phải được chú thích bởi annotation **java.lang.annotation.Documented**.  

```java
import java.lang.annotation.Documented;

@Documented
@interface ClassPreamble {
    // Annotation element definitions
}
```


## 3. Predefined Annotation Types

Một tập các annotation types được định nghĩa trước trong Java SE API. Một số annotation types được sử dụng bởi Java compiler, và một số được áp dụng cho các annotation types khác.


### 3.1, Annotation Types Used by the Java Language

Các predefined annotation types được sử dụng bởi Java compiler, được định nghĩa trong package java.lang bao gồm: @Deprecated, @Override, và @SuppressWarnings, @SafeVarargs và @FunctionalInterface.

#### *3.1.1, @Deprecated*

@Deprecated annotation chỉ ra rằng các elements được đánh dấu không còn được dùng nữa, và không nên được sử dụng nữa. Compiler tạo một warning bất cứ khi nào program sử dụng một field, method, constructor, hay type với @Deprecated annotation. 

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


#### *3.1.2, @Override*

@Override annotation thông báo cho compiler rằng element được đánh dấu được dùng để override một element được khai báo trong một superclass.

Mặc dù không bắt buộc phải sử dụng annotation này khi overriding một method, nhưng nó giúp ngăn ngừa các errors. Nếu một method được đánh dấu bởi @Override không override chính xác một method trong một trong các superclasses của nó, thì compiler sẽ tạo ra một error.

```java
// mark method as a superclass method that has been overridden
@Override 
int overriddenMethod() { }
```


#### *3.1.3, @SuppressWarnings*

@SuppressWarnings annotation yêu cầu compiler loại bỏ các warnings xác định mà nếu không sử dụng annotation này thì nó sẽ được tạo. 

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


#### *3.1.4, @SafeVarargs*

@SafeVarargs annotation được áp dụng cho method hoặc constructor declarations, xác nhận rằng code không thực hiện các hoạt động tiềm ẩn không an toàn trên các varargs parameters (vd: String... args) của nó. Khi annotation này được sử dụng, các unchecked warnings liên quan đến việc sử dụng varargs sẽ bị loại bỏ.


#### *3.1.5, @FunctionalInterface*

@FunctionalInterface annotation, được giới thiệu trong Java SE 8, chỉ ra rằng type declaration được đánh dấu nhằm trở thành một functional interface.


### 3.2, Annotations That Apply to Other Annotations

Các annotations mà áp dụng cho các annotation types khác được gọi là các *meta-annotations*. Có một số meta-annotation types được định nghĩa trong package java.lang.annotation.

#### *3.2.1, @Retention*

@Retention annotation xác định cách lưu trữ các annotations của annotation type được đánh dấu:  

- RetentionPolicy.SOURCE – Các annotations của annotation type được đánh dấu, chỉ được giữ trong source level và bị compiler bỏ qua.  
- RetentionPolicy.CLASS – Các annotations của annotation type được đánh dấu, được compiler giữ lại tại compile time, nhưng bị JVM bỏ qua.  
- RetentionPolicy.RUNTIME – Các annotations của annotation type được đánh dấu, được JVM giữ lại, để được sử dụng bởi runtime environment.  


#### *3.2.2, @Documented*

@Documented annotation chỉ ra rằng bất cứ khi nào annotation của annotation type được đánh dấu được sử dụng, thì những elements này phải được ghi lại bởi Javadoc tool (theo mặc đinh, các annotations không được bao gồm trong Javadoc).


#### *3.2.3, @Target*

@Target annotation hạn chế các loại Java elements mà các annotations của một annottion type được đánh dấu có thể được áp dụng. 

@Target annotation nhận đối số là một mảng các element types có thể là:

- ElementType.ANNOTATION_TYPE: có thể được áp dụng cho một annotation type declaration.  
- ElementType.CONSTRUCTOR: có thể được áp dụng cho một constructor declaration.  
- ElementType.FIELD: có thể được áp dụng cho một field declaration (bao gồm các enum constants).  
- ElementType.LOCAL_VARIABLE: có thể được áp dụng cho một local variable declaration.  
- ElementType.METHOD: có thể được áp dụng cho một method declaration.  
- ElementType.PACKAGE: có thể được áp dụng cho một package declaration.  
- ElementType.PARAMETER: có thể được áp dụng cho các parameter declarations của một method.  
- ElementType.TYPE: có thể được áp dụng cho một type declaration.  
- ElementType.TYPE_PARAMETER: có thể được áp dụng cho một type parameter declaration của một generic.  
- ElementType.TYPE_USE: có thể được áp dụng cho việc sử dụng các types.  


#### *3.2.4, @Inherited*

@Inherited annotation chỉ ra rằng các annotations được áp dụng cho một class declaration của một annotation type được đánh dấu, có thể được thừa kế bởi subclass (điều này không đúng theo mặc định). Khi user truy vấn annotation type và class không có annotation của type này, thì superclass sẽ được truy vấn cho annotation type này. 

Annotation này chỉ áp dụng cho class declarations.


#### *3.2.5, @Repeatable*

@Repeatable annotation, được giới thiệu trong Java SE 8, chỉ ra rằng các annotations của một annotation type được đánh dấu có thể được áp dụng nhiều hơn một lần cho cùng một declaration hoặc sử dụng type.


## 4. Type Annotations and Pluggable Type Systems

Trước Java SE 8, annotations chỉ có thể được áp dụng cho các declarations. Kể từ Java SE 8, annotations cũng có thể được áp dụng cho bất kỳ sử dụng type nào. Điều này có nghĩa là các annotations có thể được sử dụng ở bất kỳ nơi nào sử dụng một type như: class instance creation expressions (new), casts, implements clause và throws clause.

Type annotations được tạo ra để hỗ trợ cải thiện các type checking mạnh hơn. Java SE 8 không cung cấp type checking framework, nhưng nó cho phép viết (hoặc download) type checking framework được triển khai dưới dạng một hoặc nhiều pluggable modules (module có thể cắm thêm) được sử dụng cùng với Java compiler.

*VD: Khi muốn đảm bảo rằng một biến cụ thể trong chương trình không bao giờ được gán cho null; tránh kích hoạt NullPointerException, có thể viết một custom plug-in để kiểm tra điều này. Sau đó, sử dụng để chú thích biến cụ thể đó, cho biết rằng nó không bao giờ được gán cho null:*

```java
@NonNull String str;
```

*Khi compile code, bao gồm module NonNull tại command line, compiler sẽ in cảnh báo nếu phát hiện sự cố tiềm ẩn, cho phép sửa đổi mã để tránh lỗi. Sau khi sửa mã để loại bỏ tất cả các cảnh báo, lỗi cụ thể này sẽ không xảy ra khi chương trình chạy.*

Có thể sử dụng nhiều type-checking modules, trong đó mỗi module kiểm tra một loại lỗi khác nhau.

Với việc sử dụng hợp lý các type annotation và sự hiện diện của pluggable type checkers, có thể viết mã mạnh hơn và ít bị lỗi hơn.

Trong nhiều trường hợp, không cần phải viết type-checking modules của riêng mình. Có thể sử dụng các modules của bên thứ 3 như Checker Framework được tạo bởi University of Washington.


## 5. Repeating Annotations

Kể từ Java SE 8, repeatable annotation type cho phép áp dụng nhiều annotations của một annotation type cho một declaration hay một sử dụng type.  

Các *repeating annotations* được lưu trữ trong một *container annotation* được tạo tự động bởi Java compiler.  

*Ví dụ 1: Đặt một bộ hẹn giờ để chạy một method vào 11:00 p.m thứ 6 hàng tuần và ngày cuối cùng của tháng:*

```java
@Schedule(dayOfMonth="last")
@Schedule(dayOfWeek="Fri", hour="23")
public void doPeriodicCleanup() { ... }
```

### 5.1, Step 1: Declare a Repeatable Annotation Type

Annotation type phải được đánh dấu với @Repeatable meta-annotation.

```java
import java.lang.annotation.Repeatable;

// repeating annotations @Schedule is stored in an container annotation @Schedules
@Repeatable(Schedules.class)
public @interface Schedule {
  String dayOfMonth() default "first";
  String dayOfWeek() default "Mon";
  int hour() default 12;
}
```

Value của @Repeatable meta-annotation, là type của container annotation mà Java compiler tạo để lưu trữ các repeating annotations.

### 5.2, Step 2: Declare the Containing Annotation Type

Containing annotation type phải có một value element với một array type, component type của array type phải là repeatable annotation type:

```java
public @interface Schedules {
    Schedule[] value();
}
```

### 5.3, Retrieving Annotations

Có một số methods có sẵn trong *Reflection API* có thể được sử dụng để truy xuất các annotations, ví dụ như `AnnotatedElement.getAnnotation(Class<T>)` chỉ return một annotation duy nhất nếu có annotation của type được yêu cầu. Nếu có nhiều hơn một annotation của type được yêu cầu, trước tiên có thể lấy container annotation của chúng. Các methods khác được giới thiệu trong Java SE 8 quét qua container annotation để return nhiều annotations cùng một lúc, ví dụ như `AnnotatedElement.getAnnotationsByType(Class<T>)`.

### 5.4, Design Considerations

Khi thiết kế một annotation type:  

- Phải xem xét số lượng của các annotations của type đó, có thể sử dụng một annotation 0 lần, 1 lần hoặc, nếu annotation type được đánh dấu là @Repeatable, nhiều lần.  
- Cũng có thể hạn chế nơi có thể sử dụng annotation type bằng cách sử dụng meta-annotation @Target. Ví dụ: có thể tạo một annotation type có thể lặp lại chỉ có thể được sử dụng trên các methods và fields.  

Điều quan trọng là phải thiết kế annotation type một cách cẩn thận để đảm bảo rằng programmer sử dụng annotations thấy nó linh hoạt và mạnh mẽ nhất có thể.