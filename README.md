# Perfumer Shop — Java E-commerce Web Application

Ứng dụng web bán nước hoa phục vụ học tập và thực hành **Software Engineering**, được phát triển theo kiến trúc **JSP/Servlet + DAO + SQL Server** và đóng gói thành tệp WAR bằng Maven.

> **Trạng thái:** Đã có mã nguồn các luồng mua hàng, giỏ hàng, quản trị sản phẩm và đơn hàng. Chưa có bằng chứng chạy lại toàn bộ ứng dụng từ một môi trường mới; xem phần giới hạn trước khi sử dụng.

## 1. Công nghệ

| Thành phần | Công nghệ |
| --- | --- |
| Backend | Java 11, Servlet API 4.0, JSP |
| Giao diện | JSP, HTML, CSS, JavaScript, JSTL |
| Dữ liệu | SQL Server, JDBC, DAO |
| Đóng gói | Maven, WAR |
| Application Server | Apache Tomcat 9 |
| Email | JavaMail, SMTP (cần cấu hình riêng) |
| Thanh toán | **Mô phỏng thanh toán** trong `VNPayServlet`, **không phải VNPay API thật** |

## 2. Các chức năng có trong mã nguồn

**Khách hàng:** đăng ký, đăng nhập, xem/tìm kiếm/lọc sản phẩm, giỏ hàng, wishlist, thông tin cá nhân, đặt hàng và trang mô phỏng thanh toán. Các luồng email (xác nhận đơn hàng và quên mật khẩu) có mã nguồn nhưng cần cấu hình SMTP và kiểm thử lại.

**Quản trị:** các Servlet quản lý tài khoản, sản phẩm, nhà cung cấp, đơn hàng; trang dashboard và thống kê doanh thu.

**Tổ chức mã nguồn:**
- `src/main/java/perfumeshop/controller/`: Servlet xử lý HTTP request.
- `src/main/java/perfumeshop/dal/`: Data Access Objects truy vấn SQL Server.
- `src/main/java/perfumeshop/model/`: đối tượng nghiệp vụ.
- `src/main/java/perfumeshop/filter/`: bộ lọc phiên đăng nhập và phân quyền.
- `src/main/webapp/`: JSP, CSS, JavaScript và tài nguyên giao diện.

## 3. Cài đặt local

### Yêu cầu

JDK 11, Maven, SQL Server, Apache Tomcat 9. Repo **chưa kèm script tạo schema SQL Server**, vì vậy bạn cần có database `Perfume_Website` đúng cấu trúc mà các DAO đang sử dụng. Đây là hạn chế quan trọng đối với việc chạy từ máy mới.

### Cấu hình SQL Server

Clone và build:

```powershell
git clone https://github.com/VuHuuThinh05/Perfumer-Shop-Maven.git
cd Perfumer-Shop-Maven
mvn clean package
```

Ứng dụng đọc các **biến môi trường** sau khi chạy trong IDE hoặc Tomcat:

| Biến | Ý nghĩa |
| --- | --- |
| `DB_URL` | URL JDBC đến SQL Server |
| `DB_USERNAME` | Tài khoản SQL Server |
| `DB_PASSWORD` | Mật khẩu SQL Server |
| `SMTP_USERNAME` | Địa chỉ gửi thư (nếu dùng tính năng email) |
| `SMTP_PASSWORD` | Mật khẩu ứng dụng SMTP (nếu dùng email) |

Tham khảo [`.env.example`](.env.example) để biết định dạng cấu hình. **Java không tự đọc tệp `.env`**; phải thiết lập biến môi trường trong cấu hình chạy của IDE/Tomcat. Không commit mật khẩu thật.

Ví dụ cho một phiên PowerShell dùng để khởi chạy Tomcat từ chính phiên đó:

```powershell
$env:DB_URL="jdbc:sqlserver://localhost:1433;databaseName=Perfume_Website;encrypt=true;trustServerCertificate=true"
$env:DB_USERNAME="your_local_sql_user"
$env:DB_PASSWORD="your_local_sql_password"
```

Thiết lập thêm `SMTP_USERNAME` và `SMTP_PASSWORD` nếu thực sự sử dụng tính năng gửi email.

### Deploy Tomcat

Sau khi Maven build thành công, copy `target/perfume-shop-maven.war` vào thư mục `webapps/` của Tomcat 9, rồi khởi động Tomcat. Đường dẫn dự kiến khi triển khai WAR với tên mặc định:

```text
http://localhost:8080/perfume-shop-maven/home
```

Nếu cấu hình context path khác, URL sẽ thay đổi tương ứng.

## 4. Lưu ý bảo mật và giới hạn

- Đã chuyển cấu hình kết nối SQL Server/SMTP sang biến môi trường; các khóa hoặc mật khẩu từng commit vẫn nằm trong **lịch sử Git**. Cần thu hồi/đổi ngay nếu còn hiệu lực.
- Đã loại bỏ việc tạo cookie chứa mật khẩu người dùng trong `LoginServlet`. Thay đổi này **không cung cấp cơ chế đăng nhập tự động**; người dùng vẫn cần đăng nhập bằng mật khẩu.
- **Mật khẩu tài khoản đang được so khớp trực tiếp trong SQL** ở `UserDAO`, chưa dùng mật khẩu băm. Không dùng hệ thống này với tài khoản thật hoặc triển khai công khai trước khi sửa.
- Luồng quên mật khẩu và một số DAO có phương thức stub trả về `null`; không coi các chức năng này là hoàn thiện khi chưa kiểm thử.
- `VNPayServlet` chỉ mô phỏng thanh toán trong phiên web, không tích hợp cổng VNPay thật; không xử lý giao dịch thanh toán thực tế.
- Chưa có bộ kiểm thử tự động đáng tin cậy hay script database schema trong repository để chứng minh có thể tái triển khai đầy đủ.

## 5. Hướng hoàn thiện tiếp theo

1. Cung cấp schema/seed SQL Server dạng dữ liệu mẫu (không chứa thông tin thật), kiểm tra chạy được từ máy mới.
2. Thay lưu trữ mật khẩu dạng rõ bằng Argon2 hoặc BCrypt, cập nhật đăng ký/đăng nhập/đổi mật khẩu/quên mật khẩu đồng bộ.
3. Kiểm tra luồng đặt hàng theo transaction và trạng thái thành công/thất bại; tránh xác nhận đơn hàng dựa trên chênh lệch tổng số đơn.
4. Viết smoke tests và bổ sung ảnh màn hình thật (trang chủ, giỏ hàng, quản trị) sau khi kiểm thử.
5. Nếu muốn tích hợp VNPay thật, cần xây dựng và kiểm chứng riêng, không dùng mã mô phỏng làm bằng chứng đã tích hợp.

Dự án có thể dùng để chứng minh kinh nghiệm tiếp xúc với **Java Web, Servlet/JSP, SQL Server và quy trình xây dựng ứng dụng thương mại điện tử**; những giới hạn trên được ghi rõ để không gây hiểu nhầm về mức độ sẵn sàng triển khai.
