import java.util.ArrayList;

public class RemoveLastCommand implements CommandRunnerSQL{
    public RemoveLastCommand(){}

    @Override
    public String execute(Command com, ArrayList<Person> list, Interactor sql) {
        String resp = "";
        resp = sql.queryS("DELETE FROM passports WHERE id = (SELECT COUNT (*) FROM passports);");
        if (resp.startsWith("Success.")) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getID() == list.size()) {
                    list.remove(i);
                    break;
                }
            }
            return "Success!" + '\n';
        }
        else return resp + '\n';
    }
}