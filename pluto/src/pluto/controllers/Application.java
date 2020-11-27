package pluto.controllers;


import pluto.helpers.PlutoConsole;
import pluto.models.UserModel;
import pluto.models.database.Database;

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
        /* Database db = new Database();
        try {
            db.loadAll();
        } catch (Database.DatabaseDamagedException | Database.DatabaseNotFound e) {
            e.printStackTrace();
            System.exit(1);
        } */

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                PlutoConsole.log("Shutdown hook is running: Saving database files...");
                //db.saveAll();
            }
        });

        UserController userController = new UserController();
        userController.login();
    }
}
