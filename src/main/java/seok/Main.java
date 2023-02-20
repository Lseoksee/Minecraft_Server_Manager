package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.TextArea;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Checkbox;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Main extends WindowAdapter implements ActionListener, KeyListener, MouseListener {
    static Thread readThread;
    static OutputStream outputStream;
    static FileClass setfile; // 서버 설정파일
    static String filename; // jar 파일이름
    static String longver; // .포함 버전
    static int realver; // 실제 숫자 버전

    static JFrame fr = new JFrame();
    static JLabel state = new JLabel();

    static Choice gamemode = new Choice();
    static JLabel gamela = new JLabel("게임모드:", JLabel.RIGHT);

    static Choice difficulty = new Choice();
    static JLabel difficultyla = new JLabel("난이도:", JLabel.RIGHT);

    static JTextField person = new JTextField();
    static JLabel personla = new JLabel("참여인원:", JLabel.RIGHT);

    static Checkbox hard = new Checkbox();
    static JLabel hardla = new JLabel("하드코어:", JLabel.RIGHT);

    static Checkbox real = new Checkbox();
    static JLabel realla = new JLabel("비정품 허용:", JLabel.RIGHT);

    static Checkbox command = new Checkbox();
    static JLabel commandla = new JLabel("커맨드 블록 허용:", JLabel.RIGHT);

    static JTextField sername = new JTextField();
    static JLabel sernamela = new JLabel("서버이름:", JLabel.RIGHT);

    static JTextField ram = new JTextField();
    static JLabel ramla = new JLabel("램(GB):", JLabel.RIGHT);

    static Button world = new Button("월드삭제");
    static Button savebt = new Button("저장하기");
    static Button manyset = new Button("추가설정");

    static TextArea consol = new TextArea();
    static Button startbt = new Button("시작");
    static Button stopbt = new Button("정지");

    static JTextField meesge = new JTextField();

    static JTextField jarkey = new JTextField();
    static Button jarok = new Button("확인");

    static SystemTray Tray;
    static TrayIcon trayico;
    static PopupMenu menu;
    static MenuItem open;
    static MenuItem exit;

    static boolean trayover;
    ClassLoader cl = getClass().getClassLoader();

    public static void main(String[] args) {
        //os 스타일 ui로 변경
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}
        
        fr.setSize(500, 750); // (프레임크기-객체크기)*
        fr.addWindowListener(new Main());
        fr.setResizable(false);
        fr.setLocationRelativeTo(null);
        fr.setLayout(null);
        fr.getContentPane().setBackground(Color.white);
        fr.setFocusable(true);
        fr.setIconImage(fr.getToolkit().getImage(new Main().cl.getResource("seok/img/mincraft.png")));

        if (!new findjar().searchjar()) {
            JLabel versub = new JLabel("버전을 입력해주세요.", JLabel.CENTER);
            fr.setTitle("마인크래프트 서버 관리자");

            jarkey.setBounds((fr.getWidth() - 216) / 2, (fr.getHeight() - 31) / 2, 200, 25);
            jarkey.addKeyListener(new Main());

            versub.setBounds(0, jarkey.getY() - 30, fr.getWidth() - 16, 20);
            versub.setFont(new Font("맑은 고딕", Font.BOLD, 17));

            jarok.setBounds((fr.getWidth() - 96) / 2, jarkey.getY() + 30, 80, 25);
            jarok.addActionListener(new Main());

            fr.add(versub);
            fr.add(jarkey);
            fr.add(jarok);
            fr.setVisible(true);
        } else {
            maingui();
        }
    }

    public static void maingui() {
        File file = new File("eula.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write("eula=true");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fr.setTitle("마인크래프트 " + longver + " 서버 관리자");

        // 상단 상태라벨
        state.setText("마인크래프트 서버 관리자");
        state.setHorizontalAlignment(JLabel.CENTER);
        state.setFont(new Font("맑은 고딕", Font.BOLD, 17));

        // 게임모드
        gamemode.add("서바이벌");
        gamemode.add("크리에이티브");
        gamemode.add("모험모드");
        gamemode.add("관전모드");
        gamela.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 난이도
        difficulty.add("평화로움");
        difficulty.add("쉬움");
        difficulty.add("보통");
        difficulty.add("어려움");
        difficultyla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 참여인원
        personla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 하드코어
        hardla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 정품여부
        realla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 커멘드 블록
        commandla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 서버이름
        sernamela.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 램
        ram.setText(jarstart.finalram);
        ramla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 월드삭제
        world.addActionListener(new Main());

        // 저장
        savebt.addActionListener(new Main());

        // 추가설정
        manyset.addActionListener(new Main());

        // 콘솔
        consol.setEditable(false);

        // 시작
        startbt.addActionListener(new Main());

        // 정지
        stopbt.addActionListener(new Main());

        // 메시지 입력창
        meesge.addKeyListener(new Main());
        meesge.setEditable(false);

        //값불러오기
        setfile = new FileClass("server.properties");
        new setbounds();

        fr.add(startbt);
        fr.add(stopbt);
        fr.add(state);
        fr.add(consol);
        fr.add(gamemode);
        fr.add(gamela);
        fr.add(difficulty);
        fr.add(difficultyla);
        fr.add(person);
        fr.add(personla);
        fr.add(real);
        fr.add(realla);
        fr.add(hard);
        fr.add(hardla);
        fr.add(command);
        fr.add(commandla);
        fr.add(sername);
        fr.add(sernamela);
        fr.add(ram);
        fr.add(ramla);
        fr.add(meesge);
        fr.add(savebt);
        fr.add(world);
        fr.add(manyset);
        fr.setVisible(true);
    }

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
                trayico = new TrayIcon(ImageIO.read(new Main().cl.getResourceAsStream("seok/img/minecraft_tray.png")));
                menu = new PopupMenu("Tray Menu");
                open = new MenuItem("열기");
                open.addActionListener(new Main());
                exit = new MenuItem("종료");
                exit.addActionListener(new Main());

                trayico.setToolTip("마인크래프트 버킷 구동기");
                trayico.addMouseListener(new Main());

                menu.add(open);
                menu.add(exit);
                trayico.setPopupMenu(menu);
                Tray.add(trayico);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startbt && readThread == null) {
            consol.setText(null);
            if (!setfile.mode.equals(gamemode.getSelectedItem()) ||
                    !setfile.diff.equals(difficulty.getSelectedItem()) ||
                    !setfile.plear.equals(person.getText()) ||
                    !setfile.hardcore == hard.getState() ||
                    !setfile.reel == real.getState() ||
                    !setfile.comman == command.getState() ||
                    !setfile.name.equals(sername.getText())) {
                setfile.save();
            }
            state.setForeground(null);
            state.setText("서버시작중...");
            readThread = new Thread(new jarstart());
            readThread.start();
        } else if (e.getSource() == stopbt && readThread != null) {
            try {
                String message = "stop\n";
                outputStream.write(message.getBytes());
                outputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == savebt && readThread == null) {
            setfile.save();
        } else if (e.getSource() == jarok) {
            findjar.seljar();
        } else if (e.getSource() == open) {
            trayover = true;
            fr.setVisible(true);
        } else if (e.getSource() == exit) {
            try {
                String message = "stop\n";
                outputStream.write(message.getBytes());
                outputStream.flush();
                System.exit(0);
            } catch (Exception e1) {
                System.exit(0);
            }
        } else if (e.getSource() == manyset && readThread == null) {
            try {
                if (new File(setfile.filepath).exists()) {
                    ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "notepad.exe \"server.properties\"");
                    Process process = processBuilder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while (reader.readLine() != null) {
                    }
                    new FileClass(setfile.filepath);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == world && readThread == null) {
            try {
                if (new File("./world").isDirectory()) {
                    int result = JOptionPane.showConfirmDialog(null, "월드를 삭제할까요?", "알림", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        new ProcessBuilder("cmd", "/c", "rmdir", "/s", "/q", "world", "world_nether", "world_the_end").start();
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && readThread != null) {
            String message = meesge.getText() + "\n";
            consol.append("-->" + message);
            try {
                outputStream.write(message.getBytes());
                outputStream.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            meesge.setText(null);
        } else if (e.getSource() == jarkey && e.getKeyCode() == KeyEvent.VK_ENTER) {
            findjar.seljar();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 좌클릭
        if (e.getSource() == trayico && e.getButton() == MouseEvent.BUTTON1) {
            trayover = true;
            Tray = null;
            fr.setVisible(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}