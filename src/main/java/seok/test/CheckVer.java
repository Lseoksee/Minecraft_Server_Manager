package seok.test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class CheckVer {
    public static void main(String[] args) {
        /* 깃허브 api를 사용한 버전확인 방법 */
        try {
            URL url = new URL("https://api.github.com/repos/Lseoksee/spicetify-cli-korean/releases/latest");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            // httprequset 연결부분

            InputStream is = huc.getInputStream();
            JSONObject jsonObject = new JSONObject(new String(is.readAllBytes(), "utf-8"));
            System.out.println(jsonObject.getString("tag_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}