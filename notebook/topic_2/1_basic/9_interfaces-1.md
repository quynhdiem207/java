# Chapter 9: Interfaces

Một *interface declaration* giới thiệu một *reference type* mới có các *members* là các *classes, interfaces, constants, và methods*. Type này không có instance variables, và điển hình khai báo các *abstract methods*; Các classes không liên quan có thể triển khai interface bằng cách cung cấp các implementations cho các abstract methods của nó. Các interfaces không thể được khởi tạo trực tiếp.

Một *nested interface* là bất kỳ interface nào có declaration xảy ra bên trong body của một class or interface khác.

Một *top level interface* là một interface không phải *nested interface*.

Interfaces được phân làm 2 loại, gồm: *normal interfaces* và *annotation types*.

Một interface có thể được khai báo là một *direct extension* của một hoặc nhiều interfaces khác, có nghĩa là nó kế thừa tất cả các *member types, instance methods, và constants* của các interfaces mà nó extends, ngoại trừ bất kỳ members nào mà nó có thể override or hide.

Một class có thể được khai báo để triển khai một một hoặc nhiều interfaces, có nghĩa là bất kỳ instance nào của  class này sẽ implements tất cả các abstract methods được xác định bởi các interfaces. Một class nhất thiết phải implements tất cả các interfaces mà direct superclasses của nó implements và direct superinterfaces của nó extends. Sự kế thừa (nhiều) interface này cho phép các objects hỗ trợ (nhiều) hành vi thông dụng mà không cần chia sẻ một superclass.

Một variable có type được khai báo là một interface type có thể giữ giá trị là một tham chiếu tới bất kỳ instance nào của một class mà implements interface đã chỉ định. Nếu class không triển khai tất cả các abstract methods của interface, thì một trong các superclasses của nó phải được khai báo để triển khai interface.


## 1. Interface Declarations

Một *interface declaration* xác định một *reference type* được đặt tên mới. 

Có 2 loại interface declarations:  

- normal interface declarations,  
- annotation type declarations.  

```
- Normal Interface Declaration:
    {InterfaceModifier} interface Identifier [TypeParameters] [ExtendsInterfaces] InterfaceBody
```

Nếu một interface có cùng simple name với bất kỳ enclosing classes or interfaces nào của nó, sẽ gây ra compile-time error.


### 1.1, Interface Modifiers

Một interface declaration có thể gồm nhiều interface modifiers.

```
InterfaceModifier:
(one of)
Annotation public protected private
abstract static strictfp
```

Access modifier *public* dùng cho cả *top level và member interface declaration*.

Các sccess modifiers *protected* và *private* chỉ dùng cho các *member interfaces* có declarations được bao bọc trực tiếp bởi một *class declaration*.

Modifier *static* chỉ dùng cho *member interfaces*, không dùng cho *top level interfaces*.


#### *1.1.1, abstract Interfaces*

Mọi interface ngầm định đều là *abstract*.


#### *1.1.2, strictfp Interfaces*

Tác dụng của *strictfp* modifier là làm cho tất cả các *float or double expression* trong interface declaration phải là FP-strict một cách tường minh.

Điều này ngụ ý rằng tất cả các methods được khai báo trong interface và tất cả các nested type được khai báo trong interface, ngầm định đều là strictfp.


### 1.2, Generic Interfaces and Type Parameters

Một interface là *generic* nếu nó khai báo một hoặc nhiều *type variables*.

Những type variables này được gọi là các *type parameters* của interface.

```
- TypeParameters:    < TypeParameterList >
- TypeParameterList: TypeParameter {, TypeParameter}
- TypeParameter:     {TypeParameterModifier} Identifier [TypeBound]
- TypeParameterModifier: Annotation
- TypeBound:
    + extends TypeVariable
    + extends ClassOrInterfaceType {AdditionalBound}
- AdditionalBound: & InterfaceType
```

Trong type parameter section của một interface, một type variable T phụ thuộc trực tiếp vào type variable S nếu S là ràng buộc của T, trong khi đó T phụ thuộc vào S nếu T phụ thuộc trực tiếp vào S, hoặc T phụ thuộc trực tiếp vào một type variable U mà phụ thuộc vào S (diễn ra đệ quy). Nếu một type variable phụ thuộc vào chính nó, sẽ gây ra compile-time error.

