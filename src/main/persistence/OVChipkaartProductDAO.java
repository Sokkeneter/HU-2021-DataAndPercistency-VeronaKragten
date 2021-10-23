package main.persistence;

import main.domain.OVChipkaart;
import main.domain.Product;
import java.sql.SQLException;

public interface OVChipkaartProductDAO {
    boolean save(OVChipkaart ovChipkaart, Product product) throws SQLException;

    boolean update(OVChipkaart ovChipkaart, Product product) throws SQLException;

    boolean delete(OVChipkaart ovChipkaart, Product product) throws SQLException;


}
