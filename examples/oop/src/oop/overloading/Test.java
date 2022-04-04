package oop.overloading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class Super {
    Super() {
        System.out.println("Super");
    }
    static String greeting() { return "Goodnight"; }
    String name() { return "Richard"; }
}

class Sub extends Super {
    String name;
    {
        name = "Diêm";
        System.out.println("Instance initializer");
    }
    Sub() {
        System.out.println("Sub");
    }
    Sub(String name) {
        this.name = name;
        System.out.println("Sub 2");
    }
//    static String greeting() { return "Hello, " +  super.name(); }
    String name() { return super.name() + " Dick"; }
    String sayGoodnight() {
        return super.greeting() + ", " + name();
    }
    void test(int count, String... args) {
        System.out.println(count);
        for (String i: args) {
            System.out.print(i + ", ");
        }
    }
}

interface A {
    Super test();
}

interface B extends A {
    Sub test();
}

class Outer {
    int i = 100;

    static void classMethod() {
        int l = 200;

        // A local class
        // Class declaration của LocalInStaticContext xảy ra trong static context vì nằm trong static method classMethod.
        // class LocalInStaticContext không có enclosing instance.
        class LocalInStaticContext {
            // Instance variables của class Outer không available trong body của static method.
//            int k = i;  // Compile-time error

            // Từ trong inner class có thể tham chiếu đến Local variables của method bao quanh mà không có lỗi miễn chúng là final.
            int m = l;  // OK
        }

        System.out.println(new LocalInStaticContext().m);
    }

    void foo() {
        int l = 400;
        // A local class
        // class declaration của Local không xảy ra trong static context vì nằm trong non-static method foo.
        // class Local có một enclosing instance của class Outer.
        class Local {
            // Instance variables của class Outer là available trong body của non-static method.
            int j = i;
            int m = l;
        }

        System.out.println(new Local().m);
    }
}

abstract class Person{
    abstract void eat();

    abstract void gretting();
}

interface Eatable{
    void eat();
}

enum Season { WINTER, SPRING, SUMMER, FALL }

public class Test {
    int x, y;
//    abstract void move(int dx, int dy);                      // compile-time error
//    void move(int dx, int dy) { x += dx; y += dy; }          // compile-time error
//    int move(int dx, int dy) { x += dx; y += dy; return 1; } // compile-time error
//    void list(Collection lst) {}
    void list(Collection<String> lst) {}
    public static void main(String[] args) {
        Sub sub = new Sub("Hoa");
        sub.test(4, "Hello", "Hi");
        System.out.println(sub.name());
        System.out.println(sub.sayGoodnight());

        for (Season s : Season.values())
            System.out.println(s + "\tindex: " + s.ordinal());

        Outer.classMethod();
        new Outer().foo();

        // 1. A class is created, but its name is decided by the compiler,
        //    which extends the Person class and provides the implementation of the eat() method.
        // 2. An object of the Anonymous class is created that is referred to by 'p,'
        //    a reference variable of Person type.
        Person p = new Person(){
            void eat(){ System.out.println("nice fruits"); }
            class D {
                String greet = "Hello";
            };
            void gretting() {
                System.out.println(new D().greet);
            }
        };
        p.eat();
        p.gretting();

        // 1. A class is created, but its name is decided by the compiler,
        //    which implements the Eatable interface and provides the implementation of the eat() method.
        // 2. An object of the Anonymous class is created that is referred to by 'p',
        //    a reference variable of the Eatable type.
        Eatable e = new Eatable(){
            public void eat(){ System.out.println("nice fruits"); }
        };
        e.eat();
    }
}
