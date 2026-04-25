/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dokladoz;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * @author Кузьмич
 */
public class MakerMessage {

    static String time;
    String nomerZeli;
    String ktoStrelial;
    String kalibr;
    String snariad;
    String rashod;
    String tipZeli;
    String sistema;
    String podrazdelenie;
    String nomerPoPoriadky;

    public MakerMessage(String nomerZeli, String nomerPoPoriadky, String ktoStrelial, String kalibr,
                        String snariad, String rashod, String tipZeli, String sistema, String podrazdelenie) {
        this.nomerZeli = nomerZeli;
        this.nomerPoPoriadky = nomerPoPoriadky;
        this.ktoStrelial = ktoStrelial;
        this.kalibr = kalibr;
        this.snariad = snariad;
        this.rashod = rashod;
        this.tipZeli = tipZeli;
        this.sistema = sistema;
        this.podrazdelenie = podrazdelenie;
    }

    public String getKoordZeli() {
        String koord = " ";
        if (!this.nomerZeli.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\YO_NA\\Zeli"), StandardCharsets.UTF_8))) {
                String line;
                String[] mas;
                int y;
                while ((line = reader.readLine()) != null) {
                    mas = line.split(",");
                    if (mas[0].equals(this.nomerZeli)) {
                        y = Integer.parseInt(mas[2]);
                        if (y < 10000) {
                            mas[2] = 0 + mas[2];
                        }
                        koord = mas[1] + " " + mas[2];
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ex.getMessage());
                return koord;
            }
        }
        return koord;
    }

    public String getMessageVskruto() {
        String msg;
        msg = getDataTime() + " в районе н.п.Константиновка "
                + getKvadrat() + "Ц" + this.nomerZeli + " " + getKoordZeli()
                + ". Разведывательным огневым комплексом вскрыто "
                + this.tipZeli + " противника, нанесено огневое поражение артиллерией 1194 мсп расчётом "
                + this.sistema + " '" + this.ktoStrelial + "' (" + this.podrazdelenie
                + "). В результате огневого поражения  противник подавлен."
                + " Расход " + this.kalibr + " " + this.snariad + " - " + this.rashod
                + ". Средства контроля ОПП - БПЛА ККТ.";
        ExcelWriter.go(nomerPoPoriadky, time, nomerZeli, tipZeli, getKvadrat(), ktoStrelial
                + " " + podrazdelenie, rashod, "подавлена", "подано");
        return msg;
    }

    public String getMessageIzoliacia() {
        String msg;
        msg = getDataTime() + " в районе н.п.Константиновка "
                + getKvadrat() + "Ц" + this.nomerZeli + " " + getKoordZeli()
                + ". В рамках изоляции района, нанесено огневое поражение артиллерией 1194 мсп расчётом "
                + this.sistema + " '" + this.ktoStrelial + "' (" + this.podrazdelenie
                + "). В результате огневого поражения  противник подавлен."
                + " Расход " + this.kalibr + " " + this.snariad + " - " + this.rashod + ".";
        ExcelWriter.go(nomerPoPoriadky, time, nomerZeli, tipZeli, getKvadrat(), ktoStrelial
                + " " + podrazdelenie, rashod, "подавлена", "подано");
        return msg;
    }

    public String getKvadrat() {
        String kvadrat;
        String[] str = getKoordZeli().split(" ");
        int x = Integer.parseInt(str[0]);
        int y = Integer.parseInt(str[1]);
        int xBolsh = x / 1000;
        int yBolsh = y / 1000;
        String strY;
        if (yBolsh < 10) {
            strY = 0 + "" + yBolsh;
        } else {
            strY = String.valueOf(yBolsh);
        }

        int ostatokX = x % 1000;
        int ostatokY = y % 1000;
        int ylitka = 0;
        if (ostatokX > 666 && ostatokY < 333) {
            ylitka = 1;
        } else if (ostatokX > 666 && ostatokY > 333 && ostatokY < 666) {
            ylitka = 2;
        } else if (ostatokX > 666 && ostatokY > 666) {
            ylitka = 3;
        } else if (ostatokX < 666 && ostatokX > 333 && ostatokY > 666) {
            ylitka = 4;
        } else if (ostatokX < 333 && ostatokY > 666) {
            ylitka = 5;
        } else if (ostatokX < 333 && ostatokY > 333 && ostatokY < 666) {
            ylitka = 6;
        } else if (ostatokX < 333 && ostatokY < 333) {
            ylitka = 7;
        } else if (ostatokX > 333 && ostatokX < 666 && ostatokY < 333) {
            ylitka = 8;
        } else if (ostatokX > 333 && ostatokX < 666 && ostatokY > 333 && ostatokY < 666) {
            ylitka = 9;
        }
        kvadrat = "(" + xBolsh + strY + "-" + ylitka + ")";
        return kvadrat;
    }

    public String getDataTime() {
        String dataTime;
        Calendar date = Calendar.getInstance();

        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minyt = date.get(Calendar.MINUTE);

        if (minyt < 10) {
            time = hour + ":" + "0" + minyt;
        } else {
            time = hour + ":" + minyt;
        }

        String strMinyt;
        if (minyt < 10) {
            strMinyt = 0 + "" + minyt;
        } else {
            strMinyt = String.valueOf(minyt);
        }

        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        String strMonth;
        if (month < 10) {
            strMonth = 0 + "" + month;
        } else {
            strMonth = String.valueOf(month);
        }

        int year = date.get(Calendar.YEAR);
        dataTime = "B " + hour + ":" + strMinyt + " " + day + "." + strMonth + "." + year;
        return dataTime;
    }
}
