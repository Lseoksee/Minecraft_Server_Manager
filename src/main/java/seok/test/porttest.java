package seok.test;

import java.net.Socket;
public class porttest {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 25565)){
            System.out.println("성공적으로 연결됨");
        } catch (Exception e) {
            System.out.println("포트포워딩 필요");
        }
    }
}