/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author N0026925
 */
public class baixa_os extends javax.swing.JDialog {

    tratar tr;

    List<Integer> liChave_acao_oms = new ArrayList<>();
    List<Integer> liChave_acao_contrato = new ArrayList<>();

    /**
     * Creates new form baixa_os
     */
    public baixa_os(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    public baixa_os(java.awt.Frame parent, boolean modal, tratar tr, String cop) {
        super(parent, modal);
        initComponents();

        this.tr = tr;

        if (cop.equals("COP DTH")) {
            jlbl_acao_oms.setVisible(false);
            jlbl_acao_contrato.setVisible(false);
            jcb_acao_oms.setVisible(false);
            jcb_acao_contrato.setVisible(false);
        }

        global.fill_combo(status, "SELECT status FROM status_os ORDER BY status", true);

        global.fill_combo(cod_baixa, "SELECT codigo "
                + "FROM cod_baixa_novo "
                + "WHERE cop = '" + cop + " '"
                + "ORDER BY codigo", true);

        liChave_acao_oms = global.fill_combo_key(
                jcb_acao_oms,
                "select id, nome "
                + "from ouvidoria.tb_acao_oms "
                + "where fl_ativo = 1 "
                + "order by nome ",
                true,
                1,
                2
        );

        global.open_modal(this, "Baixa de OS (N2)");

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
        status = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cod_baixa = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obs_baixa = new javax.swing.JTextArea();
        jbtn_confirmar_baixa = new javax.swing.JButton();
        jlbl_acao_oms = new javax.swing.JLabel();
        jlbl_acao_contrato = new javax.swing.JLabel();
        jcb_acao_oms = new javax.swing.JComboBox();
        jcb_acao_contrato = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Status");

        jLabel2.setText("Código Baixa");

        jLabel3.setText("Observação");

        obs_baixa.setColumns(20);
        obs_baixa.setLineWrap(true);
        obs_baixa.setRows(1);
        obs_baixa.setWrapStyleWord(true);
        jScrollPane1.setViewportView(obs_baixa);

        jbtn_confirmar_baixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        jbtn_confirmar_baixa.setText("Confirma Baixa");
        jbtn_confirmar_baixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn_confirmar_baixaActionPerformed(evt);
            }
        });

        jlbl_acao_oms.setText("Ação OMS");

        jlbl_acao_contrato.setText("Ação Contrato");

        jcb_acao_oms.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcb_acao_oms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcb_acao_omsActionPerformed(evt);
            }
        });

        jcb_acao_contrato.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbtn_confirmar_baixa))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addComponent(jlbl_acao_oms))
                                .addGap(26, 26, 26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jlbl_acao_contrato)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                            .addComponent(cod_baixa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jcb_acao_oms, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jcb_acao_contrato, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cod_baixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbl_acao_oms)
                    .addComponent(jcb_acao_oms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcb_acao_contrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlbl_acao_contrato))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jbtn_confirmar_baixa, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
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

    private void jbtn_confirmar_baixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn_confirmar_baixaActionPerformed

        String codigo = (String) cod_baixa.getSelectedItem();
        if (codigo.length() == 0) {
            global.show_error_message("É necessário selecionar um código de baixa!");
            return;
        }

        if (jcb_acao_oms.isVisible()) {
            if (jcb_acao_oms.getSelectedIndex() <= 0) {
                global.show_error_message("Selecione uma Ação OMS");
                return;
            }
            if (jcb_acao_contrato.getSelectedIndex() <= 0) {
                global.show_error_message("Selecione uma Ação Contrato");
                return;
            }

            this.dispose();

            tr.finaliza_os((String) status.getSelectedItem(),
                    (String) cod_baixa.getSelectedItem(), obs_baixa.getText(), false,
                    liChave_acao_oms.get(jcb_acao_oms.getSelectedIndex() - 1),
                    liChave_acao_contrato.get(jcb_acao_contrato.getSelectedIndex() - 1),
                    2
            );

        } else {
            this.dispose();

            tr.finaliza_os((String) status.getSelectedItem(),
                    (String) cod_baixa.getSelectedItem(), obs_baixa.getText(), false,
                    0,
                    0,
                    2
            );
        }

    }//GEN-LAST:event_jbtn_confirmar_baixaActionPerformed

    private void jcb_acao_omsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcb_acao_omsActionPerformed

        if (jcb_acao_oms.getSelectedIndex() > 0 && liChave_acao_oms.size() > 0) {
            liChave_acao_contrato = global.fill_combo_key(jcb_acao_contrato,
                    "select id, nome "
                    + "from ouvidoria.tb_acao_contrato "
                    + "where fl_ativo = 1 "
                    + "and id in "
                    + "("
                    + "select id_contrato "
                    + "from ouvidoria.tb_acao_omsxcontratoxsla "
                    + "where id_oms = " + liChave_acao_oms.get(jcb_acao_oms.getSelectedIndex() - 1)
                    + ")"
                    + "order by nome ",
                    true,
                    1, 2
            );
        } else {
            jcb_acao_contrato.removeAllItems();
        }

    }//GEN-LAST:event_jcb_acao_omsActionPerformed

    /**
     * jbtn_confirmar_baixa args the command line arguments
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
            java.util.logging.Logger.getLogger(baixa_os.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(baixa_os.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(baixa_os.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(baixa_os.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                baixa_os dialog = new baixa_os(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cod_baixa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtn_confirmar_baixa;
    private javax.swing.JComboBox jcb_acao_contrato;
    private javax.swing.JComboBox jcb_acao_oms;
    private javax.swing.JLabel jlbl_acao_contrato;
    private javax.swing.JLabel jlbl_acao_oms;
    private javax.swing.JTextArea obs_baixa;
    private javax.swing.JComboBox status;
    // End of variables declaration//GEN-END:variables
}