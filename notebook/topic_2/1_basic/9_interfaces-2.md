# Chapter 9: Interfaces

## 6. Annotation Types

Một *annotation type declaration* xác định một *annotation type* mới, một loại *interface type* đặc biệt. Để phân biệt một *annotation type declaration* với một *normal interface declaration*, một ký tự at-sign (@) được đặt trước keyword interface.

```
- AnnotationTypeDeclaration:
    {InterfaceModifier} @ interface Identifier AnnotationTypeBody
```

Nếu một *annotation type* có cùng *simple name* với enclosing classes or interfaces, sẽ gây ra compile-time error.

*Direct superinterface* của mọi *annotation type* là *java.lang.annotation.Annotation*.

**Note**: Một *annotation type* không thể là *generic*, và không được phép có *extends clause*.

Một *annotation type* kế thừa một số *members* từ *java.lang.annotation.Annotation*, bao gồm các methods được khai báo ngầm định tương ứng với các *instance methods* của *Object*, tuy nhiên những methods này không định nghĩa các *elements* của *annotation type*.


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

- Một *method declaration* trong một annotation type declaration không thể có các *formal parameters, type parameters, or a throws clause*.  
- Một *method declaration* trong một annotation type declaration không thể là *default* hoặc *static*.  

*Return type* của một method được khai báo trong một annotation type phải là một trong những type sau, nếu không sẽ gây ra compile-time error:  

- Một primitive type,  
- String,  
- Class hoặc một invocation của Class,  
- Một enum type,  
- Một annotation type,  
- Một array type có component type là một trong những types đã liệt kê ở trên.  

*Note: Return type của một method trong một annotation type không thể là một nested array type (mảng đa chiều).*

```java
@interface Verboten {
    String[][] value(); // compile-time error
}
```

Sẽ gây ra compile-time error nếu:

- Bất kỳ method nào được khai báo trong một annotation type có một signature là override-equivalent với signature của bất kỳ public hoặc protected method được khai báo trong class Object, hoặc trong interface java.lang.annotation.Annotation.  
- Một annotation type declaration T chứa một element của type T, trực tiếp hoặc gián tiếp.  

```java
@interface SelfRef { SelfRef value(); } // compile-time error

@interface Ping { Pong value(); } // compile-time error
@interface Pong { Ping value(); } // compile-time error
```

Một annotation type không có các elements được gọi là một *marker annotation type*.

Một annotation type có một element được gọi là một *single-element annotation type*.

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

*Note: Trong một annotation type declaration có thể chứa các element declarations khác ngoài các method declarations:*

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

Một *annotation type* T là *repeatable* nếu declaration của nó được chú thích với một *@Repeatable* annotation có value element xác định một *containing annotation type* của T.

*Note: Một containing annotation type TC của repeatable annotation type T phải khai báo value() method có return type là T[], và bất kỳ methods nào khác value() được khai báo bởi TC phải có một default value.*

*Ví dụ 1:*

```java
@Repeatable(FooContainer.class)
@interface Foo {}

@interface FooContainer { Object[] value(); } // compile-time error
@interface FooContainer { Foo[] value(); }    // OK

@Foo @Foo
@interface X {}
```


### 6.4, Predefined Annotation Types

#### *6.4.1, @Target*

Một annotation của type *java.lang.annotation.Target* được sử dụng trên declaration của một *annotation type* T để xác định các *contexts* trong đó T được áp dụng. 

java.lang.annotation.Target có một single element, *value*, của type *java.lang.annotation.ElementType[]*, để xác định các contexts.

Các *annotation types* có thể được áp dụng trong *declaration contexts*, trong đó các annotations áp dụng cho các *declarations*, hoặc trong *type contexts*, trong đó các annotations áp dụng cho các *types* được sử dụng trong các declarations và expressions.

Có 8 *declaration contexts*, mỗi context tương ứng với một enum constant của *java.lang.annotation.ElementType*:

- Package declarations:  
    java.lang.annotation.ElementType.*PACKAGE*,  

- Type declarations: class, interface, enum, and annotation type declarations:  
    java.lang.annotation.ElementType.*TYPE*,  
    java.lang.annotation.ElementType.*ANNOTATION_TYPE*,  

- Method declarations (bào gồm các elements của annotation types):  
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


#### *6.4.2, @Retention*

annotation type *java.lang.annotation.Retention* được sử dụng để chỉ ra rằng các *annotations* chỉ hiện diện trong source code, hay chúng có thể hiện diện trong dạng binary của một class hoặc interface. Một annotation hiện diện ở dạng binary có thể có hoặc không available tại runtimethông qua reflection libraries của Java SE platform.

Nếu một annotation a tương ứng với type T, và T có một (meta-)annotation m tương ứng với *java.lang.annotation.Retention*, thì:  

- Nếu m có một element có value là *java.lang.annotation.RetentionPolicy.SOURCE*, thì Java compiler phải đảm bảo rằng a không hiện diện trong dạng binary của class hoặc interface mà a xuất hiện.  
- Nếu m có một element có value là *java.lang.annotation.RetentionPolicy.CLASS* hoặc *java.lang.annotation.RetentionPolicy.RUNTIME*, thì Java compiler phải đảm bảo rằng a sẽ hiện diện trong dạng binary của class hoặc interface mà a xuất hiện, trừ khi m chú thích một *local variable declaration*.  

    Một annotation trên một local variable declaration không bao giờ được giữ lại trong biểu diễn binary.  

    Ngoài ra, nếu m có một element có value là j*ava.lang.annotation.RetentionPolicy.RUNTIME*, reflection libraries của Java SE platform phải available tại runtime.  

