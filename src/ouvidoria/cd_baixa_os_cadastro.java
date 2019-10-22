/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author ROBSMAC
 */
//public class dth_mr_cadastro extends javax.swing.JFrame {
public class cd_baixa_os_cadastro extends javax.swing.JDialog {

    Menu mn;
    JTable main_tab;

    /**
     * Creates new form dth_mr_cadastro
     */
    public cd_baixa_os_cadastro() {
        initComponents();
    }

    public cd_baixa_os_cadastro(Menu mn, boolean modal) {
        super(mn, modal);
        initComponents();

        this.mn = mn;
        //atualiza_painel();

        global.open_modal(this, "Cadastro de Códigos de Baixa de OS");

    }

    public void atualiza_painel() {

        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);

        jpnl_planilha.removeAll();
        jpnl_planilha.repaint();
        jpnl_planilha.revalidate();

        /*
         String query = "SELECT id as ID, codigo as Cód_OS, quebra as Quebra, cop as COP, 'N2' as nivel "
         + "from ouvidoria.cod_baixa_novo "
         + "where codigo like '%" + jtxt_localizar.getText().trim() + "%' "
         + "order by codigo;";
         */
        String query = "";
        query = query
                + "select id as ID, nome as Codigo, quebra as Quebra, cop as COP, nivel as Nivel, fl_ativo as Ativo \n"
                + "from \n"
                + "( \n"
                + "SELECT id as ID, codigo as nome, quebra as Quebra, cop as COP, 'N2' as nivel, 'Sim' as fl_ativo "
                + "from ouvidoria.cod_baixa_novo "
                + "where codigo like '%" + jtxt_localizar.getText().trim() + "%' \n"
                + "union all \n"
                + "select id as ID, nome as nome, 0 as Quebra, 'COP NET' as COP, 'N3' as nivel, case when fl_ativo=1 then 'Sim' else 'Não' end as fl_ativo "
                + "from ouvidoria.tb_acao_acomp_status "
                + "where nome like '%" + jtxt_localizar.getText().trim() + "%' \n"
                + ") a \n"
                + "order by codigo;";

        try {
            setCursor(hourglassCursor);
            JTable tab = global.getTable(query, jpnl_planilha);

            //int invisible_ids[] = {3, 4, 5, 6};
            int invisible_ids[] = {0, 2};
            int column_widths[] = {50, 200, 100, 150, 50, 50};

            global.hide_columns(invisible_ids, tab);
            global.adjust_columns(column_widths, tab);

            main_tab = tab;

            TableFilterHeader filter = new TableFilterHeader(main_tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);

            main_tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            main_tab.setDefaultEditor(Object.class, null); //bloqueando edicao de celulas
            jlbl_status_bar.setText(main_tab.getRowCount() + " registro(s) encontrado(s)");
            setCursor(normalCursor);

            main_tab.addKeyListener(new java.awt.event.KeyAdapter() {
                //public void keyPressed(java.awt.event.KeyEvent evt) {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    AtualizaOpcoes();
                }
            });

