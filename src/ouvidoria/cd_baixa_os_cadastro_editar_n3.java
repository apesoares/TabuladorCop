/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class cd_baixa_os_cadastro_editar_n3 extends javax.swing.JDialog {

    JTable main_tab;
    int piEdicao = 0;

    Integer li_id;
    String ls_Quebra;

    //backup dos campos
    String ls_Nome_bkp;
    String ls_Quebra_bkp;
    String ls_COP_bkp;

    public cd_baixa_os_cadastro_editar_n3() {
        initComponents();
    }

    /**
     * Creates new form dth_mr_editar
     *
     * @param iEdicao
     * @param iID
     * @param sNome
     * @param sQuebra
     * @param sCOP
     */
    public cd_baixa_os_cadastro_editar_n3(Integer iEdicao, Integer iID, String sNome) {
        initComponents();

        //backup dos campos
        ls_Nome_bkp = sNome;

        //id
        li_id = iID;
        //nome
        jtxt_baixa_os.setText(sNome);

        //operacao de edicao
        piEdicao = iEdicao;

        this.setTitle("(N3) Código de Baixa de OS - ");

        if (iEdicao == 0) {
            jtxt_baixa_os.setEditable(true);
            this.setTitle(this.getTitle() + "Inclusão");
            jbt_salvar.setText("Incluir e prosseguir");
        } else {
            jtxt_baixa_os.setEditable(true);
            this.setTitle(this.getTitle() + "Alteração");
            jbt_salvar.setText("Alterar e prosseguir");
        }

        global.open_modal(this, this.getTitle());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtxt_baixa_os = new javax.swing.JTextField();
        jbt_salvar = new javax.swing.JButton();

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

        jLabel1.setText("Status Acomp (Código de Baixa N3)");

        jtxt_baixa_os.setColumns(20);
        jtxt_baixa_os.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxt_baixa_osKeyReleased(evt);
            }
        });

        jbt_salvar.setText("Salvar e prosseguir");
        jbt_salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_salvarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtxt_baixa_os, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 89, Short.MAX_VALUE))
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
                .addComponent(jtxt_baixa_os, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(jbt_salvar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbt_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_salvarActionPerformed

        StringBuilder sSQL = new StringBuilder();
        StringBuilder sSQL_Log = new StringBuilder();
        StringBuilder sMsg = new StringBuilder();

        String s_nome_baixa_os = "";
        String s_acao = "";

        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);

        //jtxt_baixa_os.setText(jtxt_baixa_os.getText().trim().toUpperCase());
        jtxt_baixa_os.setText(jtxt_baixa_os.getText().trim());

        if (jtxt_baixa_os.getText().length() == 0) {
            sMsg.append("Informe o nome da Baixa de OS \n");
        }
        if (sMsg.length() != 0) {
            global.show_error_message("Faltam informações. Veja abaixo: \n\n" + sMsg.toString());
            return;
        }

        s_nome_baixa_os = jtxt_baixa_os.getText();

        //atualizações - inicio
        try {
            boolean bGravar = true;
            boolean bProximaTela = true;
            if (piEdicao == 0) {
                if (ls_Nome_bkp.trim().toUpperCase().equals(s_nome_baixa_os.toUpperCase())) {
                    bProximaTela = false;
                    global.show_message("Ou informe uma nova opção de baixa ou feche a janela");
                    return;
                }
            } else {
                if (ls_Nome_bkp.trim().toUpperCase().equals(s_nome_baixa_os.toUpperCase())) {
                    bGravar = false;
                    bProximaTela = true;
                }
            }

            if (piEdicao == 1) {
                Connection conn = Db_class.mysql_conn();
                sSQL.delete(0, sSQL.length());

                sSQL.append("select nome ");
                sSQL.append("from ouvidoria.tb_acao_acomp_status ");
                sSQL.append("where trim(upper(nome)) = trim(upper('").append(s_nome_baixa_os).append("')) ");
                sSQL.append("and id <> ").append(li_id).append("; ");

                ResultSet rs = Db_class.mysql_result(conn, sSQL.toString());
                if (rs.next()) {
                    bGravar = false;
                    bProximaTela = false;
                    global.show_warning_message("Nada gravado.\n\nVerifique se a opção de Baixa já existe.");
                }

                Db_class.close_conn(conn);
            }

            if (bGravar) {
                setCursor(hourglassCursor);

                Connection conn = Db_class.mysql_conn();
                sSQL.delete(0, sSQL.length());

                if (piEdicao == 0) {
                    sSQL.append("insert into ouvidoria.tb_acao_acomp_status ");
                    sSQL.append("(nome) ");
                    sSQL.append("select ");
                    sSQL.append("'").append(s_nome_baixa_os).append("' as codigo ");
                    sSQL.append("from dual ");
                    sSQL.append("where not exists ");
                    sSQL.append("( ");
                    sSQL.append("select nome ");
                    sSQL.append("from ouvidoria.tb_acao_acomp_status ");
                    sSQL.append("where trim(upper(nome)) = trim(upper('").append(s_nome_baixa_os).append("')) ");
                    sSQL.append(") ");
                    sSQL.append("; ");
                } else {
                    sSQL.append("update ouvidoria.tb_acao_acomp_status ");
                    sSQL.append("set ");
                    sSQL.append("nome = '").append(s_nome_baixa_os).append("' ");
                    sSQL.append("where id = ").append(li_id).append("; ");
                }

                //Db_class.mysql_insert(sSQL.toString(), conn);
                PreparedStatement pStmt = conn.prepareStatement(sSQL.toString(), Statement.RETURN_GENERATED_KEYS);
                int iLinhasAfetadas = pStmt.executeUpdate();

                if (iLinhasAfetadas == 1) {
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
                    if (iLinhasAfetadas == 0) {
                        bProximaTela = false;
                        setCursor(normalCursor);
                        global.show_warning_message("Nada gravado.\n\nVerifique se a opção de Baixa já existe.");
                    } else {
                        bProximaTela = false;
                        setCursor(normalCursor);
                        global.show_warning_message("Conflito de opções de Baixa.\n\nVerifique se a opção informada está replicada e corrija.");
                    }
                }
                Db_class.close_conn(conn);
                setCursor(normalCursor);
            }

            if (bProximaTela) {
                cd_baixa_os_cadastro_editar_n3_complemento cad_baixa_os_editar_n3_complemento = new cd_baixa_os_cadastro_editar_n3_complemento(s_nome_baixa_os);
            }

            this.dispose();
            //atualizações - final
        } catch (Exception ex) {
            setCursor(normalCursor);
            global.show_error_message("Problemas para gravar as informações\n\nErro: " + ex.getMessage());
        }

    }//GEN-LAST:event_jbt_salvarActionPerformed

    private void jtxt_baixa_osKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxt_baixa_osKeyReleased
        // TODO add your handling code here:
        if (jtxt_baixa_os.getText().length() > 30) {
            //global.show_message("string grande");
            jtxt_baixa_os.setText(jtxt_baixa_os.getText().substring(0, 30).trim());
        }
    }//GEN-LAST:event_jtxt_baixa_osKeyReleased

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
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n3.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n3.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n3.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro_editar_n3.class
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
        final String sNome = args[2];

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cd_baixa_os_cadastro_editar_n3(iEdicao, iID, sNome).setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbt_salvar;
    private javax.swing.JTextField jtxt_baixa_os;
    // End of variables declaration//GEN-END:variables
}
