package vertx.example.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by darryl on 31/07/2015.
   A shared Porpoerties manager module so that we can have a single shared properties file shared by all of the
   verticles.
 */
public class PropertiesManager {
    /*
      Return Server Properties List
   */
    // TODO Does this need better exception handling?
    public static Properties getServerProperties() throws IOException {
        InputStream inputStream = null;
        Properties prop = null;

        //the base folder is ./, the root of the server.properties file
        String propFileName = "./server.properties";

        FileInputStream file  = new FileInputStream(propFileName);
        prop = new Properties();

        prop.load(file);

        return prop;
    }
}
