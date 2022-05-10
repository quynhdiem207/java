# Chapter 14: Blocks and Statements

Trình tự thực thi một chương trình được điều khiển bởi các statements.

Một số statements chứa các statements khác hoặc các expressions dưới dạng một phần trong cấu trúc của chúng.


## 1. Normal and Abrupt Completion of Statements

Mọi statement đều có một chế độ thực thi bình thường, trong đó các bước tính toán nhất định được thực hiện.

Nếu tất cả các bước được thực thi như mô tả, không có dấu hiệu hoàn thành đột ngột, thì statement được coi là hoàn thành bình thường (*complete normally*). Tuy nhiên, một số events có thể ngăn một statement hoàn thành bình thường:  

- *break*, *continue*, và *return* statements gây ra việc chuyển quyền điều khiển có thể ngăn cản các statements chứa chúng hoàn thành bình thường.  
- Đánh giá một số expressions nhất định có thể ném các exceptions từ JVM. Một *throw* statement tường minh cũng có thể dẫn đến một exception. Một exception gây ra việc chuyển quyền điều khiển có thể ngăn cản các statements hoàn thành bình thường.  

Nếu một event như vậy xảy ra, thì việc thực thi của một hoặc nhiều statements có thể bị chấm dứt trước khi tất cả các bước của chế độ thực thi bình thường của chúng được hoàn thành; các statements như vậy được coi là hoàn thành đột ngột (*complete abruptly*).

Một abrupt completion luôn có một lý do, đó là:  

- Một *break* không có label,  
- Một *break* có một label đã cho,  
- Một *continue* không có label,  
- Một *continue* có một label đã cho,  
- Một *return* không có value,  
- Một *return* có một value đã cho,  
- Một *throw* có một value đã cho, bao gồm các exceptions được ném bởi JVM.  

Thuật ngữ "complete normally" và "complete abruptly" cũng được áp dụng cho việc đánh giá (evaluation) các expressions. Lý do duy nhất mà một expression có thể hoàn thành đột ngột là một exception được ném ra, vì một throw với value đã cho hoặc một run-time exception hoặc error.  

Nếu một statement đánh giá một expression, thì expression hoàn thành đột ngột luôn khiến cho statement đó hoàn thành đột ngột ngay lập tức, với cùng lý do. Tất cả các bước tiếp theo trong chế độ thực thi bình thường sẽ không được thực thi.

Trừ khi có quy định khác, nếu không một substatement hoàn thành đột ngột luôn khiến cho statement đó hoàn thành đột ngột ngay lập tức, với cùng lý do. Tất cả các bước tiếp theo trong chế độ thực thi bình thường của statement đó sẽ không được thực thi.

Trừ khi có quy định khác, nếu không một statement hoàn thành bình thường nếu tất cả các expressions mà nó đánh giá và tất cả các substatements mà nó thực thi đều hoàn thành bình thường.


## 2. Blocks

Một *block* là một chuỗi các statements, local class declarations, và local variable declaration statements bên trong một cặp dấu ngoặc {}.

Một block được thực thi bằng cách thực thi từng local variable declaration statements và các statements khác theo thứ tự từ đầu đến cuối (trái sang phải). Nếu tất cả các statements của block hoàn thành bình thường, thì block ddoshoanf thành bình thường. Nếu bất kỳ statements nào của block hoàn thành đột ngột vì bất kỳ lý do nào, thì block đó hoàn thành đột ngột vì cùng một lý do.


## 3. Local Class Declarations

Một *local class* là một nested class mà không phải một member của bất kỳ class nào và có tên.

Tất cả các local classes đều là inner classes.

Mọi local class declaration statement đều được chứa tức thời bởi một block.

Sẽ gây ra compile-time error nếu: một local class declaration chứa bất kỳ access modifiers *public*, *protected*, hay *private*, hoặc modifier *static*.

*Ví dụ 1. Local Class Declarations*

