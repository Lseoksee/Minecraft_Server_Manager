package seok.UI;

import seok.Main;

public class Networkinfobounds extends NetworkinfoUI {
    public Networkinfobounds() {
        int width = Main.fr.getWidth() - 18; // 프레임 사이즈 -16 - 탭바사이즈(5)

        netinfo.setBounds(0, 0, width, 30);

        //RCON 설정 페널
        rconpan.setBounds(10, 50, width-20,250);
    }
}