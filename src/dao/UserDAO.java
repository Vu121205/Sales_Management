package dao;

import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            // lấy role từ DB
            String role = rs.getString("Role");
            // khởi tạo user với đầy đủ tham số
            return new User(username, password, role);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return null; // login thất bại
    }
}
