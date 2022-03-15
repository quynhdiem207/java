## 1. Variables  

Định nghĩa:  

- Biến là một nơi lưu trữ giá trị, & có một type liên kết, type có thể là *Primitive type* hoặc *Reference type*.  
- Giá trị của biến có thể được thay đổi bởi an assignment, increment (++), or decrement(--) operator.  

Cú pháp khai bao biến:  

> Type variableName = value;


### 1.1, Variables of Primitive Type

Một *Variable* của một *Primitive Type* luôn giữ một *Primitive Value* của chính Primitive Type đó.  

```java
public class Example {
    public static void main(String[] args) {
        int a = 3;
    }
}
```


### 1.2, Variables of Reference Type

Một *Variable* của một *Class Type* T có thể giữ: một *null reference*, or một *reference tới một instance của class T or của bất kỳ class nào là subclass của class T*.  

*Note*: Một object được cho là instance của class của nó & của tất cả các superclass của class của nó.  

```java
public abstract class Person {
	String message = "...";

    public abstract void study();
    
    public void showMessage() {
        System.out.println(message);
    }
}

public class Student extends Person {
	private String name;

	public Student() {}

	public Student(String name) {
		this.name = name;
	}
    
	public void run() {
		System.out.println(name + " is running...");
	}

	@Override
	public void study() {
		System.out.println(name + " is studying now...");
	}
}

public class Example {
    public static void main(String[] args) {
        Student student = new Student("Quang");
        student.study();       // Quang is studying now...
        student.showMessage(); // ...
        student.run();         // Quang is running...

        Person person = new Student("Anh");
        person.study();       // Anh is studying now...
        person.showMessage(); // ...
        person.run();         // ERROR
    }
}
```

Một *Variable* của một *Interface Type* có thể giữ: một *null reference*, or một *reference tới một instance của bất kỳ class nào implements interface này*.  

*Note*: Mặc dù type của variable or expression là *Interface type*, nhưng không có instance của interface.  

```java
public interface Member {
	String NAME = "Diêm";
	
	void showName();
}

public class Student implements Member {
	private String name;

	public Student() {}

	public Student(String name) {
		this.name = name;
	}
    
	public void run() {
		System.out.println(name + " is running...");
	}

	@Override
	public void showName() {
		System.out.println("My name is " + name);
	}
}

public class Example {
    public static void main(String[] args) {
        Student student = new Student("Quang");
        student.showName(); // My name is Quang
        student.run();      // Quang is running...
        System.out.println(Student.NAME); // Diêm

        Member member = new Student("Anh");
        member.showName(); // My name is Anh
        member.run();      // ERROR
        System.out.println(Member.NAME); // Diêm
    }
}
```

Một *Variable* của một *Array of T Type* với T là:  

- Một *Primitive Type*: variable có thể giữ *null reference* hoặc *reference tới bất cứ array của 'Array of T' Type*,  
    ```java
    public class Example {
        public static void main(String[] args) {
            int[] x = { 1, 2 };
            int[] y = null;
            y = x; // OK

            float[] arrFloat = { 1f };
            arrFloat = arrInt; // ERROR
        }
    }
    ```  

- Một *Reference Type*: variable có thể giữ *null reference* hoặc *reference tới bất cứ array của 'Array of S' Type*, với S là subclass or subinterface của T.  
    ```java
    public class Person {
        String message = "...";
    }

    public class Student extends Person {
        private String name;
    }

    public class Example {
        public static void main(String[] args) {
            Student[] arrStudent = { new Student() };

            Person[] arrPerson = { new Person(), new Student("Hà") };
            Person[] arrPerson2 = null;
            arrPerson2 = arrPerson;  // OK
            arrPerson2 = arrStudent; // OK
        }
    }
    ```  

Một *Variable* của *Object[]* type có thể giữ *null reference* hoặc *reference tới một array của bất cứ Reference Type nào*.  

```java
public class Person {
    String message = "...";
}

public class Example {
    public static void main(String[] args) {
        int[] arrInt = { 1, 2 };
        Person[] arrPerson = { new Person() };

        Object[] arrObj = { new Person(), "Hello" };
        arrObj = null;
        arrObj = arrPerson; // OK
        arrObj = arrInt;    // ERROR
    }
}
```

Một *Variable* của *Object* type có thể giữ *null reference* hoặc *reference tới bất cứ object nào* (object có thể là một class instance or một array).  

```java
public class Person {
    String message = "...";
}

public class Example {
    public static void main(String[] args) {
        int[] arrInt = { 1, 2 };
        Person[] arrPerson = { new Person() };

        Object obj = null;
        obj = 1;            // OK (Boxing: primitive value 1 -> Integer)
		obj = new Person(); // OK
		obj = arrInt;       // OK
		obj = arrPerson;    // OK
    }
}
```


### 1.3, Kinds of Variables

Có 8 loại biến:  

- **Class Variable**:  
    + Là *static field* trong class declaration, or *field* trong interface declaration.  
    + Được tạo & được khởi tạo với default value khi class or interface của nó được chuẩn bị (giai đoạn Preparation trong chức năng Linking của Class Loader Subsystem).  
    + Không còn tồn tại khi class or interface của nó bị unload.  

- **Instance Variable**:  
    + Là *field* được khai báo không sử dụng static keyword trong class declaration.  
    + Được tạo & được khởi tạo với default value với vai trò là một thành phần của object mới được tạo của class chứa nó or subclass của class chứa nó.  
    + Không còn tồn tại khi object không còn được tham chiếu.  

