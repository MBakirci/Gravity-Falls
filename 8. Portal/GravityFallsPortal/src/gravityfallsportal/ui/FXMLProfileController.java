/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.ui;

import gravityfallsportal.ui.FXMLDocumentController;
import gravityfallsportal.ui.User;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Wout
 */
public class FXMLProfileController implements Initializable {
    @FXML
    private Button BackBtn;
    @FXML
    private Label Username;
    @FXML
    private Label FirstName;
    @FXML
    private Label LastName;
    @FXML
    private Label Age;
    @FXML
    private Label Email;
    @FXML
    private Label Wins;
    @FXML
    private Label Highscore;
    
    private User User;
    /**
     * Initializes the controller class.
     */
    
    public void SetUser(User user)
    {
        this.User = user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void fillText(){
        Username.setText(User.getUserName());
        FirstName.setText(User.getFirstName());
        LastName.setText(User.getLastName());
        Age.setText(User.getAge() + "");
        Email.setText(User.getEmail());
        Wins.setText(User.getWins() + "");
        Highscore.setText(User.getHighscore() + "");
    }

    @FXML
    private void handlebackbtn(ActionEvent event) throws IOException {
          Stage stage;
        Parent root;
            stage = (Stage) BackBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            root = (Parent) fxmlLoader.load();
            FXMLDocumentController controller = fxmlLoader.<FXMLDocumentController>getController();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            stage.show();
    }
    
}
