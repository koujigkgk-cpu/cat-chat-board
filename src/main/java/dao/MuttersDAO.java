package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Mutter;

public class MuttersDAO {
    // ★Supabase接続設定：ポート6543とSSLモード、プロジェクトID付きユーザー名を維持
    private final String JDBC_URL = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:6543/postgres?sslmode=require";
    private final String DB_USER = "postgres.arpakswzlfpntdwrrghy";
    private final String DB_PASS = "carp8912carp";
    
    public List<Mutter> findAll() {
        List<Mutter> mutterList = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                
                // ★いいねテーブル(likes)と結合してカウントを取得する高度なSQL
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
                    String image = rs.getString("image");
                    // タイムスタンプを文字列へ変換
                    String createdAt = rs.getTimestamp("created_at") != null ? 
                                       rs.getTimestamp("created_at").toString() : "";
                    int replyId = rs.getInt("reply_id");
                    int likeCount = rs.getInt("like_count");

                    // ★Mutter.java のコンストラクタ引数の順番に厳密に合わせます
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

    public boolean create(Mutter mutter) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                // 画像URL(image)も含めて保存
                String sql = "INSERT INTO mutters (name, text, reply_id, image) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, mutter.getUserName());
                ps.setString(2, mutter.getText());
                ps.setInt(3, mutter.getReplyId());
                ps.setString(4, mutter.getImage());
                
                int result = ps.executeUpdate();
                return result == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
