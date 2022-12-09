# The BaseStream Interface

*BaseStream* là interface cơ sở cho các streams, là chuỗi các elements hỗ trợ các sequential và parallel aggregate operations.

```java
public interface BaseStream<T,S extends BaseStream<T,S>> extends AutoCloseable {}
```

Các subinterface của BaseStream bao gồm: DoubleStream, IntStream, LongStream, Stream.

BaseStream interface hỗ trợ các methods sau:

```
Modifier and Type |                        Method and Description
------------------|--------------------------------------------------------------------------------
void	          | close()
                  | - Đóng stream này, khiến tất cả các close handlers cho stream pipeline này được
                  |   gọi.
------------------|--------------------------------------------------------------------------------
boolean	          | isParallel()
                  | - Trả về true nếu stream này thực thi song song một terminal operation.
------------------|--------------------------------------------------------------------------------
S	              | onClose(Runnable closeHandler)
                  | - Trả về một stream tương đương, với một close handler bổ sung mà được chạy nếu
                  |   stream bị đóng. Có thể trả lại chính nó.
                  | - Các close handler được chạy khi close() method được gọi trên stream và được
                  |   thực thi theo thứ tự chúng đã được thêm vào. Tất cả các close handler được chạy,
                  |   ngay cả khi các close handler trước đó ném ra các ngoại lệ. Nếu bất kỳ close
                  |   handler nào ném ra một ngoại lệ, thì ngoại lệ đầu tiên được ném ra sẽ được
                  |   chuyển tiếp đến lệnh gọi close(), mọi ngoại lệ còn lại được thêm vào ngoại lệ
                  |   đó dưới dạng ngoại lệ bị loại bỏ.
                  | - Runnable là một functional interface có "void	run()" method.
                  | - Đây là một intermediate operation.
------------------|--------------------------------------------------------------------------------
S	              | parallel()
                  | - Trả về một parallel stream tương đương.
                  | - Có thể trả lại chính nó, vì stream đã song song, hoặc vì trạng thái stream đã
                  |   được sửa đổi thành song song.
                  | - Đây là một intermediate operation.
------------------|--------------------------------------------------------------------------------
S	              | sequential()
                  | - Trả về một sequential stream tương đương.
                  | - Có thể trả lại chính nó, vì stream đã tuần tự, hoặc vì trạng thái stream đã
                  |   được sửa đổi thành tuần tự.
                  | - Đây là một intermediate operation.
------------------|--------------------------------------------------------------------------------
S	              | unordered()
                  | - Trả về một unordered stream tương đương.
                  | - Có thể trả lại chính nó, vì stream đã không có thứ tự, hoặc vì trạng thái stream
                  |   đã được sửa đổi thành không có thứ tự.
                  | - Đây là một intermediate operation.
------------------|--------------------------------------------------------------------------------
Iterator<T>	      | iterator()
                  | - Trả về một iterator cho các elements của stream này.
                  | - Đây là một terminal operation.
------------------|--------------------------------------------------------------------------------
Spliterator<T>	  | spliterator()
                  | - Trả về một spliterator cho các elements của stream này.
                  | - Đây là một terminal operation.
```