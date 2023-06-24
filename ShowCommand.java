import java.util.ArrayList;
import java.util.Optional;

public class ShowCommand implements CommandRunner{
    public ShowCommand(){}

    @Override
    public String execute(Command com, ArrayList<Person> list) {
        try{
            int id = 0;
            try {
                Optional<Person> guy = list.stream().filter(num -> num.getID() == Integer.parseInt(com.getArg(0))).findFirst();
                Person man;
                if (guy.isPresent()) man = guy.get();
                else  return "Failed to locate the record.\n";
                return man.getString();
            }
            catch (Exception e){return "Incorrect parameter.\n";}
        }
        catch(Exception e){return "Failed to locate the record.\n";}
    }
}
