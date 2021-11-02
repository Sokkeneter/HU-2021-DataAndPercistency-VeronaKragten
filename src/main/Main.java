package main;

import main.domain.Adres;
import main.domain.OVChipkaart;
import main.domain.Product;
import main.domain.Reiziger;
import main.persistence.AdresDAO;
import main.persistence.OVChipkaartDAO;
import main.persistence.ProductDAO;
import main.persistence.psql.AdresDAOPSql;
import main.persistence.ReizigerDAO;
import main.persistence.psql.OVChipkaartDAOPsql;
import main.persistence.psql.ProductDAOPSql;
import main.persistence.psql.ReizigerDAOPsql;

import java.sql.*;
import java.util.List;

public class Main {
    private Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");

    public Main() throws SQLException {}

    private void getConnection(){}
    private void closeConnection(){}

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        Reiziger reiziger = new Reiziger("B de pepperoni", java.sql.Date.valueOf("1981-03-14"));
        reiziger.setId(6);
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
    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");
        Reiziger reiziger = new Reiziger("B de pepperoni", java.sql.Date.valueOf("1981-03-14"));
        reiziger.setId(6);
        Adres adres = new Adres("2987HY","14", "pepperoni", "Amsterdam", reiziger);
        adres.setId(6);
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
        reizigerDAOPsql.delete(reiziger);
    }

    private static void testOvChipkaartDAO(OVChipkaartDAO odao, ReizigerDAO reizigerDAO) throws SQLException {
        System.out.println("++++++++++test ovchipkaart dao+++++++++++++");

        Reiziger reiziger = reizigerDAO.findById(5);
        OVChipkaart ovChipkaart = new OVChipkaart(8, java.sql.Date.valueOf("2021-03-14"), 1,12, reiziger);
        odao.delete(ovChipkaart);
        odao.save(ovChipkaart);
        ovChipkaart.setSaldo(13.20);
        odao.update(ovChipkaart);

        for(Reiziger reiziger1: reizigerDAO.findAll()){
            List<OVChipkaart> kaarten = odao.findByReiziger(reiziger1);
                    System.out.println(reiziger1.getNaam());
                    for(OVChipkaart ovChipkaart2 : kaarten){
                        System.out.println(ovChipkaart2);
                    }
            System.out.println("---------------------");

        }

        odao.delete(ovChipkaart);
        System.out.println("+++++++++++++++++++++++");

    }

    private static void testProductDAO(ProductDAO pdao) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");
        ReizigerDAO reizigerDAOPsql = new ReizigerDAOPsql(connection);
        OVChipkaartDAO ovChipkaartDAOPsql = new OVChipkaartDAOPsql(connection);
        Product product = new Product(12,"baababobo", "productproductproduct", (long) 2.34);
        Reiziger reiziger = reizigerDAOPsql.findById(5);
        OVChipkaart ovChipkaart = new OVChipkaart(8, java.sql.Date.valueOf("2021-03-14"), 1,12, reiziger);
//        add ovchipkaart
        ovChipkaartDAOPsql.delete(ovChipkaart);
        ovChipkaartDAOPsql.save(ovChipkaart);
        product.addOvChipkaart(ovChipkaart);

        //save product
        pdao.delete(product);
        pdao.save(product);

        System.out.println("*****product zonder extra ovchipkaart****");
        for(Product product1: pdao.findAll()){
            System.out.println(product1);
        }
//        update
        product.setNaam("dagkaart met glitters");
        product.addOvChipkaart(ovChipkaartDAOPsql.findByReiziger(reizigerDAOPsql.findById(5)).get(0)); //truly sorry about this
        pdao.update(product);
        System.out.println("****met ovchipkaart*****");
        System.out.println(pdao.findByOVChipkaart(ovChipkaartDAOPsql.findByReiziger(reizigerDAOPsql.findById(5)).get(0)));
        System.out.println("*********");

        for(Product product1: pdao.findAll()){
            System.out.println(product1);
        }
        pdao.delete(product);

    }

    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip","postgres","qwerty11");
            ReizigerDAO reizigerDAOPsql = new ReizigerDAOPsql(connection);
            AdresDAO adresDAOPSql = new AdresDAOPSql(connection);
            OVChipkaartDAO ovChipkaartDAOPsql = new OVChipkaartDAOPsql(connection);

            ProductDAO productDAO = new ProductDAOPSql(connection);


//            testReizigerDAO(reizigerDAOPsql);
//            testAdresDAO(adresDAOPSql);
            testOvChipkaartDAO(ovChipkaartDAOPsql, reizigerDAOPsql);
            testProductDAO(productDAO);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
