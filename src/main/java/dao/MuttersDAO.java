package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Mutter;

public class MuttersDAO {
    // 修正後のURL例（ポートを6543にし、末尾にオプションを追加）
// ポートを5432にし、クラウド接続を安定させるオプションをすべて盛り込みました
private final String JDBC_URL = "jdbc:postgresql://db.arpakswzlfpntdwrrghy.supabase.co:5432/postgres?sslmode=require&tcpKeepAlive=true&prepareThreshold=0";    private final String DB_USER = "postgres";
    private final String DB_PASS = "carp8912carp"; // ★ご自身のパスワードに書き換えてください

    public List<Mutter> findAll() {
        List<Mutter> mutterList = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                // SQLでいいねの数(like_count)も一緒に取得
                String sql = "SELECT m.id, m.name, m.text, m.reply_id, m.image, m.created_at, " +
                             "COUNT(l.id) AS like_count " +
                             "FROM mutters m " +
                             "LEFT JOIN likes l ON m.id = l.mutter_id " +
                             "GROUP BY m.id, m.name, m.text, m.reply_id, m.image, m.created_at " +
                             "ORDER BY m.id DESC";
                
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String text = rs.getString("text");
                    int replyId = rs.getInt("reply_id");
                    String image = rs.getString("image");
                    String createdAt = rs.getTimestamp("created_at").toString();
                    int likeCount = rs.getInt("like_count");

                    // ★Mutter.java のコンストラクタ (int, String, String, String, String, int, int) に合わせます
                    // 順番: id, userName, text, image, createdAt, replyId, likeCount
                    Mutter mutter = new Mutter(id, name, text, image, createdAt, replyId, likeCount);
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
