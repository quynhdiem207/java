# --- Topic 1 ---  

+ [1. Client - Server](#1-client---server)  
+ [2. Domain - HTTP](#2-domain---http)  


## 1. Client - Server   

- **Question**:  
    + Cách thức hoạt động của một website?  
    + Sự khác biệt giữa client và server là gì?  

- **Requirements**: Hiểu khái niệm, các hoạt động, có thể vẽ lại được mô hình hoạt động.  

Website hoạt động dựa trên mô hình *Client - Server*.  

Mô hình client - server là mô hình mạng máy tính giúp các máy tính giao tiếp & truyền tải dữ liệu cho nhau dựa trên một chuẩn giao thức (như HTTP) thông qua thông điệp (message), trong đó:  

- Client:  
    + Là máy tính đóng vai trò là máy khách sử dụng tài nguyên được cung cấp từ server,  
    + Thực hiện gửi yêu cầu tới server & đợi phản hồi trả về.  
- Server:  
    + Là máy tính đóng vai trò là máy chủ lưu trữ tài nguyên cung cấp cho client,  
    + Thực hiện tiếp nhận yêu cầu, xử lý & phản hồi lại cho client.  

Sơ đồ làm việc khi truyền tải dữ liệu giữa client & server:  

- Kết nối (mở kết nối - client thiết lập phiên giao tiếp với server hoặc sử dụng kết nối trước đó);  
- Gửi Request message;  
- Gửi Response message;  
- Đóng kết nối hoặc sử dụng lại cho truy vấn khác.  

>        ---- request --->
> Client                    Server
>        <--- response --- 


## 2. Domain - HTTP    

### 2.1, Domain  

- Là địa chỉ của website, hay chính là định danh thay thế cho địa chỉ IP của server giúp dễ nhớ hơn.  
- Vai trò: dễ nhớ  
- cấu trúc:  
    (sub_domain.)domain_name.suffix   

    Trong đó:  
    >- sub_domain:  tên miền phụ,  
    >- domain:      tên miền,  
    >- suffix:      hậu tố  
    >   + second_level_domain.top_level_domain,  
    >   + second_level_domain (SLD): tên miền thứ cấp,  
    >   + top_level_domain (TLD):    tên miền bậc cao  

    eg: fullstack.edu.vn, mail.google.com  

**DNS Server** (Domain Name System Server): hệ thống phân giải tên miền, làm nhiệm vụ phân giải domain thành địa chỉ IP.  

*Cách thức hoạt động*: Khi client thông qua browser truy cập website bằng domain name, thì browser sẽ gửi request đến DNS server để lấy địa chỉ IP của server. Sau đó, gửi request đến web server có IP đã nhận được từ DNS Server. Web server sẽ tiếp nhận request, tiến hành xử lý & phản hồi lại cho client. Browser sẽ render thông tin nhận được từ server ra màn hình.  

### 2.2, HTTP  

*HTTP* là một giao thức giao tiếp, được sử dụng để truyền tải thông tin trên mạng internet.  

- Request  
- Response  
- Header  
- Payload  
- Status code  

#### 2.2.1, HTTP Request  

HTTP Request: là gói tin chứa thông tin yêu cầu gửi từ client tới server.  

HTTP Request gồm 3 thành phần chính:  

+ Request Line: gổm các thành phần:  
    + Request Method: chỉ ra hành động được thực hiện với tài nguyên.     
    + Request URI: sử dụng để tìm tài nguyên trên server;  
    + HTTP Version: Phiên bản giao thức HTTP.  
+ Request Header: Cho phép client truyền thông tin bổ sung về Request.  
+ Request Body: Chứa data được đính kèm request gửi đến server.  

HTTP Request Methods bao gồm:  

- GET: yêu cầu lấy tài nguyên xác định.  
- POST: yêu cầu server tạo mới một tài nguyên.  
- PUT: yêu cầu server ghi đè hoặc tạo mới 1 tài nguyên.  
- PATCH: yêu cầu server cập nhật 1 phần của tài nguyên.  
- DELETE: yêu cầu server xóa tài nguyên xác định.  
- HEAD: Tương tự GET, nhưng response trả về không có body.  

#### 2.2.2, HTTP Response  

HTTP Response: là gói tin được gửi bởi server đến client để phản hồi request trước đó của client.  

HTTP Response gồm 3 thành phần chính:  

+ Status Line: bao gồm 3 thành phần:  
    + HTTP Version: Phiên bản giao thức HTTP;  
    + Status Code: Mã trạng thái;  
    + Status Text (còn gọi là Reason Phrase): Mô tả trạng thái.  
+ Response Header: Cho phép server truyền thông tin bổ sung về response,  
+ Response Body: Chứa tài nguyên mà server phản hồi lại yêu cầu của client.  

### 2.2.3, HTTP Headers  

HTTP Headers cho phép client hoặc server truyền thông tin bổ sung về HTTP Request hoặc Response.  

Nó chứa thông tin dưới dạng các cặp key/value, có thể bao gồm thông tin về:  

- Authentication  
- Caching  
- Client hints  
- Connection management  
- Content negotiation  
- Cookies  
- CORS  
- Message body information  
- Proxies  
- Redirects  
- Request context  
- Response context  
- Other:  
    - Date: chứa ngày giờ message được gửi.  

View details: [HTTP Header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers#request_context).  

### 2.2.4, HTTP Payload  

HTTP Payload là:  

- Request Body (chứa data được đính kèm request gửi đến server),  
- Response Body (chứa tài nguyên mà server phản hồi lại yêu cầu của client).  

### 2.2.5, HTTP Status Code  

HTTP Status Code mô tả trạng thái của HTTP Response, bao gồm:  

- 1xx: Thông tin – Request đã được tiếp nhận, quá trình tiếp tục;  

- 2xx: Thành công – Request đã được tiếp nhận, & xử lý thành công;  
    + 200 OK: request được thực hiện thành công.  
    + 202 Accepted: request đã được nhận, nhưng không có kết quả nào trả về, thông báo cho client tiếp tục chờ   đợi.
    + 204 No Content: request đã được xử lý nhưng không có thành phần nào được trả về.  
    + 205 Reset: giống như 204 nhưng mã này còn yêu câu client reset lại document view.  
    + 206 Partial Content: server chỉ gửi về một phần dữ liệu, phụ thuộc vào giá trị range header của client đã gửi.  

- 3xx: Chuyển hướng – Cần thực hiện thêm hành động để hoàn thành Request;  
    + 301 Moved Permanently: tài nguyên đã được chuyển hoàn toàn tới địa chỉ Location trong HTTP response.  
    + 303 See other: tài nguyên đã được chuyển tạm thời tới địa chỉ Location trong HTTP response.  
    + 304 Not Modified: tài nguyên không thay đổi từ lần cuối client request, nên client có thể sử dụng tài nguyên đã lưu trong cache.  

- 4xx: Lỗi client – Request chứa cú pháp sai hoặc không thể thực hiện được;  
    + 400 Bad Request: request không đúng dạng, cú pháp.  
    + 401 Unauthorized: client chưa xác thực.  
    + 403 Forbidden: client không có quyền truy cập.  
    + 404 Not Found: không tìm thấy tài nguyên.  
    + 405 Method Not Allowed: phương thức không được server hỗ trợ.  

- 5xx: Lỗi server – Server không thực hiện được một Request rõ ràng hợp lệ.  
    + 500 Internal Server Error: có lỗi trong quá trình xử lý của server.  
    + 501 Not Implemented: server không hỗ trợ chức năng client yêu cầu.  
    + 503 Service Unavailable: Server bị quá tải, hoặc bị lỗi xử lý.  
