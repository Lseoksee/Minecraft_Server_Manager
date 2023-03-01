package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.awt.Color;
import javax.swing.JOptionPane;

public class jarstart extends Main implements Runnable {
    public static final String finalram = "4";
    String path;

    public jarstart() {
        if (!setfile.mode.equals(gamemode.getSelectedItem()) ||
            !setfile.diff.equals(difficulty.getSelectedItem()) ||
            !setfile.plear.equals(person.getText()) ||
            !setfile.hardcore == hard.getState() ||
            !setfile.reel == real.getState() ||
            !setfile.comman == command.getState() ||
            !setfile.name.equals(sername.getText())) {
            setfile.save();
        }
        
        String java = "";
        if (version.matches("\\d+\\.(?:1[0-5]|[0-9])(\\.\\d+)*"))
            java = "jre"; 
        else
            java = "jdk";
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
            ProcessBuilder processBuilder = new ProcessBuilder(path, "-Xmx" + ram.getText() + "G", "-Xms1G", "-jar", "Minecraft_" + version + "_server.jar", "nogui");
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
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(fr, version+" 버전에 맞는 자바를 찾지 못하였습니다.", "경고", JOptionPane.ERROR_MESSAGE);
            readThread = null;
        } catch (Exception ex) {
        }
    }
}