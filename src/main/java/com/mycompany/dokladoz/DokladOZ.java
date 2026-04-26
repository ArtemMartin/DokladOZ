/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.dokladoz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Кузьмич
 */
public class DokladOZ {

    static String tipZadachi;

    public static void main(String[] args) {
        OsnFrame osnFrame = new OsnFrame();
        osnFrame.setVisible(true);

        osnFrame.getBtnSformirovMSG().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MakerMessage makerMessage = new MakerMessage(osnFrame.getPoleNomZeli().getText(),
                        osnFrame.getTfNomPoPor().getText(),
                        (String)osnFrame.getKtoStrelial().getSelectedItem(),
                        osnFrame.getPoleKalibr().getText(),
                        osnFrame.getPoleTipBK().getText(),
                        osnFrame.getPoleRashod().getText(),
                        osnFrame.getPoleTipZeli().getText(),
                        (String) osnFrame.getBoxSistema().getSelectedItem(),
                        (String) osnFrame.getBoxPodrazdelenie().getSelectedItem());

                tipZadachi = osnFrame.getjBoxIzol().getSelectedItem().toString();
                if (tipZadachi.equals("Вскрыто")) {
                    osnFrame.setAreaVuvodMSG(makerMessage.getMessageVskruto());
                } else if (tipZadachi.equals("Сковать район")) {
                    osnFrame.setAreaVuvodMSG(makerMessage.getMessageIzoliacia());
                }
            }
        });
    }

}
