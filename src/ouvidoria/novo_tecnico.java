/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author N0026925
 */
public class novo_tecnico extends javax.swing.JDialog {

    Menu mn;
    tratar tr;
    boolean tratando;
    String cop;

    /**
     * Creates new form novo_tecnico
     */
    public novo_tecnico(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        global.initCloseListener(this);

    }

    public novo_tecnico(Menu mn, boolean modal, String cop_rec) {

        super(mn, modal);
        initComponents();

        this.mn = mn;
        this.cop = cop_rec;

        telefone.addKeyListener(new key_telefone());

        global.fill_combo(cidade, "SELECT cluster_tec "
                + "FROM cidades_novo "
                + "WHERE cop = '" + cop_rec + "' "
                + "GROUP BY cluster_tec "
                + "ORDER BY cluster_tec", false);

        global.fill_combo(tipo_tec, "SELECT tipo_tec "
                + "FROM tipo_tec "
                + "ORDER BY tipo_tec", true);

        cidade.addItemListener(new cidade_listener());

        global.initCloseListener(this);

        global.open_modal(this, "Cadastro - Novo técnico");

    }

    public novo_tecnico(Menu mn, boolean modal, tratar tr, String cidade_pass, String cop_rec) {

        super(mn, modal);
        initComponents();

        this.mn = mn;
        this.cop = cop_rec;

        telefone.addKeyListener(new key_telefone());

        global.fill_combo(cidade, "SELECT cluster_tec "
                + "FROM cidades_novo "
                + "WHERE cop = '" + cop_rec + "' "
                + "GROUP BY cluster_tec "
                + "ORDER BY cluster_tec", false);

        global.fill_combo(tipo_tec, "SELECT tipo_tec "
                + "FROM tipo_tec "
                + "ORDER BY tipo_tec", true);

        cidade.addItemListener(new cidade_listener());

        this.tr = tr;

        tratando = true;

        set_cidade(cidade_pass);

        global.initCloseListener(this);

        global.open_modal(this, "Cadastro - Novo técnico");

    }

    public novo_tecnico(Menu mn, boolean modal, String check, String cop_rec) {

        super(mn, modal);
        initComponents();

        this.mn = mn;
        this.cop = cop_rec;

        telefone.addKeyListener(new key_telefone());

        global.fill_combo(cidade, "SELECT cluster_tec "
                + "FROM cidades_novo "
                + "WHERE cop = '" + cop_rec + "' "
                + "GROUP BY cluster_tec "
                + "ORDER BY cluster_tec", false);

        global.fill_combo(tipo_tec, "SELECT tipo_tec "
                + "FROM tipo_tec "
                + "ORDER BY tipo_tec", true);

        cidade.addItemListener(new cidade_listener());

        global.initCloseListener(this);

    }

    class cidade_listener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {

                String nome_cidade = (String) cidade.getSelectedItem();

                area.removeAllItems();

                try {
                    Connection conn = Db_class.mysql_conn();

                    String query = "SELECT count(*) "
                            + "FROM areas_novo "
                            + "LEFT JOIN cidades_novo cd ON areas_novo.cidade = cd.ci_depara "
                            + "WHERE areas_novo.cidade = '" + nome_cidade + "' "
                            + "OR cd.cluster_tec = '" + nome_cidade + "' "
                            + "GROUP BY cidade ";

                    System.out.println(query);

                    ResultSet rs = Db_class.mysql_result(conn, query);

                    rs.next();
                    long cont = (long) rs.getObject(1);

                    area.addItem("");

                    if (cont > 1) {

                        area.addItem("Todas");

                    }

                    global.fill_combo(area, "SELECT area "
                            + "FROM areas_novo "
                            + "LEFT JOIN cidades_novo cd ON areas_novo.cidade = cd.ci_depara "
                            + "WHERE areas_novo.cidade = '" + nome_cidade + "' "
                            + "OR cd.cluster_tec = '" + nome_cidade + "' "
                            + "GROUP BY area ORDER BY area", false);

                    if (cont == 1) {
                        area.setSelectedIndex(1);
                    }

                    parceira.removeAllItems();
                    parceira.addItem("");
                    parceira.addItem("NET");

                    global.fill_combo(parceira, "SELECT parceira "
                            //+ "FROM cop_info_parceiras "
                            + "FROM vw_cop_info_parceiras "
                            //+ "WHERE cidade IN (SELECT cidade_qualinet FROM cidades_novo "
                            + "WHERE cidade IN (SELECT cop_info FROM cidades_novo "
                            + "WHERE cluster_tec = '" + nome_cidade + "') "
                            + "GROUP BY parceira "
                            + "ORDER BY parceira", false);

                    Db_class.close_conn(conn);

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                    Logger.getLogger(novo_tecnico.class.getName()).log(Level.SEVERE, null, ex);
                }

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cidade = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        area = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        login = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        nome = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        parceira = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        telefone = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obs = new javax.swing.JTextArea();
        botao_inserção = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        tipo_tec = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Cidade");

        jLabel2.setText("Área");

        jLabel3.setText("Login");

        jLabel4.setText("Nome");

        jLabel5.setText("Parceira");

