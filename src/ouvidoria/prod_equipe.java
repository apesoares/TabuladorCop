/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Cursor;
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
public class prod_equipe extends javax.swing.JPanel {

    JTable tab;
    Menu mn;
    boolean coord = false;

    /**
     * Creates new form prod_indiv
     */
    public prod_equipe(Menu mn) {
        initComponents();

        this.mn = mn;

        jXDatePicker1.setDate(new Date());
        jXDatePicker2.setDate(new Date());

        //atualiza_tab();
    }

    public prod_equipe(Menu mn, boolean coord) {
        initComponents();

        this.mn = mn;
        this.coord = coord;

        jXDatePicker1.setDate(new Date());
        jXDatePicker2.setDate(new Date());

        //atualiza_tab();
    }

    public void atualiza_tab() {

        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);

        painel_tabela.removeAll();
        painel_grafico.removeAll();

        String where_clause = "";
        String turno_clause = "";
        String funcionario_clause = "";

        if (turno.getSelectedItem().equals("Manhã")) {
            turno_clause = "'Manhã'";
        } else if (turno.getSelectedItem().equals("Tarde")) {
            turno_clause = "'Tarde'";
        } else if (turno.getSelectedItem().equals("Noite")) {
            turno_clause = "'Noite'";
        } else if (turno.getSelectedItem().equals("Madrugada")) {
            turno_clause = "'Madrugada'";
        } else {
            turno_clause = "'Manhã','Tarde','Noite', 'Madrugada'";
        }

        funcionario_clause = cb_funcionario.getSelectedItem().toString();

        where_clause = "WHERE "
                + "ação " + ((oms_button.isSelected()) ? "=" : "!=") + " 'Inserção OMS' "
                + "and turno IN (" + turno_clause + ")"
                + "and date_time_acao BETWEEN '" + global.get_simple_date(jXDatePicker1.getDate()) + " 00:00:00' "
                + "and '" + global.get_simple_date(jXDatePicker2.getDate()) + " 23:59:59' ";

        /*
         if (oms_button.isSelected()) {
         where_clause = "WHERE date_time_acao BETWEEN '" + global.get_simple_date(jXDatePicker1.getDate()) + " 00:00:00' "
         + "and '" + global.get_simple_date(jXDatePicker2.getDate()) + " 23:59:59' "
         + "and ação = 'Inserção OMS' "
         + "and turno IN (" + turno_clause + ")";
         } else {
         where_clause = "WHERE date_time_acao BETWEEN '" + global.get_simple_date(jXDatePicker1.getDate()) + " 00:00:00' "
         + "and '" + global.get_simple_date(jXDatePicker2.getDate()) + " 23:59:59' "
         + "and ação != 'Inserção OMS' "
         + "and turno IN (" + turno_clause + ")";;
         }
         */

        //String order_clause = " ORDER BY date_time_acao";
        //String query = "SELECT * FROM prod_view " + where_clause + order_clause + " DESC";
        //String query_count = "SELECT count(*) FROM prod_view " + where_clause + order_clause;
        String query = PlanilhaSupervisor(where_clause);
        String query_count = "SELECT count(*) FROM prod_view " + where_clause;

