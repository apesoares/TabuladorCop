/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import com.human.gateway.client.service.SimpleMessageService;
import java.awt.Color;
import java.awt.Component;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import ouvidoria.util.UtilBD;

/**
 *
 * @author N0026925
 */
public class painel extends javax.swing.JPanel {

    Menu mn;
    JTable main_tab;
    painel main_painel;
    SortedSet<Date> dt_acomp_indexes;
    int[] city_indexes;
    int[] type_indexes;
    int[] janela_indexes;
    //Date filter_date = new Date();
    String filter_city_query = "cidade is not null ";
    String filter_type_query = "grupo_os is not null ";
    String filter_janela_query = "janela is not null ";

    //String filter_date_query = "current_date()";
    DateFormat sdf_brasil = new SimpleDateFormat("yyyy-MM-dd");
    String filter_date_query = sdf_brasil.format(new Date(System.currentTimeMillis()));

    JPanel my_panel;
    Timer current_timer;
    List<Integer> current_ids = new ArrayList<Integer>();
    String cop_cid;

    int ifl_n3;
    List<Integer> li_box_mail = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
    List<String> ls_box_mail = new ArrayList<>(Arrays.asList("Sinalização Parceiras", "Sinalização NET", "Pendências Parceiras", "Pendências NET"));
    String sQtdDetalhe = "Mais detalhes";

    public int DetectaBoxMail(String ps_selecao) {
        int iRetorno = -1;
        
        for (int i = 0; i < ls_box_mail.size(); i++) {
            if (ps_selecao == ls_box_mail.get(i)) {
                iRetorno = li_box_mail.get(i);
                break;
            }
        }
        
        return iRetorno;
    }
    
    /**
     * Creates new form painel
     *
     * @param mn
     * @param cop_cid
     */
    public painel(Menu mn, String cop_cid, int ifl_n3) {

        initComponents();

        this.mn = mn;
        main_painel = this;
        this.my_panel = jPanel1;
        this.cop_cid = cop_cid;
        this.ifl_n3 = ifl_n3;
        
        box_mail.removeAllItems();
        jbt_mais_detalhes.setVisible(false);
        if ("COP NET".equals(cop_cid)) {
            if (ifl_n3 == 0) {
                box_mail.addItem(ls_box_mail.get(0));
                box_mail.addItem(ls_box_mail.get(1));
                box_mail.addItem(ls_box_mail.get(2));
                box_mail.addItem(ls_box_mail.get(3));
            } else {
                box_mail.addItem(ls_box_mail.get(1));
                box_mail.addItem(ls_box_mail.get(2));
                box_mail.addItem(ls_box_mail.get(3));
                jbt_mais_detalhes.setVisible(true);
            }
        } else {
            box_mail.addItem(ls_box_mail.get(0));
            box_mail.addItem(ls_box_mail.get(2));
        }

        Timer timer = new Timer();

        Task task = new Task();

        current_timer = timer;

        if (this instanceof painel_encaixe) {

        } else {

            current_timer.schedule(task, 1200000);

        }
        //filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date, true, false, cop_cid);
        filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date_query, true, false, cop_cid);

        ft.aplica_filtro();

