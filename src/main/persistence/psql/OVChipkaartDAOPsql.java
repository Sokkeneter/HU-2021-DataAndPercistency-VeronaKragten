package main.persistence.psql;

import main.domain.OVChipkaart;
import main.domain.Product;
import main.domain.Reiziger;
import main.persistence.OVChipkaartDAO;
import main.persistence.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private final Connection conn;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        try{
            int kaartNummer = ovChipkaart.getKaartNummer();
            Date geldigTot = ovChipkaart.getGeldigTot();
            int klasse = ovChipkaart.getKlasse();
            double saldo = ovChipkaart.getSaldo();
            int reizigerId = ovChipkaart.getReiziger().getId();

            String query = "INSERT INTO ov_chipkaart VALUES (?, ?, ?, ?, ?);";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, kaartNummer);
            preparedStmt.setDate(2, geldigTot);
            preparedStmt.setInt(3, klasse);
            preparedStmt.setDouble(4, saldo);
            preparedStmt.setInt(5, reizigerId);
            int resultSet = preparedStmt.executeUpdate();
//            list of products
            for(Product product: ovChipkaart.getProducten()){
                String queryPOVC = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer ) VALUES (?,?) ON CONFLICT DO NOTHING;";
                PreparedStatement preparedStmtPOVC = conn.prepareStatement(queryPOVC);
                int productnummer = product.getProductNummer();
                preparedStmtPOVC.setInt(1, kaartNummer);
                preparedStmtPOVC.setInt(2, productnummer);
                preparedStmtPOVC.executeUpdate();
            }

            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        try{
            int kaartNummer = ovChipkaart.getKaartNummer();
            Date geldigTot = ovChipkaart.getGeldigTot();
            int klasse = ovChipkaart.getKlasse();
            double saldo = ovChipkaart.getSaldo();
            int reizigerId = ovChipkaart.getReiziger().getId();
            String query = "update ov_chipkaart set kaart_nummer = ?, geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? where kaart_nummer = ?;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, kaartNummer);
            preparedStmt.setDate(2, geldigTot);
            preparedStmt.setInt(3, klasse);
            preparedStmt.setDouble(4, saldo);
            preparedStmt.setInt(5, reizigerId);
            preparedStmt.setInt(6, kaartNummer);

            String deletePOVC = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
            PreparedStatement preparedStmtDeletePOVC = conn.prepareStatement(deletePOVC);
            preparedStmtDeletePOVC.setInt(1, kaartNummer);

            preparedStmtDeletePOVC.executeUpdate();

            for(Product product: ovChipkaart.getProducten()){
                String queryPOVC = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer ) VALUES (?,?);";
                PreparedStatement preparedStmtPOVC = conn.prepareStatement(queryPOVC);
                int productnummer = product.getProductNummer();
                preparedStmtPOVC.setInt(1, kaartNummer);
                preparedStmtPOVC.setInt(2, productnummer);
                preparedStmtPOVC.executeUpdate();
            }
            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        try{
            String deletePOVC = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
            PreparedStatement preparedStmtDeletePOVC = conn.prepareStatement(deletePOVC);
            preparedStmtDeletePOVC.setInt(1, ovChipkaart.getKaartNummer());
            preparedStmtDeletePOVC.executeUpdate();

            int kaartNummer = ovChipkaart.getKaartNummer();
            String query = "DELETE FROM ov_chipkaart CASCADE where kaart_nummer = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, kaartNummer);

            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        try{
            List<OVChipkaart> ovChipkaartList = new ArrayList<OVChipkaart>();
            String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, reiziger.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int kaartnummer = resultSet.getInt("kaart_nummer");
                Date geldigTot = resultSet.getDate("geldig_tot");
                int klasse = resultSet.getInt("klasse");
                double saldo = resultSet.getDouble("saldo");
                OVChipkaart ovChipkaart = new OVChipkaart(kaartnummer,geldigTot,klasse,saldo,reiziger);


//                producten
                String queryOVC = "SELECT * FROM product JOIN ov_chipkaart_product ON product.product_nummer = ov_chipkaart_product.product_nummer WHERE kaart_nummer = ?;";
                PreparedStatement statementOVC = conn.prepareStatement(queryOVC);
                statementOVC.setInt(1, kaartnummer);
                ResultSet resultSetOVC = statementOVC.executeQuery();
                while(resultSetOVC.next()){
                    int productNummer = resultSetOVC.getInt("product_nummer");
                    String naam = resultSetOVC.getString("naam");
                    String beschrijving = resultSetOVC.getString("beschrijving");
                    long prijs = resultSetOVC.getLong("prijs");

                    Product productOvChipkaart = new Product(productNummer,naam,beschrijving,prijs);
                    ovChipkaart.addProduct(productOvChipkaart);
                }

                ovChipkaartList.add(ovChipkaart);


            }
            return ovChipkaartList;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        try{
            List<OVChipkaart> ovChipkaartList = new ArrayList<>();
            String query = "SELECT * FROM ov_chipkaart";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int kaartnummer = resultSet.getInt("kaart_nummer");
                Date geldigTot = resultSet.getDate("geldig_tot");
                int klasse = resultSet.getInt("klasse");
                double saldo = resultSet.getDouble("saldo");
                int reizigerId = resultSet.getInt("reiziger_id");
                ReizigerDAO rdaesql = new ReizigerDAOPsql(conn);
                Reiziger reiziger = rdaesql.findById(reizigerId);

                OVChipkaart ovChipkaart = new OVChipkaart(kaartnummer,geldigTot,klasse,saldo,reiziger);
                ovChipkaartList.add(ovChipkaart);
            }
            return ovChipkaartList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
