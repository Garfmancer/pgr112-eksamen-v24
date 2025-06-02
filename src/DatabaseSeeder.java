import objects.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

// inspirasjon av JDBCaustralia oppgaven
public class DatabaseSeeder {

    private final DiscoveryService discoveryService;
    private final ObjectFactory objectFactory;

    public DatabaseSeeder(){
        discoveryService = new DiscoveryService();
        objectFactory = new ObjectFactory();
    }

    public void truncateTables() throws SQLException {
        Connection connection = discoveryService.getDiscoveriesDB().getConnection();
        connection.setAutoCommit(false);
        boolean autoCommit = connection.getAutoCommit();

        try {
            discoveryService.truncateTables(connection);
            connection.commit();
        } catch (SQLException sqlex) {
            connection.rollback();
            throw new RuntimeException(sqlex);
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    public void populateDatabase() throws SQLException, FileNotFoundException {

        File file = new File("funn.txt");

        try(Scanner reader = new Scanner(file);
            Connection connection = discoveryService.getDiscoveriesDB().getConnection()) {
            connection.setAutoCommit(false);
            boolean autoCommit = connection.getAutoCommit();

            try {
                while (reader.hasNext()) {

                    // hoppe over personer:
                    reader.nextLine();

                    int personCount = Integer.parseInt(reader.nextLine());
                    for (int i = 0; i < personCount; i++) {
                        discoveryService.insertPerson(objectFactory.createPerson(reader), connection);
                    }

                    // hoppe over museer:
                    reader.nextLine();


                    int museumCount = Integer.parseInt(reader.nextLine());
                    for (int i = 0; i < museumCount; i++) {
                        discoveryService.insertMuseum(objectFactory.createMuseum(reader), connection);
                    }

                    // hoppe over funn:
                    reader.nextLine();

                    while(reader.hasNext()) {

                        ItemBase base = discoveryService.createBaseItem(reader);

                        switch (base.getClassification()) {
                            case "Mynt" -> discoveryService.insertCoin(objectFactory.createCoin(base, reader), connection);
                            case "Smykke" -> discoveryService.insertJewelry(objectFactory.createJewelry(base, reader), connection);
                            case "Våpen" -> discoveryService.insertWeapon(objectFactory.createWeapon(base, reader), connection);
                        }

                        // skip separator
                        reader.nextLine();
                    }
                }

                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw new RuntimeException(exception);
            }
            finally {
                connection.setAutoCommit(autoCommit);
            }
        }
    }
}


