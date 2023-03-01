package seok;

import java.io.BufferedReader;
import java.io.File;
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

    public static void zip(String serch, String zip) throws Exception {
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
            if (as.indexOf(".jar") != -1) {
                twocount++;
                if (as.matches("Minecraft_\\d+\\.\\d+(\\.\\d+)*_server\\.jar")) {
                    filename = as;
                    version = as.split("_")[1];
                    count++;
                    twocount = 1;
                    break;
                } else {
                    filename = as;
                }
            }
        }
        if (twocount == 0) {
            JOptionPane.showMessageDialog(fr, "버킷 jar 파일을 찾을 수 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        if (twocount > 1) {
            while (true) {
                filename = FileClass.filedia(fr, "*.jar", "버킷 jar파일을 선택하시오", 0, false);
                if (filename == null) {
                    System.exit(0);
                }
                if (new File("./" + filename).exists())
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
                    version = splet[1];
                    new File(filename).renameTo(new File("Minecraft_" + version + "_server.jar"));
                    filename = "Minecraft_" + version + "_server";
                    break;
                }
            }
            reader.close();
            return true;
        } catch (Exception e) {
            return newver();
        }
    }

    public boolean newver() {
        try {
            reset();
            zip("version.json", filename);
            byte str[] = stream.readAllBytes();
            JSONObject jsonObject = new JSONObject(new String(str));
            zipFile.close();
            stream.close();
            version = jsonObject.getString("id");
            new File(filename).renameTo(new File("Minecraft_" + version + "_server.jar"));
            filename = "Minecraft_" + version + "_server";
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void reset() {
        try {
            zipFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}