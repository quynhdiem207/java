## 3. Type Variables

*Type variable* là một unqualified identifier được sử dụng như là một type trong class, interface, method, và constructor.  

Một *type variable* được giới thiệu bởi khai báo của một *type parameter* của một *generic* class, interface, method, hoặc constructor.  

Syntax of *type parameter declaration*:  

```
{TypeParameterModifier} TypeIdentifier [TypeBound]
```

Trong đó:  

- TypeParameterModifier: Annotation  
- TypeBound:  
    + extends TypeVariable  
    + extends ClassOrInterfaceType {AdditionalBound}  
- AdditionalBound: & InterfaceType  

*Ví dụ: type variable T có ràng buộc C & I: <T extends C & I>.*  

```java
<T extends C & I> void test(T t) {}
```

Scope của một *type variable* được khai báo dưới dạng một *type parameter* là toàn bộ phần declaration của generic class, interface, method, và constructor mà nó được giới thiệu.  

Mọi *type variable* được khai báo dưới dạng một *type parameter* đều có một ràng buộc (bound):  

- Nếu không có ràng buộc nào được khai báo cho một type variable, thì sẽ giả định là *Object*.  
- Nếu có một ràng buộc được khai báo, nó sẽ là một trong các type sau:  
    + Một type variable T riêng lẻ,  
    + Một class or interface type T có thể được theo sau bởi các interface types I1 & ... & In.  
        *Note*: Sẽ xảy ra compile time error nếu bất kỳ type nào trong I1 ... In là một class type or type variable.  
    
    *Note*: erasures của tất cả các types của một ràng buộc phải khác nhau, neus không sẽ xảy ra compile time error.  

Các members của một type variable X với ràng buộc T & I1 &...& In là các members của intersection type T & I1 &...& In.  

*Ví dụ: type variable 'T' có các members giống với intersection type 'C & I', là type mà có các members giống với empty class 'CT'.*    

```java
package TypeVarMembers;

class C { 
    public void mCPublic() {}
    protected void mCProtected() {} 
    void mCPackage() {}
    private void mCPrivate() {} 
} 

interface I {
    void mI();
}

class CT extends C implements I {
    public void mI() {}
}

class Test {
    static <T extends C & I> void test(T t) { 	
        t.mI();           // OK
        t.mCPublic();     // OK 
        t.mCProtected();  // OK 
        t.mCPackage();    // OK
        t.mCPrivate();    // Compile-time error
    }

    public static void main(String[] args) {
        CT ct = new CT();
        test(ct);
    }
}
```


## 4. Parameterized Types

Một *class or interface declaration* là *generic* định nghĩa một tập các *parameterized types*.

*Ví dụ: List<T> là một generic type, định nghĩa một tập các parameterized types gồm: List<String>, List<Object>, ect.* 

Một *parameterized type* là một class or interface type có dạng:  

```
C<T1,...,Tn> 

Trong đó:
    - C là tên của generic type,  
    - <T1,...,Tn> là danh sách các type arguments biểu thị một tham số hóa cụ thể của generic type.  
```

Hai *parameterized types* khác nhau rõ ràng nếu thỏa mãn một trong hai điều kiện sau:

- Chúng là các tham số hóa của các *generic type declaration* riêng biệt,  
    ```
    *vd: List<String> và ArrayList<String>*  
    ```  
- Bất kỳ *type arguments* nào của chúng đều khác nhau rõ ràng.  
    ```
    *vd: List<String> và List<Object>*  
    ``` 

*Ví dụ: Seq<int> là không hợp lệ, vì primitive type không thể là type arguments.*  

**Note**: Một *parameterized type* có thể là một tham số hóa của *nested generic class or interface*.  

*Ví dụ: nếu một non-generic class C có một generic class member D<T>, thì 'C.D<Object>' là một parameterized type. Và nếu một generic class C<T> có một non-generic class member D, thì member type 'C<String>.D' là một parameterized type.*  


### 4.1, Type Arguments of Parameterized Types  

*Type arguments* có thể là *reference types* hoặc *wildcards*.  

```
- TypeArguments:     < TypeArgumentList >
- TypeArgumentList:  TypeArgument {, TypeArgument}

- TypeArgument:
    + ReferenceType
    + Wildcard

- Wildcard: {Annotation} ? [WildcardBounds]
    + WildcardBounds:
        + extends ReferenceType
        + super ReferenceType
```

*Wildcard* đại diện cho *unknown* type, có 3 dạng:  

- *Unbound wildcard* có dạng **?**,  
    *VD: List<?> đại diện cho một list của unknown type.*  

- *Upper bound wildcard* có dạng **? extends B** trong đó B là upper bound,  
    *VD: List<? extends Number> đại diện cho một list của Number và các subtypes của nó như Integer, Float, etc.*  