```java
class Global {
    class Cyclic {}

    void foo() {
        new Cyclic(); // tạo một instance của Global.Cyclic không phải của local class Cyclic, 
            // vì statement xuất hiện trước scope của local class declaration. 

        class Cyclic extends Cyclic {} // circular definition
            // Thực tế scope của một local class declaration bao gồm toàn bộ declaration của nó,
            // (không chỉ là body của nó), nghĩa là definition của local class Cyclic là tuần hoàn,
            // vì nó extends chính nó, chứ không phải Global.Cyclic. 
            // Vì vậy, declaration của local class Cyclic bị từ chối tại compile time.

        {
            class Local {}
            {
                class Local {} // compile-time error
            }
            class Local {} // compile-time error
                // Vì tên của local class không thể được khai báo lại trong cùng method
                // (hoặc constructor hoặc initializer)
            
            class AnotherLocal {
                void bar() {
                    class Local {} // ok
                        // Tuy nhiên, Local có thể được khai báo lại trong context của class lồng sâu hơn.
                }
            }

        }

        class Local {} // ok, not in scope of prior Local
            // declaration này hợp lệ, vì nó xảy ra bên ngoài scope của declaration of class Local trước đó.
    }
}
```


## 4. Local Variable Declaration Statements

Một *local variable declaration* statement khai báo một hoặc nhiều local variable names.

```
- LocalVariableDeclarationStatement: LocalVariableDeclaration ;
- LocalVariableDeclaration:          {VariableModifier} UnannType VariableDeclaratorList
- VariableModifier:
    (one of)
    Annotation final
- VariableDeclaratorList: VariableDeclarator {, VariableDeclarator}
- VariableDeclarator:     VariableDeclaratorId [= VariableInitializer]
- VariableDeclaratorId:   Identifier [Dims]
- Dims:                   {Annotation} [ ] {{Annotation} [ ]}
- VariableInitializer:
    Expression
    ArrayInitializer
```

Mọi local variable declaration statement đều được chứa tức thời trong một block.

Ngoài các local variable declaration statements, một local variable declaration có thể xuất hiện trong phần header của một for statement hoặc try-with-resources statement.


### 4.1. Local Variable Declarators and Types

Mỗi declarator trong một local variable declaration khai báo một local variable, có tên là Identifier.

Nếu keyword tùy chọn *final* xuất hiện khi bắt đầu khai báo, thì variable được khai báo là một final variable.


### 4.2. Execution of Local Variable Declarations

Một local variable declaration statement là một câu lệnh thực thi. Mỗi lần nó được thực thi, các declarators được xử lý theo thứ tự từ trái sang phải. Nếu một declarator có một initializer, thì initializer sẽ được đánh giá và giá trị của nó được gán cho variable.

*Note: Nếu một declarator không có một initializer, thì local variable đó phải được gán trước bất kỳ tham chiếu nào tới variable đó.*

Mọi initializer (ngoại trừ initializer đầu tiên) chỉ được đánh giá nếu quá trình đánh giá của initializer phía trước hoàn thành bình thường.

Việc thực thi của local variable declaration chỉ hoàn thành bình thường nếu quá trình đánh giá của initializer cuối cùng hoàn thành bình thường.

Nếu local variable declaration không chứa các initializers, thì việc thực thi nó luôn hoàn thành bình thường.


## 5. The assert Statement

*Assertion* (xác nhận) cho phép kiểm tra tính đúng đắn của bất kỳ giả định nào trong chương trình, được sử dụng cho mục đích thử nghiệm trong quá trình phát triển để phát hiện lỗi mà programmer cho là đúng. 

Một *assert statement* chứa một *boolean expression* (điều kiện giả định được cho là đúng). 

```
- AssertStatement:
    assert Expression ;
    assert Expression : Expression ;
```

Sẽ gây ra compile-time error nếu:  

- Expression1 trong assert statement không có type boolean hoặc Boolean.  
- Expression2 trong assert statement là void.  

