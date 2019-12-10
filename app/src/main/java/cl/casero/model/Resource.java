package cl.casero.model;

import cl.casero.MainActivity;

public class Resource {

    /**
     *
     * @param resourceId R.string.***
     * @return
     */
    public static String getString(int resourceId){
        return MainActivity.getInstance().getResources().getString(resourceId);
    }

    /**
     *
     * @param resourceId R.array.***
     * @return
     */
    public static String[] getStringArray(int resourceId){
        return MainActivity.getInstance().getResources().getStringArray(resourceId);
    }

    /**
     *
     * @param resourceId R.string.***
     * @return
     */
    public static float getFloat(int resourceId){
        return Float.parseFloat(getString(resourceId));
    }

    /**
     *
     * @param resourceId R.string.***
     * @return
     */
    public static int getInt(int resourceId){
        return Integer.parseInt(getString(resourceId));
    }

    /**
     *
     * @param resourceId R.color.***
     * @return
     */
    public static int getColor(int resourceId){
        return MainActivity.getInstance().getResources().getColor(resourceId);
    }
}
