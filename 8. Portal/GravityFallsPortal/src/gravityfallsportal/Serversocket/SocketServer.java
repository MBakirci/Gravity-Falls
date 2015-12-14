package gravityfallsportal.Serversocket;

import gravityfallsportal.Serversocket.Database;
import gravityfallsportal.socket.Message;
import java.io.*;
import java.net.*;

class ServerThread extends Thread {

    public SocketServer server = null;
    public Socket socket = null;
    public int ID = -1;
    public String username = "";
    public ObjectInputStream streamIn = null;
    public ObjectOutputStream streamOut = null;

    public ServerThread(SocketServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();

    }

    public void send(Message msg) {
        try {
            streamOut.writeObject(msg);
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
                 Message msg = (Message) streamIn.readObject();
                server.handle(ID, msg);
            } catch (Exception ioe) {
                System.out.println(ID + "ERROR reading: " + ioe.getMessage());
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

public class SocketServer implements Runnable {

    public ServerThread clients[];
    public ServerSocket server = null;
    public Thread thread = null;
    public int clientCount = 0, port = 13000;
    public Database db;

    public SocketServer() {

        clients = new ServerThread[50];
        db = new Database("C:\\Users\\Asror\\Documents");

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

    public SocketServer(int Port) {

        clients = new ServerThread[50];
        port = Port;
        db = new Database("C:\\Users\\Asror\\Documents");

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

    public synchronized void handle(int ID, Message msg) {
        if (msg.type.equals("message")) {
            if (msg.recipient.equals("All")) {
                Announce("message", msg.sender, msg.content);
            } else {
                findUserThread(msg.recipient).send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
                clients[findClient(ID)].send(new Message(msg.type, msg.sender, msg.content, msg.recipient));
            }
        } else if (msg.type.equals("test")) {
            clients[findClient(ID)].send(new Message("test", "SERVER", "OK", msg.sender));
        }
    }

    public void Announce(String type, String sender, String content) {
        Message msg = new Message(type, sender, content, "All");
        for (int i = 0; i < clientCount; i++) {
            clients[i].send(msg);
        }
    }

    public void SendUserList(String toWhom) {
        for (int i = 0; i < clientCount; i++) {
            findUserThread(toWhom).send(new Message("newuser", "SERVER", clients[i].username, toWhom));
        }
    }

    public ServerThread findUserThread(String usr) {
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
            ServerThread toTerminate = clients[pos];
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
            clients[clientCount] = new ServerThread(this, socket);
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
