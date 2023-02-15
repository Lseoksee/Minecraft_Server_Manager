package com.seok;

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

    public void getUUID(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            String alive = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().toString();
            if (alive == null) {
                System.out.println("복돌");
            } else {
                System.out.println(alive);
            }
        } catch (Exception e) {
            System.out.println("복돌");
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

class search implements Runnable {
    static int i = -1;
    static JSONArray array;

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
        while (i < array.length()-Thread.activeCount()+1) {
            synchronized (this) {
                i++;
            }
            JSONObject jsonObject = array.getJSONObject(i);
            new getuuid().getUUID((String) jsonObject.get("name")); 
        }
    }
}