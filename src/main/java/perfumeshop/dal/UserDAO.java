package perfumeshop.dal;

import perfumeshop.utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import perfumeshop.model.Spending;
import perfumeshop.model.User;

public class UserDAO extends DBContext {

    // ===================== LOGIN =====================
    public User check(String username, String password) {
        String sql = "SELECT * FROM Users WHERE userName=? AND password=? AND status=1";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("userName"),
                        rs.getString("fullName"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("Image"),
                        rs.getString("BirthDay"),
                        rs.getInt("roleID")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== CHECK ADMIN =====================
    public int checkAccountAdmin(String userName) {
        String sql = "SELECT roleID FROM Users WHERE userName=? AND status=1";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();

            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ===================== GET ALL USERS =====================
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE status=1";

        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(new User(
                        rs.getString("userName"),
                        rs.getString("fullName"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("Image"),
                        rs.getString("BirthDay"),
                        rs.getInt("roleID")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===================== CHECK DUPLICATE =====================
    public boolean checkUserNameDuplicate(String username) {
        String sql = "SELECT 1 FROM Users WHERE userName=?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===================== INSERT USER =====================
    public void insertUser(String userName, String fullName, String password,
                           int roleId, String email, String birthDay, String phone) {

        String sql = "INSERT INTO Users(userName, fullName, password, roleID, email, BirthDay, phone, status) "
                   + "VALUES (?,?,?,?,?,?,?,1)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, userName);
            st.setString(2, fullName);
            st.setString(3, password);
            st.setInt(4, roleId);
            st.setString(5, email);
            st.setString(6, birthDay);
            st.setString(7, phone);

            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== UPDATE IMAGE =====================
    public void updateImage(String image, String userName) {
        String sql = "UPDATE Users SET Image=? WHERE userName=?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, image);
            st.setString(2, userName);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== GET USER =====================
    public User getUserByUserName(String userName) {
        String sql = "SELECT * FROM Users WHERE userName=?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("userName"),
                        rs.getString("fullName"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("Image"),
                        rs.getString("BirthDay"),
                        rs.getInt("roleID")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== COUNT USER =====================
    public int countAllUser() {
        String sql = "SELECT COUNT(*) FROM Users WHERE status=1";

        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ===================== DELETE USER =====================
    public void deleteUser(String username) {
        String sql = "UPDATE Users SET status=0 WHERE userName=?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== SEARCH =====================
    public List<User> getUsersBySearchName(String txt) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE userName LIKE ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + txt + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new User(
                        rs.getString("userName"),
                        rs.getString("fullName"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("Image"),
                        rs.getString("BirthDay"),
                        rs.getInt("roleID")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===================== CHANGE PASSWORD =====================
    public void updatePassByUserName(String pass, String username) {
        String sql = "UPDATE Users SET password=? WHERE userName=?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, pass);
            st.setString(2, username);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== STUB SAFE =====================
    public List<Spending> getTop5Customers() {
        return new ArrayList<>();
    }

    public String getUserNameByEmail(String email) {
        return null;
    }

    public String checkEmailExist(String email) {
        return null;
    }

    public int getNumberUsers() {
        return countAllUser();
    }

    public void insert(User user) {
        insertUser(
                user.getUserName(),
                user.getFullName(),
                user.getPassword(),
                user.getRoleID(),
                user.getEmail(),
                user.getBirthdate(),
                user.getPhone()
        );
    }

    public void changePassword(User user) {
        updatePassByUserName(user.getPassword(), user.getUserName());
    }

    public void update(String name, String address, String phone, String email, String birthdate, String username) {
        String sql = "UPDATE Users SET fullName=?, address=?, phone=?, email=?, BirthDay=? WHERE userName=?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, name);
            st.setString(2, address);
            st.setString(3, phone);
            st.setString(4, email);
            st.setString(5, birthdate);
            st.setString(6, username);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}