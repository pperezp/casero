package cl.casero.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import cl.casero.MainActivity;
import cl.casero.R;
import cl.casero.model.SQLiteOpenHelperImpl;
import cl.casero.model.Resource;

public abstract class AbstractDao<T> implements Dao<T> {

    protected Context context;
    protected SQLiteOpenHelperImpl sqLiteOpenHelper;
    protected SQLiteDatabase sqLiteDatabase;
    protected Cursor cursor;
    protected String query;
    protected SimpleDateFormat dateFormat;

    public AbstractDao() {
        this.context = MainActivity.getInstance().getApplicationContext();
        dateFormat = new SimpleDateFormat(Resource.getString(R.string.database_date_pattern));
    }
}
