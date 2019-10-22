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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 *
 * @author N0026925
 */
public class divisao_operador extends javax.swing.JDialog {
    
    Menu mn;
    int[] current_rows;
    JTable current_tab;
    JDialog current_dlg;
    String turno_name;
    String field_name;
    public String tipo_serv;

    /**
     * Creates new form divisao_operador
     */
    public divisao_operador() {
        initComponents();
    }
    
    public divisao_operador(Menu mn, String tipo) {
        
        initComponents();
        
        this.mn = mn;
        this.tipo_serv = tipo;
        
        global.fill_combo(user_select, "SELECT nome FROM users "
                + "WHERE cop = '" + mn.get_cop() + "' "
                + "ORDER BY nome", true);
        
        user_select.addItemListener(new user_listener());
        
        user_select.setVisible(false);
        label_select.setVisible(false);
        
        atualizar();
        
        this.setPreferredSize(new Dimension(1000, 500));
        
        global.open_modal(this, "Divisão de cidades - " + tipo);
        
    }
    
    public void atualizar() {
        
        jPanel1.removeAll();
        
        if (turno.getSelectedItem().equals("Manhã")) {
            
            turno_name = "manha";
            
        } else {
            
            turno_name = "tarde";
            
        }
        
        field_name = "user_" + tipo_serv + "_" + turno_name;
        
        try {
            JTable tab = global.getTable("SELECT ifnull(ci_depara,'') as Cidade, "
                    + "ifnull(" + field_name + ",'') as 'Operador " + turno_name + "' "
                    + "FROM cidades_novo "
                    + "WHERE cop = 'COP NET' "
                    + "ORDER BY ci_depara ", jPanel1);
            
            tab.addMouseListener(new tab_list());
            
            current_tab = tab;
            
            jPanel1.repaint();
            jPanel1.revalidate();
            
        } catch (Exception ex) {
            Logger.getLogger(divisao_operador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public class tab_list extends MouseAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
            if (SwingUtilities.isRightMouseButton(e)) {
                
                current_rows = current_tab.getSelectedRows();
                
                JDialog dlg = new JDialog(mn, true);
                dlg.setPreferredSize(new Dimension(300, 50));
                
                current_dlg = dlg;
                
                JComboBox combo = new JComboBox();
                
                dlg.add(combo);
                
                global.fill_combo(combo, "SELECT nome FROM users "
                        + "WHERE cop = 'COP NET' "
                        + "ORDER BY nome", true);
                
                combo.addItemListener(new combo_listener());
                
                global.modal_click(dlg, "Atribuir operador", e.getLocationOnScreen(), current_tab);
                
            }
            
        }
        
    }
    
    public class combo_listener implements ItemListener {
        
        @Override
        public void itemStateChanged(ItemEvent ie) {
            
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                
                JComboBox combo = (JComboBox) ie.getSource();
                
                String nome = (String) combo.getSelectedItem();
                String cidades = "";
                String sql_cidades = "";
                
                int cont = 0;
                
                for (int id : current_rows) {
                    
                    cidades = cidades + "\n" + current_tab.getValueAt(id, 0);
                    
                    if (cont == 0) {
                        
                        sql_cidades = "'" + current_tab.getValueAt(id, 0) + "'";
                        
                    } else {
                        
                        sql_cidades = sql_cidades + ", " + "'" + current_tab.getValueAt(id, 0) + "'";
                        
                    }
                    
                    cont++;
                    
                }
                
                int result = global.dialog_question("Deseja atribuir as cidades abaixo para " + nome + "?\n" + cidades);
                
                if (result != 0) {
                    
                    current_dlg.dispose();
                    return;
                    
                }
                try {
                    Connection conn = Db_class.mysql_conn();
                    
                    String query = "UPDATE cidades SET " + field_name + " = '" + nome + "' "
                            + "WHERE ci_depara IN (" + sql_cidades + ")";
                    
                    Db_class.mysql_insert(query, conn);
                    
                    Db_class.close_conn(conn);
                    
                    current_dlg.dispose();
                    
                    global.show_message("Cidades atribuídas com sucesso!");
                    
                    atualizar();
                    
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                    Logger.getLogger(divisao_operador.class.getName()).log(Level.SEVERE, null, ex);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jRadioButton2 = new javax.swing.JRadioButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jRadioButton1 = new javax.swing.JRadioButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        label_select = new javax.swing.JLabel();
        user_select = new javax.swing.JComboBox();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        turno = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        jPanel1.setLayout(new java.awt.BorderLayout());
        add(jPanel1, java.awt.BorderLayout.CENTER);

        jToolBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Lista Geral");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jRadioButton2);
        jToolBar1.add(jSeparator2);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Individual");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jRadioButton1);
        jToolBar1.add(jSeparator3);

        label_select.setText("Selecione um usuário   ");
        jToolBar1.add(label_select);

        user_select.setMaximumSize(new java.awt.Dimension(200, 22));
        user_select.setPreferredSize(new java.awt.Dimension(200, 22));
        jToolBar1.add(user_select);
        jToolBar1.add(jSeparator4);

        jLabel1.setText("Selecione o turno   ");
        jToolBar1.add(jLabel1);

        turno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Manhã", "Tarde" }));
        turno.setMinimumSize(new java.awt.Dimension(100, 22));
        turno.setPreferredSize(new java.awt.Dimension(100, 22));
        turno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                turnoItemStateChanged(evt);
            }
        });
        jToolBar1.add(turno);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        
        if (jRadioButton2.isSelected()) {
            
            label_select.setVisible(false);
            user_select.setVisible(false);
            
            atualizar();
            
        } else {
            
            label_select.setVisible(true);
            user_select.setVisible(true);
            
            atualiza_individual();
            
        }

    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        
        if (jRadioButton1.isSelected()) {
            
            label_select.setVisible(true);
            user_select.setVisible(true);
            
            atualiza_individual();
            
        } else {
            
            label_select.setVisible(false);
            user_select.setVisible(false);
            
            atualizar();
            
        }

    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void turnoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_turnoItemStateChanged
        
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            
            if (jRadioButton2.isSelected()) {
                
                label_select.setVisible(false);
                user_select.setVisible(false);
                
                atualizar();
                
            } else {
                
                label_select.setVisible(true);
                user_select.setVisible(true);
                
                atualiza_individual();
                
            }
            
        }

    }//GEN-LAST:event_turnoItemStateChanged
    
    public void atualiza_individual() {
        
        if (turno.getSelectedItem().equals("Manhã")) {
            
            turno_name = "manha";
            
        } else {
            
            turno_name = "tarde";
            
        }
        
        field_name = "user_" + tipo_serv + "_" + turno_name;
        
        String user = (String) user_select.getSelectedItem();
        
        divisao_cidades_user dcu = new divisao_cidades_user(user, mn, field_name);
        
        jPanel1.removeAll();
        
        jPanel1.add(dcu);
        
        jPanel1.repaint();
        jPanel1.revalidate();
        
    }
    
    public class user_listener implements ItemListener {
        
        @Override
        public void itemStateChanged(ItemEvent ie) {
            
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                
                atualiza_individual();
                
            }
            
        }
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel label_select;
    private javax.swing.JComboBox turno;
    private javax.swing.JComboBox user_select;
    // End of variables declaration//GEN-END:variables
}
