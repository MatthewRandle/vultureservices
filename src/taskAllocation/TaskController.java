package taskAllocation;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import utils.SceneController;
import utils.Variables;
import utils.Task;

/*
 * method to set up the task allocation screen 
 */
public class TaskController implements Initializable { 
	Connection connection;
	PreparedStatement ps;
	@FXML private TableView  <Task> taskTable;
    @FXML private TableColumn<Task, String> taskNameCol;
    @FXML private TableColumn<Task, Integer> jobNumberCol;
    @FXML private TableColumn<Task, Boolean> completedCol;
    @FXML private TableColumn<Task, String> descriptionCol;
    @FXML private TableColumn<Task, Integer> durationCol;
    @FXML private TableColumn<Task, Integer> urgencyCol;
    @FXML private TableColumn<Task, Boolean> suspendedCol;
    @FXML private TableColumn<Task, String> usernameCol;
    @FXML private TableColumn<Task, String> dueDateCol;
    @FXML private TableColumn<Task, String> dateAssignedCol;
    public ChoiceBox<String> viewTaskText; 
    public ChoiceBox<String> assignTaskText;
    public TextField assignIdText;
    public TextField jobNumberText;
    public TextField durationText;
    public TextField urgencyText;
    public TextField dueDateText;
    public TextField dateAssignedText;

    public Label username;
    ObservableList<Task> taskList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
   
