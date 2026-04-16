package perfumeshop.dal;

import perfumeshop.utils.DBContext;
import java.sql.*;
import java.util.*;
import perfumeshop.model.*;

public class ProductDAO extends DBContext {

    private CategoryDAO cd = new CategoryDAO();
    private SupplierDAO sd = new SupplierDAO();

    // ================= COMMON MAP =================
    private Product mapProduct(ResultSet rs) throws Exception {
        String img = rs.getString("image");
        String[] images = (img != null) ? img.split(",") : new String[]{};

        Category c = cd.getCategoryById(rs.getInt("CategoryID"));
        Supplier s = sd.getSupplierById(rs.getInt("SupplierID"));

        double price = rs.getDouble("UnitPrice");
        double discount = rs.getDouble("Discount");

        return new Product(
                rs.getString("ProductName"),
                images,
                rs.getString("describe"),
                rs.getString("QuantityPerUnit"),
                rs.getInt("ProductID"),
                rs.getInt("UnitsInStock"),
                rs.getInt("StarRating"),
                price,
                discount,
                price * (1 - discount),
                rs.getDate("releaseDate"),
                c, s
        );
    }

    // ================= GET ALL =================
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= GET BY ID =================
    public Product getProductByID(int id) {
        String sql = "SELECT * FROM Products WHERE ProductID=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapProduct(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= FEATURE =================
    public List<Product> getFeaturedProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP 3 * FROM Products ORDER BY NEWID()";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= HOT DEAL =================
    public Product getHotDeal() {
        String sql = "SELECT TOP 1 * FROM Products ORDER BY (UnitPrice*(1-Discount)) ASC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return mapProduct(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= BEST SELLER =================
    public List<Product> getTopBestSellers(int number) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Products ORDER BY QuantitySold DESC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, number);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= FILTER CATEGORY =================
    public List<Product> getProductsByCategoryid(int cid) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE CategoryID=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, cid);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= BRAND YEAR =================
    public List<Product> getProductsBrandByInYear(int year, Category category) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE YEAR(releaseDate)=?";

        if (category != null) {
            sql += " AND CategoryID=?";
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, year);

            if (category != null) {
                st.setInt(2, category.getId());
            }

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= GIFT SET =================
    public List<Product> getGiflSets() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE CategoryID=5";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= SEARCH =================
    public List<Product> searchByName(String text) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE ProductName LIKE ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, "%" + text + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= PAGING =================
    public List<Product> getListByPage(List<Product> list, int start, int end) {
        List<Product> result = new ArrayList<>();

        for (int i = start; i < end && i < list.size(); i++) {
            result.add(list.get(i));
        }

        return result;
    }

