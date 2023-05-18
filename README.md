# Java-Socket-Programming
Create Multi Client Chat Server using Sockets and Threads in Java

<p align="center">
  <br>
<img width="300" alt="프로필 화면" src="https://github.com/zihooy/Java-Socket-Programming/assets/101122651/00b7d09f-58bc-4bd9-9d50-3633721ebb2c">
<img width="300" alt="채팅방 목록" src="https://github.com/zihooy/Java-Socket-Programming/assets/101122651/8bd60931-7279-4eb7-a557-51233cf4331e">
<img width="300" alt="채팅창 내용" src="https://github.com/zihooy/Java-Socket-Programming/assets/101122651/425a3fd9-8a7d-4f0a-b8af-54f76813cb5a">  <br>
</p>

## 프로젝트 소개

<p align="justify">
2일동안 Java의 Socket과 Thread를 활용하여 N:M 채팅방을 구현하였습니다.
채팅방의 기본 기능에 챗봇, 음성인식, 캡처, 필터링 등을 추가하였습니다.
</p>

<br>

## 구현 기능
### 기능 1: 프로필 페이지
    - 랜덤 사진 load(카카오 캐릭터 중 한개)
    - 랜덤 이름 할당(카카오 캐릭터 중 한개)
    - 이름 수정 가능     
        
### 기능 2: 방 목록 페이지
    - 클릭 시 해당 방 탭 생성
    - 채팅방 간 이동 가능
     
### 기능 3: 채팅 페이지
    - 채팅방에 참여한 사람끼리 N:N 대화 가능
    - 챗봇 가이드:
        - 번역하기
        - 뉴스 스크랩
        - 오늘의 날씨 알려주기
        - python 코드 실행
        - c 코드 실행
    - 랜덤 뽑기: 방에 참여한 사람 중 1명을 랜덤으로 선택
    - 음성인식: mp3 파일을 선택하면 text로 변환하여 메시지를 보냄
    - 캡처: 현재 화면을 캡처 후 저장
    - 비속어 Filtering: 서버에 저장된 단어(비속어) 입력시 필터링되서 메시지를 보냄
    
<br>

## Test Case

<p align="justify">
  
```html
  
    0. 채팅
    비트
    소나무
    안녕하세요 포스코 DX 5기 참여자 가나다입니다.
    안녕하세요 포스코 DX 5기 참여자 라마바입니다.
    안녕하세요 포스코 DX 5기 참여자 사아자입니다.
    제 파이썬 코드를 실행시켜보겠습니다.
    
    1. 버튼 클릭
    <챗봇가이드>
    /번역 오늘 저녁 회식 있는데 같이 가실래요?
    /뉴스 비트컴퓨터
    /뉴스 아이브
    /날씨
    (방 이동)
    /python for i in range(5): print('*' * (i + 1))
    /c언어 "#include <stdio.h> \n\n int main() {\n    printf(\"Hello, World!\\n\");\n    return 0;\n}"
    
    2. 버튼 클릭
    <랜덤뽑기>
    <음성인식>
    <캡처>
```

<br>


## 프로그램 구조
      
1) Server 구조

1.  Server

    `start(Stage stage)`: for문을 통한 Server Start Class를 Thread로 5개 생성

2.  Server Start
    - `startServer()` : whilte true일동안 반복 (접속할 클라이언트 대기 -> 접속한 클라이언트에 대한 수신 스레드 생성)
        - 대기하다가 연결된 클라이언트가 있으면 해당 클라이언트에 대한 소켓을 생성
        - 해당 클라이언트의 메시지를 수신하는 스레드 생성
    - `sendToAll(String msg)` : 해당 방에 있는 클라이언트에게 데이터 송신
        - iterator을 통해 client 가져오기
        - clients에 저장된 key값이 있으면, 현재 클라이언트와 송신하기 위해서 `DataOutputStream` out 객체 가져오기
        - 가져온 후에 현재 클라이언트에게 송신
3. Server Receiver: 클라이언트에게 메시지를 받아 기능 적용
    - API 통신을 위해 get, post, connet 메소드 생성
    - `translation(String msg)` : 파라미터로 오는 한국어를 영어로 번역해준다.
    - `weather()`: 현재 날씨와 온도 습도데이터를 가져오고 온도에 따라 옷차림을 추천해준다.
    - `String newsInfo(String msg)` : 메세지의  검색 파라미터를 통해 관련 뉴스를 가져옵니다.
    - `chatbotGuide()` : 채팅 봇 가이드라인으로 현재 사용가능한 기능들을 명시해준다.
    - `randomKey(String str)` : 현재 속한 채팅방 내에서 랜덤으로 한명을 지목해줍니다.
    - `pyInterpreter(String str)`: 파이썬 코드를 받아와 인터프리트하여 결과값을 반환합니다.
    - `cCompiler(String str)` : C언어 코드를 받아와 컴파일하여 결과 값을 반환해줍니다.
    - `String filterText(String sText)` : 비속어를 설정하면 필터링하여 반환한다.
    - `String maskWord` : 단어를 받아 첫 글자이외엔 모두 *로 변환하다.

2) Client 구조

1. Client
    - `start(Stage stage)`
        - UI 구성
        - Name Enum을 활용하여 랜덤으로 이름 생성
    - `connect(int port)`: 접속 소켓을 생성하여 연결을 요청
        - 서버와 수신/송신하는 스레드(Client Sender, Client Receiver) 생성
    - `captureScreen(Stage stage)`: snapshot 메서드를 사용하여 화이트라벨 이미지로 저장하고 PNG파일로 로컬에 저장할 수 있도록 환경 제공
    - `setOnAction(Stage stage)` : 버튼에 대한 액션을 구현
    - `speachToText(String file)`
2. Client Receiver
    - `ClientReceiver(Socket socket, Client client)` : 서버로부터 수신
    - `run()`: 반복 (수신 대기하다가 메시지 받으면 저장 -> 출력)
3. Client Sender
    - `ClientSender(Socket socket, String name)`  : 서버에게 송신
    - `run()` : 이름 전송후 반복 (사용자의 입력 대기하다가 입력 받으면 저장 -> 입력 메시지를 서버로 전송)
