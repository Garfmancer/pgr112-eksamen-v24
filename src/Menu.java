import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private final DiscoveryService discoveryService;

    public Menu() {
        discoveryService = new DiscoveryService();
    }

    public void run() {
        try (Connection connection = discoveryService.getDiscoveriesDB().getConnection();
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                menu();
                System.out.print("Velg et alternativ: ");
                int choice;

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                } else {
                    System.out.println("Ugyldig input. Vennligst skriv inn et tall.");
                    scanner.next();
                    continue;
                }

                switch (choice) {
                    case 1:
                        discoveryService.printItems(null, connection);
                        break;
                    case 2:
                        System.out.print("Tast inn årstall: ");
                        int age = scanner.nextInt();
                        discoveryService.printItems(age, connection);
                        break;
                    case 3:
                        discoveryService.countItems(connection);
                        break;
                    case 4:
                        System.out.println();
                        System.out.println("Avslutter programmet...");
                        System.out.println();
                        return;
                    default:
                        System.out.println();
                        System.out.println("Ugyldig valg. Prøv igjen.");
                        System.out.println();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Databasefeil", e);
        }
    }


    public void menu() {
        System.out.println();
        System.out.println("Valg meny");
        System.out.println("-------------------------");
        System.out.println("1 - Se informasjon om alle funngjenstander");
        System.out.println("2 - Se informasjon om alle funngjenstander eldre enn gitt årstall");
        System.out.println("3 - Få informasjon om antall funngjenstander registrert");
        System.out.println("4 - Avslutte programmet");
    }

}
