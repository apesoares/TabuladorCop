/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ouvidoria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author N0026925
 */
public class Db_class_contatos {

    public static Connection mysql_conn() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        //ALTERAR_BCC_OK      
        //String url = "jdbc:mysql://10.5.12.185/";
        String url = "jdbc:mysql://10.5.9.185/";
        String dbName = "ouvidoria";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "common_user";
        String password = "Pvm5jrCZWQcUPyhc";

        Connection conn = null;

        Class.forName(driver).newInstance();
        try {
            conn = DriverManager.getConnection(url + dbName, userName, password);
        } catch (SQLException ex) {

        }

        return conn;
    }

    public static ResultSet mysql_result(Connection conn, String query) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet res = st.executeQuery(query);

        return res;
    }

    public static int mysql_insert(String query, Connection conn) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        Statement st = conn.createStatement();
        int res = st.executeUpdate(query);

        return res;

    }

    public static void close_conn(Connection conn) {

        try {
            conn.close();
        } catch (SQLException ex) {

        }

    }

}
