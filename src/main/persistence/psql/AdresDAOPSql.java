package main.persistence.psql;

import main.domain.Adres;
import main.domain.Reiziger;
import main.persistence.AdresDAO;
import main.persistence.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPSql implements AdresDAO {
    private final Connection conn;

    public AdresDAOPSql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        try{int id = adres.getId();
            String postcode = adres.getPostcode();
            String huisnummer = adres.getHuisnummer();
            String straat = adres.getStraat();
            String woonplaats = adres.getWoonplaats();
            int reizigerId = adres.getReiziger().getId();
            String query = "INSERT INTO adres VALUES (?, ?, ?, ?,  ?, ?);";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, postcode);
            preparedStmt.setString(3, huisnummer);
            preparedStmt.setString(4, straat);
            preparedStmt.setString(5, woonplaats);
            preparedStmt.setInt(6, reizigerId);

            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        try{
            int id = adres.getId();
            String postcode = adres.getPostcode();
            String huisnummer = adres.getHuisnummer();
            String straat = adres.getStraat();
            String woonplaats = adres.getWoonplaats();
            int reizigerId = adres.getReiziger().getId();
            String query = "update adres set adres_id = ?, postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? where adres_id = ?;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, postcode);
            preparedStmt.setString(3, huisnummer);
            preparedStmt.setString(4, straat);
            preparedStmt.setString(5, woonplaats);
            preparedStmt.setInt(6, reizigerId);

            preparedStmt.setInt(7, id);

            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        try{
            int id = adres.getId();
            String query = "DELETE FROM adres where adres_id = ?";
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
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        try{
            String query = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, reiziger.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int adres_id = resultSet.getInt("adres_id");
                String postcode = resultSet.getString("postcode");
                String huisnummer = resultSet.getString("huisnummer");
                String straat = resultSet.getString("straat");
                String woonplaats = resultSet.getString("woonplaats");

                Adres adres = new Adres(postcode, huisnummer, straat, woonplaats, reiziger);
                adres.setId(adres_id);
                return adres;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        try{
            List<Adres> adresList = new ArrayList<Adres>();
            String query = "SELECT * FROM adres";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int adres_id = resultSet.getInt("adres_id");
                String postcode = resultSet.getString("postcode");
                String huisnummer = resultSet.getString("huisnummer");
                String straat = resultSet.getString("straat");
                String woonplaats = resultSet.getString("woonplaats");
                int reizigerId = resultSet.getInt("reiziger_id");
//                crying*
                ReizigerDAO rdaesql = new ReizigerDAOPsql(conn);
                Reiziger reiziger = rdaesql.findById(reizigerId);

                Adres adres = new Adres( postcode, huisnummer, straat, woonplaats, reiziger);
                adres.setId(adres_id);
                adresList.add(adres);
            }
            return adresList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
