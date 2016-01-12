/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameroomm;

import gameroom.socket.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.swing.DefaultListModel;
import java.util.ArrayList;

/**
 * FXML Controller class
 *
 * @author Wout
 */
public class GameRoomController implements Initializable {
    @FXML
    private ImageView MapImageView;
    @FXML
    private Button BtnMapDown;
    @FXML
    private Button BtnMapUp;
    @FXML
    private TextField MapTextField;
    @FXML
    private TextField TimeTextField;
    @FXML
    private Button BtnTimeDown;
    @FXML
    private Button BtnTimeUp;
    @FXML
    private Label LbItems;
    @FXML
    private Label LbSpectators;
    @FXML
    private TextField PasswordTextField;
    @FXML
    private Label LbPassword;
    @FXML
    private RadioButton RbItemsOn;
    @FXML
    private RadioButton RbSpectatorsOn;
    @FXML
    private RadioButton RbItemsOff;
    @FXML
    private RadioButton RbSpectatorsOff;
    @FXML
    private Button BtnReady;
    @FXML
    private Button BtnInvite;
    @FXML
    private TextField MessageTextField;
    @FXML
    private Button BtnSend;
    @FXML
    public TextArea ChatboxTextArea;
    @FXML
    private Label LbRoomName;
    @FXML
    private Label LbHostName;
    @FXML
    private Pane PanePlayer1;
    @FXML
    private Pane PanePlayer2;
    @FXML
    private Pane PanePlayer3;
    @FXML
    private Pane PanePlayer4;
    @FXML
    public Label LbPlayer1;
    @FXML
    public Label LbPlayer2;
    @FXML
    public Label LbPlayer3;
    @FXML
    public Label LbPlayer4;
    @FXML
    private Button btnKickPlayer1;
    @FXML
    private Button btnKickPlayer2;
    @FXML
    private Button btnKickPlayer3;
    @FXML
    private Button btnKickPlayer4;

    public SocketClient client;
    public SocketServer server;
    public int port;
    public String serverAddr, username, password;
    public Thread clientThread;
    public DefaultListModel model;
    public File file;
    public String historyFile = "D:/History.xml";
    public Player player;
    public static ArrayList players;
   
    int Time = 10;
    
    
    @FXML
    private void TimeDown(MouseEvent event) {
        if(Time > 3){
        Time = Time - 1;
        String ChangedTime = Integer.toString(Time) + ":00";
        TimeTextField.setText(ChangedTime);
        }
    }
    @FXML
    private void TimeUp(MouseEvent event) {
         if(Time < 15){
        Time = Time + 1;
        String ChangedTime = Integer.toString(Time) + ":00";
        TimeTextField.setText(ChangedTime);
        }
    }
    @FXML
    private void ItemsSetOn(MouseEvent event) {
        if(RbItemsOn.isSelected() == true)
        {
            RbItemsOff.setSelected(false);
        }
        else if(RbItemsOn.isSelected() == false)
        {
            RbItemsOff.setSelected(true);
        }
    }
    @FXML
    private void ItemsSetOff(MouseEvent event) {
        if(RbItemsOff.isSelected() == true)
        {
            RbItemsOn.setSelected(false);
        }
        else if(RbItemsOff.isSelected() == false)
        {
            RbItemsOn.setSelected(true);
        }
    }
    @FXML
    private void SpectatorsSetOn(MouseEvent event) {
        if(RbSpectatorsOn.isSelected() == true)
        {
            RbSpectatorsOff.setSelected(false);
        }
        else if(RbSpectatorsOn.isSelected() == false)
        {
            RbSpectatorsOff.setSelected(true);
        }
    }
    @FXML
    private void SpectatorsSetOff(MouseEvent event) {
        if(RbSpectatorsOff.isSelected() == true)
        {
            RbSpectatorsOn.setSelected(false);
        }
        else if(RbSpectatorsOff.isSelected() == false)
        {
            RbSpectatorsOn.setSelected(true);
        }
    }
    
