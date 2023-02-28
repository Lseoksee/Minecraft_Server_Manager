package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.awt.Color;

public class jarstart extends Main implements Runnable {
    public static final String finalram = "4";
    String path;

    public jarstart() {
        consol.setText(null);
        if (!setfile.mode.equals(gamemode.getSelectedItem()) ||
            !setfile.diff.equals(difficulty.getSelectedItem()) ||
            !setfile.plear.equals(person.getText()) ||
            !setfile.hardcore == hard.getState() ||
            !setfile.reel == real.getState() ||
            !setfile.comman == command.getState() ||
            !setfile.name.equals(sername.getText())) {
            setfile.save();
        }
        state.setForeground(null);
        state.setText("서버시작중...");

        if (realver > 1000) {
            realver /= 10;
        }
        String java = "";
        if (realver >= 116)
            java = "jdk";
        else
            java = "jre";
        String link = "C:/Program Files/Java/";
        File fr = new File(link);
        String list[] = fr.list();
        for (String as : list) {
            if (as.substring(0, 3).equals(java)) {
                path = "\"" + link + as + "/bin/java" + "\"";
            }
        }
        readThread = new Thread(this);
        readThread.start();
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(path, "-Xmx" + ram.getText() + "G", "-Xms1G", "-jar", "Minecraft_" + longver + "_server.jar", "nogui");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            outputStream = process.getOutputStream();

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
            PlayerOpton.i = -1;
            PlayerOpton.addoplist.removeAllElements();
            PlayerOpton.chatlog.setText(null);
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