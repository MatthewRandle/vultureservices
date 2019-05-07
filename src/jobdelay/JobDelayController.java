package jobdelay;
import utils.Variables;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import utils.SceneController;

public class JobDelayController implements Initializable {

	
	Connection connection;
	PreparedStatement ps;
	
	//the job number text field
	public TextField jobField;
	
	//the job status select box
	public ChoiceBox<String> jobStatus;

	
	/**
	 * Initialize method initializes certain things when u first start the program.
	 * (Basically a constructor).
	 */
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		//adds the text values to the select box, otherwise it would just be blank.
		jobStatus.getItems().addAll("Active", "Suspended");
	}
	
	/*
	 * Loads job info from the database when u click the load job button
	 */
	   public void loadJob() {
	        try {
	        	// gets the text from the job number field and converts it to an integer
	        	String jobField1 = this.jobField.getText();
	        	int jobNumber = Integer.parseInt(jobField1);

	            connection = Variables.getConnection();
	            ps = Variables.getPreparedStatement();

	            String query = "SELECT status FROM jobs WHERE jobs.job_number = ?";
	            ps = connection.prepareStatement(query);
	            //sets the ? mark to job number (just basic pepared statement stuff)
	            ps.setInt(1, jobNumber);

	            ResultSet results = ps.executeQuery();

	            //if there is no results, then throw error
	            if (!results.isBeforeFirst() ) {
	                System.out.println("ER_JOB_DOES_NOT_EXIST");
	                sqlConfirmation("The specified job does not exist.");
	            }
	            //otherwise set the jobstatus box to the jobstatus stored in the database
	            else {
	    			String jobStatus;
	    			
	                while(results.next()) {
	                	jobStatus = results.getString("status"); 
	                	this.jobStatus.setValue(jobStatus);
	                	this.jobStatus.setDisable(false);
	                             	
	                }   
	            }
	        }
	        catch(SQLException error) {
	            System.out.println("Error getting Job");
	            System.out.println(error);
	        }
	    }
	   
	   /**
	    * Save the status of a job (ie make active or suspend)
	    */
	   public void saveStatus() {
	    	 try {
	    		 	//the field where u input the job number
	    		 	String jobField;
	    		 	//converted value of job number to an integer
	    		 	int jobNumber;  
	    		 	//the job status
	    			String jobStatus;
	    			
	    			//gets the text from the job number field
			 		jobField = this.jobField.getText();
			 		
			 		//converts it to an integer
			 		jobNumber = Integer.parseInt(jobField);
			 		
			 		//gets the value of the job status field
	    			jobStatus = this.jobStatus.getValue(); 

	    		//sql stuff	
	             connection = Variables.getConnection();
	             ps = Variables.getPreparedStatement();

	             //update query
	             String query = "UPDATE jobs SET status = ? WHERE job_number = ?";
	             ps = connection.prepareStatement(query);
	             //sets the first ? to job status
	             ps.setString(1, jobStatus);
	             //sets the second ? to job number
	             ps.setInt(2, jobNumber);
	             ps.executeUpdate();
	             //calls sql confirmation method which is the pop up box u see
	             sqlConfirmation("Job Updated Successfully");
	         }
	         catch(SQLException error) {
	             sqlConfirmation("The Data Was Not Inserted Successfully");
	        	 System.out.println("Error Inserting Into Database");
	             System.out.println(error);
	         }
	    	 catch(RuntimeException error) {
	    		 sqlConfirmation("The Data Was Not Inserted Successfully");
	    		 System.out.println("Interface Error");
	    		 System.out.println(error);
	    	 }
	     }
	   
	   //custom pop up box confirmation
	    public void sqlConfirmation(String message) {
        	Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
    	}

}
