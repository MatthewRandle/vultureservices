package userAccount;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jobcard.JobController;
import utils.Job;
import utils.Part;
import utils.SceneController;
import utils.Task;
import utils.User;
import utils.Variables;

/**
 * Acts as the home screen for the application after a user has logged in.
 * Loads data relevant to the users type.
 *
 * @author  Matthew Randle
 */

public class UserAccountController implements Initializable {
    Connection con;
    PreparedStatement ps;
    @FXML Label username;
    @FXML Button notifications, editAccount, jobs, taskAllocation, jobDelay;
    @FXML Label totalCompletedJobs, totalActiveJobs, totalCompletedTasks, totalActiveTasks;

    @FXML private Pane techGroup, hrGroup, managerGroup;
    @FXML private TableView  <Task> currentTaskTable, overdueTaskTable, suspendedTaskTable;
    @FXML private TableColumn<Task, String> taskNameCol, descriptionCol, taskNameCol_suspended, descriptionCol_suspended, taskNameCol_overdue, descriptionCol_overdue;
    @FXML private TableColumn<Task, Integer> jobNumberCol, durationCol, urgencyCol, jobNumberCol_suspended, jobNumberCol_overdue, durationCol_overdue, urgencyCol_overdue;

    @FXML private TableView<Job> activeJobsTable, overdueJobsTable, partsNeededTable;
    @FXML private TableColumn<Job, String> clientCol, clientCol_overdue, clientCol_hr, partsNeededCol_hr, manufacturerCol;
    @FXML private TableColumn<Job, Date> arrivalDateCol, returnDateCol, arrivalDateCol_overdue, returnDateCol_overdue, returnDateCol_hr;
    @FXML private TableColumn<Job, Integer> jobNumberCol_job, labourTimeCol, jobNumberCol_job_overdue, labourTimeCol_overdue, jobNumberCol_hr;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameCol, roleCol;

    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, String> partCol, availableCol;

    ObservableList<Task> currentTaskList = FXCollections.observableArrayList();
    ObservableList<Task> suspendedTaskList = FXCollections.observableArrayList();
    ObservableList<Task> overdueTaskList = FXCollections.observableArrayList();

    ObservableList<Job> activeJobList = FXCollections.observableArrayList();
    ObservableList<Job> overdueJobList = FXCollections.observableArrayList();
    ObservableList<Job> jobsThatNeedPartsList = FXCollections.observableArrayList();

    ObservableList<User> userList = FXCollections.observableArrayList();
    ObservableList<Part> partsList = FXCollections.observableArrayList();

    /**
     * Setup function for javafx. Gets connection variables and calls some setup functions.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        checkUserType();
        username.setText(Variables.getUserName());
        checkNotifications();
    }

    /**
     * Used by toolbar buttons to navigate to different scenes
     * @param event caused by the button
     */
    public void navigate(ActionEvent event) {
        Button source = (Button) event.getSource();

        if(source.getText().equals("Jobs")) {
            SceneController.activate("Job");
        }
        else if(source.getText().equals("Task Allocation")) {
            SceneController.activate("taskAllocation");
        }
        else if(source.getText().equals("Job Delay")) {
            SceneController.activate("jobDelay");
        }
    }

    /**
     * Checks the user type and calls methods to load data that is relevant to that type.
     * Also makes the correct elements visible.
     */
    public void checkUserType() {
        if(Variables.getUserType().equals("Technician")) {
            getCurrentTasks();
            getSuspendedTasks();
            getOverdueTasks();
            addAllTaskEventListeners();
            techGroup.setDisable(false);
            techGroup.setVisible(true);
        }
        else if(Variables.getUserType().equals("Manager")) {
            getActiveJobs();
            getOverdueJobs();
            getTotalCompletedJobs();
            getTotalActiveJobs();
            getTotalActiveTasks();
            getTotalCompletedTasks();
            managerGroup.setDisable(false);
            managerGroup.setVisible(true);
        }
        else if(Variables.getUserType().equals("HR")) {
            getUsers();
            getJobsThatNeedParts();
            getParts();
            addPartsEventListeners();
            addUsersEventListeners();
            addEditPartsEventListeners();
            hrGroup.setDisable(false);
            hrGroup.setVisible(true);
        }

        //remove header buttons for these user types
        if(Variables.getUserType().equals("HR") || Variables.getUserType().equals("Finance") || Variables.getUserType().equals("Customer Services")) {
            jobs.setDisable(true);
            jobs.setVisible(false);
            taskAllocation.setDisable(true);
            taskAllocation.setVisible(false);
            jobDelay.setDisable(true);
            jobDelay.setVisible(false);
        }
    }

