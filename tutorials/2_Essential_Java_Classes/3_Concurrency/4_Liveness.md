# Lession 3. Concurrency

## 4. Liveness

Khả năng thực thi kịp thời của một concurrent application (ứng dụng đồng thời) được gọi là *liveness* của nó. 


### 4.1, Deadlock

*Deadlock* mô tả một tình huống mà 2 hoặc nhiều threads bị chặn mãi mãi, chờ đợi nhau.

*Ví dụ: Alphonse và Gaston là bạn bè và là những người rất tôn trọng phép lịch sự. Một quy tắc lịch sự nghiêm ngặt là khi bạn cúi chào một người bạn, bạn phải cúi đầu cho đến khi bạn của bạn có cơ hội để cúi chào lại. Thật không may, quy tắc này không tính đến khả năng 2 người bạn có thể cúi đầu chào nhau cùng một lúc. Ứng dụng ví dụ này, Deadlock, mô hình hóa khả năng này:*

```java
public class Deadlock {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public synchronized void bow(Friend bower) {
            System.out.format(
                "%s: %s has bowed to me!%n", 
                this.name, bower.getName()
            );
            bower.bowBack(this);
        }
        public synchronized void bowBack(Friend bower) {
            System.out.format(
                "%s: %s has bowed back to me!%n",
                this.name, bower.getName()
            );
        }
    }

    public static void main(String[] args) {
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
        new Thread(new Runnable() {
            public void run() { alphonse.bow(gaston); }
        }).start();
        new Thread(new Runnable() {
            public void run() { gaston.bow(alphonse); }
        }).start();
    }
}
```

*Khi Deadlock chạy, rất có thể cả 2 threads sẽ chặn nhau khi chúng cố gắng gọi bowBack. Việc chặn này sẽ không bao giờ kết thúc, bởi vì mỗi thread đang đợi thread kia thoát ra khỏi bow.*


### 4.2, Starvation and Livelock

Starvation và livelock là một vấn đề ít phổ biến hơn nhiều so với deadlock, nhưng vẫn là những vấn đề mà mọi nhà thiết kế phần mềm đồng thời có thể gặp phải.


#### *Starvation*

*Starvation* mô tả một tình huống trong đó một thread không thể truy cập thường xuyên vào các tài nguyên được chia sẻ và không thể tiến bộ. Điều này xảy ra khi các tài nguyên được chia sẻ không khả dụng trong thời gian dài bởi các threads "tham lam". 

*Ví dụ, giả sử một object cung cấp một synchronized method thường mất nhiều thời gian để trả về. Nếu một thread thường xuyên gọi method này, các threads khác cũng cần truy cập đồng bộ thường xuyên vào cùng một object thường sẽ bị chặn.*


#### *Livelock*

Một thread thường hoạt động để đáp lại hành động của một thread khác. Nếu hành động của một thread khác cũng là phản hồi đối với hành động của một thread khác nữa, thì có thể dẫn đến *livelock*. Cũng giống như deadlock, các threads đã bị livelock không thể tiến bộ thêm nữa. Tuy nhiên, các threads không bị chặn - chúng chỉ đơn giản là quá bận rộn để phản hồi lẫn nhau để tiếp tục công việc. 

*Ví dụ: 2 người cố gắng vượt qua nhau trong một hành lang: Alphonse di chuyển sang trái để Gaston vượt qua, trong khi Gaston di chuyển sang phải để Alphonse vượt qua. Nhận thấy rằng họ vẫn đang chặn nhau, Alphone di chuyển sang bên phải của mình, trong khi Gaston di chuyển sang bên trái của mình. Họ vẫn đang chặn nhau, vì vậy ...*