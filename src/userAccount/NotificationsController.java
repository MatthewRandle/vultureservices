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

public class NotificationsController implements Initializable {
    Connection con;
    PreparedStatement ps;
    @FXML ListView notificationsList;
    ObservableList<String> notifications = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        notificationsList.setItems(notifications);
        getNotifications();
        setEventHandlers();
    }

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
