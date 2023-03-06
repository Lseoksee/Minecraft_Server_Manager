package seok.test;

public class regex {
    public static void main(String[] args) {
        //정규식 테스트
        String str = "[10:59:57 INFO]: [Not Secure] [Server] 야";
        System.out.println(str.matches("^\\[.*\\]: <(.*)>(.*)|^\\[.*\\]:( \\[Not Secure\\])? \\[Server\\] (.*)|^\\[.*\\]:(.*)issued server command:(.*)"));
    }
}
