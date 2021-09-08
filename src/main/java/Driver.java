import java.sql.*;
public class Driver {
    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from reiziger;");
            while (resultSet.next()){
                String reiziger_id = resultSet.getString("reiziger_id");
                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");
                String geboortedatum = resultSet.getString("geboortedatum");

                System.out.println(String.format("#%s: %s. %s. %s (%s)", reiziger_id,voorletters, tussenvoegsel, achternaam, geboortedatum));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
