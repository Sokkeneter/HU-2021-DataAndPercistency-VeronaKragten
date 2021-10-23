package main.persistence.psql;

import main.domain.OVChipkaart;
import main.domain.Product;
import main.domain.Reiziger;
import main.persistence.OVChipkaartDAO;
import main.persistence.ProductDAO;
import main.persistence.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPSql implements ProductDAO {
    private final Connection conn;
    private final OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPSql(Connection conn, OVChipkaartDAO ovChipkaartDAO) {
        this.conn = conn;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        try{
            int productnummer = product.getProductNummer();
            String naam = product.getNaam();
            String beschrijving = product.getBeschrijving();
            long prijs = product.getPrijs();

            String query = "INSERT INTO product VALUES (?, ?, ?, ?);";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productnummer);
            preparedStmt.setString(2, naam);
            preparedStmt.setString(3, beschrijving);
            preparedStmt.setLong(4, prijs);
            int resultSet = preparedStmt.executeUpdate();
//          persist list of ovchipcards
            for(OVChipkaart ovChipkaart : product.getOvChipkaarten()){
                String queryPOVC = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer ) VALUES (?,?) ON CONFLICT DO NOTHING;";
                PreparedStatement preparedStmtPOVC = conn.prepareStatement(queryPOVC);
                int kaartNummer = ovChipkaart.getKaartNummer();
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
    public boolean update(Product product) throws SQLException {

        try{
            int productnummer = product.getProductNummer();
            String naam = product.getNaam();
            String beschrijving = product.getBeschrijving();
            long prijs = product.getPrijs();

            String query = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productnummer);
            preparedStmt.setString(2, naam);
            preparedStmt.setString(3, beschrijving);
            preparedStmt.setLong(4, prijs);

            // what if the updated product has a different list of ovchipcards?

//          delete all povc with productnummer
            String deletePOVC = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
            PreparedStatement preparedStmtDeletePOVC = conn.prepareStatement(deletePOVC);

            //           re-save them from updated product
            for(OVChipkaart ovChipkaart : product.getOvChipkaarten()){
                String queryPOVC = "INSERT INTO ov_chipkaart_product VALUES kaart_nummer, product_nummer (?,?);";
                PreparedStatement preparedStmtPOVC = conn.prepareStatement(queryPOVC);
                int kaartNummer = ovChipkaart.getKaartNummer();
                preparedStmt.setInt(1, kaartNummer);
                preparedStmt.setInt(2, productnummer);
                preparedStmtPOVC.executeUpdate();
            }
            preparedStmtDeletePOVC.executeUpdate();

            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        try{
            String deletePOVC = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
            PreparedStatement preparedStmtDeletePOVC = conn.prepareStatement(deletePOVC);
            preparedStmtDeletePOVC.setInt(1, product.getProductNummer());
            preparedStmtDeletePOVC.executeUpdate();

            int productnummer = product.getProductNummer();
            String query = "DELETE FROM product CASCADE where product_nummer = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productnummer);

            int resultSet = preparedStmt.executeUpdate();
            return Boolean.getBoolean(String.valueOf(resultSet));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        try{
            List<Product> productList = new ArrayList<Product>();
            String query = "SELECT * FROM product JOIN ov_chipkaart_product ON product.product_nummer = ov_chipkaart_product.product_nummer  WHERE kaart_nummer = ?;";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, ovChipkaart.getKaartNummer());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productNummer = resultSet.getInt("product_nummer");
                String naam = resultSet.getString("naam");
                String beschrijving = resultSet.getString("beschrijving");
                long prijs = resultSet.getLong("prijs");
                Product product = new Product(productNummer,naam,beschrijving,prijs);

//                ovchipkaarten
                String queryOVC = "SELECT * FROM ov_chipkaart JOIN ov_chipkaart_product ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer WHERE product_nummer = ?;";
                PreparedStatement statementOVC = conn.prepareStatement(queryOVC);
                statementOVC.setInt(1, productNummer);
                ResultSet resultSetOVC = statementOVC.executeQuery();
                while(resultSetOVC.next()){
                    int kaartnummer = resultSetOVC.getInt("kaart_nummer");
                    Date geldigTot = resultSetOVC.getDate("geldig_tot");
                    int klasse = resultSetOVC.getInt("klasse");
                    double saldo = resultSetOVC.getDouble("saldo");
                    int reizigerId = resultSetOVC.getInt("reiziger_id");
                    ReizigerDAO rdaesql = new ReizigerDAOPsql(conn);
                    Reiziger reiziger = rdaesql.findById(reizigerId);

                    OVChipkaart ovChipkaartProduct = new OVChipkaart(kaartnummer,geldigTot,klasse,saldo,reiziger);
                    product.addOvChipkaart(ovChipkaartProduct);
                }

                productList.add(product);
            }
            return productList;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try{
            List<Product> productList = new ArrayList<Product>();
            String query = "SELECT * FROM product";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productNummer = resultSet.getInt("product_nummer");
                String naam = resultSet.getString("naam");
                String beschrijving = resultSet.getString("beschrijving");
                long prijs = resultSet.getLong("prijs");

                Product product = new Product(productNummer,naam, beschrijving, prijs);

                //                ovchipkaarten
                String queryOVC = "SELECT * FROM ov_chipkaart JOIN ov_chipkaart_product ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer WHERE product_nummer = ?;";
                PreparedStatement statementOVC = conn.prepareStatement(queryOVC);
                statementOVC.setInt(1, productNummer);
                ResultSet resultSetOVC = statementOVC.executeQuery();
                while(resultSetOVC.next()){
                    int kaartnummer = resultSetOVC.getInt("kaart_nummer");
                    Date geldigTot = resultSetOVC.getDate("geldig_tot");
                    int klasse = resultSetOVC.getInt("klasse");
                    double saldo = resultSetOVC.getDouble("saldo");
                    int reizigerId = resultSetOVC.getInt("reiziger_id");
                    ReizigerDAO rdaesql = new ReizigerDAOPsql(conn);
                    Reiziger reiziger = rdaesql.findById(reizigerId);

                    OVChipkaart ovChipkaartProduct = new OVChipkaart(kaartnummer,geldigTot,klasse,saldo,reiziger);
                    product.addOvChipkaart(ovChipkaartProduct);
                }
                productList.add(product);
            }
            return productList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
