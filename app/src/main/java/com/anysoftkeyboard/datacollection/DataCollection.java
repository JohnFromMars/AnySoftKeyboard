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
    private ArrayList<Keystroke> keystrokes;


    public DataCollection() {
        words = new ArrayList<>();
        keystrokes = new ArrayList<>();
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    public void addKeystroke(Keystroke keystroke) {
        this.keystrokes.add(keystroke);
    }

    public Keystroke getLastKeystroke() {
        return keystrokes.get(this.keystrokes.size() - 1);
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public ArrayList<Keystroke> getKeystrokes() {
        return keystrokes;
    }

    public void setKeystrokes(ArrayList<Keystroke> keystrokes) {
        this.keystrokes = keystrokes;
    }
}
