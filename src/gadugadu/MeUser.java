/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gadugadu;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;

/**
 *
 * @author Monika
 */
public class MeUser extends User{
    private ArrayList<User> friends;
    private ArrayList<User> unknownUsers;
    private  ArrayList<User> onlineFriends;
    private  ArrayList<User> offlineFriends;
    
    private MainForm mainFrame;

    public MeUser(){
        friends = new ArrayList();
        onlineFriends = new ArrayList();
        offlineFriends = new ArrayList();
        unknownUsers = new ArrayList();
        
        //mainFrame = new MainForm();
    }
    /**
     * @return the friends
     */
    public ArrayList<User> getFriends() {
        return friends;
    }

    /**
     * @param friends the friends to set
     */
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
        
    }
    
    public void addFriend(User friend){
        this.friends.add(friend);
    }
    
    public void deleteFriend(User friend){
        this.friends.remove(friend);
    }

    /**
     * @return the mainFrame
     */
    public MainForm getMainFrame() {
        return mainFrame;
    }

    /**
     * @param mainFrame the mainFrame to set
     */
    public void setMainFrame(MainForm mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * @return the onlineFriends
     */
    public ArrayList<User> getOnlineFriends() {
        return onlineFriends;
    }

    /**
     * @param aOnlineFriends the onlineFriends to set
     */
    public void setOnlineFriends(ArrayList<User> aOnlineFriends) {
        onlineFriends = aOnlineFriends;
        
        DefaultListModel newModel = new DefaultListModel();
        for(User u: this.onlineFriends){
            newModel.addElement(u.getUserName());
        }
        this.mainFrame.updateOnlineList(newModel);
    }

    /**
     * @return the offlineFriends
     */
    public ArrayList<User> getOfflineFriends() {
        return offlineFriends;
        
    }

    /**
     * @param aOfflineFriends the offlineFriends to set
     */
    public void setOfflineFriends(ArrayList<User> aOfflineFriends) {
        offlineFriends = aOfflineFriends;
        
        DefaultListModel newModel = new DefaultListModel();
        for(User u: this.offlineFriends){
            newModel.addElement(u.getUserName());
        }
        this.mainFrame.updateOfflineList(newModel);
    }

    /**
     * @return the unknownUsers
     */
    public ArrayList<User> getUnknownUsers() {
        return unknownUsers;
    }

    /**
     * @param unknownUsers the unknownUsers to set
     */
    public void setUnknownUsers(ArrayList<User> unknownUsers) {
        this.unknownUsers = unknownUsers;
    }
    
     public void addUnknownUser(User unknown){
        this.friends.add(unknown);
    }
    
    public void deleteUnknownUser(User unknown){
        this.friends.remove(unknown);
    }
}
