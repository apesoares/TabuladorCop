/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author ROBSMAC Registrar as ações sobre as OSs, inclusive movimentações de
 * níveis
 */
public class LogS {

    public Boolean LogComum_Inclusao(Integer iID_Mestre, String sNum_OS, String sTipo_OS, Integer iID_Operacao, Integer iNivel_0, Integer iNivel_1) {
        Boolean bGravou = true; //nenhum erro
        try {
            Connection conn = Db_class.mysql_conn();
            StringBuilder sSQL = new StringBuilder();

            sSQL.append("insert into tb_log_comum \n");
            sSQL.append("(id_os, num_os, tipo_os, id_operacao, nivel_0, nivel_1, login, dt) \n");
            sSQL.append("values \n");
            sSQL.append("( \n");
            sSQL.append(iID_Mestre).append(", '").append(sNum_OS).append("', '");
            sSQL.append(sTipo_OS).append("', ").append(iID_Operacao).append(", \n");
            sSQL.append(iNivel_0).append(", ").append(iNivel_1).append(", \n");
            sSQL.append("'").append(System.getProperty("user.name")).append("', now() \n");
            sSQL.append(") \n");

            Db_class.mysql_insert(sSQL.toString(), conn);
            conn = null;
        } catch (Exception e) {
            bGravou = false;
        }
        return bGravou;
    }

    public void LogComum_InclusaoPacote(Integer iID_Mestre, Integer iID_Operacao, Integer iNivel_0, Integer iNivel_1) {
        Integer iQtd_Erros = 0;
        StringBuilder sSQL = new StringBuilder();
        Connection conn;
        ResultSet rs;

        try {
            conn = Db_class.mysql_conn();
            sSQL.append("select id_mestre, num_os, tipo_os \n");
            sSQL.append("from painel \n");
            sSQL.append("where id_mestre = ").append(iID_Mestre).append(" \n");

            rs = Db_class.mysql_result(conn, sSQL.toString());

            while (rs.next()) {                
                if (!LogComum_Inclusao(iID_Mestre, rs.getString(2), rs.getString(3), iID_Operacao, iNivel_0, iNivel_1)) {
                    iQtd_Erros = iQtd_Erros + 1;
                }
            }
            
            rs.close();
            conn.close();
            rs = null;
            conn = null;

            if (iQtd_Erros > 0) {
                global.show_warning_message("Houve " + iQtd_Erros + " problemas de registro de histórico de ações.\n\n" +  "Favor reportar seu supervisor.");
            }

        } catch (Exception e) {
            global.show_error_message("Problemas no registro de histórico de ações.\n\n" + "Erro: " + e.getMessage() + "n\n" + "Favor reportar seu supervisor.");
        }
    }

    public void LogComum_InclusaoEncerramento(Integer iID_Mestre, Integer iFl_N3) {
        Integer iQtd_Erros = 0;
        Integer iNivel = (iFl_N3==1)?3:2;
        StringBuilder sSQL = new StringBuilder();
        Connection conn;
        ResultSet rs;

        try {
            conn = Db_class.mysql_conn();
            sSQL.append("select id_mestre, num_os, tipo_os \n");
            sSQL.append("from painel \n");
            sSQL.append("where id_mestre = ").append(iID_Mestre).append(" \n");
            sSQL.append("and fl_n3 = ").append(iFl_N3).append(" \n");

            rs = Db_class.mysql_result(conn, sSQL.toString());

            while (rs.next()) {                
                if (!LogComum_Inclusao(iID_Mestre, rs.getString(2), rs.getString(3), 6, iNivel, iNivel)) {
                    iQtd_Erros = iQtd_Erros + 1;
                }
            }
            
            rs.close();
            conn.close();
            rs = null;
            conn = null;

            if (iQtd_Erros > 0) {
                global.show_warning_message("Houve " + iQtd_Erros + " problemas de registro de histórico de ações.\n\n" +  "Favor reportar seu supervisor.");
            }

        } catch (Exception e) {
            global.show_error_message("Problemas no registro de histórico de ações.\n\n" + "Erro: " + e.getMessage() + "n\n" + "Favor reportar seu supervisor.");
        }
    }

}
