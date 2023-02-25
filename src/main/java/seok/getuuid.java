package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class getuuid {

    static int i = -1;
    static JSONArray array;

    public void getUUID(String uuid, String name) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().toString();
            System.out.println(name);
        } catch (Exception e) {
        }

    }

    public static void main(String[] args) {
        Runnable r = new search();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
    }
}

class search extends getuuid implements Runnable {

    public search() {
        try {
            File file = new File("ops.json");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = fis.readAllBytes();
            array = new JSONArray(new String(data));
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (i < array.length()-1) {
            synchronized (this) {
                i++;
            }
            JSONObject jsonObject = array.getJSONObject(i);
            new getuuid().getUUID(jsonObject.getString("uuid"), jsonObject.getString("name"));
        }
    }
}