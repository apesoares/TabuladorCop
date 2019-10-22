/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.SwingUtilities;

/**
 *
 * @author N0026925
 */
public class Menu extends javax.swing.JFrame {

    String nome;
    String perfil;
    String cop;
    String login;

    localiza_parceiro_tecnico loc_par_tec;

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();

        //jMenu8.setVisible(false);
        jTabbedPane1.addMouseListener(new tab_listener());

        get_user_info();

        if (!is_coord()) {
            jMenuItem9.setVisible(false); //Geral > Produtividade > Produtividade individual
            jMenuItem8.setVisible(false); //Geral > produtividade > Produtividade equipe
            jMenu16.setVisible(false);
            jMenu8.setVisible(false); //Geral > Relatórios

            jsep_1.setVisible(false);
            jmni_geral_cadastros.setVisible(false);
        }

        //se for CDesk
        if (get_perfil().equals("4")) {
            jMenu15.setVisible(false);
            jMenu11.setVisible(false);
            jMenu13.setVisible(true);
            
            jMenuItem14.setVisible(false);
            jmni_geral_loc_par_tec.setVisible(true);
            jMenu6.setVisible(false);
            jMenu8.setVisible(false);
            jmni_geral_cadastros.setVisible(true);

            jmni_geral_cadastros_mr.setVisible(false);
            jmni_geral_cadastros_cod_baixa_os.setVisible(false);
            jmni_geral_cadastros_area_despacho.setVisible(false);
            jmni_geral_cadastros_usuarios.setVisible(true);
            jmni_geral_cadastros_tipos_os.setVisible(false);
        }
        
        //se não for nem CDESK nem DEV_MASTER
        if (!get_perfil().equals("4") && !get_perfil().equals("0")) {
            jmni_geral_cadastros_usuarios.setVisible(false);
        }

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/16x16/phone_vintage.png")));

        DisplayTrayIcon DTI = new DisplayTrayIcon(this);

