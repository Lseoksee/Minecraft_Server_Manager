package seok;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlayerOpton extends Main implements Runnable, ListSelectionListener {

    static JPanel oppan; // 플레이어 관리
    static JSONArray array;

    static JLabel oplLabel;
    static JLabel title;
    static JList<String> opList;
    static JScrollPane opscroll;
    static DefaultListModel<String> addoplist;
    static JButton opselbt;
    static JButton opdelbt;
    static JButton opfile;
    static JTextField opField;
    static JPopupMenu menu;
    static JMenuItem del;

    static JLabel chatLabel;
    static JTextArea chatlog;
    static JTextField chatField;
    static JScrollPane chatscroll;
    static JButton chatinput;
    static JButton chatclear;
    static JButton chatsave;
    int i = -1;

    public PlayerOpton(boolean real) {
        if (real) {
            title.setForeground(Color.ORANGE);
            title.setText("현재 비정품 서버 입니다.");
        } else {
            title.setForeground(Color.GREEN);
            title.setText("현재 정품 서버 입니다.");
        }
        try {
            Path path = Paths.get("ops.json");
            array = new JSONArray(new String(Files.readAllBytes(path)));
        } catch (Exception e) {
            array = new JSONArray("[]");
        }
    }

    public JPanel Playergui() {
        // 패널설정
        oppan = new JPanel();
        oppan.setLayout(null);
        oppan.setBackground(Color.WHITE);

        // 타이틀
        title = new JLabel("", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 17));

        // OP 리스트
        oplLabel = new JLabel("OP 리스트");
        oplLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // op 리스트 페이지 설정
        addoplist = new DefaultListModel<>();
        opList = new JList<>(addoplist);
        opList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        opList.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        opList.addListSelectionListener(new PlayerOpton());
        opList.addKeyListener(new PlayerOpton());
        opList.addMouseListener(new PlayerOpton());
        opscroll = new JScrollPane(opList);

        // op추가 버튼
        opselbt = new JButton("추가");
        opselbt.addActionListener(new PlayerOpton());

        // op삭제버튼
        opdelbt = new JButton("삭제");
        opdelbt.addActionListener(new PlayerOpton());

        // op가져오기 버튼
        opfile = new JButton("가져오기");
        opfile.addActionListener(new PlayerOpton());

        // op 입력창
        opField = new JTextField();
        opField.addKeyListener(new PlayerOpton());
        opField.addFocusListener(new PlayerOpton());

        // op 팝업메뉴
        menu = new JPopupMenu();
        del = new JMenuItem("삭제");
        del.addActionListener(new PlayerOpton());
        menu.add(del);
        opList.add(menu);

        // 채팅로그
        chatLabel = new JLabel("채팅로그");
        chatLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        // 채팅 페이지 설정
        chatlog = new JTextArea();
        chatlog.setEditable(false);
        chatlog.setLineWrap(true);
        chatlog.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        DefaultCaret caret = (DefaultCaret) chatlog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chatscroll = new JScrollPane(chatlog);

        // 채팅 입력창
        chatField = new JTextField();
        chatField.setText("여기에 메시지를 입력");
        chatField.addFocusListener(new PlayerOpton());
        chatField.addKeyListener(new PlayerOpton());

        // 채팅 입력 버튼
        chatinput = new JButton("입력");
        chatinput.addActionListener(new PlayerOpton());

        // 채팅 초기화 버튼
        chatclear = new JButton("초기화");
        chatclear.addActionListener(new PlayerOpton());

        // 채팅 저장버튼
        chatsave = new JButton("저장하기");
        chatsave.addActionListener(new PlayerOpton());

        new Playerbounds();

        oppan.add(oplLabel);
        oppan.add(title);
        oppan.add(opscroll);
        oppan.add(opselbt);
        oppan.add(opdelbt);
        oppan.add(opfile);
        oppan.add(opField);
        oppan.add(chatLabel);
        oppan.add(chatscroll);
        oppan.add(chatField);
        oppan.add(chatinput);
        oppan.add(chatclear);
        oppan.add(chatsave);
        return oppan;
    }

    // 추가
    public void seloplist() {
        try {
            if (opField.getText().equals("") || addoplist.contains(opField.getText())) {
                opField.setText(null);
                return;
            }
            String message = "op " + opField.getText() + "\n";
            outputStream.write(message.getBytes());
            outputStream.flush();
            addoplist.addElement(opField.getText());
            opList.ensureIndexIsVisible(addoplist.getSize() - 1);
            opField.setText(null);
        } catch (Exception e) {
            opField.setText(null);
        }
    }

    // 삭제
    public void deloplist() {
        try {
            if (opField.getText().equals("") || !addoplist.contains(opField.getText())) {
                opField.setText(null);
                return;
            }
            String message = "deop " + opField.getText() + "\n";
            outputStream.write(message.getBytes());
            outputStream.flush();
            addoplist.removeElement(opField.getText());
            opField.setText(null);
        } catch (Exception e) {
            opField.setText(null);
        }
    }

    // 채팅입력
    public void chatinput() {
        if (!chatField.getText().equals("") && chatField.getText().equals("여기에 메시지를 입력")) {
            return;
        }
        try {
            String message = "say " + chatField.getText() + "\n";
            outputStream.write(message.getBytes());
            outputStream.flush();
            chatField.setText(null);
        } catch (Exception e) {
            chatField.setText(null);
        }
    }

    // 마우스 이벤트
    @Override
    public void mouseClicked(MouseEvent e) {
        // op 팝업메뉴
        if (e.getSource() == opList && e.getButton() == MouseEvent.BUTTON3) {
            opList.setSelectedIndex(opList.locationToIndex(e.getPoint())); // 우클릭도 선택되게
            menu.show(opList, e.getX(), e.getY());
        }
    }

    // 키보드 이벤트
    @Override
    public void keyPressed(KeyEvent e) {
        // op키보드 삭제
        if (e.getSource() == opList && e.getKeyCode() == KeyEvent.VK_DELETE) {
            deloplist();
        }
        // op키보드 추가
        if (e.getSource() == opField && e.getKeyCode() == KeyEvent.VK_ENTER) {
            seloplist();
        }
        // 채팅 키보드 입력
        if (e.getSource() == chatField && e.getKeyCode() == KeyEvent.VK_ENTER) {
            chatinput();
        }
    }

    // 버튼 이벤트
    @Override
    public void actionPerformed(ActionEvent e) {
        // op 추가버튼
        if (e.getSource() == opselbt) {
            seloplist();
        }
        // op 삭제버튼
        if (e.getSource() == del || e.getSource() == opdelbt) {
            deloplist();
        }
        // op 파일선택버튼
        if (e.getSource() == opfile) {
            try {
                String check = FileClass.filedia(fr, "*.txt", "OP리스트 텍스트 파일을 선택하시오", 0, true);
                BufferedReader br = new BufferedReader(new FileReader(check));
                String line;
                while ((line = br.readLine()) != null) {
                    if (addoplist.contains(line)) {
                        continue;
                    }
                    String message = "op " + line + "\n";
                    outputStream.write(message.getBytes());
                    outputStream.flush();
                    addoplist.addElement(line);
                    opList.ensureIndexIsVisible(addoplist.getSize() - 1);
                }
                br.close();
            } catch (Exception ex) {
                return;
            }
        }
        // 채팅 입력버튼
        if (e.getSource() == chatinput) {
            System.out.println(Thread.activeCount());
            /* chatinput(); */
        }
        // 채팅 지우기버튼
        if (e.getSource() == chatclear) {
            chatlog.setText(null);
        }
        // 채팅 저장버튼
        if (e.getSource() == chatsave && !chatlog.getText().equals("")) {
            try {
                String save = FileClass.filedia(fr, "Chatlog-" + LocalDate.now() + ".txt", "저장할 위치를 지정하시오", 1, true);
                BufferedWriter bw = new BufferedWriter(new FileWriter(save));
                bw.write(chatlog.getText());
                bw.close();
            } catch (Exception e1) {
                return;
            }
        }
    }

    // 선택값 변화있다면
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // oplist 선택변화
        if (e.getSource() == opList) {
            opField.setText(opList.getSelectedValue());
        }
    }

    // 포커스 이벤트
    @Override
    public void focusGained(FocusEvent e) {
        // op 입력창 포커스
        if (e.getSource() == opField) {
            opField.setText(null);
        }
        // 채팅 입력창 포커스
        if (e.getSource() == chatField && chatField.getText().equals("여기에 메시지를 입력")) {
            chatField.setText(null);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        // 채팅 입력창 디포커스
        if (e.getSource() == chatField && chatField.getText().equals("")) {
            chatField.setText("여기에 메시지를 입력");
        }
    }

    // op리스트 가져오기
    @Override
    public void run() {
        while (i < array.length() - 1) {
            synchronized(this) {
                i++;
            }
            JSONObject jsonObject = array.getJSONObject(i);
            String name = jsonObject.getString("name");
            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + jsonObject.getString("uuid"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().toString();
                if (!real.getState()) {
                    addoplist.addElement(name); // 정품서버인 경우 리스트에 추가
                    opList.ensureIndexIsVisible(addoplist.getSize() - 1);
                }
            } catch (Exception e) {
                if (real.getState()) {
                    addoplist.addElement(name); // 비정품 서버인경우 리스트에 추가
                    opList.ensureIndexIsVisible(addoplist.getSize() - 1);
                }
            }
        }
    }

    public PlayerOpton() {
    }
}