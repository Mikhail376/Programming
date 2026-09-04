package server.manager;

import common.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseManager {
    private final String jdbcUrl, dbUser, dbPass;

    public DatabaseManager(String jdbcUrl, String dbUser, String dbPass) {
        this.jdbcUrl = jdbcUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    public Map<Long, Dragon> loadAll() throws SQLException {
        Map<Long, Dragon> map = new TreeMap<>();
        String sql = "SELECT * FROM dragons ORDER BY collection_key";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getLong("collection_key"), mapDragon(rs));
            }
        }
        return map;
    }


    public long insertDragon(Dragon dragon, Long collectionKey) throws SQLException {
        String sql = "INSERT INTO dragons (collection_key, name, coord_x, coord_y, creation_date, age, wingspan, weight, character, head_size, head_eyes_count, head_tooth_count, owner_username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillParams(ps, dragon, collectionKey);
            if (ps.executeUpdate() > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getLong(1);
                }
            }
            return -1;
        }
    }

    public boolean updateDragon(Dragon dragon, Long collectionKey) throws SQLException {
        String sql = "UPDATE dragons SET name=?, coord_x=?, coord_y=?, age=?, wingspan=?, weight=?, character=?, head_size=?, head_eyes_count=?, head_tooth_count=? WHERE collection_key=?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            ps.setString(idx++, dragon.getName());
            ps.setInt(idx++, dragon.getCoordinates().getX());
            ps.setDouble(idx++, dragon.getCoordinates().getY());
            ps.setObject(idx++, dragon.getAge(), Types.BIGINT);
            ps.setDouble(idx++, dragon.getWingspan());
            ps.setFloat(idx++, dragon.getWeight());
            ps.setString(idx++, dragon.getCharacter().name());
            if (dragon.getHead() != null) {
                ps.setLong(idx++, dragon.getHead().getSize());
                ps.setLong(idx++, dragon.getHead().getEyesCount());
                ps.setDouble(idx++, dragon.getHead().getToothCount());
            } else {
                ps.setNull(idx++, Types.BIGINT); ps.setNull(idx++, Types.BIGINT); ps.setNull(idx++, Types.DOUBLE);
            }
            ps.setLong(idx++, collectionKey);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeDragon(Long collectionKey) throws SQLException {
        String sql = "DELETE FROM dragons WHERE collection_key = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, collectionKey);
            return ps.executeUpdate() > 0;
        }
    }

    public void clearAll() throws SQLException {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM dragons");
        }
    }

    private void fillParams(PreparedStatement ps, Dragon d, Long key) throws SQLException {
        int idx = 1;
        ps.setLong(idx++, key);
        ps.setString(idx++, d.getName());
        ps.setInt(idx++, d.getCoordinates().getX());
        ps.setDouble(idx++, d.getCoordinates().getY());
        ps.setTimestamp(idx++, Timestamp.valueOf(d.getCreationDate()));
        ps.setObject(idx++, d.getAge(), Types.BIGINT);
        ps.setDouble(idx++, d.getWingspan());
        ps.setFloat(idx++, d.getWeight());
        ps.setString(idx++, d.getCharacter().name());
        if (d.getHead() != null) {
            ps.setLong(idx++, d.getHead().getSize());
            ps.setLong(idx++, d.getHead().getEyesCount());
            ps.setDouble(idx++, d.getHead().getToothCount());
        } else {
            ps.setNull(idx++, Types.BIGINT); ps.setNull(idx++, Types.BIGINT); ps.setNull(idx++, Types.DOUBLE);
        }
        ps.setString(idx++, d.getOwner());
    }

    private Dragon mapDragon(ResultSet rs) throws SQLException {
        Coordinates coords = new Coordinates(rs.getInt("coord_x"), rs.getDouble("coord_y"));
        DragonHead head = null;
        if (rs.getObject("head_size") != null) {
            head = new DragonHead(rs.getLong("head_size"), rs.getLong("head_eyes_count"), rs.getDouble("head_tooth_count"));
        }
        return new Dragon(
                rs.getLong("id"),
                rs.getString("name"),
                coords,
                rs.getTimestamp("creation_date").toLocalDateTime(),
                rs.getObject("age", Long.class),
                rs.getDouble("wingspan"),
                rs.getFloat("weight"),
                DragonCharacter.valueOf(rs.getString("character")),
                head,
                rs.getString("owner_username")
        );
    }
}