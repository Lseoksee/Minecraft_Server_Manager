package seok.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import seok.Main;

public class NetworkinfoUI extends Main implements Runnable {

    private JPanel networkinfopan; // 네트워크 정보 패널

    public static JLabel netinfo;

    public static JPanel inipppanel;
    public static JLabel iniplabel;
    public static JTextField inip;
    public static JButton inipcopybutton;

    public static JPanel outippanel;
    public static JLabel outiplabel;
    public static JTextField outip;
    public static JButton outcopybt;

    public static JSpinner setport;
    public static JLabel setportlabel;

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
        iniplabel = new JLabel("내부 IP 주소:", JLabel.RIGHT);
        iniplabel.setFont(APPFONT);

        // 내부 ip 주소 필드
        inip = new JTextField(getinip());
        inip.setMaximumSize(new Dimension(120, 24));
        inip.setEditable(false);

        // 내부 ip 주소 복사 버튼
        inipcopybutton = new JButton("복사");
        inipcopybutton.addActionListener(this);

        // 내부 ip 주소 패널
        inipppanel = new JPanel();
        inipppanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        inipppanel.setBackground(null);

        inipppanel.add(iniplabel);
        inipppanel.add(inip);
        inipppanel.add(inipcopybutton);

        // 외부 ip 주소 라벨
        outiplabel = new JLabel("외부 IP 주소:", JLabel.RIGHT);
        outiplabel.setFont(APPFONT);

        // 외부 ip 주소 필드
        outip = new JTextField(getoutip());
        outip.setEditable(false);

        // 외부 ip 주소 복사 버튼
        outcopybt = new JButton("복사");
        outcopybt.addActionListener(this);

        // 외부 ip 주소 패널
        outippanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        outippanel.setBackground(null);

        outippanel.add(outiplabel);
        outippanel.add(outip);
        outippanel.add(outcopybt);

        // 포트포워딩 포트 설정
        setport = new JSpinner();
        setport.setValue(25565);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        JSpinner.DefaultEditor setportedit = (JSpinner.DefaultEditor) setport.getEditor();
        JFormattedTextField textField = setportedit.getTextField();
        textField.setHorizontalAlignment(JTextField.LEFT);
        textField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(numberFormat)));

        setportlabel = new JLabel("테스트 포트:");
        setportlabel.setFont(APPFONT.deriveFont(Font.PLAIN, 13));

        // 포트포워딩 테스트 버튼
        porttestbt = new JButton("포트포워딩 테스트");
        porttestbt.setMargin(new Insets(0, 0, 0, 0)); // margen 조정
        porttestbt.addActionListener(this);

        // 포트포워딩 테스트 정보 라벨
        porttestlabel = new JLabel("", JLabel.CENTER);
        porttestlabel.setFont(APPFONT.deriveFont(Font.BOLD));

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
        networkinfopan.add(inipppanel);
        networkinfopan.add(outippanel);
        networkinfopan.add(setport);
        networkinfopan.add(setportlabel);
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
                        int port = Integer.parseInt(setport.getValue().toString());
                        ServerSocket svs = new ServerSocket(port); // 테스트로 오픈할 포트
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
                if (!MainUI.state.getText().equals("서버가 정상적으로 시작되었습니다")) {
                    porttestlabel.setText("서버가 열린 후 다시 시도해 주세요");
                    return;
                }
                new Thread(this).start();
            }
        }

        if (e.getSource() == inipcopybutton) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			StringSelection strSel = new StringSelection(inip.getText());
			clipboard.setContents(strSel, null);
        }

        if (e.getSource() == outcopybt) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			StringSelection strSel = new StringSelection(outip.getText());
			clipboard.setContents(strSel, null);
        }
    }

    @Override
    public void run() {
        // 포트포워딩 테스트
        String ip = outip.getText();
        if (ip.equals("")) {
            porttestlabel.setForeground(Color.RED);
            porttestlabel.setText("외부 ip 주소를 확인 할 수 없음");
            return;
        }
        try (Socket socket = new Socket()) {
            int port = Integer.parseInt(setport.getValue().toString());
            socket.connect(new InetSocketAddress(ip, port), 2000);
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
