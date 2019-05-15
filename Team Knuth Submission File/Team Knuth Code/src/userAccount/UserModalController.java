package userAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.Variables;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Loads the users information and allows editing/deletion of users settings.
 *
 * @author  Matthew Randle
 */

public class UserModalController implements Initializable {
    String originalUsername, originalPassword;
    Integer userID;
    @FXML TextField userName, password;
    @FXML ComboBox userRoles;
    @FXML Button deleteUser, cancel;
    @FXML Label roleLabel;
    Connection con;
    PreparedStatement ps;
    ObservableList<String> types = FXCollections.observableArrayList();

    /**
     * Gets connection variables and gets the user roles.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = Variables.getConnection();
        ps = Variables.getPreparedStatement();

        userRoles.setItems(types);
        getUserRoles();
    }

    /**
     * Gets the id for the user that is being edited
     */
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

    /**
     * Sets the initial value for the username text field.
     * @param username is the username of the account that is being edited
     */
    public void setUsername(String username) {
        userName.setText(username);
        originalUsername = username;
    }

    /**
     * Gets all the user roles from the database and places them into the combo box.
     */
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

    /**
     * Sets the user role for the currently edited user
     * @param type is the current type of the user
     */
    public void setUserRole(String type) {
        userRoles.getSelectionModel().select(type);
    }

    public void getPassword() {
        try {
            ps = con.prepareStatement("SELECT password from users WHERE id = ?;");
            ps.setInt(1, userID);
            ResultSet results = ps.executeQuery();

            while(results.next()) {
                originalPassword = results.getString("password");
                password.setText(originalPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits the users information based on the values in the text fields and combo box.
     */
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

    /**
     * Delete the user from the database.
     */
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

    /**
     * Shows delete button instead of cancel button (editing a current user).
     */
    public void setEditModal() {
        deleteUser.setDisable(false);
        deleteUser.setVisible(true);
    }

    /**
     * Shows cancel button instead of delete button (making a new user).
     */
    public void setNewModal() {
        cancel.setDisable(false);
        cancel.setVisible(true);
    }

    /**
     * Shows the correct text fields and buttons when editing your own account.
     */
    public void setEditSelfModal() {
        cancel.setDisable(false);
        cancel.setVisible(true);
        userRoles.setDisable(true);
        userRoles.setVisible(false);
        roleLabel.setText("Password");
        password.setDisable(false);
        password.setVisible(true);
    }

    /**
     * Closes the user modal window.
     */
    public void close() {
        Stage stage = (Stage) userName.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles when the save button is clicked as different data will be saved
     * depending on if you are creating a new user or editing a current one or
     * editing your own account.
     */
    public void saveUser() {
        //we are editing a user
        if(deleteUser.isVisible()) {
            editUser();
        }
        //if we are editing self
        else if(password.isVisible()) {
            editSelf();
        }
        else {
            newUser();
        }
    }

    /**
     * Edits database for the current signed in user.
     */
    public void editSelf() {
        try {
            ps = con.prepareStatement("UPDATE users SET username = ?, password = ? WHERE id = ?;");
            ps.setString(1, userName.getText());
            ps.setString(2, password.getText());
            ps.setInt(3, userID);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new user to the database.
     */
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