        //atualiza_painel();
        
    }

    public painel() {

    }

    public painel(Menu mn, boolean is_bsod, String cop_cid) {

        initComponents();

        this.mn = mn;
        main_painel = this;
        this.my_panel = jPanel1;
        this.cop_cid = cop_cid;

        //filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date, true, false, true);
        filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date_query, true, false, true);

        ft.aplica_filtro();
    }

    public void atualiza_encaixe() {

        if (!mn.check_version()) {

            return;

        }

        try {

            Connection conn = Db_class.mysql_conn();

            String query = "SELECT COUNT(*) FROM painel "
                    + "WHERE status = 'Encaixe' "
                    + " and cop = '" + cop_cid + "'"
                    + "and data_acomp = '" + global.get_simple_date(new Date()) + "'"
                    + "and tipo_trat = 'Acompanhamento'"
                    //+ "and os_principal = 'Sim' ";
                    + "and fl_n3 = 0 ";

            ResultSet rs = Db_class.mysql_result(conn, query);

            rs.next();

            long count = (long) rs.getObject(1);

            if (count == 0) {
                //label_qtd_encaixe.setBackground(global.get_color(0, 204, 0));
                label_encaixe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png")));
            } else {
                //label_qtd_encaixe.setBackground(global.get_color(255, 0, 0));
                label_encaixe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exclamation.png")));
            }

            label_qtd_encaixe.setText(String.valueOf(count));

            String query_ids = "SELECT id FROM painel "
                    + "WHERE cidade IN (" + filter_city_query + ") "
                    + "and grupo_os IN (" + filter_type_query + ") and "
                    + "janela IN (" + filter_janela_query + ") "
                    + "and status = 'Encaixe' "
                    + "and data_acomp = '" + global.get_simple_date(new Date()) + "' "
                    + "and tipo_trat = 'Acompanhamento' "
                    //+ "and os_principal = 'Sim' ";
                    + "and fl_n3 = 0 ";

            ResultSet rs_ids = Db_class.mysql_result(conn, query_ids);

            List<Integer> lista_ids = new ArrayList<Integer>();

            while (rs_ids.next()) {

                lista_ids.add(rs_ids.getInt(1));

            }

            List<Integer> missing_ids = new ArrayList<Integer>();
            String missing_ids_string = "";

            if (current_ids.size() > 0) {

                for (int id : lista_ids) {

                    if (!current_ids.contains(id)) {

                        if (missing_ids.size() > 0) {

                            missing_ids_string = missing_ids_string + ", " + id;

                        } else {

                            missing_ids_string = "" + id;

                        }

                        missing_ids.add(id);

                    }

                }

            }

            //System.out.println(current_ids.size() + " - " + lista_ids.size());

            String final_str = "\n";

            if (missing_ids.size() > 0) {

                String query_missing = "SELECT cidade, janela, grupo_os "
                        + "FROM painel "
                        + "WHERE id IN (" + missing_ids_string + ")";

                ResultSet true_missing = Db_class.mysql_result(conn, query_missing);

                while (true_missing.next()) {

                    final_str = final_str + true_missing.getString(1) + " - "
                            + true_missing.getString(2) + " - "
                            + true_missing.getString(3) + "\n\n";

                }

                DisplayTrayIcon.trayIcon.displayMessage("Atenção! Novo caso de ouvidoria!",
                        final_str,
                        TrayIcon.MessageType.WARNING);

            }

            String query_passado = "SELECT COUNT(*) FROM painel "
                    + "WHERE data_acomp < current_date and status "
                    + "NOT IN ('Encerrado','Encerrado - Os Duplicada/Cancelada','Encaixe') "
                    + "and tipo_trat = 'Acompanhamento' "
                    //+ "and os_principal = 'Sim' "
                    + "and cop = '" + cop_cid + "' "
                    + "and fl_n3 = 0 ";

            ResultSet rs_passado = Db_class.mysql_result(conn, query_passado);

            rs_passado.next();

            long qtd_passado = rs_passado.getLong(1);

            label_qtd_passado.setText(String.valueOf(qtd_passado));

            String query_datas_passadas = "SELECT DATE_FORMAT(data_acomp,'%d/%m/%Y'), COUNT(*) "
                    + "FROM painel "
                    + "WHERE data_acomp < current_date "
                    + "and status NOT IN ('Encerrado','Encerrado - Os Duplicada/Cancelada','Encaixe') "
                    + "and tipo_trat = 'Acompanhamento' "
                    //+ "and os_principal = 'Sim' "
                    + "and cop = '" + cop_cid + "' "
                    + "and fl_n3 = 0 "
                    + "GROUP BY data_acomp "
                    + "ORDER BY data_acomp DESC ";

            rs = Db_class.mysql_result(conn, query_datas_passadas);

            String table_code = "<table border=\"1\" cellpadding=\"5\">"
                    + "<tr bgcolor=\"000000\" font color=\"FFFFFF\">"
                    + "<th>Data</th>"
                    + "<th>Qtd</th>"
                    + "</tr>";

            boolean ctrl_past = false;

            while (rs.next()) {

                table_code = table_code + "<tr>"
                        + "<td>" + rs.getString(1) + "</td>"
                        + "<td>" + rs.getLong(2) + "</td>"
                        + "</tr>";

                ctrl_past = true;

            }

            if (ctrl_past) {

                table_code = "<html>" + table_code + "</table><html>";
                label_qtd_passado.setToolTipText(table_code);

            }

            current_ids = lista_ids;

            //System.out.println("Atualizou! - " + global.get_br_date_time(new Date()));

            Task task = new Task();

            current_timer.cancel();

            Timer timer = new Timer();

            current_timer = timer;

            current_timer.schedule(task, 1200000);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void atualiza_painel() {

        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();

        if (!mn.check_version()) {

            return;

        }

        String part_encerr = "";

        if (jCheckBox1.isSelected()) {

            part_encerr = "";

        } else {

            part_encerr = "painel.status not in ('Encerrado','Encerrado - Os Duplicada/Cancelada') and";

        }

        String qCriterio = "WHERE (" + part_encerr + " painel.status <> 'Encaixe') "
                + "and painel.cidade IN (" + filter_city_query + ") "
                + "and painel.grupo_os IN (" + filter_type_query + ") and "
                + "painel.janela IN (" + filter_janela_query + ") and "
                // + "painel.data_acomp = '" + global.get_simple_date(filter_date) + "' "
                + "painel.data_acomp in (" + filter_date_query + ") "
                + "and painel.tipo_trat = 'Acompanhamento' "
                //+ "and painel.os_principal = 'Sim' "
                + "and painel.canal <> 'BSOD' "
                + "and painel.fl_n3 = " + ifl_n3 + " ";
        
        String qCriterioUmRegistro = "and painel.id in "
                + "("
                + "SELECT min(id) as id "
                + "FROM ouvidoria.painel "
                + qCriterio
                + "GROUP BY contrato, janela, data_acomp "
                + ")";

        String query = "SELECT id,  "
                + "ifnull(painel.contrato,'') as Contrato, "
                + "ifnull(painel.cidade,'') as Cidade, "
                + "ifnull(DATE_FORMAT(painel.data_acomp,'%d/%m/%Y'),'') as Data, "
                + "ifnull(painel.janela,'') as Janela, "
                + "ifnull(painel.tipo_os,'') as 'Tipo OS', "
                + "ifnull(painel.nome_cli,'') as 'Nome cliente', "
                + "ifnull(painel.epo,'') as EPO, "
                + "ifnull(painel.login_tec,'') as 'Login Tecnico', "
                + "ifnull(painel.status,'') as Status, "
                + "ifnull(painel.status_monitoria,'') as status_monitoria, "
                + "ifnull(painel.canal,'') as Canal, "
                + "ifnull(painel.user_mark,'') as 'Usuário Tratando', "
                + "ifnull(painel.obs_horario,'') as partic, "
                + "ifnull(painel.tecnico,'') as Técnico, "
                + "ifnull(painel.telefone_tec,'') as telefone_tec, "
                + "if(oc.status regexp ('Pendente'), 'Pendente', 'OS Executada') as 'Status NET', "
                + "ifnull(sms,'Não') as SMS, "
                + "ifnull(painel.area_despacho, '') as 'Área de despacho' "
                + "FROM painel "
                + "LEFT JOIN (SELECT \n"
                + "    `painel`.`contrato`,\n"
                + "    group_concat(`painel`.`num_os` separator '-'),    \n"
                + "	if(ocupacao.cod_os is null,'Encerrado', 'Pendente') as status_net_sms,\n"
                + "    group_concat(if(ocupacao.cod_os is null,'Encerrado', 'Pendente') separator '-') as status\n"
                + "FROM `ouvidoria`.`painel`\n"
                + "LEFT JOIN ouvidoria.ocupacao ON painel.num_os = ocupacao.cod_os\n"
                + "GROUP BY contrato) oc "
                + "ON painel.contrato = oc.contrato "
                + qCriterio
                + qCriterioUmRegistro
                + "ORDER BY painel.cidade, painel.janela, painel.contrato ";

        //System.out.println(query);

        try {
            JTable tab = global.getTable(query, jPanel1);

            main_tab = tab;

            int[] invisible_columns = {0, 10, 13, 14, 15, 16};
            int[] column_widths = {0, 50, 80, 60, 60, 150, 90, 100, 80, 100, 0, 60, 80, 0, 0, 0, 0, 50, 100};

            global.hide_columns(invisible_columns, main_tab);
            global.adjust_columns(column_widths, main_tab);

            main_tab.addMouseListener(new painel_right_click());

            for (int columnTable = 1; columnTable < main_tab.getColumnCount(); columnTable++) {

                main_tab.getColumnModel().getColumn(columnTable).setCellRenderer(new CustomRenderer());

            }

            TableFilterHeader filter = new TableFilterHeader(main_tab, AutoChoices.ENABLED);
            filter.setAdaptiveChoices(true);

            main_tab.addKeyListener(new ClipboardKeyAdapter(main_tab));

            label_qtd_casos.setText(String.valueOf(main_tab.getRowCount()));

        } catch (Exception ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
        }

        jPanel1.repaint();
        jPanel1.revalidate();

        atualiza_encaixe();
        
        jPanel1.repaint();
        jPanel1.revalidate();
    }

    public class painel_right_click extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (SwingUtilities.isRightMouseButton(e)) {

                int linha = main_tab.rowAtPoint(e.getPoint());

                main_tab.setRowSelectionInterval(linha, linha);

                int id_caso = (int) main_tab.getValueAt(linha, 0);

                tratar tr = new tratar(mn, true, id_caso, main_painel, 0, main_painel.cop_cid, ifl_n3);

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
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jbtn_marcar_casos = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        box_mail = new javax.swing.JComboBox<String>();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jbt_mais_detalhes = new javax.swing.JButton();
        pnl_info_casos = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        label_casos = new javax.swing.JLabel();
        label_qtd_casos = new javax.swing.JLabel();
        pnl_info_passado = new javax.swing.JPanel();
        label_passado = new javax.swing.JLabel();
        label_qtd_passado = new javax.swing.JLabel();
        pnl_info_encaixe = new javax.swing.JPanel();
        label_encaixe = new javax.swing.JLabel();
        label_qtd_encaixe = new javax.swing.JLabel();

        addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                formAncestorResized(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1241, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(765, 37));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setAutoscrolls(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(500, 30));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_refresh.png"))); // NOI18N
        jButton1.setText("Atualizar");
        jButton1.setToolTipText("Atualizar");
        jButton1.setHideActionText(true);
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/16x16/filter.png"))); // NOI18N
        jButton2.setText("Filtros");
        jButton2.setToolTipText("Filtros");
        jButton2.setFocusable(false);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jbtn_marcar_casos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pencil.png"))); // NOI18N
        jbtn_marcar_casos.setText("Marcar Casos");
        jbtn_marcar_casos.setToolTipText("Marcar Casos");
        jbtn_marcar_casos.setFocusable(false);
        jbtn_marcar_casos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jbtn_marcar_casos.setMinimumSize(new java.awt.Dimension(23, 23));
        jbtn_marcar_casos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn_marcar_casosActionPerformed(evt);
            }
        });
        jToolBar1.add(jbtn_marcar_casos);
        jToolBar1.add(jSeparator4);

        jLabel1.setText("Conteúdo: ");
        jLabel1.setFocusable(false);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToolBar1.add(jLabel1);

        box_mail.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sinalização Parceiras", "Sinalização NET", "Pendências Parceiras", "Pendências NET" }));
        box_mail.setToolTipText("Informe o conteúdo a apresentar");
        box_mail.setMinimumSize(new java.awt.Dimension(150, 20));
        jToolBar1.add(box_mail);
        jToolBar1.add(jSeparator5);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/email_go.png"))); // NOI18N
        jButton4.setText("Enviar E-mails");
        jButton4.setToolTipText("Enviar E-mails");
        jButton4.setFocusable(false);
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/phone.png"))); // NOI18N
        jButton5.setText("Envio SMS");
        jButton5.setToolTipText("Envio SMS");
        jButton5.setFocusable(false);
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jCheckBox1.setText("Encerrados");
        jCheckBox1.setToolTipText("Incluir os casos encerrados");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jCheckBox1);
        jToolBar1.add(jSeparator3);

        jbt_mais_detalhes.setBackground(new java.awt.Color(255, 255, 102));
        jbt_mais_detalhes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jbt_mais_detalhes.setForeground(new java.awt.Color(255, 0, 0));
        jbt_mais_detalhes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/color_wheel.png"))); // NOI18N
        jbt_mais_detalhes.setToolTipText("Mais detalhes e legenda");
        jbt_mais_detalhes.setFocusable(false);
        jbt_mais_detalhes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbt_mais_detalhes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbt_mais_detalhes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbt_mais_detalhesActionPerformed(evt);
            }
        });
        jToolBar1.add(jbt_mais_detalhes);

        pnl_info_casos.setLayout(new javax.swing.BoxLayout(pnl_info_casos, javax.swing.BoxLayout.LINE_AXIS));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setMaximumSize(new java.awt.Dimension(10, 30));
        jSeparator1.setMinimumSize(new java.awt.Dimension(10, 30));
        jSeparator1.setPreferredSize(new java.awt.Dimension(10, 30));
        pnl_info_casos.add(jSeparator1);

        label_casos.setBackground(new java.awt.Color(51, 51, 255));
        label_casos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_casos.setForeground(new java.awt.Color(255, 255, 255));
        label_casos.setText(" Casos");
        label_casos.setMaximumSize(new java.awt.Dimension(50, 25));
        label_casos.setMinimumSize(new java.awt.Dimension(50, 25));
        label_casos.setOpaque(true);
        label_casos.setPreferredSize(new java.awt.Dimension(45, 25));
        pnl_info_casos.add(label_casos);

        label_qtd_casos.setBackground(new java.awt.Color(51, 51, 255));
        label_qtd_casos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_qtd_casos.setForeground(new java.awt.Color(255, 255, 255));
        label_qtd_casos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_qtd_casos.setText("0");
        label_qtd_casos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        label_qtd_casos.setMaximumSize(new java.awt.Dimension(50, 25));
        label_qtd_casos.setMinimumSize(new java.awt.Dimension(50, 25));
        label_qtd_casos.setOpaque(true);
        label_qtd_casos.setPreferredSize(new java.awt.Dimension(50, 25));
        pnl_info_casos.add(label_qtd_casos);

        jToolBar1.add(pnl_info_casos);

        pnl_info_passado.setLayout(new javax.swing.BoxLayout(pnl_info_passado, javax.swing.BoxLayout.LINE_AXIS));

        label_passado.setBackground(new java.awt.Color(0, 153, 51));
        label_passado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_passado.setForeground(new java.awt.Color(255, 255, 255));
        label_passado.setText(" Passado");
        label_passado.setMaximumSize(new java.awt.Dimension(55, 25));
        label_passado.setMinimumSize(new java.awt.Dimension(55, 25));
        label_passado.setOpaque(true);
        label_passado.setPreferredSize(new java.awt.Dimension(55, 25));
        pnl_info_passado.add(label_passado);

        label_qtd_passado.setBackground(new java.awt.Color(0, 153, 51));
        label_qtd_passado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_qtd_passado.setForeground(new java.awt.Color(255, 255, 255));
        label_qtd_passado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_qtd_passado.setText("0");
        label_qtd_passado.setMaximumSize(new java.awt.Dimension(50, 25));
        label_qtd_passado.setMinimumSize(new java.awt.Dimension(50, 25));
        label_qtd_passado.setOpaque(true);
        label_qtd_passado.setPreferredSize(new java.awt.Dimension(50, 25));
        pnl_info_passado.add(label_qtd_passado);

        jToolBar1.add(pnl_info_passado);

        pnl_info_encaixe.setLayout(new javax.swing.BoxLayout(pnl_info_encaixe, javax.swing.BoxLayout.LINE_AXIS));

        label_encaixe.setBackground(new java.awt.Color(255, 0, 0));
        label_encaixe.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_encaixe.setForeground(new java.awt.Color(255, 255, 255));
        label_encaixe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        label_encaixe.setText(" Encaixe");
        label_encaixe.setMaximumSize(new java.awt.Dimension(75, 25));
        label_encaixe.setMinimumSize(new java.awt.Dimension(75, 25));
        label_encaixe.setOpaque(true);
        label_encaixe.setPreferredSize(new java.awt.Dimension(75, 25));
        pnl_info_encaixe.add(label_encaixe);

        label_qtd_encaixe.setBackground(new java.awt.Color(255, 0, 0));
        label_qtd_encaixe.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_qtd_encaixe.setForeground(new java.awt.Color(255, 255, 255));
        label_qtd_encaixe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_qtd_encaixe.setText("0");
        label_qtd_encaixe.setMaximumSize(new java.awt.Dimension(50, 25));
        label_qtd_encaixe.setMinimumSize(new java.awt.Dimension(50, 25));
        label_qtd_encaixe.setOpaque(true);
        label_qtd_encaixe.setPreferredSize(new java.awt.Dimension(50, 25));
        pnl_info_encaixe.add(label_qtd_encaixe);

        jToolBar1.add(pnl_info_encaixe);

        jPanel2.add(jToolBar1);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        atualiza_painel();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        open_filters();

    }//GEN-LAST:event_jButton2ActionPerformed

    public void open_filters() {

        if (!mn.check_version()) {

            return;

        }

        boolean check_null = false;

        if (city_indexes == null || janela_indexes == null || type_indexes == null) {

            check_null = true;

        }

        //filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date, check_null, true, cop_cid);
        filtros ft = new filtros(mn, true, this, city_indexes, janela_indexes, type_indexes, filter_date_query, check_null, true, cop_cid);

    }

    private void jbtn_marcar_casosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn_marcar_casosActionPerformed

        if (!mn.check_version()) {

            return;

        }

        marcar_casos();

    }//GEN-LAST:event_jbtn_marcar_casosActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        atualiza_painel();

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (!mn.check_version()) {
            return;
        }

        //cop_cid = "xxx";
        int index = DetectaBoxMail(box_mail.getSelectedItem().toString());
        if (cop_cid.equals("COP NET")) {
            //int index = box_mail.getSelectedIndex();
            //index = 1;
            switch (index) {
                case 0:
                    mass_mail_send_epo(false);
                    break;
                case 1:
                    mass_mail_send_iat(false);
                    break;
                case 2:
                    mass_mail_send_epo(true);
                    break;
                case 3:
                    mass_mail_send_iat(true);
                    break;
            }
        } else {
            //int index = box_mail.getSelectedIndex();
            //index = 0;
            switch (index) {
                case 0:
                    mass_mail_dth(false);
                    break;
                case 1:
                    global.show_error_message("Opção não disponível para o painel DTH!");
                    break;
                case 2:
                    mass_mail_dth(true);
                    break;
                case 3:
                    global.show_error_message("Opção não disponível para o painel DTH!");
                    break;
            }
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if (cop_cid.equals("COP DTH")) {

            global.show_error_message("Funcionalidade não disponível para o painel DTH!");
            return;

        } else {

            envio_sms();

        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jbt_mais_detalhesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt_mais_detalhesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbt_mais_detalhesActionPerformed

    private void ReorganizaTela() {
        if (this.getBounds().width < 1200) {
            //global.show_warning_message("pequeno");
            jButton1.setText("");
            jButton2.setText("");
            jbtn_marcar_casos.setText("");
            jButton4.setText("");
            jButton5.setText("");
            jButton1.setSize(jButton1.getMinimumSize());
            jButton2.setSize(jButton2.getMinimumSize());
            jbtn_marcar_casos.setSize(jbtn_marcar_casos.getMinimumSize());
            jButton4.setSize(jButton4.getMinimumSize());
            jButton5.setSize(jButton5.getMinimumSize());
        } else {
            //global.show_warning_message("grande");
            jButton1.setText("Atualizar");
            jButton2.setText("Filtros");
            jbtn_marcar_casos.setText("Marcar casos");
            jButton4.setText("Enviar E-mails");
            jButton5.setText("Envio SMS");
            jButton1.setSize(jButton1.getPreferredSize());
            jButton2.setSize(jButton2.getPreferredSize());
            jbtn_marcar_casos.setSize(jbtn_marcar_casos.getPreferredSize());
            jButton4.setSize(jButton4.getPreferredSize());
            jButton5.setSize(jButton5.getPreferredSize());
        }
    }
    
    private void formAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_formAncestorResized
        // TODO add your handling code here:
        ReorganizaTela();
    }//GEN-LAST:event_formAncestorResized

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        ReorganizaTela();
    }//GEN-LAST:event_formComponentShown

    public void mass_mail_send_epo(boolean is_pend) {

        //ResultSet rs2;
        //String sAreaDespacho;

        try {

            Connection conn = Db_class.mysql_conn();

            String query_parceiras = "SELECT "
                    + "ifnull(cidade,'') as cidade, "
                    + "ifnull(parceira,'') as parceira, "
                    + "ifnull(emails,'') as email, "
                    + "ifnull(tipo,'') as tipo "
                    + "FROM parceiras_view_novo_002 "
                    //+ "WHERE parceira is not null "
                    + "where atividade = 'CasosCriticosEPO' and parceira is not null "
                    + "GROUP BY cidade, parceira "
                    + "ORDER BY parceira";

            ResultSet rs = Db_class.mysql_result(conn, query_parceiras);

            String assunto = "";
            String to = "";
            String cc = "";
            String table_code = "";
            String in_clause = "";
            int cont_mails = 0;
            boolean control_parceira = false;

            while (rs.next()) {
                if (rs.getString(4).trim().equals("NORMAL")) {

                    String parceira_base = rs.getString(2);
                    String cidade_base = rs.getString(1);
                    String mail_group = rs.getString(3);

                    table_code = "<table border=\"1\" cellpadding=\"5\">"
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

                    control_parceira = false;

                    int[] selected_rows = main_tab.getSelectedRows();

                    for (int id : selected_rows) {

                        int id_caso = (int) main_tab.getValueAt(id, 0);
                        String cidade = (String) main_tab.getValueAt(id, 2);
                        String contrato = (String) main_tab.getValueAt(id, 1);
                        String tipo_os = (String) main_tab.getValueAt(id, 5);
                        String data = (String) main_tab.getValueAt(id, 3);
                        String janela = (String) main_tab.getValueAt(id, 4);
                        String parceira = (String) main_tab.getValueAt(id, 7);
                        String tecnico = (String) main_tab.getValueAt(id, 14);
                        String status = (String) main_tab.getValueAt(id, 9);
                        String canal_atend = (String) main_tab.getValueAt(id, 11);
                        String obs = (String) main_tab.getValueAt(id, 13);
                        String status_net = (String) main_tab.getValueAt(id, 16);
                        String area_despacho = (String) main_tab.getValueAt(id, 18);

                        /*
                        //pegando a Area Despacho
                        //rs2 = Db_class.mysql_result(conn, "select trim(area_despacho) as area_despacho from painel where id = " + id_caso);
                        //rs2.next();
                        //sAreaDespacho = rs2.getString(1);
                        //rs2 = null;
                        //
                        */

                        /*
                        if (parceira_base.equals(parceira) && cidade_base.equals(cidade)) {
                            int xi;
                            xi = 0;
                        }
                        */

                        if (parceira_base.equals(parceira) && cidade_base.equals(cidade)
                                && !parceira.equals("NET") && !status_net.equals("OS Executada")) {

                            if (!control_parceira) {

                                control_parceira = true;

                                if (!is_pend) {

                                    assunto = "Sinalização Ouvidoria - " + cidade + " - "
                                            + parceira + " - " + data;

                                } else {

                                    assunto = "Pendência Ouvidoria - " + cidade + " - "
                                            + parceira + " - " + janela;

                                }

                                ResultSet rs_cc;
                                String query_cc = "";

                                //query_cc = "select email from cop_info_tecnica ";
                                query_cc = "select email from vw_cop_info_tecnica ";
                                query_cc = query_cc + "where cidade like '" + cidade + "%' ";
                                query_cc = query_cc + "and ";
                                query_cc = query_cc + "(";
                                query_cc = query_cc + "cidade like '%" + area_despacho + "' ";
                                query_cc = query_cc + "or cidade like concat('%',";
                                query_cc = query_cc + "(";
                                query_cc = query_cc + "select grupo from tb_cidadeXdespachoXgrupo ";
                                query_cc = query_cc + "where cidade = '" + cidade + "' ";
                                query_cc = query_cc + "and area_despacho = '" + area_despacho + "' ";
                                query_cc = query_cc + "limit 1";
                                query_cc = query_cc + ")";
                                query_cc = query_cc + ")";
                                query_cc = query_cc + ")";
                                query_cc = query_cc + "and atividade = 'CasosCriticosEPO'";

                                rs_cc = Db_class.mysql_result(conn, query_cc);
                                while (rs_cc.next()) {
                                    cc = cc + rs_cc.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                                }
                                rs_cc = null;

                                if ("".equals(cc)) {
                                    //até a versão 1.6.2
                                    //query_cc = "SELECT email from cop_info_tecnica WHERE cidade = '" + cidade + "'";
                                    query_cc = "SELECT email from vw_cop_info_tecnica WHERE cidade = '" + cidade + "' ";
                                    query_cc = query_cc + "and atividade = 'CasosCriticosEPO'";
                                    
                                    rs_cc = Db_class.mysql_result(conn, query_cc);
                                    while (rs_cc.next()) {
                                        cc = cc + rs_cc.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                                    }
                                    rs_cc = null;
                                }

                                /*
                                 String query_cc = "SELECT emails FROM tecnica_view WHERE cidade = '" + cidade + "'";
                                 ResultSet rs_cc = Db_class.mysql_result(conn, query_cc);
                                 rs_cc.next();
                                 cc = rs_cc.getString(1);
                                 */
                            }

                            if (cont_mails == 0) {

                                in_clause = "" + id_caso;
                                cont_mails++;

                            } else {

                                in_clause = in_clause + ", " + id_caso;
                                cont_mails++;

                            }

                            table_code = table_code + "<tr>"
                                    + "<td>" + cidade + "</td>"
                                    + "<td>" + contrato + "</td>"
                                    + "<td>" + tipo_os + "</td>"
                                    + "<td>" + data + "</td>"
                                    + "<td>" + janela + "</td>"
                                    + "<td>" + parceira + "</td>"
                                    + "<td>" + tecnico + "</td>"
                                    + "<td>" + status + "</td>"
                                    + "<td>" + canal_atend + "</td>"
                                    + "<td>" + obs + "</td>"
                                    + "<td>" + area_despacho + "</td>"
                                    + "</tr>";

                        }

                    }

                    table_code = table_code + "</table>";

                    String body_1 = "";
                    String body_2 = "";

                    if (!is_pend) {

                        body_1 = global.get_saudacao() + "<br><br>"
                                + "Segue caso de ouvidoria a ser atendido.<br><br>"
                                + "Atendimento deve ser realizado dentro do 1° horário do início da janela, ou conforme conveniência do cliente.<br><br>";

                    } else {

                        body_1 = global.get_saudacao() + "<br><br>"
                                + "Segue caso de ouvidoria PENDENTE DE EXECUÇÃO.<br><br>"
                                + "Favor nos informar como esta o andamento/ execução desta OS visto que, o período de agendamento esta se encerrando.<br><br>";

                    }

                    String final_html = body_1 + table_code + body_2;

                    if (control_parceira) {

                        SendMail.send(final_html, assunto, mail_group, cc, mn);

                    }

                } else {

                    String parceira_base = rs.getString(2);
                    String cidade_base = rs.getString(1);
                    String mail_group = rs.getString(3);

                    table_code = "<table border=\"1\" cellpadding=\"5\">"
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

                    control_parceira = false;

                    int[] selected_rows = main_tab.getSelectedRows();

                    for (int id : selected_rows) {

                        int id_caso = (int) main_tab.getValueAt(id, 0);
                        String cidade = (String) main_tab.getValueAt(id, 2);
                        String contrato = (String) main_tab.getValueAt(id, 1);
                        String tipo_os = (String) main_tab.getValueAt(id, 5);
                        String data = (String) main_tab.getValueAt(id, 3);
                        String janela = (String) main_tab.getValueAt(id, 4);
                        String parceira = (String) main_tab.getValueAt(id, 7);
                        String tecnico = (String) main_tab.getValueAt(id, 14);
                        String status = (String) main_tab.getValueAt(id, 9);
                        String canal_atend = (String) main_tab.getValueAt(id, 11);
                        String obs = (String) main_tab.getValueAt(id, 13);
                        String status_net = (String) main_tab.getValueAt(id, 16);
                        String area_despacho = (String) main_tab.getValueAt(id, 18);

                        /*
                        //pegando a Area Despacho
                        rs2 = Db_class.mysql_result(conn, "select trim(area_despacho) as area_despacho from painel where id = " + id_caso);
                        rs2.next();
                        sAreaDespacho = rs2.getString(1);
                        rs2 = null;
                        //
                        */

                        /*
                        if (parceira_base.equals(parceira) && cidade_base.equals(cidade)) {
                            int xi;
                            xi = 0;
                        }
                        */

                        if (parceira_base.equals(parceira) && cidade_base.equals(cidade)
                                && !parceira.equals("NET") && !status_net.equals("OS Executada")) {

                            if (!control_parceira) {
                                control_parceira = true;
                                if (!is_pend) {
                                    assunto = "Sinalização Casos Críticos - " + cidade + " - "
                                            + parceira + " - " + data;
                                } else {
                                    assunto = "Pendência Ouvidoria - " + cidade + " - "
                                            + parceira + " - " + janela;
                                }

                                ResultSet rs_cc;
                                String query_cc = "";

                                //query_cc = "select email from cop_info_tecnica ";
                                query_cc = "select email from vw_cop_info_tecnica ";
                                query_cc = query_cc + "where cidade like '" + cidade + "%' ";
                                query_cc = query_cc + "and ";
                                query_cc = query_cc + "(";
                                query_cc = query_cc + "cidade like '%" + area_despacho + "' ";
                                query_cc = query_cc + "or cidade like concat('%',";
                                query_cc = query_cc + "(";
                                query_cc = query_cc + "select grupo from tb_cidadeXdespachoXgrupo ";
                                query_cc = query_cc + "where cidade = '" + cidade + "' ";
                                query_cc = query_cc + "and area_despacho = '" + area_despacho + "' ";
                                query_cc = query_cc + "limit 1";
                                query_cc = query_cc + ")";
                                query_cc = query_cc + ")";
                                query_cc = query_cc + ") ";
                                query_cc = query_cc + "and atividade = 'CasosCriticosEPO'";

                                rs_cc = Db_class.mysql_result(conn, query_cc);
                                while (rs_cc.next()) {
                                    cc = cc + rs_cc.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                                }
                                rs_cc = null;

                                if ("".equals(cc)) {
                                    //até a versão 1.6.2
                                    //query_cc = "SELECT email from cop_info_tecnica WHERE cidade = '" + cidade + "'";
                                    query_cc = "SELECT email from vw_cop_info_tecnica WHERE cidade = '" + cidade + "' ";
                                    query_cc = query_cc + "and atividade = 'CasosCriticosEPO'";
                                    rs_cc = Db_class.mysql_result(conn, query_cc);
                                    while (rs_cc.next()) {
                                        cc = cc + rs_cc.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                                    }
                                    rs_cc = null;
                                }

                                /*
                                 String query_cc = "SELECT emails FROM tecnica_view WHERE cidade = '" + cidade + "'";
                                 ResultSet rs_cc = Db_class.mysql_result(conn, query_cc);
                                 rs_cc.next();
                                 cc = rs_cc.getString(1);
                                 */
                            }

                            if (cont_mails == 0) {
                                in_clause = "" + id_caso;
                                cont_mails++;
                            } else {
                                in_clause = in_clause + ", " + id_caso;
                                cont_mails++;
                            }

                            table_code = table_code + "<tr>"
                                    + "<td>" + cidade + "</td>"
                                    + "<td>" + contrato + "</td>"
                                    + "<td>" + tipo_os + "</td>"
                                    + "<td>" + data + "</td>"
                                    + "<td>" + janela + "</td>"
                                    + "<td>" + parceira + "</td>"
                                    + "<td>" + status + "</td>"
                                    + "<td>" + canal_atend + "</td>"
                                    + "<td>" + obs + "</td>"
                                    + "<td>" + area_despacho + "</td>"
                                    + "</tr>";

                        }

                    }

                    table_code = table_code + "</table>";

                    String body_1 = "";
                    String body_2 = "";

                    if (!is_pend) {

                        body_1 = global.get_saudacao() + "<br><br>"
                                + "Segue caso de ouvidoria a ser atendido.<br><br>"
                                + "Atendimento deve ser realizado dentro do 1° horário do início da janela, ou conforme conveniência do cliente.<br><br>";

                    } else {

                        body_1 = global.get_saudacao() + "<br><br>"
                                + "Segue caso de ouvidoria PENDENTE DE EXECUÇÃO.<br><br>"
                                + "Favor nos informar como esta o andamento/ execução desta OS visto que, o período de agendamento esta se encerrando.<br><br>";

                    }

                    String final_html = body_1 + table_code + body_2;

                    if (control_parceira) {

                        SendMail.send(final_html, assunto, mail_group, cc, mn);

                    }

                }

            }

            //cont_mails = 0; //forcando erro
            if (cont_mails > 0) {

                String query_mail = "";

                if (!is_pend) {

                    if (this instanceof painel_encaixe) {

                        query_mail = "UPDATE painel SET email = 'Sim', status = 'Em Acompanhamento', "
                                + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                                + "Enviado e-mail de sinalização.\n\n', obs) "
                                + "WHERE id IN (" + in_clause + ")";

                    } else {

                        query_mail = "UPDATE painel SET email = 'Sim', "
                                + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                                + "Enviado e-mail de sinalização.\n\n', obs) "
                                + "WHERE id IN (" + in_clause + ")";

                    }

                } else if (this instanceof painel_encaixe) {

                    query_mail = "UPDATE painel SET email = 'Sim', status = 'Em Acompanhamento', "
                            + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                            + "Enviado e-mail de pendência.\n\n', obs) "
                            + "WHERE id IN (" + in_clause + ")";

                } else {

                    query_mail = "UPDATE painel SET email = 'Sim', "
                            + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                            + "Enviado e-mail de pendência.\n\n', obs) "
                            + "WHERE id IN (" + in_clause + ")";

                }

                Db_class.mysql_insert(query_mail, conn);

                atualiza_painel();
            } else {
                global.show_warning_message(
                        "É necessário selecionar ao menos um contrato com parceira definida.\n"
                        + "Caso o tenha feito, alguns critérios podem não ter sido satisfeitos.\n\n"
                        + "Favor verificar.\n\n"
                        + "Critérios:\n"
                        + "1) Cidade deve estar devidamente cadastrada\n"
                        + "2) Parceira deve estar devidamente cadastrada\n"
                        + "3) Parceira não deve ser 'NET'\n"
                        + "4) Status da requisição não deve ser 'OS Executada'"
                        + "\n\n"
                        + "Os supervisores conseguem localizar Parceiras e suas Cidades\n"
                        + "de forma mais abrangente. Às vezes, uma pequena diferença no nome de\n"
                        + "parceira bloqueia a geração do e-mail."
                );
            }

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
            global.show_error_message(
                    "Erro identificado.\n\n"
                    + "Detalhes\n"
                    + ex.getMessage()
            );
        }

    }

    public void mass_mail_send_iat(boolean is_pend) {

        try {

            Connection conn = Db_class.mysql_conn();

            String query_parceiras = "SELECT "
                    + "ifnull(cidade,'') as cidade, "
                    + "ifnull(emails,'') as email "
                    //+ "FROM tecnica_view "
                    + "FROM tecnica_view_iat "
                    + "GROUP BY cidade "
                    + "ORDER BY cidade";

            ResultSet rs = Db_class.mysql_result(conn, query_parceiras);

            String assunto = "";
            String to = "";
            String cc = "";
            String table_code = "";
            String in_clause = "";
            boolean control_parceira = false;
            int cont_mails = 0;

            while (rs.next()) {

                String cidade_base = rs.getString(1);

                //String mail_group = rs.getString(2); //codigo ate a versao 1.6.3
                String mail_group = "";
                //String query_mail_group = "SELECT email from cop_info_tecnica WHERE cidade = '" + cidade_base + "'";
                String query_mail_group = "SELECT email from vw_cop_info_tecnica WHERE cidade = '" + cidade_base + "' ";
                query_mail_group = query_mail_group + "and atividade = 'CasosCriticosIAT'";

                ResultSet rs_mail_group = Db_class.mysql_result(conn, query_mail_group);
                while (rs_mail_group.next()) {
                    mail_group = mail_group + rs_mail_group.getString(1).replaceAll("<", "").replaceAll(">", "").trim() + ";";
                }
                rs_mail_group = null;
                //

                table_code = "<table border=\"1\" cellpadding=\"5\">"
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
                        + "</tr>";

                control_parceira = false;

                int[] selected_rows = main_tab.getSelectedRows();

                for (int id : selected_rows) {

                    int id_caso = (int) main_tab.getValueAt(id, 0);
                    String cidade = (String) main_tab.getValueAt(id, 2);
                    String contrato = (String) main_tab.getValueAt(id, 1);
                    String tipo_os = (String) main_tab.getValueAt(id, 5);
                    String data = (String) main_tab.getValueAt(id, 3);
                    String janela = (String) main_tab.getValueAt(id, 4);
                    String parceira = (String) main_tab.getValueAt(id, 7);
                    String tecnico = (String) main_tab.getValueAt(id, 14);
                    String status = (String) main_tab.getValueAt(id, 9);
                    String canal_atend = (String) main_tab.getValueAt(id, 11);
                    String obs = (String) main_tab.getValueAt(id, 13);
                    String status_net = (String) main_tab.getValueAt(id, 16);

                    if (cidade_base.equals(cidade)
                            && parceira.equals("NET") && !status_net.equals("OS Executada")) {

                        if (!control_parceira) {
                            control_parceira = true;
                            if (!is_pend) {
                                assunto = "Sinalização Casos Críticos - " + cidade + " - "
                                        + "Equipes NET - " + data;
                            } else {
                                assunto = "Pendência Ouvidoria - " + cidade + " - "
                                        + "Equipes NET - " + janela;
                            }
                        }

                        if (cont_mails == 0) {
                            in_clause = "" + id_caso;
                            cont_mails++;
                        } else {
                            in_clause = in_clause + ", " + id_caso;
                            cont_mails++;
                        }

                        table_code = table_code + "<tr>"
                                + "<td>" + cidade + "</td>"
                                + "<td>" + contrato + "</td>"
                                + "<td>" + tipo_os + "</td>"
                                + "<td>" + data + "</td>"
                                + "<td>" + janela + "</td>"
                                + "<td>" + parceira + "</td>"
                                + "<td>" + tecnico + "</td>"
                                + "<td>" + status + "</td>"
                                + "<td>" + canal_atend + "</td>"
                                + "<td>" + obs + "</td>"
                                + "</tr>";

                    }

                }

                table_code = table_code + "</table>";

                String body_1 = "";
                String body_2 = "";

                if (!is_pend) {

                    body_1 = global.get_saudacao() + "<br><br>"
                            + "Segue caso de ouvidoria a ser atendido.<br><br>"
                            + "Atendimento deve ser realizado dentro do 1° horário do início da janela, ou conforme conveniência do cliente.<br><br>";

                } else {

                    body_1 = global.get_saudacao() + "<br><br>"
                            + "Segue caso de ouvidoria PENDENTE DE EXECUÇÃO.<br><br>"
                            + "Favor nos informar como esta o andamento/ execução desta OS visto que, o período de agendamento esta se encerrando.<br><br>";

                }

                String final_html = body_1 + table_code + body_2;

                if (control_parceira) {

                    SendMail.send(final_html, assunto, mail_group, cc, mn);

                }

            }

            //cont_mails = 0; //forcando erro
            if (cont_mails > 0) {

                String query_mail = "";

                if (!is_pend) {

                    if (this instanceof painel_encaixe) {

                        query_mail = "UPDATE painel SET email = 'Sim', status = 'Em Acompanhamento', "
                                + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                                + "Enviado e-mail de sinalização.\n\n', obs) "
                                + "WHERE id IN (" + in_clause + ")";

                    } else {

                        query_mail = "UPDATE painel SET email = 'Sim', "
                                + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                                + "Enviado e-mail de sinalização.\n\n', obs) "
                                + "WHERE id IN (" + in_clause + ")";

                    }

                } else if (this instanceof painel_encaixe) {

                    query_mail = "UPDATE painel SET email = 'Sim', status = 'Em Acompanhamento', "
                            + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                            + "Enviado e-mail de pendência.\n\n', obs) "
                            + "WHERE id IN (" + in_clause + ")";

                } else {

                    query_mail = "UPDATE painel SET email = 'Sim', "
                            + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                            + "Enviado e-mail de pendência.\n\n', obs) "
                            + "WHERE id IN (" + in_clause + ")";

                }

                Db_class.mysql_insert(query_mail, conn);

                atualiza_painel();

            } else {
                global.show_warning_message(
                        "É necessário selecionar ao menos um contrato.\n"
                        + "Caso o tenha feito, alguns critérios podem não ter sido satisfeitos.\n"
                        + "Favor verificar.\n\n"
                        + "Critérios:\n"
                        + "1) Cidade deve estar devidamente cadastrada\n"
                        + "3) Parceira deve ser 'NET'\n"
                        + "4) Status da requisição não deve ser 'OS Executada'"
                        + "\n\n"
                        + "Os supervisores conseguem localizar Parceiras e suas Cidades\n"
                        + "de forma mais abrangente. Às vezes, uma pequena diferença no nome de\n"
                        + "parceira bloqueia a geração do e-mail."
                );
            }

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
            global.show_error_message(
                    "Erro identificado.\n\n"
                    + "Detalhes\n"
                    + ex.getMessage()
            );
        }

    }

    public void mass_mail_dth(boolean is_pend) {

        try {

            Connection conn_contatos = Db_class.mysql_conn();
            Connection conn = Db_class.mysql_conn();

            int[] selected_rows_parceira = main_tab.getSelectedRows();
            String in_clause_parceiro = "";

            boolean control_first = false;

            for (int id : selected_rows_parceira) {

                if (!control_first) {

                    in_clause_parceiro = "'" + main_tab.getValueAt(id, 7) + "'";
                    control_first = true;

                } else {

                    in_clause_parceiro = in_clause_parceiro + ",'" + main_tab.getValueAt(id, 7) + "'";

                }

            }

            String query_parceiras = "SELECT "
                    + "'', "
                    + "codigo as parceiro, "
                    + "GROUP_CONCAT(DISTINCT CONCAT(email1,';', email2) separator ';') as email_parceiro, "
                    + "'NORMAL' "
                    + "FROM cop_info_dth "
                    + "WHERE codigo IN (" + in_clause_parceiro + ") "
                    + "GROUP BY codigo "
                    + "ORDER BY codigo";

            ResultSet rs = Db_class.mysql_result(conn_contatos, query_parceiras);

            String assunto = "";
            String to = "";
            String cc = "";
            String table_code = "";
            String in_clause = "";
            int cont_mails = 0;
            boolean control_parceira = false;

            while (rs.next()) {

                String parceira_base = rs.getString(2);
                String cidade_base = rs.getString(1);
                String mail_group = rs.getString(3);

                table_code = "<table border=\"1\" cellpadding=\"5\">"
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
                        + "</tr>";

                control_parceira = false;

                int[] selected_rows = main_tab.getSelectedRows();

                for (int id : selected_rows) {

                    int id_caso = (int) main_tab.getValueAt(id, 0);
                    String cidade = (String) main_tab.getValueAt(id, 2);
                    String contrato = (String) main_tab.getValueAt(id, 1);
                    String tipo_os = (String) main_tab.getValueAt(id, 5);
                    String data = (String) main_tab.getValueAt(id, 3);
                    String janela = (String) main_tab.getValueAt(id, 4);
                    String parceira = (String) main_tab.getValueAt(id, 7);
                    String tecnico = (String) main_tab.getValueAt(id, 14);
                    String status = (String) main_tab.getValueAt(id, 9);
                    String canal_atend = (String) main_tab.getValueAt(id, 11);
                    String obs = (String) main_tab.getValueAt(id, 13);
                    String status_net = (String) main_tab.getValueAt(id, 16);

                    if (parceira_base.equals(parceira) && !parceira.equals("NET")
                            && !status_net.equals("OS Executada")) {

                        if (!control_parceira) {

                            control_parceira = true;

                            if (!is_pend) {

                                assunto = "Sinalização Casos Críticos - " + cidade + " - "
                                        + parceira + " - " + data;

                            } else {

                                assunto = "Pendência Casos Críticos - " + cidade + " - "
                                        + parceira + " - " + janela;

                            }

                            String query_mr = "SELECT "
                                    + "if(cargo = 'Analista', email,'') as email "
                                    + "FROM cop_info_dth "
                                    + "WHERE "
                                    + "mr = '" + cidade + "' "
                                    + "GROUP BY mr "
                                    + "ORDER BY mr";

                            ResultSet rs_mr = Db_class.mysql_result(conn_contatos, query_mr);

                            if (rs_mr.next()) {

                                cc = rs_mr.getString(1);

                            }

                        }

                        if (cont_mails == 0) {

                            in_clause = "" + id_caso;
                            cont_mails++;

                        } else {

                            in_clause = in_clause + ", " + id_caso;
                            cont_mails++;

                        }

                        table_code = table_code + "<tr>"
                                + "<td>" + cidade + "</td>"
                                + "<td>" + contrato + "</td>"
                                + "<td>" + tipo_os + "</td>"
                                + "<td>" + data + "</td>"
                                + "<td>" + janela + "</td>"
                                + "<td>" + parceira + "</td>"
                                + "<td>" + tecnico + "</td>"
                                + "<td>" + status + "</td>"
                                + "<td>" + canal_atend + "</td>"
                                + "<td>" + obs + "</td>"
                                + "</tr>";

                    }

                }

                table_code = table_code + "</table>";

                String body_1 = "";
                String body_2 = "";

                if (!is_pend) {

                    body_1 = global.get_saudacao() + "<br><br>"
                            + "Segue caso de ouvidoria a ser atendido.<br><br>"
                            + "Atendimento deve ser realizado dentro do 1° horário do início da janela, ou conforme conveniência do cliente.<br><br>";

                } else {

                    body_1 = global.get_saudacao() + "<br><br>"
                            + "Segue caso de ouvidoria PENDENTE DE EXECUÇÃO.<br><br>"
                            + "Favor nos informar como esta o andamento/ execução desta OS visto que, o período de agendamento esta se encerrando.<br><br>";

                }

                String final_html = body_1 + table_code + body_2;

                if (control_parceira) {

                    SendMail.send(final_html, assunto, mail_group, cc, mn);

                }

            }

            //cont_mails = 0; //forcando erro
            if (cont_mails > 0) {

                String query_mail = "";

                if (!is_pend) {

                    if (this instanceof painel_encaixe) {

                        query_mail = "UPDATE painel SET email = 'Sim', status = 'Em Acompanhamento', "
                                + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                                + "Enviado e-mail de sinalização.\n\n', obs) "
                                + "WHERE id IN (" + in_clause + ")";

                    } else {

                        query_mail = "UPDATE painel SET email = 'Sim', "
                                + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                                + "Enviado e-mail de sinalização.\n\n', obs) "
                                + "WHERE id IN (" + in_clause + ")";

                    }

                } else if (this instanceof painel_encaixe) {

                    query_mail = "UPDATE painel SET email = 'Sim', status = 'Em Acompanhamento', "
                            + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                            + "Enviado e-mail de pendência.\n\n', obs) "
                            + "WHERE id IN (" + in_clause + ")";

                } else {

                    query_mail = "UPDATE painel SET email = 'Sim', "
                            + "obs = CONCAT('" + mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": "
                            + "Enviado e-mail de pendência.\n\n', obs) "
                            + "WHERE id IN (" + in_clause + ")";

                }

                Db_class.mysql_insert(query_mail, conn);

                atualiza_painel();

            } else {
                global.show_warning_message(
                        "É necessário selecionar ao menos um contrato.\n"
                        + "Caso o tenha feito, alguns critérios podem não ter sido satisfeitos.\n"
                        + "Favor verificar.\n\n"
                        + "Critérios:\n"
                        + "1) Parceira deve estar devidamente cadastrada\n"
                        + "2) Parceira não deve ser 'NET'\n"
                        + "3) Status da requisição não deve ser 'OS Executada'"
                        + "\n\n"
                        + "Os supervisores conseguem localizar Parceiras e suas Cidades\n"
                        + "de forma mais abrangente. Às vezes, uma pequena diferença no nome de\n"
                        + "parceira bloqueia a geração do e-mail."
                );
            }

            Db_class.close_conn(conn_contatos);
            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
            global.show_error_message(
                    "Erro identificado.\n\n"
                    + "Detalhes\n"
                    + ex.getMessage()
            );
        }

    }

    public void marcar_casos() {

        int[] selected_indexes = main_tab.getSelectedRows();
        String in_clause = "";

        int count = 0;
        int count_errors = 0;
        int count_users = 0;

        for (int i : selected_indexes) {
            //Object obj_id = main_tab.getValueAt(i, 0);
            //int id = (int) obj_id;

            Object obj_id;
            long id;
            if (ifl_n3 == 1) {
                obj_id = main_tab.getValueAt(i, 18);
                id = (long) obj_id;
            } else {
                UtilBD utilBD = new UtilBD();
                id = utilBD.Identifica_IdMestre(Integer.parseInt(main_tab.getValueAt(i, 0).toString()));
                utilBD = null;
            }

            String status = (String) main_tab.getValueAt(i, 9);
            String user_trat = (String) main_tab.getValueAt(i, 12);

            if ((!status.equals("Encerrado") && !status.equals("Encerrado - Os Duplicada/Cancelada"))
                    && !user_trat.equals(mn.get_nome())) {
                if (count == 0) {
                    in_clause = "" + id;
                } else {
                    in_clause = in_clause + "," + id + "";
                }

                count++;
            } else if (status.equals("Encerrado") || status.equals("Encerrado - Os Duplicada/Cancelada")) {
                count_errors++;
            } else if (user_trat.equals(mn.get_nome())) {
                count_users++;
            }
        }
        try {
            if (count <= 0 && count_errors == 0 && count_users == 0) {
                global.show_error_message("Selecione pelo menos um caso válido!");
                return;
            }

            if (count_errors > 0) {
                global.show_error_message("Casos encerrados não podem ser marcados!");
                return;
            }

            if (count_users > 0) {
                global.show_error_message("Este caso já está marcado com o seu nome!");
                return;
            }

            Connection conn = Db_class.mysql_conn();
            /*
            String query = "UPDATE painel SET obs = CONCAT('" + mn.get_nome() + " em "
                    + global.get_br_date_time(new Date()) + ": Caso marcado para tratamento.\n\n',obs) , user_mark = '" + mn.get_nome() + "'"
                    + "WHERE id IN (" + in_clause + ")";
            */
            String query = "UPDATE painel "
                    + "SET "
                    + "obs = CONCAT('" + mn.get_nome() + " em "
                    + global.get_br_date_time(new Date()) + ": Caso marcado para tratamento.\n\n',obs), "
                    + "user_mark = '" + mn.get_nome() + "' "
                    + "WHERE id_mestre IN (" + in_clause + ") "
                    + "and fl_n3 = " + ifl_n3;

            Db_class.mysql_insert(query, conn);

            Db_class.close_conn(conn);

            atualiza_painel();

            global.show_message("Casos marcados com sucesso!");

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void set_filter_indexes(int[] city_indexes, int[] type_indexes, int[] janela_indexes, SortedSet<Date> dt_acomp) {

        this.city_indexes = city_indexes;
        this.type_indexes = type_indexes;
        this.janela_indexes = janela_indexes;
        //this.filter_date = data;
        this.dt_acomp_indexes = dt_acomp;

        current_ids.clear();

    }

    public void set_filter_querys(String query_cidade, String query_tipo, String query_janela, String query_dt_acomp) {

        this.filter_city_query = query_cidade;
        this.filter_type_query = query_tipo;
        this.filter_janela_query = query_janela;
        this.filter_date_query = query_dt_acomp;

    }

    public JButton getjButton1() {
        return jButton1;
    }

    public void setjButton1(JButton jButton1) {
        this.jButton1 = jButton1;
    }

    public JButton getjButton2() {
        return jButton2;
    }

    public void setjButton2(JButton jButton2) {
        this.jButton2 = jButton2;
    }

    public JCheckBox getjCheckBox1() {
        return jCheckBox1;
    }

    public void setjCheckBox1(JCheckBox jCheckBox1) {
        this.jCheckBox1 = jCheckBox1;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public void setjPanel1(JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    public JLabel getLabel_encaixe() {
        return label_encaixe;
    }

    public JLabel getLabel_qtd() {
        return label_qtd_encaixe;
    }

    public JLabel getlabel_panel() {

        return label_qtd_casos;

    }

    public JLabel getlabel_passado() {

        return label_qtd_passado;

    }

    public class Task extends TimerTask {

        @Override
        public void run() {

            main_painel.atualiza_encaixe();

        }

    }

    class CustomRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 6703872492730589499L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status_monit = (String) table.getValueAt(row, 10);
            String status_caso = (String) table.getValueAt(row, 9);
            String status_net = (String) table.getValueAt(row, 16);

            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(true);

            /*
            if (table.getValueAt(row, 1) == "10707361") {
                int x = 0;
            }
            */
           
            if ((status_caso.equals("Encerrado")
                    || status_caso.equals("Encerrado - Os Duplicada/Cancelada"))
                    && isSelected) {

                cellComponent.setBackground(global.get_color(128, 128, 128));
                cellComponent.setForeground(table.getSelectionForeground());

            } else if (status_caso.equals("Encerrado")
                    || status_caso.equals("Encerrado - Os Duplicada/Cancelada")) {

                cellComponent.setBackground(global.get_color(192, 192, 192));
                cellComponent.setForeground(table.getForeground());

            } else if (status_net.equals("OS Executada") && isSelected) {

                cellComponent.setBackground(global.get_color(0, 102, 204));
                cellComponent.setForeground(table.getSelectionForeground());

            } else if (status_net.equals("OS Executada")) {

                cellComponent.setBackground(global.get_color(204, 229, 255));
                cellComponent.setForeground(table.getForeground());

            } else if (status_monit.equals("TC17 – Técnico no local") && isSelected) {

                cellComponent.setBackground(global.get_color(102, 204, 0));
                cellComponent.setForeground(table.getSelectionForeground());

            } else if (status_monit.equals("TC17 – Técnico no local")) {

                cellComponent.setBackground(global.get_color(204, 255, 153));
                cellComponent.setForeground(table.getForeground());

            } else if (status_monit.equals("IVR – SLA será Cumprido") && isSelected) {

                cellComponent.setBackground(global.get_color(240, 128, 128));
                cellComponent.setForeground(table.getSelectionForeground());

            } else if (status_monit.equals("IVR – SLA será Cumprido")) {

                cellComponent.setBackground(global.get_color(255, 204, 204));
                cellComponent.setForeground(table.getForeground());

            } else if (status_monit.equals("IVR – Atraso") && isSelected) {

                cellComponent.setBackground(global.get_color(178, 102, 255));
                cellComponent.setForeground(table.getSelectionForeground());

            } else if (status_monit.equals("IVR – Atraso")) {

                cellComponent.setBackground(global.get_color(204, 153, 255));
                cellComponent.setForeground(table.getForeground());

            } else if (isSelected) {

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

    public void envio_sms() {

        try {
            int[] selected_rows = main_tab.getSelectedRows();

            if (selected_rows.length == 0) {

                global.show_error_message("É necessário selecionar pelo menos uma linha!");
                return;

            }

            int result = global.dialog_question("Confirma o envio de SMS para " + selected_rows.length + " casos?");

            if (result != 0) {

                return;

            }

            List<String> list_errors = new ArrayList<String>();

            Connection conn = Db_class.mysql_conn();

            for (int row : selected_rows) {

                int num_id = (int) main_tab.getValueAt(row, 0);
                String tecnico = (String) main_tab.getValueAt(row, 8);
                String phone = (String) main_tab.getValueAt(row, 15);
                String data = (String) main_tab.getValueAt(row, 3);
                String janela = (String) main_tab.getValueAt(row, 4);
                String contrato = (String) main_tab.getValueAt(row, 1);
                String cidade = (String) main_tab.getValueAt(row, 2);
                String sms_check = (String) main_tab.getValueAt(row, 17);

                String texto_sms = "Caso critico " + contrato + " agendamento para " + data + " "
                        + "janela das " + janela + " em sua rota";

                if (!phone.equals("") && (phone.length() >= 10 && phone.length() <= 11)
                        && !sms_check.equals("Sim")) {

                    phone = phone.replace(" ", "");

                    String phone_final = "55" + phone;

                    zenvia_test zt = new zenvia_test();

                    SimpleMessageService sms = zt.get_service(mn.get_zenvia_user(), mn.get_zenvia_password());

                    String num_id_sms = "" + num_id;

                    zt.send(sms, phone_final, texto_sms, num_id_sms, "COP INFORMA");

                    String query = "UPDATE ouvidoria.painel SET sms = 'Sim', "
                            + "obs = CONCAT('" + global.get_obs_hist(mn) + " Enviado "
                            + "sms de sinalização para o técnico " + tecnico + "\n\n',obs)"
                            + "WHERE id = " + num_id;

                    Db_class.mysql_insert(query, conn);

                } else {

                    list_errors.add(cidade + " - " + contrato + " - " + tecnico);

                }

                System.out.println(texto_sms);

            }

            String texto_fora = "";

            for (String erro : list_errors) {

                texto_fora = texto_fora + "\n" + erro;

            }

            if (list_errors.size() > 0) {

                global.show_error_message("Os seguintes casos não puderam ser enviados\n "
                        + "verifique se os casos possuem telefones devidamente cadastrados e "
                        + "tente novamente:" + texto_fora);

            }

            global.show_message("SMS Enviado com sucesso!");

            atualiza_painel();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(painel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void setToolTip_Mais_Detalhes (String sTexto) {
        jbt_mais_detalhes.setToolTipText(sQtdDetalhe);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box_mail;
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton2;
    public javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    public javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbt_mais_detalhes;
    public javax.swing.JButton jbtn_marcar_casos;
    public javax.swing.JLabel label_casos;
    public javax.swing.JLabel label_encaixe;
    public javax.swing.JLabel label_passado;
    public javax.swing.JLabel label_qtd_casos;
    public javax.swing.JLabel label_qtd_encaixe;
    public javax.swing.JLabel label_qtd_passado;
    public javax.swing.JPanel pnl_info_casos;
    public javax.swing.JPanel pnl_info_encaixe;
    public javax.swing.JPanel pnl_info_passado;
    // End of variables declaration//GEN-END:variables
}
