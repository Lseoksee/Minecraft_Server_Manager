package seok.UI;


public class Networkinfobounds extends NetworkinfoUI {
    public Networkinfobounds() {
        int width = fr.getWidth() - 18; // 프레임 사이즈 -16 - 탭바사이즈(5)

        //타이틀
        netinfo.setBounds(0, 0, width, 30);

        //내부 ip 주소 라벨
        inip.setBounds(15, 70, width/2-50,24);

        //내부 ip 주소
        iniptext.setBounds((width/2)-15, 70, width/2-100,24);

        //외부 ip 주소 라벨
        outip.setBounds(15, inip.getY()+35, width/2-50,24);

        //외부 ip 주소
        outiptext.setBounds((width/2)-15, iniptext.getY()+35, width/2-100,24);

        //포트포워딩 테스트 버튼
        porttestbt.setBounds(width/2-70, outip.getY()+50, 140,30);

        //포트포워딩 테스트 라벨
        porttestlabel.setBounds(0, porttestbt.getY()+50, width,30);

        //RCON 설정 페널
        rconpan.setBounds(10, porttestlabel.getY()+100, width-20,250);
    }
}