package seok;

public class Playerbounds extends PlayerOpton {
    public Playerbounds() {
        int width = Main.fr.getWidth() - 21; // 프레임 사이즈 -16 - 탭바사이즈(5)

        //타이틀
        title.setBounds(0, 0, width, 30);
        //op라벨
        oplLabel.setBounds(0, 50, width, 24);
        //op화면
        opscroll.setBounds(0, oplLabel.getY() + 25, width, 200);
        //추가버튼
        opselbt.setBounds(width-125, opscroll.getY() + opscroll.getHeight()+5, 60, 25);
        //삭제버튼
        opdelbt.setBounds(width-60, opscroll.getY() + opscroll.getHeight()+5, 60, 25);
        //입력창
        opField.setBounds(0, opscroll.getY() + opscroll.getHeight()+5, 350, 25);
    }
}