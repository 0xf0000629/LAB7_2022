import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AddUpdaterFast {
    public AddUpdaterFast(){};
    public int nextfreeID(ArrayList<Person> list){
        ArrayList <Integer> ids = new ArrayList<Integer>();
        for (int i=0;i<list.size();i++){
            ids.add(list.get(i).getID());
        }
        ids.sort(new IntegerComparator());
        for (int i=0;i<ids.size();i++){
            if (i+1 != ids.get(i)){
                return i+1;
            }
        }
        return ids.size()+1;
    }
    public String execute(Command com, ArrayList <Person> list, Interactor sql){
        String nfile = com.getFile();
        String user = com.getUser();
        String response = "";
        try {
            int id = 0, oops = 0;
            if (com.getName().contentEquals("updatefast"))
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
                catch (Exception oof) {oops = 1;}

            int file = 0; if (!nfile.contentEquals("")) file = 1;
            System.out.printf("Requesting element...\n");
            String name = com.getArg(1);

            System.out.printf("Name processed (" + name + ")...\n");
            double cx = Double.parseDouble(com.getArg(2));
            int cy = Integer.parseInt(com.getArg(3));
            System.out.printf("Coordinates processed (" + cx + "," + cy + ")...\n");
            // height is unnecessary and has to be >0 so ask until one of them is true
            float hi = Float.parseFloat(com.getArg(4));
            if (!Float.isNaN(hi) && hi < 0) {
                response = "Height has to be more than 0 or none!\n";
            }
            System.out.printf("Height processed (" + hi + ")...\n");
            double lx = Double.parseDouble(com.getArg(5));
            long ly = Long.parseLong(com.getArg(6));
            long lz = Long.parseLong(com.getArg(7));
            System.out.printf("Location coordinates processed (" + lx + ',' + ly + ',' + lz + ")...\n");
            LocalDateTime bd =
                    LocalDateTime.parse(com.getArg(8) + "T" + com.getArg(9));
            System.out.printf("BDate processed (" + bd + ")...\n");
            Color col = Color.BLUE; //this will be rewritten
            String colstr = com.getArg(10);
            if (colstr.toLowerCase().contentEquals("red"))col = Color.RED;
            if (colstr.toLowerCase().contentEquals("green"))col = Color.GREEN;
            if (colstr.toLowerCase().contentEquals("yellow"))col = Color.YELLOW;
            if (colstr.toLowerCase().contentEquals("brown"))col = Color.BROWN;
            if (colstr.toLowerCase().contentEquals("blue"))col = Color.BLUE;
            System.out.printf("Color processed (" + col + ")...\n");
            String passid = com.getArg(11);
            int ch = sql.selectPID(passid);
            if (ch == id || ch == -1) {}
            else{
                response = "This passport ID is already in use!\n";
            }
            System.out.printf("Passport ID processed (" + passid + ")...\n");
            if (oops == 0) {
                if (com.getName().contentEquals("addfast")) {
                    response = sql.insertP(new Person(id, name, new Coordinates(cx, cy), hi, bd, passid, col, new Location(lx, ly, lz), user));
                    if (!response.toLowerCase().startsWith("error")) {
                        list.add(new Person(Integer.parseInt(response), name, new Coordinates(cx, cy), hi, bd, passid, col, new Location(lx, ly, lz), user));
                        response = "Added element.\n";
                    }
                    else return response + '\n';
                }
                else {
                    response = sql.updateP(new Person(id, name, new Coordinates(cx, cy), hi, bd, passid, col, new Location(lx, ly, lz), user));
                    if (!response.toLowerCase().startsWith("error")) {
                        for (int loc = 0; loc < list.size(); loc++) {
                            if (list.get(loc).getID() == id) {
                                list.set(loc, new Person(id, name, new Coordinates(cx, cy), hi, bd, passid, col, new Location(lx, ly, lz), user));
                                break;
                            }
                        }
                        response = "Updated element.\n";
                    }
                    else return response + '\n';
                }
            }
            else response = "Argument error.\n";
        }
        catch (Exception e){
            response = "Error: " + e.toString();
        }
        return response;
    }
}
