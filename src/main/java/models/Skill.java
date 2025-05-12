package models;

public class Skill {
    int farmingXP = 0;
    int miningXP = 0;
    int foragingXP = 0;
    int fishingXP = 0;

    public int getFarmingLevel() {
        if (farmingXP >= 1200) {
            return 4;
        } else if (farmingXP >= 750) {
            return 3;
        } else if (farmingXP >= 400) {
            return 2;
        } else if (farmingXP >= 150) {
            return 1;
        } else {
            return 0;
        }
    }
    public int getMiningLevel() {
        if (miningXP >= 1200) {
            return 4;
        } else if (miningXP >= 750) {
            return 3;
        } else if (miningXP >= 400) {
            return 2;
        } else if (miningXP >= 150) {
            return 1;
        } else {
            return 0;
        }
    }
    public int getForagingLevel() {
        if (foragingXP >= 1200) {
            return 4;
        } else if (foragingXP >= 750) {
            return 3;
        } else if (foragingXP >= 400) {
            return 2;
        } else if (foragingXP >= 150) {
            return 1;
        } else {
            return 0;
        }
    }
    public int getFishingLevel() {
        if (fishingXP >= 1200) {
            return 4;
        } else if (fishingXP >= 750) {
            return 3;
        } else if (fishingXP >= 400) {
            return 2;
        } else if (fishingXP >= 150) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isFarmingLevelMax() {return getFarmingLevel() == 4;}
    public boolean isMiningLevelMax() {return getMiningLevel() == 4;}
    public boolean isForagingLevelMax() {return getForagingLevel() == 4;}
    public boolean isFishingLevelMax() {return getFishingLevel() == 4;}
}
