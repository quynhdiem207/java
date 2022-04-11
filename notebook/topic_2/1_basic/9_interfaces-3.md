# Chapter 9: Interfaces

## 8. Functional Interfaces

Một *functional interface* là một interface mà chỉ có một *abstract method* (ngoài các methods của Object). "Single" method này có thể ở dạng nhiều abstract methods với override-equivalent signatures được thừa kế từ các superinterfaces, trong trường hợp này các methods được kế thừa đại diện cho một method duy nhất.

Đối với interface I, gọi M là tập các abstract methods là các members của I không cùng signature với bất kỳ *public instance method* nào của class *Object*. Thì, I là một *functional interface* nếu tồn tại một method m trong M thỏa mãn 2 điều kiện sau:

- Signature của m là một subsignature của mọi signature của method trong M.  
- m là return-type-substitutable cho mọi method trong M.  

Để tạo một *interface instance*, ngoài cách thông thường là khai báo và khởi tạo một class, các instances của một *functional interface* có thể được tạo bằng các *method reference expressions* và các *lambda expressions*.

*Ví dụ 1. Functional Interfaces*

```java
// a functional interface.
interface Runnable {
    void run();
}

// This interface is "NOT" a functional interface.
interface NonFunc {
    boolean equals(Object obj); // equals is a public instance method of Object
}

// However, its subinterface can be functional by declaring an abstract method,
// which is not a member of Object.
interface Func extends NonFunc {
    int compare(String o1, String o2);
}

// Similarly, the interface java.util.Comparator<T> is functional,
// because it has one abstract non-Object method:
interface Comparator<T> {
    boolean equals(Object obj);
    int compare(T o1, T o2);
}

// This interface is "NOT" functional,
// because it declares two abstract methods which are not public members of Object:
interface Foo {
    int m();
    Object clone(); // clone is a protected instance method of Object
}
```

*Ví dụ 2. Functional Interfaces and Erasure*

```java
//  Z is a functional interface,
// because while it inherits two abstract methods which are not members of Object,
// they have the same signature, so the inherited methods logically represent a single method
interface X { int m(Iterable<String> arg); }
interface Y { int m(Iterable<String> arg); }
interface Z extends X, Y {}

//  Z is a functional interface, 
// because Y.m is a subsignature of X.m and is return-type-substitutable for X.m:
interface X { Iterable m(Iterable<String> arg); }
interface Y { Iterable<String> m(Iterable arg); }
interface Z extends X, Y {}

// Interface cannot have two members which are not subsignatures of each other, but have the same erasure.
// Thus, in the following three interface hierarchies where Z causes a compile-time error,
// Z is "NOT" a functional interface,
// because none of its abstract members are subsignatures of all other abstract members
interface X { int m(Iterable<String> arg); }
interface Y { int m(Iterable<Integer> arg); }
interface Z extends X, Y {} // compile-time error

interface X { int m(Iterable<String> arg, Class c); }
interface Y { int m(Iterable arg, Class<?> c); }
interface Z extends X, Y {} // compile-time error

interface X<T> { void m(T arg); }
interface Y<T> { void m(T arg); }
interface Z<A, B> extends X<A>, Y<B> {} // compile-time error

// Similarly, Interface may only have methods with override-equivalent signatures,
// if one is return-type-substitutable for all the others.
// Thus, in the following interface hierarchy where Z causes a compile-time error,
// Z is "NOT" a functional interface,
// because none of its abstract members are return-type-substitutable for all other abstract members
interface X { long m(); }
interface Y { int  m(); }
interface Z extends X, Y {}

// The declarations of Foo<T,N> and Bar are legal,
// in each, the methods called m are not subsignatures of each other, but do have different erasures.
// Still, the fact that the methods in each are not subsignatures,
// means Foo<T,N> and Bar are not functional interfaces.
// However, Baz is "a functional interface",
// because the methods it inherits from Foo<Integer,Integer> have the same signature,
// and so logically represent a single method.
interface Foo<T, N extends Number> {
    void m(T arg);
    void m(N arg);
}
 
interface Bar extends Foo<String, Integer> {}  // NOT Functional
interface Baz extends Foo<Integer, Integer> {} // Functional

// generic methods
interface Exec { <T> T execute(Action<T> a); } // Functional

interface X { <T> T execute(Action<T> a); }
interface Y { <S> S execute(Action<S> a); }
interface Exec extends X, Y {}
    // Functional: signatures are logically "the same"

interface X { <T>   T execute(Action<T> a); }
interface Y { <S,T> S execute(Action<S> a); }
interface Exec extends X, Y {}
    // Error: different signatures, same erasure
```

*Ví dụ 3. Generic Functional Interfaces*

```java
// Functional<S,T> is a functional interface,
// I.m is return-type-substitutable for J.m and K.m,
// but the functional interface type Functional<String,Integer> clearly 
// cannot be implemented with a single method. 
// However, other parameterizations of Functional<S,T> which are functional interface types are possible.
interface I    { Object m(Class c); }
interface J<S> { S m(Class<?> c); }
interface K<T> { T m(Class<?> c); }
interface Functional<S,T> extends I, J<S>, K<T> {} // Functional
```

