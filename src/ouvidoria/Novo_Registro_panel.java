/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author N0026925
 */
public class Novo_Registro_panel extends javax.swing.JPanel {

    Menu mn;
    boolean mail_attached = false;
    private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    String path_mail;
    String last_obs;
    String cop;
    String cop_simples; //removendo a parte COP, deixando apenas o que interessa
    Novo_Registro_panel nr;

    public Novo_Registro_panel() {
        initComponents();
    }

    public Novo_Registro_panel(Menu mn, String cop_rec) {

        initComponents();

        //contruct_layout();
        this.mn = mn;
        this.cop = cop_rec;
        this.nr = this;

        this.cop_simples = this.cop.replaceAll("COP ", "");

        tab_os.getColumnModel().getColumn(0).setPreferredWidth(120);
        tab_os.getColumnModel().getColumn(1).setPreferredWidth(500);

        tab_os.getTableHeader().setReorderingAllowed(false);

        global.fill_combo(cidade, "SELECT ci_depara "
                + "FROM cidades_novo "
                + "WHERE cop = '" + cop_rec + "' "
                + "GROUP BY ci_depara "
                + "ORDER BY ci_depara", true);

        cidade.addItem("Outras cidades");

        global.fill_combo(janela, "SELECT nome_janela "
                + "FROM janelas "
                + "WHERE cop = '" + cop_rec + "' "
                + "GROUP BY nome_janela "
                + "ORDER BY nome_janela", true);

        cidade.addItemListener(new cidade_listener());

        global.fill_combo(tipo_os, "SELECT tipo_os "
                + "FROM tipo_os_novo "
                + "WHERE cop = '" + cop_rec + "' "
                + "GROUP BY tipo_os "
                + "ORDER BY tipo_os ", true);

        global.fill_combo(tipo_trat, "SELECT tipo_trat "
                + "FROM tipo_trat "
                + "GROUP BY tipo_trat "
                + "ORDER BY tipo_trat", true);

        global.fill_combo(canal, "SELECT canal "
                + "FROM canal "
                + "GROUP BY canal "
                + "ORDER BY canal", true);

        data.setDate(new Date());

        contrato.addKeyListener(new key_contrato());

        tipo_trat.addItemListener(new indevido_listener());

        jPanel1.setPreferredSize(new Dimension(700, 800));

    }

    class cidade_listener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {

                if (cidade.getSelectedItem().equals("Outras cidades")) {

                    area.removeAllItems();
                    area.addItem("Sem area");
                    area.setSelectedIndex(0);

                } else {

                    try {
                        String nome_cidade = (String) cidade.getSelectedItem();

                        global.fill_combo(area, "SELECT area "
                                + "FROM areas_novo "
                                + "WHERE cidade = '" + nome_cidade + "' "
                                + "GROUP BY cidade, area "
                                + "ORDER BY area", true);

                        String query_cidade = "SELECT ifnull(obs,'') "
                                + "FROM cidades_novo "
                                + "WHERE ci_depara = '" + nome_cidade + "'";

                        Connection conn = Db_class.mysql_conn();

                        if (area.getModel().getSize() == 2) {

                            area.setSelectedIndex(1);

                        }

                        ResultSet rs = Db_class.mysql_result(conn, query_cidade);

                        rs.next();

                        obs_cidade.setText(rs.getString(1));

                        obs_cidade.setCaretPosition(0);

                        Db_class.close_conn(conn);

                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                        Logger.getLogger(Novo_Registro_panel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

        }
    }

    class key_contrato implements KeyListener {

        @Override
        public void keyTyped(KeyEvent evt) {
            //KeyTyped é para Caracteres Imprimíveis

            if ((evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && (evt.getKeyCode() != KeyEvent.VK_ENTER))) {
                if (!Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                    evt.setKeyCode(0);
                    return;
                }

                if (contrato.getText().length() >= global.iMaxContrato) {
                    evt.consume();
                    evt.setKeyCode(0);
                    return;
                }
            }

        }

        @Override
        public void keyPressed(KeyEvent evt) {
            //KeyPressed é para Caracteres Não-Imprimíveis

            if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) {
                String sTexto = "";
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
                DataFlavor dataFlavor = DataFlavor.stringFlavor;

                if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
                    try {
                        sTexto = (String) systemClipboard.getData(dataFlavor);
                        sTexto = sTexto.trim();
                        if (sTexto.length() <= global.iMaxContrato) {
                            sTexto = sTexto.substring(0, sTexto.length());
                            for (int i = 0; i < sTexto.length(); i++) {
                                if (Integer.parseInt(sTexto.substring(i, i + 1)) >= 0) {

                                }
                            }
                        } else {
                            sTexto = "";
                            global.show_error_message("O que era para ser colado possui mais de " + global.iMaxContrato + " caracteres");
                        }
                    } catch (Exception e) {
                        sTexto = "";
                        global.show_error_message("O que era para ser colado não é um número");
                    }
                }
                if ("".equals(sTexto)) {
                    contrato.setTransferHandler(null);
                } else {
                    contrato.setTransferHandler(null);
                    contrato.setText(sTexto);
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent ke) {

        }

    }

    class indevido_listener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {

            if (ie.getStateChange() == ItemEvent.SELECTED) {
                // -----------------------------------------------------------------
                // -- SOLIC 87804 - inicio
                // -----------------------------------------------------------------
                /*
                 if (tipo_trat.getSelectedItem().equals("OMS Indevido")) {
                 descr_indev.setEnabled(true);
                 global.fill_combo(descr_indev, "SELECT descr FROM indevidos ORDER BY descr", true);
                 } else if (tipo_trat.getSelectedItem().equals("Acompanhamento")) {
                 descr_indev.removeAllItems();
                 descr_indev.addItem("");
                 descr_indev.addItem("Roteamento Na Fila Errada");
                 descr_indev.setEnabled(true);
                 } else {
                 descr_indev.setEnabled(false);
                 descr_indev.removeAllItems();
                 }
                 */
                descr_indev.removeAllItems();
                if (!tipo_trat.getSelectedItem().equals("")) {
                    descr_indev.setEnabled(true);
                    global.fill_combo(
                            descr_indev,
                            "SELECT descr FROM indevidos "
                            + "where id in ("
                            + "select id_indevido from tb_tipotratxindevidos where "
                            + "id_tipo_trat = ("
                            + "select id_tipo from tipo_trat where tipo_trat = '" + tipo_trat.getSelectedItem() + "'"
                            + ")"
                            + ") "
                            + "and cop in ('TODOS', '" + cop_simples + "') "
                            + "ORDER BY descr",
                            true
                    );

                    if (descr_indev.getItemCount() == 2) {
                        descr_indev.setSelectedIndex(1);
                    }
                } else {
                    descr_indev.setEnabled(false);
                }
                // -----------------------------------------------------------------
                // -- SOLIC 87804 - final
                // -----------------------------------------------------------------
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        painel_dados = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        contrato = new javax.swing.JTextField();
        solic = new javax.swing.JTextField();
        obs_horario = new javax.swing.JTextField();
        nome_cli = new javax.swing.JTextField();
        cidade = new javax.swing.JComboBox<String>();
        area = new javax.swing.JComboBox<String>();
        canal = new javax.swing.JComboBox<String>();
        tipo_trat = new javax.swing.JComboBox<String>();
        janela = new javax.swing.JComboBox<String>();
        descr_indev = new javax.swing.JComboBox<String>();
        data = new org.jdesktop.swingx.JXDatePicker();
        painel_os = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tab_os = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        num_os = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tipo_os = new javax.swing.JComboBox();
        botao_add = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        sim = new javax.swing.JRadioButton();
        sem_sucesso = new javax.swing.JRadioButton();
        com_sucesso = new javax.swing.JRadioButton();
        jLabel15 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        botao_mail = new javax.swing.JButton();
        nao = new javax.swing.JRadioButton();
        botao_script = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        encaixe_sim = new javax.swing.JRadioButton();
        encaixe_nao = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obs = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        obs_cidade = new javax.swing.JTextArea();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.BorderLayout());

        painel_dados.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Contrato");

        jLabel5.setText("Cidade");

        jLabel6.setText("Area");

        jLabel7.setText("Canal");

        jLabel8.setText("Data");

        jLabel9.setText("Janela");

        jLabel10.setText("Solicitante");

        jLabel11.setText("Nome Cli");

        jLabel12.setText("Tipo Trat");

        jLabel13.setText("Descr");

        jLabel16.setText("Partic");

        cidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cidadeActionPerformed(evt);
            }
        });

        descr_indev.setMaximumRowCount(30);