Khi tham chiếu đến một *type parameter* của một interface I ở bất cứ đâu trong declaration của một *field* hoặc *type member* hoặc *static method* của I, sẽ gây ra compile-time error, vì chúng đều là *static*.

một generic interface declaration định nghĩa một tập các parameterized types. Tất cả các parameterized types này đều chia sẻ cùng một interface tại runtime.

```java
interface A {}
interface B {}
interface C {}
class D {}

public interface Person<E extends D & A> extends B, C {}
```


### 1.3, Superinterfaces and Subinterfaces

Nếu một *extends* clause được cung cấp, thì interface đang được khai báo sẽ extends mỗi interfaces được đặt tên khác, và kế thừa các *member types, methods, và constants* của chúng.

Những interfaces được đặt tên này là các *direct superinterfaces* của interface đang được khai báo.

Bất kỳ class nào implements interface được khai báo thì cũng được coi là implement tất cả các interfaces mà interface này extends.

```
- ExtendsInterfaces: extends InterfaceTypeList
- InterfaceTypeList: InterfaceType {, InterfaceType}
```

Nếu InterfaceType trong extends clause có các type arguments, thì nó phải là parameterized type đã định dạng tốt, và không có type arguments nào là wildcard, nếu không sẽ gây ra compile-time error.

Cho interface declaration (có thể là generic) I<F1,...,Fn> (n ≥ 0), thì *direct superinterfaces* của interface type I<F1,...,Fn> là các types được đưa ra trong *extends* clause của declaration của I, nếu có extends clause.

Cho *generic* interface declaration I<F1,...,Fn> (n > 0), thì *direct superinterfaces* của *parameterized interface type* I<T1,...,Tn>, với Ti (1 ≤ i ≤ n), là tất cả các types J<U1 θ,...,Uk θ>, trong đó J<U1,...,Uk> là một direct superinterface của I<F1,...,Fn> và θ là phép thay thế [F1:=T1,...,Fn:=Tn].

Mối quan hệ superinterface là mối quan hệ bắc cầu qua direct superinterface. Một interface K là một superinterface của interface I nếu thỏa mãn một trong các điều sau:

- K là một direct superinterface của I.  
- Tồn tại một interface J sao cho K là một superinterface của J, và J là một superinterface của I, áp dụng đệ quy định nghĩa này.  

Interface I được cho là một subinterface của interface K bất cứ khi nào K là một superinterface của I.

Trong khi mọi class đều là một extension của class Object, nhưng không có interface duy nhất nào mà tất cả các interfaces đều là extensions của nó.

Một interface I phụ thuộc trực tiếp vào type T nếu T được đề cập trong extends clause của I.

Một interface I phụ thuộc vào một reference type T nếu thỏa mãn bất kỳ điều nào sau đây:

- I trực tiếp phụ thuộc vào T.  
- I trực tiếp phụ thuộc vào một class C mà phụ thuộc vào T.  
- I trực tiếp phụ thuộc vào một interface J mà phụ thuộc vào T (sử dụng đệ quy định nghĩa này).  

Nếu một interface phụ thuộc vào chính nó, sẽ gây ra compile-time error.  

Tại runtime khi interfaces được loaded, nếu phát hiện interface được khai báo vòng tròn, thì ngoại lệ *ClassCircularityError* sẽ được ném ra.

```java
interface A {}
interface B extends A {}

// C là subinterface của A và B
interface C extends B {}
```


### 1.4, Interface Body and Member Declarations

Body của một interface có thể khai báo các *members* của interface, đó là các *fields, methods, classes, và interfaces*.


## 2. Interface Members

Các members của một interface type bao gồm:

