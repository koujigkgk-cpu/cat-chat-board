package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.User;

public class UserDAO {
    private final String JDBC_URL = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:6543/postgres?sslmode=require";
    private final String DB_USER = "postgres.arpakswzlfpntdwrrghy";
    private final String DB_PASS = "arp8912carp";

    public boolean create(User user) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        // カラム名に合わせてSQLを修正
        String sql = "INSERT INTO ACCOUNTS(USER_ID, PASS, NAME) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            
            pStmt.setString(1, user.getId());
            pStmt.setString(2, user.getPass());
            pStmt.setString(3, user.getName()); // 必要であれば名前も保存

            int result = pStmt.executeUpdate();
            return (result == 1);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}