    // ================= UPDATE STOCK =================
    public void updateValueProduct(Product product, int value) {
        String sql = "UPDATE Products SET UnitsInStock = UnitsInStock - ?, QuantitySold = QuantitySold + ? WHERE ProductName=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, value);
            st.setInt(2, value);
            st.setString(3, product.getName());
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= COUNT =================
    public int countAllProduct() {
        String sql = "SELECT COUNT(*) FROM Products";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    // ================= COUNT PRODUCT BY DISCOUNT =================
public int getNumberProductsByDiscount(double discount) {
    String sql = "SELECT COUNT(*) FROM Products WHERE Discount >= ?";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setDouble(1, discount);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}


// ================= SEARCH BY CHECKBOX =================
public List<Product> searchByCheckBox(int[] cid) {
    List<Product> list = new ArrayList<>();

    StringBuilder sql = new StringBuilder("SELECT * FROM Products WHERE 1=1");

    if (cid != null && cid.length > 0 && cid[0] != 0) {
        sql.append(" AND CategoryID IN (");

        for (int i = 0; i < cid.length; i++) {
            sql.append("?");
            if (i < cid.length - 1) sql.append(",");
        }

        sql.append(")");
    }

    try {
        PreparedStatement st = connection.prepareStatement(sql.toString());

        if (cid != null && cid.length > 0 && cid[0] != 0) {
            for (int i = 0; i < cid.length; i++) {
                st.setInt(i + 1, cid[i]);
            }
        }

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(mapProduct(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}


// ================= LOAD MORE (NEXT 9 PRODUCT) =================
public List<Product> getNext9Product(int amount) {
    List<Product> list = new ArrayList<>();

    String sql = "SELECT * FROM Products ORDER BY ProductID OFFSET ? ROWS FETCH NEXT 9 ROWS ONLY";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, amount);

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(mapProduct(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}


// ================= COMPAT STRING VERSION =================
public List<Product> getTopBestSellers(String number) {
    try {
        return getTopBestSellers(Integer.parseInt(number));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return new ArrayList<>();
}
// ================= SEARCH BY PRICE =================
public List<Product> searchByPrice(double price1, double price2, int[] cid) {
    List<Product> list = new ArrayList<>();

    StringBuilder sql = new StringBuilder("SELECT * FROM Products WHERE 1=1");

    // filter theo giá
    if (price1 > 0) {
        sql.append(" AND UnitPrice >= ?");
    }
    if (price2 > 0) {
        sql.append(" AND UnitPrice <= ?");
    }

    // filter theo category
    if (cid != null && cid.length > 0 && cid[0] != 0) {
        sql.append(" AND CategoryID IN (");

        for (int i = 0; i < cid.length; i++) {
            sql.append("?");
            if (i < cid.length - 1) sql.append(",");
        }

        sql.append(")");
    }

    try {
        PreparedStatement st = connection.prepareStatement(sql.toString());

        int index = 1;

        // set giá
        if (price1 > 0) {
            st.setDouble(index++, price1);
        }
        if (price2 > 0) {
            st.setDouble(index++, price2);
        }

        // set category
        if (cid != null && cid.length > 0 && cid[0] != 0) {
            for (int i = 0; i < cid.length; i++) {
                st.setInt(index++, cid[i]);
            }
        }

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(mapProduct(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
// ================= TOP 10 BEST SELLER =================
public List<Product> getTop10SellerProduct() {
    List<Product> list = new ArrayList<>();
    String sql = "SELECT TOP 10 * FROM Products ORDER BY QuantitySold DESC";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(mapProduct(rs));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
// ================= COUNT PRODUCT TYPES =================
public int countAllTypeProduct() {
    String sql = "SELECT COUNT(*) FROM Products";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
// ================= TOTAL QUANTITY SOLD =================
public int getSumQuantitySold() {
    String sql = "SELECT SUM(QuantitySold) FROM Products";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
// ================= DELETE PRODUCT =================
public void deleteProduct(int id) {
    String sql = "DELETE FROM Products WHERE ProductID=?";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
// ================= INSERT PRODUCT =================
public void insertProduct(String name, String image, double price,
                         String describe, int quantity,
                         String quantityunit, String date,
                         double discount, int supplierID, int categoryID) {

    String sql = "INSERT INTO Products " +
            "(ProductName, SupplierID, CategoryID, QuantityPerUnit, UnitPrice, UnitsInStock, image, describe, releaseDate, Discount, status) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,1)";

    try {
        PreparedStatement st = connection.prepareStatement(sql);

        st.setString(1, name);
        st.setInt(2, supplierID);
        st.setInt(3, categoryID);
        st.setString(4, quantityunit);
        st.setDouble(5, price);
        st.setInt(6, quantity);
        st.setString(7, image);
        st.setString(8, describe);
        st.setString(9, date); // format yyyy-MM-dd
        st.setDouble(10, discount);

        st.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
// ================= UPDATE PRODUCT =================
public void editProduct(String name, String image, double price,
                        String describe, int quantity,
                        String quantityunit, String date,
                        double discount, int supplierID,
                        int categoryID, int productID) {

    String sql;

    if (image != null && !image.isEmpty()) {
        sql = "UPDATE Products SET ProductName=?, SupplierID=?, CategoryID=?, " +
              "QuantityPerUnit=?, UnitPrice=?, UnitsInStock=?, image=?, " +
              "describe=?, releaseDate=?, Discount=? WHERE ProductID=?";
    } else {
        sql = "UPDATE Products SET ProductName=?, SupplierID=?, CategoryID=?, " +
              "QuantityPerUnit=?, UnitPrice=?, UnitsInStock=?, " +
              "describe=?, releaseDate=?, Discount=? WHERE ProductID=?";
    }

    try {
        PreparedStatement st = connection.prepareStatement(sql);

        st.setString(1, name);
        st.setInt(2, supplierID);
        st.setInt(3, categoryID);
        st.setString(4, quantityunit);
        st.setDouble(5, price);
        st.setInt(6, quantity);

        if (image != null && !image.isEmpty()) {
            st.setString(7, image);
            st.setString(8, describe);
            st.setString(9, date);
            st.setDouble(10, discount);
            st.setInt(11, productID);
        } else {
            st.setString(7, describe);
            st.setString(8, date);
            st.setDouble(9, discount);
            st.setInt(10, productID);
        }

        st.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}