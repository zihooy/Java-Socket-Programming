package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import name.Name;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.*;
import javax.imageio.ImageIO;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

public class Client extends Application {
    String serverIp = "192.168.0.83";
    Socket socket;
    public TextArea textArea;
    public TextField userName;

    Button sendButton;
    Button randomButton;
    Button chatbotButton;
    Button voiceButton;
    Button captureButton;
    Button exit;
    TextField input;
    Button room1;
    Button room2;
    Button room3;
    Button room4;
    Button room5;
    Tab tab1;
    Tab tab2;
    Tab tab3;
    BorderPane thirdTab;
    TabPane tabPane;

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));

        //첫번째 tab
        BorderPane firstTab = new BorderPane();


        Name randomProfile = Name.randomName();
        String name = randomProfile.name().toString();
        String imageUrl = randomProfile.getImg();

        userName = new TextField(Name.randomName().name().toString());
        userName.setPromptText("닉네임을 입력하세요.");
        userName.setStyle("-fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0;-fx-control-inner-background:#3A1D1D;");

        HBox userNameh = new HBox(userName);
        userNameh.setAlignment(Pos.CENTER);

        userNameh.prefWidthProperty().bind(stage.widthProperty().multiply(0.7));
        HBox.setMargin(userNameh, new Insets(50, 0, 0, 50));

        Circle cir2 = new Circle(250, 250, 120);
        cir2.setStroke(Color.GREY);
        //String imageUrl = userName.getText();
        Image im = new Image(imageUrl, false);
        cir2.setFill(new ImagePattern(im));
        cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.BLACK));

        VBox info = new VBox(cir2, userNameh);
        info.setAlignment(Pos.CENTER);


        firstTab.setCenter(info);

        //두번째 tab: 방 목록
        BorderPane secondTab = new BorderPane();

        room1 = new Button("[포스코DX] 5기 공지방");
        room1.prefWidthProperty().bind(stage.widthProperty().multiply(1.0));
        room1.setStyle("-fx-background-color: #3A1D1D; -fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0;-fx-alignment: LEFT;");
        //room1.setMargin(new Insets(100, 0, 0, 0));

        room2 = new Button("[포스코DX] 2조 Study 채팅방");
        room2.prefWidthProperty().bind(stage.widthProperty().multiply(1.0));
        room2.setStyle("-fx-background-color: #3A1D1D; -fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0; -fx-alignment: LEFT;");

        room3 = new Button("[포스코DX] 5조 채팅 과제방");
        room3.prefWidthProperty().bind(stage.widthProperty().multiply(1.0));
        room3.setStyle("-fx-background-color: #3A1D1D; -fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0; -fx-alignment: LEFT;");

        room4 = new Button("[포스코DX] Python Study방");
        room4.prefWidthProperty().bind(stage.widthProperty().multiply(1.0));
        room4.setStyle("-fx-background-color: #3A1D1D; -fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0; -fx-alignment: LEFT;");

        room5 = new Button("[포스코DX] Java Study방");
        room5.prefWidthProperty().bind(stage.widthProperty().multiply(1.0));
        room5.setStyle("-fx-background-color: #3A1D1D; -fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0; -fx-alignment: LEFT;");

        VBox roomList = new VBox(room1, room2, room3, room4, room5);

        secondTab.setCenter(roomList);

        //세번째 tab: 채팅
        textArea = new TextArea();
        textArea.setEditable(false);