        javax.swing.GroupLayout painel_dadosLayout = new javax.swing.GroupLayout(painel_dados);
        painel_dados.setLayout(painel_dadosLayout);
        painel_dadosLayout.setHorizontalGroup(
            painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painel_dadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painel_dadosLayout.createSequentialGroup()
                        .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(painel_dadosLayout.createSequentialGroup()
                                .addComponent(contrato, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cidade, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(painel_dadosLayout.createSequentialGroup()
                                .addComponent(area, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(canal, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painel_dadosLayout.createSequentialGroup()
                                .addComponent(data, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(janela, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painel_dadosLayout.createSequentialGroup()
                                .addComponent(solic)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nome_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(obs_horario, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tipo_trat, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(painel_dadosLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(27, 27, 27)
                        .addComponent(descr_indev, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        painel_dadosLayout.setVerticalGroup(
            painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painel_dadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(contrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(canal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(janela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(solic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nome_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12)
                    .addComponent(tipo_trat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descr_indev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(painel_dadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(obs_horario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        painel_os.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "OS's deste contrato"));
        painel_os.setPreferredSize(new java.awt.Dimension(466, 106));
        painel_os.setLayout(new java.awt.BorderLayout());

        tab_os.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero da OS", "Tipo de OS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tab_os.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tab_osMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tab_os);

        painel_os.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(506, 82));

        jLabel1.setText("Num OS");

        num_os.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                num_osKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                num_osKeyTyped(evt);
            }
        });

        jLabel2.setText("Tipo OS");

        tipo_os.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        botao_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        botao_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tipo_os, 0, 459, Short.MAX_VALUE)
                    .addComponent(num_os, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botao_add)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(num_os))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botao_add)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tipo_os, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(23, 23, 23))
        );

        painel_os.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setText("Ativo com cliente");

        buttonGroup1.add(sim);
        sim.setText("Sim");
        sim.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                simItemStateChanged(evt);
            }
        });

        buttonGroup2.add(sem_sucesso);
        sem_sucesso.setSelected(true);
        sem_sucesso.setText("Sem Sucesso");
        sem_sucesso.setEnabled(false);

        buttonGroup2.add(com_sucesso);
        com_sucesso.setText("Com Sucesso");
        com_sucesso.setEnabled(false);

        jLabel15.setText("Tentativa de ativo");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.png"))); // NOI18N
        jButton2.setText("Limpar Campos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png"))); // NOI18N
        jButton1.setText("Inserir Registro");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        botao_mail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/attach.png"))); // NOI18N
        botao_mail.setText("Anexar E-mail");
        botao_mail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botao_mailMouseClicked(evt);
            }
        });
        botao_mail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_mailActionPerformed(evt);
            }
        });

        buttonGroup1.add(nao);
        nao.setSelected(true);
        nao.setText("Não");

        botao_script.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/paste_plain.png"))); // NOI18N
        botao_script.setText("Copiar Script");
        botao_script.setToolTipText("");
        botao_script.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botao_scriptMouseClicked(evt);
            }
        });

        jLabel3.setText("Encaixe");

        buttonGroup3.add(encaixe_sim);
        encaixe_sim.setText("Sim");
        encaixe_sim.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                encaixe_simItemStateChanged(evt);
            }
        });

        buttonGroup3.add(encaixe_nao);
        encaixe_nao.setSelected(true);
        encaixe_nao.setText("Não");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(botao_mail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addComponent(botao_script)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(encaixe_sim)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(encaixe_nao)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(sim)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(com_sucesso)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sem_sucesso)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(sim)
                    .addComponent(nao)
                    .addComponent(sem_sucesso)
                    .addComponent(jLabel15)
                    .addComponent(com_sucesso))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(encaixe_sim)
                            .addComponent(encaixe_nao))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botao_mail)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(botao_script))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Observações"));

        obs.setColumns(20);
        obs.setLineWrap(true);
        obs.setRows(5);
        obs.setWrapStyleWord(true);
        jScrollPane1.setViewportView(obs);

        obs_cidade.setEditable(false);
        obs_cidade.setColumns(20);
        obs_cidade.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        obs_cidade.setForeground(new java.awt.Color(255, 51, 51));
        obs_cidade.setLineWrap(true);
        obs_cidade.setRows(2);
        obs_cidade.setWrapStyleWord(true);
        jScrollPane2.setViewportView(obs_cidade);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(painel_dados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painel_os, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painel_dados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painel_os, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel5);

        jPanel1.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(730, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tab_osMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab_osMouseClicked

        if (SwingUtilities.isRightMouseButton(evt)) {

            JPopupMenu menu = new JPopupMenu();
            JMenuItem item = new JMenuItem("Remover OS");

            final int row = tab_os.rowAtPoint(evt.getPoint());

            tab_os.setRowSelectionInterval(row, row);

            item.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/delete.png")));;

            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {

                    int result = global.dialog_question("Tem certeza que deseja remover esta OS?\n"
                            + "Esta alteração não poderá ser desfeita");

                    if (result != 0) {

                        return;

                    }

                    DefaultTableModel model = (DefaultTableModel) tab_os.getModel();
                    model.removeRow(row);

                }

            });

            menu.add(item);

            menu.show(evt.getComponent(), evt.getX(), evt.getY());

        }
    }//GEN-LAST:event_tab_osMouseClicked

    private void botao_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_addActionPerformed

        if (num_os.getText().equals("") || tipo_os.getSelectedItem().equals("")) {
            global.show_error_message("É necessário preencher o número e o tipo de OS!");
            return;
        }

        //testando o número da OS
        if (num_os.getText().length() > global.iMaxNumOS) {
            global.show_error_message("Número da OS não pode possuir mais de " + global.iMaxNumOS + " caracteres");
            return;
        }
        try {
            Long lNumOS = Long.parseLong(num_os.getText());
        } catch (Exception e) {
            global.show_error_message("Número da OS inválido");
            return;
        }
        //
        
        DefaultTableModel model = (DefaultTableModel) tab_os.getModel();
        model.addRow(new Object[]{num_os.getText(), tipo_os.getSelectedItem()});

        num_os.setText("");
        tipo_os.setSelectedItem("");

        num_os.requestFocus();
    }//GEN-LAST:event_botao_addActionPerformed

    private void simItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_simItemStateChanged

        if (sim.isSelected()) {

            com_sucesso.setEnabled(true);
            sem_sucesso.setEnabled(true);

        } else {

            sem_sucesso.setSelected(true);
            com_sucesso.setEnabled(false);
            sem_sucesso.setEnabled(false);

        }
    }//GEN-LAST:event_simItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        reset_form();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        insere_registro();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void botao_mailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botao_mailMouseClicked

        if (mail_attached) {

            JPopupMenu pop = new JPopupMenu();

            JMenuItem menu_remove = new JMenuItem("Remover anexo");
            menu_remove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png")));

            menu_remove.addActionListener(new remove_anexo());

            pop.add(menu_remove);

            pop.add(new JSeparator());

            JMenuItem menu_subs = new JMenuItem("Substituir anexo");
            menu_subs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_refresh.png")));

            menu_subs.addActionListener(new substituir_anexo());

            pop.add(menu_subs);

            pop.show(evt.getComponent(), evt.getX(), evt.getY());
            pop.setSize(300, 200);

        } else {

            attach_mail am = new attach_mail(this, cop, 0);

        }

    }//GEN-LAST:event_botao_mailMouseClicked

    public class remove_anexo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            remove_file_attach();
            global.show_message("Anexo removido com sucesso!");

        }

    }

    public class substituir_anexo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            attach_mail am = new attach_mail(nr, cop, 0);

        }

    }

    private void botao_mailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_mailActionPerformed

    }//GEN-LAST:event_botao_mailActionPerformed

    private void botao_scriptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botao_scriptMouseClicked

        JPopupMenu pop = new JPopupMenu();

        JMenuItem atual = new JMenuItem("Copiar script da tabulação atual");

        atual.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                copy_obs();

            }
        });

        JMenuItem antigo = new JMenuItem("Copiar script da tabulação anterior");

        antigo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                copy_last_obs();

            }
        });

        pop.add(atual);
        pop.add(antigo);

        pop.show(evt.getComponent(), evt.getX(), evt.getY());
        pop.setSize(300, 200);

    }//GEN-LAST:event_botao_scriptMouseClicked

    private void num_osKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_num_osKeyTyped
        //KeyTyped é para Caracteres Imprimíveis

        if ((evt.getKeyCode() != KeyEvent.VK_BACK_SPACE && (evt.getKeyCode() != KeyEvent.VK_ENTER))) {
            if (!Character.isDigit(evt.getKeyChar())) {
                evt.consume();
                evt.setKeyCode(0);
                return;
            }

            if (num_os.getText().length() >= global.iMaxNumOS) {
                evt.consume();
                evt.setKeyCode(0);
                return;
            }
        }

    }//GEN-LAST:event_num_osKeyTyped

    private void encaixe_simItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_encaixe_simItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_encaixe_simItemStateChanged

    private void cidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cidadeActionPerformed

    private void num_osKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_num_osKeyPressed
        //KeyPressed é para Caracteres Não-Imprimíveis

        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) {
            String sTexto = "";
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
            DataFlavor dataFlavor = DataFlavor.stringFlavor;

            if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
                try {
                    sTexto = (String) systemClipboard.getData(dataFlavor);
                    sTexto = sTexto.trim();
                    if (sTexto.length() <= global.iMaxNumOS) {
                        sTexto = sTexto.substring(0, sTexto.length());
                        for (int i = 0; i < sTexto.length(); i++) {
                            if (Integer.parseInt(sTexto.substring(i, i + 1)) >= 0) {

                            }
                        }
                    } else {
                        sTexto = "";
                        global.show_error_message("O que era para ser colado possui mais de " + global.iMaxNumOS + " caracteres");
                    }
                } catch (Exception e) {
                    sTexto = "";
                    global.show_error_message("O que era para ser colado não é um número");
                }
            }
            if ("".equals(sTexto)) {
                num_os.setTransferHandler(null);
            } else {
                num_os.setTransferHandler(null);
                num_os.setText(sTexto);
            }
        }

    }//GEN-LAST:event_num_osKeyPressed

    public void set_file_attach(String path) {

        path_mail = path;
        mail_attached = true;
        botao_mail.setForeground(Color.red);
        botao_mail.setToolTipText("Arquivo anexado: " + path);
        this.repaint();
        this.revalidate();
        global.show_message("Anexo incluído com sucesso!");

    }

    public void remove_file_attach() {

        mail_attached = false;
        botao_mail.setForeground(Color.BLACK);
        botao_mail.setToolTipText(null);
        this.repaint();
        this.revalidate();

    }

    public void reset_form() {

        canal.setSelectedIndex(0);
        cidade.setSelectedIndex(0);
        contrato.setText("");
        data.setDate(new Date());
        janela.setSelectedIndex(0);
        nome_cli.setText("");
        obs_horario.setText("");
        solic.setText("");
        tipo_trat.setSelectedIndex(0);
        num_os.setText("");
        tipo_os.setSelectedIndex(0);
        obs.setText("");
        obs_cidade.setText("");
        nao.setSelected(true);
        encaixe_nao.setSelected(true);

        remove_file_attach();

        DefaultTableModel model = (DefaultTableModel) tab_os.getModel();

        int rows = model.getRowCount();

        for (int i = 0; i <= rows - 1; i++) {
            model.removeRow(0);
        }

        nao.setSelected(true);

    }

    public boolean check_fields() {

        //testando se a data e anterior ao dia atual
        Calendar cal_dt_hoje = Calendar.getInstance();
        SimpleDateFormat sdf_formato = new SimpleDateFormat("yyyy-MM-dd");
        String s_dt_hoje = sdf_formato.format(cal_dt_hoje.getTime()).replaceAll("-", "");
        String s_dt_escolhida = global.get_simple_date(data.getDate()).replaceAll("-", "");
        if (Integer.parseInt(s_dt_escolhida) < Integer.parseInt(s_dt_hoje)) {
            global.show_error_message("A data selecionada não pode ser anterior ao dia de hoje");
            return false;
        }
        //
        
        //testando o número do contrato
        if (contrato.getText().length() > global.iMaxContrato) {
            global.show_error_message("Número do contrato não pode possuir mais de " + global.iMaxContrato + " caracteres");
            return false;
        }
        try {
            Long lContrato = Long.parseLong(contrato.getText());
        } catch (Exception e) {
            global.show_error_message("Número do contrato inválido");
            return false;
        }
        //

        // -----------------------------------------------------------------
        // -- SOLIC 87804 - inicio
        // -----------------------------------------------------------------
        Component[] cps = painel_dados.getComponents();
        for (Component cp : cps) {
            /*
             if (!cp.equals(descr_indev) && !cp.equals(obs_horario) && !cp.equals(obs_cidade)) {
             boolean status = global.check_cp(cp);
             if (!status) {
             global.show_error_message("É necessário preencher todos os campos!");
             cp.requestFocus();
             return false;
             }
             }
             if (cp.equals(descr_indev) && tipo_trat.getSelectedItem().equals("OMS Indevido")) {
             if (descr_indev.getSelectedItem().equals("")) {
             global.show_error_message("É necessário preencher todos os campos!");
             cp.requestFocus();
             return false;
             }
             }
             */
            if (!cp.equals(obs_horario) && !cp.equals(obs_cidade)) {
                boolean status = global.check_cp(cp);
                if (!status) {
                    global.show_error_message("Com exceção dos campos 'Partic' e o segundo de 'Observações,\ntodos são obrigatórios");
                    cp.requestFocus();
                    return false;
                }
            }
        }

        if (tab_os.getRowCount() == 0) {
            global.show_error_message("Informe ao menos uma OS");
            num_os.requestFocus();
            return false;
        }

        if ("".equals(obs.getText().trim())) {
            global.show_error_message("Informe ao menos a primeira observação");
            obs.requestFocus();
            return false;
        }

        // -----------------------------------------------------------------
        // -- SOLIC 87804 - final
        // -----------------------------------------------------------------
        return true;

    }

    public void insere_registro() {

        if (!mn.check_version()) {

            return;

        }

        if (!check_fields()) {

            return;

        }

        try {

            Connection conn = Db_class.mysql_conn();

            String i_contrato = contrato.getText();
            String i_cidade = (String) cidade.getSelectedItem();
            String i_area = (String) area.getSelectedItem();
            String i_data = global.get_simple_date(data.getDate());
            String i_janela = (String) janela.getSelectedItem();
            String i_tipo_os = (String) tab_os.getValueAt(0, 1);
            String i_solic = solic.getText();
            String i_nome_cli = nome_cli.getText();
            String i_tipo_trat = (String) tipo_trat.getSelectedItem();
            String i_descr_indevido = (String) descr_indev.getSelectedItem();
            String i_canal = (String) canal.getSelectedItem();
            String i_obs_hora = obs_horario.getText();
            String i_obs = mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                    + "Inserção do caso, agendamento para " + global.get_br_date(data.getDate())
                    + " janela das " + i_janela
                    + ".\n\n" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": " + obs.getText();
            String final_i_obs = i_obs.replace("'", "");
            String login = mn.get_login();
            String nome_user = mn.get_nome();
            String hora_insert = global.get_simple_datetime(new Date());
            String cop = this.cop;
            String ativo = "";
            String tentativa = "";
            String status = "";
            String os = (String) tab_os.getValueAt(0, 0);
            String main_os = "Sim";
            String is_encaixe = "";

            String mail_attach = "Não";

            if (mail_attached) {

                mail_attach = "Sim";

            }

            String query_grupo = "SELECT grp_descricao FROM tipo_os_novo WHERE "
                    + "tipo_os = '" + i_tipo_os + "'";

            ResultSet rs_grupo = Db_class.mysql_result(conn, query_grupo);

            rs_grupo.next();
            String i_grupo_os = rs_grupo.getString(1);

            String query_cidade = "SELECT sistema, cluster FROM cidades_novo WHERE "
                    + "ci_depara = '" + i_cidade + "'";

            ResultSet rs_cidade = Db_class.mysql_result(conn, query_cidade);

            rs_cidade.next();
            String i_sistema = rs_cidade.getString(1);
            String i_cluster = rs_cidade.getString(2);

            String hoje = global.get_simple_date(new Date());

            // -----------------------------------------------------------------
            // -- SOLIC 87804 - inicio
            // -----------------------------------------------------------------
            /*
             if (this.cop.equals("COP NET")) {
             status = "Encaixe";
             } else {
             status = "Em Acompanhamento";
             }
             */
            String query_status = "";
            query_status = query_status + "select status from tb_tipotratxindevidos ";
            query_status = query_status + "where id_tipo_trat = (select id_tipo from tipo_trat where tipo_trat = '" + (String) tipo_trat.getSelectedItem() + "' limit 1) ";
            query_status = query_status + "and id_indevido = (select id from indevidos where descr = '" + (String) descr_indev.getSelectedItem() + "' limit 1) ";

            ResultSet rs_status = Db_class.mysql_result(conn, query_status);

            if (rs_status.next()) {
                status = rs_status.getString(1);
            } else {
                status = "Em Acompanhamento";
            }

            // -----------------------------------------------------------------
            // -- SOLIC 87804 - final
            // -----------------------------------------------------------------
            if (sim.isSelected()) {

                ativo = "Sim";

            } else {

                ativo = "Não";

            }

            if (com_sucesso.isSelected()) {

                tentativa = "Com Sucesso";

            } else {

                tentativa = "Sem Sucesso";

            }

            if (encaixe_sim.isSelected()) {

                is_encaixe = "Sim";

            } else {

                is_encaixe = "Não";

            }

            String query = "INSERT INTO painel "
                    + "(contrato, "
                    + "cidade, "
                    + "area_despacho, "
                    + "data_acomp,"
                    + "janela, "
                    + "tipo_os, "
                    + "solicitante, "
                    + "nome_cli, "
                    + "tipo_trat, "
                    + "descr_indevido, "
                    + "canal,"
                    + "obs_horario, "
                    + "obs, "
                    + "user_insert, "
                    + "nome_insert, "
                    + "date_time_insert, "
                    + "cop, "
                    + "ativo, "
                    + "tentativa, "
                    + "status, "
                    + "ultimo_acomp, "
                    + "hora_ultimo_acomp, "
                    + "grupo_os, "
                    + "cluster, "
                    + "sistema, "
                    + "mail_attach, "
                    + "num_os, "
                    + "os_principal, "
                    + "encaixe, "
                    + "ultimo_acomp_login) "
                    + "VALUES('" + i_contrato + "',"
                    + "'" + i_cidade + "',"
                    + "'" + i_area + "',"
                    + "'" + i_data + "',"
                    + "'" + i_janela + "',"
                    + "'" + i_tipo_os + "',"
                    + "'" + i_solic + "',"
                    + "'" + i_nome_cli + "',"
                    + "'" + i_tipo_trat + "',"
                    + "'" + i_descr_indevido + "',"
                    + "'" + i_canal + "',"
                    + "'" + i_obs_hora + "',"
                    + "'" + final_i_obs + "',"
                    + "'" + login + "',"
                    + "'" + nome_user + "',"
                    + "'" + hora_insert + "', "
                    + "'" + cop + "',"
                    + "'" + ativo + "',"
                    + "'" + tentativa + "', "
                    + "'" + status + "', "
                    + "'" + nome_user + "', "
                    + "current_timestamp, "
                    + "'" + i_grupo_os + "',"
                    + "'" + i_cluster + "', "
                    + "'" + i_sistema + "', "
                    + "'" + mail_attach + "', "
                    + "'" + os + "',"
                    + "'" + main_os + "', "
                    + "'" + is_encaixe + "', "
                    + "'" + login + "')";

            Db_class.mysql_insert(query, conn);

            String query_last_id = "SELECT last_insert_id() FROM painel";

            ResultSet rs_last_id = Db_class.mysql_result(conn, query_last_id);

            rs_last_id.next();

            int last_id = rs_last_id.getInt(1);

            query = "UPDATE painel SET id_mestre = id "
                    + "WHERE id = " + last_id;

            Db_class.mysql_insert(query, conn);

            int linhas_os = tab_os.getRowCount();

            if (linhas_os > 1) {

                for (int i = 1; i <= (linhas_os - 1); i++) {

                    String current_os = (String) tab_os.getValueAt(i, 0);
                    String current_tipo = (String) tab_os.getValueAt(i, 1);
                    main_os = "Não";

                    query = "INSERT INTO painel "
                            + "(contrato, "
                            + "cidade, "
                            + "area_despacho, "
                            + "data_acomp,"
                            + "janela, "
                            + "tipo_os, "
                            + "solicitante, "
                            + "nome_cli, "
                            + "tipo_trat, "
                            + "descr_indevido, "
                            + "canal,"
                            + "obs_horario, "
                            + "obs, "
                            + "user_insert, "
                            + "nome_insert, "
                            + "date_time_insert, "
                            + "cop, "
                            + "ativo, "
                            + "tentativa, "
                            + "status, "
                            + "ultimo_acomp, "
                            + "hora_ultimo_acomp, "
                            + "grupo_os, "
                            + "cluster, "
                            + "sistema, "
                            + "mail_attach, "
                            + "num_os, "
                            + "id_mestre, "
                            + "os_principal, "
                            + "encaixe, "
                            + "ultimo_acomp_login) "
                            + "VALUES"
                            + "('" + i_contrato + "',"
                            + "'" + i_cidade + "',"
                            + "'" + i_area + "',"
                            + "'" + i_data + "',"
                            + "'" + i_janela + "',"
                            + "'" + current_tipo + "',"
                            + "'" + i_solic + "',"
                            + "'" + i_nome_cli + "',"
                            + "'" + i_tipo_trat + "',"
                            + "'" + i_descr_indevido + "',"
                            + "'" + i_canal + "',"
                            + "'" + i_obs_hora + "',"
                            + "'" + final_i_obs + "',"
                            + "'" + login + "',"
                            + "'" + nome_user + "',"
                            + "'" + hora_insert + "', "
                            + "'" + cop + "',"
                            + "'" + ativo + "',"
                            + "'" + tentativa + "', "
                            + "'" + status + "', "
                            + "'" + nome_user + "', "
                            + "current_timestamp, "
                            + "'" + i_grupo_os + "',"
                            + "'" + i_cluster + "', "
                            + "'" + i_sistema + "', "
                            + "'" + mail_attach + "', "
                            + "'" + current_os + "',"
                            + "" + last_id + ", "
                            + "'" + main_os + "', "
                            + "'" + is_encaixe + "', "
                            + "'" + login + "')";

                    Db_class.mysql_insert(query, conn);

                }
            }

            global.insert_prod(last_id, "Inserção OMS", mn);

            CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
            };

            if (mail_attached) {

                File fl = new File(path_mail);

                Path source = Paths.get(fl.getPath());

                //ALTERAR_BCC_OK
                Path dest = Paths.get("\\\\10.5.9.180\\GRVCOPCC$\\NET\\Ouvidoria - " + last_id + ".msg");
                try {
                    java.nio.file.Files.copy(source, dest, options);
                } catch (IOException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            String nome_net = "";

            String copia = "********* COP OUVIDORIA **********\n"
                    + "Contrato: " + contrato.getText() + "\n"
                    + "Cidade: " + cidade.getSelectedItem() + "\n"
                    + "Area: " + area.getSelectedItem() + "\n"
                    + "Canal: " + canal.getSelectedItem() + "\n"
                    + "Data: " + global.get_br_date(data.getDate()) + "\n"
                    + "Janela: " + janela.getSelectedItem() + "\n"
                    + "Solicitante: " + solic.getText() + "\n"
                    + "Nome Cliente: " + nome_cli.getText() + "\n"
                    + "Particularidade: " + obs_horario.getText() + "\n"
                    + "Os's: " + get_oss() + "\n"
                    + "Obs: " + obs.getText() + "\n"
                    + "" + mn.get_nome() + " - Ouvidoria COP - " + nome_net;;

            last_obs = copia;

            Db_class.close_conn(conn);

            copy_obs();

            //incluindo Logs de inclusão de OSs e movimentação de OSs entre níveis
            LogS log = new LogS();
            log.LogComum_InclusaoPacote(last_id, 1, 1, 1);
            if ("Acompanhamento".equals(i_tipo_trat)) {
                log.LogComum_InclusaoPacote(last_id, 5, 1, 2);
            } else {
                if ("OMS Indevido".equals(i_tipo_trat)) {
                    log.LogComum_InclusaoPacote(last_id, 7, 1, 1);
                } else {
                    if ("Devolucao sem Tratativa".equals(i_tipo_trat)) {
                        log.LogComum_InclusaoPacote(last_id, 8, 1, 1);
                    }
                }
            }
            log = null;
            //

            global.show_message("Registro inserido com sucesso! A observação está na área de transferência!");

            reset_form();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Novo_Registro_panel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void copy_obs() {

        String nome_net = "";

        String copia = "********* COP OUVIDORIA **********\n"
                + "Contrato: " + contrato.getText() + "\n"
                + "Cidade: " + cidade.getSelectedItem() + "\n"
                + "Area: " + area.getSelectedItem() + "\n"
                + "Canal: " + canal.getSelectedItem() + "\n"
                + "Data: " + global.get_br_date(data.getDate()) + "\n"
                + "Janela: " + janela.getSelectedItem() + "\n"
                + "Solicitante: " + solic.getText() + "\n"
                + "Nome Cliente: " + nome_cli.getText() + "\n"
                + "Particularidade: " + obs_horario.getText() + "\n"
                + "Os's: " + get_oss() + "\n"
                + "Obs: " + obs.getText() + "\n"
                + "" + mn.get_nome() + " - Ouvidoria COP - " + nome_net;

        StringSelection clip_obs = new StringSelection(copia);

        CLIPBOARD.setContents(clip_obs, null);

    }

    public void copy_last_obs() {

        StringSelection clip_obs = new StringSelection(last_obs);

        CLIPBOARD.setContents(clip_obs, null);

    }

    public String get_oss() {

        int linhas_os = tab_os.getRowCount();

        String os_str = "";

        for (int i = 0; i <= (linhas_os - 1); i++) {

            String current_os = (String) tab_os.getValueAt(i, 0);
            String current_tipo = (String) tab_os.getValueAt(i, 1);

            if (i == 0) {

                os_str = current_os + " - " + current_tipo;

            } else {

                os_str = os_str + "\n" + current_os + " - " + current_tipo;

            }

        }

        return os_str;

    }

    public Menu getMn() {
        return mn;
    }

    public boolean isMail_attached() {
        return mail_attached;
    }

    public static Clipboard getCLIPBOARD() {
        return CLIPBOARD;
    }

    public Novo_Registro_panel getNr() {
        return nr;
    }

    public String getPath_mail() {
        return path_mail;
    }

    public JComboBox getArea() {
        return area;
    }

    public JButton getBotao_add() {
        return botao_add;
    }

    public JButton getBotao_mail() {
        return botao_mail;
    }

    public ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }

    public ButtonGroup getButtonGroup2() {
        return buttonGroup2;
    }

    public JComboBox getCanal() {
        return canal;
    }

    public JComboBox getCidade() {
        return cidade;
    }

    public JRadioButton getCom_sucesso() {
        return com_sucesso;
    }

    public JTextField getContrato() {
        return contrato;
    }

    public JXDatePicker getData() {
        return data;
    }

    public JComboBox getDescr_indev() {
        return descr_indev;
    }

    public JButton getjButton1() {
        return jButton1;
    }

    public JButton getjButton2() {
        return jButton2;
    }

    public JLabel getjLabel14() {
        return jLabel14;
    }

    public JLabel getjLabel15() {
        return jLabel15;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JPanel getjPanel2() {
        return painel_dados;
    }

    public JPanel getjPanel3() {
        return jPanel3;
    }

    public JPanel getjPanel4() {
        return jPanel4;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    public JScrollPane getjScrollPane3() {
        return jScrollPane3;
    }

    public JComboBox getJanela() {
        return janela;
    }

    public JRadioButton getNao() {
        return nao;
    }

    public JTextField getNome_cli() {
        return nome_cli;
    }

    public JTextField getNum_os() {
        return num_os;
    }

    public JTextArea getObs() {
        return obs;
    }

    public JTextArea getObs_cidade() {
        return obs_cidade;
    }

    public JTextField getObs_horario() {
        return obs_horario;
    }

    public JPanel getPainel_os() {
        return painel_os;
    }

    public JRadioButton getSem_sucesso() {
        return sem_sucesso;
    }

    public JRadioButton getSim() {
        return sim;
    }

    public JTextField getSolic() {
        return solic;
    }

    public JTable getTab_os() {
        return tab_os;
    }

    public JComboBox getTipo_os() {
        return tipo_os;
    }

    public JComboBox getTipo_trat() {
        return tipo_trat;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> area;
    private javax.swing.JButton botao_add;
    private javax.swing.JButton botao_mail;
    private javax.swing.JButton botao_script;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox<String> canal;
    private javax.swing.JComboBox<String> cidade;
    private javax.swing.JRadioButton com_sucesso;
    private javax.swing.JTextField contrato;
    private org.jdesktop.swingx.JXDatePicker data;
    private javax.swing.JComboBox<String> descr_indev;
    private javax.swing.JRadioButton encaixe_nao;
    private javax.swing.JRadioButton encaixe_sim;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JComboBox<String> janela;
    private javax.swing.JRadioButton nao;
    private javax.swing.JTextField nome_cli;
    private javax.swing.JTextField num_os;
    private javax.swing.JTextArea obs;
    private javax.swing.JTextArea obs_cidade;
    private javax.swing.JTextField obs_horario;
    private javax.swing.JPanel painel_dados;
    private javax.swing.JPanel painel_os;
    private javax.swing.JRadioButton sem_sucesso;
    private javax.swing.JRadioButton sim;
    private javax.swing.JTextField solic;
    private javax.swing.JTable tab_os;
    private javax.swing.JComboBox tipo_os;
    private javax.swing.JComboBox<String> tipo_trat;
    // End of variables declaration//GEN-END:variables
}
