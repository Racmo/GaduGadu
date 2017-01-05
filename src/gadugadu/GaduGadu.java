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
    public static BufferedReader inMessage;

    public static Socket statusSocket;
    public static PrintWriter outStatus;
    public static BufferedReader inStatus;

    public static String serverIP;

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

        GaduGadu.serverIP = "192.168.5.165";
        Boolean mainWindow = false;

        me = new MeUser();

        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);

        try {
            socket = new Socket(GaduGadu.serverIP, 5001);
            // socket.setSoTimeout(1000);
            outMessage = new PrintWriter(socket.getOutputStream(), true);
            inMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            statusSocket = new Socket(GaduGadu.serverIP, 5001);
            outStatus = new PrintWriter(statusSocket.getOutputStream(), true);
            inStatus = new BufferedReader(new InputStreamReader(statusSocket.getInputStream()));

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
        String serverMsg = "";
        String statusMsg = "";

        System.out.println("Nowy wątek GADU");

        while (true) {
            try {
                //Nowy wątek

                String threadName = Thread.currentThread().getName();

//                wątek odbierający wiadomość o statusach znajomych
                if (threadName.equals("statusThread")) {
                    getStatusesFromServer();                
                   
                    statusMsg = GaduGadu.inStatus.readLine();

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
                    serverMsg = GaduGadu.inMessage.readLine();
                     
                    if (serverMsg.contains("#MESSAGE")) {
                        //wiadomosc od jakiegoś użytkownika
                        System.out.println("WIADOMOSC OD UZYTKOWNIKA: "+serverMsg);

                        int id = 0;
                        boolean isFriend = false;
                        User friend = null;

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
                        //znajdz znajomego ktory wyslal wiadomosc
                        for (User u : GaduGadu.me.getFriends()) {
                            if (u.getUserId() == id) {
                                if (!u.getConversation()) {
                                    u.startConversation();
                                    break;
                                }
                                isFriend = true; //nadawca to znajomy
                                friend = u;
                            }
                        }
                        //jesli jest znajomym
                        User tmpUnknown = null;
                        
                        if (isFriend) {
                            friend.getWindow().addMessage(friend.getUserName()+" >>> "+serverMsg.replaceFirst(Integer.toString(id), ""));
                        }
                        //jesli jest nieznajomym uzytkownikiem
                        else{
                            boolean exists=false;
                            for(User u: GaduGadu.me.getUnknownUsers()){
                                if(u.getUserId()==id){
                                    exists = true;
                                    tmpUnknown = u;
                                    break;
                                }
                            }
                            //jesli nie ma takiego unknown usera
                            if(!exists){
                                User unknown = new User();
                                unknown.setUserId(id);
                                unknown.startConversation();
                                unknown.getWindow().addMessage(serverMsg.replaceFirst(Integer.toString(id), ""));
                                unknown.getWindow().setConversationName("Unknown: " + Integer.toString(id));
                                GaduGadu.me.addUnknownUser(unknown);
                            }
                            //jesli jest
                            else{
                                if(!tmpUnknown.getConversation()){
                                   tmpUnknown.getWindow().setVisible(true);
                                }
                                tmpUnknown.getWindow().addMessage(serverMsg.replaceFirst(Integer.toString(id), ""));
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