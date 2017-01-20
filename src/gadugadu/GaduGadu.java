/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gadugadu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Monika
 */
public class GaduGadu implements Runnable {

    public static MeUser me;

    public static Socket socket;
    public static PrintWriter outMessage;
    public static MySocketReader inMessage;

    public static Socket statusSocket;
    public static PrintWriter outStatus;
    public static MySocketReader inStatus;

    public static String serverIP;
    public static boolean checkStatuses;
    public static boolean shutdown;

    //Wysyła zapytanie o statusy znajomych użytkowników
    public static void getStatusesFromServer() {
        String message = "#STATUS";

        for (User u : GaduGadu.me.getFriends()) {
            message = message + " " + u.getUserId();
        }

        GaduGadu.outStatus.println(message);
        //System.out.println("Wyslano zapytanie o statusy znajomych");
    }

    //Uaktualnia listy onlineFriends i offlineFriends
    public static void updateStatuses() {
        ArrayList<User> newOnline = new ArrayList();
        ArrayList<User> newOffline = new ArrayList();

        for (User u : me.getFriends()) {
            if (u.getOnline() == true) {
                newOnline.add(u);
            } else {
                newOffline.add(u);
            }
        }
        GaduGadu.me.setOnlineFriends(newOnline);
        GaduGadu.me.setOfflineFriends(newOffline);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        ServerConfigurationForm serverConfigurationForm = new ServerConfigurationForm();
        serverConfigurationForm.setVisible(true);

        Boolean mainWindow = false;
        checkStatuses = true;
        shutdown = false;

        me = new MeUser();

        while (true) {
            if (GaduGadu.serverIP != null) {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
                break;
            }
            Thread.sleep(10);
        }

        try {
            socket = new Socket(GaduGadu.serverIP, 5001);
            socket.setSoTimeout(0);
            outMessage = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inMessage = new MySocketReader(inBufferedReader);
            
            statusSocket = new Socket(GaduGadu.serverIP, 5001);
            statusSocket.setSoTimeout(0);
            outStatus = new PrintWriter(statusSocket.getOutputStream(), true);
            BufferedReader statusBufferedReader = new BufferedReader(new InputStreamReader(statusSocket.getInputStream()));
            inStatus = new MySocketReader(statusBufferedReader);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Nie udalo się połączyc z serwerem");
        }

        while (true) {

            if ((GaduGadu.me.getOnline() == true) && (mainWindow == false)) {
                MainForm mainForm = new MainForm();
                GaduGadu.me.setMainFrame(mainForm);
                mainForm.setVisible(true);
                mainWindow = true;

                GaduGadu gadu = new GaduGadu();
                (new Thread(gadu, "statusThread")).start(); //sprawdzanie statusow znajomych
                (new Thread(gadu, "messageThread")).start(); //odbieranie wiadomosci

                break;
            }

//           if(GaduGadu.me.getOnline()==true){
//               updateStatuses();
//           }
            Thread.sleep(10);
        }

    }

    @Override
    public void run() {
        String serverMsg;
        String statusMsg;

        System.out.println("Nowy wątek GADU");

        while (!shutdown) {
            try {
                //Nowy wątek

                String threadName = Thread.currentThread().getName();

//                wątek odbierający wiadomość o statusach znajomych
                if (threadName.equals("statusThread")) {
                                       
                    getStatusesFromServer();

                    statusMsg = GaduGadu.inStatus.read();

//            System.out.println("statusMsg: "+statusMsg);
                    if (statusMsg.contains("#STATUS")) {
                        //dodaj użytkowników o odebranych id do listy online, resztę wrzuć na listę offline
                        statusMsg = statusMsg.replace("#STATUS", "");

                        for (User u : GaduGadu.me.getFriends()) {
                            u.setOnline(false);

                            if (!"".equals(statusMsg)) {
                                for (String sid : statusMsg.split(" ")) {
                                    if (!"".equals(sid)) {
                                        // System.out.println("sid: " + sid);
                                        int id = Integer.parseInt(sid);
                                        if (u.getUserId() == id) {
                                            u.setOnline(true);
                                        }
                                    }
                                }
                            }
                        }
                        updateStatuses();
                    }

//                wątek odbierajacy wiadomosci od użytkowników
                } else if (threadName.equals("messageThread")) {
                    int id = 0;
                    boolean isFriend = false;

                    serverMsg = GaduGadu.inMessage.read();

                    System.out.println("WIADOMOSC OD UZYTKOWNIKA: " + serverMsg);
                    if (serverMsg.contains("#MESSAGE")) {
                        System.out.println("contains #MESSAGE");

                        serverMsg = serverMsg.replace("#MESSAGE", "");
                        if (!"".equals(serverMsg)) {
                            for (String s : serverMsg.split(" ")) {
                                if (!"".equals(s)) {
                                    System.out.println("id nadawcy: " + s);
                                    id = Integer.parseInt(s);
                                    break;
                                }
                            }
                        }
                        //wyszukaj uzytkownika ktory wyslal wiadomosc w tablicy friends
                        for (User u : GaduGadu.me.getFriends()) {
                            if (u.getUserId() == id) {
                                isFriend = true;
                                if (!u.getConversation()) {
                                    u.startConversation();
                                }
                                u.getWindow().addMessage(u.getUserName() + " >>> " + serverMsg.replaceFirst(Integer.toString(id), ""));
                            }
                        }

                        //jesli to nieznajomy
                        if (!isFriend) {
                            User unknown = null;
                            boolean exists = false;

                            for (User u : GaduGadu.me.getUnknownUsers()) {
                                if (u.getUserId() == id) {
                                    exists = true;
                                    unknown = u;
                                    break;
                                }
                            }

                            if (exists) {
                                if (!unknown.getConversation()) {
                                    unknown.startConversation();
                                }
                                unknown.getWindow().addMessage("Unknown >>> " + serverMsg.replaceFirst(Integer.toString(id), ""));
                            } else {
                                User newUnknown = new User();
                                newUnknown.setUserId(id);
                                newUnknown.startConversation();
                                newUnknown.getWindow().addMessage("Unknown >>> " + serverMsg.replaceFirst(Integer.toString(id), ""));
                                newUnknown.getWindow().setConversationName("Unknown: " + Integer.toString(id));
                                GaduGadu.me.addUnknownUser(newUnknown);
                            }
                        }
                    }
                }

                Thread.sleep(500);

            } catch (IOException ex) {
                Logger.getLogger(GaduGadu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(GaduGadu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
      
    }

}
