package userAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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
    Integer userID;
    @FXML TextField userName;
    @FXML ComboBox userRoles;
    @FXML Button deleteUser, cancel;
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

    public void getUserID() {
        try {
            ps = con.prepareStatement("SELECT id from users WHERE username = ?;");
            ps.setString(1, originalUsername);
            ResultSet results = ps.executeQuery();

            while(results.next()) {
                userID = results.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void editUser() {
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
                close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        try {
            ps = con.prepareStatement("DELETE FROM users WHERE id = ?;");
            ps.setInt(1, userID);
            ps.executeUpdate();
            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //shows delete button instead of cancel button (editing a current user)
    public void setEditModal() {
        deleteUser.setDisable(false);
        deleteUser.setVisible(true);
    }

    //shows cancel button instead of delete button (making a new user)
    public void setNewModal() {
        cancel.setDisable(false);
        cancel.setVisible(true);
    }

    public void close() {
        Stage stage = (Stage) userName.getScene().getWindow();
        stage.close();
    }

    public void saveUser() {
        //we are editing a user
        if(deleteUser.isVisible()) {
            editUser();
        }
        else {
            newUser();
        }
    }

    public void newUser() {
        try {
            ps = con.prepareStatement("SELECT id from user_types WHERE type = ?;");
            ps.setString(1, userRoles.getValue().toString());
            ResultSet results = ps.executeQuery();
            int type = -1;

            while(results.next()) {
                type = results.getInt("id");
            }

            if(type != -1) {
                ps = con.prepareStatement("INSERT INTO users (username, password, user_types_id) VALUES (?, 'vulture', ?)");
                ps.setString(1, userName.getText());
                ps.setInt(2, type);
                ps.executeUpdate();
                close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
