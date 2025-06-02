import objects.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public interface IDiscoveryService {

    void insertPerson(Person person, Connection connection) throws SQLException;
    void insertMuseum(Museum museum, Connection connection) throws SQLException;
    void insertCoin(Coin coin, Connection connection) throws SQLException;
    void insertJewelry(Jewelry jewelry, Connection connection) throws SQLException;
    void insertWeapon(Weapon weapon, Connection connection) throws SQLException;
    void countItems(Connection connection) throws SQLException;
}
