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

    public static void main(String[] args) {
        OsnFrame osnFrame = new OsnFrame();
        osnFrame.setVisible(true);
        osnFrame.getBtnSformirovMSG().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MakerMessage makerMessage = new MakerMessage(osnFrame.getPoleNomZeli().getText(),
                        osnFrame.getPoleKoord().getText(),
                        osnFrame.getKtoStrelial().getText(),
                        osnFrame.getPoleKalibr().getText(),
                        osnFrame.getPoleTipBK().getText(),
                        osnFrame.getPoleRashod().getText(),
                        osnFrame.getPoleTipZeli().getText());
                osnFrame.setAreaVuvodMSG(makerMessage.getMessage());
            }
        });
    }

}
