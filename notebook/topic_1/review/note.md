## 1. Web

- vnexpess là một website.  
- website được đặt trong một web server.  
    + Chịu trách nhiệm đón các request.  
    + Chỉ sử dụng giao thức HTTP.  
    + Chỉ chứa nội dung tĩnh, hoặc một chút động.  
    + Không có sự tương tác dữ liệu qua lại.  
- web app bao gồm web server, DB, Cache, Queue, xử lý, ...  
    + Chỉ sử dụng giao thức HTTP.  
    + Cung cấp nội dung động, có sự tương tác dữ liệu CRUD.  
- Appilcation server không chỉ bao gồm web app, còn gồm rất nhiều thứ khác như giao tiếp, ...  
    + Sử dụng cả các giao thức khác ngoài HTTP.  


## 2. IP - Domain

- Khi truy cập web vnexpress, trước tiên sẽ tìm đến file etc/hosts,  
    + Windows 10 - "C:\Windows\System32\drivers\etc\hosts"  
    + Linux - "/etc/hosts"  
    + Mac OS X - "/private/etc/hosts"  
- Nếu không thấy sẽ tìm đến cache,  
- Nếu không thấy sẽ tìm đến DNS.  


## 3. HTTP

RFC: là document quy định về tất cả mọi thứ của web.

*Ví dụ: quy định về HTTP là RFC 72xx.*

HTTP là quy cách đóng gói và bóc tách gói tin.

```
        Req body    Res body    safe    cache   ilempotation
POST    o           o           x       o       x
PUT     o           o           x       x       o
GET     option      o           o       o       o
DELETE  option      o           x       x       o
```

Ilempotation: nhận request nhiều lần nhưng chỉ xử lý 1 lần.

*Ví dụ: Gủi form đăng ký nhiều lần, sửa bài viết nhiều lần.*