            main_tab.addMouseListener(new java.awt.event.MouseAdapter() {
                //public void mouseClicked(java.awt.event.MouseAdapter evt) {
                public void mouseClicked(MouseEvent e) {
                    AtualizaOpcoes();
                }
            });

        } catch (Exception ex) {
            setCursor(normalCursor);
            jlbl_status_bar.setText("Erro na última consulta");
            Logger.getLogger(div_turno.class.getName()).log(Level.SEVERE, null, ex);
            global.show_error_message("Problemas na consulta.\n\nErro original: " + ex.getMessage());
        }

    }

    private void AtualizaOpcoes() {
        //int linha = main_tab.rowAtPoint(e.getPoint());
        if ("N2".equals(main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 4).toString())) {
            jbt_excluir.setEnabled(true);
            jbt_ativar.setEnabled(false);
            jbt_desativar.setEnabled(false);
        } else {
            jbt_excluir.setEnabled(false);
            jbt_ativar.setEnabled(true);
            jbt_desativar.setEnabled(true);

            if ("Sim".equals(main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 5).toString())) {
                jbt_ativar.setEnabled(false);
            } else {
                jbt_desativar.setEnabled(false);
            }
        }

        jmni_servicos_excluir.setEnabled(jbt_excluir.isEnabled());
        jmni_servicos_ativar.setEnabled(jbt_ativar.isEnabled());
        jmni_servicos_desativar.setEnabled(jbt_desativar.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jbt_alterar = new javax.swing.JButton();
        jbt_incluir = new javax.swing.JButton();
        jbt_excluir = new javax.swing.JButton();
        jlbl_status_bar = new javax.swing.JLabel();
        jbt_incluir1 = new javax.swing.JButton();
        jbt_ativar = new javax.swing.JButton();
        jbt_desativar = new javax.swing.JButton();
        jpnl_busca = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jbt_localizar = new javax.swing.JButton();
        jtxt_localizar = new javax.swing.JTextField();
        jpnl_planilha = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmni_servicos_alterar = new javax.swing.JMenuItem();
        jmni_servicos_excluir = new javax.swing.JMenuItem();
        jmni_servicos_ativar = new javax.swing.JMenuItem();
        jmni_servicos_desativar = new javax.swing.JMenuItem();
        jmni_servicos_incluir_n2 = new javax.swing.JMenuItem();
        jmni_servicos_incluir_n3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jmni_servicos_fechar = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DTH - Cadastro de Microrregiões");
        setBackground(new java.awt.Color(255, 51, 204));

        jbt_alterar.setText("Alterar");
        jbt_alterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_alterarActionPerformed(evt);
            }
        });

        jbt_incluir.setText("Incluir para N2");
        jbt_incluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_incluirActionPerformed(evt);
            }
        });

        jbt_excluir.setText("Excluir");
        jbt_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_excluirActionPerformed(evt);
            }
        });

        jlbl_status_bar.setText("Aguardando comando");

        jbt_incluir1.setText("Incluir para N3");
        jbt_incluir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_incluir1ActionPerformed(evt);
            }
        });

        jbt_ativar.setText("Ativar");
        jbt_ativar.setEnabled(false);
        jbt_ativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_ativarActionPerformed(evt);
            }
        });

        jbt_desativar.setText("Desativar");
        jbt_desativar.setEnabled(false);
        jbt_desativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_desativarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jbt_alterar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbt_excluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbt_ativar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbt_desativar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbt_incluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbt_incluir1)
                .addGap(0, 223, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbl_status_bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbt_alterar)
                    .addComponent(jbt_incluir)
                    .addComponent(jbt_excluir)
                    .addComponent(jbt_incluir1)
                    .addComponent(jbt_ativar)
                    .addComponent(jbt_desativar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jlbl_status_bar))
        );

        jpnl_busca.setBackground(new java.awt.Color(255, 255, 204));
        jpnl_busca.setPreferredSize(new java.awt.Dimension(424, 23));

        jLabel1.setText("Código de baixa a localizar (parte ou todo)");

        jbt_localizar.setText("Localizar");
        jbt_localizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_localizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnl_buscaLayout = new javax.swing.GroupLayout(jpnl_busca);
        jpnl_busca.setLayout(jpnl_buscaLayout);
        jpnl_buscaLayout.setHorizontalGroup(
            jpnl_buscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_buscaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtxt_localizar, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbt_localizar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnl_buscaLayout.setVerticalGroup(
            jpnl_buscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnl_buscaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jbt_localizar)
                .addComponent(jtxt_localizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jpnl_planilha.setBackground(new java.awt.Color(204, 204, 255));
        jpnl_planilha.setToolTipText("");

        javax.swing.GroupLayout jpnl_planilhaLayout = new javax.swing.GroupLayout(jpnl_planilha);
        jpnl_planilha.setLayout(jpnl_planilhaLayout);
        jpnl_planilhaLayout.setHorizontalGroup(
            jpnl_planilhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpnl_planilhaLayout.setVerticalGroup(
            jpnl_planilhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 145, Short.MAX_VALUE)
        );

        jMenu1.setText("Serviços");

        jmni_servicos_alterar.setText("Alterar");
        jmni_servicos_alterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_servicos_alterarActionPerformed(evt);
            }
        });
        jMenu1.add(jmni_servicos_alterar);

        jmni_servicos_excluir.setText("Excluir");
        jmni_servicos_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_servicos_excluirActionPerformed(evt);
            }
        });
        jMenu1.add(jmni_servicos_excluir);

        jmni_servicos_ativar.setText("Ativar");
        jmni_servicos_ativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_servicos_ativarActionPerformed(evt);
            }
        });
        jMenu1.add(jmni_servicos_ativar);

        jmni_servicos_desativar.setText("Desativar");
        jmni_servicos_desativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_servicos_desativarActionPerformed(evt);
            }
        });
        jMenu1.add(jmni_servicos_desativar);

        jmni_servicos_incluir_n2.setText("Incluir para N2");
        jmni_servicos_incluir_n2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_servicos_incluir_n2ActionPerformed(evt);
            }
        });
        jMenu1.add(jmni_servicos_incluir_n2);

        jmni_servicos_incluir_n3.setText("Incluir para N3");
        jmni_servicos_incluir_n3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_servicos_incluir_n3ActionPerformed(evt);
            }
        });
        jMenu1.add(jmni_servicos_incluir_n3);
        jMenu1.add(jSeparator1);

        jmni_servicos_fechar.setText("Fechar janela");
        jmni_servicos_fechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_servicos_fecharActionPerformed(evt);
            }
        });
        jMenu1.add(jmni_servicos_fechar);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnl_busca, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpnl_planilha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpnl_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnl_planilha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmni_servicos_fecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_servicos_fecharActionPerformed
        // TODO add your handling code here:
        //global.show_message("Fechar janela");
        this.dispose();
    }//GEN-LAST:event_jmni_servicos_fecharActionPerformed

    private void jbt_localizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_localizarActionPerformed
        // TODO add your handling code here:
        atualiza_painel();
    }//GEN-LAST:event_jbt_localizarActionPerformed

    private void jmni_servicos_alterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_servicos_alterarActionPerformed
        // TODO add your handling code here:

        if (!testaEdicao()) {
            global.show_error_message("Nada selecionado");
        } else {
            if ("N2".equals(main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 4).toString())) {
                cd_baixa_os_cadastro_editar_n2 cad_baixa_os_editar = new cd_baixa_os_cadastro_editar_n2(
                        1,
                        Integer.parseInt(main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 0).toString()),
                        main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 1).toString(),
                        main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 2).toString(),
                        main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 3).toString()
                );
            } else {
                cd_baixa_os_cadastro_editar_n3 cad_baixa_os_editar = new cd_baixa_os_cadastro_editar_n3(
                        1,
                        Integer.parseInt(main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 0).toString()),
                        main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 1).toString()
                );
            }
        }

    }//GEN-LAST:event_jmni_servicos_alterarActionPerformed

    private boolean testaEdicao() {
        boolean bRetorno = true;

        if (main_tab == null) {
            bRetorno = false;
        } else {
            if (main_tab.getRowCount() < 1) {
                bRetorno = false;
            } else {
                if (main_tab.getSelectedRowCount() != 1) {
                    bRetorno = false;
                }
            }
        }

        return bRetorno;
    }

    private void jmni_servicos_incluir_n2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_servicos_incluir_n2ActionPerformed
        // TODO add your handling code here:

        cd_baixa_os_cadastro_editar_n2 cad_baixa_os_editar = new cd_baixa_os_cadastro_editar_n2(0, 0, "", "sim", "");

    }//GEN-LAST:event_jmni_servicos_incluir_n2ActionPerformed

    private void jbt_alterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_alterarActionPerformed
        // TODO add your handling code here:
        jmni_servicos_alterarActionPerformed(null);
    }//GEN-LAST:event_jbt_alterarActionPerformed

    private void jbt_incluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_incluirActionPerformed
        // TODO add your handling code here:
        jmni_servicos_incluir_n2ActionPerformed(null);
    }//GEN-LAST:event_jbt_incluirActionPerformed

    private void jmni_servicos_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_servicos_excluirActionPerformed
        // TODO add your handling code here:
        if (!testaEdicao()) {
            global.show_error_message("Nada selecionado");
        } else {
            cd_baixa_os_cadastro_editar_n2 cad_baixa_os_editar = new cd_baixa_os_cadastro_editar_n2(
                    2,
                    Integer.parseInt(main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 0).toString()),
                    main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 1).toString(),
                    main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 2).toString(),
                    main_tab.getModel().getValueAt(main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 3).toString()
            );
        }
    }//GEN-LAST:event_jmni_servicos_excluirActionPerformed

    private void jbt_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_excluirActionPerformed
        // TODO add your handling code here:
        jmni_servicos_excluirActionPerformed(null);
    }//GEN-LAST:event_jbt_excluirActionPerformed

    private void jbt_incluir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_incluir1ActionPerformed
        // TODO add your handling code here:
        jmni_servicos_incluir_n3ActionPerformed(null);
    }//GEN-LAST:event_jbt_incluir1ActionPerformed

    private void jmni_servicos_incluir_n3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_servicos_incluir_n3ActionPerformed
        // TODO add your handling code here:
        //cd_baixa_os_cadastro_editar_n3 cad_baixa_os_editar = new cd_baixa_os_cadastro_editar_n3_complemento(0, 0, "", "sim", "");
        cd_baixa_os_cadastro_editar_n3 cad_baixa_os_editar = new cd_baixa_os_cadastro_editar_n3(0, 0, "");
    }//GEN-LAST:event_jmni_servicos_incluir_n3ActionPerformed

    private void jmni_servicos_ativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_servicos_ativarActionPerformed
        // TODO add your handling code here:
        if (!testaEdicao()) {
            global.show_error_message("Nada selecionado");
        } else {
            BaixaN3_AtivaDesativa(Integer.parseInt(main_tab.getValueAt(main_tab.getSelectedRow(), 0).toString()), 1);
            main_tab.getModel().setValueAt("Sim", main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 5);
            AtualizaOpcoes();
        }
    }//GEN-LAST:event_jmni_servicos_ativarActionPerformed

    private void jbt_ativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_ativarActionPerformed
        // TODO add your handling code here:
        jmni_servicos_ativarActionPerformed(null);
    }//GEN-LAST:event_jbt_ativarActionPerformed

    private void jmni_servicos_desativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_servicos_desativarActionPerformed
        // TODO add your handling code here:
        if (!testaEdicao()) {
            global.show_error_message("Nada selecionado");
        } else {
            BaixaN3_AtivaDesativa(Integer.parseInt(main_tab.getValueAt(main_tab.getSelectedRow(), 0).toString()), 0);
            main_tab.getModel().setValueAt("Não", main_tab.convertRowIndexToModel(main_tab.getSelectedRow()), 5);
            AtualizaOpcoes();
        }
    }//GEN-LAST:event_jmni_servicos_desativarActionPerformed

    private void jbt_desativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_desativarActionPerformed
        // TODO add your handling code here:
        jmni_servicos_desativarActionPerformed(null);
    }//GEN-LAST:event_jbt_desativarActionPerformed

    private void BaixaN3_AtivaDesativa(int iId, int iStatus) {
        try {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            StringBuilder sSQL = new StringBuilder();
            Connection conn = Db_class.mysql_conn();

            sSQL.append("update ouvidoria.tb_acao_acomp_status ");
            sSQL.append("set ");
            sSQL.append("fl_ativo = ").append(iStatus).append(" ");
            sSQL.append("where id = ").append(iId).append("; ");

            Db_class.mysql_insert(sSQL.toString(), conn);
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
        } catch (Exception ex) {
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
            global.show_error_message("Problemas para gravar as informações\n\nErro: " + ex.getMessage());
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
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cd_baixa_os_cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cd_baixa_os_cadastro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JButton jbt_alterar;
    private javax.swing.JButton jbt_ativar;
    private javax.swing.JButton jbt_desativar;
    private javax.swing.JButton jbt_excluir;
    private javax.swing.JButton jbt_incluir;
    private javax.swing.JButton jbt_incluir1;
    private javax.swing.JButton jbt_localizar;
    private javax.swing.JLabel jlbl_status_bar;
    private javax.swing.JMenuItem jmni_servicos_alterar;
    private javax.swing.JMenuItem jmni_servicos_ativar;
    private javax.swing.JMenuItem jmni_servicos_desativar;
    private javax.swing.JMenuItem jmni_servicos_excluir;
    private javax.swing.JMenuItem jmni_servicos_fechar;
    private javax.swing.JMenuItem jmni_servicos_incluir_n2;
    private javax.swing.JMenuItem jmni_servicos_incluir_n3;
    private javax.swing.JPanel jpnl_busca;
    private javax.swing.JPanel jpnl_planilha;
    private javax.swing.JTextField jtxt_localizar;
    // End of variables declaration//GEN-END:variables
}
