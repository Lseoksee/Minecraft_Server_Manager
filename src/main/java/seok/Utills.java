package seok;

import java.io.FileInputStream;
import java.io.IOException;

import org.mozilla.universalchardet.UniversalDetector;

public class Utills {

    /**
     * 해당 파일에 인코딩 정보를 얻습니다.
     * @param fileName 파일 경로
     * @return 인코딩정보 (ex. UTF-8, EUC-KR...)
     * @throws IOException
     */
    public static String CheckFileEncoding(String fileName) throws IOException {
        byte buf[] = new byte[1024];

        FileInputStream fileInputStream = new FileInputStream(fileName);
        UniversalDetector detector = new UniversalDetector(null);

        int nRead = 0;
        while ((nRead= fileInputStream.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nRead);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();

        detector.reset();
        fileInputStream.close();

        return encoding;
    }
}
