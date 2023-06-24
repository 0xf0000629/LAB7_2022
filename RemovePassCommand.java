import java.util.ArrayList;

public class RemovePassCommand implements CommandRunnerSQL{
    public RemovePassCommand(){}

    @Override
    public String execute(Command com, ArrayList<Person> list, Interactor sql) {
        String id = "";
        String resp = "";
        try {
            id = com.getArg(0);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPID().contentEquals(id)) {
                    if (!list.get(i).getUser().contentEquals(com.getUser())) {
                        return "You cannot modify this record.\n";
                    }
                }
            }
        }
        catch (Exception e){return "Incorrect parameter.\n";}
        resp = sql.queryS("DELETE FROM passports WHERE passportid = '" + id + "';");
        if (resp.startsWith("Success.")) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPID() == id) {
                    list.remove(i);
                    break;
                }
            }
            return "Success!" + '\n';
        }
        else return resp + '\n';
    }
}

