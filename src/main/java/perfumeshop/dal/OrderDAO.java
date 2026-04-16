package perfumeshop.dal;

import perfumeshop.utils.DBContext;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import perfumeshop.model.*;

public class OrderDAO extends DBContext {

    public void addOrder(User cus, Cart cart) {
        try {
            String sql = "INSERT INTO Orders(Date, UserName, TotalMoney, status) VALUES (?,?,?,?)";

            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, LocalDate.now().toString());
            st.setString(2, cus.getUserName());
            st.setDouble(3, cart.getTotalMoney());
            st.setInt(4, 0);

            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();

            if (rs.next()) {
                int orderID = rs.getInt(1);

                for (Item item : cart.getListItems()) {
                    String sql2 = "INSERT INTO OrderDetails(OrderID, ProductID, Quantity, UnitPrice, Discount) VALUES (?,?,?,?,?)";

                    PreparedStatement st2 = connection.prepareStatement(sql2);
                    st2.setInt(1, orderID);
                    st2.setInt(2, item.getProduct().getId());
                    st2.setInt(3, item.getQuantity());
                    st2.setDouble(4, item.getProduct().getPrice());
                    st2.setDouble(5, item.getProduct().getDiscount());
                    st2.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getNumberOrders() {
    String sql = "SELECT COUNT(*) FROM Orders";

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
}// ================= TOTAL MONEY =================
public double sumAllMoneyOrder() {
    String sql = "SELECT SUM(TotalMoney) FROM Orders";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getDouble(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
// ================= TOTAL MONEY BY MONTH =================
public double totalMoneyMonth(int month, int year) {
    String sql = "SELECT SUM(TotalMoney) FROM Orders WHERE MONTH([Date])=? AND YEAR([Date])=?";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, month);
        st.setInt(2, year);

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getDouble(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
// ================= TOTAL MONEY BY WEEK =================
public double totalMoneyWeek(int day, int from, int to, int year, int month) {
    String sql;

    if (from > to) {
        // tuần vắt qua tháng
        sql = "SELECT SUM(TotalMoney) FROM Orders " +
              "WHERE ((DAY([Date]) >= ? AND MONTH([Date]) = ?) " +
              "OR (DAY([Date]) <= ? AND MONTH([Date]) = ?)) " +
              "AND YEAR([Date]) = ? AND DATEPART(dw, [Date]) = ?";
    } else {
        sql = "SELECT SUM(TotalMoney) FROM Orders " +
              "WHERE DAY([Date]) BETWEEN ? AND ? " +
              "AND MONTH([Date]) = ? AND YEAR([Date]) = ? " +
              "AND DATEPART(dw, [Date]) = ?";
    }

    try {
        PreparedStatement st = connection.prepareStatement(sql);

        if (from > to) {
            st.setInt(1, from);
            st.setInt(2, month);
            st.setInt(3, to);
            st.setInt(4, month + 1);
            st.setInt(5, year);
            st.setInt(6, day);
        } else {
            st.setInt(1, from);
            st.setInt(2, to);
            st.setInt(3, month);
            st.setInt(4, year);
            st.setInt(5, day);
        }

        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getDouble(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
// ================= UPDATE ORDER STATUS =================
public void updateStatus(int id) {
    String sql = "UPDATE Orders SET status = 1 WHERE OrderID = ?";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setInt(1, id);
        st.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
// ================= GET ALL ORDERS =================
public List<Order> getAll() {
    List<Order> list = new ArrayList<>();
    String sql = "SELECT * FROM Orders ORDER BY status ASC";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Order o = new Order(
                    rs.getInt("OrderID"),
                    rs.getDate("Date"),
                    rs.getString("UserName"),
                    rs.getDouble("TotalMoney"),
                    rs.getInt("status") == 1
            );
            list.add(o);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}