    	sortTaskName();
    	fillTables();
    	fillComboBoxes();
        taskTable.setItems(taskList);
		username.setText(Variables.getUserName());
    }
    
    /*
     * function to fill the columns in the table view, will be called at the end of every sorting method
     */
    public void fillTables()
    {
    jobNumberCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("jobNumber"));
    durationCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("taskDuration"));
    completedCol.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("taskComplete"));
    descriptionCol.setCellValueFactory(new PropertyValueFactory<Task, String>("taskNotes"));
    urgencyCol.setCellValueFactory(new PropertyValueFactory<Task, Integer>("taskUrgency"));
    usernameCol.setCellValueFactory(new PropertyValueFactory<Task, String>("assignedTo"));
    suspendedCol.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("taskSuspended"));
    taskNameCol.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
    dueDateCol.setCellValueFactory(new PropertyValueFactory<Task, String>("dueDate"));
    dateAssignedCol.setCellValueFactory(new PropertyValueFactory<Task, String>("assignedDate"));
    taskTable.setItems(taskList);
    }
    
    /*
     * fills the combo boxes with the current technicians on the database 
     * 
     * 
     */
    public void fillComboBoxes()
    {
    	try
    	{

            connection = Variables.getConnection();
            ps = Variables.getPreparedStatement();
    		String query = ("SELECT username FROM users WHERE user_types_id = 1 ");

              ps = connection.prepareStatement(query);

              ResultSet results = ps.executeQuery();
              
              while (results.next())
              {
             	viewTaskText.getItems().add(results.getString("username"));
             	assignTaskText.getItems().add(results.getString("username"));
    		}
    	}
    	
    	catch (SQLException e) {
			confirmation("ERROR Retrieving List of Technicians");
			e.printStackTrace();
		}      
    }
    
    
    /*
     * sorts the table view by the taskname column 
     */
    public void sortTaskName()
    {
    
    	
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	

          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number ORDER BY task_name");
        	  
        	  
        	  while (results.next())
        	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));;
        	     }

		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}
          fillTables();
    }

    /*
     * sorts the table view to only show tasks that have been completed (true)
     */
    public void sortCompleted()
    {
    
    	
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	

          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE completed = 1 ORDER BY tasks.id");
        	  
        	  
      	  
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
     	     }

		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}
          
          fillTables();

    }
    
    /*
     * sorts the table view to only show tasks that have been not been completed (false)
     */
    public void sortNotCompleted()
    {
    
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	
          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE completed = 0 ORDER BY tasks.id");
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
     	     }

		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}   
          fillTables();
    }
    
    /*
     * sorts the table view by the urgency of the task 
     */
    public void sortUrgency()
    {
    
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	
          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number ORDER BY urgency");
        	    	  
      	  
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
     	     }

		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}
          
          fillTables();
    }
    
    /*
     * sorts the table view to only show tasks that have been suspended (true)
     */
    public void sortSuspended()
    { 	
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	
          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE suspended = 1 ORDER BY tasks.id");
        	        	  
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
     	     }
		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}        
          fillTables();
    }
    
    /*
     * sorts the table view by the expected duration of the task 
     */
    public void sortDuration()
    { 	
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	
          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number ORDER BY duration");
        	          	  
      	  
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
     	     }
		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}         
          fillTables();
    }
    
    /*
     * sorts the table view to only show tasks that have been assigned (true)
     */
    public void sortAssigned()
    {   	
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	
          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE username <> '' ORDER BY tasks.id");
        	  
        	  
      	  
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
     	     }

		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}     
          fillTables();
    }
    
    /*
     * sorts the table view to only show tasks that have not been assigned (false)
     */
    public void sortUnassigned()
    { 	
    	  Connection con = Variables.getConnection();
    	  ps = Variables.getPreparedStatement();
    	
          try {
        	  taskTable.getItems().clear();
        	ResultSet results = con.createStatement().executeQuery("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE username = '' ORDER BY tasks.id");
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
     	     }

		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}       
          fillTables();
    }
    
    /*
     * sorts the table view by tasks that are overdue 
     */
    public void setOverDue()
    {	
    	 
          try {
        	  taskTable.getItems().clear();
          	Date date = new Date(System.currentTimeMillis());   
          	String dateS = date.toString();
            connection = Variables.getConnection();
            ps = Variables.getPreparedStatement();
       	

            String query = ("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE date_due < ? AND completed = 'false' ");
           
        	
        	   ps = connection.prepareStatement(query);
               ps.setString(1 , dateS);
               
               ResultSet results = ps.executeQuery();
        	   	  
        	  while (results.next())
     	     {
        		  taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
           				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
           				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
        		  }
        	  }
          
    
     	     
     	     catch (SQLException e) {
     			confirmation("SQL ERROR");
			e.printStackTrace();
		}
          fillTables();
    }
    
    /*
     * sorts the table view to only show tasks that are assigned to the technician name selected from the choice box 
     */
    public void searchUsername()
    {
    
          try {
        	  taskTable.getItems().clear();
        	  
        	String username1 = this.viewTaskText.getValue();
        	
        	if (username1.isEmpty())
        	{
        		confirmation("Please select a username");
        	}
        	
        	else
        	{
            connection = Variables.getConnection();
            ps = Variables.getPreparedStatement();

            String query = ("SELECT * FROM tasks JOIN jobs ON tasks.job_number = jobs.job_number WHERE username = ? ORDER BY duration");

            ps = connection.prepareStatement(query);
            ps.setString(1 , username1);

            ResultSet results = ps.executeQuery();
      	  
            while (results.next())
   	     {
            	taskList.add(new Task(results.getString("task_name"), results.getInt("job_number"), results.getString("description"),
         				  results.getInt("duration"), results.getBoolean("completed"), results.getInt("urgency"), results.getBoolean("suspended"),
         				  results.getString("username"),results.getString("date_due"),results.getString("date_assigned")));
   	     }

		} 
          }catch (SQLException e) {
			e.printStackTrace();
			confirmation("SQL ERROR");

		}      
          fillTables();
          
    }
       
    /*
     * method to fill the text fields with the selected task name, job number, urgency, assignedDate and dueDate when a task is selected from the tableview
     */
    public void fillTextFields()
    {
        	Task task =   taskTable.getSelectionModel().getSelectedItem();     	
        	String TaskName1 = task.getTaskName();
        	assignIdText.setText(TaskName1);
        	
        	int jobNumber1 = task.getJobNumber();
        	String jobNumberString = Integer.toString(jobNumber1);
        	jobNumberText.setText(jobNumberString);	
        	int duration1 = task.getTaskDuration();
        	String durationString = Integer.toString(duration1);
			durationText.setText(durationString);
			int urgencyInt = task.getTaskUrgency();
			String urgencyString = Integer.toString(urgencyInt);
			urgencyText.setText(urgencyString);
			
			String assignedDateString = task.getAssignedDate();
			String dueDateString = task.getDueDate();
			
			dueDateText.setText(dueDateString);
			dateAssignedText.setText(assignedDateString);
			
        	

        
          }
   
    /*
     * method to update the user name column with the selected technician and automatically calculate the date assigned and date due using the duration field of the selected task 
     */
    public void assignTask()
    {
          try {
        	  taskTable.getItems().clear();
          //	Task task =   taskTable.getSelectionModel().getSelectedItem();     	
        	String username= this.assignTaskText.getValue();    
        	String taskName = assignIdText.getText();
        	String jobNumber = jobNumberText.getText();
        	String durationString = durationText.getText();
        	int durationInt = Integer.parseInt(durationString);
        	String urgencyString = urgencyText.getText();
        	int urgencyInt = Integer.parseInt(urgencyString);
        	
        	Date date = new Date(System.currentTimeMillis() + durationInt * 60 * 60 * 1000);     
        	Date currentDate = new Date(System.currentTimeMillis());     

            String dueDateString = date.toString();
            String currentDateString = currentDate.toString();

            connection = Variables.getConnection();
            ps = Variables.getPreparedStatement();
            

            String query = ("UPDATE tasks SET username = ? , date_due = ?, urgency = ?,date_assigned = ? WHERE task_name = ? AND job_number = ?");
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, dueDateString);
            ps.setInt(3, urgencyInt);
            ps.setString(4, currentDateString);
            ps.setString(5, taskName);
            ps.setString(6, jobNumber);
            
            ps.executeUpdate();            
            
        	  sortTaskName();
        	  confirmation(taskName + ": has been assigned to " + username);

		} catch (SQLException e) {
			confirmation("SQL ERROR");
			e.printStackTrace();
		}       
          fillTables();
    }
    
    /*
     * method to check that there has been a technician selected foam the choice box and that the task has not already been assigned 
     */
    public void checkAssign()
    {
    	Task task =   taskTable.getSelectionModel().getSelectedItem(); 
    	String username = task.getAssignedTo();
    	String selectedOption = assignTaskText.getValue();
    	if (username.isEmpty() && !(selectedOption == null))
    	{
    		  assignTask();
    	}
     	if (selectedOption == null)
    	{
    		confirmation("Please Select a Technician");
    	}	
     	else if(!username.isEmpty() && !(selectedOption == null) )
    	{
    	 confirmation("Task Is already Assigned");
    	}
    }
     
    /*
     * method to display a pop up if there is an error or confirmation is needed 
     * 
     * @param String this is the message that will appear with the popup 
     */
    public void confirmation(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
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

    /*public void setJobScreen() {
    	SceneController.activate("Job");
    }
    
    public void setLoadJobScreen() {
    	SceneController.activate("Job");
    }
    
    public void setTaskScreen() {
    	SceneController.activate("TaskAllocation");
    }
    
    public void setUserAccountScreen() {
    	SceneController.activate("userAccount");
    }
    
   */ 
	
}
