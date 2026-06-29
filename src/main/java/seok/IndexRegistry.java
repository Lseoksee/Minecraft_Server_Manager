/** @see https://stackoverflow.com/a/35532028 에서 참고함 */

/** @note
java 11이후 getDeclaredMethod()를 이용한 private 매소드 접근은 원칙적으로 금지되어있음
따라서 레지스트리 접근에 대한 권장 방법은 reg 명령 또는, jni(jna)를 통해 접근해야함 
지금은 jvm 실행 인자에 --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED 를 추가하여 강재적으로 사용중 
*/

package seok;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

public class IndexRegistry {
    private static final int HKEY_CLASSES_ROOT = 0x80000000;
    private static final int HKEY_CURRENT_USER = 0x80000001;
    private static final int HKEY_LOCAL_MACHINE = 0x80000002;

    private static final String CLASSES_ROOT = "HKEY_CLASSES_ROOT";
    private static final String CURRENT_USER = "HKEY_CURRENT_USER";
    private static final String LOCAL_MACHINE = "HKEY_LOCAL_MACHINE";

    private static final int REG_SUCCESS = 0;
    private static final int KEY_READ = 0x20019;

    private Method regOpenKey = null;
    private Method regQueryValueEx = null;
    private Method regCloseKey = null;
    private Method regQueryInfoKey = null;
    private Method regEnumValue = null;
    private Method regEnumKeyEx = null;

    private Preferences indexRoot = null;

    private int javaVer = 0;
    private int hkey = 0;

    /**
     * 
     * @param indexRoot 레지스트리 탐색 루트
     *                  <br>
     *                  HKEY_LOCAL_MACHINE: Preferences.systemRoot
     *                  <br>
     *                  HKEY_CURRENT_USER: Preferences.userRoot()
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public IndexRegistry(Preferences indexRoot) throws NoSuchMethodException, SecurityException {
        javaVer = getJavaVersion();
        this.indexRoot = indexRoot;

        if (indexRoot == Preferences.userRoot()) {
            hkey = HKEY_CURRENT_USER;
        } else if (indexRoot == Preferences.systemRoot()) {
            hkey = HKEY_LOCAL_MACHINE;
        } else {
            throw new IllegalArgumentException("올바른 Preferences 인자를 객체를 사용");
        }

        Class<? extends Preferences> regClass = Preferences.userRoot().getClass();

        // java 11 이후로는 WindowsReg 메소드들에 인자가 int 에서 long 으로 바뀜
        if (javaVer < 11) {
            /* java 11 이전버전 */
            regOpenKey = regClass.getDeclaredMethod("WindowsRegOpenKey",
                    new Class[] { int.class, byte[].class, int.class });

