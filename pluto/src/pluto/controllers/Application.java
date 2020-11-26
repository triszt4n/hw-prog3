package pluto.controllers;


import pluto.models.UserModel;
import pluto.models.database.Database;

/***
 * Main class for starting up application. Application was developed under JDK 15 and JUnit 5.4.
 *
 * @author Trisztan Piller
 * @version 1.0.0
 */
public class Application {
    public static void main(String[] args) {
        Database db = new Database();
        try {
            db.loadAll();
        } catch (Database.DatabaseDamagedException | Database.DatabaseNotFound e) {
            e.printStackTrace();
            System.exit(1);
        }

        UserController userController = new UserController();
        userController.login();
    }
}
