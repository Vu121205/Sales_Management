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
            
            // Gán giá trị cho 2 tham số ? trong câu SQL
            ps.setString(1, username);
            ps.setString(2, password);
            // Thực thi câu truy vấn
            ResultSet rs = ps.executeQuery();
            
            // Nếu có kết quả → tức là username/password đúng
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
    
    public boolean signUp(String username, String password, String role) {
        String checkSql = "SELECT * FROM users WHERE username=?";
        String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            // kiểm tra tồn tại
            try (PreparedStatement check = con.prepareStatement(checkSql)) {
                check.setString(1, username);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    return false; // username đã tồn tại
                }
            }

            // thêm mới
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role); // luôn là "user"
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        }
}