- *Lower bound wildcard* có dạng **? super B** trong đó B là lower bound.  
    *VD: List<? super Integer> đại diện cho một list của Integer và các supertypes của nó như Number, Object.*  

Wildcard **? extends Object** tương đương với unbound wildcard **?**.  

Hai *type arguments* được cho là khác nhau rõ ràng nếu thỏa mãn một trong các điều sau:  

- Cả 2 arguments đều không phải type variable or wildcard, và không cùng type,  
- Một argument là type variable or wildcard với upper bound S, argument T còn lại không phải type variable or wildcard, và không thỏa mãn |S| <: |T| và |T| <: |S|,  
- Cả 2 argument là type variable or wildcard với upper bound tương ứng là S và T, Và không thỏa mãn |S| <: |T| và |T| <: |S|.  

Type argument T1 được cho là chứa type argument T2 ( viết là **T2 <= T1**) nếu một tập các types ký hiệu là T2 là một tập con của tập các types kí hiệu là T1:  

- ? extends T <= ? extends S, if T <: S  
- ? extends T <= ?  
- ? super T <= ? super S, if S <: T  
- ? super T <= ?  
- ? super T <= ? extends Object  
- T <= T  
- T <= ? extends T  
- T <= ? super T  

*Ví dụ: Unbounded Wildcards*  

```java
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.ArrayList;

class Test {
    // element type of an array is parameterized by wildcard
    public Method getMethod(Class<?>[] parameterTypes) { ... }

    // a wildcard collection
    static void printCollection(Collection<?> c) {
        for (Object o : c) {
            System.out.println(o);
        }
    }

    public static void main(String[] args) {
        Collection<String> cs = new ArrayList<String>();
        cs.add("hello");
        cs.add("world");
        printCollection(cs);
    }
}
```

*Ví dụ: Bounded Wildcards*  

```java
boolean addAll(Collection<? extends E> c) { ... }

<T> boolean addAll(Collection<T> c) { ... }

void Reference(T referent, ReferenceQueue<? super T> queue) { ... }
```


### 4.2, Members and Constructors of Parameterized Types

Gọi C là một *generic class or interface declaration* với *type parameters* A1,...,An, and đặt C<T1,...,Tn> là một tham số hóa của C, sao cho với 1 ≤ i ≤ n, Ti là một type (không phải một wildcard). Thì:  

```
- Gọi m là một member or constructor được khai báo trong C, có type là T,  
  Thì type của m trong C<T1,...,Tn> là T[A1:=T1,...,An:=Tn].  

- Gọi m là một member or constructor được khai báo trong D, 
  trong đó D là một class được extend bởi C or một interface được implement bởi C. 
  Gọi D<U1,...,Uk> là supertype of C<T1,...,Tn> tương ứng với D.  
  Thì type của m trong C<T1,...,Tn> là D<U1,...,Uk>.  
```

Nếu bất kỳ type arguments trong tham số hóa của C là wildcard, thì:  

```
- Types của fields, methods, and constructors trong C<T1,...,Tn> là types của fields, methods, and constructors trong *capture conversion* của C<T1,...,Tn>.  
- Gọi D là một class or interface declaration (có thể là generic) trong C. 
  Khi đó type của D trong C<T1,...,Tn> là D sao cho, 
  nếu D là generic, tất cả type arguments là unbounded wildcards.  
```

Một static member được khai báo trong generic type declaration phải được tham chiếu đến bằng cách sử dụng non-generic type tương ứng với generic type, nếu không sẽ xảy ra compile-time error. Nói cách khác, Sử dụng một parameterized type để tham chiếu đến static member được khai báo trong generic type declaration là không hợp lệ.  


## 5. Type Erasure

*Type Erasure* là một mapping (ánh xạ) từ type (có thể gồm parameterized types và type variables) sang type (không bao gồm parameterized types và type variables).  

Erasure của type T được ký hiệu: **|T|**.  

Erasure mapping được định nghĩa như sau:  

```
- Erasure của một parameterized type G<T1,...,Tn> là |G|.  
- Erasure của một nested type T.C là |T|.C.  
- Erasure của một array type T[] là |T|[].  
- Erasure của một type variable là erasure của leftmost bound của nó.  
- Erasure của every other type là chính type đó.  
```

*Type Erasure* cũng map signature của một method hoặc constructor sang một signature không có parameterized types or type variables. Erasure của một method hoặc constructor signature s là một signature bao gồm tên giống với s và erasures của tất cả các formal parameter types được đưa ra trong s.  

Return type của một method và các type parameters của một generic method or constructor cũng bị xóa nếu signature của method or constructor bị xóa.  

Erasure của signature của một generic method sẽ không có type parameters.


## 6. Reifiable Types

