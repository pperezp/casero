package cl.casero.model;

import android.os.Environment;

import java.io.File;

public class Database {

    public static final int VERSION;
    public static final String PATH;

    private Database() {
        throw new IllegalStateException("Utility class");
    }

    static {
        PATH = getPath() + "/caseroBD/casero.sqlite";
        VERSION = 2;
    }

    private static String getPath() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        return externalStorageDirectory.getPath();
    }
}
