package com.seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Color;

public class jarstart extends Main implements Runnable {
    public static final String finalram="4";
    static StringBuffer log = new StringBuffer();
    String path;

    public jarstart() {
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
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(path, "-Xmx"+ram.getText()+"G", "-Xms1G", "-jar", "Minecraft_"+longver+"_server.jar", "nogui");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            outputStream = process.getOutputStream();
            meesge.setEditable(true);
            gamemode.setEnabled(false);
            difficulty.setEnabled(false);
            person.setEnabled(false);
            real.setEnabled(false);
            command.setEnabled(false);
            sername.setEnabled(false);
            ram.setEnabled(false);
            String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("Done")) {
                            state.setForeground(Color.GREEN);
                            state.setText("서버가 정상적으로 시작되었습니다");
                        }
                        consol.append(line+"\n");
                        log.append(line+"\n");
                    }
            readThread = null;
            meesge.setEditable(false);
            gamemode.setEnabled(true);
            difficulty.setEnabled(true);
            person.setEnabled(true);
            real.setEnabled(true);
            command.setEnabled(true);
            sername.setEnabled(true);
            ram.setEnabled(true);
            state.setForeground(null);
            if (state.getText() == "서버가 정상적으로 시작되었습니다") {
                state.setText("마인크래프트 서버 관리자");
            } else {
                state.setForeground(Color.RED);
                state.setText("서버가 정상종료 되지 않았습니다!"); 
            } 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
