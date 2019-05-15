import utils.Variables;
import utils.Database;
import utils.SceneController;

public class Main {

    //used to setup initial config
    public static void main(String[] args)
    {
        //get a new database connection
        Variables.setConnection(Database.getConnection());

        //launch javafx from SceneController
        javafx.application.Application.launch(SceneController.class);
    }
}