package models.PlayerInteraction;

import models.Enums.FriendshipLevel;
import models.Time;

import java.time.LocalDate;
import java.util.ArrayList;

public class Friendship {

    private final String user1;
    private final String user2;
    private int xp;
    private int friendshipLevel;
    private Time lastInteraction;

    private boolean isFirstTalking;
    private boolean hasGiftedEachOther;
    private boolean isFirstHug;

    private ArrayList<Message> messages = new ArrayList<>();


    public Friendship(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.friendshipLevel = 0;
        this.xp = 0;
    }


    public void dailyDecay() {
//        if (!lastInteraction.isEqual(LocalDate.now())) {
//            // TODO : complete this function
//        }
    }

    // Getters & toString...


    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public int getXp() {
        return xp;
    }

    public int getFriendshipLevel() {
        return friendshipLevel;
    }

    public Time getLastInteraction() {
        return lastInteraction;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public boolean isFirstTalking() {
        return isFirstTalking;
    }

    public boolean isHasGiftedEachOther() {
        return hasGiftedEachOther;
    }

    public boolean isFirstHug() {
        return isFirstHug;
    }


    // Auxiliary functions :

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setFriendshipLevel(int friendshipLevel) {
        this.friendshipLevel = friendshipLevel;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }



}
