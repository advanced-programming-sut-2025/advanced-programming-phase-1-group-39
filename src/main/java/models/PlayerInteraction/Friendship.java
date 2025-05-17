package models.PlayerInteraction;

import models.Time;

import java.time.LocalDate;
import java.util.ArrayList;

public class Friendship {

    private final String user1;
    private final String user2;
    private int xp;
    private Time lastInteraction;

    private boolean isFirstTalking;
    private boolean hasGiftedEachOther;
    private boolean isFirstHug;

    private boolean askedMarriage = false;
    private boolean isMarried = false;

    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<Gift> gifts = new ArrayList<>();


    public Friendship(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
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
        if (xp > 1000) {
            return 4;
        } else if (xp > 600) {
            return 3;
        } else if (xp >= 300) {
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

    public ArrayList<Gift> getGifts() {
        return gifts;
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

    // Auxiliary functions :

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addGift(Gift gift) {
        gifts.add(gift);
    }

    // MARRIAGE
    public void askMarriage() {
        askedMarriage = true;
    }
    public void notAskMarriage() {
        askedMarriage = false;
    }
    public boolean isAskedMarriage() {
        return askedMarriage;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void marriage() {
        isMarried = true;
    }
}
