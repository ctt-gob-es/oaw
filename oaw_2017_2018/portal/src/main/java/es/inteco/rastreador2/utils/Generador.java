package es.inteco.rastreador2.utils;

import java.util.HashSet;
import java.util.Set;

public class Generador {

    String[] ip1;
    String[] ip2;
    int[] ipo = new int[4];
    int[] ipd = new int[4];


    public Generador(String ipa, String ipb) {
        this.ip1 = ipa.split("\\.");
        this.ip2 = ipb.split("\\.");

        for (int i = 0; i < 4; i++) {
            ipo[i] = Integer.parseInt(ip1[i]);
            ipd[i] = Integer.parseInt(ip2[i]);
        }
    }

    public Set<String> genera() {
        final Set<String> result = new HashSet<>();

        result.add(String.format("%d.%d.%d.%d", ipo[0], ipo[1], ipo[2], ipo[3]));

        while (!((ipo[0] == ipd[0]) && (ipo[1] == ipd[1]) && (ipo[2] == ipd[2]) && (ipo[3] == ipd[3]))) {
            if (ipo[3] == 255) {
                ipo[3] = 0;
                if (ipo[2] == 255) {
                    ipo[2] = 0;
                    if (ipo[1] == 255) {
                        ipo[1] = 0;
                        if (ipo[0] == 255) {
                            return null;
                        } else {
                            ipo[0]++;
                            result.add(String.format("%d.%d.%d.%d", ipo[0], ipo[1], ipo[2], ipo[3]));
                        }
                    } else {
                        ipo[1]++;
                        result.add(String.format("%d.%d.%d.%d", ipo[0], ipo[1], ipo[2], ipo[3]));
                    }
                } else {
                    ipo[2]++;
                    result.add(String.format("%d.%d.%d.%d", ipo[0], ipo[1], ipo[2], ipo[3]));
                }
            } else {
                ipo[3]++;
                result.add(String.format("%d.%d.%d.%d", ipo[0], ipo[1], ipo[2], ipo[3]));
            }
        }
        return result;
    }

}
