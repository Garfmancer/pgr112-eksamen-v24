import java.io.FileNotFoundException;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) {
        // del 1
        DatabaseSeeder partOne = new DatabaseSeeder();
        Menu partTwo = new Menu();

        try {
            // del 1
            partOne.truncateTables();
            partOne.populateDatabase();

            // del 2
            partTwo.run();

        } catch (SQLException | FileNotFoundException e) {
            System.out.println("SQLExeception caught" + e.getMessage());
            e.printStackTrace();
        }
    }
}