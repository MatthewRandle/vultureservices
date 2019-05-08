package userAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.Variables;
import utils.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserAccountController implements Initializable {
    Connection con;
    PreparedStatement ps;
    @FXML private TableView  <Task> currentTaskTable, overdueTaskTable, suspendedTaskTable;
    //@FXML private TableColumn<Task, String> taskNameCol, jobNumberCol, descriptionCol, durationCol, urgencyCol;
    //@FXML private TableColumn<Task, String> taskNameCol_overdue, jobNumberCol_overdue, descriptionCol_overdue, durationCol_overdue, urgencyCol_overdue;
    //@FXML private TableColumn<Task, String> taskNameCol_suspended, jobNumberCol_suspended, descriptionCol_suspended, durationCol_suspended, urgencyCol_suspended;
    @FXML private TableColumn<Task, String> taskNameCol, descriptionCol;
    @FXML private TableColumn<Task, Integer> jobNumberCol, durationCol, urgencyCol;

    ObservableList<Task> currentTaskList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        try {
            ResultSet results = con.createStatement().executeQuery("select * from tasks");

            while (results.next()) {
                System.out.println(results.getInt("job_number"));
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
}