# Lession 1. Interfaces

## Object Ordering

*Ví dụ: Một List có thể được sắp xếp như sau:*

```java
Collections.sort(l);
```

Nếu List bao gồm các *String* elements, nó sẽ được sắp xếp theo thứ tự bảng chữ cái. Nếu nó bao gồm các *Date* elements, nó sẽ được sắp xếp theo thứ tự thời gian. Làm thế nào điều này xảy ra? Cả *String* và *Date* đều triển khai *Comparable* interface. Triển khai Comparable cung cấp một thứ tự tự nhiên cho một class, cho phép các objects của class đó được sắp xếp tự động. 

Bảng sau đây tóm tắt một số Java platform class quan trọng triển khai **Comparable**:

```
    Class       |       Natural Ordering
----------------|--------------------------------------
Byte	        | Số có dấu.
Character   	| Số không dấu.
Short	        | Số có dấu.
Integer	        | Số có dấu.
Long	        | Số có dấu.
Float	        | Số có dấu.
Double	        | Số có dấu.
BigInteger	    | Số có dấu.
BigDecimal	    | Số có dấu.
Boolean	        | Boolean.FALSE < Boolean.TRUE
File	        | Thứ tự từ điển phụ thuộc System trên path name.
String	        | Thứ tự từ điển.
Date	        | Thứ tự thời gian
CollationKey	| Thứ tự từ điển theo Locale-specific
```

Nếu bạn cố gắng sắp xếp một list, các elements trong list đó không triển khai *Comaparable*, thì *Collections.sort(list)* sẽ ném ra một *ClassCastException*. Tương tự, *Collections.sort(list, comparator)* sẽ ném một *ClassCastException* nếu bạn cố gắng sắp xếp một list mà các elements của nó không thể được so sánh với nhau bằng cách sử dụng *comparator*. Các elements có thể được so sánh với nhau được gọi là *mutually comparable*. Mặc dù các elements của các types khác nhau có thể là mutually comparable, nhưng không có class nào được liệt kê ở đây cho phép so sánh giữa các class.


### Writing Your Own Comparable Types

[Comparable<T>] interface bao gồm một method duy nhất: **int compareTo(T)**

```java
public interface Comparable<T> {
    public int compareTo(T o);
}
```

Chi tiết như sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int	              | compareTo(T o)
                  | - So sánh object này với object được chỉ định theo thứ tự.
                  | - Trả về số nguyên âm, 0 hoặc số nguyên dương tương ứng với object này nhỏ hơn,
                  |   bằng hoặc lớn hơn object đã chỉ định.
                  | - Ném NullPointerException, nếu object được chỉ định là null.
                  | - Ném ClassCastException, nếu do type mà object được chỉ định không thể so sánh
                  |   với object này.
                  | - x.compareTo(y) phải ném một exception nếu y.compareTo(x) ném một exception.
```

*Ví dụ: Name class triển khai Comparable interface, đại diện cho tên của một người:*

```java
import java.util.*;

public class Name implements Comparable<Name> {
    private final String firstName, lastName;

