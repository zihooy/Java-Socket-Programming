package server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Application {
    @Override
    public void start(Stage stage) {

        // 서버 오픈
        Thread main = new Thread(new ServerStart(5002));
        main.start();

        for (int i = 5003; i < 5007; i++) {
            Thread sub = new Thread(new ServerStart(i));
            sub.start();
        }

        //V(vertical)Box, H(horizontal)Box
        VBox root = new VBox();
        root.setPrefSize(400, 300);
        //-------------------------------------
        Button btn1 = new Button("서버 오픈");

        root.getChildren().addAll(btn1);
        //-------------------------------------
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("서버");
        stage.show();
    }

    // 접속한 클라이언트 목록


    // key:클라이언트 이름+아이피, value:데이터 송신 관련 객체 (서버 -> 클라이언트)

    public static void main(String args[]) {
        launch();


    }
}