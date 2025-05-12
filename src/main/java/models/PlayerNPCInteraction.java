package models;

public class PlayerNPCInteraction {
    private String NPCName;
    private int friendshipLevel;
    private int friendshipScore;
    private boolean isFirstTalking;

    public PlayerNPCInteraction(String NPCName) {
        this.NPCName = NPCName;
        this.friendshipLevel = 0;
        this.friendshipScore = 0;
        this.isFirstTalking = true;
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

    public boolean isFirstTalking() {
        return isFirstTalking;
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
}
