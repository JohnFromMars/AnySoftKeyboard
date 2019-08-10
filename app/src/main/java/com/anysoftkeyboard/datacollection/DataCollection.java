package com.anysoftkeyboard.datacollection;


import java.util.ArrayList;

/**
 *
 */
public class DataCollection {
    private String deviceId;
    private String startPoint;
    private String endPoint;
    private ArrayList<Word> words;


    public DataCollection() {
        words = new ArrayList<>();
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
}
