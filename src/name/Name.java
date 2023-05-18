package name;

import java.util.Random;

public enum Name {
    제이지ಠɞಠ("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbGtp5cN9Nudlp3aWYExRLzm6FU12yq2L0ltZjsiy6xQ&s"),
    프로도ಠɞಠ("https://item.kakaocdn.net/do/9dd38eb7b46d44bdd6e819c8b202986af604e7b0e6900f9ac53a43965300eb9a"),
    라이언ಠɞಠ("https://w7.pngwing.com/pngs/390/806/png-transparent-rilakkuma-kakaotalk-kakao-friends-south-korea-kakaofriends-sticker-desktop-wallpaper-snout-thumbnail.png"),
    네오ಠɞಠ("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9wdnGCdA822C5hzJVAZGjXLc4ZXAIbzRxkv5Dabm8419HOCH7gn9dINoyia55kGxAEXU&usqp=CAU"),
    춘식ಠɞಠ("https://item.kakaocdn.net/do/b5d3d6a7b67fbf5afdaffb79fffbf8b19f17e489affba0627eb1eb39695f93dd"),
    어피치ಠɞಠ("https://w7.pngwing.com/pngs/885/845/png-transparent-peach-head-illustration-kakaotalk-kakao-friends-sticker-peach-miscellaneous-child-face-thumbnail.png"),
    무지ಠɞಠ("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJ5RBqoZgvV3w8bIzitKZIIMM_5WcoyzMgQGoi388RMx5rK8Hjl7lddccEmY815cQbsJ0&usqp=CAU"),
    죠르디ಠɞಠ("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuuI6NMtI6-h41ekPENeosDmPXHuxkvstqDg&usqp=CAU");

    private static final Random random = new Random();

    private final String img;
    public String getImg() {
        return img;
    }

    public static Name randomName() {
        Name[] names = values();
        return names[random.nextInt(names.length)];
    }

    Name(String img) {
        this.img = img;
    }
}
