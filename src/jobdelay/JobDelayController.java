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
	
	public TextField jobField;
	public ChoiceBox<String> jobStatus;
	
	public TextField taskField;
	public ChoiceBox<String> taskCompleted;
	public ChoiceBox<String> taskStatus;
	
	public TextField taskField1;
	public TextField taskDateField;
	
	/**
	 * Initialise method initialises certain things when u first start the program.
	 * (Basically a constructor).
	 */
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		//adds the text values to the select box, otherwise it would just be blank.
		jobStatus.getItems().addAll("Active", "Suspended");
		taskStatus.getItems().addAll("1", "0");
		taskCompleted.getItems().addAll("1", "0");
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
	            //sets the ? mark to job number (just basic prepared statement stuff)
	            ps.setInt(1, jobNumber);

	            ResultSet results = ps.executeQuery();

	            //if there is no results, then throw error
	            if (!results.isBeforeFirst() ) {
	                System.out.println("ER_JOB_DOES_NOT_EXIST");
	                sqlConfirmation("The specified job does not exist.");
	            }
	            //otherwise set the jobStatus box to the jobStatus stored in the database
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
	    * Save the status of a job (i.e. make active or suspend)
	    */
	   public void saveStatus() {
	    	 try {
	    		 	String jobField;
	    		 	int jobNumber;  
	    			String jobStatus;
	    			
			 		jobField = this.jobField.getText();
			 		jobNumber = Integer.parseInt(jobField);			 		
	    			jobStatus = this.jobStatus.getValue(); 

	    		//SQL
	             connection = Variables.getConnection();
	             ps = Variables.getPreparedStatement();

	             String query = "UPDATE jobs SET status = ? WHERE job_number = ?";
	             ps = connection.prepareStatement(query);
	             ps.setString(1, jobStatus);
	             ps.setInt(2, jobNumber);
	             ps.executeUpdate();
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
	   
	    public void sqlConfirmation(String message) {
        	Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.show();
    	}
	    
	    public void loadTaskStatus() {
			   try {
				   String taskField = this.taskField.getText();
				   int taskNumber = Integer.parseInt(taskField);
				   
				   connection = Variables.getConnection();
		           ps = Variables.getPreparedStatement();
		           
		           String query = "SELECT suspended FROM tasks WHERE tasks.id = ?";
		           ps = connection.prepareStatement(query);
		           ps.setInt(1, taskNumber);
		           
		           ResultSet results1 = ps.executeQuery();
		           
		           if (!results1.isBeforeFirst() ) {
		                System.out.println("ER_TASK_DOES_NOT_EXIST");
		                sqlConfirmation("The specified task does not exist.");
		            }
		           else {
		        	   String taskStatus;
		        	   while(results1.next()) {
		        		   	taskStatus = results1.getString("suspended"); 
		               		this.taskStatus.setValue(taskStatus);
		               		this.taskStatus.setDisable(false);
		        	   }
		           }
		       }
			   catch(SQLException error) {
		            System.out.println("Error getting Task");
		            System.out.println(error);
		        }
		   }
	    
	    public void loadTaskCompleted() {
	    	try {
				   String taskField = this.taskField.getText();
				   int taskNumber = Integer.parseInt(taskField);
				   
				   connection = Variables.getConnection();
		           ps = Variables.getPreparedStatement();
		           
		           String query1 = "SELECT completed FROM tasks WHERE tasks.id = ?";
		           ps = connection.prepareStatement(query1);
		           ps.setInt(1, taskNumber);
		           
		           ResultSet results2 = ps.executeQuery();
		           
		           if (!results2.isBeforeFirst() ) {
		                System.out.println("ER_TASK_DOES_NOT_EXIST");
		                sqlConfirmation("The specified task does not exist.");
		            }
		           else {
		        	   String taskCompleted;
		        	   while(results2.next()) {
		               		taskCompleted = results2.getString("completed");
		               		this.taskCompleted.setValue(taskCompleted);
		               		this.taskCompleted.setDisable(false);
		        	   }
		           }
		       }
			   catch(SQLException error) {
		            System.out.println("Error getting Task");
		            System.out.println(error);
		        }
	    }
	    
	    public void saveTaskStatus() {
	    	 try {
	    		 	String taskField;
	    		 	int taskNumber;  
	    			String taskStatus;
	    				    			
			 		taskField = this.taskField.getText();
			 		taskNumber = Integer.parseInt(taskField);
			 		taskStatus = this.taskStatus.getValue(); 

	    		//SQL 	
	             connection = Variables.getConnection();
	             ps = Variables.getPreparedStatement();

	             //update query
	             String querySuspended = "UPDATE tasks SET suspended = ? WHERE tasks.id = ?";
	             ps = connection.prepareStatement(querySuspended);
	             ps.setString(1, taskStatus);
	             ps.setInt(2, taskNumber);
	             ps.executeUpdate();
	             sqlConfirmation("Task Status Updated Successfully");
	             	            
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
	    
	    public void saveTaskCompleted() {
	    	try {
    		 	String taskField;
    		 	int taskNumber;  
    			String taskCompleted;
    			
		 		taskField = this.taskField.getText();
		 		taskNumber = Integer.parseInt(taskField);
		 		taskCompleted = this.taskCompleted.getValue();

    		//SQL 	
             connection = Variables.getConnection();
             ps = Variables.getPreparedStatement();

             //update query             
             String queryCompleted = "UPDATE tasks SET completed = ? WHERE tasks.id =?";
             ps = connection.prepareStatement(queryCompleted);
             ps.setString(1, taskCompleted);
             ps.setInt(2, taskNumber);
             sqlConfirmation("Task completed Updated Successfully");
            
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
	    
	    public void loadTaskDate() {
	    	try {	
	    		String taskDateField = this.taskDateField.getText();
	        	int taskNumber1 = Integer.parseInt(taskDateField);
	        	
	    		connection = Variables.getConnection();
	    		ps = Variables.getPreparedStatement();
		          
	    		String query = "SELECT tasks.id, task_name, completed, suspended, completed_by\r\n" + 
	   						   "FROM tasks JOIN task_report ON tasks.id = task_report.taskID \r\n" + 
	           				   "WHERE tasks.completed = 0 AND tasks.suspended = 0 AND tasks.id = ?;";
    			ps = connection.prepareStatement(query);	    			
    			ps.setInt(1, taskNumber1);
    			   			
	    		ResultSet results2 = ps.executeQuery(); 
		        if (!results2.isBeforeFirst() ) {
		        	System.out.println("ER_TASK_DOES_NOT_EXIST");
		            sqlConfirmation("The specified task does not exist.");
		            }		                     		           
			   }
			   catch(SQLException error) {
		            System.out.println("Error getting data");
		            System.out.println(error);
		        }
	    	}       
}
  
	    
	    
	    


