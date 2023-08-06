package seok.UI;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import seok.FileClass;
import seok.Jarstart;
import seok.Main;

public class MainUI extends Main {
    public static JPanel mainpan; // 메인화면

    public static JLabel state;

    public static JComboBox<String> gamemode;
    public static JLabel gamela;

    public static JComboBox<String> difficulty;
    public static JLabel difficultyla;

    public static JSpinner person;
    public static JLabel personla;

    public static Checkbox hard;
    public static JLabel hardla;

    public static Checkbox real;
    public static JLabel realla;

    public static Checkbox command;
    public static JLabel commandla;

    public static JTextField sername;
    public static JLabel sernamela;

    public static JSpinner ram;
    public static JLabel ramla;

    public static JButton world;
    public static JButton savebt;
    public static JButton manyset;

    public static JTextArea consol;
    public static JScrollPane consolsc;
    public static JButton startbt;
    public static JButton stopbt;

    public static JTextField meesge;

    public JPanel main() {
        fr.setTitle("마인크래프트 " + jarver.version + " 서버 관리자");

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
        person = new JSpinner();
        JSpinner.DefaultEditor personedit = (JSpinner.DefaultEditor) person.getEditor();
        personedit.getTextField().setHorizontalAlignment(JTextField.LEFT);
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
        ram = new JSpinner();
        JSpinner.DefaultEditor ramedit = (JSpinner.DefaultEditor) ram.getEditor();
        ramedit.getTextField().setHorizontalAlignment(JTextField.LEFT);
        ram.setValue(Jarstart.FINALRAM);
        ramla = new JLabel("램(GB):", JLabel.RIGHT);
        ramla.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 월드삭제
        world = new JButton("월드삭제");
        world.addActionListener(this);

        // 저장
        savebt = new JButton("저장하기");
        savebt.addActionListener(this);

        // 추가설정
        manyset = new JButton("추가설정");
        manyset.addActionListener(this);

        // 콘솔
        consol = new JTextArea();
        consol.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        consol.setEditable(false);
        consol.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) consol.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        consolsc = new JScrollPane(consol);
        consolsc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 시작
        startbt = new JButton("시작");
        startbt.addActionListener(this);

        // 정지
        stopbt = new JButton("정지");
        stopbt.addActionListener(this);

        // 메시지 입력창
        meesge = new JTextField();
        meesge.addKeyListener(this);
        meesge.addFocusListener(this);
        meesge.setEditable(false);

        // 값불러오기
        setfile = new FileClass("server.properties");
        new Mainbounds();

        // 패널 설정
        mainpan = new JPanel();
        mainpan.setLayout(null);
        mainpan.setBackground(Color.WHITE);

        mainpan.add(startbt);
        mainpan.add(stopbt);
        mainpan.add(state);
        mainpan.add(consolsc);
        mainpan.add(gamemode);
        mainpan.add(gamela);
        mainpan.add(difficulty);
        mainpan.add(difficultyla);
        mainpan.add(person);
        mainpan.add(personla);
        mainpan.add(real);
        mainpan.add(realla);
        mainpan.add(hard);
        mainpan.add(hardla);
        mainpan.add(command);
        mainpan.add(commandla);
        mainpan.add(sername);
        mainpan.add(sernamela);
        mainpan.add(ram);
        mainpan.add(ramla);
        mainpan.add(meesge);
        mainpan.add(savebt);
        mainpan.add(world);
        mainpan.add(manyset);
        return mainpan;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 시작
        if (e.getSource() == startbt && readThread == null) {
            new Jarstart();
        }
        // 정지
        if (e.getSource() == stopbt && readThread != null) {
            try {
                String message = "stop\n";
                outputStream.write(message.getBytes());
                outputStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // 저장
        if (e.getSource() == savebt && readThread == null) {
            setfile.save();
        }
        // 추가설정 버튼
        if (e.getSource() == manyset && readThread == null) {
            try {
                if (new File(setfile.filepath).exists()) {
                    ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "notepad.exe \"server.properties\"");
                    Process process = processBuilder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while (reader.readLine() != null) {
                    }
                    new FileClass(setfile.filepath);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        // 월드삭제 버튼
        if (e.getSource() == world && readThread == null) {
            try {
                if (new File("./world").isDirectory()) {
                    int result = JOptionPane.showConfirmDialog(fr, "월드를 삭제할까요?", "알림", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        new ProcessBuilder("cmd", "/c", "rmdir", "/s", "/q", "world", "world_nether", "world_the_end").start();
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 메시지 입력창
        if (e.getSource() == meesge && e.getKeyCode() == KeyEvent.VK_ENTER && readThread != null) {
            String message = meesge.getText() + "\n";
            consol.append("--> " + message);
            try {
                outputStream.write(message.getBytes());
                outputStream.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            meesge.setText(null);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        // 명령어 입력창 포커스
        if (e.getSource() == meesge && meesge.getText().equals("여기에 명령어 입력")) {
            meesge.setText(null);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        // 명령어 입력창 디포커스
        if (e.getSource() == meesge && meesge.getText().equals("")) {
            meesge.setText("여기에 명령어 입력");
        }
    }
}
