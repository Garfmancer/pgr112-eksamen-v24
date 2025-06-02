import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


// inspirasjon av JDBCaustralia oppgaven
public class PropertiesProvider {

    public static final Properties PROPS;

    static {
        PROPS = new Properties();
        try {
            PROPS.load(new FileInputStream("properties.txt"));
        } catch (IOException e) {
            System.out.println("Unable to load properties"+ e.getMessage());
        }
    }

}