        try {
            Connection conn = Db_class.mysql_conn();

            ResultSet rs = Db_class.mysql_result(conn, query_count);

            rs.next();

            int contagem = rs.getInt(1);

            label_contagem.setText("" + contagem);

            String query_chart = "";

            if ("Representante".equals(funcionario_clause)) {
                //consulta tradicional
                query_chart = "SELECT Nome_Usuario, count(*) FROM prod_view " + where_clause + " "
                        + "GROUP BY Nome_Usuario ORDER BY Nome_Usuario ASC";
            } else {
                query_chart = GraficoSupervisor(where_clause);
            }

            ResultSet rs_chart = Db_class.mysql_result(conn, query_chart);

            try {
                JTable tab = global.getTable(query, painel_tabela);

                this.tab = tab;

                tab.addMouseListener(new tab_click());

                tab.addKeyListener(new ClipboardKeyAdapter(tab));

                //int[] invisible_ids = {0, 11, 12, 16};
                //int[] column_width = {0, 50, 50, 60, 70, 80, 70, 70, 100, 80, 80, 0, 0, 0, 0, 0, 0};
                int[] invisible_ids = {1, 12, 13, 17};
                int[] column_width = {100, 0, 50, 50, 60, 70, 80, 70, 70, 50, 40, 80, 0, 0, 0, 0, 0, 0};

                global.hide_columns(invisible_ids, tab);
                global.adjust_columns(column_width, tab);

                TableFilterHeader filter = new TableFilterHeader(tab, AutoChoices.ENABLED);
                filter.setAdaptiveChoices(true);

            } catch (Exception ex) {
                Logger.getLogger(prod_equipe.class.getName()).log(Level.SEVERE, null, ex);
            }

            painel_tabela.repaint();
            painel_tabela.revalidate();

            chart_class cc = new chart_class();

            CategoryDataset dataset = cc.create_dual_Dataset(rs_chart, "Tipos de Ações", "");

            //final JFreeChart chart = cc.createChart(dataset, "Produtividade Equipe", "Usuários", "Qtde Ações");
            final JFreeChart chart = cc.createChart(dataset, "Produtividade Equipe", funcionario_clause, "Qtde Ações");
            final ChartPanel chartPanel = new ChartPanel(chart);

            painel_grafico.add(chartPanel);

            painel_grafico.repaint();
            painel_grafico.revalidate();

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(prod_equipe.class.getName()).log(Level.SEVERE, null, ex);
        }

        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(normalCursor);

    }

    public String GraficoSupervisor(String sCondicao) {
        String sSQL = "";
        sSQL = sSQL + "select nome_usuario, sum(qtd) as qtd \n";
        sSQL = sSQL + "from \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "select \n";
        sSQL = sSQL + "ifnull \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "select u.nome_supervisor from tb_sin_representantes u \n";
        sSQL = sSQL + "where u.nome = nucleo_fmt.nome_usuario \n";
        sSQL = sSQL + "and (u.data1 >= nucleo_fmt.dt_acao or ifnull(u.data1, 0) = 0) \n";
        sSQL = sSQL + "and u.data0_supervisor <= nucleo_fmt.dt_acao \n";
        sSQL = sSQL + "order by u.data0_supervisor desc \n";
        sSQL = sSQL + "limit 1 \n";
        sSQL = sSQL + ") \n";
        sSQL = sSQL + ", '<Não identificado>' \n";
        sSQL = sSQL + ") as nome_usuario, \n";
        sSQL = sSQL + "nucleo_fmt.qtd \n";
        sSQL = sSQL + "from \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "select fc_NormalizaString(Nome_Usuario) as Nome_Usuario, str_to_date(concat(dt_acao, ' 00:00:00'), '%Y-%m-%d %H:%i:%s') as dt_acao, qtd \n";
        sSQL = sSQL + "from \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "SELECT Nome_Usuario, date_format(date_time_acao, '%Y-%m-%d') as dt_acao, count(*) as qtd \n";
        sSQL = sSQL + "FROM prod_view \n";
        sSQL = sSQL + sCondicao + " \n";
        sSQL = sSQL + "GROUP BY Nome_Usuario, date_format(date_time_acao, '%Y-%m-%d') \n";
        sSQL = sSQL + "ORDER BY Nome_Usuario, date_format(date_time_acao, '%Y-%m-%d') ASC \n";
        sSQL = sSQL + ") nucleo \n";
        sSQL = sSQL + ") nucleo_fmt \n";
        sSQL = sSQL + ") final \n";
        sSQL = sSQL + "group by nome_usuario \n";
        sSQL = sSQL + "order by nome_usuario; \n";

        return sSQL;
    }

    public String PlanilhaSupervisor(String sCondicao) {
        String sSQL = "";
        sSQL = sSQL + "select \n";
        sSQL = sSQL + "ifnull \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "select u.nome_supervisor from tb_sin_representantes u \n";
        sSQL = sSQL + "where u.nome = nucleo.nome_usuario \n";
        sSQL = sSQL + "and (u.data1 >= date_format(nucleo.date_time_acao, '%Y-%m-%d') or ifnull(u.data1, 0) = 0) \n";
        sSQL = sSQL + "and u.data0_supervisor <= date_format(nucleo.date_time_acao, '%Y-%m-%d')\n";
        sSQL = sSQL + "order by u.data0_supervisor desc \n";
        sSQL = sSQL + "limit 1 \n";
        sSQL = sSQL + ") \n";
        sSQL = sSQL + ", '<Não identificado>' \n";
        sSQL = sSQL + ") as Supervisor, \n";
        sSQL = sSQL + "nucleo.* \n";
        sSQL = sSQL + "from \n";
        sSQL = sSQL + "( \n";
        sSQL = sSQL + "SELECT \n";
        sSQL = sSQL + "id_caso, cidade, contrato, data_acomp, janela, epo, técnico, `grupo os`, ação, `tipo trat`, \n";
        sSQL = sSQL + "ativo, `descr indevido`, data_hora, date_time_acao, \n";
        sSQL = sSQL + "fc_NormalizaString(nome_usuario) as nome_usuario, \n";
        sSQL = sSQL + "cop, turno \n";
        sSQL = sSQL + "FROM prod_view \n";
        sSQL = sSQL + sCondicao + " \n";
        sSQL = sSQL + ") nucleo \n";
        sSQL = sSQL + "order by nucleo.date_time_acao; \n";

        return sSQL;
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
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        label_contagem = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        oms_button = new javax.swing.JRadioButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        dpt_button = new javax.swing.JRadioButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cb_funcionario = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        painel_grafico = new javax.swing.JPanel();
        painel_tabela = new javax.swing.JPanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel1.setText("Data inicio   ");
        jToolBar1.add(jLabel1);

        jXDatePicker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jXDatePicker1);
        jToolBar1.add(jSeparator1);

        jLabel2.setText("Data fim   ");
        jToolBar1.add(jLabel2);

        jXDatePicker2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jXDatePicker2);
        jToolBar1.add(jSeparator3);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_refresh_small.png"))); // NOI18N
        jButton1.setText("Atualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator6);

        jLabel3.setText("Total de ações ");
        jToolBar1.add(jLabel3);

        label_contagem.setBackground(new java.awt.Color(0, 0, 0));
        label_contagem.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_contagem.setForeground(new java.awt.Color(255, 255, 255));
        label_contagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_contagem.setText("   ");
        label_contagem.setToolTipText("Total de tratamentos envolvidos no relatório");
        label_contagem.setMaximumSize(new java.awt.Dimension(40, 25));
        label_contagem.setOpaque(true);
        label_contagem.setPreferredSize(new java.awt.Dimension(40, 25));
        jToolBar1.add(label_contagem);
        jToolBar1.add(jSeparator2);

        buttonGroup1.add(oms_button);
        oms_button.setSelected(true);
        oms_button.setText("OMS");
        oms_button.setToolTipText("DTH");
        oms_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oms_buttonActionPerformed(evt);
            }
        });
        jToolBar1.add(oms_button);
        oms_button.getAccessibleContext().setAccessibleDescription("");

        jToolBar1.add(jSeparator4);

        buttonGroup1.add(dpt_button);
        dpt_button.setText("Dispatcher");
        dpt_button.setToolTipText("Cabo");
        dpt_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpt_buttonActionPerformed(evt);
            }
        });
        jToolBar1.add(dpt_button);
        dpt_button.getAccessibleContext().setAccessibleDescription("");

        jToolBar1.add(jSeparator5);

        jLabel4.setText("Turno ");
        jToolBar1.add(jLabel4);

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Manhã", "Tarde", "Noite", "Madrugada" }));
        turno.setMaximumSize(new java.awt.Dimension(200, 32767));
        turno.setPreferredSize(new java.awt.Dimension(130, 26));
        turno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                turnoItemStateChanged(evt);
            }
        });
        turno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turnoActionPerformed(evt);
            }
        });
        jToolBar1.add(turno);

        jLabel5.setText(" Funcionário ");
        jToolBar1.add(jLabel5);

        cb_funcionario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Representante", "Supervisor" }));
        cb_funcionario.setToolTipText("Selecione quem deverá aparecer no gráfico");
        cb_funcionario.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_funcionarioItemStateChanged(evt);
            }
        });
        jToolBar1.add(cb_funcionario);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new java.awt.BorderLayout());

        painel_grafico.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        painel_grafico.setPreferredSize(new java.awt.Dimension(300, 400));
        painel_grafico.setLayout(new java.awt.BorderLayout());
        jPanel2.add(painel_grafico, java.awt.BorderLayout.NORTH);

        painel_tabela.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        painel_tabela.setPreferredSize(new java.awt.Dimension(4, 450));
        painel_tabela.setRequestFocusEnabled(false);

        javax.swing.GroupLayout painel_tabelaLayout = new javax.swing.GroupLayout(painel_tabela);
        painel_tabela.setLayout(painel_tabelaLayout);
        painel_tabelaLayout.setHorizontalGroup(
            painel_tabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 872, Short.MAX_VALUE)
        );
        painel_tabelaLayout.setVerticalGroup(
            painel_tabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
        );

        jPanel2.add(painel_tabela, java.awt.BorderLayout.CENTER);

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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jXDatePicker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker1ActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_jXDatePicker1ActionPerformed

    private void jXDatePicker2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker2ActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_jXDatePicker2ActionPerformed

    private void oms_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oms_buttonActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_oms_buttonActionPerformed

    private void dpt_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpt_buttonActionPerformed

        atualiza_tab();

    }//GEN-LAST:event_dpt_buttonActionPerformed

    private void turnoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_turnoItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            atualiza_tab();
        }

    }//GEN-LAST:event_turnoItemStateChanged

    private void turnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_turnoActionPerformed

    private void cb_funcionarioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_funcionarioItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            atualiza_tab();
        }
    }//GEN-LAST:event_cb_funcionarioItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cb_funcionario;
    private javax.swing.JRadioButton dpt_button;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar jToolBar1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    private javax.swing.JLabel label_contagem;
    private javax.swing.JRadioButton oms_button;
    private javax.swing.JPanel painel_grafico;
    private javax.swing.JPanel painel_tabela;
    private javax.swing.JComboBox turno;
    // End of variables declaration//GEN-END:variables
}
