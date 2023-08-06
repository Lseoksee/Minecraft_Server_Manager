package seok;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;
import java.io.BufferedReader;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import javax.swing.JOptionPane;

import seok.UI.MainUI;

public class FileClass extends MainUI {
    public int mode;
    public int diff;
    public int plear;
    public boolean hardcore;
    public boolean reel;
    public boolean comman;
    public String name;
    public String filepath;
    public BufferedReader filReader;

    // 값 불러오기
    public FileClass(String filepath) {
        this.filepath = filepath;
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(filepath));

            switch (properties.getProperty("gamemode")) {
                case "survival" -> mode = 0;
                case "creative" -> mode = 1;
                case "adventure" -> mode = 2;
                case "spectator" -> mode = 3;
                default -> mode = Integer.parseInt(properties.getProperty("gamemode"));
            }

            switch (properties.getProperty("difficulty")) {
                case "peaceful" -> diff = 0;
                case "easy" -> diff = 1;
                case "normal" -> diff = 2;
                case "hard" -> diff = 3;
                default -> diff = Integer.parseInt(properties.getProperty("difficulty"));
            }

            plear = Integer.parseInt(properties.getProperty("max-players"));
            hardcore = Boolean.parseBoolean(properties.getProperty("hardcore"));
            reel = !Boolean.parseBoolean(properties.getProperty("online-mode"));
            comman = Boolean.parseBoolean(properties.getProperty("enable-command-block"));
            name = properties.getProperty("motd");
        } catch (FileNotFoundException e) {
            /* 기본값 할당 */
            mode = 0;
            diff = 1;
            plear = 20;
            hardcore = false;
            reel = false;
            comman = false;
            name = "A Minecraft Server";
            meesge.setEditable(false);
            gamemode.setEnabled(false);
            difficulty.setEnabled(false);
            person.setEnabled(false);
            hard.setEnabled(false);
            real.setEnabled(false);
            command.setEnabled(false);
            sername.setEnabled(false);
            ram.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 게임모드
        gamemode.setSelectedIndex(mode);
        // 난이도
        difficulty.setSelectedIndex(diff);
        // 참여인원
        person.setValue(plear);
        // 하드코어
        hard.setState(hardcore);
        // 정품여부
        real.setState(reel);
        // 커멘드
        command.setState(comman);
        // 이름
        sername.setText(name);
    }

    // 저장하기
    public void save() {
        try {
            filReader = new BufferedReader(new FileReader(filepath));
            // 게임모드
            if (gamemode.getSelectedItem() == "서바이벌") {
                mode = 0;
            } else if (gamemode.getSelectedItem() == "크리에이티브") {
                mode = 1;
            } else if (gamemode.getSelectedItem() == "모험모드") {
                mode = 2;
            } else if (gamemode.getSelectedItem() == "관전모드") {
                mode = 3;
            }
            // 난이도
            if (difficulty.getSelectedItem() == "평화로움") {
                diff = 0;
            } else if (difficulty.getSelectedItem() == "쉬움") {
                diff = 1;
            } else if (difficulty.getSelectedItem() == "보통") {
                diff = 2;
            } else if (difficulty.getSelectedItem() == "어려움") {
                diff = 3;
            }
            // 유저
            plear = (int) person.getValue();
            // 하드코어
            hardcore = hard.getState();
            // 정품여부(위에서 반대로 불러왔기 때문에 같다로 한다)
            reel = real.getState();
            // 커멘드여부
            comman = command.getState();
            // 서버이름
            name = sername.getText();

            replaceproperties();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(fr, "먼저 서버를 실행해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 파일변경
    public void replaceproperties() throws Exception {
        String sp[] = new String[2];
        StringBuffer sb = new StringBuffer();

        String read;
        while ((read = filReader.readLine()) != null) {
            sp = read.split("=", 2);
            switch (sp[0]) {
                case "gamemode" -> sb.append(sp[0] + "=" + mode + "\n");
                case "difficulty" -> sb.append(sp[0] + "=" + diff + "\n");
                case "hardcore" -> sb.append(sp[0] + "=" + hardcore + "\n");
                case "max-players" -> sb.append(sp[0] + "=" + plear + "\n");
                case "online-mode" -> sb.append(sp[0] + "=" + !reel + "\n");
                case "enable-command-block" -> sb.append(sp[0] + "=" + comman + "\n");
                case "motd" -> sb.append(sp[0] + "=" + escapeToUnicode(name) + "\n");
                default -> sb.append(read + "\n");
            }
        }

        FileOutputStream fos = new FileOutputStream(filepath);
        fos.write(sb.toString().getBytes());
        fos.close();

        state.setForeground(Color.GREEN);
        state.setText("저장완료!");
    }

    // 파일선택
    public static String filedia(Frame fr, String scanfile, String messege, int whatDialog, boolean allpath) {
        // 프레임, 파일, 메세지, 방식(0=불러오기,1저장하기), 전체경로?
        try {
            FileDialog fileDialogOpen = new FileDialog(fr, messege, whatDialog);
            fileDialogOpen.setFile(scanfile);
            fileDialogOpen.setDirectory(new File("").getAbsolutePath());
            fileDialogOpen.setVisible(true);
            String file = fileDialogOpen.getFile();
            if (allpath && file != null)
                return fileDialogOpen.getDirectory() + file;
            else
                return file;
        } catch (NullPointerException e) {
            return null;
        }
    }

    // String to 유니코드
    private String escapeToUnicode(String input) {
        StringBuilder builder = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (ch < 128) {
                builder.append(ch);
            } else {
                builder.append("\\u").append(String.format("%04X", (int) ch));
            }
        }
        return builder.toString();
    }
}