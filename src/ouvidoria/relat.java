/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author N0026925
 */
public class relat extends javax.swing.JPanel {

    Menu mn;
    JTable main_tab;

    /**
     * Creates new form relat
     */
    public relat(Menu mn) {
        initComponents();

        this.mn = mn;

        dt_ini.setDate(new Date());
        dt_fim.setDate(new Date());

        cop_rec.addItem("Todos");
        cop_rec.addItem("COP NET");
        cop_rec.addItem("COP DTH");

        cop_rec.addItemListener(new cop_listener());

        global.fill_combo(relatorio, "SELECT nome_relat "
                + "FROM relatorios "
                + "ORDER BY nome_relat", true);

        charge_combos();

    }

    public void charge_combos() {

        ItemListener[] il = cidade.getItemListeners();

        for (ItemListener i : il) {

            cidade.removeItemListener(i);

        }

        cidade.removeAllItems();
        grp_os.removeAllItems();
        parceira.removeAllItems();

        String cop_selected = (String) cop_rec.getSelectedItem();

        if (cop_selected.equals("Todos")) {

            cop_selected = " is not null";

        } else {

            cop_selected = " = '" + cop_selected + "'";

        }

        cidade.addItem("Todas");

        global.fill_combo(cidade, "SELECT ci_depara FROM "
                + "cidades_novo "
                + "WHERE cop " + cop_selected + " "
                + "ORDER BY ci_depara", false);

        grp_os.addItem("TODOS");

        global.fill_combo(grp_os, "SELECT grp_descricao "
                + "FROM tipo_os_novo "
                + "WHERE grp_descricao is not null "
                + "GROUP BY grp_descricao "
                + "ORDER BY grp_descricao", false);

        parceira.addItem("Todas");
        parceira.addItem("NET");

        //global.fill_combo(parceira, "SELECT parceira FROM parceiras_view "
        //global.fill_combo(parceira, "SELECT parceira FROM parceiras_view_novo "
        global.fill_combo(parceira, "SELECT parceira FROM parceiras_view_novo_002 "
                + "WHERE cop " + cop_selected + " "
                + "and parceira is not null "
                + "GROUP BY parceira "
                + "ORDER BY parceira", false);

        cidade.addItemListener(new cidade_listener());

    }

    public class cidade_listener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {

                String cop_selected = (String) cop_rec.getSelectedItem();
                
                if (cop_selected.equals("Todos")) {

                    cop_selected = " is not null ";

                } else {

                    cop_selected = " = '" + cop_selected + "' ";

                }

                parceira.removeAllItems();

                parceira.addItem("Todas");
                parceira.addItem("NET");

