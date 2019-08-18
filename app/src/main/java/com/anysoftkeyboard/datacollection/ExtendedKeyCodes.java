package com.anysoftkeyboard.datacollection;

import com.anysoftkeyboard.api.KeyCodes;

public class ExtendedKeyCodes extends KeyCodes {

    //Key code
    public static final int QUESTION_MARK = 63;
    public static final int COMMA = 44;
    public static final int PERIOD = 46;
    public static final int SINGLE_QUOTATION = 39;
    public static final int EXCLAMATION = 33;
    public static final int AT = 64;
    public static final int HASHTAG = 35;
    public static final int DOLLAR = 36;
    public static final int PERCENTAGE = 37;
    public static final int AND_SIGN = 38;


    public ExtendedKeyCodes() {
    }

    /**
     * This method check if the primary code is numeric
     *
     * @param primaryCode primary code
     * @return true or false
     */
    public static boolean isNumber(int primaryCode) {
        return primaryCode >= 48 && primaryCode <= 57;
    }

    /**
     * This method check if the primary code is alphabetic
     *
     * @param primaryCode primary code
     * @return true or false
     */
    public static boolean isAlphabetic(int primaryCode) {
        return (primaryCode >= 97 && primaryCode <= 122) || (primaryCode >= 65 && primaryCode <= 90);
    }

    /**
     * This method check if the primary code is punctuation, currently includes , ? . ' !
     *
     * @param primaryCode primary key code
     * @return true or false
     */
    public static boolean isPunctuation(int primaryCode) {

        int[] puncts = {COMMA, QUESTION_MARK, PERIOD, SINGLE_QUOTATION, EXCLAMATION};
        boolean result = false;

        for (int i = 0; i < puncts.length; i++) {
            result = result || (primaryCode == puncts[i]);
        }

        return result;
    }

    /**
     * This method checks f the primary code is a seperator, which includes space, enter
     *
     * @param primaryCode primary key code
     * @return true or false
     */
    public static boolean isSeperator(int primaryCode) {
        return primaryCode == KeyCodes.SPACE || primaryCode == KeyCodes.ENTER;
    }

    /**
     * This method checks if the primary code is an error, which includes delete, delete word and forward delete
     *
     * @param primaryCode primary key code
     * @return true or false
     */
    public static boolean isError(int primaryCode) {
        return primaryCode == KeyCodes.DELETE || primaryCode == KeyCodes.DELETE_WORD || primaryCode == KeyCodes.FORWARD_DELETE;
    }
}
