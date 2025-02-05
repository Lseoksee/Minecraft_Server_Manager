package seok;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seok.UI.MainUI;
import seok.UI.PlayerOptonUI;
import seok.dto.SearchJavaDTO;

import java.awt.Color;

public class Jarstart extends MainUI implements Runnable {
    public static final int FINALRAM = 4;
    String path;

    public Jarstart() {
        try {
            List<SearchJavaDTO> javaList = Utills.getSystemJava();

            // INFO: ~1.16.x
            if (jarver.version.matches("\\d+\\.(?:1[0-6]|[0-9])(\\.\\d+)*")) {
                SearchJavaDTO java = choiceJavaVersion(javaList, 8, 11);
                path = java.getPath() + "\\bin\\java";
                System.out.println("선택된 자바 버전: " + java.getVersion());
            // INFO: 1.17.x ~ 1.18.x
            } else if (jarver.version.matches("\\d+\\.(?:1[7-8]|[0-9])(\\.\\d+)*")) {
                SearchJavaDTO java = choiceJavaVersion(javaList, 16, 17);
                path = java.getPath() + "\\bin\\java";
                System.out.println("선택된 자바 버전: " + java.getVersion());
            // INFO: 1.19.x ~
            } else {
                SearchJavaDTO java = choiceJavaVersion(javaList, 17, 0);
                path = java.getPath() + "\\bin\\java";
                System.out.println("선택된 자바 버전: " + java.getVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
            path = "java";
        }
    }
 
    public void runServer() {
        readThread = new Thread(this);
        readThread.start();
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(path,
                    "-Xmx" + ram.getValue() + "G", "-Xms" + ram.getValue() + "G",
                    "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8",
                    "-jar", "Minecraft_" + jarver.version + "_server.jar",
                    "nogui");
            processBuilder.redirectErrorStream(true); // 에러스트림 인풋스트림 병합
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

            // 버킷 실행 스트림이 UTF-8 이므로 스트림에 쓸때도 UTF-8 로 보내줘야함
            // outputStream.write(message.getBytes("UTF-8"));
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
                if (line.matches(
                        "^\\[.*\\]: <(.*)>(.*)|^\\[.*\\]:( \\[Not Secure\\])? \\[Server\\] (.*)|^\\[.*\\]:(.*)issued server command:(.*)")) {
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

    /**
     * 검색된 Java 리스트 중에서 조건에 맞는 Java를 선택합니다.
     * 
     * @param javaList 검색된 Java 리스트
     * @param minVer  최소 java 버전
     * @param maxVer  최대 java 버전 (0이면 무제한)
     * 
     */
    private SearchJavaDTO choiceJavaVersion(List<SearchJavaDTO> javaList, int minVer, int maxVer) {
        SearchJavaDTO prev = javaList.get(0);

        for (int i = 1; i < javaList.size(); i++) {
            SearchJavaDTO now = javaList.get(i);

            ArrayList<String> prevVersion = new ArrayList<>(Arrays.asList(prev.getVersion().split("_|\\.")));
            ArrayList<String> nowVersion = new ArrayList<>(Arrays.asList(now.getVersion().split("_|\\.")));

            // java 8 버전에서 표기법이 1.8 이런식이라서 앞자리 1을 빼버림
            if (prevVersion.get(0).equals("1")) {
                prevVersion.remove(0);
            }
            if (nowVersion.get(0).equals("1")) {
                nowVersion.remove(0);
            }

            // 이전 java와 현재 순회하는 java가 조건에 만족하는 지를 확인
            // for-loop를 돌기 전 무조건 0번 인덱스를 선택하기 때문에 조건에 맞지 않는 java를 선택할 여지가 생기므로
            // 이전에 선택된 java에 대해서도 검증을 도입
            int prevVer = Integer.parseInt(prevVersion.get(0));
            int nowVer = Integer.parseInt(nowVersion.get(0));
            if (prevVer < minVer || (prevVer > maxVer && maxVer != 0)) {
                prev = now;
                continue;
            } else if (nowVer < minVer ||  (nowVer > maxVer && maxVer != 0)) {
                continue;
            }

            // 버전과 상관없이 x32 -> x64로 넘어가는 경우에는 무조건 선택
            if (prev.getArch().equals("x32") && now.getArch().equals("x64")) {
                prev = now;
                continue;
            }

            // 이전에 선택된 java보다 현재 순회되고 있는 java에 버전이 더 높은경우 선택
            if (isUpJavaVersion(0, prevVersion, nowVersion)) {
                prev = now;
            }
        }

        return prev;
    }

    /** 자바 버전 비교 함수 */
    private boolean isUpJavaVersion(int token, ArrayList<String> prev, ArrayList<String> now) {
        int token_prev = 0;
        int token_now = 0;

        if (token >= prev.size() && token >= now.size()) {
            return false;
        }

        if (token < prev.size()) {
            token_prev = Integer.parseInt(prev.get(token));
        }
        if (token < now.size()) {
            token_now = Integer.parseInt(now.get(token));
        }

        if (token_prev > token_now) {
            return false;
        }
        if (token_prev < token_now) {
            return true;
        }

        return isUpJavaVersion(token + 1, prev, now);
    }
}
