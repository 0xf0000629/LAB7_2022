import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Person implements Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float height; //Поле может быть null, Значение поля должно быть больше 0
    private java.time.LocalDateTime birthday; //Поле не может быть null
    private String passportID; //Значение этого поля должно быть уникальным, Строка не может быть пустой, Длина строки не должна быть больше 50, Поле не может быть null
    private Color eyeColor; //Поле не может быть null
    private Location location; //Поле может быть null
    private String createdBy; // not null

    public Person(int iid, String i_name, Coordinates c, Float i_height, java.time.LocalDateTime bd, String pass, Color col, Location l, String username){
        id = iid;
        name = i_name;
        coordinates = c;
        Date date = new Date();
        creationDate = date;
        height = i_height;
        birthday = bd;
        passportID = pass;
        eyeColor = col;
        location = l;
        createdBy = username;
    }
    public Person(int iid, String i_name, Coordinates c, java.util.Date createDate, Float i_height, java.time.LocalDateTime bd, String pass, Color col, Location l, String username){
        id = iid;
        name = i_name;
        coordinates = c;
        creationDate = createDate;
        height = i_height;
        birthday = bd;
        passportID = pass;
        eyeColor = col;
        location = l;
        createdBy = username;
    }

    public String getColorStr(){ //returns a string representation of the eye color
        switch (eyeColor){
            case RED: return "red";
            case BLUE: return "blue";
            case YELLOW: return "yellow";
            case GREEN: return "green";
            case BROWN: return "brown";
            default: return "none";
        }
    }

    public String getStringRep(){ //returns a string representation of a person's record
        String full = "";
        SimpleDateFormat formcd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formbd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        full += Integer.toString(id) + ": " + name
                + "; Coordinates: " + Double.toString(coordinates.getX())
                + ", " + Integer.toString(coordinates.getY())
                + "; creation date/time: " + formcd.format(creationDate);

        if (height.isNaN()) full += "; height: null";
        else full += "; height: " + Float.toString(height);

        full += "; date of birth: " + birthday.format(formbd)
                + "; Passport ID: " + passportID
                + "; eye color: " + getColorStr();

        full += "; Location: ";

        if (Double.isNaN(location.getX())) full += " X - null";
        else full += "X - " + Double.toString(location.getX());

        if (location.getY() == Long.MIN_VALUE) full += "; Y - null";
        else full += "; Y - " + Long.toString(location.getY());

        if (location.getZ() == Long.MIN_VALUE) full += "; Z - null";
        else full += "; Z - " + Long.toString(location.getZ());

        full += "; created by " + getUser();

        return full;
    }
    public String getString(){ //returns a string representation of a person's record
        String full = "";
        SimpleDateFormat formcd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formbd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        full += Integer.toString(id) + " " + name
                + " " + Double.toString(coordinates.getX())
                + " " + Integer.toString(coordinates.getY())
                + " " + formcd.format(creationDate);

        if (height.isNaN()) full += " null";
        else full += " " + Float.toString(height);

        if (Double.isNaN(location.getX())) full += " null";
        else full += " " + Double.toString(location.getX());

        if (location.getY() == Long.MIN_VALUE) full += " null";
        else full += " " + Long.toString(location.getY());

        if (location.getZ() == Long.MIN_VALUE) full += " null";
        else full += " " + Long.toString(location.getZ());

        full += " " + birthday.format(formbd)
                + " " + getColorStr()
                + " " + passportID;
        if (createdBy.isEmpty())  full += " " + "<guest>";
            else full += " " + getUser();

        return full;
    }

    public String getSQL(){ //returns an XML representation of a person's record
        String full = "";
        SimpleDateFormat formcd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formbd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        full += "'" + name + "', " +
                Double.toString(coordinates.getX()) + ", " +
                Integer.toString(coordinates.getY()) + ", " +
                "'" + formcd.format(creationDate) + "', ";

        if (height.isNaN()) full += "null, ";
        else full += Float.toString(height) + ", ";

        full += "'" + birthday.format(formbd) + "', " +
                "'" + passportID + "', " +
                "'" + getColorStr() + "', ";

        if (Double.isNaN(location.getX())) full += "null, ";
        else full += Double.toString(location.getX()) + ", ";

        if (location.getY() == Long.MIN_VALUE) full += "null, ";
        else full += Long.toString(location.getY()) + ", ";

        if (location.getZ() == Long.MIN_VALUE) full += "null, ";
        else full += Long.toString(location.getZ()) + ", ";

        full += "'" + createdBy + "'";

        return full;
    }

    public int getID(){return id;} //returns an ID
    public String getPID(){return passportID;} //returns an passport ID
    public Float getH(){return height;} //returns the height
    public String getUser(){return createdBy;}
    public LocalDateTime getBD(){
        return birthday;
    } //returns the birthday
}