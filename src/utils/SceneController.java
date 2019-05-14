package utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.HashMap;

import userAccount.UserAccountController;

public final class SceneController extends Application {
    private static HashMap<String, Pane> screenMap = new HashMap<>();
    private static Scene mainScene;

    public static void SceneController(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Setup the login as the default scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login/View.fxml"));
        Parent root = fxmlLoader.load();
        mainScene = new Scene(root);

        primaryStage.setTitle("Vulture Services");
        primaryStage.setScene(mainScene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void registerScreens() throws Exception {
        //Register all available scenes here
        addScreen("userAccount", FXMLLoader.load(getClass().getResource( "/userAccount/UserAccount.fxml" )));
        addScreen("Job", FXMLLoader.load(getClass().getResource( "/jobcard/Job.fxml" )));
        addScreen("taskAllocation", FXMLLoader.load(getClass().getResource( "/taskAllocation/TaskAllocate.fxml" )));
        addScreen("jobDelay", FXMLLoader.load(getClass().getResource( "/jobdelay/JobDelay.fxml" )));
        addScreen("statistics", FXMLLoader.load(getClass().getResource( "/statistics/Statistics.fxml" )));
    }

    @Override
    public void stop() {
        try {
            if(Variables.getPreparedStatement() != null) {
                Variables.getPreparedStatement().close();
            }
            if(Variables.getConnection() != null) {
                Variables.getConnection().close();
            }
        }
        catch(SQLException error) {
            System.out.println("Could not close connection");
            System.out.println(error);
        }
    }

    public static void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }

    public static void removeScreen(String name){
        screenMap.remove(name);
    }

    public static void activate(String name) {
        mainScene.setRoot( screenMap.get(name) );

        if(name.equals("userAccount")) {
            //UserAccountController.checkUserType();
        }
    }
}
