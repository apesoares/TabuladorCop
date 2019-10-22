/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria.util;

import java.sql.Connection;
import java.sql.ResultSet;
import ouvidoria.Db_class;

/**
 *
 * @author ROBSMAC
 */
public class UtilBD {

    public int Identifica_IdMestre(int pi_id) {
        int iRetorno = 0;

        Connection conn;
        try {
            conn = Db_class.mysql_conn();

            String query = "SELECT id_mestre "
                    + "FROM painel "
                    + "WHERE id = " + pi_id + " limit 1";

            ResultSet rs = Db_class.mysql_result(conn, query);

            if (rs.next()) {
                iRetorno = rs.getInt(1);
            }

            rs.close();
            conn.close();

        } catch (Exception ex) {
        }

        return iRetorno;
    }

}
