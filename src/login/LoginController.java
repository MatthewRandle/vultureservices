package login;
import utils.Variables;
import utils.SceneController;

import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public TextField username;
    public PasswordField password;
    Connection connection;
    PreparedStatement ps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void login() {
        try {
            String username = this.username.getText();
            String password = this.password.getText();

            connection = Variables.getConnection();
            ps = Variables.getPreparedStatement();

            String query = "SELECT * FROM users WHERE users.username = ? AND users.password = ?;";
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet results = ps.executeQuery();

            if (!results.isBeforeFirst() ) {
                System.out.println("ER_USER_DOES_NOT_EXIST");
            }
            else {
                Integer userID = -1;
//
                while(results.next()) {
                    userID = results.getInt("id");
                }

                if(userID != null && userID != -1) {
                    Variables.setUserID(userID);
                    SceneController.activate("menu");
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Error logging user in");
            System.out.println(error);
        }
    }
}
