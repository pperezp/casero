package cl.casero.model;

import cl.casero.MainActivity;

public class Resource {

    public static String getString(int resourceId) {
        return MainActivity.getInstance().getResources().getString(resourceId);
    }

    public static String[] getStringArray(int resourceId) {
        return MainActivity.getInstance().getResources().getStringArray(resourceId);
    }

    public static float getFloat(int resourceId) {
        return Float.parseFloat(getString(resourceId));
    }

    public static int getInt(int resourceId) {
        return Integer.parseInt(getString(resourceId));
    }

    public static int getColor(int resourceId) {
        return MainActivity.getInstance().getResources().getColor(resourceId);
    }
}
