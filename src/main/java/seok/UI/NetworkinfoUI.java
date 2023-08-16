package seok.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import seok.Main;

public class NetworkinfoUI extends Main implements Runnable {

    private JPanel networkinfopan; // 네트워크 정보 패널

    public static JLabel netinfo;

    public static JLabel inip;
    public static JTextField iniptext;

    public static JLabel outip;
    public static JTextField outiptext;

    public static JButton porttestbt;
    public static JLabel porttestlabel;

    public static JPanel rconpan;

    public JPanel networkinfo() {
        // 타이틀 설정
        netinfo = new JLabel();
        netinfo.setText("네트워크 정보");
        netinfo.setHorizontalAlignment(JLabel.CENTER);
        netinfo.setFont(APPFONT.deriveFont(Font.BOLD, 17));

        // 내부 ip 주소 라벨
        inip = new JLabel("내부 IP 주소:", JLabel.RIGHT);
        inip.setFont(APPFONT);

        // 내부 ip 주소 필드
        iniptext = new JTextField(getinip());
        iniptext.setEditable(false);

        // 외부 ip 주소 라벨
        outip = new JLabel("외부 IP 주소:", JLabel.RIGHT);
        outip.setFont(APPFONT);

        // 외부 ip 주소 필드
        outiptext = new JTextField(getoutip());
        outiptext.setEditable(false);

        // 포트포워딩 테스트 버튼
        porttestbt = new JButton("포트포워딩 테스트");
        porttestbt.addActionListener(this);

        // 포트포워딩 테스트 정보 라벨
        porttestlabel = new JLabel("테스트", JLabel.CENTER);
        porttestlabel.setFont(APPFONT);

        // ROCN 설정 쪽
        TitledBorder rconbBorder = BorderFactory.createTitledBorder("RCON 설정"); // 테두리 설정
        rconbBorder.setTitleFont(APPFONT.deriveFont(Font.PLAIN, 14));
        rconpan = new JPanel();
        rconpan.setBorder(rconbBorder);
        rconpan.setBackground(null);

        new Networkinfobounds();

        networkinfopan = new JPanel(null);
        networkinfopan.setBackground(Color.WHITE);

        networkinfopan.add(netinfo);
        networkinfopan.add(inip);
        networkinfopan.add(iniptext);
        networkinfopan.add(outip);
        networkinfopan.add(outiptext);
        networkinfopan.add(porttestbt);
        networkinfopan.add(porttestlabel);
        networkinfopan.add(rconpan);

        return networkinfopan;
    }

    // 내부 ip 주소 리턴
    private String getinip() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 외부 ip 주소 리턴
    private String getoutip() {
        try {
            URL url = new URL("http://checkip.amazonaws.com/"); // 외부 ip 리턴
            HttpURLConnection hur = (HttpURLConnection) url.openConnection();
            return new BufferedReader(new InputStreamReader(hur.getInputStream(), "utf-8")).readLine();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 포트포워딩 테스트 버튼
        if (e.getSource() == porttestbt) {
            porttestlabel.setForeground(null);
            porttestlabel.setText("테스트중...");
            if (readThread == null) {
                // 서버 시작전 포트 포워딩 테스트
                new Thread(() -> {
                    try {
                        ServerSocket svs = new ServerSocket(25565); // 테스트로 오픈할 포트
                        svs.setSoTimeout(2000);
                        new Thread(this).start();
                        svs.accept();
                        svs.close();
                    } catch (Exception e1) {
                        porttestlabel.setText("포트포워딩 필요");
                        e1.printStackTrace();
                    }
                }).start();

            } else {
                // 서버 시작 후 포트 테스트
                new Thread(this).start();
            }
        }
    }

    @Override
    public void run() {
        // 포트포워딩 테스트 
        String ip = outiptext.getText();
        if (ip.equals("")) {
            porttestlabel.setForeground(Color.RED);
            porttestlabel.setText("외부 ip 주소를 확인 할 수 없음");
            return;
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, 25565), 2000);
            // 오픈된 포트를 외부IP주소로 연결하여 포트포워딩이 정상적으로 되었는지를 확인
            porttestlabel.setForeground(Color.GREEN);
            porttestlabel.setText("성공적으로 연결됨");
        } catch (Exception e) {
            e.printStackTrace();
            porttestlabel.setForeground(Color.RED);
            porttestlabel.setText("포트포워딩 필요");
        }
    }
}