//        root.setCenter(textArea);
        root.setStyle("-fx-background-color:#9bbbd4; -fx-font-family: NanumSquare Regular; ");
        textArea.setStyle("-fx-control-inner-background:#9bbbd4; -fx-font-family: NanumSquare Regular; -fx-background-insets: 0;" +
                " -fx-background-color: transparent, transparent, transparent, transparent;");

        textArea.setPrefHeight(400);
        input = new TextField();
        input.prefWidthProperty().bind(stage.widthProperty().multiply(0.85));

        //input.setPrefWidth(Double.MAX_VALUE)
        input.setOnAction(event -> {
//            send(userName.getText() + ": " + input.getText() + "\n");
//            input.setText("");
//            input.requestFocus();
        });
        sendButton = new Button("보내기");
        sendButton.setStyle("-fx-background-color:#F7E600; -fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0;");

        randomButton = new Button("랜덤 뽑기");
        randomButton.setStyle("-fx-background-color:#556677; -fx-text-fill: white; -fx-start-margin: 20;-fx-end-margin: 20; ");

        chatbotButton = new Button("챗봇 가이드");
        chatbotButton.setStyle("-fx-background-color:#556677; -fx-text-fill: white; -fx-start-margin: 20;-fx-end-margin: 20; ");

        voiceButton = new Button("음성인식");
        voiceButton.setStyle("-fx-background-color:#556677; -fx-text-fill: white; -fx-start-margin: 20;-fx-end-margin: 20; ");

        captureButton = new Button("캡처");
        captureButton.setStyle("-fx-background-color:#556677; -fx-text-fill: white; -fx-start-margin: 20;-fx-end-margin: 20; ");

        exit = new Button("나가기");
        exit.setStyle("-fx-background-color:#556677; -fx-text-fill: white; -fx-start-margin: 20;-fx-end-margin: 20; ");

        HBox functionContainer = new HBox(chatbotButton, randomButton, voiceButton, captureButton, exit);

       // HBox functionContainer = new HBox(chatbotButton, randomButton, voiceButton, captureButton);
        functionContainer.setSpacing(5);
        HBox inputContainer = new HBox(input, sendButton);
        VBox sendContainer = new VBox(functionContainer, inputContainer);
        thirdTab = new BorderPane();
        thirdTab.setCenter(textArea);
        thirdTab.setBottom(sendContainer);


        //Tab 생성
        tabPane = new TabPane();

        //tabPane.setStyle("-fx-backgroundbackground-color: #3A1D1D; -fx-text-fill: white; -fx-start-margin: 0;-fx-end-margin: 0;");

        tab1 = new Tab("프로필", firstTab);
        tab1.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-color: white; -fx-start-margin: 0;-fx-end-margin: 0;");
        tab2 = new Tab("방 선택", secondTab);
        tab2.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-color: white; -fx-start-margin: 0;-fx-end-margin: 0;");

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
//        tabPane.getTabs().add(tab3);

        VBox vBox = new VBox(tabPane);
        root.setTop(vBox);

        //root.setBottom(pane);
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle(" [카카오톡] ");
        stage.setScene(scene);

