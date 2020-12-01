package pluto.app;


import io.github.cdimascio.dotenv.DotenvException;
import pluto.controllers.UserController;
import pluto.database.Database;
import pluto.exceptions.DatabaseDamagedException;
import pluto.exceptions.DatabaseNotFound;
import pluto.exceptions.DatabasePersistenceException;
import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;

/***
 * Main class for starting up application. Application was developed under JDK 15 and JUnit 5.4.
 * <ul>
 *     <li>Connects and loads contents of database.</li>
 *     <li>Passes control to a UserController.</li>
 *     <li>Saves stored data back to database on exit.</li>
 * </ul>
 *
 * @author Trisztan Piller
 * @version 1.0.0
 */
public class Application {
    public static void main(String[] args) {
        PlutoConsole.setup();

        try {
            Database.loadFromJsonFiles();
        } catch (DatabaseDamagedException dde) {
            dde.printStackTrace();
            System.exit(1);
        } catch (DatabaseNotFound dnf) {
            PlutoConsole.err("Initialization failed: " + dnf.getMessage());
            PlutoConsole.log("Continuing with fetching administrator...");
        }

        try {
            Database.loadAdmin();
        } catch (NoSuchAlgorithmException | ValidationException | DotenvException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                PlutoConsole.log("Shutdown hook is running: Saving database files...");
                try {
                    Database.saveToJsonFiles();
                } catch (DatabasePersistenceException e) {
                    e.printStackTrace();
                }
            }
        });

        UserController userController = new UserController();
        userController.login();
    }
}
