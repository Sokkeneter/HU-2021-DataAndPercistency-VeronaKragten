package main.persistence;

import domain.Adres;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private final Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try{
            Statement statement = conn.createStatement();
            int id = reiziger.getId();
            String voorletters = reiziger.getVoorletters();
            String tussenvoegsel = reiziger.getTussenvoegsel();
            String achternaam = reiziger.getAchternaam();
            Date geboortedatum = reiziger.getGeboorteDatum();
            int resultSet = statement.executeUpdate(String.format(
                    "INSERT INTO reiziger\n" +
                            "VALUES ('%s', '%s', '%s', '%s', '%s'); ", id, voorletters, tussenvoegsel, achternaam, geboortedatum));
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try{
            int id = reiziger.getId();
            String voorletters = reiziger.getVoorletters();
            String tussenvoegsel = reiziger.getTussenvoegsel();
            String achternaam = reiziger.getAchternaam();
            java.sql.Date geboortedatum = (java.sql.Date) reiziger.getGeboorteDatum();
//        https://alvinalexander.com/java/java-mysql-update-query-example/ voorbeeld gebruikt
            String query = "update reiziger set reiziger_id = ?, voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? where reiziger_id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, voorletters);
            preparedStmt.setString(3, tussenvoegsel);
            preparedStmt.setString(4, achternaam);
            preparedStmt.setDate(5, geboortedatum);

            preparedStmt.setInt(6, id);

            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try{
            int id = reiziger.getId();
            String query = "DELETE FROM reiziger where reiziger_id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);

            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        try{
            String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int reiziger_id = resultSet.getInt("reiziger_id");
                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");
                java.sql.Date geboortedatum = resultSet.getDate("geboortedatum");

                Reiziger reiziger = new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);
                return reiziger;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbatum(String datum){
        try{
            List<Reiziger> reizigerList = new ArrayList<Reiziger>();
            String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            java.sql.Date birthDate = java.sql.Date.valueOf(datum);
            statement.setDate(1, birthDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int reiziger_id = resultSet.getInt("reiziger_id");
                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");
                java.sql.Date geboortedatum = resultSet.getDate("geboortedatum");

                Reiziger reiziger = new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);
                reizigerList.add(reiziger);
            }
            return reizigerList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll(){
        try{
            List<Reiziger> reizigerList = new ArrayList<Reiziger>();
            String query = "SELECT * FROM reiziger";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int reiziger_id = resultSet.getInt("reiziger_id");
                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");
                java.sql.Date geboortedatum = resultSet.getDate("geboortedatum");

                Reiziger reiziger = new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);
                reizigerList.add(reiziger);
            }
            return reizigerList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
