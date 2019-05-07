package taskAllocation;

import javafx.fxml.Initializable;
import utils.SceneController;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskController implements Initializable { 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
	
    public void testMethod() {
    	System.out.println("Test worked");
    }
    
    public void setJobScreen() {
    	SceneController.activate("Job");
    }
    
    public void setLoadJobScreen() {
    	SceneController.activate("Job");
    }
    
    public void setTaskScreen() {
    	SceneController.activate("taskAllocation");
    }
    
    public void setUserAccountScreen() {
    	SceneController.activate("userAccount");
    }
	
}
