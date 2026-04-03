package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LikeDAO {
    // Supabaseの接続情報に変更
    private final String JDBC_URL = "jdbc:postgresql://db.arpakswzlfpntdwrrghy.supabase.co:5432/postgres";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "あなたのパスワード"; // ★Supabaseで設定したパスワードに変更してください

    public int toggleLike(int mutterId, String userName) {
        try {
            // PostgreSQLのドライバをロード
            Class.forName("org.postgresql.Driver");
            
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                // すでにいいねがあるかチェック (PostgreSQLに合わせてテーブル名は小文字推奨)
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
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return 0;
    }
}
