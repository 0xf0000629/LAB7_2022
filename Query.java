import javax.xml.crypto.Data;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.ReadWriteLock;


public class Query extends RecursiveTask<String> {
    private Command com;
    private DataManager session;
    private Interactor psql;
    public Query(Command c, DataManager sesh, Interactor sql){
        com = c; session = sesh; psql = sql;
    }
    protected String compute() {
        try {
            String resp = session.runcommand(com, psql);
            while (Thread.activeCount() > 200){}
            ExecutorService threadPool = Executors.newFixedThreadPool(1);
            CompletableFuture.supplyAsync(() -> {
                try {
                    session.asktool.out.writeObject(resp);
                    System.out.printf("Responded to " + com.getUser() + ".\n");
                    //System.out.printf("Thread " + Thread.currentThread().getId() + " finished\n");
                    threadPool.shutdown();
                    return "OK";
                }
                catch (Exception e){
                    System.out.printf("Thread " + Thread.currentThread().getId() + " failed: " + e.toString() + "\n");
                    threadPool.shutdown();
                    return "ERROR";
                }
            }, threadPool);
            //System.out.printf("Query " + Thread.currentThread().getId() + " finished\n");
            return "OK";
        }
        catch (Exception ee){
            System.out.printf("Query " + Thread.currentThread().getId() + " failed: " + ee.toString() + "\n");
            return "ERROR";
        }
    }
}