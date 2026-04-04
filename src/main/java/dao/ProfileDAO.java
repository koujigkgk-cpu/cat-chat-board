package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Profile;

public class ProfileDAO {
    // Supabaseの接続情報
    // 修正後のURL例（ポートを6543にし、末尾にオプションを追加）
// ポートを5432にし、クラウド接続を安定させるオプションをすべて盛り込みました
// 修正前: ...co:5432/postgres?sslmode=require...
// 修正後: 以下のURLにまるごと差し替えてみてください
// これが「Java専用」の接続伝票です
private final String JDBC_URL = "jdbc:postgresql://db.arpakswzlfpntdwrrghy.supabase.co:6543/postgres?sslmode=require&prepareThreshold=0&loginTimeout=20"; 
private final String DB_USER = "postgres";
    private final String DB_PASS = "carp8912carp";
    
    public Profile findByUserId(String userId) {
        Profile profile = null;
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                String sql = "SELECT user_id, name, address, personality, introduction, icon_filename FROM profiles WHERE user_id = ?";
                PreparedStatement pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, userId);
                ResultSet rs = pStmt.executeQuery();

                if (rs.next()) {
                    profile = new Profile(
                        rs.getString("user_id"), rs.getString("name"),
                        rs.getString("address"), rs.getString("personality"),
                        rs.getString("introduction"), rs.getString("icon_filename")
                    );
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
            return null; 
        }
        return profile;
    }

    public boolean update(Profile profile) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                // PostgreSQL用のUPSERT（なければ挿入、あれば更新）構文
                String sql = "INSERT INTO profiles (user_id, name, address, personality, introduction, icon_filename) " +
                             "VALUES (?, ?, ?, ?, ?, ?) " +
                             "ON CONFLICT (user_id) DO UPDATE SET " +
                             "name=EXCLUDED.name, address=EXCLUDED.address, personality=EXCLUDED.personality, " +
                             "introduction=EXCLUDED.introduction, icon_filename=EXCLUDED.icon_filename";
                
                PreparedStatement pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, profile.getUserId());
                pStmt.setString(2, profile.getName());
                pStmt.setString(3, profile.getAddress());
                pStmt.setString(4, profile.getPersonality());
                pStmt.setString(5, profile.getIntroduction());
                pStmt.setString(6, profile.getIconFilename());

                int result = pStmt.executeUpdate();
                return result > 0;
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}
