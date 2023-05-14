package seok.test;

import java.time.Duration;
import java.util.List;

import io.graversen.minecraft.rcon.MinecraftRcon;
import io.graversen.minecraft.rcon.RconResponse;
import io.graversen.minecraft.rcon.commands.PlayerListCommand;
import io.graversen.minecraft.rcon.commands.TimeCommand;
import io.graversen.minecraft.rcon.commands.WeatherCommand;
import io.graversen.minecraft.rcon.query.playerlist.PlayerNames;
import io.graversen.minecraft.rcon.query.playerlist.PlayerNamesMapper;
import io.graversen.minecraft.rcon.service.ConnectOptions;
import io.graversen.minecraft.rcon.service.MinecraftRconService;
import io.graversen.minecraft.rcon.service.RconDetails;
import io.graversen.minecraft.rcon.util.TimeLabels;
import io.graversen.minecraft.rcon.util.Weathers;

public class rcon {
    public static void main(String[] args) {
        // 마인크래프트 rcon을 통한 서버 제어

        // rcon 환경은 기본포트이고 비번이 test인 상태 (만일 포트가 바뀐다면 RconDetails의 생성자를 통해 생성)
        final MinecraftRconService minecraftRconService = new MinecraftRconService(
                RconDetails.localhost("seok"),
                ConnectOptions.defaults());

        // 쓰레드 문제로 인해 ofSeconds의 값은 5이하로 설정하지 말것
        minecraftRconService.connectBlocking(Duration.ofSeconds(5));

        // 실제 rcon을 통해 접속 시도 만약 IllegalStateException 이 발생하면 위에 ofSeconds값을 높여볼것
        final MinecraftRcon minecraftRcon = minecraftRconService.minecraftRcon()
                .orElseThrow(IllegalStateException::new);

        // 시간 조절(time set day)
        final TimeCommand timeCommand = new TimeCommand(TimeLabels.NOON);
        // 날씨조절 (Weather clear)
        final WeatherCommand weatherCommand = new WeatherCommand(Weathers.CLEAR, Duration.ofHours(1).toSeconds());

        // 최종적으로 서버에 request
        minecraftRcon.sendAsync(timeCommand, weatherCommand);

        //플레이어 리스트 커멘드
        final PlayerListCommand playerListCommand = PlayerListCommand.names();

        // 서버 request 후 response 받기 (sendSync로 하면 response를 받아볼수 있음)
        RconResponse response = minecraftRcon.sendSync(playerListCommand);
        
        // respone 데이터로 플레이어 목록 가져오기
        PlayerNames names =  new PlayerNamesMapper().apply(response);
        List<String> list = names.getPlayerNames();
        if (!list.isEmpty()) {
            System.out.println(list);
        } else {
            System.out.println("접속자 없음");
        }

        // 연결 종료
        minecraftRconService.disconnect();
    }
}
