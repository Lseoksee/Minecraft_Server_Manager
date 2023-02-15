package com.seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import org.json.JSONObject;

public class findjar extends Main {

    static ZipFile zipFile;
    static ZipEntry entry;
    static InputStream stream;

    public static void zip(String serch, String zip) throws IOException {
        zipFile = new ZipFile(zip);
        entry = zipFile.getEntry(serch);
        stream = zipFile.getInputStream(entry);
    }

    public boolean searchjar() {
        File fi = new File("./"); // 컴파일시 위치 변경할것!
        String list[] = fi.list();
        int count = 0;
        int twocount = 0;
        for (String as : list) {
            String text[] = as.split("_");
            if (as.contains(".jar")) {
                twocount++;
                if (text[0].equals("Minecraft")) {
                    filename = as;
                    longver = as.split("_")[1];
                    realver = Integer.parseInt(longver.replace(".", ""));
                    count++;
                    twocount = 1;
                    break;
                } else {
                    filename = as;
                }
            }
        }
        if (twocount == 0) {
            JOptionPane.showMessageDialog(null, "버킷 jar 파일을 찾을 수 없습니다.", "경고", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        if (twocount > 1) {
            while (true) {
                filename = FileClass.filedia(fr, "*.jar");
                if (filename == null) {
                    System.exit(0);
                }
                if (new File("./"+filename).exists()) 
                    break;
                else
                    continue;
            }
        }
        if (count == 0) {
            return oldver();
        }
        return true;
    }

    public boolean oldver() {
        try {
            zip("patch.properties", filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                String splet[] = line.split("=");
                if (splet[0].equals("version")) {
                    zipFile.close();
                    stream.close();
                    longver = splet[1];
                    new File(filename).renameTo(new File("Minecraft_"+longver+"_server.jar"));
                    filename = "Minecraft_"+longver+"_server";
                    realver = Integer.parseInt(longver.replace(".", ""));
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            return newver();
        } catch (NullPointerException e) {
            return newver();
        }
        return true;
    }

    public boolean newver() {
        try {
            reset();
            zip("version.json", filename);
            byte str[] = stream.readAllBytes();
            JSONObject jsonObject = new JSONObject(new String(str));
            zipFile.close();
            stream.close();
            longver = jsonObject.getString("id");
            new File(filename).renameTo(new File("Minecraft_"+longver+"_server.jar"));
            filename = "Minecraft_"+longver+"_server";
            realver = Integer.parseInt(longver.replace(".", ""));
        } catch (IOException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    
    //서버 버전 인식불가 처리
    public static void seljar() {
        try {
            findjar.reset();
            longver = jarkey.getText();
            new File(filename).renameTo(new File("Minecraft_"+longver+"_server.jar"));
            filename = "Minecraft_"+longver+"_server";
            realver = Integer.parseInt(longver.replace(".", ""));
            fr.removeAll();
            maingui();
        } catch (NumberFormatException e) {
            jarkey.setText(null);
        }
    }

    public static void reset() {
        try {
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }
}
