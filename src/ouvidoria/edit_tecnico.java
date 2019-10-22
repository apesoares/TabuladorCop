/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author N0026925
 */
public class edit_tecnico extends novo_tecnico {

    int id;
    edit_tecnico et;
    consulta_tec ct;

    public edit_tecnico(Menu mn, String[] array_tec, int id, consulta_tec ct) {

        super(mn, true, "", "COP NET");

        this.id = id;
        this.ct = ct;

        et = this;

        fill_fields(array_tec);

        for (ActionListener al : botao_inserção.getActionListeners()) {

            botao_inserção.removeActionListener(al);

        }

        botao_inserção.setText("Editar Técnico");
        botao_inserção.addActionListener(new edit_save());

        global.open_modal(this, "Editar técnico");

    }

    public void fill_fields(String[] array_tec) {

        cidade.setSelectedItem(array_tec[0]);
        area.setSelectedItem(array_tec[1]);
        login.setText(array_tec[2]);
        nome.setText(array_tec[3]);
        parceira.setSelectedItem(array_tec[4]);
        telefone.setText(array_tec[5]);
        turno.setSelectedItem(array_tec[6]);
        tipo_tec.setSelectedItem(array_tec[7]);
        obs.setText(array_tec[8]);

    }

    public class edit_save implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            if (!check_fields()) {

                return;

            }

            editar();

        }

    }

    public void editar() {

        try {
            Connection conn = Db_class.mysql_conn();

            String query_cluster = "SELECT cluster_tec "
                    + "FROM cidades_novo "
                    + "WHERE ci_depara = '" + cidade.getSelectedItem() + "' "
                    + "OR cluster_tec = '" + cidade.getSelectedItem() + "'"
                    + "GROUP BY cluster_tec";

            ResultSet rs = Db_class.mysql_result(conn, query_cluster);

            rs.next();

            String cluster = rs.getString(1);

            String query = "UPDATE tecnicos SET "
                    + "cluster = '" + cluster + "', "
                    + "cidade = '" + cidade.getSelectedItem() + "', "
                    + "area = '" + area.getSelectedItem() + "', "
                    + "login = '" + login.getText() + "', "
                    + "nome = '" + nome.getText() + "', "
                    + "parceira = '" + parceira.getSelectedItem() + "', "
                    + "telefone = '" + telefone.getText() + "', "
                    + "obs = '" + obs.getText() + "', "
                    + "turno_tec = '" + turno.getSelectedItem() + "', "
                    + "tipo_tec = '" + tipo_tec.getSelectedItem() + "' "
                    + "WHERE idtecnicos = " + id;

            Db_class.mysql_insert(query, conn);

            global.insert_prod(0, "Alteração cadastro técnico", mn);

            global.show_message("Técnico alterado com sucesso!");

            et.dispose();

            ct.atualiza_tec();

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(edit_tecnico.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
