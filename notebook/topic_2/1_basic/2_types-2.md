## 3. Type Variables

*Type variable* là một identifier không đủ tiêu chuẩn được sử dụng như là một type trong class, interface, method, và constructor.  

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
        + AdditionalBound: & InterfaceType  

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

Các members của một type variable X với ràng buộc T & I1 & ... & In là các members của intersection type T & I1 & ... & In.  

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
    *vd: List<String> và ArrayList<String>*  
- Bất kỳ *type arguments* nào của chúng đều khác nhau rõ ràng.  
    *vd: List<String> và List<Object>*  

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

*Wildcard* có thể được cung cấp ràng buộc một cách tường minh:  

- Một *upper bound* được biểu thị với syntax: **? extends B** trong đó B là upper bound,  
- Một *lower bound* được biểu thị với syntax: **? super B** trong đó B là lower bound.  

*Wildcard* **? extends Object** tương đương với *unbound wildcard* **?**.  

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
<E> boolean addAll(Collection<? extends E> c) { ... }

<T> boolean addAll(Collection<T> c) { ... }

<T> void Reference(T referent, ReferenceQueue<? super T> queue) { ... }
```


### 4.2, Members and Constructors of Parameterized Types

Gọi C là một *generic class or interface declaration* với *type parameters* A1,...,An, and đặt C<T1,...,Tn> là một tham số hóa của C, sao cho với 1 ≤ i ≤ n, Ti là một type (không phải một wildcard). Thì:  

- Gọi m là một member or constructor được khai báo trong C, có type là T,  
    Thì type của m trong C<T1,...,Tn> là T[A1:=T1,...,An:=Tn].  
- Gọi m là một member or constructor được khai báo trong D, trong đó D là một class được extend bởi C or một interface được implement bởi C.  
    Gọi D<U1,...,Uk> là supertype of C<T1,...,Tn> tương ứng với D.  
    Thì type của m trong C<T1,...,Tn> là D<U1,...,Uk>.  

Nếu bất kỳ type arguments trong tham số hóa của C là wildcard, thì:  

- Types của fields, methods, and constructors trong C<T1,...,Tn> là types của fields, methods, and constructors trong *capture conversion* của C<T1,...,Tn>.  
- Gọi D là một class or interface declaration (có thể là generic) trong C. Khi đó type của D trong C<T1,...,Tn> là D sao cho, nếu D là generic, tất cả type arguments là unbounded wildcards.  

Một static member được khai báo trong generic type declaration phải được tham chiếu đến bằng cách sử dụng non-generic type tương ứng với generic type, nếu không sẽ xảy ra compile-time error. Nói cách khác, Sử dụng một parameterized type để tham chiếu đến static member được khai báo trong generic type declaration là không hợp lệ.  


## 5. Type Erasure

*Type Erasure* là một mapping (ánh xạ) từ type (có thể gồm parameterized types & type variables) sang type (không bao gồm parameterized types & type variables).  

Erasure của type T được ký hiệu: **|T|**.  

Erasure mapping được định nghĩa như sau:  

- The erasure của một *parameterized type* G<T1,...,Tn> là |G|.  
- The erasure của một *nested type* T.C là |T|.C.  
- The erasure của một *array type* T[] là |T|[].  
- The erasure của một *type variable* là the erasure của ràng buộc tận cùng bên trái (leftmost bound) của nó.  
- The erasure của *every other type* là chính type đó.  

*Type Erasure* cũng ánh xạ signature của một method hoặc constructor với một signature không có parameterized types or type variables. The erasure của một method hoặc constructor signature s là một signature bao gồm tên giống với s và erasures của tất cả các parameter types chính thức được đưa ra trong s.  

Return type của một method và các type parameters của một generic method or constructor cũng bị xóa nếu signature của method or constructor bị xóa.  

The erasure của signature của một generic method không có type parameters.


## 6. Subtyping 

- T là *subtype* của S          (T <: S),  
- T là *direct subtype* của S   (T <1 S),  
- T là *proper subtype* của S   (T < S),  