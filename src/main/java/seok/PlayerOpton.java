package seok;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

public class PlayerOpton implements Runnable, KeyListener, ActionListener, MouseListener, ListSelectionListener, FocusListener {

    static JPanel oppan; // 플레이어 관리
    static int i = -1;
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
    
    public PlayerOpton(boolean real) {
        if (real) {
            title.setForeground(Color.ORANGE);
            title.setText("현재 비정품 서버 입니다.");
        } else {
            title.setForeground(Color.GREEN);
            title.setText("현재 정품 서버 입니다.");
        }

        try {
            FileInputStream fis = new FileInputStream("ops.json");
            array = new JSONArray(new String(fis.readAllBytes()));
            fis.close();
        } catch (Exception e) {
            array = new JSONArray("[]");
        } 
    }

    public JPanel Playergui() {
        oppan = new JPanel();
        oppan.setLayout(null);
        oppan.setBackground(Color.WHITE);

        title = new JLabel("", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 17));
        
        //OP 리스트
        oplLabel = new JLabel("OP 리스트");
        addoplist = new DefaultListModel<>();
        opList = new JList<>(addoplist);
        opList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        opscroll = new JScrollPane(opList);
        
        opselbt = new JButton("추가");
        opdelbt = new JButton("삭제");
        opfile = new JButton("가져오기");
        opField = new JTextField();

        oplLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        opList.setFont(new Font("맑은 고딕", Font.PLAIN, 15));

        menu = new JPopupMenu();
        del = new JMenuItem("삭제");
        menu.add(del);
        opList.add(menu);

        opselbt.addActionListener(new PlayerOpton());
        opdelbt.addActionListener(new PlayerOpton());
        opfile.addActionListener(new PlayerOpton());
        opField.addKeyListener(new PlayerOpton());
        opField.addFocusListener(new PlayerOpton());
        del.addActionListener(new PlayerOpton());
        opList.addListSelectionListener(new PlayerOpton());
        opList.addKeyListener(new PlayerOpton());
        opList.addMouseListener(new PlayerOpton());

        //채팅로그
        chatLabel = new JLabel("채팅로그");
        chatlog = new JTextArea();
        chatlog.setEditable(false);
        chatlog.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret) chatlog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chatscroll = new JScrollPane(chatlog);
        chatField = new JTextField();
        chatField.setText("여기에 메시지를 입력");

        chatlog.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        chatLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        chatField.addFocusListener(new PlayerOpton());
        chatField.addKeyListener(new PlayerOpton());

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
        return oppan;
    }
    
    //추가
    public void seloplist() {
        try {
            if (opField.getText().equals("") || addoplist.contains(opField.getText())) {
                opField.setText(null);
                return;
            }
            String message = "op "+opField.getText()+"\n";
            Main.outputStream.write(message.getBytes());
            Main.outputStream.flush();
            addoplist.addElement(opField.getText());
            opList.ensureIndexIsVisible(addoplist.getSize()-1);
            opField.setText(null);
        } catch (Exception e) {
            opField.setText(null);
        }
    }

    //삭제
    public void deloplist() {
        try {
            if (opField.getText().equals("") || !addoplist.contains(opField.getText())) {
                opField.setText(null);
                return;
            }
            String message = "deop "+opField.getText()+"\n";
            Main.outputStream.write(message.getBytes());
            Main.outputStream.flush();
            addoplist.removeElement(opField.getText());
            opField.setText(null);
        } catch (Exception e) {
            opField.setText(null);
        }
    }

    //채팅입력
    public void chatinput() {
        try {
            String message = "say "+chatField.getText()+"\n";
            Main.outputStream.write(message.getBytes());
            Main.outputStream.flush();
            chatField.setText(null);
        } catch (Exception e) {
            chatField.setText(null);
        }
    }

    //마우스 이벤트
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == opList && e.getButton() == MouseEvent.BUTTON3) {
            opList.setSelectedIndex(opList.locationToIndex(e.getPoint())); // 우클릭도 선택되게
            menu.show(opList, e.getX(), e.getY());
        }
    }

    //키보드 이벤트
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == opList && e.getKeyCode() == KeyEvent.VK_DELETE) {
            deloplist();
        }
        if (e.getSource() == opField && e.getKeyCode() == KeyEvent.VK_ENTER) {
            seloplist();
        }
        if (e.getSource() == chatField && e.getKeyCode() == KeyEvent.VK_ENTER) {
            chatinput();
        }
    }

    //버튼 이벤트
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == del || e.getSource() == opdelbt) {
            deloplist();
        }
        if (e.getSource() == opselbt) {
            seloplist();
        }
        if (e.getSource() == opfile) {
            try {
                String check = FileClass.filedia(Main.fr, "*.txt", "OP리스트 텍스트 파일을 선택하시오",  true);
                BufferedReader br = new BufferedReader(new FileReader(check));
                String line;
                while ((line = br.readLine()) != null) {
                    if (addoplist.contains(line)) {
                        continue;
                    }
                    String message = "op "+line+"\n";
                    Main.outputStream.write(message.getBytes());
                    Main.outputStream.flush();
                    addoplist.addElement(line);
                    opList.ensureIndexIsVisible(addoplist.getSize()-1);
                }
                br.close();
            } catch (Exception ex) {
                return;
            }
        }
    }

    
    //선택값 변화있다면 
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == opList) {
            opField.setText(opList.getSelectedValue());
        } 
    }

    //포커스 이벤트
    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == opField) {
            opField.setText(null);
        }
        if (e.getSource() == chatField && chatField.getText().equals("여기에 메시지를 입력")) {
            chatField.setText(null);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == chatField && chatField.getText().equals("")) {
            chatField.setText("여기에 메시지를 입력");
        }
    }

    //op리스트 가져오기
    @Override
    public void run() {
        for (i = 0; i < array.length() -1; i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String name = jsonObject.getString("name");
            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + jsonObject.getString("uuid"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().toString();
                if (!Main.real.getState()) {
                    addoplist.addElement(name); // 정품서버인 경우 리스트에 추가
                    opList.ensureIndexIsVisible(addoplist.getSize()-1);
                }
            } catch (Exception e) {
                if (Main.real.getState()) {
                    addoplist.addElement(name); // 비정품 서버인경우 리스트에 추가
                    opList.ensureIndexIsVisible(addoplist.getSize()-1);
                }
            }
        }
    }

    public void getUUID(String uuid, String name) {

    }

    public PlayerOpton() {
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