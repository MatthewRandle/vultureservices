package jobcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import taskAllocation.Task;
import utils.SceneController;
import utils.Variables;

/**
 * JobController controls the Job Card interface, providing database connection
 * and formats for the interface.
 * 
 * @author Ryan Pickering - 17013352
 * @version 1
 * @date 16/05/2019
 */

public class JobController implements Initializable 
{

	Connection connection;
	PreparedStatement ps;

	/* Job Variables */
	public TextField jobField, clientID, motorID, manufacturer, manufactureYear, labourTime, checkedBy, inspectedBy;
	public DatePicker arrivalDate, returnDate, checkedDate, inspectedDate;
	public TextArea quotedParts;
	public ChoiceBox<String> jobStatus;
	public CheckBox approved, notApproved;
	public boolean task1Complete, task2Complete, task3Complete, task4Complete, task5Complete, task6Complete,
			task7Complete, task8Complete;
	public Label username;

	/* Task Variables */
	public TextField[] taskTimeList, taskAssignedList;
	public TextArea[] taskNotesList, taskNameList;
	public CheckBox[] checkBoxList;
	public ToggleButton[] taskCompletedList;
	public ArrayList<Task> taskArrayList, loadedTaskList;
	public TextArea taskName1, taskName2, taskName3, taskName4, taskName5, taskName6, taskName7, taskName8, taskName9,
			taskNotes1, taskNotes2, taskNotes3, taskNotes4, taskNotes5, taskNotes6, taskNotes7, taskNotes8, taskNotes9;
	public TextField taskTime1, taskTime2, taskTime3, taskTime4, taskTime5, taskTime6, taskTime7, taskTime8, taskTime9,
			taskAssigned1, taskAssigned2, taskAssigned3, taskAssigned4, taskAssigned5, taskAssigned6, taskAssigned7,
			taskAssigned8, taskAssigned9;
	public CheckBox suspended1, suspended2, suspended3, suspended4, suspended5, suspended6, suspended7, suspended8,
			suspended9;
	public ToggleButton taskComplete1, taskComplete2, taskComplete3, taskComplete4, taskComplete5, taskComplete6,
			taskComplete7, taskComplete8, taskComplete9;
	public Pane topPane, bottomPane;
	public Button clearChecked, clearInspected;

	/**
	 * Calls methods as soon as the scene has been loaded
	 * 
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		//create a connection to the database
		connection = Variables.getConnection();

		// Disables the bottom pane when the program starts.
		this.bottomPane.setDisable(true);

		// Initialises the Job Status choice box.
		jobStatus.getItems().addAll("Active", "Suspended");

		// Initialises the arraylists
		initialiseArrays();

		// Prevents strings being entered into the number text fields
		formatTextField(manufactureYear);
		formatTextField(labourTime);

		// Prevent strings from being entered into each task time field
		for (TextField textfield : taskTimeList) 
		{
			formatTextField(textfield);
		}
		
		//Display who is currently logged in.
        username.setText(Variables.getUserName());
	}

	/**
	 * Creates a new job when the 'new' button is pressed. Clears all of the
	 * existing values on the interface. Checks in case a job with that ID exists
	 * 
	 * @param int
	 *            the new job number to create
	 */
	public void newJob(int jobNumber) 
	{
		taskArrayList = new ArrayList<>();
		// checks to see if a job with the specified number already exists
		if (!checkJob(jobNumber)) 
		{
			clearFields();
			this.jobField.setText("" + jobNumber);
		} else {
			confirmation("A Job with that number already exists.");
		}
	}

