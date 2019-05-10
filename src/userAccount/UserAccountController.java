package userAccount;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userAccount.UserModalController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import utils.Variables;
import utils.Task;
import utils.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class UserAccountController implements Initializable {
    Connection con;
    PreparedStatement ps;
    @FXML Label username;
    @FXML private Pane techGroup, hrGroup, managerGroup;
    @FXML private TableView  <Task> currentTaskTable, overdueTaskTable, suspendedTaskTable;
    @FXML private TableColumn<Task, String> taskNameCol, descriptionCol, taskNameCol_suspended, descriptionCol_suspended, taskNameCol_overdue, descriptionCol_overdue;
    @FXML private TableColumn<Task, Integer> jobNumberCol, durationCol, urgencyCol, jobNumberCol_suspended, jobNumberCol_overdue, durationCol_overdue, urgencyCol_overdue;

    ObservableList<Task> currentTaskList = FXCollections.observableArrayList();
    ObservableList<Task> suspendedTaskList = FXCollections.observableArrayList();
    ObservableList<Task> overdueTaskList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        checkUserType();
        username.setText(Variables.getUserName());

        addEventListeners();
    }

    public void addEventListeners() {
        currentTaskTable.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        if (event.getClickCount() == 2 && (!row.isEmpty())) {
                            Task rowData = row.getItem();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserModal.fxml"));
                            Parent root = loader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.show();
                            //System.out.println(rowData.getTaskName());

                        }
                    }
                    catch(IOException err) {

                    }
                }
            });
            return row;
        });
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
            techGroup.setDisable(false);
            techGroup.setVisible(true);
        }
        else if(Variables.getUserType().equals("Manager")) {
            managerGroup.setDisable(false);
            managerGroup.setVisible(true);
        }
        else if(Variables.getUserType().equals("HR")) {
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
            ResultSet results = con.createStatement().executeQuery("select * from tasks");

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
            // TODO Auto-generated catch block
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
            ResultSet results = con.createStatement().executeQuery("select * from tasks WHERE overdue = 1");

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        taskNameCol_overdue.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        jobNumberCol_overdue.setCellValueFactory(new PropertyValueFactory<>("jobNumber"));
        durationCol_overdue.setCellValueFactory(new PropertyValueFactory<>("duration"));
        descriptionCol_overdue.setCellValueFactory(new PropertyValueFactory<>("description"));
        urgencyCol_overdue.setCellValueFactory(new PropertyValueFactory<>("urgency"));

        overdueTaskTable.setItems(overdueTaskList);
    }

    public void openUserEditModal() {
    }

    public void newUser() {

    }

    public void orderMoreParts() {

    }

    public void orderNewParts() {

    }
}