Mặc định, assertion là disabled (vô hiệu hóa), có thể kích hoạt chúng bằng command line.

- Nếu assertion là disabled, assert statement sẽ bị bỏ qua khi thực thi.  
- Nếu assertion là enabled, assert statement sẽ được thực thi, gây ra việc đánh giá boolean expression, và nếu kết quả đánh giá là false, thì JVM sẽ tạo và ném ra một instance của AssertionError (kèm theo detail message là giá trị kết quả của việc đánh giá Expression2 nếu Expression2 hiện diện).  

Nếu việc đánh giá Expression, hay thực hiện unboxing (nếu có) hoàn thành đột ngột vì lý do nào đó, thì assert statement hoàn thành đột ngột vì lý do đó.  

```java
class Main {
    public static void main(String[] args) {
        String[] weekends = ["Fri", "Sar", "Sun"];
        assert weekends.length == 2;
        System.out.println("Weekend have " + weekends.length + " days");
    }
}

// Output: 
// - Assertion is disabled: Weekend have 3 days
// - Assertion is enabled:  Exception in thread "main" java.lang.ExceptionError
```


## 6. Labeled Statements

Các statements có thể có prefix *label*.

```
- LabeledStatement:
    Identifier : Statement
```

Label được sử dụng với *break* hoặc *continue* statements xuất hiện ở bất cứ đâu trong labeled statement.  

Nếu statement được gắn label bởi một identifier, và statement chứa bên trong hoàn thành đột ngột bởi vì một break với cùng Identifier, thì labeled statement đó hoàn thành bình thường. Trong các trường hợp hoàn thành đột ngột khác của statement chứa bên trong, thì labeled statement hoàn thành đột ngột vì cùng lý do.

*Ví dụ 1. Labels and Identifiers*

```java
class Test {
    char[] value;
    int offset, count;
    int indexOf(TestString str, int fromIndex) {
        char[] v1 = value, v2 = str.value;
        int max = offset + (count - str.count);
        int start = offset + ((fromIndex < 0) ? 0 : fromIndex);
        // Labeled Statement:
        // label cùng tên với local variable i không che khuất label trong scope của declaration của i
        i:
            for (int i = start; i <= max; i++) {
                int n = str.count, j = i, k = str.offset;
                while (n-- != 0) {
                    if (v1[j++] != v2[k++])
                        continue i;
                } 
                return i - offset;
            }
        return -1;
    }
}
```


## 7. The if Statement

*if* statement cho phép thực thi có điều kiện một statement, hoặc một sự lựa chọn có điều kiện của hai statements, thực thi statement này hoặc statement kia.

```
IfThenStatement:      if ( Expression ) Statement1
IfThenElseStatement:  if ( Expression ) Statement1 else Statement2
```

Expression phải có type *boolean* hoặc *Boolean*, nếu không sẽ gây ra compile-time error.

Khi if statement được thực thi, Expression sẽ được đánh giá, việc thực thi tiếp tục phụ thuộc vào kết quả đánh giá (nếu kết quả là Boolen type thì nó sẽ được unboxing):  

- Nếu kết quả đánh giá là true, thì Statement1 được thực thi,  
- Nếu kết quả đánh giá là false, và nếu có else keyword thì Statement2 được thực thi.  

Nếu việc đánh giá Expression, hay thực hiện unboxing (nếu có), hay thực thi các substatements hoàn thành đột ngột vì lý do nào đó, thì if statement hoàn thành đột ngột vì lý do đó.  

```java
class Test {
    void test(int i) {
        if(i > 0)
            System.out.println('Hello');
        else 
            System.out.println('Goodbye');
    }
}
```


## 8. The switch Statement

*switch* statement chuyển quyền điều khiển cho một trong một số statements phụ thuộc vào giá trị của một expression.

