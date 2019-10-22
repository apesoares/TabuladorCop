/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

/**
 *
 * @author N0026925
 */
public class painel_encaixe extends painel {

    JLabel current_label;
    JLabel current_label_passado;

    public painel_encaixe(Menu mn, int ifl_n3) {

        super(mn, "COP NET", ifl_n3);

        JCheckBox chk = getjCheckBox1();
        JLabel label_encx = getLabel_encaixe();
        JLabel label_qtde = getLabel_qtd();
        chk.setVisible(false);
        label_encx.setVisible(false);
        label_qtde.setVisible(false);

    }

    @Override
    public void atualiza_painel() {

        my_panel.removeAll();
        my_panel.repaint();
        my_panel.revalidate();

        String qCriterio = "WHERE painel.status = 'Encaixe' "
                + "and painel.cidade IN (" + filter_city_query + ") "
                + "and painel.grupo_os IN (" + filter_type_query + ") "
                + "and painel.janela IN (" + filter_janela_query + ") "
                //+ "and painel.data_acomp = '" + global.get_simple_date(this.filter_date) + "' "
                + "and painel.data_acomp in (" + filter_date_query + ") "
                + "and painel.tipo_trat = 'Acompanhamento' "
                //+ "and painel.os_principal = 'Sim' "
                + "and painel.canal <> 'BSOD' "
                + "and painel.fl_n3 = " + ifl_n3 + " ";

        String qCriterioUmRegistro = "and painel.id in "
                + "("
                + "SELECT min(id) as id "
                + "FROM ouvidoria.painel "
                + qCriterio
                + "GROUP BY contrato, janela, data_acomp "
                + ")";

        String query = "SELECT id, "
                + "ifnull(painel.contrato,'') as Contrato, "
                + "ifnull(painel.cidade,'') as Cidade, "
                + "ifnull(DATE_FORMAT(painel.data_acomp,'%d/%m/%Y'),'') as Data, "
                + "ifnull(painel.janela,'') as Janela, "
                + "ifnull(painel.tipo_os,'') as 'Tipo OS', "
                + "ifnull(painel.nome_cli,'') as 'Nome cliente', "
                + "ifnull(painel.epo,'') as EPO, "
                + "ifnull(painel.login_tec,'') as 'Login Tecnico', "
                + "ifnull(painel.status,'') as Status, "
                + "ifnull(painel.status_monitoria,'') as status_monitoria, "
                + "ifnull(painel.canal,'') as Canal, "
                + "ifnull(painel.user_mark,'') as 'Usuário Tratando', "
                + "ifnull(painel.obs_horario,'') as partic, "
                + "ifnull(painel.tecnico,'') as Técnico, "
                + "painel.telefone_tec, "
                + "if(oc.status regexp ('Pendente'), 'Pendente', 'OS Executada') as 'Status NET', "
                + "ifnull(sms,'Não') as SMS, "
                + "ifnull(painel.area_despacho, '') as 'Área de despacho' "
                + "FROM painel "
                + "LEFT JOIN (SELECT \n"
                + "    `painel`.`contrato`,\n"
                + "    group_concat(`painel`.`num_os` separator '-'),    \n"
                + "	if(ocupacao.cod_os is null,'Encerrado', 'Pendente') as status_net_sms,\n"
                + "    group_concat(if(ocupacao.cod_os is null,'Encerrado', 'Pendente') separator '-') as status\n"
                + "FROM `ouvidoria`.`painel`\n"
                + "LEFT JOIN ouvidoria.ocupacao ON painel.num_os = ocupacao.cod_os\n"
                + "GROUP BY contrato) oc "
                + "ON painel.contrato = oc.contrato "
                + qCriterio
                + qCriterioUmRegistro
                + "ORDER BY painel.cidade, painel.janela, painel.contrato ";

        //System.out.println(query);

        try {
            JTable tab = global.getTable(query, my_panel);

            main_tab = tab;

            int[] invisible_columns = {0, 10, 13, 14,15,16};
            int[] column_widths = {0, 50, 80, 60, 60, 150, 90, 100, 80, 100, 0, 60, 80, 0, 0, 0, 0, 50, 100};

            global.hide_columns(invisible_columns, main_tab);
            global.adjust_columns(column_widths, main_tab);

            main_tab.addMouseListener(new painel_right_click());

            for (int columnTable = 1; columnTable < main_tab.getColumnCount(); columnTable++) {

                main_tab.getColumnModel().getColumn(columnTable).setCellRenderer(new CustomRenderer());

            }

            TableFilterHeader filter = new TableFilterHeader(main_tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);

            main_tab.addKeyListener(new ClipboardKeyAdapter(main_tab));

            if (label_qtd_casos != null) {

                label_qtd_casos.setText(String.valueOf(main_tab.getRowCount()));

            }

            Connection conn = Db_class.mysql_conn();

            String query_passado = "SELECT COUNT(*) FROM painel "
                    + "WHERE data_acomp < current_date and status = 'Encaixe' "
                    + "and cidade IN (" + filter_city_query + ") and "
                    + "grupo_os IN (" + filter_type_query + ") and "
                    + "janela IN (" + filter_janela_query + ") and "
                    + "tipo_trat = 'Acompanhamento' "
                    //+ "and os_principal = 'Sim' ";
                    + "and fl_n3 = " + ifl_n3 + " ";

            ResultSet rs = Db_class.mysql_result(conn, query_passado);

            if (rs.next()) {
                long qtd_passado = rs.getLong(1);
                label_qtd_passado.setText(String.valueOf(qtd_passado));
            } else {
                label_qtd_passado.setText("0");
            }

            String query_datas_passadas = "SELECT DATE_FORMAT(data_acomp,'%d/%m/%Y'), COUNT(*) "
                    + "FROM painel "
                    + "WHERE data_acomp < current_date and "
                    + "cidade IN (" + filter_city_query + ") and "
                    + "grupo_os IN (" + filter_type_query + ") and "
                    + "janela IN (" + filter_janela_query + ") "
                    + "and status = 'Encaixe' and "
                    + "tipo_trat = 'Acompanhamento' "
                    //+ "and os_principal = 'Sim' "
                    + "and fl_n3 = " + ifl_n3 + " "
                    + "GROUP BY data_acomp "
                    + "ORDER BY data_acomp DESC ";

            rs = Db_class.mysql_result(conn, query_datas_passadas);

            String table_code = "<table border=\"1\" cellpadding=\"5\">"
                    + "<tr bgcolor=\"000000\" font color=\"FFFFFF\">"
                    + "<th>Data</th>"
                    + "<th>Qtd</th>"
                    + "</tr>";

            boolean ctrl_past = false;

            while (rs.next()) {

                table_code = table_code + "<tr>"
                        + "<td>" + rs.getString(1) + "</td>"
                        + "<td>" + rs.getLong(2) + "</td>"
                        + "</tr>";
                ctrl_past = true;

            }

            if (ctrl_past) {

                table_code = "<html>" + table_code + "</table><html>";

                label_qtd_passado.setToolTipText(table_code);

            }

            Db_class.close_conn(conn);

        } catch (Exception ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
        }

        my_panel.repaint();
        my_panel.revalidate();

    }

}