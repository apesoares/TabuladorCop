/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
public class cd_baixa_os_cadastro_editar_n2 extends javax.swing.JDialog {

    JTable main_tab;
    int piEdicao = 0;

    Integer li_id;
    String ls_Quebra;

    //backup dos campos
    String ls_Codigo_bkp;
    String ls_Quebra_bkp;
    String ls_COP_bkp;

    public cd_baixa_os_cadastro_editar_n2() {
        initComponents();
    }

    /**
     * Creates new form dth_mr_editar
     *
     * @param iEdicao
     * @param iID
     * @param sCodigo
     * @param sQuebra
     * @param sCOP
     */
    public cd_baixa_os_cadastro_editar_n2(Integer iEdicao, Integer iID, String sCodigo, String sQuebra, String sCOP) {
        initComponents();

        //backup dos campos
        ls_Codigo_bkp = sCodigo;
        ls_Quebra_bkp = sQuebra;
        ls_COP_bkp = sCOP;

        //id
        li_id = iID;
        //nome
        jtxt_cod_baixa_os.setText(sCodigo);
        //quebra
        ls_Quebra = sQuebra;
        //tipo
        jcb_tipo_cop.setSelectedIndex(0);
        for (int i = 0; i < jcb_tipo_cop.getItemCount(); i++) {
            if (sCOP.trim().toUpperCase().equals(jcb_tipo_cop.getItemAt(i).toString().trim().toUpperCase())) {
                jcb_tipo_cop.setSelectedIndex(i);
                break;
            }
        }

        piEdicao = iEdicao;

        this.setTitle("(N2) Código de Baixa de OS - ");

        if (iEdicao == 0) {
            jtxt_cod_baixa_os.setEditable(true);
            jcb_tipo_cop.setEnabled(true);
            jcb_tipo_cop.setSelectedIndex(-1);
            this.setTitle(this.getTitle() + "Inclusão");
            jbt_salvar.setText("Incluir");
        } else {
            if (piEdicao == 1) {
                jtxt_cod_baixa_os.setEditable(true);
                jcb_tipo_cop.setEnabled(true);
                this.setTitle(this.getTitle() + "Alteração");
                jbt_salvar.setText("Alterar");
            } else {
                jtxt_cod_baixa_os.setEditable(false);
                jcb_tipo_cop.setEnabled(false);
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
        jLabel1 = new javax.swing.JLabel();
        jtxt_cod_baixa_os = new javax.swing.JTextField();
        jbt_salvar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jcb_tipo_cop = new javax.swing.JComboBox();

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

        jLabel1.setText("O código de Baixa faz parte do nome");

        jtxt_cod_baixa_os.setColumns(20);
        jtxt_cod_baixa_os.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxt_cod_baixa_osKeyReleased(evt);
            }
        });

