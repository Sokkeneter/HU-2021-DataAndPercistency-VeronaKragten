package main.persistence;

import main.domain.OVChipkaart;
import main.domain.Product;
import main.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;


public interface ProductDAO {
    boolean save(Product product) throws SQLException;

    boolean update(Product product) throws SQLException;

    boolean delete(Product product) throws SQLException;

    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException;

    List<Product> findAll() throws SQLException;

}
