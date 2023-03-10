package seok;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.FileDialog;
import java.io.BufferedReader;
import org.apache.commons.text.StringEscapeUtils;

public class FileClass extends Main {
    int mode;
    int diff;
    int plear;
    boolean hardcore;
    boolean reel;
    boolean comman;
    String name;
    String filepath;
    BufferedReader filReader;

    // 값 불러오기
    public FileClass(String filepath) {
        this.filepath = filepath;
        try {
            String line;
            String sb[] = new String[3];
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                sb = line.split("=");
                switch (sb[0]) {
                    case "gamemode" -> {
                        if (sb[1].equals("0") || sb[1].equals("survival")) {
                            mode = 0;
                        } else if (sb[1].equals("1") || sb[1].equals("creative")) {
                            mode = 1;
                        } else if (sb[1].equals("2") || sb[1].equals("adventure")) {
                            mode = 2;
                        } else if (sb[1].equals("3") || sb[1].equals("spectator")) {
                            mode = 3;
                        }
                    }
                    case "difficulty" -> {
                        if (sb[1].equals("0") || sb[1].equals("peaceful")) {
                            diff = 0;
                        } else if (sb[1].equals("1") || sb[1].equals("easy")) {
                            diff = 1;
                        } else if (sb[1].equals("2") || sb[1].equals("normal")) {
                            diff = 2;
                        } else if (sb[1].equals("3") || sb[1].equals("hard")) {
                            diff = 3;
                        }
                    }
                    case "max-players" -> plear = Integer.parseInt(sb[1]);
                    case "hardcore" -> hardcore = Boolean.parseBoolean(sb[1]);
                    case "online-mode" -> reel = !Boolean.parseBoolean(sb[1]);
                    case "enable-command-block" -> comman = Boolean.parseBoolean(sb[1]);
                    case "motd" -> name = StringEscapeUtils.unescapeJava(sb[1]);
                    default -> {
                        continue;
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
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
        } catch (IOException e) {
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
            name = StringEscapeUtils.escapeJava(sername.getText());

            replaceini();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(fr, "먼저 서버를 실행해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // 파일변경
    public void replaceini() {
        try {
            String sp[] = new String[3];
            String read;
            StringBuffer sb = new StringBuffer();
            while ((read = filReader.readLine()) != null) {
                sp = read.split("=");
                switch (sp[0]) {
                    case "gamemode" -> sb.append(sp[0] + "=" + mode + "\n");
                    case "difficulty" -> sb.append(sp[0] + "=" + diff + "\n");
                    case "hardcore" -> sb.append(sp[0] + "=" + hardcore + "\n");
                    case "max-players" -> sb.append(sp[0] + "=" + plear + "\n");
                    case "online-mode" -> sb.append(sp[0] + "=" + !reel + "\n");
                    case "enable-command-block" -> sb.append(sp[0] + "=" + comman + "\n");
                    case "motd" -> sb.append(sp[0] + "=" + name + "\n");
                    default -> sb.append(read + "\n");
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filepath)); // 위치바꿀것
            byte[] bytetext = sb.toString().getBytes();
            fileOutputStream.write(bytetext);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}