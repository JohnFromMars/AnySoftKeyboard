package com.anysoftkeyboard.datacollection;

/**
 *
 */
public class Word {
    private long startTime;
    private long endTime;
    private int characters;


    public Word() {
        this.startTime = 0;
        this.endTime = 0;
        this.characters = 0;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getCharacters() {
        return characters;
    }

    public void setCharacters(int characters) {
        this.characters = characters;
    }

    /**
     *
     * @return
     */
    public boolean isInitAndNotComplelte() {
        return startTime > 0 && endTime == 0;
    }

    /**
     * add 1 to character
     */
    public void addCharacter() {
        this.characters = this.characters + 1;
    }

    /**
     * minus 1 to character
     */
    public void deleteCharacter() {
        if (!isNoCharacter()) {
            this.characters = this.characters - 1;
        }
    }

    /**
     * check if there is no character in this word object
     *
     * @return
     */
    public boolean isNoCharacter() {
        return this.characters <= 0;
    }

    @Override
    public String toString() {
        return "Word{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", characters=" + characters +
                '}';
    }
}
