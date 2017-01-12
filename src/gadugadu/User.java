/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gadugadu;

import java.io.Serializable;

/**
 *
 * @author Monika
 */
public class User implements Serializable{
    private int userId;
    private String userName;
    private transient Boolean online;
    private transient Boolean conversation; //informuje czy okno rozmowy z danym uzytkownikiem jest otwarte
    private transient ConversationForm window; //okno rozmowy z danym uzytkownikiem

    public User() {
        this.userId = 0;
        this.userName = "";
        this.online = false;
        this.conversation = false;
    }
    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the online
     */
    public Boolean getOnline() {
        return online;
    }

    /**
     * @param online the online to set
     */
    public void setOnline(Boolean online) {
        this.online = online;
    }

    /**
     * @return the conversation
     */
    public Boolean getConversation() {
        return conversation;
    }

    /**
     * @param conversation the conversation to set
     */
    public void setConversation(Boolean conversation) {
        this.conversation = conversation;
    }

    /**
     * @return the window
     */
    public ConversationForm getWindow() {
        return window;
    }

    /**
     * @param window the window to set
     */
    public void setWindow(ConversationForm window) {
        this.window = window;
    }
    
    public void startConversation(){
        ConversationForm conversationWindow = new ConversationForm();
        if(!this.userName.isEmpty()){
            conversationWindow.setConversationName(this.userName);
        }
        else{
            conversationWindow.setConversationName("Unknown user");
        }
        conversationWindow.setVisible(true);
        
        this.setConversation(true);
        this.setWindow(conversationWindow);
    }
}
