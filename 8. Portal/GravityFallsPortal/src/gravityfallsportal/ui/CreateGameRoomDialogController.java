/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mehmet
 */
public class CreateGameRoomDialogController implements Initializable {

    @FXML
    private TextField txtGameRoomName;
    @FXML
    private TextField txtMaxPlayers;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblPaneTitle;
    @FXML
    private Spinner<Double> nmbrMaxPlayer;

    private FXMLLobbyController control;

    public void setControl(FXMLLobbyController control) {
        this.control = control;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void createGameRoom(ActionEvent event) throws RemoteException, UnknownHostException, MalformedURLException, IOException {

        Stage stage = (Stage) btnOK.getScene().getWindow();
        String name = txtGameRoomName.getText();
        if (nmbrMaxPlayer.equals(new Double(4))) {
            System.out.println(4);
        }

        int maxPlayers = 4;

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);
        control.addroom(name, maxPlayers, ip);
        //GameRoom gr = new GameRoom(name, maxPlayers);
        //System.out.println(gr.getId());
        stage.close();

    }

    @FXML
    private void CancelGameRoom(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public boolean isOkClicked() {
        return true;
    }

}
