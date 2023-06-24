import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Interactor {
    private String url;
    private static Connection conn = null;

    public Interactor(String ps, String db, String user, String pass) throws java.sql.SQLException{
        url = "jdbc:postgresql://"+
                ps+":5432/"+
                db+"?user="+
                user+"&password=" +
                pass+"&ssl=false";
        if (conn == null)
        conn = DriverManager.getConnection(url);
    }
    public Interactor(){}
    public String query(String que){
        try {
            Statement st = conn.createStatement();
            String rp = "";
            ResultSet resp = st.executeQuery(que);
            int columns = resp.getMetaData().getColumnCount();
            for (int i = 1; i<=columns;i++){
                rp += resp.getMetaData().getColumnName(i) + "\t";
            }
            rp += '\n';
            while (resp.next()){
                for (int i = 1; i<=columns;i++){
                    rp += resp.getString(i) + "\t";
                }
                rp += '\n';
            }
            rp += '\n';
            return rp;
        }
        catch (java.sql.SQLException e){
            return "fuck!";
        }
    }
    public ArrayList <Person> getlist(){
        try {
            ArrayList<Person> pep = new ArrayList<Person>();
            Statement st = conn.createStatement();
            String str = "";
            ResultSet resp = st.executeQuery("SELECT * FROM passports");
            int columns = resp.getMetaData().getColumnCount();
            while (resp.next()) {

                str = resp.getString(1);
                int idd = Integer.parseInt(str);

                str = resp.getString(2);
                String namei = str;

                str = resp.getString(3);
                Double xx = Double.parseDouble(str);
                str = resp.getString(4);
                int yy = Integer.parseInt(str);

                str = resp.getString(5);
                String crdatei = str;
                SimpleDateFormat form2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date creaDate = form2.parse(crdatei);

                str = resp.getString(6);
                Float hh = Float.parseFloat(str);

                str = resp.getString(7);
                String bdate = str;
                bdate = bdate.replace(' ', 'T');
                LocalDateTime bdDate = LocalDateTime.parse(bdate);

                str = resp.getString(8);
                String pid = str;

                str = resp.getString(9);
                String eyecol = str;
                Color coli = Color.BLUE;
                if (eyecol.toLowerCase().contentEquals("red")) coli = Color.RED;
                if (eyecol.toLowerCase().contentEquals("green")) coli = Color.GREEN;
                if (eyecol.toLowerCase().contentEquals("yellow")) coli = Color.YELLOW;
                if (eyecol.toLowerCase().contentEquals("brown")) coli = Color.BROWN;
                if (eyecol.toLowerCase().contentEquals("blue")) coli = Color.BLUE;

                str = resp.getString(10);
                double lx = Double.parseDouble(str);
                str = resp.getString(11);
                Long ly = Long.parseLong(str);
                str = resp.getString(12);
                Long lz = Long.parseLong(str);
                str = resp.getString(13);
                String user = str;

                pep.add(new Person(idd, namei, new Coordinates(xx, yy), creaDate, hh, bdDate, pid, coli, new Location(lx, ly, lz), user));
            }
            return pep;
        }
        catch (Exception e){
            System.out.printf("Load-up error: " + e.toString());
            return new ArrayList <Person> ();
        }
    }
    public String insertP(Person x){
        try {
            String que = "INSERT INTO passports(name, coordsX, coordsY, creationDate, height, birthday, passportID, eyeColor, locX, locY, locZ, username)\n";
            que += "VALUES (" + x.getSQL() + ");";
            PreparedStatement st = conn.prepareStatement(que, Statement.RETURN_GENERATED_KEYS);
            String rp = "";
            int done = st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    return "" + keys.getInt(1);
                }
                else {
                    return "Error: couldn't restore keys.\n";
                }
            }
        }
        catch (Exception e){
            return "Error: " + e.toString();
        }
    }
    public String queryS(String s){
        try {
            Statement st = conn.createStatement();
            boolean done = st.execute(s);
            return "Success.\n";
        }
        catch (Exception e){
            return "Error: " + e.toString();
        }
    }
    public String updateP(Person x){
        try {
            String que = "UPDATE passports SET (name, coordsX, coordsY, creationDate, height, birthday, passportID, eyeColor, locX, locY, locZ, username) = ";
            que += "(" + x.getSQL() + ")";
            que += "WHERE id = " + x.getID() + ";";
            Statement st = conn.createStatement();
            String rp = "";
            boolean done = st.execute(que);
            return "Updated element.\n";
        }
        catch (Exception e){
            return "Error: " + e.toString();
        }
    }
    public String selectPass(String user){
        try {
            String que = "SELECT password FROM users ";
            que += "WHERE username = '" + user + "';";
            Statement st = conn.createStatement();
            String rp = "";
            ResultSet resp = st.executeQuery(que);
            resp.next();
            return resp.getString(1);
        }
        catch (Exception e){
            return "Error: " + e.toString();
        }
    }
    public String insertPass(String user, String pass){
        try {
            String que = "INSERT INTO users(username, password) ";
            que += "VALUES ('" + user + "', '" + pass + "');";
            Statement st = conn.createStatement();
            String rp = "";
            boolean done = st.execute(que);
            return "OK";
        }
        catch (Exception e){
            return "Error: " + e.toString();
        }
    }
    public int selectPID(String pid){
        try {
            String que = "SELECT id FROM passports ";
            que += "WHERE passportid = '" + pid + "';";
            Statement st = conn.createStatement();
            String rp = "";
            ResultSet resp = st.executeQuery(que);
            resp.next();
            return resp.getInt(1);
        }
        catch (Exception e){
            return -1;
        }
    }
}