```
- SwitchStatement:           switch ( Expression ) SwitchBlock
- SwitchBlock:               { {SwitchBlockStatementGroup} {SwitchLabel} }
- SwitchBlockStatementGroup: SwitchLabels BlockStatements
- SwitchLabels:              SwitchLabel {SwitchLabel}
- SwitchLabel:
    case ConstantExpression :
    case EnumConstantName :
    default :
- EnumConstantName: Identifier
```

Body của switch statement được gọi là *switch block*. Bất kỳ statement nào được chứa tức thời bởi switch block có thể được gắn label với một hoặc nhiều *switch labels*, là các *case* hoặc *default* labels. Mọi case label đều có một *case constant*, là một *constant expression* hoặc tên của một *enum constant*.

Các quy tắc cần tuân thủ của switch statement, nếu không sẽ gây ra compile-time error:  

- Type của Expression phải là *char, byte, short, int, Character, Byte, Short, Integer, String, hoặc một enum type*.  
- Mọi case constant phải tương thích gán với type của Expression.  
- Nếu type của Expression là một enum type, thì mọi case constant phải là một enum constant của type đó.  
- Hai case constants không thể có cùng giá trị.  
- Case constant không thể là null.  
- Chỉ có thể có nhiều nhất một default label.  

Khi switch statement được thực thi, trước tiên Expression được đánh giá:  

- Nếu kết quả đánh giá là null, thì một NullPointerException sẽ được ném ra.  
- Nếu không, nếu kết quả đánh giá là một reference type trong các wrapper class, thì nó sẽ được thực hiện unboxing conversion. Việc thực thi tiếp tục dựa vào việc so sánh kết quả đánh giá Expression và các case constants:  
    + Nếu một trong các case constants bằng với giá trị của Expression, thì tất cả các substatements sau case label đó được thực thi tuần tự.  
    + Nếu không có case constants nào bằng với giá trị của Expression, thì nếu có một default label, thì tất cả các substatements sau default label đó được thực thi tuần tự.  

Nếu bất kỳ statement nào được chứa tức thời bởi switch block hoàn thành đột ngột, thì nó được xử lý như sau:  

- Nếu việc thực thi của Statement hoàn thành đột ngột vì một *break* không có label, thì switch statement hoàn thành bình thường.  
- Nếu việc thực thi của Statement hoàn thành đột ngột vì các lý do khác, thì switch statement hoàn thành đột ngột vì lý do đó.  

```java
// CASE 1:
class TooMany {
    static void howMany(int k) {
        switch (k) {
            case 1: System.out.print("one ");
            case 2: System.out.print("too ");
            case 3: System.out.println("many");
        }
    }
    public static void main(String[] args) {
        howMany(3); // many
        howMany(2); // too many
        howMany(1); // one two many
    }
}

// CASE 2:
class TwoMany {
    static void howMany(int k) {
        switch (k) {
            case 1: System.out.println("one");
                    break;  // exit the switch
            case 2: System.out.println("two");
                    break;  // exit the switch
            case 3: System.out.println("many");
                    break;  // not needed, but good style
        }
    }
    public static void main(String[] args) {
        howMany(1); // one
        howMany(2); // two
        howMany(3); // many
    }
}
```


## 9. The while Statement

*while* statement thực thi một Expression và một Statement lặp đi lặp lại cho đến khi giá trị của Expression là false.

```
- WhileStatement:
    while ( Expression ) Statement
```

Expression phải có type boolean hoặc Boolean, nếu không sẽ gây ra compile-time error.

while statement được thực thi, trước tiên sẽ đánh giá Expression. Nếu kết quả đánh giá là type Boolean, sẽ thực hiện unboxing conversion. Quá trình thực thi sẽ tiếp tục dựa trên giá trị của Expression:  

- Nếu giá trị là true, thì Statement được thực thi. Sau đó, while statement được thực thi lại, bắt đầu với việc đánh giá lại Expression.  
- Nếu giá trị là false, while statement hoàn thành bình thường.  

