# --- Classes ---

*Class declaration* (khai báo class) định nghĩa reference type mới, & mô tả chúng được triển khai như thế nào.  

*Top level class* là class mà không phải *nested class* (class lồng nhau).  

*Nested class* là bất kỳ class nào mà khai báo nằm trong body của class or interface khác, bao gồm:  

- Member class,  
- Local class,  
- Anonymous class.  

Class được đặt tên có thể được khai báo là *abstract*, và phải được khai báo là abstract nếu nó được triển khai không đầy đủ; một lớp như vậy không thể được khởi tạo, nhưng có thể được extend bởi các subclass.  

Class có thể được khai báo là *final*, trong trường hợp đó, nó không thể có subclass. 

Class được khai báo là *public*, thì có thể được tham chiếu từ mã trong bất kỳ package nào của module của nó và có thể từ mã trong các module khác.  

Mọi Class ngoại trừ Object chỉ có thể extend một class duy nhất và có thể triển khai các interfaces.  

Class có thể là *generic*, nghĩa là, chúng có thể khai báo các type variables mà các ràng buộc (binding) của chúng có thể khác nhau giữa các instance khác nhau của class.

Classes có thể được decorated với annotations giống như bất kỳ loại declaration nào khác.  

Body của Class khai báo các:  

- Member:  
    + Fields  
    + Methods  
    + Nested classes  
    + Nested interfaces  
- static initializers  
- instance initializers  
- constructors  

Scope của member là toàn bộ phần nội dung của khai báo của class mà member đó thuộc về.  

Field, method, member class, member interface, & constructor declarations có thể bao gồm các access modifiers: public, protected, private.

Các members của class bao gồm cả các member được khai báo & member được thừa kế:  

- Các fields mới được khai báo của class có thể che dấu (hide) các fields được khai báo trong superclass or superinterface,  
- Các class members & interface members mới được khai báo của class có thể che dấu (hide) các class & interface members được khai báo trong superclass or superinterface,  
- Các methods mới được khai báo của class có thể che dấu (hide), triển khai (implement), ghi đè (override) các methods được khai báo trong superclass or superinterface.  

*Field declaration* mô tả class variables & instance variables. Field có thể được khai báo là *final*, trong trường hợp đó, nó chỉ có thể được gán 1 lần. Bất kỳ Field declaration nào cũng có thể bao gồm một initializer.  

*Member class declaration* mô tả *nested class* là member của class bao quanh. Các member class có thể là *static*, trong trường hợp đó chúng không có quyền truy cập các instance variables của class bao quanh; hoặc member class cũng có thể là *inner class*.  

*Member interface declaration* mô tả *nested interface* là member của class bao quanh.  

*Method declaration* mô tả code mà có thể được gọi bởi *method invocation expression*. Một *Class method* được gọi thông qua class type; Một *Instance method* được gọi thông qua instance của class type. Method mà declaration không chỉ ra nó được triển khai thế nào phải được khai báo là *abstract*. Method có thể được khai báo là *final*, trong trường hợp đó nó không thể bị che giấu (hidden) hay ghi đè (overriden). Method có thể được triển khai bằng platform-dependent native code. *Synchronized method* tự động lock một object trước khi thực thi phần body của nó, & tự động unlock object đó khi trở lại, như thể sử dụng một synchronized statement (câu lệnh đồng bộ hóa), do đó cho phép các activities của nó được đồng bộ với các activities của các threads khác.  

Method names có thể được overloaded.

*Instance initializers* là blocks of executable code mà có thể được sử dụng để giúp khởi tạo một instance khi nó được tạo ra.

*Static initializers* là blocks of executable code mà có thể được sử dụng để giúp khởi tạo một class.  

*Constructors* tương tự với methods, nhưng không thể được gọi trực tiếp bởi một lời gọi method (method call); chúng được sử dụng để khởi tạo class instance mới. Giống với methods, chúng có thể được overloaded.  


## 1. Class Declarations

Class declaration xác định một *reference type* được đặt tên mới.

Có 2 loại class declarations:  

- normal class declarations,  
- enum declarations.  

Syntax của *normal class declaration*:  

> {ClassModifier} class TypeIdentifier [TypeParameters] [Superclass] [Superinterfaces] ClassBody

Nếu class có cùng tên với class or interface bao quanh nó, sẽ xảy ra compile-time error.  


### 1.1, Class Modifiers

Class declaration có thể bao gồm các *class modifiers*.  

Các Class Modifiers bao gồm:  

- Annotation,  
- public,  
- protected,  
- private,  
- abstract,  
- static,  
- final,  
- strictfp.  

*public* chỉ sử dụng trong *top level class declaration* & *member class declaration*, không sử dụng trong *local class declaration* & *annonymous class declaration*.  

*protected* & *private* chỉ sử dụng trong *member class declaration* bên trong một class declaration bao quanh trực tiếp.  

*static* chỉ sử dụng trong *member class declaration*, không sử dụng trong *top level, or local, or anonymous class declaration*.  

Sẽ xảy ra compile-time error nếu trong class declaration có một modifier keyword xuất hiện nhiều hơn một lần, or có nhiều hơn một trong các modifiers: public, protected, private.  

#### *1.1.1, abstract Classes*

*Abstract Class* là một class không hoàn chỉnh, hoặc được coi là không hoàn chỉnh.  

Nếu cố gắng tạo instance của abstract class bằng cách sử dụng *class instance creation expression*, thì sẽ xảy ra compile-time error.  

Subclass của abstract class mà bản thân nó không phải abstract thì có thể được khởi tạo, dẫn đến việc thực thi một constructor cho abstract class, và do đó sẽ thực thi field initializers cho instance variables của class đó.

#### *1.1.2, final Classes*
#### *1.1.3, strictfp Classes*