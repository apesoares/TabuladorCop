/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import com.human.gateway.client.bean.Response;
import com.human.gateway.client.bean.SimpleMessage;
import com.human.gateway.client.exception.ClientHumanException;
import com.human.gateway.client.service.SimpleMessageService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author N0026925
 */
public class zenvia_test {

    public zenvia_test() {

    }

    public SimpleMessageService get_service(String account, String password) {

        SimpleMessageService cliente = new SimpleMessageService(account, password);

        return cliente;

    }

    public List send(SimpleMessageService cliente, String phone, String msg, String id, String from) {

        SimpleMessage mensagem = new SimpleMessage();

        mensagem.setTo(phone);
        mensagem.setMessage(msg);
        mensagem.setId(id);
        mensagem.setFrom(from);

        try {

            List<Response> retornos = cliente.send(mensagem);

            return retornos;

        } catch (ClientHumanException ex) {
            Logger.getLogger(zenvia_test.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public String consulta(SimpleMessageService cliente, String ids) {

        try {
            List<Response> retornos = cliente.query(ids);

            for (Response r : retornos) {

                String codigo = r.getReturnCode();
                String msg = r.getReturnDescription();

                System.out.println(codigo + " - " + msg);

                String query = "SELECT status_final FROM zenvia_codes "
                        + "WHERE codigo = " + codigo;

                Connection conn = Db_class.mysql_conn();

                ResultSet rs = Db_class.mysql_result(conn, query);

                rs.next();

                String retorno = rs.getString(1);

                System.out.println(codigo + " - " + retorno);

                return retorno;

            }

        } catch (ClientHumanException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(zenvia_test.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
