package userAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import utils.Variables;

/**
 * Loads notification for the user that is logged in.
 *
 * @author  Matthew Randle
 */

public class NotificationsController implements Initializable {
    Connection con;
    PreparedStatement ps;
    @FXML ListView notificationsList;
    ObservableList<String> notifications = FXCollections.observableArrayList();

    /**
     * Gets connection variables and calls functions that are needed to get some initial data.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        notificationsList.setItems(notifications);
        getNotifications();
        setEventHandlers();
    }

    /**
     * Deletes a notification from the database
     * @param alert is used to know which notification to delete
     */
    public void deleteNotification(String alert) {
        try {
            ps = con.prepareStatement("DELETE FROM alerts WHERE alert = ?");
            ps.setString(1, alert);
            ps.executeUpdate();
            notifications = FXCollections.observableArrayList();
            getNotifications();
            notificationsList.setItems(notifications);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the notifications from the database for the currently logged in user
     */
    public void getNotifications() {
        try {
            String query = "select * from alerts JOIN user_types ON user_types.id = alerts.user_type WHERE user_types.type = ?;";
            ps = con.prepareStatement(query);
            ps.setString(1, Variables.getUserType());
            ResultSet results = ps.executeQuery();

            while(results.next()) {
                notifications.add(results.getString("alert"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets event handlers for the list view.
     * When clicked the deleteNotification function is called, with the clicked rows alert passed.
     */
    public void setEventHandlers() {
        notificationsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 ) {
                    deleteNotification(notificationsList.getSelectionModel().getSelectedItem().toString());
                }
            }
        });
    }
}
