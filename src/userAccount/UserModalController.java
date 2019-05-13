package userAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Variables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserModalController implements Initializable {
    String originalUsername;
    @FXML TextField userName;
    @FXML ComboBox userRoles;
    Connection con;
    PreparedStatement ps;
    ObservableList<String> types = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        userRoles.setItems(types);
        getUserRoles();
    }

    public void setUsername(String username) {
        userName.setText(username);
        originalUsername = username;
    }

    public void getUserRoles() {
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM user_types;");

            while(results.next()) {
                types.add(results.getString("type"));
            }
        }
        catch(SQLException err) {
            err.printStackTrace();
        }
    }

    public void setUserRole(String type) {
        userRoles.getSelectionModel().select(type);
    }

    public void saveUser() {
        try {
            ps = con.prepareStatement("SELECT id from user_types WHERE type = ?;");
            ps.setString(1, userRoles.getValue().toString());
            ResultSet results = ps.executeQuery();
            int type = -1;

            while(results.next()) {
                type = results.getInt("id");
            }

            if(type != -1) {
                ps = con.prepareStatement("UPDATE users SET username = ?, user_types_id = ? WHERE username = ?;");
                ps.setString(1, userName.getText());
                ps.setInt(2, type);
                ps.setString(3, originalUsername);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {

    }
}
