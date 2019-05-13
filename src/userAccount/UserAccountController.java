package userAccount;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import utils.Job;
import utils.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import utils.Variables;
import utils.Task;
import utils.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class UserAccountController implements Initializable {
    Connection con;
    PreparedStatement ps;
    @FXML Label username;
    @FXML Button notifications;
    @FXML Label totalCompletedJobs, totalActiveJobs, totalCompletedTasks, totalActiveTasks;

    @FXML private Pane techGroup, hrGroup, managerGroup;
    @FXML private TableView  <Task> currentTaskTable, overdueTaskTable, suspendedTaskTable;
    @FXML private TableColumn<Task, String> taskNameCol, descriptionCol, taskNameCol_suspended, descriptionCol_suspended, taskNameCol_overdue, descriptionCol_overdue;
    @FXML private TableColumn<Task, Integer> jobNumberCol, durationCol, urgencyCol, jobNumberCol_suspended, jobNumberCol_overdue, durationCol_overdue, urgencyCol_overdue;

    @FXML private TableView<Job> activeJobsTable, overdueJobsTable, partsNeededTable;
    @FXML private TableColumn<Job, String> clientCol, clientCol_overdue, clientCol_hr, partsNeededCol_hr;
    @FXML private TableColumn<Job, Date> arrivalDateCol, returnDateCol, arrivalDateCol_overdue, returnDateCol_overdue, returnDateCol_hr;
    @FXML private TableColumn<Job, Integer> jobNumberCol_job, labourTimeCol, jobNumberCol_job_overdue, labourTimeCol_overdue, jobNumberCol_hr;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameCol, roleCol;

    ObservableList<Task> currentTaskList = FXCollections.observableArrayList();
    ObservableList<Task> suspendedTaskList = FXCollections.observableArrayList();
    ObservableList<Task> overdueTaskList = FXCollections.observableArrayList();

    ObservableList<Job> activeJobList = FXCollections.observableArrayList();
    ObservableList<Job> overdueJobList = FXCollections.observableArrayList();
    ObservableList<Job> jobsThatNeedPartsList = FXCollections.observableArrayList();

    ObservableList<User> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        checkUserType();
        username.setText(Variables.getUserName());
        checkNotifications();

        //addEventListeners();
    }

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

    public void checkUserType() {
        if(Variables.getUserType().equals("Technician")) {
            getCurrentTasks();
            getSuspendedTasks();
            getOverdueTasks();
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
            addPartsEventListeners();
            addUsersEventListeners();
            hrGroup.setDisable(false);
            hrGroup.setVisible(true);
        }
    }

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

    public void getOverdueTasks() {
        try {
            String query = "select * from tasks WHERE username = ?;";
            ps = con.prepareStatement(query);
            ps.setString(1, Variables.getUserName());
            ResultSet results = ps.executeQuery();

            while (results.next()) {
                String dateDueString = results.getString("date_due");

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

    public void getJobsThatNeedParts() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("select * from jobs WHERE quoted_parts IS NOT NULL AND status != 'Suspended';");

            while (results.next()) {
                if(!results.getString("quoted_parts").isEmpty()) {
                    jobsThatNeedPartsList.add(
                            new Job(
                                    results.getString("client"),
                                    results.getInt("job_number"),
                                    results.getString("quoted_parts"),
                                    results.getDate("return_date")
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

        partsNeededTable.setItems(jobsThatNeedPartsList);
    }

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
                            userModalController.setUsername(rowData.getUsername());
                            userModalController.setUserRole(rowData.getType());

                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();
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

    public void newUser() {

    }

    public void orderMoreParts() {

    }

    public void orderNewParts() {

    }

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Notifications.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
        catch(IOException err) {
            err.printStackTrace();
        }
    }
}