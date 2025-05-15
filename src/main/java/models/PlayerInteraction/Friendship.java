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

    private boolean isMarried;

    private ArrayList<Message> messages = new ArrayList<>();


    public Friendship(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.friendshipLevel = 0;
        this.xp = 0;
        this.isFirstTalking = false;
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
        if (xp >= 450) {
            return 4;
        } else if (xp >= 300) {
            return 3;
        } else if (xp >= 200) {
            return 2;
        } else if (xp >= 100) {
            return 1;
        } else {
            return 0;
        }
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

    public boolean isMarried() {
        return isMarried;
    }

    public void setFirstTalking(boolean firstTalking) {
        isFirstTalking = firstTalking;
    }

    public void setFirstHug(boolean firstHug) {
        isFirstHug = firstHug;
    }

    public void setHasGiftedEachOther(boolean hasGiftedEachOther) {
        this.hasGiftedEachOther = hasGiftedEachOther;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setFriendshipLevel(int friendshipLevel) {
        this.friendshipLevel = friendshipLevel;
    }

    // Auxiliary functions :

    public void addMessage(Message message) {
        messages.add(message);
    }




}
