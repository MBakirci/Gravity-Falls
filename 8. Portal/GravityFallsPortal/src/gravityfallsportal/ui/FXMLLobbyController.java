/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityfallsportal.ui;

import gameroomm.GameRoomController;
import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.IGameRoom;
import shared.ILobby;
import server.*;

/**
 * FXML Controller class
 *
 * @author mehmet
 */
public class FXMLLobbyController implements Initializable, RemotePropertyListener {

    ////RMI
    private static final String bindingName = "AEXBanner";
    private Registry registry = null;
    private ILobby lobby = null;
    ////RMI

    @FXML
    private Button btnCreate;
    @FXML
    private TableView<GameRoom> tblRoom;
    @FXML
    private TableColumn<GameRoom, String> clmName;
    @FXML
    private TableColumn<GameRoom, Integer> clmPlayers;
    @FXML
    private TableColumn<GameRoom, Button> clmButtons;

    ObservableList<GameRoom> rooms;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String ipAddress = "145.93.49.69";
        int portNumber = 1099;
        // Print IP address and port number for registry
        System.out.println("Client: IP Address: " + ipAddress);
        System.out.println("Client: Port number " + portNumber);

        // Locate registry at IP address and port number
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null) {
            System.out.println("Client: Registry located");
        } else {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
        }

        // Bind student administration using registry
        if (registry != null) {
            try {
                lobby = (ILobby) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                lobby = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                lobby = null;
            }
        }

        // Print result binding student administration
        if (lobby != null) {
            System.out.println("Client: Student administration bound");
        } else {
            System.out.println("Client: Student administration is null pointer");
        }

        // Test RMI connection
        if (lobby != null) {
            try {
                UnicastRemoteObject.exportObject(this, 1101);
                lobby.addListener(this, "rooms2");
            } catch (Exception e) {

            }

        }

        clmName.setCellValueFactory(new PropertyValueFactory<GameRoom, String>("name"));
        clmPlayers.setCellValueFactory(new PropertyValueFactory<GameRoom, Integer>("maxPlayers"));
        rooms = FXCollections.observableArrayList();
        try {
            for (IGameRoom g : lobby.getGameRooms()) {
                System.out.println(g.getRoomname());
                rooms.add(new GameRoom(g.getRoomname(), g.getRoomMaxPlayers(), "127.0.0.1"));
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblRoom.setItems(rooms);
        tblRoom.getColumns().addAll(joinColl(), specColl());
    }

    private TableColumn<GameRoom, GameRoom> joinColl() {
        TableColumn<GameRoom, GameRoom> joinCol = new TableColumn<>("");
        joinCol.setMinWidth(20);
        joinCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        joinCol.setCellFactory(param -> new TableCell<GameRoom, GameRoom>() {
            private final Button joinButton = new Button("Join");

            @Override
            protected void updateItem(GameRoom gameroom, boolean empty) {
                super.updateItem(gameroom, empty);
                if (gameroom == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(joinButton);
                joinButton.setOnAction(event -> {
                    GameRoom gr = tblRoom.getItems().get(getIndex());
                    try {
                        joinRoom(gr);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(gr.getId());
                    System.out.println(gr.getHost());

                }
                );
            }
        });
        return joinCol;
    }

    private TableColumn<GameRoom, GameRoom> specColl() {
        TableColumn<GameRoom, GameRoom> spectCol = new TableColumn<>("");
        spectCol.setMinWidth(20);
        spectCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        spectCol.setCellFactory(param -> new TableCell<GameRoom, GameRoom>() {
            private final Button specButton = new Button("Spectate");

            @Override
            protected void updateItem(GameRoom gameroom, boolean empty) {
                super.updateItem(gameroom, empty);
                if (gameroom == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(specButton);
                specButton.setOnAction(event -> rooms.remove(gameroom));
            }
        });
        return spectCol;
    }

    @FXML
    private void newRoom(ActionEvent event) throws IOException {
        // Load the fxml file and create a new stage for the popup
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateGameRoomDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Set the person into the controller
        CreateGameRoomDialogController controller = loader.getController();
        controller.setControl(this);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
        this.refreshRooms();

    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) throws RemoteException {
        //System.out.println(pce.getNewValue());
        // Update JavaFX Scene Graph
        this.rooms = FXCollections.observableArrayList();
        
        if (pce.getOldValue() != lobby.getGameRooms()) {
            try {
                for (IGameRoom g : lobby.getGameRooms()) {
                    GameRoom gr = (GameRoom) g;

                    System.out.println(gr.getRoomname());
                    this.rooms.add(gr);
                    //rooms.add(gr);

                }
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLLobbyController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(this.rooms.size());
            tblRoom.refresh();
            tblRoom.setItems(this.rooms);
        }
    }

    public void addroom(String name, int maxplayers, String host) throws RemoteException {
        lobby.addGameRoom(name, maxplayers, host);
    }

    public void refreshRooms() {
        this.rooms = FXCollections.observableArrayList();
        try {
            for (IGameRoom g : lobby.getGameRooms()) {
                GameRoom gr = (GameRoom) g;

                System.out.println(gr.getRoomname());
                this.rooms.add(gr);
                //rooms.add(gr);

            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(this.rooms.size());
        tblRoom.refresh();
        tblRoom.setItems(this.rooms);
    }

    private void joinRoom(GameRoom gameroom) throws IOException {
        Parent root;
        Stage stage;
        stage = (Stage) btnCreate.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gameroomm/GameRoom.fxml"));
        root = (Parent) fxmlLoader.load();
        GameRoomController controller = fxmlLoader.<GameRoomController>getController();
        //controller.joinRoom(this.me.getUserName());
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

}