Bởi vì một vài type information bị xóa (erased), không phải tất cả các types đều available at runtime. Types mà hoàn toàn available tại runtime được gọi là *reifiable types*.

Một type là *reifiable* chỉ khi thỏa mãn một trong các điều sau:

- Nó đề cập đến một *non-generic* class or interface type declaration.  
- Nó là một *parameterized type*, trong đó tất cả các type arguments là unbounded wildcards.  
- Nó là một *raw type*.  
- Nó là một *primitive type*.  
- Nó là một *array type* có element type là reifiable.  
- Nó là một *nested type*, trong đó với mỗi type T được phân cách bởi dấu "." là reifiable.  
    ```
    Ví dụ: Nếu generic class X<T> có một generic member class Y<U>, thì:
    - Type X<?>.Y<?> là reifiable, vì X<?> là reifiable và Y<?> là reifiable. 
    - Type X<?>.Y<Object> không phải reifiable vì Y<Object> không phải reifiable.
    ```

Một *intersection type* không phải *reifiable*.


## 7. Raw Types

*Raw type* là một trong những:  

- *Reference type* mà được tạo thành bởi lấy tên của *generic type declaration* mà không có *type argument list*.  
- *Array type* có element type là raw type.  
- *Non-static member type* của một *raw type* R mà không phải được kế thừa từ superclass or superinterface của R.  

Non-generic class or interface type không phải một raw type.

```java
class Outer<T>{
    T t;

    class Inner {
        T s;
        T setOuterT(T t1) { t = t1; return t; }
    }
}

public class Test {
    public static void main(String[] args) {
		Outer.Inner x = null;         // OK
        x = new Outer().new Inner();
        x.s = "Hi";
        
		Outer<Number>.Inner y = null; // OK
        y = new Outer().new Inner();
        y.s = 2;
		
        System.out.println(x.s + " - " + y.s);
        
		Outer a = new Outer();
        Outer<Double>.Inner z = a.new Inner();
        z.setOuterT(3.0);
        System.out.println(a.t); // 3.0
	}
}
```

Superclasses or superinterfaces của một raw type là erasures của superclasses or superinterfaces của bất kỳ tham số hóa nào của generic type.

Type của một constructor, instance method, or non-static field của một raw type C mà không phải được thừa kế từ superclasses or superinterfaces của nó là raw type tương ứng với erasure của type của nó trong generic declaration tương ứng với C.

Type của một static method or static field của một raw type C chính là type của nó trong generic declaration tương ứng với C.

**Note**:  

- Khi truyền type arguments cho một non-static type member của một raw type mà không phải được thừa kế từ superclasses or superinterfaces của nó, sẽ xảy ra compile-time error.  
- Khi sử dụng một type member của một parameterized type như một raw type, sẽ xảy ra compile-time error.  

```java
class Outer<T>{
    class Inner<S> {
        S s;
    }
}

public class Test {
    public static void main(String[] args) {
		Outer.Inner x = null;                  // OK
        x = new Outer().new Inner();
        x.s = "Hi";

		Outer<Number>.Inner<String> y = null;  // OK
        y = new Outer().new Inner();
        y.s = "Hello";
        
        System.out.println(x.s + " - " + y.s); // Hi - Hello

        Outer.Inner<Double> x = null;          // ERROR
        Outer<Double>.Inner x = null;          // ERROR
	}
}
```

**Note**: Supertype của một class có thể là một raw type.

*Ví dụ 1: Raw Types*

```java
class Cell<E> {
    E value;

    Cell(E v)     { value = v; }
    E get()       { return value; }
    void set(E v) { value = v; }

    public static void main(String[] args) {
        Cell x = new Cell<String>("abc");
        System.out.println(x.value);  // abc -> OK, has type Object
        System.out.println(x.get());  // abc -> OK, has type Object
        x.set("def");                 // unchecked warning
        x.set(2);                     // unchecked warning

        Cell<String> x = new Cell<String>("abc");
        x.set(2);                     // ERROR
    }
}
```

*Ví dụ 2: Raw Types and Inheritance*

```java
import java.util.*;

class NonGeneric {
    Collection<Number> myNumbers() { return null; }
}

abstract class RawMembers<T> extends NonGeneric implements Collection<String> {
    static Collection<NonGeneric> cng = new ArrayList<NonGeneric>();

    public static void main(String[] args) {
        RawMembers rw = null;

        // RawMembers inherits myNumbers() from the NonGeneric class whose erasure is also NonGeneric.
        // Return type of myNumbers() in RawMembers is not erased, 
        // and assign rw.myNumbers() to Collection<Number> requires no unchecked conversion
        Collection<Number> cn = rw.myNumbers();      // OK

        // RawMembers<T> inherits method Iterator<String> iterator() from Collection<String> superinterface.
        // Raw type RawMembers inherits iterator() from Collection, the erasure of Collection<String>,
        // which means that the return type of iterator() in RawMembers is Iterator
        // As a result, assign rw.iterator() to Iterator<String> requires an unchecked conversion
        Iterator<String> is = rw.iterator();         // Unchecked warning

        // static member cng retains its parameterized type
        Collection<NonGeneric> cnn = RawMembers.cng; // OK, static member
    }
}
```