                global.fill_combo(parceira, "SELECT parceira "
                        //+ "FROM parceiras_view_novo "
                        + "FROM parceiras_view_novo_002 "
                        + "WHERE cop " + cop_selected + " "
                        + "and cidade = '" + cidade.getSelectedItem() + "'"
                        + "and parceira is not null "
                        + "GROUP BY parceira "
                        + "ORDER BY parceira", false);

            }

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        relatorio = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cidade = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        dt_ini = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        dt_fim = new org.jdesktop.swingx.JXDatePicker();
        jLabel5 = new javax.swing.JLabel();
        grp_os = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        parceira = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        cop_rec = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        painel_tabela = new javax.swing.JPanel();
        painel_grafico = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(0));

        jLabel1.setText("Selecione o relatório");

        jLabel2.setText("Selecione a cidade");

        jLabel3.setText("Data Inicial");

        jLabel4.setText("Data Final");

        jLabel5.setText("Selecione o grupo de OS");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/magnifier.png"))); // NOI18N
        jButton1.setText("Mostrar dados");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Selecione a parceira");

        jLabel7.setText("Selecione o Segmento");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(cop_rec, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dt_ini, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dt_fim, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(grp_os, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(parceira, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addComponent(relatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cidade, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cidade, dt_fim, dt_ini, grp_os, parceira, relatorio});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cop_rec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(relatorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parceira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_ini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dt_fim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grp_os, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(413, Short.MAX_VALUE))
        );

        add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        jPanel4.setMaximumSize(new java.awt.Dimension(500, 500));
        jPanel4.setMinimumSize(new java.awt.Dimension(500, 500));
        jPanel4.setPreferredSize(new java.awt.Dimension(500, 500));
        jPanel4.setLayout(new java.awt.BorderLayout());

        painel_tabela.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        painel_tabela.setLayout(new java.awt.BorderLayout());
        jPanel4.add(painel_tabela, java.awt.BorderLayout.CENTER);

        painel_grafico.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        painel_grafico.setPreferredSize(new java.awt.Dimension(846, 450));
        painel_grafico.setLayout(new java.awt.BorderLayout());
        jPanel4.add(painel_grafico, java.awt.BorderLayout.PAGE_START);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (relatorio.getSelectedItem().equals("")) {
            global.show_error_message("É necessário selecionar um relatório!");
            relatorio.requestFocus();
            return;
        }

        painel_grafico.removeAll();
        painel_tabela.removeAll();

        try {
            Connection conn = Db_class.mysql_conn();

            String selected_relat = (String) relatorio.getSelectedItem();

            String query = "SELECT ifnull(`relatorios`.`query_base_chart`,''),\n"
                    + "     ifnull(`relatorios`.`where_base_chart`,''),\n"
                    + "     ifnull(`relatorios`.`group_clause_chart`,''),\n"
                    + "     ifnull(`relatorios`.`order_clause_chart`,''),\n"
                    + "     ifnull(`relatorios`.`query_base_table`,''),\n"
                    + "     ifnull(`relatorios`.`where_base_table`,''),\n"
                    + "     ifnull(`relatorios`.`group_clause_table`,''),\n"
                    + "     ifnull(`relatorios`.`order_clause_table`,''),"
                    + "     ifnull(add_click,'Não'),"
                    + "     ifnull(invisible_ids,''), "
                    + "     ifnull(columns_widths,''), "
                    + "     ifnull(data_base,''), "
                    + "     ifnull(tabela_base,'') "
                    + "FROM `ouvidoria`.`relatorios` "
                    + "WHERE nome_relat = '" + selected_relat + "'";

            ResultSet rs = Db_class.mysql_result(conn, query);

            rs.next();

            String base_chart = rs.getString(1);
            String where_chart = rs.getString(2);
            String group_chart = rs.getString(3);
            String order_chart = rs.getString(4);

            String base_table = rs.getString(5);
            String where_table = rs.getString(6);
            String group_table = rs.getString(7);
            String order_table = rs.getString(8);

            String add_click = rs.getString(9);

            String invisible_ids = rs.getString(10);
            String columns_widths = rs.getString(11);

            String data_base = rs.getString(12);
            String tabela_base = rs.getString(13);

            String data_ini = global.get_simple_date(dt_ini.getDate());
            String data_final = global.get_simple_date(dt_fim.getDate());

            String grupo_selected = (String) grp_os.getSelectedItem();
            String cidade_selected = (String) cidade.getSelectedItem();
            String parceira_selected = (String) parceira.getSelectedItem();

            if (grupo_selected.equals("TODOS")) {
                grupo_selected = "is not null";
            } else {
                grupo_selected = "= '" + grupo_selected + "'";
            }

            if (cidade_selected.equals("Todas")) {
                cidade_selected = "is not null";
            } else {
                cidade_selected = "= '" + cidade_selected + "'";
            }

            if (parceira_selected.equals("Todas")) {
                parceira_selected = "";
            } else {
                parceira_selected = "and parceira = '" + parceira_selected + "'";
            }

            String cop_selected = (String) cop_rec.getSelectedItem();

            if (cop_selected.equals("Todos")) {
                cop_selected = " is not null";
            } else {
                cop_selected = " = '" + cop_selected + "'";
            }

            String where_clause_chart = "";
            String where_clause_base = "";

            if (where_chart.equals("")) {
                where_clause_chart = "WHERE ";
            } else {
                where_clause_chart = where_chart + " and ";
            }

            if (where_table.equals("")) {
                where_clause_base = "WHERE ";
            } else {
                where_clause_base = where_table + " and ";
            }

            if (data_base.equals("date_time_insert")) {
                data_ini = global.get_simple_date(dt_ini.getDate()) + " 00:00:00";
                data_final = global.get_simple_date(dt_fim.getDate()) + " 23:59:59";
            }

            String select_str_chart = "";

            if (tabela_base.equals("painel")) {
                select_str_chart = base_chart + " "
                        + "" + where_clause_chart + " " + data_base + " BETWEEN '" + data_ini + "' and '" + data_final + "' and "
                        + "grupo_os " + grupo_selected + " and cidade " + cidade_selected + " "
                        + parceira_selected + " "
                        + "and cop " + cop_selected + " "
                        + group_chart + " " + order_chart;
            } else {
                select_str_chart = base_chart + " "
                        + "" + where_clause_chart + " " + data_base + " BETWEEN '" + data_ini + "' and '" + data_final + "' and "
                        + " cop = " + cop_selected + " "
                        + group_chart + " " + order_chart;
            }

            //System.out.println(select_str_chart);

            ResultSet rs_chart = Db_class.mysql_result(conn, select_str_chart);

            ResultSetMetaData meta = rs_chart.getMetaData();

            int cont_colunas = meta.getColumnCount();

            String xtitle = "";
            String ytitle = "";
            String ytitle2 = "";

            String serie1 = "";
            String serie2 = "";

            if (cont_colunas == 3) {
                xtitle = meta.getColumnName(1);
                ytitle = meta.getColumnName(2);
                ytitle2 = meta.getColumnName(3);

                serie1 = meta.getColumnName(2);
                serie2 = meta.getColumnName(3);

                chart_class cc = new chart_class();

                CategoryDataset[] datasets = cc.get_data_sets(rs_chart, serie1, serie2);

                final JFreeChart chart = cc.create_dual_axis_chart(datasets, selected_relat, xtitle, ytitle, ytitle2);
                final ChartPanel chartPanel = new ChartPanel(chart);

                painel_grafico.add(chartPanel);

                painel_grafico.repaint();
                painel_grafico.revalidate();
            } else {
                xtitle = meta.getColumnName(1);
                ytitle = meta.getColumnName(2);

                serie1 = meta.getColumnName(1);
                serie2 = meta.getColumnName(2);

                chart_class cc = new chart_class();

                CategoryDataset dataset = cc.create_dual_Dataset(rs_chart, serie1, serie2);

                final JFreeChart chart = cc.createChart(dataset, selected_relat, xtitle, ytitle);
                final ChartPanel chartPanel = new ChartPanel(chart);

                painel_grafico.add(chartPanel);

                painel_grafico.repaint();
                painel_grafico.revalidate();
            }

            String str_select_table = "";

            if (tabela_base.equals("painel")) {
                str_select_table = base_table + " "
                        + "" + where_clause_base + " " + data_base + " BETWEEN '" + data_ini + "' and '" + data_final + "' and "
                        + "grupo_os " + grupo_selected + " and cidade " + cidade_selected + " "
                        + parceira_selected + " "
                        + "and cop " + cop_selected + " "
                        + group_table + " " + order_table;
            } else {
                str_select_table = base_table + " "
                        + "" + where_clause_base + " " + data_base + " BETWEEN '" + data_ini + "' and '" + data_final + "' and "
                        + " cop " + cop_selected + " "
                        + group_table + " " + order_table;
            }

            //System.out.println(str_select_table);

            JTable tab = global.getTable(str_select_table, painel_tabela);

            if (relatorio.getSelectedItem().equals("Volume de Casos - Geral")
                    || relatorio.getSelectedItem().equals("Volume de Casos - Geral N2")
                    || relatorio.getSelectedItem().equals("Volume de Casos - Geral N3")
                    || relatorio.getSelectedItem().equals("Volume de Tabulações - OMS")
                    ) {

                tab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tab.setSize(10000, 10000);

                for (int i = 0; i <= tab.getColumnCount() - 1; i++) {

                    tab.getColumnModel().getColumn(i).setPreferredWidth(100);

                }
            }

            tab.addKeyListener(new ClipboardKeyAdapter(tab));

            String[] invisible_splited = invisible_ids.split("/", -1);

            int[] final_invisible_ids = new int[invisible_splited.length];

            for (String str : invisible_splited) {

                if (!str.equals("")) {

                    final_invisible_ids[Arrays.asList(invisible_splited).indexOf(str)] = Integer.parseInt(str);

                }

            }

            if (!columns_widths.equals("")) {

                String[] widths_splited = columns_widths.split("/", -1);

                int[] final_columns_widths = new int[widths_splited.length];

                for (String str : widths_splited) {

                    if (!str.equals("")) {

                        final_columns_widths[Arrays.asList(widths_splited).indexOf(str)] = Integer.parseInt(str);

                    }

                }

                if (!invisible_ids.equals("")) {

                    global.hide_columns(final_invisible_ids, tab);

                }

                global.adjust_columns(final_columns_widths, tab);

            }

            TableFilterHeader filter = new TableFilterHeader(tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);

            if (add_click.equals("Sim")) {

                tab.addMouseListener(new tab_click());
                tab.setToolTipText("Clique com o botão direito em uma linha para ver detalhes do caso!");

            }

            main_tab = tab;

            painel_tabela.repaint();
            painel_tabela.revalidate();

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(relat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(relat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    public class cop_listener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if (e.getStateChange() == ItemEvent.SELECTED) {

                charge_combos();

            }

        }

    }

    public class tab_click extends MouseAdapter {

        public void mouseClicked(MouseEvent evt) {

            if (SwingUtilities.isRightMouseButton(evt)) {

                int selected_row = main_tab.rowAtPoint(evt.getPoint());

                int id = (int) main_tab.getValueAt(selected_row, 0);

                String cop = (String) main_tab.getValueAt(selected_row, (main_tab.getColumnCount() - 1));

                tratar tr = new tratar(mn, true, id, cop, -1);

            }

        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cidade;
    private javax.swing.JComboBox cop_rec;
    private org.jdesktop.swingx.JXDatePicker dt_fim;
    private org.jdesktop.swingx.JXDatePicker dt_ini;
    private javax.swing.JComboBox grp_os;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel painel_grafico;
    private javax.swing.JPanel painel_tabela;
    private javax.swing.JComboBox parceira;
    private javax.swing.JComboBox relatorio;
    // End of variables declaration//GEN-END:variables
}
