import java.util.ArrayList;

public interface CommandRunnerSQL {
    public String execute(Command com, ArrayList<Person>list, Interactor sql);
}