- Các members được khai báo trong body của interface.  
- Các members được thừa kế từ bất kỳ direct superinterfaces nào.  
- Nếu một interface không có direct superinterfaces, thì interface ngầm định khai báo một *public abstract member method* m với signature s, return type r, và throws clause t tương ứng với mỗi *public instance method* m với signature s, return type r, and throws clause t được khai báo trong class *Object*, trừ khi một abstract method với cùng signature, cùng return type, và throws clause tương thích được khai báo tường minh bởi interface.  
    Sẽ gây ra compile-time error nếu:  

    + Interface khai báo tường minh một method m như vậy trong trường hợp m được khai báo *final* trong Object.  
    + Interface khai báo tường minh một method với một signature là override-equivalent với một public method của Object, nhưng lại có return type khác, hoặc throws clause không tương thích, hoặc không phải abstract.  

Interface kế thừa từ các interfaces mà nó extends, tất cả các members của những interfaces này, ngoại trừ các *fields, classes, và interfaces* mà nó che giấu (hide); các *abstract hoặc default methods* mà nó overrides; và các *static methods*.

Các Fields, methods, và member types của một interface type có thể có cùng name, vì chúng được sử dụng trong các contexts khác nhau và được phân biệt bởi các thủ tục tìm kiếm khác nhau. Tuy nhiên, không nên làm vậy.


## 3. Field (Constant) Declarations

```
- ConstantDeclaration: {ConstantModifier} UnannType VariableDeclaratorList ;
- ConstantModifier:
    (one of)
    Annotation public
    static final

- VariableDeclaratorList: VariableDeclarator {, VariableDeclarator}
- VariableDeclarator:     VariableDeclaratorId [= VariableInitializer]
- VariableDeclaratorId:   Identifier [Dims]
- Dims:                   {Annotation} [] {{Annotation} []}
- VariableInitializer:
    + Expression
    + ArrayInitializer
```

Mọi *field declaration* trong body của một interface ngầm định là *public, static, và final*. DDuocj phép chỉ định một cách dư thừa bất kỳ hoặc tất cả các modifiers này cho các fields.

Nếu body của một interface declaration khai báo 2 fields với cùng name, sẽ gây ra compile-time error.

Nếu interface khai báo một field với một tên nhất định, thì declaration của field đó được coi là ẩn (hide) bất kỳ và tất cả các declarations có thể truy cập của các fields với cùng tên trong các superinterfaces của interface đó.

Interface có thể kế thừa nhiều hơn một field với cùng tên, sẽ không gây ra compile-time error. Tuy nhiên, trong body của interface này nếu tham chiếu tới bất kỳ field nào như vậy bằng simple name của nó sẽ gây ra compile-time error, bởi vì tham chiếu không rõ ràng, cần sử dụng qualified name.

Nếu cùng một field declaration được kế thừa từ một interface bằng nhiều paths, thì field đó chỉ được coi là kế thừa một lần duy nhất. Nó có thể được tham chiếu bằng simple name.

*Ví dụ 1. Ambiguous Inherited Fields and Multiply Inherited Fields*

```java
interface BaseColors {
    int RED = 1, GREEN = 2, BLUE = 4;
}
interface RainbowColors extends BaseColors {
    int YELLOW = 3, ORANGE = 5, INDIGO = 6, VIOLET = 7;
}
interface PrintColors extends BaseColors {
    int YELLOW = 8, CYAN = 16, MAGENTA = 32;
}

// 1. Interface LotsOfColors kế thừa 2 fields cùng tên YELLOW.
//    Điều này không sao miễn là interface không chứa bất kỳ tham chiếu nào bởi simple name đến field YELLOW.
// 2. Các fields RED, GREEN, và BLUE được kế thừa bởi interface LotsOfColors theo nhiều cách,
//    thông qua interface RainbowColors và cũng thông qua interface PrintColors,
//    nhưng kết quả thực tế các fields này chỉ được kế thừa một lần duy nhất.
interface LotsOfColors extends RainbowColors, PrintColors {
    int FUCHSIA = 17, VERMILION = 43, CHARTREUSE = RED + 90; // OK
}
```


### 3.1, Initialization of Fields in Interfaces

Mỗi declarator trong một field declaration của một interface đều phải có *variable initializer*, nếu không sẽ gây ra compile-time error.

*Initializer* không nhất thiết phải là một *constant expression*.

