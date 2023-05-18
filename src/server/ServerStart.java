package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ServerStart extends Thread {

    ConcurrentHashMap clients = new ConcurrentHashMap();

    int port;

    public ServerStart(int port) {
        this.port = port;
    }

    public void run() {
        startServer();
    }

    // 서버 오픈
    public void startServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            // 서버 컴퓨터의 5001 포트에서 채팅 프로그램 동작
            serverSocket = new ServerSocket(port);
            System.out.println("[" + port + "] 서버가 시작되었습니다.");

            // 반복 (접속할 클라이언트 대기 -> 접속한 클라이언트에 대한 수신 스레드 생성)
            while (true) {
                // 대기하다가 연결된 클라이언트가 있으면 해당 클라이언트에 대한 소켓을 생성함
                socket = serverSocket.accept();
                System.out.println(port + ": [" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");
                // 해당 클라이언트의 메시지를 수신하는 스레드 생성
                ServerReceiver thread = new ServerReceiver(this, socket, port);
                // 스레드 동작
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 모든 클라이언트에게 송신
    void sendToAll(String msg) {
        Iterator it = clients.keySet().iterator();

        // clients에 저장된 key값이 있으면 반복
        while (it.hasNext()) {
            try {
                // 현재 클라이언트와 송신하기 위해서 out 객체 가져오기
                DataOutputStream out = (DataOutputStream) clients.get(it.next());
                // 현재 클라이언트에게 송신
                out.writeUTF(msg);
            } catch (IOException e) {
            }
        }
    }

    /*
    void sendRoomInfo() {
        int[] array = {1, 2, 3, 4, 5};
        Iterator it = clients.keySet().iterator();
        // clients에 저장된 key값이 있으면 반복
        while (it.hasNext()) {
            try {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                // 현재 클라이언트와 송신하기 위해서 out 객체 가져오기
                // 현재 클라이언트에게 송신
                DataOutputStream out = (DataOutputStream) clients.get(it.next());
                out.writeInt(array.length);
                // int 배열의 각 요소 전송
                for (int i = 0; i < array.length; i++) {
                    out.writeInt(array[i]);
                }

                // byte 배열로 변환
                byte[] byteArray = byteStream.toByteArray();

                // byteArray를 전송하는 로직 작성

                // 전송 완료 후 스트림 닫기
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     */
}