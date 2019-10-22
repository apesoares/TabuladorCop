/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class prod_indiv extends javax.swing.JPanel {

    JTable tab;
    Menu mn;
    boolean coord = false;
    String user_str;

    /**
     * Creates new form prod_indiv
     */
    public prod_indiv(Menu mn) {
        initComponents();

        this.mn = mn;

        jXDatePicker1.setDate(new Date());
        jXDatePicker2.setDate(new Date());

        usuario.setVisible(false);
        label_usuario.setVisible(false);
        turno.setVisible(false);
        label_turno.setVisible(false);

        atualiza_tab();

    }

    public prod_indiv(Menu mn, boolean coord) {
        initComponents();

        this.mn = mn;
        this.coord = coord;

        jXDatePicker1.setDate(new Date());
        jXDatePicker2.setDate(new Date());

        usuario.setVisible(true);
        label_usuario.setVisible(true);
        turno.setVisible(true);
        label_turno.setVisible(true);

        global.fill_combo(usuario, "SELECT nome FROM users "
                + "WHERE cop = '" + mn.get_cop() + "' ORDER BY nome", true);

        usuario.addItemListener(new user_change());

        atualiza_tab();

    }

    public void atualiza_tab() {

        painel_tabela.removeAll();
        painel_grafico.removeAll();

        if (coord) {

            user_str = (String) usuario.getSelectedItem();

        } else {

            user_str = mn.get_nome();

        }

        String where_clause = "WHERE nome_usuario = '" + user_str + "' "
                + "and date_time_acao BETWEEN '" + global.get_simple_date(jXDatePicker1.getDate()) + " 00:00:00' "
                + "and '" + global.get_simple_date(jXDatePicker2.getDate()) + " 23:59:59'";

        String order_clause = " ORDER BY date_time_acao";

        String query = "SELECT * FROM prod_view " + where_clause + order_clause + " DESC";

        String query_count = "SELECT count(*) FROM prod_view " + where_clause + order_clause;

        try {
            Connection conn = Db_class.mysql_conn();

            ResultSet rs = Db_class.mysql_result(conn, query_count);

            rs.next();

            int contagem = rs.getInt(1);

            label_contagem.setText("" + contagem);

            String query_chart = "";

            if (por_tipo.isSelected()) {

                query_chart = "SELECT Ação, count(*) FROM prod_view " + where_clause + " "
                        + "GROUP BY Ação " + order_clause + " ASC";

            } else {

                query_chart = "SELECT DATE_FORMAT(Date(date_time_acao),'%d/%m/%Y'), count(*) FROM prod_view " + where_clause + " "
                        + "GROUP BY DATE_FORMAT(Date(date_time_acao),'%d/%m/%Y') " + order_clause + " ASC";

            }

            ResultSet rs_chart = Db_class.mysql_result(conn, query_chart);
            
            System.out.println(query);
            
            try {
                JTable tabela = global.getTable(query, painel_tabela);

                this.tab = tabela;

                tab.addMouseListener(new tab_click());

                tab.addKeyListener(new ClipboardKeyAdapter(tab));

                int[] invisible_ids = {0, 13, 14, 15, 16};
                int[] column_width = {0, 50, 50, 60, 70, 80, 70, 70, 100, 80, 80, 80, 80, 0, 0, 0, 0};

                global.hide_columns(invisible_ids, tab);
                global.adjust_columns(column_width, tab);

                TableFilterHeader filter = new TableFilterHeader(this.tab, AutoChoices.ENABLED);
                filter.setAdaptiveChoices(true);

            } catch (Exception ex) {
                Logger.getLogger(prod_indiv.class.getName()).log(Level.SEVERE, null, ex);
            }

            painel_tabela.repaint();
            painel_tabela.revalidate();

            chart_class cc = new chart_class();

            CategoryDataset dataset = cc.create_dual_Dataset(rs_chart, "Tipos de Ações", "");

            final JFreeChart chart = cc.createChart(dataset, "Produtividade - " + user_str, "Ações", "Qtde Ações");
            final ChartPanel chartPanel = new ChartPanel(chart);

            painel_grafico.add(chartPanel);

            painel_grafico.repaint();
            painel_grafico.revalidate();
            
            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(prod_indiv.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public class tab_click extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e)) {

                int row = tab.rowAtPoint(e.getPoint());

                tab.setRowSelectionInterval(row, row);

                int id = (int) tab.getValueAt(row, 0);
                String cop = (String) tab.getValueAt(row, 16);

                tratar tr = new tratar(mn, true, id, cop, -1);

            }

        }

    }

    public class user_change implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {

                atualiza_tab();

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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        panel_tool = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        label_turno = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        label_usuario = new javax.swing.JLabel();
        usuario = new javax.swing.JComboBox();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jToolBar3 = new javax.swing.JToolBar();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        por_tipo = new javax.swing.JRadioButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        por_data = new javax.swing.JRadioButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        label_contagem = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        painel_tabela = new javax.swing.JPanel();
        painel_grafico = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.BorderLayout());

        panel_tool.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panel_tool.setLayout(new java.awt.BorderLayout());

        jToolBar2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar2.setRollover(true);
        jToolBar2.add(jSeparator9);

        label_turno.setText("Turno   ");
        jToolBar2.add(label_turno);

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Manhã", "Tarde" }));
        turno.setMaximumSize(new java.awt.Dimension(200, 25));
        turno.setMinimumSize(new java.awt.Dimension(58, 25));
        turno.setPreferredSize(new java.awt.Dimension(200, 25));
        turno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                turnoItemStateChanged(evt);
            }
        });
        jToolBar2.add(turno);
        jToolBar2.add(jSeparator5);

        label_usuario.setText("Usuário   ");
        jToolBar2.add(label_usuario);

        usuario.setLightWeightPopupEnabled(false);
        usuario.setMaximumSize(new java.awt.Dimension(250, 25));
        usuario.setMinimumSize(new java.awt.Dimension(100, 22));
        usuario.setPreferredSize(new java.awt.Dimension(250, 25));
        jToolBar2.add(usuario);
        jToolBar2.add(jSeparator7);

        panel_tool.add(jToolBar2, java.awt.BorderLayout.PAGE_END);

        jToolBar3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar3.setFloatable(false);
        jToolBar3.setMaximumSize(new java.awt.Dimension(735, 32771));
        jToolBar3.setMinimumSize(new java.awt.Dimension(738, 31));
        jToolBar3.add(jSeparator8);

        jLabel1.setText("Data inicio   ");
        jToolBar3.add(jLabel1);

        jXDatePicker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker1ActionPerformed(evt);
            }
        });
        jToolBar3.add(jXDatePicker1);
        jToolBar3.add(jSeparator1);

        jLabel2.setText("Data fim   ");
        jToolBar3.add(jLabel2);

        jXDatePicker2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker2ActionPerformed(evt);
            }
        });
        jToolBar3.add(jXDatePicker2);
        jToolBar3.add(jSeparator3);

        buttonGroup1.add(por_tipo);
        por_tipo.setSelected(true);
        por_tipo.setText("Por tipo");
        por_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                por_tipoActionPerformed(evt);
            }
        });
        jToolBar3.add(por_tipo);
        jToolBar3.add(jSeparator2);

        buttonGroup1.add(por_data);
        por_data.setText("Por data");
        por_data.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                por_dataActionPerformed(evt);
            }
        });
        jToolBar3.add(por_data);
        jToolBar3.add(jSeparator4);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_refresh_small.png"))); // NOI18N
        jButton1.setText("Atualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton1);
        jToolBar3.add(jSeparator6);

        jLabel3.setText("Tratamentos no período   ");
        jToolBar3.add(jLabel3);

        label_contagem.setBackground(new java.awt.Color(0, 0, 0));
        label_contagem.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_contagem.setForeground(new java.awt.Color(255, 255, 255));
        label_contagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_contagem.setText("   ");
        label_contagem.setMaximumSize(new java.awt.Dimension(30, 25));
        label_contagem.setOpaque(true);
        label_contagem.setPreferredSize(new java.awt.Dimension(30, 25));
        jToolBar3.add(label_contagem);

        panel_tool.add(jToolBar3, java.awt.BorderLayout.CENTER);

        jPanel1.add(panel_tool, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new java.awt.BorderLayout());

        painel_tabela.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        painel_tabela.setPreferredSize(new java.awt.Dimension(4, 450));
        painel_tabela.setRequestFocusEnabled(false);

        javax.swing.GroupLayout painel_tabelaLayout = new javax.swing.GroupLayout(painel_tabela);
        painel_tabela.setLayout(painel_tabelaLayout);
        painel_tabelaLayout.setHorizontalGroup(
            painel_tabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 782, Short.MAX_VALUE)
        );
        painel_tabelaLayout.setVerticalGroup(
            painel_tabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 275, Short.MAX_VALUE)
        );

        jPanel2.add(painel_tabela, java.awt.BorderLayout.CENTER);

        painel_grafico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        painel_grafico.setPreferredSize(new java.awt.Dimension(4, 400));
        painel_grafico.setLayout(new java.awt.BorderLayout());
        jPanel2.add(painel_grafico, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void por_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_por_tipoActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_por_tipoActionPerformed

    private void por_dataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_por_dataActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_por_dataActionPerformed

    private void jXDatePicker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker1ActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_jXDatePicker1ActionPerformed

    private void jXDatePicker2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker2ActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_jXDatePicker2ActionPerformed

    private void turnoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_turnoItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED) {

            try {
                Connection conn = Db_class.mysql_conn();

                String query = "";
                String selected_turno = (String) turno.getSelectedItem();

                if (selected_turno.equals("Manhã") || selected_turno.equals("Tarde")) {

                    query = "SELECT nome FROM users "
                            + "WHERE turno = '" + selected_turno + "'"
                            + "and cop = '" + mn.get_cop() + "' "
                            + "ORDER BY nome";

                } else {

                    query = "SELECT nome FROM users "
                            + "WHERE cop = '" + mn.get_cop() + "' ORDER BY nome";

                }

                global.fill_combo(usuario, query, true);
                
                Db_class.close_conn(conn);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(prod_indiv.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_turnoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    private javax.swing.JLabel label_contagem;
    private javax.swing.JLabel label_turno;
    private javax.swing.JLabel label_usuario;
    private javax.swing.JPanel painel_grafico;
    private javax.swing.JPanel painel_tabela;
    private javax.swing.JPanel panel_tool;
    private javax.swing.JRadioButton por_data;
    private javax.swing.JRadioButton por_tipo;
    private javax.swing.JComboBox turno;
    private javax.swing.JComboBox usuario;
    // End of variables declaration//GEN-END:variables
}
