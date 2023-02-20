package seok;

public class setbounds extends Main {
    public setbounds() {
        int width = fr.getWidth()-16;
        //상단 상태라벨
        state.setBounds(0, 20, width, 30);
        //게임모드
        gamemode.setBounds((width-150)/2+40, 60, 170, 24);
        //게임모드 라벨 
        gamela.setBounds(0, 60, width/2-40, 24);
        //난이도
        difficulty.setBounds((width-150)/2+40, gamemode.getY()+40, gamemode.getWidth(), 24);
        //난이도 라벨
        difficultyla.setBounds(0, gamela.getY()+40, width/2-40, 24);
        //참여 인원
        person.setBounds((width-150)/2+40, difficulty.getY()+40, gamemode.getWidth(), 24);
        //참여 인원 라벨
        personla.setBounds(0, difficultyla.getY()+40, width/2-40, 24);
        //하드코어
        hard.setBounds((width-150)/2+40, person.getY()+43, 14, 24);
        //하드코어 라벨
        hardla.setBounds(0, personla.getY()+40, width/2-40, 24);
        //정품 여부
        real.setBounds((width-150)/2+40, hard.getY()+40, 14, 24);
        //정품 여부 라벨
        realla.setBounds(0, hardla.getY()+40, width/2-40, 24);
        //커멘드 블록
        command.setBounds((width-150)/2+40, real.getY()+40, 14, 24);
        //커멘드 블록 라벨
        commandla.setBounds(0, realla.getY()+40, width/2-40, 24);
        //서버이름
        sername.setBounds((width-150)/2+40, command.getY()+40, gamemode.getWidth(), 24);
        //서버이름 라벨
        sernamela.setBounds(0, commandla.getY()+40, width/2-40, 24);
        //램
        ram.setBounds((width-150)/2+40, sername.getY()+40, gamemode.getWidth(), 24);
        //램 라벨
        ramla.setBounds(0, sernamela.getY()+40, width/2-40, 24);
        //월드삭제
        world.setBounds((width-80)/2-120, ram.getY()+40, 80, 30);
        //저장
        savebt.setBounds((width-80)/2, ram.getY()+40, 80, 30);
        //추가설정
        manyset.setBounds((width-80)/2+120, ram.getY()+40, 80, 30);
        //콘솔
        consol.setBounds(0, savebt.getY()+40, width, 210);
        //메시지창
        meesge.setBounds((width-(width-60))/2, consol.getY()+220, width-60, 20);
        //시작
        startbt.setBounds((width-80)/2-80, meesge.getY()+30, 80, 30);
        //정지
        stopbt.setBounds((width-80)/2+80, meesge.getY()+30, 80, 30);
    }
}
