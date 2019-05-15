package login;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.SceneController;
import utils.Variables;

/**
 * Handles user logging in.
 *
 * @author  Matthew Randle
 */

public class LoginController implements Initializable {

    public TextField username;
    public PasswordField password;
    Connection connection;
    PreparedStatement ps;
    @FXML Label loginError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Takes the username and password inputted to the text fields and attempts to log user in.
     * If the username and password are correct the user is sent to the userAccount screen.
     * If not an error shows.
     * @throws Exception
     * @see Exception
     */
    public void login() throws Exception {
        try {
            String username = this.username.getText();
            String password = this.password.getText();

            connection = Variables.getConnection();
            ps = Variables.getPreparedStatement();

            String query = "SELECT * FROM users JOIN user_types ON users.user_types_id = user_types.id WHERE users.username = ? AND users.password = ?;";
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet results = ps.executeQuery();

            if (!results.isBeforeFirst() ) {
                System.out.println("ER_USER_DOES_NOT_EXIST");
                loginError.setDisable(false);
                loginError.setVisible(true);
            }
            else {
                Integer userID = -1;
                String userType = "";
//
                while(results.next()) {
                    userID = results.getInt("id");
                    userType = results.getString("type");
                }

                if(userID != null && userID != -1 && !userType.equals("")) {
                    Variables.setUserID(userID);
                    Variables.setUserName(username);
                    Variables.setUserType(userType);

                    SceneController controller = new SceneController();
                    controller.registerScreens();

                    if(userType.equals("Finance") || userType.equals("Customer Services")) {
                        SceneController.activate("statistics");
                    }
                    else {
                        SceneController.activate("userAccount");
                    }
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Error logging user in");
            error.printStackTrace();
        }
    }
}
