/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aexbanner;

import Server.MockEffectenbeurs;
import Shared.Fonds;
import Shared.IEffectenbeurs;
import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**
 *
 * @author Dennis
 */
public class BannerController implements RemotePropertyListener {

    private AEXBanner banner;
    private IEffectenbeurs effectenbeurs;
    private Timer pollingTimer;
    private ArrayList<Fonds> koersen;
    private RMIClient rmiClient;

    public BannerController(AEXBanner banner) throws RemoteException {

        rmiClient = new RMIClient("127.0.0.1", 1099);
        this.banner = banner;
        UnicastRemoteObject.exportObject(this, 1100);
        rmiClient.addListener(this);
        //this.effectenbeurs = new MockEffectenbeurs();

        // Start polling timer: update banner every two seconds
        //pollingTimer = new Timer();
        // TODO
        //pollingTimer.scheduleAtFixedRate(new getKoersen(), 1000, 2000);
    }

    // Stop banner controller
    public void stop() {
        pollingTimer.cancel();
        // Stop simulation timer of effectenbeurs
        // TODO
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) throws RemoteException {
        koersen = rmiClient.getGeneratedKoersen();
        String bannerString = "";

        for (Fonds f : koersen) {
            bannerString = f.getNaam() + " " + Double.toString(f.getKoers()) + ", " + bannerString;
        }

        banner.setKoersen(bannerString);
    }
}
