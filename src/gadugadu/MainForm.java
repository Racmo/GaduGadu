/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gadugadu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;

/**
 *
 * @author Monika
 */
public class MainForm extends javax.swing.JFrame {

    private DefaultListModel onlineListModel;
    private DefaultListModel offlineListModel;
    private ObjectOutputStream outputFile;
    private ObjectInputStream inputFile;
      
    //wyszukuje użytkownika i włącza okno konwersacji z danym użytkownikiem
    public void startConversation(String name){
        for(User u: GaduGadu.me.getFriends()){
            if(u.getUserName().equals(name)){
                if(u.getConversation()==false){
                    u.setConversation(true);

                    ConversationForm conversationForm = new ConversationForm();
                    conversationForm.setVisible(true);
                    conversationForm.setConversationName(u.getUserName());
                    u.setWindow(conversationForm);
                }
            }
        }
    }
    
    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        jLabel1.setText(GaduGadu.me.getUserName());
        
        onlineListModel = new DefaultListModel();
        offlineListModel = new DefaultListModel();
        
        for(User u: GaduGadu.me.getOnlineFriends()){
            onlineListModel.addElement(u.getUserName());
        }
        for(User u: GaduGadu.me.getOfflineFriends()){
            offlineListModel.addElement(u.getUserName());
        }
        
        onlineList.setModel(onlineListModel);
        offlineList.setModel(offlineListModel);
        
        onlineList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
               if(!e.getValueIsAdjusting()){
                   startConversation(onlineList.getSelectedValue());
               }
            }
        });
        
        offlineList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                startConversation(offlineList.getSelectedValue());
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GaduGadu.outMessage.println("#LOGOUT");
                try {
                    //zapis do pliku listy znajomych
                    outputFile = new ObjectOutputStream(new FileOutputStream("save/friendList.ser"));
                    
                    outputFile.writeObject(GaduGadu.me.getFriends());
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Błąd. Lista znajomych nie została zapisana.");
                } catch (IOException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                               
            }
        });
        
        try {
            //odczyt z pliku listy zapisanych znajomych użytkowników
             System.out.println("Ścieżka: "+System.getProperty("user.dir"));
            inputFile = new ObjectInputStream(new FileInputStream("save/friendList.ser"));
           
            ArrayList<User> newUserList = (ArrayList<User>) inputFile.readObject();
            //uzupelnienie pustych pól:
            for(User u: newUserList){
                u.setConversation(false);
            }
            GaduGadu.me.setFriends(newUserList);
            
               
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Nie znaleziono pliku z listą znajomych.");
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void updateOnlineList(DefaultListModel model){
        onlineList.setModel(model);
    }
    
    public void updateOfflineList(DefaultListModel model){
        offlineList.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        serverIpText = new javax.swing.JTextField();
        addFriendButton = new javax.swing.JButton();
        logOutButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        onlineList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        offlineList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        serverIpText.setText("Server IP");
        serverIpText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverIpTextActionPerformed(evt);
            }
        });

        addFriendButton.setText("Add friend");
        addFriendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFriendButtonActionPerformed(evt);
            }
        });

        logOutButton.setText("Log out");

        jLabel1.setFont(new java.awt.Font("Euphemia", 1, 18)); // NOI18N
        jLabel1.setText("ID: UserName");

        onlineList.setBackground(new java.awt.Color(153, 255, 153));
        onlineList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(onlineList);

        offlineList.setBackground(new java.awt.Color(255, 153, 153));
        offlineList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(offlineList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(serverIpText)
            .addGroup(layout.createSequentialGroup()
                .addComponent(addFriendButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(serverIpText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addFriendButton)
                    .addComponent(logOutButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void serverIpTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverIpTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serverIpTextActionPerformed

    private void addFriendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFriendButtonActionPerformed
        // Dodanie znajomego
        AddFriendForm addFriendForm = new AddFriendForm();
        addFriendForm.setVisible(true);     
    }//GEN-LAST:event_addFriendButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFriendButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton logOutButton;
    private javax.swing.JList<String> offlineList;
    private javax.swing.JList<String> onlineList;
    private javax.swing.JTextField serverIpText;
    // End of variables declaration//GEN-END:variables
}
