# Lession 3. Implementations

## 7. Convenience Implementations

Section này mô tả một số mini-implementations có thể thuận tiện hơn và hiệu quả hơn các general-purpose implementations khi bạn không cần toàn bộ sức mạnh của chúng. Tất cả các implementations trong section này được tạo sẵn thông qua các static factory methods thay vì các public classes.


### 7.1, List View of an Array

**Arrays.asList** method trả về một List view của array argument của nó. Các thay đổi đối với List ghi vào array và ngược lại. Kích thước của collection là kích thước của array và không thể thay đổi. Nếu add hoặc remove method được gọi trong List, thì một UnsupportedOperationException sẽ được ném ra.

Implementation này là cầu nối giữa các array-based và collection-based APIs, nó cho phép bạn truyền một array cho một method mong đợi một Collection hoặc một List. 

Tuy nhiên, implementation này cũng có một cách sử dụng khác: nếu bạn cần một fixed-size List, thì nó hiệu quả hơn bất kỳ general-purpose List implementation nào.

```java
List<String> list = Arrays.asList(new String[size]);
```

Lưu ý rằng một reference đến backing array không được giữ lại.


### 7.2, Immutable Multiple-Copy List

Đôi khi, bạn sẽ cần một immutable List chứa nhiều bản copy của cùng một element. **Collections.nCopies** method trả về một List như vậy. 

Implementation này có 2 cách sử dụng chính: Đầu tiên là sử dụng để khởi tạo giá trị cho một List vừa mới được tạo; 

*Ví dụ: giả sử bạn muốn một ArrayList ban đầu bao gồm 1.000 null elements:*

```java
List<Type> list = new ArrayList<Type>(Collections.nCopies(1000, (Type) null));
```

Cách sử dụng thứ 2 là phát triển một List hiện có. 

*Ví dụ: giả sử bạn muốn thêm 69 bản copy của string "fruit bat" vào cuối `List<String>`:*

```java
lovablePets.addAll(Collections.nCopies(69, "fruit bat"));
```


### 7.3, Immutable Singleton Set

Đôi khi bạn sẽ cần một *immutable singleton Set*, bao gồm một element duy nhất được chỉ định. **Collections.singleton** method trả về một Set như vậy. 

Một cách sử dụng của implementation này là xóa tất cả các lần xuất hiện của một element được chỉ định khỏi một Collection:

```java
c.removeAll(Collections.singleton(e));
```

Một cách tương tự sẽ xóa tất cả các elements ánh xạ đến một value được chỉ định khỏi Map. 

*Ví dụ: giả sử bạn có Map - job - ánh xạ mọi người đến dòng công việc của họ, và giả sử bạn muốn xóa bỏ tất cả các luật sư:*

```java
job.values().removeAll(Collections.singleton(LAWYER));
```

Một cách sử dụng nữa của implementation này là cung cấp một input value duy nhất cho một method được viết để chấp nhận một collection của các giá trị.


### 7.4, Empty Set, List, and Map Constants

**Collections** class cung cấp các methods để trả về các empty Set, List, và Map - **emptySet**, **emptyList**, và **emptyMap**. Cách sử dụng chính của các constants này là làm input cho các methods nhận một Collection của các giá trị khi bạn không muốn cung cấp bất kỳ giá trị nào:

```java
tourist.declarePurchases(Collections.emptySet());
```