- **Array Components**:  
    + Là các *variables* không được đặt tên.  
    + Được tạo & được khởi tạo với default value bất cứ khi nào một object mới là một array được tạo.  
    + Không còn tồn tại khi array không còn được tham chiếu.  

- **Method Parameters**:  
    + Là *tên các argument values* được truyền cho một method.  
    + Được tạo & khởi tạo với argument value tương ứng cho mỗi parameter được khai báo trong method declaration mỗi khi method được gọi.  
    + Không còn tồn tại khi method thực thi hoàn tất.  

- **Constructor Parameters**:  
    + Là *tên các argument values* được truyền cho một constructor.  
    + Được tạo & khởi tạo với argument value tương ứng cho mỗi parameter được khai báo trong constructor declaration mỗi khi constructor được gọi.  
    + Không còn tồn tại khi constructor thực thi hoàn tất.  

- **Lambda Parameters**:  
    + Là *tên các argument values* được truyền cho một Lambda expression.  
    + Được tạo & khởi tạo với argument value tương ứng cho mỗi parameter được khai báo trong lambda expression mỗi khi method được implemented bởi Lambda được gọi.  
    + Không còn tồn tại khi lambda expression thực thi hoàn tất.  

- **Exception Parameter**:  
    + Được tạo & khởi tạo với object được ném đại diện cho ngoại lệ mỗi khi một exception được bắt bởi mệnh đề catch của câu lệnh try.  
    + Không còn tồn tại khi block của mệnh đề catch thực thi hoàn tất.  

- **Local variables**:  
    + Được tạo cho mỗi biến cục bộ được khai báo trong câu lệnh khai báo chứa bên trong block mà luồng điều khiển đi vào.  
    + Không còn tồn tại khi block thực thi hoàn tất.  
    + Không thể sử dụng giá trị của *local variable* trước khi nó được khởi tạo hoặc được gán một giá trị khác.  

```java
class Point {
    static int numPoints;   // numPoints is a class variable
    int x, y;               // x and y are instance variables
    int[] w = new int[10];  // w[0] is an array component
    int setX(int x) {       // x is a method parameter
        int oldx = this.x;  // oldx is a local variable
        this.x = x;
        return oldx;
    }
}
```

***Note***: có thể khai báo các *variables* có tên trùng nhau nếu chúng có scope (phạm vi) khác nhau.  

```java
public class Student {
    int score;

    public static void main(String[] args) {
        int score = 5;
        System.out.println(score);      // 5
        System.out.println(this.score); // 0
    }
}
```


### 1.4, final Variables

Một *final Variables* chỉ có thể được gán một lần.  

Khi final variable được gán, nó sẽ luôn giữ giá trị này.  

Nếu một final variable giữ một tham chiếu đến một object, thì state của object có thể bị thay đổi bởi các thao tác trên object, nhưng nó vẫn luôn tham chiếu đến object này. Tương tự, nếu một final variable giữ một tham chiếu đến một array, thì các components (phần tử) của array có thể bị thay đổi bởi các thao tác trên array, nhưng nó vẫn luôn tham chiếu đến array này.  

Một *blank final variable* (final variable trống) là final variable được khai báo mà thiếu một initializer (bộ khởi tạo).  

Một *constant variable* là một final variable của *primitive type* or *String type* được khởi tạo với một *constant expression* (biểu thức hằng).  

Có 3 loại variable được khai báo ngầm định là *final* gồm:  

- Field của một interface,  
- Một local variable được khai báo như là resource của câu lệnh try-with-resources,  
- Một exception parameter của mệnh đề multi-catch.  

```java
class Point {
    int x, y;
    int useCount;
    Point(int x, int y) { 
        this.x = x; 
        this.y = y; 
    }
    static final Point origin = new Point(0, 0);
}
```


### 1.5, Initial Values of Variables

Mọi *variable* đều phải có value trước khi value của nó được sử dụng:  

- Mỗi class variable, instance variable, or array component đều được khởi tạo với một *default value* khi nó được tạo:  
    + defaut value của *byte type* is (byte) 0,  
    + defaut value của *short type* is (short) 0,  
    + defaut value của *int type* is 0,  
    + defaut value của *long type* is 0L,  
    + defaut value của *float type* is 0.0f,  
    + defaut value của *double type* is 0.0d,  
    + defaut value của *char type* is null character '\u0000',  
    + defaut value của *boolean type* is false,  
    + defaut value của *all reference types* is null,  

- Mỗi method parameter được khởi tạo với *argument value* tương ứng được cung cấp khi gọi method.  
- Mỗi constructor parameter được khởi tạo với *argument value* tương ứng được cung cấp khi gọi constructor.  
- Mỗi exception parameter được khởi tạo với *object* được ném đại diện cho exception.  
- Mỗi local variable phải được cung cấp một *value* tường minh trước khi nó được sử dụng, bằng cách khởi tạo or gán.  

```java
class Point {
    static int npoints;
    int x, y;
    Point root;
}

class Test {
    public static void main(String[] args) {
        System.out.println("npoints = " + Point.npoints);      // npoints = 0
        Point p = new Point();
        System.out.println("p.x = " + p.x + ", p.y = " + p.y); // p.x = 0, p.y = 0
        System.out.println("p.root = " + p.root);              // p.root = null
    }
}
```
