/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

/**
 *
 * @author N0026925
 */
public class painel_bsod extends painel {

    JLabel current_label;
    JLabel current_label_passado;

    public painel_bsod(Menu mn) {

        super(mn, true, "COP NET");

        /*
        label_encaixe.setVisible(false);
        label_qtd_encaixe.setVisible(false);
        label_qtd_passado.setVisible(false);
        */
        pnl_info_passado.setVisible(false);
        pnl_info_encaixe.setVisible(false);

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

            part_encerr = "(painel.status <> 'Encerrado' and painel.status <> 'Encerrado - Os Duplicada/Cancelada') and";

        }

        String query = "SELECT id,  "
                + "ifnull(painel.contrato,'') as REC, "
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
                + "ifnull(painel.obs_horario,'') as Previsão, "
                + "ifnull(painel.tecnico,'') as Técnico, "
                + "painel.telefone_tec, "
                + "if(oc.status regexp ('Pendente'), 'Pendente', 'OS Executada') as 'Status NET' "
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
                + "WHERE " + part_encerr + " "
                + "painel.cidade IN (" + filter_city_query + ") "
                + "and painel.grupo_os IN (" + filter_type_query + ") "
                + "and painel.janela IN (" + filter_janela_query + ") and "
                //+ "painel.data_acomp = '" + global.get_simple_date(this.filter_date) + "' "
                + "painel.data_acomp in (" + filter_date_query + ") "
                + "and painel.tipo_trat = 'Acompanhamento' "
                + "and painel.os_principal = 'Sim' "
                + "and painel.canal = 'BSOD' "
                + "ORDER BY painel.cidade, painel.janela ";

        System.out.println(query);

        try {
            JTable tab = global.getTable(query, my_panel);

            main_tab = tab;

            int[] invisible_columns = {0, 5, 10, 11, 14, 15, 16};
            int[] column_widths = {0, 50, 80, 60, 60, 0, 120, 80, 80, 100, 0, 0, 80, 100, 0, 0, 0};

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

        } catch (Exception ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
        }

        my_panel.repaint();
        my_panel.revalidate();

    }

    @Override
    public void open_filters() {

        if (!mn.check_version()) {

            return;

        }

        boolean check_null = false;

        if (city_indexes == null || janela_indexes == null || type_indexes == null) {

            check_null = true;

        }

        //filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date, check_null, true, true);
        filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date_query, check_null, true, true);

    }

}
