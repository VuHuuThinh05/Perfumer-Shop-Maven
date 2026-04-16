package perfumeshop.dal;

import perfumeshop.utils.DBContext;
import java.sql.*;
import java.util.*;
import perfumeshop.model.Wallet;

public class WalletDAO extends DBContext {

    public Wallet getWalletByUserName(String name) {
        String sql = "SELECT * FROM Wallets WHERE UserName=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Wallet(rs.getString("UserName"), rs.getDouble("Balance"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void inputMoney(String userName, double value) {
        String sql = "UPDATE Wallets SET Balance = Balance + ? WHERE UserName=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setDouble(1, value);
            st.setString(2, userName);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deductionMoney(String userName, double value) {
        String sql = "UPDATE Wallets SET Balance = Balance - ? WHERE UserName=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setDouble(1, value);
            st.setString(2, userName);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addWallet(Wallet wallet) {
    String sql = "INSERT INTO Wallets(UserName, Balance) VALUES (?, ?)";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, wallet.getUserName());
        st.setDouble(2, wallet.getBalance());
        st.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public int getNumberWallets() {
    String sql = "SELECT COUNT(*) FROM Wallets";

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
// ================= TRỪ TIỀN =================
public void decuctionMoney(String userName, double value) {
    String sql = "UPDATE Wallets SET Balance = Balance - ? WHERE UserName = ?";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setDouble(1, value);
        st.setString(2, userName);
        st.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
// ================= SEARCH WALLET =================
public List<Wallet> getWalletBySearchName(String txtSearch) {
    List<Wallet> list = new ArrayList<>();
    String sql = "SELECT * FROM Wallets WHERE UserName LIKE ?";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, "%" + txtSearch + "%");

        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(new Wallet(
                    rs.getString("UserName"),
                    rs.getDouble("Balance")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
// ================= GET ALL WALLET =================
public List<Wallet> getAll() {
    List<Wallet> list = new ArrayList<>();
    String sql = "SELECT * FROM Wallets ORDER BY Balance DESC";

    try {
        PreparedStatement st = connection.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(new Wallet(
                    rs.getString("UserName"),
                    rs.getDouble("Balance")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
}