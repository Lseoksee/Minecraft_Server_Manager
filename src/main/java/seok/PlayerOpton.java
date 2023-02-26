package seok;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlayerOpton implements Runnable, KeyListener, ActionListener, MouseListener, ListSelectionListener {

    static int i = -1;
    static JSONArray array;
    static JLabel oplLabel;
    static JLabel title;
    static JList<String> opList;
    static JScrollPane opscroll;
    static DefaultListModel<String> addoplist;
    static JPanel oppan; // OP리스트
    static JButton opselbt;
    static JButton opdelbt;
    static JTextField opField;
    static JPopupMenu menu;
    static JMenuItem del;

    public PlayerOpton(boolean real) {
        if (real) {
            title.setForeground(Color.ORANGE);
            title.setText("현재 비정품 서버 입니다.");
        } else {
            title.setForeground(Color.GREEN);
            title.setText("현재 정품 서버 입니다.");
        }
        try {
            File file = new File("ops.json");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = fis.readAllBytes();
            array = new JSONArray(new String(data));
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public JPanel Playergui() {
        oplLabel = new JLabel("OP 리스트");
        oppan = new JPanel();
        oppan.setLayout(null);
        oppan.setBackground(Color.WHITE);

        addoplist = new DefaultListModel<>();
        opList = new JList<>(addoplist);
        opscroll = new JScrollPane(opList);
        opList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        opselbt = new JButton("추가");
        opdelbt = new JButton("삭제");
        opField = new JTextField();

        title = new JLabel("", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 17));

        oplLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        opList.setFont(new Font("맑은 고딕", Font.PLAIN, 15));

        menu = new JPopupMenu();
        del = new JMenuItem("삭제");
        menu.add(del);
        opList.add(menu);

        opselbt.addActionListener(new PlayerOpton());
        opdelbt.addActionListener(new PlayerOpton());
        opField.addKeyListener(new PlayerOpton());
        del.addActionListener(new PlayerOpton());
        opList.addListSelectionListener(new PlayerOpton());
        opList.addKeyListener(new PlayerOpton());
        opList.addMouseListener(new PlayerOpton());

        new Playerbounds();

        oppan.add(oplLabel);
        oppan.add(title);
        oppan.add(opscroll);
        oppan.add(opselbt);
        oppan.add(opdelbt);
        oppan.add(opField);
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
            addoplist.add(0 ,opField.getText());
            opscroll.getViewport().setViewPosition(new Point(0, 0));
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
            opField.setText(null);
            addoplist.removeElement(opList.getSelectedValue());
        } catch (Exception e) {
            opField.setText(null);
        }
    }

    //선택값 변화있다면 
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == opList) {
            opField.setText(opList.getSelectedValue());
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
    }

    //op리스트 가져오기
    @Override
    public void run() {
        while (i < array.length() - 1) {
            synchronized (this) {
                i++;
            }
            JSONObject jsonObject = array.getJSONObject(i);
            new PlayerOpton().getUUID(jsonObject.getString("uuid"), jsonObject.getString("name"));
        }
    }

    public void getUUID(String uuid, String name) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().toString();
            if (!Main.real.getState()) {
                addoplist.addElement(name); // 정품서버인 경우 리스트에 추가
            }

        } catch (Exception e) {
            if (Main.real.getState()) {
                addoplist.addElement(name); // 비정품 서버인경우 리스트에 추가
            }
        }
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