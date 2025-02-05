package seok;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.mozilla.universalchardet.UniversalDetector;

import seok.dto.SearchJavaDTO;
import seok.lib.WinRegistry;

public class Utills {

    /**
     * 해당 파일에 인코딩 정보를 얻습니다.
     * 
     * @param fileName 파일 경로
     * @return 인코딩정보 (ex. UTF-8, EUC-KR...)
     * @throws IOException
     */
    public static String CheckFileEncoding(String fileName) throws IOException {
        byte buf[] = new byte[1024];

        FileInputStream fileInputStream = new FileInputStream(fileName);
        UniversalDetector detector = new UniversalDetector(null);

        int nRead = 0;
        while ((nRead = fileInputStream.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nRead);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();

        detector.reset();
        fileInputStream.close();

        return encoding;
    }

    /**
     * 시스템의 설치되어있는 모든 JAVA 정보를 얻습니다. <br>
     * </br>
     * 래지스트리를 탐색하는 방식이라 포더블로 설치한 경우에는 탐지가 불가합니다.
     * 
     * @return <code>SearchJavaDTO</code>
     * 
     * @throws FileNotFoundException
     *                               시스템 내에 자바를 감지할 수 없음
     * 
     * @throws Exception
     *                               그 외 애러
     */
    public static List<SearchJavaDTO> getSystemJava()
            throws FileNotFoundException, Exception {
        final String jreRootPath = "SOFTWARE\\JavaSoft\\Java Runtime Environment\\";
        final String jdkRootPath = "SOFTWARE\\JavaSoft\\JDK\\";

        List<String> jreRoot = WinRegistry.subKeysForPath(WinRegistry.HKEY_LOCAL_MACHINE, jreRootPath);
        List<String> jdkRoot = WinRegistry.subKeysForPath(WinRegistry.HKEY_LOCAL_MACHINE, jdkRootPath);
        List<SearchJavaDTO> javaList = new ArrayList<>();

        jreRoot.forEach((item) -> {
            try {
                SearchJavaDTO javaDTO = new SearchJavaDTO();
                Map<String, String> jreValues = WinRegistry.valuesForPath(WinRegistry.HKEY_LOCAL_MACHINE,
                        jreRootPath + item);

                // 해당 경로에 java.exe 파일이 없으면 무시
                String path = jreValues.get("JavaHome");
                if (!new File(path + "/bin/java.exe").exists()) {
                    return;
                }

                // JAVA 컴파일 정보를 담은 파일
                FileInputStream releaseFile = new FileInputStream(path + "/release");
                Properties release = new Properties();
                release.load(releaseFile);
                releaseFile.close();

                // release 파일에서 OS_ARCH 정보 읽어 오기 (jre=amd64, jdk=x86_64)
                String arch = (String) release.get("OS_ARCH");

                javaDTO.setType("jre");
                javaDTO.setVersion(item);
                javaDTO.setPath(jreValues.get("JavaHome"));

                if (arch.equals("\"amd64\"") || arch.equals("\"x86_64\"")) {
                    javaDTO.setArch("x64");
                } else {
                    javaDTO.setArch("x32");
                }

                javaList.add(javaDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        jdkRoot.forEach((item) -> {
            try {
                SearchJavaDTO javaDTO = new SearchJavaDTO();
                Map<String, String> jdkValues = WinRegistry.valuesForPath(WinRegistry.HKEY_LOCAL_MACHINE,
                        jdkRootPath + item);

                // 해당 경로에 java.exe 파일이 없으면 무시
                String path = jdkValues.get("JavaHome");
                if (!new File(path + "/bin/java.exe").exists()) {
                    return;
                }

                // JAVA 컴파일 정보를 담은 파일
                FileInputStream releaseFile = new FileInputStream(path + "/release");
                Properties release = new Properties();
                release.load(releaseFile);
                releaseFile.close();

                // release 파일에서 OS_ARCH 정보 읽어 오기
                String arch = (String) release.get("OS_ARCH");

                javaDTO.setType("jre");
                javaDTO.setVersion(item);
                javaDTO.setPath(jdkValues.get("JavaHome"));

                if (arch.equals("\"amd64\"") || arch.equals("\"x86_64\"")) {
                    javaDTO.setArch("x64");
                } else {
                    javaDTO.setArch("x32");
                }

                javaList.add(javaDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (javaList.size() == 0) {
            throw new FileNotFoundException("시스템에 설치된 자바를 탐지할 수 없습니다.");
        }

        return javaList;
    }
}
