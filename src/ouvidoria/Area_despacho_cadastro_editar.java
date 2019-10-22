/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

/**
 *
 * @author ROBSMAC
 */
public class Area_despacho_cadastro_editar extends javax.swing.JDialog {

    JTable main_tab;
    int piEdicao = 0;

    Integer li_id;

    //backup dos campos
    String ls_Area_bkp;

    public Area_despacho_cadastro_editar() {
        initComponents();
    }

    /**
     * Creates new form dth_mr_editar
     *
     * @param iEdicao
     * @param iID
     * @param sCidade
     * @param sArea
     */
    public Area_despacho_cadastro_editar(Integer iEdicao, Integer iID, String sCidade, String sArea) {
        initComponents();

        //backup dos campos
        ls_Area_bkp = sArea;

        //id
        li_id = iID;
        //cidade
        jtxt_cidade.setText(sCidade);
        //area
        jtxt_area.setText(sArea);

        piEdicao = iEdicao;

        this.setTitle("Cadastro de Áreas de Despacho - ");
        
        if (iEdicao == 0) {
            jbl_cidade_alerta.setVisible(true);
            jtxt_cidade.setEditable(true);
            jtxt_area.setEditable(true);
            this.setTitle(this.getTitle() + "Inclusão");
            jbt_salvar.setText("Incluir");
        } else {
            jbl_cidade_alerta.setVisible(false);
            if (piEdicao == 1) {
                jtxt_cidade.setEditable(false);
                jtxt_area.setEditable(true);
                this.setTitle(this.getTitle() + "Alteração");
                jbt_salvar.setText("Alterar");
            } else {
                jtxt_cidade.setEditable(false);
                jtxt_area.setEditable(false);
                this.setTitle(this.getTitle() + "Exclusão");
                jbt_salvar.setText("Excluir");
            }
        }

        global.open_modal(this, this.getTitle());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jlbl_cidade = new javax.swing.JLabel();
        jtxt_cidade = new javax.swing.JTextField();
        jbt_salvar = new javax.swing.JButton();
        jtxt_area = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jbl_cidade_alerta = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edição de MR");

        jlbl_cidade.setText("Cidade");

        jtxt_cidade.setEditable(false);

        jbt_salvar.setText("Salvar");
        jbt_salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_salvarActionPerformed(evt);
            }
        });

        jLabel2.setText("Informe a Área de Despacho");

        jbl_cidade_alerta.setForeground(new java.awt.Color(255, 0, 51));
        jbl_cidade_alerta.setText("Se a cidade já existir, escreva como já está.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 196, Short.MAX_VALUE)
                        .addComponent(jbt_salvar))
                    .addComponent(jtxt_area)
                    .addComponent(jtxt_cidade)
                    .addComponent(jbl_cidade_alerta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbl_cidade)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbl_cidade)
                .addGap(2, 2, 2)
                .addComponent(jtxt_cidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbl_cidade_alerta)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(1, 1, 1)
                .addComponent(jtxt_area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbt_salvar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbt_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_salvarActionPerformed

        StringBuilder sSQL = new StringBuilder();
        StringBuilder sSQL_Log = new StringBuilder();
        StringBuilder sMsg = new StringBuilder();

        String s_cidade = "";
        String s_area = "";
        String s_acao = "";

        if (piEdicao == 0 || piEdicao == 1) {

            jtxt_area.setText(jtxt_area.getText().trim().toUpperCase());

            s_cidade = jtxt_cidade.getText().trim();
            s_area = jtxt_area.getText();

            if (s_cidade.length() == 0) {
                sMsg.append("Informe a Cidade \n");
            }
            if (s_area.length() == 0) {
                sMsg.append("Informe a Área de Despacho \n");
            }
            if (sMsg.length() != 0) {
                global.show_error_message("Faltam informações. Veja abaixo: \n\n" + sMsg.toString());
                return;
            }

            if (ls_Area_bkp.equals(s_area.toUpperCase())) {
                global.show_message("Nenhuma mudança realizada.\n\nA janela será fechada");
                this.dispose();
                return;
            }
        }

        //atualizações - inicio
        try {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            Connection conn = Db_class.mysql_conn();
            sSQL.delete(0, sSQL.length());

            if (piEdicao == 0) {
                sSQL.append("insert into ouvidoria.areas_novo ");
                sSQL.append("(cidade, area) ");
                sSQL.append("select ");
                sSQL.append("'").append(s_cidade).append("' as cidade, ");
                sSQL.append("'").append(s_area).append("' as area ");
                sSQL.append("from dual ");
                sSQL.append("where not exists ");
                sSQL.append("( ");
                sSQL.append("select distinct id ");
                sSQL.append("from ouvidoria.areas_novo ");
                sSQL.append("where trim(upper(cidade)) = trim(upper('").append(s_cidade).append("')) ");
                sSQL.append("and trim(upper(area)) = trim(upper('").append(s_area).append("')) ");
                sSQL.append(") ");
                sSQL.append("; ");
            } else {
                if (piEdicao == 1) {
                    sSQL.append("update ouvidoria.areas_novo ");
                    sSQL.append("set ");
                    sSQL.append("area = '").append(s_area).append("' ");
                    sSQL.append("where id = ").append(li_id).append("; ");
                } else {
                    sSQL.append("delete from ouvidoria.areas_novo ");
                    sSQL.append("where id = ").append(li_id).append("; ");
                }
            }

            Db_class.mysql_insert(sSQL.toString(), conn);

            //log - inicio
            sSQL_Log.delete(0, sSQL_Log.length());
            sSQL_Log.append("insert into tb_log (login, dt_registro, comando) ");
            sSQL_Log.append("select ");
            sSQL_Log.append("'").append(System.getProperty("user.name")).append("', ");
            sSQL_Log.append("now(), ");
            sSQL_Log.append("trim( ");
            sSQL_Log.append("substr( ");
            sSQL_Log.append("'").append(sSQL.toString().replaceAll("'", "")).append("' ");
            sSQL_Log.append(", 1, 1000) ");
            sSQL_Log.append(") ");
            sSQL_Log.append("; ");

            Db_class.mysql_insert(sSQL_Log.toString(), conn);
            //log - final

            Db_class.close_conn(conn);

            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);

            global.show_message("Informações gravadas.\n\nNa janela anterior, execute uma nova consulta para\nver o resultado.");
            this.dispose();
            //atualizações - final
        } catch (Exception ex) {
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
            global.show_error_message("Problemas para gravar as informações\n\nErro: " + ex.getMessage());
        }

    }//GEN-LAST:event_jbt_salvarActionPerformed

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
            java.util.logging.Logger.getLogger(Area_despacho_cadastro_editar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Area_despacho_cadastro_editar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Area_despacho_cadastro_editar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Area_despacho_cadastro_editar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        final Integer iEdicao = Integer.parseInt(args[0]);
        final Integer iID = Integer.parseInt(args[1]);
        final String sCidade = args[2];
        final String sArea = args[3];

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Area_despacho_cadastro_editar(iEdicao, iID, sCidade, sArea).setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jbl_cidade_alerta;
    private javax.swing.JButton jbt_salvar;
    private javax.swing.JLabel jlbl_cidade;
    private javax.swing.JTextField jtxt_area;
    private javax.swing.JTextField jtxt_cidade;
    // End of variables declaration//GEN-END:variables
}
