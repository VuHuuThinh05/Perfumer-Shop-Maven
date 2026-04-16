# 🌸 Perfume Shop Web (Java JSP/Servlet + SQL Server)

## 📌 Giới thiệu

Perfume Shop là một hệ thống web bán nước hoa được xây dựng bằng Java JSP/Servlet (Maven) kết hợp với SQL Server. Dự án mô phỏng đầy đủ các chức năng của một website thương mại điện tử cơ bản như quản lý sản phẩm, giỏ hàng, đặt hàng, thanh toán (VNPay) và quản lý người dùng.

---

## 🛠️ Công nghệ sử dụng

* Backend: Java Servlet, JSP
* Build Tool: Maven
* Database: SQL Server
* Server: Apache Tomcat 9
* Frontend: HTML, CSS, JSTL
* Payment: VNPay API

---

## ⚙️ Cài đặt & chạy project

### 1. Clone project

```bash
git clone https://github.com/VuHuuThinh05/Perfumer-Shop-Maven.git
cd Perfumer-Shop-Maven
```

### 2. Cấu hình Database

Mở file `DBContext.java` và chỉnh sửa:

```java
String url = "jdbc:sqlserver://YOUR_SERVER:1433;databaseName=Perfume_Website;encrypt=true;trustServerCertificate=true";
String username = "sa";
String password = "12345";
```

### 3. Build project

```bash
mvn clean package
```

### 4. Deploy lên Tomcat

Copy file `.war` trong thư mục `target/` vào `apache-tomcat/webapps/`, sau đó start Tomcat.

### 5. Truy cập

```
http://localhost:8080/PerfumeShop
```

---

## 🔑 Chức năng chính

### 👤 User

* Đăng ký / Đăng nhập
* Xem sản phẩm
* Tìm kiếm & lọc sản phẩm
* Thêm vào giỏ hàng
* Đặt hàng
* Thanh toán VNPay
* Quản lý thông tin cá nhân

### 🛠️ Admin

* Quản lý sản phẩm (CRUD)
* Quản lý category / supplier
* Quản lý đơn hàng
* Thống kê doanh thu

---

## 📂 Cấu trúc project

```
src/
 ├── controller/     (Servlet)
 ├── dal/            (DAO - Database)
 ├── model/          (Entity)
 ├── utils/          (DBContext)
 └── web/            (JSP + Frontend)
```

---


```
http://java.sun.com/jsp/jstl/core
```

---

## 🚀 Hướng phát triển

* Nâng cấp sang Spring Boot
* Tích hợp JWT Authentication
* Cải thiện UI (React / Vue)
* Docker hóa project

---

## 👨‍💻 Tác giả

Vu Huu Thinh
GitHub: https://github.com/VuHuuThinh05

---

## ⭐ Ghi chú

Project được xây dựng phục vụ mục đích học tập và thực hành phát triển web Java.
