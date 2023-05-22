package seok;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;

import org.json.JSONObject;

public class Findjar extends Main {

    String filename; // jar 파일이름
    String version; // .포함 버전

    private ZipFile zipFile;
    private ZipEntry entry;
    private InputStream stream;

    public Findjar() {
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
            oldver();
        }
    }

    private void oldver() {
        try {
            zip("patch.properties", filename);
            Properties properties = new Properties();
            properties.load(stream);
            version = properties.getProperty("version");
            zipFile.close();
            stream.close();

            new File(filename).renameTo(new File("Minecraft_" + version + "_server.jar"));
            filename = "Minecraft_" + version + "_server";

        } catch (Exception e) {
            newver();
        }
    }

    private void newver() {
        try {
            zipFile.close();
            zip("version.json", filename);
            JSONObject jsonObject = new JSONObject(
                    new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n")));
            // 1.8 에서 inputstream에 readallbyte 메소드가 없음
            zipFile.close();
            stream.close();
            version = jsonObject.getString("id");
            new File(filename).renameTo(new File("Minecraft_" + version + "_server.jar"));
            filename = "Minecraft_" + version + "_server";
        } catch (Exception e) {
            try {
                zipFile.close();
                stream.close();
            } catch (Exception ex) {
            }
        }
    }

    private void zip(String serch, String zip) throws Exception {
        zipFile = new ZipFile(zip);
        entry = zipFile.getEntry(serch);
        stream = zipFile.getInputStream(entry);
    }
}
