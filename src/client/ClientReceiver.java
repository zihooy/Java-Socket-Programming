package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiver extends Thread {
    Socket socket;
    DataInputStream in;
    Client client;

    // (서버와 수신하는) 스레드 객체의 멤버 초기화
    ClientReceiver(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        try {
            in = new DataInputStream(socket.getInputStream());  // 데이터 수신을 편하게 하도록 도와주는 객체 (서버 -> 클라이언트)
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        // 반복 (수신 대기하다가 메시지 받으면 저장 -> 출력)
        while (in != null) {
            try {
                String msg = in.readUTF();
                System.out.println(msg);
                client.textArea.appendText(msg + '\n');
            } catch (IOException e) {
            }
        }
    }
}