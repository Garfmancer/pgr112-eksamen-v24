import com.mysql.cj.jdbc.MysqlDataSource;
import objects.*;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

// inspirasjon av JDBCaustralia oppgaven
public class DiscoveryService implements IDiscoveryService {

    private final MysqlDataSource discoveriesDB;

    public DiscoveryService() {
        discoveriesDB = new MysqlDataSource();

        discoveriesDB.setServerName(PropertiesProvider.PROPS.getProperty("host"));
        discoveriesDB.setPortNumber(Integer.parseInt(PropertiesProvider.PROPS.getProperty("port")));
        discoveriesDB.setDatabaseName(PropertiesProvider.PROPS.getProperty("db_name"));
        discoveriesDB.setUser(PropertiesProvider.PROPS.getProperty("uname"));
        discoveriesDB.setPassword(PropertiesProvider.PROPS.getProperty("pwd"));
    }

    public MysqlDataSource getDiscoveriesDB() {
        return discoveriesDB;
    }

    public void truncateTables(Connection connection) throws SQLException {

        try(Statement statement = connection.createStatement()) {
            statement.execute("set foreign_key_checks=0");
            statement.execute("truncate table funn.museum");
            statement.execute("truncate table funn.mynt");
            statement.execute("truncate table funn.person");
            statement.execute("truncate table funn.smykke");
            statement.execute("truncate table funn.vaapen");
            statement.execute("set foreign_key_checks=1");
        }

    }

    private final static String INSERT_PERSON = "insert into person values(?,?,?,?)";