            regCloseKey = regClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { int.class });

            regQueryValueEx = regClass.getDeclaredMethod("WindowsRegQueryValueEx",
                    new Class[] { int.class, byte[].class });

            regQueryInfoKey = regClass.getDeclaredMethod("WindowsRegQueryInfoKey1", new Class[] { int.class });

            regEnumValue = regClass.getDeclaredMethod("WindowsRegEnumValue",
                    new Class[] { int.class, int.class, int.class });

            regEnumKeyEx = regClass.getDeclaredMethod("WindowsRegEnumKeyEx",
                    new Class[] { int.class, int.class, int.class });

        } else {
            /* java 11 이후버전 */
            regOpenKey = regClass.getDeclaredMethod("WindowsRegOpenKey",
                    new Class[] { long.class, byte[].class, int.class });

            regCloseKey = regClass.getDeclaredMethod("WindowsRegCloseKey", new Class[] { long.class });

            regQueryValueEx = regClass.getDeclaredMethod("WindowsRegQueryValueEx",
                    new Class[] { long.class, byte[].class });

            regQueryInfoKey = regClass.getDeclaredMethod("WindowsRegQueryInfoKey1", new Class[] { long.class });

            regEnumValue = regClass.getDeclaredMethod("WindowsRegEnumValue",
                    new Class[] { long.class, int.class, int.class });

            regEnumKeyEx = regClass.getDeclaredMethod("WindowsRegEnumKeyEx",
                    new Class[] { long.class, int.class, int.class });
        }

        regOpenKey.setAccessible(true);
        regCloseKey.setAccessible(true);
        regQueryValueEx.setAccessible(true);
        regQueryInfoKey.setAccessible(true);
        regEnumValue.setAccessible(true);
        regEnumKeyEx.setAccessible(true);
    }

    /**
     * 해당 경로의 레지스트리 하위경로를 탐색합니다.
     * 
     * @param path 경로
     * @return path에 위차한 하위 레지스트리
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public List<String> getRegistryDir(String path)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (javaVer < 11) {
            /* java 11 이전버전 */
            return __getRegistryDirOld(path);
        } else {
            /* java 11 이후버전 */
            return __getRegistryDirNew(path);
        }
    }

    /**
     * 
     * @param path 해당 경로에 레지스트리를 얻습니다.
     * @return 키-값 쌍에 레지스트리 목록
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public HashMap<String, String> getRegistryMapForPath(String path)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (javaVer < 11) {
            /* java 11 이전버전 */
            return __getRegistryMapForPathOld(path);
        } else {
            /* java 11 이후버전 */
            return __getRegistryMapForPathNew(path);
        }
    }

    private List<String> __getRegistryDirOld(String path)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<String> results = new ArrayList<String>();
        int[] handles = (int[]) regOpenKey.invoke(indexRoot,
                new Object[] { new Integer(hkey), toCstr(path), new Integer(KEY_READ) });
        if (handles[1] != REG_SUCCESS)
            throw new IllegalArgumentException(
                    "레지스트리를 찾을 수 없음: '" + getParentKey(hkey) + "\\" + path + "'");

        int[] info = (int[]) regQueryInfoKey.invoke(indexRoot, new Object[] { new Integer(handles[0]) });
        int count = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj,
                             // confirmed by Petrucio
        int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] nameByte = (byte[]) regEnumKeyEx.invoke(indexRoot,
                    new Object[] { new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1) });
            results.add(parseValue(nameByte));
        }
        regCloseKey.invoke(indexRoot, new Object[] { new Integer(handles[0]) });
        return results;
    }

    private List<String> __getRegistryDirNew(String path)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<String> results = new ArrayList<String>();
        long[] handles = (long[]) regOpenKey.invoke(indexRoot,
                new Object[] { new Long(hkey), toCstr(path), new Integer(KEY_READ) });
        if (handles[1] != REG_SUCCESS)
            throw new IllegalArgumentException(
                    "레지스트리를 찾을 수 없음: '" + getParentKey(hkey) + "\\" + path + "'");

        long[] info = (long[]) regQueryInfoKey.invoke(indexRoot, new Object[] { handles[0] });
        long count = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj,
                              // confirmed by Petrucio
        long maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            byte[] nameByte = (byte[]) regEnumKeyEx.invoke(indexRoot,
                    new Object[] { new Long(handles[0]), new Integer(index), (int) maxlen + 1 });
            results.add(parseValue(nameByte));
        }
        regCloseKey.invoke(indexRoot, new Object[] { new Long(handles[0]) });
        return results;
    }

    private HashMap<String, String> __getRegistryMapForPathOld(String path)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HashMap<String, String> results = new HashMap<String, String>();
        int[] handles = (int[]) regOpenKey.invoke(indexRoot,
                new Object[] { new Integer(hkey), toCstr(path), new Integer(KEY_READ) });
        if (handles[1] != REG_SUCCESS)
            throw new IllegalArgumentException(
                    "레지스트리를 찾을 수 없음: '" + getParentKey(hkey) + "\\" + path + "'");

        int[] info = (int[]) regQueryInfoKey.invoke(indexRoot, new Object[] { new Integer(handles[0]) });
        int count = info[2]; // Fixed: info[0] was being used here
        int maxlen = info[4]; // while info[3] was being used here, causing wrong results
        for (int index = 0; index < count; index++) {
            byte[] keyByte = (byte[]) regEnumValue.invoke(indexRoot,
                    new Object[] { new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1) });
            String key = parseValue(keyByte);

            // key 값으로 value 찾기
            byte[] valueByte = (byte[]) regQueryValueEx.invoke(indexRoot,
                    new Object[] { new Integer(handles[0]), toCstr(key) });
            String value = parseValue(valueByte);
            results.put(key, value);
        }
        regCloseKey.invoke(indexRoot, new Object[] { new Integer(handles[0]) });
        return results;
    }

    private HashMap<String, String> __getRegistryMapForPathNew(String path)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HashMap<String, String> results = new HashMap<String, String>();
        long[] handles = (long[]) regOpenKey.invoke(indexRoot,
                new Object[] { new Long(hkey), toCstr(path), new Integer(KEY_READ) });
        if (handles[1] != REG_SUCCESS)
            throw new IllegalArgumentException(
                    "레지스트리를 찾을 수 없음: '" + getParentKey(hkey) + "\\" + path + "'");

        long[] info = (long[]) regQueryInfoKey.invoke(indexRoot, new Object[] { handles[0] });
        long count = info[2]; // Fixed: info[0] was being used here
        long maxlen = info[4]; // while info[3] was being used here, causing wrong results
        for (int index = 0; index < count; index++) {
            byte[] keyByte = (byte[]) regEnumValue.invoke(indexRoot,
                    new Object[] { handles[0], new Integer(index), (int) maxlen + 1 });
            String key = parseValue(keyByte);

            // key 값으로 value 찾기
            byte[] valueByte = (byte[]) regQueryValueEx.invoke(indexRoot,
                    new Object[] { handles[0], toCstr(key) });
            String value = parseValue(valueByte);
            results.put(key, value);
        }
        regCloseKey.invoke(indexRoot, new Object[] { new Long(handles[0]) });
        return results;
    }

    private static String parseValue(byte buf[]) {
        if (buf == null)
            return null;
        String ret = new String(buf);
        if (ret.charAt(ret.length() - 1) == '\0')
            return ret.substring(0, ret.length() - 1);
        return ret;
    }

    private static byte[] toCstr(String str) {
        if (str == null)
            str = "";
        return (str += "\0").getBytes();
    }

    private static String getParentKey(int hkey) {
        if (hkey == HKEY_CLASSES_ROOT)
            return CLASSES_ROOT;
        else if (hkey == HKEY_CURRENT_USER)
            return CURRENT_USER;
        else if (hkey == HKEY_LOCAL_MACHINE)
            return LOCAL_MACHINE;
        return null;
    }

    private static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }
}
