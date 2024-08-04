package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import seok.UI.MainUI;
import seok.UI.PlayerOptonUI;

import java.awt.Color;

public class Jarstart extends MainUI implements Runnable {
    public static final int FINALRAM = 4;
    String path;

    public Jarstart() {
        String link = "C:/Program Files/Java/";
        File fr = new File(link);
        String list[] = fr.list();
        if (jarver.version.matches("\\d+\\.(?:1[0-5]|[0-9])(\\.\\d+)*")) {
            for (String as : list) {
                if (as.matches(".*jre.*|.*jdk-(8|11).*")) {
                    path = "\"" + link + as + "/bin/java" + "\"";
                }     
            }
        } else {
            for (String as : list) {
                if (as.matches(".*jdk-(1[7-9]|[2-9][0-9]).*")) 
                    path = "\"" + link + as + "/bin/java" + "\"";
            }
        }
        if (path == null) path = "java";
        readThread = new Thread(this);
        readThread.start();
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(path, "-Xmx"+ram.getValue()+"G", "-Xms"+ram.getValue()+"G", "-Dfile.encoding=MS949", "-jar", "Minecraft_" + jarver.version + "_server.jar", "nogui");
            processBuilder.redirectErrorStream(true);       //에러스트림 인풋스트림 병합
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            outputStream = process.getOutputStream();

            consol.setText(null);
            state.setForeground(null);
            state.setText("서버시작중...");
            meesge.setEditable(true);
            meesge.setText("여기에 명령어 입력");

            pane.setEnabledAt(1, true);
            gamemode.setEnabled(false);
            difficulty.setEnabled(false);
            person.setEnabled(false);
            hard.setEnabled(false);
            real.setEnabled(false);
            command.setEnabled(false);
            sername.setEnabled(false);
            ram.setEnabled(false);

            String line;
            while ((line = reader.readLine()) != null) {
                Thread.sleep(10);
                if (line.indexOf("Done") != -1) {
                    state.setForeground(Color.GREEN);
                    state.setText("서버가 정상적으로 시작되었습니다");
                }
                if (line.matches("^\\[.*\\]: <(.*)>(.*)|^\\[.*\\]:( \\[Not Secure\\])? \\[Server\\] (.*)|^\\[.*\\]:(.*)issued server command:(.*)")) {
                    PlayerOptonUI.chatlog.append(line + "\n");
                }
                consol.append(line + "\n");
            }

            meesge.setEditable(false);
            meesge.setText(null);
            pane.setEnabledAt(1, false);
            gamemode.setEnabled(true);
            difficulty.setEnabled(true);
            person.setEnabled(true);
            hard.setEnabled(true);
            real.setEnabled(true);
            command.setEnabled(true);
            sername.setEnabled(true);
            ram.setEnabled(true);
            state.setForeground(null);

            oplistclick = false;
            PlayerOptonUI.addoplist.removeAllElements();
            pane.setSelectedIndex(0);

            if (state.getText() == "서버가 정상적으로 시작되었습니다") {
                state.setText("마인크래프트 서버 관리자");
            } else {
                state.setForeground(Color.RED);
                state.setText("서버가 정상종료 되지 않았습니다!");
            }
            readThread = null;
        } catch (Exception ex) {
        }
    }
}
