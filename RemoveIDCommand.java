import java.sql.SQLException;
import java.util.ArrayList;

public class RemoveIDCommand implements CommandRunnerSQL{
    public RemoveIDCommand(){}

    @Override
    public String execute(Command com, ArrayList<Person> list, Interactor sql) {
        int id = 0;
        String resp = "";
        try {
            id = Integer.parseInt(com.getArg(0));
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getID() == id) {
                    if (!list.get(i).getUser().contentEquals(com.getUser())) {
                        return "You cannot modify this record.\n";
                    }
                }
            }
        }
        catch (Exception e){return "Incorrect parameter.";}
        resp = sql.queryS("DELETE FROM passports WHERE id = " + id + ";");
        if (resp.startsWith("Success.")) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getID() == id) {
                    list.remove(i);
                    break;
                }
            }
            return "Success!" + '\n';
        }
        else return resp + '\n';
    }
}

