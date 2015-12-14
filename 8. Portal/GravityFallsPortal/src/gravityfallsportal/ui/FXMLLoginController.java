/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.ui;

import gravityfallsportal.Serversocket.Server;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void Login(ActionEvent event) throws IOException {

        Stage stage;
        Parent root;

        if (txtUName.getText().equals("admin") && txtPass.getText().equals("admin")) {
            User admin = new User();
            admin.setUserName(txtUName.getText());
            String[] args = new String[]{"hallo"};
            Server.main(args);
        } else {
            User user = new User();
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

    }

}
