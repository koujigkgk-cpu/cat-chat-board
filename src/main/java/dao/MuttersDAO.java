package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Mutter;

public class MuttersDAO {
    // Supabaseの接続情報
    private final String JDBC_URL = "jdbc:postgresql://db.arpakswzlfpntdwrrghy.supabase.co:5432/postgres";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "あなたのパスワード"; // ★ここを自分のパスワードに！

    public List<Mutter> findAll() {
        List<Mutter> mutterList = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                // 最新の投稿が上にくるように ID の降順で取得
                String sql = "SELECT id, name, text, reply_id, image, created_at FROM mutters ORDER BY id DESC";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String text = rs.getString("text");
                    int replyId = rs.getInt("reply_id");
                    String image = rs.getString("image");
                    // 必要に応じて model.Mutter のコンストラクタに合わせて調整してください
                    Mutter mutter = new Mutter(id, name, text, replyId, image);
                    mutterList.add(mutter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mutterList;
    }

    public void create(Mutter mutter) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                String sql = "INSERT INTO mutters (name, text, reply_id, image) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, mutter.getUserName());
                ps.setString(2, mutter.getText());
                ps.setInt(3, mutter.getReplyId());
                ps.setString(4, mutter.getImage());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean delete(int id) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                String sql = "DELETE FROM mutters WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                int result = ps.executeUpdate();
                return result > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
