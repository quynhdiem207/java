/*
 * (C) Copyright 2022
 * 
 * @author diem_quynh
 * @date Mar 18, 2022
 * @version 1.0
 */

package example;

import java.util.*;

class ClassA<T> {
	T a;
}

class ClassB<T> extends ClassA<T> {}

class A {}

class B extends A {}

public class GenericSuper {

	public static void main(String[] args) {
		ClassB<String> strB = new ClassB<String>();
		ClassA<String> strA = strB;
		
		System.out.println(strB.a);
		
		Outer.Inner x = null;
		x = new Outer().new Inner();
		x.s = "Hi";
		
		Outer<Number>.Inner y = null;
		y = new Outer().new Inner();
		y.s = 2;
		
		System.out.println(x.s + " - " + y.s);
		
		Outer a = new Outer();
		Outer<Double>.Inner z = a.new Inner();
		z.setOuterT(3.0);
		System.out.println(a.t);
		
		Cell x1 = new Cell<String>("abc");
        System.out.println(x1.value);  // OK, has type Object
        System.out.println(x1.get());  // OK, has type Object
        x1.set("def");                 // unchecked warning
        Object y1 = x1;
        
        RawMembers rw = null;
//        Collection<Number> cn = rw.myNumbers();
                              // OK
//        Iterator<String> is = rw.iterator();
                            // Unchecked warning
        Collection<NonGeneric> cnn = rw.cng;
                                   // OK, static member
	}

}

class Outer<T>{
    T t;
    class Inner {
    	T s;
        T setOuterT(T t1) { t = t1; return t; }
    }
}

class Cell<E> {
    E value;

    Cell(E v)     { value = v; }
    E get()       { return value; }
    void set(E v) { value = v; }
}

class NonGeneric {
    Collection<Number> myNumbers() { return null; }
}

abstract class RawMembers<T> extends NonGeneric
                             implements Collection<String> {
    static Collection<NonGeneric> cng =
        new ArrayList<NonGeneric>();
}
