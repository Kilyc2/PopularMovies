package com.popularmovies.utils;


public class Numeric {

    public static boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException nfE) {
            return false;
        }
        return true;
    }
}
