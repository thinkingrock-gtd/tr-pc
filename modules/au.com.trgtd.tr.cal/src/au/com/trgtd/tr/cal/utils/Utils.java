package au.com.trgtd.tr.cal.utils;

public class Utils {
    
    /**
     * Checks if two objects are equal including where either or both are null.
     * @param o1 the first object
     * @param o2 the first object
     * @return true if the objects are equal or both null.
     */
    public static boolean equal(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (null == o1 || null == o2) {
            return false;
        }
        return o1.equals(o2);
    }

}
