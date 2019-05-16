package jobdelay;
import utils.Variables;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import utils.SceneController;

/**
 * JobDelayController controls the Job Delay interface, providing database connection
 * and formats for the interface.
 * 
 * @author Ethan Roe - 16006250
 * @version 1
 * @date 16/05/2019
 */

public class JobDelayController implements Initializable {

	Connection connection;
	PreparedStatement ps;
	
	public TextField jobField, taskField, taskCompleted, taskField1, taskDateField, jobField2;
	public ChoiceBox<String> taskStatus, jobStatus;
	public Label username;
	
	/**
	 * Calls methods as soon as the scene has been loaded
	 * 
	 * @param location
	 * @param resources
	 */
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		jobStatus.getItems().addAll("Active", "Suspended");
		taskStatus.getItems().addAll("1", "0");
		
		username.setText(Variables.getUserName());
	}
	
	/*
	 * Loads job info from the database when u click the load job button
	 */
	   public void loadJob() {
	        try {
	        	String jobField1 = this.jobField.getText();
	        	int jobNumber = Integer.parseInt(jobField1);

	            connection = Variables.getConnection();
	            ps = Variables.getPreparedStatement();

	            String query = "SELECT status FROM jobs WHERE jobs.job_number = ?";
	            ps = connection.prepareStatement(query);
	            ps.setInt(1, jobNumber);

	            ResultSet results = ps.executeQuery();

	            if (!results.isBeforeFirst() ) {
	                System.out.println("ER_JOB_DOES_NOT_EXIST");
	                sqlConfirmation("The specified job does not exist.");
	            }
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
	    		 	String jobField = this.jobField.getText();
	    		 	int jobNumber = Integer.parseInt(jobField);	
	    			String jobStatus = this.jobStatus.getValue(); 
	    			
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
	    
	    /*
	     * Loads the task status from database (active or suspended)
	     */
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
	    
	    /*
	     * saves the status of the task, if it is changed, to the database
	     */
	    public void saveTaskStatus() {
	    	 try {
	    		 String taskField = this.taskField.getText();
	    		 int taskNumber = Integer.parseInt(taskField);
	    		 String taskStatus = this.taskStatus.getValue(); 
	    		 
	             connection = Variables.getConnection();
	             ps = Variables.getPreparedStatement();

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
	    
	    
	    /*
	     * Alerts the technicians to any tasks that are not completed when task number is entered
	     */
	    public void alertTechnician() {
	    	try {
	    		String taskCompleted = this.taskCompleted.getText();
				int taskNumber = Integer.parseInt(taskCompleted);
				String alert = "Task with ID: " + taskNumber + " is remaining from the previous day.";
				
				connection = Variables.getConnection();
		        ps = Variables.getPreparedStatement();
		        String query1 = "SELECT tasks.id, completed FROM tasks WHERE tasks.id = ? AND completed = 0";
		        ps = connection.prepareStatement(query1);
		        ps.setInt(1, taskNumber);
		           
		        ResultSet results = ps.executeQuery();
		        if (!results.isBeforeFirst() ) {
		             System.out.println("ER_TASK_DOES_NOT_EXIST");
		             sqlConfirmation("The specified task does not exist, or is not remaining from the previous day.");
		        	}
		        else {
		        	String alertQuery = "INSERT INTO alerts (alert, user_type) VALUES (?, ?)";
				    ps = connection.prepareStatement(alertQuery);
				    ps.setString(1, alert);
				    ps.setInt(2, 1);
				    ps.executeUpdate();
			        sqlConfirmation("Technicians alerted successfully");
			        }
		        }
	    	catch(SQLException error) {
    			sqlConfirmation("The data was not inserted successfully.");
       	 		System.out.println("Error Inserting Into Database");
       	 		System.out.println(error);
    		}
   	 		catch(RuntimeException error) {
   	 			sqlConfirmation("Please fill in the task number field and ensure it is correct.");
   	 			System.out.println("Interface Error");
   	 			System.out.println(error);
   	 		}	
	    }
	    
	    /*
	     * Alerts management to any tasks that are overdue
	     */
	    public void alertManagement() {
	    	try {
	    		String taskField1 = this.taskField1.getText();
	        	int taskNumber1 = Integer.parseInt(taskField1);
	        	String alert = "Task with ID:  " + taskNumber1 + " has not been completed";
	        	Date date = new Date(System.currentTimeMillis());
	        	String day = date.toString();

	    		connection = Variables.getConnection();
	    		ps = Variables.getPreparedStatement();

	    		String query = "SELECT tasks.id, completed, suspended, date_assigned\r\n" + 
	   						   "FROM tasks \r\n" + 
	           				   "WHERE tasks.completed = 0 AND tasks.suspended = 0 AND tasks.id = ? AND date_assigned < ?";	       
		        
    			ps = connection.prepareStatement(query);	    			
    			ps.setInt(1, taskNumber1);
    			ps.setString(2, day);
    			   			
	    		ResultSet results2 = ps.executeQuery(); 

		        if (!results2.isBeforeFirst() ) {
		        	System.out.println("ER_TASK_DOES_NOT_EXIST");
		            sqlConfirmation("This task does not meet the required details to check if overdue.");
		            }    
		        else {
		        	String alertQuery2 = "INSERT INTO alerts (alert, user_type) VALUES (?, ?)";
			        ps = connection.prepareStatement(alertQuery2);
			        ps.setString(1, alert);
			        ps.setInt(2, 2);
			        ps.executeUpdate();
		            sqlConfirmation("Management alerted successfully");
		        	}
	    	}
	    	catch(SQLException error) {
	    		sqlConfirmation("The data was not inserted successfully.");
           		System.out.println("Error Inserting Into Database");
           		System.out.println(error);
           		}
       	 	catch(RuntimeException error) {
       	 		sqlConfirmation("Please fill in the task number field and ensure it is correct.");
       	 		System.out.println("Interface Error");
       	 		System.out.println(error);
       	 		}
	    	}
	    	    
	    /*
	     * Alerts customer services to jobs that are expected to take longer or will take longer than expected
	     */
	    public void alertCustomerServices() {
	    	try {
	    		String jobField2 = this.jobField2.getText();
		    	int jobNumber = Integer.parseInt(jobField2);
		    	String alert = "Job with number: " + jobNumber + " is taking longer than expected or will take longer than expected";
	    		Date currentDate = new Date(System.currentTimeMillis());
		    	String day = currentDate.toString();

		    	connection = Variables.getConnection();
	    		ps = Variables.getPreparedStatement();

	    		String getJobs = "SELECT job_number, return_date, approved FROM jobs WHERE job_number = ? AND return_Date < ? AND approved = 0";

	    		ps = connection.prepareStatement(getJobs);
	    		ps.setInt(1, jobNumber);
	    		ps.setString(2, day);

	    		ResultSet results3 = ps.executeQuery();

	    		if (!results3.isBeforeFirst() ) {
		        	System.out.println("ER_JOB_DOES_NOT_EXIST");
		            sqlConfirmation("The job number entered is either incorrect or does not meet the criteria.");
		            }
	    		else {
	    			String alertQuery3 = "INSERT INTO alerts (alert, user_type) VALUES (?, ?)";
			        ps = connection.prepareStatement(alertQuery3);
			        ps.setString(1, alert);
			        ps.setInt(2, 6);
			        ps.executeUpdate();
		            sqlConfirmation("Customer services alerted successfully");
	    		}
	    	}
	    	catch(SQLException error) {
                sqlConfirmation("Data not inserted. Please fill in the job number field and ensure it is correct.");
           	 	System.out.println("Error Inserting Into Database");
                System.out.println(error);
            }
       	 	catch(RuntimeException error) {
	       		 sqlConfirmation("Data not inserted. Please fill in the job number field and ensure it is correct.");
	       		 System.out.println("Interface Error");
	       		 System.out.println(error);
       	 	}	
	    }
	    	
	    /*
	     * method for navigation for 'back' button 
	     * 
	     * @param ActionEvent ae - the actionevent passed from the GUI
	     */
	    public void navigate(ActionEvent event) {
	        Button source = (Button) event.getSource();

	        if(source.getText().equals("Back")) {
	            SceneController.activate("userAccount");
	        }
	    }
}