    public Name(String firstName, String lastName) {
        if (firstName == null || lastName == null)
            throw new NullPointerException();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String firstName() { return firstName; }
    public String lastName()  { return lastName;  }

    public boolean equals(Object o) {
        if (!(o instanceof Name))
            return false;
        Name n = (Name) o;
        return n.firstName.equals(firstName) && n.lastName.equals(lastName);
    }

    public int hashCode() {
        return 31*firstName.hashCode() + lastName.hashCode();
    }

    public String toString() {
	return firstName + " " + lastName;
    }

    public int compareTo(Name n) {
        int lastCmp = lastName.compareTo(n.lastName);
        return (lastCmp != 0 ? lastCmp : firstName.compareTo(n.firstName));
    }
}
```

*Trong ví dụ trên có một số điểm cần lưu ý sau:*

- Name objects là immutable, bởi vì Name class ghi đè equals method để so sánh bằng 2 Name objects dựa vào content của các objects chứ không phải references của chúng, nên khi chúng là các elements của Set hoặc các keys của Map, thì cần phải là immutable type để tránh phá hỏng collection khi chúng bị sửa đổi.  
- Constructor kiểm tra các đối số của nó để tìm null. Điều này đảm bảo rằng tất cả các Name objects đều được định dạng tốt để không có methods nào khác sẽ ném ra NullPointerException.  
- hashCode method được định nghĩa lại. Điều này là cần thiết cho bất kỳ lớp nào định nghĩa lại equals method.  
- equals method trả về false nếu object được chỉ định là null hoặc có type không thích hợp. CompareTo method ném một runtime exception trong những trường hợp này.  
- toString method được định nghĩa lại để in Name ở dạng con người có thể đọc được. Các toString method của các collection types khác nhau phụ thuộc vào toString method của các elements, keys và values của chúng.

*Để kiểm chứng cách compareTo trong ví dụ trên hoạt động, xem chương trình sau:*

```java
import java.util.*;

public class NameSort {
    public static void main(String[] args) {
        Name nameArray[] = {
            new Name("John", "Smith"),
            new Name("Karl", "Ng"),
            new Name("Jeff", "Smith"),
            new Name("Tom", "Rich")
        };

        List<Name> names = Arrays.asList(nameArray);
        Collections.sort(names);
        System.out.println(names);
    }
}

// Output: [Karl Ng, Tom Rich, Jeff Smith, John Smith]
```


### Comparators

Nếu bạn muốn sắp xếp một số objects theo một thứ tự khác với thứ tự tự nhiên của chúng, hoặc nếu bạn muốn sắp xếp một số objects không triển khai *Comparable*, bạn sẽ cần cung cấp một **Comparator** - một objects đóng gói một thứ tự. 

`Comparator<T>` là một functional interface, bao gồm một functional method duy nhất: **int compare(T, T)**  

```java
public interface Comparator<T> {
    int compare(T o1, T o2);
}
```

Chi tiết như sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
int	              | compare(T o1, T o2)
                  | - So sánh 2 arguments theo thứ tự.
                  | - Trả về số nguyên âm, 0 hoặc số nguyên dương tương ứng với 1st argument nhỏ hơn,
                  |   bằng hoặc lớn hơn 2nd argument.
                  | - Ném NullPointerException, nếu một argument là null.
                  | - Ném ClassCastException, nếu do type mà 2 argument không thể so sánh với nhau.
                  | - compare(x, y) phải ném một exception nếu compare(y, x) ném một exception.
```

*Ví dụ: Giả sử bạn có Employee class như sau:*

```java
public class Employee implements Comparable<Employee> {
    public Name name()     { ... }
    public int number()    { ... }
    public Date hireDate() { ... }
}
```

*Trong ví dụ trên, giả sử rằng thứ tự tự nhiên của các Employee instances là theo tên nhân viên. Ông chủ yêu cầu một danh sách nhân viên theo thứ tự thâm niên:*

```java
import java.util.*;

public class EmpSort {
    static final Comparator<Employee> SENIORITY_ORDER =  
        new Comparator<Employee>() {
            public int compare(Employee e1, Employee e2) {
                return e2.hireDate().compareTo(e1.hireDate());
            }
        };

    // Employee database
    static final Collection<Employee> employees = ... ;

    public static void main(String[] args) {
        List<Employee> e = new ArrayList<Employee>(employees);
        Collections.sort(e, SENIORITY_ORDER);
        System.out.println(e);
    }
}
```

*Trong ví dụ trên, việc so sánh dựa theo thứ tự tự nhiên của Date, áp dụng cho giá trị được trả về bởi hireDate accessor method.*

*Comparator* trong chương trình trên hoạt động tốt để sắp xếp một *List*, nhưng nó có một thiếu sót: Nó không thể được sử dụng để sắp xếp một sorted collection, chẳng hạn như *TreeSet*, vì nó tạo ra một thứ tự không tương thích với *equals*. 

*Ví dụ: bất kỳ 2 nhân viên nào được thuê vào cùng một ngày sẽ được so sánh là ngang bằng nhau. Khi bạn sắp xếp một List, điều này không quan trọng; nhưng khi bạn đang sử dụng Comparator để sắp xếp một sorted collection, thì điều đó rất nguy hiểm. Nếu bạn sử dụng Comparator này để chèn nhiều nhân viên được thuê vào cùng một ngày vào TreeSet, chỉ người đầu tiên sẽ được thêm vào set; element thứ hai sẽ được coi là một element trùng lặp và sẽ bị bỏ qua.*

Để khắc phục sự cố này, chỉ cần tinh chỉnh *Comparator* để nó tạo ra thứ tự tương thích với *equals*. Nói cách khác, hãy điều chỉnh nó để các element duy nhất được coi là bằng nhau khi sử dụng *compare*, cũng là các element được coi là bằng nhau khi so sánh bằng cách sử dụng *equals*. Cách để làm được điều này là thực hiện một two-part comparison. 

*Đối với ví dụ trên: 1st part là ngày thuê - và 2nd part là một attribute xác định object duy nhất. Ở đây, employee number là attribute hiển nhiên:*

```java
static final Comparator<Employee> SENIORITY_ORDER = 
    new Comparator<Employee>() {
        public int compare(Employee e1, Employee e2) {
            int dateCmp = e2.hireDate().compareTo(e1.hireDate());
            if (dateCmp != 0)
                return dateCmp;

            return (e1.number() < e2.number() ? -1 :
                (e1.number() == e2.number() ? 0 : 1));
        }
    };
```