package jobcard;

import utils.Variables;

import utils.SceneController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import taskAllocation.Task;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * JobController controls the Job Card interface, providing database connection and formats for the interface.
 * @author Ryan Pickering - 17013352
 * @version 1
 * @date 08/05/2019
 */

public class JobController implements Initializable {

    Connection connection;
    PreparedStatement ps;

    /* Job Variables */
    public TextField jobField, clientID, motorID, manufacturer, manufactureYear, labourTime, checkedBy, inspectedBy;
    public DatePicker arrivalDate, returnDate, checkedDate, inspectedDate;
    public TextArea quotedParts;
    public ChoiceBox < String > jobStatus;
    public CheckBox approved;
    public boolean task1Complete, task2Complete, task3Complete, task4Complete, task5Complete, task6Complete,
    task7Complete, task8Complete;

    /* Task Variables */
    public TextField[] taskTimeList, taskAssignedList;
    public TextArea[] taskNotesList;
    public TextArea[] taskNameList;
    public CheckBox[] checkBoxList;
    public ArrayList < Task > taskArrayList, loadedTaskList;
    public ToggleButton[] taskCompletedList;
    public TextArea taskName1, taskName2, taskName3, taskName4, taskName5, taskName6, taskName7, taskName8, taskName9;
    public TextArea taskNotes1, taskNotes2, taskNotes3, taskNotes4, taskNotes5, taskNotes6, taskNotes7, taskNotes8, taskNotes9;
    public TextField taskTime1, taskTime2, taskTime3, taskTime4, taskTime5, taskTime6, taskTime7, taskTime8, taskTime9;
    public TextField taskAssigned1, taskAssigned2, taskAssigned3, taskAssigned4, taskAssigned5, taskAssigned6, taskAssigned7, taskAssigned8, taskAssigned9;
    public CheckBox suspended1, suspended2, suspended3, suspended4, suspended5, suspended6, suspended7, suspended8, suspended9;
    public ToggleButton taskComplete1, taskComplete2, taskComplete3, taskComplete4, taskComplete5, taskComplete6, taskComplete7, taskComplete8, taskComplete9;
    public Pane topPane, bottomPane;
    public int index;

    /**
     * Calls methods as soon as the scene has been loaded
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = Variables.getConnection();

        //Disables the bottom pane when the program starts.
        this.bottomPane.setDisable(true);
        // Initialises the Job Status choice box.
        jobStatus.getItems().addAll("Active", "Suspended");
        //Initialises the arraylists
        initialiseArrays();
        //Prevents strings being entered into the number text fields
        formatTextField(manufactureYear);
        formatTextField(labourTime);
        for (TextField textfield: taskTimeList) {
            formatTextField(textfield);
        }
    }

    /**
     * Creates a new job when the 'new' button is pressed.
     * Clears all of the existing values on the interface.
     * @param ActionEvent ae - the actionevent passed from the GUI
     */
    public void newJob(int jobNumber) {
        clearFields();
        this.jobField.setText(""+jobNumber);
    }

    private void clearFields() {
        // enables and clears all of the fields on the interface.
        this.bottomPane.setDisable(false);
        this.clientID.setDisable(false);
        //this.jobField.clear();
        this.clientID.clear();
        this.arrivalDate.setDisable(false);
        this.arrivalDate.setValue(null);
        this.returnDate.setDisable(false);
        this.returnDate.setValue(null);
        this.quotedParts.setDisable(false);
        this.quotedParts.clear();
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
        this.jobStatus.setValue(null);
        this.inspectedBy.setDisable(false);
        this.inspectedBy.clear();
        this.inspectedDate.setDisable(false);
        this.inspectedDate.setValue(null);
        this.approved.setDisable(false);
        this.approved.setSelected(false);
        this.taskName1.clear();
        for (TextArea textarea: taskNameList) {
            textarea.clear();
        }
        for (TextArea textarea: taskNotesList) {
            textarea.clear();
        }
        for (TextField textfield: taskTimeList) {
            textfield.clear();
        }
        for (TextField textfield: taskAssignedList) {
            textfield.clear();
        }
        for (CheckBox checkbox: checkBoxList) {
            checkbox.setSelected(false);
        }

        for (ToggleButton togglebutton: taskCompletedList) {
            togglebutton.setSelected(false);
        }
    }

