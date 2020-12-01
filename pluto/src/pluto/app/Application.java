package pluto.app;


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
        try {
            Database.loadFromJsonFiles();
        } catch (DatabaseDamagedException | DatabaseNotFound e) {
            e.printStackTrace();
            //System.exit(1);
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

        // TEMPORARY
        try {
            Database.seed();
        } catch (ValidationException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // --------

        UserController userController = new UserController();
        userController.login();
    }
}
