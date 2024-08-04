package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

import org.json.JSONObject;

import seok.UI.EventSuper;
import seok.UI.MainUI;
import seok.UI.NetworkinfoUI;
import seok.UI.PlayerOptonUI;

public class Main extends EventSuper {
    public static final String RESVER = "v1.6.1"; // 앱 버전
    public static final Font APPFONT = new Font("맑은 고딕", Font.PLAIN, 15); // 앱 폰트

    public static Thread readThread; // 로그 쓰레드
    public static OutputStream outputStream;
    public static Findjar jarver; // 서버 실행파일 찾기
    public static String propertiesfile = "server.properties"; // 서버 설정파일

    public static JFrame fr; // 메인 프레임
    public static JTabbedPane pane; // 텝 패널

    public static SystemTray Tray;
    public static TrayIcon trayico;
    public static PopupMenu menu;
    public static MenuItem open;
    public static MenuItem exit;

    public static boolean trayover;
    public static boolean oplistclick;

    private JTextField jarkey;
    private JButton jarok;

    private ClassLoader cl = getClass().getClassLoader();

    public static void main(String[] args) {
        // os 스타일 ui로 변경
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        Main main = new Main();

        fr = new JFrame();
        fr.setSize(500, 750); // (프레임크기-객체크기)*
        fr.setResizable(false);
        fr.setLocationRelativeTo(null);
        fr.setFocusable(true);
        fr.setIconImage(fr.getToolkit().getImage(main.cl.getResource("mincraft.png")));
        // 종료 이벤트
        fr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                try {
                    we.getWindow().setVisible(false);
                    we.getWindow().dispose();
                    if (readThread == null) {
                        System.exit(0);
                    } else if (!trayover) {
                        // 시스템 트레이
                        Tray = SystemTray.getSystemTray();
                        trayico = new TrayIcon(ImageIO.read(main.cl.getResourceAsStream("minecraft_tray.png")));
                        menu = new PopupMenu("Tray Menu");
                        open = new MenuItem("열기");
                        open.addActionListener(main);
                        exit = new MenuItem("종료");
                        exit.addActionListener(main);

                        trayico.setToolTip("마인크래프트 버킷 구동기");
                        trayico.addMouseListener(main);
                        menu.add(open);
                        menu.add(exit);
                        trayico.setPopupMenu(menu);
                        Tray.add(trayico);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        jarver = new Findjar();

        if (jarver.version == null)
            main.whatver();
        else
            main.start();

        new Thread(() -> main.CheckVer()).start(); // 버전확인 쓰레드 실행
    }

    public void start() {
        File file = new File("eula.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write("eula=true");
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pane = new JTabbedPane();

        MainUI mainui = new MainUI();
        PlayerOptonUI playeroptonui = new PlayerOptonUI();
        NetworkinfoUI networkinfoui = new NetworkinfoUI();

        pane.addTab("메인", mainui.main());
        pane.addTab("플레이어 관리", playeroptonui.playeropton());
        pane.addTab("네트워크 정보", networkinfoui.networkinfo());
        pane.setEnabledAt(1, false);
        pane.addChangeListener(this);
        pane.addMouseListener(this);

        fr.add(pane);
        fr.setVisible(true);
    }

    public void whatver() {
        JLabel versub = new JLabel("버전을 입력해주세요.", JLabel.CENTER);
        JPanel jp = new JPanel();
        jp.setLayout(null);
        jp.setBackground(Color.WHITE);

        fr.setTitle("마인크래프트 서버 관리자");

        jarkey = new JTextField();
        jarkey.setBounds((fr.getWidth() - 216) / 2, (fr.getHeight() - 31) / 2, 200, 25);
        jarkey.addKeyListener(this);

        versub.setBounds(0, jarkey.getY() - 30, fr.getWidth() - 16, 20);
        versub.setFont(APPFONT.deriveFont(Font.BOLD, 17));

        jarok = new JButton("확인");
        jarok.setBounds((fr.getWidth() - 96) / 2, jarkey.getY() + 30, 80, 25);
        jarok.addActionListener(this);

        jp.add(versub);
        jp.add(jarkey);
        jp.add(jarok);
        fr.add(jp);
        fr.setVisible(true);
    }

    // 서버 버전 인식불가 처리
    public void seljar() {
        if (!jarkey.getText().matches("\\d+\\.\\d+(\\.\\d+)*")) {
            jarkey.setText(null);
            return; // 올바른 형태가아니면
        }
        try {
            jarver.version = jarkey.getText();
            new File(jarver.filename).renameTo(new File("Minecraft_" + jarver.version + "_server.jar"));
            jarver.filename = "Minecraft_" + jarver.version + "_server";
            fr.getContentPane().removeAll();
            this.start();
        } catch (Exception e) {
            jarkey.setText(null);
            return;
        }
    }

    public void CheckVer() {
        /* 깃허브 api를 사용한 버전확인 방법 */
        try {
            URL url = new URL("https://api.github.com/repos/Lseoksee/Minecraft_Server_Manager/releases/latest");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            // httprequset 연결부분

            InputStream is = huc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

            JSONObject jsonObject = new JSONObject(br.readLine());
            String onver = jsonObject.getString("tag_name");
            br.close();

            if (!onver.equals(Main.RESVER)) {
                String mesege = "업데이트가 있습니다!\n현재버전: " + Main.RESVER + "\n최신버전: " + onver;
                int state = JOptionPane.showConfirmDialog(fr, mesege, "업데이트 알림", JOptionPane.OK_CANCEL_OPTION);
                if (state == 0) {
                    Desktop.getDesktop().browse(new URI(jsonObject.getString("html_url")));
                }
            }
        } catch (IOException e) {
            System.out.println("API 토큰 소진");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 트레이 열기버튼
        if (e.getSource() == open) {
            trayover = true;
            fr.setVisible(true);
        }
        // 트레이 닫기버튼
        if (e.getSource() == exit) {
            try {
                String message = "stop\n";
                outputStream.write(message.getBytes("UTF-8"));
                outputStream.flush();
                System.exit(0);
            } catch (Exception e1) {
                System.exit(0);
            }
        }
        // 첫시작 버튼
        if (e.getSource() == jarok) {
            seljar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 트레이 우클릭 옵션
        if (e.getSource() == trayico && e.getButton() == MouseEvent.BUTTON1) {
            trayover = true;
            fr.setVisible(true);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // 탭 변화
        if (pane.getSelectedIndex() == 1) {
            if (!oplistclick) {
                Runnable r = new PlayerOptonUI(MainUI.real.getState());
                new Thread(r).start();
                new Thread(r).start();
                new Thread(r).start();
                new Thread(r).start();
                new Thread(r).start();
                new Thread(r).start();
                oplistclick = true;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 처음시작 버전입력
        if (e.getSource() == jarkey && e.getKeyCode() == KeyEvent.VK_ENTER) {
            seljar();
        }
    }
}