/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author N0026925
 */
public class global {

    public static Integer iBDProducao;
    public static Integer iMaxContrato = 10; //comprimento máximo do número de contrato
    public static Integer iMaxNumOS = 10; //comprimento máximo do número de OS

    public static void fill_combo(JComboBox combo, String query, boolean remove) {

        try {

            Connection conn = Db_class.mysql_conn();

            if (remove) {

                combo.removeAllItems();
                combo.addItem("");

            }

            ResultSet rs = Db_class.mysql_result(conn, query);

            while (rs.next()) {

                combo.addItem(rs.getObject(1));

            }

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static List<Integer> fill_combo_key(JComboBox combo, String query, boolean remove, int piChave, int piTexto) {

        List<Integer> liChaves = new ArrayList<>();

        try {

            Connection conn = Db_class.mysql_conn();

            if (remove) {
                combo.removeAllItems();
                combo.addItem("");
            }

            ResultSet rs = Db_class.mysql_result(conn, query);
            while (rs.next()) {
                liChaves.add(rs.getInt(piChave));
                combo.addItem(rs.getObject(piTexto));
            }

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        }

        return liChaves;

    }

    public static void fill_list_box(JList lista, String query, boolean remove, boolean add_net) {

        DefaultListModel dlm = new DefaultListModel();

        try {

            Connection conn = Db_class.mysql_conn();

            if (remove) {

                dlm.removeAllElements();

            }

            ResultSet rs = Db_class.mysql_result(conn, query);

            if (add_net) {

                dlm.addElement("NET");

            }

            while (rs.next()) {

                dlm.addElement(rs.getObject(1));

            }

            lista.setModel(dlm);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String show_input_text(String sTitulo, String sPadrao) {
        String sResultado = "";

        sResultado = JOptionPane.showInputDialog(sTitulo, sPadrao);

        return sResultado;
    }

    public static int show_perguntaSimNao(String sTitulo, String sPergunta) {
        int iResultado = 0; //não
        int iResposta = JOptionPane.showConfirmDialog(null, sPergunta, sTitulo, JOptionPane.YES_NO_OPTION);

        if (iResposta == 0) {
            iResultado = 1;
        }
        
        return iResultado;
    }

    public static void show_message(String msg) {

        JOptionPane.showMessageDialog(null, msg, "Mensagem", JOptionPane.INFORMATION_MESSAGE);

    }

    public static void show_error_message(String msg) {

        JOptionPane.showMessageDialog(null, msg, "Erro!", JOptionPane.ERROR_MESSAGE);

    }

    public static void show_warning_message(String msg) {

        JOptionPane.showMessageDialog(null, msg, "Erro!", JOptionPane.WARNING_MESSAGE);

    }

    public static boolean check_cp(Component cp) {

        if (cp instanceof JComboBox) {

            JComboBox combo = (JComboBox) cp;

            if (!check_combo_box(combo)) {

                return false;

            }

        }

        if (cp instanceof JTextField) {

            JTextField text = (JTextField) cp;

            if (!check_text_field(text)) {

                return false;

            }

        }

        return true;
    }

    public static boolean check_combo_box(JComboBox combo) {

        if (combo.getSelectedItem().equals("")) {

            return false;

        } else {
            return true;
        }

    }

    public static boolean check_text_field(JTextField txt) {

        if (txt.getText().equals("")) {

            return false;

        } else {

            return true;

        }

    }

    public static String get_simple_date(Date dt) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        df.setLenient(false);

        String date_string = df.format(dt);

        return date_string;

    }

    public static String get_simple_datetime(Date dt) {

        String date_string = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
        return date_string;

    }

    public static String get_br_date(Date dt) {

        String date_string = new SimpleDateFormat("dd/MM/yyyy").format(dt);
        return date_string;

    }

    public static String get_br_date_time(Date dt) {

        String date_string = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dt);
        return date_string;

    }

    public static DefaultTableModel non_edit_buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<Object> columnNames = new Vector<Object>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.addElement(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.addElement(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        DefaultTableModel dtm = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;

            }

        };

        return dtm;

    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<Object> columnNames = new Vector<Object>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            //columnNames.addElement(metaData.getColumnName(column));
            columnNames.addElement(metaData.getColumnLabel(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.addElement(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    public static JTable get_non_edit_Table(String query, JPanel panel) throws Exception {

        Connection conn = null;
        ResultSet rs = null;

        conn = Db_class.mysql_conn();

        rs = Db_class.mysql_result(conn, query);

        JTable tab = new JTable(non_edit_buildTableModel(rs));

        JScrollPane scroller = new JScrollPane(tab);

        panel.add(scroller);

        scroller.setLocation(10, 10);
        scroller.setSize(panel.getWidth() - 20, panel.getHeight() - 20);
        scroller.setVisible(true);
        tab.setVisible(true);
        tab.setSize(scroller.getWidth() - 20, scroller.getHeight() - 20);
        tab.getTableHeader().setReorderingAllowed(false);

        Db_class.close_conn(conn);

        return tab;

    }

    public static JTable getTable(String query, JPanel panel) throws Exception {

        Connection conn = null;
        ResultSet rs = null;

        conn = Db_class.mysql_conn();

        rs = Db_class.mysql_result(conn, query);

        JTable tab = new JTable(buildTableModel(rs));

        JScrollPane scroller = new JScrollPane(tab);

        panel.add(scroller);

        scroller.setLocation(0, 0);
        scroller.setSize(panel.getWidth() - 0, panel.getHeight() - 0);
        scroller.setVisible(true);
        tab.setVisible(true);
        tab.setSize(scroller.getWidth() - 0, scroller.getHeight() - 0);
        tab.getTableHeader().setReorderingAllowed(false);

        Db_class.close_conn(conn);

        return tab;

    }

    public static void hide_columns(int[] indexes, JTable tab) {

        for (int i : indexes) {

            tab.getColumnModel().getColumn(i).setMinWidth(0);
            tab.getColumnModel().getColumn(i).setMaxWidth(0);

        }

    }

    public static void adjust_columns(int[] column_width, JTable tab) {

        int tamanho = column_width.length;

        for (int i = 0; i <= tab.getColumnCount() - 1; i++) {

            tab.getColumnModel().getColumn(i).setPreferredWidth(column_width[i]);

        }

    }

    public static void open_modal(JDialog dlg, String title) {

        dlg.toFront();
        dlg.setModal(true);
        dlg.setLocationRelativeTo(null);
        dlg.pack();
        dlg.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - dlg.getWidth() / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - dlg.getHeight() / 2);

        dlg.setResizable(false);

        dlg.setTitle(title);

        dlg.setVisible(true);

    }

    public static void open_nonmodal(JDialog dlg, String title) {

        dlg.toFront();
        dlg.setModal(false);
        dlg.setLocationRelativeTo(null);
        dlg.pack();
        dlg.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - dlg.getWidth() / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - dlg.getHeight() / 2);

        dlg.setResizable(false);

        dlg.setTitle(title);

        dlg.setVisible(true);

    }

    public static int dialog_question(String msg) {

        int dialog = JOptionPane.YES_NO_OPTION;

        int dialogresult = JOptionPane.showConfirmDialog(null, msg, "Pergunta", dialog);
        return dialogresult;

    }

    public static void select_all_list(JList lista) {

        int start = 0;
        int end = lista.getModel().getSize() - 1;
        if (end >= 0) {
            lista.setSelectionInterval(start, end);
        }

    }

    public static String get_saudacao() {

        String saudacao = "";

        String hour_manha = "12:00:00";
        Date time1;
        try {
            time1 = new SimpleDateFormat("HH:mm:ss").parse(hour_manha);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            String hour_tarde = "18:00:00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(hour_tarde);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);

            Calendar now = Calendar.getInstance();

            int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
            int minute = now.get(Calendar.MINUTE);

            Date date_convert = parseDate(hour + ":" + minute);

            Date date1 = calendar1.getTime();
            Date date2 = calendar2.getTime();

            if (date_convert.before(date1)) {

                saudacao = "Bom Dia";

            } else if (date_convert.after(date1) && date_convert.before(date2)) {

                saudacao = "Boa Tarde";

            } else {

                saudacao = "Boa Noite";

            }
        } catch (ParseException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        }

        return saudacao;

    }

    private static Date parseDate(String date) {

        final String inputFormat = "HH:mm";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    public static Color get_color(int red, int green, int blue) {

        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        float hue = hsb[0];
        float saturation = hsb[1];
        float brightness = hsb[2];

        Color cor = Color.getHSBColor(hue, saturation, brightness);

        return cor;

    }

    public static void initCloseListener(final JDialog jd) {
        jd.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        jd.getRootPane().getActionMap().put("close", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                jd.dispose();

            }
        });
    }

    public static void insert_prod(int id_caso, String acao, Menu mn) {
        try {
            Connection conn = Db_class.mysql_conn();

            String query = "INSERT INTO produtividade "
                    + "(id_caso, acao, login_user, nome_user, cop, date_time_acao)"
                    + "VALUES (" + id_caso + ", '" + acao + "', '" + mn.get_login() + "',"
                    + "'" + mn.get_nome() + "', '" + mn.get_cop() + "', current_timestamp)";

            Db_class.mysql_insert(query, conn);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(tratar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void modal_click(JDialog dlg, String title, Point pt, JTable tab) {

        dlg.toFront();
        dlg.setModal(true);
        dlg.setLocationRelativeTo(tab);
        dlg.pack();
        dlg.setLocation(pt);

        dlg.setResizable(false);

        dlg.setTitle(title);

        dlg.setVisible(true);

    }

    public static void fill_list_box_ct(JList lista, String query, boolean remove, boolean add_net) {

        DefaultListModel dlm = new DefaultListModel();

        try {

            Connection conn = Db_class.mysql_conn();

            if (remove) {

                dlm.removeAllElements();

            }

            ResultSet rs = Db_class.mysql_result(conn, query);

            if (add_net) {

                dlm.addElement("NET");

            }

            while (rs.next()) {

                dlm.addElement(rs.getObject(1));

            }

            lista.setModel(dlm);

            Db_class.close_conn(conn);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(global.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String get_obs_hist(Menu mn) {

        String txt = mn.get_nome() + " em " + global.get_br_date_time(new Date()) + ": ";

        return txt;

    }

    public static Integer Valido_CidadeXParceira(String s_cidade, String s_parceira) {
        Integer iRetorno = 0;
        Connection conn_ativ;
        try {
            conn_ativ = Db_class.mysql_conn();
            CallableStatement cSP = conn_ativ.prepareCall("{call ouvidoria.sp_valido_cidadexparceira(?, ?, ?)}");
            cSP.setString(1, s_cidade);
            cSP.setString(2, s_parceira);
            cSP.registerOutParameter(3, java.sql.Types.INTEGER);
            cSP.execute();
            iRetorno = cSP.getInt(3);
            conn_ativ.close();
            conn_ativ = null;
        } catch (Exception ex) {
            iRetorno = 0;
            global.show_error_message(
                    "Problemas ao validar Cidade X Parceira.\n\n"
                    + "Erro: " + ex.getMessage()
            );
            try {
                conn_ativ = null;
            } catch (Exception ex0) {
            }
        }
        return iRetorno;
    }

}
