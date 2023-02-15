package com.seok;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.Choice;
import java.awt.Font;
import java.awt.Checkbox;
import javax.imageio.ImageIO;
import javax.swing.JTextField;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.MenuItem;
import java.awt.PopupMenu;

public class Main extends WindowAdapter implements ActionListener, KeyListener, MouseListener {
    static Thread readThread;
    static OutputStream outputStream;
    static FileClass setfile = new FileClass("./server.properties"); // 서버 설정파일
    static String filename; // jar 파일이름
    static String longver; // .포함 버전
    static int realver; // 실제 숫자 버전

    static Frame fr = new Frame();
    static Label state = new Label();

    static Choice gamemode = new Choice();
    static Label gamela = new Label();

    static Choice difficulty = new Choice();
    static Label difficultyla = new Label();

    static JTextField person = new JTextField();
    static Label personla = new Label();

    static Checkbox real = new Checkbox();
    static Label realla = new Label();

    static Checkbox command = new Checkbox();
    static Label commandla = new Label();

    static JTextField sername = new JTextField();
    static Label sernamela = new Label();

    static JTextField ram = new JTextField();
    static Label ramla = new Label();

    static Button savebt = new Button();

    static TextArea consol = new TextArea();
    static Button startbt = new Button();
    static Button stopbt = new Button();

    static JTextField meesge = new JTextField();

    static JTextField jarkey = new JTextField();
    static Button jarok = new Button();

    static SystemTray Tray;
    static TrayIcon trayico;
    static PopupMenu menu;
    static MenuItem open;
    static MenuItem exit;

    static boolean trayover;
    ClassLoader cl = getClass().getClassLoader();

    public static void main(String[] args) {
        fr.setSize(400, 650); // (프레임크기-객체크기-8)*
        fr.addWindowListener(new Main());
        fr.setResizable(false);
        fr.setLocationRelativeTo(null);
        fr.setLayout(null);
        fr.setFocusable(true);
        fr.setIconImage(fr.getToolkit().getImage(new Main().cl.getResource("com/seok/img/mincraft.png")));

        if (!new findjar().searchjar()) {
            fr.setTitle("마인크래프트 서버 관리자");
            Label versub = new Label("버전을 입력해주세요.");

            versub.setBounds(8, 285, 384, 20);
            versub.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            versub.setAlignment(Label.CENTER);

            jarkey.setBounds(96, 308, 200, 25);
            jarkey.addKeyListener(new Main());

            jarok.setLabel("확인");
            jarok.setBounds(156, 340, 80, 25);
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
        setfile.searchset();

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
        state.setBounds(8, 40, 384, 20);
        state.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        state.setAlignment(Label.CENTER);

        // 게임모드
        gamemode.add("서바이벌");
        gamemode.add("크리에이티브");
        gamemode.add("모험모드");
        gamemode.add("관전모드");
        gamemode.setBounds(171, 70, 150, 24);
        gamemode.select(setfile.mode);
        //choice 높이값=24
        gamela.setText("게임모드:");
        gamela.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        gamela.setAlignment(Label.RIGHT);
        gamela.setBounds(8, 70, 160, 24);

        // 난이도
        difficulty.add("평화로움");
        difficulty.add("쉬움");
        difficulty.add("보통");
        difficulty.add("어려움");
        difficulty.setBounds(171, 110, 150, 24);
        difficulty.select(setfile.diff);
        //choice 높이값=24
        difficultyla.setText("난이도:");
        difficultyla.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        difficultyla.setAlignment(Label.RIGHT);
        difficultyla.setBounds(8, 110, 160, 24);

        // 참여인원
        person.setBounds(171, 150, 150, 24);
        person.setText(setfile.plear);

        personla.setText("참여인원:");
        personla.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        personla.setAlignment(Label.RIGHT);
        personla.setBounds(8, 150, 160, 24);

        // 정품여부
        real.setBounds(171, 190, 14, 24);
        real.setState(setfile.reel);

        realla.setText("비정품 허용:");
        realla.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        realla.setAlignment(Label.RIGHT);
        realla.setBounds(8, 190, 160, 24);

        // 커멘드 블록
        command.setBounds(171, 230, 14, 24);
        command.setState(setfile.comman);
        
        commandla.setText("커맨드 블록 허용:");
        commandla.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        commandla.setAlignment(Label.RIGHT);
        commandla.setBounds(8, 230, 160, 24);

        // 서버이름
        sername.setBounds(171, 270, 150, 24);
        sername.setText(setfile.name);

        sernamela.setText("서버이름:");
        sernamela.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        sernamela.setAlignment(Label.RIGHT);
        sernamela.setBounds(8, 270, 160, 24);

        // 램
        ram.setBounds(171, 310, 150, 24);
        ram.setText(jarstart.finalram);

        ramla.setText("램(GB):");
        ramla.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        ramla.setAlignment(Label.RIGHT);
        ramla.setBounds(8, 310, 160, 24);

        //저장
        savebt.setLabel("저장하기");
        savebt.addActionListener(new Main());
        savebt.setBounds(156, 340, 80, 30);

        // 콘솔
        consol.setBounds(8, 380, 384, 180);
        consol.setEditable(false);

        // 시작
        startbt.setLabel("시작");
        startbt.addActionListener(new Main());
        startbt.setBounds(106, 600, 80, 30);

        // 정지
        stopbt.setLabel("정지");
        stopbt.addActionListener(new Main());
        stopbt.setBounds(206, 600, 80, 30);

        // 메시지 입력창
        meesge.setBounds(36, 570, 320, 20);
        meesge.addKeyListener(new Main());
        meesge.setEditable(false);

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
        fr.add(command);
        fr.add(commandla);
        fr.add(sername);
        fr.add(sernamela);
        fr.add(ram);
        fr.add(ramla);
        fr.add(meesge);
        fr.add(savebt);
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
                trayico = new TrayIcon(ImageIO.read(new Main().cl.getResourceAsStream("com/seok/img/minecraft_tray.png")));
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
        //좌클릭
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