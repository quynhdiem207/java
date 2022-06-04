## 1. Buffer là gì?

Buffer là vùng lưu trữ dữ liệu tạm thời và thường được lưu trữ trong bộ nhớ tạm (RAM). Công nghệ này hiện nay được áp dụng rất nhiều trên các website nghe nhạc, xem phim hay các ứng dụng livestream.


## 2. Các ứng dụng của Buffer

Ví dụ khi bạn xem video trực tuyến hay nghe nhạc trực tuyến thì có hai cách để trình duyệt tải dữ liệu này:

- Tải hết toàn bộ dữ liệu của video, nhạc rồi mới chạy.  
- Tải từng phần của video, nhạc và chạy từng phần nôi dung mỗi khi dữ liệu được tải về máy. Ta có thể hiểu là khi này data của toàn bộ video hay nhạc được băm nhỏ rồi tải về lưu trong bộ nhớ tạm của trình duyệt, player của trình duyệt sẽ lấy dữ liệu đã tải này xử lý thành âm thanh hình ảnh rồi phát cho bạn xem. Dữ liệu tải đến đâu thì play đến đấy, nếu bạn xem nhanh quá thì phải chờ dữ liệu được tải thêm cho đến khi hoàn thành.  

Với cách thứ hai thì từng phần dữ liệu video, nhạc được chia nhỏ tải về máy được gọi là buffer.


## 3. Vai Trò Của Buffer (Và Tại Sao Cần Sử Dụng Buffer)

Cách đầu tiên khi ta tải video của trình duyệt ở trên sẽ khiến người dùng phải chờ đợi một thời gian trước khi dữ liệu của toàn bộ video được tải về toàn bộ. Trong trường hợp dung lượng đoạn video có kích cỡ lớn (dài vài giờ đồng hồ có thể lên đến cả Gb) thì cách làm này sẽ khiến người dùng phải đợi rất lâu để có thể bắt đầu xem video. Thường thì cách này được ứng dụng từ xa xưa khi các công nghệ streamming chưa có.

Cách làm thứ hai thì người dùng có thể xem ngay nội dung video khi từng phần chia nhỏ dữ liệu của video (buffer) được tải xuống máy. Trường hợp tốc độ tải về từng phần nhỏ dữ liệu này nhanh hơn tốc độ xem video của người dùng thì khi đó người dùng sẽ có thể coi video một cách liên tục mà không bị giật.


## 4. Cache là gì?

Cache là kỹ thuật lưu lại những dữ liệu đã được xử lý vào 1 bộ nhớ tạm. Bộ nhớ tạm này sẽ có tốc độ truy suất nhanh (RAM, hoặc local storage của client). Những lần sau cần dùng thông tin thì chỉ cần truy suất ngay từ bộ nhớ tạm mà không cần phải làm thêm gì.


## 5. Sự khác biệt giữa Buffer và Cache?

Buffer giống Cache ở điểm là nó cũng lưu data ở bộ nhớ tạm. Tuy nhiên Buffer được sử dụng chủ yếu để giảm thời gian chờ giữa việc nhận và xử lý dữ liệu bởi một thiết bị nào đó, data được băm nhỏ, tải đến đâu xử lý đến đó.

Cache được sử dụng dựa trên nguyên tắc cùng một dữ liệu sẽ được truy cập nhiều lần do đó data được lưu trữ trong cache sẽ làm giảm phần lớn thời gian truy cập, đỡ phải tải dữ liệu lại một lần nữa.