Nếu T không có một (meta-)annotation m tương ứng với java.lang.annotation.Retention, thì Java compiler phải coi T như thể nó có một meta-annotation m với một element có value là *java.lang.annotation.RetentionPolicy.CLASS*.  


#### *6.4.3, @Inherited*

Annotation type *java.lang.annotation.Inherited* được sử dụng để chỉ ra rằng các *annotations* trên một *class* C tương ứng với một *annotation type* đã cho, sẽ được kế thừa bởi các subclasses của C.


#### *6.4.4, @Override*

Annotation type *java.lang.annotation.Override* hỗ trợ phát hiện các vấn đề phát sinh khi override một *method declaration*.  

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


#### *6.4.5, @SuppressWarnings*

Các Java compilers ngày càng có khả năng đưa ra các warnings hữu ích "lint-like".

Annotation type *SuppressWarnings* hỗ trợ kiểm soát các warnings đưa ra bởi Java compiler. Nó chứa một single element là một array of String.  

Nếu một declaration được chú thích với annotation @SuppressWarnings(value = {S1, ..., Sk}), thì Java compiler không được phép đưa ra bất kỳ warning nào được xác định bởi một trong S1 ... Sk nếu warning đó đã được tạo ra là kết quả của declaration được chú thích hoặc bất kỳ phần nào của nó.

Các Unchecked warnings được xác định bởi string "unchecked".  


#### *6.4.6, @Deprecated*

Annotation *@Deprecated* được sử dụng để chú thích cho một *program element* không được khuyến khích sử dụng, thường bởi vì nó nguy hiểm, hoặc tồn tại một element thay thế tốt hơn.

Java compiler phải đưa ra một warning phản đối khi một *type, method, field, or constructor* có declaration được chú thích với @Deprecated được sử dụng (overridden, invoked, or referenced by name) trong một cấu trúc được khai báo tường minh hay ngầm định, trừ khi:

- Sử dụng trong một entity mà bản thân nó được chú thích với annotation @Deprecated; hoặc  
- Sử dụng trong một entity mà đực chú thích để loại bỏ warning với annotation @SuppressWarnings("deprecation"); or  
- Cả sử dụng và declaration đều bên trong outermost class.  

Sử dụng @Deprecated annotation trên một *local variable declaration* hoặc trên một *parameter declaration* không có tác dụng.


#### *6.4.7, @SafeVarargs*

Một *variable arity parameter* (tham số hiếm bất định) với một *non-reifiable element type* có thể gây *heap pollution* và gây ra các compile-time unchecked warnings. Nếu body của variable arity method là well-behaved (an toàn) đối với variable arity parameter, thì các warnings như vậy là uninformative (không có thông tin cần thiết).

Annotation type *java.lang.annotation.SafeVarargs*, được sử dụng để chú thích một *method or constructor declaration*, ngăn Java compiler đưa ra các unchecked warnings cho declaration hoặc invocation của một variable arity method or constructor, nếu không compiler sẽ làm vậy vì variable arity parameter có non-reifiable element type.

Sẽ gây ra compile-time error nếu:

- Một fixed arity method or constructor declaration được chú thích với annotation @SafeVarargs.  
- Một variable arity method declaration không phải static hoặc final được chú thích với annotation @SafeVarargs.  

```java
public static <T> boolean addAll(Collection<? super T> c, T... elements)
// The variable arity parameter has declared type T[], which is non-reifiable.
```


#### *6.4.8, @Repeatable*

Annotation type *java.lang.annotation.Repeatable* được sử dụng trên declaration của một *repeatable annotation type* để chỉ ra *containing annotation type* của nó.  


#### *6.4.9, @FunctionalInterface*

Annotation type *java.lang.annotation.FunctionalInterface* được sử dụng để chỉ ra rằng một *interface* là một *functional interface*. Nó hỗ trợ phát hiện các method declarations không phù hợp xuất hiện trong một interface hoặc được thừa kế bởi một interface mà là *functional*.  


## 7. Annotations

Một *annotation* là một *marker* liên kết thông tin với một cấu trúc chương trình, nhưng không có hiệu lực tại runtime. Một annotation biểu thị một *invocation* cụ thể của một *annotation type* và thường cung cấp các values cho các elements của type đó.


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
    @ TypeName

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

// EXAMPLE 2:
@interface Endorsers {
    String[] value();
}

@Endorsers({"Children", "Unscrupulous dentists"})
public class Lollipop { ... }

@Endorsers("Epicurus") // Cặp ngoặc {} có thể được lược bỏ
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

Một *declaration annotation* là một annotation áp dụng cho một *declaration*, and type của nó có thể áp dụng trong declaration context được đại diện bởi declaration đó.

Một *type annotation* là một annotation áp dụng cho một *type* (hoặc bất kỳ phần nào của một type), và type của nó có thể được áp dụng trong type contexts.

*Ví dụ*:

```java
@Target(ElementType.TYPE_USE)
@interface Foo {}

// CASE 1:
class Test {
    class A {
        static class B {} // compile-time error
    }

    // it is not possible to write A.this in the body of B, 
    // as B has no lexically enclosing instances. 
    // Therefore, it is not possible to apply @Foo to A in the type A.B,
    // because A is logically just a name, not a type.
    @Foo A.B x;  // Illegal 
}

// CASE 2:
class Test {
    static class C {
        class D {}
    }

    // it is possible to write C.this in the body of D.
    // Therefore, it is possible to apply @Foo to C in the type C.D,
    // because C represents the type of some object at run time.
    @Foo C.D x;  // Legal 
}
```