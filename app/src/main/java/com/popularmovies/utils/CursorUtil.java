package com.popularmovies.utils;

import android.database.Cursor;

public class CursorUtil {

    public static boolean isValidCursor(Cursor cursor) {
        try {
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        }
    }

    public static void closeCursor(Cursor cursor) {
        if (isValidCursorForClose(cursor)) {
            cursor.close();
        }
    }

    private static boolean isValidCursorForClose(Cursor cursor) {
        try {
            return !cursor.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