Sẽ gây ra compile-time error nếu:

- *Initializer* của một interface field sử dụng *simple name* của chính field đó hoặc một field khác có declaration đứng sau trong program text trong cùng interface.  
- Keyword *this* hoặc *super* xuất hiện trong *initializer* của một interface field, trừ khi nó xuất hiện trong body của một *anonymous class*.

Tại runtime, initializer sẽ được đánh giá, và việc gán giá trị cho field được thực thi một lần duy nhất, khi interface được khởi tạo.  

**Note**: Các *interface fields* là các *constant variables* được khởi tạo trước các interface fields khác. Điều này cũng áp dụng cho các *static fields* là *constant variables* trong các classes.

*Ví dụ 1. Forward Reference to a Field*

```java
interface Test {
    float f = j; // compile-time error
        // becase j is referred to in the initialization of f before j is declared
    int   j = 1;
    int   k = k + 1; // compile-time error 
        // because the initialization of k refers to k itself
}
```


## 4. Method Declarations

```
- InterfaceMethodDeclaration: {InterfaceMethodModifier} MethodHeader MethodBody
- InterfaceMethodModifier:
    (one of)
    Annotation public
    abstract default static strictfp

- MethodHeader:
    + Result MethodDeclarator [Throws]
    + TypeParameters {Annotation} Result MethodDeclarator [Throws]
- Result:
    + UnannType
    + void
- MethodDeclarator: Identifier ( [FormalParameterList] ) [Dims]
- MethodBody:
    + Block
    + ;
```

Mọi *method declaration* trong body của một interface đều ngầm định là *public*. Nhưng vẫn được phép chỉ định dư thừa public modifier.

Một *default method* là một method được khai báo trong một interface với *default* modifier; body của nó luôn là một block. Nó cung cấp một *default implementation* cho bất kỳ class nào implements interface này mà không override method này. Các *default methods* khác với các *concrete methods* được khai báo trong các classes.

Một interface có thể khai báo các *static methods* có thể được gọi mà không cần tham chiếu tới một object cụ thể.

Nếu sử dụng tên của *type parameter* của bất kỳ declaration bao quanh nào trong header hoặc body của một *static method* của một interface, sẽ gây ra compile-time error.

Tác dụng của *strictfp* modifier là làm cho tất cả các float hoặc double expressions bên trong của một *default or static method* là FP-strict một cách tường minh.

Một interface method thiếu *default* modifier hoặc *static* modifier, thì sẽ được ngầm định là *abstract*, bởi vậy body của nó được biểu thị bởi dấu chấm phẩy (;), không phải một block. Tuy nhiên, vẫn có thể chỉ định tường minh abstract modifier cho method.

Sẽ gây ra compile-time error nếu:  

- Một method được khai báo với nhiều hơn một trong các modifiers abstract, default, hoặc static.  
- Một *abstract* method declaration chứa keyword *strictfp*.  
- Body của một interface khai báo tường minh hay ngầm định 2 methods với override-equivalent signatures. Tuy nhiên, một interface có thể kế thừa một số abstract methods với các signatures như vậy.

Một method trong một interface có thể là *generic*. Các quy tắc cho *type parameters* của *generic method* trong một interface cũng giống như cho generic method trong một class.

```java
interface A {
    String getName();
    static int getRandom() {
        return (int) Math.floor(Math.random() * 100);
    }
    default int getAge() {
        return 20;
    }
}

class B implements A {
    public String getName() {
        return "Diêm";
    }

    public static void main(String[] args) {
        B b = new B();
        System.out.println(b.getAge());    // 20
        System.out.println(A.getRandom()); // 99 
        System.out.println(A.getName());   // Diêm 
    }
}
```


### 4.1, Inheritance and Overriding

Một interface I kế thừa từ direct superinterfaces của nó tất cả các *abstract và default methods* m nếu thảo mãn tất cả các điều sau:  

- m là một member của một direct superinterface J của I.  
- Không có method nào được khai báo trong I mà có một signature là một subsignature của signature của m.  
- Không tồn tại method m' là một member của một direct superinterface J' của I (m khác m', J khác J'), sao cho m' overrides từ J' declaration của m.  

