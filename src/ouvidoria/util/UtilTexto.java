/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria.util;

import javax.swing.JTable;

/**
 *
 * @author ROBSMAC
 */
public class UtilTexto {

    public boolean LocalizaNome_EmTabela(JTable tTab, int iColuna, String sNome, boolean bIgnorarCaixa, boolean bIgnorarAcento) {
        boolean bAchou = false;
        String sNome_Tab = "";

        sNome = sNome.trim();

        if (!bIgnorarCaixa) {
            sNome = sNome.toUpperCase();
        }

        if (!bIgnorarAcento) {
            sNome = RemoveAcento(sNome);
        }

        if (tTab.getRowCount() > 0) {
            for (int i = 0; i <= tTab.getRowCount() - 1; i++) {
                sNome_Tab = tTab.getModel().getValueAt(i, iColuna).toString();
                if (!bIgnorarCaixa) {
                    sNome_Tab = sNome_Tab.toUpperCase();
                }
                if (!bIgnorarAcento) {
                    sNome_Tab = RemoveAcento(sNome_Tab);
                }
                if (sNome.equals(sNome_Tab)) {
                    bAchou = true;
                    break;
                }
            }
        }

        return bAchou;
    }

    public String RemoveAcento(String sTexto) {
        String sRetorno = sTexto;
        String[][] sLista = {
            {"á", "a"}, {"é", "e"}, {"í", "i"}, {"ó", "o"}, {"ú", "u"},
            {"Á", "A"}, {"É", "E"}, {"í", "I"}, {"Ó", "O"}, {"Ú", "U"},
            {"à", "a"}, {"è", "e"}, {"Ì", "i"}, {"ò", "o"}, {"ù", "u"},
            {"À", "A"}, {"È", "E"}, {"Ì", "I"}, {"Ò", "O"}, {"Ù", "U"},
            {"ã", "a"}, {"õ", "o"},
            {"Â", "A"}, {"Õ", "õ"},
            {"â", "a"}, {"ê", "e"}, {"î", "i"}, {"ô", "o"}, {"û", "u"},
            {"Â", "A"}, {"Ê", "E"}, {"Î", "I"}, {"Ô", "O"}, {"Û", "U"},
            {"ä", "a"}, {"ë", "e"}, {"ï", "i"}, {"ö", "o"}, {"ü", "u"},
            {"Ä", "A"}, {"Ë", "E"}, {"Ï", "I"}, {"Ö", "O"}, {"Ü", "U"},
            {"ç", "c"},
            {"Ç", "C"},
            {"ñ", "n"},
            {"Ñ", "N"}
        };
        
        for (int i = 0; i < sLista.length; i++) {
            sRetorno = sTexto.replaceAll(sLista[i][0], sLista[i][1]);
        }
        
        return sRetorno;
    }

}
