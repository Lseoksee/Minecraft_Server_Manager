package seok;

public class chatlog extends Main implements Runnable {

    String oldline;
    String line;

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(100);
                line = jarstart.line;
                // 채팅 정규식
                if (line != oldline && line.matches("(.*)<(.*)>(.*)|(.*)\\[Server\\](.*)|(.*)issued server command(.*)")) {
                    PlayerOpton.chatlog.append(line + "\n");
                    oldline = line;
                }
            }
        } catch (Exception e) {
            jarstart.log.setLength(0);
            oldline = null;
        }
    }

    public chatlog() {
        jarstart.line = null;
        String[] buffeString = jarstart.log.toString().split("\n");
        for (String string : buffeString) {
            // 채팅 정규식
            if (string.matches("(.*)<(.*)>(.*)|(.*)\\[Server\\](.*)|(.*)issued server command(.*)")) {
                PlayerOpton.chatlog.append(string + "\n");
            }
        }
        chatThread = new Thread(this);
        chatThread.start();
    }
}
