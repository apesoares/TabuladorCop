/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;

/**
 *
 * @author N0026925
 */
public class consulta_tec extends javax.swing.JPanel {

    Menu mn;
    JTable main_tab;
    int current_row;
    consulta_tec ct;

    /**
     * Creates new form consulta_tec
     */
    public consulta_tec(Menu mn) {
        initComponents();

        this.mn = mn;

        box_cidade.addItem("Todas");

        global.fill_combo(box_cidade, "SELECT cluster_tec "
                + "FROM cidades_novo "
                + "WHERE cluster_tec is not null "
                + "GROUP BY cluster_tec "
                + "ORDER BY cluster_tec", false);

        box_cidade.addItemListener(new combo_tec_listener());

        atualiza_tec();

        ct = this;

    }

    public class combo_tec_listener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {

                atualiza_tec();

            }

        }

    }

    public void atualiza_tec() {

        jPanel2.removeAll();
        jPanel2.repaint();
        jPanel2.revalidate();

        String filtro_cidade = "";

        String cidade_selected = (String) box_cidade.getSelectedItem();

        if (!cidade_selected.equals("Todas")) {

            filtro_cidade = "WHERE (cidade = '" + cidade_selected + "' "
                    + "OR tecnicos.cluster = '" + cidade_selected + "') ";

        } else {

            filtro_cidade = "";

        }

        String query = "SELECT ifnull(tecnicos.cidade,'') as Cidade, "
                + "ifnull(area,'') as Area, "
                + "ifnull(login,'') as Login, "
                + "ifnull(nome,'') as Nome, "
                + "ifnull(parceira,'') as Parceira, "
                + "ifnull(telefone,'') as Telefone, "
                + "ifnull(turno_tec,'') as Turno, "
                + "ifnull(tipo_tec,'') as Tipo, "
                + "ifnull(tecnicos.obs,'') as Observação, "
                + "idtecnicos "
                + "FROM tecnicos "
                + "LEFT JOIN cidades_novo cd ON tecnicos.cluster = cd.cluster_tec "
                + filtro_cidade + "  "
                + "GROUP BY login, nome, cidade, area "
                + "ORDER BY cidade, parceira, nome";
        try {
            JTable tab_tec = global.getTable(query, jPanel2);

            tab_tec.addMouseListener(new tab_tec_listener());

            main_tab = tab_tec;

            TableFilterHeader filter = new TableFilterHeader(main_tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);

            int[] invisible_ids = {9};

            global.hide_columns(invisible_ids, tab_tec);

        } catch (Exception ex) {
            Logger.getLogger(consulta_tec.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public class tab_tec_listener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e)) {

                JPopupMenu pop = new JPopupMenu();
                JMenuItem edit = new JMenuItem("Editar Técnico");
                edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pencil.png")));
                edit.addActionListener(new edit_action());

                pop.add(edit);

                JMenuItem delete = new JMenuItem("Excluir Técnico");
                delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png")));
                delete.addActionListener(new delete_action());
                pop.add(delete);

                pop.setSize(300, 200);
                pop.show(e.getComponent(), e.getX(), e.getY());

                current_row = main_tab.rowAtPoint(e.getPoint());

                main_tab.setRowSelectionInterval(current_row, current_row);

            }

        }

    }

    public class edit_action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            String cidade = (String) main_tab.getValueAt(current_row, 0);
            String area = (String) main_tab.getValueAt(current_row, 1);
            String login = (String) main_tab.getValueAt(current_row, 2);
            String nome = (String) main_tab.getValueAt(current_row, 3);
            String parceira = (String) main_tab.getValueAt(current_row, 4);
            String telefone = (String) main_tab.getValueAt(current_row, 5);
            String turno = (String) main_tab.getValueAt(current_row, 6);
            String tipo = (String) main_tab.getValueAt(current_row, 7);
            String obs = (String) main_tab.getValueAt(current_row, 8);
            int id = (int) main_tab.getValueAt(current_row, 9);

            String[] array_tec = {cidade, area, login, nome, parceira, telefone, turno, tipo, obs};

            edit_tecnico et = new edit_tecnico(mn, array_tec, id, ct);

        }

    }

    public class delete_action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            int resposta = global.dialog_question("Tem certeza que deseja excluir esse técnico?\n\n"
                    + "Essa alteração não poderá ser desfeita.");

            if (resposta != 0) {

                return;

            }

            try {
                Connection conn = Db_class.mysql_conn();

                int id = (int) main_tab.getValueAt(current_row, 9);

                String query = "DELETE FROM tecnicos WHERE idtecnicos = " + id;

                Db_class.mysql_insert(query, conn);

                Db_class.close_conn(conn);

                global.show_message("Técnico excluído com sucesso!");

                ct.atualiza_tec();

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(consulta_tec.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        box_cidade = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new java.awt.BorderLayout());
        add(jPanel2, java.awt.BorderLayout.CENTER);

        jToolBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setRollover(true);

        jLabel1.setText("Selecione a cidade   ");
        jToolBar1.add(jLabel1);

        box_cidade.setLightWeightPopupEnabled(false);
        box_cidade.setMaximumSize(new java.awt.Dimension(200, 30));
        box_cidade.setPreferredSize(new java.awt.Dimension(200, 30));
        jToolBar1.add(box_cidade);
        jToolBar1.add(jSeparator1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_refresh.png"))); // NOI18N
        jButton2.setText("Atualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jToolBar1.add(jSeparator2);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        atualiza_tec();

    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox box_cidade;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
