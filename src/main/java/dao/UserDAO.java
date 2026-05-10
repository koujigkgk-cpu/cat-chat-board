package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDAO {
    private final String JDBC_URL = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:6543/postgres?sslmode=require";
    private final String DB_USER = "postgres.arpakswzlfpntdwrrghy";
    private final String DB_PASS = "Carp8912Carp";

    // --- 新規登録用メソッド（Register.javaから呼ばれる） ---
    public boolean create(User user) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "INSERT INTO accounts(user_id, pass, name) VALUES (?, ?, ?)";        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            
            pStmt.setString(1, user.getId());
            pStmt.setString(2, user.getPass());
            pStmt.setString(3, user.getName());

            int result = pStmt.executeUpdate();
            return (result == 1);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- ログイン検索用メソッド（LoginLogic.javaから呼ばれる） ---
    public User findByLogin(User user) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        // IDとパスワードが一致するレコードを探すSQL
        String sql = "SELECT * FROM accounts WHERE user_id = ? AND pass = ?";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {
            
            pStmt.setString(1, user.getId());
            pStmt.setString(2, user.getPass());

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    // 一致するユーザーが見つかったら、DBの内容でUserインスタンスを作成して返す
                    String id = rs.getString("user_id");
                    String pass = rs.getString("pass");
                    String name = rs.getString("name");
                    return new User(id, name, pass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null; // 見つからなければnullを返す
    }
}