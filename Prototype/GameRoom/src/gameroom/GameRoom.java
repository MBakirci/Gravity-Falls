/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameroom;

import com.socket.Player;
import com.socket.SocketServer;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import java.net.*;
import jdk.nashorn.internal.objects.NativeString;

/**
 *
 * @author Wout
 */
public class GameRoom extends Application {
    
    public SocketServer server;
    public Player player;
    public Thread serverThread;
    public String filePath = "D:/Data.xml";
    public JFileChooser fileChooser;
    
    @Override
    public void start(Stage stage) throws Exception {
       
        Parent root = FXMLLoader.load(getClass().getResource("GameRoom.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        
    }
     

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
