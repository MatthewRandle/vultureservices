package login;
import javafx.scene.Scene;
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
            System.out.println(error);
        }
    }
}