    public void insertPerson(Person person, Connection connection) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_PERSON)) {

            statement.setInt(1, person.id());
            statement.setString(2, person.name());
            statement.setString(3, person.phoneNumber());
            statement.setString(4, person.email());

            statement.executeUpdate();
        }
    }

    private final static String INSERT_MUSEUM = "insert into museum values(?,?,?)";

    public void insertMuseum(Museum museum, Connection connection) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_MUSEUM)) {

            statement.setInt(1, museum.id());
            statement.setString(2, museum.name());
            statement.setString(3, museum.location());

            statement.executeUpdate();
        }
    }

    private final static String INSERT_COIN = "insert into mynt values(?,?,?,?,?,?,?,?)";

    public void insertCoin(Coin coin, Connection connection) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_COIN)) {

            statement.setInt(1, coin.getId());
            statement.setString(2, coin.getDiscoveredLocation());
            statement.setInt(3, coin.getDiscoveredByPersonId());
            statement.setDate(4, coin.getDiscoveredDate());
            statement.setInt(5, coin.getEstimatedAge());

            if(coin.getMuseumId() != null) {
                statement.setInt(6, coin.getMuseumId());
            } else {
                statement.setNull(6, Types.INTEGER);
            }

            statement.setInt(7, coin.getDiameter());
            statement.setString(8, coin.getMetal());

            statement.executeUpdate();
        }
    }

    private final static String INSERT_JEWELRY = "insert into smykke values(?,?,?,?,?,?,?,?,?)";

    public void insertJewelry(Jewelry jewelry, Connection connection) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_JEWELRY)) {

            statement.setInt(1, jewelry.getId());
            statement.setString(2, jewelry.getDiscoveredLocation());
            statement.setInt(3, jewelry.getDiscoveredByPersonId());
            statement.setDate(4, jewelry.getDiscoveredDate());
            statement.setInt(5, jewelry.getEstimatedAge());

            if(jewelry.getMuseumId() != null) {
                statement.setInt(6, jewelry.getMuseumId());
            } else {
                statement.setNull(6, Types.INTEGER);
            }
            statement.setString(7, jewelry.getType());

            statement.setInt(8, jewelry.getValueEstimate());
            statement.setString(9, jewelry.getFileName());

            statement.executeUpdate();
        }
    }


    private final static String INSERT_WEAPON = "insert into vaapen values(?,?,?,?,?,?,?,?,?)";

    public void insertWeapon(Weapon weapon, Connection connection) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_WEAPON)) {

            statement.setInt(1, weapon.getId());
            statement.setString(2, weapon.getDiscoveredLocation());
            statement.setInt(3, weapon.getDiscoveredByPersonId());
            statement.setDate(4, weapon.getDiscoveredDate());
            statement.setInt(5, weapon.getEstimatedAge());

            if(weapon.getMuseumId() != null) {
                statement.setInt(6, weapon.getMuseumId());
            } else {
                statement.setNull(6, Types.INTEGER);
            }

            statement.setString(7, weapon.getType());
            statement.setString(8, weapon.getMaterial());
            statement.setInt(9, weapon.getWeight());

            statement.executeUpdate();
        }
    }




    private final static String COUNT_ALL_ITEMS = """
            select\s
            	sum(all_counts) as total
            from (
            	select count(*) as all_counts from mynt
                union all
                select count(*) from vaapen
                union all
                select count(*) from smykke
            ) as combined
            """;

    public void countItems(Connection connection) throws SQLException {

        try(PreparedStatement statement = connection.prepareStatement(COUNT_ALL_ITEMS)){

            ResultSet crs = statement.executeQuery();

            while (crs.next()) {
                Integer total = crs.getInt("total");

                System.out.println();
                System.out.printf("Antall funnet gjenstander: %s%n", total);
            }


        }
    }


    public ItemBase createBaseItem(Scanner reader){
        ItemBase itemBase = new ItemBase();

        itemBase.setId(Integer.parseInt(reader.nextLine()));
        itemBase.setDiscoveredLocation(reader.nextLine());
        itemBase.setDiscoveredByPersonId(Integer.parseInt(reader.nextLine()));
        itemBase.setDiscoveredDate(Date.valueOf(reader.nextLine()));
        itemBase.setEstimatedAge(Integer.parseInt(reader.nextLine()));

        String museumId = reader.nextLine();

        if(!museumId.isEmpty()) {
            itemBase.setMuseumId(Integer.parseInt(museumId));
        }

        itemBase.setClassification(reader.nextLine());

        return itemBase;
    }

    public void printItems(Integer ageHigherThan, Connection connection) throws SQLException {

        List<ItemDescription> items = new ArrayList<>();

        retrieveCoinsAndAddToList(ageHigherThan, connection, items);
        retrieveWeaponsAndAddToList(ageHigherThan, connection, items);
        retrieveJewelriesAndAddToList(ageHigherThan, connection, items);

        System.out.println();
        items.forEach(System.out::println);
        System.out.println();
    }

    // left join på museum da den kan være null og vi må fortsatt inkludere de
    private final static String SELECT_COINS = """
            select\s
                m.Funnsted discoveredLocation,
                m.Funntidspunkt discoveredDate,
                m.Antatt_årstall estimatedAge,
                m.Diameter diameter,
                m.Metall metal,
                p.Navn name,
                p.Tlf phoneNumber,
                p.E_post email,
                mu.Navn museum
            from mynt m
            inner join person p on m.Finner_id = p.Id
            left join museum mu on m.Museum_id = mu.Id
            where 1 = 1
            """;

    private void retrieveCoinsAndAddToList(Integer ageHigherThan, Connection connection, List<ItemDescription> collection) throws SQLException {

        StringBuilder baseCoinQuery = new StringBuilder(SELECT_COINS);

        if(ageHigherThan != null) {
            baseCoinQuery.append(" AND m.Antatt_årstall > ?");
        }

        try(PreparedStatement statement = connection.prepareStatement(baseCoinQuery.toString())) {

            if(ageHigherThan != null) {
                setFilter(ageHigherThan, statement);
            }

            ResultSet crs = statement.executeQuery();

            while (crs.next()) {
                collection.add(this.createCoinItemDescription(crs));
            }
        }
    }

    private final static String SELECT_JEWELRY = """
            select\s
                s.Funnsted discoveredLocation,
                s.Funntidspunkt discoveredDate,
                s.Antatt_årstall estimatedAge,
                s.Verdiestimat valueEstimate,
                s.Type type,
                p.Navn name,
                p.Tlf phoneNumber,
                p.E_post email
            from smykke s
            inner join person p on s.Finner_id = p.Id
            where 1 = 1
            """;

    private void retrieveJewelriesAndAddToList(Integer ageHigherThan, Connection connection, List<ItemDescription> collection) throws SQLException {

        StringBuilder jewelryQuery = new StringBuilder(SELECT_JEWELRY);

        if(ageHigherThan != null) {
            jewelryQuery.append(" and s.Antatt_årstall > ?");
        }

        try(PreparedStatement statement = connection.prepareStatement(jewelryQuery.toString())) {

            if(ageHigherThan != null) {
                setFilter(ageHigherThan, statement);
            }

            ResultSet jrs = statement.executeQuery();

            while (jrs.next()) {
                collection.add(this.createJewelryItemDescription(jrs));
            }
        }
    }

    private final static String SELECT_WEAPONS = """
           select\s
                v.Funnsted discoveredLocation,
                v.Funntidspunkt discoveredDate,
                v.Antatt_årstall estimatedAge,
                v.Type type,
                v.Materiale material,
                v.Vekt weight,
                p.Navn name,
                p.Tlf phoneNumber,
                p.E_post email
            from vaapen v
            inner join person p on v.Finner_id = p.Id
            where 1 = 1
           """;

    private void retrieveWeaponsAndAddToList(Integer ageHigherThan, Connection connection, List<ItemDescription> collection) throws SQLException {

        StringBuilder weaponsQuery = new StringBuilder(SELECT_WEAPONS);

        if(ageHigherThan != null) {
            weaponsQuery.append(" and v.Antatt_årstall > ?");
        }

        try(PreparedStatement statement = connection.prepareStatement(weaponsQuery.toString())) {

            if(ageHigherThan != null) {
                setFilter(ageHigherThan, statement);
            }

            ResultSet wrs = statement.executeQuery();

            while (wrs.next()) {
                collection.add(this.createWeaponItemDescription(wrs));
            }
        }
    }

    private void setFilter(Integer ageHigherThan, PreparedStatement statement) throws SQLException {
        if(ageHigherThan != null){
            statement.setInt(1, ageHigherThan);
        }
    }

    private ItemDescription createCoinItemDescription(ResultSet rs) throws SQLException {
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setClassification("coin");
        this.setBaseItemInformation(rs, itemDescription);
        this.setDiscovererInformation(rs, itemDescription);

        itemDescription.setMuseum(rs.getNString("museum"));
        itemDescription.setDiameter(rs.getInt("diameter"));
        itemDescription.setMetal(rs.getString("metal"));

        return itemDescription;
    }

    private ItemDescription createJewelryItemDescription(ResultSet rs) throws SQLException {
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setClassification("jewelry");
        this.setBaseItemInformation(rs, itemDescription);
        this.setDiscovererInformation(rs, itemDescription);

        itemDescription.setType(rs.getString("type"));
        itemDescription.setValueEstimate(rs.getInt("valueEstimate"));

        return itemDescription;
    }

    private ItemDescription createWeaponItemDescription(ResultSet rs) throws SQLException {
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setClassification("weapon");
        this.setBaseItemInformation(rs, itemDescription);
        this.setDiscovererInformation(rs, itemDescription);

        itemDescription.setType(rs.getString("type"));
        itemDescription.setMaterial(rs.getString("material"));
        itemDescription.setWeight(rs.getInt("weight"));

        return itemDescription;
    }

    private void setBaseItemInformation(ResultSet rs, ItemDescription itemDescription) throws SQLException {
        itemDescription.setDiscoveredDate(rs.getDate("discoveredDate").toString());
        itemDescription.setEstimatedAge(rs.getInt("estimatedAge"));
    }

    private void setDiscovererInformation(ResultSet rs, ItemDescription itemDescription) throws SQLException {
        itemDescription.setDiscovererName(rs.getString("name"));
        itemDescription.setDiscovererEmail(rs.getString("email"));
        itemDescription.setDiscovererPhoneNumber(rs.getString("phoneNumber"));
    }

}