    /**
     * Saves the values on the job card into a new job, or overwrites the existing
     * job.
     * @param ActionEvent ae - the actionevent passed from the GUI
     */
    public void save(ActionEvent ae) {
        try {
            saveTask(); //saves the task section
            // Job Details Section
            String jobField, clientID, quotedParts, motorID, manufacturer, manYear, labourTimeValue, checkedBy,
            jobStatus, inspectedBy;
            int jobNumber, manufactureYear, labourTime;
            LocalDate arrivalDate, returnDate, checkedDate, inspectedDate;
            boolean approved;

            /*
             * Gets the text from the job number fields and converts it to an integer.
             */
            jobField = this.jobField.getText();
            jobNumber = Integer.parseInt(jobField);

            /*
             * gets the values from all of the fields on the interface and stores them into
             * local variables.
             */
            arrivalDate = this.arrivalDate.getValue();
            returnDate = this.returnDate.getValue();
            quotedParts = this.quotedParts.getText();
            motorID = this.motorID.getText();
            clientID = this.clientID.getText();
            manufacturer = this.manufacturer.getText();
            manYear = this.manufactureYear.getText();
            manufactureYear = Integer.parseInt(manYear);
            labourTimeValue = this.labourTime.getText();
            labourTime = Integer.parseInt(labourTimeValue);
            checkedBy = this.checkedBy.getText();
            checkedDate = this.checkedDate.getValue();
            jobStatus = this.jobStatus.getValue();
            inspectedBy = this.inspectedBy.getText();
            inspectedDate = this.inspectedDate.getValue();
            approved = this.approved.isSelected();

            // Creates prepared statement
            ps = Variables.getPreparedStatement();

            // if job number does not exist then save new, else update current record
            String query = "INSERT INTO jobs (job_number, client, arrival_date, return_date, quoted_parts, motor_serial, manufacturer, manufacture_year," +
                "labour_time, checked_by, checked_date, inspected_by, inspected_date, status, approved) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE client=?, arrival_date=?, return_date=?, quoted_parts=?, motor_serial=?, manufacturer=?, manufacture_year=?, labour_time=?," +
                "checked_by=?, checked_date=?, inspected_by=?, inspected_date=?, status=?, approved=?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, jobNumber);
            ps.setString(2, clientID);
            ps.setDate(3, Date.valueOf(arrivalDate));
            ps.setDate(4, Date.valueOf(returnDate));
            ps.setString(5, quotedParts);
            ps.setString(6, motorID);
            ps.setString(7, manufacturer);
            ps.setInt(8, manufactureYear);
            ps.setInt(9, labourTime);
            ps.setString(10, checkedBy);
            ps.setDate(11, Date.valueOf(checkedDate));
            ps.setString(12, inspectedBy);
            ps.setDate(13, Date.valueOf(inspectedDate));
            ps.setString(14, jobStatus);
            ps.setBoolean(15, approved);
            ps.setString(16, clientID);
            ps.setDate(17, Date.valueOf(arrivalDate));
            ps.setDate(18, Date.valueOf(returnDate));
            ps.setString(19, quotedParts);
            ps.setString(20, motorID);
            ps.setString(21, manufacturer);
            ps.setInt(22, manufactureYear);
            ps.setInt(23, labourTime);
            ps.setString(24, checkedBy);
            ps.setDate(25, Date.valueOf(checkedDate));
            ps.setString(26, inspectedBy);
            ps.setDate(27, Date.valueOf(inspectedDate));
            ps.setString(28, jobStatus);
            ps.setBoolean(29, approved);
            ps.executeUpdate();
            confirmation("Job Updated Successfully");
        } catch (SQLException error) {
            confirmation("The Data Was Not Inserted Successfully");
            System.out.println("Error Inserting Into Database");
            System.out.println(error);
        } catch (RuntimeException error) {
            confirmation("The Data Was Not Inserted Successfully");
            System.out.println("Interface Error");
            System.out.println(error);
        }
    }