//        stage.setOnCloseRequest(event -> stopClient());

        stage.show();
        setOnAction(stage);
    }

    private void setOnAction(Stage stage) {
        exit.setOnAction(actionEvent -> {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            tabPane.getTabs().remove(2);
        });
        captureButton.setOnAction(event -> {
            captureScreen(stage);
        });

        sendButton.setOnAction(event -> {
            Thread sender = new Thread(new ClientSender(socket, "[" + userName.getText() + "] " + input.getText()));
            sender.start();
            input.setText("");
            input.requestFocus();
        });

        randomButton.setOnAction(event -> {

            Thread sender = new Thread(new ClientSender(socket, "[" + "랜덤뽑기" + "] " + input.getText()));
            sender.start();
            input.setText("");
            input.requestFocus();

        });
        chatbotButton.setOnAction(event -> {

            Thread sender = new Thread(new ClientSender(socket, "[" + "챗봇가이드" + "] " + input.getText()));
            sender.start();
            input.setText("");
            input.requestFocus();

        });

        voiceButton.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(stage);
            System.out.println(file);
            String result = speachToText(String.valueOf(file));
            Thread sender = new Thread(new ClientSender(socket, "[" + userName.getText() + "] (음성인식) " + result));
            sender.start();
            input.setText("");
            input.requestFocus();

        });

        room1.setOnAction(actionEvent -> {
            textArea.clear();
            if (tabPane.getTabs().size() > 2)
                tabPane.getTabs().remove(2);
            tab3 = new Tab(room1.getText(), thirdTab);
            tab3.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-color: white; -fx-start-margin: 0;-fx-end-margin: 0;");

            tabPane.getTabs().add(tab3);
            connect(5002);
        });

        room2.setOnAction(actionEvent -> {
            textArea.clear();
            if (tabPane.getTabs().size() > 2)
                tabPane.getTabs().remove(2);
            tab3 = new Tab(room2.getText(), thirdTab);
            tab3.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-color: white; -fx-start-margin: 0;-fx-end-margin: 0;");
            tabPane.getTabs().add(tab3);
            connect(5003);
        });

        room3.setOnAction(actionEvent -> {
            textArea.clear();
            if (tabPane.getTabs().size() > 2)
                tabPane.getTabs().remove(2);
            tab3 = new Tab(room3.getText(), thirdTab);
            tab3.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-color: white; -fx-start-margin: 0;-fx-end-margin: 0;");
            tabPane.getTabs().add(tab3);
            connect(5004);
        });

        room4.setOnAction(actionEvent -> {
            textArea.clear();
            if (tabPane.getTabs().size() > 2)
                tabPane.getTabs().remove(2);
            tab3 = new Tab(room4.getText(), thirdTab);
            tab3.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-color: white; -fx-start-margin: 0;-fx-end-margin: 0;");
            tabPane.getTabs().add(tab3);
            connect(5005);
        });

        room5.setOnAction(actionEvent -> {
            textArea.clear();
            if (tabPane.getTabs().size() > 2)
                tabPane.getTabs().remove(2);
            tab3 = new Tab(room5.getText(), thirdTab);
            tab3.setStyle("-fx-background-color: #a9a9a9; -fx-text-fill: white; -fx-color: white; -fx-start-margin: 0;-fx-end-margin: 0;");
            tabPane.getTabs().add(tab3);
            connect(5006);
        });
    }
    private void captureScreen(Stage stage) {
        // 현재 화면을 캡처하여 WritableImage로 저장합니다.

        WritableImage screenshot = stage.getScene().snapshot(null);

        // 파일 선택 대화상자를 열고 저장할 파일을 선택합니다.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Screen Capture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                // 캡처한 이미지를 파일로 저장합니다.
                ImageIO.write(SwingFXUtils.fromFXImage(screenshot, null), "png", file);
                System.out.println("Screen capture saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error saving screen capture: " + e.getMessage());
            }
        }
    }

    private void connect(int port) {
        try {
            if (socket != null) {
                socket.close();
            }
            // 소켓을 생성하여 연결을 요청한다.
            socket = new Socket(serverIp, port);
            System.out.println("서버에 연결되었습니다.");

            // 클라이언트 이름 입력하기
            Scanner scanner = new Scanner(System.in);
            System.out.print("회원 이름을 입력해주세요: ");
            String name = userName.getText(); //scanner.nextLine();

            // 서버와 수신/송신하는 스레드 생성
            Thread sender = new Thread(new ClientSender(socket, name));
            Thread receiver = new Thread(new ClientReceiver(socket, Client.this));

            // 스레드 동작
            sender.start();
            receiver.start();
        } catch (ConnectException ce) {
            // 서버에 접속 실패
            System.out.println("서버와 연결을 실패했습니다.");
            ce.printStackTrace();
        } catch (Exception e) {
        }
    }

    public static String speachToText(String file) {
        String clientId = "5alscm98vr";             // Application Client ID";
        String clientSecret = "1HpRalN4xebTZu5sim1g2x5LyhuacixlnM56R9MW";     // Application Client Secret";
        String result = "";
        try {

            String imgFile = file;
            File voiceFile = new File(imgFile);

            String language = "Kor";        // 언어 코드 ( Kor )
            String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
            URL url = new URL(apiURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(voiceFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            BufferedReader br = null;
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 오류 발생
                System.out.println("error! responseCode= " + responseCode);
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String inputLine;

            if (br != null) {
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                JSONTokener tokener = new JSONTokener(String.valueOf(response));
                JSONObject jsonObj = new JSONObject(tokener);
                result = (String) jsonObj.get("text");
                System.out.println(result);
            } else {
                System.out.println("error !!!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public static void main(String[] args) {
        launch();
    }
}