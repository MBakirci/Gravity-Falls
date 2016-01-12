/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Shared.Fonds;
import Shared.IEffectenbeurs;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**
 *
 * @author Dennis
 */
public class MockEffectenbeurs extends UnicastRemoteObject implements IEffectenbeurs {

    public ArrayList<Fonds> fondslist;
    private Timer fondstimer;
    private BasicPublisher publisher;
    
    public MockEffectenbeurs() throws RemoteException {
        publisher = new BasicPublisher(new String[]{"fondsen"});
        fondslist = new ArrayList<>();

        fondslist.add(new Fonds("ABN", 100.0));
        fondslist.add(new Fonds("RABO", 200.0));
        fondslist.add(new Fonds("ING", 300.0));
        fondslist.add(new Fonds("SNS", 400.0));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new GenerateKoersen(), 0, 1500);
    }

    @Override
    public ArrayList<Fonds> getKoersen() {
        return fondslist;
    }

    @Override
    public void addListener(RemotePropertyListener rl, String string) throws RemoteException {
        publisher.addListener(rl, string);
    }

    @Override
    public void removeListener(RemotePropertyListener rl, String string) throws RemoteException {
        publisher.addListener(rl, string);
    }

    class GenerateKoersen extends TimerTask {

        @Override
        public void run() {
            Random r = new Random();
            for (Fonds f : fondslist) {
                int newKoers = r.nextInt(500);
                f.setKoers(newKoers);
            }
            publisher.inform(this, "fondsen", null, fondslist);
        }
    }
}
