import domain.Reiziger;
import main.persistence.ReizigerDAO;
import main.persistence.ReizigerDAOPsql;

import java.sql.*;
import java.util.List;

public class Main {
    private Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");

    public Main() throws SQLException {}

    private void getConnection(){}
    private void closeConnection(){}

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }

    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");
            Reiziger reiziger = new Reiziger(6,"B","de", "pepperoni", java.sql.Date.valueOf("1981-03-14"));
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(connection);
            System.out.println("+++++++++++++++++++++++");
//            findAll
            System.out.println("alle reizigers: ");
            for(Reiziger reiziger1: reizigerDAOPsql.findAll()){
                System.out.println(reiziger1);
            }
            System.out.println("+++++++++++++++++++++++");

//            save
            reizigerDAOPsql.save(reiziger);
            System.out.println("reiziger 6: "+ reizigerDAOPsql.findById(6));
//            update
            reiziger.setAchternaam("papperano");
            reizigerDAOPsql.update(reiziger);
//            findById
            System.out.println("reiziger 6 after update: "+ reizigerDAOPsql.findById(6));
            System.out.println("+++++++++++++++++++++++");
//           delete
            reizigerDAOPsql.delete(reiziger);
            System.out.println("alle reizigers na deletion: ");
            for(Reiziger reiziger1: reizigerDAOPsql.findAll()){
                System.out.println(reiziger1);
            }
            System.out.println("+++++++++++++++++++++++");
//          findByGb
            System.out.println("alle reizigers met geboortedatum 2002-12-03: ");
            for(Reiziger reiziger1: reizigerDAOPsql.findByGbatum("2002-12-03")){
                System.out.println(reiziger1);
            }
            System.out.println("+++++++++++++++++++++++");

//            testReizigerDAO(reizigerDAOPsql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