```java
interface Top {
    default String name() { return "unnamed"; }
}
interface Left extends Top {
    default String name() { return getClass().getName(); }
}
interface Right extends Top {}

// Right inherits name() from Top, but Bottom inherits name() from Left, not Right.
// This is because name() from Left overrides the declaration of name() in Top.
// A subinterface do not inherite a method that has already been overridden by another of its superinterfaces.
interface Bottom extends Left, Right {}
```

**Note**: 

- Một interface không thừa kế các *static methods* từ các superinterfaces của nó.  
- Nếu một interface I khai báo một *static method* m, và signature của m là một subsignature của một *instance method* m' trong một superinterface của I, và m' có thể truy cập trong I, thì sẽ gây ra compile-time error.  

    *Note: Một static method trong một interface không thể "hide" một instance method trong một superinterface.*


#### *4.1.1, Overriding (by Instance Methods)*

Một *instance method* m1, được khai báo hoặc được kế thừa bởi một interface I, sẽ overrides từ I một instance method m2 khác mà được khai báo trong interface J, nếu thỏa mãn tất cả các điều sau:

- I là một subinterface của J.  
- Signature của m1 là một subsignature của signature của m2.  

**Note**: Một *default method* bị overridden có thể được truy cập bằng cách sử dụng một *method invocation expression* có chứa keyword **super**  mà qualified bởi một *superinterface name*.

```java
class Super {}
class Sub extends Super {}

interface A {
    Super test();
    default int getAge() { return 11; }
}

interface B extends A {
    Sub test();
    default int getAge() { return A.super.getAge() * 2; } // 22
}

class C implements B {
    public Sub test() { return new Sub(); }
}

class D implements B {
    public Sub test() { return new Sub(); }
    public int getAge() { return B.super.getAge() * 3; } // 66

    public static void main(String[] args) {
        B c = new C();
        B d = new D();
        System.out.println(c.getAge()); // 22
        System.out.println(d.getAge()); // 66
    }
}
```


#### *4.1.2, Requirements in Overriding*

*Return type*: Nếu method declaration d1 với return type R1 overrides bất kỳ method declarations d2 nào với return type R2, thì d1 phải là *return-type-substitutable* cho d2.

*throws clause*: Giả sử B là một interface, và A là một superinterface của B, và một method declaration m2 trong B overrides bất kỳ method declarations m1 nào trong A. Thì:

- Nếu m2 có một throws clause đề cập đến bất kỳ checked exception types nào, thì m1 phải có một throws clause.  
- Đối với mọi checked exception type được liệt kê trong throws clause của m2, thì cùng exception class đó hoặc một trong các supertypes của nó phải được liệt kê trong erasure của throws clause của m1.  
- Nếu unerased throws clause của m1 không chứa một supertype của mỗi exception type trong throws clause của m2 (được chỉnh thành type parameters của m1 nếu cần), thì sẽ gây ra compile-time unchecked warning.  

*Signature*: Nếu một type declaration T có một member method m1 và tồn tại một method m2 được khai báo trong T or một supertype của T, thì sẽ gây ra compile-time error khi tất cả các điều sau là đúng:

- m1 và m2 có cùng name.  
- m2 có thể truy cập từ T.  
- Signature của m1 không phải một subsignature của signature của m2.  
- Signature của m1 hoặc một số method mà m1 overrides (trực tiếp hoặc gián tiếp) có cùng erasure với signature của m2 hoặc một số method mà m2 overrides (trực tiếp hoặc gián tiếp).  

Nếu một *default method* là override-equivalent với một *non-private method* của class *Object*, sẽ gây ra comple-time error.

```java
interface A {
    Object getInfo(String file) throws Exception;
}

interface B extends A {
    String getInfo(String file) throws IOException; // OK
}
```


#### *4.1.3, Inheriting Methods with Override-Equivalent Signatures*

Interface có thể kế thừa một số methods với các *override-equivalent signatures*.

Nếu một interface I kế thừa một *default method* có signature là override-equivalent với một method khác được kế thừa bởi I (dù method này là abstract hay default), thì sẽ gây ra compile-time error, trừ khi trong I khai báo một non-static method là override-equivalent với 2 methods kia.  