```java
class TwoMany {
    public static void main(String[] args) {
        int i = args.length;
        while (i > 0) {
            i--;
            System.out.println(args[i]);
        }
    }
}
```


### 9.1. Abrupt Completion of while Statement

Nếu Statement hoàn thành đột ngột được xử lý như sau:

- Nếu Statement hoàn thành đột ngột vì một break không có label, thì while statement hoàn thành bình thường.  
- Nếu Statement hoàn thành đột ngột vì một continue không có label, thì while statement được thực thi lại.  
- Nếu Statement hoàn thành đột ngột vì continue với label L, thì:  
    + Nếu while statement có label L, thì while statement được thực thi lại.  
    + Nếu while statement không có label L, thì while statement hoàn thành đột ngột vì một continue với label L.  
- Nếu Statement hoàn thành đột ngột vì bất kỳ lý do nào khác, thì while statement hoàn thành đột ngột vì cùng lý do.  


## 10. The do Statement

*do* statement thực thi một Statement và một Expression lặp đi lặp lại cho đến khi giá trị của Expression là false.

```
- DoStatement:
    do Statement while ( Expression ) ;
```

Expression phải có type boolean hoặc Boolean, nếu không sẽ gây ra compile-time error.

Một do statement được thực thi, thì trước tiên sẽ thực thi Statemen. Nếu Statement hoàn thành bình thường, thì Expression được đánh giá. Nếu kết quả là type Boolean, nó sẽ được unboxing. Việc thực thi tiếp theo dự vào giá trị của Expression:  

- Nếu giá trị là true, thì do statement được thực thi lại.  
- Nếu giá trị là false, thì do statement hoàn thành bình thường.


### 10.1. Abrupt Completion of do Statement

Nếu Statement hoàn thành đột ngột được xử lý như sau:  

- Nếu Statement hoàn thành đột ngột vì một break không có label, thì do statement hoàn thành bình thường.  
- Nếu Statement hoàn thành đột ngột vì một continue không có label, thì Expression được đánh giá. Việc thực thi tiếp tục dựa vào kết quả đánh giá:  
    + Nếu giá trị là true, thì do statement được thực thi lại.  
    + Nếu giá trị là false, thì do statement hoàn thành bình thường.  
- Nếu Statement hoàn thành đột ngột vì continue với label L, thì:  
    + Nếu do statement có label L, thì Expression được đánh giá. Việc thực thi tiếp tục dựa vào kết quả đánh giá:  
        + Nếu giá trị là true, thì do statement được thực thi lại.  
        + Nếu giá trị là false, thì do statement hoàn thành bình thường.  
    + Nếu do statement không có label L, thì do statement hoàn thành đột ngột vì một continue với label L.  
- Nếu Statement hoàn thành đột ngột vì bất kỳ lý do nào khác, thì do statement hoàn thành đột ngột vì cùng lý do.  

```java
public static String toHexString(int i) {
    StringBuffer buf = new StringBuffer(8);
    do {
        buf.append(Character.forDigit(i & 0xF, 16));
        i >>>= 4;
    } while (i != 0);
    return buf.reverse().toString();
}
```


## 11. The for Statement

*for* statement có 2 dạng:  

- for statement cơ bản,  
- for statement nâng cao.  


### 11.1. The basic for Statement

*for* statement cơ bản thực thi một số initialization code, sau đó thực thi một Expression, một Statement, và một số update code lặp đi lặp lại cho đến khi giá trị của Expression là false.

```
- BasicForStatement:
    for ( [ForInit] ; [Expression] ; [ForUpdate] ) Statement
- ForInit:
    StatementExpressionList
    LocalVariableDeclaration
- ForUpdate:
    StatementExpressionList
- StatementExpressionList:
    StatementExpression {, StatementExpression}
```

Expression phải có type boolean hoặc Boolean, nếu không sẽ gây ra compile-time error.


#### *11.1.1. Initialization of for Statement*

for statement được thực thi, trước tiên sẽ thực thi ForInit code:  

