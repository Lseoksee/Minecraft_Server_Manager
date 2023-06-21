package seok.test;

import java.net.InetSocketAddress;
import java.net.Socket;
public class porttest {
    public static void main(String[] args) {
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress("58.232.252.163", 25565), 2000);
            System.out.println("성공적으로 연결됨");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("포트포워딩 필요");
        }
    }
}