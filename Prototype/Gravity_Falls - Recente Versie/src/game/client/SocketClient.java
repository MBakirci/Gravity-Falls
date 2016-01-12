package game.client;

import game.client.PortalController;
import game.socketmsg.Message;
import game.socketmsg.MsgLocations;
import java.io.*;
import java.net.*;

public class SocketClient implements Runnable {

    public int port;
    public String serverAddr;
    public Socket socket;
    public PortalController ui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;

    public SocketClient(PortalController frame) throws IOException {
        ui = frame;
        this.serverAddr = ui.serverAddr;
        this.port = ui.port;
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
                MsgLocations mloc = (MsgLocations) In.readObject();
                if (mloc.objectid != null) {
                    ui.removeObjects(mloc);
                } else if (mloc.bull != null) {
                    //System.out.println("bullet");
                    ui.addBullet(mloc);
                } else if (mloc.crystalx != 0) {
                    ui.addCrystal(mloc);
                } else {
                    ui.addchars(mloc);
                }

            } catch (Exception ex) {
                System.out.println(ex);
                ex.printStackTrace();
                keepRunning = false;
                ui.clientThread.stop();

            }
        }
    }

    public void send(MsgLocations mloc) {
        try {
            Out.writeObject(mloc);
            Out.flush();

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void send(Message msg) {
        try {
            Out.writeObject(msg);
            Out.flush();

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void closeThread(Thread t) {
        t = null;
    }
}