Có 4 loại *functional interface*, bao gồm:  

- Type của một *non-generic* functional interface,  
- Một *parameterized type* mà là một tham số hóa của một generic functional interface,  
- *Raw type* của một generic functional interface,  
- Một *intersection type* bao gồm một notional functional interface


## 9. Function Types

*Function type* của một *functional interface* I là một *method type* mà có thể được sử dụng để override abstract method(s) của I.

Gọi M là tập các *abstract methods* được định nghĩa cho I. *Function type* của I bao gồm:

- Các *type parameters*, *formal parameters*, và *return type*:
    Gọi m là một method trong M với:  

    + Một signature là một subsignature của mọi signature của method trong M; và  
    + Một return type là một subtype của mọi return type của method trong M (sau khi điều chỉnh phù hợp với bất kỳ type parameters nào).  

    Nếu không có method nào như vậy tồn tại, thì m là một method trong M sao cho:  

    + Có một signature là một subsignature của mọi signature của method trong M; và  
    + Là return-type-substitutable cho mọi method trong M.  

    Các type parameters, formal parameter types, và return type của function type đã cho bởi m.  

- *throws clause*:

    throws clause của function type được trích xuất từ các throws clauses của các methods trong M.  
    
    + Nếu function type là generic, những mệnh đề này trước tiên sẽ được điều chỉnh cho phù hợp với các type parameters của function type.  
    + Nếu function type không phải generic, nhưng ít nhất một method trong M là generic, thì những mệnh đề này trước tiên sẽ bị xóa (erased).  
    
    Sau đó, throws clause của function type bao gồm mọi type E thỏa mãn các ràng buộc sau:  

    + E được đề cập trong một trong các throws clauses.  
    + Đối với mỗi throws clause, E là một subtype của một số type được đặt tên trong mệnh đề đó.  

*Function type* của một *functional interface type* được xác định như sau:  

- Function type của một *non-generic* functional interface type I đơn giản chính là function type của functional interface I, như đã định nghĩa ở trên.  
- Function type của một *parameterized* functional interface type [I<A1...An>], trong đó A1...An là các types và các type parameters tương ứng của I là P1...Pn, được suy ra bằng cách áp dụng phép thay thế [P1:=A1, ..., Pn:=An] cho function type của generic functional interface [I<P1...Pn>].  
- Function type của một *parameterized* functional interface type [I<A1...An>], sao cho một hoặc nhiều A1...An là wildcard, là function type của non-wildcard parameterization của I, [I<T1...Tn>]. Non-wildcard parameterization được xác định như sau:  
    Gọi P1...Pn là các type parameters của I với các ràng buộc tương ứng B1...Bn. Với mọi i (1 ≤ i ≤ n), Ti được suy ra theo form của Ai:  

    + Nếu Ai là một type, thì Ti = Ai.  
    + Nếu Ai là một wildcard, và ràng buộc của type parameter tương ứng, Bi, đề cập đến một trong P1...Pn, thì Ti là undefined và không có function type.  
    + Nếu không thì:  
        + Nếu Ai là một unbound wildcard ?, thì Ti = Bi.  
        + Nếu Ai là một upper-bounded wildcard ? extends Ui, thì Ti = glb(Ui, Bi).  
        + Nếu Ai là một lower-bounded wildcard ? super Li, thì Ti = Li.  
- Function type của *raw type* của một generic functional interface I<...> là erasure của function type của generic functional interface I<...>.  
- Function type của một *intersection type* chứa một notional functional interface là function type của notional functional interface.  

*Ví dụ 1. Function Types*

```java
interface X { void m() throws IOException; }
interface Y { void m() throws EOFException; }
interface Z { void m() throws ClassNotFoundException; }

// function type of functional interface XY is:
// ()->void throws EOFException
// because EOFException is mentioned in one of the throws clauses,
// and is a subtype of some types in all throws clauses
interface XY extends X, Y {}

// function type of functional interface XYZ is:
// ()->void
// because there is not a type is mentioned in one of the throws clause,
// and is a subtype of some types in all throws clauses.
interface XYZ extends X, Y, Z {}
```

*Ví dụ 2. Function Types*

```java
interface A {
    List<String> foo(List<String> arg)
      throws IOException, SQLTransientException;
}
interface B {
    List foo(List<String> arg)
      throws EOFException, SQLException, TimeoutException;
}
interface C {
    List foo(List arg) throws Exception;
}

// function type of functional interface D is:
// (List<String>)->List<String> throws EOFException, SQLTransientException
interface D extends A, B {}

// function type of functional interface E is:
// (List)->List throws EOFException, SQLTransientException
interface E extends A, B, C {}
```

*Ví dụ 3. Generic Function Types*

```java
interface G1 {
    <E extends Exception> Object m() throws E;
}
interface G2 {
    <F extends Exception> String m() throws Exception;
}

// function type of functional interface G is:
// <F extends Exception> ()->String throws F
interface G extends G1, G2 {}
```

**Note**: Một *generic function type* cho một *functional interface* có thể được triển khai bằng một *method reference expression*, nhưng không phải bằng một *lambda expression*, vì không có cua pháp generic lambda expressions.