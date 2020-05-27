/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientUI;

import client.*;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Admin
 */
public class chatGUI extends javax.swing.JFrame implements KeyListener {
    
    private User user;
    private String friendName;
        
    /**
     * Creates new form chatGUI
     */
    private DefaultListModel listOnl = new DefaultListModel();
    private DefaultListModel listOff = new DefaultListModel();
    StyledDocument doc;
    SimpleAttributeSet messageAlign;
    
    public chatGUI() {
        initComponents();
        initSetting();
    }
    
    public chatGUI(User user) {
        this.user = user;
        friendName = null;
        initComponents();
        initSetting();
    }        
    
    private void initSetting(){
        //Create list for online and offline friends
        offList.setModel(listOnl);
        onlineList.setModel(listOff);
        
        jTextField1.addActionListener(typeMessage);
        findUser.addActionListener(newChat);
        doc = jTextPane1.getStyledDocument();
        messageAlign= new SimpleAttributeSet();
        JScrollPane   scroll = new JScrollPane();
        jTextPane1.add(scroll); 
        
        jLabel1.setText("Welcome!");
        
        //  TODO
        // initial add online offline friends
        addName("quithu98", 1);
        
        user.setChatUI(this);
        user.start();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        logout = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        offList = new javax.swing.JList<>();
        findUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        findToChat = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        onlineList = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        reset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        logout.setText("Back");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        jLabel1.setText("F1");

        jTextField1.setText("Type here");
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        offList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(offList);

        findUser.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        findUser.setText("Enter name...");
        findUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                findUserFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                findUserFocusLost(evt);
            }
        });
        findUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findUserActionPerformed(evt);
            }
        });

        jLabel2.setText("Online");

        findToChat.setText("Chat");
        findToChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findToChatActionPerformed(evt);
            }
        });

        onlineList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        onlineList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onlineListMouseClicked(evt);
            }
        });
        onlineList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                onlineListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(onlineList);

        jLabel3.setText("Offine");

        jScrollPane4.setViewportView(jTextPane1);

        reset.setText("Reset");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(logout)
                        .addGap(187, 187, 187)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addComponent(jScrollPane4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(reset)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(findUser, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(findToChat))))))
                .addGap(0, 28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(logout)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(reset))
                        .addGap(5, 5, 5)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton2)
                        .addComponent(jButton3)
                        .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(findUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(findToChat)))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Display the messages communicated with friend
    public void displayMess(String friend) throws BadLocationException {
        doc.remove(0, doc.getLength());
        
        // get message record with friend
        MessRecord messRecord = user.getMessRecordList().get(friend);
        
        for (int i = 0; i < messRecord.getNumOfMess(); i++) {
            String message = messRecord.getMessList().get(i);
            if (message.charAt(0) == '1') {
                ownerChatMessage(message.substring(1));
            } else {
                friendChatMessage(message.substring(1));
            }
        }
    } 
    
    
    //Send message to other clients
    public void ownerChat() throws BadLocationException{
        if (!jTextField1.getText().equals("")) {
            
            // get friend's info
            Friend friend = (user.getFriendList()).get(friendName);
            
            //create a thread to send message
            SendMess sendMess = new SendMess(user, friend, false);
            sendMess.setChatUI(this);
            
            //get message
            sendMess.setMess(jTextField1.getText());
            sendMess.start();
            
            jTextField1.setText("");

        }
    }
     
    private void ownerChatMessage(String message){
        StyleConstants.setAlignment(messageAlign, StyleConstants.ALIGN_RIGHT);
        try
        {
            int length = doc.getLength();
            doc.insertString(doc.getLength(), "\n" + message, null);
            doc.setParagraphAttributes(length+1, 1, messageAlign, false);
        }
        catch(BadLocationException e) { System.out.println(e);}
    }
    
    
    private void friendChatMessage(String message){
        StyleConstants.setAlignment(messageAlign, StyleConstants.ALIGN_LEFT);
        try
        {
            int length = doc.getLength();
            doc.insertString(doc.getLength(),"\n" + message, null);
            doc.setParagraphAttributes(length+1, 1, messageAlign, false);
        }
        catch(BadLocationException e) { System.out.println(e);}
    }
    
    
    private int checkExistance(String name, DefaultListModel friendlist){
        int size = friendlist.getSize();
        for (int i =0; i < size; i++){
            if (friendlist.getElementAt(i).equals(name)) 
                return i;
        }
        return -1;
    }
    
    // ***** selectedList = 1 online friendlist, 0  offline friend list *****
    // Add a new name to list
    public void addName(String name, int selectedList){
        if (selectedList == 0){
            if (checkExistance(name, listOnl) == -1){
                listOnl.addElement(name);
            }
        }  
        
        if (selectedList == 1){
            if (checkExistance(name, listOff) == -1){
                listOff.addElement(name);
            }
        } 
    } 
    // Remove a name from list
    public void removeName(String name, int selectedList){
        if (selectedList == 0){
            int pos = checkExistance(name, listOnl);
            if ( pos != -1){
                listOnl.remove(pos);
            }
        }  
        
        if (selectedList == 1){
            int pos = checkExistance(name, listOff);
            if ( pos != -1){
                listOff.remove(pos);
            }
        }
    } 
    
    
    private void findUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findUserActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_findUserActionPerformed
    
    private boolean beginChat(){
        if (checkExistance(findUser.getText(),listOnl) != -1){
            jLabel1.setText(findUser.getText());
            //Begin connection here
            return true;
        }
        //else {
            JFrame frame = null;
            JOptionPane.showMessageDialog(frame, "Invalid friend or your friend is offline");
        //}
        return false;
    }
 
    private void findToChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findToChatActionPerformed
       
        if (beginChat() == false) {
            boolean found = user.findUser(findUser.getText());

            if (found == false) {
                JOptionPane.showMessageDialog(null, "User not found");
            } else {
                // TODO
                // add friend 
            }
        }
        
    }//GEN-LAST:event_findToChatActionPerformed

    private void onlineListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_onlineListValueChanged
        // TODO add your handling code here:
        friendName = onlineList.getSelectedValue();
        jLabel1.setText(friendName);
    }//GEN-LAST:event_onlineListValueChanged

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        //Send mesage to message field
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        // TODO add your handling code here:
        if (jTextField1.getText().equals("Type here"))
            jTextField1.setText("");
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
        if (jTextField1.getText().equals("")){
            jTextField1.setText("Type here");
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private void findUserFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_findUserFocusGained
        // TODO add your handling code here:
        if (findUser.getText().equals("Enter name...")){
            findUser.setText("");
        } 
    }//GEN-LAST:event_findUserFocusGained

    private void findUserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_findUserFocusLost
        // TODO add your handling code here:
        if (findUser.getText().equals("")){
            findUser.setText("Enter name...");
        }
    }//GEN-LAST:event_findUserFocusLost

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        // TODO
        // close the received socket
        
        dispose();
        //logout here
        loginGUI newUser = new loginGUI();
        newUser.setVisible(true);
    }//GEN-LAST:event_logoutActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            ownerChat();
        } catch (BadLocationException ex) {
            Logger.getLogger(chatGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetActionPerformed

    private void onlineListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onlineListMouseClicked
        // TODO add your handling code here:
        jLabel1.setText(onlineList.getSelectedValue());
    }//GEN-LAST:event_onlineListMouseClicked
    Action typeMessage = new AbstractAction()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try {
                ownerChat();
            } catch (BadLocationException ex) {
                Logger.getLogger(chatGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    Action newChat = new AbstractAction()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            //System.out.println("some action");
            beginChat();
        }
    };
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
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(chatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new chatGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton findToChat;
    private javax.swing.JTextField findUser;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JButton logout;
    private javax.swing.JList<String> offList;
    private javax.swing.JList<String> onlineList;
    private javax.swing.JButton reset;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
