/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.ui;

import gravityfallsportal.Serversocket.Server;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mehmet
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private TextField txtUName;
    @FXML
    private Button btnLogin;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Label errorText;
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final String DB_URL = "jdbc:mysql://84.246.4.143:9131/waligravityfalls";

    //  Database credentials
    static final String USER = "WaliGravityFalls";
    static final String PASS = "gravityfalls1";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        errorText.setVisible(false);
    }

    @FXML
    private void Login(ActionEvent event) throws IOException {
        errorText.setVisible(false);
        Stage stage;
        Parent root;
        int loginvalue = 0;
        Connection conn = null;
        Statement stmt = null;
        User user = new User();
        
        if (txtUName.getText() != "" && txtPass.getText() != "") {
        
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = (Statement) conn.createStatement();
            String sql;
            String sqlUser;
            sql = "SELECT Count(*) as id FROM users where Username = '"+ txtUName.getText() +"' and Password = '"+ txtPass.getText() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                loginvalue = rs.getInt("id");
                
            }
            sqlUser = "SELECT * FROM users where Username = '" + txtUName.getText() +"' and Password = '"+ txtPass.getText() + "'";
            ResultSet UserRs = stmt.executeQuery(sqlUser);
            while (UserRs.next())
            {
                user.setUserName(UserRs.getString("Username"));
                user.setFirstName(UserRs.getString("FirstName"));
                user.setLastName(UserRs.getString("LastName"));
                user.setAge(UserRs.getInt("Age"));
                user.setEmail(UserRs.getString("Email"));
                user.setWins(UserRs.getInt("Wins"));
                user.setHighscore(UserRs.getInt("Highscore"));
            }

            //STEP 6: Clean-up environment
            rs.close();
            UserRs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

        if (loginvalue == 1) {

        
            System.out.println("Goodbye!");
            
            user.setUserName(txtUName.getText());
            stage = (Stage) btnLogin.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            root = (Parent) fxmlLoader.load();
            FXMLDocumentController controller = fxmlLoader.<FXMLDocumentController>getController();
            controller.setConnected();
            controller.setUser(user);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        }
        else {
            errorText.setVisible(true);
            errorText.setText("Username and password do not match.");
        }
        }
    }
}
