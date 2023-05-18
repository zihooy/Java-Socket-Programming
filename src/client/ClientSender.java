package client;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class ClientSender extends Thread {
    Socket socket;
    DataOutputStream out;
    String name;

    // (서버와 송신하는) 스레드 객체의 멤버 초기화 
    ClientSender(Socket socket, String name) {
        this.socket = socket;
        try {
            this.out = new DataOutputStream(socket.getOutputStream()); // 데이터 송신을 편하게 하도록 도와주는 객체 (클라이언트 -> 서버) 
            this.name = name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            // 처음에는 name 전송 
            if (out != null) {
                out.writeUTF(name);
            }
            // 이후에는 다음을 반복 (사용자의 입력 대기하다가 입력 받으면 저장 -> 입력 메시지를 서버로 전송)
            while (out != null) {
                String msg = scanner.nextLine();
                out.writeUTF("[" + name + "] " + msg);
            }
        } catch (IOException e) {
        }
    }
}