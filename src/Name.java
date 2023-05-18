import java.util.Random;

public enum Name {
    제이지, 프로토, 라이언, 네오, 춘식,
    튜브, 어피치, 무지, 콘, 주니어, 죠르디,
    스카피, 케로, 베로니, 앙몬드, 콥, 빠냐;

    private static final Random random = new Random();

    public static Name randomName() {
        Name[] names = values();
        return names[random.nextInt(names.length)];
    }
}