- Nếu ForInit code là một list các statement expressions, thì các expressions được đánh giá tuần tự từ trái sang phải; giá trị của chúng (nếu có) sẽ bị loại bỏ.  
- Nếu ForInit code là một local variable declaration, sẽ được thực thi như thể xuất hiện trong một block.  

Nếu thực thi ForInit code hoàn thành đột ngột vì một lý do nào đó, thì for statement hoàn thành đột ngột vì cùng lý do.  


#### *11.1.2. Iteration of for Statement*

Tiếp theo, một bước lặp for được thực thi như sau:  

- Nếu Expression hiện diện, nó được đánh giá. nếu kết quả là type Boolean, sẽ được unboxing.  
- Nếu Expression không hiện diện, hoặc nó hiện diện và kết quả đánh giá là true, thì Statement được thực thi. Nếu Statement hoàn thành bình thường, thì thực hiện tuần tự 2 bước sau:  
    + 1, Nếu ForUpdate hiện diện, thì các expressions được đánh giá tuần tự từ trái sang phải; giá trị của chúng (nếu có) bị loại bỏ. Nếu đánh giá bất kỳ expressions nào hoàn thành đột ngột vì một lý do nào đó, thì for statement hoàn thành đột ngột vì cùng lý do.  
    + 2, thực hiện một bước lặp for khác.
- Nếu Expression hiện diện và kết quả đánh giá là false, thì for statement hoàn thành bình thường.

Nếu Expression không hiện diện, thì cách duy nhất để for statement hoàn thành bình thường là sử dụng một break statement.


#### *11.1.3. Abrupt Completion of for Statement*

Nếu Statement hoàn thành đột ngột, sẽ được xử lý như sau:

- Nếu Statement hoàn thành đột ngột vì một break không có label, thì for statement hoàn thành bình thường.  
- Nếu Statement hoàn thành đột ngột vì một continue không có label, thì thực hiện 2 bước sau theo trình tự:  
    + 1, Nếu phần ForUpdate hiện diện, thì Expressions được đánh giá theo thứ tự từ trái sang phải (giá trị chủa chúng nếu có sẽ bị loại bỏ).  
    + 2, Thực hiện một bước lặp for khác.  
- Nếu Statement hoàn thành đột ngột vì continue với label L, thì:  
    + Nếu for statement có label L, thì thực hiện 2 bước sau theo trình tự:  
        + 1, Nếu phần ForUpdate hiện diện, thì Expressions được đánh giá theo thứ tự từ trái sang phải (giá trị chủa chúng nếu có sẽ bị loại bỏ).  
        + 2, Thực hiện một bước lặp for khác.  
    + Nếu for statement không có label L, thì do statement hoàn thành đột ngột vì một continue với label L.  
- Nếu Statement hoàn thành đột ngột vì bất kỳ lý do nào khác, thì for statement hoàn thành đột ngột vì cùng lý do.  


### 11.2. The enhanced for statement

*for* statement nâng cao có dạng:  

```
- EnhancedForStatement:
    for ( {VariableModifier} TargetType VariableDeclaratorId : Expression ) Statement
```

Type của Expression phải là *Iterable* hoặc một *array type*, nếu không sẽ gây ra compile-time error.

for statement nâng cao có ý nghĩa tương tự với for statement cơ bản như sau:

- Nếu type của Expression là một subtype của Iterable:  
    ```
    for (I #i = Expression.iterator(); #i.hasNext(); ) {
        {VariableModifier} TargetType Identifier = (TargetType) #i.next();
        Statement
    }
    ```  

- Nếu type của Expression là một array type T[]:  
    ```
    T[] #a = Expression;
    for (int #i = 0; #i < #a.length; #i++) {
        {VariableModifier} TargetType Identifier = #a[#i];
        Statement
    }
    ```  

*Ví dụ:*

```java
int sum(int[] a) {
    int sum = 0;
    for (int i : a) sum += i;
    return sum;
}
```