        //this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //fecha a aplicação

    }

    public void get_user_info() {

        Integer iBDProducao;

        this.login = System.getProperty("user.name");
        //this.login = "IAMAR";

        //iBDProducao = 0;
        
        iBDProducao = 1;
        //if ("robsmac".equals(System.getProperty("user.name").trim().toLowerCase())) {
        if (Arrays.asList("robsmac","nmartin","hcorbo").contains(System.getProperty("user.name").trim().toLowerCase())) {
            iBDProducao = JOptionPane.showConfirmDialog(null, "Deseja utilizar o banco de dados de desenvolvimento", "Opção extra disponível", YES_NO_OPTION, QUESTION_MESSAGE, null);
            if (iBDProducao == -1) {
                iBDProducao = 1;
            }
        }
        
        //iBDProducao = 0; //forcando desenvolvimento
        
        global.iBDProducao = iBDProducao;

        //String query = "SELECT nome, cop, perfil FROM users WHERE login = '" + login + "' and perfil <> 0";
        String query = "SELECT nome, cop, perfil FROM users WHERE login = '" + login + "' and perfil <> -1"; //não pegar inativos
        try {
            Connection conn = Db_class.mysql_conn();

            if (conn == null) {
                global.show_error_message("Problemas para conectar ao banco de dados já no Login");
                System.exit(0);
                this.dispose();
            }

            ResultSet rs = Db_class.mysql_result(conn, query);

            if (rs.next()) {
                this.nome = rs.getString(1);
                this.cop = rs.getString(2);
                this.perfil = rs.getString(3);
                /*
                 System.out.println(nome);
                 System.out.println(cop);
                 System.out.println(perfil);
                 */

                label_user.setText("   Usuário logado - " + this.nome + " (" + this.cop + ")");
                if (global.iBDProducao == 0) {
                    label_user.setOpaque(true);
                    label_user.setBackground(Color.CYAN);
                    label_user.setToolTipText("Banco de Desenvolvimento");
                }
                Db_class.close_conn(conn);
            } else {
                Db_class.close_conn(conn);
                global.show_error_message("O usuário " + login.toUpperCase() + ", logado no sistema operacional,\n" +
                        "não possui acesso ao sistema\n\n" +
                        "Tabulador COP / Ouvidoria\n\n" +
                        "O Supervisor de Campanha consegue corrigir o caso."
                        );
                System.exit(0);
                this.dispose();
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String get_nome() {

        return this.nome;

    }

    public String get_cop() {

        return this.cop;

    }

    public String get_perfil() {

        return this.perfil;

    }

    public String get_login() {

        return this.login;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        label_user = new javax.swing.JLabel();
        label_version = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jmn_net_n3_pos_baixa = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu16 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jmni_geral_loc_par_tec = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jsep_1 = new javax.swing.JPopupMenu.Separator();
        jmni_geral_cadastros = new javax.swing.JMenu();
        jmni_geral_cadastros_mr = new javax.swing.JMenuItem();
        jmni_geral_cadastros_cod_baixa_os = new javax.swing.JMenuItem();
        jmni_geral_cadastros_area_despacho = new javax.swing.JMenuItem();
        jmni_geral_cadastros_usuarios = new javax.swing.JMenuItem();
        jmni_geral_cadastros_tipos_os = new javax.swing.JMenuItem();
        jmni_geral_cadastros_tipos_trat = new javax.swing.JMenuItem();

        jCheckBox1.setText("jCheckBox1");

        jMenu1.setText("jMenu1");

        jMenuItem3.setText("jMenuItem3");

        jMenuItem5.setText("jMenuItem5");

        jMenuItem11.setText("jMenuItem11");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setPreferredSize(new java.awt.Dimension(695, 27));
        jPanel1.setLayout(new java.awt.BorderLayout());

        label_user.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_user.setText("Usuário");
        label_user.setOpaque(true);
        label_user.setPreferredSize(new java.awt.Dimension(334, 14));
        jPanel1.add(label_user, java.awt.BorderLayout.WEST);

        label_version.setBackground(new java.awt.Color(255, 255, 255));
        label_version.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_version.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        label_version.setText("Versão 2.6.7");
        label_version.setMaximumSize(new java.awt.Dimension(5, 14));
        label_version.setMinimumSize(new java.awt.Dimension(5, 14));
        label_version.setOpaque(true);
        label_version.setPreferredSize(new java.awt.Dimension(5, 14));
        jPanel1.add(label_version, java.awt.BorderLayout.CENTER);

        jLabel3.setText("     ");
        jPanel1.add(jLabel3, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jMenuBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jMenu15.setText("NET     ");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/script.png"))); // NOI18N
        jMenuItem1.setText("N1 - OMS");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/table_lightning.png"))); // NOI18N
        jMenu2.setText("N2 - Acompanhamento");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/net.png"))); // NOI18N
        jMenuItem2.setText("Painel de Acompanhamento");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plugin.png"))); // NOI18N
        jMenuItem4.setText("Painel de Encaixes");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenu15.add(jMenu2);

        jmn_net_n3_pos_baixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eye.png"))); // NOI18N
        jmn_net_n3_pos_baixa.setText("N3 - Pós-baixa");
        jmn_net_n3_pos_baixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmn_net_n3_pos_baixaActionPerformed(evt);
            }
        });
        jMenu15.add(jmn_net_n3_pos_baixa);

        jMenu10.setText("Cadastros   ");

        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/book.png"))); // NOI18N
        jMenuItem16.setText("Particularidades Cidades");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem16);

        jMenu14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/wrench.png"))); // NOI18N
        jMenu14.setText("Técnicos");

        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/new.png"))); // NOI18N
        jMenuItem19.setText("Novo Técnico");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem19);

        jMenuItem20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pencil.png"))); // NOI18N
        jMenuItem20.setText("Consulta/ Edição");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem20);

        jMenu10.add(jMenu14);

        jMenu16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/map.png"))); // NOI18N
        jMenu16.setText("Divisão de Cidades");

        jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/application_view_columns.png"))); // NOI18N
        jMenuItem21.setText("Consulta Geral");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem21);

        jMenuItem22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/group_gear.png"))); // NOI18N
        jMenuItem22.setText("Divisão IAT");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem22);

        jMenuItem23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/tractor.png"))); // NOI18N
        jMenuItem23.setText("Divisão EPO");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem23);

        jMenu10.add(jMenu16);

        jMenu15.add(jMenu10);

        jMenuBar1.add(jMenu15);

        jMenu11.setText("DTH     ");

        jMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/script_red.png"))); // NOI18N
        jMenuItem15.setText("Novo Registro - DTH");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem15);

        jMenuItem17.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/claro.PNG"))); // NOI18N
        jMenuItem17.setText("Painel de Acompanhamento");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem17);

        jMenu3.setText("Cadastros   ");

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/book.png"))); // NOI18N
        jMenuItem6.setText("Particularidades MR's");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenu11.add(jMenu3);

        jMenuBar1.add(jMenu11);

        jMenu13.setText("Geral");

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clock.png"))); // NOI18N
        jMenuItem14.setText("Divisão de turnos");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem14);

        jmni_geral_loc_par_tec.setText("Localizar parceiras ou técnicos");
        jmni_geral_loc_par_tec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_geral_loc_par_tecActionPerformed(evt);
            }
        });
        jMenu13.add(jmni_geral_loc_par_tec);

        jMenu6.setText("Produtividade   ");

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        jMenuItem7.setText("Minha Produtividade");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem7);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/group.png"))); // NOI18N
        jMenuItem8.setText("Produtividade Equipe");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem8);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user_red.png"))); // NOI18N
        jMenuItem9.setText("Produtividade Individual");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem9);

        jMenu13.add(jMenu6);

        jMenu8.setText("Relatórios   ");

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/book_open.png"))); // NOI18N
        jMenuItem13.setText("Relatórios Gerenciais");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem13);

        jMenu13.add(jMenu8);
        jMenu13.add(jsep_1);

        jmni_geral_cadastros.setText("Cadastros");

        jmni_geral_cadastros_mr.setText("Microrregião (apenas DTH)");
        jmni_geral_cadastros_mr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_geral_cadastros_mrActionPerformed(evt);
            }
        });
        jmni_geral_cadastros.add(jmni_geral_cadastros_mr);

        jmni_geral_cadastros_cod_baixa_os.setText("Códigos de baixa de OS");
        jmni_geral_cadastros_cod_baixa_os.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_geral_cadastros_cod_baixa_osActionPerformed(evt);
            }
        });
        jmni_geral_cadastros.add(jmni_geral_cadastros_cod_baixa_os);

        jmni_geral_cadastros_area_despacho.setText("Área de despacho");
        jmni_geral_cadastros_area_despacho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_geral_cadastros_area_despachoActionPerformed(evt);
            }
        });
        jmni_geral_cadastros.add(jmni_geral_cadastros_area_despacho);

        jmni_geral_cadastros_usuarios.setText("Usuários");
        jmni_geral_cadastros_usuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_geral_cadastros_usuariosActionPerformed(evt);
            }
        });
        jmni_geral_cadastros.add(jmni_geral_cadastros_usuarios);

        jmni_geral_cadastros_tipos_os.setText("Tipos de OS");
        jmni_geral_cadastros_tipos_os.setEnabled(false);
        jmni_geral_cadastros_tipos_os.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_geral_cadastros_tipos_osActionPerformed(evt);
            }
        });
        jmni_geral_cadastros.add(jmni_geral_cadastros_tipos_os);

        jmni_geral_cadastros_tipos_trat.setText("Tipos de Tratamentos");
        jmni_geral_cadastros_tipos_trat.setEnabled(false);
        jmni_geral_cadastros_tipos_trat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmni_geral_cadastros_tipos_tratActionPerformed(evt);
            }
        });
        jmni_geral_cadastros.add(jmni_geral_cadastros_tipos_trat);

        jMenu13.add(jmni_geral_cadastros);

        jMenuBar1.add(jMenu13);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        if (!check_version()) {

            return;

        }

        //Novo_registro nr = new Novo_registro(this, true);
        Novo_Registro_panel nrp = new Novo_Registro_panel(this, "COP NET");

        add_tab("Novo Registro - NET", nrp);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        painel pn = new painel(this, "COP NET", 0);

        this.add_tab("Painel de Acompanhamento - NET", pn);

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        if (!check_version()) {
            return;
        }

        painel_encaixe pe = new painel_encaixe(this, 0);

        this.add_tab("Painel de Encaixes", pe);

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed

        if (!check_version()) {

            return;

        }

        partic_cidade pc = new partic_cidade(this, true, "COP DTH");

    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        if (!check_version()) {

            return;

        }

        prod_indiv pi = new prod_indiv(this);

        this.add_tab("Minha Produtividade", pi);

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        if (!check_version()) {

            return;

        }

        prod_indiv pi = new prod_indiv(this, true);

        this.add_tab("Produtividade Individual", pi);

    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed

        if (!check_version()) {
            return;
        }

        prod_equipe pe = new prod_equipe(this);
        this.add_tab("Produtividade Equipe", pe);

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        this.setVisible(false);

    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed

        if (!check_version()) {
            return;
        }

        div_turno dt = new div_turno(this, true);

    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed

        if (!check_version()) {

            return;

        }

        relat rl = new relat(this);

        add_tab("Relatórios Gerenciais", rl);

    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed

        if (!check_version()) {

            return;

        }

        painel_dth pn = new painel_dth(this, "COP DTH");

        this.add_tab("Painel de Acompanhamento - DTH", pn);

    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed

        if (!check_version()) {

            return;

        }

        partic_cidade pc = new partic_cidade(this, true, "COP NET");

    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed

        novo_tecnico nt = new novo_tecnico(this, true, "COP NET");

    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed

        consulta_tec ct = new consulta_tec(this);

        add_tab("Consulta Técnicos", ct);

    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed

        if (!check_version()) {

            return;

        }

        consulta_divisao cd = new consulta_divisao(this);

        add_tab("Consulta divisão de cidades", cd);

    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed

        if (!check_version()) {

            return;

        }

        divisao_operador div_op = new divisao_operador(this, "iat");

    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed

        if (!check_version()) {

            return;

        }

        divisao_operador div_op = new divisao_operador(this, "epo");

    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed

        if (!check_version()) {

            return;

        }

        //Novo_registro nr = new Novo_registro(this, true);
        Novo_Registro_panel nrp = new Novo_Registro_panel(this, "COP DTH");

        add_tab("Novo Registro - DTH", nrp);

    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jmni_geral_cadastros_mrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_geral_cadastros_mrActionPerformed
        // TODO add your handling code here:

        if (!check_version()) {
            return;
        }

        dth_mr_cadastro cad_mr = new dth_mr_cadastro(this, true);

    }//GEN-LAST:event_jmni_geral_cadastros_mrActionPerformed

    private void jmni_geral_cadastros_tipos_osActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_geral_cadastros_tipos_osActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmni_geral_cadastros_tipos_osActionPerformed

    private void jmni_geral_cadastros_cod_baixa_osActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_geral_cadastros_cod_baixa_osActionPerformed
        // TODO add your handling code here:
        if (!check_version()) {
            return;
        }

        cd_baixa_os_cadastro cad_cd_bx_os = new cd_baixa_os_cadastro(this, true);

    }//GEN-LAST:event_jmni_geral_cadastros_cod_baixa_osActionPerformed

    private void jmni_geral_loc_par_tecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_geral_loc_par_tecActionPerformed
        // TODO add your handling code here:
        if (!check_version()) {
            return;
        }

        if (loc_par_tec == null) {
            loc_par_tec = new localiza_parceiro_tecnico(this, false);
        } else {
            loc_par_tec.setVisible(true);
            loc_par_tec.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - loc_par_tec.getWidth() / 2,
                    (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - loc_par_tec.getHeight() / 2);

        }
    }//GEN-LAST:event_jmni_geral_loc_par_tecActionPerformed

    private void jmni_geral_cadastros_area_despachoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_geral_cadastros_area_despachoActionPerformed
        // TODO add your handling code here:
        if (!check_version()) {
            return;
        }

        Area_despacho_cadastro cad_area_despacho = new Area_despacho_cadastro(this, true);
    }//GEN-LAST:event_jmni_geral_cadastros_area_despachoActionPerformed

    private void jmni_geral_cadastros_usuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_geral_cadastros_usuariosActionPerformed
        // TODO add your handling code here:
        if (!check_version()) {
            return;
        }

        cd_usuario cad_usuario = new cd_usuario(this, true);
    }//GEN-LAST:event_jmni_geral_cadastros_usuariosActionPerformed

    private void jmni_geral_cadastros_tipos_tratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmni_geral_cadastros_tipos_tratActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmni_geral_cadastros_tipos_tratActionPerformed

    private void jmn_net_n3_pos_baixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmn_net_n3_pos_baixaActionPerformed

        painel_n3 pn = new painel_n3(this, 1);

        this.add_tab("Painel N3", pn);

    }//GEN-LAST:event_jmn_net_n3_pos_baixaActionPerformed

    public void add_tab(String nome, Component cp) {

        jTabbedPane1.add(nome, cp);
        jTabbedPane1.setSelectedComponent(cp);

    }

    class tab_listener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e)) {

                int aba = jTabbedPane1.indexAtLocation(e.getX(), e.getY());
                Component tab = jTabbedPane1.getComponent(aba);
                jTabbedPane1.remove(tab);

            }

        }

    }

    public boolean check_version() {

        try {
            Connection conn = Db_class.mysql_conn();

            //String query = "SELECT version FROM version WHERE id_version = 1";
            String query = "select concat(versao_maior,'.',versao_menor,'.',versao_seq_ajustes) as db_versao from ouvidoria.tb_versao where fl_ativa = 1";

            ResultSet rs = Db_class.mysql_result(conn, query);

            rs.next();

            String active_version = label_version.getText();
            String db_version = rs.getString(1);
            Db_class.close_conn(conn);

            if (!active_version.equals("Versão " + db_version)) {
                /*
                 //ALTERAR_BCC_OK
                 global.show_error_message("Esta versão do software está desatualizada.\n"
                 + "Uma nova versão estara sendo disponibilizada");
                 return false;
                 */
                label_version.setToolTipText("Versão " + db_version + " é a oficial. Para utilizá-la, basta reabrir a aplicação.");
                label_version.setBackground(Color.orange);

                return true;
            } else {
                label_version.setToolTipText(null);
                label_version.setBackground(Color.white);
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;

    }

    public boolean is_coord() {

        //if (get_perfil().equals("1")) {
        //se Supervisor ou DEV_MASTER
        if (get_perfil().equals("1") || get_perfil().equals("0")) {
            return true;
        } else {
            return false;
        }

    }

    public String get_zenvia_user() {

        String user = "";

        //ALTERAR_BCC_OK
        user = "net.G638";
        return user;

    }

    public String get_zenvia_password() {

        //ALTERAR_BCC_OK
        String password = "";

        password = "T9adoXVWjn";
        return password;

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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem jmn_net_n3_pos_baixa;
    private javax.swing.JMenu jmni_geral_cadastros;
    private javax.swing.JMenuItem jmni_geral_cadastros_area_despacho;
    private javax.swing.JMenuItem jmni_geral_cadastros_cod_baixa_os;
    private javax.swing.JMenuItem jmni_geral_cadastros_mr;
    private javax.swing.JMenuItem jmni_geral_cadastros_tipos_os;
    private javax.swing.JMenuItem jmni_geral_cadastros_tipos_trat;
    private javax.swing.JMenuItem jmni_geral_cadastros_usuarios;
    private javax.swing.JMenuItem jmni_geral_loc_par_tec;
    private javax.swing.JPopupMenu.Separator jsep_1;
    private javax.swing.JLabel label_user;
    private javax.swing.JLabel label_version;
    // End of variables declaration//GEN-END:variables
}
