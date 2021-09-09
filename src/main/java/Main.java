import domain.Adres;
import domain.Reiziger;
import main.persistence.AdresDAO;
import main.persistence.AdresDAOPSql;
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
        Reiziger reiziger = new Reiziger(6,"B","de", "pepperoni", java.sql.Date.valueOf("1981-03-14"));
        System.out.println("+++++++++++++++++++++++");
//            findAll
        System.out.println("alle reizigers: ");
        for(Reiziger reiziger1: rdao.findAll()){
            System.out.println(reiziger1);
        }
        System.out.println("+++++++++++++++++++++++");

//            save
        rdao.save(reiziger);
        System.out.println("reiziger 6: "+ rdao.findById(6));
//            update
        reiziger.setAchternaam("papperano");
        rdao.update(reiziger);
//            findById
        System.out.println("reiziger 6 after update: "+ rdao.findById(6));
        System.out.println("+++++++++++++++++++++++");
//           delete
        rdao.delete(reiziger);
        System.out.println("alle reizigers na deletion: ");
        for(Reiziger reiziger1: rdao.findAll()){
            System.out.println(reiziger1);
        }
        System.out.println("+++++++++++++++++++++++");
//          findByGb
        System.out.println("alle reizigers met geboortedatum 2002-12-03: ");
        for(Reiziger reiziger1: rdao.findByGbatum("2002-12-03")){
            System.out.println(reiziger1);
        }
        System.out.println("+++++++++++++++++++++++");
//        rdao.save(reiziger);
    }
    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");
        Reiziger reiziger = new Reiziger(6,"B","de", "pepperoni", java.sql.Date.valueOf("1981-03-14"));
        Adres adres = new Adres(6,"2987HY","14", "pepperoni", "Amsterdam", 6);
        ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(connection);
        reizigerDAOPsql.save(reiziger);
        adao.save(adres);
        adres.setWoonplaats("Gouda");
        adao.update(adres);

        for(Adres adres1: adao.findAll()){
            int adresID = adres1.getId();
            Reiziger reiziger1 = reizigerDAOPsql.findById(adresID);
            System.out.println(reiziger1 + ", " + adres1);
        }
        adao.delete(adres);
    }

    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(connection);
            AdresDAOPSql adresDAOPSql = new AdresDAOPSql(connection);

            testReizigerDAO(reizigerDAOPsql);
            testAdresDAO(adresDAOPSql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
