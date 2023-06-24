import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.RecursiveTask;

public class Request extends RecursiveTask<String> {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public Request(ObjectInputStream ii, ObjectOutputStream oo){
        out = oo; in = ii;
    }
    protected String compute() {
        try {
            Command com = null;
            try {
                com = Command.class.cast(in.readObject());
            } catch (Exception e) {
                out.writeObject("That was not a command.\n");
            }
            String user = com.getUser();
            System.out.printf("User " + user + " sent a command.\n");

            Interactor sequel = new Interactor();
            DataManager mom = new DataManager();
            mom.asktool = new AskMore(in, out);
            Query que = new Query(com, mom, sequel);
            que.fork();
            //System.out.printf("Request " + Thread.currentThread().getId() + " finished\n");
            return "OK";
        }
        catch (Exception ee){
            System.out.printf("Request " + Thread.currentThread().getId() + " failed: " + ee.toString() + "\n");
            return "ERROR";
        }
    }
}
