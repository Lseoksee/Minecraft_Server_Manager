package seok.UI;

public class Networkinfobounds extends NetworkinfoUI {
    public Networkinfobounds() {
        int width = fr.getWidth() - 21; // 프레임 사이즈 -16 - 탭바사이즈(5)

        // 타이틀
        netinfo.setBounds(0, 0, width, 30);
        // 내부 ip 주소 패널
        inipppanel.setBounds(width/2-200, 70, 400, 30);
        // 외부 ip 주소 패널
        outippanel.setBounds(width/2-200, inipppanel.getY()+35, 400, 30);
        // 포트포워딩 포트 설정
        setport.setBounds((width / 2 - 40) + 40, outippanel.getY() + 50, 80, 20);
        // 포트포워딩 포트 설정 라벨
        setportlabel.setBounds((width / 2 - 40) - 40, outippanel.getY() + 50, 80, 20);
        // 포트포워딩 테스트 버튼
        porttestbt.setBounds(width / 2 - 60, setport.getY() + 30, 120, 30);
        // 포트포워딩 테스트 라벨
        porttestlabel.setBounds(0, porttestbt.getY() + 35, width, 30);
        // RCON 설정 페널
        rconpan.setBounds(10, porttestlabel.getY() + 100, width - 20, 250);
    }
}