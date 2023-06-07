package seok;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.json.JSONObject;

public class CheckVer extends Main implements Runnable {

    @Override
    public void run() {
        /* 깃허브 api를 사용한 버전확인 방법 */
        try {
            URL url = new URL("https://api.github.com/repos/Lseoksee/Minecraft_Server_Manager/releases/latest");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            // httprequset 연결부분

            InputStream is = huc.getInputStream();
            JSONObject jsonObject = new JSONObject(
                    new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n")));
            String onver = jsonObject.getString("tag_name");

            if (!onver.equals(Main.RESVER)) {
                String mesege = "업데이트가 있습니다!\n현재버전: " + Main.RESVER + "\n최신버전: " + onver;
                int state = JOptionPane.showConfirmDialog(fr, mesege, "업데이트 알림", JOptionPane.OK_CANCEL_OPTION);
                if (state == 0) {
                    Desktop.getDesktop().browse(new URI(jsonObject.getString("html_url")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}