package seok;

public class chatlog extends PlayerOpton  {

    String oldline;
    String line;
    String oldbuffer;   //stringbuffer와 겹치는 문제 해결을 위해
    static boolean newobj;  //채팅로그 갱신 여부
    static Thread chatThread;
    

    @Override
    public void run() {
        try {
            while (!newobj) {
                Thread.sleep(100);
                line = jarstart.line;
                if (line != oldline && !line.equals(oldbuffer) &&  (
                line.indexOf("<") != -1 || 
                line.indexOf("[Server]") != -1 || 
                line.indexOf("issued server command") != -1
                )) {
                    chatlog.append(line+"\n");
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
            if (
            string.indexOf("<") != -1 || 
            string.indexOf("[Server]") != -1 || 
            string.indexOf("issued server command") != -1
            ) {
                chatlog.append(string+"\n");
                oldbuffer = string;
            }
        }
        chatThread = new Thread(this);
        chatThread.start();
    }   
}
