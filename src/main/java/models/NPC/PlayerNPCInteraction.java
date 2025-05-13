package models.NPC;

import models.Time;

public class PlayerNPCInteraction {
    private String NPCName;
    private int friendshipLevel;
    private int friendshipScore;
    private boolean isFirstTalking;
    private boolean isFirstGift;
    private Time activeMission3 = null;
    protected int daysPassed;

    public PlayerNPCInteraction(String NPCName, int daysPassed) {
        this.NPCName = NPCName;
        this.friendshipLevel = 0;
        this.friendshipScore = 0;
        this.isFirstTalking = true;
        this.isFirstGift = true;
        this.daysPassed = daysPassed;
    }

    public String getNPCName() {
        return NPCName;
    }

    public int getFriendshipLevel() {
        return friendshipLevel;
    }

    public int getFriendshipScore() {
        return friendshipScore;
    }

    public Time getActiveMission3() {
        return activeMission3;
    }

    public int getDaysPassed() {
        return daysPassed;
    }

    public boolean isFirstTalking() {
        return isFirstTalking;
    }

    public boolean isFirstGift() {
        return isFirstGift;
    }

    public void setNPCName(String NPCName) {
        this.NPCName = NPCName;
    }

    public void setFriendshipLevel(int friendshipLevel) {
        this.friendshipLevel = friendshipLevel;
    }

    public void setFriendshipScore(int friendshipScore) {
        this.friendshipScore = friendshipScore;
    }

    public void setFirstTalking(boolean firstTalking) {
        isFirstTalking = firstTalking;
    }

    public void setFirstGift(boolean firstGift) {
        isFirstGift = firstGift;
    }

    public void setActiveMission3(Time activeMission3) {
        this.activeMission3 = activeMission3;
    }
}
