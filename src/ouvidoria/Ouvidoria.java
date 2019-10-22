/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author N0026925
 */
public class Ouvidoria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        Menu mn = new Menu();

        mn.setExtendedState(JFrame.MAXIMIZED_BOTH);

        mn.setTitle("Tratamento Ouvidoria");

        CopyOption[] options = new CopyOption[]{
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES
        };

        String bits = System.getProperty("os.arch");
        System.setProperty("java.library.path", "C:\\TABULADOR_NET\\");
        System.out.println(System.getProperty("user.home"));

        final java.lang.reflect.Field sysPathsField;
        try {
            sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Ouvidoria.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (bits.equals("x86")) {

            //ALTERAR_BCC_OK
            /*Path source = Paths.get("\\\\namrsv0010\\amr-tecnica\\COP\\Relatórios Logistica - Mario\\Teste Esteira\\jacob-1.18-M2-x86.dll");
            Path dest = Paths.get(System.getProperty("user.home") + "\\jacob-1.18-M2-x86.dll");
            try {
                java.nio.file.Files.copy(source, dest, options);
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            //System.load("\\\\namrsv0010\\amr-tecnica\\COP\\Relatórios Logistica - Mario\\Teste Esteira\\jacob-1.18-M2-x86.dll");
            System.load("C:\\TABULADOR_NET\\jacob-1.18-M2-x86.dll");

        } else {
                
            //ALTERAR_BCC_OK
            /*Path source = Paths.get("\\\\namrsv0010\\amr-tecnica\\COP\\Relatórios Logistica - Mario\\Teste Esteira\\jacob-1.18-M2-x64.dll");
            Path dest = Paths.get(System.getProperty("user.home") + "\\jacob-1.18-M2-x64.dll");
            try {
                java.nio.file.Files.copy(source, dest, options);
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            //System.load("\\\\namrsv0010\\amr-tecnica\\COP\\Relatórios Logistica - Mario\\Teste Esteira\\jacob-1.18-M2-x64.dll");
            System.load("C:\\TABULADOR_NET\\jacob-1.18-M2-x64.dll");

        }

        mn.setVisible(true);

        if (!mn.check_version()) {

            return;

        }

        lastest_changes lc = new lastest_changes(mn, true);

    }

}
