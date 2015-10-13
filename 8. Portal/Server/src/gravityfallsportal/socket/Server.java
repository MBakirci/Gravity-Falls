/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.socket;

import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 *
 * @author Asror
 */
public class Server {

    
    
    public SocketServer server;
    public Thread serverThread;
    public String filePath = "D:/Data.xml";
    public JFileChooser fileChooser;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {  
        Thread the1 = new Thread(new SocketServer());

    }




}
