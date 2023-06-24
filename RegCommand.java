import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

public class RegCommand implements CommandRunnerSQL{
    public RegCommand(){}
    public String SHAit(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (Exception e){
            return "oops";
        }
    }
    @Override
    public String execute(Command com, ArrayList<Person> list, Interactor sql) {
        String resp = "";
        resp = sql.selectPass(com.getArg(0));
        if (resp.startsWith("OK")) {
            return "This user already exists!" + '\n';
        }
        else{
            String reg = sql.insertPass(com.getArg(0), SHAit(com.getArg(1)));
            if (reg.contentEquals("OK"))
                return "Registered!" + '\n';
            else {System.out.printf(reg); return "Couldn't register." + '\n';}
        }
    }
}