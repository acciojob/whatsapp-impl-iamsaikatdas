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
        if (users.size()==2)
            return createPersonalChat(users);

        customGroupCount++;
        Group group = new Group("Group: " + customGroupCount, users.size());
        groupUserMap.put(group, users);
        adminMap.put(group, users.get(0));
        return group;
    }
    public Group createPersonalChat(List<User> users) {
        String groupName = users.get(1).getName();
        Group personalGroup = new Group(groupName, 2);
        groupUserMap.put(personalGroup, users);
        return personalGroup;
    }

    public boolean createUser(String name, String mobile)throws Exception {
        if (userMobile.contains(mobile))
            return false;

        userMobile.add(mobile);
        User user = new User(name, mobile);
        userMap.put(mobile, user);
        return true;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId, content, new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist.");
        if (!userInGroup(group, sender)) throw new Exception("You are not allowed to send message");

        List<Message> msg = new ArrayList<>();
        if (groupMessageMap.containsKey(group))
            msg = groupMessageMap.get(group);

        msg.add(message);
        groupMessageMap.put(group, msg);
        return msg.size();
    }
    public boolean userInGroup(Group group, User sender){
        List<User> users = groupUserMap.get(group);
        for (User user:users){
            if (user.equals(sender)) return true;
        }
        return false;
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if (!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if (!adminMap.get(group).equals(approver)) throw new Exception("Approver does not have rights");
        if (!this.userExistsInGroup(group, user)) throw new Exception("User is not a participant");

        adminMap.put(group, user);
        return "SUCCESS";
    }
        public boolean userExistsInGroup (Group group, User sender){
            List<User> users = groupUserMap.get(group);
            for (User user : users) {
                if (user.equals(sender)) return true;
            }

            return false;
        }
}