    public boolean isWin32() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverAddr = "127.0.0.1";
        players = new ArrayList(4);
        players.add(new Player(false, "Asror"));
        players.add(new Player(true, "Wout"));
        players.add(new Player(false, "Richard"));
        players.add(new Player(false, "Mehmet"));
        
       
        port = 13000;
        try {
            username = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(GameRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            for (int i = 0; i < players.size(); i++) {
                Player TempPlayer = (Player)players.get(i);
                if(TempPlayer.GetAdmin() == true){
                    server = new SocketServer(player);
                    LbHostName.setText("Host: " + username);
                } 
            }
            UpdatePlayerLabels();
            client = new SocketClient(this);
            clientThread = new Thread(client);
            clientThread.start();
            ChatboxTextArea.appendText(username + " has succesfully connected to the server" + "\n");
            
        } catch (Exception ex) {
            ChatboxTextArea.appendText("[Application > Me] : Server not found, press on the retry button to retry\n");
            BtnSend.textProperty().set("Retry");
        }

        // TODO
    }

    @FXML
    private void handlebtnsend(ActionEvent event) throws IOException {
        if (BtnSend.textProperty().get().equals("Retry")) {
            try {
                client = new SocketClient(this);
                clientThread = new Thread(client);
                clientThread.start();
                client.send(new MessageClient("test", "testUser", "testContent", "SERVER"));
                ChatboxTextArea.appendText(username + " has succesfully connected to the server" + "\n");
                BtnSend.textProperty().set("Send");
            } catch (Exception ex) {
                ChatboxTextArea.appendText("[Application > Me] : Server not found, press on the retry button to retry\n");
                BtnSend.textProperty().set("Retry");
            }
        } else {
            String msg = MessageTextField.getText();
            String target = "All";

            if (!msg.isEmpty() && !target.isEmpty()) {
                MessageTextField.setText("");
                client.send(new MessageClient("message", username, msg, target));
            }
        }
    }
    
    private void UpdatePlayerLabels()
    {
        int count = players.size();
            LbPlayer1.setText("Waiting for players...");
            btnKickPlayer1.setDisable(true);
            LbPlayer2.setText("Waiting for players...");
            btnKickPlayer2.setDisable(true);
            LbPlayer3.setText("Waiting for players...");
            btnKickPlayer3.setDisable(true);
            LbPlayer4.setText("Waiting for players...");
            btnKickPlayer4.setDisable(true);
            for (int i = 0; i < players.size(); i++) {
                Player TempPlayer = (Player)players.get(i); 
                if(i==0 && count >= 1)
                {
                    LbPlayer1.setText(TempPlayer.GetNaam());
                    if(TempPlayer.GetAdmin() == false){
                        btnKickPlayer1.disableProperty().set(false);
                    }
                    else{
                        btnKickPlayer1.disableProperty().set(true);
                    }
                        
                }else if(i==1 && count >= 2)
                {
                    LbPlayer2.setText(TempPlayer.GetNaam());
                    if(TempPlayer.GetAdmin() == false){
                        btnKickPlayer2.disableProperty().set(false);
                    }
                    else{
                        btnKickPlayer2.disableProperty().set(true);
                    }
                }else if(i==2 && count >= 3)
                {
                    LbPlayer3.setText(TempPlayer.GetNaam());
                    if(TempPlayer.GetAdmin() == false){
                        btnKickPlayer3.disableProperty().set(false);
                    }
                    else{
                        btnKickPlayer3.disableProperty().set(true);
                    }
                }else if(i==3 && count == 4)
                {
                    LbPlayer4.setText(TempPlayer.GetNaam());
                    if(TempPlayer.GetAdmin() == false){
                        btnKickPlayer4.disableProperty().set(false);
                    }
                    else{
                        btnKickPlayer4.disableProperty().set(true);
                    }
                }
            }
            
    }

    @FXML
    private void KickPlayer1(MouseEvent event) {
         players.remove(0);
         UpdatePlayerLabels();
    }

    @FXML
    private void KickPlayer2(MouseEvent event) {
        players.remove(1);
         UpdatePlayerLabels();
    }

    @FXML
    private void KickPlayer3(MouseEvent event) {
        players.remove(2);
         UpdatePlayerLabels();
    }

    @FXML
    private void KickPlayer4(MouseEvent event) {
        players.remove(3);
         UpdatePlayerLabels();
    }
}
