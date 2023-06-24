import javax.xml.crypto.Data;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataManager {
    private static ArrayList <Person> list = null;
    private java.util.Date init;
    public AskMore asktool;
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    public DataManager(){
        Date date = new Date();
        init = date;
        if (list == null)
        list = new ArrayList<Person>();
    }
    public DataManager get(){
        return this;
    }
    public String runcommand(Command com, Interactor sql){
        try {Double.parseDouble(com.getName()); return "";} catch (Exception e) {}
        switch (com.getName()) {
            case "test":
                return new TestCommand().execute(com, list);
            case "help":
                return new HelpCommand().execute(com, list);
            case "askme":
                return new TestAsker().execute(asktool);
            case "ping":
                return "pong\n";
            case "execute_script":
                return com.getArg(0);
            case "login":
                return new LoginCommand().execute(com, list, sql);
            case "register":
                return new RegCommand().execute(com, list, sql);
        }
        lock.readLock().lock();
        try {
            switch (com.getName()) {
                case "show":
                    return new ShowCommand().execute(com, list);
                case "showall":
                    return new ShowAllCommand().execute(com, list);
                case "info":
                    return new InfoCommand().execute(com, list, init);
                case "sum_of_heights":
                    return new SumHeightCommand().execute(com, list);
            }
        }
        finally {lock.readLock().unlock();}
        lock.writeLock().lock();
        try {
            switch (com.getName()) {
                case "load":
                    list = sql.getlist();
                case "sort":
                    return new SortCommand().execute(com, list);
                case "shuffle":
                    return new ShuffleCommand().execute(com, list);
                case "add", "update":
                    return new AddUpdater().execute(com, list, asktool, sql);
                case "addfast", "updatefast":
                    return new AddUpdaterFast().execute(com, list, sql);
                case "remove_by_id":
                    return new RemoveIDCommand().execute(com, list, sql);
                case "clear":
                    return new ClearCommand().execute(com, list, sql);
                case "remove_by_passport_id":
                    return new RemovePassCommand().execute(com, list, sql);
            }
        }
        finally {lock.writeLock().unlock();}
        return "Couldn't recognise the command: " + com.getName() + '\n';
    }
}
