/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.*;

public class SendMail {

    public static void send(String html_code, String assunto, String mail_group, String cc, Menu mn) {

        ActiveXComponent axcOutlook = new ActiveXComponent("Outlook.Application");
        Dispatch criacaoEmail = Dispatch.invoke(axcOutlook.getObject(), "CreateItem", Dispatch.Get,
                new Object[]{"0"}, new int[0]).toDispatch();

        //Object anexo1 = new Object();  
        //anexo1 = "D:\\Documents and Settings\\N0026925\\Desktop\\ocorr_tc5.xlsb";  
        Dispatch.put(criacaoEmail, "To", mail_group);
        Dispatch.put(criacaoEmail, "Subject", assunto);

        Dispatch.put(criacaoEmail, "CC", cc);

        Dispatch.put(criacaoEmail, "HTMLBody", html_code);
        Dispatch.put(criacaoEmail, "ReadReceiptRequested", "false");

        Dispatch attachs = Dispatch.get(criacaoEmail, "Attachments").toDispatch();

        String final_html = "";

        final_html = "<html>" + html_code + "<br><br>";

        com.jacob.com.Dispatch.put(criacaoEmail, "HTMLBody", final_html);

        Dispatch.call(criacaoEmail, "Display");

    }

}
