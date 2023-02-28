package seok;

public class Playerbounds extends PlayerOpton {
    public Playerbounds() {
        int width = Main.fr.getWidth() - 21; // 프레임 사이즈 -16 - 탭바사이즈(5)
        // 타이틀
        title.setBounds(0, 0, width, 30);
        // op라벨
        oplLabel.setBounds(0, 50, 100, 24);
        // op파일 불러오기
        opfile.setBounds(width - 80, oplLabel.getY(), 80, 25);
        // op화면
        opscroll.setBounds(0, oplLabel.getY() + 25, width, 200);
        // 추가버튼
        opselbt.setBounds(width - 125, opscroll.getY() + opscroll.getHeight() + 5, 60, 25);
        // 삭제버튼
        opdelbt.setBounds(width - 60, opscroll.getY() + opscroll.getHeight() + 5, 60, 25);
        // 입력창
        opField.setBounds(0, opscroll.getY() + opscroll.getHeight() + 5, 350, 25);

        // 채팅라벨
        chatLabel.setBounds(0, opField.getY() + 70, oplLabel.getWidth(), oplLabel.getHeight());
        // 채팅스크롤
        chatscroll.setBounds(0, chatLabel.getY() + 25, width, 200);
        // 채팅 입력창
        chatField.setBounds(0, chatscroll.getY() + chatscroll.getHeight() + 5, opField.getWidth(), opField.getHeight());
    }
}