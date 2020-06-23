/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author quyenhooppa
 */
public class GroupChat {
    
    private String groupName;
    private LinkedHashMap<String, Friend> memberList;
    private ArrayList<String> messList;

    
    public GroupChat(String groupName, String membersInfo) {
        this.groupName = groupName;
        this.messList = new ArrayList<>();
        createMemberList(membersInfo);
    }

    public String getGroupName() {
        return groupName;
    }

    public LinkedHashMap<String, Friend> getMemberList() {
        return memberList;
    }

    public ArrayList<String> getMessList() {
        return messList;
    }

    
    private void createMemberList(String membersInfo) {
        memberList = new LinkedHashMap<>();
        int numOfMembers = Character.getNumericValue(membersInfo.charAt(0));
        int curPos = 1;
        int pos = curPos;
        
        for (int i = 0; i < numOfMembers; i++) {
            while (membersInfo.charAt(curPos) != '%') {
                curPos++;
            }
            newMember(membersInfo.substring(pos, curPos));
            pos = curPos + 1; // char next %
            curPos++; // char next %
        }
    }
    
    private void newMember(String memberInfo) {
        int curPos = 0;
        int pos = curPos;
                    
        String name;
        String ip;
        int port;
        
        while (memberInfo.charAt(curPos) != '-') {
            curPos++;
        }
        name = memberInfo.substring(pos, curPos);
        pos = curPos + 1; // char next -
        curPos++; // char next -
        
        while (memberInfo.charAt(curPos) != '-') {
            curPos++;
        }
        ip = memberInfo.substring(pos, curPos);
        pos = curPos + 1; // char next -
        
        port = Integer.parseInt(memberInfo.substring(pos));
        
        memberList.put(name, new Friend(name, ip, port, 1));
    }
    
    
    public void sendGroupMess(User sender, String mess) {
        for (int i = 0; i < memberList.size(); i++) {
            Friend friend = (new ArrayList<>(memberList.values())).get(i);
            
            if (!friend.getName().equals(sender.getUserName())) {
                System.out.println(friend.getName() + friend.getIP() + friend.getSentPort());
                new SendMess(sender, friend, 
                        groupName + "%" + mess, 7).start();
            }
        }
        
        addMess(mess.substring(mess.indexOf('%') + 1), 1);
        if (sender.getChatUI().getCurGroupName().equals(groupName)) {
            sender.getChatUI().displayGroupMess(groupName);
        }
    }
    
    // add messagae to the list, type of messages is sent (1) or received (0)
    public void addMess(String mess, int type) {
        if (type == 1) { // mess sent
            String encodedMess = String.valueOf(type) + mess;
            messList.add(encodedMess);
        } else { // mess recevied: sender: \t mess
            String encodedMess = String.valueOf(type) + mess;
            messList.add(encodedMess);
        }
    }

}
