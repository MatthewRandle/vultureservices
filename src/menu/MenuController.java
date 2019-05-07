package menu;
import utils.Variables;
import utils.SceneController;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	    }
	    
	    public void setJobScreen() {
	    	SceneController.activate("Job");
	    }
	    
	    public void setLoadJobScreen() {
	    	SceneController.activate("loadJob");
	    }
	    
	    public void setTaskScreen() {
	    	SceneController.activate("taskAllocation");
	    }
	    
	    public void setUserAccountScreen() {
	    	SceneController.activate("userAccount");
	    }
	    public void setJobDelayScreen() {
	    	SceneController.activate("jobDelay");
	    }
}
