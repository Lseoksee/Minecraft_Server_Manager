package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.awt.Color;

public class jarstart extends Main implements Runnable {
    public static final int finalram = 4;
    String path;

    public jarstart() {
        if (setfile.mode != gamemode.getSelectedIndex() ||
            setfile.diff != difficulty.getSelectedIndex() ||
            setfile.plear != (int) person.getValue() ||
            !setfile.hardcore == hard.getState() ||
            !setfile.reel == real.getState() ||
            !setfile.comman == command.getState() ||
            !setfile.name.equals(sername.getText())) {
                setfile.save();
        }
        String link = "C:/Program Files/Java/";
        File fr = new File(link);
        String list[] = fr.list();
        if (version.matches("\\d+\\.(?:1[0-5]|[0-9])(\\.\\d+)*")) {
            for (String as : list) {
                if (as.matches(".*jre.*|.*jdk-(8|11).*")) {
                    path = "\"" + link + as + "/bin/java" + "\"";
                }     
            }
        } else {
            for (String as : list) {
                if (as.matches(".*jdk-17.*")) 
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
            ProcessBuilder processBuilder = new ProcessBuilder(path, "-Xmx"+ram.getValue()+"G", "-Xms"+ram.getValue()+"G", "-jar", "Minecraft_" + version + "_server.jar", "nogui");
            processBuilder.redirectErrorStream(true);       //에러스트림 인풋스트림 병합
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            outputStream = process.getOutputStream();

            consol.setText(null);
            state.setForeground(null);
            state.setText("서버시작중...");
            meesge.setEditable(true);
            meesge.setText("여기에 명령어 입력");

            pane.setEnabled(true);
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
                if (line.matches("(.*)<(.*)>(.*)|(.*)\\[Server\\](.*)|(.*)issued server command(.*)")) {
                    PlayerOpton.chatlog.append(line + "\n");
                }
                consol.append(line + "\n");
            }

            meesge.setEditable(false);
            meesge.setText(null);
            pane.setEnabled(false);
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
            PlayerOpton.addoplist.removeAllElements();
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