/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import ouvidoria.util.UtilBD;

/**
 *
 * @author N0026925
 */
public class tratar extends javax.swing.JDialog {

    int current_id;
    Menu mn;
    Date data_real;
    painel pn;
    String current_status_monit;
    String current_status_caso;
    Date current_date;
    String current_janela;
    boolean on_prod = false;
    tratar tr;
    JTable real_tab_os;
    private static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    String cop;
    int ifl_n3;

    /**
     * Creates new form tratar
     */
    public tratar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

    }

    public tratar(Menu mn, boolean modal, int id_caso, String cop_rec, int pifl_n3) {

        super(mn, modal);
        initComponents();

        this.tr = this;
        this.cop = cop_rec;
        this.ifl_n3 = pifl_n3;

        //current_id = id_caso;
        //current_id = Identifica_IdMestre(id_caso);
        UtilBD utilBD = new UtilBD();
        current_id = utilBD.Identifica_IdMestre(id_caso);
        utilBD = null;

        if (current_id == 0) {
            global.show_error_message("Os detalhes do caso não foram encontrados.\nTente 'atualizar'");
            return;
        }
        this.mn = mn;

        on_prod = true;

        if (!mn.check_version()) {
            return;
        }

        fill_panel();

        global.initCloseListener(this);

        jTabbedPane1.remove(jPanel7);

        //add_mail_tab();
        global.open_modal(this, "Acompanhamento OMS " + buscaField(cidade.getText()));

    }

    public tratar(Menu mn, boolean modal, int id_caso, painel pn, int tab_index, String cop_rec, int pifl_n3) {
        super(mn, modal);
        initComponents();

        //current_id = id_caso;
        //current_id = Identifica_IdMestre(id_caso);
        UtilBD utilBD = new UtilBD();
        current_id = utilBD.Identifica_IdMestre(id_caso);
        utilBD = null;

        if (current_id == 0) {
            global.show_error_message("Os detalhes do caso não foram encontrados.\nTente 'atualizar'");
            return;
        }

        this.mn = mn;
        this.pn = pn;
        this.tr = this;
        this.cop = cop_rec;
        this.ifl_n3 = pifl_n3;

        if (!mn.check_version()) {
            return;
        }

        //fill_panel();
        if (!fill_panel()) {
            return;
        }

        select_tab_index(tab_index);

        global.initCloseListener(this);

        jTabbedPane1.remove(jPanel7);

        //add_mail_tab();
        global.open_modal(this, "Acompanhamento OMS " + buscaField(cidade.getText()));

    }

    public boolean fill_panel() {

        boolean bContinuar = false;

        //painel_os.removeAll();
        String query_caso = "SELECT ifnull(id,'') as Id,\n"
                + "ifnull(contrato,'') as Contrato,\n"
                + "ifnull(cidade,'') as Cidade,\n"
                + "ifnull(area_despacho,'') as Area_Despacho,\n"
                + "ifnull(data_acomp,'') as Data_Acomp,\n"
                + "ifnull(janela,'') as Janela,\n"
                + "ifnull(tipo_os,'') as Tipo_Os,\n"
                + "ifnull(solicitante,'') as Solicitante,\n"
                + "ifnull(nome_cli,'') as Nome_Cli,\n"
                + "ifnull(tipo_trat,'') as Tipo_Trat,\n"
                + "ifnull(descr_indevido,'') as Descr_Indevido,\n"
                + "ifnull(canal,'') as Canal,\n"
                + "ifnull(obs_horario,'') as Obs_Horario,\n"
                + "ifnull(obs,'') as Obs,\n"
                + "ifnull(epo,'') as Epo,\n"
                + "ifnull(tecnico,'') as Tecnico,\n"
                + "ifnull(tipo_tecnico,'') as Tipo_Tecnico,\n"
                + "ifnull(telefone_tec,'') as Telefone_Tec,\n"
                + "ifnull(obs_tec,'') as Obs_Tec,\n"
                + "ifnull(cluster,'') as Cluster,\n"
                + "ifnull(cop,'') as Cop,\n"
                + "ifnull(date_time_insert,'') as Date_Time_Insert,\n"
                + "ifnull(user_insert,'') as User_Insert,\n"
                + "ifnull(nome_insert,'') as Nome_Insert,\n"
                + "ifnull(sistema,'') as Sistema,\n"
                + "ifnull(status,'') as Status,\n"
                + "ifnull(ativo,'') as Ativo,\n"
                + "ifnull(tentativa,'') as Tentativa,\n"
                + "ifnull(status_monitoria,'') as Status_Monitoria,\n"
                + "ifnull(ultimo_acomp,'') as Ultimo_Acomp,\n"
                + "ifnull(hora_ultimo_acomp,'') as Hora_Ultimo_Acomp,"
                + "ifnull(email,'Não') as email, "
                + "ifnull(turno_tec,'') as turno, "
                + "ifnull(login_tec,'') as login_tec, "
                + "ifnull(mail_attach,'Não') as mail_attach, "
                /*
                 + "( "
                 + "select distinct id_mestre "
                 + "from painel "
                 + "where id_mestre = " + current_id + " "
                 //+ "where id_mestre = " + "9999999" + " "
                 + "and status not like '%Encerrado%' "
                 + "and fl_n3 = " + ifl_n3 + " "
                 + ") as registro_aberto "
                 + "FROM painel "
                 + "WHERE id = " + current_id + " ";
                 */
                + "id_mestre as registro_aberto "
                + "from painel "
                //+ "and status not like '%Encerrado%' and fl_n3 = " + ifl_n3 + " "
                //+ "where id_mestre = " + current_id + " and fl_n3 = " + ifl_n3 + " "
                + "where id_mestre = " + current_id + " and (fl_n3 = " + ifl_n3 + " or -1 = " + ifl_n3 + ") "
                + "limit 1 ";
        try {
            Connection conn = Db_class.mysql_conn();

            ResultSet rs = Db_class.mysql_result(conn, query_caso);

            //rs.next();
            if (!rs.next()) {
                global.show_message("Não há mais OSs a tratar neste nível, para o caso selecionado");
                this.dispose();
                return bContinuar;
            }

            current_date = rs.getDate(5);
            current_janela = rs.getString(6);
            current_status_caso = rs.getString(26);
            current_status_monit = rs.getString(29);

            if (pn instanceof painel_bsod) {

                obs_horario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT))));
                jLabel11.setText("Previsão");
                jLabel3.setText("REC");

            }

            contrato.setText(rs.getString(2));
            cidade.setText(rs.getString(3));
            area.setText(rs.getString(4));
            tipo_os.setText(rs.getString(7));
            data.setText(global.get_br_date(rs.getDate(5)));
            janela.setText(rs.getString(6));
            nome_cli.setText(rs.getString(9));
            canal.setText(rs.getString(12));
            obs_horario.setText(rs.getString(13));
            obs_antiga.setText(rs.getString(14));

            global.fill_combo(janela_alter, "SELECT nome_janela FROM janelas ORDER BY nome_janela", true);
            global.fill_combo(tipo_os_agenda, "SELECT tipo_os FROM tipo_os_novo GROUP BY tipo_os ORDER BY tipo_os ", true);
            //global.fill_combo(sts_monit, "SELECT status_monit FROM status_monit ORDER BY status_monit", true);
            //global.fill_combo(sts_caso, "SELECT status FROM status ORDER BY status", true);
            global.fill_combo(sts_monit, "SELECT descricao FROM tb_status_monit where fl_n3 = " + this.ifl_n3 + " AND FL_ATIVO = 1 ORDER BY descricao", true);
            global.fill_combo(sts_caso, "SELECT descricao FROM tb_status_caso where fl_n3 = " + this.ifl_n3 + " AND FL_ATIVO = 1 ORDER BY descricao", true);

            tipo_tec.setText(rs.getString(17));
            tecnico.setText(rs.getString(16));
            epo.setText(rs.getString(15));
            telefone.setText(rs.getString(18));
            obs_tec.setText(rs.getString(19));
            turno_tec.setText(rs.getString(33));
            login_tec.setText(rs.getString(34));

            //if (rs.getString(26).equals("Encaixe")) {
            if (rs.getString(26).equals("Encaixe") && this.ifl_n3 != 1) {
                sts_caso.removeAllItems();
                sts_caso.setEnabled(false);
                botao_status_caso.setEnabled(false);
                sts_monit.setEnabled(false);
                botao_status_monit.setEnabled(false);
            } else {
                sts_caso.setEnabled(true);
                botao_status_caso.setEnabled(true);
                sts_monit.setEnabled(true);
                botao_status_monit.setEnabled(true);
            }

            sts_caso.setSelectedItem(rs.getString(26));
            sts_monit.setSelectedItem(rs.getString(29));

            ins_por.setText(rs.getString(24));

            if (!rs.getString(22).equals("")) {
                ins_time.setText(global.get_br_date_time(rs.getTimestamp(22)));

            }

            ult_acomp_por.setText(rs.getString(30));
            if (!rs.getString(31).equals("")) {
                ult_acomp_time.setText(global.get_br_date_time(rs.getTimestamp(31)));
            }

            solic.setText(rs.getString(8));

            String contrato_query = rs.getString(2);

            String query_reincid = "SELECT count(*) "
                    + "FROM painel "
                    + "WHERE contrato = '" + contrato_query + "' "
                    + "and data_acomp BETWEEN current_date - INTERVAL 30 DAY and current_date";

            ResultSet rs_reincid = Db_class.mysql_result(conn, query_reincid);

            rs_reincid.next();

            long count = rs_reincid.getLong(1);

            if (count <= 1) {

                reincid.setText("Não");

            } else {

                reincid.setText("Sim");

            }

            obs_antiga.setCaretPosition(0);
            tipo_os.setCaretPosition(0);
            obs_horario.setCaretPosition(0);

            if (rs.getString(32).equals("Sim")) {

                check_mail.setSelected(true);

            }

            if (epo.getText().equals("")) {

                botao_email.setEnabled(false);
                botao_email.setToolTipText("É necessário atribuir um técnico antes de enviar o e-mail!");

                UIManager.put("ToolTip.background", global.get_color(255, 255, 153));

            } else {

                botao_email.setEnabled(true);

            }

            //tipo_tec.addItemListener(new tipo_tec_listener());
            String query_obs_cid = "SELECT COUNT(*), obs FROM cidades_novo "
                    + "WHERE ci_depara = '" + cidade.getText() + "'";

            ResultSet rs_cidade = Db_class.mysql_result(conn, query_obs_cid);

            rs_cidade.next();

            if (rs.getLong(1) >= 1) {

                obs_cidade.setText(rs_cidade.getString(2));

            } else {

                painel_cidade.setVisible(false);

            }

            if (rs.getString(26).equals("Encerrado")
                    || rs.getString(26).equals("Encerrado - Os Duplicada/Cancelada")) {

                if (rs.getInt(36) == 0) {
                    disable_components(this);
                }

            }

            mail_at.setText(rs.getString(35));
            if (rs.getString(35).equals("Sim")) {

                open_mail.setEnabled(true);

            }

            if (on_prod) {

                disable_components(this);

            }

            obs_cidade.setCaretPosition(0);

            if (ifl_n3 != 1) {
                jbtn_reverter_baixa.setVisible(false);
                jsep_reverter_baixa.setVisible(false);
            }

            create_tab_os();

            if (ifl_n3 != -1) {
                if (this.cop.equals("COP DTH")) {

                    jLabel1.setText("MR");
                    jLabel2.setText("Cidade");
                    jButton5.setEnabled(false);

                }
            }

            Db_class.close_conn(conn);
            bContinuar = true;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bContinuar;

    }

    public void create_tab_os() {

        painel_os.removeAll();

        Connection conn;
        try {
            conn = Db_class.mysql_conn();

            String query_os = "";
            int[] column_widths = {40, 150, 30, 200, 0};
            if (ifl_n3 != 1) {
                query_os = "SELECT "
                        + "ifnull(num_os,'') as 'Núm OS', "
                        + "ifnull(tipo_os,'') as 'Tipo OS', "
                        + "ifnull(status_os,'Aberta') as 'Status N2', "
                        + "ifnull(cod_baixa,'') as 'Cod_Baixa N2', "
                        + "'Não' as 'Baixado N3?' "
                        + "FROM painel "
                        + "WHERE id_mestre = " + current_id + " "
                        + "and (fl_n3 = " + ifl_n3 + " or -1 = " + ifl_n3 + ") ";

                column_widths[4] = 0;
            } else {
                query_os = "SELECT \n"
                        + "ifnull(p.num_os,'') as 'Núm OS', \n"
                        + "ifnull(p.tipo_os,'') as 'Tipo OS', \n"
                        + "ifnull(p.status_os,'Aberta') as 'Status N2', \n"
                        + "ifnull(p.cod_baixa,'') as 'Cod_Baixa N2', \n"
                        + "case n3.id_acomp_status when '0' then 'Não' else 'Sim' end as 'Baixado N3?' \n"
                        + "FROM ouvidoria.painel p \n"
                        + "left join ouvidoria.tb_acao_registro_n3 n3 \n"
                        + "  on p.id_mestre = n3.id_mestre and p.num_os = n3.num_os and p.tipo_os = n3.tipo_os \n"
                        + "WHERE p.id_mestre = " + current_id + " \n"
                        + "and (p.fl_n3 = " + ifl_n3 + " or -1 = " + ifl_n3 + ") \n";

                column_widths[4] = 50;
            }

            JTable tab_os = global.getTable(query_os, painel_os);

            global.adjust_columns(column_widths, tab_os);

            real_tab_os = tab_os;
            botao_encerra.setEnabled(true);

            for (int i = 0; i <= (real_tab_os.getRowCount() - 1); i++) {

                String status = (String) real_tab_os.getValueAt(i, 2);

                if (status.equals("Aberta") || status.indexOf("Encerrado") > 0) {

                    botao_encerra.setEnabled(false);

                }

            }

            for (int columnTable = 1; columnTable < real_tab_os.getColumnCount(); columnTable++) {

                real_tab_os.getColumnModel().getColumn(columnTable).setCellRenderer(new CustomRenderer());

            }

            Db_class.close_conn(conn);

            painel_os.repaint();
            painel_os.revalidate();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        obs_antiga = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        obs_nova = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        obs_horario = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        tecnico = new javax.swing.JTextField();
        botao_procurar = new javax.swing.JButton();
        telefone = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        obs_tec = new javax.swing.JTextArea();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        login_tec = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        epo = new javax.swing.JTextField();
        tipo_tec = new javax.swing.JTextField();
        turno_tec = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        botao_status_monit = new javax.swing.JButton();
        sts_monit = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        sts_caso = new javax.swing.JComboBox();
        botao_status_caso = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        botao_email = new javax.swing.JButton();
        check_mail = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        janela_alter = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        data_alter = new org.jdesktop.swingx.JXDatePicker();
        jLabel20 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        obs_horario_alter = new javax.swing.JTextArea();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        obs_agendamento = new javax.swing.JTextArea();
        jLabel32 = new javax.swing.JLabel();
        tipo_os_agenda = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        painel_os = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton7 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton9 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton10 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jbt_reverter_os = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jbtn_reverter_baixa = new javax.swing.JButton();
        jsep_reverter_baixa = new javax.swing.JToolBar.Separator();
        botao_encerra = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        ins_por = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        ins_time = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        ult_acomp_por = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        ult_acomp_time = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        solic = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        contrato = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        nome_cli = new javax.swing.JTextField();
        area = new javax.swing.JTextField();
        data = new javax.swing.JTextField();
        cidade = new javax.swing.JTextField();
        tipo_os = new javax.swing.JTextField();
        janela = new javax.swing.JTextField();
        canal = new javax.swing.JTextField();
        reincid = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        mail_at = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        open_mail = new javax.swing.JButton();
        painel_cidade = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        obs_cidade = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(150, 235));

        obs_antiga.setEditable(false);
        obs_antiga.setColumns(20);
        obs_antiga.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        obs_antiga.setLineWrap(true);
        obs_antiga.setRows(2);
        obs_antiga.setWrapStyleWord(true);
        jScrollPane3.setViewportView(obs_antiga);

        obs_nova.setColumns(20);
        obs_nova.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        obs_nova.setLineWrap(true);
        obs_nova.setRows(2);
        obs_nova.setWrapStyleWord(true);
        jScrollPane2.setViewportView(obs_nova);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        jButton1.setText("Inserir Observação");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setText("Nova Observ.");

        jLabel10.setText("Histórico");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Particularidade");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/attach.png"))); // NOI18N
        jButton2.setText("Anexar E-mail");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        jButton6.setText("Alterar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(obs_horario, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(31, 31, 31))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(obs_horario)
                                .addGap(6, 6, 6))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Observações", jPanel2);

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel15.setText("Parceira");

        jLabel18.setText("Tipo de Técnico");

        jLabel12.setText("Técnico");

        jLabel13.setText("Telefone");

        tecnico.setEditable(false);

        botao_procurar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/magnifier.png"))); // NOI18N
        botao_procurar.setText("Procurar...");
        botao_procurar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botao_procurarMouseClicked(evt);
            }
        });
        botao_procurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_procurarActionPerformed(evt);
            }
        });

        telefone.setEditable(false);

        jLabel14.setText("Observação");

        obs_tec.setEditable(false);
        obs_tec.setColumns(20);
        obs_tec.setRows(1);
        jScrollPane4.setViewportView(obs_tec);

        jLabel28.setText("Turno");

        jLabel29.setText("Login");

        login_tec.setEditable(false);

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/wrench.png"))); // NOI18N
        jButton5.setText("Cadastro do Técnico");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        epo.setEditable(false);

        tipo_tec.setEditable(false);

        turno_tec.setEditable(false);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.png"))); // NOI18N
        jButton3.setText("Remover Técnico");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel12)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(tipo_tec, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(login_tec, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(tecnico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(epo, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(turno_tec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addGap(67, 67, 67)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botao_procurar)))
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {epo, login_tec, telefone});

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tecnico, tipo_tec, turno_tec});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel18)
                    .addComponent(tipo_tec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(login_tec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12)
                    .addComponent(tecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(epo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel28)
                    .addComponent(turno_tec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(telefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botao_procurar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {botao_procurar, jButton3, jButton5});

        jTabbedPane1.addTab("EPO/ Técnico", jPanel6);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        botao_status_monit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/monitor.png"))); // NOI18N
        botao_status_monit.setText("Alterar Status de Monitoria");
        botao_status_monit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_status_monitActionPerformed(evt);
            }
        });

        jLabel19.setText("Status de Monitoria");

        jLabel17.setText("Status do caso");

        botao_status_caso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_refresh_small.png"))); // NOI18N
        botao_status_caso.setText("Alterar Status do caso");
        botao_status_caso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_status_casoActionPerformed(evt);
            }
        });

        botao_email.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        botao_email.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email.png"))); // NOI18N
        botao_email.setText("Enviar E-mail de sinalização");
        botao_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_emailActionPerformed(evt);
            }
        });

        check_mail.setText("E-mail enviado");
        check_mail.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sts_monit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sts_caso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botao_status_monit)
                            .addComponent(botao_status_caso)
                            .addComponent(botao_email, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(check_mail)
                        .addGap(251, 251, 251))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3))
                .addGap(14, 14, 14))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {sts_caso, sts_monit});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {botao_email, botao_status_caso, botao_status_monit});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19)
                    .addComponent(sts_monit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botao_status_monit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17)
                    .addComponent(sts_caso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botao_status_caso, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(check_mail)
                    .addComponent(botao_email, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {sts_caso, sts_monit});

        jTabbedPane1.addTab("Monitoria", jPanel3);

        jLabel16.setText("Nova data");

        jLabel20.setText("Nova janela");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/date.png"))); // NOI18N
        jButton4.setText("Alterar agendamento");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel21.setText("Particularidade");

        obs_horario_alter.setColumns(20);
        obs_horario_alter.setRows(1);
        jScrollPane5.setViewportView(obs_horario_alter);

        jLabel30.setText("Observação");

        obs_agendamento.setColumns(20);
        obs_agendamento.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        obs_agendamento.setLineWrap(true);
        obs_agendamento.setRows(2);
        obs_agendamento.setWrapStyleWord(true);
        jScrollPane6.setViewportView(obs_agendamento);

        jLabel32.setText("Tipo de OS");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel30)
                            .addComponent(jLabel16)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6)
                            .addComponent(jScrollPane5)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tipo_os_agenda, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                        .addComponent(data_alter, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(janela_alter, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel16)
                    .addComponent(data_alter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(janela_alter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(tipo_os_agenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Agendamento", jPanel7);

        painel_os.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        painel_os.setLayout(new java.awt.BorderLayout());

        jToolBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setRollover(true);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png"))); // NOI18N
        jButton7.setText("Baixar OS");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);
        jToolBar1.add(jSeparator1);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        jButton9.setText("Adicionar nova OS");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);
        jToolBar1.add(jSeparator5);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/date.png"))); // NOI18N
        jButton10.setText("Reagendar OS");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);
        jToolBar1.add(jSeparator6);

        jbt_reverter_os.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_undo.png"))); // NOI18N
        jbt_reverter_os.setText("Reverter OS");
        jbt_reverter_os.setToolTipText("Desfaz as baixas, devolvendo ao N2");
        jbt_reverter_os.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_reverter_osActionPerformed(evt);
            }
        });
        jToolBar1.add(jbt_reverter_os);
        jToolBar1.add(jSeparator4);

        jbtn_reverter_baixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_undo.png"))); // NOI18N
        jbtn_reverter_baixa.setText("Reverter Baixa");
        jbtn_reverter_baixa.setToolTipText("Redefine a Baixa N2 ou N3");
        jbtn_reverter_baixa.setMaximumSize(new java.awt.Dimension(110, 23));
        jbtn_reverter_baixa.setMinimumSize(new java.awt.Dimension(110, 23));
        jbtn_reverter_baixa.setPreferredSize(new java.awt.Dimension(110, 23));
        jbtn_reverter_baixa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbtn_reverter_baixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn_reverter_baixaActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtn_reverter_baixa);
        jToolBar1.add(jsep_reverter_baixa);

        botao_encerra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        botao_encerra.setText("Encerrar Caso");
        botao_encerra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_encerraActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png"))); // NOI18N
        jButton11.setText("Caso duplicado/Cancelado");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/paste_plain.png"))); // NOI18N
        jButton13.setText("Copiar Script");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painel_os, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addGap(65, 65, 65)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botao_encerra, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painel_os, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botao_encerra, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jPanel9Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {botao_encerra, jButton11});

        jTabbedPane1.addTab("Os's do contrato", jPanel9);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel22.setText("Inserido por");

        ins_por.setEditable(false);

        jLabel23.setText("Hora inserção");

        ins_time.setEditable(false);

        jLabel24.setText("Ult Acomp por");

        ult_acomp_por.setEditable(false);

        jLabel25.setText("Hora ult acomp");

        ult_acomp_time.setEditable(false);

        jLabel26.setText("Solicitante");

        solic.setEditable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(solic, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_por, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ult_acomp_por, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ult_acomp_time, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(ins_time, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ins_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(ins_por, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))))
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ult_acomp_time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(ult_acomp_por, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(solic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(172, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Informações Adicionais", jPanel5);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("Contrato");

        contrato.setEditable(false);
        contrato.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        jLabel1.setText("Cidade");

        jLabel2.setText("Área Despacho");

        jLabel4.setText("Data Acomp");

        jLabel5.setText("Janela");

        jLabel6.setText("Tipo OS");

        jLabel7.setText("Canal Atend");

        jLabel9.setText("Nome do Cliente");

        nome_cli.setEditable(false);
        nome_cli.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        area.setEditable(false);
        area.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        data.setEditable(false);
        data.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        cidade.setEditable(false);
        cidade.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        tipo_os.setEditable(false);
        tipo_os.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        janela.setEditable(false);
        janela.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        canal.setEditable(false);
        canal.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        reincid.setEditable(false);

        jLabel27.setText("Reincid 30 dias");

        mail_at.setEditable(false);

        jLabel31.setText("E-mail Anexo");

        open_mail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email_open.png"))); // NOI18N
        open_mail.setText("Abrir E-mail");
        open_mail.setEnabled(false);
        open_mail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                open_mailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reincid, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nome_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(data, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contrato, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cidade, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tipo_os, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(janela, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(canal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(6, 6, 6)
                        .addComponent(mail_at, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(open_mail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {canal, cidade, janela, tipo_os});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {area, contrato, data, nome_cli, reincid});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(contrato, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(tipo_os, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4)
                    .addComponent(data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(janela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(nome_cli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(canal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel27)
                    .addComponent(reincid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(mail_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(open_mail)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {area, contrato, data, nome_cli});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {canal, cidade, janela, tipo_os});

        painel_cidade.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        painel_cidade.setLayout(new java.awt.BorderLayout());

        obs_cidade.setEditable(false);
        obs_cidade.setColumns(20);
        obs_cidade.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        obs_cidade.setForeground(new java.awt.Color(255, 51, 51));
        obs_cidade.setLineWrap(true);
        obs_cidade.setRows(1);
        obs_cidade.setTabSize(2);
        obs_cidade.setWrapStyleWord(true);
        jScrollPane7.setViewportView(obs_cidade);

        painel_cidade.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(painel_cidade, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel1, jTabbedPane1, painel_cidade});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painel_cidade, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!mn.check_version()) {

            return;

        }

        String texto = get_obs_hist() + obs_nova.getText();

        String texto_final = texto.replace("'", "");

        insert_obs(texto_final);

        insert_prod(current_id, "Inserção de Observação");

        set_ult_acomp();

        global.show_message("Observação inserida com sucesso!");

        obs_nova.setText("");

        //this.dispose();
        //tratar tr = new tratar(mn, true, current_id, pn, 0, cop);
        fill_panel();


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (!mn.check_version()) {

            return;

        }

        if (data_alter.getDate() == null || janela_alter.getSelectedItem().equals("")
                || tipo_os_agenda.getSelectedItem().equals("")) {

            return;

        }

        //encerra_form ef = new encerra_form(mn, true, true, this);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void botao_status_monitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_status_monitActionPerformed

        if (!mn.check_version()) {

            return;

        }

        altera_monitoria();


    }//GEN-LAST:event_botao_status_monitActionPerformed

    private void botao_status_casoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_status_casoActionPerformed

        if (!mn.check_version()) {

            return;

        }

        if (!sts_caso.getSelectedItem().equals("")) {

            altera_status_caso((String) sts_caso.getSelectedItem(), "", "");

        } else {

            global.show_error_message("É necessário informar o status do caso!");
            return;

        }

    }//GEN-LAST:event_botao_status_casoActionPerformed

    private void botao_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_emailActionPerformed

        if (!mn.check_version()) {
            return;
        }

        String sMailCondition = "";
        sMailCondition = mailAtividades(current_id);
        if (sMailCondition.length() > 0) {
            sMailCondition = "\nand atividade in (" + sMailCondition + ")\n";
        }

        Connection conn;
        try {
            conn = Db_class.mysql_conn();

            String query = "SELECT cidade, contrato, tipo_os, data_acomp, janela, epo, "
                    + "tecnico, status, canal, ifnull(obs_horario,''), area_despacho "
                    //+ "FROM painel WHERE id = " + current_id;
                    + "FROM painel WHERE id_mestre = " + current_id + " and fl_n3 = " + ifl_n3 + " limit 1";

            ResultSet rs = Db_class.mysql_result(conn, query);

            rs.next();

            String html_code = "";

            html_code = "";

            String body = global.get_saudacao() + "<br><br>"
                    + "Segue caso de ouvidoria a ser atendido.<br><br>"
                    + "Atendimento deve ser realizado dentro do 1° horário do início da janela, ou conforme conveniência do cliente.<br><br>";

            String body3 = "Solicitamos que,  assim que a OS for executada nos sinalizem através da devolutiva deste e-mail. Assim, como qualquer outra informação que se faça necessário.<br><br>";

            String final_html = "";

            if (rs.getString(7).equals("")) {

                html_code = "<table border=\"1\" cellpadding=\"5\">"
                        + "<tr bgcolor=\"000000\" font color=\"FFFFFF\">"
                        + "<th>Cidade</th>"
                        + "<th>Contrato</th>"
                        + "<th>Tipo OS</th>"
                        + "<th>Data</th>"
                        + "<th>Janela</th>"
                        + "<th>Parceira</th>"
                        + "<th>Status</th>"
                        + "<th>Canal Atend</th>"
                        + "<th>Observação</th>"
                        + "<th>Área de Despacho</th>"
                        + "</tr>";

                html_code = html_code + "<tr>"
                        + "<td>" + rs.getString(1) + "</td>"
                        + "<td>" + rs.getString(2) + "</td>"
                        + "<td>" + rs.getString(3) + "</td>"
                        + "<td>" + global.get_br_date(rs.getDate(4)) + "</td>"
                        + "<td>" + rs.getString(5) + "</td>"
                        + "<td>" + rs.getString(6) + "</td>"
                        + "<td>" + rs.getString(8) + "</td>"
                        + "<td>" + rs.getString(9) + "</td>"
                        + "<td>" + rs.getString(10) + "</td>"
                        + "<td>" + rs.getString(11) + "</td>"
                        + "</tr>"
                        + "</table>";

                final_html = body + body3 + html_code;

            } else {

                html_code = "<table border=\"1\" cellpadding=\"5\">"
                        + "<tr bgcolor=\"000000\" font color=\"FFFFFF\">"
                        + "<th>Cidade</th>"
                        + "<th>Contrato</th>"
                        + "<th>Tipo OS</th>"
                        + "<th>Data</th>"
                        + "<th>Janela</th>"
                        + "<th>Parceira</th>"
                        + "<th>Técnico</th>"
                        + "<th>Status</th>"
                        + "<th>Canal Atend</th>"
                        + "<th>Observação</th>"
                        + "<th>Área de Despacho</th>"
                        + "</tr>";

                html_code = html_code + "<tr>"
                        + "<td>" + rs.getString(1) + "</td>"
                        + "<td>" + rs.getString(2) + "</td>"
                        + "<td>" + rs.getString(3) + "</td>"
                        + "<td>" + global.get_br_date(rs.getDate(4)) + "</td>"
                        + "<td>" + rs.getString(5) + "</td>"
                        + "<td>" + rs.getString(6) + "</td>"
                        + "<td>" + rs.getString(7) + "</td>"
                        + "<td>" + rs.getString(8) + "</td>"
                        + "<td>" + rs.getString(9) + "</td>"
                        + "<td>" + rs.getString(10) + "</td>"
                        + "<td>" + rs.getString(11) + "</td>"
                        + "</tr>"
                        + "</table>";

                final_html = body + html_code;

            }

            String assunto = "";
            String to = "";
            String cc = "";

            String query_to;
            ResultSet rs_to;
            ResultSet rs_cc;

            if (rs.getString(6).equals("NET")) {
                //if ("NET".equals("NET")) {

                assunto = "Sinalização Ouvidoria - " + rs.getString(1) + " - "
                        + rs.getString(7) + " - " + rs.getString(5);

                query_to = "";
                to = "";

                //query_to = "select email from cop_info_tecnica ";
                query_to = "select distinct email from vw_cop_info_tecnica ";
                query_to = query_to + "where cidade like '" + rs.getString(1) + "%' ";
                query_to = query_to + "and ";
                query_to = query_to + "(";
                query_to = query_to + "cidade like '%" + rs.getString(11) + "' ";
                query_to = query_to + "or cidade like concat('%',";
                query_to = query_to + "(";
                query_to = query_to + "select grupo from tb_cidadeXdespachoXgrupo ";
                query_to = query_to + "where cidade = '" + rs.getString(1) + "' ";
                query_to = query_to + "and area_despacho = '" + rs.getString(11) + "' ";
                query_to = query_to + "limit 1";
                query_to = query_to + ")";
                query_to = query_to + ")";
                query_to = query_to + ")";
                query_to = query_to + sMailCondition;

                rs_to = Db_class.mysql_result(conn, query_to);
                while (rs_to.next()) {
                    to = to + rs_to.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                }
                rs_to = null;

                //to = "";
                if ("".equals(to)) {
                    /*
                     //até a versão 1.6.2
                     query_to = "SELECT emails FROM tecnica_view WHERE cidade = '" + rs.getString(1) + "'";
                     rs_to = Db_class.mysql_result(conn, query_to);
                     rs_to.next();
                     to = rs_to.getString(1);
                     rs_to = null;
                     */
                    //query_to = "SELECT email from cop_info_tecnica WHERE cidade = '" + rs.getString(1) + "'";
                    query_to = "SELECT distinct email from vw_cop_info_tecnica WHERE cidade = '" + rs.getString(1) + "'";
                    query_to = query_to + sMailCondition;

                    rs_to = Db_class.mysql_result(conn, query_to);
                    while (rs_to.next()) {
                        to = to + rs_to.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                    }
                    rs_to = null;
                }

                cc = "";

            } else {

                assunto = "Sinalização Ouvidoria - " + rs.getString(1) + " - "
                        + rs.getString(6) + " - " + rs.getString(5);

                /*
                 query_to = "SELECT emails "
                 + "FROM parceiras_view_novo "
                 + "WHERE cidade = '" + rs.getString(1) + "' "
                 + "and parceira regexp ('" + rs.getString(6) + "')";

                 System.out.println(query_to);
                 rs_to = Db_class.mysql_result(conn, query_to);
                 rs_to.next();
                 to = rs_to.getString(1);
                 */
                /*
                 query_to = "select distinct cd.cop_info, cip.parceira, cip.email "
                 + "from ouvidoria.cidades_novo cd "
                 //+ "left join ouvidoria.cop_info_parceiras cip on cd.cop_info = cip.cidade "
                 + "left join ouvidoria.vw_cop_info_parceiras cip on cd.cop_info = cip.cidade "
                 + "WHERE cd.cop_info = '" + rs.getString(1) + "' "
                 + "and cip.parceira regexp ('" + rs.getString(6) + "')";
                 */
                query_to = "select distinct cip.cidade, cip.parceira, cip.email "
                        + "from ouvidoria.vw_cop_info_parceiras cip "
                        + "where cip.cidade like '" + rs.getString(1) + "%' "
                        + "and cip.cidade like '%" + rs.getString(11) + "' "
                        //+ "and cip.parceira regexp ('" + rs.getString(6) + "')";
                        + "and cip.parceira = '" + rs.getString(6) + "'";

                query_to = query_to + sMailCondition;

                rs_to = Db_class.mysql_result(conn, query_to);
                while (rs_to.next()) {
                    to = to + rs_to.getString(3).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                }
                rs_to = null;

                if (to.isEmpty()) {
                    query_to = "select distinct cd.cop_info, cip.parceira, cip.email "
                            + "from ouvidoria.cidades_novo cd "
                            //+ "left join ouvidoria.cop_info_parceiras cip on cd.cop_info = cip.cidade "
                            + "left join ouvidoria.vw_cop_info_parceiras cip on cd.cop_info = cip.cidade "
                            + "WHERE cd.cop_info = '" + rs.getString(1) + "' "
                            //+ "and cip.parceira regexp ('" + rs.getString(6) + "')";
                            + "and cip.parceira = '" + rs.getString(6) + "'";
                    query_to = query_to + sMailCondition;

                    rs_to = Db_class.mysql_result(conn, query_to);
                    while (rs_to.next()) {
                        to = to + rs_to.getString(3).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                    }
                    rs_to = null;
                }

                String query_cc = "";

                //query_cc = "select email from cop_info_tecnica ";
                query_cc = "select distinct email from vw_cop_info_tecnica ";
                query_cc = query_cc + "where cidade like '" + rs.getString(1) + "%' ";
                query_cc = query_cc + "and ";
                query_cc = query_cc + "(";
                query_cc = query_cc + "cidade like '%" + rs.getString(11) + "' ";
                query_cc = query_cc + "or cidade like concat('%',";
                query_cc = query_cc + "(";
                query_cc = query_cc + "select grupo from tb_cidadeXdespachoXgrupo ";
                query_cc = query_cc + "where cidade = '" + rs.getString(1) + "' ";
                query_cc = query_cc + "and area_despacho = '" + rs.getString(11) + "' ";
                query_cc = query_cc + "limit 1";
                query_cc = query_cc + ")";
                query_cc = query_cc + ")";
                query_cc = query_cc + ")";
                query_cc = query_cc + sMailCondition;

                rs_cc = Db_class.mysql_result(conn, query_cc);
                while (rs_cc.next()) {
                    cc = cc + rs_cc.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                }
                rs_cc = null;

                //cc = "";
                if ("".equals(cc)) {
                    /*
                     //até a versão 1.6.2
                     query_cc = "SELECT emails FROM tecnica_view WHERE cidade = '" + rs.getString(1) + "'";
                     rs_cc = Db_class.mysql_result(conn, query_cc);
                     rs_cc.next();
                     cc = rs_cc.getString(1) + ";";
                     rs_cc = null;
                     */
                    //query_cc = "SELECT email from cop_info_tecnica WHERE cidade = '" + rs.getString(1) + "'";
                    query_cc = "SELECT distinct email from vw_cop_info_tecnica WHERE cidade = '" + rs.getString(1) + "'";
                    query_cc = query_cc + sMailCondition;

                    rs_cc = Db_class.mysql_result(conn, query_cc);
                    while (rs_cc.next()) {
                        cc = cc + rs_cc.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                    }
                    rs_cc = null;
                }
            }

            //to = ""; //forçando para testes
            if (to.isEmpty()) {
                if (rs.getString(6).equals("NET")) {
                    /*
                     global.show_warning_message("No e-mail, o campo Para/To está vazio.\n"
                     + "Verifique se o Técnico existe.\n\n"
                     + "A janela de envio lhe será apresentada.");
                     */
                    global.show_warning_message("No e-mail, o campo Para/To está vazio.\n"
                            + "Verifique se o Técnico existe.\n\n"
                            + "O e-mail não será preparado.");
                } else {
                    /*
                     global.show_warning_message("No e-mail, o campo Para/To está vazio.\n"
                     + "Verifique se o Parceiro existe.\n\n"
                     + "A janela de envio lhe será apresentada.");
                     */
                    global.show_warning_message("No e-mail, o campo Para/To está vazio.\n"
                            + "Verifique se o Parceiro existe.\n\n"
                            + "O e-mail não será preparado.");
                }
            }

            if (!to.isEmpty()) { //se o PARA/TO não estiver VAZIO, processa o e-mail
                SendMail.send(final_html, assunto, to, cc, mn);

                //String query_update = "UPDATE painel SET email = 'Sim' WHERE id = " + current_id;
                String query_update = "UPDATE painel SET email = 'Sim' "
                        + "where id_mestre = " + current_id + " "
                        + "and fl_n3 = " + ifl_n3;

                Db_class.mysql_insert(query_update, conn);

                insert_obs(get_obs_hist() + " Enviado e-mail de sinalização.");

                set_ult_acomp();

                if (current_status_caso.equals("Encaixe")) {

                    altera_status_caso("Em Acompanhamento", "", "");
                    this.dispose();

                } else {

                    //int tab_index = jTabbedPane1.getSelectedIndex();
                    //this.dispose();
                    //tratar tr = new tratar(mn, true, current_id, pn, tab_index, cop);
                    fill_panel();

                }
            }

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
            global.show_error_message(
                    "Erro identificado.\n\n"
                    + "Detalhes\n"
                    + ex.getMessage()
            );
        }
    }//GEN-LAST:event_botao_emailActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if (!mn.check_version()) {
            return;
        }

        novo_tecnico nt = new novo_tecnico(mn, true, this, cidade.getText(), cop);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (!mn.check_version()) {

            return;

        }

        attach_mail ae = new attach_mail(current_id, this, cop, ifl_n3);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void open_mailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_open_mailActionPerformed

        if (!mn.check_version()) {

            return;

        }

        try {
            //ALTERAR_BCC_OK
            Desktop.getDesktop().open(new File("\\\\10.5.9.180\\GRVCOPCC$\\NET\\Ouvidoria - " + current_id + ".msg"));
        } catch (IOException ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_open_mailActionPerformed

    private void botao_procurarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botao_procurarMouseClicked

        if (!mn.check_version()) {

            return;

        }

        JPopupMenu pop = new JPopupMenu();

        if (cop.equals("COP NET")) {

            JMenuItem menu_tecnico = new JMenuItem("Técnicos");
            menu_tecnico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png")));

            menu_tecnico.addActionListener(new procura_tec_listener());

            pop.add(menu_tecnico);

            pop.add(new JSeparator());

            JMenuItem menu_parceira = new JMenuItem("Parceiras");
            menu_parceira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lorry.png")));
            menu_parceira.addActionListener(new procura_parceira_listener());
            pop.add(menu_parceira);

            pop.show(evt.getComponent(), evt.getX(), evt.getY());
            pop.setSize(300, 200);

        } else {

            parceiras_dth pdth = new parceiras_dth(mn, true, cidade.getText(), tr);

        }

        //

    }//GEN-LAST:event_botao_procurarMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        limpa_tec();
        altera_tec(true);
        insert_obs(get_obs_hist() + "Técnico removido do caso.");

        LiberaSMS();
        insert_obs(get_obs_hist() + "Permissão para enviar SMS concedida");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void LiberaSMS() {

        try {

            Connection conn = Db_class.mysql_conn();

            String query = "UPDATE ouvidoria.painel SET sms = 'Liberado' "
                    + "WHERE id = " + current_id;

            Db_class.mysql_insert(query, conn);

        } catch (Exception ex) {
            global.show_error_message("Não foi possível liberar o técnico para receber SMS.\n\n"
                    + "Erro:\n" + ex.getMessage());
        }

    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {

            Connection conn = Db_class.mysql_conn();

            String query = "UPDATE painel SET obs_horario = '" + obs_horario.getText() + "'"
                    + "WHERE id = " + current_id;

            Db_class.mysql_insert(query, conn);

            global.show_message("Particularidade alterada com sucesso");

            insert_obs(get_obs_hist() + " Particularidade alterada para: \"" + obs_horario.getText() + "\"");

            set_ult_acomp();

            pn.atualiza_painel();

            //this.dispose();
            Db_class.close_conn(conn);

            //tratar tr = new tratar(mn, true, current_id, pn, 0, cop);
            fill_panel();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        int[] selected_rows = real_tab_os.getSelectedRows();

        if (selected_rows.length == 0) {
            global.show_error_message("É necessário selecionar pelo menos uma OS");
            return;
        }

        if (ifl_n3 != 1) {
            for (int i = 0; i <= (selected_rows.length - 1); i++) {
                if (!real_tab_os.getValueAt(selected_rows[i], 2).equals("Aberta")) {
                    global.show_error_message("Apenas OSs abertas podem ser baixadas.\n\n"
                            + "Caso deseje trocar o código de baixa, clique em 'Reverter OS'");
                    return;
                }
            }
            baixa_os bo = new baixa_os(mn, true, this, cop);
        } else {
            for (int i = 0; i <= (selected_rows.length - 1); i++) {
                if (real_tab_os.getValueAt(selected_rows[i], 4).equals("Sim")) {
                    global.show_error_message("Apenas OSs abertas no N3 podem ser baixadas.\n\n"
                            + "Para retornar o tratamento para o N2, clique em 'Reverter OS'.\n"
                            + "Caso queira substituir o código de baixa do N2 ou do N3, clique em 'Reverter Baixa'."
                    );
                    return;
                }
            }
            baixa_os_n3 bo = new baixa_os_n3(mn, true, this, cop);
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        nova_os no = new nova_os(mn, true, this, contrato.getText(), current_id,
                current_janela, current_date, cop);

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        int[] selected_rows = real_tab_os.getSelectedRows();

        if (selected_rows.length == 0) {

            global.show_error_message("É necessário selecionar pelo menos uma OS");

            return;

        }

        String[] oss = new String[selected_rows.length];
        String[] tipos = new String[selected_rows.length];

        for (int i = 0; i <= (selected_rows.length - 1); i++) {

            oss[i] = (String) real_tab_os.getValueAt(selected_rows[i], 0);
            tipos[i] = (String) real_tab_os.getValueAt(selected_rows[i], 1);

            if (real_tab_os.getValueAt(selected_rows[i], 2).equals("Aberta")) {

                global.show_error_message("Apenas OSs fechadas podem ser reagendadas");

                return;

            }

        }

        nova_os no = new nova_os(mn, true, this, contrato.getText(), current_id,
                true, oss, tipos, current_janela, current_date, cop);

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        int result = global.dialog_question("Tem certeza que deseja encerrar esse caso?\n\n"
                + "Essa alteração não poderá ser desfeita.");

        if (result != 0) {

            return;

        }

        encerra_form ef = new encerra_form(mn, true, false, this, "Encerrado - Os Duplicada/Cancelada");

    }//GEN-LAST:event_jButton11ActionPerformed

    private void botao_encerraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_encerraActionPerformed

        int result = global.dialog_question("Tem certeza que deseja encerrar esse caso?\n\n"
                + "Essa alteração não poderá ser desfeita.");

        if (result != 0) {

            return;

        }

        encerra_form ef = new encerra_form(mn, true, false, this, "Encerrado");

    }//GEN-LAST:event_botao_encerraActionPerformed

    private void jbt_reverter_osActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_reverter_osActionPerformed

        int result = global.dialog_question("Tem certeza que deseja reverter a baixa desta(s) OS(s)?");

        if (result != 0) {
            return;
        }

        if (ifl_n3 != 1) {
            finaliza_os("null", "null", "", true, -1, -1, 2);
        } else {
            finaliza_os("null", "null", "", true, -1, -1, 3);
        }

        //finaliza_os("null", "null", "", true, -1, -1, 2);

    }//GEN-LAST:event_jbt_reverter_osActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        String nome_net = "";

        String copia = "********* CASOS CRITICOS **********\n"
                + "Contrato: " + contrato.getText() + "\n"
                + "Cidade: " + cidade.getText() + "\n"
                + "Area: " + area.getText() + "\n"
                + "Canal: " + canal.getText() + "\n"
                + "Data: " + data.getText() + "\n"
                + "Janela: " + janela.getText() + "\n"
                + "Solicitante: " + solic.getText() + "\n"
                + "Nome Cliente: " + nome_cli.getText() + "\n"
                + "Particularidade: " + obs_horario.getText() + "\n"
                + "Os's: " + get_oss() + "\n"
                + "Nome Técnico: " + tecnico.getText() + "\n"
                + "Login Técnico: " + login_tec.getText() + "\n"
                + "EPO: " + epo.getText() + "\n"
                + "" + mn.get_nome() + " - CASOS CRITICOS BCC - " + nome_net;

        StringSelection clip_obs = new StringSelection(copia);

        CLIPBOARD.setContents(clip_obs, null);

    }//GEN-LAST:event_jButton13ActionPerformed

    private void botao_procurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_procurarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botao_procurarActionPerformed

    private void jbtn_reverter_baixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn_reverter_baixaActionPerformed

        int[] selected_rows = real_tab_os.getSelectedRows();

        if (selected_rows.length == 0) {
            global.show_error_message("É necessário selecionar pelo menos uma OS");
            return;
        }

        baixa_os_reversao bo = new baixa_os_reversao(mn, true, this, cop);
    }//GEN-LAST:event_jbtn_reverter_baixaActionPerformed

    public String get_oss() {

        int linhas_os = real_tab_os.getRowCount();

        String os_str = "";

        for (int i = 0; i <= (linhas_os - 1); i++) {

            String current_os = (String) real_tab_os.getValueAt(i, 0);
            String current_cod = (String) real_tab_os.getValueAt(i, 3);

            if (i == 0) {

                os_str = current_os + " - " + current_cod;

            } else {

                os_str = os_str + "\n" + current_os + " - " + current_cod;

            }

        }

        return os_str;

    }

    public void finaliza_os(String status, String codigo, String obs, boolean is_reverting, int iAcao_OMS, int iAcao_Contrato, int iNivelOrigem) {

        try {
            Connection conn;
            ResultSet rs;

            conn = Db_class.mysql_conn();

            //controle de N3 e SLA
            boolean bN3 = false;
            String sSLA = "";
            int iSLA = 0;

            //variaveis diversas
            String sSQL = "";
            String numeros = "";
            int cont = 0;
            String codigo_str = codigo;
            String status_str = status;

            //critica dos parametros
            int[] selected_oss = real_tab_os.getSelectedRows();
            if (selected_oss.length == 0) {
                global.show_error_message("É necessário selecionar pelo menos um Serviço");
                return;
            }

            //se reversao ou se COP é DTH (sem Acao OMS e/ou Contrato)
            if (is_reverting || iAcao_OMS == 0 || iAcao_Contrato == 0) {
                iAcao_OMS = 0;
                iAcao_Contrato = 0;
                bN3 = false;
            }
            //preparando atualizacao
            if (!codigo.equals("null")) {
                codigo = "'" + codigo + "'";
            }
            if (!status.equals("null")) {
                status = "'" + status + "'";
            }

            //Preparando para registrar as ações
            LogS log = new LogS();
            //

            if (!is_reverting) {
                //N3 e SLA
                sSQL = "select 1 from ouvidoria.tb_acao_oms \n";
                sSQL = sSQL + "where id = " + iAcao_OMS + "\n";
                sSQL = sSQL + "and fl_n3 = 1";

                rs = Db_class.mysql_result(conn, sSQL);
                if (rs.next()) {
                    //registrado para N3
                    bN3 = true;
                }
                rs.close();

                if (bN3) {
                    //SLA
                    sSQL = "select \n";
                    sSQL = sSQL + "case unidade \n";
                    sSQL = sSQL + "when 'H' then 'Hour' \n";
                    sSQL = sSQL + "when 'D' then 'Day' \n";
                    sSQL = sSQL + "when 'M' then 'Minute' \n";
                    sSQL = sSQL + "else '?' \n";
                    sSQL = sSQL + "end as unidade, quantidade \n";
                    sSQL = sSQL + "from ouvidoria.tb_acao_sla \n";
                    sSQL = sSQL + "where id in \n";
                    sSQL = sSQL + "( \n";
                    sSQL = sSQL + "select id_sla \n";
                    sSQL = sSQL + "from ouvidoria.tb_acao_omsxcontratoxsla \n";
                    sSQL = sSQL + "where id_oms = " + iAcao_OMS + " \n";
                    sSQL = sSQL + "and id_contrato = " + iAcao_Contrato + " \n";
                    sSQL = sSQL + ") \n";
                    sSQL = sSQL + "limit 1 \n";

                    rs = Db_class.mysql_result(conn, sSQL);
                    if (rs.next()) {
                        sSLA = rs.getString(1);
                        iSLA = rs.getInt(2);

                        //convertendo para minutos
                        if (sSLA.equals("Day")) {
                            iSLA = iSLA * 24 * 60;
                        } else {
                            if (sSLA.equals("Hour")) {
                                iSLA = iSLA * 60;
                            }
                        }
                        sSLA = "Minute";
                    }
                    rs.close();
                }

                if (sSLA.equals("?")) {
                    global.show_error_message("O SLA não foi reconhecido.\n\nPor favor, informe isso à GINT.");
                    return;
                }
            }

            for (int linha : selected_oss) {
                String current_os = (String) real_tab_os.getValueAt(linha, 0);
                String sTipo_OS = (String) real_tab_os.getValueAt(linha, 1);

                //atualizando o caso
                //painel central
                String query_baixa = "UPDATE painel SET \n"
                        + "status_os = " + status + ", "
                        + "cod_baixa = " + codigo + ", "
                        + "id_acao_oms = " + iAcao_OMS + ", "
                        + "id_acao_contrato = " + iAcao_Contrato + ", "
                        + "fl_n3 = " + bN3 + " \n"
                        //+ "fl_n3 = " + (iNivelOrigem == 3 ? 1 : bN3) + " \n"
                        //+ "fl_bx_n2 = " + (iNivelOrigem==3?0:1) + " \n"
                        + "WHERE id_mestre = " + current_id + " "
                        + "and num_os = '" + current_os + "' "
                        + "and tipo_os = '" + sTipo_OS + "'";

                Db_class.mysql_insert(query_baixa, conn);

                //detalhe N3
                if (iNivelOrigem == 3) {
                    query_baixa = "UPDATE tb_acao_registro_n3 SET \n"
                            + "fl_bx_n2 = 0 \n"
                            + "WHERE id_mestre = " + current_id + " "
                            + "and num_os = '" + current_os + "' "
                            + "and tipo_os = '" + sTipo_OS + "'";

                    Db_class.mysql_insert(query_baixa, conn);
                }

                //acumulador de detalhes para Obs
                if (cont == 0) {
                    numeros = current_os + " (" + sTipo_OS + ")";
                } else {
                    numeros = numeros + ", " + current_os + " (" + sTipo_OS + ")";
                }

                //caso N3
                if (!is_reverting) {
                    //registrando baixa de OS
                    //log.LogComum_Inclusao(current_id, Integer.parseInt(current_os), sTipo_OS, 2, 2, 2);
                    log.LogComum_Inclusao(current_id, current_os, sTipo_OS, 2, iNivelOrigem, iNivelOrigem);

                    if (bN3) {
                        if (iNivelOrigem == 3) {
                            //excluindo qualquer registro N3 para evitar réplicas
                            sSQL = "delete from ouvidoria.tb_acao_registro_n3 \n";
                            sSQL = sSQL + "where id_mestre = " + current_id + " \n";
                            sSQL = sSQL + "and num_os = " + current_os + " \n";
                            sSQL = sSQL + "and tipo_os = '" + sTipo_OS.trim() + "'";

                            Db_class.mysql_insert(sSQL, conn);
                        }

                        //os valores-padrao de FL_BX_N2 e FL_BX_TEC_N3 estao definidos na tabela
                        sSQL = "insert into ouvidoria.tb_acao_registro_n3 \n";
                        sSQL = sSQL + "( \n";
                        sSQL = sSQL + "id_mestre, num_os, tipo_os, dt_baixa, dt_prevista, dt_media \n";
                        sSQL = sSQL + ") \n";
                        sSQL = sSQL + "values \n";
                        sSQL = sSQL + "( \n";
                        sSQL = sSQL + current_id + ", " + current_os + ", '" + sTipo_OS.trim() + "', " + "\n";
                        sSQL = sSQL + "now()" + ", " + "DATE_ADD(now(), INTERVAL " + iSLA + " " + sSLA + "), \n";
                        sSQL = sSQL + "DATE_ADD(now(), INTERVAL " + iSLA / 2 + " " + sSLA + ")" + " \n";
                        sSQL = sSQL + ") \n";

                        Db_class.mysql_insert(sSQL, conn);

                        //registrando movimentacao para o N3
                        //log.LogComum_Inclusao(current_id, Integer.parseInt(current_os), sTipo_OS, 5, 2, 3);
                        if (iNivelOrigem != 3) {
                            log.LogComum_Inclusao(current_id, current_os, sTipo_OS, 5, iNivelOrigem, 3);
                            //ZeraUsuarioTratando(current_id, current_os, sTipo_OS);
                            ZeraUsuarioTratando(current_id, bN3);
                        }
                    }
                } else {
                    //registrando reversão de OS
                    //log.LogComum_Inclusao(current_id, Integer.parseInt(current_os), sTipo_OS, 4, 2, 2);
                    log.LogComum_Inclusao(current_id, current_os, sTipo_OS, 4, iNivelOrigem, iNivelOrigem);

                    //se reversao ou se COP é DTH (sem Acao OMS e/ou Contrato)
                    sSQL = "delete from ouvidoria.tb_acao_registro_n3 \n";
                    sSQL = sSQL + "where id_mestre = " + current_id + " \n";
                    sSQL = sSQL + "and num_os = " + current_os + " \n";
                    sSQL = sSQL + "and tipo_os = '" + sTipo_OS.trim() + "'";

                    Db_class.mysql_insert(sSQL, conn);

                    //registrando movimentacao para o N2
                    //log.LogComum_Inclusao(current_id, Integer.parseInt(current_os), sTipo_OS, 5, 3, 2);
                    if (iNivelOrigem != 2) {
                        log.LogComum_Inclusao(current_id, current_os, sTipo_OS, 5, iNivelOrigem, 2);
                        //ZeraUsuarioTratando(current_id, current_os, sTipo_OS);
                        ZeraUsuarioTratando(current_id, bN3);
                    }
                }

                cont++;
            }

            //fechando o registro as ações
            log = null;
            //

            if (!obs.equals("")) {
                obs = "\n" + obs;
            }

            if (is_reverting) {
                if (selected_oss.length == 1) {
                    insert_obs(get_obs_hist() + "Reversão da baixa da OS " + numeros + obs);
                } else {
                    insert_obs(get_obs_hist() + "Reversão da baixa das OS`s " + numeros + obs);
                }
            } else if (selected_oss.length == 1) {
                insert_obs(get_obs_hist() + "Baixa da OS " + numeros + " como " + status_str + " "
                        + "com o código " + codigo_str + obs);
            } else {
                insert_obs(get_obs_hist() + "Baixa das OS`s " + numeros + " como " + status_str + " "
                        + "com o código " + codigo_str + obs);
            }

            if (is_reverting) {
                global.insert_prod(current_id, "Reversão de OS", mn);
                global.show_message("Os's revertidas com sucesso");
            } else {
                global.insert_prod(current_id, "Baixa de OS", mn);
                global.show_message("Os's baixadas com sucesso");
            }

            //this.dispose();
            //tratar tr = new tratar(mn, true, current_id, pn, 4, cop);
            fill_panel();

            Db_class.close_conn(conn);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            /*
             Logger.getLogger(tratar.class
             .getName()).log(Level.SEVERE, null, ex);
             */
            global.show_error_message("Problemas na baixa de OS.\n\nPor favor, informe isso à GINT.\n\nErro: " + ex.getMessage());
        }
    }

    //public void ZeraUsuarioTratando(int current_id, String current_os, String sTipo_OS) {
    public void ZeraUsuarioTratando(int current_id, boolean bN3) {
        try {
            Connection conn;

            conn = Db_class.mysql_conn();

            String query_upd = "UPDATE painel SET \n"
                    + "user_mark = '' "
                    + "WHERE id_mestre = " + current_id + " "
                    //+ "and num_os = '" + current_os + "' "
                    //+ "and tipo_os = '" + sTipo_OS + "'";
                    + "and fl_n3 = " + bN3;

            Db_class.mysql_insert(query_upd, conn);
            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            global.show_error_message("Problemas para limpar o campo de 'Usuário Tratando'.\n\nErro: " + ex.getMessage());
        }
    }

    public void finaliza_os_n3(String obs, int iAcomp_Status, int iAcomp_Acao, String sAcomp_Status, String sAcomp_Acao, Integer iBx_Tec) {

        try {
            Connection conn;
            ResultSet rs;

            conn = Db_class.mysql_conn();

            //controle de N3 e SLA
            boolean bN3 = false;
            String sSLA = "";
            int iSLA = 0;

            //variaveis diversas
            String sSQL = "";
            String numeros = "";
            int cont = 0;

            //critica dos parametros
            int[] selected_oss = real_tab_os.getSelectedRows();
            if (selected_oss.length == 0) {
                global.show_error_message("É necessário selecionar pelo menos um Serviço");
                return;
            }

            //Preparando para registrar as ações
            LogS log = new LogS();
            //

            for (int linha : selected_oss) {
                String current_os = (String) real_tab_os.getValueAt(linha, 0);
                String sTipo_OS = (String) real_tab_os.getValueAt(linha, 1);

                if (cont == 0) {
                    numeros = current_os + " (" + sTipo_OS + ")";
                } else {
                    numeros = numeros + ", " + current_os + " (" + sTipo_OS + ")";
                }

                sSQL = "update ouvidoria.tb_acao_registro_n3 \n";
                sSQL = sSQL + "set \n";
                sSQL = sSQL + "dt_baixa_n3 = now(), \n";
                sSQL = sSQL + "id_acomp_status = " + iAcomp_Status + ", \n";
                sSQL = sSQL + "id_acomp_acao = " + iAcomp_Acao + ", \n";
                sSQL = sSQL + "fl_bx_tec_n3 = " + iBx_Tec + " \n";
                sSQL = sSQL + "where id_mestre = " + current_id + " \n";
                sSQL = sSQL + "and num_os = " + current_os + " \n";
                sSQL = sSQL + "and tipo_os = '" + sTipo_OS.trim() + "' \n";

                Db_class.mysql_insert(sSQL, conn);

                //registrando baixa de OS
                //log.LogComum_Inclusao(current_id, Integer.parseInt(current_os), sTipo_OS, 2, 3, 3);
                log.LogComum_Inclusao(current_id, current_os, sTipo_OS, 2, 3, 3);

                cont++;
            }

            //fechando o registro as ações
            log = null;
            //

            if (!obs.equals("")) {
                obs = "\n" + obs;
            }

            if (selected_oss.length == 1) {
                insert_obs(get_obs_hist() + "Baixa da OS " + numeros + " como " + sAcomp_Status + " "
                        + "com o código " + sAcomp_Acao + obs);
            } else {
                insert_obs(get_obs_hist() + "Baixa das OS`s " + numeros + " como " + sAcomp_Status + " "
                        + "com o código " + sAcomp_Acao + obs);
            }

            global.insert_prod(current_id, "Baixa de OS", mn);
            global.show_message("Os's baixadas com sucesso");

            //this.dispose();
            //tratar tr = new tratar(mn, true, current_id, pn, 4, cop);
            fill_panel();

            Db_class.close_conn(conn);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            /*
             Logger.getLogger(tratar.class
             .getName()).log(Level.SEVERE, null, ex);
             */
            global.show_error_message("Problemas na baixa de OS.\n\nPor favor, informe isso à GINT.\n\nErro: " + ex.getMessage());

        }
    }

    public class procura_tec_listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            procurar_tecnico pt = new procurar_tecnico(mn, true, cidade.getText(), area.getText(), tr);

        }

    }

    public class procura_parceira_listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            parceiras pr = new parceiras(mn, true, cidade.getText(), tr);

        }

    }

    public void altera_agenda(String cod_baixa, String hora) {

        try {

            String data_insert = global.get_simple_date(data_alter.getDate());
            String tipo_os_insert = (String) tipo_os_agenda.getSelectedItem();
            String janela_insert = (String) janela_alter.getSelectedItem();
            String partic_insert = obs_horario_alter.getText();

            if (data_alter.getDate().equals(current_date) && janela_insert.equals(current_janela)) {

                global.show_error_message("É necessário escolher um agendamento diferente do atual!");
                return;

            }

            int resposta = global.dialog_question("Tem certeza que deseja alterar o agendamento deste caso?");

            if (resposta != 0) {

                return;

            }

            Connection conn = Db_class.mysql_conn();

            String query_grupo = "SELECT grp_descricao FROM tipo_os_novo WHERE "
                    + "tipo_os = '" + tipo_os_insert + "'";

            ResultSet rs_grupo = Db_class.mysql_result(conn, query_grupo);

            rs_grupo.next();
            String grupo_os_insert = rs_grupo.getString(1);

            String obs_agenda = "";

            if (obs_agendamento.getText().equals("")) {

                obs_agenda = get_obs_hist() + "Agendamento alterado para "
                        + global.get_br_date(data_alter.getDate()) + " janela das " + janela_insert + "\n\n";

            } else {

                obs_agenda = get_obs_hist() + "Agendamento alterado para "
                        + global.get_br_date(data_alter.getDate()) + " janela das " + janela_insert + "\n\n"
                        + get_obs_hist() + obs_agendamento.getText() + "\n\n";

            }

            String final_obs_agenda = obs_agenda.replace("'", "");

            String query = "INSERT INTO `ouvidoria`.`painel`\n"
                    + "(`contrato`,\n"
                    + "`cidade`,\n"
                    + "`area_despacho`,\n"
                    + "`data_acomp`,\n"
                    + "`janela`,\n"
                    + "`tipo_os`,\n"
                    + "`solicitante`,\n"
                    + "`nome_cli`,\n"
                    + "`tipo_trat`,\n"
                    + "`descr_indevido`,\n"
                    + "`canal`,\n"
                    + "`obs_horario`,\n"
                    + "`obs`,\n"
                    + "`cluster`,\n"
                    + "`cop`,\n"
                    + "`date_time_insert`,\n"
                    + "`user_insert`,\n"
                    + "`nome_insert`,\n"
                    + "`sistema`,\n"
                    + "`status`,\n"
                    + "`ativo`,\n"
                    + "`tentativa`,\n"
                    + "`status_monitoria`,\n"
                    + "`ultimo_acomp`,\n"
                    + "`hora_ultimo_acomp`,\n"
                    + "grupo_os,\n"
                    + "ultimo_acomp_login \n"
                    + ") \n"
                    + "SELECT "
                    + "ifnull(contrato,'') as Contrato,\n"
                    + "ifnull(cidade,'') as Cidade,\n"
                    + "ifnull(area_despacho,'') as Area_Despacho,\n"
                    + "'" + data_insert + "',\n"
                    + "'" + janela_insert + "',\n"
                    + "'" + tipo_os_insert + "',\n"
                    + "ifnull(solicitante,'') as Solicitante,\n"
                    + "ifnull(nome_cli,'') as Nome_Cli,\n"
                    + "ifnull(tipo_trat,'') as Tipo_Trat,\n"
                    + "ifnull(descr_indevido,'') as Descr_Indevido,\n"
                    + "ifnull(canal,'') as Canal,\n"
                    + "'" + partic_insert + "',\n"
                    + "CONCAT('" + final_obs_agenda + "', obs),\n"
                    + "ifnull(cluster,'') as Cluster,\n"
                    + "ifnull(cop,'') as Cop,\n"
                    + "current_timestamp,\n"
                    + "'" + mn.get_login() + "',\n"
                    + "'" + mn.get_nome() + "',\n"
                    + "ifnull(sistema,'') as Sistema,\n"
                    + "'Encaixe',\n"
                    + "ifnull(ativo,'') as Ativo,\n"
                    + "ifnull(tentativa,'') as Tentativa,\n"
                    + "null,\n"
                    + "'" + mn.get_nome() + "',\n"
                    + "current_timestamp, "
                    + "'" + grupo_os_insert + "', \n "
                    + "'" + mn.get_login() + "' \n"
                    + "FROM painel WHERE id = " + current_id;

            Db_class.mysql_insert(query, conn);

            //String query_id = "SELECT LAST_INSERT_ID() FROM painel";
            //ResultSet rs_last_id = Db_class.mysql_result(conn, query_id);
            //rs_last_id.next();
            //long last_id = rs_last_id.getLong(1);
            String query_update = "UPDATE painel SET status = 'Encerrado', obs = CONCAT('" + final_obs_agenda + "', obs) "
                    + "WHERE id = " + current_id;

            set_ult_acomp();

            Db_class.mysql_insert(query_update, conn);

            this.dispose();

            altera_status_caso("Encerrado", cod_baixa, hora);

            Db_class.close_conn(conn);

            insert_prod(current_id, "Alteração de agendamento");

            pn.atualiza_painel();

            //tratar tr = new tratar(mn, true, (int) last_id, pn, tab_index);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insert_obs(String text) {
        try {
            Connection conn = Db_class.mysql_conn();

            String query;
            query = "UPDATE painel SET obs = CONCAT('" + text + "\n\n', obs)"
                    //+ "WHERE id = " + current_id;
                    + "WHERE id_mestre = " + current_id;
            //+ "and fl_n3 = " + ifl_n3;

            Db_class.mysql_insert(query, conn);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String get_obs_hist() {

        String txt = mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": ";

        return txt;

    }

    public void set_ult_acomp() {
        try {
            Connection conn = Db_class.mysql_conn();

            String query = "UPDATE painel SET ultimo_acomp = '" + mn.get_nome() + "', "
                    + "hora_ultimo_acomp = current_timestamp, "
                    + "ultimo_acomp_login = '" + mn.get_login() + "' "
                    //+ "WHERE id = " + current_id;
                    + "WHERE id_mestre = " + current_id + " "
                    + "and fl_n3 = " + ifl_n3;

            Db_class.mysql_insert(query, conn);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void altera_monitoria() {
        try {

            String status_monit = (String) sts_monit.getSelectedItem();

            if (status_monit.equals(current_status_monit)) {

                global.show_error_message("É necessário selecionar um status diferente do status atual!");
                return;
            }

            Connection conn = Db_class.mysql_conn();

            String query = "UPDATE painel SET status_monitoria = '" + status_monit + "' "
                    + "WHERE id = " + current_id;

            Db_class.mysql_insert(query, conn);

            if (status_monit.equals("")) {

                insert_obs(get_obs_hist() + "Status de monitoria alterado para \"Vazio\"");

            } else {

                insert_obs(get_obs_hist() + "Status de monitoria alterado para \"" + status_monit + "\"");

            }

            set_ult_acomp();

            Db_class.close_conn(conn);

            insert_prod(current_id, "Alteração Status Monitoria");

            global.show_message("Status de monitoria alterado com sucesso!");

            pn.atualiza_painel();

            int tab_index = jTabbedPane1.getSelectedIndex();

            //this.dispose();
            //tratar tr = new tratar(mn, true, current_id, pn, tab_index, cop);
            fill_panel();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void altera_status_caso(String status_caso, String depto_baixa, String hora_baixa) {
        try {

            /*
             //por conta da separação entre N2 e N3, isso não pode ser empecilho
             if (status_caso.equals(current_status_caso)) {

             global.show_error_message("É necessário selecionar um status diferente do status atual!");
             return;
             }
             */
            Connection conn = Db_class.mysql_conn();

            String query_part = "";

            if (status_caso.equals("Encerrado") || status_caso.equals("Encerrado - Os Duplicada/Cancelada")) {

                query_part = ", depto_baixa = '" + depto_baixa + "', hora_baixa = '" + hora_baixa + "' ";

            }

            String query = "UPDATE painel SET status = '" + status_caso + "' " + query_part
                    //+ "WHERE id = " + current_id;
                    + "where id_mestre = " + current_id + " "
                    + "and fl_n3 = " + ifl_n3;

            Db_class.mysql_insert(query, conn);

            //Preparando para registrar as ações
            LogS log = new LogS();
            log.LogComum_InclusaoEncerramento(current_id, ifl_n3);
            log = null;
            //

            insert_obs(get_obs_hist() + "Status do caso alterado para \"" + status_caso + "\"");

            set_ult_acomp();

            Db_class.close_conn(conn);

            insert_prod(current_id, "Alteração Status Caso");

            global.show_message("Status do caso alterado com sucesso!");

            pn.atualiza_painel();

            int tab_index = jTabbedPane1.getSelectedIndex();

            //this.dispose();
            //tratar tr = new tratar(mn, true, current_id, pn, tab_index, cop);
            fill_panel();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void select_tab_index(int index) {

        jTabbedPane1.setSelectedIndex(index);

    }

    public void set_tecnico(String[] array_tec) {

        login_tec.setText(array_tec[0]);
        tecnico.setText(array_tec[1]);
        epo.setText(array_tec[2]);
        telefone.setText(array_tec[3]);
        obs_tec.setText(array_tec[4]);
        turno_tec.setText(array_tec[5]);
        tipo_tec.setText(array_tec[6]);

    }

    public void limpa_tec() {

        login_tec.setText("");
        tecnico.setText("");
        epo.setText("");
        telefone.setText("");
        obs_tec.setText("");
        turno_tec.setText("");
        tipo_tec.setText("");

    }

    public void altera_tec(boolean is_removing) {
        try {

            Connection conn = Db_class.mysql_conn();

            String query = "UPDATE painel SET epo = '" + epo.getText() + "', "
                    + "tecnico = '" + tecnico.getText() + "', "
                    + "tipo_tecnico = '" + tipo_tec.getText() + "', "
                    + "telefone_tec = '" + telefone.getText() + "', "
                    + "obs_tec = '" + obs_tec.getText() + "', "
                    + "turno_tec = '" + turno_tec.getText() + "',"
                    + "login_tec = '" + login_tec.getText() + "' "
                    //+ "WHERE id = " + current_id;
                    + "WHERE id_mestre = " + current_id + " "
                    + "and fl_n3 = " + ifl_n3;

            Db_class.mysql_insert(query, conn);

            if (is_removing) {

                insert_obs(get_obs_hist() + "Técnico/Parceira removido do caso.");

            } else if (tecnico.getText().equals("")) {

                insert_obs(get_obs_hist() + "Atribuída a parceira " + epo.getText());

            } else {

                insert_obs(get_obs_hist() + "Atribuido técnico " + tecnico.getText() + ", parceira "
                        + "" + epo.getText() + " (" + tipo_tec.getText() + ")");
            }
            set_ult_acomp();

            Db_class.close_conn(conn);

            insert_prod(current_id, "Alteração de Técnico");

            if (is_removing) {

                global.show_message("Técnico/Parceira removido com sucesso!");

            } else if (tecnico.getText().equals("")) {

                global.show_message("Parceira atribuída com sucesso!");

            } else {

                global.show_message("Técnico atribuído com sucesso!");

            }
            pn.atualiza_painel();

            if (current_status_caso.equals("Encaixe") && mn.cop.equals("COP SP")) {

                altera_status_caso("Em Acompanhamento", "", "");
                this.dispose();

            } else {

                //int tab_index = jTabbedPane1.getSelectedIndex();
                //this.dispose();
                //tratar tr = new tratar(mn, true, current_id, pn, tab_index, cop);
                fill_panel();

            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void disable_components(Container c) {

        Component[] cps = c.getComponents();

        if (cps.length > 0) {

            for (Component cp : cps) {

                if (cp instanceof JComboBox) {

                    cp.setEnabled(false);

                }

                if (cp instanceof Container) {

                    disable_components((Container) cp);

                } else {

                    cp.setEnabled(false);

                }

            }
        } else if (c instanceof JLabel) {

        } else {

            c.setEnabled(false);

        }
    }

    public void insert_prod(int id_caso, String acao) {
        try {
            Connection conn = Db_class.mysql_conn();

            String query = "INSERT INTO produtividade "
                    + "(id_caso, acao, login_user, nome_user, cop, date_time_acao)"
                    + "VALUES (" + id_caso + ", '" + acao + "', '" + mn.get_login() + "',"
                    + "'" + mn.get_nome() + "', '" + mn.get_cop() + "', current_timestamp)";

            Db_class.mysql_insert(query, conn);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void set_parceira(String parceira) {

        epo.setText(parceira);
        login_tec.setText("");
        tecnico.setText("");
        telefone.setText("");
        obs_tec.setText("");
        turno_tec.setText("");
        tipo_tec.setText("");

        altera_tec(false);

    }

    public void insert_nova_os(String num_os, Date nova_data, String nova_janela,
            String novo_tipo, String nova_partic, String nova_obs) {

        String data_insert = global.get_simple_date(nova_data);
        String tipo_os_insert = novo_tipo;
        String janela_insert = nova_janela;
        String partic_insert = nova_partic;
        String num_os_insert = num_os;

        String query_find = "SELECT id, count(*) "
                + "FROM painel "
                + "WHERE contrato = '" + this.contrato.getText() + "' "
                + "and data_acomp = '" + data_insert + "' "
                + "and janela = '" + janela_insert + "' "
                + "and (status <> 'Encerrado' and status <> 'Encerrado - Os Duplicada/Cancelada') "
                + "and os_principal = 'Sim'";

        try {
            Connection conn = Db_class.mysql_conn();

            String query_grupo = "SELECT grp_descricao FROM tipo_os_novo WHERE "
                    + "tipo_os = '" + tipo_os_insert + "'";

            ResultSet rs_grupo = Db_class.mysql_result(conn, query_grupo);

            rs_grupo.next();
            String grupo_os_insert = rs_grupo.getString(1);

            ResultSet rs = Db_class.mysql_result(conn, query_find);

            rs.next();

            int contagem = rs.getInt(2);
            int id_mestre = rs.getInt(1);

            String obs_agenda = "";

            if (nova_obs.equals("")) {

                obs_agenda = get_obs_hist() + "Nova OS " + num_os + " agendada para "
                        + global.get_br_date(nova_data) + " janela das " + janela_insert;

            } else {

                obs_agenda = get_obs_hist() + "Nova OS " + num_os + " agendada para "
                        + global.get_br_date(nova_data) + " janela das " + janela_insert + "\n\n"
                        + get_obs_hist() + nova_obs;

            }

            String final_obs_agenda = obs_agenda.replace("'", "");

            String query_insert = "";

            //Preparando para registrar as ações
            LogS log = new LogS();
            //

            if (contagem > 0) {

                query_insert = "INSERT INTO painel "
                        + "(contrato,\n"
                        + "    cidade,\n"
                        + "    area_despacho,\n"
                        + "    data_acomp,\n"
                        + "    janela,\n"
                        + "    tipo_os,\n"
                        + "    solicitante,\n"
                        + "    nome_cli,\n"
                        + "    tipo_trat,\n"
                        + "    descr_indevido,\n"
                        + "    canal,\n"
                        + "    obs_horario,\n"
                        + "    obs,\n"
                        + "    epo,\n"
                        + "    tecnico,\n"
                        + "    tipo_tecnico,\n"
                        + "    telefone_tec,\n"
                        + "    obs_tec,\n"
                        + "    cluster,\n"
                        + "    cop,\n"
                        + "    date_time_insert,\n"
                        + "    user_insert,\n"
                        + "    nome_insert,\n"
                        + "    sistema,\n"
                        + "    status,\n"
                        + "    ativo,\n"
                        + "    tentativa,\n"
                        + "    status_monitoria,\n"
                        + "    ultimo_acomp,\n"
                        + "    hora_ultimo_acomp,\n"
                        + "    grupo_os,\n"
                        + "    email,\n"
                        + "    turno_tec,\n"
                        + "    login_tec,\n"
                        + "    user_mark,\n"
                        + "    mail_attach,\n"
                        + "    cod_baixa,\n"
                        + "    hora_baixa,\n"
                        + "    num_os,\n"
                        + "    id_mestre,\n"
                        + "    os_principal,\n"
                        + "    status_os,\n"
                        + "    encaixe,\n"
                        + "    ultimo_acomp_login \n"
                        + ") \n"
                        + "SELECT "
                        + "    `painel`.`contrato`,\n"
                        + "    `painel`.`cidade`,\n"
                        + "    `painel`.`area_despacho`,\n"
                        + "    '" + data_insert + "',\n"
                        + "    '" + janela_insert + "',\n"
                        + "    '" + tipo_os_insert + "',\n"
                        + "    `painel`.`solicitante`,\n"
                        + "    `painel`.`nome_cli`,\n"
                        + "    `painel`.`tipo_trat`,\n"
                        + "    `painel`.`descr_indevido`,\n"
                        + "    `painel`.`canal`,\n"
                        + "    '" + partic_insert + "',\n"
                        + "     CONCAT('" + final_obs_agenda + "', obs),\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    `painel`.`cluster`,\n"
                        + "    `painel`.`cop`,\n"
                        + "    current_timestamp,\n"
                        + "    '" + mn.get_login() + "',\n"
                        + "    '" + mn.get_nome() + "',\n"
                        + "    `painel`.`sistema`,\n"
                        + "    'Encaixe',\n"
                        + "    `painel`.`ativo`,\n"
                        + "    `painel`.`tentativa`,\n"
                        + "     null,\n"
                        + "    `painel`.`ultimo_acomp`,\n"
                        + "    `painel`.`hora_ultimo_acomp`,\n"
                        + "    '" + grupo_os_insert + "',\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    '" + num_os_insert + "',\n"
                        + "    id,\n"
                        + "    'Não',\n"
                        + "    null,\n"
                        + "    'Não', "
                        + "    '" + mn.get_login() + "'\n"
                        + "FROM `ouvidoria`.`painel`"
                        + "WHERE id = " + id_mestre;

                Db_class.mysql_insert(query_insert, conn);

            } else {

                query_insert = "INSERT INTO painel "
                        + "(contrato,\n"
                        + "    cidade,\n"
                        + "    area_despacho,\n"
                        + "    data_acomp,\n"
                        + "    janela,\n"
                        + "    tipo_os,\n"
                        + "    solicitante,\n"
                        + "    nome_cli,\n"
                        + "    tipo_trat,\n"
                        + "    descr_indevido,\n"
                        + "    canal,\n"
                        + "    obs_horario,\n"
                        + "    obs,\n"
                        + "    epo,\n"
                        + "    tecnico,\n"
                        + "    tipo_tecnico,\n"
                        + "    telefone_tec,\n"
                        + "    obs_tec,\n"
                        + "    cluster,\n"
                        + "    cop,\n"
                        + "    date_time_insert,\n"
                        + "    user_insert,\n"
                        + "    nome_insert,\n"
                        + "    sistema,\n"
                        + "    status,\n"
                        + "    ativo,\n"
                        + "    tentativa,\n"
                        + "    status_monitoria,\n"
                        + "    ultimo_acomp,\n"
                        + "    hora_ultimo_acomp,\n"
                        + "    grupo_os,\n"
                        + "    email,\n"
                        + "    turno_tec,\n"
                        + "    login_tec,\n"
                        + "    user_mark,\n"
                        + "    mail_attach,\n"
                        + "    cod_baixa,\n"
                        + "    hora_baixa,\n"
                        + "    num_os,\n"
                        + "    id_mestre,\n"
                        + "    os_principal,\n"
                        + "    status_os, \n"
                        + "    encaixe, \n "
                        + "    ultimo_acomp_login \n"
                        + ")"
                        + "SELECT "
                        + "    `painel`.`contrato`,\n"
                        + "    `painel`.`cidade`,\n"
                        + "    `painel`.`area_despacho`,\n"
                        + "    '" + data_insert + "',\n"
                        + "    '" + janela_insert + "',\n"
                        + "    '" + tipo_os_insert + "',\n"
                        + "    `painel`.`solicitante`,\n"
                        + "    `painel`.`nome_cli`,\n"
                        + "    `painel`.`tipo_trat`,\n"
                        + "    `painel`.`descr_indevido`,\n"
                        + "    `painel`.`canal`,\n"
                        + "    `painel`.`obs_horario`,\n"
                        + "     CONCAT('" + final_obs_agenda + "', obs),\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    `painel`.`cluster`,\n"
                        + "    `painel`.`cop`,\n"
                        + "    current_timestamp,\n"
                        + "    '" + mn.get_login() + "',\n"
                        + "    '" + mn.get_nome() + "',\n"
                        + "    `painel`.`sistema`,\n"
                        + "    'Encaixe',\n"
                        + "    `painel`.`ativo`,\n"
                        + "    `painel`.`tentativa`,\n"
                        + "     null,\n"
                        + "    `painel`.`ultimo_acomp`,\n"
                        + "    `painel`.`hora_ultimo_acomp`,\n"
                        + "    '" + grupo_os_insert + "',\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    null,\n"
                        + "    '" + num_os_insert + "',\n"
                        + "    null,\n"
                        + "    'Sim',\n"
                        + "    null,\n"
                        + "    'Não', \n"
                        + "    '" + mn.get_login() + "' \n"
                        + "FROM `ouvidoria`.`painel` "
                        + "WHERE id = " + current_id;

                Db_class.mysql_insert(query_insert, conn);

                String query_last_id = "SELECT last_insert_id() FROM painel";

                ResultSet rs_last_id = Db_class.mysql_result(conn, query_last_id);

                rs_last_id.next();

                int last_id = rs_last_id.getInt(1);

                String query = "UPDATE painel SET id_mestre = id "
                        + "WHERE id = " + last_id;

                id_mestre = last_id;

                Db_class.mysql_insert(query, conn);

                Db_class.close_conn(conn);

            }

            //registrando reagendamento
            //log.LogComum_Inclusao(id_mestre, Integer.parseInt(num_os_insert), tipo_os_insert, 1, 2, 2);
            log.LogComum_Inclusao(id_mestre, num_os_insert, tipo_os_insert, 1, 2, 2);

            //fechando o registro as ações
            log = null;
            //

            global.insert_prod(current_id, "Adicionar OS", mn);

            global.show_message("Os incluída com sucesso!");

            //this.dispose();
            //tratar tr = new tratar(mn, true, current_id, pn, 4, cop);
            fill_panel();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(nova_os.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void reagenda_os(String array_num_os[], Date nova_data, String nova_janela,
            String novo_tipo[], String nova_partic, String nova_obs) {

        String data_insert = global.get_simple_date(nova_data);
        String tipo_os_insert = "";
        String janela_insert = nova_janela;
        String partic_insert = nova_partic;

        try {
            Connection conn = Db_class.mysql_conn();

            String obs_agenda = "";

            String query_insert = "";
            String os_numbers = "";
            int index = 0;
            String status_real = "";

            if (cop.equals("COP NET")) {

                status_real = "Encaixe";

            } else {

                status_real = "Em Acompanhamento";

            }

            //Preparando para registrar as ações
            LogS log = new LogS();
            //

            for (String os : array_num_os) {

                tipo_os_insert = novo_tipo[index];

                String query_grupo = "SELECT grp_descricao "
                        + "FROM tipo_os_novo "
                        + "WHERE tipo_os = '" + tipo_os_insert + "'";

                ResultSet rs_grupo = Db_class.mysql_result(conn, query_grupo);

                rs_grupo.next();
                String grupo_os_insert = rs_grupo.getString(1);

                index++;

                String query_find = "SELECT id, count(*) "
                        + "FROM painel "
                        + "WHERE contrato = '" + this.contrato.getText() + "' "
                        + "and data_acomp = '" + data_insert + "' "
                        + "and janela = '" + janela_insert + "'";

                ResultSet rs = Db_class.mysql_result(conn, query_find);

                rs.next();

                int contagem = rs.getInt(2);
                int id_mestre = rs.getInt(1);

                os_numbers = os_numbers + os + ",";

                if (contagem > 0) {

                    query_insert = "INSERT INTO painel "
                            + "(contrato,\n"
                            + "    cidade,\n"
                            + "    area_despacho,\n"
                            + "    data_acomp,\n"
                            + "    janela,\n"
                            + "    tipo_os,\n"
                            + "    solicitante,\n"
                            + "    nome_cli,\n"
                            + "    tipo_trat,\n"
                            + "    descr_indevido,\n"
                            + "    canal,\n"
                            + "    obs_horario,\n"
                            + "    obs,\n"
                            + "    epo,\n"
                            + "    tecnico,\n"
                            + "    tipo_tecnico,\n"
                            + "    telefone_tec,\n"
                            + "    obs_tec,\n"
                            + "    cluster,\n"
                            + "    cop,\n"
                            + "    date_time_insert,\n"
                            + "    user_insert,\n"
                            + "    nome_insert,\n"
                            + "    sistema,\n"
                            + "    status,\n"
                            + "    ativo,\n"
                            + "    tentativa,\n"
                            + "    status_monitoria,\n"
                            + "    ultimo_acomp,\n"
                            + "    hora_ultimo_acomp,\n"
                            + "    grupo_os,\n"
                            + "    email,\n"
                            + "    turno_tec,\n"
                            + "    login_tec,\n"
                            + "    user_mark,\n"
                            + "    mail_attach,\n"
                            + "    cod_baixa,\n"
                            + "    hora_baixa,\n"
                            + "    num_os,\n"
                            + "    id_mestre,\n"
                            + "    os_principal,\n"
                            + "    status_os, \n"
                            + "    encaixe, \n"
                            + "    ultimo_acomp_login \n"
                            + ") \n"
                            + "SELECT "
                            + "    `painel`.`contrato`,\n"
                            + "    `painel`.`cidade`,\n"
                            + "    `painel`.`area_despacho`,\n"
                            + "    '" + data_insert + "',\n"
                            + "    '" + janela_insert + "',\n"
                            + "    '" + tipo_os_insert + "',\n"
                            + "    `painel`.`solicitante`,\n"
                            + "    `painel`.`nome_cli`,\n"
                            + "    `painel`.`tipo_trat`,\n"
                            + "    `painel`.`descr_indevido`,\n"
                            + "    `painel`.`canal`,\n"
                            + "    '" + partic_insert + "',\n"
                            + "      `painel`.`obs`,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    `painel`.`cluster`,\n"
                            + "    `painel`.`cop`,\n"
                            + "    current_timestamp,\n"
                            + "    '" + mn.get_login() + "',\n"
                            + "    '" + mn.get_nome() + "',\n"
                            + "    `painel`.`sistema`,\n"
                            + "    '" + status_real + "',\n"
                            + "    `painel`.`ativo`,\n"
                            + "    `painel`.`tentativa`,\n"
                            + "     null,\n"
                            + "    `painel`.`ultimo_acomp`,\n"
                            + "    `painel`.`hora_ultimo_acomp`,\n"
                            + "    '" + grupo_os_insert + "',\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    '" + os + "',\n"
                            + "    id,\n"
                            + "    'Não',\n"
                            + "    null,\n"
                            + "    'Não', \n "
                            + "    '" + mn.get_login() + "' \n"
                            + "FROM `ouvidoria`.`painel` "
                            + "WHERE id = " + id_mestre;

                    Db_class.mysql_insert(query_insert, conn);

                } else {

                    query_insert = "INSERT INTO painel "
                            + "(contrato,\n"
                            + "    cidade,\n"
                            + "    area_despacho,\n"
                            + "    data_acomp,\n"
                            + "    janela,\n"
                            + "    tipo_os,\n"
                            + "    solicitante,\n"
                            + "    nome_cli,\n"
                            + "    tipo_trat,\n"
                            + "    descr_indevido,\n"
                            + "    canal,\n"
                            + "    obs_horario,\n"
                            + "    obs,\n"
                            + "    epo,\n"
                            + "    tecnico,\n"
                            + "    tipo_tecnico,\n"
                            + "    telefone_tec,\n"
                            + "    obs_tec,\n"
                            + "    cluster,\n"
                            + "    cop,\n"
                            + "    date_time_insert,\n"
                            + "    user_insert,\n"
                            + "    nome_insert,\n"
                            + "    sistema,\n"
                            + "    status,\n"
                            + "    ativo,\n"
                            + "    tentativa,\n"
                            + "    status_monitoria,\n"
                            + "    ultimo_acomp,\n"
                            + "    hora_ultimo_acomp,\n"
                            + "    grupo_os,\n"
                            + "    email,\n"
                            + "    turno_tec,\n"
                            + "    login_tec,\n"
                            + "    user_mark,\n"
                            + "    mail_attach,\n"
                            + "    cod_baixa,\n"
                            + "    hora_baixa,\n"
                            + "    num_os,\n"
                            + "    id_mestre,\n"
                            + "    os_principal,\n"
                            + "    status_os, \n"
                            + "    encaixe, \n"
                            + "    ultimo_acomp_login \n"
                            + ")"
                            + "SELECT "
                            + "    `painel`.`contrato`,\n"
                            + "    `painel`.`cidade`,\n"
                            + "    `painel`.`area_despacho`,\n"
                            + "    '" + data_insert + "',\n"
                            + "    '" + janela_insert + "',\n"
                            + "    '" + tipo_os_insert + "',\n"
                            + "    `painel`.`solicitante`,\n"
                            + "    `painel`.`nome_cli`,\n"
                            + "    `painel`.`tipo_trat`,\n"
                            + "    `painel`.`descr_indevido`,\n"
                            + "    `painel`.`canal`,\n"
                            + "    `painel`.`obs_horario`,\n"
                            + "     `painel`.`obs`,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    `painel`.`cluster`,\n"
                            + "    `painel`.`cop`,\n"
                            + "    current_timestamp,\n"
                            + "    '" + mn.get_login() + "',\n"
                            + "    '" + mn.get_nome() + "',\n"
                            + "    `painel`.`sistema`,\n"
                            + "    '" + status_real + "',\n"
                            + "    `painel`.`ativo`,\n"
                            + "    `painel`.`tentativa`,\n"
                            + "     null,\n"
                            + "    `painel`.`ultimo_acomp`,\n"
                            + "    `painel`.`hora_ultimo_acomp`,\n"
                            + "    '" + grupo_os_insert + "',\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    null,\n"
                            + "    '" + os + "',\n"
                            + "    null,\n"
                            + "    'Sim',\n"
                            + "    null, \n"
                            + "    'Não', \n"
                            + "    '" + mn.get_login() + "' \n"
                            + "FROM `ouvidoria`.`painel`"
                            + "WHERE id = " + current_id;

                    Db_class.mysql_insert(query_insert, conn);

                    String query_last_id = "SELECT last_insert_id() FROM painel";

                    ResultSet rs_last_id = Db_class.mysql_result(conn, query_last_id);

                    rs_last_id.next();

                    int last_id = rs_last_id.getInt(1);

                    String query = "UPDATE painel SET id_mestre = id "
                            + "WHERE id = " + last_id;

                    id_mestre = last_id;

                    Db_class.mysql_insert(query, conn);

                }

                //registrando reagendamento
                //log.LogComum_Inclusao(id_mestre, Integer.parseInt(os), tipo_os_insert, 3, 2, 2);
                log.LogComum_Inclusao(id_mestre, os, tipo_os_insert, 3, 2, 2);

            }

            //fechando o registro as ações
            log = null;
            //

            if (array_num_os.length == 1) {

                if (nova_obs.equals("")) {

                    obs_agenda = get_obs_hist() + "OS " + os_numbers + " reagendada para "
                            + global.get_br_date(nova_data) + " janela das " + janela_insert;

                } else {

                    obs_agenda = get_obs_hist() + "OS " + os_numbers + " reagendada para "
                            + global.get_br_date(nova_data) + " janela das " + janela_insert + "\n\n"
                            + get_obs_hist() + nova_obs;

                }

            } else if (nova_obs.equals("")) {

                obs_agenda = get_obs_hist() + "OS`s " + os_numbers + " reagendadas para "
                        + global.get_br_date(nova_data) + " janela das " + janela_insert;

            } else {

                obs_agenda = get_obs_hist() + "OS`s " + os_numbers + " reagendadas para "
                        + global.get_br_date(nova_data) + " janela das " + janela_insert + "\n\n"
                        + get_obs_hist() + nova_obs;

            }

            String final_obs_agenda = obs_agenda.replace("'", "");

            insert_obs(final_obs_agenda);

            global.insert_prod(current_id, "Reagendar OS", mn);

            global.show_message("OS's reagendadas com sucesso!");

            //this.dispose();
            //tratar tr = new tratar(mn, true, current_id, pn, 4, cop);
            fill_panel();

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(nova_os.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    class CustomRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 6703872492730589499L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {

                cellComponent.setBackground(table.getSelectionBackground());
                cellComponent.setForeground(table.getSelectionForeground());

            } else {

                cellComponent.setBackground(Color.WHITE);
                cellComponent.setForeground(table.getForeground());
                table.setShowHorizontalLines(true);
                table.setShowVerticalLines(true);

            }
            return cellComponent;

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
            java.util.logging.Logger.getLogger(tratar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tratar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tratar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tratar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                tratar dialog = new tratar(new javax.swing.JFrame(), true);
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

    private String mailAtividades(Integer i_id_mestre) {
        String sRetorno = "";
        Connection conn_ativ;
        try {
            conn_ativ = Db_class.mysql_conn();
            CallableStatement cSP = conn_ativ.prepareCall("{call ouvidoria.sp_tratar_painel_oss(?, ?)}");
            cSP.setInt(1, i_id_mestre);
            cSP.registerOutParameter(2, java.sql.Types.VARCHAR);
            cSP.execute();
            sRetorno = cSP.getString(2);
            conn_ativ.close();
            conn_ativ = null;
        } catch (Exception ex) {
            sRetorno = "";
            global.show_error_message(
                    "Problemas com a definição de quem receberá e-mail.\n"
                    + "Assim, tanto EPO quanto IAT o receberão.\n\n"
                    + "Erro: " + ex.getMessage()
            );
            try {
                conn_ativ = null;
            } catch (Exception ex0) {
            }
        }
        return sRetorno;
    }

    private String buscaField(String s_cidade) {
        String sRetorno = "";
        Connection conn_busca;
        try {
            conn_busca = Db_class.mysql_conn();
            ResultSet rs = Db_class.mysql_result(conn_busca, "select ifnull(link_field, '') as link_field from ouvidoria.cidades_novo where upper(trim(cop_info)) = '" + s_cidade.toUpperCase().trim() + "'");
            if (rs.next()) {
                if (rs.getString(1).length() > 0) {
                    sRetorno = " - Cidade de '" + s_cidade.trim() + "' está no Field " + rs.getString(1);
                } else {
                    sRetorno = " - Field da Cidade de '" + s_cidade.trim() + "' não encontrado";
                }
            }
            rs = null;
            Db_class.close_conn(conn_busca);
        } catch (Exception ex) {
            sRetorno = "";
            global.show_error_message(
                    "Problemas com a detecção do Field da Cidade.\n\n"
                    + "Erro: " + ex.getMessage()
            );
            try {
                conn_busca = null;
            } catch (Exception ex0) {
            }
        }
        return sRetorno;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField area;
    private javax.swing.JButton botao_email;
    private javax.swing.JButton botao_encerra;
    private javax.swing.JButton botao_procurar;
    private javax.swing.JButton botao_status_caso;
    private javax.swing.JButton botao_status_monit;
    private javax.swing.JTextField canal;
    private javax.swing.JCheckBox check_mail;
    private javax.swing.JTextField cidade;
    private javax.swing.JTextField contrato;
    private javax.swing.JTextField data;
    private org.jdesktop.swingx.JXDatePicker data_alter;
    private javax.swing.JTextField epo;
    private javax.swing.JTextField ins_por;
    private javax.swing.JTextField ins_time;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField janela;
    private javax.swing.JComboBox janela_alter;
    private javax.swing.JButton jbt_reverter_os;
    private javax.swing.JButton jbtn_reverter_baixa;
    private javax.swing.JToolBar.Separator jsep_reverter_baixa;
    private javax.swing.JTextField login_tec;
    private javax.swing.JTextField mail_at;
    private javax.swing.JTextField nome_cli;
    private javax.swing.JTextArea obs_agendamento;
    private javax.swing.JTextArea obs_antiga;
    private javax.swing.JTextArea obs_cidade;
    private javax.swing.JFormattedTextField obs_horario;
    private javax.swing.JTextArea obs_horario_alter;
    private javax.swing.JTextArea obs_nova;
    private javax.swing.JTextArea obs_tec;
    private javax.swing.JButton open_mail;
    private javax.swing.JPanel painel_cidade;
    private javax.swing.JPanel painel_os;
    private javax.swing.JTextField reincid;
    private javax.swing.JTextField solic;
    private javax.swing.JComboBox sts_caso;
    private javax.swing.JComboBox sts_monit;
    private javax.swing.JTextField tecnico;
    private javax.swing.JTextField telefone;
    private javax.swing.JTextField tipo_os;
    private javax.swing.JComboBox tipo_os_agenda;
    private javax.swing.JTextField tipo_tec;
    private javax.swing.JTextField turno_tec;
    private javax.swing.JTextField ult_acomp_por;
    private javax.swing.JTextField ult_acomp_time;
    // End of variables declaration//GEN-END:variables
}
