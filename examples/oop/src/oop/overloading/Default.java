package oop.overloading;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Collection;

interface AB extends AC, AD {
    default int getAge() {return 2;};
}

interface AC {
    int getAge();
    String getName();
}

interface AD {
    default int getAge() { return 11; }
    String getName();
}

//public class Default extends AB implements AC, AD {
//    public int getAge() { return 33; }
//}

//@interface Verboten {
//    String[][] value();
//}

interface Foo<T, N extends Number> {
    void m(T arg);
    void m(N arg);
//    <T>   T execute(Collection<T> a);
//    <S,T> S execute(Collection<S> a);
//    void list(Collection lst);         // compile-time error
//    void list(Collection<String> lst);
}
interface Bar extends Foo<String, Integer> {}
interface Baz extends Foo<Integer, Integer> {}

interface I    { Object m(Class c); }
interface J<S> { S m(Class<?> c); }
interface K<T> { T m(Class<?> c); }
interface Functional<S,T> extends I, J<S>, K<T> {
    Object m(Class c);
}