    /**
     * Gets all tasks for the user that are suspended and loads them into the table
     */
    public void getSuspendedTasks() {
        try {
            String query = "select * from tasks JOIN users ON users.username = tasks.username where suspended = 1 AND users.username = ?;";
            ps = con.prepareStatement(query);
            ps.setString(1, Variables.getUserName());

            ResultSet results = ps.executeQuery();

            while (results.next()) {
                suspendedTaskList.add(
                        new Task(
                                results.getString("task_name"),
                                results.getInt("job_number"),
                                results.getString("description"),
                                results.getInt("duration"),
                                results.getInt("urgency")
                        )
                );
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        taskNameCol_suspended.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        jobNumberCol_suspended.setCellValueFactory(new PropertyValueFactory<>("jobNumber"));
        descriptionCol_suspended.setCellValueFactory(new PropertyValueFactory<>("description"));

        suspendedTaskTable.setItems(suspendedTaskList);
    }

    /**
     * Gets the current tasks for the user and loads them into the correct table
     */
    public void getCurrentTasks() {
        try {
            String query = "select * from tasks JOIN users ON users.username = tasks.username where users.username = ?;";
            ps = con.prepareStatement(query);
            ps.setString(1, Variables.getUserName());

            ResultSet results = ps.executeQuery();

            while (results.next()) {
                currentTaskList.add(
                        new Task(
                                results.getString("task_name"),
                                results.getInt("job_number"),
                                results.getString("description"),
                                results.getInt("duration"),
                                results.getInt("urgency")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        taskNameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        jobNumberCol.setCellValueFactory(new PropertyValueFactory<>("jobNumber"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

        currentTaskTable.setItems(currentTaskList);
    }

    /**
     * Gets the overdue tasks for that user and loads them into the table
     */
    public void getOverdueTasks() {
        try {
            String query = "select * from tasks WHERE username = ?;";
            ps = con.prepareStatement(query);
            ps.setString(1, Variables.getUserName());
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                String dateDueString = results.getString("date_due");

                //we only need to row if there is a date due string
                if(dateDueString != null) {
                    //if the date has passed
                    if(new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(dateDueString).before(new Date())) {
                        overdueTaskList.add(
                                new Task(
                                        results.getString("task_name"),
                                        results.getInt("job_number"),
                                        results.getString("description"),
                                        results.getInt("duration"),
                                        results.getInt("urgency")
                                )
                        );
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch(ParseException err) {
            err.printStackTrace();
        }

        taskNameCol_overdue.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        jobNumberCol_overdue.setCellValueFactory(new PropertyValueFactory<>("jobNumber"));
        durationCol_overdue.setCellValueFactory(new PropertyValueFactory<>("duration"));
        descriptionCol_overdue.setCellValueFactory(new PropertyValueFactory<>("description"));
        urgencyCol_overdue.setCellValueFactory(new PropertyValueFactory<>("urgency"));

        overdueTaskTable.setItems(overdueTaskList);
    }

    /**
     * Gets all the active jobs and loads into the table
     */
    public void getActiveJobs() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select * from jobs WHERE status = 'Active';");

            while (results.next()) {
                activeJobList.add(
                        new Job(
                                results.getString("client"),
                                results.getInt("job_number"),
                                results.getInt("labour_time"),
                                results.getDate("arrival_date"),
                                results.getDate("return_date")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));
        jobNumberCol_job.setCellValueFactory(new PropertyValueFactory<>("jobNumber"));
        arrivalDateCol.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        labourTimeCol.setCellValueFactory(new PropertyValueFactory<>("labourTime"));

        activeJobsTable.setItems(activeJobList);
    }

    /**
     * Gets all overdue jobs
     */
    public void getOverdueJobs() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select * from jobs WHERE end_date IS NULL;");

            while (results.next()) {
                if(results.getDate("return_date") != null) {
                    //if job is overdue
                    if(results.getDate("return_date").before(new Date())) {
                        overdueJobList.add(
                                new Job(
                                        results.getString("client"),
                                        results.getInt("job_number"),
                                        results.getInt("labour_time"),
                                        results.getDate("arrival_date"),
                                        results.getDate("return_date")
                                )
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clientCol_overdue.setCellValueFactory(new PropertyValueFactory<>("client"));
        jobNumberCol_job_overdue.setCellValueFactory(new PropertyValueFactory<>("jobNumber"));
        arrivalDateCol_overdue.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        returnDateCol_overdue.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        labourTimeCol_overdue.setCellValueFactory(new PropertyValueFactory<>("labourTime"));

        overdueJobsTable.setItems(overdueJobList);
    }

    /**
     * Gets a total of all completed jobs and sets a label with the value
     */
    public void getTotalCompletedJobs() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select count(*) from jobs WHERE end_date IS NOT NULL;");

            while (results.next()) {
                totalCompletedJobs.setText(Integer.toString(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the total active jobs and sets a label with the value
     */
    public void getTotalActiveJobs() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select count(*) from jobs WHERE status = 'Active';");

            while (results.next()) {
                totalActiveJobs.setText(Integer.toString(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the total completed tasks and sets a label with the value
     */
    public void getTotalCompletedTasks() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select count(*) from tasks WHERE date_completed IS NOT NULL;");

            while (results.next()) {
                totalCompletedTasks.setText(Integer.toString(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the total active tasks and sets a label with the value
     */
    public void getTotalActiveTasks() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select count(*) from tasks WHERE date_completed IS NULL;");

            while (results.next()) {
                totalActiveTasks.setText(Integer.toString(results.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the users from the database and loads them into users table
     */
    public void getUsers() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select * from users JOIN user_types ON users.user_types_id = user_types.id;");

            while (results.next()) {
                userList.add(
                        new User(
                                results.getString("username"),
                                results.getString("type")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        usersTable.setItems(userList);
    }

    /**
     * Gets all jobs that need parts and loads them into correct table
     */
    public void getJobsThatNeedParts() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select * from jobs WHERE quoted_parts IS NOT NULL AND status != 'Suspended';");

            while (results.next()) {
                //if the job has quoted parts
                if(!results.getString("quoted_parts").isEmpty()) {
                    jobsThatNeedPartsList.add(
                            new Job(
                                    results.getString("client"),
                                    results.getInt("job_number"),
                                    results.getString("quoted_parts"),
                                    results.getDate("return_date"),
                                    results.getString("manufacturer")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        jobNumberCol_hr.setCellValueFactory(new PropertyValueFactory<>("jobNumber"));
        clientCol_hr.setCellValueFactory(new PropertyValueFactory<>("client"));
        partsNeededCol_hr.setCellValueFactory(new PropertyValueFactory<>("quotedParts"));
        returnDateCol_hr.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        manufacturerCol.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        
        partsNeededTable.setItems(jobsThatNeedPartsList);
    }

    /**
     * Gets all the parts that exist on the system and loads them into parts table
     */
    public void getParts() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select * from parts;");

            while (results.next()) {
                partsList.add(
                        new Part(
                                results.getString("part"),
                                results.getString("available")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        partCol.setCellValueFactory(new PropertyValueFactory<>("part"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));

        partsTable.setItems(partsList);
    }

    /**
     * Adds event listeners to all the rows on the parts table.
     * When a row is double clicked the parts availability is chanegd
     */
    public void addEditPartsEventListeners() {
        partsTable.setRowFactory(tv -> {
            TableRow<Part> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        //if the row was double clicked
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            String currentAvailability;
                            Part rowData = row.getItem();

                            if(rowData.getAvailable().equals("Available")) {
                                currentAvailability = "Not Available";
                            }
                            else {
                                currentAvailability = "Available";
                            }

                            String query = "UPDATE parts SET available = ? WHERE part = ?;";
                            ps = con.prepareStatement(query);
                            ps.setString(1, currentAvailability);
                            ps.setString(2, rowData.getPart());
                            ps.executeUpdate();
                            partsList = FXCollections.observableArrayList();
                            getParts();
                        }
                    }
                    catch(SQLException err) {
                        err.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    /**
     * Calls the function to add event listeners for each task table
     */
    public void addAllTaskEventListeners() {
        addTaskEventListeners(currentTaskTable);
        addTaskEventListeners(overdueTaskTable);
        addTaskEventListeners(suspendedTaskTable);
    }

    /**
     * Adds event listeners to all rows of a given task table table.
     * When a row is clicked the job that the row corresponds to is loaded.
     * @param table
     */
    public void addTaskEventListeners(TableView<Task> table) {
        table.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        Task rowData = row.getItem();
                        JobController jobController = Variables.getJobController();
                        jobController.loadJob(rowData.getJobNumber());
                        SceneController.activate("Job");
                    }
                }
            });
            return row;
        });
    }

    /**
     * Adds events listeners for all the jobs that need parts table.
     * When a row is clicked the job is suspended and the order_parts table is updated.
     */
    public void addPartsEventListeners() {
        partsNeededTable.setRowFactory(tv -> {
            TableRow<Job> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            Job rowData = row.getItem();
                            String query = "UPDATE jobs SET status = 'Suspended' WHERE job_number = ?;";
                            ps = con.prepareStatement(query);
                            ps.setInt(1, rowData.getJobNumber());
                            ps.executeUpdate();
                            
                            int frequency = -1;
                            
                            query = "SELECT frequency FROM order_parts WHERE motor = ?;";
                            ps = con.prepareStatement(query);
                            ps.setString(1, rowData.getManufacturer());
                            ResultSet results = ps.executeQuery();
                            
                            while(results.next()) {
                            	frequency = results.getInt("frequency");
                            }
                            
                            //doesn't exist yet
                            if(frequency == -1) {
                            	query = "INSERT INTO order_parts (motor, frequency) VALUES (?, 1);";
                                ps = con.prepareStatement(query);
                                ps.setString(1, rowData.getManufacturer());
                                ps.executeUpdate();
                            }
                            else {
                            	query = "UPDATE order_parts SET frequency = ? WHERE motor = ?;";
                                ps = con.prepareStatement(query);
                                ps.setInt(1, frequency + 1);
                                ps.setString(2, rowData.getManufacturer());
                                ps.executeUpdate();
                            }                                 
                            
                            jobsThatNeedPartsList = FXCollections.observableArrayList();
                            getJobsThatNeedParts();
                        }
                    }
                    catch(SQLException err) {
                        err.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    /**
     * Adds event listeners to users table.
     * When a row is clicked the user modal class is loaded and the data for the clicked user is passed.
     */
    public void addUsersEventListeners() {
        usersTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            User rowData = row.getItem();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserModal.fxml"));
                            Parent root = loader.load();

                            UserModalController userModalController = loader.getController();
                            userModalController.setEditModal();
                            userModalController.setUsername(rowData.getUsername());
                            userModalController.setUserRole(rowData.getType());
                            userModalController.getUserID();

                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();

                            //reload users table to show changes
                            userList = FXCollections.observableArrayList();
                            getUsers();
                        }
                    }
                    catch(IOException err) {
                        err.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    /**
     * When the new user button is clicked this function loads up the user modal class
     * with the correct settings for creating a new user.
     */
    public void newUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserModal.fxml"));
            Parent root = loader.load();

            UserModalController userModalController = loader.getController();
            userModalController.setNewModal();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            //reload users table to show changes
            userList = FXCollections.observableArrayList();
            getUsers();
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }

    /**
     * When the edit account button is clicked this function loads up the user modal class
     * with the correct settings for editing your own information (current logged in user)
     */
    public void editSelf() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserModal.fxml"));
            Parent root = loader.load();

            UserModalController userModalController = loader.getController();
            userModalController.setUsername(Variables.getUserName());
            userModalController.getUserID();
            userModalController.getPassword();
            userModalController.setEditSelfModal();
            userModalController.getUserID();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }

    /**
     * Checks if the user that is logged in has any notifications, if they do
     * the notifications button is made visible
     */
    public void checkNotifications() {
        try {
            String query = "select * from alerts JOIN user_types ON user_types.id = alerts.user_type WHERE user_types.type = ?;";
            ps = con.prepareStatement(query);
            ps.setString(1, Variables.getUserType());
            ResultSet results = ps.executeQuery();
            boolean haveAlerts = results.next();

            if(haveAlerts) {
                notifications.setDisable(false);
                notifications.setVisible(true);
                notifications.setManaged(true);
            }
            else {
                notifications.setDisable(true);
                notifications.setVisible(false);
                notifications.setManaged(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *If the user that is logged in has any notifications the notifications class is loaded.
     */
    public void showNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Notifications.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            checkNotifications();
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }

    /**
     * When logout button is clicked this function returns the user to the login page
     */
    public void logout() {
        SceneController.activate("login");
    }
}