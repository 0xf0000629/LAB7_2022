import java.util.ArrayList;

public class ClearCommand implements CommandRunnerSQL{
    public ClearCommand(){}

    @Override
    public String execute(Command com, ArrayList<Person> list, Interactor sql) {
        if (list.size() > 0) {
            int id = 0;
            String resp = "";
            resp = sql.queryS("DELETE FROM passports WHERE username = '" + com.getUser() + "';");
            if (resp.startsWith("Success.")) {
                list.removeIf(e -> e.getUser().contentEquals(com.getUser()));
                return "Success!" + '\n';
            }
            else return resp + '\n';
        }
        else
            return "The list is empty." + '\n';
    }
}