**Note**: Các member của raw type sẽ không bị xóa gồm: static member, và member được thừa kế từ non-generic supertype.  


## 8. Intersection Types

Một *intersection type* có dạng *T1 &...& Tn* (n > 0), trong đó Ti (1 ≤ i ≤ n) là các types.

Values của một intersection type là những objects mà là values của tất cả các types Ti với 1 ≤ i ≤ n.

Mỗi intersection type T1 &...& Tn tạo ra một class or interface danh nghĩa nhằm xác định các members của intersection type.  

Các members của một intersection type *T1 &...& Tn* (n > 0) trong đó Ti (1 ≤ i ≤ n) là tất cả các members của các class or interface Ti.  


## 9. Subtyping 

Các *supertypes* của một type được xác định bằng cách bắc cầu thông qua quan hệ *direct supertype*.  

- **T <: S** biểu thị T là *subtype* của S,  
- **T <1 S** biểu thị T là *direct subtype* của S,  
- **T < S** biểu thị T là *proper subtype* của S, nếu T <: S và S ≠ T.    

*Note*: Đối với parameterized type: T <: S không có nghĩa là `C<T> <: C<S>`.  

**Note**: Quan hệ *subtype* khác với quan hệ *subclass*.


### 9.1, Subtyping among Primitive Types

Các quy tắc sau định nghĩa quan hệ *direct supertype* giữa các *primitive types*:  

- double >1 float  
- float >1 long  
- long >1 int  
- int >1 char  
- int >1 short  
- short >1 byte  


### 9.2, Subtyping among Class and Interface Types

Cho một *non-generic type* declaration C, *direct supertypes* của C gồm:  

```
- Direct superclass của C.
- Direct superinterfaces của C.
- Type Object, nếu C là interface type không có direct superinterfaces.
```

Cho một *generic type* declaration C<F1,...,Fn> (n > 0), *direct supertypes* của *raw type* C gồm:

```
- Direct superclass của the raw type C.
- Direct superinterfaces của the raw type C.
- Type Object, nếu C<F1,...,Fn> là một generic interface type không có direct superinterfaces.
```

Cho một *generic type* declaration C<F1,...,Fn> (n > 0), *direct supertypes* của the *generic type* C<F1,...,Fn> gồm:

```
- Direct superclass của C<F1,...,Fn>.
- Direct superinterfaces của C<F1,...,Fn>.
- Type Object, nếu C<F1,...,Fn> là một generic interface type không có direct superinterfaces.
- Raw type C.
```

Cho một *generic type* declaration C<F1,...,Fn> (n > 0), *direct supertypes* của *parameterized type* C<T1,...,Tn>, với mọi Ti (1 ≤ i ≤ n), gồm:

```
- D<U1 θ,...,Uk θ>, trong đó D<U1,...,Uk> là một generic type mà là direct supertype của generic type C<T1,...,Tn> và θ là phép thay thế [F1:=T1,...,Fn:=Tn].
- C<S1,...,Sn>, trong đó Si chứa Ti (1 ≤ i ≤ n).
- Type Object, nếu C<F1,...,Fn> là một generic interface type không có direct superinterfaces.
- Raw type C.
```

Cho một *generic type* declaration C<F1,...,Fn> (n > 0), *direct supertypes* của *parameterized type* C<R1,...,Rn> với ít nhất một trong các Ri (1 ≤ i ≤ n) là *wildcard*, là direct supertypes của parameterized type C<X1,...,Xn> (kết quả của *capture conversion* sang C<R1,...,Rn>).

*Direct supertypes* của một *intersection type* T1 &...& Tn là Ti (1 ≤ i ≤ n).

*Direct supertypes* của một *type variable* là các types được liệt kê trong ràng buộc của nó.

Một *type variable* là một *direct supertype* của lower bound của nó.

*Direct supertypes* của *null type* là tất cả các reference types khác với null.


### 9.3, Subtyping among Array Types

Các quy tắc sau định nghĩa quan hệ *direct supertype* giữa các *array types*:  

- Nếu S và T đều là reference types, thì S[] >1 T[] chỉ khi S >1 T.  
- Object >1 Object[]  
- Cloneable >1 Object[]  
- java.io.Serializable >1 Object[]  
- Nếu P là primitive type, thì:  
    + Object >1 P[]  
    + Cloneable >1 P[]  
    + java.io.Serializable >1 P[]  