        jLabel6.setText("Telefone");

        telefone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telefoneActionPerformed(evt);
            }
        });
        telefone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                telefoneKeyReleased(evt);
            }
        });

        jLabel7.setText("Observação");

        obs.setColumns(20);
        obs.setRows(5);
        jScrollPane1.setViewportView(obs);

        botao_inserção.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png"))); // NOI18N
        botao_inserção.setText("Inserir Registro");
        botao_inserção.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_inserçãoActionPerformed(evt);
            }
        });

        jLabel8.setText("Turno");

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Manhã", "Tarde", "Integral" }));

        jLabel9.setText("Tipo Técnico");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tipo_tec, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(parceira, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cidade, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botao_inserção, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(cidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(parceira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(telefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(tipo_tec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(botao_inserção)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botao_inserçãoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_inserçãoActionPerformed

        if (!check_fields()) {

            return;

        }

        insere_tecnico();


    }//GEN-LAST:event_botao_inserçãoActionPerformed

    private void telefoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telefoneActionPerformed

    }//GEN-LAST:event_telefoneActionPerformed

    private void telefoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telefoneKeyReleased

        long x;

        if (telefone.getText().equals("")) {

        } else {

            try {
                x = Long.parseLong(telefone.getText());
            } catch (NumberFormatException nfe) {

                telefone.setText("");
                global.show_error_message("Apenas números são permitidos para esse campo!");

            }
        }

    }//GEN-LAST:event_telefoneKeyReleased

    public void insere_tecnico() {

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

            String query = "INSERT INTO tecnicos "
                    + "(cluster, "
                    + "cidade, "
                    + "area, "
                    + "login, "
                    + "nome, "
                    + "parceira, "
                    + "telefone, "
                    + "turno_tec, "
                    + "obs, "
                    + "tipo_tec) "
                    + "VALUES ('" + cluster + "', "
                    + "'" + cidade.getSelectedItem() + "',"
                    + "'" + area.getSelectedItem() + "', "
                    + "'" + login.getText() + "',"
                    + "'" + nome.getText() + "',"
                    + "'" + parceira.getSelectedItem() + "', "
                    + "'" + telefone.getText() + "',"
                    + "'" + turno.getSelectedItem() + "',"
                    + "'" + obs.getText() + "', "
                    + "'" + tipo_tec.getSelectedItem() + "')";

            Db_class.mysql_insert(query, conn);

            if (tratando) {

                int resposta = global.dialog_question("Deseja alocar o técnico cadastrado ao caso em tratamento?");

                if (resposta != 0) {

                    global.show_message("Técnico cadastrado com sucesso!");
                    return;

                }

                String login_p = login.getText();
                String tecnico_p = nome.getText();
                String parceira_p = (String) parceira.getSelectedItem();
                String telefone_p = telefone.getText();
                String turno_p = (String) turno.getSelectedItem();
                String tipo_p = (String) tipo_tec.getSelectedItem();
                String obs_p = obs.getText();

                String[] array_info_tec = {login_p, tecnico_p, parceira_p, telefone_p, obs_p, turno_p, tipo_p};

                this.dispose();
                tr.set_tecnico(array_info_tec);
                tr.altera_tec(false);

            } else {

                global.show_message("Técnico cadastrado com sucesso!");

            }

            global.insert_prod(0, "Cadastro de técnico", mn);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            global.show_error_message(ex.getMessage());
        }

    }

    public boolean check_fields() {

        Component[] cps = jPanel1.getComponents();

        for (Component cp : cps) {

            boolean status = global.check_cp(cp);

            if (!status) {

                global.show_error_message("É necessário preencher todos os campos!");

                cp.requestFocus();

                return false;
            }

        }

        return true;

    }

    public void set_cidade(String cidade_pass) {

        cidade.setSelectedItem(cidade_pass);

    }

    class key_telefone implements KeyListener {

        @Override
        public void keyTyped(KeyEvent ke) {

        }

        @Override
        public void keyPressed(KeyEvent ke) {

        }

        @Override
        public void keyReleased(KeyEvent ke) {

            long x;

            if (telefone.getText().equals("")) {

            } else {

                try {
                    x = Long.parseLong(telefone.getText());
                } catch (NumberFormatException nfe) {

                    telefone.setText("");
                    global.show_error_message("Apenas números são permitidos para esse campo!");

                }
            }

        }

    }

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
            java.util.logging.Logger.getLogger(novo_tecnico.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(novo_tecnico.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(novo_tecnico.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(novo_tecnico.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                novo_tecnico dialog = new novo_tecnico(new javax.swing.JFrame(), true);
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
    public javax.swing.JComboBox area;
    public javax.swing.JButton botao_inserção;
    public javax.swing.JComboBox cidade;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    public javax.swing.JLabel jLabel8;
    public javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField login;
    public javax.swing.JTextField nome;
    public javax.swing.JTextArea obs;
    public javax.swing.JComboBox parceira;
    public javax.swing.JTextField telefone;
    public javax.swing.JComboBox tipo_tec;
    public javax.swing.JComboBox turno;
    // End of variables declaration//GEN-END:variables
}
