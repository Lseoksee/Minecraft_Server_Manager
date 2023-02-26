package seok;

public class Mainbounds extends Main {
    public Mainbounds() {
        int width = fr.getWidth()-21; // 프레임 사이즈 -16 - 탭바사이즈(5)
        int objwidth = 170;
        //상단 상태라벨
        state.setBounds(0, 0, width, 30);
        //게임모드
        gamemode.setBounds((width-objwidth)/2+45, 35, objwidth, 24);
        //게임모드 라벨 
        gamela.setBounds(0, 35, width/2-45, 24);
        //난이도
        difficulty.setBounds((width-objwidth)/2+45, gamemode.getY()+40, objwidth, 24);
        //난이도 라벨
        difficultyla.setBounds(0, gamela.getY()+40, width/2-45, 24);
        //참여 인원
        person.setBounds((width-objwidth)/2+45, difficulty.getY()+40, objwidth, 24);
        //참여 인원 라벨
        personla.setBounds(0, difficultyla.getY()+40, width/2-45, 24);
        //하드코어
        hard.setBounds((width-objwidth)/2+45, person.getY()+43, 11, 24);
        //하드코어 라벨
        hardla.setBounds(0, personla.getY()+40, width/2-45, 24);
        //정품 여부
        real.setBounds((width-objwidth)/2+45, hard.getY()+40, 11, 24);
        //정품 여부 라벨
        realla.setBounds(0, hardla.getY()+40, width/2-45, 24);
        //커멘드 블록
        command.setBounds((width-objwidth)/2+45, real.getY()+40, 11, 24);
        //커멘드 블록 라벨
        commandla.setBounds(0, realla.getY()+40, width/2-45, 24);
        //서버이름
        sername.setBounds((width-objwidth)/2+45, command.getY()+40, objwidth, 24);
        //서버이름 라벨
        sernamela.setBounds(0, commandla.getY()+40, width/2-45, 24);
        //램
        ram.setBounds((width-objwidth)/2+45, sername.getY()+40, objwidth, 24);
        //램 라벨
        ramla.setBounds(0, sernamela.getY()+40, width/2-45, 24);
        //월드삭제
        world.setBounds((width-80)/2-120, ram.getY()+40, 80, 30);
        //저장
        savebt.setBounds((width-80)/2, ram.getY()+40, 80, 30);
        //추가설정
        manyset.setBounds((width-80)/2+120, ram.getY()+40, 80, 30);
        //콘솔
        consolsc.setBounds(0, savebt.getY()+40, width, 210);
        //메시지창
        meesge.setBounds((width-(width-60))/2, consolsc.getY()+220, width-60, 20);
        //시작
        startbt.setBounds((width-80)/2-80, meesge.getY()+30, 80, 30);
        //정지
        stopbt.setBounds((width-80)/2+80, meesge.getY()+30, 80, 30);
    }
}