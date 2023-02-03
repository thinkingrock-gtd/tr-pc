/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.util;

import java.awt.Frame;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.windows.WindowManager;

/**
 * Some static utility methods.
 *
 * @author Jeremy Moore
 */
public final class Utils {

    /**
     * Utility method to convert a List of Byte elements to an array of byte
     * elements.
     *
     * @param list The List of Byte elements.
     * @return a byte array containing the list Bytes as bytes or an empty byte
     * array if list is null.
     */
    public static final byte[] byteArray(List<Byte> list) {
        if (null == list) {
            return new byte[0];
        }
        byte[] arr = new byte[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * Utility method to compare two objects for equality including cases where
     * one or both of the objects are null.
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return true if both objects are null or both objects are not null and
     * are equal (using the object.equals() method).
     */
    public static final boolean equal(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public static final boolean equal(byte[] bytesA, byte[] bytesB) {
        if (bytesA == bytesB) {
            return true;
        }
        if (bytesA == null || bytesB == null) {
            return false;
        }
        if (bytesA.length != bytesB.length) {
            return false;
        }
        for (int i = 0; i < bytesA.length; i++) {
            if (bytesA[i] != bytesB[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utility method to determine whether a string is in an array of strings.
     *
     * @param string The string to look for.
     * @param array The array.
     * @return true if string is equal to an element of array.
     */
    public static final boolean in(String string, String[] array) {
        if (string == null || array == null) {
            return false;
        }
        for (String element : array) {
            if (string.equals(element)) {
                return true;
            }
        }
        return false;
    }

    private static Frame appFrame;

    /**
     * Gets the application window frame.
     *
     * @return the frame.
     */
    public static final Frame getAppFrame() {
        if (appFrame == null) {
            if (SwingUtilities.isEventDispatchThread()) {
                appFrame = WindowManager.getDefault().getMainWindow();
            } else {
                SwingUtilities.invokeLater(() -> {
                    appFrame = WindowManager.getDefault().getMainWindow();
                });
            }
        }
        return appFrame;
    }

    /**
     * Sleep ignoring InterruptedException.
     *
     * @param ms The number of milliseconds to sleep.
     */
    public static final void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

}
