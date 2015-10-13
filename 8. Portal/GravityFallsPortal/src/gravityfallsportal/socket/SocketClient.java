package gravityfallsportal.socket;

import java.io.*;
import java.net.*;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import gravityfallsportal.ui.FXMLDocumentController;

public class SocketClient implements Runnable {

    public int port;
    public String serverAddr;
    public Socket socket;
    public FXMLDocumentController ui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;

    public SocketClient(FXMLDocumentController controller) throws IOException {
        ui = controller;
        this.serverAddr = "192.168.31.1";
        this.port = 13000;
        socket = new Socket(InetAddress.getByName(serverAddr), port);

        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());


    }

    @Override
    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : " + msg.toString());

                if (msg.type.equals("message")) {
                    if (msg.recipient.equals(ui.username)) {
                        ui.tachatlog.appendText("[" + msg.sender + " > Me] : " + msg.content + "\n");
                    } else {
                        ui.tachatlog.appendText("[" + msg.sender + " > " + msg.recipient + "] : " + msg.content + "\n");
                    }

                    if (!msg.content.equals(".bye") && !msg.sender.equals(ui.username)) {
                        String msgTime = (new Date()).toString();
                    }
                } else if (msg.type.equals("login")) {
    
                } else if (msg.type.equals("test")) {

                } else if (msg.type.equals("newuser")) {
                   
                } else if (msg.type.equals("signup")) {
                    
                } else if (msg.type.equals("signout")) {
                    
                } else if (msg.type.equals("upload_req")) {

                } else if (msg.type.equals("upload_res")) {
                   
                } else {
                    ui.tachatlog.appendText("[SERVER > Me] : Unknown message type\n");
                }
            } catch (Exception ex) {
                keepRunning = false;
   
                ui.clientThread.stop();

                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }

    public void send(Message msg) {
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : " + msg.toString());

            if (msg.type.equals("message") && !msg.content.equals(".bye")) {
                String msgTime = (new Date()).toString();
            }
        } catch (IOException ex) {
            System.out.println("Exception SocketClient send()");
        }
    }

    public void closeThread(Thread t) {
        t = null;
    }
}
