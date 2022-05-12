# Lession 2: Basic I/O

*I/O Streams* giúp đơn giản hóa các thao tác I/O.

Trong Java platform, các class hỗ trợ các hoạt động I/O Streams nằm trong *java.io* package, các class hỗ trợ các hoạt động File I/O nằm trong *java.nio.file* package.


## 1. I/O Streams

- **Byte Streams** xử lý I/O của raw binary data.  
- **Character Streams** xử lý I/O của character data, tự động xử lý dịch từ và sang local character set.  
- **Buffered Streams** tối ưu hóa input and output bằng cách giảm số lượng calls đến native API.  
- **Scanning and Formatting** cho phép một chương trình đọc và ghi văn bản được định dạng.  
- **I/O from the Command Line** mô tả Standard Streams và Console object.  
- **Data Streams** xử lý binary I/O của primitive data type và các String values.  
- **Object Streams** xử lý binary I/O của các objects.  


## 2. File I/O

- **What is a Path?** xem xét khái niệm về path trên một file system.  
- **The Path Class** giới thiệu các class cơ sở của *java.nio.file* package.  
- **Path Operations** xem xét các methods trong *Path* class xử lý các syntactic operations.  
- **File Operations** giới thiệu các khái niệm chung của các file I/O methods.  
- **Checking a File or Directory** cho biết cách kiểm tra sự tồn tại và khả năng truy cập của file.  
- **Deleting a File or Directory**.  
- **Copying a File or Directory**.  
- **Moving a File or Directory**.  
- **Managing Metadata** giải thích cách đọc và set các file attributes.  
- **Reading, Writing and Creating Files** cho biết các stream và channel methods để đọc và ghi files.  
- **Random Access Files** cho biết cách đọc và ghi files theo cách không tuần tự.  
- **Creating and Reading Directories** bao gồm các API cụ thể cho các directories, chẳng hạn như cách liệt kê nội dung của một directory.  
- **Links, Symbolic or Otherwise** bao gồm các vấn đề cụ thể cho các symbolic và hard links.  
- **Walking the File Tree** trình bày cách truy cập đệ quy từng file và directory trong mộtfile tree.  
- **Finding Files** shows how to search for files using pattern matching.  
- **Watching a Directory for Changes** cho biết cách sử dụng watch service để phát hiện các files được thêm, xóa, hoặc sửa trong một hoặc nhiều directories.  
- **Other Useful Methods** bao gồm các API quan trọng khác.  
- **Legacy File I/O Code** cho biết cách tận dụng chức năng *Path* nếu bạn có code cũ sử dụng các *java.io.File* class. Cung cấp một table mapping (bảng ánh xạ) từ *java.io.File* API sang *java.nio.file* API.  