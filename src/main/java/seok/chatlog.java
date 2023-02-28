package seok;

public class chatlog extends Main implements Runnable {

    String oldline;
    String line;
    String oldbuffer; // stringbuffer와 겹치는 문제 해결을 위해
    static boolean newobj; // 채팅로그 갱신 여부

    @Override
    public void run() {
        try {
            while (!newobj) {
                Thread.sleep(100);
                line = jarstart.line;
                // 채팅 정규식
                if (line != oldline && !line.equals(oldbuffer)
                        && line.matches("(.*)<(.*)>(.*)|(.*)\\[Server\\](.*)|(.*)issued server command(.*)")) {
                    PlayerOpton.chatlog.append(line + "\n");
                    oldline = line;
                }
            }
            jarstart.log.setLength(0);
            newobj = false;
            Thread.sleep(0);
            chatThread.interrupt();
        } catch (Exception e) {
        }
    }

    public chatlog() {
        String[] buffeString = jarstart.log.toString().split("\n");
        for (String string : buffeString) {
            // 채팅 정규식
            if (string.matches("(.*)<(.*)>(.*)|(.*)\\[Server\\](.*)|(.*)issued server command(.*)")) {
                PlayerOpton.chatlog.append(string + "\n");
                oldbuffer = string;
            }
        }
        chatThread = new Thread(this);
        chatThread.start();
    }
}
