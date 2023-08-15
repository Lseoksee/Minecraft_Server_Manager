package seok.UI;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import seok.Main;

public class NetworkinfoUI extends Main {

    private JPanel networkinfopan; // 네트워크 정보 패널

    public static JLabel netinfo;

    public static JLabel inip;
    public static JTextField iniptext;

    public static JLabel outip;
    public static JTextField outiptext;

    public static JPanel rconpan;

    public JPanel networkinfo() {
        netinfo = new JLabel();
        netinfo.setText("네트워크 정보");
        netinfo.setHorizontalAlignment(JLabel.CENTER);
        netinfo.setFont(new Font("맑은 고딕", Font.BOLD, 17));

        // ROCN 설정 쪽
        TitledBorder rconbBorder = BorderFactory.createTitledBorder("RCON 설정"); // 테두리 설정
        rconbBorder.setTitleFont(new Font("맑은 고딕", Font.PLAIN, 14));
        rconpan = new JPanel();
        rconpan.setBorder(rconbBorder);
        rconpan.setBackground(null);

        new Networkinfobounds();

        networkinfopan = new JPanel(null);
        networkinfopan.setBackground(Color.WHITE);

        networkinfopan.add(netinfo);
        networkinfopan.add(rconpan);

        return networkinfopan;
    }

    private String getoutip() {
        try {
            URL url = new URL("http://checkip.amazonaws.com/"); // 외부 ip 리턴
            HttpURLConnection hur = (HttpURLConnection) url.openConnection();
            return new BufferedReader(new InputStreamReader(hur.getInputStream(), "utf-8")).readLine();
        } catch (Exception e) {
            return null;
        }
    }
}
