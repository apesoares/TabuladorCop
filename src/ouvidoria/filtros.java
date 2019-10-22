/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Position;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DaySelectionModel;

/**
 *
 * @author N0026925
 */
public class filtros extends javax.swing.JDialog {

    Menu mn;
    painel pn;

    /**
     * Creates new form filtros
     */
    public filtros(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /*
    public filtros(Menu mn, boolean modal, painel pn, int[] city_indexes,
            int[] janela_indexes, int[] tipo_indexes, Date data_r, boolean check,
            boolean is_visible, String cop) {
    */
    public filtros(Menu mn, boolean modal, painel pn, int[] city_indexes,
            int[] janela_indexes, int[] tipo_indexes, String data_r, boolean check,
            boolean is_visible, String cop) {

        super(mn, modal);
        initComponents();

        this.mn = mn;
        this.pn = pn;

        if (cop.equals("COP DTH")) {

            jTabbedPane2.setTitleAt(1, "MR");

        }

        global.fill_list_box(cidade, "SELECT ci_depara FROM cidades_novo  "
                + "WHERE cop = '" + cop + "' "
                + " ORDER BY ci_depara", true, false);

        global.fill_list_box(janela, "SELECT nome_janela FROM janelas  "
                + "WHERE cop = '" + cop + "' "
                + "ORDER BY nome_janela", true, false);

        global.fill_list_box(tipo_os, "SELECT grp_descricao FROM tipo_os_novo  "
                + "WHERE grp_descricao <> 'DESCO' "
                + "GROUP BY grp_descricao ORDER BY grp_descricao", true, false);

        if (!check) {

            cidade.setSelectedIndices(city_indexes);
            janela.setSelectedIndices(janela_indexes);
            tipo_os.setSelectedIndices(tipo_indexes);

        } else {

            List<Object> lista_ids = new ArrayList<Object>();

            try {
                Connection conn = Db_class.mysql_conn();

                String query = "SELECT ci_depara,"
                        + "ifnull(user_iat_manha,''), "
                        + "ifnull(user_iat_tarde,''), "
                        + "ifnull(user_epo_manha,''), "
                        + "ifnull(user_epo_tarde,'')"
                        + " FROM cidades_novo "
                        + "WHERE (user_iat_manha = '" + mn.get_nome() + "' OR "
                        + "user_iat_tarde = '" + mn.get_nome() + "' OR "
                        + "user_epo_manha = '" + mn.get_nome() + "' OR "
                        + "user_epo_tarde = '" + mn.get_nome() + "')"
                        + "ORDER BY ci_depara";

                ResultSet rs = Db_class.mysql_result(conn, query);

                List<Object> lista_tipo_os = new ArrayList<Object>();

                while (rs.next()) {

                    int id = cidade.getNextMatch(rs.getString(1), 0, Position.Bias.Forward);
                    lista_ids.add(id);

                    if (!rs.getString(2).equals("") || !rs.getString(3).equals("")) {

                        if (!lista_tipo_os.contains(1)) {
                            lista_tipo_os.add(1);
                        }

                    }
                    if (!rs.getString(4).equals("") || !rs.getString(5).equals("")) {

                        if (!lista_tipo_os.contains(0)) {

                            lista_tipo_os.add(0);
                            lista_tipo_os.add(2);

                        }

                    }

                    System.out.println(id + " - " + rs.getString(1));

                }

                if (lista_tipo_os.size() > 0) {

                    int[] array_ints_tipo = new int[lista_tipo_os.size()];

                    for (int i = 0; i <= lista_tipo_os.size() - 1; i++) {

                        array_ints_tipo[i] = (int) lista_tipo_os.get(i);

                    }

                    tipo_os.setSelectedIndices(array_ints_tipo);

                } else {

                    global.select_all_list(tipo_os);

                }

                if (lista_ids.size() > 0) {

                    int[] array_ints = new int[lista_ids.size()];

                    for (int i = 0; i <= lista_ids.size() - 1; i++) {

                        array_ints[i] = (int) lista_ids.get(i);

                    }

                    cidade.setSelectedIndices(array_ints);

                } else {

                    global.select_all_list(cidade);

                }

                Db_class.close_conn(conn);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(filtros.class.getName()).log(Level.SEVERE, null, ex);
            }

            //global.select_all_list(tipo_os);
            global.select_all_list(janela);

        }

        //data.setSelectionDate(data_r);
        String[] s_data_r = data_r.split(",");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //long[] flaggedDates = new long[s_data_r.length];
        Date[] flaggedDates = new Date[s_data_r.length];
        DaySelectionModel dt = new DaySelectionModel();
        dt.setSelectionMode(DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        
        for (int i = 0; i < s_data_r.length; i++) {
            try {
                //data.setSelectionDate(formatter.parse(s_data_r[i].replaceAll("'", "")));
                data.setSelectionInterval(formatter.parse(s_data_r[i].replaceAll("'", "")), formatter.parse(s_data_r[i].replaceAll("'", "")));
                flaggedDates[i] = formatter.parse(s_data_r[i].replaceAll("'", "")); //.getTime();
                dt.addSelectionInterval(formatter.parse(s_data_r[i].replaceAll("'", "")), formatter.parse(s_data_r[i].replaceAll("'", "")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        data.setFlaggedDates(flaggedDates);
        data.setSelectionModel(dt);

        global.initCloseListener(this);

        if (is_visible) {

            global.open_modal(this, "Filtros");

        }

    }

    /*
    public filtros(Menu mn, boolean modal, painel pn, int[] city_indexes,
            int[] janela_indexes, int[] tipo_indexes, Date data_r, boolean check,
            boolean is_visible, boolean is_bsod) {
    */

    public filtros(Menu mn, boolean modal, painel pn, int[] city_indexes,
            int[] janela_indexes, int[] tipo_indexes, String data_r, boolean check,
            boolean is_visible, boolean is_bsod) {

        super(mn, modal);
        initComponents();

        this.mn = mn;
        this.pn = pn;

        global.fill_list_box(cidade, "SELECT ci_depara FROM cidades_novo  "
                + "ORDER BY ci_depara", true, false);

        global.fill_list_box(janela, "SELECT nome_janela FROM janelas  "
                + "ORDER BY nome_janela", true, false);

        global.fill_list_box(tipo_os, "SELECT grp_descricao FROM tipo_os_novo  "
                + "WHERE grp_descricao <> 'DESCO' "
                + "GROUP BY grp_descricao ORDER BY grp_descricao", true, false);

        if (!check) {

            cidade.setSelectedIndices(city_indexes);
            janela.setSelectedIndices(janela_indexes);
            tipo_os.setSelectedIndices(tipo_indexes);

        } else {

            List<Object> lista_ids = new ArrayList<Object>();

            try {
                Connection conn = Db_class.mysql_conn();

                String query = "SELECT ci_depara,"
                        + "ifnull(user_iat_manha,''), "
                        + "ifnull(user_iat_tarde,''), "
                        + "ifnull(user_epo_manha,''), "
                        + "ifnull(user_epo_tarde,'')"
                        + " FROM cidades_novo "
                        + "WHERE (user_iat_manha = '" + mn.get_nome() + "' OR "
                        + "user_iat_tarde = '" + mn.get_nome() + "' OR "
                        + "user_epo_manha = '" + mn.get_nome() + "' OR "
                        + "user_epo_tarde = '" + mn.get_nome() + "')"
                        + "ORDER BY ci_depara";

                ResultSet rs = Db_class.mysql_result(conn, query);

                List<Object> lista_tipo_os = new ArrayList<Object>();

                while (rs.next()) {

                    int id = cidade.getNextMatch(rs.getString(1), 0, Position.Bias.Forward);
                    lista_ids.add(id);

                    if (!rs.getString(2).equals("") || !rs.getString(3).equals("")) {

                        if (!lista_tipo_os.contains(1)) {
                            lista_tipo_os.add(1);
                        }

                    }
                    if (!rs.getString(4).equals("") || !rs.getString(5).equals("")) {

                        if (!lista_tipo_os.contains(0)) {

                            lista_tipo_os.add(0);
                            lista_tipo_os.add(2);

                        }

                    }

                    System.out.println(id + " - " + rs.getString(1));

                }

                if (lista_tipo_os.size() > 0) {

                    int[] array_ints_tipo = new int[lista_tipo_os.size()];

                    for (int i = 0; i <= lista_tipo_os.size() - 1; i++) {

                        array_ints_tipo[i] = (int) lista_tipo_os.get(i);

                    }

                    tipo_os.setSelectedIndices(array_ints_tipo);

                } else {

                    global.select_all_list(tipo_os);

                }

                if (lista_ids.size() > 0) {

                    int[] array_ints = new int[lista_ids.size()];

                    for (int i = 0; i <= lista_ids.size() - 1; i++) {

                        array_ints[i] = (int) lista_ids.get(i);

                    }

                    cidade.setSelectedIndices(array_ints);

                } else {

                    global.select_all_list(cidade);

                }

                Db_class.close_conn(conn);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(filtros.class.getName()).log(Level.SEVERE, null, ex);
            }

            //global.select_all_list(tipo_os);
            global.select_all_list(janela);

        }

        //data.setSelectionDate(data_r);
        String[] s_data_r = data_r.split(",");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < s_data_r.length; i++) {
            try {
                //data.setSelectionDate(formatter.parse(s_data_r[i].replaceAll("'", "")));
                data.setSelectionInterval(formatter.parse(s_data_r[i].replaceAll("'", "")), formatter.parse(s_data_r[i].replaceAll("'", "")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        global.initCloseListener(this);

        if (is_visible) {

            global.open_modal(this, "Filtros");

        }

    }

    public void aplica_filtro() {

        //Date data_agenda = data.getSelectionDate();
        SortedSet<Date> dt_acomp_indices = data.getSelection();
        int[] city_indices = cidade.getSelectedIndices();
        int[] janela_indices = janela.getSelectedIndices();
        int[] type_indices = tipo_os.getSelectedIndices();

        //pn.set_filter_indexes(dt_acomp_indices, city_indices, type_indices, janela_indices, data_agenda);
        pn.set_filter_indexes(city_indices, type_indices, janela_indices, dt_acomp_indices);

        String query_dt_acomp = "";
        String query_city = "";
        String query_janela = "";
        String query_type = "";

        int count_dt_acomp = 0;
        int count_city = 0;
        int count_type = 0;
        int count_janela = 0;

        Iterator<Date> x = dt_acomp_indices.iterator();
        while (x.hasNext()) {
            if (count_dt_acomp == 0) {
                query_dt_acomp = "'" + global.get_simple_date(x.next()) + "'";
            } else {
                query_dt_acomp = query_dt_acomp + ", '" + global.get_simple_date(x.next()) + "'";
            }
            
            count_dt_acomp = count_dt_acomp + 1;
        }

        for (int indice : city_indices) {

            if (count_city == 0) {

                query_city = " '" + cidade.getModel().getElementAt(indice) + "'";
                count_city++;

            } else {

                query_city = query_city + ", '" + cidade.getModel().getElementAt(indice) + "'";
                count_city++;
            }

        }

        for (int indice : type_indices) {

            if (count_type == 0) {

                query_type = "'" + tipo_os.getModel().getElementAt(indice) + "'";
                count_type++;

            } else {

                query_type = query_type + ",'" + tipo_os.getModel().getElementAt(indice) + "'";
                count_type++;
            }

        }

        for (int indice : janela_indices) {

            if (count_janela == 0) {

                query_janela = "'" + janela.getModel().getElementAt(indice) + "'";
                count_janela++;

            } else {

                query_janela = query_janela + ", '" + janela.getModel().getElementAt(indice) + "'";
                count_janela++;
            }

        }

        pn.set_filter_querys(query_city, query_type, query_janela, query_dt_acomp);

        pn.atualiza_painel();

        this.dispose();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        data = new org.jdesktop.swingx.JXMonthView();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cidade = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        janela = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tipo_os = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        jButton1.setText("Aplicar Filtros");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        data.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        data.setSelectionMode(org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        data.setTodayBackground(java.awt.Color.red);
        data.setTraversable(true);

        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setText("Utilize <Ctrl> + Clique para selecionar");

        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("mais dias");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(data, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(data, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Data", jPanel5);

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setViewportView(cidade);

        jButton2.setText("Marcar Todas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Cidade", jPanel6);

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane2.setViewportView(janela);

        jButton3.setText("Marcar Todas");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Janela", jPanel7);

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane3.setViewportView(tipo_os);

        jButton4.setText("Marcar Todas");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Tipo OS", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        aplica_filtro();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        global.select_all_list(tipo_os);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        global.select_all_list(cidade);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        global.select_all_list(janela);

    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(filtros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(filtros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(filtros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(filtros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                filtros dialog = new filtros(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList cidade;
    private org.jdesktop.swingx.JXMonthView data;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JList janela;
    private javax.swing.JList tipo_os;
    // End of variables declaration//GEN-END:variables
}
