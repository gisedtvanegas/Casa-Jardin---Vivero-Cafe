/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    private Connection conn;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String host    = getEnvOrDefault("MYSQLHOST", "localhost");
    private String port    = getEnvOrDefault("MYSQLPORT", "3307");
    private String user    = getEnvOrDefault("MYSQLUSER", "root");
    private String password = getEnvOrDefault("MYSQLPASSWORD", "");
    private String baseDatos = getEnvOrDefault("MYSQLDATABASE", "viverodb");

    private String railwayUrl = getEnvOrDefault("MYSQL_URL", "");
    private String url = construirUrl();

    private static String getEnvOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    private String construirUrl() {
        if (!railwayUrl.isEmpty()) {
            String jdbcUrl = railwayUrl.startsWith("jdbc:") ? railwayUrl : "jdbc:" + railwayUrl;
            return jdbcUrl + (jdbcUrl.contains("?") ? "&" : "?")
                    + "useTimezone=true&serverTimezone=UTC";
        }
        return "jdbc:mysql://" + host + ":" + port + "/" + baseDatos
                + "?useTimezone=true&serverTimezone=UTC";
    }

    public Conexion() {
        conn = null;
        try {
            Class.forName(driver);
            // Railway puede entregar MYSQL_URL o las variables MYSQLHOST,
            // MYSQLPORT, MYSQLUSER, MYSQLPASSWORD y MYSQLDATABASE.
            conn = railwayUrl.isEmpty()
                    ? DriverManager.getConnection(url, user, password)
                    : DriverManager.getConnection(url);
            if (conn == null) {
                System.out.println("No se estableció la conexion" + "\n" + url);
            } else {
                System.out.println("Conexión Establecida ");
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public Connection getConn() {
        return conn;
    }
}
