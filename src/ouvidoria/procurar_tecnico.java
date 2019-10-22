/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import ouvidoria.global;

/**
 *
 * @author N0026925
 */
public class procurar_tecnico extends javax.swing.JDialog {

    JTable tab;
    tratar tr;
    String sCidade;

    /**
     * Creates new form procurar_tecnico
     */
    public procurar_tecnico(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        global.open_modal(this, null);

    }

    public procurar_tecnico(Menu mn, boolean modal, String cidade, String area, tratar tr) {
        super(mn, modal);
        initComponents();
        
        sCidade = cidade;

        String query_cluster = "SELECT cluster_tec FROM cidades_novo "
                + "WHERE ci_depara = '" + cidade + "'";
        try {
            Connection conn = Db_class.mysql_conn();

            ResultSet rs_cluster = Db_class.mysql_result(conn, query_cluster);

            rs_cluster.next();

            String cluster = rs_cluster.getString(1);

            this.tr = tr;

            String query_tecs = "SELECT "
                    + "    ifnull(`tecnicos`.`cidade`,'') as Cidade,\n"
                    + "    ifnull(`tecnicos`.`area`,'') as Area,\n"
                    + "    ifnull(`tecnicos`.`login`,'') as Login,\n"
                    + "    ifnull(`tecnicos`.`nome`,'') as Nome,\n"
                    + "    ifnull(`tecnicos`.`parceira`,'') as Parceira,\n"
                    + "    ifnull(`tecnicos`.`telefone`,'') as Telefone,\n"
                    + "    ifnull(turno_tec,'') as Turno, "
                    + "    ifnull(tipo_tec,'') as Tipo, "
                    + "    ifnull(`tecnicos`.`obs`,'') as Obs \n"
                    + "FROM `ouvidoria`.`tecnicos` "
                    + "WHERE cluster = '" + cluster + "' and (area = '" + area + "' or area = 'Todas')"
                    + "GROUP BY login, nome, cidade, area "
                    + "ORDER BY nome ";

            tab = global.get_non_edit_Table(query_tecs, jPanel1);

            tab.addMouseListener(new tec_listener(this));
            
            TableFilterHeader filter = new TableFilterHeader(tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);
            
            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(procurar_tecnico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(procurar_tecnico.class.getName()).log(Level.SEVERE, null, ex);
        }

        global.open_modal(this, "Selecione um técnico");

    }

    public class tec_listener extends MouseAdapter {

        procurar_tecnico pt;

        public tec_listener(procurar_tecnico pt) {

            this.pt = pt;

        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {

                int row = tab.rowAtPoint(e.getPoint());
                //String cidade = (String) tab.getValueAt(row, 0); //e cluster e não cidade
                String area = (String) tab.getValueAt(row, 1);
                String login = (String) tab.getValueAt(row, 2);
                String tecnico = (String) tab.getValueAt(row, 3);
                String parceira = (String) tab.getValueAt(row, 4);
                String telefone = (String) tab.getValueAt(row, 5);
                String turno = (String) tab.getValueAt(row, 6);
                String tipo = (String) tab.getValueAt(row, 7);
                String obs = (String) tab.getValueAt(row, 8);

                //verificando se Cidade X Parceira ainda existe
                if (global.Valido_CidadeXParceira(sCidade, parceira) == 0) {
                    global.show_warning_message(
                            "O relacionamento Cidade X Parceira não existe mais.\n"
                            + "Por favor, acesse o cadastro do técnico selecionado\n"
                            + "e acerte Cidade e/ou Parceira.\n\n"
                            + "Esse tipo de problema pode acontecer quando a parceira\n"
                            + "muda de nome.\n\n"
                    );
                    return;
                }
                
                //continuando o processo de associação do técnico
                String[] array_info_tec = {login, tecnico, parceira, telefone, obs, turno, tipo};

                pt.dispose();
                tr.set_tecnico(array_info_tec);

                tr.altera_tec(false);

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1011, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 268, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(procurar_tecnico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(procurar_tecnico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(procurar_tecnico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(procurar_tecnico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                procurar_tecnico dialog = new procurar_tecnico(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
