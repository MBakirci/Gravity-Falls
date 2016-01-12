package gameroom.socket;

import gameroomm.GameRoomController;
import java.io.*;
import java.net.*;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public GameRoomController ui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;
    
    public SocketClient(GameRoomController frame) throws IOException{
        ui = frame; this.serverAddr = ui.serverAddr; this.port = ui.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
            
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());

    }

    @Override
    public void run() {
        boolean keepRunning = true;
        while(keepRunning){
            try {
                MessageClient msg = (MessageClient) In.readObject();
                System.out.println("Incoming : "+msg.toString());
                
                if(msg.type.equals("message")){

                         ui.ChatboxTextArea.appendText("["+ msg.sender +"] : " + msg.content + "\n");
                                            
                }
                else if(msg.type.equals("test")){
                }
                else{
                     ui.ChatboxTextArea.appendText("[SERVER > Me] : Unknown message type\n");
                }
            }
            catch(Exception ex) {
                keepRunning = false;                
                ui.clientThread.stop();
                
                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }
    
    public void send(MessageClient msg){
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : "+msg.toString());
            

        } 
        catch (IOException ex) {
            System.out.println("Exception SocketClient send()");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
    }
}
