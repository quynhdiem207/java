# Chapter 8: Classes

## 2. Class Members

Các members của class type bao gồm:  

- Các members được thừa kế từ direct superclass của nó, ngoại trừ class Object không có direct superclass.  
- Các members được thừa kế từ direct superinterface của nó,  
- Các members được khai báo trong body của class.  

Các members của class được khai báo là *private* sẽ không được thừa kế bởi các subclass của class đó.  

Chỉ các members của class được khai báo là *public* or *protected* mới được thừa kế bởi các subclass được khai báo trong package khác, các members được khai báo là *private* hay *package-private* sẽ không được thừa kế bởi subclass khác package.

Constructors, static initializers, và instance initializers không phải members, do đó chúng không được kế thừa.  

Các fields, methods, và member types của class có thể cùng tên, vì chúng được sử dụng trong các ngữ cảnh khác nhau và được phân biệt bởi các thủ tục khác nhau.  

Type của member biểu thị:  

- Đối với field, là type của nó,  
- Đối với method, là bộ 4 bao gồm:  
    + type parameters: các declarations của bất kỳ type parameters của method member.  
    + argument types: list các types của các arguments cho method member.  
    + return type: return type của method member.  
    + throws clause: exception types được khai báo trong throws clause của method member.  

**Note**: Class không kế thừa các *static methods* từ *direct superinterface* của nó.


*Ví dụ 1: Use of Class Members*  

```java
package points;
class Point {
    int x, y;
    private Point() { reset(); }
    Point(int x, int y) { this.x = x; this.y = y; }
    private void reset() { this.x = 0; this.y = 0; }
}

package points;
class ColoredPoint extends Point {
    int color;
    void clear() { 
        reset(); // error
        // Do reset là private member của Point, không được thừa kế bởi ColoredPoint
    }  
}

package points;
class Test {
    public static void main(String[] args) {
        ColoredPoint c = new ColoredPoint(0, 0); // error 
        // ColoredPoint has no constructor declared with two int parameters,
        // constructor của Point không được thừa kế bởi ColoredPoint

        c.reset(); // error 
        // Do reset là private member của Point, không được thừa kế bởi ColoredPoint
    }
}
```


*Ví dụ 2: Inheritance of Class Members with Package Access* 

```java
package points;
public class Point {
    int x, y;
    public void move(int dx, int dy) { 
        x += dx; y += dy;
    }
}

package points;
public class Point3d extends Point {
    int z;
    public void move(int dx, int dy, int dz) {
        x += dx; y += dy; z += dz;  // OK -> same package
    }
}

package another;
import points.Point3d;
class Point4d extends Point3d {
    int w;
    public void move(int dx, int dy, int dz, int dw) {
        x += dx; y += dy; z += dz; w += dw; // compile-time errors
        // Because Point4d and Point3d are different package,
        // therefore, default access fields x, y, z of Point3d are not inherit by Point4d 
    }
}

// Giải quyết vấn đề:
package another;
import points.Point3d;
class Point4d extends Point3d {
    int w;
    public void move(int dx, int dy, int dz, int dw) {
        // using method move of superclass Point3d
        super.move(dx, dy, dz); // OK
        w += dw;
    }
}
```


*Ví dụ 3: Inheritance of public and protected Class Members*

```java
package points;
public class Point {
    public int x, y;
    protected int useCount = 0;
    static protected int totalUseCount = 0;
    public void move(int dx, int dy) {
        x += dx; y += dy; useCount++; totalUseCount++;
    }
}

package another;
class Test extends points.Point {
    public void moveBack(int dx, int dy) {
        x -= dx; y -= dy; useCount++; totalUseCount++; // OK
        // protected and public fields x, y, useCount, totalUseCount are inherited in all subclasses of Point.
    }
}
```


*Ví dụ 4: Inheritance of private Class Members*

```java
class Point {
    int x, y;
    private static int totalMoves;
    void move(int dx, int dy) {
        x += dx; y += dy; 
        totalMoves++; // OK
    }
    void printMoves() { 
        System.out.println(totalMoves); // OK
    }
}

class Point3d extends Point {
    int z;
    void move(int dx, int dy, int dz) {
        super.move(dx, dy); 
        z += dz; 
        totalMoves++; // error
        // class variable totalMoves can be used only within the class Point;
        // private field totalMoves is not inherited by the subclass Point3d.
    }
}
```


*Ví dụ 5: Accessing Members of Inaccessible Classes*

Có thể khai báo class không phải *public*, mặc dù class type này không thể được tham chiếu từ ngoài package của nó, nhưng các instance của class type này vẫn có thể có sẵn tại runtime và được tham chiếu từ ngoài package mà class type được khai báo, thông qua việc từ trong package của class này, gán tham chiếu của instance cho một variable với type là superclass or superinterface của class type đó. Và có thể truy xuất các public non-static methods mà implement hoặc override các public methods của superclass từ bên ngoài package của class type.

```java
package points;
public class Point {
    public int x, y;
    public void move(int dx, int dy) {
        x += dx; y += dy;
    }
}

package morePoints;
class Point3d extends points.Point { // class Point3d is not public
    public int z;
    public void move(int dx, int dy, int dz) {
        super.move(dx, dy); z += dz;
    }
    public void move(int dx, int dy) {
        move(dx, dy, 0);
    }
}

/**
* 1, It's possible to use instances of Point3d outside package morePoints,
*    even though the type Point3d is not available outside the package morePoints.
* 2, An invocation morePoints.OnePoint.getOne() in a third package would return a Point3d,
*    that can be used as a Point.
* 3, The two-argument version of method move could then be invoked for that object, 
*    which is permissible because method move of Point3d is public.
* 4, Fields x and y of that object could also be accessed from such a third package.
* 5, Field z of class Point3d is public, but can not be accessed outside package morePoints,
*    because a reference to an instance of class Point3d in a variable of type Point,
*    which has no field named z, and expression ((Point3d)p).z is not correct, 
*    because class type Point3d cannot be referred to outside package morePoints.
*/
package morePoints;
public class OnePoint {
    public static points.Point getOne() { 
        return new Point3d(); 
    }
}

/**
* However, field z as public is not useless, 
* in package morePoints, a public subclass Point4d of class Point3d would inherit the field z,
* then it can be accessed outside package morePoints, 
* through variables and expressions of the public type Point4d.
*/
package morePoints;
public class Point4d extends Point3d {
    public int w;
    public void move(int dx, int dy, int dz, int dw) {
        super.move(dx, dy, dz); w += dw;
    }
}
```