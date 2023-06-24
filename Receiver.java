import javax.xml.crypto.Data;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Receiver {
    public static ServerSocket serverSocket;
    public static Socket clientSocket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;

    public static void start(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }
    public static int connect(){
        try{
            clientSocket = serverSocket.accept();
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        }catch (Exception e){return 1;}
        return 0;
    }

    public static void stop() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    public static void main(String[] args) {
        Receiver server = new Receiver();
        DataManager mom = new DataManager();
        try {mom.runcommand(new Command("server", "load", Arrays.asList("h"), ""), new Interactor(args[1], args[2], args[3], args[4]));}
        catch (SQLException e){System.out.printf("Couldn't connect to the SQL server.");return;}

        try {server.start(Integer.parseInt(args[0]));}
        catch (Exception e){
            try {server.start(Integer.parseInt(args[0]));}
            catch (Exception e2) {
                System.out.printf("Can't start the server: " + e2.toString() + "\n");
                return;
            }
        }
        while (true){ //listening
            while (connect() == 1){}
            Request inc = new Request(in, out);
            inc.fork();
        }
    }
}
