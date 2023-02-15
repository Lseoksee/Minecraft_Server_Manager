package com.seok;

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
    String mode;
    String diff;
    String plear;
    boolean reel;
    boolean comman;
    String name;
    String filepath;
    BufferedReader filReader;

    public FileClass(String filepath) {
        this.filepath = filepath;
    }

    // 값 불러오기
    public void searchset() {
        try {
            String line;
            String sb[] = new String[3];
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                sb = line.split("=");
                switch (sb[0]) {
                    case "gamemode" -> {
                        if (sb[1].equals("0") || sb[1].equals("survival")) {
                            mode = "서바이벌";
                        } else if (sb[1].equals("1") || sb[1].equals("creative")) {
                            mode = "크리에이티브";
                        } else if (sb[1].equals("2") || sb[1].equals("adventure")) {
                            mode = "모험모드";
                        } else if (sb[1].equals("3") || sb[1].equals("spectator")) {
                            mode = "관전모드";
                        }
                    }
                    case "difficulty" -> {
                        if (sb[1].equals("0") || sb[1].equals("peaceful")) {
                            diff = "평화로움";
                        } else if (sb[1].equals("1") || sb[1].equals("easy")) {
                            diff = "쉬움";
                        } else if (sb[1].equals("2") || sb[1].equals("normal")) {
                            diff = "보통";
                        } else if (sb[1].equals("3") || sb[1].equals("hard")) {
                            diff = "어려움";
                        }
                    }
                    case "max-players" -> plear = sb[1];
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
            mode = "서바이벌";
            diff = "평화로움";
            plear = "20";
            reel = false;
            comman = false;
            name = "A Minecraft Server";
            meesge.setEditable(false);
            gamemode.setEnabled(false);
            difficulty.setEnabled(false);
            person.setEnabled(false);
            real.setEnabled(false);
            command.setEnabled(false);
            sername.setEnabled(false);
            ram.setEnabled(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            filReader = new BufferedReader(new FileReader(filepath));
            if (gamemode.getSelectedItem() == "서바이벌") {
                mode = "0";
            } else if (gamemode.getSelectedItem() == "크리에이티브") {
                mode = "1";
            } else if (gamemode.getSelectedItem() == "모험모드") {
                mode = "2";
            } else if (gamemode.getSelectedItem() == "관전모드") {
                mode = "3";
            }
            // 게임모드
            if (difficulty.getSelectedItem() == "평화로움") {
                diff = "0";
            } else if (difficulty.getSelectedItem() == "쉬움") {
                diff = "1";
            } else if (difficulty.getSelectedItem() == "보통") {
                diff = "2";
            } else if (difficulty.getSelectedItem() == "어려움") {
                diff = "3";
            }
            // 난이도
            plear = person.getText();
            // 유저
            reel = !real.getState();
            // 정품여부
            comman = command.getState();
            // 커멘드여부
            name = StringEscapeUtils.escapeJava(sername.getText());
            // 서버이름
            replaceini();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "먼저 서버를 실행해 주세요.", "알림", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // 파일변경
    public void replaceini() { // 파일명, 바꿀라인(첫줄이1), 텍스트
        try {
            String sp[] = new String[3];
            String read;
            StringBuffer sb = new StringBuffer();
            while ((read = filReader.readLine()) != null) {
                sp = read.split("=");
                switch (sp[0]) {
                    case "gamemode" -> sb.append(sp[0] + "=" + mode + "\n");
                    case "difficulty" -> sb.append(sp[0] + "=" + diff + "\n");
                    case "max-players" -> sb.append(sp[0] + "=" + plear + "\n");
                    case "online-mode" -> sb.append(sp[0] + "=" + reel + "\n");
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
        state.setForeground(Color.green);
        state.setText("저장완료!");
    }

    // 파일선택
    public static String filedia(Frame fr, String scanfile) { // 프레임, 찾을파일형
        try {
            FileDialog fileDialogOpen = new FileDialog(fr, "버킷 jar파일을 선택하시오", FileDialog.LOAD);
            fileDialogOpen.setFile(scanfile);
            fileDialogOpen.setDirectory(new File("./").getAbsolutePath());
            fileDialogOpen.setVisible(true);
            return fileDialogOpen.getFile();
        } catch (NullPointerException e) {
            return null;
        }
    }
}