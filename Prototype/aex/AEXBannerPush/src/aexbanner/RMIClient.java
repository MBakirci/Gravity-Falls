/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aexbanner;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import Shared.IEffectenbeurs;
import Shared.Fonds;
import fontys.observer.RemotePropertyListener;
import java.util.ArrayList;


/**
 * Example of RMI using Registry
 *
 * @author Nico Kuijpers
 */
public class RMIClient extends Application{

    // Set binding name for student administration
    private static final String bindingName = "aex";

    // References to registry and student administration
    private Registry registry = null;
    private IEffectenbeurs effectenBeurs = null;

    // Constructor
    public RMIClient(String ipAddress, int portNumber) {

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

        // Print contents of registry
        if (registry != null) {
            printContentsRegistry();
        }

        // Bind student administration using registry
        if (registry != null) {
            try {
                effectenBeurs = (IEffectenbeurs) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                effectenBeurs = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                effectenBeurs = null;
            }
        }

        // Print result binding student administration
        if (effectenBeurs != null) {
            System.out.println("Client: Student administration bound");
        } else {
            System.out.println("Client: Student administration is null pointer");
        }

        // Test RMI connection
        if (effectenBeurs != null) {
            getGeneratedKoersen();
        }
    }

    // Print contents of registry
    private void printContentsRegistry() {
        try {
            String[] listOfNames = registry.list();
            System.out.println("Client: list of names bound in registry:");
            if (listOfNames.length != 0) {
                for (String s : registry.list()) {
                    System.out.println(s);
                }
            } else {
                System.out.println("Client: list of names bound in registry is empty");
            }
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot show list of names bound in registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
    }

    public void addListener(RemotePropertyListener pl) throws RemoteException
    {
        effectenBeurs.addListener(pl, "fondsen");
    }
    
    // Test RMI connection
    public ArrayList<Fonds> getGeneratedKoersen() {
        // Get number of students
        ArrayList<Fonds> koersen = new ArrayList<>();
        try {
            System.out.println("Client: Number of students: " + effectenBeurs.getKoersen());
            koersen = effectenBeurs.getKoersen();
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot get number of students");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
        return koersen;
    }

    // Main method
   /* public static void main(String[] args) {

        // Welcome message
        System.out.println("CLIENT USING REGISTRY");

        // Get ip address of server
        Scanner input = new Scanner(System.in);
        System.out.print("Client: Enter IP address of server: ");
        String ipAddress = input.nextLine();

        // Get port number
        System.out.print("Client: Enter port number: ");
        int portNumber = input.nextInt();

        // Create client
        RMIClient client = new RMIClient(ipAddress, portNumber);
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
