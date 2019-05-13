package userAccount;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
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
}
