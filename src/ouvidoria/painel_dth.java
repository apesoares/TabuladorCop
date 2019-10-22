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
public class painel_dth extends painel {

    JLabel current_label;
    JLabel current_label_passado;

    public painel_dth(Menu mn, String cop) {

        super(mn, cop, 0);

        /*
        label_encaixe.setVisible(false);
        label_qtd_encaixe.setVisible(false);
        label_passado.setVisible(false);
        label_qtd_passado.setVisible(false);
        */
        pnl_info_passado.setVisible(false);
        pnl_info_encaixe.setVisible(false);

    }

    @Override
   public void atualiza_painel() {

        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();

        if (!mn.check_version()) {

            return;

        }

        String part_encerr = "";

        if (jCheckBox1.isSelected()) {

            part_encerr = "";

        } else {

            part_encerr = "painel.status not in ('Encerrado','Encerrado - Os Duplicada/Cancelada') and";

        }

        String query = "SELECT id,  \n"
                + "ifnull(painel.contrato,'') as Contrato, \n"
                + "ifnull(painel.cidade,'') as MR, \n"
                + "ifnull(DATE_FORMAT(painel.data_acomp,'%d/%m/%Y'),'') as Data, \n"
                + "ifnull(painel.janela,'') as Janela, \n"
                + "ifnull(painel.tipo_os,'') as 'Tipo OS', \n"
                + "ifnull(painel.nome_cli,'') as 'Nome cliente', \n"
                + "ifnull(painel.epo,'') as Parceiro, \n"
                + "ifnull(painel.login_tec,'') as 'Login Tecnico', \n"
                + "ifnull(painel.status,'') as Status, \n"
                + "ifnull(painel.status_monitoria,'') as status_monitoria, \n"
                + "ifnull(painel.canal,'') as Canal, \n"
                + "ifnull(painel.user_mark,'') as 'Usuário Tratando', \n"
                + "ifnull(painel.obs_horario,'') as partic, \n"
                + "ifnull(painel.tecnico,'') as Técnico, \n"
                + "painel.telefone_tec, "
                + "if(ac.status regexp ('Pendente'), 'Pendente', 'OS Executada') as 'Status NET' "
                + "FROM ouvidoria.painel \n"
                + "LEFT JOIN (SELECT \n"
                + "    `painel`.`contrato`,\n"
                + "    group_concat(`painel`.`num_os` separator '-'),    \n"
                + "	if(act.codigo_os is null,'Encerrado', 'Pendente') as status_activia,\n"
                + "    group_concat(if(act.codigo_os is null,'Encerrado', 'Pendente') separator '-') as status\n"
                + "FROM `ouvidoria`.`painel`\n"
                + "LEFT JOIN "
                + "(SELECT codigo_os, status_os FROM activia_dth "
                + "WHERE status_os <> 'CONCLUIDA') act "
                + "ON painel.num_os = act.codigo_os\n"
                + "WHERE painel.cop = 'COP DTH' "
                + "GROUP BY painel.contrato) ac "
                + "ON painel.contrato = ac.contrato "
                + "WHERE (" + part_encerr + " painel.status <> 'Encaixe') \n"
                + "and painel.cidade IN (" + filter_city_query + ") \n"
                + "and painel.grupo_os IN (" + filter_type_query + ") and \n"
                + "painel.janela IN (" + filter_janela_query + ") and \n"
                //+ "painel.data_acomp = '" + global.get_simple_date(filter_date) + "' \n"
                + "painel.data_acomp in (" + filter_date_query + ") \n"
                + "and painel.tipo_trat = 'Acompanhamento' \n"
                + "and painel.os_principal = 'Sim' \n"
                + "ORDER BY painel.cidade, painel.janela ";

        //System.out.println(query);

        try {
            JTable tab = global.getTable(query, jPanel1);

            main_tab = tab;

            int[] invisible_columns = {0, 8, 10, 13, 14, 15, 16};
            int[] column_widths = {0, 50, 80, 60, 60, 150, 90, 100, 0, 100, 0, 60, 80, 0, 0, 0, 0};

            global.hide_columns(invisible_columns, main_tab);
            global.adjust_columns(column_widths, main_tab);

            main_tab.addMouseListener(new painel_right_click());

            for (int columnTable = 1; columnTable < main_tab.getColumnCount(); columnTable++) {

                main_tab.getColumnModel().getColumn(columnTable).setCellRenderer(new CustomRenderer());

            }

            TableFilterHeader filter = new TableFilterHeader(main_tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);

            main_tab.addKeyListener(new ClipboardKeyAdapter(main_tab));

            label_qtd_casos.setText(String.valueOf(main_tab.getRowCount()));

        } catch (Exception ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
        }

        jPanel1.repaint();
        jPanel1.revalidate();

        //atualiza_encaixe();
    }

}
