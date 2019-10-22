/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

/**
 *
 * @author N0026925
 */
public class painel_n3 extends painel {

    JLabel current_label;
    JLabel current_label_passado;

    public painel_n3(Menu mn, int ifl_n3) {

        super(mn, "COP NET", ifl_n3);

        //JCheckBox chk = getjCheckBox1();
        JLabel label_encx = getLabel_encaixe();
        JLabel label_qtde = getLabel_qtd();
        //chk.setVisible(false);
        label_encx.setVisible(false);
        label_qtde.setVisible(false);

    }

    @Override
    public void atualiza_painel() {

        my_panel.removeAll();
        my_panel.repaint();
        my_panel.revalidate();

        String part_encerr = "";
        if (jCheckBox1.isSelected()) {
            part_encerr = "";
        } else {
            part_encerr = "painel.status not in ('Encerrado','Encerrado - Os Duplicada/Cancelada') and";
        }
        
        String qCriterio = "where " + part_encerr + " painel.cidade IN (" + filter_city_query + ") "
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
        //qCriterioUmRegistro = "";

        String query = "select * from "
                + "("
                + "select a.*, b.dt_baixa as 'Hora Acomp', "
                + "ouvidoria.fc_ExtraiSLAdt(b.dt_baixa, b.dt_prevista, 1, 0, 1) as SLA, "
                //+ "b.situacao, "
                + "case "
                + "when a.status_caso like '%encerrado%' or b.situacao is null then 'cinza' "
                + "else b.situacao "
                + "end  "
                + "as situacao, "
                + "b.dt_baixa_n3 as 'Baixa N3', "
                + "case b.situacao "
                + "when 'roxo' then 1 "
                + "when 'vermelho' then 2 "
                + "when 'amarelo' then 3 "
                + "when 'verde' then 4 "
                + "else 99 "
                + "end ordem_prioridade "
                + "from "
                + "("
                + "SELECT painel.id, "
                + "ifnull(painel.contrato,'') as Contrato, "
                + "ifnull(painel.cidade,'') as Cidade, "
                + "ifnull(DATE_FORMAT(painel.data_acomp,'%d/%m/%Y'),'') as Data, "
                + "ifnull(painel.janela,'') as Janela, "
                + "ifnull(painel.tipo_os,'') as 'Tipo OS', "
                + "ifnull(painel.nome_cli,'') as 'Nome cliente', "
                + "ifnull(painel.epo,'') as EPO, "
                + "ifnull(painel.login_tec,'') as 'Login Tecnico', "
                + "ifnull(painel.status,'') as status_caso, "
                + "ifnull(painel.status_monitoria,'') as status_monitoria, "
                + "ifnull(painel.canal,'') as Canal, "
                + "ifnull(painel.user_mark,'') as 'Usuário Tratando', "
                + "ifnull(painel.obs_horario,'') as partic, "
                + "ifnull(painel.tecnico,'') as Técnico, "
                + "painel.telefone_tec, "
                + "if(oc.status regexp ('Pendente'), 'Pendente', 'OS Executada') as 'Status NET', "
                + "ifnull(sms,'Não') as SMS, "
                + "ifnull(painel.id_mestre, 0) as id_mestre, "
                + "ifnull(painel.tipo_os, '') as tipo_os, "
                + "ifnull(painel.num_os, 0) as num_os, "
                + "ifnull(acao_contrato.nome, '?') as 'Motivo Acomp' "
                + "FROM painel "
                + "LEFT JOIN \n"
                + "(\n"
                + "SELECT \n"
                + "`painel`.`contrato`,\n"
                + "group_concat(`painel`.`num_os` separator '-'),    \n"
                + "if(ocupacao.cod_os is null,'Encerrado', 'Pendente') as status_net_sms,\n"
                + "group_concat(if(ocupacao.cod_os is null,'Encerrado', 'Pendente') separator '-') as status\n"
                + "FROM `ouvidoria`.`painel`\n"
                + "LEFT JOIN ouvidoria.ocupacao ON painel.num_os = ocupacao.cod_os\n"
                + "where painel.data_acomp in (" + filter_date_query + ")\n"
                + "GROUP BY contrato\n"
                + ") oc "
                + "ON painel.contrato = oc.contrato \n"
                + "left join ouvidoria.tb_acao_contrato acao_contrato on acao_contrato.id = painel.id_acao_contrato \n"
                //+ "WHERE painel.status = 'Encaixe' " + "and painel.cidade IN (" + filter_city_query + ") "
                //+ "where painel.cidade IN (" + filter_city_query + ") "
                /*
                + "where " + part_encerr + " painel.cidade IN (" + filter_city_query + ") "
                + "and painel.grupo_os IN (" + filter_type_query + ") "
                + "and painel.janela IN (" + filter_janela_query + ") "
                //+ "and painel.data_acomp = '" + global.get_simple_date(this.filter_date) + "' "
                + "and painel.data_acomp in (" + filter_date_query + ") "
                + "and painel.tipo_trat = 'Acompanhamento' "
                //+ "and painel.os_principal = 'Sim' "
                + "and painel.canal <> 'BSOD' "
                + "and painel.fl_n3 = " + ifl_n3 + " "
                */
                + qCriterio
                + qCriterioUmRegistro
                + "ORDER BY painel.cidade, painel.janela "
                + ") a "
                + "left join "
                + "("
                + "select distinct id_mestre, num_os, tipo_os, dt_baixa, dt_prevista, dt_media, \n"
                + "case \n"
                + "when dt_prevista < dt_baixa_n3 then 'cinza' \n"
                + "when dt_prevista < now() then 'roxo' \n"
                + "when dt_prevista <= date_add(now(), interval +30 minute)  then 'vermelho' \n"
                + "when now() >= dt_media then 'amarelo' \n"
                + "else 'verde' \n"
                + "end situacao, \n"
                + "dt_baixa_n3 \n"
                + "from ouvidoria.tb_acao_registro_n3 \n"
                + "where dt_baixa >= date_add(now(), interval -30 day) \n"
                + ") b on a.id_mestre = b.id_mestre and a.num_os = b.num_os and a.tipo_os = b.tipo_os \n"
                + ") c "
                + "order by ordem_prioridade, data, janela, contrato, 'tipo os'";

        //System.out.println(query);
        try {
            JTable tab = global.getTable(query, my_panel);

            main_tab = tab;

            //int[] invisible_columns = {0, 10, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26};
            int[] invisible_columns = {0, 13, 14, 15, 16, 17, 18, 19, 20, 24, 26};
            int[] column_widths = {30, 50, 80,
                60, 80, 150,
                80, 80, 80,
                80, 80, 80,
                80, 50, 50,
                50, 50, 25,
                50, 30, 75,
                75, 100, 100,
                40, 50, 50
            };

            global.hide_columns(invisible_columns, main_tab);
            global.adjust_columns(column_widths, main_tab);

            main_tab.addMouseListener(new painel_right_click());

            for (int columnTable = 1; columnTable < main_tab.getColumnCount(); columnTable++) {

                main_tab.getColumnModel().getColumn(columnTable).setCellRenderer(new CustomRendererN3());

            }

            TableFilterHeader filter = new TableFilterHeader(main_tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);

            main_tab.addKeyListener(new ClipboardKeyAdapter(main_tab));

            if (label_qtd_casos != null) {
                label_qtd_casos.setText(String.valueOf(main_tab.getRowCount()));
            }

            //contando registros por cores
            if (tab.getRowCount() > 0) {
                int iQtd_Roxo = 0;
                int iQtd_Vermelho = 0;
                int iQtd_Amarelo = 0;
                int iQtd_Verde = 0;
                int iQtd_Outros = 0;

                String sOS = "";
                for (int i = 0; i < tab.getRowCount(); i++) {
                    sOS = tab.getValueAt(i, 24).toString();
                    if (sOS.isEmpty()) {
                        sOS = "";
                    }
                    
                    if (sOS.equals("roxo")) {
                        iQtd_Roxo = iQtd_Roxo + 1;
                    } else if (sOS.equals("vermelho")) {
                        iQtd_Vermelho = iQtd_Vermelho + 1;
                    } else if (sOS.equals("amarelo")) {
                        iQtd_Amarelo = iQtd_Amarelo + 1;
                    } else if (sOS.equals("verde")) {
                        iQtd_Verde = iQtd_Verde + 1;
                    } else {
                        iQtd_Outros = iQtd_Outros + 1;
                    }
                }

                sQtdDetalhe = "";
                sQtdDetalhe = sQtdDetalhe + "<html>";
                sQtdDetalhe = sQtdDetalhe + "<table>";
                sQtdDetalhe = sQtdDetalhe + "<tr bgcolor='#e2b4fc'><td>" + iQtd_Roxo + "</td><td>item(ns) com SLA não cumprido</td></tr>";
                sQtdDetalhe = sQtdDetalhe + "<tr bgcolor='#fcb4b4'><td>" + iQtd_Vermelho + "</td><td>item(ns) próximo(s) do SLA<br></td></tr>";
                sQtdDetalhe = sQtdDetalhe + "<tr bgcolor='#ffff99'><td>" + iQtd_Amarelo + "</td><td>item(ns) além de 50% do SLA<br></td></tr>";
                sQtdDetalhe = sQtdDetalhe + "<tr bgcolor='#c0fcb4'><td>" + iQtd_Verde + "</td><td>item(ns) novos<br></td></tr>";
                sQtdDetalhe = sQtdDetalhe + "<tr bgcolor='#ebebe0'><td>" + iQtd_Outros + "</td><td>de outro(s) item(ns)</td></tr>";
                sQtdDetalhe = sQtdDetalhe + "</table>";
                sQtdDetalhe = sQtdDetalhe + "</html>";
                
                //#a1a146

            } else {
                sQtdDetalhe = "";
                sQtdDetalhe = "Sem mais detalhes";
            }

            setToolTip_Mais_Detalhes(sQtdDetalhe);

            Connection conn = Db_class.mysql_conn();

            String query_passado = "SELECT COUNT(*) FROM painel "
                    + "WHERE data_acomp < current_date " // and status = 'Encaixe' "
                    + "and cidade IN (" + filter_city_query + ") and "
                    + "grupo_os IN (" + filter_type_query + ") and "
                    + "janela IN (" + filter_janela_query + ") and "
                    + "tipo_trat = 'Acompanhamento' "
                    + "and status not in ('Encerrado','Encerrado - Os Duplicada/Cancelada') "
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
                    //+ "and status = 'Encaixe' and "
                    + "and tipo_trat = 'Acompanhamento' "
                    //+ "and os_principal = 'Sim' "
                    + "and status not in ('Encerrado','Encerrado - Os Duplicada/Cancelada') "
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

    class CustomRendererN3 extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 6703872492730589499L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status_monit = (String) table.getValueAt(row, 10);
            String status_caso = (String) table.getValueAt(row, 9);
            String status_net = (String) table.getValueAt(row, 16);
            String sSituacao = (String) table.getValueAt(row, 24);
            Timestamp sDt_baixa_N3 = (Timestamp) table.getValueAt(row, 25);

            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);

            /*
             if (table.getValueAt(row, 1) == "10707361") {
             int x = 0;
             }
             */
            if (sDt_baixa_N3 == null) {
                if (sSituacao.equals("roxo")) {
                    if (isSelected) {
                        cellComponent.setBackground(global.get_color(179, 54, 188));
                        cellComponent.setForeground(table.getSelectionForeground());
                    } else {
                        cellComponent.setBackground(global.get_color(226, 180, 252));
                        cellComponent.setForeground(table.getForeground());
                    }
                    return cellComponent;
                }

                if (sSituacao.equals("vermelho")) {
                    if (isSelected) {
                        cellComponent.setBackground(global.get_color(188, 54, 54));
                        cellComponent.setForeground(table.getSelectionForeground());
                    } else {
                        cellComponent.setBackground(global.get_color(252, 180, 180));
                        cellComponent.setForeground(table.getForeground());
                    }
                    return cellComponent;
                }

                if (sSituacao.equals("amarelo")) {
                    if (isSelected) {
                        //cellComponent.setBackground(global.get_color(255, 255, 0));
                        cellComponent.setBackground(global.get_color(204, 204, 0));
                        cellComponent.setForeground(table.getSelectionForeground());
                    } else {
                        //cellComponent.setBackground(global.get_color(161, 161, 70));
                        cellComponent.setBackground(global.get_color(255, 255, 153));
                        cellComponent.setForeground(table.getForeground());
                    }
                    return cellComponent;
                }

                if (sSituacao.equals("verde")) {
                    if (isSelected) {
                        cellComponent.setBackground(global.get_color(76, 171, 85));
                        cellComponent.setForeground(table.getSelectionForeground());
                    } else {
                        cellComponent.setBackground(global.get_color(192, 252, 180));
                        cellComponent.setForeground(table.getForeground());
                    }
                    return cellComponent;
                }
            }

            if (isSelected) {
                //cellComponent.setBackground(global.get_color(207, 231, 245));
                //cellComponent.setForeground(global.get_color(0, 0, 0));
                cellComponent.setBackground(global.get_color(156, 156, 99));
                cellComponent.setForeground(table.getSelectionForeground());
            } else {
                //cellComponent.setBackground(global.get_color(255, 255, 255));
                //cellComponent.setForeground(global.get_color(0, 0, 0));
                cellComponent.setBackground(global.get_color(235, 235, 224));
                cellComponent.setForeground(table.getForeground());
            }

            /*
             cellComponent.setBackground(Color.WHITE);
             cellComponent.setForeground(table.getForeground());
             */
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);

            return cellComponent;
        }
    }

}
