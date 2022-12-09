# Lession 3. Concurrency

## 7.1, Lock Objects

Synchronized code dựa trên một loại reentrant lock (khóa nhập lại) đơn giản. Loại khóa này dễ sử dụng, nhưng có nhiều hạn chế. Các khóa phức tạp hơn được hỗ trợ bởi *java.util.concurrent.locks* package. Chúng ta sẽ không xem xét package này một cách chi tiết, nhưng thay vào đó sẽ tập trung vào interface cơ bản nhất của nó, *Lock*.

Các *Lock* objects hoạt động rất giống với các implicit locks được sử dụng bởi synchronized code. Như với các implicit locks, chỉ một thread có thể sở hữu một Lock object tại một thời điểm. Các Lock objects cũng hỗ trợ một cơ chế *wait/notify*, thông qua các *Condition* objects liên quan của chúng.

Ưu điểm lớn nhất của các Lock objects so với các implicit locks là khả năng quay trở lại sau nỗ lực lấy được khóa. *tryLock* method sẽ trở lại nếu khóa không khả dụng ngay lập tức hoặc trước khi hết thời gian chờ (nếu được chỉ định). *lockInterruptibly* method sẽ trở lại nếu một thread khác gửi một interrupt trước khi khóa được thu nhận.

Hãy sử dụng các Lock objects để giải quyết vấn đề bế tắc mà chúng ta đã thấy trong *Liveness*. 

*Ví dụ: Alphonse và Gaston đã tự rèn luyện khả năng để ý khi một người bạn sắp cúi chào. Chúng tôi mô hình hóa sự cải tiến này bằng cách yêu cầu các Friend objects của chúng tôi phải có khóa cho cả 2 người tham gia trước khi tiếp tục cúi đầu. Đây là mã nguồn của mô hình cải tiến, Safelock. Để chứng minh tính linh hoạt của Lock, chúng tôi giả định rằng Alphonse và Gaston say mê khả năng cúi đầu an toàn mới phát hiện của họ đến nỗi họ không thể ngừng cúi đầu trước nhau:*

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Safelock {
    static class Friend {
        private final String name;
        private final Lock lock = new ReentrantLock();

        public Friend(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public boolean impendingBow(Friend bower) {
            Boolean myLock = false;
            Boolean yourLock = false;
            try {
                myLock = lock.tryLock();
                yourLock = bower.lock.tryLock();
            } finally {
                if (! (myLock && yourLock)) {
                    if (myLock) {
                        lock.unlock();
                    }
                    if (yourLock) {
                        bower.lock.unlock();
                    }
                }
            }
            return myLock && yourLock;
        }
            
        public void bow(Friend bower) {
            if (impendingBow(bower)) {
                try {
                    System.out.format(
                        "%s: %s has bowed to me!%n", 
                        this.name, bower.getName()
                    );
                    bower.bowBack(this);
                } finally {
                    lock.unlock();
                    bower.lock.unlock();
                }
            } else {
                System.out.format(
                    "%s: %s started to bow to me, but saw that"
                    + " I was already bowing to him.%n",
                    this.name, bower.getName()
                );
            }
        }

        public void bowBack(Friend bower) {
            System.out.format(
                "%s: %s has bowed back to me!%n",
                this.name, bower.getName()
            );
        }
    }

    static class BowLoop implements Runnable {
        private Friend bower;
        private Friend bowee;

        public BowLoop(Friend bower, Friend bowee) {
            this.bower = bower;
            this.bowee = bowee;
        }
    
        public void run() {
            Random random = new Random();
            for (;;) {
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {}
                bowee.bow(bower);
            }
        }
    }
            

    public static void main(String[] args) {
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
        new Thread(new BowLoop(alphonse, gaston)).start();
        new Thread(new BowLoop(gaston, alphonse)).start();
    }
}
```