Nếu không thì, interface được coi là kế thừa tất cả các methods, ngoại trừ các phương thức bị override.

Một trong các methods được kế thừa phải là *return-type-substitutable* cho mọi method được kế thừa khác, nếu không sẽ gây ra compile-time error.

Trong trường hợp nhiều paths với cùng một method declaration được thừa kế từ một interface, thì method chỉ được kế thừa một lần, và không gây ra compile-time error.

```java
interface AC {
    int getAge();
    String getName();
}

interface AD {
    default int getAge() { return 11; }
    String getName();
}

interface AB extends AC, AD {
    int getAge(); // OK
}
```


### 4.2, Overloading

Nếu 2 methods của một interface (cho dù cả 2 được khai báo trong cùng một interface, hay cả 2 đều được thừa kế bởi một interface, hay 1 được khai báo và 1 được thừa kế) có cùng *name* nhưng khác *signatures*, mà không phải *override-equivalent*, thì method name được gọi là *overloaded*.

Điều này không dẫn đến compile-time error. Không có mối quan hệ bắt buộc nào giữa các *return types*, hoặc giữa các *throws clauses* của 2 methods với cùng *name* nhưng khác *signatures* mà không phải *override-equivalent*.

*Ví dụ 1. Overloading an abstract Method Declaration*

```java
interface PointInterface {
    void move(int dx, int dy);
}

// - The method named move is overloaded in interface RealPointInterface 
//   with three different signatures, two of them declared and one inherited. 
// - Any non-abstract class that implements interface RealPointInterface
//   must provide implementations of all three method signatures.
interface RealPointInterface extends PointInterface {
    void move(float dx, float dy);
    void move(double dx, double dy);
}
```


### 4.3, Interface Method Body

Một *default method* có một *block body*. Block code này cung cấp một implementation của method trong trường hợp một class implements interface này nhưng không cung cấp implementation cho method này.

Một *static method* cũng có một *block body*, cung cấp implementation cho method này.

Một *abstract method* có body là một dấu chấm phẩy (;) biểu thị rằng thiếu implementation cho method này.

Sẽ gây ra compile-time error nếu:  

- Một interface method declaration là *abstract* (ngầm định hay tường minh) và có một *block body*.  
- Một interface method declaration là *default* hoặc *static* và có body là dấu chấm phẩy (;).  
- Cố tham chiếu tới current object sử dụng keyword *this* hoặc *super* trong body của một *static method*.  
- Một method được khai báo có một *return type*, nhưng method body có thể hoàn thành bình thường (không được kết thúc thực thi bởi *return* hay *throw* statement gây ra việc chuyển quyền điều khiển dẫn đến việc hoàn thành đột ngột).  


## 5. Member Type Declarations

Các interfaces có thể chứa các *member type declarations*.

Một *member type declaration* trong một interface ngầm định là *public* và *static*. Nhưng vẫn được phép chỉ định dư thừa các modifiers này.

Sẽ xảy ra compile-time error nếu một *member type declaration* trong một interface có modifier là *protected* hoặc *private*.  

Nếu một interface khai báo một *member type* với một tên nhất định, thì declaration của type đó được cho là ẩn (hide) bất kỳ và tất cả các declarations của các member types với cùng tên trong superinterfaces của interface đó.

Một interface kế thừa từ direct superinterfaces của nó tất cả các *non-private member types* của các  superinterfaces mà có thể truy cập trong interface đó và không bị ẩn bởi một declaration trong interface đó.

Một interface có thể kế thừa 2 hoặc nhiều type declarations với cùng tên. Nếu cố gắng tham chiếu đến bất kỳ class hoặc interface được kế thừa không rõ ràng bằng simple name của nó, sẽ gây ra compile-time error.

Nếu cùng một type declaration được kế thừa từ một interface bằng nhiều paths, thì class hoặc interface đó chỉ được coi là kế thừa một lần duy nhất. Nó có thể được tham chiếu bằng simple name.

```java
interface A {
    class B {}
    interface C {}
}
```