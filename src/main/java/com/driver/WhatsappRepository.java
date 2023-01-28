package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<String, User> userMap;
    private HashMap<Message, User> sendMap;
    private HashMap<Integer, Message > messageMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMap = new HashMap<String , User>();
        this.sendMap = new HashMap<Message , User>();
        this.messageMap = new HashMap<Integer, Message>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public Group createGroup(List<User> users)  {
        if (users.size()>2){
            customGroupCount++;
            Group group = new Group("Group: " + groupUserMap, users.size());
            adminMap.put(group, users.get(0));
            groupUserMap.put(group, users);
            groupMessageMap.put(group, new ArrayList<Message>());
            return group;
        }
        Group group = new Group("Group: " + users.get(1), users.size());
        adminMap.put(group, users.get(0));
        groupUserMap.put(group, users);
        groupMessageMap.put(group, new ArrayList<Message>());
        return group;
    }

    public String createUser(String name, String mobile)throws Exception {
        if (userMobile.contains(mobile)){
            throw new Exception("User already exits");
        }
        userMobile.add(mobile);
        User user = new User(name, mobile);
        userMap.put(mobile, user);
        return "SUCESS";
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId, content);
        messageMap.put(messageId, message);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if (!groupUserMap.containsKey(group)) {
            throw new Exception("Group does not exits.");
        }
        boolean flag = false;
        for (User user :
                groupUserMap.get(group)) {
            if (groupUserMap.equals(user.getMobile())) {
                flag = true;
                break;
            }
        }
        if (flag == false) {
            throw new Exception("You're not allowed to send message");
        }
        sendMap.put(message, sender);
        List<Message> list = groupMessageMap.get(group);
        list.add(message);
        groupMessageMap.put(group, list);
        return list.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(adminMap.get(group)!=approver){
            throw new Exception("Approver does not have rights");
        }
        boolean flag = false;
        for(User u : groupUserMap.get(group)){
            if(user.getMobile().equals(u.getMobile())){
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new Exception("User is not a participant");
        }
        adminMap.put(group, user);
        return "SUCCESS";
    }
}