    /**
     * Saves the task section of the interface
     */
    public void saveTask() {

        //Gets the job number from the text field.
        String jobField = this.jobField.getText();
        int jobNumber = Integer.parseInt(jobField);

        //Creates task objects for each task row and adds them to an array.
        createTask(taskName1, jobNumber, taskNotes1, taskTime1, taskAssigned1, suspended1, taskComplete1);
        createTask(taskName2, jobNumber, taskNotes2, taskTime2, taskAssigned2, suspended2, taskComplete2);
        createTask(taskName3, jobNumber, taskNotes3, taskTime3, taskAssigned3, suspended3, taskComplete3);
        createTask(taskName4, jobNumber, taskNotes4, taskTime4, taskAssigned4, suspended4, taskComplete4);
        createTask(taskName5, jobNumber, taskNotes5, taskTime5, taskAssigned5, suspended5, taskComplete5);
        createTask(taskName6, jobNumber, taskNotes6, taskTime6, taskAssigned6, suspended6, taskComplete6);
        createTask(taskName7, jobNumber, taskNotes7, taskTime7, taskAssigned7, suspended7, taskComplete7);
        createTask(taskName8, jobNumber, taskNotes8, taskTime8, taskAssigned8, suspended8, taskComplete8);
        createTask(taskName9, jobNumber, taskNotes9, taskTime9, taskAssigned9, suspended9, taskComplete9);

        //Get the values for each task
        for (Task theTask: taskArrayList) {
            String taskName = theTask.getTaskName();
            String taskNotes = theTask.getTaskNotes();
            int taskTime = theTask.getTaskDuration();
            String taskAssigned = theTask.getAssignedTo();
            boolean suspended = theTask.isTaskSuspended();
            boolean complete = theTask.isTaskComplete();
            String query = null;

            try {
                // Creates prepared statement
                ps = Variables.getPreparedStatement();

                //If the row with the same task name and job number exists, then update
                if (checkRow(jobNumber, taskName)) {
                    query = "UPDATE tasks SET completed=?, description=?, duration=?, urgency=?, suspended=?, username=? \r\n" +
                        "WHERE task_name=? AND job_number=?";
                    ps = connection.prepareStatement(query);
                    ps.setBoolean(1, complete);
                    ps.setString(2, taskNotes);
                    ps.setInt(3, taskTime);
                    ps.setInt(4, 6); //change to urgency
                    ps.setBoolean(5, suspended);
                    ps.setString(6, taskAssigned);
                    ps.setString(7, taskName);
                    ps.setInt(8, jobNumber);
                    ps.executeUpdate();
                }
                //Otherwise insert a new row.
                else {
                    query = "INSERT INTO tasks (task_name, job_number, completed, description, duration, urgency, suspended, username) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    ps = connection.prepareStatement(query);
                    ps.setString(1, taskName);
                    ps.setInt(2, jobNumber);
                    ps.setBoolean(3, complete);
                    ps.setString(4, taskNotes);
                    ps.setInt(5, taskTime);
                    ps.setInt(6, 6); //change to urgency
                    ps.setBoolean(7, false); //change to supsnedned
                    ps.setString(8, taskAssigned);
                    ps.executeUpdate();
                }
                confirmation("Job Updated Successfully");
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
     * @param ae - The action event passed from the GUI
     */
    public void loadJob(int jobNumber) {
        clearFields();
        //Gets the job number from the text field
        //String jobField1 = this.jobField.getText();
        //int jobNumber = Integer.parseInt(jobField1);
        //Selects the job with the specified job number
        try {
            ps = Variables.getPreparedStatement();
            String query = "SELECT * FROM jobs WHERE jobs.job_number = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, jobNumber);
            ResultSet results = ps.executeQuery();

            //If there is no job for the specified job number
            if (!results.isBeforeFirst()) {
                System.out.println("ER_JOB_DOES_NOT_EXIST");
                confirmation("The specified job does not exist.");
                //Otherwise create local variables for each value
            } else {
                String clientID, quotedParts, motorID, manufacturer, checkedBy, inspectedBy, jobStatus;
                Date arrivalDate, returnDate, checkedDate, inspectedDate;
                int manufactureYear, labourTime;
                boolean approved;

                while (results.next()) {
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

                    /*Enables the text fields to be edited*/
                    this.bottomPane.setDisable(false);

                    this.jobField.setText(""+jobNumber);
                    this.jobField.setDisable(false);
                    
                    this.clientID.setText(clientID);
                    this.clientID.setDisable(false);

                    // Converts arrivalDate value to LocalDate
                    LocalDate arrivalDate1 = ((java.sql.Date) arrivalDate).toLocalDate();
                    this.arrivalDate.setValue(arrivalDate1);
                    this.arrivalDate.setDisable(false);

                    // Converts returnDate value to LocalDate
                    LocalDate returnDate1 = ((java.sql.Date) returnDate).toLocalDate();
                    this.returnDate.setValue(returnDate1);
                    this.returnDate.setDisable(false);

                    this.quotedParts.setText(quotedParts);
                    this.quotedParts.setDisable(false);

                    this.motorID.setText(motorID);
                    this.motorID.setDisable(false);

                    this.manufacturer.setText(manufacturer);
                    this.manufacturer.setDisable(false);

                    this.manufactureYear.setText("" + manufactureYear);
                    this.manufactureYear.setDisable(false);

                    this.labourTime.setText("" + labourTime);
                    this.labourTime.setDisable(false);

                    this.checkedBy.setText(checkedBy);
                    this.checkedBy.setDisable(false);

                    LocalDate checkedDate1 = ((java.sql.Date) checkedDate).toLocalDate();
                    this.checkedDate.setValue(checkedDate1);
                    this.checkedDate.setDisable(false);

                    this.jobStatus.setValue(jobStatus);
                    this.jobStatus.setDisable(false);

                    this.inspectedBy.setText(inspectedBy);
                    this.inspectedBy.setDisable(false);

                    LocalDate inspectedDate1 = ((java.sql.Date) inspectedDate).toLocalDate();
                    this.inspectedDate.setValue(inspectedDate1);
                    this.inspectedDate.setDisable(false);

                    this.approved.setSelected(approved);
                    this.approved.setDisable(false);
                }
            }
        } catch (SQLException error) {
            System.out.println("Error getting Job");
            System.out.println(error);
        }
        loadTask(jobNumber);
    }

    /**
     * Loads the tasks for a specified job number
     * @param jobNumber - the job number to load tasks from
     */
    public void loadTask(int jobNumber) {
        String taskName, taskNotes, username;
        int duration, urgency;
        boolean completed, suspended;
        try {
            ps = Variables.getPreparedStatement();
            //Query to obtain all tasks with a specific job number, ordering them by most urgent.
            String query = "SELECT * FROM tasks WHERE tasks.job_number = ? ORDER BY urgency ASC";
            ps = connection.prepareStatement(query);
            ps.setInt(1, jobNumber);
            ResultSet results = ps.executeQuery();
            loadedTaskList = new ArrayList < Task > ();
            while (results.next()) {
                taskName = results.getString("task_name");
                completed = results.getBoolean("completed");
                suspended = results.getBoolean("suspended");
                taskNotes = results.getString("description");
                username = results.getString("username");
                duration = results.getInt("duration");
                urgency = results.getInt("urgency");
                //Creates a task object and adds it to an array
                loadedTaskList.add(new Task(taskName, jobNumber, taskNotes, duration, completed, urgency, suspended, username));
            }
        } catch (SQLException error) {
            System.out.println("Error getting Tasks");
            System.out.println(error);
        }
        displayTasks();
    }

    /**
     * Displays the tasks on the interface
     */
    public void displayTasks() {
        for (int i = 0; i < loadedTaskList.size(); i++) {
            Task currentTask = loadedTaskList.get(i);

            TextArea taskName = taskNameList[i];
            taskName.setText(currentTask.getTaskName());

            TextArea taskNotes = taskNotesList[i];
            taskNotes.setText(currentTask.getTaskNotes());

            TextField taskTime = taskTimeList[i];
            taskTime.setText("" + currentTask.getTaskDuration());

            TextField taskAssigned = taskAssignedList[i];
            taskAssigned.setText(currentTask.getAssignedTo());

            CheckBox suspended = checkBoxList[i];
            suspended.setSelected(currentTask.isTaskSuspended());

            ToggleButton taskComplete = taskCompletedList[i];
            taskComplete.setSelected(currentTask.isTaskComplete());
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
        TextField taskAssigned, CheckBox suspended, ToggleButton taskComplete) {
        if (!taskName.getText().isEmpty()) {
            String taskTimeString = taskTime.getText();
            int taskLength = Integer.parseInt(taskTimeString);
            taskArrayList.add(new Task(taskName.getText(), jobNumber, taskNotes.getText(), taskLength, taskComplete.isSelected(), 10, suspended.isSelected(), taskAssigned.getText()));
        }
    }

    /**
     * Prevent strings being entered into the specified text field
     * 
     * @param textfield - the specific textfield to modify
     * 
     * @return null - if its a string enter nothing
     * 
     * @return c - otherwise enter the integer
     */
    public void formatTextField(TextField textfield) {
        textfield.setTextFormatter(new TextFormatter < > (c -> {
            if (!c.getControlNewText().matches("\\d*")) {
                return null;
            } else {
                return c;
            }
        }));
    }

    public boolean checkRow(int jobNumber, String taskName) {
        boolean exists = false;
        try {
            ps = Variables.getPreparedStatement();

            String query = "SELECT * FROM tasks WHERE job_number=? and task_name=?;";
            ps = connection.prepareStatement(query);
            ps.setInt(1, jobNumber);
            ps.setString(2, taskName);

            ResultSet results = ps.executeQuery();
            if (!results.isBeforeFirst()) {
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

    public void setInspectedBy() {
        String username;
        username = Variables.getUserName();
        this.inspectedBy.setText("" + username);
    }

    public void setCheckedBy() {
        String username;
        username = Variables.getUserName();
        this.checkedBy.setText("" + username);
    }

    public void loadMenuScreen(ActionEvent ae) {
        SceneController.activate("menu");
    }

    public void initialiseArrays() {
        taskArrayList = new ArrayList < Task > ();
        taskNameList = new TextArea[] {
            taskName1,
            taskName2,
            taskName3,
            taskName4,
            taskName5,
            taskName5,
            taskName7,
            taskName8,
            taskName9
        };

        taskNotesList = new TextArea[] {
            taskNotes1,
            taskNotes2,
            taskNotes3,
            taskNotes4,
            taskNotes5,
            taskNotes6,
            taskNotes7,
            taskNotes8,
            taskNotes9
        };

        taskTimeList = new TextField[] {
            taskTime1,
            taskTime2,
            taskTime3,
            taskTime4,
            taskTime5,
            taskTime6,
            taskTime7,
            taskTime8,
            taskTime9
        };

        taskAssignedList = new TextField[] {
            taskAssigned1,
            taskAssigned2,
            taskAssigned3,
            taskAssigned4,
            taskAssigned5,
            taskAssigned6,
            taskAssigned7,
            taskAssigned8,
            taskAssigned9
        };

        checkBoxList = new CheckBox[] {
            suspended1,
            suspended2,
            suspended3,
            suspended4,
            suspended5,
            suspended6,
            suspended7,
            suspended8,
            suspended9
        };

        taskCompletedList = new ToggleButton[] {
            taskComplete1,
            taskComplete2,
            taskComplete3,
            taskComplete4,
            taskComplete5,
            taskComplete6,
            taskComplete7,
            taskComplete8,
            taskComplete9
        };

    }

    public void navigate(ActionEvent event) {
        Button source = (Button) event.getSource();

        if (source.getText().equals("Jobs")) {
            SceneController.activate("Job");
        } else
        if (source.getText().equals("Task Allocation")) {
            SceneController.activate("taskAllocation");
        } else
        if (source.getText().equals("Job Delay")) {
            SceneController.activate("jobDelay");
        }
    }

    /*
     * Display a confirmation message
     */
    public void confirmation(String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
    
    public void enterJobNumber(ActionEvent ae) {
    	TextInputDialog dialog = new TextInputDialog();
    	 
    	dialog.setTitle("Vulture Services");
    	dialog.setHeaderText("Enter a Job Number:");
    	dialog.setContentText("Job Number:");
    	 
    	Optional<String> result = dialog.showAndWait();
    	Integer jobNumber = Integer.valueOf(result.get());
    	result.ifPresent(name -> {
    	    newJob(jobNumber);
    	});
    }
    
    public void loadJobNumber(ActionEvent ae) {
    	TextInputDialog dialog = new TextInputDialog();
    	 
    	dialog.setTitle("Vulture Services");
    	dialog.setHeaderText("Enter a Job Number:");
    	dialog.setContentText("Job Number:");
    	 
    	Optional<String> result = dialog.showAndWait();
    	Integer jobNumber = Integer.valueOf(result.get());
    	result.ifPresent(name -> {
    	    loadJob(jobNumber);
    	});
    }
    
    /**
     * Exports the job card to a text file.
     * File path: Desktop/Vulture_Services
     * @param ae - The action event from the GUI
     */
    public void exportJob(ActionEvent ae) 
    {
    	//Converts the values from the job card into Java variables
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
        
        //Gets the current local date and time 
        SimpleDateFormat dateFormat= new SimpleDateFormat("'Date:' yyyy-MM-dd 'Time:' HH:mm:ss z");  
        Date date = new Date(System.currentTimeMillis());  
        
        //Path for the file to be printed to
        String filePath = System.getProperty("user.home") + "/Desktop/Vulture_Services";
        
        //If the file directory does not exist, create a new one
        File directory = new File(filePath);
        if (!directory.exists()) 
        {
        	directory.mkdirs();
        }
        
        //Print out the job card to the text file. File Name: 'JOB_jobnumber.txt'
        try (PrintWriter out = new PrintWriter(filePath + "/JOB_" + jobNumber + ".txt")) 
        { 
        	out.println("Vulture Services Job - " + dateFormat.format(date));
        	out.println("Client Name: "+clientID);
			out.println("Job Number: "+jobNumber);
			out.println("Arrival Date: "+arrivalDate);
			out.println("Return Date: "+returnDate);
			out.println("Quoted Parts: "+quotedParts);
			out.println("Motor Serial: "+motorID);
			out.println("Manufacturer: "+manufacturer);
			out.println("Manufacture Year: "+manufactureYear);
			out.println("Labour Time: "+labourTime);
			out.println("Inspected By: "+inspectedBy);
			out.println("Inspected Date: "+inspectedDate);
			out.println("Job Status: "+jobStatus);
    	//Catch file not found exception
        } catch (FileNotFoundException e) {
			confirmation("The File Was Not Found");
			e.printStackTrace();
    	} catch (Exception e) {
			confirmation("The file was not exported");
			e.printStackTrace();
		}
        //Confirm that the job card has been exported.
        confirmation("The Job Card Has Been Exported.");
    }
}