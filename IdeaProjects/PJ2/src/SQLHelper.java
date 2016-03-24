
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class SQLHelper {
// jdbc:mysql://192.168.33.11:3306/ccbfl?characterEncoding=utf-8
    public static String driver = "com.mysql.jdbc.Driver";
    public static String url = "jdbc:mysql://127.0.0.1:3306/note?characterEncoding=utf-8";
    public static String user = "root";
    public static String password = "jly941205";

    public static Connection getConnetion() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.print("connect");
        return conn;
    }
}
