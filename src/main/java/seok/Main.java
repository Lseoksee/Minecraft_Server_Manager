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
import java.awt.TextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.Checkbox;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Main implements ActionListener, KeyListener, MouseListener {
    static Thread readThread;
    static OutputStream outputStream;
    static FileClass setfile; // 서버 설정파일
    static String filename; // jar 파일이름
    static String longver; // .포함 버전
    static int realver; // 실제 숫자 버전

    static JFrame fr;
    static JTabbedPane pane;
    static JPanel Pane1;
    static JLabel state;

    static JComboBox<String> gamemode;
    static JLabel gamela;

    static JComboBox<String> difficulty;
    static JLabel difficultyla;

    static JTextField person;
    static JLabel personla;

    static Checkbox hard;
    static JLabel hardla;

    static Checkbox real;
    static JLabel realla;

    static Checkbox command;
    static JLabel commandla;

    static JTextField sername;
    static JLabel sernamela;

    static JTextField ram;
    static JLabel ramla;

    static JButton world;
    static JButton savebt;
    static JButton manyset;

    static TextArea consol;
    static JButton startbt;
    static JButton stopbt;

    static JTextField meesge;

    static JTextField jarkey;
    static JButton jarok;

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

        fr = new JFrame();
        fr.setSize(500, 750); // (프레임크기-객체크기)*
        fr.setResizable(false);
        fr.setLocationRelativeTo(null);
        fr.setIconImage(fr.getToolkit().getImage(new Main().cl.getResource("seok/img/mincraft.png")));
        //종료 이벤트
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
        });

        if (!new findjar().searchjar()) {
            JLabel versub = new JLabel("버전을 입력해주세요.", JLabel.CENTER);
            fr.setTitle("마인크래프트 서버 관리자");

            jarkey = new JTextField();
            jarkey.setBounds((fr.getWidth() - 216) / 2, (fr.getHeight() - 31) / 2, 200, 25);
            jarkey.addKeyListener(new Main());

            versub.setBounds(0, jarkey.getY() - 30, fr.getWidth() - 16, 20);
            versub.setFont(new Font("맑은 고딕", Font.BOLD, 17));

            jarok = new JButton("확인");
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
        state = new JLabel();
        state.setText("마인크래프트 서버 관리자");
        state.setHorizontalAlignment(JLabel.CENTER);
        state.setFont(new Font("맑은 고딕", Font.BOLD, 17));

        // 게임모드
        gamemode = new JComboBox<>();
        gamemode.addItem("서바이벌");
        gamemode.addItem("크리에이티브");
        gamemode.addItem("모험모드");
        gamemode.addItem("관전모드");
        gamela = new JLabel("게임모드:", JLabel.RIGHT);
        gamela.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 난이도
        difficulty = new JComboBox<>();
        difficulty.addItem("평화로움");
        difficulty.addItem("쉬움");
        difficulty.addItem("보통");
        difficulty.addItem("어려움");
        difficultyla = new JLabel("난이도:", JLabel.RIGHT);
        difficultyla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 참여인원
        person = new JTextField();
        personla = new JLabel("참여인원:", JLabel.RIGHT);
        personla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 하드코어
        hard = new Checkbox();
        hardla = new JLabel("하드코어:", JLabel.RIGHT);
        hardla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 정품여부
        real = new Checkbox();
        realla = new JLabel("비정품 허용:", JLabel.RIGHT);
        realla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 커멘드 블록
        command = new Checkbox();
        commandla = new JLabel("커맨드 블록 허용:", JLabel.RIGHT);
        commandla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 서버이름
        sername = new JTextField();
        sernamela = new JLabel("서버이름:", JLabel.RIGHT);
        sernamela.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 램
        ram = new JTextField();
        ram.setText(jarstart.finalram);
        ramla = new JLabel("램(GB):", JLabel.RIGHT);
        ramla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 월드삭제
        world = new JButton("월드삭제");
        world.addActionListener(new Main());

        // 저장
        savebt = new JButton("저장하기");
        savebt.addActionListener(new Main());

        // 추가설정
        manyset = new JButton("추가설정");
        manyset.addActionListener(new Main());

        // 콘솔
        consol = new TextArea();
        consol.setEditable(false);

        // 시작
        startbt = new JButton("시작");
        startbt.addActionListener(new Main());

        // 정지
        stopbt = new JButton("정지");
        stopbt.addActionListener(new Main());

        // 메시지 입력창
        meesge = new JTextField();
        meesge.addKeyListener(new Main());
        meesge.setEditable(false);

        //값불러오기
        setfile = new FileClass("server.properties");
        new setbounds();
        
        //패널 설정
        Pane1 = new JPanel();
        Pane1.setLayout(null);
        Pane1.setBackground(Color.white);
        Pane1.setFocusable(true);

        Pane1.add(startbt);
        Pane1.add(stopbt);
        Pane1.add(state);
        Pane1.add(consol);
        Pane1.add(gamemode);
        Pane1.add(gamela);
        Pane1.add(difficulty);
        Pane1.add(difficultyla);
        Pane1.add(person);
        Pane1.add(personla);
        Pane1.add(real);
        Pane1.add(realla);
        Pane1.add(hard);
        Pane1.add(hardla);
        Pane1.add(command);
        Pane1.add(commandla);
        Pane1.add(sername);
        Pane1.add(sernamela);
        Pane1.add(ram);
        Pane1.add(ramla);
        Pane1.add(meesge);
        Pane1.add(savebt);
        Pane1.add(world);
        Pane1.add(manyset);

        pane = new JTabbedPane();
        pane.addTab("기본설정", Pane1);
        pane.addTab("OP리스트", new JLabel("OP리스트"));

        fr.add(pane);
        fr.setVisible(true);
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
                !setfile.name.equals(sername.getText())
                ) {
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
                    int result = JOptionPane.showConfirmDialog(fr, "월드를 삭제할까요?", "알림", JOptionPane.YES_NO_OPTION);
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