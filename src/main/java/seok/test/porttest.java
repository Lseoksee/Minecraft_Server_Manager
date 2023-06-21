package seok.test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class porttest {
        public static void main(String[] args) {
        try {
            ServerSocket svs = new ServerSocket(25565); //테스트로 오픈할 포트
            new Thread(new isopen()).start();
            svs.accept();
            svs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class isopen implements Runnable {

    @Override
    public void run() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("58.232.252.164", 25565), 2000);   
            //오픈된 포트를 외부IP주소로 연결하여 포트포워딩이 정상적으로 되었는지를 확인
            System.out.println("성공적으로 연결됨");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("포트포워딩 필요");
        }
    }
}