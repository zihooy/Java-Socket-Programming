package server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerReceiver extends Thread {
    ServerStart s;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    int port;

    // (접속한 클라이언트와 수신하는) 스레드 객체의 멤버 초기화
    ServerReceiver(ServerStart s, Socket socket, int port) {
        this.s = s;
        this.socket = socket;
        this.port = port;
        try {
            in = new DataInputStream(socket.getInputStream()); // 데이터 수신을 편하게 하도록 도와주는 객체 (접속한 클라이언트 -> 서버)
            out = new DataOutputStream(socket.getOutputStream()); // 데이터 송신을 편하게 하도록 도와주는 객체 (서버 -> 접속한 클라이언트)
        } catch (IOException e) {
        }
    }

    public String translation(String msg) {

        String clientId = "Lqu41yICh7rXxjj3tc3l";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "9aiVPUlKa0";//애플리케이션 클라이언트 시크릿값";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;
        msg = msg.replace("/번역", "");
        try {
            text = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text);

        JSONObject json = new JSONObject(responseBody);
        JSONObject result = json.getJSONObject("message").getJSONObject("result");
        String translatedText = result.getString("translatedText");
        return translatedText;
    }

    public static String filterText(String sText) {
        Pattern p = Pattern.compile("fuck|shit|개새끼|비트", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sText);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            //System.out.println(m.group());
            m.appendReplacement(sb, maskWord(m.group()));
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public static String maskWord(String word) {
        StringBuffer buff = new StringBuffer();
        char[] ch = word.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (i < 1) {
                buff.append(ch[i]);
            } else {
                buff.append("*");
            }
        }
        return buff.toString();
    }

    public String weather() {
        String result = "";
        try {
            //서울시청의 위도와 경도
            String lon = "126.977948";  //경도
            String lat = "37.566386";   //위도

            //OpenAPI call하는 URL
            String urlstr = "http://api.openweathermap.org/data/2.5/weather?"
                    + "lat=" + lat + "&lon=" + lon
                    + "&appid=782f639b92273124c69837e8ebe5af0f";

            URI uri = new URI(urlstr);
            JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
            JSONObject jsonObj = new JSONObject(tokener);

            result = result.concat("오늘의 날씨입니다.\n");

            //지역 출력
            //System.out.println("지역 : " + jsonObj.get("name"));

            //날씨 출력
            JSONArray weatherArray = (JSONArray) jsonObj.get("weather");
            JSONObject obj = (JSONObject) weatherArray.get(0);
            result = result.concat("날씨 : ");
            result = result.concat(String.valueOf(obj.get("main")));

            //온도 출력(절대온도라서 변환 필요)
            JSONObject mainArray = (JSONObject) jsonObj.get("main");
            double ktemp = Double.parseDouble(mainArray.get("temp").toString());
            double temp = ktemp - 273.15;
            String rtemp = String.format("%.1f", temp);
            result = result.concat("\n온도 : ").concat(rtemp);

            //체감온도 출력
            double ftemp = Double.parseDouble(mainArray.get("feels_like").toString());
            double fftemp = ftemp - 273.15;
            String rfftemp = String.format("%.1f", fftemp);
            result = result.concat("\n체감온도 : ").concat(rfftemp);

            //습도 출력
            double humid = Double.parseDouble(mainArray.get("humidity").toString());
            result = result.concat("\n습도 : ").concat(String.valueOf(humid));


            //기온별 옷차림 출력
            if (temp > 27) result = result.concat("\n오늘은 매우 덥습니다. \n나시티, 반바지, 민소매 원피스를 입으시기를 추천합니다.");
            else if (temp > 23) result = result.concat("\n오늘은 조금 덥습니다. \n반팔, 얇은 셔츠, 얇은 긴팔, 반바지, 면바지를 입으시기를 추천합니다.");
            else if (temp > 20) result = result.concat("\n오늘은 조금 쌀쌀합니다. \n긴팔티, 가디건, 후드티, 면바지, 슬랙스를 입으시기를 추천합니다.");
            else if (temp > 17)
                result = result.concat("\n오늘은 조금 쌀쌀합니다. \n니트, 가디건, 후드티, 맨투맨, 청바지, 면바지, 슬랙스, 원피스를 입으시기를 추천합니다.");
            else if (temp > 12) result = result.concat("\n오늘은 조금 춥습니다. \n자켓, 셔츠, 가디건, 간절기 야상, 살색 스타킹을 입으시기를 추천합니다.");
            else if (temp > 10) result = result.concat("\n오늘은 조금 춥습니다. \n트렌치코트, 간절기야상, 레이어드하여 껴입기를 추천합니다.");
            else result = result.concat("\n오늘은 매우 춥습니다. \n패딩, 야상, 목도리를 착용하기를 추천합니다.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static String newsInfo(String msg) {
        String clientId = "Bnm6em7oqUJKIphrbrWt"; //애플리케이션 클라이언트 아이디
        String clientSecret = "6LqQFLby_t"; //애플리케이션 클라이언트 시크릿
        System.out.println(msg);
        msg = msg.replace("/뉴스", "");
        String text = null;
        try {
            text = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + text;    // JSON 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL, requestHeaders);

        System.out.println(responseBody);

        JSONTokener tokener = new JSONTokener(responseBody);
        JSONObject jsonObj = new JSONObject(tokener);

        JSONArray itemsArray = (JSONArray) jsonObj.get("items");
        String result = "";
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject obj = (JSONObject) itemsArray.get(i);
            result = result.concat(String.valueOf(obj.get("pubDate")).substring(0, 16)).concat(" | " + obj.get("title")).concat(" | " + obj.get("link")).concat("\n");
        }
        System.out.println(result);
        return result;
    }


    @Override
    public void run() {
        String name = "";
        String clients_key = "";
        try {

            // 처음 클라이언트가 접속한 상황 -> 대기하다가 수신된 클라이언트 이름을 받아서 clients에 저장, 현재 클라이언트 수 출력
            name = in.readUTF();
            clients_key = name + "(" + socket.getInetAddress() + ")";
            s.clients.put(clients_key, out); // 클라이언트 이름, 데이터 송신 관련 객체를 저장
            System.out.println("#" + name + "님이 들어오셨습니다.");
            s.sendToAll("#" + name + "님이 들어오셨습니다.");
            //s.sendToAll;
            s.sendToAll("현재 서버접속자 수는 " + s.clients.size() + "입니다.");


            // 이후에는 다음을 반복 (수신 대기하다 메시지 받으면 저장 -> 모든 클라이언트에게 메시지 송신)
            while (in != null) {
                String msg = in.readUTF();
                System.out.println(msg);
                msg = filterText(msg);
                if (msg.contains("/번역")) {
                    msg = translation(msg);
                } else if (msg.contains("랜덤뽑기")) {
                    msg = randomKey(msg);
                } else if (msg.contains("/날씨")) {
                    msg = weather();
                } else if (msg.contains("/뉴스")) {
                    msg = newsInfo(msg);
                } else if (msg.contains("챗봇가이드")) {
                    msg = chatbotGuide();
                } else if (msg.contains("/python")) {
                    msg = pyInterpreter(msg);
                } else if (msg.contains("/c언어")) {
                    msg = cCompiler(msg);
                }

                s.sendToAll(msg);
            }
        } catch (IOException e) {
        }
        // try문이 종료되면 다음을 실행 ex) 클라이언트가 나가서 in객체가 제대로 동작을 안 함
        finally {
            System.out.println("#" + port + ": " + name + "님이 나가셨습니다.");
            s.clients.remove(clients_key);

            System.out.println(port + ": " + "[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속을 종료하였습니다.");
            System.out.println(port + ": " + "현재 서버접속자 수는 " + s.clients.size() + "입니다.");
            s.sendToAll("#" + name + "님이 나가셨습니다.");
        }
    }

    public String chatbotGuide() {
        String chatbottext =
                "밑의 양식에 맞춰 챗봇기능을 사용해 보세요"
                        + "\n"
                        + "1./번역 {번역할 내용} -> 번역기능"
                        + "\n"
                        + "2./뉴스 {검색내용} -> 최근뉴스 스크랩"
                        + "\n"
                        + "3./날씨 -> 기온 및 옷차림 추천"
                        + "\n"
                        + "4./python -> 파이썬 코드 실행";

        return chatbottext;
    }

    public String randomKey(String str) {
        ArrayList<String> keys = new ArrayList<>(s.clients.keySet());
        Collections.shuffle(keys);
        // 첫 번째 요소 선택
        String randomKey = keys.get(0);
        String randomStr = "Random Key" + randomKey;
        String randomText = "당첨자는 " + randomKey.split("\\(/")[0] + "입니다.";
        System.out.println(randomKey.split("\\(/"));
        return randomText;
    }

    public String pyInterpreter(String str) {
        //String pythonCode = "print('Hello, Python!')";
        String[] parts = str.split("/");
        str = parts[1];
        str = str.replace("python", "");
        System.out.println(str + "parameter check");
        str = str.trim();
        String pythonCode = str; // 실행할 파이썬 코드
        System.out.println(pythonCode);
        String outputString = "";

        // 파이썬 코드를 파일로 저장
        String fileName = "script.py";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(pythonCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일로 저장된 파이썬 스크립트 실행
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", fileName);
            Process process = processBuilder.start();

            // 파이썬 스크립트의 출력 스트림 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            // 실행 결과 출력
            System.out.println("파이썬 스크립트 실행 결과:");
            System.out.println(output.toString());
            outputString = output.toString();
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
                System.out.println("파일 삭제 완료");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputString;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text) {
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        try {
            con.setRequestMethod("POST");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    public String cCompiler(String str) {
        //String pythonCode = "print('Hello, Python!')";
        String[] parts = str.split("/");
        str = parts[1];
        str = str.replace("C언어", "");
        System.out.println(str + "parameter check");
        str = str.trim();
        String outputString = "";

        try {
            // C 파일 경로 설정
//            String currentDirectory = System.getProperty("user.dir");

            String cFilePath = "scripter.c";
            String cTestcode = str;
            // C 파일에 스크립트 작성
            FileWriter fileWriter = new FileWriter(cFilePath);
            fileWriter.write(cTestcode);
            fileWriter.close();


            System.out.println("파일 생성");
            //C 파일 컴파일 명령 설정


            ProcessBuilder compileProcessBuilder = new ProcessBuilder("gcc", cFilePath, "-o", "executable");

            // C 파일 컴파일 실행
            Process compileProcess = compileProcessBuilder.start();
            int compileExitCode = compileProcess.waitFor();
            //    if (compileExitCode == 0) {
            System.out.println("C 파일 컴파일 성공");

            // 실행 파일 실행 명령 설정
            ProcessBuilder runProcessBuilder = new ProcessBuilder("executable");

            // 실행 파일 실행
            Process runProcess = runProcessBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
            outputString = output.toString();
            System.out.println("실행 결과:\n" + output.toString());
            int runExitCode = runProcess.waitFor();
            if (runExitCode == 0) {
                System.out.println("C 프로그램 실행 성공");
            } else {
                System.out.println("C 프로그램 실행 실패");
            }
//            } else {
//                System.out.println("C 파일 컴파일 실패");
//            }

            // 생성한 파일 삭제
//            File cFile = new File(cFilePath);
//            File executableFile = new File("executable");
//            if (cFile.delete() ) {
//                System.out.println("파일 삭제 성공");
//            } else {
//                System.out.println("파일 삭제 실패");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return outputString;
    }
}