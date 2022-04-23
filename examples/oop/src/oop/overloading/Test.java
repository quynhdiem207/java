package oop.overloading;

import java.awt.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface X { int x = 4; }

class Super {
    int x = 2;
    Super() {
        System.out.println("Super");
    }
    static String greeting() { return "Goodnight"; }
    public String name() { return "Richard"; }
    void sayHello() {}
    class T {}
//    Iterable m(Collection<String> s) {return new ArrayList<>();}
//    Iterable m(Collection<Integer> s) {return new ArrayList<>();}
    Iterable m() {return new ArrayList<>();}
}

class Sub extends Super implements X {
    static float x = 3.0f;
    class TS {}
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
    Iterable<String> m() {return new ArrayList<>();}
    public String name() { return super.name() + " Dick"; }
    static String greeting() { TS t = null; return "Hello..."; }
    String sayGoodnight() {
        return Super.greeting() + ", " + name();
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
    default int getAge() {
        return 11;
    }
    default String name() {return "Diem";}
    Object getInfo(String file) throws Exception;
}

interface B extends A {
    Sub test();
    static int getRandom() {
        return (int) Math.floor(Math.random() * 100);
    }
    default int getAge() {
        return A.super.getAge() * 2;
    }
    String getInfo(String file) throws IOException;
}

class D extends Super implements B {
//    public String name() {return "Hi";}
    public Sub test() {
        return new Sub();
    }
    public int getAge() {
        return B.super.getAge() * 3;
    }
    public String getInfo(String file) throws IOException { return ""; }
}

class Outer extends Super {
    int i = 100;
    int x = 6;

    class Inner {
        Inner(Outer Outer.this) {
            System.out.println(Outer.this.x + "***");
            System.out.println(Outer.super.x + "***");
        }

        class InnerMem {
            void test(InnerMem this) {
                System.out.println(Inner.this.getClass() + "***");
            }
        }
    }

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
        System.out.println(Outer.this.getClass() + " &&& " + this.getClass());
        int l = 400;
        // A local class
        // class declaration của Local không xảy ra trong static context vì nằm trong non-static method foo.
        // class Local có một enclosing instance của class Outer.
        class Local {
            Local(Outer Outer.this) {}

            // Instance variables của class Outer là available trong body của non-static method.
            int j = i;
            int m = l;

            void test(Local this) {
                System.out.println(Outer.this.getClass() + " ---- ");
            }
        }

        System.out.println(new Local().m);
        new Local().test();
    }
}
class Point { int x, y; }
class ColoredPoint extends Point { int color; }
class Test1 {
//    static int test(ColoredPoint p) {
//        return p.color;
//    }
    static String test(Point p) {
        return "Point";
    }
    public static void main1() {
        ColoredPoint cp = new ColoredPoint();
        String s = test(cp);  // compile-time error
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
    void run() {
        try {
            throw new Exception();
        } catch (IOException e) {

        } catch (Exception e) {

        }
//        throw new OutOfMemoryError();

    }
    void list(Collection<String> lst) {}
    public static void main(String[] args) {
        Sub sub = new Sub("Hoa");
        sub.test(4, "Hello", "Hi");
        System.out.println(((Super) sub).greeting());
        System.out.println(sub.greeting());
        System.out.println(sub.name());
        System.out.println(sub.sayGoodnight());
        sub.m();

        Super sup = new Super();
        System.out.println(sup.greeting() + ", " + sup.name());
        sup.m();

        float x = 10f - 0.1f;
        float y = 10f - 0.1f - 0.1f;
        System.out.println(x + " - " + y);
        System.out.println(Math.abs(9.8f - y) <= 1e-6);

        String[] x1 = new String[] {"Hello"};
        System.out.println((x1[0] == "Hello") + "%%%");


//        Number[] a = {new Integer(1), new Integer(2)};
//        Integer[] b = (Integer[]) a; // RUNTIME ERROR

//        int[] a = null;
//        a[0] = 1;
        int ia[][] = { {1, 2}, null };
        System.out.println(ia[0][0]);

        Class<int[]> cl = null;

//        int[] x = {1, 2};
//        Integer[] y = (Integer[]) x;
//        long[] y = new long[x.length];
//        for (int i = 0; i < x.length; i++) {
//            y[i] = x[i];
//        }
//        System.out.println(b[1] + "--------");

        Sub sub2 = sub;
        System.out.println(((Super) sub2).greeting() + ", " + sub2.name());

        for (Season s : Season.values())
            System.out.println(s + "\tindex: " + s.ordinal());

        D d = new D();
        System.out.println(d.getAge());
        System.out.println(B.getRandom());

        Outer.classMethod();
        new Outer().foo();
        new Outer().new Inner().new InnerMem().test();

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

        Test1.main1();
    }
}
