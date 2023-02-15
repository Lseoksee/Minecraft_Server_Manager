package com.seok.test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class porttest {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 25565)){
            System.out.println("성공적으로 연결됨");
        } catch (ConnectException e) {
            System.out.println("포트포워딩 필요");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
