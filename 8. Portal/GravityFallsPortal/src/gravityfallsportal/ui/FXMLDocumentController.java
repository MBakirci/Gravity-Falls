/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import gravityfallsportal.socket.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.DefaultListModel;

/**
 *
 * @author Asror
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private AnchorPane portal;
    @FXML
    private Button btnsend;
    @FXML
    private TextField tfchat;
    @FXML
    public TextArea tachatlog;
    @FXML
    private ImageView imvGame1;

    public SocketClient client;
    public Thread clientThread;
    private User user;
    public String username;
    private boolean connected = false;

    @FXML
    private void handlebtnsend(ActionEvent event) throws IOException {
        String msg = tfchat.getText();

        if (!msg.isEmpty()) {
            tfchat.setText("");
            client.send(new Message("message", username, msg, "All"));
        }

    }

    public void connect() throws IOException {

    }

    public void setUser(User user) {
        this.user = user;
        ConnectWithServer();
    }

    public void setConnected() {
        this.connected = true;
    }

    public void ConnectWithServer() {
        if (connected) {
            try {
                username = this.user.getUserName();
                client = new SocketClient(this);

                clientThread = new Thread(client);
                clientThread.start();
                connected = true;
            } catch (IOException ex) {

                tachatlog.appendText("Connection Failed !");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    @FXML
    private void handleimvGame1(MouseEvent event) {
        try {
            Process p = Runtime.getRuntime().exec("gnome-calculator");
            p.waitFor();
            System.out.println("Google Chrome launched!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
