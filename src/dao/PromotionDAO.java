package dao;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import model.Promotion;

public class PromotionDAO {
    private Connection conn;

    public PromotionDAO() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/salesmanagement","root","");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Promotion> getAllPromotions() throws SQLException {
        List<Promotion> list = new ArrayList<>();
        String sql = "SELECT * FROM promotions";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Promotion p = new Promotion(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDouble("discount"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("description")
            );
            list.add(p);
        }
        return list;
    }

    public boolean insertPromotion(Promotion p) throws SQLException {
        String sql = "INSERT INTO promotions (id, name, discount, start_date, end_date, description) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getId());
        ps.setString(2, p.getName());
        ps.setDouble(3, p.getDiscount());
        ps.setDate(4, Date.valueOf(p.getStartDate()));
        ps.setDate(5, Date.valueOf(p.getEndDate()));
        ps.setString(6, p.getDescription());
        return ps.executeUpdate() > 0;
    }

    public boolean updatePromotion(Promotion p) throws SQLException {
        String sql = "UPDATE promotions SET name=?, discount=?, start_date=?, end_date=?, description=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getName());
        ps.setDouble(2, p.getDiscount());
        ps.setDate(3, Date.valueOf(p.getStartDate()));
        ps.setDate(4, Date.valueOf(p.getEndDate()));
        ps.setString(5, p.getDescription());
        ps.setString(6, p.getId());
        return ps.executeUpdate() > 0;
    }

    public boolean deletePromotion(String id) throws SQLException {
        String sql = "DELETE FROM promotions WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate() > 0;
    }
}
