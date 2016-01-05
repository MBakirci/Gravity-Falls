package game.server;

import java.io.*;
import java.net.*;
import game.socketmsg.Message;
import game.socketmsg.MsgLocations;
import game.level.Level;

class gameServerThread extends Thread {

    public gameserver server = null;
    public Socket socket = null;
    public int ID = -1;
    public String username = "";
    public ObjectInputStream streamIn = null;
    public ObjectOutputStream streamOut = null;
    private Level level;

    public gameServerThread(gameserver _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();
    }

    public void send(MsgLocations mloc) {
        try {
            streamOut.writeObject(mloc);
            streamOut.flush();
        } catch (IOException ex) {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }

    public int getID() {
        return ID;
    }

    @SuppressWarnings("deprecation")
    public void run() {

        System.out.println("\nServer Thread " + ID + " running.");
        while (true) {
            try {
                MsgLocations mloc = (MsgLocations) streamIn.readObject();
                server.handle(ID, mloc);
            } catch (Exception ioe) {
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }

    public void open() throws IOException {
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }
}

public class gameserver implements Runnable {

    public gameServerThread clients[];
    public ServerSocket server = null;
    public Thread thread = null;
    public int clientCount = 0, port = 13000;

    public gameserver() {

        clients = new gameServerThread[50];

        try {
            while (!(available(port))) {
                port++;
            }
            server = new ServerSocket(port);
            port = server.getLocalPort();
            System.out.println("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port : " + port + "\nRetrying");

        }
    }

    public static boolean available(int port) {
        if (port < 12999 || port > 14000) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {

            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public gameserver(int Port) {

        clients = new gameServerThread[50];
        port = Port;

        try {
            server = new ServerSocket(port);
            port = server.getLocalPort();
            System.out.println("Server startet. IP : " + server.getInetAddress() + ", Port : " + server.getLocalPort());
            start();
        } catch (IOException ioe) {
            System.out.println("\nCan not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("\nWaiting for a client ...");
                addThread(server.accept());
            } catch (Exception ioe) {
                System.out.println("\nServer accept error: \n");

            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void handle(int ID, MsgLocations mloc) {
        Announce(mloc);
    }

    public void Announce(MsgLocations mlocc) {
        MsgLocations mloc = mlocc;
        for (int i = 0; i < clientCount; i++) {
            clients[i].send(mloc);
        }
    }

    public gameServerThread findUserThread(String usr) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].username.equals(usr)) {
                return clients[i];
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            gameServerThread toTerminate = clients[pos];
            System.out.println("\nRemoving client thread " + ID + " at " + pos);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("\nError closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("\nClient accepted: " + socket);
            clients[clientCount] = new gameServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                System.out.println("\nError opening thread: " + ioe);
            }
        } else {
            System.out.println("\nClient refused: maximum " + clients.length + " reached.");
        }
    }
}
