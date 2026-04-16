package perfumeshop.dal;

import perfumeshop.utils.DBContext;
import java.sql.*;
import java.util.*;
import perfumeshop.model.Supplier;

public class SupplierDAO extends DBContext {

    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("SupplierID"));
                s.setCompanyName(rs.getString("CompanyName"));
                s.setConTactName(rs.getString("ContactName"));
                s.setCountry(rs.getString("Country"));
                s.setPhone(rs.getString("Phone"));
                s.setHomepage(rs.getString("HomePage"));
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Supplier getSupplierById(int id) {
        String sql = "SELECT * FROM Suppliers WHERE SupplierID=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("SupplierID"));
                s.setCompanyName(rs.getString("CompanyName"));
                s.setConTactName(rs.getString("ContactName"));
                s.setCountry(rs.getString("Country"));
                s.setPhone(rs.getString("Phone"));
                s.setHomepage(rs.getString("HomePage"));
                return s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(Supplier s) {
        String sql = "INSERT INTO Suppliers(CompanyName, ContactName, Country, Phone, HomePage) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, s.getCompanyName());
            st.setString(2, s.getContactName());
            st.setString(3, s.getCountry());
            st.setString(4, s.getPhone());
            st.setString(5, s.getHomepage());
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSupplier(int id) {
        String sql = "DELETE FROM Suppliers WHERE SupplierID=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editSupplier(int id, String name, String contact, String country, String phone, String homepage) {
        String sql = "UPDATE Suppliers SET CompanyName=?, ContactName=?, Country=?, Phone=?, HomePage=? WHERE SupplierID=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, name);
            st.setString(2, contact);
            st.setString(3, country);
            st.setString(4, phone);
            st.setString(5, homepage);
            st.setInt(6, id);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countAllSupplier() {
        String sql = "SELECT COUNT(*) FROM Suppliers";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    // ================= SEARCH SUPPLIER =================
public List<Supplier> getSuppliersBySearch(String txtSearch) {
    List<Supplier> list = new ArrayList<>();
    String sql = "SELECT * FROM Suppliers WHERE CompanyName LIKE ?";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, "%" + txtSearch + "%");

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Supplier s = new Supplier();
            s.setId(rs.getInt("SupplierID"));
            s.setCompanyName(rs.getString("CompanyName"));
            s.setConTactName(rs.getString("ContactName"));
            s.setCountry(rs.getString("Country"));
            s.setPhone(rs.getString("Phone"));
            s.setHomepage(rs.getString("HomePage"));
            list.add(s);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}