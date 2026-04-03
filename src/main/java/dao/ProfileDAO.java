package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Profile;

public class ProfileDAO {
	private final String JDBC_URL = "jdbc:mysql://localhost:3306/dokoTsubu?characterEncoding=UTF-8&serverTimezone=Asia/Tokyo";
    private final String DB_USER = "root";
    private final String DB_PASS = "carp8912"; // ご自身のパスワードに

    public Profile findByUserId(String userId) {
        Profile profile = null;
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
        } catch (SQLException e) { e.printStackTrace(); return null; }
        return profile;
    }

    public boolean update(Profile profile) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO profiles (user_id, name, address, personality, introduction, icon_filename) " +
                         "VALUES (?, ?, ?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE name=?, address=?, personality=?, introduction=?, icon_filename=?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, profile.getUserId());
            pStmt.setString(2, profile.getName());
            pStmt.setString(3, profile.getAddress());
            pStmt.setString(4, profile.getPersonality());
            pStmt.setString(5, profile.getIntroduction());
            pStmt.setString(6, profile.getIconFilename());
            // UPDATE用
            pStmt.setString(7, profile.getName());
            pStmt.setString(8, profile.getAddress());
            pStmt.setString(9, profile.getPersonality());
            pStmt.setString(10, profile.getIntroduction());
            pStmt.setString(11, profile.getIconFilename());

            int result = pStmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}