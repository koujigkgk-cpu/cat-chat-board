package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LikeDAO {
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/dokotsubu?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
    private final String DB_USER = "root";
    private final String DB_PASS = "carp8912"; // ご自身の環境に合わせて変更してください

    public int toggleLike(int mutterId, String userName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                // すでに肉球があるかチェック
                String checkSql = "SELECT id FROM likes WHERE mutter_id = ? AND user_name = ?";
                PreparedStatement ps = conn.prepareStatement(checkSql);
                ps.setInt(1, mutterId);
                ps.setString(2, userName);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // あれば削除（解除）
                    String delSql = "DELETE FROM likes WHERE mutter_id = ? AND user_name = ?";
                    PreparedStatement dps = conn.prepareStatement(delSql);
                    dps.setInt(1, mutterId);
                    dps.setString(2, userName);
                    dps.executeUpdate();
                } else {
                    // なければ追加
                    String insSql = "INSERT INTO likes (mutter_id, user_name) VALUES (?, ?)";
                    PreparedStatement ips = conn.prepareStatement(insSql);
                    ips.setInt(1, mutterId);
                    ips.setString(2, userName);
                    ips.executeUpdate();
                }

                // 最新の合計数をカウントして返す
                String countSql = "SELECT COUNT(*) FROM likes WHERE mutter_id = ?";
                PreparedStatement cps = conn.prepareStatement(countSql);
                cps.setInt(1, mutterId);
                ResultSet rsCount = cps.executeQuery();
                if (rsCount.next()) return rsCount.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}