        jbt_salvar.setText("Salvar");
        jbt_salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_salvarActionPerformed(evt);
            }
        });

        jLabel4.setText("Tipo de cadastro");

        jcb_tipo_cop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione", "COP DTH", "COP NET" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtxt_cod_baixa_os, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel4)
                                .addGap(34, 34, 34)
                                .addComponent(jcb_tipo_cop, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbt_salvar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addComponent(jtxt_cod_baixa_os, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jcb_tipo_cop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jbt_salvar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbt_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_salvarActionPerformed

        StringBuilder sSQL = new StringBuilder();
        StringBuilder sSQL_Log = new StringBuilder();
        StringBuilder sMsg = new StringBuilder();

        String s_cod_baixa_os = "";
        String s_cod_baixa_os_tipo = "";
        String s_acao = "";

        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);

        if (piEdicao == 0 || piEdicao == 1) {

            jtxt_cod_baixa_os.setText(jtxt_cod_baixa_os.getText().trim().toUpperCase());

            if (jtxt_cod_baixa_os.getText().length() == 0) {
                sMsg.append("Informe o nome da Baixa de OS \n");
            }
            if (jcb_tipo_cop.getSelectedIndex() < 1) {
                sMsg.append("Informe o tipo da Baixa de OS \n");
            }
            if (sMsg.length() != 0) {
                global.show_error_message("Faltam informações. Veja abaixo: \n\n" + sMsg.toString());
                return;
            }

            s_cod_baixa_os = jtxt_cod_baixa_os.getText();
            s_cod_baixa_os_tipo = jcb_tipo_cop.getSelectedItem().toString();

            if (ls_Codigo_bkp.trim().toUpperCase().equals(s_cod_baixa_os.toUpperCase()) && ls_Quebra_bkp.trim().toUpperCase().equals(ls_Quebra.toUpperCase()) && ls_COP_bkp.equals(s_cod_baixa_os_tipo.toUpperCase())) {
                global.show_message("Nenhuma mudança realizada.\n\nA janela será fechada");
                this.dispose();
                return;
            }
        }

        //atualizações - inicio
        try {
            setCursor(hourglassCursor);

            Connection conn = Db_class.mysql_conn();
            sSQL.delete(0, sSQL.length());

            if (piEdicao == 0) {
                sSQL.append("insert into ouvidoria.cod_baixa_novo ");
                sSQL.append("(codigo, quebra, cop) ");
                sSQL.append("select ");
                sSQL.append("'").append(s_cod_baixa_os).append("' as codigo, ");
                sSQL.append("'").append(ls_Quebra).append("' as quebra, ");
                sSQL.append("'").append(s_cod_baixa_os_tipo).append("' as cop ");
                sSQL.append("from dual ");
                sSQL.append("where not exists ");
                sSQL.append("( ");
                sSQL.append("select distinct codigo ");
                sSQL.append("from ouvidoria.cod_baixa_novo ");
                sSQL.append("where trim(upper(codigo)) = trim(upper('").append(s_cod_baixa_os).append("')) ");
                sSQL.append("and trim(upper(cop)) = trim(upper('").append(s_cod_baixa_os_tipo).append("')) ");
                sSQL.append(") ");
                sSQL.append("; ");
            } else {
                if (piEdicao == 1) {
                    sSQL.append("update ouvidoria.cod_baixa_novo ");
                    sSQL.append("set ");
                    sSQL.append("codigo = '").append(s_cod_baixa_os).append("', ");
                    sSQL.append("quebra = '").append(ls_Quebra).append("', ");
                    sSQL.append("cop = '").append(s_cod_baixa_os_tipo).append("' ");
                    sSQL.append("where id = ").append(li_id).append("; ");
                } else {
                    sSQL.append("delete from ouvidoria.cod_baixa_novo ");
                    sSQL.append("where id = ").append(li_id).append("; ");
                }
            }

            //Db_class.mysql_insert(sSQL.toString(), conn);
            PreparedStatement pStmt = conn.prepareStatement(sSQL.toString(), Statement.RETURN_GENERATED_KEYS);
            int iLinhasAfetadas = pStmt.executeUpdate();

            if (iLinhasAfetadas >= 1) {
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
            } else {
                setCursor(normalCursor);
                global.show_warning_message("Nada gravado.\n\nVerifique se a opção de Baixa já existe.");
            }

            Db_class.close_conn(conn);

            setCursor(normalCursor);

            this.dispose();
            //atualizações - final
        } catch (Exception ex) {
            setCursor(normalCursor);
            global.show_error_message("Problemas para gravar as informações\n\nErro: " + ex.getMessage());
        }

    }//GEN-LAST:event_jbt_salvarActionPerformed

    private void jtxt_cod_baixa_osKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxt_cod_baixa_osKeyReleased
        // TODO add your handling code here:
        if (jtxt_cod_baixa_os.getText().length() > 100) {
            //global.show_message("string grande");
            jtxt_cod_baixa_os.setText(jtxt_cod_baixa_os.getText().substring(0, 100).trim());
        }
    }//GEN-LAST:event_jtxt_cod_baixa_osKeyReleased

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
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        final Integer iEdicao = Integer.parseInt(args[0]);
        final Integer iID = Integer.parseInt(args[1]);
        final String sCodigo = args[2];
        final String sQuebra = args[3];
        final String sCOP = args[4];

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cd_baixa_os_cadastro_editar_n2(iEdicao, iID, sCodigo, sQuebra, sCOP).setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbt_salvar;
    private javax.swing.JComboBox jcb_tipo_cop;
    private javax.swing.JTextField jtxt_cod_baixa_os;
    // End of variables declaration//GEN-END:variables
}
