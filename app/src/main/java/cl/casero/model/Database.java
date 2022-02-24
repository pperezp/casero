package cl.casero.model;

import android.os.Environment;

public class Database {
    public static final int VERSION = 2;
    public static final String PATH =
            Environment.getExternalStorageDirectory().getPath() + "/caseroBD/casero.sqlite";
}
