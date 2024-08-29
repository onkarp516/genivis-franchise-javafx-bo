package com.opethic.genivis.sql_lite;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.Base64;

public class UserToken {
    //    private static final String url = "jdbc:sqlite:src/main/resources/com/opethic/genivis/ui/SQLliteDB/genivis_user.db";
    private static final String url = "jdbc:sqlite:" + System.getenv("db_file");

    private static Connection conn;

    public static void setToken(String access_token, String refresh_token, String role) {
        //Add finally block
        try {

            conn = DriverManager.getConnection(url);

            if (tableExists(conn)) {
                dropTable(conn, "user_token");
            }
            if (!tableExists(conn)) {
                createTable(conn);
            }
            String insertSQL = "INSERT INTO user_token (access_token, refresh_token,role) VALUES (?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            pstmt.setString(1, access_token);
            pstmt.setString(2, refresh_token);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
            //  System.out.println("Token set successfully!");
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }
    }

    private static boolean tableExists(Connection conn) {
        try {
            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='user_token'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            pstmt.close();
            return exists;
        } catch (SQLException e) {
            System.err.println("Error checking if table exists: " + e.getMessage());
            return false;
        }
    }


    private static void dropTable(Connection conn, String tableName) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            //System.out.println("Table dropped successfully!");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error dropping table: " + e.getMessage());
        }
    }


    private static void createTable(Connection conn) throws SQLException {
        String createTableSQL = "CREATE TABLE user_token (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "access_token TEXT,\n"
                + "refresh_token TEXT,\n"
                + "role TEXT\n"
                + ")";
        try (var stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            //System.out.println("Table created successfully!");
        }
    }

    /**
     * @implNote This method return json object of plain text encoded in JWT token.
     * @author Shrikant Ande
     * @since 4 Apr 2024
     */
    public static JsonObject getAccessTokenDetails() {
        JsonObject payload = new JsonObject();

        try {
            conn = DriverManager.getConnection(url);
            String selectSQL = "SELECT * FROM user_token";

            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    String access_token = "";
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        access_token = rs.getString("access_token");
                        String refresh_token = rs.getString("refresh_token");
                    }
                    String[] parts = access_token.split("\\.");
                    if (parts.length == 3) {
                        //valid token
//                        header=new Gson().fromJson(decode(parts[0]),JsonObject.class);
                        payload = new Gson().fromJson(decode(parts[1]), JsonObject.class);
//                        signature=new Gson().fromJson(decode(parts[2]),JsonObject.class);
                    }

                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching token: " + e.getMessage());
        } finally {

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }


        }
        return payload;
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    public static String getAccessToken() {
        String access_token = null;
        try {
            conn = DriverManager.getConnection(url);
            String selectSQL = "SELECT access_token FROM user_token WHERE id = 1";

            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        access_token = rs.getString("access_token");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching token: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }

        }

        return access_token;
    }

    private static String getRole() {
        String role = null;
        try {
            conn = DriverManager.getConnection(url);
            String selectSQL = "SELECT role FROM user_token WHERE id = 1";

            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        role = rs.getString("role");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching token: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return role;
    }

    private static String getRefreshToken() {
        String refresh_token = null;
        try {
            conn = DriverManager.getConnection(url);
            String selectSQL = "SELECT refresh_token FROM user_token WHERE id = 1";

            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        refresh_token = rs.getString("refresh_token");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching token: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        return refresh_token;
    }


}
