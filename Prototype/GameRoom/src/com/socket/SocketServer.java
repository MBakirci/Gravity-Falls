package com.socket;

import gameroom.GameRoomController;
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

    public void send(MessageClient msg) {
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
                MessageClient msg = (MessageClient) streamIn.readObject();
                server.handle(ID, msg);
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

public class SocketServer implements Runnable {

    public ServerThread clients[];
    public ServerSocket server = null;
    public Thread thread = null;
    public int clientCount = 0, port = 13000;
    public Database db;
    public GameRoomController ui;

    public SocketServer(Player HostPlayer) {

        clients = new ServerThread[4];

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

        clients = new ServerThread[4];
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
                System.out.println("\nWaiting for a player ...");
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

    public synchronized void handle(int ID, MessageClient msg) {
        if (msg.content.equals(".bye")) {
            Announce("signout", "SERVER", msg.sender);
            remove(ID);
        } else {
            if (msg.type.equals("login")) {
                if (findUserThread(msg.sender) == null) {
                    if (db.checkLogin(msg.sender, msg.content)) {
                        clients[findClient(ID)].username = msg.sender;
                        clients[findClient(ID)].send(new MessageClient("login", "SERVER", "TRUE", msg.sender));
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                    } else {
                        clients[findClient(ID)].send(new MessageClient("login", "SERVER", "FALSE", msg.sender));
                    }
                } else {
                    clients[findClient(ID)].send(new MessageClient("login", "SERVER", "FALSE", msg.sender));
                }
            } else if (msg.type.equals("message")) {
                if (msg.recipient.equals("All")) {
                    Announce("message", msg.sender, msg.content);
                } else {
                    findUserThread(msg.recipient).send(new MessageClient(msg.type, msg.sender, msg.content, msg.recipient));
                    clients[findClient(ID)].send(new MessageClient(msg.type, msg.sender, msg.content, msg.recipient));
                }
            } else if (msg.type.equals("test")) {
                clients[findClient(ID)].send(new MessageClient("test", "SERVER", "OK", msg.sender));
            } else if (msg.type.equals("signup")) {
                if (findUserThread(msg.sender) == null) {
                    if (!db.userExists(msg.sender)) {
                        db.addUser(msg.sender, msg.content);
                        clients[findClient(ID)].username = msg.sender;
                        clients[findClient(ID)].send(new MessageClient("signup", "SERVER", "TRUE", msg.sender));
                        clients[findClient(ID)].send(new MessageClient("login", "SERVER", "TRUE", msg.sender));
                        Announce("newuser", "SERVER", msg.sender);
                        SendUserList(msg.sender);
                    } else {
                        clients[findClient(ID)].send(new MessageClient("signup", "SERVER", "FALSE", msg.sender));
                    }
                } else {
                    clients[findClient(ID)].send(new MessageClient("signup", "SERVER", "FALSE", msg.sender));
                }
            } else if (msg.type.equals("upload_req")) {
                if (msg.recipient.equals("All")) {
                    clients[findClient(ID)].send(new MessageClient("message", "SERVER", "Uploading to 'All' forbidden", msg.sender));
                } else {
                    findUserThread(msg.recipient).send(new MessageClient("upload_req", msg.sender, msg.content, msg.recipient));
                }
            } else if (msg.type.equals("upload_res")) {
                if (!msg.content.equals("NO")) {
                    String IP = findUserThread(msg.sender).socket.getInetAddress().getHostAddress();
                    findUserThread(msg.recipient).send(new MessageClient("upload_res", IP, msg.content, msg.recipient));
                } else {
                    findUserThread(msg.recipient).send(new MessageClient("upload_res", msg.sender, msg.content, msg.recipient));
                }
            }
        }
    }

    public void Announce(String type, String sender, String content) {
        MessageClient msg = new MessageClient(type, sender, content, "All");
        for (int i = 0; i < clientCount; i++) {
            clients[i].send(msg);
        }
    }

    public void SendUserList(String toWhom) {
        for (int i = 0; i < clientCount; i++) {
            findUserThread(toWhom).send(new MessageClient("newuser", "SERVER", clients[i].username, toWhom));
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