	/**
	 * Saves the values on the job card into a new job, or overwrites the existing
	 * job.
	 * 
	 * @param ActionEvent
	 *            ae - the actionevent passed from the GUI
	 */
	public void save(ActionEvent ae) 
	{
		try {
			//Save the task section
			saveTask();
			// Job Details Section
			String jobField, clientID, quotedParts, motorID, manufacturer, manYear, labourTimeValue, checkedBy,
					jobStatus, inspectedBy;
			int jobNumber, labourTime;
			Integer manufactureYear;
			LocalDate arrivalDateValue, returnDateValue, checkedDateValue, inspectedDateValue;
			Date arrivalDate, returnDate, checkedDate, inspectedDate;
			boolean approved, notApproved;

			// Gets the text from the fields and store them locally
			jobField = this.jobField.getText();
			jobNumber = Integer.parseInt(jobField);
			quotedParts = this.quotedParts.getText();
			motorID = this.motorID.getText();
			clientID = this.clientID.getText();
			manufacturer = this.manufacturer.getText();
			manYear = this.manufactureYear.getText();
			labourTimeValue = this.labourTime.getText();
			labourTime = Integer.parseInt(labourTimeValue);
			checkedBy = this.checkedBy.getText();
			jobStatus = this.jobStatus.getValue();
			inspectedBy = this.inspectedBy.getText();
			arrivalDateValue = this.arrivalDate.getValue();
			returnDateValue = this.returnDate.getValue();
			checkedDateValue = this.checkedDate.getValue();
			inspectedDateValue = this.inspectedDate.getValue();
			approved = this.approved.isSelected();
			notApproved = this.notApproved.isSelected();
			java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());

			// If the arrival date is empty, then make it null
			if (arrivalDateValue != null)
			{
				arrivalDate = Date.valueOf(arrivalDateValue);
			} else {
				arrivalDate = null;
			}
			
			// If the return date is empty, then make it null
			if (returnDateValue != null) 
			{
				returnDate = Date.valueOf(returnDateValue);
			} else {
				returnDate = null;
			}
			
			// If there is no manufacture year entered, then set it to 0
			if (!this.manufactureYear.getText().isEmpty()) 
			{
				manufactureYear = Integer.parseInt(manYear);
			} else {
				manufactureYear = 0;
			}

			//If there is no checked date, set it to null
			if (checkedDateValue != null) 
			{
				checkedDate = Date.valueOf(checkedDateValue);
			} else {
				checkedDate = null;
			}

			//if there is no inspected date, set it to null
			if (inspectedDateValue != null) 
			{
				inspectedDate = Date.valueOf(inspectedDateValue);
			} else {
				inspectedDate = null;
			}

			// Creates prepared statement
			ps = Variables.getPreparedStatement();

			// if job number does not exist then save new job, otherwise update current record
			String query = "INSERT INTO jobs (job_number, client, arrival_date, return_date, quoted_parts, motor_serial, manufacturer, manufacture_year,"
					+ "labour_time, checked_by, checked_date, inspected_by, inspected_date, status, approved, notApproved, start_date) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
					+ "ON DUPLICATE KEY UPDATE client=?, arrival_date=?, return_date=?, quoted_parts=?, motor_serial=?, manufacturer=?, manufacture_year=?, labour_time=?,"
					+ "checked_by=?, checked_date=?, inspected_by=?, inspected_date=?, status=?, approved=?, notApproved=?";
			ps = connection.prepareStatement(query);
			//set prepared values in the query
			ps.setInt(1, jobNumber);
			ps.setString(2, clientID);
			ps.setDate(3, arrivalDate);
			ps.setDate(4, returnDate);
			ps.setString(5, quotedParts);
			ps.setString(6, motorID);
			ps.setString(7, manufacturer);
			ps.setInt(8, manufactureYear);
			ps.setInt(9, labourTime);
			ps.setString(10, checkedBy);
			ps.setDate(11, checkedDate);
			ps.setString(12, inspectedBy);
			ps.setDate(13, inspectedDate);
			ps.setString(14, jobStatus);
			ps.setBoolean(15, approved);
			ps.setBoolean(16, notApproved);
			ps.setString(17, currentDate.toString());
			ps.setString(18, clientID);
			ps.setDate(19, arrivalDate);
			ps.setDate(20, returnDate);
			ps.setString(21, quotedParts);
			ps.setString(22, motorID);
			ps.setString(23, manufacturer);
			ps.setInt(24, manufactureYear);
			ps.setInt(25, labourTime);
			ps.setString(26, checkedBy);
			ps.setDate(27, checkedDate);
			ps.setString(28, inspectedBy);
			ps.setDate(29, inspectedDate);
			ps.setString(30, jobStatus);
			ps.setBoolean(31, approved);
			ps.setBoolean(32, notApproved);
			
			//checks to see if there is any errors in the input fields
			if (!displayInputErrors()) 
			{
				ps.executeUpdate();
				confirmation("Job Updated Successfully");
			}
		} catch (SQLException error) {
			confirmation("The Data Was Not Inserted Successfully");
			System.out.println("Error Inserting Into Database");
			System.out.println(error);
		} catch (RuntimeException error) {
			confirmation("The Data Was Not Inserted Successfully");
			System.out.println("Interface Error");
			error.printStackTrace();
			System.out.println(error);
		}
	}

	/**
	 * Saves the task section of the interface
	 */
	public void saveTask() 
	{
		// Gets the job number from the text field.
		String jobField = this.jobField.getText();
		int jobNumber = Integer.parseInt(jobField);

		// Creates task objects for each task row and adds them to an array.
		createTask(taskName1, jobNumber, taskNotes1, taskTime1, taskAssigned1, suspended1, taskComplete1);
		createTask(taskName2, jobNumber, taskNotes2, taskTime2, taskAssigned2, suspended2, taskComplete2);
		createTask(taskName3, jobNumber, taskNotes3, taskTime3, taskAssigned3, suspended3, taskComplete3);
		createTask(taskName4, jobNumber, taskNotes4, taskTime4, taskAssigned4, suspended4, taskComplete4);
		createTask(taskName5, jobNumber, taskNotes5, taskTime5, taskAssigned5, suspended5, taskComplete5);
		createTask(taskName6, jobNumber, taskNotes6, taskTime6, taskAssigned6, suspended6, taskComplete6);
		createTask(taskName7, jobNumber, taskNotes7, taskTime7, taskAssigned7, suspended7, taskComplete7);
		createTask(taskName8, jobNumber, taskNotes8, taskTime8, taskAssigned8, suspended8, taskComplete8);
		createTask(taskName9, jobNumber, taskNotes9, taskTime9, taskAssigned9, suspended9, taskComplete9);

		// Get the values for each task
		for (Task theTask : taskArrayList) 
		{
			String taskName = theTask.getTaskName();
			String taskNotes = theTask.getTaskNotes();
			int taskTime = theTask.getTaskDuration();
			String taskAssigned = theTask.getAssignedTo();
			boolean suspended = theTask.isTaskSuspended();
			boolean complete = theTask.isTaskComplete();
			String query = null;

			try 
			{
				// Creates prepared statement
				ps = Variables.getPreparedStatement();

				// If the row with the same task name and job number exists, then update
				if (checkRow(jobNumber, taskName)) 
				{
					query = "UPDATE tasks SET completed=?, description=?, duration=?, urgency=?, suspended=?, username=? \r\n"
							+ "WHERE task_name=? AND job_number=?";
					ps = connection.prepareStatement(query);
					ps.setBoolean(1, complete);
					ps.setString(2, taskNotes);
					ps.setInt(3, taskTime);
					ps.setInt(4, 6); // change to urgency
					ps.setBoolean(5, suspended);
					ps.setString(6, taskAssigned);
					ps.setString(7, taskName);
					ps.setInt(8, jobNumber);
					ps.executeUpdate();
				}
				// Otherwise insert a new row.
				else 
				{
					query = "INSERT INTO tasks (task_name, job_number, completed, description, duration, urgency, suspended, username) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					ps = connection.prepareStatement(query);
					ps.setString(1, taskName);
					ps.setInt(2, jobNumber);
					ps.setBoolean(3, complete);
					ps.setString(4, taskNotes);
					ps.setInt(5, taskTime);
					ps.setInt(6, 6); // change to urgency
					ps.setBoolean(7, false); // change to supsnedned
					ps.setString(8, taskAssigned);
					ps.executeUpdate();
				}
			} catch (SQLException error) {
				confirmation("The Data Was Not Inserted Successfully");
				System.out.println("Error Inserting Into Database");
				System.out.println(error);
			} catch (NumberFormatException error) {
				confirmation("You must enter a value for time needed");
				System.out.println("Number Format Error");
				System.out.println(error);
			} catch (RuntimeException error) {
				confirmation("The Data Was Not Inserted Successfully");
				System.out.println("Interface Error");
				System.out.println(error);
			}
		}
	}

	/**
	 * Loads a job by a specified job number
	 * 
	 * @param int jobNumber - the job number to load
	 */
	public void loadJob(int jobNumber) 
	{
		//clears the fields on the interface
		clearFields();

		try {
			ps = Variables.getPreparedStatement();
			//get the job with the specified job number
			String query = "SELECT * FROM jobs WHERE jobs.job_number = ?";
			ps = connection.prepareStatement(query);
			//set prepared value
			ps.setInt(1, jobNumber);
			ResultSet results = ps.executeQuery();

			// If there is no job for the specified job number
			if (!results.isBeforeFirst()) 
			{
				System.out.println("ER_JOB_DOES_NOT_EXIST");
				confirmation("The specified job does not exist.");
				// Otherwise create local variables for each value
			} else {
				String clientID, quotedParts, motorID, manufacturer, checkedBy, inspectedBy, jobStatus;
				Date arrivalDate, returnDate, checkedDate, inspectedDate;
				int manufactureYear, labourTime;
				boolean approved, notApproved;

				while (results.next()) 
				{
					clientID = results.getString("client");
					arrivalDate = results.getDate("arrival_date");
					returnDate = results.getDate("return_date");
					quotedParts = results.getString("quoted_parts");
					motorID = results.getString("motor_serial");
					manufacturer = results.getString("manufacturer");
					manufactureYear = results.getInt("manufacture_year");
					labourTime = results.getInt("labour_time");
					checkedBy = results.getString("checked_by");
					checkedDate = results.getDate("checked_date");
					inspectedBy = results.getString("inspected_by");
					inspectedDate = results.getDate("inspected_date");
					jobStatus = results.getString("status");
					approved = results.getBoolean("approved");
					notApproved = results.getBoolean("notApproved");

					/* Enables the text fields to be edited
					 * and sets loads the values into the interface */
					this.bottomPane.setDisable(false);
					this.jobField.setText("" + jobNumber);
					this.clientID.setText(clientID);
					this.clientID.setDisable(false);
					this.returnDate.setDisable(false);
					this.manufactureYear.setDisable(false);
					this.labourTime.setText("" + labourTime);
					this.labourTime.setDisable(false);
					this.checkedBy.setText(checkedBy);
					this.checkedBy.setDisable(false);
					this.quotedParts.setText(quotedParts);
					this.quotedParts.setDisable(false);
					this.motorID.setText(motorID);
					this.motorID.setDisable(false);
					this.manufacturer.setText(manufacturer);
					this.manufacturer.setDisable(false);
					this.arrivalDate.setDisable(false);
					this.checkedDate.setDisable(false);
					this.jobStatus.setValue(jobStatus);
					this.jobStatus.setDisable(false);
					this.inspectedBy.setText(inspectedBy);
					this.inspectedBy.setDisable(false);
					this.inspectedDate.setDisable(false);
					this.approved.setSelected(approved);
					this.approved.setDisable(false);
					this.notApproved.setSelected(notApproved);
					this.notApproved.setDisable(false);

					// Converts arrivalDate value to LocalDate
					if (arrivalDate != null) 
					{
						LocalDate arrivalDate1 = ((java.sql.Date) arrivalDate).toLocalDate();
						this.arrivalDate.setValue(arrivalDate1);
					}

					// Converts returnDate value to LocalDate
					if (returnDate != null) 
					{
						LocalDate returnDate1 = ((java.sql.Date) returnDate).toLocalDate();
						this.returnDate.setValue(returnDate1);
					}

					/*checks to see if the manufacture year is 0.
					 * if it is 0 then it means it was an empty value when
					 * entered into the database.
					 */
					if (manufactureYear != 0) 
					{
						this.manufactureYear.setText("" + manufactureYear);
					} else {
						this.manufactureYear.setText("");
					}
					
					//convert checked date to LocalDate
					if (checkedDate != null) {
						LocalDate checkedDate1 = ((java.sql.Date) checkedDate).toLocalDate();
						this.checkedDate.setValue(checkedDate1);
					}
					
					//convert inspected date to LocalDate
					if (inspectedDate != null) 
					{
						LocalDate inspectedDate1 = ((java.sql.Date) inspectedDate).toLocalDate();
						this.inspectedDate.setValue(inspectedDate1);
					}

				}
			}
		} catch (SQLException error) {
			System.out.println("Error getting Job");
			System.out.println(error);
		}
		//load the tasks with the specified job number
		loadTask(jobNumber);
	}

	/**
	 * Loads the tasks for a specified job number
	 * 
	 * @param jobNumber
	 *            - the job number to load tasks from
	 */
	public void loadTask(int jobNumber) 
	{
		String taskName, taskNotes, username;
		int duration, urgency;
		boolean completed, suspended;
		try {
			ps = Variables.getPreparedStatement();
			// Query to obtain all tasks with a specific job number, ordering them by most urgent
			String query = "SELECT * FROM tasks WHERE tasks.job_number = ? ORDER BY urgency ASC";
			ps = connection.prepareStatement(query);
			ps.setInt(1, jobNumber);
			ResultSet results = ps.executeQuery();
			loadedTaskList = new ArrayList<Task>();
			while (results.next()) 
			{
				taskName = results.getString("task_name");
				completed = results.getBoolean("completed");
				suspended = results.getBoolean("suspended");
				taskNotes = results.getString("description");
				username = results.getString("username");
				duration = results.getInt("duration");
				urgency = results.getInt("urgency");
				// Creates a task object and adds it to an array
				loadedTaskList.add(
						new Task(taskName, jobNumber, taskNotes, duration, completed, urgency, suspended, username));
			}
		} catch (SQLException error) {
			System.out.println("Error getting Tasks");
			System.out.println(error);
		}
		//displays the tasks on the interface
		displayTasks();
	}

	/**
	 * Displays the tasks on the interface
	 */
	public void displayTasks() 
	{
		//for each task object, get its values and display on the interface
		for (int i = 0; i < loadedTaskList.size(); i++) 
		{
			Task currentTask = loadedTaskList.get(i);

			TextArea taskName = taskNameList[i];
			taskName.setText(currentTask.getTaskName());

			TextArea taskNotes = taskNotesList[i];
			taskNotes.setText(currentTask.getTaskNotes());

			TextField taskTime = taskTimeList[i];
			taskTime.setText("" + currentTask.getTaskDuration());

			TextField taskAssigned = taskAssignedList[i];
			taskAssigned.setText(currentTask.getAssignedTo());

			ToggleButton taskComplete = taskCompletedList[i];
			taskComplete.setSelected(currentTask.isTaskComplete());

			CheckBox suspended = checkBoxList[i];
			suspended.setSelected(currentTask.isTaskSuspended());

			taskName.setDisable(currentTask.isTaskSuspended());
			taskNotes.setDisable(currentTask.isTaskSuspended());
			taskTime.setDisable(currentTask.isTaskSuspended());
			taskAssigned.setDisable(currentTask.isTaskSuspended());
			taskComplete.setDisable(currentTask.isTaskSuspended());

		}
	}

	/**
	 * Suspends a task row
	 * @param ae - the row to suspend
	 */
	public void suspendTask(ActionEvent ae) 
	{
		CheckBox checked = (CheckBox) ae.getSource();
		int position = 0;
		for (CheckBox checkbox : checkBoxList) 
		{
			if (checkbox.equals(checked)) 
			{
				TextArea taskName = taskNameList[position];
				TextArea taskNotes = taskNotesList[position];
				TextField taskTime = taskTimeList[position];
				TextField taskAssigned = taskAssignedList[position];
				ToggleButton taskComplete = taskCompletedList[position];

				taskName.setDisable(checked.isSelected());
				taskNotes.setDisable(checked.isSelected());
				taskTime.setDisable(checked.isSelected());
				taskAssigned.setDisable(checked.isSelected());
				taskComplete.setDisable(checked.isSelected());
			}
			position += 1;
		}
	}

	/**
	 * Creates a task object and adds it to an array
	 * @param taskName - the task name
	 * @param jobNumber - the job number
	 * @param taskNotes - the task description
	 * @param taskTime - the duration of the task
	 * @param taskAssigned - who the task is assigned to
	 * @param suspended - if the task is suspended
	 * @param taskComplete - if the task is complete
	 */
	public void createTask(TextArea taskName, int jobNumber, TextArea taskNotes, TextField taskTime,
			TextField taskAssigned, CheckBox suspended, ToggleButton taskComplete) 
	{
		//if the task name field is not empty, then add it to an array
		if (!taskName.getText().isEmpty()) 
		{
			String taskTimeString = taskTime.getText();
			int taskLength = Integer.parseInt(taskTimeString);
			taskArrayList.add(new Task(taskName.getText(), jobNumber, taskNotes.getText(), taskLength,
					taskComplete.isSelected(), 10, suspended.isSelected(), taskAssigned.getText()));
		}
	}

	/**
	 * Exports the job card to a text file. File path: Desktop/Vulture_Services
	 * 
	 * @param ae - The action event from the GUI
	 */
	public void exportJob(ActionEvent ae) 
	{
		// Converts the values from the job card into Java variables
		String jobField = this.jobField.getText();
		int jobNumber = Integer.parseInt(jobField);
		LocalDate arrivalDate = this.arrivalDate.getValue();
		LocalDate returnDate = this.returnDate.getValue();
		String quotedParts = this.quotedParts.getText();
		String motorID = this.motorID.getText();
		String clientID = this.clientID.getText();
		String manufacturer = this.manufacturer.getText();
		String manYear = this.manufactureYear.getText();
		int manufactureYear = Integer.parseInt(manYear);
		String labourTimeValue = this.labourTime.getText();
		int labourTime = Integer.parseInt(labourTimeValue);
		String jobStatus = this.jobStatus.getValue();
		String inspectedBy = this.inspectedBy.getText();
		LocalDate inspectedDate = this.inspectedDate.getValue();

		// Gets the current local date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("'Date:' yyyy-MM-dd 'Time:' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());

		// Path for the file to be printed to
		String filePath = System.getProperty("user.home") + "/Desktop/Vulture_Services";

		// If the file directory does not exist, create a new one
		File directory = new File(filePath);
		if (!directory.exists()) 
		{
			directory.mkdirs();
		}

		// Print out the job card to the text file. File Name: 'JOB_jobnumber.txt'
		try (PrintWriter out = new PrintWriter(filePath + "/JOB_" + jobNumber + ".txt")) 
		{
			out.println("Vulture Services Job - " + dateFormat.format(date));
			out.println("Client Name: " + clientID);
			out.println("Job Number: " + jobNumber);
			out.println("Arrival Date: " + arrivalDate);
			out.println("Return Date: " + returnDate);
			out.println("Quoted Parts: " + quotedParts);
			out.println("Motor Serial: " + motorID);
			out.println("Manufacturer: " + manufacturer);
			out.println("Manufacture Year: " + manufactureYear);
			out.println("Labour Time: " + labourTime);
			out.println("Inspected By: " + inspectedBy);
			out.println("Inspected Date: " + inspectedDate);
			out.println("Job Status: " + jobStatus);
			// Catch file not found exception
		} catch (FileNotFoundException e) {
			confirmation("The File Was Not Found");
			e.printStackTrace();
		} catch (Exception e) {
			confirmation("The file was not exported");
			e.printStackTrace();
		}
		// Confirm that the job card has been exported.
		confirmation("The Job Card Has Been Exported.");
	}

	/**
	 * Prevent strings being entered into the specified text field
	 * 
	 * @param textfield
	 *            - the specific textfield to modify
	 * 
	 * @return null - if its a string enter nothing
	 * 
	 * @return c - otherwise enter the integer
	 */
	public void formatTextField(TextField textfield) 
	{
		textfield.setTextFormatter(new TextFormatter<>(c -> 
		{
			if (!c.getControlNewText().matches("\\d*") && !c.getControlNewText().matches("\b")) 
			{
				return null;
			} else {
				return c;
			}
		}));
	}

	public boolean checkRow(int jobNumber, String taskName) 
	{
		boolean exists = false;
		try {
			ps = Variables.getPreparedStatement();

			String query = "SELECT * FROM tasks WHERE job_number=? and task_name=?;";
			ps = connection.prepareStatement(query);
			ps.setInt(1, jobNumber);
			ps.setString(2, taskName);

			ResultSet results = ps.executeQuery();
			if (!results.isBeforeFirst()) 
			{
				exists = false;
			} else {
				exists = true;
			}
		} catch (SQLException error) {
			System.out.println("Error getting Job");
			System.out.println(error);
		}
		return exists;
	}

	/**
	 * Updates the database when a task has been completed
	 * @param event
	 */
	public void completeTask(ActionEvent event) 
	{
		save(event);
		ToggleButton completedButton = (ToggleButton) event.getSource();
		//if the button has been deselected, then dont mark as completed
		if (!completedButton.isSelected()) 
		{
			return;
		}

		//Get the task name for the row that was completed
		TextArea taskName = null;
		int i = 0;
		for (ToggleButton togglebutton : taskCompletedList) 
		{
			if (togglebutton == completedButton) 
			{
				taskName = taskNameList[i];
			} else {
				i++;
			}
		}

		//insert into the database the time that the task was completed
		java.util.Date completedDate = new java.util.Date(System.currentTimeMillis());
		String dateString = completedDate.toString();
		String taskNameString = taskName.getText();
		String jobString = jobField.getText();
		int jobNumber = Integer.parseInt(jobString);
		try {
			ps = Variables.getPreparedStatement();

			String query = "UPDATE tasks SET date_completed = ? WHERE job_number = ? AND task_name = ?;";
			ps = connection.prepareStatement(query);
			ps.setString(1, dateString);
			ps.setInt(2, jobNumber);
			ps.setString(3, taskNameString);
			ps.executeUpdate();
		} catch (SQLException error) {
			System.out.println("Error");
			System.out.println(error);
		}
	}

	/**
	 * Checks if a job exists
	 * @param jobNumber - the job number to check
	 * @return true if it exists, false if not
	 */
	public boolean checkJob(int jobNumber) 
	{
		boolean exists = false;
		try {
			ps = Variables.getPreparedStatement();

			String query = "SELECT * FROM tasks WHERE job_number=?;";
			ps = connection.prepareStatement(query);
			ps.setInt(1, jobNumber);

			ResultSet results = ps.executeQuery();
			if (!results.isBeforeFirst()) 
			{
				exists = false;
			} else {
				exists = true;
			}
		} catch (SQLException error) {
			System.out.println("Error getting Job");
			System.out.println(error);
		}
		return exists;
	}

	public void setInspectedBy() 
	{
		String username;
		username = Variables.getUserName();
		this.inspectedBy.setText("" + username);
		this.inspectedDate.setValue(LocalDate.now());
	}

	public void setCheckedBy() 
	{
		String username;
		username = Variables.getUserName();
		this.checkedBy.setText("" + username);
		this.checkedDate.setValue(LocalDate.now());
	}

	public void clearField(ActionEvent ae) 
	{
		if (ae.getSource() == clearChecked) 
		{
			checkedBy.clear();
			checkedDate.setValue(null);
		} 
		else if (ae.getSource() == clearInspected) 
		{
			inspectedBy.clear();
			inspectedDate.setValue(null);
		}
	}

	/**
	 * Calculates the total labour time based on each task every time a key is pressed
	 * @param key
	 */
	public void calculateLabourTime(KeyEvent key) 
	{
		long totalLabourTime = 0;
		long timeNeeded = 0;

		for (TextField field : taskTimeList) 
		{
			if (!field.getText().equals("")) 
			{
				String textField = field.getText();
				timeNeeded += Long.parseLong(textField);
				totalLabourTime = timeNeeded;
			}
		}
		labourTime.setText("" + totalLabourTime);
	}

	/**
	 * Checks to see if the user has input invalid data into the fields
	 * 
	 * @return true if there are errors
	 * 
	 * @return false if there are no errors
	 */
	public boolean displayInputErrors() 
	{
		try {
			LocalDate arrivalDate = this.arrivalDate.getValue();
			LocalDate returnDate = this.returnDate.getValue();
			String jobNumber = this.jobField.getText();

			// Check that the Return Date is after the Arrival Date
			if (arrivalDate != null && returnDate != null && arrivalDate.isAfter(returnDate)) 
			{
				confirmation("You Cannot have Return Date before the Arrival Date");
				return true;
			}

			// Make sure the job number is not empty when saving a job
			if (jobNumber.trim().isEmpty()) 
			{
				confirmation("You Must Enter a Job Number");
				return true;
			}

			// Check to make sure that both the approved and not approved checkboxes arent
			// selected
			if (approved.isSelected() && notApproved.isSelected()) 
			{
				confirmation("A job can only be Approved or Not Approved.");
				return true;
			}
		} catch (NullPointerException exception) {
			System.out.println("No Dates Entered");
		} catch (NoSuchElementException exception) {
			System.out.println("No value entered");
		}
		return false;
	}

	/**
	 * Seperate utility function to initialise the arrays for the class
	 */
	public void initialiseArrays() 
	{
		taskArrayList = new ArrayList<Task>();
		taskNameList = new TextArea[] { taskName1, taskName2, taskName3, taskName4, taskName5, taskName5, taskName7,
				taskName8, taskName9 };

		taskNotesList = new TextArea[] { taskNotes1, taskNotes2, taskNotes3, taskNotes4, taskNotes5, taskNotes6,
				taskNotes7, taskNotes8, taskNotes9 };

		taskTimeList = new TextField[] { taskTime1, taskTime2, taskTime3, taskTime4, taskTime5, taskTime6, taskTime7,
				taskTime8, taskTime9 };

		taskAssignedList = new TextField[] { taskAssigned1, taskAssigned2, taskAssigned3, taskAssigned4, taskAssigned5,
				taskAssigned6, taskAssigned7, taskAssigned8, taskAssigned9 };

		checkBoxList = new CheckBox[] { suspended1, suspended2, suspended3, suspended4, suspended5, suspended6,
				suspended7, suspended8, suspended9 };

		taskCompletedList = new ToggleButton[] { taskComplete1, taskComplete2, taskComplete3, taskComplete4,
				taskComplete5, taskComplete6, taskComplete7, taskComplete8, taskComplete9 };
	}

	/**
	 * Display a confirmation pop up box with a specified string
	 */
	public void confirmation(String message) 
	{
		Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
	}
	
	/**
	 * Navigation method to change the scene
	 * @param event - gets the name of the button pressed to determine
	 * destination.
	 */
	 public void navigate(ActionEvent event) {
	        MenuItem source = (MenuItem) event.getSource();

	        if(source.getText().equals("Dashboard")) {
	            SceneController.activate("userAccount");
	        }
	    }

	// convert these to 1
	public void enterJobNumber(ActionEvent ae) 
	{
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Vulture Services");
		dialog.setHeaderText("Enter a Job Number:");
		dialog.setContentText("Job Number:");

		Optional<String> result = dialog.showAndWait();
		Integer jobNumber = Integer.valueOf(result.get());
		result.ifPresent(name -> 
		{
			newJob(jobNumber);
		});
	}

	public void loadJobNumber(ActionEvent ae) 
	{
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Vulture Services");
		dialog.setHeaderText("Enter a Job Number:");
		dialog.setContentText("Job Number:");

		Optional<String> result = dialog.showAndWait();
		Integer jobNumber = Integer.valueOf(result.get());
		result.ifPresent(name -> 
		{
			loadJob(jobNumber);
		});
	}

	public void setEndDate(ActionEvent event) 
	{
		java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());
		int jobNumber = Integer.parseInt(jobField.getText());
		System.out.println(jobNumber);

		try {
			// Creates prepared statement
			ps = Variables.getPreparedStatement();

			// if job number does not exist then save new, else update current record
			String query = "UPDATE jobs SET end_date = ? WHERE job_number = ?;";
			ps = connection.prepareStatement(query);
			ps.setString(1, currentDate.toString());
			ps.setInt(2, jobNumber);
			ps.executeUpdate();
			System.out.println("Updated");

		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		if (this.notApproved.isSelected()) 
		{
			try {
				int frequency = 0;
				// Creates prepared statement
				ps = Variables.getPreparedStatement();

				String query = "SELECT frequency FROM inspection_failure WHERE manufacturer = ?;";
				ps = connection.prepareStatement(query);
				ps.setString(1, this.manufacturer.getText());
				ResultSet results = ps.executeQuery();
				
				while (results.next()) 
				{
					frequency = results.getInt("frequency");
					frequency++;
				}

				String query1 = "INSERT INTO inspection_failure(manufacturer, frequency) VALUES (?, ?)"
						+ "ON DUPLICATE KEY UPDATE frequency = ?;";
				ps = connection.prepareStatement(query1);
				ps.setString(1, this.manufacturer.getText());
				ps.setInt(2, frequency);
				ps.setInt(3, frequency);
				ps.executeUpdate();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
	}

	/**
	 * Utility function to clear all fields.
	 */
	private void clearFields() 
	{
		this.bottomPane.setDisable(false);
		this.clientID.setDisable(false);
		this.clientID.clear();
		this.arrivalDate.setDisable(false);
		this.arrivalDate.setValue(null);
		this.returnDate.setDisable(false);
		this.returnDate.setValue(null);
		this.quotedParts.setDisable(false);
		this.quotedParts.clear();
		this.clearChecked.setDisable(false);
		this.motorID.setDisable(false);
		this.motorID.clear();
		this.manufacturer.setDisable(false);
		this.manufacturer.clear();
		this.manufactureYear.setDisable(false);
		this.manufactureYear.clear();
		this.labourTime.setDisable(false);
		this.labourTime.clear();
		this.checkedBy.setDisable(false);
		this.checkedBy.clear();
		this.checkedDate.setDisable(false);
		this.checkedDate.setValue(null);
		this.jobStatus.setDisable(false);
		this.jobStatus.setValue("Active");
		this.inspectedBy.setDisable(false);
		this.inspectedBy.clear();
		this.inspectedDate.setDisable(false);
		this.inspectedDate.setValue(null);
		this.approved.setDisable(false);
		this.approved.setSelected(false);
		this.notApproved.setDisable(false);
		this.notApproved.setSelected(false);
		this.taskName1.clear();

		for (TextArea textarea : taskNameList) 
		{
			textarea.clear();
			textarea.setDisable(false);
		}
		for (TextArea textarea : taskNotesList) 
		{
			textarea.clear();
			textarea.setDisable(false);
		}
		for (TextField textfield : taskTimeList) 
		{
			textfield.clear();
			textfield.setDisable(false);
		}
		for (TextField textfield : taskAssignedList) 
		{
			textfield.clear();
			textfield.setDisable(false);
		}
		for (CheckBox checkbox : checkBoxList) 
		{
			checkbox.setSelected(false);
			checkbox.setDisable(false);
		}
		for (ToggleButton togglebutton : taskCompletedList) 
		{
			togglebutton.setSelected(false);
			togglebutton.setDisable(false);
		}
	}
}