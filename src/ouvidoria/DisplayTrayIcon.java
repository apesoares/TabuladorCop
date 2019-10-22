/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author N0026925
 */
public class DisplayTrayIcon {

    Image iconImage2;
    static TrayIcon trayIcon;
    static Menu mn;

    public DisplayTrayIcon(Menu mn) {

        this.mn = mn;

        ShowTrayIcon();

    }

    public void ShowTrayIcon() {

        if (!SystemTray.isSupported()) {

            System.exit(0);
            return;

        }

        URL novaurl = System.class.getResource("/icons/cancel.png");
        Image iconImage2 = Toolkit.getDefaultToolkit().getImage(novaurl);

        final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(CreateIcon("/16x16/phone_vintage.png", "Tray Icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        trayIcon.setToolTip("Tratamento Ouvidoria");

        trayIcon.addMouseListener(new tray_double_click());

        java.awt.Menu DisplayMenu = new java.awt.Menu("Menu");

        MenuItem restoreItem = new MenuItem("Restaurar");
        MenuItem AboutItem = new MenuItem("Sobre");
        MenuItem ExitItem = new MenuItem("Sair");
        MenuItem notas = new MenuItem("Notas do patch");

        MenuItem ErrorItem = new MenuItem("Error");

        popup.add(restoreItem);
        popup.add(AboutItem);
        popup.add(notas);
        popup.addSeparator();
        popup.add(ExitItem);

        DisplayMenu.add(ErrorItem);

        trayIcon.setPopupMenu(popup);

        AboutItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "\n"
                        + "Desenvolvido por Guilherme Reis\n"
                        + "Contato: guilherme.reis@net.com.br");
            }
        });

        ExitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });

        restoreItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                mn.setVisible(true);

            }
        });

        notas.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                lastest_changes lc = new lastest_changes(mn, true);
            }
        });

        try {

            tray.add(trayIcon);

        } catch (AWTException e) {

        }
    }

    class tray_double_click extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {

                mn.setVisible(true);

            }

        }

    }

    protected static Image CreateIcon(String path, String desc) {

        URL ImageURL = DisplayTrayIcon.class.getResource(path);
        return (new ImageIcon(ImageURL, desc)).getImage();
    }

}
