package server.manager;

import java.security.MessageDigest;
import java.sql.*;

public class AuthManager {
    private final String jdbcUrl;
    private final String dbUser;
    private final String dbPass;

    public AuthManager(String jdbcUrl, String dbUser, String dbPass) {
        this.jdbcUrl = jdbcUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    public boolean authenticate(String username, String passwordHash) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка БД при проверке пользователя: " + e.getMessage());
            return false;
        }
    }

    public boolean register(String username, String passwordHash) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?) ON CONFLICT (username) DO NOTHING";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка БД при регистрации: " + e.getMessage());
            return false;
        }
    }

}